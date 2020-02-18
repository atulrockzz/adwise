package com.spiralforge.adwise.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.spiralforge.adwise.dto.AvailableSlotList;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.repository.BookingRepository;
import com.spiralforge.adwise.repository.PlanRepository;
import com.spiralforge.adwise.repository.SlotRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SlotServiceTest {

	List<AvailableSlotList> slotList1 = null;
	AvailableSlotList availableSlotList = null;
	Plan plan = null;
	Slot slot = null;
	List<Slot> slotList = null;

	@InjectMocks
	SlotServiceImpl slotServiceImpl;

	@Mock
	SlotRepository slotRepository;

	@Mock
	BookingRepository bookingRepository;

	@Mock
	PlanRepository planRepository;

	@Before
	public void before() {
		slotList1 = new ArrayList<>();

		availableSlotList = new AvailableSlotList();
		availableSlotList.setSlotId(1L);
		slotList1.add(availableSlotList);

		slot = new Slot();
		plan = new Plan();
		plan.setPlanId(1L);
		plan.setPlanRate(200.00);
		slot.setPlan(plan);
		slot.setSlotDate(LocalDate.of(2020, 02, 17));
		slot.setSlotFromTime(LocalTime.of(10, 00, 00));
		slot.setSlotToTime(LocalTime.of(11, 00, 00));
		slotList = new ArrayList<>();
		slotList.add(slot);
	}

	@Test
	public void testGetAvailableSlotsPositive() throws SlotNotFoundException {
		String slotDate = "2020-02-17";
		Mockito.when(slotRepository.findAllBySlotDate(LocalDate.of(2020, 02, 17))).thenReturn(slotList);
		Mockito.when(planRepository.findByPlanId(1L)).thenReturn(plan);
		List<AvailableSlotList> response = slotServiceImpl.getAvailableSlots(slotDate);
		assertEquals(slotList.size(), response.size());
	}

	@Test(expected = SlotNotFoundException.class)
	public void testGetAvailableSlotsException() throws SlotNotFoundException {
		String slotDate = "2020-02-17";
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.findAllBySlotDate(LocalDate.now())).thenReturn(slotList1);
		slotServiceImpl.getAvailableSlots(slotDate);

	}
	
	@Test
	public void testGetSlotBySlotIdPositive() {
		Long slotId = 1L;
		Mockito.when(slotRepository.findBySlotId(slotId)).thenReturn(Optional.of(slot));

		Optional<Slot> result = slotServiceImpl.getSlotBySlotId(slotId);
		Assert.assertEquals(true, result.isPresent());
	}
}
