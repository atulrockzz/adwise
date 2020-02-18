package com.spiralforge.adwise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.dto.BookingResponseDto;
import com.spiralforge.adwise.dto.PlanResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.exception.ValidationFailedException;
import com.spiralforge.adwise.service.BookingService;
import com.spiralforge.adwise.service.PlanService;
import com.spiralforge.adwise.util.AddWiseEnum.Role;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;
import com.spiralforge.adwise.util.BookingValidator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PlanControllerTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PlanControllerTest.class);

	@InjectMocks
	private PlanController planController;

	@Mock
	private PlanService planService;
	
	@Mock
	private BookingService bookingService;

	@Mock
	@Qualifier("bookingValidator")
	private BookingValidator<Long, BookingRequestDto> bookingValidator;


	User user = new User();
	Plan plan = new Plan();
	Slot slot = new Slot();
	Booking booking = new Booking();
	BookingRequestDto bookingRequestDto= new BookingRequestDto();
	BookingResponseDto bookingResponseDto= new BookingResponseDto();
	List<BookingRequestDto> bookingRequestDtos=new ArrayList<>();
	
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
		
		slot.setSlotId(1L);
		slot.setPlan(plan);
		slot.setSlotDate(LocalDate.now());
		slot.setSlotFromTime(LocalTime.now());
		slot.setSlotToTime(LocalTime.now());
		slot.setSlotStatus(SlotStatus.AVAILABLE);
		slot.setUser(user);
		
		booking.setBookingId(1L);
		booking.setPrice(15D);
		booking.setSlot(slot);
		booking.setSlotDate(LocalDate.now());
		booking.setSlotFromTime(LocalTime.now());
		booking.setSlotToTime(LocalTime.now());
		
		bookingRequestDto.setPlanId(1L);
		bookingRequestDto.setSlotDate(LocalDate.now());
		bookingRequestDto.setSlotFromTime(LocalTime.now());
		bookingRequestDto.setSlotToTime(LocalTime.now());
		bookingRequestDtos.add(bookingRequestDto);
	}

	@Test
	public void GetPlanByDateFromTimeToTimePositive() throws ValidationFailedException {
		logger.info("Entered into PlanByDateFromTimeToTimeNegative method in controller");
		LocalDate date=LocalDate.now();
		LocalTime time1=LocalTime.now();
		LocalTime time2=LocalTime.now().plusMinutes(23);
		Mockito.when(planService.getPlanByDateFromTimeToTime(date,time1,time2)).thenReturn(Optional.of(plan));
		Integer result=planController.getPlanByDateFromTimeToTime(date,time1,time2).getStatusCodeValue();
		assertEquals(200, result);
	}
	
	@Test
	public void testPlanByDateFromTimeToTimeNegative() throws ValidationFailedException {
		logger.info("Entered into negative PlanByDateFromTimeToTimeNegative method in controller");
		LocalDate date=LocalDate.now();
		LocalTime time1=LocalTime.now();
		LocalTime time2=LocalTime.now().plusMinutes(23);
		Mockito.when(planService.getPlanByDateFromTimeToTime(date,time1,time2)).thenReturn(Optional.ofNullable(null));
		Integer result=planController.getPlanByDateFromTimeToTime(date,time1,time2).getStatusCodeValue();
		assertEquals(404, result);
	}
	
	@Test
	public void testPlanByDateFromTimeToTimeException() throws ValidationFailedException {
		logger.info("Entered into negative PlanByDateFromTimeToTimeNegative method in controller");
		LocalDate date=LocalDate.now();
		LocalTime time1=LocalTime.now().plusMinutes(23);
		LocalTime time2=LocalTime.now();
		Mockito.when(planService.getPlanByDateFromTimeToTime(date,time1,time2)).thenReturn(Optional.of(plan));
		ResponseEntity<PlanResponseDto> response=planController.getPlanByDateFromTimeToTime(date,time1,time2);
		assertEquals(ApiConstant.SUCCESS_CODE, response.getStatusCode().value());
	}
	
	@Test
	public void GetSlotBookingPositive() {
		logger.info("Entered into PlanByDateFromTimeToTimeNegative method in controller");
		Long userId=1L;
		Mockito.when(bookingValidator.validate(userId, bookingRequestDto)).thenReturn(true);
		Mockito.when(bookingService.slotBooking(userId, bookingRequestDto)).thenReturn(Optional.of(booking));
		Integer result=planController.slotBooking(userId,bookingRequestDtos).getStatusCodeValue();
		assertEquals(200, result);
	}
	
	@Test
	public void GetSlotBookingNegative() throws ValidationFailedException {
		logger.info("Entered into PlanByDateFromTimeToTimeNegative method in controller");
		Long userId=1L;
		Mockito.when(bookingValidator.validate(userId, bookingRequestDto)).thenReturn(true);
		Mockito.when(bookingService.slotBooking(userId, bookingRequestDto)).thenReturn(Optional.ofNullable(null));
		Integer result=planController.slotBooking(userId,bookingRequestDtos).getStatusCodeValue();
		assertEquals(200, result);
	}
	
	@Test
	public void testSlotBookingTimeNegative() throws ValidationFailedException {
		logger.info("Entered into negative PlanByDateFromTimeToTimeNegative method in controller");
		Long userId=1L;

		Mockito.when(bookingService.slotBooking(userId, bookingRequestDto)).thenReturn(Optional.of(booking));
		Integer result=planController.slotBooking(userId,bookingRequestDtos).getStatusCodeValue();
		assertEquals(200, result);
	}

}
