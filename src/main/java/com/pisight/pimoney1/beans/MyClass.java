package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
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
			boxTest = new PDFExtracter(getFile("investments/new", "EFG_Dec 2016", "pdf"),"");
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

	private static HashMap<String, String> currencyMap = new HashMap<String, String>();
	static{
		currencyMap.put("U.S. Dollar", "USD");
		currencyMap.put("Australian Dollar", "AUD");
		currencyMap.put("", "SGD");
		currencyMap.put("Euro", "EUR");

	}
	
	// Assets
	private static String regex1 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})%";
	private static Pattern  p1 = Pattern.compile(regex1);
	
	
	// Cash Transactions
	private static String regex2 = "(\\d{1,2}-\\d{2}) (.*) (\\d{1,2}-\\d{2}-\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{2}(?:[A-Z]))";
	private static String regex3 = "(\\d{6}\\.\\d{3}\\.\\d) ([A-Z]{3})";
	private static Pattern p2 = Pattern.compile(regex2);
	private static Pattern p3 = Pattern.compile(regex3);
	

	private static HoldingAsset currentAsset = null;
	private static int rowCount = 0;
	private static String lastBalance = null;
	private static String category = null;
	private static String transCurrency = null;
	private static String transAccount = null;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");
		
		WebElement accNumEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Valuation of All your Portfolios')]]/following-sibling::tr[1]"));
		String accNum = accNumEle.getText().trim();
		String currency = null;
		String balance = null;
		
		String regex = "(?:.* )?(\\d{6}-\\d) .* ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accNum);
		
		if(m.matches()){
			accNum = m.group(1);
			currency = m.group(2);
			balance  = m.group(3);
		}
		
		
		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'VALUATION AS AT')]]"));
		String stmtDate = stmtDateEle.getText().trim();
		regex = "VALUATION AS AT (\\d{1,2} [A-z]{3,9} \\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);
		
		if(m.matches()){
			stmtDate = m.group(1);
		}
		
		System.out.println(accNum);
		System.out.println(currency);
		System.out.println(stmtDate);
		System.out.println(balance);

		InvestmentAccount account = new InvestmentAccount(properties);
		account.setAccountNumber(accNum);
		account.setCurrency(currency);
		account.setBillDate(stmtDate, Constants.DATEFORMAT_DD_SPACE_MMMM_SPACE_YYYY);
		account.setBalance(balance, true);
		account.setAvailableBalance(balance, true);
		account.setAccountName("Portfolio - " + currency);
		response.addInvestmentAccount(account);
		
		List<WebElement> rows = driver.findElements(By.xpath("//tr[td[contains(text() , 'CASH AND CASH EQUIVALENTS')]]/following-sibling::tr"));
		
		boolean isType1 = true;
		boolean isType2 = false;
		
		
		for(WebElement row: rows){
			String rowText = row.getText().trim();
			rowText = rowText.replace("−", "-");
			System.out.println("RowText -> " + rowText);
			
			if(rowText.contains("Cash Accounts")){
				category = HoldingAsset.CATEGORY_CASH;
			}
			
			if(rowText.toUpperCase().contains(" TOTAL") || rowText.contains("Not valid for tax purposes") || rowText.contains("Portfolio Valuation")){
				System.out.println("Not a valid row. Skipping.");
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			else if(rowText.contains("STATEMENT OF ACCOUNT")){
				isType1 = false;
				isType2 = true;
				continue;
			}
			
			if(isType1){
				getAssetType1(account, rowText);
			}
			else if(isType2){
				getAssetType2(account, rowText);
			}
			
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		Path path = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(path.toString()), account);
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


	private static void getAssetType1(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p1
		Matcher m1= p1.matcher(rowText);
		
		if(m1.matches()){
			rowCount =1;
			
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String fxValue = m1.group(4);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetCategory(category);
			account.addAsset(asset);
			currentAsset = asset;
			
		}
		else if(rowCount == 1 && currentAsset != null && rowText.matches("\\d{6}\\.\\d{3}\\.\\d")){
			String description = rowText;
			
			currentAsset.setHoldingAssetSubAccountNumber(description);
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			rowCount = 0;
			currentAsset = null;
		}
		
	}

	private static void getAssetType2(InvestmentAccount account, String rowText) throws Exception {
		// Pattern p2, p3
		
		Matcher m1 = p2.matcher(rowText);
		Matcher m2 = p3.matcher(rowText);
		
		
		if(m1.matches() && StringUtils.isNotEmpty(lastBalance) && StringUtils.isNotEmpty(transCurrency)){
			
			String date = m1.group(1);
			String description = m1.group(2);
			String valueDate = m1.group(3);
			String amount = m1.group(4);
			String runningBalance = m1.group(5);
			String type  = null;
			
			runningBalance = ParserUtility.formatAmount(runningBalance);
			if(runningBalance.contains("C")){
				runningBalance = runningBalance.replace("C", "").trim();
				
			}
			else if(runningBalance.contains("D")){
				runningBalance = "-"+ runningBalance.replace("D", "").trim();
			}
			
			lastBalance = ParserUtility.formatAmount(lastBalance);
			if(lastBalance.contains("C")){
				lastBalance = lastBalance.replace("C", "").trim();
				
			}
			else if(lastBalance.contains("D")){
				lastBalance = "-"+ lastBalance.replace("D", "").trim();
			}
			
			double runningBalanceD = Double.parseDouble(runningBalance);
			double lastBalanceD = Double.parseDouble(lastBalance);
			
			if(runningBalanceD > lastBalanceD){
				type = InvestmentTransaction.TRANSACTION_TYPE_INFLOW;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_OUTFLOW;
			}
			lastBalance = runningBalance;
			
			InvestmentTransaction transaction = new InvestmentTransaction();
			
			transaction.setAccountNumber(account.getAccountNumber());
			
			transaction.setTransactionDate(ParserUtility.getYear(date, Constants.DATEFORMAT_DD_DASH_MM, valueDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YY));
			transaction.setDescription(description);
			transaction.setValuationDate(valueDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YY);
			transaction.setAmount(amount, true);
			transaction.setType(type);
			transaction.setCurrency(transCurrency);
			transaction.setSubAccountNumber(transAccount);
			transaction.setAssetCategory(HoldingAsset.CATEGORY_CASH);
			account.addTransaction(transaction);
			
		}
		else if(m2.matches()){
			transAccount = m2.group(1);
			transCurrency = m2.group(2);
		}
		else if(rowText.contains("Initial balance")){
			lastBalance = rowText.substring(rowText.lastIndexOf(" ")).trim();
		}
	}

}
