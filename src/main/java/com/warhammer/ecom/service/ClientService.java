package com.warhammer.ecom.service;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ClientService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CartService cartService;

    public Client getById(Long clientId) throws NoSuchElementException {
        return clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
    }

    public Client getByUserId(Long userId) throws NoSuchElementException {
        return clientRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
    }

    public Client getByEmail(String email) throws NoSuchElementException {
        return clientRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
    }

    public Client create(ClientSignUpDTO clientSignUpDTO) {
        User user = userService.create(clientSignUpDTO.getEmail(), clientSignUpDTO.getPassword(), Authority.CLIENT);

        Client client = new Client();
        client.setFirstName(clientSignUpDTO.getFirstName());
        client.setLastName(clientSignUpDTO.getLastName());
        client.setBirthday(clientSignUpDTO.getBirthday());
        client.setNewsletter(clientSignUpDTO.isNewsLetter());
        client.setUser(user);

        client = clientRepository.save(client);

        Cart cart = cartService.create(client);
        client.setCurrentCart(cart);

        return clientRepository.save(client);
    }

    public Client update(Client client) throws NoSuchElementException {
        if (clientRepository.existsById(client.getId())) {
            return clientRepository.save(client);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(Client client) {
        clientRepository.deleteById(client.getId());
    }
}
