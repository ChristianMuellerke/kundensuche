package de.cmuellerke.kundensuche;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.cmuellerke.kundensuche.service.KundensucheService;
import de.cmuellerke.kundensuche.testdaten.TestdatenProvider;

@SpringBootApplication
public class KundensucheBackend implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(KundensucheBackend.class);

	@Autowired
	KundensucheService kundensucheService;

	@Autowired
	TestdatenProvider testdatenProvider;

	public static void main(String[] args) {
		SpringApplication.run(KundensucheBackend.class, args);
	}

	@Bean
	public boolean createTestIndex(RestHighLevelClient restHighLevelClient) throws Exception {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("hello-world");
			restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT); // 1
		} catch (Exception ignored) {
		}

		CreateIndexRequest createIndexRequest = new CreateIndexRequest("hello-world");
		createIndexRequest
				.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
		restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT); // 2

		return true;
	}

	@Override
	public void run(String... args) throws Exception {
		kundensucheService.deleteAll();
		kundensucheService.saveToIndex(testdatenProvider.createTestkunden());

	}

}
