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

		


		ObjectMapper mapper = new ObjectMapper();
		Path p = Paths.get(System.getProperty("user.home"), "Documents", "bankStmt.json");
		try {
			mapper.writeValue(new File(p.toString()), response);
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

}
