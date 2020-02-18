package com.spiralforge.adwise.dto;

import javax.validation.constraints.NotBlank;

import com.spiralforge.adwise.constants.ApiConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
	private Long mobileNumber;
	@NotBlank(message = ApiConstant.EMPTY_CUSTOMERINPUT_MESSAGE)
	private String password;
}
