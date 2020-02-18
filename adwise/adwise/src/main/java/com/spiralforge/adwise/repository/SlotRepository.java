package com.spiralforge.adwise.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spiralforge.adwise.entity.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
	
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	Optional<Slot> findById(Long slotId);
	
	Optional<Slot> findBySlotId(Long slotId);

	Optional<Slot> findBySlotDateAndSlotFromTimeAndSlotToTime(LocalDate slotDate, LocalTime slotFromTime,
			LocalTime slotToTime);

	@Query("select s from Slot s where s.slotDate=:slotDate and (( :slotFromTime between s.slotFromTime and s.slotToTime) or (:slotToTime between s.slotFromTime and s.slotToTime) or (:slotFromTime<s.slotFromTime and  :slotToTime >s.slotToTime)  )")
	List<Slot> getSlotByDateFromTimeToTime(@NotNull @Param("slotDate") LocalDate slotDate,
			@NotNull @Param("slotFromTime") LocalTime slotFromTime, @NotNull @Param("slotToTime") LocalTime slotToTime);

	List<Slot> findAllBySlotDate(LocalDate slotDate);

	List<Slot> findBySlotDate(LocalDate localDate);
}
