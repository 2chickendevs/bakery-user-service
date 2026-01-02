package com.twochickendevs.bakeryuserservice.auth.utils;

import com.twochickendevs.bakeryuserservice.auth.repository.TokenRepository;
import com.twochickendevs.bakeryuserservice.constant.Constant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Log4j2
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private int jwtExpirationMs;

    @Autowired
    private TokenRepository tokenRepository;

    public String generateJwtToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder().setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(now.plusMillis(jwtExpirationMs)))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(Constant.BEARER)) {
            return headerAuth.substring(7);
        }

        return "";
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String token) {
        return canParse(token) && isExistInDatabase(token);
    }

    private boolean canParse(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        }

        return false;
    }

    private boolean isExistInDatabase(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
