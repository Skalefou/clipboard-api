package org.skalefou.clipboardapi.feature.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtService {
    private static String SECRET = "oui";
    private static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + 1000 * 60 * 15);

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String id = extractId(token);
        return !isTokenExpired(token) && userDetails.getUsername().equals(id);
    }

    public String createToken(Map<String, Object> claims, UUID id) {
        String idString = id.toString();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(idString)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(EXPIRATION_DATE)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

}
