package com.spiralforge.adwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.dto.BookingResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.exception.ValidationFailedException;
import com.spiralforge.adwise.repository.PlanRepository;
import com.spiralforge.adwise.repository.SlotRepository;
import com.spiralforge.adwise.util.AddWiseEnum.Role;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PlanServiceTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PlanServiceTest.class);

	@InjectMocks
	private PlanServiceImpl planService;

	@Mock
	private PlanRepository planRepository;

	User user = new User();
	Plan plan = new Plan();
	Slot slot = new Slot();
	Slot slot1 = new Slot();
	Booking booking = new Booking();
	BookingRequestDto bookingRequestDto = new BookingRequestDto();
	BookingResponseDto bookingResponseDto = new BookingResponseDto();
	List<Plan> plans = new ArrayList();

	@Before
	public void setUp() {
		user.setUserId(1L);
		user.setMobileNumber(9916818770L);
		user.setRole(Role.ADMIN);
		user.setUserName("sujal");
		user.setPassword("pass");

		plan.setPlanId(1L);
		plan.setPlanDate(LocalDate.now());
		plan.setPlanRate(10D);
		plan.setPlanFromTime(LocalTime.now());
		plan.setPlanName("plan");
		plan.setPlanToTime(LocalTime.now());

		plans.add(plan);

		slot.setSlotId(1L);
		slot.setPlan(plan);
		slot.setSlotDate(LocalDate.now());
		slot.setSlotFromTime(LocalTime.now());
		slot.setSlotToTime(LocalTime.now());
		slot.setSlotStatus(SlotStatus.AVAILABLE);
		slot.setUser(user);

		slot1.setSlotId(1L);
		slot1.setPlan(plan);
		slot1.setSlotDate(LocalDate.now());
		slot1.setSlotFromTime(LocalTime.now());
		slot1.setSlotToTime(LocalTime.now());
		slot1.setSlotStatus(SlotStatus.AVAILABLE);
		slot1.setUser(user);

		booking.setBookingId(1L);
		booking.setPrice(15D);
		booking.setSlot(slot);
		booking.setSlotDate(LocalDate.now());
		booking.setSlotFromTime(LocalTime.now());
		booking.setSlotToTime(LocalTime.now());

		bookingRequestDto.setPlanId(1L);
		bookingRequestDto.setSlotId(1L);
		bookingRequestDto.setSlotDate(LocalDate.now());
		bookingRequestDto.setSlotFromTime(LocalTime.now());
		bookingRequestDto.setSlotToTime(LocalTime.now());
	}

	@Test
	public void testGetPlanByDateFromTimeToTimePositive() throws ValidationFailedException {
		logger.info("Entered into testGetSlotBookingPositive method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);

		Pageable sortedByPriceDesc = PageRequest.of(0, 1, Sort.by("planId").descending());
		Mockito.when(planRepository.getPlanByDateFromTimeToTime(date, time1, time2, sortedByPriceDesc))
				.thenReturn(plans);

		Optional<Plan> result = planService.getPlanByDateFromTimeToTime(date, time1, time2);
		assertEquals(true, result.isPresent());
	}

	@Test
	public void testGetPlanByDateFromTimeToTimeNegative() throws ValidationFailedException {
		logger.info("Entered into testGetSlotBookingPositive method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);

		Pageable sortedByPriceDesc = PageRequest.of(0, 1, Sort.by("planId").descending());
		Mockito.when(planRepository.getPlanByDateFromTimeToTime(date, time1, time2, sortedByPriceDesc))
				.thenReturn(Collections.emptyList());

		Optional<Plan> result = planService.getPlanByDateFromTimeToTime(date, time1, time2);
		assertEquals(false, result.isPresent());
	}

	@Test(expected = ValidationFailedException.class)
	public void testGetPlanByDateFromTimeToTimeException() throws ValidationFailedException {
		logger.info("Entered into testGetSlotBookingPositive method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now().plusMinutes(23);
		LocalTime time2 = LocalTime.now();

		Pageable sortedByPriceDesc = PageRequest.of(0, 1, Sort.by("planId").descending());
		Mockito.when(planRepository.getPlanByDateFromTimeToTime(date, time1, time2, sortedByPriceDesc))
				.thenReturn(Collections.emptyList());

		Optional<Plan> result = planService.getPlanByDateFromTimeToTime(date, time1, time2);
		assertEquals(false, result.isPresent());
	}

	@Test
	public void testGetPlanByPlanIdPositive() {
		logger.info("Entered into testGetPlanByPlanIdNegative method in controller");
		Long planId = 1L;
		Mockito.when(planRepository.findById(planId)).thenReturn(Optional.of(plan));

		Optional<Plan> result = planService.getPlanByPlanId(planId);
		assertEquals(true, result.isPresent());
	}

}
