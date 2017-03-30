package com.pisight.pimoney.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.models.BankAccount;
import com.pisight.pimoney.models.BankTransaction;
import com.pisight.pimoney.models.CardAccount;
import com.pisight.pimoney.models.CardTransaction;
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.InvestmentTransaction;
import com.pisight.pimoney.models.LoanAccount;
import com.pisight.pimoney.models.LoanTransaction;
import com.pisight.pimoney.models.Response;
import com.pisight.pimoney.models.TransactionBase;

public class ParserUtility {

	public static final String DATEFORMAT_MM_SPACE_DD_SPACE_YYYY = "MM dd yyyy";
	public static final String DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY = "dd MMM yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_COMMA_YYYY = "MMM dd,yyyy";
	public static final String DATEFORMAT_DD_DASH_MM_DASH_YYYY = "dd-MM-yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_YYYY = "MMMM dd,yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY = "MMMM dd, yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY = "MMM dd yyyy";
	public static final String DATEFORMAT_DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

	public static final String DATEFORMAT_MM_SPACE_DD = "MM dd";
	public static final String DATEFORMAT_DD_SPACE_MMM = "dd MMM";
	public static final String DATEFORMAT_MMMM_SPACE_DD = "MMMM dd";
	public static final String DATEFORMAT_DD_SLASH_MM  = "dd/MM";

	private static Logger logger = Logger.getLogger(ParserUtility.class);

	public static String getYear(String rawDate, String dateFormat, String reference, String referenceFormat) throws Exception{

		reference = formatDate(reference);
		rawDate = formatDate(rawDate);

		Date date = getDate(referenceFormat, reference);
		Date date1 = getDate(dateFormat, rawDate);
		logger.info("Raw date       :: " + rawDate);
		logger.info("Raw format     :: " + dateFormat);
		logger.info("Ref date       :: " + reference);
		logger.info("Ref format     :: " + referenceFormat);
		if(date == null || date1 == null){
			throw new Exception("Invalid date Format");
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		c.setTime(date1);
		int month1 = c.get(Calendar.MONTH);
		int day1 = c.get(Calendar.DAY_OF_MONTH);

		if((month1 > month) || (month1 == month && day1 > day)){
			c.set(Calendar.YEAR, year-1);
		}
		else{
			c.set(Calendar.YEAR, year);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(referenceFormat);
		String result = sdf.format(c.getTime());

		logger.info("Raw date       :: " + rawDate);
		logger.info("Raw format     :: " + dateFormat);
		logger.info("Ref date       :: " + reference);
		logger.info("Ref format     :: " + referenceFormat);
		logger.info("New Date       :: " + result);

		return result;

	}

	//this method removes extra spaces from the date strings
	private static String formatDate(String reference) {
		// TODO Auto-generated method stub

		String temp[] = reference.split(" ");
		String result = temp[0];
		for(int i = 1; i<temp.length; i++){
			if(!temp[i].trim().equals("")){
				result += " " + temp[i];
			}
		}

		return result;
	}

	private static Date getDate(String format, String value){

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (date == null) {
			// Invalid date format
		} else {
			// Valid date format
		}

		return date;
	}


	/**
	 * This method converts the given date String to the Pimoney Format.
	 * This method requires format of the date string passed.
	 * 
	 * @param oldDate
	 * @param format
	 * @return formated date string
	 * @throws ParseException
	 */
	public static String convertDateStringToPimoneyFormat(String oldDate, String format) throws ParseException{

		logger.info("inside convertDateStringToPimoneyFormat with date string :: " + oldDate + " and format :: " + format);
		if(StringUtils.isEmpty(oldDate)){
			return null;
		}
		oldDate = oldDate.trim();
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = sdf.parse(oldDate);

		sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(date);
	}


	/**
	 * Watson is always here for you even if you don't have the date format with you.
	 * This method also converts the given date String to the Pimoney Format.
	 * It does not requires date format for the date String. It matches with the predefined set
	 * of format defined in the Constants.java class
	 * 
	 * @param dateString
	 * @return formated date string
	 * @throws ACAException
	 */
	public static String convertDateStringToPimoneyFormat(String dateString) throws Exception{

		logger.info("inside convertDateStringToPimoneyFormat with only date string :: " + dateString);
		Date newDate = null;
		int failureCount = 0;
		SimpleDateFormat sdf = null;
		if (dateString != null) {
			dateString = dateString.trim();
			for (String parse : Constants.dateFormatList) {
				sdf = new SimpleDateFormat(parse);
				System.out.println("Trying with dateformat -> " + parse);
				try {
					newDate = sdf.parse(dateString);
					break;
				} catch (ParseException e) {
					failureCount++;
				}
			}
		}

		if(failureCount == Constants.dateFormatList.size()){
			logger.error("Date Format for the date string " + dateString + " is not in the supported dateList");
			throw new Exception("Date Format for the date string " + dateString + " is not in the supported dateList");
		}

		if(newDate != null){
			sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYY_DASH_MM_DASH_DD);
			return sdf.format(newDate);
		}
		else{
			logger.error("error in parsing date " + dateString);
			throw new Exception("error in parsing date");
		}

	}


	public static String formatDate1(String dateString, String format) throws ParseException{

		logger.info("converting data string to yyyyMMdd");
		logger.info("inside formatDate1 with date string :: " + dateString + " and format :: " + format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = sdf.parse(dateString);

		sdf = new SimpleDateFormat("yyyyMMdd");

		return sdf.format(date);
	}

	public static String formatAmount(String amount){
		if(amount == null){
			return null;
		}

		amount = amount.trim();
		return amount.replace(",", "");
	}


	public static String generateHash(Object model, HashMap<String, String> properties){

		String hash = null;

		if(model instanceof TransactionBase){
			hash = generateTransactionHash(model, properties);
		}
		else if(model instanceof Container){
			hash = generateAccountHash(model, properties);
		}
		else if(model instanceof HoldingAsset){
			hash = generateHoldingAssetHash(model, properties);
		}

		return hash;
	}

	private static String generateAccountHash(Object model, HashMap<String, String> properties) {
		// TODO Auto-generated method stub

		String institutionCode = properties.get(Constants.INSTITUTION_CODE);
		String accountNumber = "";

		if(model instanceof BankAccount){
			BankAccount account = (BankAccount) model;
			accountNumber = account.getAccountNumber();
		}
		else if(model instanceof CardAccount){
			CardAccount account = (CardAccount) model;
			accountNumber = account.getAccountNumber();
		}
		else if(model instanceof LoanAccount){
			LoanAccount account = (LoanAccount) model;
			accountNumber = account.getAccountNumber();
		}
		else if(model instanceof InvestmentAccount){
			InvestmentAccount account = (InvestmentAccount) model;
			accountNumber = account.getAccountNumber();
		}

		String valueString = "";

		if(StringUtils.isNotEmpty(institutionCode)){
			valueString += institutionCode;
		}
		if(StringUtils.isNotEmpty(accountNumber)){
			valueString += accountNumber;
		}

		return EncodeDecodeUtil.encodeString(valueString);
	}

	private static String generateTransactionHash(Object model, HashMap<String, String> properties) {
		// TODO Auto-generated method stub

		String institutionCode = properties.get(Constants.INSTITUTION_CODE);
		String accountNumber = "";
		String amount = "";
		String description = "";
		String date = "";
		String type = "";
		String quantity = "";

		if(model instanceof BankTransaction){
			BankTransaction transaction = (BankTransaction) model;
			accountNumber = transaction.getAccountNumber();
			amount = transaction.getAmount();
			description = transaction.getDescription();
			date = transaction.getTransDate();
			type = transaction.getTransactionType();
		}
		else if(model instanceof CardTransaction){
			CardTransaction transaction = (CardTransaction) model;
			accountNumber = transaction.getAccountNumber();
			amount = transaction.getAmount();
			description = transaction.getDescription();
			date = transaction.getTransDate();
			type = transaction.getTransactionType();

		}
		else if(model instanceof LoanTransaction){
			LoanTransaction transaction = (LoanTransaction) model;
			accountNumber = transaction.getAccountNumber();
			amount = transaction.getAmount();
			description = transaction.getDescription();
			date = transaction.getTransDate();
			type = transaction.getTransactionType();

		}
		else if(model instanceof InvestmentTransaction){
			InvestmentTransaction transaction = (InvestmentTransaction) model;
			accountNumber = transaction.getAccountNumber();
			amount = transaction.getAmount();
			description = transaction.getDescription();
			date = transaction.getTransactionDate();
			type = transaction.getType();
			quantity = transaction.getAssetQuantity();

		}

		String valueString = "";

		if(StringUtils.isNotEmpty(institutionCode)){
			valueString += institutionCode;
		}
		if(StringUtils.isNotEmpty(accountNumber)){
			valueString += accountNumber;
		}
		if(StringUtils.isNotEmpty(amount)){
			valueString += amount;
		}
		if(StringUtils.isNotEmpty(description)){
			valueString += description;
		}
		if(StringUtils.isNotEmpty(date)){
			valueString += date;
		}
		if(StringUtils.isNotEmpty(type)){
			valueString += type;
		}
		if(StringUtils.isNotEmpty(quantity)){
			valueString += quantity;
		}

		return EncodeDecodeUtil.encodeString(valueString);

	}


	private static String generateHoldingAssetHash(Object model, HashMap<String, String> properties) {
		// TODO Auto-generated method stub
		String institutionCode = properties.get(Constants.INSTITUTION_CODE);
		String assetName = "";
		String assetCategory = "";
		String assetInstrument = "";
		String assetQuantity = "";

		if(model instanceof HoldingAsset){
			HoldingAsset asset = (HoldingAsset) model;
			assetName = asset.getHoldingAssetDescription();
			assetCategory = asset.getHoldingAssetCategory();
			assetInstrument = asset.getHoldingAssetSecurityId();
			assetQuantity = asset.getHoldingAssetQuantity();
		}

		String valueString = "";

		if(StringUtils.isNotEmpty(institutionCode)){
			valueString += institutionCode;
		}
		if(StringUtils.isNotEmpty(assetName)){
			valueString += assetName;
		}
		if(StringUtils.isNotEmpty(assetCategory)){
			valueString += assetCategory;
		}
		if(StringUtils.isNotEmpty(assetInstrument)){
			valueString += assetInstrument;
		}
		if(StringUtils.isNotEmpty(assetQuantity)){
			valueString += assetQuantity;
		}

		return EncodeDecodeUtil.encodeString(valueString);
	}

	

	public static String convert(String json) throws JSONException
	{
		org.json.JSONObject jsonFileObject = new org.json.JSONObject(json);
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n"
				+ org.json.XML.toString(jsonFileObject, "accounts");
		return xml;
	}

	public static void addHash(Response response) {
		// TODO Auto-generated method stub

		List<BankAccount> bankAccounts = response.getBankAccounts();
		List<CardAccount> cardAccount = response.getCardAccounts();
		List<InvestmentAccount> investmentAccounts = response.getInvestmentAccounts();

		for(BankAccount account: bankAccounts){

			List<BankTransaction> transactions = account.getTransactions();

			for(BankTransaction transaction: transactions){
				transaction.setHash();
			}
			account.setHash();
		}

		for(CardAccount account: cardAccount){

			List<CardTransaction> transactions = account.getTransactions();

			for(CardTransaction transaction: transactions){
				transaction.setHash();
			}
			account.setHash();
		}

		for(InvestmentAccount account: investmentAccounts){

			List<InvestmentTransaction> transactions = account.getInvestmentTransactions();
			List<HoldingAsset> assets = account.getAssets();

			for(InvestmentTransaction transaction: transactions){
				transaction.setHash();
			}

			for(HoldingAsset asset: assets){
				asset.setHash();
			}
			account.setHash();

		}
	}
}

