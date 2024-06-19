package jw.apps.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jw.apps.backend.dto.request.SigninRequest;
import jw.apps.backend.dto.request.SignupRequest;
import jw.apps.backend.dto.response.AuthLoginResponse;
import jw.apps.backend.dto.response.MessageResponse;
import jw.apps.backend.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthService authService;
  
  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
    authService.signUp(request);
    return ResponseEntity.ok(new MessageResponse("User signed up successfully"));

    // this approach if you choose to catch exceptions ConstraintViolationException from ValidationService when validate request
    // try {
    //   authService.signupUser(request);
    //   return ResponseEntity.ok(new MessageResponse("User signed up successfully"));
    // } catch (ConstraintViolationException ex) {
    //   Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    //         String errorMessages = violations.stream()
    //                 .map(violation -> "Field '" + violation.getPropertyPath() + "': " + violation.getMessage())
    //                 .collect(Collectors.joining(", "));
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input request: " + errorMessages);
    // }

    // Using try catch block to handle unexpected exceptions
    // try { 
    //   authService.signUp(request);
    //   return ResponseEntity.ok(new MessageResponse("User signed up successfully"));
    // } catch (Exception ex) {
    //   // Handle other exceptions
    //   ErrorResponse err = new ErrorResponse();
    //   err.setCode(500);
    //   err.setMessage(ex.getMessage());
    //   return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@RequestBody SigninRequest request) {
    AuthLoginResponse processLogin = authService.signIn(request);

    return new ResponseEntity<>(processLogin, HttpStatus.OK);
  }

}
