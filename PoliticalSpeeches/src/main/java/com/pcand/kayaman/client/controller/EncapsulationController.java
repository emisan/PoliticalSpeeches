package com.pcand.kayaman.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.pcand.kayaman.service.IPoliticalSpeech;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EncapsulationController {

	@Autowired
	private IPoliticalSpeech politicalSpeech;
}
