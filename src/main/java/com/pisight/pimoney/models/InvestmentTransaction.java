package com.pisight.pimoney.models;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

import com.pisight.pimoney.beans.ParserUtility;

/**
 * @author kumar
 *
 */
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
	private String postDate = null;
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
	private String assetIssuer = null;
	private String assetISIN = "tbd";
	private String valuationDate = null;
	private String startDate = null;
	private String maturityDate = null;
	private String coupon = null;
	private String accruedInterest = null;
	private String dateFormat = null;
	private String strikePrice = null;
	private String expiryDate = null;
	private String brokerageAndLevies = null;


	//new fields added on 17th May 2017

	/**
	 * two letters transaction code to identify the transaction type
	 */
	private String transCode = "tbd";

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
	 * choose whether or not the currency gain or loss affects the security/currency cost basis.<br/>
	 * Possible values: (y/n).
	 */
	private String  markToMarket = null;;

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
	 * included in the price of the security, and therefore affects its cost basis. Example: An OTC trade has an implied commission.<br/>
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
	 * identify whether a position is short or long<br/>
	 * Possible values: (y/n).
	 */
	private String shortPositions = null;

	/**
	 * field to indicate whether or not the dividend is actual/final, or an estimate<br/>
	 * choose from fieldOptions class
	 */
	private String dividendStatus = null;

	/**
	 * Indicate whether or not the distribution from a private equity fund is recallable. A recallable distribution increases
	 *  the amount of unfunded commitment. This flag appears when you use the adjust cost (ac), dividend (dv), or 
	 *  interest (in) transaction for a private equity security type:<br/>
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
	
	
	
	//Some more new fields added on 29 May 2017
	
	/**
	 * Security type code, such as cs, cb, etc. For details, see “Standard Advent Security Types”
	 */
	private String securityType = "tbd";
	
	/**
	 * Security identifier. As a rule, this field contains one of the following values:<br/><ul>
	 *  <li>Ticker for options and for other security types when available. </li>
	 *  <li> CUSIP for USD securities. </li>
	 *  <li> SEDOL for non‐USD securities </li></ul>
	 */
	private String securityId = null;
	
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
	 * FX relationship denominator currency (ISO code)
	 */
	private String fxDenominatorCurrency = null;
	
	/**
	 *  FX relationship numerator currency (ISO code).
	 */
	private String fxNumeratorCurrency = null;
	
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
	 * Identifies a corporate action. Valid value is “Y” to indicate a corporate action.
	 */
	private String corpActionsIndicator = null;

	
	
	
	
	
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
			this.accountNumber = accountNumber.trim();
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
			this.subAccountNumber = subAccountNumber.trim();
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
			this.assetCategory = assetCategory.trim();
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
			this.assetName = assetName.trim();
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
			this.assetMarket = assetMarket.trim();
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
			this.assetInstrument = assetInstrument.trim();
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
			this.assetYield = assetYield.trim();
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
			this.assetQuantity = assetQuantity.trim();
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
			this.assetUnitCost = assetUnitCost.trim();
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
			this.assetCost = assetCost.trim();
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
			this.assetTradeDate = assetTradeDate.trim();
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
			this.assetCustodian = assetCustodian.trim();
		}
	}
	/**
	 * @return the assetIssuer
	 */
	public String getAssetIssuer() {
		return assetIssuer;
	}
	/**
	 * @param assetIssuer the assetIssuer to set
	 */
	public void setAssetIssuer(String assetBondIssuer) {
		if(StringUtils.isNotEmpty(assetBondIssuer)){
			this.assetIssuer = assetBondIssuer.trim();
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
			this.assetISIN = assetISIN.trim();
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
			this.valuationDate = valuationDate.trim();
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
			this.type = type.trim();
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
			this.amount = amount.trim();
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
			this.transactionDate = transactionDate.trim();
		}
	}
	public void setTransactionDate(String transactionDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(transactionDate)){
			this.transactionDate = ParserUtility.convertToPimoneyDate(transactionDate, dateFormat);
		}
	}
	
	public String getPostDate() {
		return postDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setPostDate(String postDate) {
		if(StringUtils.isNotEmpty(postDate)){
			this.postDate = postDate.trim();
		}
	}
	public void setPostDate(String postDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(postDate)){
			this.postDate = ParserUtility.convertToPimoneyDate(postDate, dateFormat);
		}
	}
	/**
	 * @return the Description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param escription the escription to set
	 */
	public void setDescription(String description) {
		if(StringUtils.isNotEmpty(description)){
			this.description = description.trim();
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
	 * @return the strikePrice
	 */
	public String getStrikePrice() {
		return strikePrice;
	}
	/**
	 * @param strikePrice the strikePrice to set
	 */
	public void setStrikePrice(String strikePrice) {
		if(StringUtils.isNotEmpty(strikePrice)){
			this.strikePrice = strikePrice;
		}
	}
	public void setStrikePrice(String strikePrice, boolean format) {
		if(StringUtils.isNotEmpty(strikePrice)){
			this.strikePrice = ParserUtility.formatAmount(strikePrice);
		}
	}
	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		if(StringUtils.isNotEmpty(expiryDate)){
			this.expiryDate = expiryDate;
		}
	}
	public void setExpiryDate(String expiryDate, String dateFormat) throws ParseException {
		if(StringUtils.isNotEmpty(expiryDate)){
			this.expiryDate = ParserUtility.convertToPimoneyDate(expiryDate, dateFormat);
		}
	}
	/**
	 * @return the brokerageAndLevies
	 */
	public String getBrokerageAndLevies() {
		return brokerageAndLevies;
	}
	/**
	 * @param brokerageAndLevies the brokerageAndLevies to set
	 */
	public void setBrokerageAndLevies(String brokerageAndLevies) {
		if(StringUtils.isNotEmpty(brokerageAndLevies)){
			this.brokerageAndLevies = brokerageAndLevies;
		}
	}
	public void setBrokerageAndLevies(String brokerageAndLevies, boolean format) {
		if(StringUtils.isNotEmpty(brokerageAndLevies)){
			this.brokerageAndLevies = ParserUtility.formatAmount(brokerageAndLevies);
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
	 * Indicate whether or not this security is pledged as collateral.<br/><br/>
	 * Possible values: (y/n).
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
	 * @return the shortPositions
	 */
	public String isShortPositions() {
		return shortPositions;
	}
	/**
	 * identify whether a position is short or long<br/><br/>
	 * Possible values: (y/n).
	 * @param shortPositions the shortPositions to set
	 */
	public void setShortPositions(String shortPositions) {
		this.shortPositions = shortPositions;
	}
	/**
	 * @return the dividendStatus
	 */
	public String getDividendStatus() {
		return dividendStatus;
	}
	/**
	 * field to indicate whether or not the dividend is actual/final, or an estimate<br/>
	 * choose from fieldOptions class
	 * @param dividendStatus the dividendStatus to set
	 */
	public void setDividendStatus(String dividendStatus) {
		this.dividendStatus = dividendStatus;
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
	 * @return the securityId
	 */
	public String getSecurityId() {
		return securityId;
	}
	/**
	 * Security identifier. As a rule, this field contains one of the following values:<br/><ul>
	 *  <li>Ticker for options and for other security types when available. </li>
	 *  <li> CUSIP for USD securities. </li>
	 *  <li> SEDOL for non‐USD securities </li></ul>
	 * @param securityId the securityId to set
	 */
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
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

	
}
