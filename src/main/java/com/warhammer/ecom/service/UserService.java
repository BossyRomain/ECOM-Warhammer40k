package com.warhammer.ecom.service;

import com.warhammer.ecom.config.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean login(String username, String password) {
        try {
            com.warhammer.ecom.model.User user = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
            return user.getPassword().equals(password);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public com.warhammer.ecom.model.User get(Long userId) throws NoSuchElementException {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    public com.warhammer.ecom.model.User get(String username) throws NoSuchElementException {
        return userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
    }

    public com.warhammer.ecom.model.User create(String username, String password, Authority authority) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthority(authority);

        return userRepository.save(user);
    }

    public com.warhammer.ecom.model.User create(com.warhammer.ecom.model.User user) {
        return userRepository.save(user);
    }

    public com.warhammer.ecom.model.User update(com.warhammer.ecom.model.User user) throws NoSuchElementException {
        if(userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(com.warhammer.ecom.model.User user) {
        userRepository.delete(user);
    }
}
