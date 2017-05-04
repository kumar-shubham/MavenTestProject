package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.InvestmentTransaction;
import com.pisight.pimoney.models.Response;

public class MyClass {

	//	private static Logger logger = Logger.getLogger(MyClass.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments/new1", "Emirtaes NBD", "pdf"),"");
		}catch(Exception e){
			if(e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")){
				System.out.println("Cannot decrypt PDF, the password is incorrect");
			}
			throw e;
		}

		String page = boxTest.convertPDFToHTML(" ");

		js.executeScript(page);
		try{
			scrapeStatement(driver);
		}
		catch(Exception e){
			throw e;
		}
		finally{

			driver.quit();

			System.out.println("Total Time Taken -> " + (System.currentTimeMillis()-start) + " ms");
		}


	}

	private static File getFile(String dir, String name, String type) {

		String fileName = dir + "/" + name + "." + type.toLowerCase();

		Path p = Paths.get(System.getProperty("user.home"), "kumar/statements/statements", fileName);

		return p.toFile();
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

	private static String assetRegex1 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})( .*)?";
	private static String assetRegex2 = "Accrued Income (-?(?:\\d*,)*\\d+\\.?\\d{2})";
	private static Pattern assetP1 = Pattern.compile(assetRegex1);
	private static Pattern assetP2 = Pattern.compile(assetRegex2);

	private static String transRegex1 = "(.*) (\\d{1,2}-\\d{2}-\\d{4}) (\\d{1,2}-\\d{2}-\\d{4})(?: (-?(?:\\d*,)*\\d+\\.?\\d{2}))?(?: (-?(?:\\d*,)*\\d+\\.?\\d{4}) ?/ ?[A-Z]{3})? (-?(?:\\d*,)*\\d+\\.?\\d{2}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
	private static Pattern transP1 = Pattern.compile(transRegex1);

	private static HashMap<String, InvestmentAccount> accountMap = new HashMap<String, InvestmentAccount>();

	private static InvestmentAccount currentAccount = null;
	private static HoldingAsset currentAsset = null;
	private static int rowCount = 0;
	private static String stmtDate = null;
	private static String category = null;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		List<Container> accounts = new ArrayList<Container>();

		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Statement Period')]]/following-sibling::tr[1]"));

		stmtDate = stmtDateEle.getText().trim();
		String regex = "[A-z]{3,9} \\d{1,2}, \\d{4} to ([A-z]{3,9} \\d{1,2}, \\d{4})";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(stmtDate);

		if(m.matches()){
			stmtDate = m.group(1);
		}


		getCashAccounts(driver, accounts, properties);

		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[contains(text() , 'Allocation Details – Investment Account')] and following-sibling::tr/td[text() = 'DISCLAIMER']]"));

		String acctRegex1 = "Portfolio Number : (\\w{15})"; // for Account number
		String acctRegex2 = "Reporting Currency : ([A-Z]{3})"; // for Account currency
		String acctRegex3 = "Portfolio Total (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})"; // for Account balance
		Pattern acctP1 = Pattern.compile(acctRegex1);
		Pattern acctP2 = Pattern.compile(acctRegex2);
		Pattern acctP3 = Pattern.compile(acctRegex3);
		Matcher acctM1 = null;
		Matcher acctM2 = null;
		Matcher acctM3 = null;

		boolean isAsset = false;
		boolean isTransaction = false;
		for(WebElement row: rows){

			String rowText = row.getText().trim();

			System.out.println("RowText - > " + rowText);

			if(rowText.contains("Total") || rowText.contains("Page")){
				currentAsset = null;
				rowCount = 0;
				continue;
			}

			if(rowText.contains("PORTFOLIO POSITION DETAILS")){
				isAsset = true;
				isTransaction = false;
				continue;
			}
			else if(rowText.contains("TRANSACTION SUMMARY")){
				isAsset = false;
				isTransaction = true;
				continue;
			}
			else if(rowText.contains("Fixed Income")){
				category = HoldingAsset.CATEGORY_BOND;
			}

			acctM1 = acctP1.matcher(rowText);
			acctM2 = acctP2.matcher(rowText);
			acctM3 = acctP3.matcher(rowText);

			if(acctM1.matches()){
				String accountNumber = acctM1.group(1);
				if(accountMap.get(accountNumber) == null){
					InvestmentAccount account = new InvestmentAccount(properties);
					account.setAccountNumber(accountNumber);
					account.setBillDate(stmtDate, Constants.DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY);
					accounts.add(account);
					accountMap.put(accountNumber, account);
					currentAccount = account;
				}
				else{
					currentAccount = accountMap.get(accountNumber);
				}
				continue;
			}
			else if(acctM2.matches() && currentAccount != null){
				String currency = acctM2.group(1);
				currentAccount.setCurrency(currency);
				continue;
			}
			else if(acctM3.matches() && currentAccount != null){
				String balance = acctM3.group(1);
				currentAccount.setBalance(balance, true);
				currentAccount.setAvailableBalance(balance, true);
				continue;
			}

			if(isAsset){
				getAssets(rowText);
			}
			else if(isTransaction){
				getTransactions(rowText);
			}

		}

		for(Container account: accounts){
			if(account instanceof InvestmentAccount){
				InvestmentAccount inv = (InvestmentAccount) account;
				response.addInvestmentAccount(inv);
				List<HoldingAsset> assets = inv.getAssets();
				for(HoldingAsset asset: assets){
					getDetailsFromAssetDescription(asset);
				}
				List<InvestmentTransaction> transactions = inv.getInvestmentTransactions();
				for(InvestmentTransaction transaction: transactions){
					getDetailsFromTransDescription(transaction);
				}
				if(StringUtils.isEmpty(inv.getBalance())){
					inv.setBalance("0.00");
					inv.setAvailableBalance("0.00");
				}
			}
			else if(account instanceof BankAccount){
				BankAccount bank = (BankAccount) account;
				response.addBankAccount(bank);
			}
		}


		ObjectMapper mapper = new ObjectMapper();
		Path path = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(path.toString()), accounts);
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

	private static void getDetailsFromTransDescription(InvestmentTransaction transaction) throws ParseException {
		// TODO Auto-generated method stub
		String regex1 = "(?:.* )?(-?\\d+\\.?\\d*)%( .*)?";
		String regex2 = "(?:.* )?(\\d{1,2}/\\d{2}/\\d{4})( .*)?";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);

		Matcher m1 = p1.matcher(transaction.getDescription());
		Matcher m2 = p2.matcher(transaction.getDescription());

		if(m1.matches()){
			String coupon = m1.group(1);
			transaction.setCoupon(coupon, true);
		}
		if(m2.matches()){
			String maturity = m2.group(1);
			transaction.setMaturityDate(maturity, Constants.DATEFORMAT_DD_SLASH_MM_SLASH_YYYY);
		}
		
	}

	private static void getDetailsFromAssetDescription(HoldingAsset asset) throws ParseException {
		// TODO Auto-generated method stub
		String regex1 = "(?:.* )?(-?\\d+\\.?\\d*)%( .*)?";
		String regex2 = "(?:.* )?(\\d{1,2}/\\d{2}/\\d{4})( .*)?";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);

		Matcher m1 = p1.matcher(asset.getHoldingAssetDescription());
		Matcher m2 = p2.matcher(asset.getHoldingAssetDescription());
		
		if(m1.matches()){
			String coupon = m1.group(1);
			asset.setHoldingAssetCoupon(coupon, true);
		}
		if(m2.matches()){
			String maturity = m2.group(1);
			asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_SLASH_MM_SLASH_YYYY);
		}
	}

	private static void getAssets(String rowText) {
		// Pattern assetP1, assetP2
		Matcher m1 = assetP1.matcher(rowText);
		Matcher m2 = assetP2.matcher(rowText);

		if(m1.matches() && currentAccount != null){
			rowCount = 1;
			String description = m1.group(1);
			String quantity = m1.group(2);
			String currency = m1.group(3);
			String unitCost = m1.group(4);
			String totalCost = m1.group(5);
			String unitPrice = m1.group(6);
			String value = m1.group(7);
			String fxValue = m1.group(8);
			String profit = m1.group(9);
			String ytm = m1.group(11);

			HoldingAsset asset = new HoldingAsset();

			asset.setHoldingAssetAccountNumber(currentAccount.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(unitCost, true);
			asset.setHoldingAssetCost(totalCost, true);
			asset.setHoldingAssetIndicativePrice(unitPrice, true);
			asset.setHoldingAssetCurrentValue(value, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetUnrealizedProfitLoss(profit, true);
			asset.setHoldingAssetUnrealizedProfitLossCurrency(currentAccount.getCurrency());
			asset.setHoldingAssetYield(ytm, true);
			asset.setHoldingAssetCategory(category);
			if(category.equals(HoldingAsset.CATEGORY_BOND)){
				asset.setBondNature(true);
			}
			currentAccount.addAsset(asset);
			currentAsset = asset;

		}
		else if(m2.matches() && currentAsset != null){
			String fxAccruedInterest = m2.group(1);
			currentAsset.setHoldingAssetFxAccruredInterest(fxAccruedInterest, true);
			currentAsset = null;
			rowCount = 0;
		}
		else if(currentAsset != null && rowCount >0 && rowCount <3){
			rowCount++;
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());

		}

	}

	private static void getTransactions(String rowText) throws Exception {
		// Pattern transP1

		Matcher m = transP1.matcher(rowText);

		if(m.matches()){
			String description = m.group(1);
			String tradeDate = m.group(2);
			String valueDate = m.group(3);
			String quantity = m.group(4);
			String price = m.group(5);
			String currency = m.group(7);
			String amount = m.group(8);
			String type = null;

			String desc = description.toLowerCase();
			if(desc.contains("buy") || desc.contains("purchase")){
				type = InvestmentTransaction.TRANSACTION_TYPE_BUY;
			}
			else if(desc.contains("sell") || desc.contains("sale")){
				type = InvestmentTransaction.TRANSACTION_TYPE_SELL;
			}
			else if(desc.contains("accrued interest")){
				type = InvestmentTransaction.TRANSACTION_TYPE_INFLOW;
			}
			else{
				throw new Exception("Transaction Category could not be determined. Please check.");
			}

			InvestmentTransaction transaction = new InvestmentTransaction();

			transaction.setAccountNumber(currentAccount.getAccountNumber());
			transaction.setDescription(description);
			transaction.setTransactionDate(tradeDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY);
			transaction.setValuationDate(valueDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY);
			transaction.setAssetQuantity(quantity, true);
			transaction.setAssetUnitCost(price, true);
			transaction.setCurrency(currency);
			transaction.setAmount(amount, true);
			transaction.setType(type);

			currentAccount.addTransaction(transaction);
		}

	}

	private static void getCashAccounts(WebDriver driver, List<Container> accounts, HashMap<String, String> properties) throws ParseException {
		// TODO Auto-generated method stub

		String regex = "(\\d{13}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) - (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;

		String xpath = "//tr[preceding-sibling::tr/td[contains(text() , 'ACCOUNT DETAILS - CASH')] and following-sibling::tr/td[contains(text() , 'Account Details - Liabilities')]]";
		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			m = p.matcher(rowText);

			if(m.matches()){

				String accountNumber = m.group(1);
				String currency = m.group(2);
				String balance = m.group(3);

				BankAccount account = new BankAccount(properties);

				account.setAccountNumber(accountNumber);
				account.setCurrency(currency);
				account.setAccountBalance(ParserUtility.formatAmount(balance));
				account.setAccountName("Cash Account");
				account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY));
				accounts.add(account);
			}
		}

	}

}
