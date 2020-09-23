package com.pcand.kayaman.model;

import java.util.Date;

import lombok.Data;

@Data
public class PoliticalSpeech {
	
	private String speaker;
	private String topic;
	private Date speechDate;
	private Integer spokenWordsAmount;
}
