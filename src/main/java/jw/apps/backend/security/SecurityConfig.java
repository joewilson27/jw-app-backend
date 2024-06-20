package jw.apps.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jw.apps.backend.security.jwt.JwtAuthEntryPoint;
import jw.apps.backend.security.jwt.JwtAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  private JwtAuthEntryPoint authEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        // start add handling for JWT exceptions
        .exceptionHandling(
          eh -> eh.authenticationEntryPoint(authEntryPoint)
        )
        .sessionManagement(
          sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        // end add handling for JWT exceptions
        .authorizeHttpRequests(auth ->
          auth.requestMatchers("/api/welcome/**").permitAll()
              .requestMatchers("/api/auth/**").permitAll()
              .anyRequest().authenticated()
        )
        .httpBasic(withDefaults());
        
    // add check jwt token
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // Create a local user with UserDetails and save it to the InMemoryUserDetailsManager
  // @Bean
  public UserDetailsService user() {
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    UserDetails admin = User.builder()
                        .username("admin")
                        .password(encoder.encode("password"))
                        .roles("ADMIN")
                        .build();

    UserDetails user = User.builder()
                        .username("user")
                        .password(encoder.encode("password")) // .password("{noop}password") with no encoder
                        .roles("USER")
                        .build();

    return new InMemoryUserDetailsManager(admin, user);
  }

  // tell the bean that you use BCryptPasswordEncoder as a password encoder
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Add JwtAuthenticationFilter to the bean then use in filterChain
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }


}
