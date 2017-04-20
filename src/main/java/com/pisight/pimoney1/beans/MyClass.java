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
import com.pisight.pimoney.models.Response;

public class MyClass {

//	private static Logger logger = Logger.getLogger(MyClass.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments", "PL DB Portfolio June 2015 - TX", "pdf"),"");
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

	private static int rowCount = 0;
	
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
		String rm = null;
		if(mAcc.matches()){
			accountNumber = mAcc.group(1);
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
		
		
		System.out.println("Account Number -> " + accountNumber);
		System.out.println("Manager -> " + rm);
		System.out.println("Stmt Date -> " + stmtDate);
		
		List<BankAccount> accounts  = new ArrayList<BankAccount>();
		
		List<WebElement> rows = driver.findElements(By.tagName("tr"));
		
		String acctRegex = "([A-Z]{3}) (\\d{7,8}-\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (\\d{1,2}\\.\\d{2}\\.\\d{2})( -?(?:\\d*,)*\\d+\\.?\\d{2})?( -?(?:\\d*,)*\\d+\\.?\\d{2})? (-?(?:\\d*,)*\\d+\\.?\\d{2}) (\\d{1,2}\\.\\d{2}\\.\\d{2}) \\d{1,2}";
		String transRegex1 = "(\\d{1,2}\\.\\d{2}\\.\\d{2}) (\\d{1,2}\\.\\d{2}\\.\\d{2}) (.*) [A-Z] \\d{6,12} (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String transRegex2 = "(\\d{1,2}\\.\\d{2}\\.\\d{2}) (\\d{1,2}\\.\\d{2}\\.\\d{2}) (.*) \\d{6,12} (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String headerRegex = "[A-Z]{3} .* (\\d{7,8}-\\d{3}) Period from (\\d{1,2}\\.\\d{2}\\.\\d{2}) to (\\d{1,2}\\.\\d{2}\\.\\d{2})";
		
		Pattern pAcct = Pattern.compile(acctRegex);
		Pattern pTrans1 = Pattern.compile(transRegex1);
		Pattern pTrans2 = Pattern.compile(transRegex2);
		Pattern pHeader = Pattern.compile(headerRegex);
		Matcher mAcct = null;
		Matcher mTrans1 = null;
		Matcher mTrans2 = null;
		Matcher mHeader = null;
		
		boolean isAccounts = false;
		boolean isTransactions = false;
		
		String lastBalance = null;
		BankTransaction currentTrans = null;
		BankAccount currentAccount = null;
		
		HashMap<String, BankAccount> accountMap = new HashMap<String, BankAccount>();
		
		for(WebElement row: rows){
			String rowText = row.getText().trim();
			
			System.out.println("RowText ->> " + rowText);
			
			if(rowText.contains("Cash Account Summary Report")){
				isAccounts = true;
				isTransactions = false;
			}
			else if(rowText.contains("Account Statement")){
				isTransactions = true;
				isAccounts = false;
			}
			
			if(isAccounts){
				mAcct = pAcct.matcher(rowText);
				
				if(mAcct.matches()){
					String currency = mAcct.group(1);
					String accNum = mAcct.group(2);
					String balance = mAcct.group(7);
					
					BankAccount account = new BankAccount(properties);
					
					account.setAccountNumber(accNum);
					account.setCurrency(currency);
					account.setAccountBalance(ParserUtility.formatAmount(balance));
					account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
					if(accountMap.get(accNum) == null){
						accounts.add(account);
						response.addBankAccount(account);
						accountMap.put(accNum, account);
					}
					
				}
			}
			else if(isTransactions){
				
				if(rowText.contains("Closing Balance") || rowText.contains("posted to your account") || rowText.contains("Portfolio No/Ref")){
					System.out.println("Not a transaction row so skipping");
					rowCount = 0;
					currentTrans = null;
					continue;
				}
				if(rowText.contains("Opening Balance")){
					lastBalance = rowText.substring(rowText.lastIndexOf(" "));
				}
				else if(rowText.contains("Period from")){
					mHeader = pHeader.matcher(rowText);
					
					if(mHeader.matches()){
						String accountNum = mHeader.group(1);
						currentAccount = accountMap.get(accountNum);
					}
				}
				mTrans1 = pTrans1.matcher(rowText);
				mTrans2 = pTrans2.matcher(rowText);
				
				Matcher m = null;
				if(mTrans1.matches()){
					m = mTrans1;
				}
				else if(mTrans2.matches()){
					m = mTrans2;
				}
				
				if(m != null && currentAccount != null){
					rowCount = 1;
					String valueDate = m.group(1);
					String transDate = m.group(2);
					String description = m.group(3);
					String amount = m.group(4);
					String runningBalance = m.group(5);
					String type = null;
					
					runningBalance = ParserUtility.formatAmount(runningBalance);
					lastBalance = ParserUtility.formatAmount(lastBalance);
					
					double runningBalanceD = Double.parseDouble(runningBalance);
					double lastBalanceD = Double.parseDouble(lastBalance);
					
					if(runningBalanceD > lastBalanceD){
						type = BankTransaction.TRANSACTION_TYPE_CREDIT;
					}
					else{
						type = BankTransaction.TRANSACTION_TYPE_DEBIT;
					}
					
					lastBalance = runningBalance;
					
					BankTransaction transaction = new BankTransaction();
					
					transaction.setAccountNumber(currentAccount.getAccountNumber());
					transaction.setPostDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
					transaction.setTransDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
					transaction.setDescription(description);
					transaction.setAmount(ParserUtility.formatAmount(amount));
					transaction.setTransactionType(type);
					currentTrans = transaction;
					currentAccount.addTransaction(transaction);
					
				}
				else if(currentTrans != null && rowCount == 1){
					String description = currentTrans.getDescription() + " " + rowText;
					currentTrans.setDescription(description);
					rowCount = 0;
					currentTrans = null;
				}
				else{
					rowCount = 0;
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
