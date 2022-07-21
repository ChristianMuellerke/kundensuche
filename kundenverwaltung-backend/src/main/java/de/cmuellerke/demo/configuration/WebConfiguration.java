package de.cmuellerke.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.cmuellerke.demo.tenancy.TenantInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	private final TenantInterceptor tenantInterceptor;

	@Autowired
	public WebConfiguration(TenantInterceptor tenantInterceptor) {
		this.tenantInterceptor = tenantInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry//
			.addWebRequestInterceptor(tenantInterceptor)//
			.addPathPatterns("/**")//
			.excludePathPatterns("/**/authenticate/**", "/**/error/**");
	}
}
