package jw.apps.backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {

  private String status;

  private String message;

  private T data;

  private PaginationDto pagination;
  
  private ErrorDto error;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();
  

}
