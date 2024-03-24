package de.cmuellerke.poc.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class TenantDTO {
	@NonNull
	@NotBlank
    String id;
	
	@NonNull
	@NotBlank
    String name;
}
