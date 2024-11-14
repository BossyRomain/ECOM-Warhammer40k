package com.warhammer.ecom.config;

import com.warhammer.ecom.model.Authority;
import com.warhammer.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/***
 * Configuration pour la sécurité de l'application, notament les autorisations requises pour chaque route et chaque type de requête.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(crsf -> crsf.disable())
            .authorizeHttpRequests(authz ->
                authz.
                    requestMatchers(HttpMethod.POST, "/api/clients/signup", "/api/clients/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority(Authority.ADMIN.getAuthority())
                    .requestMatchers(HttpMethod.PUT, "/api/products").hasAuthority(Authority.ADMIN.getAuthority())
                    .requestMatchers(HttpMethod.DELETE, "/api/products").hasAuthority(Authority.ADMIN.getAuthority())
                    .requestMatchers(HttpMethod.GET, "/api/products/catalogue", "api/products", "api/products/**").permitAll()
                    .anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }

    // Define PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // Use BCrypt for password encoding
    }
}
