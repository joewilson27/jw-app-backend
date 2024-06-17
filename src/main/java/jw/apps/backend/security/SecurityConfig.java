package jw.apps.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth ->
          auth.requestMatchers("/api/welcome/**").permitAll()
              .requestMatchers("/api/auth/**").permitAll()
              .anyRequest().authenticated()
        )
        .httpBasic(withDefaults());
    
    return http.build();
  }

  // Create a local user with UserDetails and save it to the InMemoryUserDetailsManager
  @Bean
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

}
