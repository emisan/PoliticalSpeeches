package com.pcand.kayaman.service;

import java.util.List;

import com.pcand.kayaman.model.PoliticalSpeech;

public interface IPoliticalSpeech {
	List<PoliticalSpeech> getAllPoliticalSpeeches(String fromURL);
	PoliticalSpeech getMostSpeechesSpokenSpeech(List<PoliticalSpeech> fromSpeeches);
	PoliticalSpeech getMostSecuritySpokenSpeech(List<PoliticalSpeech> fromSpeeches);
	PoliticalSpeech getLeastWordySpokenSpeech(List<PoliticalSpeech> fromSpeeches);
}
