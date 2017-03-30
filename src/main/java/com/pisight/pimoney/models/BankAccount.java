package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.EncodeDecodeUtil;
import com.pisight.pimoney.beans.ParserUtility;
import com.pisight.pimoney.constants.Constants;

public class BankAccount extends Container {
	
//	public BankAccount(){
//		setTag(Constants.TAG_BANK);
//	}
//	
	public BankAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_BANK);
		this.properties = properties;
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private String accountNumber = "";
	
	private String accountBalance = "";
	
	private String billDate = "";
	
	private String accountName = "";
	
	private String fingerprint = null;
	
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
		bt.setProperties(properties);
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
		String fingerprintValue = properties.get(Constants.INSTITUTION_CODE) + accountNumber + getAccountBalance()
				+ getCurrency() + billDate;
		fingerprint = EncodeDecodeUtil.encodeString(fingerprintValue);
	}

	public void setHash() {
		String hash = ParserUtility.generateHash(this, properties);
		setAccountHash(hash);
	}

}
