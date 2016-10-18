package com.pisight.pimoney1.beans;

public class LoanTransaction {
	
	public static final String TRANSACTION_TYPE_DEBIT = "debit";
	public static final String TRANSACTION_TYPE_CREDIT = "credit";
	
	private String transDate = "";
	private String description = "";
	private String amount = "";
	private String transactionType = "";
	
	
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	

}
