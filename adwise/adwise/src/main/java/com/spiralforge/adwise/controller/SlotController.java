package com.spiralforge.adwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.AvailableSlotList;
import com.spiralforge.adwise.dto.AvailableSlotResponseDto;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.service.SlotService;

@RestController
@RequestMapping("/slots")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class SlotController {

	@Autowired
	SlotService slotService;

	/**
	 * @author Muthu
	 *
	 *         Method is used to get the list of available slots on a particular
	 *         date
	 *
	 * @param slotDate which has the input as a date
	 * @return AvailableSlotResponseDto which has the list of slots with a message
	 * @throws SlotNotFoundException is called when no particular slots found for
	 *                               that date
	 */

	@GetMapping
	public ResponseEntity<AvailableSlotResponseDto> getAvailableSlots(@RequestParam String slotDate)
			throws SlotNotFoundException {
		AvailableSlotResponseDto availableSlotResponseDto = new AvailableSlotResponseDto();
		List<AvailableSlotList> slotList = slotService.getAvailableSlots(slotDate);
		if (slotList.isEmpty()) {
			availableSlotResponseDto.setMessage(ApiConstant.AVAILABLESLOT_EMPTY_LIST);
			return new ResponseEntity<>(availableSlotResponseDto, HttpStatus.NOT_FOUND);
		}
		availableSlotResponseDto.setMessage(ApiConstant.AVAILABLE_SLOT_LIST);
		availableSlotResponseDto.setSlotList(slotList);
		return new ResponseEntity<>(availableSlotResponseDto, HttpStatus.OK);
	}

}
