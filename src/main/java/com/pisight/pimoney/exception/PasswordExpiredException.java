package com.pisight.pimoney.exception;

public class PasswordExpiredException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5725009864231250325L;
	
	private static final int ERROR_CODE = 203;

	public PasswordExpiredException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
