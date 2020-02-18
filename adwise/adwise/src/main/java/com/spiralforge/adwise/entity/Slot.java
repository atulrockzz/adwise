package com.spiralforge.adwise.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import com.spiralforge.adwise.util.AddWiseEnum.SlotStatus;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = "slotSequence", initialValue = 10000, allocationSize = 1)
public class Slot {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slotSequence")
	private Long slotId;
	private LocalDate slotDate;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	
	@Enumerated(EnumType.STRING)
	private SlotStatus slotStatus;

	@OneToOne
	@JoinColumn(name = "plan_id")
	private Plan plan;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Version
	private Long version;

}
