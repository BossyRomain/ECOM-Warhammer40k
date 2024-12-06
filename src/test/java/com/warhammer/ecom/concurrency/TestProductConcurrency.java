package com.warhammer.ecom.concurrency;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.ProductRepository;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestProductConcurrency {

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Clients try to buy all the stock of one product but another client take one item from the stock before they pay.
     */
    @Test
    public void oneTryToBuyAllInvalid() throws Exception {
        final int NB_CLIENTS = 100;
        List<Client> clients = createClients(NB_CLIENTS);

        Product product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        int oldStock = product.getStock();

        cartService.addProduct(clients.get(0).getId(), product.getId(), 1);
        for (int i = 1; i < NB_CLIENTS; i++) {
            cartService.addProduct(clients.get(i).getId(), product.getId(), product.getStock());
        }

        Client c = clientRepository.findById(clients.get(0).getId()).orElseThrow(NoSuchElementException::new);
        cartService.pay(c.getCurrentCart());
        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 1; i < NB_CLIENTS; i++) {
            c = clientRepository.findById(clients.get(i).getId()).orElseThrow(NoSuchElementException::new);
            PayTask task = new PayTask(cartService, c.getCurrentCart());
            futures.add(executorService.submit(task));
        }

        // Check that all the clients who would take all the old stock have failed
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                executorService.shutdownNow();
                fail("A client succeed to pay more than the available stock left for the product " + product.getName());
            }
        }

        executorService.shutdown();

        // Check that the product's stock has decreased of one unit only
        product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        assertEquals(oldStock - 1, product.getStock());
    }

    /**
     * Many clients buy the same set of products and each product has enough stock
     * so that all the clients can succeed their purchase.
     */
    @Test
    public void multipleProductsValid() throws Exception {
        final int NB_CLIENTS = 100;
        final int MIN_STOCK = 600;
        List<Client> clients = createClients(NB_CLIENTS);
        List<Product> products = getProducts(10, MIN_STOCK);

        HashMap<Long, Integer> oldStocks = new HashMap<>();
        for (Product product : products) {
            oldStocks.put(product.getId(), product.getStock());
        }

        for (Client client : clients) {
            for (Product product : products) {
                cartService.addProduct(client.getId(), product.getId(), 1);
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (Client client : clients) {
            Client c = clientRepository.findById(client.getId()).orElseThrow(NoSuchElementException::new);
            PayTask task = new PayTask(cartService, c.getCurrentCart());
            futures.add(executorService.submit(task));
        }

        for (Future<Boolean> future : futures) {
            if (!future.get()) {
                executorService.shutdownNow();
                fail("A client failed to purchase the set of products");
            }
        }

        executorService.shutdown();


        for (Long productId : oldStocks.keySet()) {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

            int expectedStock = oldStocks.get(productId) - NB_CLIENTS;
            assertEquals(expectedStock, product.getStock(), "The product " + product.getName() + " has not the expected stock");
        }
    }

    /**
     * Many clients buy the same set of products but one of the product is out of stock.
     */
    @Test
    public void oneOutOfStock() throws Exception {
        final int NB_CLIENTS = 100;
        final int MIN_STOCK = 200;
        List<Client> clients = createClients(NB_CLIENTS);
        List<Product> products = getProducts(10, MIN_STOCK);

        HashMap<Long, Integer> oldStocks = new HashMap<>();
        for (Product product : products) {
            oldStocks.put(product.getId(), product.getStock());
        }

        Random random = new Random();
        for (Client client : clients) {
            for (Product product : products) {
                int quantity = random.nextInt(1, MIN_STOCK / NB_CLIENTS + 1);
                cartService.addProduct(client.getId(), product.getId(), quantity);
            }
        }

        // On rend un produit indisponible
        Product p = products.get(random.nextInt(products.size()));
        p.setStock(0);
        productRepository.save(p);

        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (Client client : clients) {
            Client c = clientRepository.findById(client.getId()).orElseThrow(NoSuchElementException::new);
            PayTask task = new PayTask(cartService, c.getCurrentCart());
            futures.add(executorService.submit(task));
        }

        // Check that no client has succeeded his purchase
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                executorService.shutdownNow();
                fail("One client has succeeded his purchase");
            }
        }

        executorService.shutdown();

        // Check the products' stocks have not been updated
        for (Long productId : oldStocks.keySet()) {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

            if (p.getId().equals(productId)) {
                assertEquals(0, product.getStock(), "The product " + product.getName() + " supposed to be out of stock is not out of stock");
            } else {
                assertEquals(oldStocks.get(productId), product.getStock(), "The product " + product.getName() + " has not the expected stock");
            }
        }
    }

    /**
     * Many clients buy the same set of products but one of the product has not enough stock.
     */
    @Test
    public void oneNotEnoughStock() throws Exception {
        final int NB_CLIENTS = 100;
        final int STOCK = 1000 * NB_CLIENTS;
        List<Client> clients = createClients(NB_CLIENTS);
        List<Product> products = new ArrayList<>();

        final int NB_PRODUCTS = 10;
        for (int i = 0; i < NB_PRODUCTS; i++) {
            Product p = productRepository.findById((long) i + 1).orElseThrow(NoSuchElementException::new);
            p.setStock(STOCK);
            productRepository.save(p);
            products.add(p);
        }

        Random random = new Random();
        Long productNotEnough = random.nextLong(1L, NB_PRODUCTS);

        for (Client client : clients) {
            for (Product product : products) {
                int quantity;
                if (product.getId().equals(productNotEnough)) {
                    quantity = 2000;
                } else {
                    quantity = 1000;
                }
                cartService.addProduct(client.getId(), product.getId(), quantity);
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        List<Future<Boolean>> futures = new ArrayList<>();
        for (Client client : clients) {
            Client c = clientRepository.findById(client.getId()).orElseThrow(NoSuchElementException::new);
            PayTask task = new PayTask(cartService, c.getCurrentCart());
            futures.add(executorService.submit(task));
        }

        int nbSuccess = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                nbSuccess++;
            }
        }

        executorService.shutdown();

        assertEquals(NB_CLIENTS / 2, nbSuccess, "Didn't get the expected number of clients to succeeded their purchase");
    }

    private List<Client> createClients(final int nbClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < nbClients; i++) {
            ClientSignUpDTO dto = new ClientSignUpDTO();
            dto.setFirstName("Client");
            dto.setLastName("Test");
            dto.setPassword("1234");
            dto.setEmail("client.test" + i + "@gmail.com");
            dto.setBirthday(LocalDate.now());
            dto.setNewsLetter(false);

            clients.add(clientService.create(dto, false));
        }
        return clients;
    }

    private List<Product> getProducts(final int nb, final int minStock) throws Exception {
        List<Product> products = new ArrayList<>();

        long id = 1L;
        while (products.size() < nb) {
            Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
            if (product.getStock() > minStock) {
                products.add(product);
            }
            id++;
        }

        return products;
    }

    private static class PayTask implements Callable<Boolean> {

        private final CartService service;

        private final Cart cart;

        public PayTask(CartService service, Cart cart) {
            this.service = service;
            this.cart = cart;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                return service.pay(cart);
            } catch (RuntimeException e) {
                System.out.println("=====================ERROR=============================\n" + e.getMessage());
                return false;
            }
        }
    }
}
