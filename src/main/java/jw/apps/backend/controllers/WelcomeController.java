package jw.apps.backend.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jw.apps.backend.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/welcome")
public class WelcomeController {
  
  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return new ResponseEntity<>("Hello friend! Welcome to JWApp :)", HttpStatus.OK);
  }

  @GetMapping("/standard-response")
  public ResponseEntity<ApiResponse<List<String>>> standardResponse() { 

    List<String> data = new ArrayList<>();
    data.add("Tesla");
    data.add("Ford");
    data.add("GMC");
    data.add("Chevrolet");

    ApiResponse<List<String>> responseData = new ApiResponse<>();
    responseData.setStatus("success");
    responseData.setMessage("Data retrieved successfully with standard response");
    responseData.setData(data);
  
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/standard-response-builder")
  public ApiResponse<List<String>> standardResponseBuilder() {

    List<String> data = new ArrayList<>();
    data.add("Tesla");
    data.add("Ford");
    data.add("GMC");
    data.add("Chevrolet");

    return ApiResponse.<List<String>>builder()
                          .status("success")
                          .message("Data retrieved successfully with standard response builder")
                          .data(data)
                          .build();
  }

}
