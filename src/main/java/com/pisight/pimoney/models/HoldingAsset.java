package com.pisight.pimoney.models;

import java.text.ParseException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.pisight.pimoney.beans.ParserUtility;

/**
 * @author kumar
 *
 */
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
	public static final String CATEGORY_PRIVATE_EQUITY__HEDGE_N_OTHER_FUND = "Private Equity and Other Fund";
	public static final String CATEGORY_MONEY_MARKET_N_DISCOUNTED_INSTRUMENT = "Money Market and Discounted Instrument";

	private String  holdingAssetAccountNumber;
	private String  holdingAssetSubAccountNumber;
	/**
	 * Security identifier. As a rule, this field contains one of the following values:<br/><ul>
	 *  <li>Ticker for options and for other security types when available. </li>
	 *  <li> CUSIP for USD securities. </li>
	 *  <li> SEDOL for non‐USD securities </li></ul>
	 */
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



	//new fields added on 31st May 2017

	/**
	 * two letters transaction code to identify the transaction type
	 */
	private String transCode = null;

	/**
	 * open date for the position
	 */
	private String assetCostDate = null;

	/**
	 * method for closing the transactions.<br/>
	 * examples are FIFO, LIFO<br/>
	 * choose from the FieldOptions class
	 */
	private String closingMethodCode = null;

	/**
	 * You can close against a position opened on a specific date.<br/>
	 * Practically it is an extension of closing method code
	 */
	private String versusDate = null;

	/**
	 * Enter the security type code of the source or destination cash account for this transaction.
	 */
	private String srcDstType = null;

	/**
	 * Enter the symbol for the source or destination cash account for the transaction.  
	 */
	private String srcDstSymbol = null;

	/**
	 * Fx rate as on the transaction date
	 */
	private String tradeDateFxRate = null;

	/**
	 * Fx rate as on the settlement/valuation date
	 */
	private String valuationDateFxRate = null;

	/**
	 * Fx rate as on the opening date of the position
	 */
	private String assetOriginalFxRate = null;


	/**
	 * If the traded security/currency is different than the portfolio's base currency (currency types in Sec. Type and Source/Dest. Type fields), 
	 * choose whether or not the currency gain or loss affects the security/currency cost basis.<br/><br/>
	 * Possible values: (y/n).
	 */
	private String  markToMarket = null;

	/**
	 * asset MTM 
	 */
	private String assetMTM = null;

	/**
	 * amount of foreign tax withheld from an interest payment.
	 */
	private String assetWithholdingTax = null;

	/**
	 * exchange code of the securities exchange involved in the transaction.
	 */
	private String exchange = null;

	/**
	 * amount paid to the SEC or exchange for this transaction.
	 */
	private String exchangeFee = null;

	/**
	 * currency amount of the commission paid to the broker for the trade.
	 */
	private String commission = null;

	/**
	 * amount of any other fees applicable to this transaction (Example: stamp fees).
	 */
	private String otherFees = null;

	/**
	 * Indicate whether or not the commission of this transaction is "implied," which means that it is 
	 * included in the price of the security, and therefore affects its cost basis. Example: An OTC trade has an implied commission.<br/><br/>
	 * Possible values: (y/n).
	 */
	private String impliedCommission = null;

	/**
	 * purpose associated with the commission (Example: Payment to Broker, Soft Dollar--Research)
	 */
	private String commissionPurpose = null;

	/**
	 * Indicate whether or not this security is pledged as collateral.
	 */
	private String assetPledge = null;

	/**
	 * new pledge status of the security. This field appears in long change (lc) and short change (sc) transactions only.
	 */
	private String destinationPledge = null;

	/**
	 * the custodian or location to which you want to transfer the security.
	 */
	private String destinationCustodian = null;

	/**
	 * duration on cost.
	 */
	private String assetDuration = null;

	/**
	 * the date on which the shareholder must officially own shares of the security in order to be entitled to a dividend paid for the security.
	 */
	private String recordDate = null;

	/**
	 * the Strategy Code associated with this transaction. Example: You might choose "Stock A Straddle" 
	 * to associate Stock A option transactions with positions of the underlying security.
	 */
	private String strategy = null;

	/**
	 * the amount of foreign withholding tax that is reclaimable from the issuer country. The reclaimable amount is defined by tax rules of the issuer country.
	 */
	private String reclaimAmount = null;

	/**
	 * For a dividend (dv) transaction you enter, define the accrual account if you chose to accrue the dividend. 
	 */
	private String accrualAccount = null;

	/**
	 * preferred accrual method for dividend<br/>
	 * choose from FieldOptions class
	 */
	private String dividendAccrualMethod = null;

	/**
	 * When you enter a management fee transaction (dp), use the Fee Period Date field to accrue management 
	 * fees based on the number of calendar days in the billing period. The dates in the Trade Date and Fee Period 
	 * Date fields of the management fee transaction determine the billing period. When you define the Fee Period Date field, 
	 * the management fee is typically calculated using the number of calendar days between the trade date and the fee period date.
	 * <br/><br/>
	 * <table style="vertical-align: top; left: 0px; top: 300px;" cellspacing="0" width="576"><colgroup><col style="width: 10.377%;"><col style="width: 11.959%;">
	 *<col style="width: 13.396%;"><col style="width: 10.885%;"><col style="width: 13.02%;"><col style="width: 17.189%;"><col style="width: 10.528%;"><col style="width: 12.5%;"></colgroup><tbody><tr style="vertical-align: top; height: 48px;"><td style="border: Solid 1px #ffffff; width: 10.377%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Tran. Code</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 11.959%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Sec. Type</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.396%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Security Symbol</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.885%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Trade Date</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.02%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Trade Amount</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 17.189%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Fee Period Date</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.528%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Source Type</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 12.5%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Source<br>Symbol</p></td></tr><tr style="vertical-align: top; height: 33px;"><td style="border-left: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.377%; padding-right: 10px; padding-left: 10px;"><p>dp</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 11.959%; padding-right: 10px; padding-left: 10px;"><p>epus</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.396%; padding-right: 10px; padding-left: 10px;"><p>manfee</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.885%; padding-right: 10px; padding-left: 10px;"><p>01/01/2007</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.02%; padding-right: 10px; padding-left: 10px;"><p>100</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 17.189%; padding-right: 10px; padding-left: 10px;"><p>03/31/2007</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.528%; padding-right: 10px; padding-left: 10px;"><p>caus</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 12.5%; padding-right: 10px; padding-left: 10px;"><p>cash</p></td></tr>
	 *</tbody></table><br/>
	 *In this example, a prorated fee of $1.11 ($100/90 days in the period) a day is calculated.
	 */
	private String mgmtFeePeriodDate = null;


	/**
	 * Indicate whether or not the distribution from a private equity fund is recallable. A recallable distribution increases
	 *  the amount of unfunded commitment. This flag appears when you use the adjust cost (ac), dividend (dv), or 
	 *  interest (in) transaction for a private equity security type:<br/><br/>
	 * Possible values: (y/n).
	 */
	private String recallable = null;

	/**
	 * symbol for the brokerage firm executing the transaction.
	 */
	private String brokerFirmSymbol = null;

	/**<ol>
	 * <li>for opening transaction (by, li, ti) for a private equity security type, enter the commitment amount for a private equity fund in the fund's currency.<br/> </li>
	 *  <li>for modified commitment (mc) for a private equity security type, enter the amount by which you want to reduce the total commitment amount.<br/> </li>
	 *  <li>for sell transaction (sl), enter the amount that is being transferred to the new owner. </li>
	 * </ol>
	 */
	private String committedCapital = null;

	/**
	 * amount of any contributions for a private equity fund at the time of the trade.
	 */
	private String contributedCapital = null;



	//Some more new fields added on 31st May 2017


	/**
	 * trade Date
	 */
	private String tradeDate = null;

	/**
	 * settlement date
	 */
	private String settlementDate = null;

	/**
	 * Security type code, such as cs, cb, etc. For details, see “Standard Advent Security Types”
	 */
	private String securityType = null;

	/**
	 * ISO currency code for the source or destination security type
	 */
	private String destinationCurrency = null;

	/**
	 * Ticker used to identify the security.
	 */
	private String assetTicker = null;

	/**
	 *  CUSIP used to identify the security.
	 */
	private String assetCUSIP = null;

	/**
	 * SEDOL used to identify the security.
	 */
	private String assetSEDOL = null;

	/**
	 * QUIK used to identify the security.
	 */
	private String assetQUIK = null;

	/**
	 * RIC used to identify the security.
	 */
	private String assetRIC = null;

	/**
	 * FX relationship denominator currency (ISO code)
	 */
	private String fxDenominatorCurrency = null;

	/**
	 *  FX relationship numerator currency (ISO code).
	 */
	private String fxNumeratorCurrency = null;

	/**
	 * Bloomberg Ticker. 
	 */
	private String bloombergTicker = null;

	/**
	 * Underlying Bloomberg Ticker. 
	 */
	private String underBloombergTicker = null;

	/**
	 * Underlying Cusip. 
	 */
	private String underCUSIP = null;

	/**
	 * Underlying ISIN. 
	 */
	private String underISIN = null;

	/**
	 *  Underlying RIC.
	 */
	private String underRIC = null;

	/**
	 * Underlying SEDOL.
	 */
	private String underSEDOL = null;

	/**
	 * Underlying Ticker.
	 */
	private String underTicker = null;

	/**
	 * Price Factor to determine the amount. Amount is Quantity x Price x Price Factor. Typically 1 for equity and 100 for options
	 */
	private String priceFactor = null;

	/**
	 * Price in the base currency. 
	 */
	private String basePrice = null;

	/**
	 * The ISO currency code of the account.
	 */
	private String accountCurrency = null;

	/**
	 * The settlement ISO currency code
	 */
	private String settlementCurrency = null;

	/**
	 * The last business day on which trading can occur for the given series, in mmddccyy format. 
	 * Depending on the product type, this day may be the same as the expiration day or the last business day before the expiration day.
	 */
	private String lastTradeDate = null;

	/**
	 * Indicates that a transaction is a dividend reinvestment. Field may be populated for the “dv” 
	 * and subsequent “by” transaction codes. Valid values are “Y” to indicate a reinvestment.
	 */
	private String dividendReInvestIndicator = null;

	/**
	 * Indicates an American or European Option. Valid values are “A”, “E”, or <>. American options 
	 * can be exercised on any trading day before expiration and carry a premium on trade date. 
	 * European options can only be exercised on expiration and do not carry a premium on trade date.
	 */
	private String optionType = null;

	/**
	 * The ISO 3166‐1 three character country code. This code identifies the country that issued 
	 * the security and differs from the currency code in ISO1 or ISO2. For instance, a French 
	 * security will have a securitycountrycode of “FRA” with an ISO1 of “EUR”.
	 */
	private String securityCountryCode = null;

	/**
	 * Identifies a corporate action. Valid value is “Y” to indicate a corporate action.
	 */
	private String corpActionsIndicator = null;

	/**
	 * A value‐added tax (vat) is a form of consumption tax.
	 */
	private String VAT = null;

	/**
	 * Investment Objective – Maps to Goal. This is the investment objective of the account/portfolio and determines both the target model and style benchmark.<br/>
	 * Investment objectives include:<br/><ul>
	 * <li>Liquidity</li><li>Income Conservative</li><li>Income</li><li>Income with Growth</li><li>Balanced</li><li>Growth with Income</li>
	 * <li>Growth</li><li>Growth Aggressive</li><li>Tax Free Income Conservative</li><li>Tax Free Income</li><li>Tax Free Income with Growth</li>
	 * <li>Tax Free Balanced</li><li>Tax Free Growth with Income</li><li>Tax Free Growth</li><li>Alternatives Only</li><li>Estate</li>
	 * <li>To Be Determined</li><li>Exception</li></ul>
	 */
	private String investmentObjective = null;



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
	public void setHoldingAssetYield(String holdingAssetYield, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetYield)){
			this.holdingAssetYield = ParserUtility.formatAmount(holdingAssetYield.trim());
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
	public void setHoldingAssetQuantity(String holdingAssetQuantity, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetQuantity)){
			this.holdingAssetQuantity = ParserUtility.formatAmount(holdingAssetQuantity.trim());
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
	public void setHoldingAssetAverageUnitCost(String holdingAssetAverageUnitCost, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetAverageUnitCost)){
			this.holdingAssetAverageUnitCost = ParserUtility.formatAmount(holdingAssetAverageUnitCost.trim());
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
	public void setHoldingAssetIndicativePrice(String holdingAssetIndicativePrice, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetIndicativePrice)){
			this.holdingAssetIndicativePrice = ParserUtility.formatAmount(holdingAssetIndicativePrice.trim());
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
	public void setHoldingAssetCost(String holdingAssetCost, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetCost)){
			this.holdingAssetCost = ParserUtility.formatAmount(holdingAssetCost.trim());
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
	public void setHoldingAssetCurrentValue(String holdingAssetCurrentValue, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetCurrentValue)){
			this.holdingAssetCurrentValue = ParserUtility.formatAmount(holdingAssetCurrentValue.trim());
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
	public void setHoldingAssetIndicativePriceDate(String holdingAssetIndicativePriceDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(holdingAssetIndicativePriceDate)){
			this.holdingAssetIndicativePriceDate = ParserUtility.convertToPimoneyDate(holdingAssetIndicativePriceDate.trim(), dateFormat);
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
	public void setHoldingAssetProfit(String holdingAssetProfit, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetProfit)){
			this.holdingAssetProfit = ParserUtility.formatAmount(holdingAssetProfit.trim());
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
	public void setHoldingAssetProfitPerc(String holdingAssetProfitPerc, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetProfitPerc)){
			this.holdingAssetProfitPerc = ParserUtility.formatAmount(holdingAssetProfitPerc.trim());
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
	public void setHoldingAssetMaturityDate(String holdingAssetMaturityDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(holdingAssetMaturityDate)){
			this.holdingAssetMaturityDate = ParserUtility.convertToPimoneyDate(holdingAssetMaturityDate.trim(), dateFormat);
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
	public void setHoldingAssetAccruedInterest(String holdingAssetAccruedInterest, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetAccruedInterest)){
			this.holdingAssetAccruedInterest = ParserUtility.formatAmount(holdingAssetAccruedInterest.trim());
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
	public void setHoldingAssetLastFxRate(String holdingAssetLastFxRate, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetLastFxRate)){
			this.holdingAssetLastFxRate = ParserUtility.formatAmount(holdingAssetLastFxRate.trim());
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
	public void setHoldingAssetFxAccruredInterest(String holdingAssetFxAccruredInterest, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetFxAccruredInterest)){
			this.holdingAssetFxAccruredInterest = ParserUtility.formatAmount(holdingAssetFxAccruredInterest.trim());
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
	public void setHoldingAssetStartDate(String holdingAssetStartDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(holdingAssetStartDate)){
			this.holdingAssetStartDate = ParserUtility.convertToPimoneyDate(holdingAssetStartDate.trim(), dateFormat);
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
	public void setHoldingAssetFxMarketValue(String holdingAssetFxMarketValue, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetFxMarketValue)){
			this.holdingAssetFxMarketValue = ParserUtility.formatAmount(holdingAssetFxMarketValue.trim());
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
	public void setHoldingAssetUnrealizedProfitLoss(String holdingAssetUnrealizedProfitLoss, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetUnrealizedProfitLoss)){
			this.holdingAssetUnrealizedProfitLoss = ParserUtility.formatAmount(holdingAssetUnrealizedProfitLoss.trim());
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
	public void setHoldingAssetCommencingDate(String holdingAssetCommencingDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(holdingAssetCommencingDate)){
			this.holdingAssetCommencingDate = ParserUtility.convertToPimoneyDate(holdingAssetCommencingDate.trim(), dateFormat);
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
	public void setHoldingAssetCoupon(String holdingAssetCoupon, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetCoupon)){
			this.holdingAssetCoupon = ParserUtility.formatAmount(holdingAssetCoupon.trim());
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
	public void setHoldingAssetStrikePrice(String holdingAssetStrikePrice, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetStrikePrice)){
			this.holdingAssetStrikePrice = ParserUtility.formatAmount(holdingAssetStrikePrice.trim());
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
	public void setHoldingAssetInterestTillMaturity(String holdingAssetInterestTillMaturity, boolean format) {
		if(StringUtils.isNotEmpty(holdingAssetInterestTillMaturity)){
			this.holdingAssetInterestTillMaturity = ParserUtility.formatAmount(holdingAssetInterestTillMaturity.trim());
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
		//logger.info("SET HASH --> "  + this.holdingAssetDescription + " :: " + this.fingerprint );
	}



	// NEW FIELDS ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	//############################################################################################
	//############################################################################################
	//############################################################################################
	//############################################################################################


	/**
	 * @return the transCode
	 */
	public String getTransCode() {
		return transCode;
	}
	/**
	 * two letters transaction code to identify the transaction type
	 * @param transCode the transCode to set
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	/**
	 * @return the assetCostDate
	 */
	public String getAssetCostDate() {
		return assetCostDate;
	}
	/**
	 * open date for the position
	 * @param assetCostDate the assetCostDate to set
	 */
	public void setAssetCostDate(String assetCostDate) {
		this.assetCostDate = assetCostDate;
	}
	/**
	 * @return the closingMethodCode
	 */
	public String getClosingMethodCode() {
		return closingMethodCode;
	}
	/**
	 * method for closing the transactions.<br/>
	 * examples are FIFO, LIFO<br/>
	 * choose from the FieldOptions class
	 * @param closingMethodCode the closingMethodCode to set
	 */
	public void setClosingMethodCode(String closingMethodCode) {
		this.closingMethodCode = closingMethodCode;
	}
	/**
	 * @return the versusDate
	 */
	public String getVersusDate() {
		return versusDate;
	}
	/**
	 * You can close against a position opened on a specific date.<br/>
	 * Practically it is an extension of closing method code
	 * @param versusDate the versusDate to set
	 */
	public void setVersusDate(String versusDate) {
		this.versusDate = versusDate;
	}
	/**
	 * @return the srcDstType
	 */
	public String getSrcDstType() {
		return srcDstType;
	}
	/**
	 * Enter the security type code of the source or destination cash account for this transaction.
	 * @param srcDstType the srcDstType to set
	 */
	public void setSrcDstType(String srcDstType) {
		this.srcDstType = srcDstType;
	}
	/**
	 * @return the srcDstSymbol
	 */
	public String getSrcDstSymbol() {
		return srcDstSymbol;
	}
	/**
	 * Enter the symbol for the source or destination cash account for the transaction.  
	 * @param srcDstSymbol the srcDstSymbol to set
	 */
	public void setSrcDstSymbol(String srcDstSymbol) {
		this.srcDstSymbol = srcDstSymbol;
	}
	/**
	 * @return the tradeDateFxRate
	 */
	public String getTradeDateFxRate() {
		return tradeDateFxRate;
	}
	/**
	 * Fx rate as on the transaction date
	 * @param tradeDateFxRate the tradeDateFxRate to set
	 */
	public void setTradeDateFxRate(String tradeDateFxRate) {
		this.tradeDateFxRate = tradeDateFxRate;
	}
	/**
	 * @return the valuationDateFxRate
	 */
	public String getValuationDateFxRate() {
		return valuationDateFxRate;
	}
	/**
	 *  Fx rate as on the settlement/valuation date
	 * @param valuationDateFxRate the valuationDateFxRate to set
	 */
	public void setValuationDateFxRate(String valuationDateFxRate) {
		this.valuationDateFxRate = valuationDateFxRate;
	}
	/**
	 * @return the assetOriginalFxRate
	 */
	public String getAssetOriginalFxRate() {
		return assetOriginalFxRate;
	}
	/**
	 * Fx rate as on the opening date of the position
	 * @param assetOriginalFxRate the assetOriginalFxRate to set
	 */
	public void setAssetOriginalFxRate(String assetOriginalFxRate) {
		this.assetOriginalFxRate = assetOriginalFxRate;
	}
	/**
	 * @return the markToMarket
	 */
	public String isMarkToMarket() {
		return markToMarket;
	}
	/**
	 * If the traded security/currency is different than the portfolio's base currency (currency types in Sec. Type and Source/Dest. Type fields), 
	 * choose whether or not the currency gain or loss affects the security/currency cost basis.<br/><br/>
	 * Possible values: (y/n).
	 * @param markToMarket the markToMarket to set
	 */
	public void setMarkToMarket(String markToMarket) {
		this.markToMarket = markToMarket;
	}
	/**
	 * @return the assetMTM
	 */
	public String getAssetMTM() {
		return assetMTM;
	}
	/**
	 * asset MTM 
	 * @param assetMTM the assetMTM to set
	 */
	public void setAssetMTM(String assetMTM) {
		this.assetMTM = assetMTM;
	}
	/**
	 * @return the assetWithholdingTax
	 */
	public String getAssetWithholdingTax() {
		return assetWithholdingTax;
	}
	/**
	 * amount of foreign tax withheld from an interest payment.
	 * @param assetWithholdingTax the assetWithholdingTax to set
	 */
	public void setAssetWithholdingTax(String assetWithholdingText) {
		this.assetWithholdingTax = assetWithholdingText;
	}
	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}
	/**
	 * exchange code of the securities exchange involved in the transaction.
	 * @param exchange the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	/**
	 * @return the exchangeFee
	 */
	public String getExchangeFee() {
		return exchangeFee;
	}
	/**
	 * amount paid to the SEC or exchange for this transaction.
	 * @param exchangeFee the exchangeFee to set
	 */
	public void setExchangeFee(String exchangeFee) {
		this.exchangeFee = exchangeFee;
	}
	/**
	 * @return the commission
	 */
	public String getCommission() {
		return commission;
	}
	/**
	 * currency amount of the commission paid to the broker for the trade.
	 * @param commission the commission to set
	 */
	public void setCommission(String commision) {
		this.commission = commision;
	}
	/**
	 * @return the otherFees
	 */
	public String getOtherFees() {
		return otherFees;
	}
	/**
	 * amount of any other fees applicable to this transaction (Example: stamp fees).
	 * @param otherFees the otherFees to set
	 */
	public void setOtherFees(String otherFees) {
		this.otherFees = otherFees;
	}
	/**
	 * @return the impliedCommission
	 */
	public String isImpliedCommission() {
		return impliedCommission;
	}
	/**
	 * Indicate whether or not the commission of this transaction is "implied," which means that it is 
	 * included in the price of the security, and therefore affects its cost basis. Example: An OTC trade has an implied commission.<br/><br/>
	 * Possible values: (y/n).
	 * @param impliedCommission the impliedCommission to set
	 */
	public void setImpliedCommission(String impliedCommission) {
		this.impliedCommission = impliedCommission;
	}
	/**
	 * @return the commissionPurpose
	 */
	public String getCommissionPurpose() {
		return commissionPurpose;
	}
	/**
	 * purpose associated with the commission (Example: Payment to Broker, Soft Dollar--Research)
	 * @param commissionPurpose the commissionPurpose to set
	 */
	public void setCommissionPurpose(String commissionPurpose) {
		this.commissionPurpose = commissionPurpose;
	}
	/**
	 * @return the assetPledge
	 */
	public String getAssetPledge() {
		return assetPledge;
	}
	/**
	 * Indicate whether or not this security is pledged as collateral.
	 * @param assetPledge the assetPledge to set
	 */
	public void setAssetPledge(String assetPledge) {
		this.assetPledge = assetPledge;
	}
	/**
	 * @return the destinationPledge
	 */
	public String getDestinationPledge() {
		return destinationPledge;
	}
	/**
	 * new pledge status of the security. This field appears in long change (lc) and short change (sc) transactions only.
	 * @param destinationPledge the destinationPledge to set
	 */
	public void setDestinationPledge(String destinationPledge) {
		this.destinationPledge = destinationPledge;
	}
	/**
	 * @return the destinationCustodian
	 */
	public String getDestinationCustodian() {
		return destinationCustodian;
	}
	/**
	 * the custodian or location to which you want to transfer the security.
	 * @param destinationCustodian the destinationCustodian to set
	 */
	public void setDestinationCustodian(String destinationCustodian) {
		this.destinationCustodian = destinationCustodian;
	}
	/**
	 * @return the assetDuration
	 */
	public String getAssetDuration() {
		return assetDuration;
	}
	/**
	 * duration on cost.
	 * @param assetDuration the assetDuration to set
	 */
	public void setAssetDuration(String assetDuration) {
		this.assetDuration = assetDuration;
	}
	/**
	 * @return the recordDate
	 */
	public String getRecordDate() {
		return recordDate;
	}
	/**
	 * the date on which the shareholder must officially own shares of the security in order to be entitled to a dividend paid for the security.
	 * @param recordDate the recordDate to set
	 */
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return strategy;
	}
	/**
	 * the Strategy Code associated with this transaction. Example: You might choose "Stock A Straddle" 
	 * to associate Stock A option transactions with positions of the underlying security.
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	/**
	 * @return the reclaimAmount
	 */
	public String getReclaimAmount() {
		return reclaimAmount;
	}
	/**
	 * the amount of foreign withholding tax that is reclaimable from the issuer country. The reclaimable amount is defined by tax rules of the issuer country.
	 * @param reclaimAmount the reclaimAmount to set
	 */
	public void setReclaimAmount(String reclaimAmount) {
		this.reclaimAmount = reclaimAmount;
	}
	/**
	 * @return the accrualAccount
	 */
	public String getAccrualAccount() {
		return accrualAccount;
	}
	/**
	 * For a dividend (dv) transaction you enter, define the accrual account if you chose to accrue the dividend. 
	 * @param accrualAccount the accrualAccount to set
	 */
	public void setAccrualAccount(String accrualAccount) {
		this.accrualAccount = accrualAccount;
	}
	/**
	 * @return the dividendAccrualMethod
	 */
	public String getDividendAccrualMethod() {
		return dividendAccrualMethod;
	}
	/**
	 * preferred accrual method for dividend<br/>
	 * choose from FieldOptions class
	 * @param dividendAccrualMethod the dividendAccrualMethod to set
	 */
	public void setDividendAccrualMethod(String dividendAccrualMethod) {
		this.dividendAccrualMethod = dividendAccrualMethod;
	}
	/**
	 * @return the mgmtFeePeriodDate
	 */
	public String getMgmtFeePeriodDate() {
		return mgmtFeePeriodDate;
	}
	/**
	 * When you enter a management fee transaction (dp), use the Fee Period Date field to accrue management 
	 * fees based on the number of calendar days in the billing period. The dates in the Trade Date and Fee Period 
	 * Date fields of the management fee transaction determine the billing period. When you define the Fee Period Date field, 
	 * the management fee is typically calculated using the number of calendar days between the trade date and the fee period date.
	 * <br/><br/>
	 * <table style="vertical-align: top; left: 0px; top: 300px;" cellspacing="0" width="576"><colgroup><col style="width: 10.377%;"><col style="width: 11.959%;">
	 *<col style="width: 13.396%;"><col style="width: 10.885%;"><col style="width: 13.02%;"><col style="width: 17.189%;"><col style="width: 10.528%;"><col style="width: 12.5%;"></colgroup><tbody><tr style="vertical-align: top; height: 48px;"><td style="border: Solid 1px #ffffff; width: 10.377%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Tran. Code</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 11.959%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Sec. Type</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.396%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Security Symbol</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.885%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Trade Date</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.02%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Trade Amount</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 17.189%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Fee Period Date</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.528%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Source Type</p></td><td style="border-top: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 12.5%; padding-right: 10px; padding-left: 10px;"><p class="TableHeading">Source<br>Symbol</p></td></tr><tr style="vertical-align: top; height: 33px;"><td style="border-left: Solid 1px #ffffff; border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.377%; padding-right: 10px; padding-left: 10px;"><p>dp</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 11.959%; padding-right: 10px; padding-left: 10px;"><p>epus</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.396%; padding-right: 10px; padding-left: 10px;"><p>manfee</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.885%; padding-right: 10px; padding-left: 10px;"><p>01/01/2007</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 13.02%; padding-right: 10px; padding-left: 10px;"><p>100</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 17.189%; padding-right: 10px; padding-left: 10px;"><p>03/31/2007</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 10.528%; padding-right: 10px; padding-left: 10px;"><p>caus</p></td><td style="border-right: Solid 1px #ffffff; border-bottom: Solid 1px #ffffff; width: 12.5%; padding-right: 10px; padding-left: 10px;"><p>cash</p></td></tr>
	 *</tbody></table><br/>
	 *In this example, a prorated fee of $1.11 ($100/90 days in the period) a day is calculated.
	 * @param mgmtFeePeriodDate the mgmtFeePeriodDate to set
	 */
	public void setMgmtFeePeriodDate(String mgmtFeePeriodDate) {
		this.mgmtFeePeriodDate = mgmtFeePeriodDate;
	}
	/**
	 * @return the recallable
	 */
	public String isRecallable() {
		return recallable;
	}
	/**
	 * Indicate whether or not the distribution from a private equity fund is recallable. A recallable distribution increases
	 *  the amount of unfunded commitment. This flag appears when you use the adjust cost (ac), dividend (dv), or 
	 *  interest (in) transaction for a private equity security type:<br/><br/>
	 * Possible values: (y/n).
	 * @param recallable the recallable to set
	 */
	public void setRecallable(String recallable) {
		this.recallable = recallable;
	}
	/**
	 * @return the brokerFirmSymbol
	 */
	public String getBrokerFirmSymbol() {
		return brokerFirmSymbol;
	}
	/**
	 * symbol for the brokerage firm executing the transaction.
	 * @param brokerFirmSymbol the brokerFirmSymbol to set
	 */
	public void setBrokerFirmSymbol(String brokerFirmSymbol) {
		this.brokerFirmSymbol = brokerFirmSymbol;
	}
	/**
	 * @return the committedCapital
	 */
	public String getCommittedCapital() {
		return committedCapital;
	}
	/**
	 * <ol>
	 * <li>for opening transaction (by, li, ti) for a private equity security type, enter the commitment amount for a private equity fund in the fund's currency.<br/> </li>
	 *  <li>for modified commitment (mc) for a private equity security type, enter the amount by which you want to reduce the total commitment amount.<br/> </li>
	 *  <li>for sell transaction (sl), enter the amount that is being transferred to the new owner. </li>
	 * </ol>
	 * @param committedCapital the committedCapital to set
	 */
	public void setCommittedCapital(String commitedCapital) {
		this.committedCapital = commitedCapital;
	}
	/**
	 * @return the contributedCapital
	 */
	public String getContributedCapital() {
		return contributedCapital;
	}
	/**
	 * amount of any contributions for a private equity fund at the time of the trade.
	 */
	public void setContributedCapital(String contributedCapital) {
		this.contributedCapital = contributedCapital;
	}


	//some more new fields added on 29 May 2017



	/**
	 * @return the securityType
	 */
	public String getSecurityType() {
		return securityType;
	}
	/**
	 *  Security type code, such as cs, cb, etc. For details, see “Standard Advent Security Types”
	 * @param securityType the securityType to set
	 */
	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}
	/**
	 * @return the destinationCurrency
	 */
	public String getDestinationCurrency() {
		return destinationCurrency;
	}
	/**
	 * ISO currency code for the source or destination security type
	 * @param destinationCurrency the destinationCurrency to set
	 */
	public void setDestinationCurrency(String destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}
	/**
	 * @return the assetTicker
	 */
	public String getAssetTicker() {
		return assetTicker;
	}
	/**
	 * Ticker used to identify the security.
	 * @param assetTicker the assetTicker to set
	 */
	public void setAssetTicker(String assetTicker) {
		this.assetTicker = assetTicker;
	}
	/**
	 * @return the assetCUSIP
	 */
	public String getAssetCUSIP() {
		return assetCUSIP;
	}
	/**
	 *  CUSIP used to identify the security.
	 * @param assetCUSIP the assetCUSIP to set
	 */
	public void setAssetCUSIP(String assetCUSIP) {
		this.assetCUSIP = assetCUSIP;
	}
	/**
	 * @return the assetSEDOL
	 */
	public String getAssetSEDOL() {
		return assetSEDOL;
	}
	/**
	 * SEDOL used to identify the security.
	 * @param assetSEDOL the assetSEDOL to set
	 */
	public void setAssetSEDOL(String assetSEDOL) {
		this.assetSEDOL = assetSEDOL;
	}
	/**
	 * @return the assetQUIK
	 */
	public String getAssetQUIK() {
		return assetQUIK;
	}
	/**
	 * QUIK used to identify the security.
	 * @param assetQUIK the assetQUIK to set
	 */
	public void setAssetQUIK(String assetQUIK) {
		this.assetQUIK = assetQUIK;
	}
	/**
	 * @return the fxDenominatorCurrency
	 */
	public String getFxDenominatorCurrency() {
		return fxDenominatorCurrency;
	}
	/**
	 * FX relationship denominator currency (ISO code)
	 * @param fxDenominatorCurrency the fxDenominatorCurrency to set
	 */
	public void setFxDenominatorCurrency(String fxDenominatorCurrency) {
		this.fxDenominatorCurrency = fxDenominatorCurrency;
	}
	/**
	 * @return the fxNumeratorCurrency
	 */
	public String getFxNumeratorCurrency() {
		return fxNumeratorCurrency;
	}
	/**
	 *  FX relationship numerator currency (ISO code).
	 * @param fxNumeratorCurrency the fxNumeratorCurrency to set
	 */
	public void setFxNumeratorCurrency(String fxNumeratorCurrency) {
		this.fxNumeratorCurrency = fxNumeratorCurrency;
	}
	/**
	 * @return the underBloombergTicker
	 */
	public String getUnderBloombergTicker() {
		return underBloombergTicker;
	}
	/**
	 * Underlying Bloomberg Ticker. 
	 * @param underBloombergTicker the underBloombergTicker to set
	 */
	public void setUnderBloombergTicker(String underBloombergTicker) {
		this.underBloombergTicker = underBloombergTicker;
	}
	/**
	 * @return the underCUSIP
	 */
	public String getUnderCUSIP() {
		return underCUSIP;
	}
	/**
	 * Underlying Cusip. 
	 * @param underCUSIP the underCUSIP to set
	 */
	public void setUnderCUSIP(String underCUSIP) {
		this.underCUSIP = underCUSIP;
	}
	/**
	 * @return the underISIN
	 */
	public String getUnderISIN() {
		return underISIN;
	}
	/**
	 * Underlying ISIN. 
	 * @param underISIN the underISIN to set
	 */
	public void setUnderISIN(String underISIN) {
		this.underISIN = underISIN;
	}
	/**
	 * @return the underRIC
	 */
	public String getUnderRIC() {
		return underRIC;
	}
	/**
	 *  Underlying RIC.
	 * @param underRIC the underRIC to set
	 */
	public void setUnderRIC(String underRIC) {
		this.underRIC = underRIC;
	}
	/**
	 * @return the underSEDOL
	 */
	public String getUnderSEDOL() {
		return underSEDOL;
	}
	/**
	 * Underlying SEDOL.
	 * @param underSEDOL the underSEDOL to set
	 */
	public void setUnderSEDOL(String underSEDOL) {
		this.underSEDOL = underSEDOL;
	}
	/**
	 * @return the underTicker
	 */
	public String getUnderTicker() {
		return underTicker;
	}
	/**
	 * Underlying Ticker.
	 * @param underTicker the underTicker to set
	 */
	public void setUnderTicker(String underTicker) {
		this.underTicker = underTicker;
	}
	/**
	 * @return the priceFactor
	 */
	public String getPriceFactor() {
		return priceFactor;
	}
	/**
	 * Price Factor to determine the amount. Amount is Quantity x Price x Price Factor. Typically 1 for equity and 100 for options
	 * @param priceFactor the priceFactor to set
	 */
	public void setPriceFactor(String priceFactor) {
		this.priceFactor = priceFactor;
	}
	/**
	 * @return the basePrice
	 */
	public String getBasePrice() {
		return basePrice;
	}
	/**
	 * Price in the base currency. 
	 * @param basePrice the basePrice to set
	 */
	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}
	/**
	 * @return the accountCurrency
	 */
	public String getAccountCurrency() {
		return accountCurrency;
	}
	/**
	 * The ISO currency code of the account.
	 * @param accountCurrency the accountCurrency to set
	 */
	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}
	/**
	 * @return the settlementCurrency
	 */
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	/**
	 * The settlement ISO currency code
	 * @param settlementCurrency the settlementCurrency to set
	 */
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}
	/**
	 * @return the corpActionsIndicator
	 */
	public String getCorpActionsIndicator() {
		return corpActionsIndicator;
	}
	/**
	 * Identifies a corporate action. Valid value is “Y” to indicate a corporate action.
	 * @param corpActionsIndicator the corpActionsIndicator to set
	 */
	public void setCorpActionsIndicator(String corpActionsIndicator) {
		this.corpActionsIndicator = corpActionsIndicator;
	}
	/**
	 * @return the tradeDate
	 */
	public String getTradeDate() {
		return tradeDate;
	}
	/**
	 * trade Date
	 * @param tradeDate the tradeDate to set
	 */
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	
	public void setTradeDate(String tradeDate, String dateFormat) throws ParseException {
		this.tradeDate = ParserUtility.convertToPimoneyDate(tradeDate, dateFormat);
	}
	/**
	 * @return the settlementDate
	 */
	public String getSettlementDate() {
		return settlementDate;
	}
	/**
	 *  settlement date
	 * @param settlementDate the settlementDate to set
	 */
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	/**
	 * @return the assetRIC
	 */
	public String getAssetRIC() {
		return assetRIC;
	}
	/**
	 * RIC used to identify the security.
	 * @param assetRIC the assetRIC to set
	 */
	public void setAssetRIC(String assetRIC) {
		this.assetRIC = assetRIC;
	}
	/**
	 * @return the bloombergTicker
	 */
	public String getBloombergTicker() {
		return bloombergTicker;
	}
	/**
	 * Bloomberg Ticker. 
	 * @param bloombergTicker the bloombergTicker to set
	 */
	public void setBloombergTicker(String bloombergTicker) {
		this.bloombergTicker = bloombergTicker;
	}
	/**
	 * @return the lastTradeDate
	 */
	public String getLastTradeDate() {
		return lastTradeDate;
	}
	/**
	 * The last business day on which trading can occur for the given series, in mmddccyy format. 
	 * Depending on the product type, this day may be the same as the expiration day or the last business day before the expiration day.
	 * @param lastTradeDate the lastTradeDate to set
	 */
	public void setLastTradeDate(String lastTradeDate) {
		this.lastTradeDate = lastTradeDate;
	}
	/**
	 * @return the dividendReInvestIndicator
	 */
	public String getDividendReInvestIndicator() {
		return dividendReInvestIndicator;
	}
	/**
	 * Indicates that a transaction is a dividend reinvestment. Field may be populated for the “dv” 
	 * and subsequent “by” transaction codes. Valid values are “Y” to indicate a reinvestment.
	 * @param dividendReInvestIndicator the dividendReInvestIndicator to set
	 */
	public void setDividendReInvestIndicator(String dividendReInvestIndicator) {
		this.dividendReInvestIndicator = dividendReInvestIndicator;
	}
	/**
	 * @return the optionType
	 */
	public String getOptionType() {
		return optionType;
	}
	/**
	 * Indicates an American or European Option. Valid values are “A”, “E”, or <>. American options 
	 * can be exercised on any trading day before expiration and carry a premium on trade date. 
	 * European options can only be exercised on expiration and do not carry a premium on trade date.
	 * @param optionType the optionType to set
	 */
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	/**
	 * @return the securityCountryCode
	 */
	public String getSecurityCountryCode() {
		return securityCountryCode;
	}
	/**
	 * The ISO 3166‐1 three character country code. This code identifies the country that issued 
	 * the security and differs from the currency code in ISO1 or ISO2. For instance, a French 
	 * security will have a securitycountrycode of “FRA” with an ISO1 of “EUR”.
	 * @param securityCountryCode the securityCountryCode to set
	 */
	public void setSecurityCountryCode(String securityCountryCode) {
		this.securityCountryCode = securityCountryCode;
	}
	/**
	 * @return the vAT
	 */
	public String getVAT() {
		return VAT;
	}
	/**
	 * A value‐added tax (vat) is a form of consumption tax.
	 * @param vAT the vAT to set
	 */
	public void setVAT(String vAT) {
		VAT = vAT;
	}
	/**
	 * @return the investmentObjective
	 */
	public String getInvestmentObjective() {
		return investmentObjective;
	}
	/**
	 *  Investment Objective – Maps to Goal. This is the investment objective of the account/portfolio and determines both the target model and style benchmark.<br/>
	 * Investment objectives include:<br/><ul>
	 * <li>Liquidity</li><li>Income Conservative</li><li>Income</li><li>Income with Growth</li><li>Balanced</li><li>Growth with Income</li>
	 * <li>Growth</li><li>Growth Aggressive</li><li>Tax Free Income Conservative</li><li>Tax Free Income</li><li>Tax Free Income with Growth</li>
	 * <li>Tax Free Balanced</li><li>Tax Free Growth with Income</li><li>Tax Free Growth</li><li>Alternatives Only</li><li>Estate</li>
	 * <li>To Be Determined</li><li>Exception</li></ul>
	 * @param investmentObjective the investmentObjective to set
	 */
	public void setInvestmentObjective(String investmentObjective) {
		this.investmentObjective = investmentObjective;
	}
	
	
}
