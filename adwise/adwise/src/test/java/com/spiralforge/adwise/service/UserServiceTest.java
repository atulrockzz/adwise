package com.spiralforge.adwise.service;

import static org.junit.Assert.assertEquals;

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
import org.springframework.beans.BeanUtils;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.AddSlotRequestDto;
import com.spiralforge.adwise.dto.BookedResponseDto;
import com.spiralforge.adwise.dto.BookedSlotResponseDto;
import com.spiralforge.adwise.dto.LoginRequestDto;
import com.spiralforge.adwise.dto.LoginResponseDto;
import com.spiralforge.adwise.dto.ResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.exception.UserNotFoundException;
import com.spiralforge.adwise.repository.BookingRepository;
import com.spiralforge.adwise.repository.PlanRepository;
import com.spiralforge.adwise.repository.SlotRepository;
import com.spiralforge.adwise.repository.UserRepository;
import com.spiralforge.adwise.util.AddWiseEnum.Role;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {
	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	@Mock
	SlotRepository slotRepository;

	@Mock
	BookingRepository bookingRepository;

	@Mock
	PlanRepository planRepository;

	LoginRequestDto loginRequestDto = null;
	User user = null;
	LoginRequestDto loginRequestDto1 = null;
	AddSlotRequestDto addSlotRequestDto = null;
	Slot slot = null;
	List<Slot> slotList = null;
	Plan plan = null;

	BookedSlotResponseDto bookedSlotResponseDto = new BookedSlotResponseDto();
	Role role = Role.ADMIN;
	LocalDate localDate = LocalDate.parse("2020-02-17");
	List<Slot> slots = new ArrayList<>();
	List<Booking> bookedSlots = new ArrayList<>();
	Booking booking = new Booking();
	List<BookedSlotResponseDto> responseList = new ArrayList<>();
	BookedResponseDto bookedResponseDto = new BookedResponseDto();
	List<BookedResponseDto> bookedlist = new ArrayList<>();
	SlotStatus slotStatus = SlotStatus.AVAILABLE;

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("muthu123");
		user = new User();
		user.setUserId(1L);
		user.setMobileNumber(9876543210L);
		user.setPassword("muthu123");

		loginRequestDto1 = new LoginRequestDto();
		loginRequestDto1.setMobileNumber(9876543211L);
		loginRequestDto1.setPassword("muthu123");

		addSlotRequestDto = new AddSlotRequestDto();
		addSlotRequestDto.setPlanId(1L);
		addSlotRequestDto.setSlotDate("2020-02-19");
		addSlotRequestDto.setSlotFromTime("11:00:00");
		addSlotRequestDto.setSlotToTime("12:00:00");

		slotList = new ArrayList<>();
		slot = new Slot();
		plan = new Plan();
		plan.setPlanId(1L);
		plan.setPlanName("Premium");
		plan.setPlanDate(LocalDate.of(2020, 02, 19));
		plan.setPlanFromTime(LocalTime.of(10, 01, 00));
		plan.setPlanToTime(LocalTime.of(12, 02, 00));
		slot.setPlan(plan);
		slot.setSlotDate(LocalDate.of(2019, 02, 10));
		slot.setSlotFromTime(LocalTime.of(1, 00, 00));
		slot.setSlotToTime(LocalTime.of(2, 00, 00));
		slotList.add(slot);

		slot.setSlotId(1L);
		slot.setSlotDate(localDate);
		slot.setSlotFromTime(LocalTime.of(11, 00, 00));
		slot.setSlotToTime(LocalTime.of(12, 00, 00));
		slot.setUser(user);
		slot.setSlotStatus(slotStatus);
		slots.add(slot);
		booking.setSlotDate(LocalDate.of(2020, 02, 17));
		booking.setBookingId(1L);
		booking.setPrice(200D);
		booking.setSlot(slot);
		booking.setUser(user);
		booking.setSlotFromTime(LocalTime.of(11, 00, 00));
		booking.setSlotToTime(LocalTime.of(12, 00, 00));
		bookedSlots.add(booking);
		user.setUserId(1L);
		user.setMobileNumber(453782975L);
		user.setPassword("sri");
		user.setUserName("sri");
		user.setRole(role);
		BeanUtils.copyProperties(bookedSlots, bookedSlotResponseDto);
		responseList.add(bookedSlotResponseDto);
		BeanUtils.copyProperties(slot, bookedResponseDto);
		bookedResponseDto.setSlotId(slot.getSlotId());
		bookedResponseDto.setBookedSlot(responseList);
		bookedlist.add(bookedResponseDto);
	}

	@Test
	public void testCheckLoginPositive() throws UserNotFoundException {
		Mockito.when(userRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword())).thenReturn(user);
		LoginResponseDto response = userServiceImpl.checkLogin(loginRequestDto);
		assertEquals(user.getUserId(), response.getUserId());
	}

	@Test(expected = UserNotFoundException.class)
	public void testCheckLoginNegative() throws UserNotFoundException {
		Mockito.when(userRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword())).thenReturn(user);
		userServiceImpl.checkLogin(loginRequestDto1);
	}

	@Test
	public void testAddSlotUserInvalid() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		ResponseDto response = userServiceImpl.addSlot(2L, addSlotRequestDto);
		assertEquals(ApiConstant.USER_NOTFOUND, response.getMessage());
	}

	@Test
	public void testAddSlotDateInvalid() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		AddSlotRequestDto addSlotRequestDto2 = new AddSlotRequestDto();
		addSlotRequestDto2.setSlotDate("2019-02-19");
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto2);
		assertEquals(ApiConstant.DATE_INVALID_MESSAGE, response.getMessage());
	}

	@Test
	public void testAddSlotTimeInvalid() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		AddSlotRequestDto addSlotRequestDto2 = new AddSlotRequestDto();
		addSlotRequestDto2.setSlotDate("2020-02-19");
		addSlotRequestDto2.setSlotFromTime("10:00:00");
		addSlotRequestDto2.setSlotToTime("08:00:00");
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto2);
		assertEquals(ApiConstant.TIME_INVALID_MESSAGE, response.getMessage());
	}

	@Test
	public void testAddSlotDateLreadyAdded() {
		LocalDate slotDate = LocalDate.parse(addSlotRequestDto.getSlotDate());
		LocalTime slotFromTime = LocalTime.parse(addSlotRequestDto.getSlotFromTime());
		LocalTime slotToTime = LocalTime.parse(addSlotRequestDto.getSlotToTime());
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(slotDate, slotFromTime, slotToTime))
				.thenReturn(slotList);
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.SLOT_ALREADYBOOKED, response.getMessage());
	}

	@Test
	public void testAddSlotPlanInvalid() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(slotList1);
		Mockito.when(planRepository.findByPlanIdAndPlanDate(2L, LocalDate.now())).thenReturn(Optional.ofNullable(null));
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.PLAN_NOT_FOUND, response.getMessage());
	}

	@Test
	public void testAddSlotPlanConflictBoth() {
		Plan plan2 = new Plan();
		slot.setPlan(plan2);
		plan2.setPlanId(2L);
		plan2.setPlanName("Premium");
		plan2.setPlanDate(LocalDate.of(2020, 02, 19));
		plan2.setPlanFromTime(LocalTime.of(12, 00, 00));
		plan2.setPlanToTime(LocalTime.of(13, 00, 00));
		addSlotRequestDto.setPlanId(2L);
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(slotList1);
		Mockito.when(planRepository.findByPlanIdAndPlanDate(2L, plan2.getPlanDate())).thenReturn(Optional.of(plan2));
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.PLAN_SLOT_CONFICT_MESSAGE, response.getMessage());
	}

	@Test
	public void testAddSlotFromTimePlanConflict() {
		Plan plan2 = new Plan();
		slot.setPlan(plan2);
		plan2.setPlanId(2L);
		plan2.setPlanName("Premium");
		plan2.setPlanDate(LocalDate.of(2020, 02, 19));
		plan2.setPlanFromTime(LocalTime.of(9, 00, 00));
		plan2.setPlanToTime(LocalTime.of(11, 00, 00));
		addSlotRequestDto.setPlanId(2L);
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(slotList1);
		Mockito.when(planRepository.findByPlanIdAndPlanDate(2L, plan2.getPlanDate())).thenReturn(Optional.of(plan2));
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.PLAN_SLOT_CONFICT_MESSAGE, response.getMessage());
	}

	@Test
	public void testAddSlotToTimePlanConflict() {
		Plan plan2 = new Plan();
		slot.setPlan(plan2);
		plan2.setPlanId(2L);
		plan2.setPlanName("Premium");
		plan2.setPlanDate(LocalDate.of(2020, 02, 19));
		plan2.setPlanFromTime(LocalTime.of(10, 00, 00));
		plan2.setPlanToTime(LocalTime.of(1, 00, 00));
		addSlotRequestDto.setPlanId(2L);
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(slotList1);
		Mockito.when(planRepository.findByPlanIdAndPlanDate(2L, plan2.getPlanDate())).thenReturn(Optional.of(plan2));
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.PLAN_SLOT_CONFICT_MESSAGE, response.getMessage());
	}

	@Test
	public void testAddSlotSuccess() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		List<Slot> slotList1 = new ArrayList<>();
		Mockito.when(slotRepository.getSlotByDateFromTimeToTime(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(slotList1);
		Mockito.when(planRepository.findByPlanIdAndPlanDate(1L, LocalDate.of(2020, 02, 19)))
				.thenReturn(Optional.of(plan));
		ResponseDto response = userServiceImpl.addSlot(1L, addSlotRequestDto);
		assertEquals(ApiConstant.ADDSLOT_SUCCESS, response.getMessage());
	}

	@Test
	public void testGetBookedSlotsPositive() throws UserNotFoundException, SlotNotFoundException {
		Mockito.when(userRepository.findByUserIdAndRole(1L, role)).thenReturn(Optional.of(user));
		Mockito.when(slotRepository.findBySlotDate(localDate)).thenReturn(slots);
		Mockito.when(bookingRepository.findBySlotDateAndSlot(localDate, slot)).thenReturn(bookedSlots);
		List<BookedResponseDto> bookedResponseDto = userServiceImpl.getBookedSlots(1L, "2020-02-17");
		assertEquals(1, bookedResponseDto.size());
	}

	@Test(expected = UserNotFoundException.class)
	public void testGetBookedSlotsNegativeException() throws UserNotFoundException, SlotNotFoundException {
		Mockito.when(userRepository.findByUserIdAndRole(2L, role)).thenReturn(Optional.of(user));
		userServiceImpl.getBookedSlots(1L, "2020-02-17");
	}

	@Test(expected = SlotNotFoundException.class)
	public void testGetBookedSlotsNegative() throws UserNotFoundException, SlotNotFoundException {
		localDate = LocalDate.parse("2020-02-02");
		Mockito.when(userRepository.findByUserIdAndRole(1L, role)).thenReturn(Optional.of(user));
		Mockito.when(slotRepository.findBySlotDate(localDate)).thenReturn(slots);
		userServiceImpl.getBookedSlots(1L, "2020-02-17");
	}

	@Test
	public void testGetUserByUserIdPositive() {
		Long userId = 1L;
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		Optional<User> result = userServiceImpl.getUserByUserId(userId);
		Assert.assertEquals(true, result.isPresent());
	}

}
