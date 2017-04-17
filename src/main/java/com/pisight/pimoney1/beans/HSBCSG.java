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
import com.pisight.pimoney.models.HoldingAsset;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.InvestmentTransaction;
import com.pisight.pimoney.models.Response;

public class HSBCSG {

	private static Logger logger = Logger.getLogger(HSBCSG.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments", "HSBC_inv_sg", "pdf"),"");
		}catch(Exception e){
			if(e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")){
				System.out.println("Cannot decrypt PDF, the password is incorrect");
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


	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		logger.info("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		logger.info("");

		WebElement accountEle = driver.findElement(By.xpath("//td[contains(text(), 'STATEMENT: AS OF')]"));

		String accountText = accountEle.getText().trim();

		String accountRegex = "STATEMENT: AS OF (\\d{1,2} [A-z]{3} \\d{4})";
		Pattern accountP = Pattern.compile(accountRegex, Pattern.CASE_INSENSITIVE);
		Matcher accountM = accountP.matcher(accountText);

		String stmtDate = null;

		if(accountM.matches()){
			stmtDate = accountM.group(1);
			stmtDate = ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY);
		}

		System.out.println("Stmt Date -> " + stmtDate);

		WebElement accNumEle = driver.findElement(By.xpath("//td[contains(text(), 'ACCOUNT NUMBER:')]"));

		String accNum = accNumEle.getText().trim();

		String accNumRegex = "ACCOUNT NUMBER: (\\d{4}-\\d{6}-\\d{4})";
		Pattern accNumP = Pattern.compile(accNumRegex);
		Matcher accNumM= accNumP.matcher(accNum);

		if(accNumM.matches()){
			accNum = accNumM.group(1);
			accNum = formatAmount(accNum);
		}

		System.out.println("Account Number -> " + accNum);

		WebElement balEle = driver.findElement(By.xpath("//td[contains(text(), 'NET ASSET MARKET VALUE')]"));

		String balance = balEle.getText().trim();

		String balRegex = "NET ASSET MARKET VALUE (?:\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";
		Pattern balP = Pattern.compile(balRegex);
		Matcher balM= balP.matcher(balance);

		if(balM.matches()){
			balance = balM.group(1);
			balance = formatAmount(balance);
		}

		System.out.println("Balance -> " + balance);

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'MARKET VALUE (')]"));

		String currency = currencyEle.getText().trim();

		String currencyRegex = "MARKET VALUE \\(([A-z]{3})\\) MARKET VALUE \\(([A-z]{3})\\)";
		Pattern currencyP = Pattern.compile(currencyRegex);
		Matcher currencyM= currencyP.matcher(currency);

		if(currencyM.matches()){
			currency = currencyM.group(1);
			currency = formatAmount(currency);
		}

		System.out.println("Currency -> " + currency);

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setCurrency(currency);
		account.setBalance(balance);
		account.setBillDate(stmtDate);
		account.setAccountNumber(accNum);
		account.setAccountName("Portfolio " + currency);

		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[text() = 'PORTFOLIO VALUATION'] and "
				+ "following-sibling::tr/td[text() = 'Disclaimer:']]"));
		
		String regex1 = "([A-Z]{3}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(\\d{1,2}[A-z]{3}\\d{4}) (.*) (\\d{1,2}[A-z]{3}\\d{4}) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";
		String regex3 = "(\\d{1,2}[A-z]{3}\\d{4}) (.*) ([A-Z]{3}) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\d{5,6})";
		String regex4 = "(\\d{1,2}[A-z]{3}\\d{4}) (.*) ([A-Z]{3}) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		Pattern p4 = Pattern.compile(regex4);
		
		Matcher m1 = null;
		Matcher m2 = null;
		Matcher m3 = null;
		Matcher m4 = null;
		
		
		String transType = null;
		int rowCount = 0;
		HoldingAsset currentAsset = null;
		for(WebElement row: rows){
			String rowText = row.getText().trim();
			
			System.out.println("RowText ->> " + rowText);
			
			m1 = p1.matcher(rowText);
			m2 = p2.matcher(rowText);
			m3 = p3.matcher(rowText);
			m4 = p4.matcher(rowText);
			
			if(rowText.equals("INFLOW")){
				transType = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
				rowCount = 0;
				currentAsset = null;
			}
			else if(rowText.equals("OUTFLOW")){
				transType = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
				rowCount = 0;
				currentAsset = null;
			}
			
			if(rowText.contains("HSBC Private Bank") || rowText.contains("TOTAL") || rowText.contains("MONTH END BALANCE")
					|| rowText.contains("SUBTOTAL")){
				rowCount = 0;
				currentAsset = null;
			}
			
			if(m1.matches()){
				rowCount = 1;
				String assetCurrency = m1.group(1);
				String description = m1.group(2);
				String qunatity = m1.group(3);
				String value = m1.group(4);
				String accruedInterest = m1.group(5);
				
				HoldingAsset asset = new HoldingAsset();
				
				asset.setHoldingAssetCurrency(assetCurrency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(formatAmount(qunatity));
				asset.setHoldingAssetCurrentValue(formatAmount(value));
				asset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				account.addAsset(asset);
				currentAsset = asset;
			}
			else if(m2.matches()){
				rowCount = 1;
				currentAsset = null;
				String transDate = m2.group(1);
				String description = m2.group(2);
				String valueDate = m2.group(3);
				String amount = m2.group(4);
				String type = null;
				amount = formatAmount(amount);
				
				if(amount.contains("-")){
					type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
				}
				else{
					type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
				}
				
				InvestmentTransaction transaction = new InvestmentTransaction();
				
				transaction.setAccountNumber(account.getAccountNumber());
				transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_MMM_YYYY));
				transaction.setValuationDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_MMM_YYYY));
				transaction.setDescription(description);
				transaction.setAmount(formatAmount(amount));
				transaction.setCurrency(account.getCurrency());
				transaction.setType(type);
				account.addTransaction(transaction);
				
			}
			else if(m3.matches()){
				currentAsset = null;
				rowCount = 1;
				String transDate = m3.group(1);
				String description = m3.group(2);
				String transCurrency = m3.group(3);
				String amount = m3.group(4);
				String type = null;
				amount = formatAmount(amount);
				
				if(description.contains("REVERSE") || description.contains("BREAK") || description.contains("UPLIFT")){
					type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
				}
				else{
					type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
				}
				
				InvestmentTransaction transaction = new InvestmentTransaction();
				
				transaction.setAccountNumber(account.getAccountNumber());
				transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_MMM_YYYY));
				transaction.setDescription(description);
				transaction.setAmount(formatAmount(amount));
				transaction.setCurrency(transCurrency);
				transaction.setType(type);
				account.addTransaction(transaction);
			}
			else if(m4.matches()){
				rowCount = 1;
				currentAsset = null;
				String transDate = m4.group(1);
				String description = m4.group(2);
				String transCurrency = m4.group(3);
				String amount = m4.group(4);
				amount = formatAmount(amount);
				
				InvestmentTransaction transaction = new InvestmentTransaction();
				
				transaction.setAccountNumber(account.getAccountNumber());
				transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_MMM_YYYY));
				transaction.setDescription(description);
				transaction.setAmount(formatAmount(amount));
				transaction.setCurrency(transCurrency);
				transaction.setType(transType);
				account.addTransaction(transaction);
			}
			else if(currentAsset != null && rowCount > 0 && rowCount < 4){
				rowCount++;
				if(rowText.matches(".*(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)")){
					rowCount = 0;
					currentAsset = null;
					continue;
				}
				String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			
		}

		List<HoldingAsset> assets = account.getAssets();
		List<InvestmentTransaction> transactions = account.getInvestmentTransactions();
		
		for(HoldingAsset asset: assets){
			filterAssetDescription(asset);
		}
		
		for(InvestmentTransaction transaction: transactions){
			filterTransactionDescription(transaction);
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
	
	private static String formatAmount(String amount){
		if(StringUtils.isEmpty(amount)){
			return null;
		}

		amount = amount.replace(",", "");

		if(amount.contains("(")){
			amount = amount.replace("(", "");
			amount  = amount.replace(")", "").trim();
			amount = "-"+amount;
		}

		return amount;
	}
	
	private static void filterAssetDescription(HoldingAsset asset) throws ParseException{
		if(asset == null){
			return;
		}
		
		String description = asset.getHoldingAssetDescription();
		
		String regex1 = ".* (\\d{1,2}[A-z]{3}\\d{4}) TO (\\d{1,2}[A-z]{3}\\d{4}) ?@ ?(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) ?%.*";
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1 = p1.matcher(description);
		
		if(m1.matches()){
			String startDate = m1.group(1);
			String maturityDate = m1.group(2);
			String coupon  = m1.group(3);
			
			asset.setHoldingAssetStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_MMM_YYYY));
			asset.setHoldingAssetBondMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_MMM_YYYY));
			asset.setHoldingAssetCoupon(formatAmount(coupon));
		}
	}
	
	private static void filterTransactionDescription(InvestmentTransaction transaction) throws ParseException{
		if(transaction == null){
			return;
		}
		
		String description = transaction.getDescription();
		
		String regex1 = ".* @ ?(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) ?% .*(\\d{1,2}[A-z]{3}\\d{4}) ?- ?(\\d{1,2}[A-z]{3}\\d{4}).*";
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1 = p1.matcher(description);
		
		if(m1.matches()){
			String coupon  = m1.group(1);
			String startDate = m1.group(2);
			String maturityDate = m1.group(3);
			
			transaction.setCoupon(formatAmount(coupon));
			transaction.setStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_MMM_YYYY));
			transaction.setMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_MMM_YYYY));
		}
	}

}
