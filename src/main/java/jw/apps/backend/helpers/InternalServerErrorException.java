package jw.apps.backend.helpers;

public class InternalServerErrorException extends RuntimeException {
  
  public InternalServerErrorException(String message) {
    super(message);
  }

}
