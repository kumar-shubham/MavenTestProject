package com.pisight.pimoney.models;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.pisight.pimoney.beans.ParserUtility;

public class HoldingAsset {
	
	private static Logger logger = Logger.getLogger(HoldingAsset.class);
	
	// Constants for Asset Categories
	
	public static final String CATEGORY_LOAN = "Loan";
	public static final String CATEGORY_CASH = "Cash";
	public static final String CATEGORY_BOND = "Bond";
	public static final String CATEGORY_EQUITY = "Equity";
	public static final String CATEGORY_DEPOSIT = "Deposit";
	public static final String CATEGORY_COMMODITY = "Commodity";
	public static final String CATEGORY_ALTERNATE_ASSET = "Alternate Asset";
	public static final String CATEGORY_UNIT_TRUST__MUTUAL_FUND = "Unit Trust / Mutual Fund";
	public static final String CATEGORY_ALTERNATE_TRADING_STRATEGY = "Alternate Trading Strategy";
	public static final String CATEGORY_MULTI_ASSET__STRUCTURED_NOTES = "Multi Asset Class / Structured Notes";
	public static final String CATEGORY_PRIVATE_EQUITY__HEDGE_N_OTHER_FUND = "Private Equity, Hedge Fund and Other Fund";
	public static final String CATEGORY_MONEY_MARKET_N_DISCOUNTED_INSTRUMENT = "Money Market and Discounted Instrument";
	
	private String  holdingAssetAccountNumber;
	private String  holdingAssetSubAccountNumber;
	private String  holdingAssetSecurityId;
	private String  holdingAssetName;
	private String  holdingAssetDescription;
	private String  holdingAssetCategory;
	private String  holdingAssetSubCategory;
	private String  holdingAssetCurrency;
	private String  holdingAssetYield;
	private String  holdingAssetQuantity;
	private String  holdingAssetAverageUnitCost;
	private String  holdingAssetIndicativePrice;
	private String  holdingAssetCost;
	private String  holdingAssetCurrentValue;
	private String  holdingAssetIndicativePriceDate;
	private String  holdingAssetProfit;
	private String  holdingAssetProfitPerc;
	private String  holdingAssetCustodian;
	private String  holdingAssetMaturityDate;
	private String  holdingAssetIssuer;
	private String  holdingAssetAccruedInterest;
	private String  holdingAssetLastFxRate;
	private String  holdingAssetFxAccruredInterest;
	private String  holdingAssetStartDate;
	private String  holdingAssetFxMarketValue;
	private String  holdingAssetUnrealizedProfitLoss;
	private String  holdingAssetUnrealizedProfitLossCurrency;
	private String  holdingAssetISIN;
	private String  holdingAssetCommencingDate;
	private String  holdingAssetCoupon;
	private String  holdingAssetStrikePrice;
	private String  holdingAssetInterestTillMaturity;
	private String  holdingAssetOption;
	private String  fingerprint;
	
	private boolean bondNature = false;
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	public boolean isBondNature() {
		return bondNature;
	}
	/**
	 * @return the bondNature
	 */
	public boolean hasBondNature() {
		return bondNature;
	}
	/**
	 * @param bondNature the bondNature to set
	 */
	public void setBondNature(boolean bondNature) {
		this.bondNature = bondNature;
	}
	
	/**
	 * @return the holdingAssetAccountNumber
	 */
	public String getHoldingAssetAccountNumber() {
		return holdingAssetAccountNumber;
	}
	/**
	 * @param holdingAssetAccountNumber the holdingAssetAccountNumber to set
	 */
	public void setHoldingAssetAccountNumber(String holdingAssetAccountNumber) {
		if(StringUtils.isNotEmpty(holdingAssetAccountNumber)){
			this.holdingAssetAccountNumber = holdingAssetAccountNumber.trim();
		}
	}
	/**
	 * @return the holdingAssetSubAccountNumber
	 */
	public String getHoldingAssetSubAccountNumber() {
		return holdingAssetSubAccountNumber;
	}
	/**
	 * @param holdingAssetSubAccountNumber the holdingAssetSubAccountNumber to set
	 */
	public void setHoldingAssetSubAccountNumber(String holdingAssetSubAccountNumber) {
		if(StringUtils.isNotEmpty(holdingAssetSubAccountNumber)){
			this.holdingAssetSubAccountNumber = holdingAssetSubAccountNumber.trim();
		}
	}
	/**
	 * @return the holdingAssetSecurityId
	 */
	public String getHoldingAssetSecurityId() {
		return holdingAssetSecurityId;
	}
	/**
	 * @param holdingAssetSecurityId the holdingAssetSecurityId to set
	 */
	public void setHoldingAssetSecurityId(String holdingAssetSecurityId) {
		if(StringUtils.isNotEmpty(holdingAssetSecurityId)){
			this.holdingAssetSecurityId = holdingAssetSecurityId.trim();
		}
	}
	/**
	 * @return the holdingAssetName
	 */
	public String getHoldingAssetName() {
		return holdingAssetName;
	}
	/**
	 * @param holdingAssetName the holdingAssetName to set
	 */
	public void setHoldingAssetName(String holdingAssetName) {
		if(StringUtils.isNotEmpty(holdingAssetName)){
			this.holdingAssetName = holdingAssetName.trim();
		}
	}
	/**
	 * @return the holdingAssetDescription
	 */
	public String getHoldingAssetDescription() {
		return holdingAssetDescription;
	}
	/**
	 * @param holdingAssetDescription the holdingAssetDescription to set
	 */
	public void setHoldingAssetDescription(String holdingAssetDescription) {
		if(StringUtils.isNotEmpty(holdingAssetDescription)){
			this.holdingAssetDescription = holdingAssetDescription.trim();
		}
	}
	/**
	 * @return the holdingAssetCategory
	 */
	public String getHoldingAssetCategory() {
		return holdingAssetCategory;
	}
	/**
	 * @param holdingAssetCategory the holdingAssetCategory to set
	 */
	public void setHoldingAssetCategory(String holdingAssetCategory) {
		if(StringUtils.isNotEmpty(holdingAssetCategory)){
			this.holdingAssetCategory = holdingAssetCategory.trim();
		}
	}
	/**
	 * @return the holdingAssetSubCategory
	 */
	public String getHoldingAssetSubCategory() {
		return holdingAssetSubCategory;
	}
	/**
	 * @param holdingAssetSubCategory the holdingAssetSubCategory to set
	 */
	public void setHoldingAssetSubCategory(String holdingAssetSubCategory) {
		if(StringUtils.isNotEmpty(holdingAssetSubCategory)){
			this.holdingAssetSubCategory = holdingAssetSubCategory.trim();
		}
	}
	/**
	 * @return the holdingAssetCurrency
	 */
	public String getHoldingAssetCurrency() {
		return holdingAssetCurrency;
	}
	/**
	 * @param holdingAssetCurrency the holdingAssetCurrency to set
	 */
	public void setHoldingAssetCurrency(String holdingAssetCurrency) {
		if(StringUtils.isNotEmpty(holdingAssetCurrency)){
			this.holdingAssetCurrency = holdingAssetCurrency.trim().trim();
		}
	}
	/**
	 * @return the holdingAssetYield
	 */
	public String getHoldingAssetYield() {
		return holdingAssetYield;
	}
	/**
	 * @param holdingAssetYield the holdingAssetYield to set
	 */
	public void setHoldingAssetYield(String holdingAssetYield) {
		if(StringUtils.isNotEmpty(holdingAssetYield)){
			this.holdingAssetYield = holdingAssetYield.trim();
		}
	}
	/**
	 * @return the holdingAssetQuantity
	 */
	public String getHoldingAssetQuantity() {
		return holdingAssetQuantity;
	}
	/**
	 * @param holdingAssetQuantity the holdingAssetQuantity to set
	 */
	public void setHoldingAssetQuantity(String holdingAssetQuantity) {
		if(StringUtils.isNotEmpty(holdingAssetQuantity)){
			this.holdingAssetQuantity = holdingAssetQuantity.trim();
		}
	}
	/**
	 * @return the holdingAssetAverageUnitCost
	 */
	public String getHoldingAssetAverageUnitCost() {
		return holdingAssetAverageUnitCost;
	}
	/**
	 * @param holdingAssetAverageUnitCost the holdingAssetAverageUnitCost to set
	 */
	public void setHoldingAssetAverageUnitCost(String holdingAssetAverageUnitCost) {
		if(StringUtils.isNotEmpty(holdingAssetAverageUnitCost)){
			this.holdingAssetAverageUnitCost = holdingAssetAverageUnitCost.trim();
		}
	}
	/**
	 * @return the holdingAssetIndicativePrice
	 */
	public String getHoldingAssetIndicativePrice() {
		return holdingAssetIndicativePrice;
	}
	/**
	 * @param holdingAssetIndicativePrice the holdingAssetIndicativePrice to set
	 */
	public void setHoldingAssetIndicativePrice(String holdingAssetIndicativePrice) {
		if(StringUtils.isNotEmpty(holdingAssetIndicativePrice)){
			this.holdingAssetIndicativePrice = holdingAssetIndicativePrice.trim();
		}
	}
	/**
	 * @return the holdingAssetCost
	 */
	public String getHoldingAssetCost() {
		return holdingAssetCost;
	}
	/**
	 * @param holdingAssetCost the holdingAssetCost to set
	 */
	public void setHoldingAssetCost(String holdingAssetCost) {
		if(StringUtils.isNotEmpty(holdingAssetCost)){
			this.holdingAssetCost = holdingAssetCost.trim();
		}
	}
	/**
	 * @return the holdingAssetCurrentValue
	 */
	public String getHoldingAssetCurrentValue() {
		return holdingAssetCurrentValue;
	}
	/**
	 * @param holdingAssetCurrentValue the holdingAssetCurrentValue to set
	 */
	public void setHoldingAssetCurrentValue(String holdingAssetCurrentValue) {
		if(StringUtils.isNotEmpty(holdingAssetCurrentValue)){
			this.holdingAssetCurrentValue = holdingAssetCurrentValue.trim();
		}
	}
	/**
	 * @return the holdingAssetIndicativePriceDate
	 */
	public String getHoldingAssetIndicativePriceDate() {
		return holdingAssetIndicativePriceDate;
	}
	/**
	 * @param holdingAssetIndicativePriceDate the holdingAssetIndicativePriceDate to set
	 */
	public void setHoldingAssetIndicativePriceDate(String holdingAssetIndicativePriceDate) {
		if(StringUtils.isNotEmpty(holdingAssetIndicativePriceDate)){
			this.holdingAssetIndicativePriceDate = holdingAssetIndicativePriceDate.trim();
		}
	}
	/**
	 * @return the holdingAssetProfit
	 */
	public String getHoldingAssetProfit() {
		return holdingAssetProfit;
	}
	/**
	 * @param holdingAssetProfit the holdingAssetProfit to set
	 */
	public void setHoldingAssetProfit(String holdingAssetProfit) {
		if(StringUtils.isNotEmpty(holdingAssetProfit)){
			this.holdingAssetProfit = holdingAssetProfit.trim();
		}
	}
	/**
	 * @return the holdingAssetProfitPerc
	 */
	public String getHoldingAssetProfitPerc() {
		return holdingAssetProfitPerc;
	}
	/**
	 * @param holdingAssetProfitPerc the holdingAssetProfitPerc to set
	 */
	public void setHoldingAssetProfitPerc(String holdingAssetProfitPerc) {
		if(StringUtils.isNotEmpty(holdingAssetProfitPerc)){
			this.holdingAssetProfitPerc = holdingAssetProfitPerc.trim();
		}
	}
	/**
	 * @return the holdingAssetCustodian
	 */
	public String getHoldingAssetCustodian() {
		return holdingAssetCustodian;
	}
	/**
	 * @param holdingAssetCustodian the holdingAssetCustodian to set
	 */
	public void setHoldingAssetCustodian(String holdingAssetCustodian) {
		if(StringUtils.isNotEmpty(holdingAssetCustodian)){
			this.holdingAssetCustodian = holdingAssetCustodian.trim();
		}
	}
	
	/**
	 * @return the holdingAssetMaturityDate
	 */
	public String getHoldingAssetMaturityDate() {
		return holdingAssetMaturityDate;
	}
	/**
	 * @param holdingAssetMaturityDate the holdingAssetMaturityDate to set
	 */
	public void setHoldingAssetMaturityDate(String holdingAssetMaturityDate) {
		if(StringUtils.isNotEmpty(holdingAssetMaturityDate)){
			this.holdingAssetMaturityDate = holdingAssetMaturityDate.trim();
		}
	}
	/**
	 * @return the holdingAssetIssuer
	 */
	public String getHoldingAssetIssuer() {
		return holdingAssetIssuer;
	}
	/**
	 * @param holdingAssetIssuer the holdingAssetIssuer to set
	 */
	public void setHoldingAssetIssuer(String holdingAssetIssuer) {
		if(StringUtils.isNotEmpty(holdingAssetIssuer)){
			this.holdingAssetIssuer = holdingAssetIssuer.trim();
		}
	}
	/**
	 * @return the holdingAssetAccruedInterest
	 */
	public String getHoldingAssetAccruedInterest() {
		return holdingAssetAccruedInterest;
	}
	/**
	 * @param holdingAssetAccruedInterest the holdingAssetAccruedInterest to set
	 */
	public void setHoldingAssetAccruedInterest(String holdingAssetAccruedInterest) {
		if(StringUtils.isNotEmpty(holdingAssetAccruedInterest)){
			this.holdingAssetAccruedInterest = holdingAssetAccruedInterest.trim();
		}
	}
	/**
	 * @return the holdingAssetLastFxRate
	 */
	public String getHoldingAssetLastFxRate() {
		return holdingAssetLastFxRate;
	}
	/**
	 * @param holdingAssetLastFxRate the holdingAssetLastFxRate to set
	 */
	public void setHoldingAssetLastFxRate(String holdingAssetLastFxRate) {
		if(StringUtils.isNotEmpty(holdingAssetLastFxRate)){
			this.holdingAssetLastFxRate = holdingAssetLastFxRate.trim();
		}
	}
	/**
	 * @return the holdingAssetFxAccruredInterest
	 */
	public String getHoldingAssetFxAccruredInterest() {
		return holdingAssetFxAccruredInterest;
	}
	/**
	 * @param holdingAssetFxAccruredInterest the holdingAssetFxAccruredInterest to set
	 */
	public void setHoldingAssetFxAccruredInterest(String holdingAssetFxAccruredInterest) {
		if(StringUtils.isNotEmpty(holdingAssetFxAccruredInterest)){
			this.holdingAssetFxAccruredInterest = holdingAssetFxAccruredInterest.trim();
		}
	}
	/**
	 * @return the holdingAssetStartDate
	 */
	public String getHoldingAssetStartDate() {
		return holdingAssetStartDate;
	}
	/**
	 * @param holdingAssetStartDate the holdingAssetStartDate to set
	 */
	public void setHoldingAssetStartDate(String holdingAssetStartDate) {
		if(StringUtils.isNotEmpty(holdingAssetStartDate)){
			this.holdingAssetStartDate = holdingAssetStartDate.trim();
		}
	}
	/**
	 * @return the holdingAssetFxMarketValue
	 */
	public String getHoldingAssetFxMarketValue() {
		return holdingAssetFxMarketValue;
	}
	/**
	 * @param holdingAssetFxMarketValue the holdingAssetFxMarketValue to set
	 */
	public void setHoldingAssetFxMarketValue(String holdingAssetFxMarketValue) {
		if(StringUtils.isNotEmpty(holdingAssetFxMarketValue)){
			this.holdingAssetFxMarketValue = holdingAssetFxMarketValue.trim();
		}
	}
	/**
	 * @return the holdingAssetUnrealizedProfitLoss
	 */
	public String getHoldingAssetUnrealizedProfitLoss() {
		return holdingAssetUnrealizedProfitLoss;
	}
	/**
	 * @param holdingAssetUnrealizedProfitLoss the holdingAssetUnrealizedProfitLoss to set
	 */
	public void setHoldingAssetUnrealizedProfitLoss(String holdingAssetUnrealizedProfitLoss) {
		if(StringUtils.isNotEmpty(holdingAssetUnrealizedProfitLoss)){
			this.holdingAssetUnrealizedProfitLoss = holdingAssetUnrealizedProfitLoss.trim();
		}
	}
	/**
	 * @return the holdingAssetUnrealizedProfitLossCurrency
	 */
	public String getHoldingAssetUnrealizedProfitLossCurrency() {
		return holdingAssetUnrealizedProfitLossCurrency;
	}
	/**
	 * @param holdingAssetUnrealizedProfitLossCurrency the holdingAssetUnrealizedProfitLossCurrency to set
	 */
	public void setHoldingAssetUnrealizedProfitLossCurrency(String holdingAssetUnrealizedProfitLossCurrency) {
		if(StringUtils.isNotEmpty(holdingAssetUnrealizedProfitLossCurrency)){
			this.holdingAssetUnrealizedProfitLossCurrency = holdingAssetUnrealizedProfitLossCurrency.trim();
		}
	}
	/**
	 * @return the holdingAssetISIN
	 */
	public String getHoldingAssetISIN() {
		return holdingAssetISIN;
	}
	/**
	 * @param holdingAssetISIN the holdingAssetISIN to set
	 */
	public void setHoldingAssetISIN(String holdingAssetISIN) {
		if(StringUtils.isNotEmpty(holdingAssetISIN)){
			this.holdingAssetISIN = holdingAssetISIN.trim();
		}
	}
	/**
	 * @return the holdingAssetCommencingDate
	 */
	public String getHoldingAssetCommencingDate() {
		return holdingAssetCommencingDate;
	}
	/**
	 * @param holdingAssetCommencingDate the holdingAssetCommencingDate to set
	 */
	public void setHoldingAssetCommencingDate(String holdingAssetCommencingDate) {
		if(StringUtils.isNotEmpty(holdingAssetCommencingDate)){
			this.holdingAssetCommencingDate = holdingAssetCommencingDate.trim();
		}
	}
	/**
	 * @return the holdingAssetCoupon
	 */
	public String getHoldingAssetCoupon() {
		return holdingAssetCoupon;
	}
	/**
	 * @param holdingAssetCoupon the holdingAssetCoupon to set
	 */
	public void setHoldingAssetCoupon(String holdingAssetCoupon) {
		if(StringUtils.isNotEmpty(holdingAssetCoupon)){
			this.holdingAssetCoupon = holdingAssetCoupon.trim();
		}
	}
	
	/**
	 * @return the holdingAssetStrikePrice
	 */
	public String getHoldingAssetStrikePrice() {
		return holdingAssetStrikePrice;
	}
	/**
	 * @param holdingAssetStrikePrice the holdingAssetStrikePrice to set
	 */
	public void setHoldingAssetStrikePrice(String holdingAssetStrikePrice) {
		if(StringUtils.isNotEmpty(holdingAssetStrikePrice)){
			this.holdingAssetStrikePrice = holdingAssetStrikePrice;
		}
	}
	/**
	 * @return the holdingAssetInterestTillMaturity
	 */
	public String getHoldingAssetInterestTillMaturity() {
		return holdingAssetInterestTillMaturity;
	}
	/**
	 * @param holdingAssetInterestTillMaturity the holdingAssetInterestTillMaturity to set
	 */
	public void setHoldingAssetInterestTillMaturity(String holdingAssetInterestTillMaturity) {
		if(StringUtils.isNotEmpty(holdingAssetInterestTillMaturity)){
			this.holdingAssetInterestTillMaturity = holdingAssetInterestTillMaturity;
		}
	}
	/**
	 * @return
	 */
	public String getHoldingAssetOption() {
		return holdingAssetOption;
	}
	/**
	 * @param holdingAssetOption
	 */
	public void setHoldingAssetOption(String holdingAssetOption) {
		if(StringUtils.isNotEmpty(holdingAssetOption)){
			this.holdingAssetOption = holdingAssetOption;
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
	/**
	 * @return the holdingAssetHash
	 */
	public String getHoldingAssetHash() {
		return fingerprint;
	}
	/**
	 * @param holdingAssetHash the holdingAssetHash to set
	 */
	public void setHoldingAssetHash(String holdingAssetHash) {
		this.fingerprint = holdingAssetHash;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	public void setHash() {
		String hash = ParserUtility.generateHash(this, properties);
		setHoldingAssetHash(hash);
		logger.info("SET HASH --> "  + this.holdingAssetDescription + " :: " + this.fingerprint );
	}
}
