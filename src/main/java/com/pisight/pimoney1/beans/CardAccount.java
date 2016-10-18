package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.List;

public class CardAccount extends Container {
	
	public CardAccount(){
		setTag(Container.TAG_CARD);
	}

	private String  accountNumber = "";
	private String totalLimit = "";
	private String billDate = "";
	private String dueDate = "";
	private String amountDue = "";
	private String minAmountDue = "";
	private String AccountName = "";
	private String availableCredit = "";
	private String lastStatementBalance = "";
	private String userId = "";
	
	private List<CardTransaction> transactions = new ArrayList<CardTransaction>();
	
	
	
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<CardTransaction> getTransactions() {
		return transactions;
	}
	public void addTransaction(CardTransaction ct) {
		transactions.add(ct);
	}
	public String getLastStatementBalance() {
		return lastStatementBalance;
	}
	public void setLastStatementBalance(String lastStatementBalance) {
		this.lastStatementBalance = lastStatementBalance;
	}
	public String getAvailableCredit() {
		return availableCredit;
	}
	public void setAvailableCredit(String availableCredit) {
		this.availableCredit = availableCredit;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getTotalLimit() {
		return totalLimit;
	}
	public void setTotalLimit(String totalLimit) {
		this.totalLimit = totalLimit;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue;
	}
	public String getMinAmountDue() {
		return minAmountDue;
	}
	public void setMinAmountDue(String minAmountDue) {
		this.minAmountDue = minAmountDue;
	}
	
	
}
