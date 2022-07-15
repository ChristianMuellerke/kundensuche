package de.cmuellerke.demo.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantDTO {
	long id;

	@NotBlank(message = "Name muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Name muss zwischen 2 und 100 Zeichen lang sein")
	String name;

}
