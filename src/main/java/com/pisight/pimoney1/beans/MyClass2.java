package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class MyClass2 {

	private static Logger logger = Logger.getLogger(MyClass2.class);

	private static HashMap<String, String> transCategoryMap = new HashMap<String, String>();

	private static InvestmentTransaction lastTrans = null;

	static int total1 = 0;
	static int total2 = 0;
	static int total3 = 0;
	static int total4 = 0;
	static int total5 = 0;

	static{
		transCategoryMap.put("shares", "Equities");
	}


	public static void main(String[] args) throws Exception {
		
		PDFManager pm = new PDFManager();
		
		String text = pm.ToText();
		
		System.out.println("text ->>>>>> " + text);

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter2 boxTest = null;
		try{
			boxTest = new PDFExtracter2(getFile("HDFC", "4893XXXXXXXXXX32_14-07-2016", "pdf"),"");
		}catch(Exception e){
			if(e.getMessage().contains("Cannot decrypt PDF, the password is incorrect")){
				System.out.println("Cannot decrypt PDF, the password is incorrect");
			}
			throw e;
		}


		//		String page = boxTest.convertPDFToHTML(" ", "((\\d*,)*\\d+(\\.)\\d+)", "Desription", null, "(DR)", "Balance Carried Forward", "Description");

		String page = boxTest.convertPDFToHTML(" ");
		System.out.println(page);

		js.executeScript(page);
		try{
//			scrapeStatement(driver);
		}
		catch(Exception e){
			throw e;
		}
		finally{
			System.out.println("closing driver");


			driver.quit();

			System.out.println("Total Time Taken -> " + (System.currentTimeMillis()-start) + " ms");
		}


	}

	private static File getFile(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

		return p.toFile();
	}

	private static String getFileName(String dir, String name, String type) {
		// TODO Auto-generated method stub

		String fileName = dir + "/" + name + "." + type.toLowerCase();
		System.out.println("FFFFFFFFFFFFFFFFFFFFFF  ::: " + fileName);

		Path p = Paths.get(System.getProperty("user.home"), "Downloads/statements/statements", fileName);

		System.out.println("AAAAAAAAAAAA :: " + p.toString());

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

	private static HashMap<String, String> currencyMap = new HashMap<String, String>();

	static
	{
		currencyMap.put("AUSTRALIAN DOLLAR", "AUD");
		currencyMap.put("HONG KONG DOLLAR", "HKD");
		currencyMap.put("US DOLLAR", "USD");
		currencyMap.put("SINGAPORE DOLLAR", "SGD");
	}

	private static final List<String> keywords = new ArrayList<String>();
	static{
		keywords.add("Equity");
		keywords.add("Mutual Fund");
		keywords.add("EQUITY MUTUAL FUNDS OPEN ENDED");
		keywords.add("DEBT MUTUAL FUNDS OPEN ENDED");
		keywords.add("Mutual Fund");
		keywords.add("Mutual Fund (Demat)");
		keywords.add("Exchanged Traded Fund (ETF)");
		keywords.add("Portfolio Management Services (PMS)");
		keywords.add("Structured Products");
		keywords.add("Blended Products");
		keywords.add("Alternatives");
		keywords.add("Bond - Non-tradeable");
		keywords.add("Bond - Tradeable");
		keywords.add("Fixed Income");
		keywords.add("Term Deposits");
	}

	static HoldingAsset currentAsset = null;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		logger.info("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		logger.info("");

		WebElement dateEle = driver.findElement(By.xpath("//td[contains(text(), 'as at ')]"));

		String dateText = dateEle.getText().trim();

		String dateRegex = ".*as at (\\d{1,2} [A-Za-z]+ \\d{4})( .*)?";
		Pattern dateP = Pattern.compile(dateRegex);
		Matcher dateM = dateP.matcher(dateText);

		if(dateM.matches()){
			dateText = dateM.group(1);
			dateText = ParserUtility.convertToPimoneyDate(dateText, Constants.DATEFORMAT_DD_SPACE_MMMM_SPACE_YYYY);
		}

		System.out.println("Stmt Date -> " + dateText);

		WebElement accountEle = driver.findElement(By.xpath("//td[contains(text(), 'as at ')]/../following-sibling::tr[1]"));

		String accountNumber = accountEle.getText().trim();

		String accNumRegex = "(\\d\\.\\d{5}\\.\\d \\d{4})";
		Pattern accNumP = Pattern.compile(accNumRegex);
		Matcher accNumM = accNumP.matcher(accountNumber);

		if(accNumM.matches()){
			accountNumber = accNumM.group(1);
			accountNumber = ParserUtility.formatAmount(accountNumber);
		}

		System.out.println("Account Number -> " + accountNumber);

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYY_DASH_MM_DASH_DD);
		Date stmtDate = sdf.parse(dateText);

		sdf = new SimpleDateFormat(Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY);
		String stmtDate1 = sdf.format(stmtDate);



		WebElement balanceEle = driver.findElement(By.xpath("//td[contains(text(), 'Assets as of " + stmtDate1 + "')]"));

		String balance = balanceEle.getText().trim();

		String balRegex = "Assets as of \\d{1,2}\\.\\d{2}\\.\\d{4} (-?(?:\\d*,)*\\d+\\.?\\d*)";
		Pattern balP = Pattern.compile(balRegex);
		Matcher balM = balP.matcher(balance);

		if(balM.matches()){
			balance = balM.group(1);
			balance = ParserUtility.formatAmount(balance);
		}

		System.out.println("Balance -> " + balance);

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'Reference currency:')]"));

		String currency = currencyEle.getText().trim();

		String currencyRegex = ".*Reference currency: (\\w{3})";
		Pattern currencyP = Pattern.compile(currencyRegex);
		Matcher currencyM= currencyP.matcher(currency);

		if(currencyM.matches()){
			currency = currencyM.group(1);
			currency = ParserUtility.formatAmount(currency);
		}

		System.out.println("Currency -> " + currency);

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setAccountNumber(accountNumber);
		account.setCurrency(currency);
		account.setBalance(balance);
		account.setBillDate(dateText);
		account.setAccountName("Portfolio " + currency);


		List<WebElement> rows = driver.findElements(By.xpath("//td[contains(text(), 'Total Cash:')]/../preceding-sibling::tr[1]/following-sibling::tr"));

		boolean isType1 = false;
		boolean isType2 = false;
		boolean isType3 = false;
		boolean isType4 = false;
		boolean isType5 = false;
		boolean isType6 = false;
		boolean isType7 = false;

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			rowText = rowText.replace("−", "-");
			System.out.println("Row Text -> " + rowText);

			if(rowText.contains("Total Cash:")){
				isType1 = true;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
			}
			else if(rowText.contains("Total Currencies Related:")){
				isType1 = false;
				isType2 = true;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
			}
			else if(rowText.contains("Total Bonds:")){
				isType1 = false;
				isType2 = false;
				isType3 = true;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
			}
			else if(rowText.contains("Total Equities:")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = true;
				isType5 = false;
				isType6 = false;
				isType7 = false;
			}
			else if(rowText.contains("Total Commodities/metals:")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = true;
				isType6 = false;
				isType7 = false;
			}
			else if(rowText.contains("Account Movements in ")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = true;
				isType7 = false;
			}
			else if(rowText.contains("Security Movements in ")){
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = true;
			}

			if(isType1){
				getAssetType1(rowText, account);
			}
			else if(isType2){
				getAssetType2(rowText, account);
			}
			else if(isType3){
				getAssetType3(rowText, account);
			}
			else if(isType4){
				getAssetType4(rowText, account);
			}
			else if(isType5){
				getAssetType5(rowText, account);
			}
			else if(isType6){
				getCashTransaction(rowText, account);
			}
			else if(isType7){
				getSecurityTransaction(rowText, account);
			}
		}


		ObjectMapper mapper = new ObjectMapper();
		Path p = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p.toString()), account);
			//			String x = mapper.writeValueAsString(accounts);
			//			JSONObject json = mapper.readValue(new File(p.toString()), JSONObject.class);
			//			String xml = XML.toString(json);
			//			System.out.println(xml);
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

	static int type1RowCount = 0;
	private static void getAssetType1(String rowText, InvestmentAccount account) {
		// TODO Auto-generated method stub
		String regex = "(-?(?:\\d*,)*\\d+\\.?\\d*) ([A-Za-z]{3}) (\\d\\.\\d{5}\\.\\d \\d{4})( -?(?:\\d*,)*\\d+\\.?\\d*)?"
				+ "( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			String quantity = m.group(1);
			String currency = m.group(2);
			String subAccountNum = m.group(3);
			String totalValue = m.group(5);
			String forexValue = m.group(6);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetSubAccountNumber(subAccountNum);
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(totalValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetCategory("Cash");
			asset.setHoldingAssetSubCategory("Accounts");
			currentAsset = asset;
			account.addAsset(asset);
			type1RowCount = 1;
		}
		else if(type1RowCount == 1 && currentAsset != null){
			currentAsset.setHoldingAssetDescription(rowText);
			type1RowCount = 0;
			currentAsset = null;
		}

	}

	private static void getAssetType2(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		String regex = "(-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\d{1,2}\\.\\d{2}\\.\\d{2}) ([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			String quantity = m.group(1);
			String description = m.group(2);
			String maturityDate = m.group(3);
			String currency = m.group(4);
			String unitCost = m.group(5);
			String currentUnitPrice = m.group(6);
			String totalCurrentValue = m.group(7);
			String forexValue = m.group(8);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetBondMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentUnitPrice));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(totalCurrentValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetCategory("Currency");
			asset.setHoldingAssetSubCategory("Currency");
			account.addAsset(asset);
		}
	}

	private static void getAssetType3(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		// First Line - With Date
		String regex1 = "([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) ((?:\\d{1,2}\\.\\d{2}\\.\\d{2})|(?:MaxDate)) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		// First Line - W/O Date
		String regex2 = "([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*)"
				+ " (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		// Non USD - Rated
		String regex3 = "(.*) (\\w{2,4}) (-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)%";
		// Non USD - Non Rated
		String regex4 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)%";
		// USD - Rated
		String regex5 = "(.*) (\\w{2,4}) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)%";
		// USD - Non Rated
		String regex6 = "(.* )?(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)%";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);

		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String maturityDate = m1.group(4);
			String unitCost = m1.group(5);
			String currentUnitValue = m1.group(6);
			String currentTotalValue = m1.group(7);
			String forexValue = m1.group(8);
			String profit = m1.group(9);

			if(StringUtils.isNotEmpty(maturityDate) && maturityDate.contains("MaxDate")){
				maturityDate = null;
			}

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetBondMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentUnitValue));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(currentTotalValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetProfit(ParserUtility.formatAmount(profit));
			asset.setHoldingAssetCategory("Bonds");
			asset.setHoldingAssetSubCategory("Bonds");
			currentAsset = asset;
			account.addAsset(asset);

		}
		else if(m2.matches()){
			String currency = m2.group(1);
			String quantity = m2.group(2);
			String description = m2.group(3);
			String unitCost = m2.group(4);
			String currentUnitValue = m2.group(5);
			String currentTotalValue = m2.group(6);
			String forexValue = m2.group(7);
			String profit = m2.group(8);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentUnitValue));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(currentTotalValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetProfit(ParserUtility.formatAmount(profit));
			asset.setHoldingAssetCategory("Bonds");
			asset.setHoldingAssetSubCategory("Bonds");
			currentAsset = asset;
			account.addAsset(asset);
		}
		else if(currentAsset != null){

			String assetCurrency = currentAsset.getHoldingAssetCurrency();
			String accountCurrency = account.getCurrency();

			// If the currency is equal to account currency then the set of regex is different for the second line
			if(assetCurrency.equalsIgnoreCase(accountCurrency)){
				Pattern p5 = Pattern.compile(regex5);
				Pattern p6 = Pattern.compile(regex6);

				Matcher m5 = p5.matcher(rowText);
				Matcher m6 = p6.matcher(rowText);

				String description = null;
				String yield = null;
				String accruedInterest = null;

				if(m5.matches()){
					description = m5.group(1);
					yield = m5.group(3);
					accruedInterest = m5.group(4);

				}
				else if(m6.matches()){
					description = m6.group(1);
					yield = m6.group(2);
					accruedInterest = m6.group(3);
				}

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description.trim());
				currentAsset.setHoldingAssetBondYield(ParserUtility.formatAmount(yield));
				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));
			}
			else{
				Pattern p3 = Pattern.compile(regex3);
				Pattern p4 = Pattern.compile(regex4);

				Matcher m3 = p3.matcher(rowText);
				Matcher m4 = p4.matcher(rowText);

				String description = null;
				String forexRate = null;
				String yield = null;
				String accruedInterest = null;
				String fxAccruedInterest = null;

				if(m3.matches()){
					description = m3.group(1);
					forexRate = m3.group(3);
					yield = m3.group(4);
					accruedInterest = m3.group(5);
					fxAccruedInterest = m3.group(6);

				}
				else if(m4.matches()){
					description = m4.group(1);
					forexRate = m4.group(2);
					yield = m4.group(3);
					accruedInterest = m4.group(4);
					fxAccruedInterest = m4.group(5);
				}

				description = currentAsset.getHoldingAssetDescription() + " " + description;

				currentAsset.setHoldingAssetDescription(description.trim());
				currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(forexRate));
				currentAsset.setHoldingAssetBondYield(ParserUtility.formatAmount(yield));
				currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));
				currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			}

			currentAsset = null;
		}


	}

	private static void getAssetType4(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub

		// First Line - With Date
		String regex1 = "(-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\d{1,2}\\.\\d{2}\\.\\d{2}) ([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*)( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "(-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		// First Line - W/O Date
		String regex2 = "(-?(?:\\d*,)*\\d+\\.?\\d*) (.*) ([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)"
				+ "( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";
		// With 2 Amounts
		String regex3 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)%";
		// With 1 Amount
		String regex4 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);

		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){

			String quantity = m1.group(1);
			String description = m1.group(2);
			String maturityDate = m1.group(3);
			String currency = m1.group(4);
			String unitCost = m1.group(5);
			String currentUnitValue = m1.group(6);
			String currentTotalValue = m1.group(7);
			String forexValue = m1.group(8);
			String profit = m1.group(9);

			if(currency.equalsIgnoreCase(account.getCurrency())){
				currentTotalValue = forexValue;
			}

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetBondMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentUnitValue));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(currentTotalValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetProfit(ParserUtility.formatAmount(profit));
			asset.setHoldingAssetCategory("Equity");
			asset.setHoldingAssetSubCategory("Equity");
			currentAsset = asset;
			account.addAsset(asset);
		}
		else if(m2.matches()){
			String quantity = m2.group(1);
			String description = m2.group(2);
			String currency = m2.group(3);
			String unitCost = m2.group(4);
			String currentUnitValue = m2.group(5);
			String currentTotalValue = m2.group(6);
			String forexValue = m2.group(7);
			String profit = m2.group(8);

			if(currency.equalsIgnoreCase(account.getCurrency())){
				currentTotalValue = forexValue;
			}

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentUnitValue));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(currentTotalValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			asset.setHoldingAssetProfit(ParserUtility.formatAmount(profit));
			asset.setHoldingAssetCategory("Equity");
			asset.setHoldingAssetSubCategory("Equity");
			currentAsset = asset;
			account.addAsset(asset);
		}
		else if(currentAsset != null){
			Pattern p3 = Pattern.compile(regex3);
			Pattern p4 = Pattern.compile(regex4);

			Matcher m3 = p3.matcher(rowText);
			Matcher m4 = p4.matcher(rowText);

			if(m3.matches()){
				String description = m3.group(1);
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description);
				if(currentAsset.getHoldingAssetCurrency().equalsIgnoreCase(account.getCurrency())){
					String accruedInterest = m3.group(2);
					currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));
				}
				else{
					String fxRate = m3.group(2);
					currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(fxRate));
				}
			}
			else if(m4.matches()){
				String description = m4.group(1);
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description);
			}

			currentAsset = null;
		}

	}

	private static void getAssetType5(String rowText, InvestmentAccount account) {
		// TODO Auto-generated method stub
		String regex = "(-?(?:\\d*,)*\\d+\\.?\\d*) (\\d\\.\\d{5}\\.\\d \\d{4}) ([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*)"
				+ "( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			String quantity = m.group(1);
			String subAccountNum = m.group(2);
			String currency = m.group(3);
			String cost = m.group(4);
			String marketValue = m.group(5);
			String forexValue = m.group(6);

			HoldingAsset asset = new HoldingAsset();

			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetSubAccountNumber(subAccountNum);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetCost(ParserUtility.formatAmount(cost));
			asset.setHoldingAssetCurrentValue(ParserUtility.formatAmount(marketValue));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(forexValue));
			account.addAsset(asset);
		}

	}

	static String lastBalance = null;
	static String transCurrency = null;
	private static void getCashTransaction(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		String regex = "(\\d{1,2}\\.\\d{2}\\.\\d{2}) \\d{9} (.*) (\\d{1,2}\\.\\d{2}\\.\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(rowText.contains("Balance carried forward")){
			lastBalance = rowText.substring(rowText.lastIndexOf(" "));
		}
		else if(rowText.contains("Account Movements in")){
			transCurrency = rowText.substring(rowText.lastIndexOf(" "));
		}

		if(m.matches()){
			String transDate = m.group(1);
			String description = m.group(2);
			String valueDate = m.group(3);
			String amount = m.group(4);
			String runningBalance = m.group(5);
			String type = null;

			runningBalance = ParserUtility.formatAmount(runningBalance);
			lastBalance = ParserUtility.formatAmount(lastBalance);
			
			float runningBalanceD = Float.parseFloat(runningBalance);
			float lastBalanceD = Float.parseFloat(lastBalance);

			if(runningBalanceD > lastBalanceD){
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			lastBalance = runningBalance;

			InvestmentTransaction transaction = new InvestmentTransaction();

			transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			transaction.setDescription(description);
			transaction.setValuationDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			transaction.setAmount(ParserUtility.formatAmount(amount));
			transaction.setType(type);
			transaction.setCurrency(transCurrency);
			transaction.setAssetCategory("Cash");
			account.addTransaction(transaction);
		}

	}

	private static void getSecurityTransaction(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		String regex = "(\\d{1,2}\\.\\d{2}\\.\\d{2}) (\\d{1,2}\\.\\d{2}\\.\\d{2}) \\d{9} (.*) ([A-Za-z]+) (-?(?:\\d*,)*\\d+\\.?\\d*) "
				+ "([A-Za-z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*)%?( -?(?:\\d*,)*\\d+\\.?\\d*)? (-?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(rowText.contains("Security Movements in")){
			transCurrency = rowText.substring(rowText.lastIndexOf(" "));
		}
		
		if(m.matches()){
			String transDate = m.group(1);
			String valueDate = m.group(2);
			String description = m.group(3);
			String type = m.group(4);
			String quantity = m.group(5);
			String currency = m.group(6);
			String unitPrice = m.group(7);
			String fxPrice = m.group(9);
			String currentValue = null;
			
			if(currency.equalsIgnoreCase(account.getCurrency())){
				currentValue = fxPrice;
			}
			
			if(type.toLowerCase().contains("buy")){
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}
			
			InvestmentTransaction transaction = new InvestmentTransaction();
			
			transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(transDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			transaction.setValuationDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YY));
			transaction.setDescription(description);
			transaction.setType(type);
			transaction.setAssetQuantity(ParserUtility.formatAmount(quantity));
			transaction.setCurrency(currency);
			transaction.setAssetUnitCost(ParserUtility.formatAmount(unitPrice));
			account.addTransaction(transaction);
		}
		
		
	}
}
