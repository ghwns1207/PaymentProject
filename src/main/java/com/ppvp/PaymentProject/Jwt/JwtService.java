package com.ppvp.PaymentProject.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {
  @Value("${jwt.accessToken.secretKey}")
  private String jwtSecretKey;
  public Claims parseToken(String token) {
    try {
      return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      log.error("Error parsing JWT token: {}", e.getMessage());
      return null;
    }
  }

  public boolean isTokenExpired(String token) {
    Claims claims = parseToken(token);
    if (claims == null) {
      return false;
    }
    long expiration = claims.getExpiration().getTime();
    long currentTimeMillis = System.currentTimeMillis();
    return expiration < currentTimeMillis;
  }
}
