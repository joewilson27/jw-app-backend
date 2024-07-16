package jw.apps.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tutorials")
public class Tutorial {
  
  @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String title;

	private String description;

	private boolean published;
  
}
