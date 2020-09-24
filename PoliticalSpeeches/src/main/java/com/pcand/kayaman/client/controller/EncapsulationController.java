package com.pcand.kayaman.client.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pcand.kayaman.http.server.InternalResourceServer;
import com.pcand.kayaman.model.PoliticalSpeech;
import com.pcand.kayaman.service.IPoliticalSpeechQueries;

import lombok.extern.slf4j.Slf4j;

/**
 * REST-Controller class to serve a Service-Endpoint for a REST call for encapsulation-request from a client-application
 * <br>
 * available Service-Endpoints:
 * <blockquote>
 * 	<ul>
 * 		<li>{@link EncapsulationController#getEncapsulation(String, String)}}</li>
 *  </ul>
 * </blockquote>
 * 
 * @author Hayri Emrah Kayaman
 */
@RestController
@Slf4j
public class EncapsulationController {
	
	@Autowired
	private IPoliticalSpeechQueries politicalSpeechQueries;
	
	@Autowired
	private InternalResourceServer internalResourceServer;
	
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
	
//	private static final String LEAST_WORDY = "leastWordy";
//	private static final String MOST_SECURITY = "mostSecurity";
//	private static final String MOST_SPEECHES = "mostSpeeches";
//	
//	@GetMapping(value = "/encapsulation")
//	public Map<String, String> getEncapsulation(@RequestParam(name="url1") String url1, @RequestParam(name="url2") String url2) {
//	
//		Assert.notNull(url1, "The url1 for any requested resource must not be null!");
//		Assert.notNull(url2, "The url2 for any requested resource must not be null!");
//		Assert.notNull(politicalSpeeches, "The political speeches must be autwired and can not be null!");
//	
//		log.info("requested urls : url1 = {}, url2 = {}", url1, url2);
//		
//		List<PoliticalSpeech> speeches = politicalSpeeches.getAllPoliticalSpeeches();
//		
//		Map<String, String> response = new HashMap<>();
//		
//		setEncapsulationEntry(response, MOST_SPEECHES, speeches);
//		setEncapsulationEntry(response, MOST_SECURITY, speeches);
//		setEncapsulationEntry(response, LEAST_WORDY, speeches);
//		
//		return response;
//	}
//	private void setEncapsulationEntry(Map<String, String> map, String encapsulationTopic, List<PoliticalSpeech> speeches) {
//		
//		Assert.notNull(map, "The map for setting requests must not be null!");
//		Assert.notNull(encapsulationTopic, "The encapsulation topic must not be null!");
//		Assert.notNull(speeches, "The list of political speeches must not be null!");
//		
//		PoliticalSpeech speech = null;
//		String value;
//		
//		switch(encapsulationTopic) {
//			case MOST_SPEECHES:
//				speech = politicalSpeeches.getMostSpeechesSpokenSpeech(speeches);
//				value = speech.getSpeaker();
//				map.put(MOST_SPEECHES, value);
//				break;
//			case MOST_SECURITY:
//				speech = politicalSpeeches.getMostSecuritySpokenSpeech(speeches);
//				value = speech.getSpeaker();
//				map.put(MOST_SECURITY, value);
//				break;
//			case LEAST_WORDY :
//				speech = politicalSpeeches.getLeastWordySpokenSpeech(speeches);
//				value = speech.getSpeaker();
//				map.put(LEAST_WORDY, value);
//				break;
//			default:
//				break;
//		}
//	}
	
	// usage of response with Jackson data-binding ObjectMapper and JSONObjects and StringWriter
	
	/**
	 * Returns a JSON String-presentation of requested data which rely on CSV-Files where those are reachable over 2 given URL's.
	 * <br><br>
	 * The CSV-Files represent political speech evaluation data, where we can see who the SPEAKER, what the TOPIC, on which DATE and how many WORDS 
	 * were spoken in a political speech.
	 * <br><br>
	 * The requested data shows reflects to following business questions/tasks:
	 * <blockquote>
	 * 	<ul>
	 * 		<li>Which speaker name spoke the most political speeches?</li>
	 * 		<li>Which speaker name spoke the most security related speeches?</li>
	 * 		<li>Which speaker name spoke the least words in a political speech(es)?</li>
	 *	</ul>
	 *</blockquote>
	 * <br>
	 * An outcome or response is shown in JSON-notation as followed:
	 * <blockquote>
	 * {
	 * <ul>
	 * 		<li>&quot;mostSpeeches&quot;:&quot;Adriano Celentano&quot;</li>
	 * 		<li>&quot;mostSecurity&quot;:&quot;Bill Gates&quot;</li>
	 * 		<li>&quot;leastWordy&quot;:&quot;Donald Trump&quot;</li>
	 *	</ul>
	 * }
	 * <br>
	 * another could be regarding to the content of speeches (CSV-File content)
	 * <br>
	 * {
	 * <ul>
	 * 		<li>&quot;mostSpeeches&quot;:&quot;null&quot; (nobody did)</li>
	 * 		<li>&quot;mostSecurity&quot;:&quot;Alexander Abel&quot;</li>
	 * 		<li>&quot;leastWordy&quot;:&quot;Caesare Collins&quot;</li>
	 *	</ul>
	 * }
	 * </blockquote>
	 * 
	 * @param url1 a URL pointing t a CSV-File resource including political speech data
	 * @param url2 a URL pointing t a CSV-File resource including political speech data
	 * @return given outcome as described above as possible JSON-formatted response/result
	 * 		   of given business questions or tasks
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws JSONException 
	 */
	@GetMapping(value = "/encapsulation")
	public String getEncapsulation(@RequestParam(name="url1") String url1, @RequestParam(name="url2") String url2) throws IOException {	
		
		Assert.notNull(url1, "The url1 for any requested resource must not be null!");
		Assert.notNull(url2, "The url2 for any requested resource must not be null!");
		Assert.notNull(politicalSpeechQueries, "The political speeches service must be autowired and can not be null!");
		
		log.info("requested urls : url1 = {}, url2 = {}", url1, url2);
		
		List<PoliticalSpeech> speeches = new ArrayList<>();
		
		populateSpeechesFromResourceUrl(speeches, url1);
		
		populateSpeechesFromResourceUrl(speeches, url2);
		
		Assert.notNull(speeches, "List of political speeches must not be null!");
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		JsonFactory factory = new JsonFactory();
		
		//Sonarqube rule prevention : Resources should be closed (java:S2095)
		try (JsonGenerator generator = factory.createGenerator(stream, JsonEncoding.UTF8)) {
			
			generator.writeStartObject();
			
			generator.writeStringField("mostSpeeches", politicalSpeechQueries.getMostSpeechesSpokenSpeech(speeches).getSpeaker());
			
			generator.writeStringField("mostSecurity", politicalSpeechQueries.getMostSecuritySpokenSpeech(speeches).getSpeaker());
			
			generator.writeStringField("leastWordy", politicalSpeechQueries.getLeastWordySpokenSpeech(speeches).getSpeaker());
			
			generator.writeEndObject();
		}
		
		return stream.toString();
	}

	/*
	 * Helper to populate a list with more political speeches from a given CSV-File at the end of a URI/URL
	 */
	private void populateSpeechesFromResourceUrl(List<PoliticalSpeech> speeches, String resourceUrl) {
		Assert.notNull(speeches, "List of political speeches must not be null!");
		Assert.notNull(resourceUrl, "Given URL to a resource must not be null!");
		
		Resource resource = internalResourceServer.getResourceFromURL(resourceUrl);
		
		Assert.notNull(resource, "The URL must provide an available Resource and must not be null!");
		List<PoliticalSpeech> otherSpeeches = politicalSpeechQueries.getAllPoliticalSpeeches(resourceUrl);
		if(!otherSpeeches.isEmpty()) {
			speeches.addAll(otherSpeeches);
		}
	}
}
