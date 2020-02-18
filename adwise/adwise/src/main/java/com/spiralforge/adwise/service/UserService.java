package com.spiralforge.adwise.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.spiralforge.adwise.dto.AddSlotRequestDto;
import com.spiralforge.adwise.dto.BookedResponseDto;
import com.spiralforge.adwise.dto.LoginRequestDto;
import com.spiralforge.adwise.dto.LoginResponseDto;
import com.spiralforge.adwise.dto.ResponseDto;
import com.spiralforge.adwise.entity.User;
import com.spiralforge.adwise.exception.SlotNotFoundException;
import com.spiralforge.adwise.exception.UserNotFoundException;

public interface UserService {

	Optional<User> getUserByUserId(Long userId);

	List<BookedResponseDto> getBookedSlots(@Valid Long userId, String date)
			throws UserNotFoundException, SlotNotFoundException;

	LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws UserNotFoundException;

	ResponseDto addSlot(Long userId, AddSlotRequestDto addSlotRequestDto);

}
