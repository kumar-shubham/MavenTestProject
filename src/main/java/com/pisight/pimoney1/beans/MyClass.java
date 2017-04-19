package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.Response;

public class MyClass {

//	private static Logger logger = Logger.getLogger(MyClass.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments", "PL DB Portfolio June 2015", "pdf"),"");
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


	private static HashMap<String, String> transCategoryMap = new HashMap<String, String>();

	static{
		transCategoryMap.put("CURRENT ACCOUNT", "Cash");
		transCategoryMap.put("LOAN", "Loan");
		transCategoryMap.put("FUND", "Unit Trust");	
	}

	private static HashMap<String, String> currencyMap = new HashMap<String, String>();

	static
	{
		currencyMap.put("AUSTRALIAN DOLLAR", "AUD");
		currencyMap.put("HONG KONG DOLLAR", "HKD");
		currencyMap.put("US DOLLAR", "USD");
		currencyMap.put("SINGAPORE DOLLAR", "SGD");
		currencyMap.put("EURO", "EUR");
		currencyMap.put("STERLING POUND", "GBP");
		currencyMap.put("SWISS FRANC", "CHF");
		currencyMap.put("YEN", "JPY");
		currencyMap.put("MALAYSIAN RINGGIT", "MYR");
		
		
	}

	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accountEle = driver.findElement(By.xpath("//td[contains(text(), 'Portfolio No/Ref. CCY:')]"));
		
		String accountText = accountEle.getText().trim();
		
		String accRegex = ".*Portfolio No/Ref. CCY: (\\d{6,10}) ?/  ?([A-Z]{3}).*Relationship Manager: (.*)";
		Pattern pAcc = Pattern.compile(accRegex);
		Matcher mAcc = pAcc.matcher(accountText);
		
		String accountNumber = null;
		String currency = null;
		String rm = null;
		if(mAcc.matches()){
			accountNumber = mAcc.group(1);
			currency = mAcc.group(2);
			rm = mAcc.group(3);
		}
		else{
			throw new Exception("PDF format is changed. Need to handle");
		}
		
		WebElement stmtDateEle = driver.findElement(By.xpath("//td[contains(text(), 'As of')]"));
		String stmtDate = stmtDateEle.getText().trim();
		
		String stmtRegex = ".*As of (\\d{1,2}\\.\\d{2}\\.\\d{2}).*";
		Pattern pStmt = Pattern.compile(stmtRegex);
		Matcher mStmt = pStmt.matcher(stmtDate);
		
		
		if(mStmt.matches()){
			stmtDate = mStmt.group(1);
		}
		else{
			throw new Exception("PDF format is changed. Need to handle");
		}
		
		String balanceIdentifier = "Market Value at " + stmtDate;
		String balanceXpath = "//td[contains(text(), '" + balanceIdentifier + "')]";
		WebElement balanceEle = driver.findElement(By.xpath(balanceXpath));
		String balance = balanceEle.getText().trim();
		
		String balRegex = ".*" + balanceIdentifier + " (-?(?:\\d*,)*\\d+\\.?\\d*) [A-Z]{3}(?: .*)?";
		Pattern pBal = Pattern.compile(balRegex);
		Matcher mBal = pBal.matcher(balance);
		
		if(mBal.matches()){
			balance = mBal.group(1);
		}
		else{
			throw new Exception("PDF format is changed. Need to handle");
		}
		
		System.out.println("Account Number -> " + accountNumber);
		System.out.println("Currency -> " + currency);
		System.out.println("Manager -> " + rm);
		System.out.println("Stmt Date -> " + stmtDate);
		System.out.println("Balance -> " + balance);
		
		
		
		InvestmentAccount account = new InvestmentAccount(properties);
		account.setAccountNumber(accountNumber);
		account.setCurrency(currency);
		account.setRelationshipManager(rm);
		account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
		account.setBalance(ParserUtility.formatAmount(balance));
		account.setAvailableBalance(ParserUtility.formatAmount(balance));
		
		getAssetType1(driver, account);
		getAssetType2(driver, account);
		getAssetType3(driver, account);
		getAssetType4(driver, account);
		getAssetType5(driver, account);
		
		

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

	private static void getAssetType1(WebDriver driver, InvestmentAccount account) {
		// TODO Auto-generated method stub
		
		String regex = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;
		
		String xpath = "//tr[preceding-sibling::tr/td[text() = 'List of Holdings - Cash Positions'] and following-sibling::tr/td[contains(text() , 'Total Cash Positions')]]";
		List<WebElement> rows = driver.findElements(By.xpath(xpath));
		
		for(WebElement row: rows){
			
			String rowText = row.getText().trim();
			System.out.println("Row Text -1->> " + rowText);
			
			m = p.matcher(rowText);
			
			if(m.matches()){
				
			}
		}
		
	}
	
	private static void getAssetType2(WebDriver driver, InvestmentAccount account) {
		// TODO Auto-generated method stub
		
	}
	
	private static void getAssetType3(WebDriver driver, InvestmentAccount account) {
		// TODO Auto-generated method stub
		
	}
	
	private static void getAssetType4(WebDriver driver, InvestmentAccount account) {
		// TODO Auto-generated method stub
		
	}
	
	private static void getAssetType5(WebDriver driver, InvestmentAccount account) {
		// TODO Auto-generated method stub
		
	}

}
