package com.pisight.pimoney.models;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class BankTransaction extends TransactionBase implements Serializable{

	private static final long serialVersionUID = -1864819570442948595L;
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
	private String dateFormat = "";
	
	private static final String tag = Constants.TAG_BANK;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		if(StringUtils.isNotEmpty(accountNumber)){
			this.accountNumber = accountNumber;
		}
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		if(StringUtils.isNotEmpty(currency)){
			this.currency = currency;
		}
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		if(StringUtils.isNotEmpty(transDate)){
			this.transDate = transDate;
		}
	}
	public void setTransDate(String transDate, String datFormat) throws ParseException {
		if(StringUtils.isNotEmpty(transDate)){
			this.transDate = AccountUtil.convertToDefaultDateFormat(transDate, datFormat);
		}
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		if(StringUtils.isNotEmpty(postDate)){
			this.postDate = postDate;
		}
	}
	public void setPostDate(String postDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(postDate)){
			this.postDate = AccountUtil.convertToDefaultDateFormat(postDate, dateFormat);
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if(StringUtils.isNotEmpty(description)){
			this.description = description;
		}
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		if(StringUtils.isNotEmpty(amount)){
			this.amount = amount;
		}
	}
	
	public void setAmount(String amount, boolean format) {
		if(StringUtils.isNotEmpty(amount)){
			if(format){
				this.amount = AccountUtil.formatAmount(amount);
			}
			else{
				this.amount = amount;
			}
		}
	}
	
	public String getRunningBalance() {
		return runningBalance;
	}
	
	public void setRunningBalance(String runningBalance) {
		if(StringUtils.isNotEmpty(runningBalance)){
			this.runningBalance = runningBalance;
		}
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		if(StringUtils.isNotEmpty(transactionType)){
			this.transactionType = transactionType;
		}
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		if(StringUtils.isNotEmpty(dateFormat)){
			this.dateFormat = dateFormat;
		}
	}
	public void setHash(){
		String hash = AccountUtil.generateHash(this, getProperties());
		setFingerprint(hash);
	}

	@Override
	public String getTag() {
		return tag;
	}


}
