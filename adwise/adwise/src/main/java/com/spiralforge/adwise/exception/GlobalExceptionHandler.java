package com.spiralforge.adwise.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.spiralforge.adwise.constants.ApiConstant;
import com.spiralforge.adwise.dto.ExceptionResponseDto;
import com.spiralforge.adwise.dto.ResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * @description Handle NullPointer Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleNullPointerExceptions(NullPointerException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.NO_ELEMENT_FOUND_CODE, defaultMessage);
	}

	/**
	 * @description Handle MethodArgumentNotValidException
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponseDto handleValidationError(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		FieldError fieldError = bindingResult.getFieldError();
		String defaultMessage = fieldError.getDefaultMessage();
		return new ExceptionResponseDto(ApiConstant.VALIDATION_FAILED_CODE, defaultMessage);
	}

	/**
	 * @description Handle Runtime Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleAllRuntimeExceptions(RuntimeException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR_CODE, defaultMessage);
	}

	/**
	 * @description All Handle Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleAllExceptions(Exception exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR_CODE, defaultMessage);
	}

	/**
	 * @description All Handle Exception
	 *
	 * @param exception
	 * @return ExceptionResponseDto
	 */
	@ExceptionHandler(ValidationFailedException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleValidationFailedException(ValidationFailedException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.INTERNAL_SERVER_ERROR_CODE, defaultMessage);
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public final ExceptionResponseDto handleOptimisticLockExceptions(
			ObjectOptimisticLockingFailureException exception) {
		String defaultMessage = exception.getMessage();
		return new ExceptionResponseDto(ApiConstant.UPDATE_FAILED_CODE, defaultMessage);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ResponseDto> userNotFoundException() {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(ApiConstant.LOGIN_ERROR);
		responseDto.setStatusCode(ApiConstant.LOGIN_FAILURE_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
	}

	@ExceptionHandler(SlotNotFoundException.class)
	public ResponseEntity<ResponseDto> slotNotFoundException() {
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(ApiConstant.SLOT_NOTFOUND);
		responseDto.setStatusCode(ApiConstant.SLOT_NOT_FOUND_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
	}
}
