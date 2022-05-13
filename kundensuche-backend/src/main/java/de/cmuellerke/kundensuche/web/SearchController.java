package de.cmuellerke.kundensuche.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundensuche.entity.Kunde;
import de.cmuellerke.kundensuche.service.SearchService;
import de.cmuellerke.kundensuche.web.dto.SearchResponse;

@RestController
public class SearchController {

	@Autowired
	SearchService searchService;

	@GetMapping("/search/kurzname")
	public SearchResponse<Kunde> searchByKurzname(@RequestParam("kurzname") String kurzname, Pageable page) {
		Page<Kunde> pagedKunden = searchService.searchByKurzname(kurzname, page);
		PageMetadata pageMetadata = new PageMetadata(pagedKunden.getSize(), pagedKunden.getNumber(),
				pagedKunden.getTotalElements(), pagedKunden.getTotalPages());
		return new SearchResponse<>(pagedKunden.toList(), pageMetadata);
	}

	@GetMapping("/search/vorname")
	public SearchResponse<Kunde> searchByVorname(@RequestParam("vorname") String vorname, Pageable page) {
		Page<Kunde> pagedKunden = searchService.searchByVorname(vorname, page);
		PageMetadata pageMetadata = new PageMetadata(pagedKunden.getSize(), pagedKunden.getNumber(),
				pagedKunden.getTotalElements(), pagedKunden.getTotalPages());
		return new SearchResponse<>(pagedKunden.toList(), pageMetadata);
	}

	@GetMapping("/search/nachname")
	public SearchResponse<Kunde> searchByNachname(@RequestParam("nachname") String nachname, Pageable page) {
		Page<Kunde> pagedKunden = searchService.searchByVorname(nachname, page);
		PageMetadata pageMetadata = new PageMetadata(pagedKunden.getSize(), pagedKunden.getNumber(),
				pagedKunden.getTotalElements(), pagedKunden.getTotalPages());
		return new SearchResponse<>(pagedKunden.toList(), pageMetadata);
	}

	@GetMapping("/suggestVorname")
	public List<Kunde> suggest(@RequestParam("vorname") String vorname) {
		List<Kunde> suggestions = searchService.fetchSuggestionsByVorname(vorname);
		return suggestions;
	}

	@GetMapping("/suggestKurzname")
	public List<String> suggestKurzname(@RequestParam("kurzname") String kurzname) {
		return searchService.fetchSuggestionsForKurzname(kurzname);
	}

}
