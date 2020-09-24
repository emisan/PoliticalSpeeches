package com.pcand.kayaman.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pcand.kayaman.http.server.CSVResourceServer;
import com.pcand.kayaman.http.server.InternalResourceServer;

@Configuration
public class InternalHttpServerConfig {

	@Bean
	public CSVResourceServer csvResourceServer() {
		return new CSVResourceServer();
	}
	
	@Bean
	public InternalResourceServer internalResourceService() {
		return new InternalResourceServer();
	}
}
