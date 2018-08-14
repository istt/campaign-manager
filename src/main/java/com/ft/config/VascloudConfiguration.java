package com.ft.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.ft.service.util.HeaderRequestInterceptor;


@Configuration
public class VascloudConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate result = new RestTemplate();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
		result.getInterceptors().add(new HeaderRequestInterceptor(headers));
		return result;
	}
}
