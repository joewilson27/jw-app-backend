package jw.apps.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import jakarta.transaction.Transactional;
import jw.apps.backend.dto.request.TutorialRequest;
import jw.apps.backend.entity.Tutorial;
import jw.apps.backend.helpers.InternalServerErrorException;
import jw.apps.backend.helpers.ResourceNotFoundException;
import jw.apps.backend.repository.TutorialRepository;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TutorialService {
  
  @Autowired
  ValidationService validationService;

  @Autowired
  TutorialRepository tutorialRepository;

  Logger logger = LoggerFactory.getLogger(TutorialService.class);

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

  public List<Tutorial> getAllTutorials(String title) {
    try {
      List<Tutorial> tutorials = new ArrayList<>();

      if (title == null)
        tutorialRepository.findAll().forEach(tutorials::add);
      else
        tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

      return tutorials;

    } catch (Exception e) {
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

  public Tutorial details(long id) {
    try {
      Tutorial data = tutorialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Data not found!"));

      return data;
      
    } catch (ResourceNotFoundException e) {
      throw new ResourceNotFoundException("Data not found!");
    } catch (Exception e) {
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

  @Transactional
  public Tutorial update(long id, TutorialRequest request) {
    try {

      validationService.validate(request);

      Tutorial data = tutorialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Data not found!"));

      data.setTitle(request.getTitle());
      data.setDescription(request.getDescription());
      data.setPublished(request.isPublished());

      tutorialRepository.save(data);

      return data;

    } catch (ResourceNotFoundException e) {
      throw new ResourceNotFoundException("Data not found!");
    } catch (ConstraintViolationException e) {
      throw new ConstraintViolationException(e.getMessage(), e.getConstraintViolations());
    } catch (Exception e) {
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc"))  {
      return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
  }

  public Map<String, Object> getAllTutorialsPage(String title, int page, int size, String[] sort) {
    try {
      List<Order> orders = new ArrayList<Order>();

      if (sort[0].contains(",")) {
        // will sort more than 2 fields
        // sortOrder="field, direction"
        for (String sortOrder: sort) {
          String[] _sort = sortOrder.split(",");
          orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
      } else {
        // sort=[field, direction]
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }

      List<Tutorial> tutorials = new ArrayList<Tutorial>();
      Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

      Page<Tutorial> pageTuts;
      if (title == null)
        pageTuts = tutorialRepository.findAll(pagingSort);
      else
        pageTuts = tutorialRepository.findByTitleContaining(title, pagingSort);

      tutorials = pageTuts.getContent();
      
      if (tutorials.isEmpty()) {
        return null; 
      }

      Map<String, Object> response = new HashMap<>();
      response.put("tutorials", tutorials);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return response;
    } catch (Exception e) {
      logger.error("An Error Message from getAllTutorialsPage ", e);
      throw new InternalServerErrorException("Internal Server Error");
    }
  }

}
