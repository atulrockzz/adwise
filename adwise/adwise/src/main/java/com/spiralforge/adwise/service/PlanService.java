package com.spiralforge.adwise.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.spiralforge.adwise.entity.Plan;
import com.spiralforge.adwise.exception.ValidationFailedException;

public interface PlanService {

	Optional<Plan> getPlanByDateFromTimeToTime(@NotNull LocalDate date,@NotNull LocalTime fromTime,@NotNull LocalTime toTime) throws ValidationFailedException;

	Optional<Plan> getPlanByPlanId(@Valid Long planId);

}
