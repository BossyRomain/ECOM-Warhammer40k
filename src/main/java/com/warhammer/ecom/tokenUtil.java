package com.warhammer.ecom;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/***
 * Classe regroupant des méthodes utiles pour la gestion des tokens dans l'application.
 */
@Component
public class tokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username, List<GrantedAuthority> authorities) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .claim("authorities", authorities)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        final Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    // Validate the token
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        final Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getExpiration();
    }

    // Extract the user's role from the token
    public List<GrantedAuthority> extractAuthorities(String token) {
        final Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get("authorities", List.class);
    }
}
