package com.warhammer.ecom.controller;

import com.warhammer.ecom.config.JwtUtil;
import com.warhammer.ecom.controller.dto.ClientLoginDTO;
import com.warhammer.ecom.controller.dto.ClientLoginResponseDTO;
import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.service.ClientService;
import com.warhammer.ecom.service.SecurityService;
import com.warhammer.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public Client singUp(@RequestBody ClientSignUpDTO clientSignUpDTO) {
        return clientService.create(clientSignUpDTO);
    }

    @PostMapping("/login")
    public ClientLoginResponseDTO login(@RequestBody ClientLoginDTO clientLoginDTO) {
        if(!userService.login(clientLoginDTO.getEmail(), clientLoginDTO.getPassword())) {
            throw new AccessDeniedException("Invalid email or password");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(clientLoginDTO.getEmail(), clientLoginDTO.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(
            userDetails.getUsername(),
            List.copyOf(userDetails.getAuthorities())
        );

        Client client = clientService.getByEmail(clientLoginDTO.getEmail());

        ClientLoginResponseDTO response = new ClientLoginResponseDTO();
        response.setId(client.getId());
        response.setUser(client.getUser());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setBirthday(client.getBirthday());
        response.setNewsletter(client.isNewsletter());
        response.setCurrentCart(client.getCurrentCart());
        response.setAuthToken(token);

        return response;
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(
        Authentication authentication,
        @PathVariable Long clientId
    ) {
        securityService.isAdminOrOwner(clientId, authentication);
        clientService.delete(clientService.getById(clientId));
        return ResponseEntity.noContent().build();
    }
}
