package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pisight.pimoney.beans.ParserUtility;
import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.InvestmentTransaction;
import com.pisight.pimoney.models.Response;

public class MyClass {

	private static Logger logger = Logger.getLogger(MyClass.class);

	private static HashMap<String, String> transCategoryMap = new HashMap<String, String>();

	private static InvestmentTransaction lastTrans = null;

	static int total1 = 0;
	static int total2 = 0;
	static int total3 = 0;
	static int total4 = 0;
	static int total5 = 0;

	static{
		transCategoryMap.put("shares", "Equities");
	}


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments", "SC_inv_sg", "pdf"),"");
		}catch(CryptographyException e){
			if(e.getMessage().contains("The supplied password does not match")){
				//System.out.println("The supplied password does not match");
			}
			throw e;
		}


		//		String page = boxTest.convertPDFToHTML(" ", "((\\d*,)*\\d+(\\.)\\d+)", "Desription", null, "(DR)", "Balance Carried Forward", "Description");

		String page = boxTest.convertPDFToHTML(" ");
		//System.out.println(page);

		js.executeScript(page);
		try{
			scrapeStatement(driver);
		}
		catch(Exception e){
			throw e;
		}
		finally{
			//System.out.println("closing driver");


			driver.quit();

			System.out.println("Total Time Taken -> " + (System.currentTimeMillis()-start) + " ms");
		}


	}

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		//System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		//System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toFile();
	}

	private static String getFileName(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		//System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		//System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toString();
	}

	private static WebDriver getDriver() {
		// TODO Auto-generated method stub
		Path p1 = Paths.get(System.getProperty("user.home"), "drivers", "phantomjs");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);  
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, p1.toString());

		WebDriver driver = new PhantomJSDriver(caps);

		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		return driver;
	}

	private static HashMap<String, String> currencyMap = new HashMap<String, String>();

	static
	{
		currencyMap.put("AUSTRALIAN DOLLAR", "AUD");
		currencyMap.put("HONG KONG DOLLAR", "HKD");
		currencyMap.put("US DOLLAR", "USD");
		currencyMap.put("SINGAPORE DOLLAR", "SGD");
	}

	private static final List<String> keywords = new ArrayList<String>();
	static{
		keywords.add("Equity");
		keywords.add("Mutual Fund");
		keywords.add("EQUITY MUTUAL FUNDS OPEN ENDED");
		keywords.add("DEBT MUTUAL FUNDS OPEN ENDED");
		keywords.add("Mutual Fund");
		keywords.add("Mutual Fund (Demat)");
		keywords.add("Exchanged Traded Fund (ETF)");
		keywords.add("Portfolio Management Services (PMS)");
		keywords.add("Structured Products");
		keywords.add("Blended Products");
		keywords.add("Alternatives");
		keywords.add("Bond - Non-tradeable");
		keywords.add("Bond - Tradeable");
		keywords.add("Fixed Income");
		keywords.add("Term Deposits");
	}

	static HoldingAsset currentAsset = null;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		logger.info("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		logger.info("");

		WebElement accountEle = driver.findElement(By.xpath("//td[contains(text(), 'Date ')]"));

		String accountText = accountEle.getText().trim();

		String accountRegex = ".*Date (\\d{1,2} [A-z]{3} \\d{4})( .*)?";
		Pattern accountP = Pattern.compile(accountRegex, Pattern.CASE_INSENSITIVE);
		Matcher accountM = accountP.matcher(accountText);

		String stmtDate = null;

		if(accountM.matches()){
			stmtDate = accountM.group(1);
			stmtDate = ParserUtility.convertDateStringToPimoneyFormat(stmtDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY);
		}

		//System.out.println("Stmt Date -> " + stmtDate);

		WebElement accNumEle = driver.findElement(By.xpath("//td[contains(text(), 'Portfolio ')]"));

		String accNum = accNumEle.getText().trim();

		String accNumRegex = ".*Portfolio ([A-Z]{2}\\*{4}\\d{4}-\\d)";
		Pattern accNumP = Pattern.compile(accNumRegex);
		Matcher accNumM= accNumP.matcher(accNum);

		if(accNumM.matches()){
			accNum = accNumM.group(1);
			accNum = formatAmount(accNum);
		}

		//System.out.println("Account Number -> " + accNum);

		WebElement balEle = driver.findElement(By.xpath("//td[contains(text(), 'Total Assets ')]"));

		String balance = balEle.getText().trim();

		String balRegex = "Total Assets ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})( .*)?";
		Pattern balP = Pattern.compile(balRegex);
		Matcher balM= balP.matcher(balance);

		if(balM.matches()){
			balance = balM.group(1);
			balance = formatAmount(balance);
		}

		//System.out.println("Balance -> " + balance);

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'Current value (')]"));

		String currency = currencyEle.getText().trim();

		String currencyRegex = ".*Current value \\((\\w{3})\\)( .*)?";
		Pattern currencyP = Pattern.compile(currencyRegex);
		Matcher currencyM= currencyP.matcher(currency);

		if(currencyM.matches()){
			currency = currencyM.group(1);
			currency = formatAmount(currency);
		}

		//System.out.println("Currency -> " + currency);

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setCurrency(currency);
		account.setBalance(balance);
		account.setBillDate(stmtDate);
		account.setAccountNumber(accNum);
		account.setAccountName("Portfolio " + currency);


		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[text() = 'Cash Accounts'] "
				+ "and following-sibling::tr/td[text() = 'Important Information']]"));

		// Type1 for "Cash Accounts"
		// Type2 for "Bonds"
		// Type3 for "Mutual Funds"   
		// Type4 for "Other Investments"
		// Type5 for "Securities account activity"
		// Type6 for "Loans"
		boolean isType1 = true;
		boolean isType2 = false;
		boolean isType3 = false;
		boolean isType4 = false;
		boolean isType5 = false;
		boolean isType6 = false;
		rowCount = 5;

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			//System.out.println("Row Text -> " + rowText);

			if(rowText.equalsIgnoreCase("Important Information")){
				break;
			}
			if(rowText.equals("Bonds")){
				rowCount = -1;
				isType1 = false;
				isType2 = true;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
			}
			else if(rowText.equals("Mutual Funds")){
				isType1 = false;
				isType2 = false;
				isType3 = true;
				isType4 = false;
				isType5 = false;
				isType6 = false;
			}
			else if(rowText.equals("Other Investments")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = true;
				isType5 = false;
				isType6 = false;
			}
			else if(rowText.equals("Securities account activity")){
				rowCount = -1;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = true;
				isType6 = false;
			}
			else if(rowText.equals("Loans")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = true;
			}

			if(isType1){
				getAssetType1(rowText, account);
			}
			else if(isType2){
				getAssetType2(rowText, account);
			}
			else if(isType3){
				getAssetType3(rowText, account);
			}
			else if(isType4){
				getAssetType4(rowText, account);
			}
			else if(isType5){
				getAssetType5(rowText, account);
			}
			else if(isType6){
				getAssetType6(rowText, account);
			}
		}


		ObjectMapper mapper = new ObjectMapper();
		Path p = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p.toString()), account);
			//			String x = mapper.writeValueAsString(accounts);
			//			JSONObject json = mapper.readValue(new File(p.toString()), JSONObject.class);
			//			String xml = XML.toString(json);
			//			//System.out.println(xml);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String lastBalance = null;
	private static void getAssetType1(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		//System.out.print("Inside 1 ");
		String accRegex = "(.*) - (\\d{10}) - ([A-z]{3})( \\(\\d+\\.?\\d*\\))?";
		String transRegex = "(\\d{1,2} [A-z]{3} \\d{2}) (.*) (\\d{1,2} [A-z]{3} \\d{2}) (−?(?:\\d*,)*\\d+\\.?\\d{2}) (<?(?:\\d*,)*\\d+\\.?\\d{2}>?)";
		String closeRegex = "Closing Balance ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";

		Pattern accP = Pattern.compile(accRegex);
		Pattern transP = Pattern.compile(transRegex);
		Pattern closeP = Pattern.compile(closeRegex);

		Matcher accM = accP.matcher(rowText);
		Matcher transM = transP.matcher(rowText);
		Matcher closeM = closeP.matcher(rowText);

		if(accM.matches()){
			//System.out.println("Matched 1 -> Acc");

			String description = accM.group(1);
			String subAccountNum = accM.group(2);
			String currency = accM.group(3);
			String fxRate = accM.group(4);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetSubAccountNumber(subAccountNum);
			asset.setHoldingAssetCategory("Cash");
			asset.setHoldingAssetSubCategory("Accounts");
			account.addAsset(asset);
			currentAsset = asset;
			lastTrans = null;
		}
		else if(transM.matches() && currentAsset != null){
			//System.out.println("Matched 1 -> Trans");
			
			String transDate = transM.group(1);
			String description = transM.group(2);
			String valueDate = transM.group(3);
			String amount =  transM.group(4);
			String runBal = transM.group(5);
			String type = null;
			
			runBal = formatAmount(runBal);
			lastBalance = formatAmount(lastBalance);
			
			float runBalF = Float.parseFloat(runBal);
			float lastBalanceF = Float.parseFloat(lastBalance);
			
			if(runBalF > lastBalanceF){
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			lastBalance = runBal;
			
			InvestmentTransaction transaction = new InvestmentTransaction();
			
			transaction.setTransactionDate(ParserUtility.convertDateStringToPimoneyFormat(transDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			transaction.setValuationDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			transaction.setDescription(description);
			transaction.setCurrency(currentAsset.getHoldingAssetCurrency());
			transaction.setAmount(formatAmount(amount));
			transaction.setType(type);
			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setSubAccountNumber(currentAsset.getHoldingAssetSubAccountNumber());
			transaction.setAssetCategory("Cash");
			account.addTransaction(transaction);
			lastTrans = transaction;
			rowCount = 1;
		}
		else if(closeM.matches() && currentAsset != null){
			String nominal = closeM.group(1);
			String fxValue = closeM.group(2);
			
			currentAsset.setHoldingAssetQuantity(formatAmount(nominal));
			currentAsset.setHoldingAssetCurrentValue(formatAmount(nominal));
			currentAsset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			currentAsset = null;
			lastTrans = null;
		}
		else if(rowText.contains("Opening Balance") && currentAsset != null){
			lastBalance = rowText.substring(rowText.lastIndexOf(" "));
		}
		else if(rowCount < 4 &&  lastTrans != null){
			rowCount++;
			if(!rowText.contains("Important Information") && !rowText.contains("continued") && !rowText.contains("Description")
					&& !rowText.contains("Portfolio") && !rowText.contains("Closing Balance")){
				String description = lastTrans.getDescription() + " " + rowText;
				lastTrans.setDescription(description.trim());
			}
			else{
				lastTrans = null;
			}
		}


	}

	static String assetDescription = null;
	private static void getAssetType2(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		//System.out.print("Inside 2 ");
		String regex1 = "([A-z]{3}) ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.\\d{1,4}) % (\\d{1,2} [A-z]{3} \\d{2,4}) - "
				+ "(\\d{1,2} [A-z]{3} \\d{2,4}) ((?:\\d*,)*\\d+\\.?\\d{2,3}) (\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2,3}) "
				+ "((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(\\(\\d+\\.\\d+\\) )?(\\w{12}) (.*) (\\d{1,2} [A-z]{3} \\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}) "
				+ "((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String coupon = m1.group(3);
			String bondStartDate = m1.group(4);
			String maturityDate = m1.group(5);
			String avgPurchasePrice = m1.group(6);
			String yield = m1.group(7);
			String mtm = m1.group(8);
			String value = m1.group(9);
			String fxValue = m1.group(10);
			
			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(assetDescription.trim());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetCoupon(coupon);
			asset.setHoldingAssetStartDate(ParserUtility.convertDateStringToPimoneyFormat(bondStartDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY));
			asset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturityDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY));
			asset.setHoldingAssetAverageUnitCost(formatAmount(avgPurchasePrice));
			asset.setHoldingAssetBondYield(formatAmount(yield));
			asset.setHoldingAssetIndicativePrice(formatAmount(mtm));
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory("Bonds");
			asset.setHoldingAssetSubCategory("Bonds");
			currentAsset = asset;
			account.addAsset(asset);
			assetDescription = null;
		}
		else if(m2.matches() && currentAsset != null){
			String fxRate = m2.group(1);
			String isin = m2.group(2);
			String valueDate = m2.group(4);
			String accruedInterest = m2.group(5);
			String fxAccruedInterest = m2.group(6);
			
			if(StringUtils.isNotEmpty(fxRate)){
				fxRate = fxRate.replace("(", "");
				fxRate = fxRate.replace(")", "");
			}
			
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetISIN(isin);
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			currentAsset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
			currentAsset = null;
			assetDescription = null;
		}
		else if(rowText.contains("ISIN Description")){
			rowCount = 0;
			assetDescription = null;
		}
		else if(rowCount >= 0){
			if(!StringUtils.isNumeric(rowText)){
				if(StringUtils.isEmpty(assetDescription)){
					assetDescription = rowText;
				}
				else{
					assetDescription = assetDescription + " " + rowText;
				}
				
			}
		}
	}

	private static void getAssetType3(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		//System.out.print("Inside 3 ");
		String regex1 = "([A-z]{3}) (−?(?:\\d*,)*\\d+\\.?\\d{2,4}) (.*) ((?:\\d*,)*\\d+\\.?\\d{2,3}) ((?:\\d*,)*\\d+\\.?\\d{2,3}) "
				+ "((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(\\(\\d+\\.\\d+\\) )?(\\w{12}) (.*) (\\d{1,2} [A-z]{3} \\d{2})";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String avgPurchasePrice = m1.group(4);
			String mtm = m1.group(5);
			String value = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetAverageUnitCost(formatAmount(avgPurchasePrice));
			asset.setHoldingAssetIndicativePrice(formatAmount(mtm));
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory("Mutual Funds");
			asset.setHoldingAssetSubCategory("Mutual Funds");
			currentAsset = asset;
			account.addAsset(asset);
		}
		else if(m2.matches() && currentAsset != null){
			String fxRate = m2.group(1);
			String isin = m2.group(2);
			String description = m2.group(3);
			String valueDate = m2.group(4);
			
			if(StringUtils.isNotEmpty(fxRate)){
				fxRate = fxRate.replace("(", "");
				fxRate = fxRate.replace(")", "");
			}
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetISIN(isin);
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			currentAsset = null;
		}
	}

	private static void getAssetType4(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		//System.out.print("Inside 4 ");
		String regex1 = "([A-z]{3}) (−?(?:\\d*,)*\\d+\\.?\\d{2,4}) (.*) ((?:\\d*,)*\\d+\\.?\\d{2,3}) ((?:\\d*,)*\\d+\\.?\\d{2,3}) "
				+ "((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(\\(\\d+\\.\\d+\\) )?(\\w{12} )?((?:\\d*,)*\\d+\\.?\\d*) % (\\d{1,2} [A-z]{3} \\d{2,4}) - "
				+ "(\\d{1,2} [A-z]{3} \\d{2,4}) (\\d{1,2} [A-z]{3} \\d{2})";
		String regex3 = "Interest .* ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);
		Matcher m3 = p3.matcher(rowText);

		if(m1.matches()){
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String avgPurchasePrice = m1.group(4);
			String mtm = m1.group(5);
			String value = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetAverageUnitCost(formatAmount(avgPurchasePrice));
			asset.setHoldingAssetIndicativePrice(formatAmount(mtm));
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory("Bonds");
			asset.setHoldingAssetSubCategory("Bonds");
			currentAsset = asset;
			account.addAsset(asset);
		}
		else if(m2.matches()){
			String fxRate = m2.group(1);
			String isin = m2.group(2);
			String coupon = m2.group(3);
			String bondStartDate = m2.group(4);
			String maturityDate = m2.group(5);
			String valueDate = m2.group(6);
			
			if(StringUtils.isNotEmpty(fxRate)){
				fxRate = fxRate.replace("(", "");
				fxRate = fxRate.replace(")", "");
			}
			
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetISIN(isin);
			currentAsset.setHoldingAssetCoupon(coupon);
			currentAsset.setHoldingAssetStartDate(ParserUtility.convertDateStringToPimoneyFormat(bondStartDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY));
			currentAsset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturityDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY));
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
		}
		else if(m3.matches() && currentAsset != null){
			String accruedInterest = m3.group(1);
			String fxAccruedInterest = m3.group(2);
			
			currentAsset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
			currentAsset = null;
		}
	}

	private static void getAssetType5(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		//System.out.print("Inside 5 ");
		String regex = "(.*) (\\d{1,2} [A-z]{3} \\d{2}) (\\d{1,2} [A-z]{3} \\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}) (.*) "
				+ "([A-z]{3}) ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			String type = m.group(1);
			String transDate = m.group(2);
			String valueDate = m.group(3);
			String nominal = m.group(4);
			String description = m.group(5);
			String currency = m.group(6);
			String unitPrice = m.group(7);
			String amount = m.group(8);
			
			InvestmentTransaction transaction = new InvestmentTransaction();
			lastTrans = null;
			if(type.contains("REDEMPTION") || type.contains("SALE") || type.contains("PAID")){
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}
			else if(type.contains("PURCHASE")){
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			else{
				lastTrans = transaction;
			}
			
			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setTransactionDate(ParserUtility.convertDateStringToPimoneyFormat(transDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			transaction.setValuationDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
			transaction.setAssetQuantity(formatAmount(nominal));
			transaction.setDescription(description);
			transaction.setCurrency(currency);
			transaction.setAssetUnitCost(formatAmount(unitPrice));
			transaction.setAmount(formatAmount(amount));
			transaction.setType(type);
			account.addTransaction(transaction);
			
		}
		else if(lastTrans != null){
			String type = lastTrans.getType() + " " + rowText;
			
			if(!type.equals(InvestmentTransaction.TRANSACTION_TYPE_CREDIT) && !type.equals(InvestmentTransaction.TRANSACTION_TYPE_DEBIT)){
				if(type.contains("REDEMPTION") || type.contains("SALE") || type.contains("PAID")){
					type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
					lastTrans.setType(type);
				}
				else if(type.contains("PURCHASE")){
					type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
					lastTrans.setType(type);
				}
				else{
					throw new Exception("New Transaction Type keyword found");
				}
			}
			lastTrans = null;
		}
	}

	private static void getAssetType6(String rowText, InvestmentAccount account) throws ParseException {
		System.out.print("Inside 6 ");
		String regex1 = "([A-z]{3}) LOAN( .*)?";
		String regex2 = "(\\(\\d+\\.\\d+\\) )?(\\d+\\.\\d+) % .* (\\d{1,2}\\/\\d{2}\\/\\d{2}) (\\w{12}) ((?:\\d*,)*\\d+\\.?\\d{2}) "
				+ "((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		String regex3 = "Interest .* ((?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2})";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);
		Matcher m3 = p3.matcher(rowText);
		
		if(m1.matches()){
			String currency = m1.group(1);
			String description = m1.group(2);
			
			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCategory("Loans");
			asset.setHoldingAssetSubCategory("Loans");
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(m2.matches() && currentAsset != null){
			String fxRate = m2.group(1);
			String coupon = m2.group(2);
			String maturityDate = m2.group(3);
			String subAccountNumber = m2.group(4);
			String value = m2.group(5);
			String fxValue = m2.group(6);
			
			if(StringUtils.isNotEmpty(fxRate)){
				fxRate = fxRate.replace("(", "");
				fxRate = fxRate.replace(")", "");
			}
			
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetCoupon(formatAmount(coupon));
			currentAsset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturityDate, Constants.DATEFORMAT_DD_SLASH_MM_SLASH_YY));
			currentAsset.setHoldingAssetSubAccountNumber(subAccountNumber);
			currentAsset.setHoldingAssetCurrentValue(formatAmount(value));
			currentAsset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
		}
		else if(m3.matches() && currentAsset != null){
			String accruedInterest = m3.group(1);
			String fxAccruedInterest = m3.group(2);
			
			currentAsset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
			currentAsset = null;
		}
	}

	static int rowCount = 4;

	private static String formatAmount(String amount){
		if(StringUtils.isEmpty(amount)){
			return null;
		}

		amount = amount.replace(",", "");

		if(amount.contains("<")){
			amount = amount.replace("<", "");
			amount  = amount.replace(">", "");
			amount = "-"+amount;
		}

		return amount;
	}

	private static String formatDescription(String description){
		if(StringUtils.isEmpty(description)){
			return null;
		}

		String word = description.substring(0, description.indexOf(" "));
		if(description.lastIndexOf(word) > 0){
			description = description.substring(0, description.lastIndexOf(word)-1).trim();
		}
		return description;
	}
}
