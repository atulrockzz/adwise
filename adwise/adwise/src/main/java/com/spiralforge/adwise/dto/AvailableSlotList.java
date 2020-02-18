package com.spiralforge.adwise.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

import lombok.Data;

@Data
public class AvailableSlotList {
	private Long slotId;
	private LocalDate slotDate;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	private SlotStatus slotStatus;
	private String planName;
	private Double planRate;

	private Long planId;

}
