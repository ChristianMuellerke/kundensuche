package de.cmuellerke.poc.controller;

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.cmuellerke.poc.payload.PageDTO;
import de.cmuellerke.poc.payload.PageableSearchRequestDTO;
import de.cmuellerke.poc.service.CustomerSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/customer")
@Slf4j
@AllArgsConstructor
public class SearchController {

	private final CustomerSearchService customerSearchService;
	
	@GetMapping("/search/page")
	public PageDTO<CustomerDTO> searchCustomerPage(PageableSearchRequestDTO pageableSearchRequestDTO) {
		log.info("searching...");
		return customerSearchService.findBy(pageableSearchRequestDTO);
	}
}
