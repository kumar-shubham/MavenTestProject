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
			boxTest = new PDFExtracter(getFile("investments", "lgt_inv_sg", "pdf"),"");
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
	
//	accounts (liquidity) - Type 1
	private static String regex1 = "(\\d{7}\\.\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex2 = "(([A-Z]{3}).*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	private static Pattern p1 = Pattern.compile(regex1);
	private static Pattern p2 = Pattern.compile(regex2);
	
//	fixed advances (credit) - Type 2
	private static String regex3 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) - (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) % (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex4 = "(-?(?:\\d*,)*\\d+\\.?\\d{2}) (.*) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	private static Pattern p3 = Pattern.compile(regex3);
	private static Pattern p4 = Pattern.compile(regex4);
	
//	bonds - Type 3
	private static String regex5 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex6 = "(.*) (\\d{8,9}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})(?: .*)?";
	private static String regex7 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p5 = Pattern.compile(regex5);
	private static Pattern p6 = Pattern.compile(regex6);
	private static Pattern p7 = Pattern.compile(regex7);
	
//	bond funds - Type 4
	private static String regex8 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex9 = "(.*) (\\d{8,9}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex10 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p8 = Pattern.compile(regex8);
	private static Pattern p9 = Pattern.compile(regex9);
	private static Pattern p10 = Pattern.compile(regex10);
	
//	equities - Type 5
	private static String regex11 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex12 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex13 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p11 = Pattern.compile(regex11);
	private static Pattern p12 = Pattern.compile(regex12);
	private static Pattern p13 = Pattern.compile(regex13);
	
//	structured products equities (equities) - Type 6
	private static String regex14 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex15 = "(.*) (\\d{6,9}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})?(?: .*)?";
	private static String regex16 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})";
	
	private static Pattern p14 = Pattern.compile(regex14);
	private static Pattern p15 = Pattern.compile(regex15);
	private static Pattern p16 = Pattern.compile(regex16);
	
//	precious metal accounts (commodities/metals) - Type 7
	private static String regex17 = "(\\d{7}\\.\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) n.a. (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	
	private static Pattern p17 = Pattern.compile(regex17);
	
//	futures (Derivatives) - Type 8
	private static String regex18 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex19 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex20 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p18 = Pattern.compile(regex18);
	private static Pattern p19 = Pattern.compile(regex19);
	private static Pattern p20 = Pattern.compile(regex20);
	
//	options (Derivatives) - Type 9
	private static String regex21 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex22 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex23 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p21 = Pattern.compile(regex21);
	private static Pattern p22 = Pattern.compile(regex22);
	private static Pattern p23 = Pattern.compile(regex23);
	
//	OTC (Derivatives) - Type 10
	private static String regex24 = "(.*) ([A-Z]{3})/([A-Z]{3}) (call|put) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{5}) (-?(?:\\d*,)*\\d+\\.?\\d{5})";
	private static String regex25 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d{5}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	private static Pattern p24 = Pattern.compile(regex24);
	private static Pattern p25 = Pattern.compile(regex25);
	
//	structured products - Type 11
	private static String regex26 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?:\\w)?(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) n.a. n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex27 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex28 = "(.*) (\\d{6,9}) n.a.( -?(?:\\d*,)*\\d+\\.?\\d{4})? (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})?(?: .*)?";
	private static String regex29 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p26 = Pattern.compile(regex26);
	private static Pattern p27 = Pattern.compile(regex27);
	private static Pattern p28 = Pattern.compile(regex28);
	private static Pattern p29 = Pattern.compile(regex29);
	
//	other derivatives (Derivatives) - Type 12
	private static String regex30 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?:\\w)?(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex31 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex32 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	private static Pattern p30 = Pattern.compile(regex30);
	private static Pattern p31 = Pattern.compile(regex31);
	private static Pattern p32 = Pattern.compile(regex32);
	
//	deliveries in/out - Type 13
	private static String regex33 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d{4}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	private static Pattern p33 = Pattern.compile(regex33);
	
//	buys/sells - Type 14
	private static String regex34 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d{4}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	private static String regex35 = "\\d{2}:\\d{2}:\\d{2} (.*) (\\d{6,9})";
	
	private static Pattern p34 = Pattern.compile(regex34);
	private static Pattern p35 = Pattern.compile(regex35);
	

	private static int rowCount = 0;
	private static HoldingAsset currentAsset = null;
	private static InvestmentTransaction currentTrans = null;	
	private static boolean[] typeList = new boolean[14];
	private static String transType = null;
	
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accNumEle = driver.findElement(By.xpath("//td[contains(text(), 'client number:')]"));
		String accNum = accNumEle.getText().trim();
		
		String regex = ".*client number: (\\d{7}\\.\\d{3})( .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accNum);
		
		
		if(m.matches()){
			accNum = m.group(1);
		}
		
		WebElement currencyEle = driver.findElement(By.xpath("//td[contains(text(), 'reference currency:')]"));
		String currency = currencyEle.getText().trim();
		
		regex = ".*reference currency: ([A-Z]{3})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(currency);
		
		if(m.matches()){
			currency = m.group(1);
		}
		
		WebElement stmtDateEle = driver.findElement(By.xpath("//td[contains(text(), 'statement of assets as of ')]"));
		String stmtDate = stmtDateEle.getText().trim();
		
		regex = ".*statement of assets as of (\\d{1,2}\\.\\d{2}\\.\\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);
		
		if(m.matches()){
			stmtDate = m.group(1);
		}
		
		String xpath = "//td[contains(text(), 'valuation of assets as at " + stmtDate + "')]";
		WebElement balanceEle = driver.findElement(By.xpath(xpath));
		String balance = balanceEle.getText().trim();
		
		regex = ".*valuation of assets as at (?:\\d{1,2}\\.\\d{2}\\.\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})( .*)?";
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
		account.setBillDate(ParserUtility.convertToPimoneyDate(stmtDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
		account.setAccountName("Portfolio - " + currency);
		response.addInvestmentAccount(account);
		
		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[contains(text(), 'accounts (liquidity)')]]"));
		
		setTypeTrue(1);
		
		String pageEndIdentifier = account.getAccountNumber().replace(".", "");
		
		for(WebElement row: rows){
			
			String rowText = row.getText().trim();
			
			rowText = rowText.replace("  ", "");
			
			System.out.println("RowText -> " + rowText);
			
			if(rowText.contains("total") || rowText.contains(pageEndIdentifier)){
				System.out.println("Not a valid row. skipping");
				currentAsset = null;
				currentTrans = null;
				rowCount = 0;
				continue;
			}
			
			if(rowText.contains("glossary")){
				System.out.println("End of statement");
				break;
			}
			
			if(rowText.equalsIgnoreCase("fixed advances (credit)")){
				setTypeTrue(2);
			}
			else if(rowText.equalsIgnoreCase("bonds")){
				setTypeTrue(3);
			}
			else if(rowText.equalsIgnoreCase("bond funds")){
				setTypeTrue(4);
			}
			else if(rowText.equalsIgnoreCase("equities")){
				setTypeTrue(5);
			}
			else if(rowText.equalsIgnoreCase("structured products equities (equities)")){
				setTypeTrue(6);
			}
			else if(rowText.equalsIgnoreCase("precious metal accounts (commodities/metals)")){
				setTypeTrue(7);
			}
			else if(rowText.equalsIgnoreCase("futures (Derivatives)")){
				setTypeTrue(8);
			}
			else if(rowText.equalsIgnoreCase("options (Derivatives)")){
				setTypeTrue(9);
			}
			else if(rowText.equalsIgnoreCase("OTC (Derivatives)")){
				setTypeTrue(10);
			}
			else if(rowText.equalsIgnoreCase("structured products")){
				setTypeTrue(11);
			}
			else if(rowText.equalsIgnoreCase("other derivatives (Derivatives)")){
				setTypeTrue(12);
			}
			else if(rowText.equalsIgnoreCase("deliveries in/out")){
				setTypeTrue(13);
			}
			else if(rowText.equalsIgnoreCase("buys")){
				setTypeTrue(14);
				transType = InvestmentTransaction.TRANSACTION_TYPE_BUY;
			}
			else if(rowText.equalsIgnoreCase("sells")){
				setTypeTrue(14);
				transType = InvestmentTransaction.TRANSACTION_TYPE_SELL;
			}
			
			if(typeList[0]){
				getType1(account, rowText);
			}
			else if(typeList[1]){
				getType2(account, rowText);
			}
			else if(typeList[2]){
				getType3(account, rowText);
			}
			else if(typeList[3]){
				getType4(account, rowText);
			}
			else if(typeList[4]){
				getType5(account, rowText);
			}
			else if(typeList[5]){
				getType6(account, rowText);
			}
			else if(typeList[6]){
				getType7(account, rowText);
			}
			else if(typeList[7]){
				getType8(account, rowText);
			}
			else if(typeList[8]){
				getType9(account, rowText);
			}
			else if(typeList[9]){
				getType10(account, rowText);
			}
			else if(typeList[10]){
				getType11(account, rowText);
			}
			else if(typeList[11]){
				getType12(account, rowText);
			}
			else if(typeList[12]){
				getType13(account, rowText);
			}
			else if(typeList[13]){
				getType14(account, rowText);
			}
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
	
	private static void getType1(InvestmentAccount account, String rowText) {
		// Pattern p1,p2
		
		System.out.println("Getting accounts (liquidity)");
		
		Matcher m1 = p1.matcher(rowText);
		Matcher m2 = p2.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String accountNumber = m1.group(1);
			String balance = m1.group(2);
			String fxRate = m1.group(3);
			String fxValue = m1.group(4);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetSubAccountNumber(accountNumber);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(balance));
			asset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(fxRate));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
			
			account.addAsset(asset);
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			
			String description = m2.group(1);
			String currency = m2.group(2);
			String accruedInterest = m2.group(3);
			String fxAccruedInterest = m2.group(4);
			
			currentAsset.setHoldingAssetDescription(description);
			currentAsset.setHoldingAssetCurrency(currency);
			currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			currentAsset = null;
			rowCount = 0;
		}
		
	}

	private static void getType2(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p3, p4
		
		System.out.println("Getting fixed advances (credit)");
		
		Matcher m1 = p3.matcher(rowText);
		Matcher m2 = p4.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			
			String currency = m1.group(1);
			String amount = m1.group(2);
			String description = m1.group(3);
			String startDate = m1.group(4);
			String maturityDate = m1.group(5);
			String coupon = m1.group(6);
			String fxRate = m1.group(7);
			String fxValue  = m1.group(8);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(amount));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetCoupon(ParserUtility.formatAmount(coupon));
			asset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(fxRate));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_LOAN);
			account.addAsset(asset);
			currentAsset = asset;
			
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			
			String accruedInterest = m2.group(1);
			String description = m2.group(2);
			String fxAccruedInterest = m2.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetAccruedInterest(ParserUtility.formatAmount(accruedInterest));
			currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			
			currentAsset = null;
			rowCount = 0;
			
		}
		
	}

	private static void getType3(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p5, p6, p7
		
		System.out.println("Getting bonds");
		
		Matcher m1 = p5.matcher(rowText);
		Matcher m2 = p6.matcher(rowText);
		Matcher m3 = p7.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String maturity = m1.group(5);
			String unitCost = m1.group(7);
			String currentPrice = m1.group(8);
			String fxValue = m1.group(9);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_BOND);
			asset.setBondNature(true);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String yield = m2.group(3);
			String lastFxRate = m2.group(5);
			String fxAccruedInterest = m2.group(6);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetYield(ParserUtility.formatAmount(yield));
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType4(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p8, p9, p10
		
		System.out.println("Getting bond funds");
		
		Matcher m1 = p8.matcher(rowText);
		Matcher m2 = p9.matcher(rowText);
		Matcher m3 = p10.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String unitCost = m1.group(5);
			String currentPrice = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_UNIT_TRUST__MUTUAL_FUND);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType5(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p11,p12, p13
		
		System.out.println("Getting equities");
		
		Matcher m1 = p11.matcher(rowText);
		Matcher m2 = p12.matcher(rowText);
		Matcher m3 = p13.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String unitCost = m1.group(5);
			String currentPrice = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_EQUITY);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType6(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p14,p15, p16
		
		System.out.println("Getting structured products equities (equities)");
		
		Matcher m1 = p14.matcher(rowText);
		Matcher m2 = p15.matcher(rowText);
		Matcher m3 = p16.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String maturity = m1.group(5);
			String unitCost = m1.group(6);
			String currentPrice = m1.group(7);
			String fxValue = m1.group(8);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setBondNature(true);
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_MULTI_ASSET__STRUCTURED_NOTES);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			String fxAccruedInterest = m2.group(5);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType7(InvestmentAccount account, String rowText) {
		// Pattern p17
		
		System.out.println("Getting precious metal accounts (commodities/metals)");
		
		Pattern p = Pattern.compile("(([A-Z]{3}).*)");
		
		Matcher m1 = p17.matcher(rowText);
		Matcher m2 = p.matcher(rowText);
		
		System.out.println("2^^^@@@@@@@@@@ :: " + m1.matches());
		System.out.println("2^^^@@@@@@@@@@ :: " + m2.matches());
		
		if(m1.matches()){
			rowCount = 1;
			String accountNumber = m1.group(1);
			String balance = m1.group(2);
			String fxRate = m1.group(3);
			String fxValue = m1.group(4);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetSubAccountNumber(accountNumber);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(balance));
			asset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(fxRate));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_COMMODITY);
			
			account.addAsset(asset);
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			
			String description = m2.group(1);
			String currency = m2.group(2);
			
			currentAsset.setHoldingAssetDescription(description);
			currentAsset.setHoldingAssetCurrency(currency);
			currentAsset = null;
			rowCount = 0;
		}
		
	}

	private static void getType8(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p18, p19, p20
		
		System.out.println("Getting futures (Derivatives)");
		
		Matcher m1 = p18.matcher(rowText);
		Matcher m2 = p19.matcher(rowText);
		Matcher m3 = p20.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String unitCost = m1.group(5);
			String currentPrice = m1.group(6);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_ALTERNATE_TRADING_STRATEGY);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType9(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p21,p22, p23
		
		System.out.println("Getting options (Derivatives)");
		
		Matcher m1 = p21.matcher(rowText);
		Matcher m2 = p22.matcher(rowText);
		Matcher m3 = p23.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String maturity = m1.group(5);
			String unitCost = m1.group(6);
			String currentPrice = m1.group(7);
			String fxValue = m1.group(8);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_ALTERNATE_TRADING_STRATEGY);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType10(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p24,p25
		
		System.out.println("Getting OTC (Derivatives)");
		
		Matcher m1 = p24.matcher(rowText);
		Matcher m2 = p25.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String description = m1.group(1);
			String currency = m1.group(2);
			String type = m1.group(4);
			String maturityDate = m1.group(5);
			String quantity = m1.group(6);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetOption(type);
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturityDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_ALTERNATE_TRADING_STRATEGY);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			String description = m2.group(1);
			String strikePrice = m2.group(2);
			String startDate = m2.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description);
			currentAsset.setHoldingAssetStrikePrice(ParserUtility.formatAmount(strikePrice));
			currentAsset.setHoldingAssetStartDate(ParserUtility.convertToPimoneyDate(startDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			currentAsset = null;
			rowCount = 0;
		}
		
		
	}

	private static void getType11(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p26, p27, p28,p29
		
		System.out.println("Getting structured products");
		
		Matcher m0 = p26.matcher(rowText);
		Matcher m1 = p27.matcher(rowText);
		Matcher m2 = p28.matcher(rowText);
		Matcher m3 = p29.matcher(rowText);
		
		System.out.println("2^^^^^^^^^^^^^^^^ :: " + m0.matches());
		System.out.println("2^^^^^^^^^^^^^^^^ :: " + m1.matches());
		
		if(m0.matches()){
			m1 = m0;
		}
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String maturity = m1.group(5);
			String unitCost = m1.group(6);
			String currentPrice = m1.group(7);
			String fxValue = m1.group(8);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetMaturityDate(ParserUtility.convertToPimoneyDate(maturity, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_MULTI_ASSET__STRUCTURED_NOTES);
			if(!m0.matches()){
				asset.setBondNature(true);
			}
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			String fxAccruedInterest = m2.group(5);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			currentAsset.setHoldingAssetFxAccruredInterest(ParserUtility.formatAmount(fxAccruedInterest));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType12(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p30, p31, p32
		
		System.out.println("Getting other derivatives (Derivatives)");
		
		Matcher m1 = p30.matcher(rowText);
		Matcher m2 = p31.matcher(rowText);
		Matcher m3 = p32.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String currency = m1.group(1);
			String quantity = m1.group(2);
			String description = m1.group(3);
			String isin = m1.group(4);
			String unitCost = m1.group(5);
			String currentPrice = m1.group(6);
			String fxValue = m1.group(7);
			
			HoldingAsset asset = new HoldingAsset();
			
			asset.setHoldingAssetAccountNumber(account.getAccountNumber());
			asset.setHoldingAssetCurrency(currency);
			asset.setHoldingAssetQuantity(ParserUtility.formatAmount(quantity));
			asset.setHoldingAssetDescription(description);
			asset.setHoldingAssetISIN(isin);
			asset.setHoldingAssetAverageUnitCost(ParserUtility.formatAmount(unitCost));
			asset.setHoldingAssetIndicativePrice(ParserUtility.formatAmount(currentPrice));
			asset.setHoldingAssetFxMarketValue(ParserUtility.formatAmount(fxValue));
			asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_MONEY_MARKET_N_DISCOUNTED_INSTRUMENT);
			account.addAsset(asset);
			
			currentAsset = asset;
			
		}
		else if(m2.matches() && currentAsset  != null && rowCount == 1){
			rowCount = 2;
			
			String description = m2.group(1);
			String security = m2.group(2);
			String lastFxRate = m2.group(4);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetSecurityId(security);
			currentAsset.setHoldingAssetLastFxRate(ParserUtility.formatAmount(lastFxRate));
			
			
		}
		else if(m3.matches() && currentAsset  != null && rowCount == 2){
			
			rowCount = 3;
			String description = m3.group(1);
			String valueDate = m3.group(3);
			
			description = currentAsset.getHoldingAssetDescription() + " " + description;
			
			currentAsset.setHoldingAssetDescription(description.trim());
			currentAsset.setHoldingAssetIndicativePriceDate(ParserUtility.convertToPimoneyDate(valueDate, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			
			
		}
		else if(rowCount > 2){
			String description = currentAsset.getHoldingAssetDescription() + " " + rowText;
			
			currentAsset.setHoldingAssetDescription(description.trim());
		}
		
	}

	private static void getType13(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p33
		
		Matcher m = p33.matcher(rowText);
		
		if(m.matches()){
			rowCount = 1;
			String date = m.group(1);
			String description = m.group(2);
			String units = m.group(3);
			String price = m.group(4);
			String currency = m.group(5);
			String amount = m.group(6);
			
			InvestmentTransaction transaction = new InvestmentTransaction();
			
			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			transaction.setDescription(description);
			transaction.setAssetQuantity(ParserUtility.formatAmount(units));
			transaction.setAssetUnitCost(ParserUtility.formatAmount(price));
			transaction.setCurrency(currency);
			transaction.setAmount(ParserUtility.formatAmount(amount));
			account.addTransaction(transaction);
			
			currentTrans = transaction;
		}
		else if(rowCount > 0 && currentTrans != null){
			
			String description = currentTrans.getDescription() + " " + rowText;
			
			currentTrans.setDescription(description.trim());
			
			if(description.toLowerCase().contains("put")){
				currentTrans.setType(InvestmentTransaction.TRANSACTION_TYPE_SELL);
			}
			else{
				currentTrans.setType(InvestmentTransaction.TRANSACTION_TYPE_BUY);
			}
		}
		
	}

	private static void getType14(InvestmentAccount account, String rowText) throws ParseException {
		// Pattern p34, p35
		
		Matcher m1 = p34.matcher(rowText);
		Matcher m2 = p35.matcher(rowText);
		
		if(m1.matches()){
			rowCount = 1;
			String date = m1.group(1);
			String description = m1.group(2);
			String isin = m1.group(3);
			String units  = m1.group(4);
			String price = m1.group(5);
			String currency = m1.group(6);
			String amount = m1.group(7);
			
			InvestmentTransaction  transaction = new InvestmentTransaction();
			
			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setTransactionDate(ParserUtility.convertToPimoneyDate(date, Constants.DATEFORMAT_DD_DOT_MM_DOT_YYYY));
			transaction.setDescription(description);
			transaction.setAssetISIN(isin);
			transaction.setAssetQuantity(ParserUtility.formatAmount(units));
			transaction.setAssetUnitCost(ParserUtility.formatAmount(price));
			transaction.setCurrency(currency);
			transaction.setAmount(ParserUtility.formatAmount(amount));
			transaction.setType(transType);
			account.addTransaction(transaction);
			
			currentTrans = transaction;
			
		}
		else if(m2.matches() && currentTrans  != null && rowCount == 1){
			rowCount = 2;
			String description = m2.group(1);
			
			description = currentTrans.getDescription() + " " + description;
			
			currentTrans.setDescription(description.trim());
		}
		else if(rowCount >1 && currentTrans != null){
			
			String description = currentTrans.getDescription() + " " + rowText;
			
			currentTrans.setDescription(description.trim());
		}
		
	}

	private static void setTypeTrue(int typeNum){
		
		for(int i = 0; i<typeList.length; i++){
			
			if(typeNum == (i+1)){
				typeList[i] = true;
			}
			else{
				typeList[i] = false;
			}
		}
	}

}
