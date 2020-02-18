package com.spiralforge.adwise.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PlanResponseDto implements Serializable {

	private Long planId;
	private String planName;
	private Double planRate;
	
	private Integer statusCode;
	private String message;
	
}
