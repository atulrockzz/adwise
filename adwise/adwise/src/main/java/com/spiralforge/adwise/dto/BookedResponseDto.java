package com.spiralforge.adwise.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class BookedResponseDto implements Serializable {

	private Long slotId;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	private List<BookedSlotResponseDto> bookedSlot;	
}
