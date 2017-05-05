package com.pisight.pimoney1.beans;

import static org.hamcrest.CoreMatchers.containsString;

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
			boxTest = new PDFExtracter(getFile("investments/new", "820420 Account Statement 31.01.2017", "pdf"),"");
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

	String regexQuantity1 = "(?:.* )?Number of Shares: ((?:\\d*,)*\\d+\\.?\\*)(?: .*)?";
	String regexQuantity2 = "(?:.* )?You [A-z]+ ((?:\\d*,)*\\d+\\.?\\*) Shares(?: .*)?";
	String regexQuantity3 = "(?:.* )?Principal Amount:  .((?:\\d*,)*\\d+\\.?\\d*) [A-Z]{3}(?: .*)?";
	String regexQuantity4 = "[A-Z]{3} ((?:\\d*,)*\\d+\\.?\\*) .*";
	String regexQuantity5 = "((?:\\d*,)*\\d+\\.?\\*) .*";
	Pattern pQuantity1 = Pattern.compile(regexQuantity1);
	Pattern pQuantity2 = Pattern.compile(regexQuantity2);
	Pattern pQuantity3 = Pattern.compile(regexQuantity3);
	Pattern pQuantity4 = Pattern.compile(regexQuantity4);
	Pattern pQuantity5 = Pattern.compile(regexQuantity5);
	
	String regexUnitCost1 = "(?:.* )?At the price of: ((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	String regexUnitCost2 = "(?:.* )?Premium per Share: ((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	Pattern pUntiCost1 = Pattern.compile(regexUnitCost1);
	Pattern pUntiCost2 = Pattern.compile(regexUnitCost2);
	
	String regexInterest = "(?:.* )?((?:\\d*,)*\\d+\\.?\\*) ?%(?: .*)?";
	Pattern pInterest = Pattern.compile(regexInterest);
	
	String regexInterestAndDates = "(\\d{1,2}-\\d{2}-\\d{4}) - (\\d{1,2}-\\d{2}-\\d{4}) ((?:\\d*,)*\\d+\\.?\\d*) % .* ((?:\\+|-)?(?:\\d*,)*\\d+\\.?\\d*) [A-Z]{3}";
	Pattern pInterestAndDates = Pattern.compile(regexInterestAndDates);
	
	String regexTransLevies1 = "(?:.* )?Transaction Levy: .((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	String regexTransLevies2 = "(?:.* )?Brokerage / Commission: .((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	String regexTransLevies3 = "(?:.* )?GST.*: .((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	Pattern pTransLevies1 = Pattern.compile(regexTransLevies1);
	Pattern pTransLevies2 = Pattern.compile(regexTransLevies2);
	Pattern pTransLevies3 = Pattern.compile(regexTransLevies3);
	
	String regexStrikePrice = "(?:.* )?Strike Price: ((?:\\d*,)*\\d+\\.?\\*) [A-Z]{3}(?: .*)?";
	Pattern pStrikePrice = Pattern.compile(regexStrikePrice);
	
	String regexExpiryDate = "(?:.* )?Expiry Date/Time: (\\d{1,2}-[A-Z]{3}-\\d{4}))(?: .*)?";
	Pattern pExpiryDate= Pattern.compile(regexExpiryDate);
	

	private static InvestmentTransaction currentTrans = null;
	private static int transactionLevies = 0; // to consolidate transaction levies(Transaction Levy, Brokerage / Commission, GST)
	private static boolean isInterestPresent = false; // to get interest rate if present in the next line
	private static boolean isDurationPresent = false; // to get duration if present in the next line
	private static int rowCount = 0; // rowCount 1 for get second line description if present
	public static void scrapeStatement(WebDriver driver) throws Exception{

		HashMap<String, String> properties = new HashMap<String, String>();
		Response response = new Response(properties);

		System.out.println("#@#@#@#@##@#@##@#@#@##@#@#@#@#@##@#@#@#@#@#@##@#@#@#@#");
		System.out.println("");

		WebElement accountNumberEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Assets under custody')]]"));
		String accountNumber = accountNumberEle.getText().trim();

		String regex = "(?:.* )?(?:(\\d{8}\\.\\d{4}) - )Assets under custody(?: .*)?";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(accountNumber);

		if(m.matches()){
			accountNumber = m.group(1);
		}

		WebElement stmtDateEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Value of holdings as at')]]"));
		String stmtDate = stmtDateEle.getText().trim();

		regex = "(?:.* )?Value of holdings as at (\\d{1,2}-\\d{1,2}-\\d{4})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(stmtDate);

		if(m.matches()){
			stmtDate = m.group(1);
		}

		WebElement currencyEle = driver.findElement(By.xpath("//tr[td[contains(text() , 'Exchange rate on date of printing /')]]"));
		String currency = currencyEle.getText().trim();

		regex = "(?:.* )?Exchange rate on date of printing / ([A-Z]{3})( .*)?";
		p = Pattern.compile(regex);
		m = p.matcher(currency);

		if(m.matches()){
			currency = m.group(1);
		}

		InvestmentAccount account = new InvestmentAccount(properties);

		account.setAccountNumber(accountNumber);
		account.setCurrency(currency);
		account.setBalance("0.00");
		account.setAvailableBalance("0.00");
		account.setBillDate(stmtDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YYYY);
		response.addInvestmentAccount(account);

		getCashAssets(account, driver);

		String transRegex = "(\\d{1,2}-\\d{2}-\\d{2}) (.*) \\| n°\\d+( .(?:\\d*,)*\\d+\\.?\\d*)?";
		String currencyRegex1 = "Your current account l ([A-Z]{3})";
		String currencyRegex2 = "Your contract position in ([A-Z]{3})";
		String currencyRegex3 = "Currency ([A-Z]{3})";
		p = Pattern.compile(transRegex);
		Pattern pCur1 = Pattern.compile(currencyRegex1);
		Pattern pCur2 = Pattern.compile(currencyRegex2);
		Pattern pCur3 = Pattern.compile(currencyRegex3);

		List<WebElement> rows = driver.findElements(By.xpath("//tr[td[text() = 'Your additional information']]/following-sibling::tr"));

		String transCurrency = null;
		for(WebElement row: rows){
			String rowText = row.getText().trim();
			System.out.println("RowText -> " + rowText);

			if(rowText.contains("INTERIM BALANCE") || rowText.contains("PAGE") || rowText.contains("NEW BALANCE") 
					|| rowText.contains("FORMER BALANCE")){
				currentTrans = null;
				continue;
			}

			m = p.matcher(rowText);
			Matcher mCur1 = pCur1.matcher(rowText);
			Matcher mCur2 = pCur2.matcher(rowText);
			Matcher mCur3 = pCur3.matcher(rowText);

			if(m.matches()){
				rowCount = 1;
				String transDate = m.group(1);
				String description = m.group(2);
				String amount = m.group(3);
				String type = null;

				if(StringUtils.isEmpty(amount)){
					amount  = "0.00";
					type = InvestmentTransaction.TRANSACTION_TYPE_SELL;
				}
				else if(amount.contains("+")){
					amount = amount.replace("+", "").trim();
					type = InvestmentTransaction.TRANSACTION_TYPE_SELL;
				}
				else if(amount.contains("-")){
					type = InvestmentTransaction.TRANSACTION_TYPE_BUY;
				}

				InvestmentTransaction transaction = new InvestmentTransaction();

				transaction.setAccountNumber(account.getAccountNumber());
				transaction.setTransactionDate(transDate, Constants.DATEFORMAT_DD_DASH_MM_DASH_YY);
				transaction.setDescription(description);
				transaction.setAmount(amount, true);
				transaction.setType(type);
				transaction.setCurrency(transCurrency);
				account.addTransaction(transaction);
				currentTrans = transaction;
			}
			else if(mCur1.matches()){
				transCurrency = mCur1.group(1);
				rowCount = 0;
			}
			else if(mCur2.matches()){
				transCurrency = mCur2.group(1);
				rowCount = 0;
			}
			else if(mCur3.matches()){
				transCurrency = mCur3.group(1);
				rowCount = 0;
			}
			else if(currentTrans != null){
				rowCount = 0;
				if(rowCount  == 1 && rowText.contains(":")){
					String descRegex = "(.*) \\| ([A-Z]{2}\\w{8}\\d{2})";
					Pattern pDesc = Pattern.compile(descRegex);
					Matcher mDesc = pDesc.matcher(rowText);
					if(mDesc.matches()){
						String description = mDesc.group(1);
						String isin = mDesc.group(2);
						description = description + " " + currentTrans.getDescription();
						currentTrans.setDescription(description.trim());
						currentTrans.setAssetISIN(isin);
					}
					else{
						String description = rowText + " " + currentTrans.getDescription();
						currentTrans.setDescription(description.trim());
					}
					continue;
				}
				getQuantity(rowText);
				getUnitCost(rowText);
				getInterest(rowText);
				getTransactionLevies(rowText);
				getStrikePrice(rowText);
				getExpiryDate(rowText);
				getStartAndMaturityDate(rowText);
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

	private static void getQuantity(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getUnitCost(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getInterest(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getTransactionLevies(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getStrikePrice(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getExpiryDate(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getStartAndMaturityDate(String rowText) {
		// TODO Auto-generated method stub

	}

	private static void getCashAssets(InvestmentAccount account, WebDriver driver) {
		// Cash Assets

		String regex = "(.*) ([A-Z]{3}) (\\d{8}\\.\\d{4}) .* (-?(?:\\d*,)*\\d+\\.?\\d*) \\d{1,2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = null;

		List<WebElement> rows = driver.findElements(By.xpath("//tr[preceding-sibling::tr/td[text() = 'Your accounts with transactions'] and following-sibling::tr/td[text() = 'Your additional information']]"));

		for(WebElement row: rows){
			String rowText = row.getText().trim();

			System.out.println("RowText -> " + rowText);

			m = p.matcher(rowText);

			if(m.matches()){
				String description = m.group(1);
				String currency = m.group(2);
				String quantity = m.group(4);

				description = description.replace("l", "").trim();

				HoldingAsset asset = new HoldingAsset();

				asset.setHoldingAssetAccountNumber(account.getAccountNumber());
				asset.setHoldingAssetDescription(description);
				asset.setHoldingAssetCurrency(currency);
				asset.setHoldingAssetQuantity(quantity, true);
				asset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
				account.addAsset(asset);
			}

		}

	}

}
