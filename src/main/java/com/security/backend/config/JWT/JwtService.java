package com.security.backend.config.JWT;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.security.backend.dtos.UserDto;
import com.security.backend.mappers.ModelMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"

    private final static String JwtSECRET = "324fa68ec33e3a0cd9211e42e56c2b7260cea7bbbcae91091d07b2a35c9378a3";
    private final static Long JwtEXP = 1000 * 60 * 60L; // 1hr

    public String generateToken(UserDto user) {
        return Jwts.builder()
                .setIssuer("practice.com")
                .setSubject(user.getEmail())
                .claim("roles", ModelMapper.toListOfRoleString(user.getRoles()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtEXP))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(JwtSECRET.getBytes());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isValidToken(String token) {
        return extractClaim(token, Claims::getExpiration).after(new Date());
    }

    public String getSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<GrantedAuthority> getRolesFromString(String roleString) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleString));
    }

    public List<GrantedAuthority> createAuthList(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
