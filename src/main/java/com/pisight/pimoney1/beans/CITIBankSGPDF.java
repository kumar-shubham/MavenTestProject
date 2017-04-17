package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
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
import com.pisight.pimoney.models.BankAccount;
import com.pisight.pimoney.models.BankTransaction;
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.InvestmentTransaction;
import com.pisight.pimoney.models.Response;

public class CITIBankSGPDF {

	private static Logger logger = Logger.getLogger(CITIBankSGPDF.class);

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
			//			boxTest = new PDFExtracter(getFile("investments/new", "Citibank_eStatement_11302016", "pdf"),"");
			boxTest = new PDFExtracter(getFile("CITI", "CITI1", "pdf"),"");
		}catch(Exception e){
			if(e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")){
				System.out.println("Cannot decrypt PDF, the password is incorrect");
			}
			throw e;
		}


		//		String page = boxTest.convertPDFToHTML(" ", "((\\d*,)*\\d+(\\.)\\d+)", "Desription", null, "(DR)", "Balance Carried Forward", "Description");

		String page = boxTest.convertPDFToHTML(" ");
		System.out.println(page);

		js.executeScript(page);
		try{
			scrapeStatement(driver);
		}
		catch(Exception e){
			throw e;
		}
		finally{
			System.out.println("closing driver");


			driver.quit();

			System.out.println("Total Time Taken -> " + (System.currentTimeMillis()-start) + " ms");
		}


	}

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toFile();
	}

	private static String getFileName(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

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

		HashMap<String, Container> accountMap = new HashMap<String, Container>();
		WebElement stmtDateEle = driver.findElement(By.xpath("//td[contains(text(), 'Equivalent Balance')]"));
		String stmtDate = stmtDateEle.getText().trim();

		String stmtDateRegex = ".* Equivalent Balance - ([A-Za-z]{3} \\d{2} \\d{4})";
		Pattern pStmt = Pattern.compile(stmtDateRegex);
		Matcher mStmt = pStmt.matcher(stmtDate);
		if(mStmt.matches()){
			stmtDate = mStmt.group(1);
			stmtDate = ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY);
		}

		List<Container> accounts = new ArrayList<Container>();

		String checkingXpath = "//tr[preceding-sibling::tr/td[text() = 'Checking'] and following-sibling::tr/td[contains(text(), 'Checking Total')]]";
		String savingXpath = "//tr[preceding-sibling::tr/td[text() = 'Savings & Investments'] and following-sibling::tr/td[contains(text(), 'Savings & Investments Total')]]";

		List<WebElement> accountRows = driver.findElements(By.xpath(checkingXpath));

		accountRows.addAll(driver.findElements(By.xpath(savingXpath)));

		String accountRegex = "(.*) (\\d+) ([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		Pattern pAcc = Pattern.compile(accountRegex);
		Matcher mAcc = null;

		for(WebElement row: accountRows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			mAcc = pAcc.matcher(rowText);

			if(rowText.contains("Savings & Investments Total")){
				break;
			}

			if(mAcc.matches()){
				String accountName = mAcc.group(1);
				String accountNumber = mAcc.group(2);
				String currency = mAcc.group(3);
				String balance = mAcc.group(4);

				if(!accountMap.containsKey(accountNumber)){

					if((rowText.toLowerCase().contains("investment") || rowText.toLowerCase().contains("brokerage")) &&
							!rowText.toLowerCase().contains("cash")){
						InvestmentAccount account = new InvestmentAccount(properties);
						account.setAccountName(accountName);
						account.setAccountNumber(accountNumber);
						account.setCurrency(currency);
						account.setBalance(ParserUtility.formatAmount(balance));
						account.setBillDate(stmtDate);
						accounts.add(account);
						accountMap.put(accountNumber, account);
					}
					else{
						BankAccount account = new BankAccount(properties);
						account.setAccountName(accountName);
						account.setAccountNumber(accountNumber);
						account.setCurrency(currency);
						account.setAccountBalance(ParserUtility.formatAmount(balance));
						account.setBillDate(stmtDate);
						accounts.add(account);
						accountMap.put(accountNumber, account);
					}


				}
			}
		}

		String transXpath = "//tr[preceding-sibling::tr/td[contains(text(), 'DETAILS OF YOUR')]]";

		List<WebElement> transRows = driver.findElements(By.xpath(transXpath));

		String transRegex = "([A-Za-z]{3} \\d{2} \\d{4}) ([A-Za-z]{3} \\d{2} \\d{4}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		String headerRegex = "(.*) (\\d{10}) ([A-Za-z]{3})( .*)?";

		Pattern pTrans = Pattern.compile(transRegex);
		Pattern pHeader = Pattern.compile(headerRegex);
		Matcher mTrans = null;
		Matcher mHeader = null;

		String lastBalance = null;
		BankAccount currenctAccount = null;
		for(WebElement row: transRows){
			String rowText = row.getText().trim();

			System.out.println("Row text -> " + rowText);

			mTrans = pTrans.matcher(rowText);
			mHeader = pHeader.matcher(rowText);

			if(mTrans.matches() && currenctAccount != null){
				String transDate = mTrans.group(1);
				String valueDate = mTrans.group(2);
				String description = mTrans.group(3);
				String amount = mTrans.group(4);
				String runningBalance = mTrans.group(5);
				String type = null;

				runningBalance = ParserUtility.formatAmount(runningBalance);
				lastBalance = ParserUtility.formatAmount(runningBalance);

				double runningBalanceD = Double.parseDouble(runningBalance);
				double lastBalanceD = Double.parseDouble(lastBalance);

				if(runningBalanceD - lastBalanceD > 0){
					type = BankTransaction.TRANSACTION_TYPE_CREDIT;
				}
				else{
					type = BankTransaction.TRANSACTION_TYPE_DEBIT;
				}

				BankTransaction transaction = new BankTransaction();
				transaction.setAccountNumber(currenctAccount.getAccountNumber());
				transaction.setTransDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY));
				transaction.setPostDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY));
				transaction.setAmount(ParserUtility.formatAmount(amount));
				transaction.setTransactionType(type);
				transaction.setCurrency(currenctAccount.getCurrency());
				transaction.setDescription(description);
				currenctAccount.addTransaction(transaction);

			}
			else if(mHeader.matches()){
				String accountNumber = mHeader.group(2);
				currenctAccount = (BankAccount) accountMap.get(accountNumber);
			}

		}

		String fundXpath = "//tr[preceding-sibling::tr/td[contains(text(), 'InvestmentFunds')]]/preceding-sibling::tr[1]";

		List<WebElement> fundRows = driver.findElements(By.xpath(fundXpath));

		String fundRegex = "(.*) ([A-Za-z]{3}) (−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*)";
		headerRegex = "InvestmentFunds (\\d{10})( .*)?";

		Pattern pFunds = Pattern.compile(fundRegex);
		pHeader = Pattern.compile(headerRegex);
		Matcher mFunds = null;
		mHeader = null;

		HoldingAsset currentAsset = null;
		InvestmentAccount currenctInvAccount = null;
		int count = 3;
		// taking 2 more lines for description if the length is less than 25 char
		for(WebElement row: fundRows){

			String rowText = row.getText().trim();

			System.out.println("Row Text -> " + rowText);

			mFunds = pFunds.matcher(rowText);
			mHeader = pHeader.matcher(rowText);

			if(mHeader.matches()){
				System.out.println("Investment Fund Header Matched");
				currenctInvAccount = (InvestmentAccount) accountMap.get(mHeader.group(1));
			}
			else if(mFunds.matches() && currenctInvAccount!= null){
				count = 1;
				String description = mFunds.group(1);
				String currency = mFunds.group(2);
				String quantity = mFunds.group(3);
				String totalCost = mFunds.group(4);
				String totalValue = mFunds.group(6);
				String unitPrice = mFunds.group(7);

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(currenctInvAccount.getAccountNumber());
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetCost(ParserUtility.formatAmount(totalCost));
				asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(totalValue));
				asset.setHoldingAssetIndicativePrice(unitPrice);
				asset.setHoldingAssetCategory("Unit Trust");
				asset.setHoldingAssetSubCategory("Equity");
				currentAsset = asset;
				currenctInvAccount.addAsset(asset);
			}
			else if(count == 1 || count == 2){
				if(rowText.length() <= 25 && currentAsset!= null){
					String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
					currentAsset.setHoldingAssetDescription(description.trim());
					if(count == 2){
						currentAsset = null;
					}
					count++;
				}
				else{
					currentAsset = null;
				}

			}
			else{
				count++;
			}
		}

		getBrokerageAccountDetails(driver, accountMap);

		ObjectMapper mapper = new ObjectMapper();
		Path p = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p.toString()), accounts);
			//			String x = mapper.writeValueAsString(accounts);
			//			JSONObject json = mapper.readValue(new File(p.toString()), JSONObject.class);
			//			String xml = XML.toString(json);
			//			System.out.println(xml);
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

	private static void getBrokerageAccountDetails(WebDriver driver, HashMap<String, Container> accountMap) throws Exception {
		// TODO Auto-generated method stub
		String fundXpath = "//tr[preceding-sibling::tr/td[text() = 'Brokerage']]/preceding-sibling::tr[3]/following-sibling::tr";

		List<WebElement> fundRows = driver.findElements(By.xpath(fundXpath));

		String fundRegex = "(.*) \\w+ ([A-Z]{3}) (−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*)";
		String transRegex = "([A-z]{3} \\d{1,2} \\d{4}) (.*) ([A-Z]{3}) (.*) \\d+ (−?(?:\\d*,)*\\d+\\.?\\d*) (−?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(−?(?:\\d*,)*\\d+\\.?\\d*)";
		String headerRegex = "Brokerage .*(\\d{10})";

		Pattern pFunds = Pattern.compile(fundRegex);
		Pattern pTrans = Pattern.compile(transRegex);
		Pattern pHeader = Pattern.compile(headerRegex);
		Matcher mFunds = null;
		Matcher mTrans = null;
		Matcher mHeader = null;

		HoldingAsset currentAsset = null;
		InvestmentAccount currenctInvAccount = null;
		int count = 4;
		// taking 3 more lines for description if the length is less than 25 char
		for(WebElement row: fundRows){

			String rowText = row.getText().trim();

			System.out.println("Row Text -> " + rowText);
			
			
			if(rowText.contains("FEEDBACK FROM YOU") || rowText.contains("CONSOLIDATED STATEMENT OF INTEREST EARNED")){
				break;
			}

			mFunds = pFunds.matcher(rowText);
			mTrans = pTrans.matcher(rowText);
			mHeader = pHeader.matcher(rowText);

			if(mHeader.matches()){
				System.out.println("Brokerage Header Matched");
				currenctInvAccount = (InvestmentAccount) accountMap.get(mHeader.group(1));
			}
			else if(mFunds.matches() && currenctInvAccount!= null){
				count = 1;
				String description = mFunds.group(1);
				String currency = mFunds.group(2);
				String totalCost = mFunds.group(4);
				String quantity = mFunds.group(5);
				String unitPrice = mFunds.group(6);
				String totalValue = mFunds.group(7);
				

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(currenctInvAccount.getAccountNumber());
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetCost(ParserUtility.formatAmount(totalCost));
				asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(totalValue));
				asset.setHoldingAssetIndicativePrice(unitPrice);
				asset.setHoldingAssetCategory("Unit Trust");
				asset.setHoldingAssetSubCategory("Equity");
				currentAsset = asset;
				currenctInvAccount.addAsset(asset);
			}
			else if(mTrans.matches() && currenctInvAccount != null){
				count = 4;
				currentAsset = null;
				
				String transDate = mTrans.group(1);
				String description = mTrans.group(2);
				String currency = mTrans.group(3);
				String type = mTrans.group(4);
				String quantity = mTrans.group(5);
				String unitPrice = mTrans.group(6);
				String amount = mTrans.group(7);
				
				InvestmentTransaction transaction = new InvestmentTransaction();
				
				transaction.setAccountNumber(currenctInvAccount.getAccountNumber());
				transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY));
				transaction.setDescription(description);
				transaction.setCurrency(currency);
				transaction.setAssetQuantity(ParserUtility.formatAmount(quantity));
				transaction.setAssetUnitCost(ParserUtility.formatAmount(unitPrice));
				transaction.setAmount(ParserUtility.formatAmount(amount));
				
				if(type.toLowerCase().contains("dividend")){
					transaction.setType(InvestmentTransaction.TRANSACTION_TYPE_CREDIT);
				}
				else{
					throw new Exception("New Transaction Type Keyword Found. Need to handle.");
				}
				currenctInvAccount.addTransaction(transaction);
				
			}
			else if(count > 0 && count < 4){
				if(rowText.length() <= 25 && currentAsset!= null){
					String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
					currentAsset.setHoldingAssetDescription(description.trim());
					if(count == 3){
						currentAsset = null;
					}
					count++;
				}
				else{
					currentAsset = null;
				}
			}
			else{
				count++;
			}
		}
	}

}
