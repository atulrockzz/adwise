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
import org.springframework.beans.factory.annotation.Autowired;

import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.dto.BookingResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.repository.BookingRepository;
import com.spiralforge.adwise.repository.SlotRepository;
import com.spiralforge.adwise.util.AddWiseEnum.Role;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BookingServiceTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);

	@InjectMocks
	private BookingServiceImpl bookingService;

	@Mock
	private PlanService planService;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private SlotRepository slotRepository;
	
	@Mock
	private UserService userService;
	
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
		bookings.add(booking);
		
		bookingRequestDto.setPlanId(1L);
		bookingRequestDto.setSlotId(1L);
		bookingRequestDto.setSlotDate(LocalDate.now());
		bookingRequestDto.setSlotFromTime(LocalTime.now());
		bookingRequestDto.setSlotToTime(LocalTime.now());
	}

	@Test
	public void testGetSlotBookingPositive() {
		logger.info("Entered into testGetSlotBookingPositive method in controller");
		Long slotId = 1L;
		Long userId = 1L;
		Booking booking1 = new Booking();
		booking1.setBookingId(1L);

		Mockito.when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));
		Mockito.when(bookingRepository.save(booking)).thenReturn(booking1);
		Mockito.when(slotRepository.save(slot)).thenReturn(slot1);
		Mockito.when(planService.getPlanByPlanId(bookingRequestDto.getPlanId())).thenReturn(Optional.of(plan));

		Optional<Booking> result = bookingService.slotBooking(userId, bookingRequestDto);
		assertEquals(true, result.isPresent());
	}

	@Test
	public void testGetSlotBookingForSlotNegative() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		Long slotId = 1L;
		Long userId = 1L;
		Booking booking1 = new Booking();
		booking1.setBookingId(1L);
		booking1.setPrice(15D);
		booking1.setSlot(slot);
		booking1.setSlotDate(LocalDate.now());
		booking1.setSlotFromTime(LocalTime.now());
		booking1.setSlotToTime(LocalTime.now());
		slot.setSlotToTime(LocalTime.now());
		Mockito.when(slotRepository.findById(slotId)).thenReturn(Optional.ofNullable(null));

		Optional<Booking> result = bookingService.slotBooking(userId, bookingRequestDto);
		assertEquals(false, result.isPresent());
	}
	
	@Test
	public void testGetSlotBookingForUserNegative() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		Long slotId = 1L;
		Long userId = 1L;
		Booking booking1 = new Booking();
		booking1.setBookingId(1L);
		booking1.setPrice(15D);
		booking1.setSlot(slot);
		booking1.setSlotDate(LocalDate.now());
		booking1.setSlotFromTime(LocalTime.now());
		booking1.setSlotToTime(LocalTime.now());
		slot.setSlotToTime(LocalTime.now());
		Mockito.when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.ofNullable(null));
		Optional<Booking> result = bookingService.slotBooking(userId, bookingRequestDto);
		assertEquals(false, result.isPresent());
	}

	@Test
	public void testGetSlotBookingNegative() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		Long slotId = 1L;
		Long userId = 1L;
		Booking booking1 = new Booking();
		booking1.setBookingId(1L);
		booking1.setPrice(15D);
		booking1.setSlot(slot);
		booking1.setSlotDate(LocalDate.now());
		booking1.setSlotFromTime(LocalTime.now());
		booking1.setSlotToTime(LocalTime.now());
		
		Mockito.when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));
		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking1);
		Mockito.when(planService.getPlanByPlanId(bookingRequestDto.getPlanId())).thenReturn(Optional.ofNullable(null));

		Optional<Booking> result = bookingService.slotBooking(userId, bookingRequestDto);
		assertEquals(true, result.isPresent());
	}
	
	@Test
	public void testGetSlotBookingForNegative() {
		logger.info("Entered into testGetSlotBookingNegative method in controller");
		Long slotId = 1L;
		Long userId = 1L;
		Booking booking1 = new Booking();
		booking1.setBookingId(1L);
		booking1.setPrice(15D);
		booking1.setSlot(slot);
		booking1.setSlotDate(LocalDate.now());
		booking1.setSlotFromTime(LocalTime.now());
		booking1.setSlotToTime(LocalTime.now());
		slot.setSlotToTime(LocalTime.now());
		Mockito.when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
		Mockito.when(userService.getUserByUserId(userId)).thenReturn(Optional.of(user));
		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking1);
		Mockito.when(planService.getPlanByPlanId(bookingRequestDto.getPlanId())).thenReturn(Optional.ofNullable(null));

		Optional<Booking> result = bookingService.slotBooking(userId, bookingRequestDto);
		assertEquals(true, result.isPresent());
	}

	@Test
	public void testGetBookingByDateFromTimeAndToTimeNegative() {
		logger.info("Entered into GetPlanByDateFromTimeToTimePositive method in controller");
		LocalDate date = LocalDate.now();
		LocalTime time1 = LocalTime.now();
		LocalTime time2 = LocalTime.now().plusMinutes(23);

		Mockito.when(bookingRepository.getBookingByDateFromTimeAndToTime(date, time1, time2))
				.thenReturn(bookings);

		List<Booking> result = bookingService.getBookingByDateFromTimeAndToTime(date, time1, time2);
		assertEquals(1, result.size());
	}

}
