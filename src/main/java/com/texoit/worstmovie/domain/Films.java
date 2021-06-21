package com.texoit.worstmovie.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILM")
@EqualsAndHashCode(of = "idFilm")
public class Films implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CD_FILM", length = 20, unique = true, nullable = false)
	@SequenceGenerator(name = "film_seq", sequenceName = "film_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "film_seq")
	@JsonIgnore
	private Long idFilm;
	
	@Column(name = "YEAR")
	private Integer year;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "STUDIOS")
	private String studios;
	
	@Column(name = "PRODUCERS")
	private String producers;
	
	@JsonProperty("winner")
	@Column(name = "WINNER")
	private Boolean theWinner;
}
