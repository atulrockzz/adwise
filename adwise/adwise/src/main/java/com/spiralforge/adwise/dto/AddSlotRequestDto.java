package com.spiralforge.adwise.dto;

import lombok.Data;

@Data
public class AddSlotRequestDto {
	private String slotDate;
	private String slotFromTime;
	private String slotToTime;
	private Long planId;
}
