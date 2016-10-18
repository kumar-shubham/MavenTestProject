package com.pisight.pimoney1.beans;

public abstract class Container {
	
	public static final String TAG_BANK = "bank";
	public static final String TAG_CARD = "card";
	public static final String TAG_LOAN = "loan";
	
	private String accountHolder = "";
	private String branch = "";
	private String currency = "";
	
	
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	private String tag = null;
	
	public String getTag(){
		return tag;
		
	}
	
	protected void setTag(String tag){
		this.tag = tag;
	}

}
