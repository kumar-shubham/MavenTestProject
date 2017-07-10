package com.pisight.pimoney.exception;

public class MFATimeoutException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2520454527780137835L;
	private static final int ERROR_CODE = 204;
	
	public MFATimeoutException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
