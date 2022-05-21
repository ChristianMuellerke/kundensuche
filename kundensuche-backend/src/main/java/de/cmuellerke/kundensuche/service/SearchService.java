package de.cmuellerke.kundensuche.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import de.cmuellerke.kundensuche.entity.Kunde;
import de.cmuellerke.kundensuche.repository.KundenRepository;

@Service
public class SearchService {

	@Autowired
	KundenRepository kundenRepository;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	Gson gson;

	private static final Logger LOGGER = LoggerFactory.getLogger(KundensucheService.class);

	private static final String KUNDENINDEX = "kundenindex";

	public Page<Kunde> searchByKurzname(String kurzname, Pageable page) {
		LOGGER.debug("Suche nach Kurzname {} Page: {}", kurzname, gson.toJson(page));

		return kundenRepository.findByKurzname(kurzname, page);
	}

	public Page<Kunde> searchByVorname(String vorname, Pageable page) {
		LOGGER.debug("Suche nach Vorname {} Page: {}", vorname, gson.toJson(page));

		return kundenRepository.findByVorname(vorname, page);
	}

	public List<Kunde> fetchSuggestionsByVorname(String vorname) {
		// QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("vorname", vorname +
		// "*");
		MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("vorname", vorname);

		Query searchQuery = new NativeSearchQueryBuilder()//
				.withFilter(queryBuilder)//
				// .withPageable(PageRequest.of(0, 5))//
				.build();

		SearchHits<Kunde> searchSuggestions = elasticsearchOperations.search(searchQuery, Kunde.class,
				IndexCoordinates.of(KUNDENINDEX));

		List<Kunde> suggestions = new ArrayList<Kunde>();

		searchSuggestions.getSearchHits().forEach(searchHit -> {
			suggestions.add(searchHit.getContent());
		});
		return suggestions;
	}

	public List<String> fetchSuggestionsForKurzname(String kurzname) {
		SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest")
				.prefix(kurzname, Fuzziness.AUTO);

		// when
		SearchResponse suggestResponse = ((AbstractElasticsearchTemplate) elasticsearchOperations).suggest(
				new SuggestBuilder().addSuggestion("test-suggest", completionSuggestionFuzzyBuilder),
				IndexCoordinates.of(KUNDENINDEX));

		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("test-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		options.forEach(option -> {
			LOGGER.debug("Vorschlag: " + option.getText());
		});

		return options.stream().map(option -> option.getText().toString()).collect(Collectors.toList());
	}

	public List<String> fetchSuggestionsForKurznameNeu(String kurzname) {
		// TODO funktioniert noch nicht, soll das selbe machen wir die alte Methode
		SuggestionBuilder kurznameSuggestion = new CompletionSuggestionBuilder("suggest")
				.prefix(kurzname, Fuzziness.AUTO)//
				;
		
		SuggestBuilder completionSuggestionBuilder = new SuggestBuilder()//
				.addSuggestion("test_suggest", kurznameSuggestion)//
				;
		
		NativeSearchQuery query = new NativeSearchQueryBuilder()//
			.withSuggestBuilder(completionSuggestionBuilder ) //
			.build();
		
		SearchHits<Kunde> searchHits = elasticsearchOperations.search(query, Kunde.class);
		return searchHits.getSuggest().getSuggestion("test_suggest").getEntries().stream().map(e -> {
			return e.getText();
		}).collect(Collectors.toList());
//		return searchHits.stream().map(sh -> sh.getContent().getKurzname()).collect(Collectors.toList());
	}

	public List<String> sayt(String kurzname) {
		
		Query query = buildMultiMatchQuery(kurzname);
		List<Kunde> kunden = elasticsearchOperations.search(query, Kunde.class) //
				.getSearchHits() //
				.stream() //
				.map(SearchHit::getContent) //
				.collect(Collectors.toList());
		
		LOGGER.debug("{} Kunden gefunden", kunden.size());
		
		kunden.forEach(kunde -> {
			LOGGER.debug(kunde.getKurzname() + "/" + kunde.getKurznameSAYT());
		});
		
		return new ArrayList<>();
	}

	protected Query buildMultiMatchQuery(String text) {
		return new NativeSearchQuery(QueryBuilders.multiMatchQuery(text, //
				"kurznameSAYT", "kurznameSAYT._2gram", "kurznameSAYT._3gram", "kurznameSAYT._4gram").type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
	}

	
}
