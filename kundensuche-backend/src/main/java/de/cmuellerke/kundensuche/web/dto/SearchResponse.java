package de.cmuellerke.kundensuche.web.dto;

import java.util.List;

import org.springframework.hateoas.PagedModel.PageMetadata;

public class SearchResponse<T> {

	private List<T> result;

	private PageMetadata page;

	public SearchResponse() {
	}

	public SearchResponse(List<T> result, PageMetadata page) {
		this.result = result;
		this.page = page;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public PageMetadata getPage() {
		return page;
	}

	public void setPage(PageMetadata page) {
		this.page = page;
	}
}
