package de.cmuellerke.poc.tenancy;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Tenant {
	@NonNull
	private String id;
}
