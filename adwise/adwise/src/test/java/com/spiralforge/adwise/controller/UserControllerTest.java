package com.spiralforge.adwise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.AddSlotRequestDto;
import com.spiralforge.adwise.dto.BookedResponseDto;
import com.spiralforge.adwise.dto.LoginRequestDto;
import com.spiralforge.adwise.dto.LoginResponseDto;
import com.spiralforge.adwise.dto.ResponseDto;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.exception.UserNotFoundException;
import com.spiralforge.adwise.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

	@InjectMocks
	UserContoller userController;

	@Mock
	UserService userService;

	LoginRequestDto loginRequestDto = null;
	LoginResponseDto loginResponse = null;
	AddSlotRequestDto addSlot = null;
	User user = null;
	ResponseDto responseDto= new ResponseDto();
	List<BookedResponseDto> responseDtos = new ArrayList<>();

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("muthu123");

		loginResponse = new LoginResponseDto();

		user = new User();
		user.setUserId(1L);
		user.setMobileNumber(9876543210L);
		user.setPassword("muthu123");

		addSlot = new AddSlotRequestDto();
		responseDto.setMessage(ApiConstant.ADDSLOT_SUCCESS);
	}

	@Test
	public void testCheckLoginPositive() throws UserNotFoundException {
		Mockito.when(userService.checkLogin(loginRequestDto)).thenReturn(loginResponse);
		ResponseEntity<LoginResponseDto> response = userController.checkLogin(loginRequestDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testAddSlotSuccess() {
		Mockito.when(userService.addSlot(1L, addSlot)).thenReturn(responseDto);
		ResponseEntity<ResponseDto> response = userController.addSlot(1L, addSlot);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetBookedSlots() throws UserNotFoundException, SlotNotFoundException {
		Mockito.when(userService.getBookedSlots(1L, "2020-02-17")).thenReturn(responseDtos);
		ResponseEntity<List<BookedResponseDto>> list = userController.getBookedSlots(1L, "2020-02-17");
		assertEquals(200, list.getStatusCodeValue());
	}

}
