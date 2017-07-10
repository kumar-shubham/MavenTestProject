package com.pisight.pimoney.exception;

public class SiteException extends ACAException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -793266455442570777L;
	
	private static final int ERROR_CODE = 300;

	public SiteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
