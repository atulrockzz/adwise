package com.spiralforge.adwise.constants;

/**
 * 
 * @author Muthu
 *
 */
public class ApiConstant {

	private ApiConstant() {
	}

	public static final String LOGIN_ERROR = "Please enter valid mobile number and password";
	public static final String LOGIN_SUCCESS = "You are successfully logged in";

	public static final String SUCCESS = "SUCCESSFUL";
	public static final String FAILED = "FAILED";

	public static final Integer SUCCESS_CODE = 200;
	public static final Integer FAILURE_CODE = 404;
	public static final Integer NO_CONTENT_CODE = 204;
	public static final String LOGIN_FAILURE_CODE = "DCNC0001";
	public static final String USER_NOTFOUND_CODE = "DCNC0002";
	public static final String PLAN_NOT_FOUND_CODE = "DCNC0003";
	public static final String ADDSLOT_SUCCESS_CODE = "DCNC0004";
	public static final String SLOTALREADYBOOKED_CODE = "DCNC0005";
	public static final String SLOT_NOT_FOUND_CODE = "DCNC0009";
	public static final String NO_ELEMENT_FOUND_CODE = "DCNC0010";
	public static final String VALIDATION_FAILED_CODE = "DCNC0011";
	public static final String UPDATE_FAILED_CODE = "DCNC0012";
	public static final String DATE_INVALID_CODE = "DCNC0013";

	public static final String INTERNAL_SERVER_ERROR_CODE = "DCNC012";

	public static final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
	public static final String VALIDATION_FAILED = "VALIDATION FAILED";
	public static final String NO_ELEMENT_FOUND = "NO ELEMENT FOUND";
	public static final String INVALID_TIME = "To time will be greater than from time";
	public static final String INVALID_DATA = "Data not provided properly";
	public static final String INVALID_USER = "User is not found";
	public static final String SLOT_NOT_FOUND = "Slot not found";
	public static final String BOOKING_EXIST = "Booking already exist";
	public static final String INVALID_SLOT_TIME = "Booking slot time cannot be beyond slot time";
	public static final String UPDATE_FAILED = "Slot data modified by other user";
	public static final String BOOKING_SUCCESS = "Slot booking has done succeessfully";

	public static final String SLOT_NOT_FOUND_EXCEPTION = "No slot available";
	public static final String USER_NOT_FOUND_EXCEPTION = "Invalid user";

	public static final String EMPTY_CUSTOMERINPUT_MESSAGE = "Field can't be left blank";

	public static final String AVAILABLESLOT_EMPTY_LIST = "There are no slots available";
	public static final String AVAILABLE_SLOT_LIST = "The slot list which are available is displayed";
	public static final String SLOT_EMPTY = "There are no slots available for that particular date";
	public static final String SLOT_NOTFOUND = "There are no slots found";
	public static final String BOOKING_MESSAGE = "There are no bookings found";
	public static final String PLAN_NOTFOUND = "There are no plan found";
	public static final String NO_RECORD_UPDATED = "No record saved in database";

	public static final String ADDSLOT_SUCCESS = "Slot has been successfully added";
	public static final String SLOT_ALREADYBOOKED = "Slot has been already booked";
	public static final String PLAN_NOT_FOUND = "Please check the plan";
	public static final String USER_NOTFOUND = "No user found";

	public static final String DATE_INVALID_MESSAGE = "Please enter a valid date";
	public static final String TIME_INVALID_CODE = "DCNC0014";
	public static final String TIME_INVALID_MESSAGE = "Please enter a valid time.From time cannot be greater than to time";
	public static final String PLAN_SLOT_CONFICT_MESSAGE = "No such time exists for that plan";
	public static final String PLAN_SLOT_CONFLICT_CODE = "DCNC0016";

}
