package com.pisight.pimoney.exception;

public class NoAccountFoundException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3794128284216845148L;
	private static final int ERROR_CODE = 205;

	public NoAccountFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
