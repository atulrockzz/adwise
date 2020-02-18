package com.spiralforge.adwise.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingRequestDto implements Serializable {

	private Long slotId;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	private LocalDate slotDate;
	private Long planId;
	private String customerName;	
}
