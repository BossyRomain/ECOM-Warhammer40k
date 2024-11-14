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
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommandLineRepository commandLineRepository;

    public Cart create(Client client) {
        Cart cart = new Cart();
        cart.setClient(client);
        cart.setPurchaseDate(null);
        cart.setPaid(false);
        cart.setCommandLines(new ArrayList<>());
        return cartRepository.save(cart);
    }

    public CommandLine addProduct(Client client, Product product) {
        CommandLine commandLine = new CommandLine();
        commandLine.setProduct(product);
        commandLine.setCommand(client.getCurrentCart());
        commandLine.setQuantity(1);

        return commandLineRepository.save(commandLine);
    }

    public void setProductQuantity(Client client, Product product, int quantity) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(client.getId(), product.getId()).orElseThrow(NoSuchElementException::new);
        commandLine.setQuantity(quantity);
        commandLineRepository.save(commandLine);
    }

    public void removeProduct(Client client, Product product) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(client.getId(), product.getId()).orElseThrow(NoSuchElementException::new);

        Collection<CommandLine> lines = commandLine.getCommand().getCommandLines();
        lines.remove(commandLine);
        commandLine.getCommand().setCommandLines(lines);
        cartRepository.save(commandLine.getCommand());
        commandLineRepository.delete(commandLine);
    }

    public void pay(Client client) throws NoSuchElementException {
        Cart currentCart = client.getCurrentCart();

        cartRepository.pay(currentCart.getId(), Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        cartRepository.save(currentCart);

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
}
