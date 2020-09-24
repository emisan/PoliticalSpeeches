package com.pcand.kayaman.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.opencsv.CSVReader;
import com.pcand.kayaman.model.PoliticalSpeech;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PoliticalSpeechService implements IPoliticalSpeechQueries {

	private static final String THE_LIST_OF_POLITICAL_SPEECHES_MUST_NOT_BE_NULL = "The List of political speeches must not be null";

	private static final String SECURITY_WORD = "sicherheit";
	
	private static final int SPEACKER_COLMUN_INDEX = 0;
	private static final int TOPIC_COLUMN_INDEX = 1;
	private static final int SPEAK_DATE_COLUMN_INDEX = 2;
	private static final int WORDS_SPOKEN_AMOUNT_INDEX = 3;
	
	// only needed for dev/test if we want access resource from classpath
//	private static final String CSV_FILE_PATH = "src/main/resources/politicalSpeeches.csv";
	
	
	private List<PoliticalSpeech> politicalSpeeches;
	
	public PoliticalSpeechService() {
		politicalSpeeches = new ArrayList<>();
	}
	
	@Override
	public List<PoliticalSpeech> getAllPoliticalSpeeches(String fromURL) 
	{
		// need when we want to read from CSV_FILE_PATH for dev/test purpose
//		try(FileInputStream fis = new FileInputStream(new File(fromURL))) { 
		
		// in production we want to read from a HTTP-URI or URL 
		//
		try {
			URL url = new URL(fromURL);
			
			URLConnection connection = url.openConnection();
					
			try(CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(((URLConnection)connection).getInputStream()))))
			{	
				String[] nextLine = reader.readNext(); // skip header
				
				while((nextLine = reader.readNext()) != null)
				{	
					PoliticalSpeech speech = new PoliticalSpeech();
					
					speech.setSpeaker(nextLine[SPEACKER_COLMUN_INDEX].trim());
					speech.setTopic(nextLine[TOPIC_COLUMN_INDEX].trim());
					speech.setSpeechDate(Date.valueOf(nextLine[SPEAK_DATE_COLUMN_INDEX].trim()));
					speech.setSpokenWordsAmount(Integer.parseInt(nextLine[WORDS_SPOKEN_AMOUNT_INDEX].trim()));
					
					log.info("added " + speech.toString());
					politicalSpeeches.add(speech);
				}
			}
		}
		catch(IOException exec) {
			log.error(exec.getMessage());
		}
		return politicalSpeeches;
	}

	@Override
	public PoliticalSpeech getMostSpeechesSpokenSpeech(List<PoliticalSpeech> fromSpeeches) {
		
		Assert.notNull(fromSpeeches, THE_LIST_OF_POLITICAL_SPEECHES_MUST_NOT_BE_NULL);
		
		return fromSpeeches
			   .stream()
			   .filter(speech -> LocalDate.parse(speech.getSpeechDate().toString()).getDayOfYear() == 2013)
			   .max(Comparator.comparing(PoliticalSpeech::getSpokenWordsAmount))
			   .orElseGet(PoliticalSpeech::new);
	}

	@Override
	public PoliticalSpeech getMostSecuritySpokenSpeech(List<PoliticalSpeech> fromSpeeches) {
		
		Assert.notNull(fromSpeeches, THE_LIST_OF_POLITICAL_SPEECHES_MUST_NOT_BE_NULL);
		
		return fromSpeeches
			   .stream()
			   .filter(speech -> speech.getTopic().toLowerCase().contains(SECURITY_WORD))
			   .max(Comparator.comparing(PoliticalSpeech::getSpokenWordsAmount))
			   .orElseGet(PoliticalSpeech::new);
	}

	@Override
	public PoliticalSpeech getLeastWordySpokenSpeech(List<PoliticalSpeech> fromSpeeches) {
		
		Assert.notNull(fromSpeeches, THE_LIST_OF_POLITICAL_SPEECHES_MUST_NOT_BE_NULL);
		
		Map<String, List<PoliticalSpeech>> speechesSortedAndComparedBySpeaker = 
											fromSpeeches
										   .stream()
										   .sorted(Comparator.comparing(PoliticalSpeech::getSpeaker))
										   .collect(Collectors.groupingBy(PoliticalSpeech::getSpeaker, Collectors.toList()));
		
		speechesSortedAndComparedBySpeaker.forEach((speaker, speeches) -> {
			
			politicalSpeeches.add(speeches.stream().max(Comparator.comparing(PoliticalSpeech::getSpokenWordsAmount)).get());
		});
		
		return politicalSpeeches.stream().min(Comparator.comparing(PoliticalSpeech::getSpokenWordsAmount)).orElseGet(PoliticalSpeech::new);
	}
}
