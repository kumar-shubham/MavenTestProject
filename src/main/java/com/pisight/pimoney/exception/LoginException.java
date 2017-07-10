package com.pisight.pimoney.exception;

public class LoginException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1464223821543708717L;
	
	private static final int ERROR_CODE = 201;

	public LoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
