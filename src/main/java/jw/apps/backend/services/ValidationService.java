package jw.apps.backend.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
// import jakarta.validation.ConstraintViolationException;

import jakarta.validation.Validator;
import jw.apps.backend.helpers.BadRequestException;



@Service
public class ValidationService {
  
  @Autowired
  private Validator validator;

  public void validate(Object request) {

    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);

    if (!constraintViolations.isEmpty()) {
      String errorMessages = constraintViolations.stream()
                    .map(violation -> "Field '" + violation.getPropertyPath() + "': " + violation.getMessage())
                    .collect(Collectors.joining(", "));
      
      throw new BadRequestException(errorMessages);
      
      //throw new ConstraintViolationException(constraintViolations);
    }

  }

}
