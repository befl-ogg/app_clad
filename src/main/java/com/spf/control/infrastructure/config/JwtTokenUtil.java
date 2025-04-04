package com.spf.control.infrastructure.config;

import com.spf.control.feature.user.domain.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${spf.api.jwtSecret}")
    private String jwtSecret;

    @Value("${spf.api.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spf.api.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Long getIdUserFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Long.class));
    }

    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getExpirationDateFromExpiredToken(String token) {
        Date date = null;
        try{
            date = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (ExpiredJwtException e){
            date = e.getClaims().getExpiration();
        }

        return date;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims;
        try {
            claims = getAllClaimsFromToken(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired: " + e.getMessage(), e);
        }
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims body = null;
        try {
            body = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage(), e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("JWT token is unsupported: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty: " + e.getMessage(), e);
        }

        return body;
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean isValidForRefreshToken(String token) {
        var jwtExpiration = getExpirationDateFromExpiredToken(token);
        var limitDate = new Date(jwtExpiration.getTime() + jwtRefreshExpirationMs);
        var currentDate = new Date(System.currentTimeMillis());
        return currentDate.before(limitDate);
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", getRole(userDetails));
        claims.put("userId", userDetails.getId());
        claims.put("uniqueId", UUID.randomUUID().toString());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private static String getRole(User userDetails) {
        if (userDetails.getAuthorities().isEmpty())
            return null;
        var firstAuthority = userDetails.getAuthorities().stream().findFirst();
        return firstAuthority.map(GrantedAuthority::getAuthority).orElse(null);
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return "";
    }

    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
