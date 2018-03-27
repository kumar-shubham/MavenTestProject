package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.constants.Constants;

public class LoanAccount extends Container implements Serializable{


	private static final long serialVersionUID = 1001748944893214451L;



	public LoanAccount(){
		setTag(Constants.TAG_LOAN);
	}

	public LoanAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_LOAN);
		setProperties(properties);
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}

	private String accountName = null;

	private String accountNumber = null;

	private String principalAmount = null;

	private String tenureMonths = null;

	private String loanEMI = null;

	private String balance = null;

	private String interestRate = null;

	private String startDate = null;

	private String maturityDate = null;

	private String interestPayout = null;

	private String interestType = null;

	private String interestComputationFrequency = null;

	private String accountClassification = null;

	private String dueDate = null;

	private String lastPaymentAmount = null;

	private String lastPaymentDate = null;

	private String minAmountDue = null;

	private String nickname = null;

	private String accountType = null;

	private String escrowBalance = null;

	private String recurringPayment = null;

	private String frequency = null;

	// this index shows whether the account has account details or it is just for 
	// process completion
	private int usability = 1;

	//add interest rate
	private List<LoanTransaction> transactions = new ArrayList<LoanTransaction>();



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
	public List<LoanTransaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(LoanTransaction transaction) {
		transactions.add(transaction);
	}

	public void addTransaction(LoanTransaction transaction, boolean withoutHash) {
		if(withoutHash){
			transactions.add(transaction);
		}
		else{
			addTransaction(transaction);
		}
	}
	public String getTenureMonths() {
		return tenureMonths;
	}
	public void setTenureMonths(String tenureMonths) {
		this.tenureMonths = tenureMonths;
	}
	public String getLoanEMI() {
		return loanEMI;
	}
	public void setLoanEMI(String loanEMI) {
		this.loanEMI = loanEMI;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(String principalAmount) {
		this.principalAmount = principalAmount;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the maturityDate
	 */
	public String getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @param maturityDate the maturityDate to set
	 */
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
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
	 * @return the interestType
	 */
	public String getInterestType() {
		return interestType;
	}

	/**
	 * @param interestType the interestType to set
	 */
	public void setInterestType(String interestType) {
		this.interestType = interestType;
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
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<LoanTransaction> transactions) {
		this.transactions = transactions;
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
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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
	 * @return the minAmountDue
	 */
	public String getMinAmountDue() {
		return minAmountDue;
	}

	/**
	 * @param minAmountDue the minAmountDue to set
	 */
	public void setMinAmountDue(String minAmountDue) {
		this.minAmountDue = minAmountDue;
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
	 * @return the escrowBalance
	 */
	public String getEscrowBalance() {
		return escrowBalance;
	}

	/**
	 * @param escrowBalance the escrowBalance to set
	 */
	public void setEscrowBalance(String escrowBalance) {
		this.escrowBalance = escrowBalance;
	}

	/**
	 * @return the recurringPayment
	 */
	public String getRecurringPayment() {
		return recurringPayment;
	}

	/**
	 * @param recurringPayment the recurringPayment to set
	 */
	public void setRecurringPayment(String recurringPayment) {
		this.recurringPayment = recurringPayment;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
