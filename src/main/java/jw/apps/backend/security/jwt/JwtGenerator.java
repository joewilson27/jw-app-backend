package jw.apps.backend.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jw.apps.backend.security.SecurityConstant;

@Component
public class JwtGenerator {
  
  private long jwtExpiration = SecurityConstant.JWT_EXPIRATION;

  private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  public String generateToken(Authentication authentication) {

    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

    String token = Jwts.builder()
                       .setSubject(username)
                       .setIssuedAt(currentDate)
                       .setExpiration(expireDate)
                       .signWith(key,SignatureAlgorithm.HS256)
                       .compact();

    return token;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      
      return true;
    } catch (Exception e) {
      throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", e.fillInStackTrace());
    }
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
  }

}
