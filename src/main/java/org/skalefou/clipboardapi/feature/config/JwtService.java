package org.skalefou.clipboardapi.feature.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.skalefou.clipboardapi.feature.config.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${jwt.access.expiration.time}")
    private int expirationTimeAccessMinutes;

    @Value("${jwt.secret}")
    private String secret;

    private GlobalExceptionHandler globalExceptionHandler;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private Date generateExpirationDateAccess() {
        return new Date(System.currentTimeMillis() + 1000L * 60 * expirationTimeAccessMinutes);
    }

    private Key getSignKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new SignatureException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Token expired");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractMail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String mail){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, mail);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String mail = extractMail(token);
        return !isTokenExpired(token) && userDetails.getUsername().equals(mail);
    }

    public String createToken(Map<String, Object> claims, String mail) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(mail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(generateExpirationDateAccess())
                .signWith(getSignKey())
                .compact();
    }

}
