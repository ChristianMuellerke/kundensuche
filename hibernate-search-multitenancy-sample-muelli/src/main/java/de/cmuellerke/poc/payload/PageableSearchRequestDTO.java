package de.cmuellerke.poc.payload;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageableSearchRequestDTO extends SearchRequestDTO {

	@Min(0)
	private int pageOffset;

	@Builder
	public PageableSearchRequestDTO(String forename, String familyname, String fullname, @Min(1) int limit, @Min(0) int pageOffset) {
		super(forename, familyname, fullname, limit);
		this.pageOffset = pageOffset;
	}
}

