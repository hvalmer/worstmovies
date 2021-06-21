package com.texoit.worstmovie.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String producer;
	private int interval;
	private int previousWin;
	private int followingWin;
}
