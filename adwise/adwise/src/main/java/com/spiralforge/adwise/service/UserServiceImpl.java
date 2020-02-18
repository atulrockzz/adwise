package com.spiralforge.adwise.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	SlotRepository slotRepository;

	@Autowired
	PlanRepository planRepository;

	@Override
	public Optional<User> getUserByUserId(Long userId) {
		return userRepository.findById(userId);
	}

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-17. In this method all the booked slots are fetched from
	 *        database.
	 * @param userId for checking whether user is admin or sales person.
	 * @param date   for particular date
	 * @return list of booked slots on that particular date.
	 * @throws UserNotFoundException if user is not available.
	 * @throws SlotNotFoundException if slot is unavailable.
	 */
	@Override
	public List<BookedResponseDto> getBookedSlots(@Valid Long userId, String date)
			throws UserNotFoundException, SlotNotFoundException {
		LocalDate localDate = LocalDate.parse(date);
		Role role = Role.ADMIN;
		Optional<User> user = userRepository.findByUserIdAndRole(userId, role);
		if (!user.isPresent()) {
			throw new UserNotFoundException(ApiConstant.USER_NOT_FOUND_EXCEPTION);
		}
		List<Slot> slots = slotRepository.findBySlotDate(localDate);
		if (slots.isEmpty()) {
			throw new SlotNotFoundException(ApiConstant.SLOT_NOT_FOUND_EXCEPTION);
		}
		List<BookedResponseDto> bookedlist = new ArrayList<>();
		slots.forEach(slot -> {
			List<BookedSlotResponseDto> responseList = new ArrayList<>();
			BookedResponseDto bookedResponseDto = new BookedResponseDto();
			List<Booking> bookedSlots = bookingRepository.findBySlotDateAndSlot(localDate, slot);
			bookedSlots.forEach(bookedSlot -> {
				BookedSlotResponseDto bookedSlotResponseDto = new BookedSlotResponseDto();
				BeanUtils.copyProperties(bookedSlot, bookedSlotResponseDto);
				responseList.add(bookedSlotResponseDto);
				BeanUtils.copyProperties(slot, bookedResponseDto);
			});
			bookedResponseDto.setSlotId(slot.getSlotId());
			bookedResponseDto.setBookedSlot(responseList);
			bookedlist.add(bookedResponseDto);
		});
		return bookedlist;
	}

	/**
	 * @author Muthu
	 *
	 *         Method is used for login for the salesperson and admin
	 *
	 * @param loginRequestDto which has the input as mobile number and password
	 * @return LoginResponseDto which has the details of the user that includes
	 *         name,role and id
	 * @throws UserNotFoundException is called when the credentials are invalid
	 */
	@Override
	public LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws UserNotFoundException {
		LoginResponseDto loginResponse = new LoginResponseDto();
		User user = userRepository.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword());
		if (Objects.isNull(user)) {
			logger.error(ApiConstant.LOGIN_ERROR);
			throw new UserNotFoundException(ApiConstant.LOGIN_ERROR);
		}
		BeanUtils.copyProperties(user, loginResponse);
		return loginResponse;
	}

	/**
	 * @author Muthu
	 *
	 *         Method is used to add a slot by the admin
	 *
	 * @param userId            the key which is used to identify a user
	 * @param addSlotRequestDto which has slot date,slot from time,plan id and slot
	 *                          to time
	 * @return ResponseDto which returns success/failure message with their status
	 *         code
	 */
	@Override
	public ResponseDto addSlot(Long userId, AddSlotRequestDto addSlotRequestDto) {
		Optional<User> user = userRepository.findById(userId);
		if (!(user.isPresent())) {
			return addSlotResponse(ApiConstant.USER_NOTFOUND, ApiConstant.USER_NOTFOUND_CODE);
		}
		LocalDate slotDate = LocalDate.parse(addSlotRequestDto.getSlotDate());
		if (slotDate.isBefore(LocalDate.now())) {
			return addSlotResponse(ApiConstant.DATE_INVALID_MESSAGE, ApiConstant.DATE_INVALID_CODE);
		}
		LocalTime slotFromTime = LocalTime.parse(addSlotRequestDto.getSlotFromTime());
		LocalTime slotToTime = LocalTime.parse(addSlotRequestDto.getSlotToTime());
		if (slotFromTime.isAfter(slotToTime)) {
			return addSlotResponse(ApiConstant.TIME_INVALID_MESSAGE, ApiConstant.TIME_INVALID_CODE);
		}
		List<Slot> slots = slotRepository.getSlotByDateFromTimeToTime(slotDate, slotFromTime, slotToTime);
		if (slots.isEmpty()) {
			Slot slotDetails = new Slot();
			Optional<Plan> plan = planRepository.findByPlanIdAndPlanDate(addSlotRequestDto.getPlanId(), slotDate);
			if (!(plan.isPresent())) {
				return addSlotResponse(ApiConstant.PLAN_NOT_FOUND, ApiConstant.PLAN_NOT_FOUND_CODE);
			}
			if (slotFromTime.isAfter(plan.get().getPlanFromTime()) && slotToTime.isBefore(plan.get().getPlanToTime())) {
				slotDetails.setPlan(plan.get());
				slotDetails.setSlotDate(slotDate);
				slotDetails.setSlotFromTime(slotFromTime);
				slotDetails.setSlotToTime(slotToTime);
				slotDetails.setSlotStatus(SlotStatus.AVAILABLE);
				slotDetails.setUser(user.get());
				slotRepository.save(slotDetails);
				return addSlotResponse(ApiConstant.ADDSLOT_SUCCESS, ApiConstant.ADDSLOT_SUCCESS_CODE);
			}
			return addSlotResponse(ApiConstant.PLAN_SLOT_CONFICT_MESSAGE, ApiConstant.PLAN_SLOT_CONFLICT_CODE);
		}
		return addSlotResponse(ApiConstant.SLOT_ALREADYBOOKED, ApiConstant.SLOTALREADYBOOKED_CODE);
	}

	/**
	 * @author Muthu
	 *
	 *         Method is used to store the response which has message and status
	 *         code after adding a slot
	 *
	 * @param response   Message which should be returned
	 * @param statusCode which needs to be returned
	 * @return which has the response and status code
	 */
	private ResponseDto addSlotResponse(String response, String statusCode) {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(response);
		responseDto.setStatusCode(statusCode);
		return responseDto;
	}
}
