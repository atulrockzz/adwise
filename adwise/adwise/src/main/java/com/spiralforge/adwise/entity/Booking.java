package com.spiralforge.adwise.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	private LocalTime slotFromTime;
	private LocalTime slotToTime;
	private Double price;
	private LocalDate slotDate;
	private String customerName;	
	
	@OneToOne
	@JoinColumn(name = "slot_id")
	private Slot slot;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
