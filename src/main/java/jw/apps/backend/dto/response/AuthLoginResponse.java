package jw.apps.backend.dto.response;

import lombok.Data;

@Data
public class AuthLoginResponse {
  
  private String accessToken;

  private String tokenType = "Bearer ";

}
