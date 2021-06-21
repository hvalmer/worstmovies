package com.texoit.worstmovie.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.worstmovie.domain.Films;
import com.texoit.worstmovie.service.FilmsService;

@RestController
@RequestMapping(value = "/api/texoit/v1/films", produces = "application/json")
public class FilmsController {

	static Logger LOGGER = Logger.getLogger(FilmsController.class);
	
	@Autowired
	private FilmsService filmsService;
	
	@GetMapping
	public HttpEntity all() {
		List<Films> films = filmsService.findAll();
		if(films != null) {
			return ResponseEntity.ok(films);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping(value = "/find/winners/interval/min/max")
	public HttpEntity findWinnersIntervalMinMax() {
		ProducerResource producers = filmsService.findWinnersIntervalMinMax();
		if(producers != null) {
			return ResponseEntity.ok(producers);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
}
