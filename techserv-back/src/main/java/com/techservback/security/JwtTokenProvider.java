package com.techservback.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.techservback.repository.model.User;

import java.time.Instant;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400}")
    private long jwtExpiration;

    private final JwtEncoder jwtEncoder;

    public JwtTokenProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtExpiration);

        // 1. Definir explícitamente el header con el algoritmo HS256
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        // 2. Mantener tus claims igual
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("techserv-back")
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("role", user.getRole())
        .issuedAt(now)
        .expiresAt(expiresAt)
        .build();

        // 3. Pasar tanto el header como los claims a los parámetros
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}

