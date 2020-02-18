package com.spiralforge.adwise.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.AvailableSlotList;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.entity.Slot;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.repository.PlanRepository;
import com.spiralforge.adwise.repository.SlotRepository;

@Service
public class SlotServiceImpl implements SlotService {
	
	private static final Logger logger = LoggerFactory.getLogger(SlotServiceImpl.class);

	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private SlotRepository slotRepository;

	@Override
	public Optional<Slot> getSlotBySlotId(Long slotId) {
		return slotRepository.findBySlotId(slotId);
	}

	
	/**
	 * @author Muthu
	 *
	 *         Method is used to get the list of available slots on a particular
	 *         date
	 *
	 * @param slotDate which has the input as a date
	 * @return AvailableSlotResponseDto which has the list of slots with a message
	 * @throws SlotNotFoundException is called when no particular slots found for
	 *                               that date
	 */
	@Override
	public List<AvailableSlotList> getAvailableSlots(String date) throws SlotNotFoundException {
		List<AvailableSlotList> slotList = new ArrayList<>();
		LocalDate slotDate = LocalDate.parse(date);
		List<Slot> slotDateList = slotRepository.findAllBySlotDate(slotDate);
		if (slotDateList.isEmpty()) {
			logger.error(ApiConstant.SLOT_EMPTY);
			throw new SlotNotFoundException(ApiConstant.SLOT_EMPTY);
		}
		slotDateList.forEach(slotDetails -> {
			AvailableSlotList availableSlotList = new AvailableSlotList();
			availableSlotList.setSlotDate(slotDate);
			BeanUtils.copyProperties(slotDetails, availableSlotList);
			Plan plan = planRepository.findByPlanId(slotDetails.getPlan().getPlanId());
			BeanUtils.copyProperties(plan, availableSlotList);
			slotList.add(availableSlotList);
		});
		return slotList;
	}

}
