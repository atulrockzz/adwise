package com.spiralforge.adwise.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BookingResponseDto implements Serializable {

	private Integer successCount;
	private Integer failureCount;

	private Integer statusCode;
	private String message;

}
