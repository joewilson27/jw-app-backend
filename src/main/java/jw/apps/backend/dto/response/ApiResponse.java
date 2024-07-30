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
public class ApiResponse<T> {

  private Integer code;

  private String status;

  private String message;

  private T data;
 
  private PaginationResponse pagination;
  
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();
  
}
