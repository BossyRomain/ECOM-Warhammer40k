package com.warhammer.ecom.concurrency;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import com.warhammer.ecom.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestProductConcurrency {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    /**
     * Plusieurs clients essaient d'acheter 1 produit mais le nombre de clients dépasse le stock du produit.
     */
    @Test
    public void testStockAllBuy() throws InterruptedException {
        Random random = new Random();
        Long productId = random.nextLong(1, 10);
        final Product product = productService.get(productId);
        final int stock = product.getStock();

        final int NB_CLIENTS = stock * 2;
        long firstClientId = 0;

        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < NB_CLIENTS; i++) {
            ClientSignUpDTO dto = new ClientSignUpDTO();
            dto.setFirstName("Client");
            dto.setLastName("Test");
            dto.setPassword("1234");
            dto.setEmail("client.test" + (i + 2000) + "@gmail.com");
            dto.setBirthday(LocalDate.now());
            dto.setNewsLetter(false);

            clients.add(clientService.create(dto));

            if (i == 0) {
                firstClientId = clients.get(0).getId();
            }
        }

        // Création d'un pool de threads
        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        // CountDownLatch pour que les threads commencent tous en même temps
        CountDownLatch latch = new CountDownLatch(1);

        List<Callable<Boolean>> callables = new ArrayList<>();
        for (long i = 0; i < NB_CLIENTS; i++) {
            final long id = i + firstClientId;
            Callable<Boolean> callable = () -> {
                try {
                    Client client = clientService.getById(id);
                    cartService.addProduct(client, product, 1);
                    cartService.pay(client);
                    return true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            };
            callables.add(callable);
        }

        // Lancement de tout les threads
        List<Future<Boolean>> results = executorService.invokeAll(callables);

        // Attente de la fin des threads
        executorService.shutdown();
        boolean ok = executorService.awaitTermination(30, TimeUnit.SECONDS);

        // Vérifie que le stock du produit est 0
        Product p = productService.get(productId);
        assertEquals(0, p.getStock());

        int success = 0;
        for (Future<Boolean> f : results) {
            try {
                success += f.get() ? 1 : 0;
            } catch (ExecutionException e) {
            }
        }

        assertEquals(stock, success);
        assertEquals(stock, NB_CLIENTS - success);

        for (Client client : clients) {
            clientService.delete(client);
        }
        clients.clear();
    }

    /**
     * Plusieurs clients essaient d'acheter l'intégralité du stock d'un produit mais un seul y arrive.
     */
    @Test
    public void testOneBuyAll() throws InterruptedException {
        final int NB_CLIENTS = 100;
        long firstClientId = 0;

        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < NB_CLIENTS; i++) {
            ClientSignUpDTO dto = new ClientSignUpDTO();
            dto.setFirstName("Client");
            dto.setLastName("Test");
            dto.setPassword("1234");
            dto.setEmail("client.test" + i + "@gmail.com");
            dto.setBirthday(LocalDate.now());
            dto.setNewsLetter(false);

            clients.add(clientService.create(dto));

            if (i == 0) {
                firstClientId = clients.get(0).getId();
            }
        }

        Random random = new Random();
        Long productId = random.nextLong(1, 10);
        final Product product = productService.get(productId);
        final int stock = product.getStock();

        // Création d'un pool de threads
        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        // CountDownLatch pour que les threads commencent tous en même temps
        CountDownLatch latch = new CountDownLatch(1);

        List<Callable<Boolean>> callables = new ArrayList<>();
        for (long i = 0; i < NB_CLIENTS; i++) {
            final long id = i + firstClientId;
            Callable<Boolean> callable = () -> {
                try {
                    Client client = clientService.getById(id);
                    cartService.addProduct(client, product, stock);
                    cartService.pay(client);
                    return true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            };
            callables.add(callable);
        }

        // Lancement de tout les threads
        List<Future<Boolean>> results = executorService.invokeAll(callables);

        // Attente de la fin des threads
        executorService.shutdown();
        boolean ok = executorService.awaitTermination(30, TimeUnit.SECONDS);

        // Vérifie que le stock du produit est 0
        Product p = productService.get(productId);
        assertEquals(0, p.getStock());

        int success = 0;
        for (Future<Boolean> f : results) {
            try {
                success += f.get() ? 1 : 0;
            } catch (ExecutionException e) {
            }
        }

        assertEquals(1, success);

        for (Client client : clients) {
            clientService.delete(client);
        }
        clients.clear();
    }

    /**
     * Plusieurs clients essaient d'acheter plusieurs produits en même temps,
     * chaque client essait d'acheter la moitié du stock de chaque produit dans son panier.
     */
    @Test
    public void testMultipleClientsMultipleProducts() throws InterruptedException {

    }
}
