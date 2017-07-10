package com.pisight.pimoney.exception;

public class SiteChangedException extends SiteException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8300457820208915466L;
	
	private static final int ERROR_CODE = 302;

	public SiteChangedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
