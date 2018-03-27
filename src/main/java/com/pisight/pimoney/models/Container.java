package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.pisight.pimoney.constants.Constants.Source;

/**
 * @author kumar
 *
 */
@JsonTypeInfo(use = Id.CLASS,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
@JsonSubTypes({
	@Type(value = BankAccount.class, name = "Bank"),
	@Type(value = CardAccount.class, name = "Card"),
	@Type(value = LoanAccount.class, name = "Loan"),
	@Type(value = InvestmentAccount.class, name = "Investment"),
	@Type(value = FixedDepositAccount.class, name = "fixedDeposit"),
	@Type(value = FixedDepositAccount.class, name = "Generic"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Container implements Cloneable, Serializable{

	private static final long serialVersionUID = -7724002903379020360L;

	private String accountHash = "";

	private String accountHolder = "";

	private String branch = "";

	private String currency = "";

	private String tag = null;

	private String bankId = "";

	private String fingerprint = null;
	
	private Source source = null;

	private HashMap<String, String> properties = new HashMap<String, String>();


	/**
	 * @return the properties
	 */
	public HashMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * @return the property
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}


	/**
	 * @return the accountHash
	 */
	public String getAccountHash() {
		return accountHash;
	}
	/**
	 * @param accountHash the accountHash to set
	 */
	public void setAccountHash(String accountHash) {
		if(StringUtils.isNotEmpty(accountHash)){
			this.accountHash = accountHash.trim();
		}
	}
	/**
	 * @return the accountHolder
	 */
	public String getAccountHolder() {
		return accountHolder;
	}
	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		if(StringUtils.isNotEmpty(accountHolder)){
			this.accountHolder = accountHolder.trim();
		}
	}
	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}
	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		if(StringUtils.isNotEmpty(branch)){
			this.branch = branch.trim();
		}
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
		if(StringUtils.isNotEmpty(currency)){
			this.currency = currency.trim();
		}
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		if(StringUtils.isNotEmpty(tag)){
			this.tag = tag.trim();
		}
	}
	/**
	 * @return the bankId
	 */
	public String getBankId() {
		return bankId;
	}
	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(String bankId) {
		if(StringUtils.isNotEmpty(bankId)){
			this.bankId = bankId.trim();
		}
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

	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  
	
	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	public abstract String getAccountNumber();

	public abstract int getUsability();

	public abstract void setUsability(int usability);

}
