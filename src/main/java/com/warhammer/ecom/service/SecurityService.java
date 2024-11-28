package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service("securityService")
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    public void isAdminOrOwner(Long clientId, Authentication authentication) throws NoSuchElementException, AccessDeniedException {
        User requestUser = userRepository.findByUsername(authentication.getPrincipal().toString()).orElseThrow(NoSuchElementException::new);
        Client client = clientRepository.findByUserId(requestUser.getId()).orElseThrow(NoSuchElementException::new);

        boolean isAdmin = requestUser.getAuthority().equals(Authority.ADMIN);
        boolean isOwner = client.getId().equals(clientId);
        if (!(isAdmin || isOwner)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }
}
