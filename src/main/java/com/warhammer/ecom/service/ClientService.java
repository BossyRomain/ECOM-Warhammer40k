package com.warhammer.ecom.service;

import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.controller.dto.ClientUdpateDTO;
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

    @Autowired
    private EmailService emailService;

    public Client getById(Long clientId) throws NoSuchElementException {
        return clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
    }

    public Client getByEmail(String email) throws NoSuchElementException {
        return clientRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
    }

    public Client create(ClientSignUpDTO clientSignUpDTO) {
        return create(clientSignUpDTO, true);
    }

    public Client create(ClientSignUpDTO clientSignUpDTO, boolean sendEmail) {
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

        if (sendEmail) {
            emailService.sendRegisterConfirmation(clientSignUpDTO.getEmail());
        }

        return clientRepository.save(client);
    }

    public void update(Long clientId, ClientUdpateDTO clientUdpateDTO) throws NoSuchElementException {
        Client client = clientRepository.findById(clientId).orElseThrow(NoSuchElementException::new);
        client.setFirstName(clientUdpateDTO.getFirstName());
        client.setLastName(clientUdpateDTO.getLastName());
        client.setBirthday(client.getBirthday());
    }

    public void delete(Long clientId) {
        clientRepository.deleteById(clientId);
    }
}
