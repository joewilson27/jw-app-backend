package jw.apps.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequest {
  
  @NotBlank
  @Size(max = 100)
  private String username;

  @NotBlank
  @Size(max = 100)
  private String password;

}
