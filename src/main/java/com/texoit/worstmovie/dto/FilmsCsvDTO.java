package com.texoit.worstmovie.dto;

import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmsCsvDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@CsvBindByName
	private Integer year;
	
	@CsvBindByName
	private String title;
	
	@CsvBindByName
	private String studios;
	
	@CsvBindByName
	private String producers;
	
	@CsvBindByName
	private String winner;
	
	public Boolean theWinner() {
		return (this.getWinner() != null && !this.getWinner().equals("") && this.getWinner().equals("yes"));
	}
}
