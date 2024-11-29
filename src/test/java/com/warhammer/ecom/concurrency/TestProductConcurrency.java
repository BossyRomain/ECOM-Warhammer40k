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
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
     * Plusieurs clients essaient d'acheter l'intégralité du stock d'un produit mais un seul y arrive.
     */
    @Test
    public void testOneBuyAll() throws InterruptedException {
        final int NB_CLIENTS = 50;

        List<Client> clients = createClients(NB_CLIENTS);

        Random random = new Random();
        final Long productId = random.nextLong(1, productRepository.count());
        final Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        final int stock = product.getStock();

        for (Client client : clients) {
            cartService.addProduct(client.getId(), productId, stock);
        }

        // Création d'un pool de threads
        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        // CountDownLatch pour que les threads commencent tous en même temps

        List<Callable<Boolean>> callables = new ArrayList<>();
        for (Client client : clients) {
            final Long id = client.getId();
            Callable<Boolean> callable = () -> {
                try {
                    Thread.sleep(random.nextInt(10));
                    boolean ok = cartService.pay(id);
                    if (ok) {
                        System.out.println("Pay works");
                    } else {
                        System.out.println("Failed");
                    }
                    return ok;
                } catch (RuntimeException e) {
                    System.out.println("Out of stock");
                    return false;
                } catch (Exception e) {
                    System.out.println("Failed to pay");
                    return false;
                }
            };
            callables.add(callable);
        }

        // Lancement de tout les threads
        List<Future<Boolean>> results = executorService.invokeAll(callables);

        // Attente de la fin des threads
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        // Vérifie que le stock du produit est 0
        Product p = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        assertEquals(0, p.getStock());

        int success = 0;
        for (Future<Boolean> f : results) {
            try {
                success += f.get() ? 1 : 0;
            } catch (ExecutionException e) {
            }
        }

        assertEquals(1, success);
    }

    /**
     * Cinq clients essai d'acheter cinq mêmes produits mais chaque client essaie d'acheter l'intégralité du stock d'un produit.
     */
    @Test
    public void scenario1() throws Exception {
        final int NB_CLIENTS = 5;
        List<Client> clients = createClients(NB_CLIENTS);

        Random random = new Random();
        Set<Long> ids = new HashSet<>();
        while (ids.size() < NB_CLIENTS) {
            ids.add(random.nextLong(1, 30));
        }
        List<Long> productsIds = ids.stream().toList();


        for (int i = 0; i < NB_CLIENTS; i++) {
            for (int j = 0; j < productsIds.size(); j++) {
                Product product = productRepository.findById(productsIds.get(j)).orElseThrow(NoSuchElementException::new);
                cartService.addProduct(clients.get(i).getId(), productsIds.get(j), i == j ? product.getStock() : random.nextInt(1, product.getStock() / 2));
            }
        }

        // Création d'un pool de threads
        ExecutorService executorService = Executors.newFixedThreadPool(NB_CLIENTS);

        List<Callable<Boolean>> callables = new ArrayList<>();
        for (Client client : clients) {
            final Long id = client.getId();
            Callable<Boolean> callable = () -> {
                try {
                    Thread.sleep(random.nextInt(10));
                    return cartService.pay(id);
                } catch (Exception e) {
                    return false;
                }
            };
            callables.add(callable);
        }

        // Lancement de tout les threads
        List<Future<Boolean>> results = executorService.invokeAll(callables);

        // Attente de la fin des threads
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        int success = 0;
        for (Future<Boolean> f : results) {
            try {
                success += f.get() ? 1 : 0;
                System.out.println(f.get());
            } catch (ExecutionException e) {
                System.out.println("fail this one");
            }
        }

        assertEquals(1, success);
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
}
