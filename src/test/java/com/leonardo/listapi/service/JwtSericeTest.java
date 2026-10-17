
package com.leonardo.listapi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(
                io.jsonwebtoken.io.Decoders.BASE64.decode(
                        Encoders.BASE64.encode(
                                new byte[32]
                        )
                )
        );

        String secret = Encoders.BASE64.encode(
                secretKey.getEncoded()
        );

        jwtService = new JwtService(secret);
    }

    @Test
    void deveAceitarTokenValido() {
        String token = jwtService.generateToken(
                "teste@example.com"
        );

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void deveRejeitarTokenExpirado() {
        Instant agora = Instant.now();

        String tokenExpirado = Jwts.builder()
                .subject("teste@example.com")
                .issuedAt(Date.from(agora.minusSeconds(7200)))
                .expiration(Date.from(agora.minusSeconds(3600)))
                .signWith(secretKey)
                .compact();

        assertFalse(jwtService.isTokenValid(tokenExpirado));
    }
}
