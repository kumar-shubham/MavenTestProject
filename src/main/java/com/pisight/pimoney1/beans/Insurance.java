package com.pisight.pimoney1.beans;

public class Insurance {
	
	private String instituteName = "";
	private String productName = "";
	private String type = "";
	private String policyNumber = "";
	private String premiumAmount = "";
	private String premiumFrequency = "";
	private String sumAssured = "";
	private String currency = "";
	
	
	
	
	
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(String premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	public String getPremiumFrequency() {
		return premiumFrequency;
	}
	public void setPremiumFrequency(String premiumFrequency) {
		this.premiumFrequency = premiumFrequency;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSumAssured() {
		return sumAssured;
	}
	public void setSumAssured(String sumAssured) {
		this.sumAssured = sumAssured;
	}

	
}
