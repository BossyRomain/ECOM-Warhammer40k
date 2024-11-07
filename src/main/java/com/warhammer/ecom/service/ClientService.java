package com.warhammer.ecom.service;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.CartRepository;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class ClientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartRepository cartRepository;

    public Client create(ClientSignUpDTO clientSignUpDTO) {
        User user = new User();
        user.setUsername(clientSignUpDTO.getEmail());
        user.setPassword(clientSignUpDTO.getPassword());
        user.setAuthority(Authority.CLIENT);

        user = userRepository.save(user);

        Client client = new Client();
        client.setFirstName(clientSignUpDTO.getFirstName());
        client.setLastName(clientSignUpDTO.getLastName());
        client.setBirthday(clientSignUpDTO.getBirthday());
        client.setNewsletter(clientSignUpDTO.isNewsLetter());
        client.setUser(user);

        client = clientRepository.save(client);

        Cart cart = new Cart();
        cart.setClient(client);
        cart.setPurchaseDate(null);
        cart.setPaid(false);
        cart.setCommandLines(new ArrayList<>());
        cartRepository.save(cart);

        client.setCurrentCart(cart);

        return clientRepository.save(client);
    }

    public Client login(String email, String password) throws NoSuchElementException, IllegalArgumentException {
        Client client = clientRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        if(client.getUser().getPassword().equals(password)) {
            return client;
        } else {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    public void delete(Long clientId) throws NoSuchElementException {
        if(clientRepository.findById(clientId).orElse(null) == null) {
            throw new NoSuchElementException("No client with the client id " + clientId);
        }
        clientRepository.deleteById(clientId);
    }
}
