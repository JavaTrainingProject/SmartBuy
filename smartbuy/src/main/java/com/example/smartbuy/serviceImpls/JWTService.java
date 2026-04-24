package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.entity.UserEntity;
import com.example.smartbuy.enums.Role;
import com.example.smartbuy.exception.InvalidCredentialsException;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;

    public JWTService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String email, Role role){
        HashMap<String,Object> claims=new HashMap<>();
        claims.put("role",role.name());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*2))
                .addClaims(claims)
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return refreshToken;
    }

    public UserEntity verifyRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidCredentialsException("Refresh token required");
        }

        return userRepository.findByRefreshToken(token)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));
    }

    private Key getSignedKey(){

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token){

        return extractClaims(token).getExpiration().before(new Date());
    }
}

