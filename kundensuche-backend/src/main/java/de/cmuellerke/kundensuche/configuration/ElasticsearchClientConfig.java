package de.cmuellerke.kundensuche.configuration;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories(basePackages = "de.cmuellerke.kundensuche.repository")
@ComponentScan(basePackages = { "de.cmuellerke.kundensuche" })
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		final ClientConfiguration clientConfiguration = ClientConfiguration//
				.builder()//
				.connectedTo("localhost:9200")//
				.build();

		RestClient restClient = RestClient
				.builder(HttpHost.create("localhost:9200"))
				.setDefaultHeaders(new Header[]{
						new BasicHeader("Authorization", "ApiKey " + apiKey)
				})
				.build();

		ElasticsearchTransport transport = new RestClientTransport(
				restClient, new JacksonJsonpMapper());

		// And create the API client
		return new ElasticsearchClient(transport);
	}
}