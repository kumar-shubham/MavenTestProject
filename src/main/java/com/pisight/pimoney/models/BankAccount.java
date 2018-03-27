package com.pisight.pimoney.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class BankAccount extends Container implements Serializable{

	private static final long serialVersionUID = 6589097464370367681L;


	public BankAccount(){
		setTag(Constants.TAG_BANK);
	}

	public BankAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_BANK);
		setProperties(properties);
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}


	private String accountNumber = "";
	
	private String accountName = "";

	private String accountBalance = "";

	private String billDate = "";
	
	private String availableBalance = null;
	
	private String accountClassification = null;
	
	private String currentBalance = null;
	
	private String interestRate = null;
	
	private String nickname = null;
	
	private String accountType = null;
	
	private String overdraftLimit = null;
	
	// this index shows whether the account has account details or it is just for 
	// process completion
	private int usability = 1;

	private List<BankTransaction> transactions = new ArrayList<BankTransaction>();


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
		if(StringUtils.isNotEmpty(accountNumber)){
			this.accountNumber = accountNumber;
		}
	}

	/**
	 * @return the accountBalance
	 */
	public String getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance the accountBalance to set
	 */
	public void setAccountBalance(String accountBalance) {
		if(StringUtils.isNotEmpty(accountBalance)){
			this.accountBalance = accountBalance;
		}
	}

	/**
	 * @return the billDate
	 */
	public String getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(String billDate) {
		if(StringUtils.isNotEmpty(billDate)){
			this.billDate = billDate;
		}
	}

	public void setBillDate(String billDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(billDate)){
			this.billDate = AccountUtil.convertToDefaultDateFormat(billDate, dateFormat);
		}
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		if(StringUtils.isNotEmpty(accountName)){
			this.accountName = accountName;
		}
	}

	/**
	 * @return the transactions
	 */
	public List<BankTransaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void addTransaction(BankTransaction transaction) {
		if(transaction != null){
			this.transactions.add(transaction);
		}
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<BankTransaction> transactions) {
		this.transactions = transactions;
	}

	public void setHash() {
		String hash = AccountUtil.generateHash(this, getProperties());
		setAccountHash(hash);
	}

	/**
	 * @return the availableBalance
	 */
	public String getAvailableBalance() {
		return availableBalance;
	}

	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	/**
	 * @return the accountClassification
	 */
	public String getAccountClassification() {
		return accountClassification;
	}

	/**
	 * @param accountClassification the accountClassification to set
	 */
	public void setAccountClassification(String accountClassification) {
		this.accountClassification = accountClassification;
	}

	/**
	 * @return the currentBalance
	 */
	public String getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance the currentBalance to set
	 */
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @return the interestRate
	 */
	public String getInterestRate() {
		return interestRate;
	}

	/**
	 * @param interestRate the interestRate to set
	 */
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the overdraftLimit
	 */
	public String getOverdraftLimit() {
		return overdraftLimit;
	}

	/**
	 * @param overdraftLimit the overdraftLimit to set
	 */
	public void setOverdraftLimit(String overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}

	/**
	 * @return the usability
	 */
	public int getUsability() {
		return usability;
	}

	/**
	 * @param usability the usability to set
	 */
	public void setUsability(int usability) {
		this.usability = usability;
	}
	
}
