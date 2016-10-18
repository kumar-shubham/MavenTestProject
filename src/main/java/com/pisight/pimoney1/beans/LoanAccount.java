package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.List;

public class LoanAccount extends Container {

	
	public LoanAccount(){
		setTag(Container.TAG_LOAN);
	}
	
	private String accountNumber = "";
	private String principalAmount = "";
	private String tenureMonths = "";
	private String loanEMI = "";
	private String loanType = "";
	
	private List<LoanTransaction> transactions = new ArrayList<LoanTransaction>();
	
	
	
	public List<LoanTransaction> getTransactions() {
		return transactions;
	}
	public void addTransaction(LoanTransaction lt) {
		transactions.add(lt);
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
}
