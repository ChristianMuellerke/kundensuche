package de.cmuellerke.poc.configuration;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

public class MyElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

	@Override
	public void configure(ElasticsearchAnalysisConfigurationContext context) {
		context.tokenizer("myTokenizer")
			.type( "ngram" )
			.param( "max_gram", "3")
			.param( "min_gram", "3"); 

        context.tokenFilter( "snowball_german" ) 
        	.type( "snowball" )
        	.param( "language", "German" ); 

        context.normalizer( "lowercase" )
        	.custom() 
        	.tokenFilters( "lowercase", "asciifolding" );
		
		context.analyzer( "names_german" )
        	.custom()
        	.tokenizer( "myTokenizer" )
        	.tokenFilters( "lowercase", "snowball_german", "asciifolding" );
        	; 
	}

}
