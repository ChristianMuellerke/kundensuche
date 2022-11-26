package de.cmuellerke.kundenverwaltung.configuration;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AppConfig {

	@Bean 
	public CommonsRequestLoggingFilter logFilter() {        
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter()
		{
			@Override
			protected void beforeRequest(HttpServletRequest request, String message) {
				logger.info(message);
			}

			@Override
			protected void afterRequest(HttpServletRequest request, String message) {
				logger.info(message);
			}

		};

		filter.setIncludeClientInfo(true);
		filter.setIncludeHeaders(true);
		filter.setIncludePayload(true);
		filter.setIncludeQueryString(true);
		filter.setBeforeMessagePrefix("Request started => ");
		filter.setAfterMessagePrefix("Request ended => ");

		filter.setMaxPayloadLength(10000);
		
		return filter;
	}

}