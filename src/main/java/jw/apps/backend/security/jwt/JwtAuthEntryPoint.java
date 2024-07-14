package jw.apps.backend.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    System.out.println("4 commence");
    // OLD FASHION
    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

    // test to print log when user can't login
    logger.error("Unauthenticated user {}", authException.getMessage());

    // If you want to customize the response data (show the response to the client), just use an ObjectMapper like following code: 
    // the reason because this authentication exception is different from the spring context exception
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // 1. to catch error jwt (all exceptions related to jwt authentication)
    // final Map<String, Object> body = new HashMap<>();
    // body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    // body.put("message", authException.getMessage());

    // 2. separating between invalid login and other errors authenticating
    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    if (authException instanceof BadCredentialsException) {
      // custom authentication for failure to login
      body.put("message", "Invalid username or password");
    } else {
      // other exception
      body.put("message", authException.getMessage());
    }

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
  
}
