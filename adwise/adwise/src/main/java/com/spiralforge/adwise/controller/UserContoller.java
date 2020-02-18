package com.spiralforge.adwise.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.AddSlotRequestDto;
import com.spiralforge.adwise.dto.BookedResponseDto;
import com.spiralforge.adwise.dto.LoginRequestDto;
import com.spiralforge.adwise.dto.LoginResponseDto;
import com.spiralforge.adwise.dto.ResponseDto;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.exception.UserNotFoundException;
import com.spiralforge.adwise.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class UserContoller {

	private static Logger logger = LoggerFactory.getLogger(PlanController.class);

	@Autowired
	private UserService userService;

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-17. In this method all the booked slots are fetched from
	 *        database.
	 * @param userId for checking whether user is admin or sales person.
	 * @param date   for particular date
	 * @return list of booked slots on that particular date.
	 * @throws UserNotFoundException if user is not available.
	 * @throws SlotNotFoundException if slot is unavailable.
	 */
	@GetMapping
	public ResponseEntity<List<BookedResponseDto>> getBookedSlots(@Valid @RequestParam Long userId,
			@RequestParam String date) throws UserNotFoundException, SlotNotFoundException {
		List<BookedResponseDto> responseDto = userService.getBookedSlots(userId, date);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * @author Muthu
	 *
	 *         Method is used for login for the salesperson and admin
	 *
	 * @param loginRequestDto which has the input as mobile number and password
	 * @return LoginResponseDto which has the details of the user that includes
	 *         name,role and id
	 * @throws UserNotFoundException is called when the credentials are invalid
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> checkLogin(@Valid @RequestBody LoginRequestDto loginRequestDto)
			throws UserNotFoundException {
		logger.info("For checking whether the person is staff or a customer");
		LoginResponseDto loginResponse = userService.checkLogin(loginRequestDto);
		logger.info(ApiConstant.LOGIN_SUCCESS);
		loginResponse.setMessage(ApiConstant.LOGIN_SUCCESS);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	/**
	 * @author Muthu
	 *
	 *         Method is used to add a slot by the admin
	 *
	 * @param userId            the key which is used to identify a user
	 * @param addSlotRequestDto which has slot date,slot from time,plan id and slot
	 *                          to time
	 * @return ResponseDto which returns success/failure message with their status
	 *         code
	 */
	@PostMapping("/{userId}/slot")
	public ResponseEntity<ResponseDto> addSlot(@PathVariable(value = "userId") Long userId,
			@RequestBody AddSlotRequestDto addSlotRequestDto) {
		return new ResponseEntity<>(userService.addSlot(userId, addSlotRequestDto), HttpStatus.OK);
	}

}
