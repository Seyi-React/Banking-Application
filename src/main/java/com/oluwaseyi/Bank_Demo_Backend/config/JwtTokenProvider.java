package com.oluwaseyi.Bank_Demo_Backend.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secretKey}")
    private String jwtSecret;

    @Value("${app.jwtExpirationDate}")
    private Long jwtExpirationDate;

    public String generateToken(org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // Ensure to use supported methods for your jjwt version
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key()) // Ensure this matches the method signature of your jjwt version
                .compact();
    }

    private Key key() {
        // Ensure jwtSecret is Base64 encoded
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token); // Use parseClaimsJws for JWTs
            return true;
        } catch (Exception e) {
            // Add logging or handling here
            return false;
        }
    }
}
