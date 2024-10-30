package com.warhammer.ecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.MatcherType.mvc;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(crsf -> crsf.disable())
            .authorizeHttpRequests(authz ->
                authz.requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/api/images/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
