package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.EncodeDecodeUtil;
import com.pisight.pimoney.beans.ParserUtility;
import com.pisight.pimoney.constants.Constants;

public class CardAccount extends Container {
	
//	public CardAccount(){
//		setTag(Constants.TAG_CARD);
//	}
	
	public CardAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_CARD);
		this.properties = properties;
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}

	private String accountNumber = "";
	private String totalLimit = "";
	private String billDate = "";
	private String dueDate = "";
	private String amountDue = "";
	private String minAmountDue = "";
	private String accountName = "";
	private String availableCredit = "";
	private String lastStatementBalance = "";
	private String fingerprint = null;
	
	private List<CardTransaction> transactions = new ArrayList<CardTransaction>();
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	
	
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public List<CardTransaction> getTransactions() {
		return transactions;
	}
	public void addTransaction(CardTransaction ct) {
		ct.setProperties(properties);
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
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	/**
	 * @return the fingerprint
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * @param fingerprint the fingerprint to set
	 */
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	/**
	 * @param fingerprint the fingerprint to set
	 */
	public void setFingerprint() {
		String fingerprintValue = properties.get(Constants.INSTITUTION_CODE) + accountNumber + getAmountDue() +
			getCurrency() + billDate;
		fingerprint = EncodeDecodeUtil.encodeString(fingerprintValue);
	}
	
	public void setHash() {
		String hash = ParserUtility.generateHash(this, properties);
		setAccountHash(hash);
	}
	
}
