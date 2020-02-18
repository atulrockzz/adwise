package com.spiralforge.adwise.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.adwise.dto.AvailableSlotList;
import com.spiralforge.adwise.dto.AvailableSlotResponseDto;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.service.SlotService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SlotControllerTest {

	List<AvailableSlotList> slotList = null;
	List<AvailableSlotList> slotList1 = null;
	AvailableSlotList availableSlotList = null;

	@InjectMocks
	SlotController slotController;

	@Mock
	SlotService slotService;

	@Before
	public void before() {
		slotList = new ArrayList<>();

		availableSlotList = new AvailableSlotList();
		slotList1 = new ArrayList<>();
		availableSlotList.setSlotId(1L);
		slotList1.add(availableSlotList);
	}

	@Test
	public void testGetAvailableSlotsNegative() throws SlotNotFoundException {
		Mockito.when(slotService.getAvailableSlots("2019-10-02")).thenReturn(slotList);
		ResponseEntity<AvailableSlotResponseDto> response = slotController.getAvailableSlots("2019-10-02");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testGetAvailableSlotsPositive() throws SlotNotFoundException {
		Mockito.when(slotService.getAvailableSlots("2019-10-02")).thenReturn(slotList1);
		ResponseEntity<AvailableSlotResponseDto> response = slotController.getAvailableSlots("2019-10-02");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
