package jw.apps.backend.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jw.apps.backend.services.UserService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwtGenerator jwtGenerator;

  @Autowired
  UserService userService;

  /**
   * So doFilterInternal is a link in the Security Filter Chain where BEFORE we actually get to the controllers
   * it's going to PERFORM a CHECK to SEE if there's actually a token within the header (on request) 
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Get token from the request
      String token = getJwtFromRequest(request);
      // check token exists
      if (token != null && StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
        // Get username from token
        String username = jwtGenerator.getUsernameFromToken(token);

        // check username from table user
        UserDetails userDetails = userService.loadUserByUsername(username);

        // UsernamePasswordAuthenticationToken gets {username, password} from login Request, AuthenticationManager will use it to authenticate a login account.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }

    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    // Get the token out from the headers
    String headerAuth = request.getHeader("Authorization");
    String tokenType = "Bearer ";

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(tokenType)) {
      return headerAuth.substring(tokenType.length(), headerAuth.length());
    }

    return null;
  }
  
}
