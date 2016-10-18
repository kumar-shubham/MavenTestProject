package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestScrapper {

	private static HashMap<String, String> nameMap = new HashMap<String, String>();
	private static Set<String> keySet = null;

	static{
		nameMap.put("NTUC", "NTUC Income Insurance Co-operative Limited");
		nameMap.put("AXA Life Insurance", "AXA Life Insurance Singapore Private Limited");
		nameMap.put("Great Eastern", "The Great Eastern Life Assurance Company Limited");
		nameMap.put("Manulife", "Manulife Manulife (Singapore) Pte Ltd.");
		nameMap.put("Tokio Marine", "Tokio Marine Life Insurance Singapore Ltd.");
		keySet = nameMap.keySet();
	}

	public static void main(String[] args){

		final long startTime = System.currentTimeMillis();
		WebDriver driver = getDriver();


		JavascriptExecutor js = (JavascriptExecutor) driver;

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~``");


		scrapeStatement(driver);
		System.out.println("closing driver");

		driver.quit();

		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );

	}


	public static void scrapeStatement(WebDriver driver){



		System.out.println("starting scrapping");

		//		NTUCMediShield(driver);

		//		AXAMediShield(driver);

		//		GreatEastern(driver);

		//		ManulifeManusave(driver);
		
		TokioMarine(driver);



	}

	private static void TokioMarine(WebDriver driver) {
		
		Insurance insurance = new Insurance();

		driver.get("file:///home/kumar/Downloads/statements/statements/insurance/stmts2/html/TokyoMarine%20Legacy.pdf.html");

		String instituteName = getInstituteName(driver);
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		WebElement nameEle = null;
		String name = null;
		try{
			nameEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]/../following-sibling::tr[1]"));
			if(!nameEle.getText().toLowerCase().contains("assured")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Policy No ')]/../following-sibling::tr[2]"));
			}catch(NoSuchElementException ne){
				System.out.println("Name not found");
			}
		}
		if(nameEle != null){
			name = nameEle.getText().trim();
			name  = name.replace("Plan Name", "");
			name = name.replace("plan name", "");
			name = name.replace(":", "").trim();

		}

		String policyNumber = null;
		WebElement policyEle = null;
		try{
			policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy No')]/../following-sibling::tr[1]"));
			if(!policyEle.getText().toLowerCase().contains("period")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy No')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Policy Number not found");
			}
		}
		if(policyEle != null){
			policyNumber = policyEle.getText().trim();
			policyNumber  = policyNumber.replace("Policy No", "");
			policyNumber = policyNumber.replace("policy no", "");
			policyNumber = policyNumber.replace(".", "").trim();

		}

		WebElement sumEle = null;
		String sumAssured = null;
		String currency = null;
		try{
			sumEle = page.findElement(By.xpath("//td[contains(text(), 'Sum Assured')]/../following-sibling::tr[1]"));
			if(!sumEle.getText().toLowerCase().contains("bonus")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				sumEle = page.findElement(By.xpath("//td[contains(text(), 'Sum Assured')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				sumEle = page.findElement(By.xpath("//td[contains(text(), 'Bonus Rate')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("sum assured amount not found");
			}
		}
		if(sumEle != null){
			currency = sumEle.getText().trim();
			currency = currency.replace("Sum Assured", "");
			currency = currency.replace("sum assured", "").trim();
			if(currency.indexOf(" ") > 0){
				currency = currency.substring(0, currency.indexOf(" "));
			}
			else{
				currency = null;
			}
			sumAssured = sumEle.getText().trim();
			sumAssured  = sumAssured.substring(sumAssured.lastIndexOf(" "));
			sumAssured = sumAssured.replace("$", "").trim();

		}

		String pageSource = page.getText();
		String type = null;
		if(pageSource.contains("MediShield") || pageSource.contains("Medisave")){
			type = "Protection";
		}

		insurance.setInstituteName(instituteName);
		insurance.setCurrency(currency);
		insurance.setType(type);
		insurance.setPolicyNumber(policyNumber);
		insurance.setProductName(name);
		insurance.setSumAssured(sumAssured);

		writeJSON(insurance, "TokioMarine");
		
	}


	private static void ManulifeManusave(WebDriver driver) {

		Insurance insurance = new Insurance();

		driver.get("file:///home/kumar/Downloads/statements/statements/insurance/stmts2/html/Manulife%20Manusave.pdf.html");

		String instituteName = getInstituteName(driver);
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		WebElement nameEle = null;
		String name = null;
		try{
			nameEle = page.findElement(By.xpath("//td[contains(text(), 'Basic Plan')]/../following-sibling::tr[1]"));
			if(!nameEle.getText().toLowerCase().contains("life insured")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Basic Plan')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Life Insured')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Name not found");
			}
		}
		if(nameEle != null){
			name = nameEle.getText().trim();
			name  = name.replace("Basic Plan", "");
			name = name.replace("basic plan", "").trim();
			if(name.indexOf("Sum Insured") > 0){
				name = name.substring(0, name.indexOf("Sum Insured")).trim();
			}
			name = name.replace(":", "").trim();

		}

		String policyNumber = null;
		WebElement policyEle = null;
		try{
			policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Number')]/../following-sibling::tr[1]"));
			if(!policyEle.getText().toLowerCase().contains("basic plan")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Number')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Basic Plan')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Policy Number not found");
			}
		}
		if(policyEle != null){
			policyNumber = policyEle.getText().trim();
			policyNumber  = policyNumber.replace("Policy Number", "");
			policyNumber = policyNumber.replace("policy number", "");
			if(policyNumber.indexOf("Effective Date") > 0){
				policyNumber = policyNumber.substring(0, policyNumber.indexOf("Effective Date")).trim();
			}
			policyNumber = policyNumber.replace(":", "").trim();

		}

		String surrenderValue = null;
		WebElement SVElement = null;
		try{
			SVElement = page.findElement(By.xpath("//td[contains(text(), 'estimated surrender value as of')]"));
			surrenderValue  = SVElement.getText().trim();
			if(!surrenderValue.contains("S$")){
				SVElement = page.findElement(By.xpath("//td[contains(text(), 'estimated surrender value as of')]/../following-sibling::tr[1]"));
			}
		}catch(NoSuchElementException e){
			try{
				SVElement = page.findElement(By.xpath("//td[contains(text(), 'estimated surrender value is not guaranteed')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("premium amount not found");
			}
		}

		if(SVElement != null){
			surrenderValue  = SVElement.getText().trim();
			if(surrenderValue.contains("S$")){
				surrenderValue = surrenderValue.substring(surrenderValue.indexOf("S$")+2).trim();
			}
		}


		String pageSource = page.getText();
		String type = null;
		if(pageSource.contains("MediShield") || pageSource.contains("Medisave")){
			type = "Protection";
		}
		else if(pageSource.contains("MANUSAVE")){
			type = "Saving";
		}
		String currency = null;
		if(pageSource.contains("S$")){
			currency = "SGD";
		}

		insurance.setInstituteName(instituteName);
		insurance.setCurrency(currency);
		insurance.setType(type);
		insurance.setPolicyNumber(policyNumber);
		insurance.setProductName(name);
		insurance.setSumAssured(surrenderValue);

		writeJSON(insurance, "ManulifeManusave");

	}


	private static void GreatEastern(WebDriver driver) {

		List<Insurance> list = new ArrayList<Insurance>();

		driver.get("file:///home/kumar/Downloads/statements/statements/insurance/stmts2/html/Great%20Eastern%20Bonus.pdf.html");

		String instituteName = getInstituteName(driver);
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		String pageSource = page.getText();
		String type = null;
		if(pageSource.contains("MediShield") || pageSource.contains("Medisave")){
			type = "Protection";
		}
		String currency = null;
		if(pageSource.toLowerCase().contains("singapore dollar")){
			currency = "SGD";
		}

		List<WebElement> accountsEle = page.findElements(By.xpath("//td[contains(text(), 'Policy Name and Details')]/../following-sibling::tr"));

		String startRegex = "\\d?(.*) ((\\d*,)*\\d+(.)\\d+) ((\\d*,)*\\d+(.)\\d+) .* ((\\d*,)*\\d+(.)\\d+) ((\\d*,)*\\d+(.)\\d+)";
		Pattern p = Pattern.compile(startRegex);

		Insurance lastInsurance = null;
		for(WebElement ele: accountsEle){
			String text = ele.getText().trim();

			Matcher m = p.matcher(text);

			if(m.matches()){

				lastInsurance = new Insurance();
				String policyName = m.group(1).trim();
				lastInsurance.setProductName(policyName);
				lastInsurance.setCurrency(currency);
				lastInsurance.setType(type);
				lastInsurance.setInstituteName(instituteName);
				list.add(lastInsurance);
			}
			else{

				if(text.toUpperCase().contains("END OF STATEMENT")){
					break;
				}

				if(lastInsurance != null){
					if(text.toLowerCase().contains("death")){
						text = text.replace("death", "");
						text = text.replace("Death", "").trim();
						lastInsurance.setProductName(lastInsurance.getProductName() + " " + text);
					}else if(text.toLowerCase().contains("sum assured")){
						text = text.replace("sum assured", "");
						text = text.replace("Sum Assured", "").trim();
						lastInsurance.setSumAssured(text);
					}else if(text.toLowerCase().contains("policy number")){
						text = text.replace("policy number", "");
						text = text.replace("Policy Number", "").trim();
						if(text.contains(" ")){
							text = text.substring(0, text.indexOf(" "));
						}
						lastInsurance.setPolicyNumber(text);
					}

				}
			}



		}


		writeJSON(list, "GreatEastern");

	}


	private static void AXAMediShield(WebDriver driver) {

		Insurance insurance = new Insurance();

		driver.get("file:///home/kumar/Downloads/statements/statements/insurance/stmts2/html/AXA%20MediShieldLife.pdf.html");

		String instituteName = getInstituteName(driver);
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		WebElement nameEle = null;
		String name = null;
		try{
			nameEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]/../preceding-sibling::tr[1]"));
			if(!nameEle.getText().toLowerCase().contains("policy")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Number')]/../following-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Name not found");
			}
		}
		if(nameEle != null){
			name = nameEle.getText().trim();
			name  = name.replace("Plan Name", "");
			name = name.replace("plan name", "");
			name = name.replace(":", "").trim();

		}

		String policyNumber = null;
		WebElement policyEle = null;
		try{
			policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Number')]/../following-sibling::tr[1]"));
			if(!policyEle.getText().toLowerCase().contains("plan")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Number')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Plan Name')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Policy Number not found");
			}
		}
		if(policyEle != null){
			policyNumber = policyEle.getText().trim();
			policyNumber  = policyNumber.replace("Policy Number", "");
			policyNumber = policyNumber.replace("policy number", "");
			policyNumber = policyNumber.replace(":", "").trim();

		}

		WebElement premiumEle = null;
		String premiumAmount = null;
		try{
			premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Standard MediShield Life premium')]/../following-sibling::tr[2]"));
			if(!premiumEle.getText().toLowerCase().contains("medishield")){
				premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Standard MediShield Life premium')]/../following-sibling::tr[1]"));
				if(!premiumEle.getText().toLowerCase().contains("medishield")){
					throw new NoSuchElementException("Wrong element picked");
				}else{
					premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Standard MediShield Life premium')]"));
				}

			}else{
				premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Standard MediShield Life premium')]/../following-sibling::tr[1]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Medishield Lfe Premium before Subsidies')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("premium amount not found");
			}
		}
		if(premiumEle != null){
			premiumAmount = premiumEle.getText().trim();
			if(premiumAmount.contains(" ")){
				premiumAmount  = premiumAmount.substring(premiumAmount.lastIndexOf(" "));
			}
			premiumAmount = premiumAmount.replace("$", "");
			premiumAmount = premiumAmount.replace(":", "").trim();

		}

		WebElement currencyEle = null;
		String currency = null;
		try{
			currencyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Currency')]/../following-sibling::tr[2]"));
			if(!currencyEle.getText().toLowerCase().contains("financial consultant")){
				currencyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Currency')]/../following-sibling::tr[1]"));
				if(!currencyEle.getText().toLowerCase().contains("financial consultant")){
					throw new NoSuchElementException("Wrong element picked");
				}else{
					currencyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Currency')]"));
				}

			}else{
				currencyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy Currency')]/../following-sibling::tr[1]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				currencyEle = page.findElement(By.xpath("//td[contains(text(), 'Financial Consultant Code')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("premium amount not found");
			}
		}
		if(currencyEle != null){
			currency = currencyEle.getText().trim();
			if(currency.contains(" ")){
				currency  = currency.substring(currency.lastIndexOf(" "));
			}
			currency = currency.replace("Policy Currency", "");
			currency = currency.replace(":", "").trim();

		}

		String pageSource = page.getText();
		String type = null;
		if(pageSource.contains("MediShield") || pageSource.contains("Medisave")){
			type = "Protection";
		}

		insurance.setInstituteName(instituteName);
		insurance.setCurrency(currency);
		insurance.setType(type);
		insurance.setPolicyNumber(policyNumber);
		insurance.setPremiumAmount(premiumAmount);
		insurance.setProductName(name);

		writeJSON(insurance, "AXAMediShield");

	}


	private static void NTUCMediShield(WebDriver driver){
		Insurance insurance = new Insurance();

		driver.get("file:///home/kumar/Downloads/statements/statements/insurance/stmts2/html/NTUC%20MediShieldLife.pdf.html");

		String instituteName = getInstituteName(driver);
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		WebElement nameEle = null;
		String name = null;
		try{
			nameEle = page.findElement(By.xpath("//td[contains(text(), 'Main Plan')]/../following-sibling::tr[1]"));
			if(!nameEle.getText().toLowerCase().contains("rider")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Main Plan')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				nameEle = page.findElement(By.xpath("//td[contains(text(), 'Rider (s)')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Name not found");
			}
		}
		if(nameEle != null){
			name = nameEle.getText().trim();
			name  = name.replace("Main Plan", "");
			name = name.replace("main plan", "");
			name = name.replace(":", "").trim();

		}

		String premiumFreq = null;
		String policyNumber = null;
		try{
			WebElement freqEle = page.findElement(By.xpath("//td[contains(text(), 'Period of Insurance')]"));
			String freq = freqEle.getText().trim();
			freq = freq.replace("Period of Insurance", "").trim();
			if(freq.toLowerCase().contains("to")){
				String dates[] = freq.split("to");
				String date1 = dates[0].trim();
				String date2 = dates[1].trim();
				SimpleDateFormat sdf = new SimpleDateFormat(ParserUtility.DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY);
				Date sDate = sdf.parse(date1);
				Date eDate = sdf.parse(date2);

				int diffInDays = (int) ((eDate.getTime() - sDate.getTime()) / (1000 * 60 * 60 * 24));

				if(diffInDays <= 31){
					premiumFreq = "Monthly";
				}else if(diffInDays < 130){
					premiumFreq = "Quaterly";
				}else if(diffInDays < 200){
					premiumFreq = "Half-Yearly";
				}else{
					premiumFreq = "Annual";
				}
			}
		}catch(Exception e){
			System.out.println("Premium Frequency Not found");
		}

		WebElement policyEle = null;
		try{
			policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy number')]/../following-sibling::tr[1]"));
			if(!policyEle.getText().toLowerCase().contains("period")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Policy number')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				policyEle = page.findElement(By.xpath("//td[contains(text(), 'Period of Insurance')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("Policy Number not found");
			}
		}
		if(policyEle != null){
			policyNumber = policyEle.getText().trim();
			policyNumber  = policyNumber.replace("Policy number", "");
			policyNumber = policyNumber.replace("policy number", "");
			policyNumber = policyNumber.replace(":", "").trim();

		}

		WebElement premiumEle = null;
		String premiumAmount = null;
		try{
			premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Total Annual Premium')]/../following-sibling::tr[1]"));
			if(!premiumEle.getText().toLowerCase().contains("premium")){
				throw new NoSuchElementException("Wrong element picked");
			}
			else{
				premiumEle = page.findElement(By.xpath("//td[contains(text(), 'Total Annual Premium')]"));
			}
		}
		catch(NoSuchElementException e){
			try{
				premiumEle = page.findElement(By.xpath("//td[contains(text(), 'CPF premium will be deducted')]/../preceding-sibling::tr[1]"));
			}catch(NoSuchElementException ne){
				System.out.println("premium amount not found");
			}
		}
		if(premiumEle != null){
			premiumAmount = premiumEle.getText().trim();
			premiumAmount  = premiumAmount.substring(premiumAmount.lastIndexOf(" "));
			premiumAmount = premiumAmount.replace("$", "");
			premiumAmount = premiumAmount.replace(":", "").trim();

		}

		String pageSource = page.getText();
		String type = null;
		if(pageSource.contains("MediShield") || pageSource.contains("Medisave")){
			type = "Protection";
		}

		insurance.setInstituteName(instituteName);
		insurance.setCurrency("SGD");
		insurance.setType(type);
		insurance.setPolicyNumber(policyNumber);
		insurance.setPremiumAmount(premiumAmount);
		insurance.setPremiumFrequency(premiumFreq);
		insurance.setProductName(name);

		writeJSON(insurance, "NTUCMediShield");


	}

	private static String getInstituteName(WebDriver driver){
		String page = driver.getPageSource();
		String instituteName = null;

		for(String key:keySet){

			if(page.contains(key) || page.contains(key.toLowerCase())){
				instituteName = nameMap.get(key);
			}
		}

		return instituteName;
	}

	private static void writeJSON(Object insurance, String fileName){

		ObjectMapper mapper = new ObjectMapper();
		Path p3 = Paths.get(System.getProperty("user.home"), "Documents", fileName+".json");
		try {
			mapper.writeValue(new File(p3.toString()), insurance);
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

}
