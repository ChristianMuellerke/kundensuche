package de.cmuellerke.kundensuche;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;

@SpringIntegrationTest
@ContextConfiguration(classes = { SuggestTest.Config.class })
public class SuggestTest {

	@Configuration
	@Import({ ElasticsearchRestTemplateConfiguration.class })
	static class Config {
	}

	@Autowired
	private ElasticsearchOperations operations;

	@BeforeEach
	private void setup() {
		IndexInitializer.init(operations.indexOps(CompletionEntity.class));
	}

	@AfterEach
	void after() {
		operations.indexOps(CompletionEntity.class).delete();
	}

	private void loadCompletionObjectEntities() {

		List<IndexQuery> indexQueries = new ArrayList<>();
		indexQueries.add(new CompletionEntityBuilder("1").name("Rizwan Idrees")
				.suggest(new String[] { "Rizwan Idrees" }).buildIndex());
		indexQueries.add(new CompletionEntityBuilder("2").name("Franck Marchand")
				.suggest(new String[] { "Franck", "Marchand" }).buildIndex());
		indexQueries.add(new CompletionEntityBuilder("3").name("Mohsin Husen")
				.suggest(new String[] { "Mohsin", "Husen" }).buildIndex());
		indexQueries.add(new CompletionEntityBuilder("4").name("Artur Konczak")
				.suggest(new String[] { "Artur", "Konczak" }).buildIndex());

		IndexCoordinates index = IndexCoordinates.of("test-index-core-completion");
		operations.bulkIndex(indexQueries, index);
		operations.indexOps(CompletionEntity.class).refresh();
	}

	@Test
	public void shouldFindSuggestionsForGivenCriteriaQueryUsingCompletionEntity() {

		// given
		loadCompletionObjectEntities();

		SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest").prefix("m",
				Fuzziness.AUTO);

		// when
		SearchResponse suggestResponse = ((AbstractElasticsearchTemplate) operations).suggest(
				new SuggestBuilder().addSuggestion("test-suggest", completionSuggestionFuzzyBuilder),
				IndexCoordinates.of("test-index-core-completion"));
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("test-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();

		// then
		assertThat(options).hasSize(2);
		assertThat(options.get(0).getText().string()).isIn("Marchand", "Mohsin");
		assertThat(options.get(1).getText().string()).isIn("Marchand", "Mohsin");
	}

	/**
	 * @author Mewes Kochheim
	 */
	static class CompletionEntityBuilder {

		private CompletionEntity result;

		public CompletionEntityBuilder(String id) {
			result = new CompletionEntity(id);
		}

		public CompletionEntityBuilder name(String name) {
			result.setName(name);
			return this;
		}

		public CompletionEntityBuilder suggest(String[] input) {
			return suggest(input, null);
		}

		public CompletionEntityBuilder suggest(String[] input, Integer weight) {
			Completion suggest = new Completion(input);
			suggest.setWeight(weight);

			result.setSuggest(suggest);
			return this;
		}

		public CompletionEntity build() {
			return result;
		}

		public IndexQuery buildIndex() {
			IndexQuery indexQuery = new IndexQuery();
			indexQuery.setId(result.getId());
			indexQuery.setObject(result);
			return indexQuery;
		}
	}

	/**
	 * @author Mewes Kochheim
	 */
	@Document(indexName = "test-index-core-completion")
	static class CompletionEntity {

		@Nullable
		@Id
		private String id;

		@Nullable
		private String name;

		@Nullable
		@CompletionField(maxInputLength = 100)
		private Completion suggest;

		private CompletionEntity() {
		}

		public CompletionEntity(String id) {
			this.id = id;
		}

		@Nullable
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Nullable
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Nullable
		public Completion getSuggest() {
			return suggest;
		}

		public void setSuggest(Completion suggest) {
			this.suggest = suggest;
		}
	}

}
