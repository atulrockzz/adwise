package com.spiralforge.adwise.service;

import java.util.List;
import java.util.Optional;

import com.spiralforge.adwise.dto.AvailableSlotList;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.exception.SlotNotFoundException;

public interface SlotService {

	Optional<Slot> getSlotBySlotId(Long slotId);

	List<AvailableSlotList> getAvailableSlots(String date) throws SlotNotFoundException;

}
