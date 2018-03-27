package com.pisight.pimoney.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class InvestmentAccount extends Container implements Serializable{

	private static final long serialVersionUID = -6143480333992204919L;

	public InvestmentAccount(){
		setTag(Constants.TAG_INVESTMENT);
	}

	public InvestmentAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_INVESTMENT);
		this.properties = properties;
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}

	private HashMap<String, String> properties = new HashMap<String, String>();

	private String accountNumber = null;

	private String accountName = null;

	private String secondaryAccountHolder = null;

	private String balance = null;

	private String availableBalance = null;

	private String billDate = null;

	private String  relationshipManager = null;

	private boolean transactionStatement = false;

	// this index shows whether the account has account details or it is just for 
	// process completion
	private int usability = 1;

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
		if(StringUtils.isNotEmpty(accountNumber)) {
			this.accountNumber = accountNumber.trim();
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
		if(StringUtils.isNotEmpty(accountName)) {
			this.accountName = accountName.trim();
		}
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
		if(StringUtils.isNotEmpty(secondaryAccountHolder)) {
			this.secondaryAccountHolder = secondaryAccountHolder.trim();
		}
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
		if(StringUtils.isNotEmpty(balance)) {
			this.balance = balance.trim();
		}
	}
	public void setBalance(String balance, boolean format) {
		if(StringUtils.isNotEmpty(balance)) {
			this.balance = AccountUtil.formatAmount(balance.trim());
		}
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
		if(StringUtils.isNotEmpty(availableBalance)) {
			this.availableBalance = availableBalance.trim();
		}
	}
	public void setAvailableBalance(String availableBalance, boolean format) {
		if(StringUtils.isNotEmpty(availableBalance)) {
			this.availableBalance = AccountUtil.formatAmount(availableBalance.trim());
		}
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
		if(asset != null) {
			this.assets.add(asset);
		}
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
		if(investmentTransaction != null) {
			investmentTransaction.setProperties(properties);
			investmentTransactions.add(investmentTransaction);
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
		if(StringUtils.isNotEmpty(billDate)) {
			this.billDate = billDate.trim();
		}
	}
	public void setBillDate(String billDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(billDate)) {
			this.billDate = AccountUtil.convertToDefaultDateFormat(billDate.trim(), dateFormat);
		}
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
		if(StringUtils.isNotEmpty(relationshipManager)) {
			this.relationshipManager = relationshipManager.trim();
		}
	}


	/**
	 * @return the transactionStatement
	 */
	public boolean isTransactionStatement() {
		return transactionStatement;
	}

	/**
	 * @param transactionStatement the transactionStatement to set
	 */
	public void setTransactionStatement(boolean transactionStatement) {
		this.transactionStatement = transactionStatement;
	}


	public void setHash() {
		String hash = AccountUtil.generateHash(this, properties);
		setAccountHash(hash);
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
