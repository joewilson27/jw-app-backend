package jw.apps.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    responseData.setCode(200);
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

  @GetMapping("/tutorialspage")
  public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
    @RequestParam(required = false) String title,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int size,
    @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
      Map<String, Object> data = tutorialService.getAllTutorialsPage(title, page, size, sort);

      if (data == null) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @PostMapping("/tutorials/create")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<ApiResponse<Tutorial>> storeData(@RequestBody TutorialRequest request) {
    Tutorial add = tutorialService.create(request);

    ApiResponse<Tutorial> responseData = new ApiResponse<>();
    responseData.setCode(200);
    responseData.setStatus("success");
    responseData.setMessage("Data added successfully");
    responseData.setData(add);

    return new ResponseEntity<>(responseData, HttpStatus.OK);
    //return ResponseEntity.ok(new MessageResponse("Data added successfully")); 
  }

  @GetMapping("/tutorials/{id}")
  public ResponseEntity<ApiResponse<Tutorial>> details(@PathVariable("id") long id) {
    
    Tutorial data = tutorialService.details(id);

    ApiResponse<Tutorial> responseData = new ApiResponse<>();
    responseData.setCode(200);
    responseData.setStatus("success");
    responseData.setMessage("Detail tutorial data");
    responseData.setData(data);

    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PutMapping("/tutorials/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<ApiResponse<Tutorial>> update(@PathVariable("id") long id,
                                                      @RequestBody TutorialRequest request) {

    Tutorial data = tutorialService.update(id, request);

    ApiResponse<Tutorial> responseData = new ApiResponse<>();
    responseData.setCode(200);
    responseData.setStatus("success");
    responseData.setMessage("Data updated successfully");
    responseData.setData(data);

    return new ResponseEntity<>(responseData, HttpStatus.OK);

  }
}
