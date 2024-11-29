package com.warhammer.ecom.requestsfilters;

import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.UserRepository;
import com.warhammer.ecom.tokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@Order(2)
public class AdminOrOwnerFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private tokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String username = tokenUtil.extractUsername(token.replaceAll("Bearer ", ""));

        User requestUser = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        Client client = clientRepository.findByUserId(requestUser.getId()).orElseThrow(NoSuchElementException::new);

        boolean isAdmin = requestUser.getAuthority().equals(Authority.ADMIN);
        boolean isOwner = client.getId().equals(extractClientId(request.getRequestURI()));
        if (!(isAdmin || isOwner)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private long extractClientId(String requestURI) throws NumberFormatException {
        return Long.parseLong(requestURI.substring(1).split("/")[2]);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        try {
            return !path.startsWith("/api/clients") || extractClientId(path) <= 0;
        } catch (NumberFormatException ignored) {
            return true;
        }
    }
}
