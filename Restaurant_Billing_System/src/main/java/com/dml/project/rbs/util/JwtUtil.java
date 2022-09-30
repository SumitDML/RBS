package com.dml.project.rbs.util;

import com.dml.project.rbs.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final int TOKEN_VALIDITY = 60 * 60 * 1000;

    private static final String secret_key ="Secret_Key_RBS";


    public String extractEmailFromToken(String token) {
        return extractClaimFromToken(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 5))
                .signWith(SignatureAlgorithm.HS512, secret_key).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String Email = extractEmailFromToken(token);
        return (Email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
