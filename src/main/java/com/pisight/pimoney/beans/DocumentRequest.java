package com.pisight.pimoney.beans;

public class DocumentRequest {
	
	private String name = null;
	private String container = null;
	private String locale = null;
	private String type = null;
	private String docByte = null;
	private String userId = null;
	private String institutionCode = null;
	private boolean isEncrypted = false;
	private char[] pswd = null;
	
	public boolean isEncrypted() {
		return isEncrypted;
	}
	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	public char[] getPswd() {
		return pswd;
	}
	public void setPswd(String pswd) {
		this.pswd = pswd.toCharArray();
	}
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
	public String getInstitutionCode() {
		return institutionCode;
	}
	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}
}
