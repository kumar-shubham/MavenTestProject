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
			boxTest = new PDFExtracter(getFile("investments/new", "JPM 310117", "pdf"),"");
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

	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accNumEle = driver.findElement(By.xpath("//td[contains(text(), 'Account Number: ')]"));
		String accNum = accNumEle.getText().trim();

		String regex = ".*Account Number: (\\d{6,7})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accNum);


		if(m.matches()){
			accNum = m.group(1);
		}


		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text(), 'Statement of Account')]]/following-sibling::tr[1]"));
		String stmtDate = stmtDateEle.getText().trim();

		regex = ".*\\d{1,2} \\w+ - (\\d{1,2} \\w+ \\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);

		if(m.matches()){
			stmtDate = m.group(1);
		}

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'Reference Currency -')]"));
		String currency = currencyEle.getText().trim();

		regex = ".*Reference Currency - ([A-Z]{3})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(currency);

		if(m.matches()){
			currency = m.group(1);
		}

		WebElement balanceEle = driver.findElement(By.xpath("//td[contains(text(), 'Total Net Market Value')]"));
		String balance = balanceEle.getText().trim();

		regex = ".*Total Net Market Value\\*? (?:-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(balance);

		if(m.matches()){
			balance = m.group(1);
		}

		System.out.println(accNum);
		System.out.println(currency);
		System.out.println(stmtDate);
		System.out.println(balance);

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setAccountNumber(accNum);
		account.setBalance(ParserUtility.formatAmount(balance));
		account.setAvailableBalance(ParserUtility.formatAmount(balance));
		account.setCurrency(currency);
		account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_SPACE_MMMM_SPACE_YYYY));
		account.setAccountName("Portfolio -  " + currency );
		response.addInvestmentAccount(account);

		// Cash
		getAssetType1(account, driver);

		// Fixed Income
		getAssetType2(account, driver);

		// Equities
		getAssetType3(account, driver);

		//Loan
		getAssetType4(account, driver);

		//Transactions
		getTransaction(account, driver);


		List<HoldingAsset> assets = account.getAssets();

		for(HoldingAsset asset: assets){
			getDetailsFromAssetDescription(asset);
		}
		
		List<InvestmentTransaction> transactions = account.getInvestmentTransactions();
		
		for(InvestmentTransaction trans: transactions){
			getDetailsFromTransDescription(trans);
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

	private static void getTransaction(InvestmentAccount account, WebDriver driver) throws ParseException {
		// TODO Auto-generated method stub
		String regex1 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\w+) (.*) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String regex3 = "(.* )? (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		Matcher m1 =null;
		Matcher m2 = null;
		Matcher m3 = null;

		String xpath  = "//tr[preceding-sibling::tr/td[text() = 'Cash Account Transactions'] and following-sibling::tr/td[contains(text() , 'Accounting Policies')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		boolean isCashTrans = true;
		InvestmentTransaction currentTrans = null;
		int rowCount = 0;
		String currency = null;
		for(WebElement row: rows){

			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);
			
			if(rowText.contains("V.A.T. number") || rowText.toLowerCase().contains("total") || rowText.toLowerCase().contains("transactions")){
				System.out.println("Not a valid row. skipping");
				rowCount = 0;
				currentTrans = null;
			}

			if(rowText.equalsIgnoreCase("Security Transactions")){
				isCashTrans = false;
			}


			if(isCashTrans){

				if(rowText.toUpperCase().contains(" AUSTRALIAN DOLLAR")){
					currency = "AUD";
				}
				else if(rowText.toUpperCase().contains("HONG KONG DOLLAR")){
					currency = "HKD";
				}
				else if(rowText.toUpperCase().contains("POUND STERLING")){
					currency = "GBP";
				}
				else if(rowText.toUpperCase().contains("U.S. DOLLAR")){
					currency = "USD";
				}
				else if(rowText.toUpperCase().contains("EURO")){
					currency = "EUR";
				}

				m1 = p1.matcher(rowText);

				if(m1.matches()){
					rowCount = 1;
					String transDate = m1.group(1);
					String valueDate = m1.group(2);
					String description = m1.group(3);
					String amount = m1.group(4);
					String type = null;

					if(amount.contains("-")){
						type = InvestmentTransaction.TRANSACTION_TYPE_OUTFLOW;
					}
					else{
						type = InvestmentTransaction.TRANSACTION_TYPE_INFLOW;
					}

					InvestmentTransaction transaction = new InvestmentTransaction();

					transaction.setAccountNumber(account.getAccountNumber());
					transaction.setCurrency(currency);
					transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
					transaction.setValuationDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
					transaction.setDescription(description);
					transaction.setAmount(ParserUtility.formatAmount(amount));
					transaction.setType(type);
					transaction.setAssetCategory(HoldingAsset.CATEGORY_CASH);
					account.addTransaction(transaction);
					currentTrans = transaction;
				}
				else if(currentTrans != null && rowCount < 3 && rowCount > 0){
					rowCount++;
					String description = currentTrans.getDescription() + " " +  rowText;
					currentTrans.setDescription(description);
				}
			}
			else{
				m2 = p2.matcher(rowText);
				m3 = p3.matcher(rowText);
				if(m2.matches()){
					rowCount = 1;
					String transDate = m2.group(1);
					String valueDate = m2.group(2);
					String type = m2.group(3);
					String description = m2.group(4);
					currency = m2.group(5);
					String quantity = m2.group(6);
					String price = m2.group(7);
					String amount = m2.group(8);
					
					if(quantity.contains("-")){
						type = InvestmentTransaction.TRANSACTION_TYPE_SELL;
					}
					else{
						type = InvestmentTransaction.TRANSACTION_TYPE_BUY;
					}

					InvestmentTransaction transaction = new InvestmentTransaction();

					transaction.setAccountNumber(account.getAccountNumber());
					transaction.setCurrency(currency);
					transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
					transaction.setValuationDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
					transaction.setDescription(description);
					transaction.setAmount(ParserUtility.formatAmount(amount));
					transaction.setType(type);
					transaction.setAssetQuantity(ParserUtility.formatAmount(quantity));
					transaction.setAssetUnitCost(ParserUtility.formatAmount(price));
					account.addTransaction(transaction);
					currentTrans = transaction;
				}
				else if(m3.matches() && currentTrans != null && rowCount == 1){
					
					String description = m3.group(1);
					description = currentTrans.getDescription() + " " +  description;
					currentTrans.setDescription(description);
				}
				else if(currentTrans != null && rowCount < 3 && rowCount > 0){
					rowCount++;
					String description = currentTrans.getDescription() + " " +  rowText;
					currentTrans.setDescription(description);
				}
			}

		}

	}

	private static void getDetailsFromAssetDescription(HoldingAsset asset) throws ParseException {
		// TODO Auto-generated method stub
		String descRegex = "(?:.* )?(-?(?:\\d*,)*\\d+\\.?\\d*)% (\\d{1,2}\\.\\d{2}\\.\\d{4}) ?- ?(\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
		Pattern p = Pattern.compile(descRegex);
		Matcher m = p.matcher(asset.getHoldingAssetDescription());

		String coupon = null;
		String startDate = null;
		String maturity = null;
		System.out.println(asset.getHoldingAssetDescription());
		if(m.matches()){
			System.out.println("matched^^^^^^^");
			coupon = m.group(1);
			startDate = m.group(2);
			maturity = m.group(3);
		}


		asset.setHoldingAssetCoupon(coupon, true);
		asset.setHoldingAssetStartDate(startDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
		asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);

	}
	
	private static void getDetailsFromTransDescription(InvestmentTransaction trans) throws ParseException {
		// TODO Auto-generated method stub
		String descRegex = "(?:.* )?(-?(?:\\d*,)*\\d+\\.?\\d*)% (\\d{1,2}\\.\\d{2}\\.\\d{4}) ?- ?(\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
		Pattern p = Pattern.compile(descRegex);
		Matcher m = p.matcher(trans.getDescription());

		String coupon = null;
		String startDate = null;
		String maturity = null;
		System.out.println(trans.getDescription());
		if(m.matches()){
			System.out.println("matched trans^^^^^^^");
			coupon = m.group(1);
			startDate = m.group(2);
			maturity = m.group(3);
		}

		trans.setCoupon(ParserUtility.formatAmount(coupon));
		trans.setStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
		trans.setMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));

	}

	private static void getAssetType1(InvestmentAccount account, WebDriver driver) {
		// TODO Auto-generated method stub
		String regex = "(.*) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})%";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;

		String xpath = "//tr[preceding-sibling::tr/td[contains(text() , 'Cash Holdings')] and following-sibling::tr/td[contains(text() , 'Total Cash Holdings')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			m = p.matcher(rowText);

			if(m.matches()){
				String description  = m.group(1);
				String currency = m.group(2);
				String quantity = m.group(3);
				String fxvalue = m.group(4);

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(quantity, true);
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetFxMarketValue(fxvalue, true);
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
				account.addAsset(asset);
			}

		}

	}

	private static void getAssetType2(InvestmentAccount account, WebDriver driver) throws ParseException {

		String regex1 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})\\(\\w\\) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})%";
		String regex2 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{2})(?: (-?(?:\\d*,)*\\d+\\.?\\d{2})\\(\\w\\))?";
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = null;
		Matcher m2 = null;

		String xpath = "//tr[preceding-sibling::tr/td[contains(text() , 'Fixed Income Holdings (with maturity over 1 year) by Currency')] and following-sibling::tr/td[contains(text() , 'Total Fixed Income')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		String currency = "";
		HoldingAsset currentAsset = null;
		int rowCount = 0;
		for(WebElement row: rows){
			String rowText = row.getText().trim();


			if(rowText.contains("V.A.T. number") || rowText.toLowerCase().contains("total") || rowText.toLowerCase().contains("loans")
					|| ((rowText.contains("(C)") || rowText.toLowerCase().contains("(F)")) && rowCount == 2)){
				System.out.println("Not a valid row. skipping");
				rowCount = 0;
				currentAsset = null;
			}

			if(rowText.equalsIgnoreCase("Australian Dollar")){
				currency = "AUD";
			}
			else if(rowText.equalsIgnoreCase("U.S. Dollar")){
				currency = "USD";
			}
			else if(rowText.equalsIgnoreCase("Euro")){
				currency = "EUR";
			}

			m1 = p1.matcher(rowText);
			m2 = p2.matcher(rowText);

			System.out.println("RowText -> " + rowText);
			//			System.out.println("m1 -> " + m1.matches() + " :: m2 -> " + m2.matches());

			if(m1.matches()){
				rowCount = 1;
				//				System.out.println("1^^^^^^^^^^^^^^^^^");
				String description  = m1.group(1);
				String quantity = m1.group(2);
				String unitCost = m1.group(3);
				String unitPrice = m1.group(4);
				String fxValue = m1.group(6);
				String profit = m1.group(7);
				String yield = m1.group(8);

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(quantity, true);
				asset.setHoldingAssetAverageUnitCost(unitCost, true);
				asset.setHoldingAssetIndicativePrice(unitPrice, true);
				asset.setHoldingAssetFxMarketValue(fxValue, true);
				asset.setHoldingAssetUnrealizedProfitLoss(profit, true);
				asset.setHoldingAssetUnrealizedProfitLossCurrency(account.getCurrency());
				asset.setHoldingAssetYield(yield, true);
				asset.setBondNature(true);
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_BOND);
				account.addAsset(asset);
				currentAsset = asset;

			}
			else if(m2.matches() && currentAsset != null && rowCount == 1){
				//				System.out.println("2^^^^^^^^^^^^^^^^^");
				rowCount = 2;
				String description = m2.group(1);
				String valueDate = m2.group(2);
				String accruedInterest = m2.group(3);

				description = currentAsset.getHoldingAssetDescription() + " " + description; 

				currentAsset.setHoldingAssetDescription(description);
				currentAsset.setHoldingAssetIndicativePriceDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
				currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
			}
			else if(currentAsset != null && rowCount == 2){
				String description = currentAsset.getHoldingAssetDescription() + " " + rowText; 
				currentAsset.setHoldingAssetDescription(description);
				rowCount = 0;
				currentAsset = null;
			}

		}

	}

	private static void getAssetType3(InvestmentAccount account, WebDriver driver) throws ParseException {

		String regex1 = "(.*) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})\\(\\w\\) (-?(?:\\d*,)*\\d+\\.?\\d{2})%";
		String regex2 = "(.* )?(\\d{1,2}\\.\\d{2}\\.\\d{4})(?: (-?(?:\\d*,)*\\d+\\.?\\d{2})\\(\\w\\))?";
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = null;
		Matcher m2 = null;

		String xpath = "//tr[preceding-sibling::tr/td[contains(text() , 'Equity Holdings by Market')] and following-sibling::tr/td[contains(text() , 'Total Equities')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		HoldingAsset currentAsset = null;
		int rowCount = 0;
		for(WebElement row: rows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			if(rowText.contains("V.A.T. number") || rowText.toLowerCase().contains("total") || rowText.toLowerCase().contains("loans")
					|| ((rowText.contains("(C)") || rowText.toLowerCase().contains("(F)")) && rowCount == 2)){
				System.out.println("Not a valid row. skipping");
				rowCount = 0;
				currentAsset = null;
			}

			m1 = p1.matcher(rowText);
			m2 = p2.matcher(rowText);

			if(m1.matches()){
				rowCount = 1;

				String description  = m1.group(1);
				String currency = m1.group(2);
				String quantity = m1.group(3);
				String unitCost = m1.group(4);
				String unitPrice = m1.group(5);
				String fxValue = m1.group(7);
				String profit = m1.group(8);

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetQuantity(quantity, true);
				asset.setHoldingAssetAverageUnitCost(unitCost, true);
				asset.setHoldingAssetIndicativePrice(unitPrice, true);
				asset.setHoldingAssetFxMarketValue(fxValue, true);
				asset.setHoldingAssetUnrealizedProfitLoss(profit, true);
				asset.setHoldingAssetUnrealizedProfitLossCurrency(account.getCurrency());
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_EQUITY);
				account.addAsset(asset);
				currentAsset = asset;

			}
			else if(m2.matches() && currentAsset != null && rowCount == 1){
				rowCount = 2;
				String description = m2.group(1);
				String valueDate = m2.group(2);

				description = currentAsset.getHoldingAssetDescription() + " " + description; 

				currentAsset.setHoldingAssetDescription(description);
				currentAsset.setHoldingAssetIndicativePriceDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
			}
			else if(currentAsset != null && rowCount == 2){
				String description = currentAsset.getHoldingAssetDescription() + " " + rowText; 
				currentAsset.setHoldingAssetDescription(description);
				rowCount = 0;
				currentAsset = null;
			}

		}
	}

	private static void getAssetType4(InvestmentAccount account, WebDriver driver) throws ParseException {

		String regex1 = "([A-Z]{3}) ((-?(?:\\d*,)*\\d+\\.?\\d{2})% (\\d{1,2}\\.\\d{2}\\.\\d{4}) - (\\d{1,2}\\.\\d{2}\\.\\d{4})) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		String regex2 = "(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2})";
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = null;
		Matcher m2 = null;

		String xpath = "//tr[preceding-sibling::tr/td[text() = 'Borrowings'] and following-sibling::tr/td[contains(text() , 'Loans')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		HoldingAsset currentAsset = null;
		int rowCount = 0;
		for(WebElement row: rows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			if(rowText.contains("V.A.T. number") || ((rowText.contains("(C)") || rowText.toLowerCase().contains("(F)")) && rowCount == 2)
					|| rowText.toLowerCase().contains("total") || rowText.toLowerCase().contains("loans")){
				System.out.println("Not a valid row. skipping");
				rowCount = 0;
				currentAsset = null;
			}

			m1 = p1.matcher(rowText);
			m2 = p2.matcher(rowText);

			if(m1.matches()){

				rowCount = 1;

				String currency = m1.group(1);
				String description = m1.group(2);
				String coupon = m1.group(3);
				String startDate = m1.group(4);
				String maturity = m1.group(5);
				String quantity = m1.group(6);
				String fxValue = m1.group(7);

				HoldingAsset asset = new HoldingAsset();
				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetCoupon(coupon, true);
				asset.setHoldingAssetStartDate(startDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
				asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
				asset.setHoldingAssetQuantity(quantity, true);
				asset.setHoldingAssetFxMarketValue(fxValue, true);
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_LOAN);
				account.addAsset(asset);
				currentAsset = asset;
			}
			else if(m2.matches() && currentAsset != null && rowCount == 1){

				String accruedInterest = m2.group(1);
				String fxAccruedInterest = m2.group(2);

				currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
				currentAsset.setHoldingAssetFxAccruredInterest(fxAccruedInterest, true);
				rowCount = 0;
				currentAsset = null;

			}
			else{
				rowCount = 0;
				currentAsset = null;
			}

		}
	}

}
