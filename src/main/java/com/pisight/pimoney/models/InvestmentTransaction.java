package com.pisight.pimoney.models;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.beans.ParserUtility;

public class InvestmentTransaction extends TransactionBase{
	
	public static final String TRANSACTION_TYPE_DEBIT = "debit";
	public static final String TRANSACTION_TYPE_CREDIT = "credit";
	
	public static final String TRANSACTION_TYPE_BUY = "Buy";
	public static final String TRANSACTION_TYPE_SELL = "Sell";
	public static final String TRANSACTION_TYPE_INCOME = "Income";
	public static final String TRANSACTION_TYPE_EXPENSE = "Expense";
	public static final String TRANSACTION_TYPE_INFLOW = "Inflow";
	public static final String TRANSACTION_TYPE_OUTFLOW = "Outflow";
	
	
	private String accountNumber = null;
	private String subAccountNumber = null;
	private String currency = null;
	private String type = null;
	private String amount = null;
	private String transactionDate = null;
	private String description = null;
	private String assetCategory = null;
	private String assetName = null;
	private String assetMarket = null;
	private String assetInstrument = null;
	private String assetYield = null;
	private String assetQuantity = null;
	private String assetUnitCost = null;
	private String assetCost = null;
	private String assetTradeDate = null;
	private String assetCustodian = null;
	private String assetBondRate = null;
	private String assetBondMaturityDate = null;
	private String assetBondIssuer = null;
	private String assetISIN = null;
	private String valuationDate = null;
	private String startDate = null;
	private String maturityDate = null;
	private String coupon = null;
	private String accruedInterest = null;
	private String dateFormat = null;

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
		if(StringUtils.isNotEmpty(accountNumber)){
			this.accountNumber = accountNumber;
		}
	}
	/**
	 * @return the subAccountNumber
	 */
	public String getSubAccountNumber() {
		return subAccountNumber;
	}
	/**
	 * @param subAccountNumber the subAccountNumber to set
	 */
	public void setSubAccountNumber(String subAccountNumber) {
		if(StringUtils.isNotEmpty(subAccountNumber)){
			this.subAccountNumber = subAccountNumber;
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
			this.currency = currency;
		}
	}
	/**
	 * @return the assetCategory
	 */
	public String getAssetCategory() {
		return assetCategory;
	}
	/**
	 * @param assetCategory the assetCategory to set
	 */
	public void setAssetCategory(String assetCategory) {
		if(StringUtils.isNotEmpty(assetCategory)){
			this.assetCategory = assetCategory;
		}
	}
	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}
	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		if(StringUtils.isNotEmpty(assetName)){
			this.assetName = assetName;
		}
	}
	/**
	 * @return the assetMarket
	 */
	public String getAssetMarket() {
		return assetMarket;
	}
	/**
	 * @param assetMarket the assetMarket to set
	 */
	public void setAssetMarket(String assetMarket) {
		if(StringUtils.isNotEmpty(assetMarket)){
			this.assetMarket = assetMarket;
		}
	}
	/**
	 * @return the assetInstrument
	 */
	public String getAssetInstrument() {
		return assetInstrument;
	}
	/**
	 * @param assetInstrument the assetInstrument to set
	 */
	public void setAssetInstrument(String assetInstrument) {
		if(StringUtils.isNotEmpty(assetInstrument)){
			this.assetInstrument = assetInstrument;
		}
	}
	/**
	 * @return the assetYield
	 */
	public String getAssetYield() {
		return assetYield;
	}
	/**
	 * @param assetYield the assetYield to set
	 */
	public void setAssetYield(String assetYield) {
		if(StringUtils.isNotEmpty(assetYield)){
			this.assetYield = assetYield;
		}
	}
	public void setAssetYield(String assetYield, boolean format) {
		if(StringUtils.isNotEmpty(assetYield)){
			this.assetYield = ParserUtility.formatAmount(assetYield);
		}
	}
	/**
	 * @return the assetQuantity
	 */
	public String getAssetQuantity() {
		return assetQuantity;
	}
	/**
	 * @param assetQuantity the assetQuantity to set
	 */
	public void setAssetQuantity(String assetQuantity) {
		if(StringUtils.isNotEmpty(assetQuantity)){
			this.assetQuantity = assetQuantity;
		}
	}
	public void setAssetQuantity(String assetQuantity, boolean format) {
		if(StringUtils.isNotEmpty(assetQuantity)){
			this.assetQuantity = ParserUtility.formatAmount(assetQuantity);
		}
	}
	/**
	 * @return the assetUnitCost
	 */
	public String getAssetUnitCost() {
		return assetUnitCost;
	}
	/**
	 * @param assetUnitCost the assetUnitCost to set
	 */
	public void setAssetUnitCost(String assetUnitCost) {
		if(StringUtils.isNotEmpty(assetUnitCost)){
			this.assetUnitCost = assetUnitCost;
		}
	}
	public void setAssetUnitCost(String assetUnitCost, boolean format) {
		if(StringUtils.isNotEmpty(assetUnitCost)){
			this.assetUnitCost = ParserUtility.formatAmount(assetUnitCost);
		}
	}
	/**
	 * @return the assetCost
	 */
	public String getAssetCost() {
		return assetCost;
	}
	/**
	 * @param assetCost the assetCost to set
	 */
	public void setAssetCost(String assetCost) {
		if(StringUtils.isNotEmpty(assetCost)){
			this.assetCost = assetCost;
		}
	}
	public void setAssetCost(String assetCost, boolean format) {
		if(StringUtils.isNotEmpty(assetCost)){
			this.assetCost = ParserUtility.formatAmount(assetCost);
		}
	}
	/**
	 * @return the assetTradeDate
	 */
	public String getAssetTradeDate() {
		return assetTradeDate;
	}
	/**
	 * @param assetTradeDate the assetTradeDate to set
	 */
	public void setAssetTradeDate(String assetTradeDate) {
		if(StringUtils.isNotEmpty(assetTradeDate)){
			this.assetTradeDate = assetTradeDate;
		}
	}
	public void setAssetTradeDate(String assetTradeDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(assetTradeDate)){
			this.assetTradeDate = ParserUtility.convertToPimoneyDate(assetTradeDate, dateFormat);
		}
	}
	/**
	 * @return the assetCustodian
	 */
	public String getAssetCustodian() {
		return assetCustodian;
	}
	/**
	 * @param assetCustodian the assetCustodian to set
	 */
	public void setAssetCustodian(String assetCustodian) {
		if(StringUtils.isNotEmpty(assetCustodian)){
			this.assetCustodian = assetCustodian;
		}
	}
	/**
	 * @return the assetBondRate
	 */
	public String getAssetBondRate() {
		return assetBondRate;
	}
	/**
	 * @param assetBondRate the assetBondRate to set
	 */
	public void setAssetBondRate(String assetBondRate) {
		if(StringUtils.isNotEmpty(assetBondRate)){
			this.assetBondRate = assetBondRate;
		}
	}
	public void setAssetBondRate(String assetBondRate, boolean format) {
		if(StringUtils.isNotEmpty(assetBondRate)){
			this.assetBondRate = ParserUtility.formatAmount(assetBondRate);
		}
	}
	/**
	 * @return the assetBondMaturityDate
	 */
	public String getAssetBondMaturityDate() {
		return assetBondMaturityDate;
	}
	/**
	 * @param assetBondMaturityDate the assetBondMaturityDate to set
	 */
	public void setAssetBondMaturityDate(String assetBondMaturityDate) {
		if(StringUtils.isNotEmpty(assetBondMaturityDate)){
			this.assetBondMaturityDate = assetBondMaturityDate;
		}
	}
	public void setAssetBondMaturityDate(String assetBondMaturityDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(assetBondMaturityDate)){
			this.assetBondMaturityDate = ParserUtility.convertToPimoneyDate(assetBondMaturityDate, dateFormat);
		}
	}
	/**
	 * @return the assetBondIssuer
	 */
	public String getAssetBondIssuer() {
		return assetBondIssuer;
	}
	/**
	 * @param assetBondIssuer the assetBondIssuer to set
	 */
	public void setAssetBondIssuer(String assetBondIssuer) {
		if(StringUtils.isNotEmpty(assetBondIssuer)){
			this.assetBondIssuer = assetBondIssuer;
		}
	}
	/**
	 * @return the assetISIN
	 */
	public String getAssetISIN() {
		return assetISIN;
	}
	/**
	 * @param assetISIN the assetISIN to set
	 */
	public void setAssetISIN(String assetISIN) {
		if(StringUtils.isNotEmpty(assetISIN)){
			this.assetISIN = assetISIN;
		}
	}
	/**
	 * @return the valuationDate
	 */
	public String getValuationDate() {
		return valuationDate;
	}
	/**
	 * @param valuationDate the valuationDate to set
	 */
	public void setValuationDate(String valuationDate) {
		if(StringUtils.isNotEmpty(valuationDate)){
			this.valuationDate = valuationDate;
		}
	}
	public void setValuationDate(String valuationDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(valuationDate)){
			this.valuationDate = ParserUtility.convertToPimoneyDate(valuationDate, dateFormat);
		}
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		if(StringUtils.isNotEmpty(type)){
			this.type = type;
		}
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		if(StringUtils.isNotEmpty(amount)){
			this.amount = amount;
		}
	}
	public void setAmount(String amount, boolean format) {
		if(StringUtils.isNotEmpty(amount)){
			this.amount = ParserUtility.formatAmount(amount);
		}
	}
	/**
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		if(StringUtils.isNotEmpty(transactionDate)){
			this.transactionDate = transactionDate;
		}
	}
	public void setTransactionDate(String transactionDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(transactionDate)){
			this.transactionDate = ParserUtility.convertToPimoneyDate(transactionDate, dateFormat);
		}
	}
	/**
	 * @return the escription
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param escription the escription to set
	 */
	public void setDescription(String description) {
		if(StringUtils.isNotEmpty(description)){
			this.description = description;
		}
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
		if(StringUtils.isNotEmpty(startDate)){
			this.startDate = startDate;
		}
	}
	public void setStartDate(String startDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(startDate)){
			this.startDate = ParserUtility.convertToPimoneyDate(startDate, dateFormat);
		}
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
		if(StringUtils.isNotEmpty(maturityDate)){
			this.maturityDate = maturityDate;
		}
	}
	public void setMaturityDate(String maturityDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(maturityDate)){
			this.maturityDate = ParserUtility.convertToPimoneyDate(maturityDate, dateFormat);
		}
	}
	/**
	 * @return the coupon
	 */
	public String getCoupon() {
		return coupon;
	}
	/**
	 * @param coupon the coupon to set
	 */
	public void setCoupon(String coupon) {
		if(StringUtils.isNotEmpty(coupon)){
			this.coupon = coupon;
		}
	}
	public void setCoupon(String coupon, boolean format) {
		if(StringUtils.isNotEmpty(coupon)){
			this.coupon = ParserUtility.formatAmount(coupon);
		}
	}
	/**
	 * @return the accruedInterest
	 */
	public String getAccruedInterest() {
		return accruedInterest;
	}
	/**
	 * @param accruedInterest the accruedInterest to set
	 */
	public void setAccruedInterest(String accruedInterest) {
		if(StringUtils.isNotEmpty(accruedInterest)){
			this.accruedInterest = accruedInterest;
		}
	}
	public void setAccruedInterest(String accruedInterest, boolean format) {
		if(StringUtils.isNotEmpty(accruedInterest)){
			this.accruedInterest = ParserUtility.formatAmount(accruedInterest);
		}
	}
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public void setHash(){
		String hash = ParserUtility.generateHash(this, properties);
		setFingerprint(hash);
	}
	

}
