package com.pisight.pimoney.exception;

public class AccountLockedException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6934361472289491079L;
	
	private static final int ERROR_CODE = 202;

	public AccountLockedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
