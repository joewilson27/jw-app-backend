package jw.apps.backend.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jw.apps.backend.dto.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Create global handler exception for response message status 400. MAKE SURE return type is SAME as method controller we're using
   * @param ex
   * @return
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
    
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

  /**
   * Handling exception request body is empty (directly catch exceptions without using custom exception classes)
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
    ErrorResponse err = new ErrorResponse();
    err.setCode(404);
    err.setMessage("Required request body is missing");

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

  // directly catch exceptions without using custom exception classes
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
    ErrorResponse err = new ErrorResponse();
    err.setCode(404);
    err.setMessage(ex.getMessage());

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

}
