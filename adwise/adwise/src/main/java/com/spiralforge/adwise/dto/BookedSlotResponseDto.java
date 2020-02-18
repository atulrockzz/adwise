package com.spiralforge.adwise.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookedSlotResponseDto implements Serializable {

	private Long bookingId;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	private Double price;
	private LocalDate slotDate;
	private String customerName;	

}
