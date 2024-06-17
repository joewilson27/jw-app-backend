package jw.apps.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jw.apps.backend.dto.request.SignupRequest;
import jw.apps.backend.dto.response.MessageResponse;
import jw.apps.backend.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthService authService;
  
  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
    authService.signupUser(request);
    return ResponseEntity.ok(new MessageResponse("User signed up successfully"));
  }

}
