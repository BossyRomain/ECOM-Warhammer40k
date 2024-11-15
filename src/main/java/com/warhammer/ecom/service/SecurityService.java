package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service("securityService")
public class SecurityService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    public void isAdminOrOwner(Long clientId, Authentication authentication) throws NoSuchElementException, AccessDeniedException {
        User requestUser = (User) userService.loadUserByUsername(authentication.getPrincipal().toString());
        Client client = clientService.getByUserId(requestUser.getId());

        boolean isAdmin = requestUser.getAuthority().equals(Authority.ADMIN);

        boolean isOwner = client.getId().equals(clientId);

        if(!(isAdmin || isOwner)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }
}
