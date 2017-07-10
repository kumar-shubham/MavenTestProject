package com.pisight.pimoney.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.EncodeDecodeUtil;
import com.pisight.pimoney.beans.ParserUtility;
import com.pisight.pimoney.constants.Constants;

public class InvestmentAccount extends Container {
	
//	public InvestmentAccount(){
//		setTag(Constants.TAG_INVESTMENT);
//	}
	
	public InvestmentAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_INVESTMENT);
		this.properties = properties;
//		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
//		setBankId("manual-" + bankId.hashCode());
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();

	private String accountNumber = null;
	
	private String accountName = null;
	
	private String secondaryAccountHolder = null;
	
	private String balance = null;
	
	private String availableBalance = null;
	
	private String currency = null;
	
	private String billDate = null;
	
	private String fingerprint = null;
	
	private String  relationshipManager = null;
	
    private List<HoldingAsset> assets = new ArrayList<HoldingAsset>();
	
	private List<InvestmentTransaction> investmentTransactions = new ArrayList<InvestmentTransaction>();

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
		this.accountNumber = accountNumber;
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
		this.accountName = accountName;
	}

	/**
	 * @return the secondaryAccountHolder
	 */
	public String getSecondaryAccountHolder() {
		return secondaryAccountHolder;
	}

	/**
	 * @param secondaryAccountHolder the secondaryAccountHolder to set
	 */
	public void setSecondaryAccountHolder(String secondaryAccountHolder) {
		this.secondaryAccountHolder = secondaryAccountHolder;
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
	public void setBalance(String balance, boolean format) {
		this.balance = ParserUtility.formatAmount(balance);
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
	public void setAvailableBalance(String availableBalance, boolean format) {
		this.availableBalance = ParserUtility.formatAmount(availableBalance);
	}

	/**
	 * @return the assets
	 */
	public List<HoldingAsset> getAssets() {
		return assets;
	}

	/**
	 * @param assets the assets to set
	 */
	public void setAssets(List<HoldingAsset> assets) {
		this.assets = assets;
	}
	
	/**
	 * @param asset the asset to set
	 */
	public void addAsset(HoldingAsset asset) {
		this.assets.add(asset);
	}

	/**
	 * @return the transactions
	 */
	public List<InvestmentTransaction> getInvestmentTransactions() {
		return investmentTransactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setInvestmentTransactions(List<InvestmentTransaction> investmentTransactions) {
		this.investmentTransactions = investmentTransactions;
	}
	
	/**
	 * @param transactions the transactions to add
	 */
	public void addTransaction(InvestmentTransaction investmentTransaction) {
		investmentTransaction.setProperties(properties);
		investmentTransactions.add(investmentTransaction);
	}
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
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
		this.billDate = billDate;
	}
	public void setBillDate(String billDate, String dateFormat) throws ParseException {
		this.billDate = ParserUtility.convertToPimoneyDate(billDate, dateFormat);
	}
	
	

	/**
	 * @return the relationshipManager
	 */
	public String getRelationshipManager() {
		return relationshipManager;
	}

	/**
	 * @param relationshipManager the relationshipManager to set
	 */
	public void setRelationshipManager(String relationshipManager) {
		this.relationshipManager = relationshipManager;
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
		String fingerprintValue = properties.get(Constants.INSTITUTION_CODE) + accountNumber + balance
				+ currency + billDate;
		fingerprint = EncodeDecodeUtil.encodeString(fingerprintValue);
	}
	
	public void setHash() {
		String hash = ParserUtility.generateHash(this, properties);
		setAccountHash(hash);
	}
	
	
}
