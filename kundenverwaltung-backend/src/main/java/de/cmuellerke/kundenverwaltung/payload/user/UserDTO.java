package de.cmuellerke.kundenverwaltung.payload.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	String id;
	String tenantId;
	String username;
	String email;
	LocalDateTime createdAt;
	LocalDateTime lastModifiedAt;
}
