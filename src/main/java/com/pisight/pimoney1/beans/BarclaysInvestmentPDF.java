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
import org.apache.pdfbox.exceptions.CryptographyException;
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

public class BarclaysInvestmentPDF {

	private static Logger logger = Logger.getLogger(MyClass.class);

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

		long start = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;


		PDFExtracter boxTest = null;
		try{
			boxTest = new PDFExtracter(getFile("investments", "barclays_inv_sg", "pdf"),"");
		}catch(CryptographyException e){
			if(e.getMessage().contains("The supplied password does not match")){
				System.out.println("The supplied password does not match");
			}
			throw e;
		}


		//		String page = boxTest.convertPDFToHTML(" ", "((\\d*,)*\\d+(\\.)\\d+)", "Desription", null, "(DR)", "Balance Carried Forward", "Description");

		String page = boxTest.convertPDFToHTML(" ");
		System.out.println(page);

		js.executeScript(page);
		try{
			scrapeStatement(driver);
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
	private static boolean isBond = false;
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		logger.info("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		logger.info("");

		WebElement accountEle = driver.findElement(By.xpath("//td[contains(text(), 'TOTAL VALUE (as of')]"));

		String accountText = accountEle.getText().trim();

		String accountRegex = "TOTAL VALUE \\(as of (\\d{1,2}\\-[A-z]{3}\\-\\d{2})\\) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)(?: .*)";
		Pattern accountP = Pattern.compile(accountRegex, Pattern.CASE_INSENSITIVE);
		Matcher accountM = accountP.matcher(accountText);

		String stmtDate = null;
		String balance = null;
		if(accountM.matches()){
			stmtDate = accountM.group(1);
			balance = accountM.group(2);
			stmtDate = ParserUtility.convertDateStringToPimoneyFormat(stmtDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY);
			balance = formatAmount(balance);
		}

		System.out.println("Stmt Date -> " + stmtDate);

		System.out.println("Balance -> " + balance);

		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'Reporting Currency:')]"));

		String currency = currencyEle.getText().trim();

		String currencyRegex = ".*Reporting Currency: (\\w{3})";
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
		account.setAccountName("Portfolio " + currency);


		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[contains(text(), 'INVESTMENT ASSETS')]]/preceding-sibling::tr"));

		// Type1 for "CASH AND SHORT-MATURITY BONDS"
		// Type2 for "BONDS > High Yield and Emerging Market" and  and 
		// Type3 for "EQUITIES"   
		// Type4 for "ALTERNATIVES"
		// Type5 for "INVESTMENT LIABILITIES"
		// Type6 for "TOTAL DERIVATIVES"
		// Type7 for "Investment Activity"
		// Type8 for "Cash Activity"
		// Type9 for "LOAN ACTIVITY"
		boolean isType1 = false;
		boolean isType2 = false;
		boolean isType3 = false;
		boolean isType4 = false;
		boolean isType5 = false;
		boolean isType6 = false;
		boolean isType7 = false;
		boolean isType8 = false;
		boolean isType9 = false;

		boolean isHoldings = false;
		boolean isDerivatives = false;

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			rowText = rowText.replace("−", "-");
			//			System.out.println("Row Text -> " + rowText);

			if(rowText.equalsIgnoreCase("Notes and Disclosures")){
				break;
			}
			if(rowText.contains("Holdings")){
				isHoldings = true;
				isDerivatives = false;
			}
			else if(rowText.contains("Derivative Exposure")){
				isHoldings = false;
				isDerivatives = true;
			}
			if(rowText.contains("> Cash") && isHoldings){
				rowCount = 0;
				currentAsset = null;
				isType1 = true;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("BONDS > High Yield and Emerging Market") && isHoldings){
				rowCount = 0;
				currentAsset = null;
				isType1 = false;
				isType2 = true;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("EQUITIES") && isHoldings){
				rowCount = 0;
				currentAsset = null;
				isType1 = false;
				isType2 = false;
				isType3 = true;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("ALTERNATIVES") && isHoldings){
				rowCount = 0;
				currentAsset = null;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = true;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("INVESTMENT LIABILITIES") && isHoldings){
				rowCount = 0;
				currentAsset = null;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = true;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("TOTAL DERIVATIVES") && isDerivatives){
				rowCount = 0;
				currentAsset = null;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = true;
				isType7 = false;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("Investment Activity")){
				rowCount = 0;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = true;
				isType8 = false;
				isType9 = false;
			}
			else if(rowText.contains("Cash Activity")){
				rowCount = 0;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = true;
				isType9 = false;
			}
			else if(rowText.contains("LOAN ACTIVITY")){
				rowCount = 0;
				isType1 = false;
				isType2 = false;
				isType3 = false;
				isType4 = false;
				isType5 = false;
				isType6 = false;
				isType7 = false;
				isType8 = false;
				isType9 = true;
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
				getAssetType6(rowText, account);
			}
			else if(isType7){
				getSecurityTransaction(rowText, account);
			}
			else if(isType8){
				getCashTransaction(rowText, account);
			}
			else if(isType9){
				getLoanTransaction(rowText, account);
			}
		}
		
		List<HoldingAsset> assets = account.getAssets();
		
		for(HoldingAsset asset: assets){
			filterDetailsFromDescription(asset);
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

	private static void getAssetType1(String rowText, InvestmentAccount account) {
		// TODO Auto-generated method stub
		System.out.print("Inside 1 ");
		String regex = "(\\d{8}\\.\\d{4})/[A-z]{3} (−?(?:\\d*,)*\\d+\\.?\\d*) ([A-z]{3})( −?(?:\\d*,)*\\d+\\.?\\d{4})? "
				+ "(−?(?:\\d*,)*\\d+\\.?\\d{2})( −?(?:\\d*,)*\\d+\\.?\\d*)? (−?(?:\\d*,)*\\d+\\.?\\d*)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			System.out.println("Matched 1");
			String subAccountNum = m.group(1);
			String quantity = m.group(2);
			String currency = m.group(3);
			String fxRate = m.group(4);
			String forexValue = m.group(5);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetSubAccountNumber(subAccountNum);
			asset.setHoldingAssetCurrentValue(formatAmount(quantity));
			asset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetCategory("Cash");
			asset.setHoldingAssetSubCategory("Accounts");
			account.addAsset(asset);
			if(StringUtils.isEmpty(account.getAccountNumber())){
				account.setAccountNumber(subAccountNum.substring(0, subAccountNum.indexOf(".")));
			}
		}

	}

	private static void getAssetType2(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("Inside 2 ");

		String lowerText = rowText.toLowerCase();
		if(lowerText.contains("definition") || lowerText.contains("page") || lowerText.contains("activity")
				|| lowerText.contains("equities") || lowerText.contains("investment") || lowerText.contains("asset class")
				|| lowerText.contains("alternative")){
			rowCount = 0;
			currentAsset = null;
		}

		String regex1 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3})?(?: (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})?)? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)"
				+ "( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)?( \\d{1,2})? \\(?\\d{1,2}\\.\\d\\)?";
		String regex2 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			rowCount = 1;
			System.out.println("Matched 2");
			String description = m1.group(1);
			String quantity = m1.group(2);
			String currency = m1.group(3);
			String unitCost = m1.group(4);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(5);
			}
			String currentUnitPrice = m1.group(6);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(7);
			}
			String forexValue = m1.group(8);
			String profit = m1.group(9);
			isBond = checkIfBond(rowText);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(currentUnitPrice));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetUnrealizedProfitLoss(formatAmount(profit));
			account.addAsset(asset);
			currentAsset = asset;
		}
		else if(isBond && currentAsset != null && rowCount == 1){
			rowCount++;
			getAdditionalBondDetail(rowText, currentAsset);
			currentAsset.setHoldingAssetCategory("Bonds");
			currentAsset.setHoldingAssetSubCategory("Bonds");
			isBond = false;
		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			rowCount++;
			String description = currentAsset.getHoldingAssetDescription() +  " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetCategory("Bond Funds");
			currentAsset.setHoldingAssetSubCategory("Bond Funds");
		}
		else if(currentAsset != null && rowCount > 0 ){
			String description = currentAsset.getHoldingAssetDescription() +  " " + rowText;
			currentAsset.setHoldingAssetDescription(description.trim());
			if(StringUtils.isEmpty(currentAsset.getHoldingAssetCategory())){
				currentAsset.setHoldingAssetCategory("Bond Funds");
				currentAsset.setHoldingAssetSubCategory("Bond Funds");
			}
			rowCount = 0;
		}
	}

	private static void getAssetType3(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		System.out.print("Inside 3 ");

		String lowerText = rowText.toLowerCase();
		if(lowerText.contains("definition") || lowerText.contains("page") || lowerText.contains("activity")
				|| lowerText.contains("equities") || lowerText.contains("investment") || lowerText.contains("asset class")
				|| lowerText.contains("alternative")){
			rowCount = 0;
			currentAsset = null;
		}

		String regex1 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3})?(?: (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})?)? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)"
				+ "( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)?( \\d{1,2})? \\(?\\d{1,2}\\.\\d\\)?";
		String regex2 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?)( .*)?";
		String regex3 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?)( .*)?";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);
		Matcher m3 = p3.matcher(rowText);

		if(m1.matches()){
			rowCount = 1;
			System.out.println("Matched 3");
			String description = m1.group(1);
			String quantity = m1.group(2);
			String currency = m1.group(3);
			String unitCost = m1.group(4);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(5);
			}
			String currentUnitPrice = m1.group(6);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(7);
			}
			String forexValue = m1.group(8);
			String profit = m1.group(9);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(currentUnitPrice));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetUnrealizedProfitLoss(formatAmount(profit));
			asset.setHoldingAssetCategory("Equities");
			asset.setHoldingAssetSubCategory("Equities");
			account.addAsset(asset);
			currentAsset = asset;
			isBond = checkIfBond(rowText);
		}
		else if(isBond && currentAsset != null && rowCount == 1){
			rowCount++;
			getAdditionalBondDetail(rowText, currentAsset);
			currentAsset.setHoldingAssetCategory("Bonds");
			currentAsset.setHoldingAssetSubCategory("Notes");
			isBond = false;
		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			String description = m2.group(1);
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			String fxRate = m2.group(3);
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
		}
		else if(m3.matches() && currentAsset != null && rowCount == 1){
			String description = m3.group(1);
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			String fxRate = m3.group(2);
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
		}

	}

	private static void getAssetType4(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		System.out.print("Inside 4 ");
		
		String lowerText = rowText.toLowerCase();
		if(lowerText.contains("definition") || lowerText.contains("page") || lowerText.contains("activity")
				|| lowerText.contains("equities") || lowerText.contains("investment") || lowerText.contains("asset class")
				|| lowerText.contains("alternative")){
			rowCount = 0;
			currentAsset = null;
		}
		
		String regex1 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3})?(?: (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})?)? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)"
				+ "( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)?( \\d{1,2})? \\(?\\d{1,2}\\.\\d\\)?";
		String regex2 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3}) (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?)";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			rowCount = 1;
			System.out.println("Matched 4");
			String description = m1.group(1);
			String quantity = m1.group(2);
			String currency = m1.group(3);
			String unitCost = m1.group(4);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(5);
			}
			String currentUnitPrice = m1.group(6);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(7);
			}
			String forexValue = m1.group(8);
			String profit = m1.group(9);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(currentUnitPrice));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetUnrealizedProfitLoss(formatAmount(profit));
			asset.setHoldingAssetCategory("ALTERNATIVES");
			asset.setHoldingAssetSubCategory("ALTERNATIVES");
			account.addAsset(asset);
			currentAsset = asset;
			isBond = checkIfBond(rowText);
		}
		else if(isBond && currentAsset != null && rowCount == 1){
			rowCount++;
			getAdditionalBondDetail(rowText, currentAsset);
			currentAsset.setHoldingAssetCategory("Bonds");
			currentAsset.setHoldingAssetSubCategory("Multi Asset Class/Structured Notes");
			isBond = false;
		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			String description = m2.group(1);
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			String fxRate = m2.group(4);
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetCategory("Alternative Trading Strategies");
			currentAsset.setHoldingAssetSubCategory("FX Options");
		}
		else if(currentAsset != null && rowCount > 0 ){
			String description = currentAsset.getHoldingAssetDescription() +  " " + rowText;
			System.out.println(description);
			currentAsset.setHoldingAssetDescription(description.trim());
			rowCount = 0;
			currentAsset = null;
		}
	}

	private static void getAssetType5(String rowText, InvestmentAccount account) {
		// TODO Auto-generated method stub
		System.out.print("Inside 5 ");
		String lowerText = rowText.toLowerCase();
		if(lowerText.contains("definition") || lowerText.contains("page") || lowerText.contains("activity")
				|| lowerText.contains("equities") || lowerText.contains("investment") || lowerText.contains("asset class")
				|| lowerText.contains("alternative")){
			rowCount = 0;
			currentAsset = null;
		}
		
		String regex = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3})?( \\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?)? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			System.out.println("Matched 5");
			String description = m.group(1);
			String quantity = m.group(2);
			String currency = m.group(3);
			String unitCost = m.group(4);
			String fxRate = m.group(5);
			String forexValue = m.group(6);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetCategory("Loan");
			asset.setHoldingAssetSubCategory("Loan");
			account.addAsset(asset);
		}
	}

	private static void getAssetType6(String rowText, InvestmentAccount account) {
		// TODO Auto-generated method stub
		System.out.println("Inside  6");
		
		String lowerText = rowText.toLowerCase();
		if(lowerText.contains("definition") || lowerText.contains("page") || lowerText.contains("activity")
				|| lowerText.contains("equities") || lowerText.contains("investment") || lowerText.contains("asset class")
				|| lowerText.contains("alternative")){
			rowCount = 0;
			currentAsset = null;
		}
		
		String regex1 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3})?(?: (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})?)? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) %?([A-z]{3})? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";
		String regex2 = "(.*) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( ?[A-z]{3}) (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?)";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);

		if(m1.matches()){
			rowCount = 1;
			System.out.println("Matched 6");
			String description = m1.group(1);
			String quantity = m1.group(2);
			String currency = m1.group(3);
			String unitCost = m1.group(4);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(5);
			}
			String currentUnitPrice = m1.group(6);
			if(StringUtils.isEmpty(currency)){
				currency = m1.group(7);
			}
			String forexValue = m1.group(8);
			String profit = m1.group(9);

			HoldingAsset asset = new HoldingAsset();
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetQuantity(formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetAverageUnitCost(formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(formatAmount(currentUnitPrice));
			asset.setHoldingAssetFxMarketValue(formatAmount(forexValue));
			asset.setHoldingAssetUnrealizedProfitLoss(formatAmount(profit));
			asset.setHoldingAssetCategory("DERIVATIVES");
			asset.setHoldingAssetSubCategory("DERIVATIVES");
			account.addAsset(asset);
			currentAsset = asset;
			isBond = checkIfBond(rowText);
		}
		else if(isBond && currentAsset != null && rowCount == 1){
			rowCount++;
			getAdditionalBondDetail(rowText, currentAsset);
			currentAsset.setHoldingAssetCategory("Bonds");
			currentAsset.setHoldingAssetSubCategory("Multi Asset Class/Structured Notes");
			isBond = false;
		}
		else if(m2.matches() && currentAsset != null && rowCount == 1){
			String description = m2.group(1);
			if(StringUtils.isNotEmpty(description)){
				description = currentAsset.getHoldingAssetDescription() + " " + description;
				currentAsset.setHoldingAssetDescription(description.trim());
			}
			String fxRate = m2.group(4);
			currentAsset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			currentAsset.setHoldingAssetCategory("Alternative Trading Strategies");
			currentAsset.setHoldingAssetSubCategory("FX Options");
		}
		else if(currentAsset != null && rowCount > 0 ){
			String description = currentAsset.getHoldingAssetDescription() +  " " + rowText;
			System.out.println(description);
			currentAsset.setHoldingAssetDescription(description.trim());
			rowCount = 0;
			currentAsset = null;
		}
	}

	private static void getAdditionalBondDetail(String rowText, HoldingAsset asset){

		if(asset == null){
			return;
		}
		String regex1 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( .*)?";
		String regex2 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";
		String regex3 = "(.* )?(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)";

		Pattern p1 = Pattern.compile(regex1);
		Pattern p2 = Pattern.compile(regex2);
		Pattern p3 = Pattern.compile(regex3);

		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);
		Matcher m3 = p3.matcher(rowText);

		String description = null;

		if(m1.matches()){
			description = m1.group(1);
			String fxRate = m1.group(3);
			String fxAccruedInterest = m1.group(4);

			asset.setHoldingAssetLastFxRate(formatAmount(fxRate));
			asset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
		}
		else if(m2.matches()){
			description = m2.group(1);
			String fxAccruedInterest = m2.group(2);

			asset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));

		}
		else if(m3.matches()){
			description = m3.group(1);
			String fxAccruedInterest = m3.group(2);

			asset.setHoldingAssetFxAccruredInterest(formatAmount(fxAccruedInterest));
		}
		else{
			description = rowText;
		}

		if(StringUtils.isNotEmpty(description)){
			description = asset.getHoldingAssetDescription() + " " + description;
			description = description.trim();
			asset.setHoldingAssetDescription(description);
		}
	}

	private static boolean checkIfBond(String rowText){
		if(StringUtils.isNotEmpty(rowText)){
			return (rowText.indexOf("%") != rowText.lastIndexOf("%")); 
		}
		return false;
	}

	static int rowCount = 0;
	static InvestmentTransaction currentTransaction = null;
	private static void getSecurityTransaction(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("Inside 7 ");
		String regex = "(.*) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) ?%?([A-z]{3})? "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{4}\\)?) ?%?([A-z]{3})? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) ?%?([A-z]{3})? \\d{9}";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(rowText);

		if(m.matches()){
			rowCount = 1;
			System.out.println("Matched 7");
			String description = m.group(1);
			String transDate = m.group(2);
			String valueDate = m.group(3);
			String quantity = m.group(4);
			String currency = m.group(5);
			String unitPrice = m.group(6);
			if(StringUtils.isEmpty(currency)){
				currency = m.group(7);
			}
			String amount = m.group(8);
			String type = null;

			amount = formatAmount(amount);

			if(amount.toLowerCase().contains("-")){
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			amount = amount.replace("-", "");
			quantity = quantity.replace("-", "");
			unitPrice = unitPrice.replace("-", "");

			InvestmentTransaction transaction = new InvestmentTransaction();

			transaction.setTransactionDate(ParserUtility.convertDateStringToPimoneyFormat(transDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setValuationDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setDescription(formatDescription(description));
			transaction.setType(type);
			transaction.setAssetQuantity(formatAmount(quantity));
			transaction.setCurrency(currency);
			transaction.setAssetUnitCost(formatAmount(unitPrice));
			transaction.setAmount(amount);
			if(StringUtils.isEmpty(m.group(7))){
				transaction.setAssetCategory("Bonds");
			}
			account.addTransaction(transaction);
			currentTransaction = transaction;
		}
		else if(rowCount == 1){
			rowCount = 0;
			String lowerText = rowText.toLowerCase();

			if(!lowerText.contains("page") && !lowerText.contains("continued") 
					&& !lowerText.contains("activity") && currentTransaction != null){
				String description = currentTransaction.getDescription() + " " + rowText;
				currentTransaction.setDescription(description.trim());
			}
			currentTransaction = null;
		}
	}


	static String transCurrency = null;
	static String subAccNum = null;
	private static void getCashTransaction(String rowText, InvestmentAccount account) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("Inside 8 ");
		String regex = "(.*) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)"
				+ "( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)? \\d{9}";
		String accRegex = "(\\d{8}\\.\\d{4})\\/([A-z]{3}) \\([A-z]{3}\\) \\([A-z]{3}\\)";
		Pattern p = Pattern.compile(regex);
		Pattern accP = Pattern.compile(accRegex);
		Matcher m = p.matcher(rowText);
		Matcher accM = accP.matcher(rowText);

		if(accM.matches()){
			subAccNum = accM.group(1);
			transCurrency = accM.group(2);
		}
		else if(m.matches()){
			System.out.println("Matched 8");
			String description = m.group(1);
			String transDate = m.group(2);
			String valueDate = m.group(3);
			String amount = m.group(4);
			String type = null;

			if(amount.contains("(")){
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}

			InvestmentTransaction transaction = new InvestmentTransaction();

			transaction.setTransactionDate(ParserUtility.convertDateStringToPimoneyFormat(transDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setDescription(formatDescription(description));
			transaction.setValuationDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setAmount(formatAmount(amount));
			transaction.setType(type);
			transaction.setCurrency(transCurrency);
			transaction.setSubAccountNumber(subAccNum);
			transaction.setAssetCategory("Cash");
			account.addTransaction(transaction);
		}

	}
	private static void getLoanTransaction(String rowText, InvestmentAccount account) throws ParseException {
		// TODO Auto-generated method stub
		System.out.print("Inside 9 ");
		String regex = "(.*) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (\\d{1,2}\\-[A-z]{3}\\-\\d{2}) (?:\\d{1,2}\\-[A-z]{3}\\-\\d{2}) to "
				+ "(?:\\d{1,2}\\-[A-z]{3}\\-\\d{2}) ((?:\\d*,)*\\d+\\.?\\d{4}) % (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) "
				+ "(\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)( \\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?)? (\\(?(?:\\d*,)*\\d+\\.?\\d{2}\\)?) ([A-z]{3}) \\d{9}";
		String accRegex = "(\\d{8}\\.\\d{4})\\/([A-z]{3}) \\([A-z]{3}\\) \\([A-z]{3}\\)";
		Pattern p = Pattern.compile(regex);
		Pattern accP = Pattern.compile(accRegex);
		Matcher m = p.matcher(rowText);
		Matcher accM = accP.matcher(rowText);

		if(accM.matches()){
			subAccNum = accM.group(1);
			transCurrency = accM.group(2);
		}
		else if(m.matches()){
			System.out.println("Matched 9");
			String description = m.group(1);
			String transDate = m.group(2);
			String valueDate = m.group(3);
			String amount = m.group(4);
			String type = null;

			if(amount.contains("(")){
				type = InvestmentTransaction.TRANSACTION_TYPE_DEBIT;
			}
			else{
				type = InvestmentTransaction.TRANSACTION_TYPE_CREDIT;
			}

			InvestmentTransaction transaction = new InvestmentTransaction();

			transaction.setTransactionDate(ParserUtility.convertDateStringToPimoneyFormat(transDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setDescription(description);
			transaction.setValuationDate(ParserUtility.convertDateStringToPimoneyFormat(valueDate, Constants.DATEFORMAT_DD_DASH_MMM_DASH_YY));
			transaction.setAmount(formatAmount(amount));
			transaction.setType(type);
			transaction.setCurrency(transCurrency);
			transaction.setSubAccountNumber(subAccNum);
			transaction.setAssetCategory("Loan");
			account.addTransaction(transaction);
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

	private static String formatDescription(String description){
		if(StringUtils.isEmpty(description)){
			return null;
		}

		String word = description.substring(0, description.indexOf(" "));
		if(description.lastIndexOf(word) > 0){
			description = description.substring(0, description.lastIndexOf(word)-1).trim();
		}
		return description;
	}
	
	private static void filterDetailsFromDescription(HoldingAsset asset) throws ParseException{
		if(asset == null){
			return;
		}
		String description = asset.getHoldingAssetDescription();
		String category = asset.getHoldingAssetCategory();
		String subCategory = asset.getHoldingAssetSubCategory();
		
		if(StringUtils.isEmpty(description)){
			return;
		}
		
		if(StringUtils.isNotEmpty(category) && StringUtils.isNotEmpty(subCategory) && 
				(category.toLowerCase().contains("options") || subCategory.toLowerCase().contains("options"))){
			String regex = ".* ((?:Call)|(?:Put)) ([A-z]{3}).*((?:Call)|(?:Put)) ([A-z]{3}) (\\d+\\.\\d+) .*(\\d{1,2}\\.\\d{1,2}\\.\\d{4}) .*(\\d{8,12})";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(description);
			
			if(m.matches()){
				String strikePrice = m.group(5);
				String maturity = m.group(6);
				String securityId = m.group(7);
				
				asset.setHoldingAssetStrikePrice(formatAmount(strikePrice));
				asset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
				asset.setHoldingAssetSecurityId(securityId);
			}
		}
		else if(!category.toLowerCase().contains("cash")){
			String regex1 = ".*(?:(\\d+\\.\\d+) ?%) .* (\\d{1,2} [A-z]{3} \\d{2}) .*([A-Z]{2}\\w{8}\\d{2}).*";
			String regex2 = ".* (\\d{1,2} [A-z]{3} \\d{2}) .*([A-Z]{2}\\w{8}\\d{2}).*";
			String regex3 = ".*(?:(\\d+\\.\\d+) ?%) .*([A-Z]{2}\\w{8}\\d{2}).*";
			String regex4 = ".*([A-Z]{2}\\w{8}\\d{2}).*";
			Pattern p1 = Pattern.compile(regex1);
			Pattern p2 = Pattern.compile(regex2);
			Pattern p3 = Pattern.compile(regex3);
			Pattern p4 = Pattern.compile(regex4);
			Matcher m1 = p1.matcher(description);
			Matcher m2 = p2.matcher(description);
			Matcher m3 = p3.matcher(description);
			Matcher m4 = p4.matcher(description);
			
			
			if(m1.matches()){
				String coupon = m1.group(1);
				String maturity = m1.group(2);
				String isin = m1.group(3);
				
				asset.setHoldingAssetCoupon(formatAmount(coupon));
				asset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturity, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
				asset.setHoldingAssetISIN(isin);
				System.out.println("Maturity 1 ->>>> " + maturity + " ->> " + asset.getHoldingAssetBondMaturityDate());
			}
			else if(m2.matches()){
				String maturity = m2.group(1);
				String isin = m2.group(2);
				
				asset.setHoldingAssetBondMaturityDate(ParserUtility.convertDateStringToPimoneyFormat(maturity, Constants.DATEFORMAT_DD_SPACE_MMM_SPACE_YY));
				asset.setHoldingAssetISIN(isin);
				System.out.println("Maturity 2 ->>>> " + maturity + " ->> " + asset.getHoldingAssetBondMaturityDate());
			}
			else if(m3.matches()){
				String coupon = m3.group(1);
				String isin = m3.group(2);
				
				asset.setHoldingAssetCoupon(formatAmount(coupon));
				asset.setHoldingAssetISIN(isin);
			}
			else if(m4.matches()){
				String isin = m4.group(1);
				
				asset.setHoldingAssetISIN(isin);
			}
			else{
				String symbol = description.substring(description.lastIndexOf(" "));
				asset.setHoldingAssetSecurityId(symbol);
			}
		}
	}
}
