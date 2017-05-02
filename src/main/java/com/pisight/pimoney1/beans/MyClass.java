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
import com.pisight.pimoney.models.Response;

public class MyClass {

	//	private static Logger logger = Logger.getLogger(MyClass.class);


	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments/new3", "BSI HK", "pdf"),"");
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
	
	private static String regex1 = "([A-Z]{3}) (-?\\d+\\.?\\d*) (\\d{8,9}\\.\\d{4})(?: (-?\\d+\\.?\\d*))?(?: (-?\\d+\\.?\\d{2}))?(?: (-?\\d+\\.?\\d{2})%)?";
	private static Pattern  p1 = Pattern.compile(regex1);
	
	private static String regex2 = "([A-Z]{3}) (-?\\d+\\.?\\d*) (.*) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*)%";
	private static String regex3 = "(.* )?(-?\\d+\\.?\\d*)";
	private static Pattern p2 = Pattern.compile(regex2);
	private static Pattern p3 = Pattern.compile(regex3);
	
	private static String regex4 = "([A-Z]{3}) (-?\\d+\\.?\\d*) (.*) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*)% (-?\\d+\\.?\\d*)%";
	private static String regex5 = "(.* )?\\((\\d{1,2}/\\d{2}/\\d{4})\\)";
	private static String regex6 = "(.* )?(-?\\d+\\.?\\d{2}) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{2})";
	private static String regex7 = "(.* )?(-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{2})";
	private static String regex8 = "(.* )?(-?\\d+\\.?\\d{2}) (-?\\d+\\.?\\d{2})";
	private static String regex9 = "(.* )?(-?\\d+\\.?\\d{2})";
	private static Pattern p4 = Pattern.compile(regex4);
	private static Pattern p5 = Pattern.compile(regex5);
	private static Pattern p6 = Pattern.compile(regex6);
	private static Pattern p7 = Pattern.compile(regex7);
	private static Pattern p8 = Pattern.compile(regex8);
	private static Pattern p9 = Pattern.compile(regex9);
	
	
	private static String regex10 = "([A-Z]{3}) (-?\\d+\\.?\\d*) (.*) (-?\\d+\\.?\\d{2,4}) (-?\\d+\\.?\\d{2,4}) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*)% (-?\\d+\\.?\\d*)%";
	private static String regex11 = "(.* )?\\((\\d{1,2}/\\d{2}/\\d{4})\\)";
	private static String regex12 = "(.* )?(-?\\d+\\.?\\d{4}) (-?\\d+\\.?\\d{4})";
	private static Pattern p10 = Pattern.compile(regex10);
	private static Pattern p11 = Pattern.compile(regex11);
	private static Pattern p12 = Pattern.compile(regex12);
	
	
	private static String regex13 = "([A-Z]{3}) (-?\\d+\\.?\\d*) (\\d{8,9}\\.\\d{4}) (.*) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*) (-?\\d+\\.?\\d*)%";
	private static String regex14 = "(.* )?(-?\\d+\\.?\\d{4})";
	private static Pattern p13 = Pattern.compile(regex13);
	private static Pattern p14 = Pattern.compile(regex14);

	private static HoldingAsset currentAsset = null;
	private static int rowCount = 0;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");
		
		WebElement accNumEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Portfolio')]]"));
		String accNum = accNumEle.getText().trim();
		
		String regex = "Portfolio (\\d{8,9}\\.\\d{4})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accNum);
		
		if(m.matches()){
			accNum = m.group(1);
		}
		
		WebElement currencyEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Reference currency')]]"));
		String currency = currencyEle.getText().trim();
		
		regex = "Reference currency ([A-Z]{3})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(currency);
		
		if(m.matches()){
			currency = m.group(1);
		}
		
		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Portfolio statement as at')]]"));
		String stmtDate = stmtDateEle.getText().trim();
		
		regex = "Portfolio statement as at (\\d{1,2}\\.\\d{2}\\.\\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);
		
		if(m.matches()){
			stmtDate = m.group(1);
		}
		
		WebElement balanaceEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Total of Assets')]]"));
		String balance = balanaceEle.getText().trim();
		balance = balance.replace("'", "");
		regex = "Total of Assets (-?\\d+\\.?\\d*)( .*)";
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
		account.setCurrency(currency);
		account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
		account.setBalance(ParserUtility.formatAmount(balance));
		account.setAvailableBalance(ParserUtility.formatAmount(balance));
		account.setAccountName("Portfolio - " + currency);
		response.addInvestmentAccount(account);
		
		List<WebElement> rows = driver.findElements(By.xpath("//tr[td[text() = 'Positions detail']]/following-sibling::tr"));
		
		boolean isType1 = false;
		boolean isType2 = false;
		boolean isType3 = false;
		boolean isType4 = false;
		boolean isType5 = false;
		
		
		for(WebElement row: rows){
			String rowText = row.getText().trim();
			rowText = rowText.replace("'", "");
			System.out.println("RowText -> " + rowText);
			
			if(rowText.contains("BSI Ltd")){
				System.out.println("Not a valid row. Skipping.");
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			else if(rowText.contains("Margin summary")){
				System.out.println("End of statement");
				break;
			}
			else if(rowText.contains("Cash accounts")){
				isType1 = true;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			else if(rowText.contains("Forward Forex and other Derivatives on Forex")){
				isType1 = false;
				isType2 = true;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			if(rowText.contains("Bonds")){
				isType1 = false;
				isType2 = false;
				isType3 = true;
				isType4 = false;
				isType5 = false;
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			if(rowText.contains("Equities")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = true;
				isType5 = false;
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			if(rowText.contains("Commodities")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = true;
				currentAsset = null;
				rowCount = 0;
				continue;
			}
			
			if(isType1){
				getAssetType1(account, rowText);
			}
			else if(isType2){
				getAssetType2(account, rowText);
			}
			else if(isType3){
				getAssetType3(account, rowText);
			}
			else if(isType4){
				getAssetType4(account, rowText);
			}
			else if(isType5){
				getAssetType5(account, rowText);
			}
			
		}
		
		List<HoldingAsset> assets = account.getAssets();
		
		for(HoldingAsset asset: assets){
			getDetailsFromAssetDescription(asset);
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

	private static void getDetailsFromAssetDescription(HoldingAsset asset) throws ParseException {
		// TODO Auto-generated method stub
		String regex1 = "(?:.* )?(-?\\d+\\.?\\d*)%( .*)?";
		String regex2 = "(?:.* )?(\\d{1,2}\\.\\d{2}\\.\\d{2,4})( .*)?";
		String regex3 = "(?:.* )?([A-Z]{2}\\w{8}\\d{2})( .*)?";
		
		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		
		Matcher m1 = p1.matcher(asset.getHoldingAssetDescription());
		Matcher m2 = p2.matcher(asset.getHoldingAssetDescription());
		Matcher m3 = p3.matcher(asset.getHoldingAssetDescription());
		
		if(m1.matches()){
			String coupon = m1.group(1);
			asset.setHoldingAssetCoupon(coupon, true);
		}
		if(m2.matches()){
			String maturity = m2.group(1);
			if(maturity.length() < 10){
				asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			}
			else{
				asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
			}
		}
		if(m3.matches()){
			String isin = m3.group(1);
			asset.setHoldingAssetISIN(isin);
		}
	}

	private static void getAssetType1(InvestmentAccount account, String rowText) {
		// Pattern p1
		Matcher m = p1.matcher(rowText);
		
		if(m.matches()){
			rowCount =1;
			String currency = m.group(1);
			String quantity = m.group(2);
			String number = m.group(3);
			String value = m.group(4);
			String fxValue = m.group(5);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetSubAccountNumber(number);
			asset.setHoldingAssetDescription(number);
			asset.setHoldingAssetCurrentValue(value, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetSubCategory(HoldingAsset.CATEGORY_CASH);
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(rowCount == 1 && currentAsset != null && rowText.matches("(-?\\d+\\.?\\d*)")){
			String fxRate = rowText;
			currentAsset.setHoldingAssetLastFxRate(fxRate, true);
			currentAsset = null;
			rowCount = 0;
		}
		
	}

	private static void getAssetType2(InvestmentAccount account, String rowText) {
		// Pattern p2, p3
		Matcher m1 = p2.matcher(rowText);
		Matcher m2 = p3.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String unitCost = m1.group(4);
			String unitPrice = m1.group(5);
			String fxValue = m1.group(6);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(unitCost, true);
			asset.setHoldingAssetIndicativePrice(unitPrice, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_ALTERNATE_TRADING_STRATEGY);
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(m2.matches() && rowCount == 2 && currentAsset != null){
			String description = m2.group(1);
			String fxRate = m2.group(2);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			
			currentAsset.setHoldingAssetLastFxRate(fxRate, true);
			currentAsset = null;
			rowCount = 0;
		}
		else if(rowCount == 1 && currentAsset != null){
			rowCount = 2;
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());
			
		}
		
	}

	private static void getAssetType3(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p4, p5, p6, p7, p8, p9
		
		Matcher m1 = p4.matcher(rowText);
		Matcher m2 = p5.matcher(rowText);
		Matcher m3 = p6.matcher(rowText);
		Matcher m4 = p7.matcher(rowText);
		Matcher m5 = p8.matcher(rowText);
		Matcher m6 = p9.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String unitCost = m1.group(5);
			String unitPrice = m1.group(6);
			String value = m1.group(7);
			String fxValue = m1.group(8);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(unitCost, true);
			asset.setHoldingAssetIndicativePrice(unitPrice, true);
			asset.setHoldingAssetCurrentValue(value, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetSubCategory(HoldingAsset.CATEGORY_BOND);
			asset.setBondNature(true);
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(m2.matches() && rowCount == 1 && currentAsset != null){
			rowCount = 2;
			String description = m2.group(1);
			String valueDate = m2.group(2);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetIndicativePriceDate(valueDate, Constants.DATEFORMAT_DD_SLASH_MM_SLASH_YYYY);
		}
		else if(m3.matches() && rowCount == 2 && currentAsset != null){
			rowCount = 3;
			String description = m3.group(1);
			String ytm = m3.group(2);
			String lastFxRate = m3.group(4);
			String accruedInterest = m3.group(5);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			
			currentAsset.setHoldingAssetYield(ytm, true);
			currentAsset.setHoldingAssetLastFxRate(lastFxRate, true);
			currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
		}
		else if(m4.matches() && rowCount == 2 && currentAsset != null){
			rowCount = 3;
			String description = m4.group(1);
			String lastFxRate = m4.group(3);
			String accruedInterest = m4.group(4);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetLastFxRate(lastFxRate, true);
			currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
			
		}
		else if(m5.matches() && rowCount == 2 && currentAsset != null){
			rowCount = 3;
			String description = m5.group(1);
			String ytm = m5.group(2);
			String accruedInterest = m5.group(3);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetYield(ytm, true);
			currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
			
		}
		else if(m6.matches() && rowCount == 2 && currentAsset != null){
			rowCount = 3;
			String description = m6.group(1);
			String accruedInterest = m6.group(2);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetAccruedInterest(accruedInterest, true);
			
		}
		else if(rowCount == 3 && currentAsset != null){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getAssetType4(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p10, p11, p12
		
		Matcher m1 = p10.matcher(rowText);
		Matcher m2 = p11.matcher(rowText);
		Matcher m3 = p12.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String unitCost = m1.group(4);
			String unitPrice = m1.group(5);
			String value = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(unitCost, true);
			asset.setHoldingAssetIndicativePrice(unitPrice, true);
			asset.setHoldingAssetCurrentValue(value, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetSubCategory(HoldingAsset.CATEGORY_UNIT_TRUST__MUTUAL_FUND);
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(m2.matches() && rowCount == 1 && currentAsset != null){
			rowCount = 2;
			String description = m2.group(1);
			String valueDate = m2.group(2);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetIndicativePriceDate(valueDate, Constants.DATEFORMAT_DD_SLASH_MM_SLASH_YYYY);
		}
		else if(m3.matches() && rowCount == 2 && currentAsset != null){
			rowCount = 3;
			String description = m3.group(1);
			String lastFxRate = m3.group(3);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetLastFxRate(lastFxRate, true);
		}
		
		else if(rowCount == 3 && currentAsset != null){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
		
	}

	private static void getAssetType5(InvestmentAccount account, String rowText) {
		// Pattern p13, p14
		
		Matcher m1 = p13.matcher(rowText);
		Matcher m2 = p14.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String number = m1.group(3);
			String description = m1.group(4);
			String accruedInterest = m1.group(5);
			String fxValue = m1.group(6);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(quantity, true);
			asset.setHoldingAssetSubAccountNumber(number);
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAccruedInterest(accruedInterest, true);
			asset.setHoldingAssetFxMarketValue(fxValue, true);
			asset.setHoldingAssetSubCategory(HoldingAsset.CATEGORY_COMMODITY);
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(m2.matches() && rowCount == 1 && currentAsset != null){
			String description = m2.group(1);
			String fxRate = m2.group(2);
			
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			currentAsset.setHoldingAssetLastFxRate(fxRate, true);
			currentAsset = null;
			rowCount = 0;
		}
		
	}


}
