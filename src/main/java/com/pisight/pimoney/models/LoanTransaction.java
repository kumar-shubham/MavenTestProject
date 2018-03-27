package com.pisight.pimoney.models;

import java.io.Serializable;

import com.pisight.pimoney.constants.Constants;

public class LoanTransaction extends TransactionBase implements Serializable{
	
	private static final long serialVersionUID = 1176764273639940541L;
	public static final String TRANSACTION_TYPE_DEBIT = "debit";
	public static final String TRANSACTION_TYPE_CREDIT = "credit";
	
	private String transDate = "";
	private String postDate = "";
	private String description = "";
	private String amount = "";
	private String transactionType = "";
	private String accountNumber = "";
	private String currency = "";
	private String dateFormat = "";
	
	private static final String tag = Constants.TAG_LOAN;
	
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	@Override
	public String getTag() {
		return tag;
	}
	

}
