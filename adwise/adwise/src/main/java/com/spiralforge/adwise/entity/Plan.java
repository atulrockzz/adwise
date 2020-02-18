package com.spiralforge.adwise.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long planId;
	private String planName;
	private Double planRate;
	private LocalTime planFromTime;
	private LocalTime planToTime;
	private LocalDate planDate;
}
