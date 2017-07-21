package com.selenium.test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * @author kumar
 *
 */

@Component
@Scope("prototype")
public class Sherlock {

	@Autowired
    BeanFactory beanFactory;
	
	private WebDriver driver = null;
	
	private ACADriver acaDriver = null;

	public void startDriver() throws MalformedURLException{
		if(driver == null){
//			ScriptLogger.writeInfo("BEANFACTORY ::: " + beanFactory);
			ACADriver acaDriver = (ACADriver) beanFactory.getBean("ACADriver");
			this.acaDriver = acaDriver;
			driver = acaDriver.getDriver();
		}
	}

	/**
	 * returns WebDriver object. 
	 * Do not use the WebDriver object directly unless specifically required.
	 * @return
	 */
	public WebDriver getDriverObject(){
		return driver;
	}
	
	public void setDriver(WebDriver driver){
		this.driver = driver;
	}
	
	
	public int getDownloadTimeOutInSec(){
		int timeInSec = 0;
		if(acaDriver != null){
			timeInSec = acaDriver.getDownloadTimeOutInSec();
		}
		return timeInSec;
	}
	
	public void setDownloadTimeOutInSec(int downloadTimeOutInSec) {
		if(acaDriver != null){
			acaDriver.setDownloadTimeOutInSec(downloadTimeOutInSec);
		}
	}
	
	public String getDownloadFileLocation(){
		String location = null;
		if(acaDriver != null){
			location = acaDriver.getDownloadFileLocation();
		}
		return location;
	}
	
	
	public Alert getAlertBox(){
		Alert alert = null;
		ScriptLogger.writeInfo("getting alert box");
		
		try{
			Watson.sleep(3);
			alert = driver.switchTo().alert();
		}
		catch(NoAlertPresentException Ex){
			ScriptLogger.writeInfo("alert is not present");
		}
		
		return alert;
		
	}

	/**
	 * Let Sherlock find the element using the given id. 
	 * @param id
	 * @return
	 */
	public WebElement findElementById(String id){

		if(StringUtils.isEmpty(id)){
			ScriptLogger.writeError("Sherlock can't find it. The Id passed is not valid");
			return null;
		}

		return driver.findElement(By.id(id));
	}

	/**
	 * Let Sherlock find the element using the given name.
	 * @param name
	 * @return
	 */
	public WebElement findElementByName(String name){

		if(StringUtils.isEmpty(name)){
			ScriptLogger.writeError("Sherlock can't find it. The name passed is not valid");
			return null;
		}

		return driver.findElement(By.name(name));
	}

	/**
	 * Let Sherlock find all the element using the given name.
	 * @param name
	 * @return
	 */
	public List<WebElement> findElementsByName(String name){

		if(StringUtils.isEmpty(name)){
			ScriptLogger.writeError("Sherlock can't find them. The name passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.name(name));
	}

	/**
	 * Let Sherlock find the element using the given className.
	 * @param className
	 * @return
	 */
	public WebElement findElementByClassName(String className){

		if(StringUtils.isEmpty(className)){
			ScriptLogger.writeError("Sherlock can't find it. The className passed is not valid");
			return null;
		}

		return driver.findElement(By.className(className));
	}

	/**
	 * Let Sherlock find all the element using the given className.
	 * @param className
	 * @return
	 */
	public List<WebElement> findElementsByClassName(String className){

		if(StringUtils.isEmpty(className)){
			ScriptLogger.writeError("Sherlock can't find Them. The className passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.className(className));
	}

	/**
	 * Let Sherlock find the element using the given tagName.
	 * @param tagName
	 * @return
	 */
	public WebElement findElementByTagName(String tagName){

		if(StringUtils.isEmpty(tagName)){
			ScriptLogger.writeError("Sherlock can't find it. The tagName passed is not valid");
			return null;
		}

		return driver.findElement(By.tagName(tagName));
	}

	/**
	 * Let Sherlock find all the element using the given tagName.
	 * @param tagName
	 * @return
	 */
	public List<WebElement> findElementsByTagName(String tagName){

		if(StringUtils.isEmpty(tagName)){
			ScriptLogger.writeError("Sherlock can't find them. The tagName passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.tagName(tagName));
	}

	/**
	 * Let Sherlock find the element using the given linkText.
	 * the linkText is the text wrapped inside anchor tag.
	 * It matches the exact text.
	 * @param linkText
	 * @return
	 */
	public WebElement findElementByLinkText(String linkText){

		if(StringUtils.isEmpty(linkText)){
			ScriptLogger.writeError("Sherlock can't find it. The linkText passed is not valid");
			return null;
		}

		return driver.findElement(By.linkText(linkText));

	}

	/**
	 * Let Sherlock find all the element using the given linkText.
	 * the linkText is the text wrapped inside anchor tag.
	 * It matches the exact text.
	 * @param linkText
	 * @return
	 */
	public List<WebElement> findElementsByLinkText(String linkText){

		if(StringUtils.isEmpty(linkText)){
			ScriptLogger.writeError("Sherlock can't find them. The linkText passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.linkText(linkText));

	}

	/**
	 * Let Sherlock find the element using the given linkText.
	 * the linkText is the text wrapped inside anchor tag.
	 * It matches the partial text.
	 * @param linkText
	 * @return
	 */
	public WebElement findElementByPartialLinkText(String linkText){

		if(StringUtils.isEmpty(linkText)){
			ScriptLogger.writeError("Sherlock can't find it. The linkText passed is not valid");
			return null;
		}

		return driver.findElement(By.linkText(linkText));

	}

	/**
	 * Let Sherlock find all the element using the given linkText.
	 * the linkText is the text wrapped inside anchor tag.
	 * It matches the partial text.
	 * @param linkText
	 * @return
	 */
	public List<WebElement> findElementsByPartialLinkText(String linkText){

		if(StringUtils.isEmpty(linkText)){
			ScriptLogger.writeError("Sherlock can't find them. The linkText passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.linkText(linkText));

	}

	/**
	 * Let Sherlock find the element using the given xPath.
	 * Please check xPath carefully. It will throw runtime error in case of
	 * incorrect xpath 
	 * @param xPath
	 * @return
	 */
	public WebElement findElementByXPath(String xPath){

		if(StringUtils.isEmpty(xPath)){
			ScriptLogger.writeError("Sherlock can't find it. The xPath passed is not valid");
			return null;
		}

		return driver.findElement(By.xpath(xPath));

	}

	/**
	 * Let Sherlock find all the element using the given xPath.
	 * Please check xPath carefully. It will throw runtime error in case of
	 * incorrect xpath.
	 * @param xPath
	 * @return
	 */
	public List<WebElement> findElementsByXPath(String xPath){

		if(StringUtils.isEmpty(xPath)){
			ScriptLogger.writeError("Sherlock can't find them. The xPath passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.xpath(xPath));

	}

	/**
	 * Let Sherlock find the element using the given cssSelector.
	 * Please check cssSelector carefully. It will throw runtime error in case of
	 * incorrect cssSelector.
	 * @param cssSelector
	 * @return
	 */
	public WebElement findElementByCSSSelector(String cssSelector){

		if(StringUtils.isEmpty(cssSelector)){
			ScriptLogger.writeError("Sherlock can't find it. The cssSelector passed is not valid");
			return null;
		}

		return driver.findElement(By.cssSelector(cssSelector));

	}

	/**
	 * Let Sherlock find all the element using the given cssSelector.
	 * Please check cssSelector carefully. It will throw runtime error in case of
	 * incorrect cssSelector.
	 * @param cssSelector
	 * @return
	 */
	public List<WebElement> findElementsByCSSSelector(String cssSelector){

		if(StringUtils.isEmpty(cssSelector)){
			ScriptLogger.writeError("Sherlock can't find them. The cssSelector passed is not valid");
			return new ArrayList<WebElement>();
		}

		return driver.findElements(By.cssSelector(cssSelector));

	}

	
	public WebElement findTableElementContainingText(String text){

		return this.findElementByXPath("//*[contains(text(), '" + text + "')]/ancestor::table[1]");
	}

	public List<WebElement> findTableElementsContainingText(String text){

		return this.findElementsByXPath("//*[contains(text(), '" + text + "')]/ancestor::table");
	}

	/**
	 * Sherlock can also open the website URL.
	 * You just need to pass the correct URL.
	 * He will be angry and may throw an error if the URL is not in the standard format.
	 * @param url
	 */
	public void openURL(String url){
		if(StringUtils.isEmpty(url)){
			ScriptLogger.writeInfo("Sherlock feels blind. He can't see the URL.");
			return;
		}
		driver.get(url);
	}

	/**
	 * Use this method to ask Sherlock so that he can give you the outerHTML for the WebElement.
	 * @param element
	 * @return
	 */
	public String getOuterHTML(WebElement element){
		if(element == null){
			ScriptLogger.writeError("Please consult Rajnikant. Only he can find the outerHTML of null");
			return null;
		}

		return element.getAttribute("outerHTML");
	}

	/**
	 * Use this method to ask Sherlock so that he can give you the innerHTML for the WebElement.
	 * @param element
	 * @return
	 */
	public String getInnerHTML(WebElement element){
		if(element == null){
			ScriptLogger.writeError("Please consult Rajnikant. Only he can find the innerHTML of null");
			return null;
		}

		return element.getAttribute("innerHTML");
	}

	/**
	 * Use this method to ask Sherlock so that he can give you the text for the WebElement.
	 * But be careful if the text comes as an "" then it will give you innerHTML.
	 * 
	 * @param element
	 * @return
	 */
	public String getText(WebElement element){
		if(element == null){
			ScriptLogger.writeError("Please consult Rajnikant. Only he can get text from null");
			return null;
		}
		if(!element.getText().equals("")){
			return element.getText();
		}
		else{
			return getInnerHTML(element);
		}

	}
	
	
	public String getInnerText(WebElement element){
		if(element == null){
			ScriptLogger.writeError("Please consult Rajnikant. Only he can get text from null");
			return null;
		}
		return element.getAttribute("innerText");
	}
	
	
	public void setImplicitwait(int seconds){
		ScriptLogger.writeInfo("setting implicit wait to " + seconds + " seconds");
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	public void setPageLoadTimeOUt(int seconds){
		ScriptLogger.writeInfo("setting page load timeout to " + seconds + " seconds");
		driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
	}
	
	public void clickAndWaitForElement(WebElement element){
		
		ScriptLogger.writeInfo("clickAndWaitForElement");
		
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		ScriptLogger.writeInfo("page load timeout set to 60 sec");
		try{
			ScriptLogger.writeInfo("clicking");
			element.click();
			ScriptLogger.writeInfo("clicked on element");
		}
		catch(WebDriverException e){
			ScriptLogger.writeError("WebDriver exception raised");
		}
	}
	
	public void hoverOnElement(WebElement element){
		Actions builder = new Actions(driver);
		builder.moveToElement(element).build().perform();
	}
	
	public void sendInput(WebElement element, String value){
		element.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
	}
	
	/**
	 * This will close the contract between you and Sherlock and of course the driver(WebDriver).
	 */
	public void closeDriver(){

		try{
			driver.quit();

		}catch(Exception e){
			ScriptLogger.writeError( "e -> "+ e.getMessage());
			try{
				driver.close();
			}
			catch(Exception e1){
				ScriptLogger.writeError("e1 -> "+e1.getMessage());
			}

		}
	}

}
