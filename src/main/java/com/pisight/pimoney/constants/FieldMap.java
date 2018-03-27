package com.pisight.pimoney.constants;

import java.util.HashMap;

public class FieldMap {

	public static final String KEY_ACCOUNT_HOLDER = "AccountHolder";
	public static final String KEY_BRANCH = "Branch";
	public static final String KEY_CURRENCY = "Currency";
	public static final String KEY_ACCOUNT_NUMBER = "AccountNumber";
	public static final String KEY_ACCOUNT_BALANCE = "AccountBalance";
	public static final String KEY_BALANCE = "Balance";
	public static final String KEY_BILL_DATE = "BillDate";
	public static final String KEY_ACCOUNT_NAME = "AccountName";
	public static final String KEY_TOTAL_LIMIT = "TotalLimit";
	public static final String KEY_DUE_DATE = "DueDate";
	public static final String KEY_AMOUNT_DUE = "AmountDue";
	public static final String KEY_MIN_AMOUNT_DUE = "MinAmountDue";
	public static final String KEY_AVAILABLE_CREDIT = "AvailableCredit";
	public static final String KEY_LAST_STATEMENT_BALANCE = "LastStatementBalance";
	public static final String KEY_RELATIONSHIP_MANAGER = "RelationshipManager";


	public static final HashMap<String, String> map = new HashMap<String, String>();
	public static final HashMap<String, String> mapTrans = new HashMap<String, String>();
	public static final HashMap<String, String> mapAsset = new HashMap<String, String>();
	public static final HashMap<String, String> mapSCX = new HashMap<String, String>();
	public static final HashMap<String, String> mapAssetTrans = new HashMap<String, String>();

	static{
		//Account level
		map.put(KEY_ACCOUNT_HOLDER, "KEY_ACCOUNT_HOLDER");
		map.put(KEY_CURRENCY, "iso1");
		map.put(KEY_BRANCH, "KEY_BRANCH");
		map.put(KEY_ACCOUNT_NUMBER, "portfolio");
		map.put(KEY_ACCOUNT_BALANCE, "KEY_ACCOUNT_BALANCE");
		map.put(KEY_BALANCE, "KEY_BALANCE");
		map.put(KEY_BILL_DATE, "KEY_BILL_DATE");
		map.put(KEY_ACCOUNT_NAME, "KEY_ACCOUNT_NAME");
		map.put(KEY_TOTAL_LIMIT, "KEY_TOTAL_LIMIT");
		map.put(KEY_DUE_DATE, "KEY_DUE_DATE");
		map.put(KEY_AMOUNT_DUE, "KEY_AMOUNT_DUE");
		map.put(KEY_MIN_AMOUNT_DUE, "KEY_MIN_AMOUNT_DUE");
		map.put(KEY_AVAILABLE_CREDIT, "KEY_AVAILABLE_CREDIT");
		map.put(KEY_LAST_STATEMENT_BALANCE, "KEY_LAST_STATEMENT_BALANCE");
		map.put(KEY_RELATIONSHIP_MANAGER, "KEY_RELATIONSHIP_MANAGER");

		//Transaction level
		mapTrans.put(TransactionFields.KEY_TRANS_DATE, "trade");
		mapTrans.put(TransactionFields.KEY_POST_DATE, "settle");
		mapTrans.put(TransactionFields.KEY_DESCRIPTION, "securitydescription");
		mapTrans.put(TransactionFields.KEY_AMOUNT, "amount");
		mapTrans.put(TransactionFields.KEY_RUNNING_BALANCE, "");
		mapTrans.put(TransactionFields.KEY_TRANSACTION_TYPE, "");
		mapTrans.put(TransactionFields.KEY_SUB_ACCOUNT_NUMBER, "subaccountnumber");
		mapTrans.put(TransactionFields.KEY_TYPE, "");
		mapTrans.put(TransactionFields.KEY_TRANSACTION_DATE, "trade");
		mapTrans.put(TransactionFields.KEY_ASSET_CATEGORY, "assettype");
		mapTrans.put(TransactionFields.KEY_ASSET_NAME, "");
		mapTrans.put(TransactionFields.KEY_ASSET_MARKET, "");
		mapTrans.put(TransactionFields.KEY_ASSET_INSTRUMENT, "");
		mapTrans.put(TransactionFields.KEY_ASSET_YIELD, "ytm");
		mapTrans.put(TransactionFields.KEY_ASSET_QUANTITY, "quantity");
		mapTrans.put(TransactionFields.KEY_ASSET_UNIT_COST, ""); //origface. tag not required by taurus. this value is being set in "taxcost" tag with prefix "@"
		mapTrans.put(TransactionFields.KEY_ASSET_COST, "taxcost");
		mapTrans.put(TransactionFields.KEY_ASSET_TRADE_DATE, "");  // origyld.  asked to remove
		mapTrans.put(TransactionFields.KEY_ASSET_CUSTODIAN, "custodianname");
		mapTrans.put(TransactionFields.KEY_ASSET_ISSUER, "");
		mapTrans.put(TransactionFields.KEY_ASSET_ISIN, ""); // equal to symbol1
		mapTrans.put(TransactionFields.KEY_VALUATION_DATE, "");
		mapTrans.put(TransactionFields.KEY_START_DATE, "");
		mapTrans.put(TransactionFields.KEY_MATURITY_DATE, "maturityexpirationdate");
		mapTrans.put(TransactionFields.KEY_COUPON, "couponrate ");
		mapTrans.put(TransactionFields.KEY_ACCRUED_INTEREST, "localaccruedinterest");
		mapTrans.put(TransactionFields.KEY_STRIKE_PRICE, "strikeprice");
		mapTrans.put(TransactionFields.KEY_EXPIRY_DATE, "");
		mapTrans.put(TransactionFields.KEY_BROKRAGE_N_LEVIES, "");

		//New Transaction level fields
		mapTrans.put(TransactionFields.KEY_TRANS_CODE, "trancode");
		mapTrans.put(TransactionFields.KEY_ASSET_COST_DATE, "taxdate");
		mapTrans.put(TransactionFields.KEY_CLOSING_METHOD_CODE, "cmeth");
		mapTrans.put(TransactionFields.KEY_VERSUS_DATE, "versus");
		mapTrans.put(TransactionFields.KEY_SRC_DST_TYPE, "type2");
		mapTrans.put(TransactionFields.KEY_SRC_DST_SYMBOL, "symbol2");
		mapTrans.put(TransactionFields.KEY_TRADE_DATE_FX_RATE, "tdfx");
		mapTrans.put(TransactionFields.KEY_VALUATION_DATE_FX_RATE, "sdfx");
		mapTrans.put(TransactionFields.KEY_ASSET_ORIGINAL_FX_RATE, "taxfx");
		mapTrans.put(TransactionFields.KEY_MARK_TO_MARKET, "mtm");
		mapTrans.put(TransactionFields.KEY_ASSET_MTM, "KEY_ASSET_MTM");
		mapTrans.put(TransactionFields.KEY_ASSET_WITHHOLDING_TAX, "fwtax");
		mapTrans.put(TransactionFields.KEY_EXCHANGE, "exchange");
		mapTrans.put(TransactionFields.KEY_EXCHANGE_FEE, "secfee");
		mapTrans.put(TransactionFields.KEY_COMMISSION, "commis");
		mapTrans.put(TransactionFields.KEY_OTHER_FEES, "other");
		mapTrans.put(TransactionFields.KEY_IMPLIED_COMMISSION, "implied");
		mapTrans.put(TransactionFields.KEY_COMMISSION_PURPOSE, "compurp");
		mapTrans.put(TransactionFields.KEY_ASSET_PLEDGE, "pledge");
		mapTrans.put(TransactionFields.KEY_DESTINATION_PLEDGE, "dstpledge");
		mapTrans.put(TransactionFields.KEY_DESTINATION_CUSTODIAN, "dstloc");
		mapTrans.put(TransactionFields.KEY_ASSET_DURATION, "origdur");
		mapTrans.put(TransactionFields.KEY_RECORD_DATE, "record");
		mapTrans.put(TransactionFields.KEY_STRATEGY, "strategy");
		mapTrans.put(TransactionFields.KEY_RECLAIM_AMOUNT, "reclaim");
		mapTrans.put(TransactionFields.KEY_ACCRUAL_ACCOUNT, "divacc");
		mapTrans.put(TransactionFields.KEY_DIVIDEND_ACCRUAL_METHOD, "divaccmth");
		mapTrans.put(TransactionFields.KEY_MGMT_FEE_PERIOD_DATE, "feedate");
		mapTrans.put(TransactionFields.KEY_RECALLABLE, "isrecallable	");
		mapTrans.put(TransactionFields.KEY_BROKER_FIRM_SYMBOL, "broker");
		mapTrans.put(TransactionFields.KEY_COMMITTED_CAPITAL, "committedcapital");
		mapTrans.put(TransactionFields.KEY_CONTRIBUTED_CAPITAL, "contributedcapital");

		//some more new fields
		mapTrans.put(TransactionFields.KEY_SECURITY_TYPE, "type1"); //type1. Asked to remove this tag
		mapTrans.put(TransactionFields.KEY_SECURITY_ID, "symbol1");
		mapTrans.put(TransactionFields.KEY_DEST_CURRENCY, "iso2");
		mapTrans.put(TransactionFields.KEY_ASSET_TICKER, "ticker");
		mapTrans.put(TransactionFields.KEY_ASSET_CUSIP, "cusip");
		mapTrans.put(TransactionFields.KEY_ASSET_SEDOL, "sedol");
		mapTrans.put(TransactionFields.KEY_ASSET_QUIK, "quik");
		mapTrans.put(TransactionFields.KEY_FX_DENOMINATOR_CURRENCY, "fxdenominatorcurrencycode");
		mapTrans.put(TransactionFields.KEY_FX_NUMERATOR_CURRENCY, "fxnumeratorcurrencycode");
		mapTrans.put(TransactionFields.KEY_UNDER_BLOOMBERG_TICKER, "underbloombergticker");
		mapTrans.put(TransactionFields.KEY_UNDER_CUSIP, "undercusip");
		mapTrans.put(TransactionFields.KEY_UNDER_ISIN, "underisin");
		mapTrans.put(TransactionFields.KEY_UNDER_RIC, "underric");
		mapTrans.put(TransactionFields.KEY_UNDER_SEDOL, "undersedol");
		mapTrans.put(TransactionFields.KEY_UNDER_TICKER, "underticker");
		mapTrans.put(TransactionFields.KEY_PRICE_FACTOR, "pricefactor");
		mapTrans.put(TransactionFields.KEY_BASE_PRICE, "baseprice");
		mapTrans.put(TransactionFields.KEY_ACCOUNT_CURRENCY, "accountisocode");
		mapTrans.put(TransactionFields.KEY_SETTLEMENT_CURRENCY, "settlementisocode");
		mapTrans.put(TransactionFields.KEY_CORP_ACTIONS_INDICATOR, "reorg");

		//Asset level
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ACCOUNT_NUMBER, "portfolio");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SUB_ACCOUNT_NUMBER, "subaccountnumber");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SECURITY_ID, "symbol1");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_NAME, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DESCRIPTION, "securitydescription");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CATEGORY, "assettype");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SUBCATEGORY, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CURRENCY, "iso1");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_YIELD, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_QUANTITY, "quantity");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_AVERAGE_UNIT_COST, ""); //origface. tag not required by taurus. this value is being set in "taxcost" tag with prefix "@"
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_INDICATIVE_PRICE, ""); //price. tag not required by taurus. this value is being set in "amount" tag with prefix "@"
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COST, "taxcost");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CURRENT_VALUE, "amount");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_INDICATIVE_PRICE_DATE, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_PROFIT, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_PROFIT_PERC, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CUSTODIAN, "custodianname");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_MATURITY_DATE, "maturityexpirationdate");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ISSUER, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ACCRUED_INTEREST, "localaccruedinterest");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_LAST_FX_RATE, "tdfx");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_FX_ACCRUED_INTEREST, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_START_DATE, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_FX_MARKET_VALUE, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNREALIZED_PROFIT, "opentradeequity");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNERALIZED_PROFIT_CURRENCY, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ISIN, ""); // equal to symbol1
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COMMENCING_DATE, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COUPON, "couponrate");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_STRIKE_PRICE, "strikeprice");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_INTEREST_TILL_MATURITY, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_OPTION, "");

		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_TRANS_CODE, "trancode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COST_DATE, "taxdate");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CLOSING_METHOD_CODE, "cmeth");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_VERSUS_DATE, "versus");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SRC_DST_TYPE, "type2");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SRC_DST_SYMBOL, "symbol2");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_TRADE_DATE_FX_RATE, "tdfx");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_VALUATION_DATE_FX_RATE, "sdfx");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ORIGINAL_FX_RATE, "taxfx");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_MARK_TO_MARKET, "mtm");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_MTM, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_WITHHOLDING_TAX, "fwtax");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_EXCHANGE, "exchange");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_EXCHANGE_FEE, "secfee");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COMMISSION, "commis");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_OTHER_FEES, "other");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_IMPLIED_COMMISSION, "implied");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COMMISSION_PURPOSE, "compurp");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_PLEDGE, "pledge");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DESTINATION_PLEDGE, "dstpledge");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DESTINATION_CUSTODIAN, "");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DURATION, "origdur");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_RECORD_DATE, "record");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_STRATEGY, "strategy");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_RECLAIM_AMOUNT, "reclaim");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ACCRUAL_ACCOUNT, "divacc");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DIVIDEND_ACCRUAL_METHOD, "divaccmth");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_MGMT_FEE_PERIOD_DATE, "feedate");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_RECALLABLE, "isrecallable");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_BROKER_FIRM_SYMBOL, "broker");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_COMMITTED_CAPITAL, "committedcapital");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CONTRIBUTED_CAPITAL, "contributedcapital");

		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_TRADE_DATE, "trade");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SETTLEMENT_DATE, "settle");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SECURITY_TYPE, "");  //type1. Asked to remove this tag
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DEST_CURRENCY, "iso2");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_TICKER, "ticker");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CUSIP, "cusip");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SEDOL, "sedol");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_QUIK, "quik");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_RIC, "ric");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_FX_DENOMINATOR_CURRENCY, "fxdenominatorcurrencycode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_FX_NUMERATOR_CURRENCY, "fxnumeratorcurrencycode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_BLOOMBERG_TICKER, "bloombergticker");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_BLOOMBERG_TICKER, "underbloombergticker");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_CUSIP, "undercusip");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_ISIN, "underisin");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_RIC, "underric");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_SEDOL, "undersedol");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_UNDER_TICKER, "underticker");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_PRICE_FACTOR, "pricefactor");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_BASE_PRICE, "baseprice");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_ACCOUNT_CURRENCY, "accountisocode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SETTLEMENT_CURRENCY, "settlementisocode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_LAST_TRADE_DATE, "lasttradedate");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_DIV_REINVEST_INDICATOR, "dividendreinvestmentindicator");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_OPTION_TYPE, "optiontype");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_SEC_COUNTRY_CODE, "securitycountrycode");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_CORP_ACTIONS_INDICATOR, "reorg");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_VAT, "vat");
		mapAsset.put(AssetFields.KEY_HOLDING_ASSET_INVESTMENT_OBJECTIVE, "investmentobjective");



		//SCX file mapping for deposit and loan from Asset
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_SECURITY_TYPE, "type");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_CURRENCY, "iso");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_SECURITY_ID, "symbol");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_DESCRIPTION, "name");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_ISSUER, "issuer");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_START_DATE, "dated");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_MATURITY_DATE, "maturity");
		mapSCX.put(FieldsSCX.KEY_HOLDING_ASSET_COUPON, "intdiv");


		//SCX file mapping for deposit and loan from Transaction
		mapSCX.put(FieldsSCX.KEY_SECURITY_TYPE, "type");
		mapSCX.put(FieldsSCX.KEY_TRANSACTION_CURRENCY, "iso");
		mapSCX.put(FieldsSCX.KEY_SECURITY_ID, "symbol");
		mapSCX.put(FieldsSCX.KEY_DESCRIPTION, "name");
		mapSCX.put(FieldsSCX.KEY_ASSET_ISSUER, "issuer");
		mapSCX.put(FieldsSCX.KEY_START_DATE, "dated");
		mapSCX.put(FieldsSCX.KEY_MATURITY_DATE, "maturity");
		mapSCX.put(FieldsSCX.KEY_COUPON, "intdiv");



	}


}
