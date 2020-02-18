package com.spiralforge.adwise.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spiralforge.adwise.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

	/**
	 * @author Sujal
	 * 
	 *         Method is used to fetch the plan based on date, from time and to time
	 * 
	 * @return Plan which has the plan detail that includes the plan id,plan name,
	 *         from time, to time, date and plan rate per second
	 * 
	 */
	@Query("select p from Plan p where p.planDate=:date and ((:fromTime between p.planFromTime and p.planToTime) or (:toTime between p.planFromTime and p.planToTime) or (:fromTime>p.planFromTime and :toTime <p.planToTime)) ")
	List<Plan> getPlanByDateFromTimeToTime(@NotNull @Param("date") LocalDate date,
			@NotNull @Param("fromTime") LocalTime fromTime, @NotNull @Param("toTime") LocalTime toTime, Pageable pageable);

	Plan findByPlanId(Long planId);

	Optional<Plan> findByPlanIdAndPlanDate(Long planId, LocalDate slotDate);
}
