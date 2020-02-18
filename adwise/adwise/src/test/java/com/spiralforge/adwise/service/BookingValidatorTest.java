package com.spiralforge.adwise.service;

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

import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.dto.BookingResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.util.AddWiseEnum.Role;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;
import com.spiralforge.adwise.util.BookingValidatorImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BookingValidatorTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BookingValidatorTest.class);

	@InjectMocks
	private BookingValidatorImpl bookingValidatorImpl;
	
	@Mock
	private UserService userService;

	@Mock
	private BookingService bookingService;

	@Mock
	private SlotService slotService;

	User user = new User();
	Plan plan = new Plan();
	Slot slot = new Slot();
	Slot slot1 = new Slot();
	Booking booking = new Booking();
	BookingRequestDto bookingRequestDto = new BookingRequestDto();
	BookingResponseDto bookingResponseDto = new BookingResponseDto();
	List<Booking> bookings = new ArrayList();

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
		slot.setSlotFromTime(LocalTime.parse("09:10:00"));
		slot.setSlotToTime(LocalTime.parse("11:20:00"));
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
		bookings.add(booking);
		
		bookingRequestDto.setPlanId(1L);
		bookingRequestDto.setSlotId(1L);
		bookingRequestDto.setSlotDate(LocalDate.now());
		bookingRequestDto.setSlotFromTime(LocalTime.parse("10:10:00"));
		bookingRequestDto.setSlotToTime(LocalTime.parse("10:20:00"));
	}

	@Test
	public void testValidatePositive() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.parse("10:00:00");
		LocalTime time2 = LocalTime.parse("10:23:00");
		Long slotId=1L;
		Long userId=1L;

		Mockito.when(slotService.getSlotBySlotId(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(bookingService.getBookingByDateFromTimeAndToTime(date,time1,time2)).thenReturn(bookings);
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));

		Boolean result = bookingValidatorImpl.validate(userId, bookingRequestDto);
		assertEquals(true, result);
	}
	
	@Test
	public void testValidateNegative1() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);
		Long slotId=1L;
		Long userId=1L;
		BookingRequestDto bookingRequestDto=null;

		Mockito.when(slotService.getSlotBySlotId(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(bookingService.getBookingByDateFromTimeAndToTime(date,time1,time2)).thenReturn(bookings);
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));

		Boolean result = bookingValidatorImpl.validate(userId, bookingRequestDto);
		assertEquals(false, result);
	}

	@Test
	public void testValidateNegative2() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);
		Long slotId=1L;
		Long userId=1L;

		Mockito.when(slotService.getSlotBySlotId(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(bookingService.getBookingByDateFromTimeAndToTime(date,time1,time2)).thenReturn(bookings);
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.ofNullable(null));

		Boolean result = bookingValidatorImpl.validate(userId, bookingRequestDto);
		assertEquals(false, result);
	}
	
	@Test
	public void testValidateNegative3() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		Long slotId=1L;
		Long userId=1L;

		Mockito.when(slotService.getSlotBySlotId(slotId)).thenReturn(Optional.ofNullable(null));

		Boolean result = bookingValidatorImpl.validate(userId, bookingRequestDto);
		assertEquals(false, result);
	}
	
	@Test
	public void testValidateNegative4() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);
		Long slotId=1L;
		Long userId=1L;
		bookingRequestDto.setSlotFromTime(LocalTime.parse("20:00:00"));
		bookingRequestDto.setSlotToTime(LocalTime.parse("10:00:00"));

		Mockito.when(slotService.getSlotBySlotId(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(bookingService.getBookingByDateFromTimeAndToTime(date,time1,time2)).thenReturn(bookings);
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.ofNullable(user));

		Boolean result = bookingValidatorImpl.validate(userId, bookingRequestDto);
		assertEquals(false, result);
	}

}
