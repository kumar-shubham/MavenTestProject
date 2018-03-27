package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.HashMap;

import com.pisight.pimoney.constants.Constants;

public class FixedDepositAccount extends Container implements Serializable{

	private static final long serialVersionUID = -5627095321122978087L;



	public FixedDepositAccount() {
		setTag(Constants.TAG_FIXED_DEPOSIT);
	}

	public FixedDepositAccount(HashMap<String, String> properties) {
		setTag(Constants.TAG_FIXED_DEPOSIT);
		setProperties(properties);
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}


	@Override
	public String getTag() {
		// TODO Auto-generated method stub
		return Constants.TAG_FIXED_DEPOSIT;
	}


	private String accountNumber = null;

	private String accountName = null;

	private String principleAmount = null;

	private String maturityAmount = null;

	private String startDate = null;

	private String maturityDate = null;

	private String interestRate = null;

	private String interestPayout = null;

	private String interestComputationFrequency  = null;

	// this index shows whether the account has account details or it is just for 
	// process completion
	private int usability = 1;



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

	public String getPrincipleAmount() {
		return principleAmount;
	}

	public void setPrincipleAmount(String prinipleAmount) {
		this.principleAmount = prinipleAmount;
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

	/**
	 * @return the interestComputationFrequency
	 */
	public String getInterestComputationFrequency() {
		return interestComputationFrequency;
	}

	/**
	 * @param interestComputationFrequency the interestComputationFrequency to set
	 */
	public void setInterestComputationFrequency(String interestComputationFrequency) {
		this.interestComputationFrequency = interestComputationFrequency;
	}

	/**
	 * @return the interestPayout
	 */
	public String getInterestPayout() {
		return interestPayout;
	}

	/**
	 * @param interestPayout the interestPayout to set
	 */
	public void setInterestPayout(String interestPayout) {
		this.interestPayout = interestPayout;
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
