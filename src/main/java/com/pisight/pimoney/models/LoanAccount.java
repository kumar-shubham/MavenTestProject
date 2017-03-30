package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.ParserUtility;
import com.pisight.pimoney.constants.Constants;

public class LoanAccount extends Container {

	
//	public LoanAccount(){
//		setTag(Constants.TAG_LOAN);
//	}
	
	public LoanAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_LOAN);
		this.properties = properties;
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private String accountNumber = "";
	private String principalAmount = "";
	private String tenureMonths = "";
	private String loanEMI = "";
	private String loanType = "";
	private String accountName = "";
	private String balance = "";
	private String interestRate = "";
	private String availabeBalance = "";
	
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
		String hash = ParserUtility.generateHash(transaction, properties);
		transaction.setFingerprint(hash);
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
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
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
	 * @return the availabeBalance
	 */
	public String getAvailabeBalance() {
		return availabeBalance;
	}
	/**
	 * @param availabeBalance the availabeBalance to set
	 */
	public void setAvailabeBalance(String availabeBalance) {
		this.availabeBalance = availabeBalance;
	}
	
	
}
