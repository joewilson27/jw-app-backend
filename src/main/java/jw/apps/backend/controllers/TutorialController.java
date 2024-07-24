package jw.apps.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jw.apps.backend.dto.request.TutorialRequest;
import jw.apps.backend.dto.response.ApiResponse;
import jw.apps.backend.entity.Tutorial;
import jw.apps.backend.services.TutorialService;

@RestController
@RequestMapping("/api")
public class TutorialController {
  
  @Autowired
  TutorialService tutorialService;

  @GetMapping("/tutorials/welcome")
  public ResponseEntity<ApiResponse<List<String>>> testTutorial() {

    ApiResponse<List<String>> responseData = new ApiResponse<>();
    responseData.setStatus("success");
    responseData.setMessage("Welcome to Module Tutorial");
  
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {

    List<Tutorial> tutorials = tutorialService.getAllTutorials(title);

    if (tutorials.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<>(tutorials, HttpStatus.OK);
  }

  @PostMapping("/tutorials/create")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<ApiResponse<Tutorial>> storeData(@RequestBody TutorialRequest request) {
    Tutorial add = tutorialService.create(request);

    ApiResponse<Tutorial> responseData = new ApiResponse<>();
    responseData.setStatus("success");
    responseData.setMessage("Data added successfully");
    responseData.setData(add);

    return new ResponseEntity<>(responseData, HttpStatus.OK);
    //return ResponseEntity.ok(new MessageResponse("Data added successfully")); 
  }

}
