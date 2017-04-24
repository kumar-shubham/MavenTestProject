package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
//	accounts (liquidity)
	private static String regex1 = "(\\d{7}\\.\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex2 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	Pattern p1 = Pattern.compile(regex1);
	Pattern p2 = Pattern.compile(regex2);
	
//	fixed advances (credit)
	private static String regex3 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) - (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) % (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex4 = "(-?(?:\\d*,)*\\d+\\.?\\d{2}) (.*) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
	Pattern p3 = Pattern.compile(regex3);
	Pattern p4 = Pattern.compile(regex4);
	
//	bonds
	private static String regex5 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex6 = "(.*) (\\d{8,9}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})(?: .*)?";
	private static String regex7 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	Pattern p5 = Pattern.compile(regex5);
	Pattern p6 = Pattern.compile(regex6);
	
//	bond funds
	private static String regex8 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex9 = "(.*) (\\d{8,9}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex10 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
	Pattern p7 = Pattern.compile(regex7);
	Pattern p8 = Pattern.compile(regex8);
	
//	equities
	private static String regex11 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex12 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex13 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
//	structured products equities (equities)
	private static String regex14 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex15 = "(.*) (\\d{6,9}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})?(?: .*)?"";
	private static String regex16 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})";
	
//	precious metal accounts (commodities/metals)
	private static String regex17 = "(\\d{7}\\.\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{3}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) n.a. (?: )* (-?(?:\\d*,)*\\d+\\.?\\d{2}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	
//	futures (Derivatives)
	private static String regex18 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex19 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex20 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
//	options (Derivatives)
	private static String regex21 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex22 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex23 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
//	OTC (Derivatives)
	private static String regex24 = "(.*) ([A-Z]{3})/([A-Z]{3}) (call|put) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{5}) (-?(?:\\d*,)*\\d+\\.?\\d{5})";
	private static String regex25 = "(.*) (-?(?:\\d*,)*\\d+\\.?\\d{5}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
//	structured products
	private static String regex26 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?:\\w)?(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) n.a. n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) ";
	private static String regex27 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (\\d{1,2}\\.\\d{2}\\.\\d{4}) n.a. (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) ";
	private static String regex28 = "(.*) (\\d{6,9}) n.a.( -?(?:\\d*,)*\\d+\\.?\\d{4})? (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})?(?: .*)?";
	private static String regex29 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
//	other derivatives (Derivatives)
	private static String regex30 = "([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d*) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (?:\\w)?(?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) % (-?(?:\\d*,)*\\d+\\.?\\d{2}) %";
	private static String regex31 = "(.*) (\\d{6,9}) (-?(?:\\d*,)*\\d+\\.?\\d{4}) (-?(?:\\d*,)*\\d+\\.?\\d{4})(?: .*)?";
	private static String regex32 = "(.*) (\\d{1,2}\\.\\d{2}\\.\\d{4}) (\\d{1,2}\\.\\d{2}\\.\\d{4})(?: .*)?";
	
//	deliveries in/out
	private static String regex33 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (.*) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d{4}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	
//	buys/sells
	private static String regex34 = "(\\d{1,2}\\.\\d{2}\\.\\d{4}) (.*) (\\w{12}) (-?(?:\\d*,)*\\d+\\.?\\d*) (-?(?:\\d*,)*\\d+\\.?\\d{4}) ([A-Z]{3}) (-?(?:\\d*,)*\\d+\\.?\\d{2}) (-?(?:\\d*,)*\\d+\\.?\\d{6}) (?: )*(-?(?:\\d*,)*\\d+\\.?\\d{2})";
	private static String regex35 = "(.*) (\\d{6,9})";
	
	
	

	private static int rowCount = 0;
	
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
		
		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[contains(text(), 'accounts (liquidity)')]]"));
		
		for(WebElement row: rows){
			
			String rowText = row.getText().trim();
			
			System.out.println("RowText -> " + rowText);
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

}
