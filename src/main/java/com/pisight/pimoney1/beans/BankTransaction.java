package com.pisight.pimoney1.beans;

public class BankTransaction {
	
	public static final String TRANSACTION_TYPE_DEBIT = "debit";
	public static final String TRANSACTION_TYPE_CREDIT = "credit";
	
	private String transDate = "";
	private String postDate = "";
	private String description = "";
	private String amount = "";
	private String runningBalance = "";
	private String transactionType = "";
	private String accountNumber = "";
	private String currency = "";
	
	
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRunningBalance() {
		return runningBalance;
	}
	public void setRunningBalance(String runningBalance) {
		this.runningBalance = runningBalance;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
