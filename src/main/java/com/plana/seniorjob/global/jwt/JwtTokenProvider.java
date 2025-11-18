package com.plana.seniorjob.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    // 1시간
    private final long validityInMs = 60 * 60 * 1000L;
    private final long refreshTokenValidity = 14 * 24 * 60 * 60 * 1000L;

    public String createAccessToken(Long userId, String username) {
        return createToken(userId, username, validityInMs);
    }

    public String createRefreshToken(Long userId, String username) {
        return createToken(userId, username, refreshTokenValidity);
    }

    private String createToken(Long userId, String username, long validity) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}