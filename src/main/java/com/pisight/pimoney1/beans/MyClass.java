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
			boxTest = new PDFExtracter(getFile("investments/new3", "CA HK", "pdf"),"");
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

	private static String regex1 = "(-?\\d+\\.?\\d*-?) (.*) ((?:-?\\d+\\.?\\d{3}-?)|(?:-?\\d+-?))(?:\\.[A-Z]{2})? (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?) ([A-Z]{3}) (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?)";
	private static Pattern p1 = Pattern.compile(regex1);

	private static String regex2 = "([A-Z]{3}) (-?\\d+\\.?\\d*-?) (.*) (-?\\d+\\.?\\d{3}-?) (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?) ([A-Z]{3}) (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?) (-?\\d+\\.?\\d*-?)";
	private static String regex3 = "(.* )?(\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)";
	private static Pattern p2 = Pattern.compile(regex2);
	private static Pattern p3 = Pattern.compile(regex3);

	private static String regex4 = "([A-Z]{3}) (\\d+\\.?\\d*-?) (.*) (\\d+\\.?\\d*-?) [A-Z]{3} (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)";
	private static Pattern p4 = Pattern.compile(regex4);

	private static String  regex5 = "([A-Z]{3}) (\\d+\\.?\\d*-?) (.*) (\\d+)( \\d+\\.?\\d*-?)? [A-Z]{3} (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)( \\d+\\.?\\d*-?)?";
	private static Pattern p5 = Pattern.compile(regex5);

	private static String regex6 = "([A-Z]{3}) (\\d+\\.?\\d*-?) (.*) (\\d{1,2}\\.\\d{1,2}\\.\\d{2})( \\d+\\.?\\d*-?)? [A-Z]{3} (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)( \\d+\\.?\\d*-?)?";
	private static String regex7 = "(\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)";
	private static Pattern p6 = Pattern.compile(regex6);
	private static Pattern p7 = Pattern.compile(regex7);


	private static HoldingAsset currentAsset = null;
	private static int rowCount = 0;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accountNumberEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Portfolio No.')]]"));
		String accountNumber = accountNumberEle.getText().trim();

		String regex = "(?:.* )?Portfolio No. ?(\\d{7})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accountNumber);

		if(m.matches()){
			accountNumber = m.group(1);
		}

		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'PORTFOLIO VALUATION AS OF')]]"));
		String stmtDate = stmtDateEle.getText().trim();

		regex = "(?:.* )?PORTFOLIO VALUATION AS OF (\\d{1,2}\\.\\d{1,2}\\.\\d{2,4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);

		if(m.matches()){
			stmtDate = m.group(1);
		}

		WebElement balanceEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'NET ASSETS') and not(contains(text(), 'SUMMARY'))]]"));
		String balance = balanceEle.getText().trim();
		balance = balance.replace("’", "");
		String currency = null;

		regex = "(?:.* )?NET ASSETS ([A-Z]{3}) (\\d+\\.?\\d*-?)( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(balance);

		if(m.matches()){
			currency = m.group(1);
			balance = m.group(2);
		}

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setAccountNumber(accountNumber);
		account.setCurrency(currency);
		account.setBalance(formatAmount(balance));
		account.setAvailableBalance(formatAmount(balance));
		account.setBillDate(stmtDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
		response.addInvestmentAccount(account);

		List<WebElement> rows = driver.findElements(By.tagName("tr"));

		boolean isType1 = false;
		boolean isType2 = false;
		boolean isType3 = false;
		boolean isType4 = false;
		boolean isType5 = false;

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			rowText = rowText.replace("’", "");
			System.out.println("RowText - > " + rowText);

			if(rowText.contains("CA INDOSUEZ") || rowText.contains("Page")){
				currentAsset = null;
				rowCount = 0;
				continue;
			}

			if(rowText.matches("SHARES (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)")){
				isType1 = true;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				continue;
			}
			else if(rowText.matches("BONDS/NOTES (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)")){
				isType1 = false;
				isType2 = true;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				continue;
			}
			else if(rowText.matches("PRECIOUS METALS (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)")){
				isType1 = false;
				isType2 = false;
				isType3 = true;
				isType4 = false;
				isType5 = false;
				continue;
			}
			else if(rowText.matches("CURRENT ACCOUNT (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = true;
				isType5 = false;
				continue;
			}
			else if(rowText.matches("LOANS (\\d+\\.?\\d*-?) (\\d+\\.?\\d*-?)")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = true;
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


	private static void getAssetType1(InvestmentAccount account, String rowText) {
		// Pattern p1
		Matcher m = p1.matcher(rowText);

		if(m.matches()){
			String quantity = m.group(1);
			String description = m.group(2);
			String unitCost = m.group(4);
			String unitPrice = m.group(5);
			String currency = m.group(6);
			String value = m.group(7);
			String fxValue = m.group(8);

			HoldingAsset asset =  new HoldingAsset();

			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(unitPrice));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_EQUITY);
			account.addAsset(asset);

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
			String unitCost = m1.group(5);
			String unitPrice = m1.group(6);
			String value = m1.group(8);
			String fxValue = m1.group(9);

			HoldingAsset asset =  new HoldingAsset();

			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(unitPrice));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_BOND);
			asset.setBondNature(true);
			account.addAsset(asset);
			currentAsset = asset;

		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			String description = m2.group(1);
			String accruedInterest = m2.group(2);
			String fxAccruedInterest = m2.group(3);

			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}

			currentAsset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
			currentAsset = null;
			rowCount = 0;
		}

	}

	private static void getAssetType3(InvestmentAccount account, String rowText) {
		// Pattern p4
		Matcher m = p4.matcher(rowText);

		if(m.matches()){
			String currency = m.group(1);
			String quantity = m.group(2);
			String description = m.group(3);
			String unitPrice = m.group(4);
			String value = m.group(5);
			String fxValue = m.group(6);

			HoldingAsset asset =  new HoldingAsset();

			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetIndicativePrice(formatAmount(unitPrice));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_COMMODITY);
			account.addAsset(asset);

		}

	}

	private static void getAssetType4(InvestmentAccount account, String rowText) {
		// Pattern p5
		Matcher m = p5.matcher(rowText);

		if(m.matches()){
			String currency = m.group(1);
			String quantity = m.group(2);
			String description = m.group(3);
			String subAccNum = m.group(4);
			String fxRate = m.group(5);
			String value = m.group(6);
			String fxValue = m.group(7);

			HoldingAsset asset =  new HoldingAsset();

			description = description + " " + subAccNum;

			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description );
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetSubAccountNumber(subAccNum);
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
			account.addAsset(asset);

		}


	}

	private static void getAssetType5(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p6, p7
		Matcher m1 = p6.matcher(rowText);
		Matcher m2 = p7.matcher(rowText);

		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String maturity = m1.group(4);
			String coupon = m1.group(5);
			String value = m1.group(6);
			String fxValue = m1.group(7);

			HoldingAsset asset =  new HoldingAsset();

			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetCurrentValue(formatAmount(value));
			asset.setHoldingAssetFxMarketValue(formatAmount(fxValue));
			asset.setHoldingAssetMaturityDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			asset.setHoldingAssetCoupon(formatAmount(coupon));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_LOAN);
			account.addAsset(asset);
			currentAsset = asset;

		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			String accruedInterest = m2.group(1);
			String fxAccruedInterest = m2.group(2);

			currentAsset.setHoldingAssetAccruedInterest(formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
			currentAsset = null;
			rowCount = 0;
		}

	}

	private static void getDetailsFromAssetDescription(HoldingAsset asset) throws ParseException {
		// TODO Auto-generated method stub
		String regex1 = "(?:.* )?(\\d+\\.?\\d*) %( .*)?";
		String regex2 = "(?:.* )?(\\d{1,2}\\.\\d{1,2}\\.\\d{2})( .*)?";
		String regex3 = "(?:.*)?-(\\d{1,2}\\.\\d{1,2}\\.\\d{2})( .*)?";

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
			String date = m2.group(1);
			if(asset.getHoldingAssetCategory().equals(HoldingAsset.CATEGORY_LOAN)){
				asset.setHoldingAssetStartDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			}
			else{
				asset.setHoldingAssetMaturityDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			}

		}
		if(m3.matches()){
			String date = m3.group(1);
			if(asset.getHoldingAssetCategory().equals(HoldingAsset.CATEGORY_LOAN)){
				asset.setHoldingAssetStartDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			}
			else{
				asset.setHoldingAssetMaturityDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY);
			}
		}
	}


	private static String formatAmount(String amount){

		if(StringUtils.isEmpty(amount)){
			return null;
		}

		if(amount.contains("-")){
			amount = amount.replace("-", "");
			amount = "-"+amount.trim();
		}

		return amount;
	}

}
