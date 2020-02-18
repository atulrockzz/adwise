package com.spiralforge.adwise.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spiralforge.adwise.dto.BookingRequestDto;
import com.spiralforge.adwise.entity.Booking;

public interface BookingService {

	Optional<Booking> slotBooking(@NotNull Long userId, @Valid BookingRequestDto bookingRequestDto);

	List<Booking> getBookingByDateFromTimeAndToTime(LocalDate slotDate, LocalTime slotFromTime, LocalTime slotToTime);

}
