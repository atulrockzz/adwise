package com.spiralforge.adwise.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.service.BookingService;
import com.spiralforge.adwise.service.SlotService;
import com.spiralforge.adwise.service.UserService;

/**
 * 
 * @author Sujal The booking validator is used to validate the booking
 *         information
 *
 */
@Component("bookingValidator")
public class BookingValidatorImpl implements BookingValidator<Long, BookingRequestDto> {

	private static Logger logger = LoggerFactory.getLogger(BookingValidatorImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private SlotService slotService;

	/**
	 * @author Sujal
	 * 
	 *         This method is used to validate the booking request
	 */
	@Override
	public Boolean validate(Long userId, BookingRequestDto bookingRequestDto) {
		if (Objects.isNull(bookingRequestDto) || ObjectUtils.isEmpty(bookingRequestDto)) {
			logger.error(ApiConstant.INVALID_DATA);
			return false;
		} else if (!isUserExist(userId)) {
			logger.error(ApiConstant.INVALID_USER);
			return false;
		} else if (!isValidSlot(bookingRequestDto.getSlotId())) {
			logger.error(ApiConstant.SLOT_NOT_FOUND);
			return false;
		} else if (isValidSlotFromAndToTime(bookingRequestDto)) {
			logger.error(ApiConstant.INVALID_SLOT_TIME);
			return false;
		} else if (bookingRequestDto.getSlotToTime().isBefore(bookingRequestDto.getSlotFromTime())) {
			logger.error(ApiConstant.INVALID_TIME);
			return false;
		} else if (!isBookingExist(bookingRequestDto)) {
			logger.error(ApiConstant.BOOKING_EXIST);
			return false;
		}
		return true;
	}

	@Transactional
	private boolean isValidSlotFromAndToTime(BookingRequestDto bookingRequestDto) {
		Optional<Slot> slot = slotService.getSlotBySlotId(bookingRequestDto.getSlotId());

		if (slot.isPresent()) {
			return bookingRequestDto.getSlotFromTime().isBefore(slot.get().getSlotFromTime())
					|| bookingRequestDto.getSlotToTime().isAfter(slot.get().getSlotToTime());
		}
		return true;
	}

	@Transactional
	private boolean isValidSlot(Long slotId) {
		Optional<Slot> slot = slotService.getSlotBySlotId(slotId);
		return slot.isPresent();
	}

	@Transactional
	private boolean isBookingExist(BookingRequestDto bookingRequestDto) {
		List<Booking> bookings = bookingService.getBookingByDateFromTimeAndToTime(bookingRequestDto.getSlotDate(),
				bookingRequestDto.getSlotFromTime(), bookingRequestDto.getSlotToTime());
		return bookings.isEmpty();
	}

	@Transactional
	private boolean isUserExist(Long userId) {
		Optional<User> user = userService.getUserByUserId(userId);
		return user.isPresent();
	}
}
