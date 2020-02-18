package com.spiralforge.adwise.dto;

import com.spiralforge.adwise.util.AddWiseEnum.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
	private String message;
	private Long userId;
	private String userName;
	private Role role;
}
