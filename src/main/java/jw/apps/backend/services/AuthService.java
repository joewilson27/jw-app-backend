package jw.apps.backend.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jw.apps.backend.dto.request.SignupRequest;
import jw.apps.backend.entity.ERole;
import jw.apps.backend.entity.Role;
import jw.apps.backend.entity.UserEntity;
import jw.apps.backend.helpers.BadRequestException;
import jw.apps.backend.helpers.ResourceNotFoundException;
import jw.apps.backend.repository.RoleRepository;
import jw.apps.backend.repository.UserRepository;

@Service
public class AuthService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  ValidationService validationService;

  @Autowired
  PasswordEncoder passwordEncoder; // make sure that you have set bean password encoder in security config
  
  public Boolean existsByUsername(String username) {

    if (userRepository.existsByUsername(username)) {
      return true;
    }

    return false;
  }

  public Boolean existsByEmail(String email) {

    if (userRepository.existsByEmail(email)) {
      return true;
    }

    return false;
  }
  
  public Boolean existsByUsernameOrEmail(String username, String email) {

    if (userRepository.existsByEmailOrUsername(username, email)) {
      return true;
    }

    return false;
  }

  @Transactional
  public void signupUser(SignupRequest request) {
    // validate request
    validationService.validate(request);

    // 1. Check username and email exists - one by one
    if (existsByUsername(request.getUsername())) {
      throw new BadRequestException("Username is already in use!");
    }

    if (existsByEmail(request.getEmail())) {
      throw new BadRequestException("Email is already in use!");
    }

    // 2. Check username and email exists - one time check.
    // if (existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
    //   throw new BadRequestException("Username or Email is already in use!");
    // }

    // Create a new user
    UserEntity user = new UserEntity();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<String> strRoles = request.getRole(); // type Set<String>, check type for role in SignupRequest DTO
    Set<Role> roles = new HashSet<>(); // type Set<Role>, check type for roles in UserEntity

    if (strRoles == null) {
      // it means, user didn't fill the role. Set role type 'USER' as default
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new ResourceNotFoundException("Role user is not found!"));
      roles.add(userRole);
    } else {
      // it means, user fill the role
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                             .orElseThrow(() -> new ResourceNotFoundException("Role admin is not found!"));
            roles.add(adminRole);
            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                             .orElseThrow(() -> new ResourceNotFoundException("Role mod is not found!"));
            roles.add(modRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new ResourceNotFoundException("Role user is not found!"));
            roles.add(userRole);
            break;
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
  }

}
