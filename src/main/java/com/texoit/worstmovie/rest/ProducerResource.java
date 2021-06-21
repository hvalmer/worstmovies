package com.texoit.worstmovie.rest;

import java.io.Serializable;
import java.util.List;

import com.texoit.worstmovie.dto.ProducerDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerResource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<ProducerDTO> min;
	private List<ProducerDTO> max;
}
