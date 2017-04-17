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
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.Response;

public class MyClass {

	private static Logger logger = Logger.getLogger(MyClass.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments/new3", "BNP HK trans", "pdf"),"");
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

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

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

		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));
		
		WebElement refrenceCurrencyEle = page.findElement(By.xpath("//td[contains(text(), 'REFERENCE CURRENCY ')]"));
		String refrenceCurrencyArray[] = refrenceCurrencyEle.getText().trim().split(" ");
		String refrenceCurrency = refrenceCurrencyArray[2];
		
		WebElement stmtDateEle = page.findElement(By.xpath("//td[contains(text(), 'From ')]"));
		String stmtDate[] = stmtDateEle.getText().trim().split("till");
		String statementDate = stmtDate[1].trim();
		System.out.println("Statement Date = " + statementDate);
		

		String transactionRegex = "(\\d{2}.\\d{2}.\\d{4}) (.*) (\\d{2}.\\d{2}.\\d{4}) (-?(\\d*,)*\\d+(.)\\d+)( -?(\\d*,)*\\d+(.)\\d+)?";
		String accountRegex = "((?:\\*|\\d){7}-\\d{3}-\\d{3}-\\d{3}) (.*)";

		Pattern accountRegEx = Pattern.compile(accountRegex);
		Pattern transactionRegEx = Pattern.compile(transactionRegex);
		Matcher accountMatcher = null;
		Matcher txnMatched = null;
		boolean accountFlag = false;

		List<BankAccount> accounts = new ArrayList<BankAccount>();

		List<WebElement> trEles = driver.findElements(By.tagName("tr"));
		BankAccount currentAcct = null;
		BankTransaction lastTransaction = null;
		boolean isTransactionTable = false;

		for(WebElement rowEle: trEles){

			String rowText = rowEle.getText().trim();
			System.out.println("rowtext ::: " + rowText); 
			accountMatcher = accountRegEx.matcher(rowText);
			txnMatched = transactionRegEx.matcher(rowText);

			if(!accountFlag && accountMatcher.matches()){
				String accountNumber = accountMatcher.group(1).replace("-", "");
				String accountName = accountMatcher.group(2);
				String accountCurrencyDetails[] = accountName.split("-");
				String accountCurrency = accountCurrencyDetails[1];
				accountCurrency = currencyMap.get(accountCurrency.trim());
				if(StringUtils.isEmpty(accountCurrency)){
					accountCurrency = refrenceCurrency;
				}

				BankAccount ba = new BankAccount(properties);
				ba.setAccountName(accountName);
				ba.setAccountNumber(accountNumber);
				ba.setCurrency(accountCurrency);
				ba.setBillDate(ParserUtility.convertToPimoneyDate(statementDate));
				currentAcct = ba;

				accountFlag = true;
				isTransactionTable = true;

			}else{

				if(rowText.contains("CLOSING BALANCE")){                    
					String splits[] = rowText.split(" ");
					String accountBalance = splits[3].replaceAll(",", "");
					currentAcct.setAccountBalance(accountBalance);
					accounts.add(currentAcct);
					response.addBankAccount(currentAcct);
					accountFlag = false;
					isTransactionTable = false;
					currentAcct = null;

				}else if(txnMatched.matches() && currentAcct != null){

					String txnDate = txnMatched.group(1);
					String desc = txnMatched.group(2);
					String balance = txnMatched.group(4).replaceAll(",", "");

					String transType = null;
					if(balance.contains("-")){
						transType = BankTransaction.TRANSACTION_TYPE_DEBIT;
					}else{
						transType = BankTransaction.TRANSACTION_TYPE_CREDIT;
					}

					BankTransaction bt = new BankTransaction();
					bt.setAmount(balance);
					bt.setDescription(desc);
					bt.setTransDate(ParserUtility.convertToPimoneyDate(txnDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
					bt.setTransactionType(transType);
					bt.setCurrency(currentAcct.getCurrency());
					bt.setAccountNumber(currentAcct.getAccountNumber());
					currentAcct.addTransaction(bt);
					lastTransaction = bt;

					isTransactionTable = true;

					System.out.println("Transaction = " +  "  " + txnDate + "  "+ desc +"  "+ balance +"  "+ transType);

				}else if(lastTransaction != null){

					if(rowText.contains("Page")){
						isTransactionTable = false;
						continue;
					}else if(rowText.contains("Value Date")){
						isTransactionTable = true;
						continue;
					}else if(rowText.contains("OPENING BALANCE")){
						continue;
					}

					if(isTransactionTable){
						lastTransaction.setDescription(lastTransaction.getDescription() + " " + rowText);
					}
				}
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		Path p = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p.toString()), accounts);
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

}
