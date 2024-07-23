package jw.apps.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TutorialRequest {
  
  @NotBlank
  private String title;

  @NotBlank
	private String description;

	private boolean published;

}
