package de.cmuellerke.poc.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRequestDTO {

	private String forename;
	
	private String familyname;
	
	private String fullname;
	
	@Min(1)
	private int limit;
}

