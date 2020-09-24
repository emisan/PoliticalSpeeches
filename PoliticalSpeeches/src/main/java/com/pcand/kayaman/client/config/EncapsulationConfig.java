package com.pcand.kayaman.client.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import com.pcand.kayaman.http.config.InternalHttpServerConfig;
import com.pcand.kayaman.http.server.InternalResourceServer;
import com.pcand.kayaman.service.IPoliticalSpeech;
import com.pcand.kayaman.service.PoliticalSpeechService;

@Configuration
@EnableAutoConfiguration
@ContextConfiguration(classes = {InternalHttpServerConfig.class})
public class EncapsulationConfig {
	
	@Bean
	public InternalResourceServer internalResourceServer() {
		return new InternalResourceServer();
	}
	
	@Bean
	public IPoliticalSpeech politicalSpeeches() {
		return new PoliticalSpeechService();
	}
}
