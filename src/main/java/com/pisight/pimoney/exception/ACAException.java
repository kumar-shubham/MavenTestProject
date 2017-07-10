package com.pisight.pimoney.exception;

public class ACAException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4119548120554744188L;
	
	private static final int ERROR_CODE = 100;
	
	public ACAException(String message){
		super(message);
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}

}
