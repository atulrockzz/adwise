package com.spiralforge.adwise.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spiralforge.adwise.entity.Booking;
import com.spiralforge.adwise.entity.Slot;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	/**
	 * @author Sujal
	 * 
	 *         This method is used to return the booking which exist on that slot
	 *         time
	 * @param slotDate
	 * @param slotFromTime
	 * @param slotToTime
	 * @return List<Booking> list of booking
	 */
	@Query("select b from Booking b where b.slotDate=:slotDate and (( :slotFromTime between b.slotFromTime and b.slotToTime) or (:slotToTime between b.slotFromTime and b.slotToTime) or (:slotFromTime<b.slotFromTime and  :slotToTime >b.slotToTime))")
	List<Booking> getBookingByDateFromTimeAndToTime(@NotNull @Param("slotDate") LocalDate slotDate,
			@NotNull @Param("slotFromTime") LocalTime slotFromTime, @NotNull @Param("slotToTime") LocalTime slotToTime);

	List<Booking> findBySlotDateAndSlot(LocalDate localDate, Slot slot);

}
