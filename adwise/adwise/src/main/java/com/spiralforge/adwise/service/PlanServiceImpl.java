package com.spiralforge.adwise.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.exception.ValidationFailedException;
import com.spiralforge.adwise.repository.PlanRepository;

@Service
public class PlanServiceImpl implements PlanService {

	private static Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

	@Autowired
	private PlanRepository planRepository;

	/**
	 * @author Sujal
	 * 
	 *         Method is used to get the plan based on date, from time and to time
	 * 
	 * @return Plan which has the plan detail that includes the plan id,plan name,
	 *         from time, to time, date and plan rate per second
	 * @throws ValidationFailedException
	 * 
	 */
	@Override
	public Optional<Plan> getPlanByDateFromTimeToTime(@NotNull LocalDate date, @NotNull LocalTime fromTime,
			@NotNull LocalTime toTime) throws ValidationFailedException {
		logger.info("Get a plan");
		if (toTime.isAfter(fromTime)) {
			Pageable sortedByPriceDesc = 
					  PageRequest.of(0, 1, Sort.by("planId").descending());
			List<Plan> plans =planRepository.getPlanByDateFromTimeToTime(date, fromTime, toTime, sortedByPriceDesc);
			if(!plans.isEmpty())
				return Optional.of(plans.get(0));
			else 
				return Optional.empty(); 
		}
		else
			throw new ValidationFailedException(ApiConstant.INVALID_TIME);
	}

	@Override
	public Optional<Plan> getPlanByPlanId(@Valid Long planId) {
		return planRepository.findById(planId);
	}

}
