package com.spiralforge.adwise.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.repository.BookingRepository;
import com.spiralforge.adwise.repository.SlotRepository;
import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

@Service
public class BookingServiceImpl implements BookingService {

	private static Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private SlotRepository slotRepository;

	@Autowired
	private PlanService planService;

	@Autowired
	private UserService userService;
	
	/**
	 * 
	 */
	@Transactional
	@Override
	public Optional<Booking> slotBooking(@NotNull Long userId, @Valid BookingRequestDto bookingRequestDto) {
		logger.info("inside booking service");

		Optional<Slot> slotOptional = slotRepository.findById(bookingRequestDto.getSlotId());
		Optional<User> user= userService.getUserByUserId(userId);
		if (!slotOptional.isPresent())
			return Optional.empty();
		if (!user.isPresent())
			return Optional.empty();
		Slot slot = slotOptional.get();
		
		Booking booking = new Booking();
		BeanUtils.copyProperties(bookingRequestDto, booking);
		booking.setSlot(slot);
		booking.setPrice(calculatePrice(bookingRequestDto));
		booking.setUser(user.get());
		Booking booking1 = bookingRepository.save(booking);
		if (!ObjectUtils.isEmpty(booking1)) {
			if (bookingRequestDto.getSlotToTime().equals(slot.getSlotToTime()))
				slot.setSlotStatus(SlotStatus.BOOKED);
			else
				slot.setSlotStatus(SlotStatus.PARTIAL_AVAILABLE);
			slotRepository.save(slot);
		}
		return Optional.of(booking);
	}

	/**
	 * 
	 * @param bookingRequestDto
	 * @return
	 */
	private Double calculatePrice(@Valid BookingRequestDto bookingRequestDto) {
		Optional<Plan> optionalPlan = planService.getPlanByPlanId(bookingRequestDto.getPlanId());
		if (optionalPlan.isPresent()) {
			Plan plan = optionalPlan.get();
			Long timeDifference = Duration
					.between(bookingRequestDto.getSlotFromTime(), bookingRequestDto.getSlotToTime()).getSeconds();
			logger.info("inside price calculation");
			return timeDifference * plan.getPlanRate();

		}
		return 0D;
	}

	/**
	 * 
	 */
	@Override
	public List<Booking> getBookingByDateFromTimeAndToTime(LocalDate slotDate, LocalTime slotFromTime,
			LocalTime slotToTime) {
		logger.info("inside get booking detail");
		return bookingRepository.getBookingByDateFromTimeAndToTime(slotDate, slotFromTime, slotToTime);
	}

}
