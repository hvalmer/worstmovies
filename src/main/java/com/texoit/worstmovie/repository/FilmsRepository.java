package com.texoit.worstmovie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.worstmovie.domain.Films;

public interface FilmsRepository extends JpaRepository<Films, Long> {

	List<Films> findAllByTheWinnerOrderByYearAsc(Boolean theWinner);
}
