package com.pisight.pimoney.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class CardAccount extends Container implements Serializable{

	private static final long serialVersionUID = 8740729293970100588L;


	public CardAccount(){
		setTag(Constants.TAG_CARD);
	}

	public CardAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_CARD);
		setProperties(properties);
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}

	private String accountNumber = "";

	private String accountName = "";

	private String totalLimit = "";

	private String billDate = "";

	private String dueDate = "";

	private String amountDue = "";

	private String minAmountDue = "";

	private String availableCredit = "";

	private String lastStatementBalance = "";

	private String apr = null;

	private String availableCash = null;

	private String balance = null;

	private String accountClassification = null;

	private String lastPaymentAmount = null;

	private String lastPaymentDate  = null;

	private String nickname = null;

	private String runningBalance = null;

	private String totalCashLimit = null;

	private String accountType = null;

	private String frequncy = null;

	// this index shows whether the account has account details or it is just for 
	// process completion
	private int usability = 1;

	private List<CardTransaction> transactions = new ArrayList<CardTransaction>();


	/**
	 * @return accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 */
	public void setAccountNumber(String accountNumber) {
		if(StringUtils.isNotEmpty(accountNumber)){
			this.accountNumber = accountNumber;
		}
	}

	public List<CardTransaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(CardTransaction transaction) {
		if(transaction != null){
			transaction.setProperties(getProperties());
			this.transactions.add(transaction);
		}
	}

	public String getLastStatementBalance() {
		return lastStatementBalance;
	}
	public void setLastStatementBalance(String lastStatementBalance) {
		if(StringUtils.isNotEmpty(lastStatementBalance)){
			this.lastStatementBalance = lastStatementBalance;
		}
	}

	public String getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(String availableCredit) {
		if(StringUtils.isNotEmpty(availableCredit)){
			this.availableCredit = availableCredit;
		}
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		if(StringUtils.isNotEmpty(accountName)){
			this.accountName = accountName;
		}
	}
	public String getTotalLimit() {
		return totalLimit;
	}
	public void setTotalLimit(String totalLimit) {
		if(StringUtils.isNotEmpty(totalLimit)){
			this.totalLimit = totalLimit;
		}
	}
	public String getBillDate() {
		return billDate;
	}
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
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		if(StringUtils.isNotEmpty(dueDate)){
			this.dueDate = dueDate;
		}
	}
	public void setDueDate(String dueDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(dueDate)){
			this.dueDate = AccountUtil.convertToDefaultDateFormat(dueDate, dateFormat);
		}
	}
	public String getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(String amountDue) {
		if(StringUtils.isNotEmpty(amountDue)){
			this.amountDue = amountDue;
		}
	}
	public String getMinAmountDue() {
		return minAmountDue;
	}
	public void setMinAmountDue(String minAmountDue) {
		if(StringUtils.isNotEmpty(minAmountDue)){
			this.minAmountDue = minAmountDue;
		}
	}

	public void setHash() {
		String hash = AccountUtil.generateHash(this, getProperties());
		setAccountHash(hash);
	}

	/**
	 * @return the apr
	 */
	public String getApr() {
		return apr;
	}

	/**
	 * @param apr the apr to set
	 */
	public void setApr(String apr) {
		this.apr = apr;
	}

	/**
	 * @return the availableCash
	 */
	public String getAvailableCash() {
		return availableCash;
	}

	/**
	 * @param availableCash the availableCash to set
	 */
	public void setAvailableCash(String availableCash) {
		this.availableCash = availableCash;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
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
	 * @return the lastPaymentAmount
	 */
	public String getLastPaymentAmount() {
		return lastPaymentAmount;
	}

	/**
	 * @param lastPaymentAmount the lastPaymentAmount to set
	 */
	public void setLastPaymentAmount(String lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}

	/**
	 * @return the lastPaymentDate
	 */
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}

	/**
	 * @param lastPaymentDate the lastPaymentDate to set
	 */
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
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
	 * @return the runningBalance
	 */
	public String getRunningBalance() {
		return runningBalance;
	}

	/**
	 * @param runningBalance the runningBalance to set
	 */
	public void setRunningBalance(String runningBalance) {
		this.runningBalance = runningBalance;
	}

	/**
	 * @return the totalCashLimit
	 */
	public String getTotalCashLimit() {
		return totalCashLimit;
	}

	/**
	 * @param totalCashLimit the totalCashLimit to set
	 */
	public void setTotalCashLimit(String totalCashLimit) {
		this.totalCashLimit = totalCashLimit;
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
	 * @return the frequncy
	 */
	public String getFrequncy() {
		return frequncy;
	}

	/**
	 * @param frequncy the frequncy to set
	 */
	public void setFrequncy(String frequncy) {
		this.frequncy = frequncy;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<CardTransaction> transactions) {
		this.transactions = transactions;
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
