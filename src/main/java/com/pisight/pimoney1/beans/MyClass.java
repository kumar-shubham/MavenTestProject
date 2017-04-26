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
			boxTest = new PDFExtracter(getFile("investments/new", "820420 Portfolio Statement 31.01.2017", "pdf"),"");
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




	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accNumEle = driver.findElement(By.xpath("//td[contains(text(), 'DSW')]"));
		String accNum = accNumEle.getText().trim();

		String regex = ".*DSW (\\d{8,9}\\.\\d{4})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accNum);


		if(m.matches()){
			accNum = m.group(1);
		}

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'Reference currency:')]"));
		String currency = currencyEle.getText().trim();

		regex = ".*Reference currency: ([A-Z]{3})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(currency);

		if(m.matches()){
			currency = m.group(1);
		}

		WebElement stmtDateEle = driver.findElement(By.xpath("//td[contains(text(), 'Statement date')]"));
		String stmtDate = stmtDateEle.getText().trim();

		regex = ".*Statement date (\\d{1,2}-\\d{2}-\\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);

		if(m.matches()){
			stmtDate = m.group(1);
		}

		String xpath = "//tr[td[contains(text(), 'Value of holdings as at')]]/following-sibling::tr[2]";
		WebElement balanceEle = driver.findElement(By.xpath(xpath));
		String balance = balanceEle.getText().trim();

		regex = "(?:.* )?(-?(?:\\d*,)*\\d+\\.?\\d*)( .*)?";
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
		account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
		account.setAccountName("Portfolio - " + currency);
		response.addInvestmentAccount(account);

		getAssetType1(account, driver);
		getAssetType2(account, driver);
		getAssetType3(account, driver);

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

	private static void getAssetType1(InvestmentAccount account, WebDriver driver) throws ParseException {
		// TODO Auto-generated method stub
		String regex = "([A-Z]{3})?(?: )?(.*) (-?(?:\\d*,)*\\d+\\.?\\d*)(?: (-?(?:\\d*,)*\\d+\\.?\\d*) %)?(?: (\\d{1,2}-\\d{2}-\\d{4}))?(?: (\\d{1,2}-\\d{2}-\\d{4}))? (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;

		String xpath = "//tr[preceding-sibling::tr/td[text() = 'ASSETS UNDER CUSTODY : Cash'] and following-sibling::tr/td[contains(text() , 'TOTAL CASH ')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		HoldingAsset currentAsset = null;
		String currentCurrency = null;
		int rowCount = 0;
		for(WebElement row: rows){

			String rowText = row.getText().trim();
			System.out.println("RowText - > " + rowText);

			m = p.matcher(rowText);

			if(m.matches()){
				rowCount = 1;
				String currency = m.group(1);
				String description = m.group(2);
				String quantity = m.group(3);
				String coupon = m.group(4);
				String startDate = m.group(5);
				String maturity = m.group(6);
				String fxValue = m.group(7);

				if(StringUtils.isEmpty(currency)){
					currency = currentCurrency;
				}
				else{
					currentCurrency = currency;
				}

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetCoupon(ParserUtility.formatAmount(coupon));
				asset.setHoldingAssetStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));

				if(StringUtils.isEmpty(coupon)){
					asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
				}
				else{
					asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_DEPOSIT);
				}
				account.addAsset(asset);
				currentAsset = asset;
			}
			else if(currentAsset != null && rowCount == 1 && currentAsset.getHoldingAssetCategory().equals(HoldingAsset.CATEGORY_DEPOSIT)){

				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(rowText));
				rowCount = 0;
				currentAsset = null;
			}

		}

	}

	private static void getAssetType2(InvestmentAccount account, WebDriver driver) throws ParseException {
		// TODO Auto-generated method stub

		String regex = "([A-Z]{3})?(?: )?(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) % (\\d{1,2}-\\d{2}-\\d{4}) (\\d{1,2}-\\d{2}-\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;

		String xpath = "//tr[preceding-sibling::tr/td[text() = 'ASSETS UNDER CUSTODY : Loans'] and following-sibling::tr/td[contains(text() , 'TOTAL LOANS ')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath));

		HoldingAsset currentAsset = null;
		String currentCurrency = null;
		int rowCount = 0;
		for(WebElement row: rows){

			String rowText = row.getText().trim();
			System.out.println("RowText - > " + rowText);

			m = p.matcher(rowText);

			if(m.matches()){
				rowCount = 1;
				String currency = m.group(1);
				String description = m.group(2);
				String quantity = m.group(3);
				String coupon = m.group(4);
				String startDate = m.group(5);
				String maturity = m.group(6);
				String fxValue = m.group(7);

				if(StringUtils.isEmpty(currency)){
					currency = currentCurrency;
				}
				else{
					currentCurrency = currency;
				}

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetCoupon(ParserUtility.formatAmount(coupon));
				asset.setHoldingAssetStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_LOAN);

				account.addAsset(asset);
				currentAsset = asset;
			}
			else if(currentAsset != null && rowCount == 1 && currentAsset.getHoldingAssetCategory().equals(HoldingAsset.CATEGORY_DEPOSIT)){

				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(rowText));
				rowCount = 0;
				currentAsset = null;
			}

		}

	}

	private static void getAssetType3(InvestmentAccount account, WebDriver driver) throws ParseException {
		// TODO Auto-generated method stub

		String regex1 = "([A-Z]{3})?(?: )?(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) % (-?(?:\\d*,)*\\d+\\.?\\d*) % (-?(?:\\d*,)*\\d+\\.?\\d*) % (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) %";
		String regex2 = "([A-Z]{3})?(?: )?(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) %";
		String regex3 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*)(?: (\\d{1,2}-\\d{2}-\\d{4}))? (-?(?:\\d*,)*\\d+\\.?\\d*) %(?: .{2,4})? (-?(?:\\d*,)*\\d+\\.?\\d*) % (-?(?:\\d*,)*\\d+\\.?\\d*)";
		String regex4 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*)(?: (\\d{1,2}-\\d{2}-\\d{4}))?(?: .{2,4})? (-?(?:\\d*,)*\\d+\\.?\\d*) % (-?(?:\\d*,)*\\d+\\.?\\d*)";
		String regex5 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) %";
		String regex6 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) %";
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		Pattern p4 = Pattern.compile(regex4);
		Pattern p5 = Pattern.compile(regex5);
		Pattern p6 = Pattern.compile(regex6);
		Matcher m1 = null;
		Matcher m2 = null;
		Matcher m3 = null;
		Matcher m4 = null;
		Matcher m5 = null;
		Matcher m6 = null;

		String xpath1 = "//tr[preceding-sibling::tr/td[text() = 'ASSETS UNDER CUSTODY : Bonds'] and following-sibling::tr/td[contains(text() , 'TOTAL BONDS ')]]";
		String xpath2 = "//tr[preceding-sibling::tr/td[text() = 'ASSETS UNDER CUSTODY : Equities'] and following-sibling::tr/td[contains(text() , 'TOTAL EQUITIES ')]]";
		String xpath3 = "//tr[preceding-sibling::tr/td[text() = 'ASSETS UNDER CUSTODY : Investment funds'] and following-sibling::tr/td[contains(text() , 'TOTAL INVESTMENT FUNDS ')]]";

		List<WebElement> rows = driver.findElements(By.xpath(xpath1));

		rows.addAll(driver.findElements(By.xpath(xpath2)));
		rows.addAll(driver.findElements(By.xpath(xpath3)));

		HoldingAsset currentAsset = null;
		String currentCurrency = null;
		int rowCount = 0;
		for(WebElement row: rows){

			String rowText = row.getText().trim();
			System.out.println("RowText - > " + rowText);
			
			if(rowText.contains(""))

			m1 = p1.matcher(rowText);
			m2 = p2.matcher(rowText);
			m3 = p3.matcher(rowText);
			m4 = p4.matcher(rowText);
			m5 = p5.matcher(rowText);
			m6 = p6.matcher(rowText);

			if(m1.matches()){
				rowCount = 1;

				String currency = m1.group(1);
				String description = m1.group(2);
				String quantity = m1.group(3);
				String coupon  = m1.group(4);
				String unitPrice = m1.group(5);
				String unitCost = m1.group(6);
				String profit = m1.group(7);
				String fxValue = m1.group(8);

				if(StringUtils.isEmpty(currency)){
					currency = currentCurrency;
				}
				else{
					currentCurrency = currency;
				}

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetCoupon(ParserUtility.formatAmount(coupon));
				asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(unitPrice));
				asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
				asset.setHoldingAssetUnrealizedProfitLoss(ParserUtility.formatAmount(profit));
				asset.setHoldingAssetUnrealizedProfitLossCurrency(account.getCurrency());
				asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
				asset.setBondNature(true);
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_BOND);
				account.addAsset(asset);
				currentAsset = asset;

			}
			else if(m2.matches()){
				rowCount = 1;

				String currency = m2.group(1);
				String description = m2.group(2);
				String quantity = m2.group(3);
				String unitPrice = m2.group(4);
				String unitCost = m2.group(5);
				String profit = m2.group(6);
				String fxValue = m2.group(7);

				if(StringUtils.isEmpty(currency)){
					currency = currentCurrency;
				}
				else{
					currentCurrency = currency;
				}

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
				asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(unitPrice));
				asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
				asset.setHoldingAssetUnrealizedProfitLoss(ParserUtility.formatAmount(profit));
				asset.setHoldingAssetUnrealizedProfitLossCurrency(account.getCurrency());
				asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_UNIT_TRUST__MUTUAL_FUND);
				account.addAsset(asset);
				currentAsset = asset;

			}
			else if(m3.matches() && currentAsset != null  && rowCount == 1){
				rowCount ++;

				String description = m3.group(1);
				String maturity = m3.group(3);
				String accruedInterest = m3.group(5);

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description);
				currentAsset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));

			}
			else if(m4.matches() && currentAsset != null  && rowCount == 1){
				rowCount ++;

				String description = m4.group(1);
				String maturity = m4.group(3);
				String accruedInterest = m4.group(4);

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description);
				currentAsset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY));
				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));


			}
			else if(m5.matches() && currentAsset != null  && rowCount == 1){
				rowCount ++;

				String description = m5.group(1);

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description);

			}
			else if(m6.matches() && currentAsset != null  && rowCount == 1){
				rowCount ++;

				String description = m6.group(1);

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description);

			}
			else if(currentAsset != null  && rowCount > 1){
				
				if(rowText.matches(".*[A-Z]{2}\\w{8}\\d{2}.*")){

					String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
					
					currentAsset.setHoldingAssetDescription(description);
				}
				rowCount = 0;
			}
		}


	}


}
