package jw.apps.backend.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jw.apps.backend.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
  
  /**
   * Create global handler exception for response message status 400. MAKE SURE return type is SAME as method controller we're using
   * @param ex
   * @return
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex) {
    
    ApiResponse<String> err = new ApiResponse<>();
    err.setCode(400);
    err.setStatus("error");
    err.setMessage(ex.getMessage());
    err.setData(null);

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

  /**
   * Create global handler exception for response message status 404
   * @param ex
   * @return
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {

      ApiResponse<String> err = new ApiResponse<>();
      err.setCode(404);
      err.setStatus("error");
      err.setMessage(ex.getMessage());
      err.setData(null);

      return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
  }

  /**
   * Handling exception request body is empty (directly catch exceptions without using custom exception classes)
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<?>> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {

    ApiResponse<String> err = new ApiResponse<>();
      err.setCode(404);
      err.setStatus("error");
      err.setMessage(ex.getMessage());
      err.setData(null);

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

  // directly catch exceptions without using custom exception classes
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {

    ApiResponse<String> err = new ApiResponse<>();
    err.setCode(404);
    err.setStatus("error");
    err.setMessage(ex.getMessage());
    err.setData(null);

    return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<ApiResponse<?>> handleInternalServerErrorException(InternalServerErrorException ex, HttpServletRequest request) {

    ApiResponse<String> err = new ApiResponse<>();
    err.setCode(500);
    err.setStatus("error");
    err.setMessage(ex.getMessage());
    err.setData(null);

    return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
