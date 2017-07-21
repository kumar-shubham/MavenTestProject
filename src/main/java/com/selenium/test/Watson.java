package com.selenium.test;

import java.io.File;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pisight.pimoney.constants.Constants;

/**
 * @author kumar
 *
 */
public class Watson {

	/**
	 * use this method stop the execution for particular seconds.
	 * @param seconds
	 */
	public static void sleep(int seconds){
		try {
			ScriptLogger.writeInfo("Sleeping for " + seconds + " second(s)");
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			ScriptLogger.writeError("Error ", e);
		}
	}

	/**
	 * use this method to execute script in the page.
	 * This may throw runtime error by selenium.
	 * frameindex is the index of frame.
	 * -1 is for default frame.
	 * @param sherlock
	 * @param script
	 * @param frameIndex
	 */
	public static void executeScript(Sherlock sherlock, String script, int frameIndex){
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("executing script :: " + script);
		JavascriptExecutor js = (JavascriptExecutor) sherlock.getDriverObject();
		js.executeScript(script, frameIndex);
		ScriptLogger.writeDebug("Total time taken in executing script -> " + (System.currentTimeMillis()-start));
	}


	public static void evaluateXpathAndClick(Sherlock sherlock, String xpath){

	}

	/**
	 * This method is used to take screenshot of the currently open window.
	 * idName is the name by which you want to store the screenshot.
	 * @param sherlock
	 * @param idName
	 */
	/*public static void takeScreenShot(Sherlock sherlock, String idName) {
		//get the driver
		long start = System.currentTimeMillis();
		File scrFile = ((TakesScreenshot)sherlock.getDriverObject()).getScreenshotAs(OutputType.FILE);
		//The below method will save the screen shot in d drive with test method name 
		try {
			FileUtils.copyFile(scrFile, new File(Constants.PATH_SCREENSHOT +idName+".png"));
			ScriptLogger.writeInfo("***Placed screen shot in " +  Constants.PATH_SCREENSHOT +"***");
		} catch (IOException e) {
			ScriptLogger.writeError("Error ", e);
		}
		ScriptLogger.writeDebug("Total time taken in taking screenshot -> " + (System.currentTimeMillis()-start));
	}*/

	/**
	 * This method wait for the given element for 20 seconds.
	 * It does not throw exception.
	 * @param sherlock
	 * @param ByType
	 */
	public static void waitForTheTarget(Sherlock sherlock, By ByType){
		long start = System.currentTimeMillis();
		WebDriverWait wait = new WebDriverWait(sherlock.getDriverObject(), 20);
		ScriptLogger.writeInfo("waiting for element to load");

		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(ByType));
			ScriptLogger.writeInfo("Element loaded");
		}
		catch(Exception e){
			ScriptLogger.writeError("Element not found");
		}
		ScriptLogger.writeDebug("Total time taken for waiting the target -> " + (System.currentTimeMillis()-start));

	}

	/**
	 * Use this method if you just want to switch to default frame.
	 * @param sherlock
	 */
	public static void switchToDefaultFrame(Sherlock sherlock){
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("switching to defualt frame");
		sherlock.getDriverObject().switchTo().defaultContent();
		ScriptLogger.writeDebug("Total time taken to switch to default frame -> " + (System.currentTimeMillis()-start));
	}

	/**
	 * This method is used to switch to a frame with the given name.
	 * This first switches to default frame and then to the mentioned frame.
	 * So no need to switch to default frame if using this method.
	 * @param sherlock
	 * @param name
	 * @throws Exception 
	 */
	public static void switchToFrameByName(Sherlock sherlock, String name) throws Exception{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("switching to frame with name :: " + name);
		WebDriver driver = sherlock.getDriverObject();
		try{
			driver.switchTo().defaultContent();
			driver.switchTo().frame(name);
			ScriptLogger.writeInfo("frame switched");
		}
		catch(Exception e){
			ScriptLogger.writeError("frame switching failed");
			throw new Exception("Frame switch failed");
		}
		ScriptLogger.writeDebug("Total time taken to switch to frame [" + name + "] -> " + (System.currentTimeMillis()-start));
	}

	/**
	 * This method is used to switch to a frame with the given index.
	 * This first switches to default frame and then to the mentioned frame.
	 * So no need to switch to default frame if using this method.
	 * @param sherlock
	 * @param index
	 * @throws Exception 
	 */
	public static void switchToFrameByIndex(Sherlock sherlock, int index) throws Exception{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("switching to frame with index :: " + index);
		WebDriver driver = sherlock.getDriverObject();
		try{
			driver.switchTo().defaultContent();
			driver.switchTo().frame(index);
			ScriptLogger.writeInfo("frame switched");
		}
		catch(Exception e){
			ScriptLogger.writeError("frame switching failed");
			throw new Exception("Frame switch failed");
		}
		ScriptLogger.writeDebug("Total time taken to switch to frame with index [" + index + "] -> " + (System.currentTimeMillis()-start));
	}

	/**
	 * This method is used to switch to a frame element.
	 * This first switches to default frame and then to the mentioned frame.
	 * So no need to switch to default frame if using this method.
	 * @param sherlock
	 * @param webElement
	 * @throws Exception 
	 */
	public static void switchToFrame(Sherlock sherlock, WebElement webElement) throws Exception{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("switching to frame  " + webElement);
		WebDriver driver = sherlock.getDriverObject();
		try{
			driver.switchTo().defaultContent();
			driver.switchTo().frame(webElement);
			ScriptLogger.writeInfo("frame switched");
		}
		catch(Exception e){
			ScriptLogger.writeError("frame switching failed");
			throw new Exception("Frame switch failed");
		}
		ScriptLogger.writeDebug("Total time taken to switch to frame -> " + (System.currentTimeMillis()-start));
	}

	public static void switchToFrameContainingXPath(Sherlock sherlock, String xpath) throws Exception{
		long start = System.currentTimeMillis();
		List<WebElement> frames = sherlock.findElementsByTagName("iframe");

		for(WebElement frame: frames){

			switchToFrame(sherlock, frame);

			List<WebElement> elements = sherlock.findElementsByXPath(xpath);

			if(elements.size() > 0){
				ScriptLogger.writeDebug("Total time taken to switch to frame ->" + (System.currentTimeMillis()-start));
				return;
			}
		}

		throw new Exception("Error in switching frame");

	}


	/**
	 * This method is used to switch to a frame with the given name.
	 * This doesnot switches to default frame. 
	 * So use this for nested frame switching.
	 * @param sherlock
	 * @param name
	 * @throws Exception 
	 */
	public static void switchToChildFrameByName(Sherlock sherlock, String name) throws Exception{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("switching to frame with name/id :: " + name);
		WebDriver driver = sherlock.getDriverObject();
		try{
			driver.switchTo().frame(name);
		}
		catch(Exception e){
			ScriptLogger.writeError("frame switching failed");
			throw new Exception("Frame switch failed");
		}
		ScriptLogger.writeDebug("Total time taken to switch to child frame ->" + (System.currentTimeMillis()-start));
	}

	/**
	 * This method check if the text present on the page.
	 * 
	 * @param sherlock
	 * @param text
	 * @return
	 */
	public static boolean checkIfTextPresentOnPage(Sherlock sherlock, String text){
		long start = System.currentTimeMillis();
		sherlock.setImplicitwait(5);
		WebElement element = sherlock.findElementByXPath("//*[contains(.,.)]");
		sherlock.setImplicitwait(10);

		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return element.getText().contains(text);
	}

	/**
	 * This method check if the text present on the page.
	 * 
	 * @param sherlock
	 * @param text
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfTextPresentOnPage(Sherlock sherlock, String text, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			sherlock.setImplicitwait(0);
			WebElement element = sherlock.findElementByCSSSelector("html");
			sherlock.setImplicitwait(10);
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return element.getText().contains(text);
		}
		else{
			return checkIfTextPresentOnPage(sherlock, text);
		}



	}

	/**
	 * This method check if the text present on the page.
	 * 
	 * @param sherlock
	 * @param text
	 * @return
	 */
	public static boolean checkIfTextPresentOnPage1(Sherlock sherlock, String text){

		long start = System.currentTimeMillis();
		WebElement element = null;
		try{
			sherlock.setImplicitwait(5);
			element = sherlock.findElementByXPath("//*[contains(.,'"+ text +"')]");
		}
		catch(Exception e){
			ScriptLogger.writeInfo("text not found : " + text);
		}
		finally{
			sherlock.setImplicitwait(10);
		}

		if(element == null){
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return false;
		}
		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return true;
	}


	/**
	 * This method check if the text present on the page.
	 * 
	 * @param sherlock
	 * @param text
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfTextPresentOnPage1(Sherlock sherlock, String text, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			WebElement element = null;
			try{
				sherlock.setImplicitwait(0);
				element = sherlock.findElementByCSSSelector("html:contains('" + text + "')");
			}
			catch(Exception e){
				ScriptLogger.writeInfo("text not found : " + text);
			}
			finally{
				sherlock.setImplicitwait(10);
			}

			if(element == null){
				ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
				return false;
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return true;
		}
		else{
			return checkIfTextPresentOnPage1(sherlock, text);
		}
	}

	/**
	 * This method check if all of the texts present in the textArray present on the page.
	 * returns false if any of the texts is missing.
	 * @param sherlock
	 * @param textArray
	 * @return
	 */
	public static boolean checkIfAllTextsPresentOnPage(Sherlock sherlock, String[] textArray){
		long start = System.currentTimeMillis();
		sherlock.setImplicitwait(5);
		WebElement element = sherlock.findElementByXPath("//*[contains(.,.)]");
		sherlock.setImplicitwait(10);
		String pageText = element.getText();

		if(textArray != null){

			for(int i = 0; i<textArray.length ;i++){
				if(!pageText.contains(textArray[i])){
					ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
					return false;
				}
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return true;
		}
		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return false;
	}

	/**
	 * This method check if all of the texts present in the textArray present on the page.
	 * returns false if any of the texts is missing.
	 * @param sherlock
	 * @param textArray
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfAllTextsPresentOnPage(Sherlock sherlock, String[] textArray, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			sherlock.setImplicitwait(0);
			WebElement element = sherlock.findElementByCSSSelector("html");
			sherlock.setImplicitwait(10);
			String pageText = element.getText();

			if(textArray != null){

				for(int i = 0; i<textArray.length ;i++){
					if(!pageText.contains(textArray[i])){
						ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
						return false;
					}
				}
				ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
				return true;
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return false;
		}
		else{
			return checkIfAllTextsPresentOnPage(sherlock, textArray);
		}
	}


	/**
	 * This method check if all of the texts present in the textArray present on the page.
	 * returns false if any of the texts is missing.
	 * @param sherlock
	 * @param textArray
	 * @return
	 */
	public static boolean checkIfAllTextsPresentOnPage1(Sherlock sherlock, String[] textArray){

		long start = System.currentTimeMillis();
		WebElement element = null;

		if(textArray != null){

			for(int i = 0; i<textArray.length ;i++){
				try{
					sherlock.setImplicitwait(5);
					element = sherlock.findElementByXPath("//*[contains(.,'"+ textArray[i] +"')]");
				}
				catch(Exception e){
					ScriptLogger.writeInfo("text not found : " + textArray[i] );
					ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
					return false;
				}
				finally{
					sherlock.setImplicitwait(10);
				}
				if(element == null){
					ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
					return false;
				}
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return true;
		}
		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return false;
	}


	/**
	 * This method check if all of the texts present in the textArray present on the page.
	 * returns false if any of the texts is missing.
	 * @param sherlock
	 * @param textArray
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfAllTextsPresentOnPage1(Sherlock sherlock, String[] textArray, boolean wait){

		long start = System.currentTimeMillis();
		if(!wait){
			WebElement element = null;

			if(textArray != null){

				for(int i = 0; i<textArray.length ;i++){
					try{
						sherlock.setImplicitwait(0);
						element = sherlock.findElementByCSSSelector("html:contains('" + textArray[i]  + "')");
					}
					catch(Exception e){
						ScriptLogger.writeInfo("text not found : " + textArray[i] );
						ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
						return false;
					}
					finally{
						sherlock.setImplicitwait(10);
					}
					if(element == null){
						ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
						return false;
					}
				}
				ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
				return true;
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return false;
		}
		else{
			return checkIfAllTextsPresentOnPage1(sherlock, textArray);
		}
	}


	/**
	 * This method check if any of the texts present in the textArray present on the page.
	 * returns true if any text is found.
	 * @param sherlock
	 * @param textArray
	 * @return
	 */
	public static boolean checkIfAnyTextsPresentOnPage(Sherlock sherlock, String[] textArray){
		long start = System.currentTimeMillis();
		sherlock.setImplicitwait(5);
		WebElement element = sherlock.findElementByXPath("//*[contains(.,.)]");
		sherlock.setImplicitwait(10);
		String pageText = element.getText();

		if(textArray != null){

			for(int i = 0; i<textArray.length ;i++){
				if(pageText.contains(textArray[i])){
					ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
					return true;
				}
			}
		}
		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return false;
	}


	/**
	 * This method check if any of the texts present in the textArray present on the page.
	 * returns true if any text is found.
	 * @param sherlock
	 * @param textArray
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfAnyTextsPresentOnPage(Sherlock sherlock, String[] textArray, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			sherlock.setImplicitwait(0);
			WebElement element = sherlock.findElementByCSSSelector("html");
			sherlock.setImplicitwait(10);
			String pageText = element.getText();

			if(textArray != null){

				for(int i = 0; i<textArray.length ;i++){
					if(pageText.contains(textArray[i])){
						ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
						return true;
					}
				}
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return false;
		}
		else{
			return checkIfAnyTextsPresentOnPage(sherlock, textArray);
		}
	}

	/**
	 * This method check if any of the texts present in the textArray present on the page.
	 * returns true if any text is found.
	 * @param sherlock
	 * @param textArray
	 * @return
	 */
	public static boolean checkIfAnyTextsPresentOnPage1(Sherlock sherlock, String[] textArray){
		long start = System.currentTimeMillis();
		WebElement element = null;

		if(textArray != null){

			for(int i = 0; i<textArray.length ;i++){
				try{
					sherlock.setImplicitwait(5);
					element = sherlock.findElementByXPath("//*[contains(.,'"+ textArray[i] +"')]");
				}
				catch(Exception e){
					ScriptLogger.writeInfo("text not found : " + textArray[i] );
				}
				finally{
					sherlock.setImplicitwait(10);
				}
				if(element != null){
					ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
					return true;
				}
			}
		}
		ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
		return false;
	}


	/**
	 * This method check if any of the texts present in the textArray present on the page.
	 * returns true if any text is found.
	 * @param sherlock
	 * @param textArray
	 * @param wait TODO
	 * @return
	 */
	public static boolean checkIfAnyTextsPresentOnPage1(Sherlock sherlock, String[] textArray, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			WebElement element = null;

			if(textArray != null){

				for(int i = 0; i<textArray.length ;i++){
					try{
						sherlock.setImplicitwait(0);
						element = sherlock.findElementByCSSSelector("html:contains('" + textArray[i]  + "')");
					}
					catch(Exception e){
						ScriptLogger.writeInfo("text not found : " + textArray[i] );
					}
					finally{
						sherlock.setImplicitwait(10);
					}
					if(element != null){
						ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
						return true;
					}
				}
			}
			ScriptLogger.writeDebug("Total time taken for checking text -> " + (System.currentTimeMillis()-start));
			return false;
		}
		else{
			return checkIfAnyTextsPresentOnPage1(sherlock, textArray);
		}
	}

	/**
	 * This method checks if the given element is present on the page or not.
	 * @param sherlock
	 * @param byType
	 * @return true if element present on the page
	 */
	public static boolean checkIfElementPresentOnThePage(Sherlock sherlock, By byType){
		long start = System.currentTimeMillis();
		WebDriver driver = sherlock.getDriverObject();
		if(driver == null){
			ScriptLogger.writeError("driver object is null");
			ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
			return false;
		}
		ScriptLogger.writeInfo("inside checkIfElementPresentOnThePage");
		try{
			sherlock.setImplicitwait(5);
			ScriptLogger.writeInfo("checking with " + byType.getClass());
			driver.findElement(byType);
			ScriptLogger.writeInfo("Element present on the page");
		}
		catch(Exception e){
			ScriptLogger.writeWarning("Element not present on the page");
			ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
			return false;
		}
		finally{
			sherlock.setImplicitwait(10);
		}
		ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
		return true;
	}


	/**
	 * This method checks if the given element is present on the page or not without any default wait
	 * @param sherlock
	 * @param byType
	 * @param wait TODO
	 * @return true if element present on the page
	 */
	public static boolean checkIfElementPresentOnThePage(Sherlock sherlock, By byType, boolean wait){
		long start = System.currentTimeMillis();
		if(!wait){
			WebDriver driver = sherlock.getDriverObject();
			if(driver == null){
				ScriptLogger.writeError("driver object is null");
				ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
				return false;
			}
			ScriptLogger.writeInfo("inside checkIfElementPresentOnThePage1");
			try{
				sherlock.setImplicitwait(0);
				ScriptLogger.writeInfo("checking with " + byType.getClass());
				driver.findElement(byType);
				ScriptLogger.writeInfo("Element present on the page");
			}
			catch(Exception e){
				ScriptLogger.writeWarning("Element not present on the page");
				ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
				return false;
			}
			finally{
				sherlock.setImplicitwait(10);
			}
			ScriptLogger.writeDebug("Total time taken for checking element -> " + (System.currentTimeMillis()-start));
			return true;
		}
		else{
			return checkIfElementPresentOnThePage(sherlock, byType);
		}
	}

	/**
	 * This method converts the given date String to the Pimoney Format.
	 * This method requires format of the date string passed.
	 * 
	 * @param oldDate
	 * @param format
	 * @return formated date string
	 * @throws ParseException
	 */
	public static String convertToPimoneyDate(String oldDate, String format) throws ParseException{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("inside convertDateStringToPimoneyFormat with date string :: " + oldDate + " and format :: " + format);
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date date = sdf.parse(oldDate);

		sdf = new SimpleDateFormat("yyyy-MM-dd");
		ScriptLogger.writeDebug("Total time taken for converting date -> " + (System.currentTimeMillis()-start));
		return sdf.format(date);
	}

	/**
	 * Watson is always here for you even if you don't have the date format with you.
	 * This method also converts the given date String to the Pimoney Format.
	 * It does not requires date format for the date String. It matches with the predefined set
	 * of format defined in the Constants.java class
	 * 
	 * @param dateString
	 * @return formated date string
	 * @throws Exception 
	 */
	public static String convertToPimoneyDate(String dateString) throws Exception{
		long start = System.currentTimeMillis();
		ScriptLogger.writeInfo("inside convertDateStringToPimoneyFormat with only date string :: " + dateString);
		if(StringUtils.isEmpty(dateString)) {
			return null;
		}
		Date newDate = null;
		int failureCount = 0;
		SimpleDateFormat sdf = null;
		if (dateString != null) {
			for (String parse : Constants.dateFormatList) {
				sdf = new SimpleDateFormat(parse);
				try {
					newDate = sdf.parse(dateString);
					break;
				} catch (ParseException e) {
					failureCount++;
				}
			}
		}
		else{
			return null;
		}

		if(failureCount == Constants.dateFormatList.size()){
			ScriptLogger.writeError("Date Format for the date string " + dateString + " is not in the supported dateList");
			throw new Exception("Date Format for the date string " + dateString + " is not in the supported dateList");
		}

		if(newDate != null){
			sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYY_DASH_MM_DASH_DD);
			ScriptLogger.writeDebug("Total time taken for converting date -> " + (System.currentTimeMillis()-start));
			return sdf.format(newDate);
		}
		else{
			ScriptLogger.writeError("error in parsing date " + dateString);
			throw new Exception("error in parsing date");
		}

	}


	/**
	 * Returns outer HTML of the HTML element passed. If the element is null then the method
	 * returns null;
	 * @param element
	 * @return
	 */
	public static String getOuterHTML(WebElement element){
		long start = System.currentTimeMillis();
		if(element == null){
			return null;
		}
		ScriptLogger.writeDebug("Total time taken in getting outer HTML -> " + (System.currentTimeMillis()-start));
		return element.getAttribute("outerHTML");


	}

	public static String formatAmount(String amount){
		if(amount == null){
			return null;
		}
		amount = amount.replace("$", "");

		return amount.replace(",", "");
	}


	public static String getYear(String rawDate, String dateFormat, String reference, String referenceFormat) throws Exception{

		reference = formatDate(reference);
		rawDate = formatDate(rawDate);

		Date date = getDate(referenceFormat, reference);
		Date date1 = getDate(dateFormat, rawDate);
		ScriptLogger.writeInfo("Raw date       :: " + rawDate);
		ScriptLogger.writeInfo("Raw format     :: " + dateFormat);
		ScriptLogger.writeInfo("Ref date       :: " + reference);
		ScriptLogger.writeInfo("Ref format     :: " + referenceFormat);
		if(date == null || date1 == null){
			throw new Exception("Invalid date Format");
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		c.setTime(date1);
		int month1 = c.get(Calendar.MONTH);
		int day1 = c.get(Calendar.DAY_OF_MONTH);

		if((month1 > month) || (month1 == month && day1 > day)){
			c.set(Calendar.YEAR, year-1);
		}
		else{
			c.set(Calendar.YEAR, year);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(referenceFormat);
		String result = sdf.format(c.getTime());

		ScriptLogger.writeInfo("Raw date       :: " + rawDate);
		ScriptLogger.writeInfo("Raw format     :: " + dateFormat);
		ScriptLogger.writeInfo("Ref date       :: " + reference);
		ScriptLogger.writeInfo("Ref format     :: " + referenceFormat);
		ScriptLogger.writeInfo("New Date       :: " + result);

		return result;

	}


	//this method removes extra spaces from the date strings
	private static String formatDate(String reference) {
		// TODO Auto-generated method stub

		String temp[] = reference.split(" ");
		String result = temp[0];
		for(int i = 1; i<temp.length; i++){
			if(!temp[i].trim().equals("")){
				result += " " + temp[i];
			}
		}

		return result;
	}


	private static Date getDate(String format, String value){

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
		} catch (ParseException ex) {
			ScriptLogger.writeError("Error ", ex);
		}
		if (date == null) {
			// Invalid date format
		} else {
			// Valid date format
		}

		return date;


	}

	public static String removeUnicodeSpace(String string){

		string  = string.replaceAll("\u00A0", "");
		return string;
	}

	/**
	 * This method is used to download file.<br/>
	 * This method takes two argument <br/>
	 * 1.) WebElement representing the download element<br/>
	 * 2.) Extension of the expected file i.e. pdf or csv or anything expected
	 * @param element
	 * @param expFileExtn // this is expected file extension like pdf or csv or jpg
	 * @return
	 * @throws Exception 
	 */
	public static String downLoadFile(Sherlock sherlock, WebElement element, String expFileExtn) throws Exception{

		String downloadFileLocation = sherlock.getDownloadFileLocation();

		int downloadTimeOutInSec = sherlock.getDownloadTimeOutInSec();

		String tempFileLocation = System.getProperty("user.home") + "/public/tmp/";
		String randomString = RandomStringUtils.randomAlphanumeric(8);
		tempFileLocation += randomString + "/";

		File directory = new File(downloadFileLocation	);
		if(!directory.exists()){
			directory.mkdirs();
		}

		File tempDirectory = new File(tempFileLocation);
		if(!tempDirectory.exists()){
			tempDirectory.mkdirs();
		}

		if(element == null){
			throw new Exception("Download element is null");
		}

		ScriptLogger.writeInfo("going to download file with extension -> " + expFileExtn);

		element.click();

		long startTime = new Date().getTime();

		File folder = null;

		File[] files = null;

		long diff = (new Date().getTime() - startTime)/1000;

		while(diff < downloadTimeOutInSec){
			Thread.sleep(2000);
			System.out.println("diff -> " + diff);
			folder = new File(downloadFileLocation);
			files = folder.listFiles();
			if(files.length > 0){
				String mimeType = Files.probeContentType(files[0].toPath());
				System.out.println(mimeType);
				String fileName = files[0].getName();
				if(mimeType != null && mimeType.toLowerCase().contains(expFileExtn) && fileName.toLowerCase().matches(".*\\." + expFileExtn)){
					files[0].renameTo(new File(tempFileLocation + fileName));
					directory.delete();
					return (tempFileLocation + fileName);
				}
			}
			diff = (new Date().getTime() - startTime)/1000;
		}
		return null;
	}



	/**
	 * @param ByType
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static By getByInstance(By ByType, String value) throws Exception{

		By byInstance = null;

		if(ByType instanceof ById){
			ScriptLogger.writeInfo("By instance of ById");
			byInstance = By.id(value);
		}
		else if(ByType instanceof ByClassName){
			ScriptLogger.writeInfo("By instance of ByClassName");
			byInstance = By.className(value);
		}
		else if(ByType instanceof ByLinkText){
			ScriptLogger.writeInfo("By instance of ByLinkText");
			byInstance = By.linkText(value);
		}
		else if(ByType instanceof ByName){
			ScriptLogger.writeInfo("By instance of ByName");
			byInstance = By.name(value);
		}
		else if(ByType instanceof ByTagName){
			ScriptLogger.writeInfo("By instance of ByTagName");
			byInstance = By.tagName(value);
		}
		else if(ByType instanceof ByPartialLinkText){
			ScriptLogger.writeInfo("By instance of ByPartialLinkText");
			byInstance = By.partialLinkText(value);
		}
		else if(ByType instanceof ByCssSelector){
			ScriptLogger.writeInfo("By instance of ByCssSelector");
			byInstance = By.cssSelector(value);
		}
		else if(ByType instanceof ByXPath){
			ScriptLogger.writeInfo("By instance of ByXPath");
			byInstance = By.xpath(value);
		}
		else{
			throw new Exception("By Type not supported");
		}

		return byInstance;

	}

	public static void switchToNewWindow(Sherlock sherlock){

		WebDriver driver = sherlock.getDriverObject();
		String parent=driver.getWindowHandle();

		// This will return the number of windows opened by Webdriver and will return Set of Strings
		Set<String>s1=driver.getWindowHandles();

		// Now we will iterate using Iterator
		Iterator<String> I1= s1.iterator();

		while(I1.hasNext())
		{
			String child_window=I1.next();

			// Here we will compare if parent window is not equal to child window then we            will close

			if(!parent.equals(child_window))
			{
				driver.switchTo().window(child_window);
			}
		}
	}

}
