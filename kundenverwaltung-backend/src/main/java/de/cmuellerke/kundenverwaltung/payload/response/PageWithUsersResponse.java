package de.cmuellerke.kundenverwaltung.payload.response;

import java.util.List;

import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageWithUsersResponse {
	private List<UserDTO> users;
	private int currentPage;
	private long totalItems;
	private int totalPages;
}
