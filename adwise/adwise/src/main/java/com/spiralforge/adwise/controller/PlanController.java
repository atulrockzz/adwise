package com.spiralforge.adwise.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.dto.BookingResponseDto;
import com.spiralforge.adwise.dto.PlanResponseDto;
import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.exception.ValidationFailedException;
import com.spiralforge.adwise.service.BookingService;
import com.spiralforge.adwise.service.PlanService;
import com.spiralforge.adwise.util.BookingValidator;

@RestController
@RequestMapping("/")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class PlanController {

	private static Logger logger = LoggerFactory.getLogger(PlanController.class);

	@Autowired
	private PlanService planService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	@Qualifier("bookingValidator")
	private BookingValidator<Long, BookingRequestDto> bookingValidator;

	/**
	 * @author Sujal
	 * 
	 *         Method is used to get the plan based on date, from time and to time
	 * @param date
	 * @param fromTime
	 * @param toTime
	 * @return PlanResponseDto which has the plan detail that includes the plan
	 *         id,plan name and plan rate per second
	 * @throws ValidationFailedException if the from time and to time is not proper
	 *                                   this exception will be thrown
	 * 
	 */
	@GetMapping("plans")
	public ResponseEntity<PlanResponseDto> getPlanByDateFromTimeToTime(
			@NotNull @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@NotNull @RequestParam("fromTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,
			@NotNull @RequestParam("toTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime)
			throws ValidationFailedException {
		PlanResponseDto planResponseDto = new PlanResponseDto();
		Optional<Plan> plan = planService.getPlanByDateFromTimeToTime(date, fromTime, toTime);
		if (!plan.isPresent()) {
			planResponseDto.setStatusCode(ApiConstant.NO_CONTENT_CODE);
			planResponseDto.setMessage(ApiConstant.NO_ELEMENT_FOUND);
			return new ResponseEntity<>(planResponseDto, HttpStatus.NOT_FOUND);
		} else {
			logger.info(ApiConstant.SUCCESS);
			BeanUtils.copyProperties(plan.get(), planResponseDto);
			planResponseDto.setStatusCode(ApiConstant.SUCCESS_CODE);
			planResponseDto.setMessage(ApiConstant.SUCCESS);
			return new ResponseEntity<>(planResponseDto, HttpStatus.OK);
		}
	}

	/**
	 * @author Sujal
	 * 
	 *         Method is used to book the slot for the add
	 * @param userId
	 * @param bookingRequestDto
	 * @return BookingResponseDto which has the booking id
	 * @throws ValidationFailedException if the from time and to time is not proper
	 *                                   this exception will be thrown
	 * 
	 */
	@PostMapping("users/{userId}/bookings")
	public ResponseEntity<BookingResponseDto> slotBooking(@NotNull @PathVariable("userId") Long userId,
			@Valid @RequestBody List<BookingRequestDto> bookingRequestDtos) {
		BookingResponseDto bookingResponseDto = new BookingResponseDto();

		Integer failedCount = bookingRequestDtos.stream().mapToInt(bookingRequestDto -> {
			Integer failureCount = 0;
			Boolean bookingValidFlag = bookingValidator.validate(userId, bookingRequestDto);
			if (Boolean.FALSE.equals(bookingValidFlag)) {
				failureCount++;
			} else {
				try {
				Optional<Booking> booking = bookingService.slotBooking(userId, bookingRequestDto);
				if (!booking.isPresent())
					failureCount++;
				}catch(ObjectOptimisticLockingFailureException ex) {
					logger.error("Data modified by other user");
					failureCount++;
				}
			}
			return failureCount;
		}).sum();
		
		Integer successCount=bookingRequestDtos.size()-failedCount;
		if (successCount==0) {
			bookingResponseDto.setSuccessCount(successCount);
			bookingResponseDto.setFailureCount(failedCount);
			bookingResponseDto.setStatusCode(ApiConstant.FAILURE_CODE);
			bookingResponseDto.setMessage(ApiConstant.NO_RECORD_UPDATED);
			return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
		}

		logger.info(ApiConstant.BOOKING_SUCCESS);
		bookingResponseDto.setSuccessCount(successCount);
		bookingResponseDto.setFailureCount(failedCount);
		bookingResponseDto.setStatusCode(ApiConstant.SUCCESS_CODE);
		bookingResponseDto.setMessage(ApiConstant.BOOKING_SUCCESS);
		return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);

	}

}
