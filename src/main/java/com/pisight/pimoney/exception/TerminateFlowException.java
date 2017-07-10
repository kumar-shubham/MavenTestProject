package com.pisight.pimoney.exception;

public class TerminateFlowException extends UserException {

	private static final long serialVersionUID = 5199591024307740158L;
	
	private static final int ERROR_CODE = 206;

	public TerminateFlowException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public int getErrorCode(){
		return ERROR_CODE;
	}
}
