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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.*;

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
     * Plusieurs clients essaient d'acheter l'intégralité du stock d'un produit mais un seul y arrive.
     */
    @Test
    public void testOneBuyAll() throws InterruptedException {
        final int NB_CLIENTS = 100;

        List<Client> clients = createClients(NB_CLIENTS);

        Random random = new Random();
        final Long productId = random.nextLong(1, 10);
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
                    cartService.pay(id);
                    return true;
                } catch (RuntimeException e) {
                    System.out.println("Out of stock");
                    return false;
                } catch (Exception e) {
                    System.out.println("Failed to pay");
                    fail();
                    return false;
                }
            };
            callables.add(callable);
        }

        // Lancement de tout les threads
        List<Future<Boolean>> results = executorService.invokeAll(callables);

        for (Future<Boolean> f : results) {
            try {
                System.out.println(f.get());
            } catch (ExecutionException e) {
                System.out.println("fail this one");
            }
        }

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

    private List<Client> createClients(int nbClients) {
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
