package com.pisight.pimoney.models;

import java.util.HashMap;

import com.pisight.pimoney.constants.Constants;

public class FixedDepositAccount extends Container {
	
	
//	public FixedDepositAccount() {
//		setTag(Constants.TAG_FIXED_DEPOSIT);
//	}
	
	public FixedDepositAccount(HashMap<String, String> properties) {
		setTag(Constants.TAG_FIXED_DEPOSIT);
		this.properties = properties;
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}
	
	
	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return Constants.TAG_FIXED_DEPOSIT;
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private String accountNumber = "";
	
	private String accountName = "";
	
	private String prinipleAmount = "";
	
	private String maturityAmount = "";
	
	private String startDate = "";
	
	private String maturityDate = "";
	
	private String interestRate = "";
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPrinipleAmount() {
		return prinipleAmount;
	}

	public void setPrinipleAmount(String prinipleAmount) {
		this.prinipleAmount = prinipleAmount;
	}

	public String getMaturityAmount() {
		return maturityAmount;
	}

	public void setMaturityAmount(String maturityAmount) {
		this.maturityAmount = maturityAmount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	
	

}
