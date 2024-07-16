package jw.apps.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jw.apps.backend.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/tutorial")
public class TutorialController {
  
  @GetMapping("/welcome")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ApiResponse<List<String>>> testTutorial() {

    ApiResponse<List<String>> responseData = new ApiResponse<>();
    responseData.setStatus("success");
    responseData.setMessage("Welcome to Module Tutorial");
  
    return new ResponseEntity<>(responseData, HttpStatus.OK);

  }

}
