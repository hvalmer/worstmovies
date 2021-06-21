package com.texoit.worstmovie.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.texoit.worstmovie.domain.Films;
import com.texoit.worstmovie.dto.FilmsCsvDTO;
import com.texoit.worstmovie.dto.ProducerDTO;
import com.texoit.worstmovie.repository.FilmsRepository;
import com.texoit.worstmovie.rest.ProducerResource;

@Service
public class FilmsService {

	static Logger LOGGER = Logger.getLogger(FilmsService.class);

	public static final String CSV_FILE = "movielist.csv";

	@Autowired
	private FilmsRepository filmsRepository;

	public void importFilmsCSV() {
		filmsRepository.saveAll(parseCsvDtoFileToFilm());
	}

	public List<FilmsCsvDTO> readCsvFile() {
		try {
			Resource resource = new ClassPathResource(CSV_FILE);
			return new CsvToBeanBuilder<FilmsCsvDTO>(new FileReader(resource.getFile())).withType(FilmsCsvDTO.class)
					.withIgnoreLeadingWhiteSpace(true).withSeparator(';').build().parse();
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}
		return null;
	}

	private List<Films> parseCsvDtoFileToFilm() {
		final List<FilmsCsvDTO> csvDto = readCsvFile();
		List<Films> films = new ArrayList<>();
		if (csvDto != null) {
			csvDto.stream().forEach((dto) -> films.add(Films.builder().year(dto.getYear()).title(dto.getTitle())
					.studios(dto.getStudios()).producers(dto.getProducers()).theWinner(dto.theWinner()).build()));
		}
		return films;
	}

	public List<Films> findAll() {
		return filmsRepository.findAll();
	}

	public ProducerResource findWinnersIntervalMinMax() {
		List<Films> winnersList = filmsRepository.findAllByTheWinnerOrderByYearAsc(true);
		Map<String, List<Integer>> producerMapGrouped = new HashMap<>();

		// percorrendo os filmes ganhadores
		for (Films films : winnersList) {

			// verifica se há mais de um produtor de filme
			String[] splittedProducers = films.getProducers().split(",\\s*|\\band\\bs");

			// percorrendo os produtores e separar eles com o ano em que ganharam o prêmio
			for (String producer : splittedProducers) {

				// se já existe adicionado na lista de anos vencedores do produtor, caso não,
				// apenas cria a lista de anos para o produtor
				if (producerMapGrouped.containsKey(producer.trim())) {
					List<Integer> yearValueList = producerMapGrouped.get(producer.trim());
					yearValueList.add(films.getYear());
					producerMapGrouped.put(producer.trim(), yearValueList);
				} else {
					List<Integer> yearList = new ArrayList<>();
					yearList.add(films.getYear());
					producerMapGrouped.put(producer.trim(), yearList);
				}
			}
		}

		ProducerResource resourceProducer = new ProducerResource();
		resourceProducer.setMin(retrieveCalculatedDataProducer(producerMapGrouped, false));
		resourceProducer.setMin(retrieveCalculatedDataProducer(producerMapGrouped, true));

		return resourceProducer;
	}

	private List<ProducerDTO> retrieveCalculatedDataProducer(Map<String, List<Integer>> producerMapGrouped,
			Boolean isMax) {
		int interval = 1;
		List<ProducerDTO> producerDtoList = new ArrayList<>();
		for (Map.Entry<String, List<Integer>> entry : producerMapGrouped.entrySet()) {
			List<Integer> winnerYears = entry.getValue();

			// verificar apenas os que ganharam mais de uma vez
			if (winnerYears != null && winnerYears.size() > 1) {
				int currentYear = 0;
				int currentInterval = 0;
				for (Integer year : winnerYears) {
					// primeiro ano
					if (currentYear == 0) {
						currentYear = year;
						continue;
					}

					currentInterval = year - currentYear;

					if (handleCalculateInterval(currentInterval, interval, isMax)) {
						ProducerDTO producerDto = new ProducerDTO();
						interval = currentInterval;
						producerDto.setProducer(entry.getKey());
						producerDto.setInterval(interval);
						producerDto.setPreviousWin(currentYear);
						producerDto.setFollowingWin(year);
						producerDtoList.add(producerDto);
					}
					currentYear = year;
				}
				interval = 1;
			}
		}

		return handleFinalData(producerDtoList, isMax);
	}

	private Boolean handleCalculateInterval(int currentInterval, int interval, Boolean isMax) {
		if (isMax)
			return currentInterval >= interval;
		else
			return currentInterval <= interval;
	}

	private List<ProducerDTO> handleFinalData(List<ProducerDTO> producerDtoList, Boolean isMax) {
		List<ProducerDTO> producers = new ArrayList<>();
		if (producerDtoList != null && producerDtoList.size() > 0) {
			List<ProducerDTO> producersSorted;

			// ordenando por intervalo para o menor valor
			if (isMax)
				producerDtoList.sort(Comparator.comparingInt(ProducerDTO::getInterval).reversed());
			else
				producerDtoList.sort(Comparator.comparingInt(ProducerDTO::getInterval));
			int first = producerDtoList.get(0).getInterval();

			// deixando os intervals com menor valor
			producersSorted = producerDtoList.stream().filter(p -> p.getInterval() == first)
					.collect(Collectors.toList());
			java.util.Set<String> nameSet = new HashSet<>();

			// removendo producers repetidos com o mesmo interval
			producers = producersSorted.stream().filter(e -> nameSet.add(e.getProducer())).collect(Collectors.toList());

			// ordenando pelo menor ano vencedor
			producers.sort(Comparator.comparingInt(ProducerDTO::getPreviousWin));
		}
		return producers;
	}
}
