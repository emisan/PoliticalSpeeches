package com.pcand.kayaman.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcand.kayaman.model.PoliticalSpeech;
import com.pcand.kayaman.service.IPoliticalSpeechQueries;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EncapsulationController {

	@Autowired
	private IPoliticalSpeechQueries politicalSpeech;
	
	/*
	 *  FOR INTERNAL INFO - REMOVE THIS COMMENT AND NOT USED METHODS IN PRODUCTION ENVIRONMENT
	 *  
	 *  use this commented lines of code in order to response a java.util.Map instead of an JSONObject String-presentation
	 *  
	 *  this is more foodwork, as we can use com.fasterxml.jackson-databind library to shorten the procedures
	 *  for the desired business needs
	 *  
	 *  the counterside of using Jackson, is that we stick to org.json.JSONObject with ObjectMapper and StringWriter 
	 *  to write the outcome as String as response
	 *  
	 *  therefore NULL-Exception handling and keys in JSONObject which value is NULL, will not be visible in the client/browser
	 *  
	 *  using the above implementation of getEncapsulation will give the desired outcome/response format
	 *  
	 *  {
	 *  	"mostSpeeches": "<SPEAKER_NAME>|<NULL>"
	 *  	"mostSecurity": "<SPEAKER_NAME>|<NULL>"
	 *  	"leastWordy"  : "<SPEAKER_NAME>|<NULL>"
	 *  }
	 *  
	 *  therefore this is the preferred solution, otherwise use commented method getEncapsulation above and uncomment these lines again
	 */
	
	private static final String LEAST_WORDY = "leastWordy";
	private static final String MOST_SECURITY = "mostSecurity";
	private static final String MOST_SPEECHES = "mostSpeeches";
	
	@GetMapping(value = "/encapsulation")
	public Map<String, String> getEncapsulation(@RequestParam(name="url1") String url1, @RequestParam(name="url2") String url2) {
	
		Assert.notNull(url1, "The url1 for any requested resource must not be null!");
		Assert.notNull(url2, "The url2 for any requested resource must not be null!");
		Assert.notNull(politicalSpeech, "The political speeches must be autwired and can not be null!");
	
		log.info("requested urls : url1 = {}, url2 = {}", url1, url2);
		
		List<PoliticalSpeech> speeches = politicalSpeech.getAllPoliticalSpeeches(url1);
		
		Map<String, String> response = new HashMap<>();
		
		setEncapsulationEntry(response, MOST_SPEECHES, speeches);
		setEncapsulationEntry(response, MOST_SECURITY, speeches);
		setEncapsulationEntry(response, LEAST_WORDY, speeches);
		
		return response;
	}
	private void setEncapsulationEntry(Map<String, String> map, String encapsulationTopic, List<PoliticalSpeech> speeches) {
		
		Assert.notNull(map, "The map for setting requests must not be null!");
		Assert.notNull(encapsulationTopic, "The encapsulation topic must not be null!");
		Assert.notNull(speeches, "The list of political speeches must not be null!");
		
		PoliticalSpeech speech = null;
		String value;
		
		switch(encapsulationTopic) {
			case MOST_SPEECHES:
				speech = politicalSpeech.getMostSpeechesSpokenSpeech(speeches);
				value = speech.getSpeaker();
				map.put(MOST_SPEECHES, value);
				break;
			case MOST_SECURITY:
				speech = politicalSpeech.getMostSecuritySpokenSpeech(speeches);
				value = speech.getSpeaker();
				map.put(MOST_SECURITY, value);
				break;
			case LEAST_WORDY :
				speech = politicalSpeech.getLeastWordySpokenSpeech(speeches);
				value = speech.getSpeaker();
				map.put(LEAST_WORDY, value);
				break;
			default:
				break;
		}
	}
}
