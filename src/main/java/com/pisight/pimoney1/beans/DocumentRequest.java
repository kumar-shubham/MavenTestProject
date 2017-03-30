package com.pisight.pimoney1.beans;

public class DocumentRequest {
	
	private String name = null;
	private String container = null;
	private String locale = null;
	private String type = null;
	private String docByte = null;
	private String userId = null;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContainer() {
		return container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDocByte() {
		return docByte;
	}
	public void setDocByte(String docByte) {
		this.docByte = docByte;
	}
	

}
