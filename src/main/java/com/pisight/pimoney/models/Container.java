package com.pisight.pimoney.models;

public abstract class Container {
	
	public static final String TAG_BANK = "bank";
	public static final String TAG_CARD = "card";
	public static final String TAG_LOAN = "loan";
	
	private String accountHolder = "";
	private String branch = "";
	private String currency = "";
	private String tag = null;
	private String accountHash = "";
	private String bankId = "";
	
	
	
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

	public String getTag(){
		return tag;
		
	}
	
	protected void setTag(String tag){
		this.tag = tag;
	}

	/**
	 * @return the accountHash
	 */
	public String getAccountHash() {
		return accountHash;
	}

	/**
	 * @param accountHash the accountHash to set
	 */
	public void setAccountHash(String accountHash) {
		this.accountHash = accountHash;
	}

	/**
	 * @return the bankId
	 */
	public String getBankId() {
		return bankId;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	

}
