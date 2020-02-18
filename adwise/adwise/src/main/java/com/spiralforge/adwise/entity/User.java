package com.spiralforge.adwise.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.spiralforge.adwise.util.AddWiseEnum.Role;

import lombok.Data;

@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String userName;
	private Long mobileNumber;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
}
