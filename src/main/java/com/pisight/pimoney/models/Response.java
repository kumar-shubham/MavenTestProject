package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.ParserUtility;

public class Response {
	
//	public Response(){
//	}
	
	public Response(HashMap<String, String> properties){
		this.properties = properties;
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
	
	private List<CardAccount> cardAccounts = new ArrayList<CardAccount>();
	
	private List<LoanAccount> loanAccounts = new ArrayList<LoanAccount>();
	
	private List<FixedDepositAccount> fdAccounts = new ArrayList<FixedDepositAccount>();
	
	private List<InvestmentAccount> investmentAccounts = new ArrayList<InvestmentAccount>();
	
	private boolean isEncrypted = false;
	
	private boolean isPswdCorrect = false;

	/**
	 * @return the bankAccounts
	 */
	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	/**
	 * @param bankAccounts the bankAccounts to set
	 */
	public void addBankAccount(BankAccount bankAccount) {
		String hash = ParserUtility.generateHash(bankAccount, properties);
		bankAccount.setFingerprint();
		bankAccount.setAccountHash(hash);
		bankAccounts.add(bankAccount);
	}
	
	public void addBankAccount(BankAccount bankAccount, boolean withoutHash){
		if(withoutHash){
			bankAccounts.add(bankAccount);
		}
		else{
			addBankAccount(bankAccount);
		}
	}

	/**
	 * @return the cardAccounts
	 */
	public List<CardAccount> getCardAccounts() {
		return cardAccounts;
	}

	/**
	 * @param cardAccounts the cardAccounts to set
	 */
	public void addCardAccount(CardAccount cardAccount) {
		String hash = ParserUtility.generateHash(cardAccount, properties);
		cardAccount.setFingerprint();
		cardAccount.setAccountHash(hash);
		cardAccounts.add(cardAccount);
	}
	
	public void addCardAccount(CardAccount cardAccount, boolean withoutHash){
		if(withoutHash){
			cardAccounts.add(cardAccount);
		}
		else{
			addCardAccount(cardAccount);
		}
	}

	/**
	 * @return the loanAccounts
	 */
	public List<LoanAccount> getLoanAccounts() {
		return loanAccounts;
	}

	/**
	 * @param loanAccounts the loanAccounts to set
	 */
	public void addLoanAccount(LoanAccount loanAccount) {
		String hash = ParserUtility.generateHash(loanAccount, properties);
		loanAccount.setAccountHash(hash);
		loanAccounts.add(loanAccount);
	}
	
	public void addLoanAccount(LoanAccount loanAccount, boolean withoutHash){
		if(withoutHash){
			loanAccounts.add(loanAccount);
		}
		else{
			addLoanAccount(loanAccount);
		}
	}

	/**
	 * @return the fdAccounts
	 */
	public List<FixedDepositAccount> getFdAccounts() {
		return fdAccounts;
	}

	/**
	 * @param fdAccounts the fdAccounts to set
	 */
	public void addFdAccount(FixedDepositAccount fdAccount) {
		String hash = ParserUtility.generateHash(fdAccount, properties);
		fdAccount.setAccountHash(hash);
		fdAccounts.add(fdAccount);
	}
	
	public void addFdAccount(FixedDepositAccount fdAccount, boolean withoutHash){
		if(withoutHash){
			fdAccounts.add(fdAccount);
		}
		else{
			addFdAccount(fdAccount);
		}
	}

	/**
	 * @return the investmentAccounts
	 */
	public List<InvestmentAccount> getInvestmentAccounts() {
		return investmentAccounts;
	}

	/**
	 * @param investmentAccounts the investmentAccounts to set
	 */
	public void addInvestmentAccount(InvestmentAccount investmentAccount) {
		String hash = ParserUtility.generateHash(investmentAccount, properties);
		investmentAccount.setAccountHash(hash);
		investmentAccount.setFingerprint();
		investmentAccounts.add(investmentAccount);
	}
	
	public void addInvestmentAccount(InvestmentAccount investmentAccount, boolean withoutHash){
		if(withoutHash){
			investmentAccounts.add(investmentAccount);
		}
		else{
			addInvestmentAccount(investmentAccount);
		}
	}

	/**
	 * @return the isEncrypted
	 */
	public boolean isEncrypted() {
		return isEncrypted;
	}

	/**
	 * @param isEncrypted the isEncrypted to set
	 */
	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	/**
	 * @return the isPswdCorrect
	 */
	public boolean isPswdCorrect() {
		return isPswdCorrect;
	}

	/**
	 * @param isPswdCorrect the isPswdCorrect to set
	 */
	public void setPswdCorrect(boolean isPswdCorrect) {
		this.isPswdCorrect = isPswdCorrect;
	}

	
}
