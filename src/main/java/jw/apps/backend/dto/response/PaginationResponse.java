package jw.apps.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {

  private int currentPage;

  private int totalPages;

  private int pageSize;

  private long totalItems;
  
}
