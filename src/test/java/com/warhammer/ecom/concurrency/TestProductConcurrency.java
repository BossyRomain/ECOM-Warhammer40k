package com.warhammer.ecom.concurrency;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ProductRepository;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.*;

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

    /**
     * One client try to buy all the stock of one product but another client take one item from the stock before.
     */
    @Test
    public void oneTryToBuyAllInvalid() throws Exception {
        List<Client> clients = createClients(2);

        Product product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        int oldStock = product.getStock();

        cartService.addProduct(clients.get(0).getId(), product.getId(), 1);
        cartService.addProduct(clients.get(1).getId(), product.getId(), product.getStock());

        cartService.pay(clients.get(0).getId());
        try {
            cartService.pay(clients.get(1).getId());
            fail();
        } catch (RuntimeException e) {
        }

        product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        assertEquals(oldStock - 1, product.getStock());
    }

    /**
     * Plusieurs clients achètent le même ensemble de produits et chaque produit à un stock suffisant pour
     * que tout les clients puissent réussir leurs achats.
     */
    @Test
    public void multipleProductsValid() throws Exception {
        final int NB_CLIENTS = 20;
        final int MIN_STOCK = 200;
        List<Client> clients = createClients(NB_CLIENTS);
        List<Product> products = getProducts(10, MIN_STOCK);

        HashMap<Long, Integer> oldStocks = new HashMap<>();
        for (Product product : products) {
            oldStocks.put(product.getId(), product.getStock());
        }

        HashMap<Long, Integer> askedQuantities = new HashMap<>();
        Random random = new Random();
        for (Client client : clients) {
            for (Product product : products) {
                int quantity = random.nextInt(1, MIN_STOCK / NB_CLIENTS + 1);
                cartService.addProduct(client.getId(), product.getId(), quantity);

                if (askedQuantities.containsKey(product.getId())) {
                    askedQuantities.put(product.getId(), askedQuantities.get(product.getId()) + quantity);
                } else {
                    askedQuantities.put(product.getId(), quantity);
                }
            }
        }

        for (Client client : clients) {
            cartService.pay(client.getId());
        }

        for (Long productId : oldStocks.keySet()) {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

            int expectedStock = oldStocks.get(productId) - askedQuantities.get(productId);
            assertEquals(expectedStock, product.getStock());
        }
    }

    /**
     * Plusieurs clients achètent le même ensemble de produits mais un des produits est indisponible.
     */
    @Test
    public void oneOutOfStock() throws Exception {
        final int NB_CLIENTS = 20;
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

        Product p = products.get(random.nextInt(products.size()));
        p.setStock(0);
        productRepository.save(p);

        for (Client client : clients) {
            try {
                cartService.pay(client.getId());
                fail();
            } catch (RuntimeException e) {
            }
        }

        for (Long productId : oldStocks.keySet()) {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

            if (p.getId().equals(productId)) {
                assertEquals(0, product.getStock());
            } else {
                assertEquals(oldStocks.get(productId), product.getStock());
            }
        }
    }

    /**
     * Plusieurs clients achètent le même ensemble de produits mais un des produits à un stock insuffisant.
     */
    @Test
    public void oneNotEnoughStock() throws Exception {
        Random random = new Random();
        final int NB_CLIENTS = random.nextInt(20, 100);
        final int STOCK = 1000 * NB_CLIENTS;
        List<Client> clients = createClients(NB_CLIENTS);
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product p = productRepository.findById((long) i + 1).orElseThrow(NoSuchElementException::new);
            p.setStock(STOCK);
            productRepository.save(p);
            products.add(p);
        }

        int index = random.nextInt(NB_CLIENTS / 2, clients.size());
        Client firstToFail = clients.get(index);

        int nbExpectedToSucceed = 0;
        for (Client client : clients) {
            for (Product product : products) {
                int quantity;
                if (client.getId() < firstToFail.getId()) {
                    quantity = product.getStock() / index;
                } else {
                    quantity = product.getStock() / NB_CLIENTS;
                }

                cartService.addProduct(client.getId(), product.getId(), quantity);
            }

            if (client.getId() < firstToFail.getId()) {
                nbExpectedToSucceed++;
            }
        }

        int nbSuccess = 0;
        for (Client client : clients) {
            try {
                nbSuccess += cartService.pay(client.getId()) ? 1 : 0;
            } catch (RuntimeException e) {
            }
        }

        assertEquals(nbExpectedToSucceed, nbSuccess);
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

    private List<Product> getProducts(int nb, int minStock) throws Exception {
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
}
