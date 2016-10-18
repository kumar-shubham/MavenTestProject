package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.List;

public class BankAccount extends Container {
	
	public BankAccount(){
		setTag(Container.TAG_BANK);
	}
	
	private String accountNumber = "";
	
	private String accountBalance = "";
	
	private String billDate = "";
	
	private String accountName = "";
	
	private List<BankTransaction> transactions = new ArrayList<BankTransaction>();
	
	
	
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public List<BankTransaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(BankTransaction bt) {
		transactions.add(bt);
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}


}
