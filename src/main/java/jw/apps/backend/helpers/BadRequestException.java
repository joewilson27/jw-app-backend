package jw.apps.backend.helpers;
// create custom reponse error handler to show error message on response
public class BadRequestException extends RuntimeException {
  
  public BadRequestException(String message) {
    super(message);
  }

}
