package de.cmuellerke.kundensuche.web.dto;

import org.springframework.data.domain.Pageable;

public class SearchRequest {

	public final String keyword;
	public final Pageable page;

	public SearchRequest(String keyword, Pageable page) {
		this.keyword = keyword;
		this.page = page;
	}
}