package jw.apps.backend.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jw.apps.backend.dto.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Create global handler exception for response message status 400
   * @param ex
   * @return
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleCustomServiceException(BadRequestException ex) {
    
    ErrorResponse err = new ErrorResponse();
    err.setCode(400);
    err.setMessage(ex.getMessage());

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

  /**
   * Create global handler exception for response message status 404
   * @param ex
   * @return
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
      ErrorResponse err = new ErrorResponse();
      err.setCode(404);
      err.setMessage(ex.getMessage());
      return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
  }

}
