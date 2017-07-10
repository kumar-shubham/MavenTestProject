package com.pisight.pimoney.exception;

public class SiteUnavailableException extends SiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 466742498209897541L;
	
	private static final int ERROR_CODE = 301;

	public SiteUnavailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
