package com.pcand.kayaman.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pcand.kayaman.service.IPoliticalSpeech;
import com.pcand.kayaman.service.PoliticalSpeechService;

@Configuration
public class EncapsulationConfig {
	
	@Bean
	public IPoliticalSpeech politicalSpeeches() {
		return new PoliticalSpeechService();
	}
}
