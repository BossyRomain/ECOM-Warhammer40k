package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.CartRepository;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.CommandLineRepository;
import com.warhammer.ecom.repository.ProductRepository;
import jakarta.persistence.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommandLineRepository commandLineRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    public Cart create(Client client) {
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setPurchaseDate(null);
        cart.setPaid(false);
        cart.setCommandLines(new ArrayList<>());
        cart = cartRepository.save(cart);
        client.setCurrentCart(cart);
        clientRepository.save(client);
        return cart;
    }

    public CommandLine addProduct(Long clientId, Long productId) throws NoSuchElementException, IllegalArgumentException {
        return addProduct(clientId, productId, 1);
    }

    @Transactional
    public CommandLine addProduct(Long clientId, Long productId, int quantity) throws NoSuchElementException, IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be superior to zero");
        }

        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
        Cart currentCart = client.getCurrentCart();
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds stock");
        }

        if (client.getCurrentCart().getCommandLines().stream().anyMatch(cl -> cl.getProduct().equals(product))) {
            throw new IllegalArgumentException("Product already in the client cart");
        }

        CommandLine commandLine = new CommandLine();
        commandLine.setProduct(product);
        commandLine.setQuantity(quantity);
        commandLine.setCommand(currentCart);

        commandLine = commandLineRepository.save(commandLine);

        List<CommandLine> cls = currentCart.getCommandLines();
        cls.add(commandLine);
        currentCart.setCommandLines(cls);
        return commandLine;
    }

    public void setProductQuantity(Long clientId, Long productId, int quantity) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(clientId, productId).orElseThrow(NoSuchElementException::new);
        commandLine.setQuantity(quantity);
        commandLineRepository.save(commandLine);
    }

    public void removeProduct(Long clientId, Long productId) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(clientId, productId).orElseThrow(NoSuchElementException::new);
        commandLineRepository.delete(commandLine);
    }

    @Transactional
    public boolean pay(Long clientId) throws RuntimeException, NoSuchElementException {
        return true;
    }

    @Transactional
    public boolean pay(Cart cart) throws RuntimeException, NoSuchElementException {
        try {
            List<CommandLine> commandLines = cart.getCommandLines();
            if (commandLines.size() > 1) {
                commandLines.sort((cl1, cl2) -> {
                    if (cl1.getProduct().getId() < cl2.getProduct().getId()) {
                        return -1;
                    } else if (cl1.getProduct().getId() > cl2.getProduct().getId()) {
                        return 1;
                    }
                    return 0;
                });
            } else if (commandLines.isEmpty()) {
                throw new RuntimeException("The cart is empty");
            }

            // Check products' stocks are enough
            for (CommandLine commandLine : commandLines) {
                Product product = productRepository.findByIdWithLock(commandLine.getProduct().getId()).orElseThrow(NoSuchElementException::new);
                if (commandLine.getQuantity() > product.getStock()) {
                    throw new RuntimeException("Not enough stock for this product: " + product.getName() + " " + product.getId());
                }
                int newStock = product.getStock() - commandLine.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);
            }

            cartRepository.pay(cart.getId(), Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            cartRepository.save(cart);

            return true;
        } catch (PessimisticLockException | LockTimeoutException e) {
            return false;
        }
    }

    public List<Cart> getClientCommands(Long clientId) throws NoSuchElementException {
        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
        return client.getCarts().stream().toList();
    }

    public void clear(Long clientId) throws NoSuchElementException {
        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
        client.getCurrentCart().getCommandLines().clear();
        cartRepository.save(client.getCurrentCart());
        clientRepository.save(client);
    }
}
