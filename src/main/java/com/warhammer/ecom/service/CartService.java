package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.CartRepository;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.CommandLineRepository;
import com.warhammer.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CommandLineRepository commandLineRepository;

    @Autowired
    private ProductRepository productRepository;

    public CommandLine addProduct(Long clientId, Long productId) throws NoSuchElementException {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);

        CommandLine commandLine = new CommandLine();
        commandLine.setProduct(product);
        commandLine.setCommand(client.getCurrentCart());
        commandLine.setQuantity(1);

        return commandLineRepository.save(commandLine);
    }

    public void setProductQuantity(Long clientId, Long productId, int quantity) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(clientId, productId).orElseThrow(NoSuchElementException::new);
        commandLine.setQuantity(quantity);
        commandLineRepository.save(commandLine);
    }

    public void removeProduct(Long clientId, Long productId) throws NoSuchElementException {
        CommandLine commandLine = commandLineRepository.findByClientAndProduct(clientId, productId).orElseThrow(NoSuchElementException::new);

        Collection<CommandLine> lines = commandLine.getCommand().getCommandLines();
        lines.remove(commandLine);
        commandLine.getCommand().setCommandLines(lines);
        cartRepository.save(commandLine.getCommand());
        commandLineRepository.delete(commandLine);
    }

    /**
     * When a client pay its current unpaid cart
     * @param clientId a client id
     */
    public void pay(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
        cartRepository.pay(client.getCurrentCart().getId(), Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        cartRepository.save(client.getCurrentCart());

        // Create a new unpaid cart
        Cart unpaidCart = new Cart();
        unpaidCart.setClient(client);
        unpaidCart.setPaid(false);
        unpaidCart.setPurchaseDate(null);
        unpaidCart.setCommandLines(new ArrayList<>());
        unpaidCart = cartRepository.save(unpaidCart);
        client.setCurrentCart(unpaidCart);
        clientRepository.save(client);
    }

    public List<Cart> getClientCommands(Long clientId) {
        return cartRepository.getClientCommands(clientId).stream().toList();
    }
}
