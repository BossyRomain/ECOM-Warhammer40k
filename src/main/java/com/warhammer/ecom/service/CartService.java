package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.CartRepository;
import com.warhammer.ecom.repository.CommandLineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ProductService productService;

    @Autowired
    private CommandLineRepository commandLineRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Cart create(Client client) {
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setPurchaseDate(null);
        cart.setPaid(false);
        cart.setCommandLines(new ArrayList<>());
        return cartRepository.save(cart);
    }

    public CommandLine addProduct(Client client, Product product) throws IllegalArgumentException {
        return addProduct(client, product, 1);
    }

    public CommandLine addProduct(Client client, Product product, int quantity) throws IllegalArgumentException {
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds stock");
        } else if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be superior to zero");
        }

        if (client.getCurrentCart().getCommandLines().stream().anyMatch(cl -> cl.getProduct().equals(product))) {
            throw new IllegalArgumentException("Product already in the client cart");
        }

        CommandLine commandLine = new CommandLine();
        commandLine.setProduct(product);
        commandLine.setCommand(client.getCurrentCart());
        commandLine.setQuantity(quantity);

        commandLine = commandLineRepository.save(commandLine);
        client.getCurrentCart().getCommandLines().add(commandLine);
        return commandLine;
    }

    public void setProductQuantity(Client client, Product product, int quantity) throws NoSuchElementException {
        CommandLine commandLine = getCommandLine(client, product);
        commandLine.setQuantity(quantity);
    }

    public void removeProduct(Client client, Product product) throws NoSuchElementException {
        CommandLine commandLine = getCommandLine(client, product);
        commandLine.getCommand().getCommandLines().remove(commandLine);
        commandLineRepository.delete(commandLine);
    }

    @Transactional
    public void pay(Client client) throws RuntimeException, NoSuchElementException {
        Cart currentCart = client.getCurrentCart();

        // TODO: trier la commande par id de produit croissant

        // Check products' stocks are enough
        for (CommandLine commandLine : currentCart.getCommandLines()) {
            Product p = productService.get(commandLine.getProduct().getId());
            if (commandLine.getQuantity() > p.getStock()) {
                throw new RuntimeException("Not enough stock for this product: " + p.getName());
            }
        }

        cartRepository.pay(currentCart.getId(), Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        cartRepository.save(currentCart);

        for (CommandLine commandLine : currentCart.getCommandLines()) {
            Product product = commandLine.getProduct();
            int stock = product.getStock() - commandLine.getQuantity();
            product.setStock(stock);
            productService.update(product);
        }

        emailService.sendCartPayValidation(client.getUser().getUsername(), currentCart);

        // Create a new unpaid cart
        Cart newCart = new Cart();
        newCart.setClient(client);
        newCart.setPaid(false);
        newCart.setPurchaseDate(null);
        newCart.setCommandLines(new ArrayList<>());
        newCart = cartRepository.save(newCart);
        client.setCurrentCart(newCart);
    }

    public List<Cart> getClientCommands(Client client) throws NoSuchElementException {
        return client.getCarts().stream().toList();
    }

    private CommandLine getCommandLine(Client client, Product product) throws NoSuchElementException {
        Cart cart = client.getCurrentCart();
        int i = 0;
        while (i < cart.getCommandLines().size() && !cart.getCommandLines().get(i).getProduct().equals(product)) {
            i++;
        }

        if (i == cart.getCommandLines().size()) {
            throw new NoSuchElementException();
        }

        return cart.getCommandLines().get(i);
    }
}
