package jw.apps.backend.services;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jw.apps.backend.entity.Role;
import jw.apps.backend.entity.UserEntity;
import jw.apps.backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    // 1. find by username and email (with Optional)
    // Optional<UserEntity> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail);

    // if (!userOpt.isPresent()) {
    //   throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
    // }
    // UserEntity user = userOpt.get();
    // return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

    // 2. find by username and email
    UserEntity user = userRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

    // 3. find by username
    // UserEntity user = userRepository.findByUsername(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

    // 4. find by email
    // UserEntity user = userRepository.findByEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("Username not found"));


    return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    /*
     * If you want to add more value into UserDetails, create class that implements UserDetails then add value you want to add
     */
  }

  private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
  }
  
}
