package com.spiralforge.adwise.dto;

import java.util.List;

import lombok.Data;

@Data
public class AvailableSlotResponseDto {
	private List<AvailableSlotList> slotList;
	private String message;
}
