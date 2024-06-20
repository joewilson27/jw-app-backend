package jw.apps.backend.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jw.apps.backend.security.SecurityConstant;

@Component
public class JwtGenerator {
  
  private long jwtExpiration = SecurityConstant.JWT_EXPIRATION;

  private static final Key jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConstant.JWT_SECRET));

  //private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  public String generateToken(Authentication authentication) {

    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

    // with key variable 
    // String token = Jwts.builder()
    //                    .setSubject(username)
    //                    .setIssuedAt(currentDate)
    //                    .setExpiration(expireDate)
    //                    .signWith(key,SignatureAlgorithm.HS256)
    //                    .compact();

    // with jwtSecret variable
    String token = Jwts.builder()
                      .setSubject(username)
                      .setIssuedAt(currentDate)
                      .setExpiration(expireDate)
                      .signWith(jwtSecret, SignatureAlgorithm.HS256)
                      .compact();

    return token;
  }

  public boolean validateToken(String token) {
    try {
      // with key variable 
      // Jwts.parserBuilder()
      //     .setSigningKey(key)
      //     .build()
      //     .parseClaimsJws(token);

      // with jwtSecret variable
      Jwts.parserBuilder()
          .setSigningKey(jwtSecret)
          .build()
          .parseClaimsJws(token);
      
      return true;
    } catch (Exception e) {
      throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", e.fillInStackTrace());
    }
  }

  public String getUsernameFromToken(String token) {
    // with key variable 
    //return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

    // with jwtSecret variable
    return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
  }

  // // Another approach for token TO DO
  // private static final String SECRET_KEY = "your-custom-secret-key";

  // // Generate a token
  // public String generateToken(String username) {
  //     return Jwts.builder()
  //             .setSubject(username)
  //             .setIssuedAt(new Date())
  //             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
  //             .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
  //             .compact();
  // }

  // // Validate a token
  // public boolean validateToken(String token, String username) {
  //     final String extractedUsername = getUsernameFromToken(token);
  //     return (extractedUsername.equals(username) && !isTokenExpired(token));
  // }

  // // Extract username from the token
  // public String getUsernameFromToken(String token) {
  //     return getClaimFromToken(token, Claims::getSubject);
  // }

  // // Check if the token is expired
  // private boolean isTokenExpired(String token) {
  //     final Date expiration = getClaimFromToken(token, Claims::getExpiration);
  //     return expiration.before(new Date());
  // }

  // // Extract a claim from the token // That's cool, the second params is input a dynamic method from Claims Interface (Claims::getSubject, Claims::getExpiration)
  // and will return a dynamic type of data depend on method who used this getClaimFromToken().
  // for example, for method getUsernameFromToken() this getClaimFromToken() method will return String type,
  // and for isTokenExpired() method will return boolean type.
  // private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
  //     final Claims claims = getAllClaimsFromToken(token);
  //     return claimsResolver.apply(claims);
  // }

  // // Extract all claims from the token
  // private Claims getAllClaimsFromToken(String token) {
  //     try {
  //         return Jwts.parser()
  //                 .setSigningKey(SECRET_KEY)
  //                 .parseClaimsJws(token)
  //                 .getBody();
  //     } catch (SignatureException e) {
  //         // Handle exception (e.g., log the error or rethrow it)
  //         throw new RuntimeException("Invalid JWT signature", e);
  //     }
  // }


}
