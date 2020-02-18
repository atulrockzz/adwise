package com.spiralforge.adwise.exception;

public class ValidationFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * The exception is used to validate the slot detail for a doctor if the date is
	 * not correct or the slot time is not correct it will throw exception.
	 * 
	 * @param message if validation failed the message will be thrown.
	 */
	public ValidationFailedException(String message) {
		super(message);
	}

}
