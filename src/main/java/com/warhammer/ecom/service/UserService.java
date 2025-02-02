package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.model.User;
import com.warhammer.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
            return user.getPassword().equals(password);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public User create(String username, String password, Authority authority) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthority(authority);

        return userRepository.save(user);
    }
}
