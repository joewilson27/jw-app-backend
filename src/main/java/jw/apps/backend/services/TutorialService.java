package jw.apps.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jw.apps.backend.dto.request.TutorialRequest;
import jw.apps.backend.entity.Tutorial;
import jw.apps.backend.helpers.InternalServerErrorException;
import jw.apps.backend.repository.TutorialRepository;
import jakarta.validation.ConstraintViolationException;

@Service
public class TutorialService {
  
  @Autowired
  ValidationService validationService;

  @Autowired
  TutorialRepository tutorialRepository;

  @Transactional
  public Tutorial create(TutorialRequest request) {
    //validationService.validate(request);
    try {
      validationService.validate(request);

      Tutorial add = new Tutorial();
      add.setTitle(request.getTitle());
      add.setDescription(request.getDescription());
      add.setPublished(request.isPublished());

      tutorialRepository.save(add);

      return add;

    } catch (ConstraintViolationException e) {
      System.out.println("up here");
      throw new ConstraintViolationException(e.getMessage(), e.getConstraintViolations());
    } catch (Exception e) {
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

}
