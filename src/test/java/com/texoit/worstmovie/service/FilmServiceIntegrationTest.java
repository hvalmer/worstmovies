package com.texoit.worstmovie.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.texoit.worstmovie.WorstmovieApplication;
import com.texoit.worstmovie.domain.Films;
import com.texoit.worstmovie.dto.FilmsCsvDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorstmovieApplication.class)
@Transactional
public class FilmServiceIntegrationTest {

	@Autowired
	FilmsService filmsService;
	
	private List<FilmsCsvDTO> dtos = new ArrayList<>();
	
	@Before
	public void init() {
		this.dtos = filmsService.readCsvFile();
	}
	
	@Test
	public void testSizeOfImportedData() {
		List<Films> filmsFromDataBase = filmsService.findAll();
		Assertions.assertThat(filmsFromDataBase.size()).isEqualTo(dtos.size());
	}
	
	@Test
	public void testSomeImportedData() {
		List<Films> filmsFromDataBase = filmsService.findAll();
		int index = 0;
		if(filmsFromDataBase != null && filmsFromDataBase.size() > 0)
			index = filmsFromDataBase.size() - 1;
		
		Assertions.assertThat(filmsFromDataBase.get(index).getProducers()).isEqualTo(dtos.get(index).getProducers());
		Assertions.assertThat(filmsFromDataBase.get(index).getYear()).isEqualTo(dtos.get(index).getYear());
	}
}
