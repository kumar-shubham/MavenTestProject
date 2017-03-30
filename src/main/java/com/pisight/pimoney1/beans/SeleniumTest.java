
//
// ybrowser.java: hosts the IE web browser control
//

// AFC


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.yodlee.dap.gatherer.api.JsoupUtil;
import com.yodlee.dap.gatherer.api.MySelect;
import com.yodlee.dap.gatherer.api.Robot;
import com.yodlee.dap.gatherer.browser.BrowserManager;
import com.yodlee.dap.gatherer.constants.DAPMessageConstants;
import com.yodlee.dap.gatherer.context.AgentContext;
import com.yodlee.dap.gatherer.enums.ElementsType;
import com.yodlee.dap.gatherer.gather.Constants;
import com.yodlee.dap.gatherer.gather.GathererRequest;
import com.yodlee.dap.gatherer.gather.GathererResponseItem;
import com.yodlee.dap.gatherer.gather.HiddenString;
import com.yodlee.dap.gatherer.gather.IYodInsecure.HiddenStringExtractor;
import com.yodlee.dap.gatherer.gather.IYodRobot;
import com.yodlee.dap.gatherer.gather.JController;
import com.yodlee.dap.gatherer.gather.LoginInfo;
import com.yodlee.dap.gatherer.gather.MFAImage;
import com.yodlee.dap.gatherer.gather.PropertyBagManager;
import com.yodlee.dap.gatherer.gather.RequestItem;
import com.yodlee.dap.gatherer.gather.SessionPromptRequest;
import com.yodlee.dap.gatherer.gather.SessionPromptResponse;
import com.yodlee.dap.gatherer.gather.User;
import com.yodlee.dap.gatherer.gather.YUtilities;
import com.yodlee.dap.gatherer.gather.content.Bill;
import com.yodlee.dap.gatherer.gather.content.Container;
import com.yodlee.dap.gatherer.gather.content.DocumentInfo;
import com.yodlee.dap.gatherer.gather.content.DocumentInfo.DocumentStatus;
import com.yodlee.dap.gatherer.gather.exceptions.GatherException;
import com.yodlee.dap.gatherer.gather.exceptions.GeneralException;
import com.yodlee.dap.gatherer.gather.exceptions.LoginException;
import com.yodlee.dap.gatherer.gather.exceptions.NoLinkFoundException;
import com.yodlee.dap.gatherer.gather.exceptions.TimeoutException;
import com.yodlee.dap.gatherer.sanitizer.TextSanitizer;
import com.yodlee.dap.gatherer.util.DocumentConstants;
import com.yodlee.dap.gatherer.util.GenUtil;
import com.yodlee.dap.gatherer.util.GlobalParamConstants;
import com.yodlee.dap.gatherer.validationutils.YDataLogger;
import com.yodlee.dap.gatherer.ylogger.YSystem;

import mshtml.IHTMLDocument2;
import mshtml.IHTMLElement;
import mshtml.IHTMLElementCollection;
import mshtml.IHTMLFormElement;
import mshtml.IHTMLFrameElement;
import mshtml.IHTMLSelectElement;
import mshtml.IHTMLTable;
import shdocvw.IWebBrowser2;
import yodlee.html.Form;
import yodlee.html.event.Event;
import yodlee.html.event.EventType;
import yodlee.util.JavaComBridgeHelper;
import yodlee.util.StringUtil;

public class SeleniumTest implements IYodRobot, Constants {

	public static final int SEARCH_TEXT = 0;

	public static final int SEARCH_HTML = 1;

	public static final String TABLE_ROW="tr";

	public static final String TABLE_COLUMN="td";
	public static final String TABLE_COLUMNS = "td,th";

	private boolean dumpOnlyOnPageLoad = false;

	public static final int TEXT = 1;
	public static final int RADIO = 2;
	public static final int CHECKBOX = 3;
	public static final int SUBMIT = 4;
	public static final int IMAGE = 5;
	public static final int SELECT = 6;
	public static final int TEXTAREA = 7;
	public static final int UNKNOWN = 0;

	protected RequestItem m_pRequestItem;	
	YUtilities m_pUtil;

	final int expectedStringTimeout = Integer.getInteger(
			"yodlee.gatherer.jgatherer.expectedTextTimeout_seconds", 1);

	IHTMLDocument2 m_pDocument;

	// This is the lower bound on the timeout in milliseconds.
	private volatile long minTimeout;

	// This is the upper bound on the timeout in milliseconds.
	private long maxTimeout;


	// This is the default document timeout in seconds if the
	// "yodlee.gatherer.document_timeout" is not set.
	public static final int DOCUMENT_TIMEOUT = 120;

	// This is the default maximum document timeout in seconds if the
	// "yodlee.gatherer.max_document_timeout" is not set.
	public static final int MAX_DOCUMENT_TIMEOUT = 300;

	// This is the "bonus" increment given to a navigation for making
	// progress.
	private long timeoutIncrement;

	private boolean m_popUpWindowFlag = true;


	/**
	 * The expect conditions for each GET is placed in a queue. When the entire
	 * page is loaded, the expect conditions are removed from the queue.
	 */
	private ArrayList<String> expectedTexts = null;

	String m_cryptoName = null;

	// If a userParam has this prefix, the value should not be
	// decrypted.
	public final String CLEARTEXT_INDICATOR = "cleartext:";

	// If true, Iframes will be traversed. The default is false.
	protected boolean traverseIframes = false;

	// If true, Frames will be traversed. The default is true.
	protected boolean traverseFrames = false;

	private String pageText = null;

	protected GathererRequest m_pGathererRequest;

	private WebDriver driver;

	private HashMap<String,HashMap<Bill,DownloadResponse>> billUploadDetails = null;

	private HashMap<Bill,String> billDownloadStatus = null;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	private WebElement findWebElement(String path) {
		List<WebElement> element = webElementFinder(path);
		if (element != null && !element.isEmpty()) {
			return element.get(0);
		} else {
			YSystem.out("no element Found");
			return null;
		}

	}

	public WebElement getWebEle(String path) {
		if (path.startsWith("//")) {
			return driver.findElement(By.xpath(path));
		} else {
			return driver.findElement(By.cssSelector(path));
		}
	}

	private List<WebElement> webElementFinder(String path) {
		// TODO Auto-generated method stub
		// FIXME Replace driver here
		List<WebElement> webElementList = null;
		boolean xPath = false;
		if (path.startsWith("//")) {
			xPath = true;
		}
		if (!xPath) {
			webElementList = driver.findElements(By.cssSelector(path));
		} else {
			webElementList = driver.findElements(By.xpath(path));
		}
		return webElementList;
	}

	private List<WebElement> webElementFinder(WebDriver driver,String path) {
		// TODO Auto-generated method stub
		// FIXME Replace driver here
		List<WebElement> webElementList = null;
		boolean xPath = false;
		if (path.startsWith("//")) {
			xPath = true;
		}
		if (!xPath) {
			webElementList = driver.findElements(By.cssSelector(path));
		} else {
			webElementList = driver.findElements(By.xpath(path));
		}
		return webElementList;
	}


	private String fetchWebElement1(WebElement elem) {
		String elementString = "";
		String path="";
		WebElement webEle = null;
		if (elem != null)
			elementString = elem.toString();
		if (elementString.length() > 0
				&& (elementString.contains("xpath") || elementString
						.contains("css selector"))) {
			path = elementString.substring(
					elementString.indexOf("-> ") + 3,
					elementString.length() - 1);
			path = path.substring(path.indexOf(": ") + 2);

		}
		return path;
	}

	private List<WebElement> webElementFinder(WebElement ele, String path) {
		// TODO Auto-generated method stub

		List<WebElement> webEleList = null;

		boolean xPath = false;
		if (path.startsWith("//")) {
			xPath = true;
		}
		if (!xPath) {
			webEleList = ele.findElements(By.cssSelector(path));
		} else {
			webEleList = ele.findElements(By.xpath(path));
		}
		return webEleList;
	}

	private boolean isAttributeValueFound(IHTMLDocument2 document,
			String tagName, String attributeName, String attributeValue) {

		/*if (attributeName != null && attributeName.equalsIgnoreCase("innertext")) {
			String newPath = "//" + tagName + "[contains(.,'" + attributeValue
					+ "')]";
			WebElement webElement = getWebElement(newPath);
			if (webElement != null) {
				return true;
			}
		}
		String path = pathBuilder(tagName, attributeName, attributeValue,false);
		WebElement webEle = getWebElement(path);
		if (webEle == null) {
			return false;
		}
		if (webEle.getAttribute(attributeName) == attributeValue) {
			return true;
		} else {
			return false;
		}
		 */
		return isAttributeValueFoundOnPage(tagName, attributeName, attributeValue, false, false);
	}

	@Override
	public boolean isAttributeValueFoundOnPage(String tagName,
			String attributeName, String attributeValue, boolean caseSensitive,
			boolean matchExactly) {
		long start = System.currentTimeMillis();
		String path = pathBuilder(tagName, attributeName, attributeValue,
				matchExactly);
		List<WebElement> list = webElementFinder(path);
		String value = null;
		if (list != null && !list.isEmpty()) {
			for (WebElement ele : list) {
				if(attributeName.equalsIgnoreCase("innertext")){
					value = Jsoup.parse(ele.getAttribute("outerHTML")).text();
				}else{
					value = ele.getAttribute(attributeName);
				}

				if (matchExactly) {
					if ((caseSensitive && value.equals(attributeValue))
							|| (!caseSensitive && value
									.equalsIgnoreCase(attributeValue))) {
						System.out.println("total time taken to find element: "+(System.currentTimeMillis()-start));
						return true;
					}
				} else {
					if ((caseSensitive && value.contains(attributeValue))
							|| (!caseSensitive && value.toLowerCase().contains(
									attributeValue.toLowerCase()))) {
						System.out.println("total time taken to find element: "+(System.currentTimeMillis()-start));
						return true;
					}
				}
			}
		}
		System.out.println("total time taken to find element: "+(System.currentTimeMillis()-start));
		return false;
	}

	public WebElement getVisibleElementFromList(List<WebElement> elemList){
		WebElement webElem = null;
		for(WebElement elem : elemList){
			if(elem.isDisplayed()){
				webElem = elem;
				break;
			}
		}
		return webElem;
	}

	@Override
	public WebElement getWebElement(String path){
		WebElement webEle = null;
		List<WebElement> elemList = null;
		if(isTraverseFrames() || isTraverseIframes()){
			elemList = traverseAllFrames(path,true);
			if(elemList != null && elemList.size() > 0){
				//If the site is not having frames , driver.findElements is used to get the webElement list.
				//But this list will be containing hidden elements as well, hence invalidelementstate exception will 
				//occur when hidden element is used for user actions. Following code is to get the
				//visible element from the list. If none are visible will return null.
				webEle = getVisibleElementFromList(elemList);
				//webEle = elemList.get(0);

			}
		}else{
			//To : do revisit the code and use getVisibleElementFromList() if required.
			webEle = findWebElement(path);
		}
		return webEle;
	}

	public boolean isTextFoundOnPage(IHTMLDocument2 document, String text,
			boolean caseSensitive) {
		long start = System.currentTimeMillis();
		boolean isTextPresent = false;
		/*try {
			// FIXME replace this to browser manager driver
			if (caseSensitive) {
				findElementsByText(text, caseSensitive);
				isTextPresent = true;
			} else {
				findElementsByText(text, caseSensitive);
				isTextPresent = true;

			}
		} catch (Exception e) {
			isTextPresent = false;
		}*/

		/*isTextPresent = containsText(driver, text, caseSensitive);	
		String pageSource = driver.getPageSource();
		System.out.println("pagesource: "+pageSource);
		if(!isTextPresent && (isTraverseFrames() || isTraverseIframes())){
			isTextPresent = isTextFound(driver, text, caseSensitive,false);
		}*/

		String pageText = getPageText();
		if((caseSensitive && pageText.contains(text)) || (!caseSensitive && pageText.toLowerCase().contains(text.toLowerCase()))){
			isTextPresent = true;
		}

		System.out.println("total time taken for finding the text on page: "+(System.currentTimeMillis()-start));
		return isTextPresent;
	}

	public boolean isTextFound(WebDriver driver,String text,boolean caseSensitive,boolean isParentFrame){
		List<WebElement> findElements = driver.findElements(By.tagName("iframe"));
		List<WebElement> findElements2 = driver.findElements(By.tagName("frame"));
		boolean isTextPresent = false;
		if(findElements.size()==0 && findElements2.size()==0){
			return false;
		}else{
			for(WebElement ele:findElements){
				WebDriver frame = driver.switchTo().frame(ele);
				if(containsText(frame, text, caseSensitive)){
					isTextPresent = true;
					this.driver.switchTo().defaultContent();
					break;
				}else{
					isTextPresent = isTextFound(frame, text, caseSensitive,true);
				}
				if(isParentFrame){
					driver.switchTo().parentFrame();
				}else{
					driver.switchTo().defaultContent();
				}
			}

			for(WebElement ele:findElements2){
				WebDriver frame = driver.switchTo().frame(ele);
				if(containsText(frame, text, caseSensitive)){
					isTextPresent = true;
					this.driver.switchTo().defaultContent();
					break;
				}else{
					isTextPresent = isTextFound(frame, text, caseSensitive,true);
				}
				if(isParentFrame){
					driver.switchTo().parentFrame();
				}else{
					driver.switchTo().defaultContent();
				}
			}
		}
		return isTextPresent;
	}

	public boolean containsText(WebDriver driver,String text,boolean caseSensitive){
		String visibleText = null;
		try{
			WebElement bodyElement = driver.findElement(By.tagName("body"));
			visibleText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText || arguments[0].textContent;", bodyElement);
			if(caseSensitive && visibleText.contains(text)){
				return true;
			}else if(visibleText.toLowerCase().contains(text.toLowerCase())){
				return true;
			}
		}catch(WebDriverException e){
			//YDataLogger.out("Error while getting text of body tag of page");
			return false;
		}
		return false;
	}

	public boolean findElementsByHtml(String text, boolean caseSensitive) {
		List<WebElement> webEleList = null;
		if (caseSensitive) {
			if (isTraverseFrames() || isTraverseIframes()) {
				webEleList = traverseAllFrames("//*/body", false);
				if (!GenUtil.isEmpty(webEleList)) {
					for (WebElement eacEle : webEleList) {
						String outerHTML = eacEle.getAttribute("outerHTML");
						if (outerHTML.indexOf(text) != -1) {
							return true;
						}
					}
					return false;
				} else {
					return false;
				}
			} else {
				webEleList = driver.findElements(By.xpath("//*/body"));
				if (!GenUtil.isEmpty(webEleList)) {
					WebElement bodyEle = webEleList.get(0);
					String outerHTML = bodyEle.getAttribute("outerHTML");
					if (outerHTML.indexOf(text) != -1) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

		} else {
			if (isTraverseFrames() || isTraverseIframes()) {
				webEleList = traverseAllFrames(("//*/body"), false);
				if (!GenUtil.isEmpty(webEleList)) {
					for (WebElement eacEle : webEleList) {
						String outerHTML = eacEle.getAttribute("outerHTML").toLowerCase();
						if (outerHTML.indexOf(text.toLowerCase()) != -1) {
							return true;
						}
					}
					return false;
				} else {
					return false;
				}
			} else {
				webEleList = driver.findElements(By.xpath("//*/body"));
				if (!GenUtil.isEmpty(webEleList)) {
					WebElement bodyEle = webEleList.get(0);
					String outerHTML = bodyEle.getAttribute("outerHTML").toLowerCase();
					if (outerHTML.indexOf(text.toLowerCase()) != -1) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
	}

	public List<WebElement> findElementsByText(String text,boolean caseSensitive){
		List <WebElement> webEleList = null;
		if(caseSensitive){
			if(isTraverseFrames() || isTraverseIframes()){
				webEleList = traverseAllFrames("//*[contains(.,'"+text+"')]",false);
			}else{
				webEleList = driver.findElements(By.xpath("//*[contains(.,'"+text+"')]"));
			}

		}else{
			if(isTraverseFrames() || isTraverseIframes()){
				webEleList = traverseAllFrames(("//*[contains(translate(.,'"+text.toUpperCase()+"', '"+text.toLowerCase()+"'),'"+text.toLowerCase()+"')]"),false);
			}else{
				webEleList = driver.findElements(By.xpath("//*[contains(translate(.,'"+text.toUpperCase()+"', '"+text.toLowerCase()+"'),'"+text.toLowerCase()+"')]"));
			}
		}

		return webEleList;

	}


	public boolean isTextFoundOnPage(String text, boolean caseSensitive) {

		/*boolean isTextPresent = false;
		try {
			// FIXME replace this to browser manager driver
			if (caseSensitive) {
				if(!GenUtil.isEmpty(findElementsByText(text, caseSensitive))){
					isTextPresent = true;
				}else{
					isTextPresent =  false;
				}
			} else {

				if(!GenUtil.isEmpty(findElementsByText(text, caseSensitive))){
					isTextPresent = true;
				}else{
					isTextPresent = false;
				}
			}
		} catch (Exception e) {
			isTextPresent = false;
		}*/

		return isTextFoundOnPage(null, text, caseSensitive);

	}

	public void click2(boolean sendEvents, String tag, String attrib, String val) {
		// FIXME : replace driver
		long start = System.currentTimeMillis();
		String path = pathBuilder(tag, attrib, val,false);
		System.out.println("path = "+path);
		//WebElement ele = getWebElement(path);
		WebElement ele = null;
		try{
			ele = getWebEle(path);
		}catch(NoSuchElementException ex){
			YSystem.out("No element is present with tag as "+tag+" with attribute name as "+attrib+" and attribute value as "+val);
			throw ex;
		}
		if (ele.isEnabled()) {
			try{
				YSystem.out("Going to perform click action on the element with path :"
						+ path);
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				// FIXME need to come back and make this configurable
				// driver.manage().timeouts().pageLoadTimeout(GathererProperties.getIntProperty("yodlee.gatherer.document_timeout",
				// 30), TimeUnit.SECONDS);
				ele.click();
				setPageText(null);
				int count = 1;

				if(isTraverseFrames() || isTraverseIframes())
					driver.switchTo().defaultContent();
				YSystem.out("Performed Click Action on element with path : " + path);
			}catch(ElementNotVisibleException e){
				YSystem.out("Got ElementNotVisibleException while clicking on element with tag-attribute-value <"+tag+">-"+attrib+"-"+val+" hence retrying using Javascript");
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
				if(isTraverseFrames() || isTraverseIframes()){
					driver.switchTo().defaultContent();
				}
				setPageText(null);
				YSystem.out("Performed Click Action on element with path : " + path);
				YSystem.out("total time taken for click is: "+(System.currentTimeMillis()-start));
			}
		} else {
			YSystem.out("Element is disabled / no click will happen");
		}

	}

	private boolean waitForExpectedStrings() {
		boolean isFound = false;
		if(expectedTexts != null){
			for (String text : expectedTexts) {
				if (isTextFoundOnPage(text, false)) {
					isFound = true;
					break;
				} 
			}
		}
		return isFound;
	}


	public String pathBuilder(String tag, String attrib, String val, boolean matchExactly) {
		// TODO Auto-generated method stub
		if(attrib.equals("classname"))
		{
			attrib="class";
		}

		if (null != attrib && attrib.toLowerCase().equals("innertext")) {
			// Generating and returning XPATH for innerText or Text based
			// matches
			StringBuilder sb = new StringBuilder();
			sb.append("//");
			sb.append(tag);
			sb.append("[contains(.,'");
			sb.append(val+"')");
			sb.append("]");
			String path = sb.toString();
			return path;

		} else {
			// Generating CSS for all others
			StringBuilder sb = new StringBuilder();
			sb.append(tag);
			if (attrib != null) {
				sb.append("[");
				sb.append(attrib);
				if(matchExactly){
					sb.append("=\"");
				}else{
					sb.append("=\"");
				}
				sb.append(val);
				sb.append("\"]");
			} else {
				sb.append("");
			}
			String path = sb.toString().trim();

			return path;
		}

	}

	public void clickWithEvents(IHTMLElement element) {
		click( element,true);
	}

	/**
	 * Clicks an element with the given tag, and attribute value. In addition to
	 * the click event, the following events will be fired by this API:
	 * mouseover, mousedown, mouseup, mouseout.
	 * 
	 * @param tag
	 *            the tag of the elememt
	 * @param attrib
	 *            the attribute to look for
	 * @param val
	 *            value of the attribute.
	 */
	public void clickWithEvents(IHTMLElement element, boolean wait) {
		click(element, wait);
	}


	/**
	 * Clicks an element with the given tag, and attribute value. In addition to
	 * the click event, the following events will be fired by this API:
	 * mouseover, mousedown, mouseup, mouseout.
	 * 
	 * @param tag
	 *            the tag of the elememt
	 * @param attrib
	 *            the attribute to look for
	 * @param val
	 *            value of the attribute.
	 */
	public void clickWithEvents(String tag, String attrib, String val) {
		clickWithEvents(tag, attrib, val, true);
	}

	/**
	 * Clicks an element with the given tag, and attribute value. In addition to
	 * the click event, the following events will be fired by this API:
	 * mouseover, mousedown, mouseup, mouseout.
	 * 
	 * @param tag
	 *            the tag of the elememt
	 * @param attrib
	 *            the attribute to look for
	 * @param val
	 *            value of the attribute.
	 * @param wait
	 *            if true, the method will wait until the document completes;
	 *            otherwise; it will return immediately.
	 */
	public void clickWithEvents(String tag, String attrib, String val,
			boolean wait) {
		if (!wait) {
			click2(true, tag, attrib, val);
		} else {
			//resetBeforeNavigate();
			click2(true, tag, attrib, val);
			waitForDocumentComplete();
		}
	}

	/**
	 * Clicks on an HTML element. This method will return after the document
	 * completes. The method will click the element twice if the first timed
	 * out.
	 */
	public void click(IHTMLElement element) {
		click( element, false);
	}

	public void selectOption(boolean sendEvents, String attributeName,
			String optionAttribute, String optionValue, boolean caseSensitive,
			boolean matchExactly) {
		selectOption("name", attributeName, optionAttribute, optionValue, matchExactly);
	}

	public void get(String url) {
		Robot.get(url, null);
		int count = 1;
		while (count <= expectedStringTimeout) {
			if (waitForExpectedStrings()) {
				expectedTexts = null;
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				count++;
			}
		}
		setPageText(null);
	}


	public void clickLinkWithText(String text, boolean wait) {
		click("a", "innertext", text, wait);
	}

	public void clickLinkWithHref(String href) {
		click("a", "href", href);
	}

	public void clickLinkWithText(String value) {
		click("a", "innertext", value);
	}

	public boolean clickButtonWithValue(String value) {
		click("input", "value", value);
		return true;
	}

	public void clickInputWithName(String name) {
		click("input", "name", name);
	}

	public Enumeration getElementsWithTag(String tag) {

		// FIXME : replace driver here..
		Vector eleVector = new Vector();
		List<WebElement> webEleList;
		webEleList = driver.findElements(By.tagName(tag));

		for (WebElement webEle : webEleList) {
			eleVector.add(webEle);
		}
		// FIXME not sure about the enumeration here too.
		return eleVector.elements();

	}

	public void switchToFrame(String frameName,String elementToFind,ElementsType elementSelectionType){
		try {
			Robot.switchToFrame(frameName, elementToFind, elementSelectionType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			YSystem.out(e.getMessage()); 
		}

	}

	public void switchToFrame(String tagName,String attributeName,String attributeValue){
		String path = pathBuilder(tagName, attributeName, attributeValue, false);
		WebElement ele = getWebElement(path);
		driver = driver.switchTo().frame(ele);
		YDataLogger.out("switched to the frame successfully");
	}


	public void switchToFrame(String frameName){
		driver = driver.switchTo().frame(driver.findElement(By.name(frameName)));
		YDataLogger.out("switched to the frame successfully");
	}

	public void switchToDefault(){
		driver = driver.switchTo().defaultContent();
	}


	public Enumeration getElementsWithTag(IHTMLElement e, String tag) {
		// FIXME : come and fix this casting , discuss with anjani
		WebElement ele = (WebElement) e;
		Vector elemcollection = new Vector();
		List<WebElement> webEleList = ele.findElements(By.tagName(tag));

		if (webEleList != null && !webEleList.isEmpty()) {
			for (WebElement webEle : webEleList) {
				elemcollection.add(webEle);
			}
		}

		return elemcollection.elements();

		// FIXME not sure about this can we directly return the enumeration like
		// above
		/*
		 * IHTMLElementCollection coll =
		 * JavaComBridgeHelper.toIHTMLElementCollection(e.getAll());
		 * IHTMLElementCollection pCollection =
		 * JavaComBridgeHelper.toIHTMLElementCollection(coll .tags(new
		 * Variant(tag))); return (new HTMLElementEnumeration(pCollection));
		 */
	}

	public boolean isTextFoundOnPage2(String text, boolean caseSensitive) {

		return isTextFoundOnPage(text, caseSensitive);
	}

	public Enumeration getElementsContaining(IHTMLElement element,
			String elemtag, String attrib, String value) {
		Vector elemcollection = new Vector();
		// FIXME : check this conversion
		WebElement ele = element.getWebElement();

		String path = pathBuilder(elemtag, attrib, value, false);
		List<WebElement> webEleList = webElementFinder(ele, path);

		/*
		 * if(isTraverseFrames() || isTraverseIframes()){ webEleList =
		 * traverseAllFrames(path,false); }else{ webEleList =
		 * webElementFinder(path); }
		 */

		if (webEleList != null) {
			for (WebElement webEle : webEleList) {
				elemcollection.add(new IHTMLElement(webEle));
			}
		}

		// FIXME : come and check what is this below and if it affects the
		// enumeration.
		/*
		 * IHTMLElementCollection coll =
		 * JavaComBridgeHelper.toIHTMLElementCollection(element.getAll());
		 * IHTMLElementCollection tags =
		 * JavaComBridgeHelper.toIHTMLElementCollection(coll .tags(new
		 * Variant(elemtag))); int len = tags.getLength(); for (int elemnum = 0;
		 * elemnum < len; elemnum++) { IHTMLElement elem =
		 * JavaComBridgeHelper.toIHTMLElement(tags.item(new Variant(elemnum)));
		 * if (attrib == null) { elemcollection.addElement(elem); continue; } if
		 * (!"innertext".equals(attrib) && !"innerhtml".equals(attrib)) {
		 * Variant attribute = elem.getAttribute(attrib, 0); if (attribute !=
		 * null && attribute.toString() != null &&
		 * attribute.toString().toLowerCase().indexOf( value.toLowerCase()) >=
		 * 0) { elemcollection.addElement(elem); } } else if
		 * ("innertext".equals(attrib)) { String attribute =
		 * elem.getInnerText(); if (attribute != null &&
		 * attribute.toLowerCase().indexOf(value.toLowerCase()) >= 0) {
		 * elemcollection.addElement(elem); } } else if
		 * ("innerhtml".equals(attrib)) { String attribute =
		 * elem.getInnerHTML(); if (attribute != null &&
		 * attribute.toLowerCase().indexOf(value.toLowerCase()) >= 0) {
		 * elemcollection.addElement(elem); } } }
		 */

		return elemcollection.elements();
	}

	@Override
	public int getAuthenStatus(HiddenString userName, HiddenString password,
			String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void get(HiddenString url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(String url, HiddenString body) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(String field, HiddenString value) {
		String val= HiddenStringExtractor.extractHiddenString(value);
		setInputAny(field, val);
	}

	@Override
	public void setInputAny(String field, HiddenString value) {
		// TODO Auto-generated method stub
		setInputAny(field,value.toString());
	}

	@Override
	public void setInput(String formName, String field, HiddenString value) {
		// TODO Auto-generated method stub
		String val= value.toString();

		if (val != null) {
			val = val.trim();
		}

		WebElement elemList = driver.findElement(By.tagName(formName));

		WebElement ele = elemList.findElement(By.cssSelector("input[name="+field+"]"));
		ele.clear();
		ele.sendKeys(val);
		if (isTraverseFrames() || isTraverseIframes())
			driver.switchTo().defaultContent();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		YSystem.out("FillForm Complete");
	}

	@Override
	public boolean setInputTextElementWithText(String text, HiddenString value) {
		// TODO Auto-generated method stub
		return false;
	}

	public void selectOption(String attributeName, String optionAttribute,
			HiddenString optionValue, boolean caseSensitive,
			boolean matchExactly) {
		if(optionValue != null)
			selectOption("name", attributeName, optionAttribute, HiddenStringExtractor.extractHiddenString(optionValue), matchExactly);
		else
			YSystem.out(2,"optionValue is NULL");
	}


	@Override
	public void selectOption(String attribute, String value,
			String optionAttribute, HiddenString optionValue) {
		if(optionValue != null)
			selectOption(attribute,value, optionAttribute, HiddenStringExtractor.extractHiddenString(optionValue), false);
		else
			YSystem.out(2,"optionValue is NULL");
	}

	@Override
	public void set(String name, HiddenString value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void submitForm(String action, String method, String target,
			Hashtable values, Hashtable hiddenValues) {
		Hashtable h = new Hashtable();

		for (Enumeration keys = values.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			String value = (String) values.get(key);
			h.put(key, value);
		}

		if (hiddenValues != null) {
			for (Enumeration keys = hiddenValues.keys(); keys.hasMoreElements();) {
				String key = (String) keys.nextElement();
				String value = ((HiddenString) hiddenValues.get(key))
						.getHiddenString();
				h.put(key, value);
			}
		}
		this.submitForm(action, method, target, h);

	}

	/**
	 * Find the frame element with name, and switching to that.First looking for "frame", if not found 
	 * try finding "IFrame".
	 * 
	 * @param Frame Name
	 */
	@Override
	public void chooseFrame(String name) {
		if (name != null) {
			// Fetching frame with name
			String path = pathBuilder("frame", "name", name, false);
			driver.switchTo().defaultContent();
			WebElement elem = findWebElement(path);
			if (elem != null) {
				try{
					driver.switchTo().frame(elem);
				}catch(Exception e){
					YSystem.out(2, "Got Exception while choosing the frame Element. Skipping since the exception is not breaking anything and is expected");
				}
			} else {
				path = pathBuilder("iframe", "name", name, false);
				elem = findWebElement(path);
				if (elem != null) {
					try {
						driver.switchTo().frame(elem);
					} catch (Exception e) {
						YSystem.out(2, "Got Exception while choosing the iframe Element. Skipping since the exception is not breaking anything and is expected");
					}
				}
			}
		}
	}

	@Override
	public void setDocumentCompleteVar(boolean value) {
		// TODO Auto-generated method stub

	}


	public String getContentText() {
		return driver.getPageSource();
	}

	@Override
	public IHTMLTable getLastTableContainingText(String[] text) {
		//logic: count table and then iterate in reverse order checking if we have text present inside it
		List<WebElement> allTables = driver.findElements(By.tagName("table"));
		int j, n = text.length;
		for(int i = allTables.size() - 1; i>=0; --i )
		{
			WebElement webEle =  allTables.get(i);
			String checkText = webEle.getText();
			if (checkText == null)
				continue;
			for (j = 0; j < n; j++) {
				if (checkText.indexOf(text[j]) == -1)
					break;
			}
			if (j == n) {
				return JavaComBridgeHelper.toIHTMLTable(webEle);
			}
		}
		return null;
	}



	@Override
	public IHTMLTable getTableAfterTableContainingText(String[] text) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Fetch the WebElement from the DOM, using the selector used in the WebElement passed through argument
	 * 
	 * @param elem
	 * @return webEle
	 */
	private WebElement fetchWebElement(WebElement elem) {
		String elementString = "";
		WebElement webEle = null;
		if (elem != null)
			elementString = elem.toString();
		if (elementString.length() > 0
				&& (elementString.contains("xpath") || elementString
						.contains("css selector"))) {
			String path = elementString.substring(
					elementString.indexOf("-> ") + 3,
					elementString.length() - 1);
			path = path.substring(path.indexOf(": ") + 2);
			webEle = getWebElement(path);
		}
		return webEle;
	}

	@Override
	public Vector<IHTMLElement> getTableRowCellElements(IHTMLElement e,
			int rownum, int cellnum) {

		// Manipulating the given indexes, since xpath indexes start from 0.
		if (rownum > 0)
			rownum--;
		if (cellnum > 0)
			cellnum--;

		WebElement elem = e.getWebElement();
		WebElement webEle = null;
		Vector<IHTMLElement> v = new Vector<IHTMLElement>();

		// Element passed through argument is throwing staleElement exception,
		// hence re-fetching from the DOM.Can remove this
		// once the above bug is fixed.
		webEle = fetchWebElement(elem);

		if (webEle != null) {
			// Keeping OR condition: one with tbody another without. Return top
			// level <tr> nodes.
			List<WebElement> tableRows = webEle.findElements(By
					.xpath("./tbody/tr | ./tr"));
			if(tableRows != null && tableRows.size() > 0)
				webEle = tableRows.get(rownum);

			// Return top level <td> nodes
			List<WebElement> columns = webEle.findElements(By.xpath("./td"));
			if(columns != null && columns.size() > 0)
				webEle = columns.get(cellnum);

			// Return all top level child nodes.
			columns = webEle.findElements(By.xpath("./*"));

			for (WebElement e1 : columns) {
				// Since RemoteWebElement is returned from findElements method,
				// need to create IHTMLWebElement explicitly
				IHTMLElement iEle = new IHTMLElement(e1);
				v.add(iEle);
			}
		}
		return v;
	}

	@Override
	public String getTableRowCellTextBeforeFirstLink(IHTMLTable e, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableRowCellTextOfFirstLink(IHTMLTable e, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tableRowContainsElement(IHTMLTable e, int rownum, String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tableRowCellContainsElement(IHTMLTable e, int rownum,
			int cellnum, String tag) {
		WebElement element= e.getWebElement();
		WebElement elem;
		try{
			List<WebElement> tableRows = element.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName(TABLE_COLUMNS)).get(cellnum).findElements(By.tagName(tag));
			elem =tableRows.get(tableRows.size()-1);
			return true;
		}
		catch(Exception ex){
			YSystem.out("data not present");
			return false;
		}
	}

	@Override
	public IHTMLElement getTableRowCellUrl(IHTMLTable e, int rownum, int cellnum) {
		// TODO Auto-generated method stub
		WebElement element= e.getWebElement();
		WebElement elem;
		try{
			List<WebElement> tableRows = element.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName(TABLE_COLUMNS)).get(cellnum).findElements(By.tagName("a"));
			elem =tableRows.get(tableRows.size()-1);
			return (IHTMLElement)elem;
		}
		catch(Exception ex){
			YSystem.out("data not present");
			return null;
		}

	}

	@Override
	public Vector getTableRowCellLinks(IHTMLElement table, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub

		WebElement ele=table.getWebElement();
		Vector links =new Vector();
		List<WebElement> tablerows=ele.findElements(By.tagName("tr"));
		WebElement webele;
		webele=tablerows.get(rownum);
		List<WebElement> cells=webele.findElements(By.cssSelector("th,td"));
		WebElement link=cells.get(cellnum);
		List<WebElement> a=link.findElements(By.cssSelector("a"));
		for(int i=0;i<a.size();i++){
			if(a.get(i).getAttribute("href")!=null){
				links.add(a.get(i).getAttribute("href"));
			}
		}

		return links;  	  


	}

	@Override
	public Vector getTableRowCellLinkText(IHTMLElement table, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableRowCellTextBeforeFirstLink(IHTMLElement e,
			int rownum, int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableRowCellTextOfFirstLink(IHTMLElement e, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tableRowContainsElement(IHTMLElement e, int rownum,
			String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tableRowCellContainsElement(IHTMLElement e, int rownum,
			int cellnum, String tag) {
		WebElement element= e.getWebElement();
		WebElement elem;
		try{
			List<WebElement> tableRows = element.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName(TABLE_COLUMNS)).get(cellnum).findElements(By.tagName(tag));
			elem =tableRows.get(tableRows.size()-1);
			return true;
		}
		catch(Exception ex){
			YSystem.out("data not present");
			return false;
		}
	}

	@Override
	public IHTMLElement getTableRowCellUrl(IHTMLElement e, int rownum,
			int cellnum) {
		// TODO Auto-generated method stub
		WebElement element= e.getWebElement();
		WebElement elem;
		try{
			List<WebElement> tableRows = element.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName(TABLE_COLUMNS)).get(cellnum).findElements(By.tagName("a"));
			elem =tableRows.get(tableRows.size()-1);
			return (IHTMLElement)elem;
		}
		catch(Exception ex){
			YSystem.out("data not present");
			return null;
		}

	}

	private Enumeration getInnermostTables(String text, boolean caseSensitive,
			int row, int textOrHtml) {
		long start = System.currentTimeMillis();
		if (text.equals("") || text == null)
			throw new IllegalArgumentException(
					"The input string to getInnermostTables is empty string or null");

		Vector eleVector = new Vector();
		String path = null;
		if(caseSensitive){
			path = "//table[contains(.,'"+text+"')]";		
		}else{
			path = "//table[contains(translate(.,'"+text.toUpperCase()+"', '"+text.toLowerCase()+"'),'"+text.toLowerCase()+"')]";
		}

		List<WebElement> listEle = webElementFinder(path);

		if(listEle!=null && listEle.size()>0){
			for(WebElement ele:listEle){
				if(! ele.getAttribute("innerHTML").toLowerCase().contains("<table")){
					eleVector.add(new IHTMLElement(ele, ele.getAttribute("outerHTML"), null));
				}
			}
		}

		/*ArrayList<IHTMLElement> elementFinder = traverseAllFramesHash(path, false, null);
		WebElement parent = null;
		WebDriver frameDriver = null;
		ArrayList<WebElement> framePath = null;
		ArrayList<WebElement> currentPath = null;
		String currentTag,outerHTML =null;
		int size = -1;
		if(elementFinder!=null){
			size = elementFinder.size()-1;
		}
		IHTMLElement wEle = null;
		for(int i=size;i>=0;i--){
			wEle = elementFinder.get(i);
			Elements elements = wEle.getJSoupElement().select(tag);
			if (elements.size() <= 1) {
				parent = wEle.getWebElement();
				framePath = wEle.getFramePath();
				if (framePath != null
						&& (currentPath == null || !currentPath
								.equals(framePath))) {
					driver.switchTo().defaultContent();
					currentPath = framePath;
					for (WebElement ele : framePath) {
						if (frameDriver == null) {
							frameDriver = driver.switchTo().frame(ele);
						} else {
							frameDriver = frameDriver.switchTo().frame(ele);
						}
					}
				}
				// parent = parent.findElement(By.xpath(".."));

				currentTag = parent.getTagName();
				while (!tag.equals(currentTag)) {
					if (currentTag.equalsIgnoreCase("html")) {
						break;
					}
					parent = parent.findElement(By.xpath(".."));
					currentTag = parent.getTagName();
				}

				if (tag.equals(parent.getTagName())) {
					outerHTML = parent.getAttribute("outerHTML");
					eleVector.add(new IHTMLElement(parent, outerHTML, framePath));
				}
				frameDriver = null;
			}
		}
		driver.switchTo().defaultContent();*/
		System.out.println("total time taken in getting innermost table: "+(System.currentTimeMillis()-start));
		return eleVector.elements();
	}

	@Override
	public Enumeration getInnermostTablesContainingText(String text,
			boolean caseSensitive, int row) {

		if (text.equals("") || text == null)
			throw new IllegalArgumentException(
					"The input string to getInnermostTablesContainingText is empty string or null");

		return getInnermostTables(text, caseSensitive, row, SEARCH_TEXT);


	}

	@Override
	public Enumeration getInnermostTablesContainingHTML(String text,
			boolean caseSensitive, int row) {
		if (text.equals("") || text == null)
			throw new IllegalArgumentException(
					"The input string to getInnermostTablesContainingText is empty string or null");

		return getInnermostTables(text, caseSensitive, row, SEARCH_HTML);
	}

	@Override
	public Enumeration getInnerMostTablesContainingTextAsRow(String s,
			int rownum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getInnerMostTablesContainingHTMLAsRow(String s,
			int rownum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShowMode(boolean mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHTMLElement getTableRowCellElement(IHTMLElement t, int rownum,
			int cellnum) {
		// Manipulating the given indexes, since xpath indexes start from 0.
		if (rownum > 0)
			rownum--;
		if (cellnum > 0)
			cellnum--;
		IHTMLElement iEle = null;
		WebElement elem = t.getWebElement();
		WebElement webEle = null;

		// Element passed through argument is throwing staleElement exception,
		// hence re-fetching from the DOM.Can remove this
		// once the above bug is fixed.
		webEle = fetchWebElement(elem);

		if (webEle != null) {
			// Keeping OR condition: one with tbody another without. Return top
			// level <tr> nodes.
			List<WebElement> tableRows = webEle.findElements(By
					.xpath("./tbody/tr | ./tr"));
			if(tableRows != null && tableRows.size() > 0)
				webEle = tableRows.get(rownum);
			// Return top level <td> nodes
			List<WebElement> columns = webEle.findElements(By.xpath("./td"));
			if(columns != null && columns.size() > 0)
				webEle = columns.get(cellnum);
			// Since RemoteWebElement is returned from findElements method,
			// need to create IHTMLWebElement explicitly
			iEle = new IHTMLElement(webEle);
		}
		return iEle;
	}

	@Override
	public String parseDate(String format, String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseHistoricalDate(String format, String date) {
		return YUtilities.parseHistoricalDate(format, date);
	}

	@Override
	public void deleteCookie() {
		driver.manage().deleteAllCookies();

	}

	@Override
	public void selectOption(String attribute, String value,
			String optionAttribute, String optionValue) {
		selectOption(attribute, value, optionAttribute, optionValue, false);
	}

	@Override
	public String documentDump() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableRowUrlText(IHTMLElement e, int rownum, int urlnum) {
		WebElement tableRow = driver.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName("a")).get(urlnum);
		return tableRow.getText();
	}

	@Override
	public int getNumTableRowUrls(IHTMLElement e, int rownum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCellWithText(IHTMLElement table, String text, int row) {
		// TODO Auto-generated method stub
		//Document doc = getJSoupDocument(table);
		Elements elements = getJsoupElements(table, TABLE_ROW);
		if(elements.size()>0){
			Element ele = elements.get(row);
			elements = ele.select(TABLE_COLUMNS);
			for(int i=0;i<elements.size();i++){
				ele = elements.get(i);
				if(ele.hasText() && ele.text().toLowerCase().contains(text.toLowerCase())){
					return i;
				}
			}
		}
		return -1;

		/*WebElement e = t.getWebElement();
		List<WebElement> tableRows = e.findElements(By.tagName("tr"));
		e = tableRows.get(row);
		List<WebElement> columns = e.findElements(By.tagName("td"));
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getText() == null) {
				continue;
			}
			if (columns.get(i).getText().toLowerCase()
					.indexOf(text.toLowerCase()) != -1) {
				return i;
			}
		}

		return -1;*/
	}

	private Elements getJsoupElements(IHTMLElement element,String path) {
		// TODO Auto-generated method stub
		if(element.getJSoupElement()==null){
			element.setJSoupElement(Jsoup.parse(element.getWebElement().getAttribute("outerHTML")));
		}
		return element.getJSoupElement().select(path);
	}

	@Override
	public int getCellWithHtml(IHTMLElement table, String html, int rowIndex) {
		//Document doc = getJSoupDocument(table);	
		Elements elements = getJsoupElements(table, TABLE_ROW);
		Element element = null;
		if(elements.size()>0){
			element = elements.get(rowIndex);
			elements = element.select(TABLE_COLUMNS);
			if(elements.size()>0){
				for(int i=0;i<elements.size();i++){
					element = elements.get(i);
					if(element.outerHtml()!=null && element.outerHtml().toLowerCase().contains(html.toLowerCase())){
						return i;
					}
				}
			}
		}

		return -1;

		/*// TODO Auto-generated method stub
		WebElement e = table.getWebElement();
		List<WebElement> tableRows = e.findElements(By.tagName("tr"));
		e = tableRows.get(rowIndex);
		List<WebElement> columns = e.findElements(By.tagName("td"));
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getAttribute("outerHTML") == null) {
				continue;
			}
			if (columns.get(i).getAttribute("outerHTML").toLowerCase()
					.indexOf(html.toLowerCase()) != -1) {
				return  i;
			}
		}
		return -1;*/
	}

	@Override
	public int getTableRowContainingHtml(IHTMLElement table, String html) {

		// TODO Auto-generated method stub
		/*WebElement e = table.getWebElement();
		List<WebElement> tableRows = e.findElements(By.tagName("tr"));*/
		//Document doc = getJSoupDocument(table);
		Elements tableRows = getJsoupElements(table, TABLE_ROW);
		Element row = null;
		for (int i=0;i<tableRows.size();i++) {
			row = tableRows.get(i);
			if (row.outerHtml() == null) {
				continue;
			}

			if (row.outerHtml().toLowerCase().contains(html.toLowerCase())) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void parseTable(IHTMLElement table, int startrow, int endrow,
			int[] cellpos, String[] cellheaders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void parseTable(IHTMLElement table, int startrow, int endrow,
			int[] cellpos, String[] cellheaders, boolean createItem) {
		// TODO Auto-generated method stub

	}


	@Override
	public IWebBrowser2 getBrowser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getTablesContainingHTMLAsRow(String s, int rownum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHTMLDocument2 getm_pDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHTMLDocument2 getDocument() {
		Document doc = null;
		if (driver.getPageSource() != null)
			doc = Jsoup.parse(driver.getPageSource());	
		m_pDocument = new IHTMLDocument2(doc);
		return m_pDocument;
	}

	@Override
	public String getLocationURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void goBack() {
		// TODO Auto-generated method stub

	}

	@Override
	public void goBack(boolean wait) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean clickCheckBoxWithText(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setInputTextElementWithText(String text, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int setElementValue(IHTMLElement pElement, String value) {
		//logic: using tagname and then based on tag name, performing actions

		WebElement webEle= pElement.getWebElement();
		String tagname= webEle.getTagName();

		if(tagname.equalsIgnoreCase("input")){
			tagname = pElement.getAttribute("type", 0).toString();
		}

		if (tagname.equalsIgnoreCase("text") || tagname.equalsIgnoreCase("password") || tagname.equalsIgnoreCase("hidden"))
		{
			webEle.sendKeys(value);
		}	

		if (tagname.equalsIgnoreCase("radio") || tagname.equalsIgnoreCase("checkbox")){
			webEle.findElement(By.xpath("//"+tagname+"[@value="+value+"]")).click();
			return 0;
		}

		if (tagname.equalsIgnoreCase("submit")){
			return 0;
		}

		if (tagname.equalsIgnoreCase("select")){
			//need to test this
			Select select = new Select(webEle);
			select.deselectAll();
			select.selectByVisibleText(value);
			return 0;
		}

		if (tagname.equalsIgnoreCase("textarea")){
			webEle.sendKeys(value);
		}

		return 0;
	}


	@Override
	public Enumeration getTablesContainingTextFromAll(String Text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeFromString(String str, String chartoremove) {
		// TODO Auto-generated method stub
		StringTokenizer remstr = new StringTokenizer(str, chartoremove);
		String newstr = new String();
		while (remstr.hasMoreTokens()) {
			newstr = newstr + remstr.nextToken();
		}
		return newstr;
	}

	@Override
	public String cleanEndOfString(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getTablesContainingTextAsRow(String s) {
		// TODO Auto-generated method stub
		Enumeration e=null;
		WebElement elem=driver.findElement(By.cssSelector("table"));
		List<WebElement> tableRows = elem.findElements(By.xpath("//*[contains(text(),'"+s+"')]"));
		Vector matchingTables=new Vector();
		for(int i=0;i<tableRows.size();i++){

			WebElement ele=tableRows.get(i);
			System.out.println("element is"+ele.getText());
			if(ele.getText().contains(s)){
				matchingTables.add(ele);
			}
			e=matchingTables.elements();
		}
		return e;
	}

	/**
	 * Traverse through all frames and return a list of WebElements found across the DOM using the 
	 * path given.
	 * 
	 * @param path (can be css selector or xpath)
	 * @param returnOnFirstFound (returns first element found if true)
	 * @return
	 */
	public List<WebElement> traverseAllFrames(String path,
			boolean returnOnFirstFound) {
		long start = System.currentTimeMillis();
		driver.switchTo().defaultContent(); 
		List<WebElement> elemList = null; 
		List<WebElement> frameElements = driver.findElements(By.tagName("frame"));
		elemList=findFrames(driver,frameElements,path,returnOnFirstFound,elemList);
		System.out.println("time taken in traversing frames: "+(System.currentTimeMillis()-start));
		return elemList;
	}

	private List<WebElement> findFrames(WebDriver driver2, List<WebElement> frameElements,
			String path,boolean returnOnFirstFound,List<WebElement> elemList) {
		// TODO Auto-generated method stub

		List<WebElement> internalframes=null;
		for(WebElement ele : frameElements){ 
			elemList=processElements(path, driver2, frameElements, returnOnFirstFound);
			if(elemList!=null && !elemList.isEmpty())
			{
				return elemList;
			}
			WebDriver frame = driver2.switchTo().frame(ele);
			internalframes=driver2.findElements(By.cssSelector("frame,iframe"));
			if(internalframes!=null && !internalframes.isEmpty())
			{
				elemList= findFrames(frame, internalframes, path,returnOnFirstFound,elemList);
				if(elemList!=null && !elemList.isEmpty())
				{
					return elemList;
				}

			}
		}
		driver.switchTo().defaultContent();
		return elemList;

	}
	public ArrayList<IHTMLElement> traverseAllFramesHash(String path,
			boolean returnOnFirstFound,ArrayList<IHTMLElement> eleList) {
		long start = System.currentTimeMillis();
		driver.switchTo().defaultContent();
		List<WebElement> listElem = webElementFinder(driver, path);
		if(listElem!=null && !listElem.isEmpty()){
			if(eleList==null){
				eleList = new ArrayList<IHTMLElement>();
			}
			if(returnOnFirstFound){
				eleList.add(new IHTMLElement(listElem.get(0), listElem.get(0).getAttribute("outerHTML"), null));
				//elemMap.put(listElem.get(0), listElem.get(0).getAttribute("outerHTML"));
				return eleList;
			}else{
				for(WebElement ele: listElem){
					eleList.add(new IHTMLElement(ele, ele.getAttribute("outerHTML"), null));
					//elemMap.put(ele, ele.getAttribute("outerHTML"));
				}
			}
		}
		if(isTraverseFrames() || isTraverseIframes()){
			eleList = processFrames(driver,path,returnOnFirstFound,eleList,false,null);
		}
		System.out.println("time taken in traversing frames: "+(System.currentTimeMillis()-start));
		return eleList;
	}

	public ArrayList<IHTMLElement> processFrames(WebDriver driver, String path, boolean returnOnFirstFound,ArrayList<IHTMLElement> eleList,boolean isParent,ArrayList<WebElement> listWeb) {
		List<WebElement> frameElements = driver.findElements(By.cssSelector("frame,iframe"));
		for(WebElement ele :  frameElements){
			WebDriver frame = driver.switchTo().frame(ele);
			List<WebElement> listElem = webElementFinder(frame, path);
			if(listWeb==null){
				listWeb = new ArrayList<WebElement>();
			}	
			listWeb.add(ele);
			if(listElem!=null && !listElem.isEmpty()){
				if(eleList==null){
					eleList = new ArrayList<IHTMLElement>();
				}
				if(returnOnFirstFound){
					//eleList.put(listElem.get(0), listElem.get(0).getAttribute("outerHTML"));
					eleList.add(new IHTMLElement(listElem.get(0),listElem.get(0).getAttribute("outerHTML"),listWeb));
					break;
				}else{
					for(WebElement element: listElem){
						eleList.add(new IHTMLElement(element, element.getAttribute("outerHTML"), listWeb));
						//eleList.put(element, element.getAttribute("outerHTML"));
					}
				}
			}
			eleList = processFrames(frame,path,returnOnFirstFound,eleList,true,listWeb);
			if(isParent){
				listWeb.remove(listWeb.size()-1);
				driver.switchTo().parentFrame();
			}else{
				listWeb = null;
				driver.switchTo().defaultContent();
			}
		}
		//driver.switchTo().defaultContent();
		return eleList;
	}


	/**
	 * Iterating through frames and checks whether path present or not. If present adds the 
	 * WebElement into the list.
	 * 
	 * @param webElementToFind
	 * @param elementSelectionType
	 * @param driver
	 * @param returnFirstFound
	 * @param findElements2
	 */
	private List<WebElement> processElements(String path,
			WebDriver driver, List<WebElement> findElements,
			boolean returnOnFirstFound) {

		List<WebElement> elemList = null;

		if (findElements == null || findElements.isEmpty()) {
			YSystem.out(2, DAPMessageConstants.NO_FRAME_FOUND);
			elemList = webElementFinder(path);//Getting element from driver
			return elemList;
		}

		for (WebElement webElement : findElements) {
			try {
				WebElement elem = null;
				//Switching to each frames.
				WebDriver frame = driver.switchTo().frame(webElement);

				if (path.startsWith("//")) {
					elem = frame.findElement(By.xpath(path));
				} else {
					elem = frame.findElement(By.cssSelector(path));
				}

				//Need to revisit this check, since hidden elements won't be passed. If hidden elements
				//are also required then can replace the check with (elem != null)
				if (elem.isEnabled()) {
					if (elemList == null)
						elemList = new ArrayList<WebElement>();
					elemList.add(elem);
					if (returnOnFirstFound) {
						return elemList;
					}
				}
			} catch (Exception e) {
				//YSystem.out(2, "Got Exception while parsing the frame Element. Skipping since the exception is not breaking anything and is expected");
			}
			//Switches the frame to default. Required if the frame traversal is called for just fetching the elements but not for doing
			//user actions on those. Used while scraping from elements.
			driver.switchTo().parentFrame();
		}
		return elemList;

	}


	@Override
	public void setInputAny(String field, String value) {

		// FIXME This by default finds an input tag on the page with a name
		// attribute which has a value "formName"

		// FIXME Come back and add ScriptConstants.
		long start = System.currentTimeMillis();
		if (value != null) {
			value = value.trim();
		}
		List<WebElement> elemList = null;
		WebElement ele = null;
		String path = pathBuilder("input", "name", field, true);
		// ele = getWebElement(path);
		try {
			ele = getWebEle(path);
		} catch (NoSuchElementException ex) {
			YSystem.out("Unable to enter value in input with name as " + field);
			throw ex;
		}
		ele.clear();
		ele.sendKeys(value);
		if (isTraverseFrames() || isTraverseIframes())
			driver.switchTo().defaultContent();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		YSystem.out("time taken to fill form: "
				+ (System.currentTimeMillis() - start));
		YSystem.out("FillForm Complete");

	}

	@Override
	public void clickInputWithValue(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Enumeration getChildTables(IHTMLElement elem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAnyTableContainingAsFirstRowText(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTableRowText(IHTMLElement e, int rownum) {
		WebElement element = e.getWebElement();
		org.jsoup.nodes.Document doc = Jsoup.parse(element
				.getAttribute("outerHTML"));
		Elements elementRows = doc.select(TABLE_ROW);
		Element rowEle;
		try {
			rowEle = elementRows.get(rownum);
		} catch (Exception ex) {
			return "";
		}
		return JsoupUtil.getText(rowEle, true);
	}

	@Override
	public String getTableRowUrl(IHTMLElement e, int rownum, int urlnum) {
		WebElement tableRow = driver.findElements(By.tagName(TABLE_ROW)).get(rownum).findElements(By.tagName("a")).get(urlnum);
		String url = null;
		if( tableRow != null ){
			url = tableRow.getAttribute("href");
		} 
		return url;
	}

	@Override
	public boolean tablefirstRowContainsText(IHTMLElement table, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTableRowContaining(IHTMLElement table, String text) {

		// TODO Auto-generated method stub
		/*WebElement e = table.getWebElement();
		List<WebElement> tableRows = e.findElements(By.tagName("tr"));
		for (int i = 0; i < tableRows.size(); i++) {
			WebElement row = tableRows.get(i);

			if (row.getText() == null) {
				continue;
			}

			if (row.getText().toLowerCase().indexOf(text.toLowerCase()) != -1) {
				return i;
			}
		}*/

		//using JSoup finding the index of row containing given text
		//Document doc = getJSoupDocument(table);
		Elements elements = getJsoupElements(table, TABLE_ROW);
		Element element = null;
		for(int i =0;i<elements.size();i++){
			element = elements.get(i);
			if(element.hasText() && element.text().toLowerCase().contains(text.toLowerCase())){
				return i;
			}
		}

		return -1;
	}

	@Override
	public Enumeration getTableRowsContainingInnerText(IHTMLTable table,
			String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getTableRowsContainingElement(IHTMLTable table,
			String tag, String attributeName, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableRowUrlWithText(IHTMLElement table, int rownum,
			String text) {
		WebElement element = table.getWebElement();
		WebElement rowData = element.findElements(By.tagName(TABLE_ROW)).get(rownum);
		String url = rowData.findElement(By.name(text)).getAttribute("a");
		return url;
	}

	@Override
	public Enumeration getTablesContainingTextAsRow(String text, int rownum) {
		// TODO Auto-generated method stub
		return null;
	}

	/* using JSOUP here*/

	@Override
	public String getTableRowCellText(IHTMLElement e, int rownum, int cellnum) {

		// TODO Auto-generated method stub
		//Document doc = getJSoupDocument(e); 
		//System.out.println("elements document"+doc.html());
		Elements elementRows=getJsoupElements(e, TABLE_ROW);
		Element cellEle;
		try
		{
			Elements elementCols=elementRows.get(rownum).select(TABLE_COLUMNS);
			cellEle=elementCols.get(cellnum);
		}
		catch(Exception ex)
		{
			return "";
		}
		//System.out.println("final here"+JsoupUtil.getText(ad,true));
		return JsoupUtil.getText(cellEle,true);


	}

	@Override
	public int getNumTableRows(IHTMLTable table) {
		// FIXME : come here and fix this Weblement

		//WebElement e= (WebElement)table;
		WebElement tableElement= table.getWebElement();
		List<WebElement> tableRows = tableElement.findElements(By.tagName("tr"));
		return tableRows.size();
	}

	public int getNumTableRowCells(IHTMLTable table, int rownum) {
		int count = 0;
		WebElement element = table.getWebElement();
		org.jsoup.nodes.Document doc = Jsoup.parse(element
				.getAttribute("outerHTML"));
		Elements elementRows = doc.select(TABLE_ROW);
		try {
			Elements elementCols = elementRows.get(rownum).select(TABLE_COLUMNS);
			count = elementCols.size();
		} catch (Exception ex) {
			YSystem.printStackTrace(ex);
		}
		return count;
	}

	@Override
	public int getNumTableRows(IHTMLElement table) {
		// TODO Auto-generated method stub
		//	WebElement e = table.getWebElement();
		Elements tableRows = getJsoupElements(table, TABLE_ROW); //e.findElements(By.tagName("tr"));
		return tableRows.size();
	}


	@Override
	public int getNumTableRowCells(IHTMLElement table, int rownum) {
		// TODO Auto-generated method stub

		WebElement tableElement = table.getWebElement();
		WebElement tableColumn = tableElement.findElement(By.xpath("//tr"));
		List<WebElement> tableRows = tableColumn.findElements(By.tagName("th"));
		return tableRows.size();
	}

	@Override
	public void setBaseDirectory(String baseDir) {
		// TODO Auto-generated method stub

	}

	@Override
	public void click(String tag, String attrib, String val) {
		// TODO Auto-generated method stub
		click(tag, attrib, val, true);

	}

	@Override
	public void click(String tag, String attrib, String val, boolean wait) {
		if (!wait) {
			click2(false, tag, attrib, val);
		} else {
			//resetBeforeNavigate();
			click2(false, tag, attrib, val);
			waitForDocumentComplete();
		}
	}

	@Override
	public String getTableRowCellHTML(IHTMLElement e, int rownum, int cellnum) {

		// TODO Auto-generated method stub
		/*XPATH: tr[rownum]/td[Cellnum]*/
		//Document doc= getJSoupDocument(e);
		//System.out.println("elements document"+doc.html());
		Elements elementRows=getJsoupElements(e, TABLE_ROW);
		Element cellEle;
		try
		{
			Elements elementCols=elementRows.get(rownum).select(TABLE_COLUMNS);
			cellEle=elementCols.get(cellnum);
		}
		catch(Exception ex)
		{
			return "";
		}
		//System.out.println("final here"+JsoupUtil.getText(ad,true));
		return cellEle.html();
	}

	@Override
	public String doTableRowCell(String verb, String what, IHTMLElement e,
			int rownum, int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTableRowWithCellText(IHTMLElement table, String celltext,
			int cellnum) {
		//logic: finding the table and then rows and then columns. After that selecting the particular cell with given text
		WebElement element = table.getWebElement();
		List<WebElement> tableRows = element.findElements(By.tagName(TABLE_ROW));

		for (int i=0; i<=tableRows.size();i++) {
			WebElement webEle = tableRows.get(i).findElements(By.tagName(TABLE_COLUMNS)).get(cellnum);
			String text = webEle.getText();
			if(text.indexOf(celltext) >= 0){
				return i;
			}
		}
		return -1;
	}

	@Override
	public void waitForDocumentComplete2(int numtimes) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selectEncoding(String encoding) {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForDownloadComplete() {
		// TODO Auto-generated method stub

	}

	public String parseMoney(String money) {
		return parseMoney(money, false);
	}

	public String parseMoney(String money, boolean isCent) {
		YSystem.out("4^money=" + money);
		money = money.trim();

		// Remove commas.
		StringBuffer buf = new StringBuffer();
		for (StringTokenizer st = new StringTokenizer(money, ","); st
				.hasMoreTokens();) {
			buf.append(st.nextToken());
		}
		money = buf.toString();

		// Remove dollar signs.
		if (money.indexOf("$") >= 0) {
			money = removeFromString(money, "$");
			money = removeFromString(money, " ");
		}
		money = money.trim();

		double amt = -1;
		if (!isCent) {
			try {
				amt = (Double.valueOf(money).doubleValue());
			} catch (Exception e) {
				return "N/A";
			}
			return Double.toString(amt);
		} else {
			if ((money.length() - money.indexOf(".")) == 2) {
				money += "0";
			}
			if (money.startsWith(".")) {
				money = "0" + money;
			}
			return money;
		}
	}


	@Override
	public String parseMiles(String miles) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumSelectOptions(String attribute, String value) {
		// TODO Auto-generated method stub

		String path = pathBuilder("select",attribute, value,false);
		WebElement ele = getWebElement(path);

		// ele= findWebElement(path);
		MySelect mysel=new MySelect(ele);
		List<WebElement> opt=mysel.getOptions();
		return opt.size();
	}


	@Override
	public void selectOption(String attributeName, String optionAttribute,
			String optionValue, boolean caseSensitive, boolean matchExactly) {
		selectOption("name", attributeName, optionAttribute, optionValue,
				matchExactly);
	}

	@Override
	public void selectOption(String attribute, String value, int optionnum) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSelectedText(IHTMLSelectElement selElem) {


		WebElement element=selElem.getWebElement();
		MySelect mysel = new MySelect(element);
		String Selected = mysel.getFirstSelectedOption().getText();
		return Selected;
	}


	public String getSelectOptionText(String attribute, String value,
			int optionnum) {
		//logic: selecting all values from dropdown based upon locator, then selecting value based upon index
		Select dropdown = new Select(driver.findElement(By.cssSelector("["+attribute+"="+value+"]")));
		dropdown.selectByIndex(optionnum);
		String selectedValue = dropdown.getFirstSelectedOption().getText();
		System.out.println("^^^^^Printing the selected value:"+selectedValue);
		return selectedValue;
	}

	@Override
	public IHTMLElement getFollowingElement(IHTMLElement pElement, String tag) {
		// TODO Auto-generated method stub
		return getFollowingElement(pElement, tag, 1);
	}

	@Override
	public IHTMLElement getFollowingElement(IHTMLElement pElement, String tag,int tagOccuranceIndex) {
		// TODO Auto-generated method stub
		WebElement ele = pElement.getWebElement();
		ele  =  ele.findElement(By.xpath("(./following-sibling::"+tag+")["+tagOccuranceIndex+"]"));
		if(ele!=null){
			return new IHTMLElement(ele);
		}else{
			return null;
		}
	}

	@Override
	public void addChildItem(GathererResponseItem pItem) {
		// TODO Auto-generated method stub
		int numDataItems = m_pRequestItem.getNumDataItems();
		if (numDataItems > 0) {
			GathererResponseItem dataItem = m_pRequestItem
					.getDataItem(numDataItems - 1);
			dataItem.addChild(pItem);
		}

	}

	@Override
	public void setRequestItem(RequestItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGathererRequest(GathererRequest request) {
		this.m_pGathererRequest = request;

	}

	@Override
	public void addMessage(String message, int messageType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addWarning(String message, int code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setType(String type) {
		if(m_pRequestItem != null)
			m_pRequestItem.setTag(type);
	}

	@Override
	public void createItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(String name, String value) {
		// TODO Auto-generated method stub
		int numDataItems = m_pRequestItem.getNumDataItems();
		if (numDataItems > 0) {
			GathererResponseItem dataItem = m_pRequestItem.getDataItem(numDataItems - 1);
			dataItem.set(name, value);
		}

	}

	@Override
	public void set(Hashtable keyValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sortBy(String val) {
		// TODO Auto-generated method stub

		m_pUtil = new YUtilities();
		m_pUtil.sortBy(m_pRequestItem, val);


	}

	@Override
	public void get(String url, String userAgent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(String url, String body) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHTMLElement getLinkWithText(String OriginalText) {
		// TODO Auto-generated method stub
		String text = OriginalText.toLowerCase();
		List<WebElement> tableRows = driver.findElements(By.tagName("a"));
		for(int i=0;i<tableRows.size();i++){
			WebElement ele=tableRows.get(i);
			String txt=ele.getText();
			if(txt!=null){
				if(txt.toLowerCase().contains(text)){
					return new IHTMLElement(ele);
				}
			}

		}
		throw (new NoLinkFoundException());

	}

	@Override
	public String getHref(IHTMLElement pLink) {
		WebElement element = pLink.getWebElement();
		String hrefVal = element.getAttribute("href");
		return hrefVal;
	}

	@Override
	public void click(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(String field, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(String formName, String field, String value) {
		// TODO Auto-generated method stub
		String path=pathBuilder("form", "name", formName, false);
		WebElement webEle = getWebElement(path);
		setInputAny(field, value);
	}

	@Override
	public void submitForm(String formName) {
		setPageText(null);
		String path = "//form[@name='"+formName+"']";
		WebElement ele = null;
		ele = getWebElement(path);
		if (ele.isEnabled()) {
			YSystem.out("Going to perform submit action on the element with path :"
					+ path);
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			// FIXME need to come back and make this configurable
			// driver.manage().timeouts().pageLoadTimeout(GathererProperties.getIntProperty("yodlee.gatherer.document_timeout",
			// 30), TimeUnit.SECONDS);
			ele.submit();
			int count = 1;
			while (count <= expectedStringTimeout) {
				if (waitForExpectedStrings()) {
					expectedTexts = null;
					break;
				} else {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					count++;	
				}

			}

			if (isTraverseFrames() || isTraverseIframes())
				driver.switchTo().defaultContent();
			YSystem.out("Performed Submit Action on element with path : "
					+ path);
		} else {
			YSystem.out("Element is disabled / no submit will happen");
		}

	}

	@Override
	public void submitForm(IHTMLFormElement form) {

		setPageText(null);
		WebElement element = form.getWebElement();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		element.submit();
		int count = 1;
		while (count <= expectedStringTimeout) {
			if (waitForExpectedStrings()) {
				expectedTexts = null;
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				count++;
			}

		}
	}

	@Override
	public void useFrame(IHTMLFrameElement pFrame) {
		// TODO Auto-generated method stub

	}

	private List<WebElement> findWebElementList(String path) {
		List<WebElement> element = webElementFinder(path);
		if (element != null && !element.isEmpty()) {
			return element;
		} else {
			YSystem.out("no element Found");
			return null;
		}

	}

	public List<WebElement> getWebElementList(String path){
		List<WebElement> webEle = null;
		List<WebElement> elemList = null;
		if(isTraverseFrames() || isTraverseIframes()){
			elemList = traverseAllFrames(path,true);
			if(elemList != null && elemList.size() > 0){
				webEle = elemList;
			}
		}else{
			webEle = findWebElementList(path);
		}
		return webEle;
	}

	@Override
	public void useFrameBrowser(IWebBrowser2 iWebBrowser2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sleep(int cSeconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rescan() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAbortWaitForDocumentComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitForDocumentComplete() {
		// TODO Auto-generated method stub

	}

	public void submitForm(int iForm) {
		//logic: getting all elements using 'form' tag and storing in list, then clicking on user want to click with value in 'iform'
		setPageText(null);
		List<WebElement> webEle = driver.findElements(By.tagName("form"));

		if (webEle != null) {
			webEle.get(iForm).submit();
		}
		else {
			throw new GeneralException("no forms found on page");
		}

	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return driver.getCurrentUrl();
	}

	@Override
	public void setTraverseIframes(boolean traverseIframes) {
		this.traverseIframes = traverseIframes;

	}

	@Override
	public void setTraverseFrames(boolean traverseFrames) {
		this.traverseFrames = traverseFrames;
	}

	@Override
	public Enumeration getTablesContainingText(String Text) {
		// TODO Auto-generated method stub
		return getInnermostTables(Text, false, 0, SEARCH_TEXT);
	}

	@Override
	public Vector createVector(IHTMLElementCollection pCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTableList(Vector pTables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTable(int iTable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRowList(Vector pRows) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRow(int iRow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCellList(Vector pCells) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCell(int iCell) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHTMLElement getFollowingElement(IHTMLElement pElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHTMLElement getFirstAnchor(IHTMLElement pElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getTagElementsWithAttributeValue(String tag,
			String attribute, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHTMLElement getContainerWithTag(IHTMLElement pElement, String tag) {

		WebElement webEle = pElement.getWebElement();

		while (true) {
			webEle = webEle.findElement(By.xpath(".."));
			if (webEle == null) {
				throw (new GeneralException());
			}
			if (webEle.getTagName().equalsIgnoreCase(tag)) {
				return (IHTMLElement) webEle;
			}
		}
	}

	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultTimeout() {
		// TODO Auto-generated method stub
		minTimeout = Integer.getInteger("yodlee.gatherer.document_timeout",
				DOCUMENT_TIMEOUT).intValue() * 1000;

		maxTimeout = Integer.getInteger("yodlee.gatherer.max_document_timeout",
				MAX_DOCUMENT_TIMEOUT).intValue() * 1000;

		timeoutIncrement = 0;


	}

	@Override
	public void setTimeout(int iSeconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPopUpWindowFlag(boolean flag) {
		// TODO Auto-generated method stub
		m_popUpWindowFlag = flag;

	}

	@Override
	public void clickLinkByOrdinal(int linkID) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHTMLDocument2[] getAllFrames() {
		IHTMLDocument2 doc[] = null;
		List <Document> listDocs = null;
		List<WebElement> frameElements = webElementFinder("//*[contains(local-name(), 'frame')]");
		for (WebElement webElement : frameElements) {
			try {
				if(!(webElement.getTagName().toLowerCase().contains("frameset"))){
					Document jDoc = Jsoup.parse(webElement.getAttribute("outerHTML"));


					if(listDocs == null){
						listDocs = new ArrayList<Document>();
						listDocs.add(jDoc);
					}else{
						listDocs.add(jDoc);
					}
				}
			}catch(Exception e){

			}
		}
		if(listDocs != null){
			doc = new IHTMLDocument2[listDocs.size()];
			int i=0;
			//Doing two for loops since first loop wont give the exact 
			//count of frames in the HTML. frameset will be also part of webelement list.
			for(Document docmt : listDocs){
				IHTMLDocument2 document = new IHTMLDocument2(docmt);
				try{ 
					doc[i] = document;
					i++;
				}catch(Exception exc){
					//skipping exception
				}

			}
		}
		return doc;
	}

	@Override
	public List<Document> getframes() 
	{
		List <Document> listDocs = null;
		List<WebElement> frameElements = webElementFinder("//*[contains(local-name(), 'frame')]");
		for (WebElement webElement : frameElements) {
			try {
				if(!(webElement.getTagName().toLowerCase().contains("frameset"))){
					Document jDoc = Jsoup.parse(webElement.getAttribute("outerHTML"));


					if(listDocs == null){
						listDocs = new ArrayList<Document>();
						listDocs.add(jDoc);
					}else{
						listDocs.add(jDoc);
					}
				}
			}catch(Exception e){

			}
		}
		return listDocs;
	}

	@Override
	public int getCountOfTextOnPage(String text, boolean caseSensitive) {
		if(caseSensitive){
			//finding the elements using XPATH as it is caseSensitive
			int size = driver.findElements(By.xpath("//*[text()='"+text+"']")).size();
			return size;
		}else{
			//using the contains as we need to ignore the case
			WebElement body = driver.findElement(By.tagName("body"));
			String bodyText = body.getText();
			int count = 0;
			while (bodyText.contains(text)){
				count++;
				bodyText = bodyText.substring(bodyText.indexOf(text) + text.length());
			}
			return count;
		}
	}

	@Override
	public int getCountOfHTMLOnPage(String text, boolean caseSensitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHTMLFoundOnPage(String text, boolean caseSensitive) {
		// TODO Auto-generated method stub
		return findElementsByHtml(text,caseSensitive);
	}


	@Override
	public int getCountOfAttributeValueOnPage(String tagName,
			String attributeName, String attributeValue, boolean caseSensitive,
			boolean matchExactly) {
		// TODO Auto-generated method stub
		String pathbuilder=pathBuilder(tagName, attributeName, attributeValue, false);
		if(getWebElementList(pathbuilder)==null){
			return -1;
		}
		return getWebElementList(pathbuilder).size();
	}

	@Override
	public int login(LoginInfo loginInfo, User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int custom_login(LoginInfo loginInfo, User user, int flags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getPreloginPage(LoginInfo loginInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkFailString(LoginInfo loginInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkFailString(String[] failStringArray) {
		// TODO Auto-generated method stub

	}

	@Override
	public void expectFailString(LoginInfo loginInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public int login(GathererRequest pRequest) {
		// TODO Auto-generated method stub
		return 0;
	}

	// watch out for: NullPointerException, IllegalArgumentException,
	// GeneralException
	// TODO: move this into YUtilities?
	private String[] crypt(String[] values, int op, int method,
			Hashtable cryptParams) throws Exception {

		String oldCryptName = null;
		String[] returnValues;

		if (null != cryptParams) {
			oldCryptName = (String) cryptParams.put("cryptLib", m_cryptoName);
			returnValues = YUtilities.crypt(values, op, method, cryptParams);
		} else {
			if (null != m_cryptoName) {
				Hashtable params = new Hashtable();
				params.put("cryptLib", m_cryptoName);
				returnValues = YUtilities.crypt(values, op, method, params);
			} else {
				returnValues = YUtilities
						.crypt(values, op, method, cryptParams);
			}
		}

		if (null != oldCryptName) {
			cryptParams.put("cryptLib", oldCryptName);
		}

		return returnValues;
	}

	private String doCrypt(String value, boolean validate) {

		// If the value is prepended by the cleartext indicator, just
		// strip off the cleartext indicator and return the rest of
		// the string.
		if (value != null && value.startsWith(CLEARTEXT_INDICATOR)) {
			YSystem.out("4^YBrowser.doCrypt: value to be decrypted has cleartext indicator");
			return value.substring(CLEARTEXT_INDICATOR.length());
		}

		String[] values = new String[] { value };
		String[] decryptedValues;
		decryptedValues = this.decrypt(values, validate);

		try {
			return decryptedValues[0];
		} catch (ArrayIndexOutOfBoundsException ex) {
			YSystem.printStackTrace(ex);
			throw new GeneralException("decrypt did not return any values");
		}
	}

	public String doCrypt(String value) {
		return doCrypt(value, true);
	}

	@Override
	public String doEncrypt(String crypt) {
		// TODO Auto-generated method stub
		return null;
	}

	// throws: LoginException, NullPointerException, IllegalArgumentException,
	// GeneralException
	public String[] decrypt(String[] values, boolean validate) {

		if (validate) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] == null || values[i].trim().length() <= 0)
					throw new LoginException(
							"Some value passed to YBrowser.decrypt is null or zero length");
			}
		}

		int method = CRYPT_METHOD_NOT_SPECIFIED;
		if (this.m_pGathererRequest != null)
			method = this.m_pGathererRequest.getDecryptMethod();

		switch (method) {
		case CRYPT_METHOD_NOT_SPECIFIED:
		case CRYPT_METHOD_SOFTWARE:
			try {
				return this.crypt(values, CRYPT_OP_DECRYPT,
						CRYPT_METHOD_SOFTWARE, null);
			} catch (GatherException ge) {
				throw ge;
			} catch (Exception e) {
				YSystem.printStackTrace(e);
				throw new GeneralException(
						"Caught exception while trying to decrypt: " + e);
			}
		case CRYPT_METHOD_INGRIAN:
			if (this.m_pGathererRequest != null) {
				Hashtable params = this.m_pGathererRequest.getDecryptParams();
				try {
					return this.crypt(values, CRYPT_OP_DECRYPT,
							CRYPT_METHOD_INGRIAN, params);
				} catch (GatherException ge) {
					throw ge;
				} catch (Exception e) {
					YSystem.printStackTrace(e);
					throw new GeneralException(
							"Caught exception while trying to decrypt: " + e);
				}

			} else {
				throw new GeneralException(
						"No GathererRequest available to get Ingrian info from");
			}

		case YUtilities.CRYPT_METHOD_NONE:
			return values;

		default:
			throw new GeneralException(
					"Unknown/Unsupported decrypt method specified in GathererRequest");
		}
	}

	@Override
	public String[] encrypt(String[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int custom_login(GathererRequest pRequest, int flags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int query(GathererRequest pRequest) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void getPreloginPage(GathererRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the page at the the login URL. This method throws a
	 * <code>SiteUnavailableException</code> exception if the page times out.
	 */
	@Override
	public void getLoginPage(LoginInfo loginInfo) {
		getLoginPage(loginInfo.getLoginURL());
	}

	@Override
	public void getLoginPage(LoginInfo loginInfo, boolean enableImageDownload) {
		if (loginInfo.getLoginURL() != null && !loginInfo.getLoginURL().equals("")) 
		{
			getLoginPage(loginInfo.getLoginURL());
		}

	}


	@Override
	public void getLoginPage(GathererRequest request) {
		getLoginPage(request.getLoginUrl());
	}

	private void getLoginPage(String loginUrl) {
		if (loginUrl != null && !loginUrl.equals("")) {
			try {
				get(loginUrl);
			} catch (TimeoutException te) {

				// If we get a timeout here. It could either be
				// that the site has changed its login URL or the
				// site is down. Thus, we will throw a
				// SiteUnavailableException.
				throw new TimeoutException("Login URL timed out: "
						+ loginUrl);
			}
		}
	}

	@Override
	public void checkFailString(GathererRequest pRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDocumentHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInnerHTMLContaining(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getDocumentDump() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExpectOr createFailStringExpectConditions(GathererRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExpectOr createExpectOr(String[] arr) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExpectAnd createExpectAnd(String[] arr) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addExpect(ExpectExpression expr) {
		// TODO Auto-generated method stub

	}

	public void addExpect() {
		// TODO Auto-generated method stub

	}

	public void createExpectList() {
		expectedTexts = null;
	}

	public void clearExpectLists() {
		// TODO Auto-generated method stub

	}

	public String getExpectListsAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void newExpectAndExpression() {
		// TODO Auto-generated method stub

	}

	@Override
	public void expectFailString(GathererRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void expectInnerText(String text) {
		if (expectedTexts == null) {
			expectedTexts = new ArrayList<String>();
		}
		if (text != null)
			expectedTexts.add(text);

	}

	@Override
	public void expectAttributeValue(String tagName, String attributeName,
			String expectedAttributeValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public IHTMLElement[] findElements(String tag, String[] attributes,
			String[] values, boolean caseSensitive, boolean matchExactly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHTMLElement[] findElements(String tag, String attribute,
			String value, boolean caseSensitive, boolean matchExactly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean prepareToDownload() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void submitForm(String action, String method, String target,
			Hashtable values) {
		String params = "";
		setPageText(null);
		for (Enumeration keys = values.keys(); keys.hasMoreElements();) {

			String key = (String) keys.nextElement();
			String value = (String) values.get(key);
			params = params
					+ "var hiddenField = document.createElement(\"input\");hiddenField.setAttribute(\"type\", \"hidden\");hiddenField.setAttribute(\"name\", \""
					+ key + "\");hiddenField.setAttribute(\"value\", \""
					+ value + "\");form.appendChild(hiddenField);";

		}
		String javaScript = "var form = document.createElement(\"form\");form.setAttribute(\"method\", \""
				+ method
				+ "\");form.setAttribute(\"action\", \""
				+ action
				+ "\");"
				+ params
				+ "document.body.appendChild(form);form.submit();";

		Robot.executeJavaScript(javaScript);

		int count = 1;
		while (count <= expectedStringTimeout) {
			if (waitForExpectedStrings()) {
				expectedTexts = null;
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				count++;	
			}

		}

	}

	@Override
	public void submitForm(Form form) {
		// TODO Auto-generated method stub
	}

	@Override
	public int fillForm(IHTMLDocument2 m_pDoc, String formid,
			Hashtable pVariables, String submitid, int flags) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void reinitialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRefreshLimit(int secs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendTitle(String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendToDocumentDump() {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendLastPageToDump() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAuthenStatus(String userName, String password, String url) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAuthenContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int doWinInetPost(String target, String header, String params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int doWinInetGet(String target, String header, String params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int secureDoWinInetPost(String target, String header, String params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int secureDoWinInetGet(String target, String header, String params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int doWinInetGetCookie(String url, String cookieName,
			StringBuffer cookieData, int cookieSize) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int doWinInetSetCookie(String url, String cookieName,
			String cookieData) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openWinInetConnection(String userName, String password,
			String serverName, int portNo, boolean isHttps) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void closeWinInetConnection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearAllowedDomains() {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector getAllowedDomains() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAllowedDomains(String domains) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearMessageBoxText() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMessageBoxText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMessageBoxTextArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAsapMode(boolean asapMode) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getAsapMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sortByDate(String container, String[] order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sortTransactionsByDate() {
		// TODO Auto-generated method stub
		String[] order = { "trans_date", "post_date" };
		m_pRequestItem.sortByDate("transactionlist", order);

	}

	@Override
	public void sortMailByDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public long incrementCounter(String propName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFlag(String propName, boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTimer(String propName, boolean reset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTimer(String propName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopTimer(String propName) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRequestPropertiesAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBrowserSilent(boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputAny(String field, HiddenString value, boolean useNew) {
		// TODO Auto-generated method stub

	}

	@Override
	public SessionPromptResponse getTokenId(SessionPromptRequest spr,
			long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionPromptResponse getQuestionResponse(SessionPromptRequest spr,
			long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionPromptResponse getMFAImageResponse(SessionPromptRequest spr,
			long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionPromptResponse getTokenId(SessionPromptRequest spr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionPromptResponse getQuestionResponse(SessionPromptRequest spr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionPromptResponse getMFAImageResponse(SessionPromptRequest spr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConvertedImage(MFAImage mfaImage) {
		// TODO Auto-generated method stub

	}

	@Override
	public IWebBrowser2 getIWebBrowser2FromFrame(IHTMLElement pFrame) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is a pure java alternative for various native implemented feed APIs. 
	 * This API open a connection to a server via a proxy if supplied. 
	 * It POSTs the user supplied request and header and retrieves the response back. 
	 * If this request need any feed server authentication or proxy server authentication, 
	 * same should be indicated in the header using Proxy-Authorization or Authorization properties. 
	 *
	 * @param feedServerUrl Url of the feed server. This can not be null.
	 * @param proxyServerUrl Url of the proxy server. This can be null.
	 * @param headerMap a Map of header properties. It is a name value pair.
	 * @param requestXML request payload.
	 *
	 * @return the feed response
	 *
	 * @throws Exception 
	 * @throws GeneralException 
	 */
	public String getFeedContentsThroughProxy(String feedServerUrl, String proxyServerUrl, Map<String,String>headerMap, String requestXML) throws Exception
	{
		URL feedConnUrl = null;
		URLConnection urlConnection = null ;        
		String contents = null;
		Proxy proxy = null ;

		Properties sysProps = System.getProperties();
		YSystem.out(2, "feedServerUrl = "+feedServerUrl+",proxyServerUrl = "+proxyServerUrl);
		YSystem.out(2, "Request Header Map : " +  headerMap);
		YSystem.out(2, "Length of Request Payload (Payload is not printed for security reasons ): " + requestXML.length());        
		//get the timeout value
		int connectTimeout = 5000; 
		try {
			connectTimeout  = Integer.parseInt(sysProps.getProperty(
					"yodlee.gatherer.waitForDocumentComplete"));
		}
		catch (NumberFormatException nfe) {
			YSystem.out("1^WaitForDocumentComplete: cannot parse "
					+ "yodlee.gatherer.waitForDocumentComplete "
					+ sysProps.getProperty("yodlee.gatherer.waitForDocumentComplete"));

			YSystem.out("1^Will be setting to the default value of 5000");          

		}
		try {
			feedConnUrl = new URL(feedServerUrl);
		} catch (MalformedURLException e) {
			YSystem.printStackTrace(e);
			throw new GeneralException("feedServerUrl is Malformed");
		}

		if(proxyServerUrl != null)
		{
			// Creating a Proxy object reflecting the proxy server and all its settings
			String proxyHost = proxyServerUrl.substring(proxyServerUrl.indexOf("://") + 3, proxyServerUrl.lastIndexOf(':'));
			int proxyPort = Integer.parseInt(proxyServerUrl.substring(proxyServerUrl.lastIndexOf(':') + 1));
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, addr);           
		}       

		// Getting a connection to the feed server from feed server URL object through the already created proxy
		try {

			if(proxy != null){
				urlConnection = feedConnUrl.openConnection(proxy);
				urlConnection.setConnectTimeout(connectTimeout);
			}else{
				urlConnection = feedConnUrl.openConnection();
				urlConnection.setConnectTimeout(connectTimeout);
			}
		} catch (SocketTimeoutException s) {            
			YSystem.printStackTrace(s);
			throw new GeneralException("Connection timed out");            
		} catch (IOException e) {            
			YSystem.printStackTrace(e);
			throw new GeneralException("IOException while opening connection to feed server through proxy");            
		}

		// If it comes here it means the connection to the feed server is successfully created using the proxy server.
		// Below we will set the header fields and send a post request to the feed server and retrieve the output.        

		// specify that we will send output and accept input
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);

		// set the header from the user supplied headerMap
		for(Iterator<Map.Entry<String,String>> headerIter = headerMap.entrySet().iterator();headerIter.hasNext();)
		{
			Map.Entry<String, String> entry = headerIter.next();
			urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
		}  

		// tell the web server what we are sending
		OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(),"UTF8");
		writer.write(requestXML);
		writer.flush();

		// reading the response
		InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream(),"UTF8");
		StringBuilder buf = new StringBuilder();
		char[] cbuf = new char[ 2048 ];
		int num;

		while ( -1 != (num=reader.read( cbuf )))
		{
			buf.append( cbuf, 0, num );
		}
		contents = buf.toString();
		reader.close();
		writer.close();
		TextSanitizer ts = new TextSanitizer();
		String sanitizedConetent = ts.sanitizeFeedContents(contents);
		YSystem.out(3," Sanitized Response from server after POST : \n" );
		YSystem.out(3,sanitizedConetent);
		return contents; 
	}


	//copied as it is, from YBrowser
	public String getFeedContentsThroughProxy(String feedServerUrl,
			String proxyServerUrl, Map<String, String> headerMap)
					throws Exception {
		URL feedConnUrl = null;
		URLConnection urlConnection = null ;        
		String contents = null;
		Proxy proxy = null ;
		//get the timeout value
		Properties sysProps = System.getProperties();
		int connectTimedout = 5000; 
		try {
			connectTimedout  = Integer.parseInt(sysProps.getProperty(
					"yodlee.gatherer.waitForDocumentComplete"));
		}
		catch (NumberFormatException nfe) {
			YSystem.out("1^WaitForDocumentComplete: cannot parse "
					+ "yodlee.gatherer.waitForDocumentComplete "
					+ sysProps.getProperty("yodlee.gatherer.waitForDocumentComplete"));

			YSystem.out("1^Will be setting to the default value of 5000");          

		}
		YSystem.out(2, "feedServerUrl = "+feedServerUrl+",proxyServerUrl = "+proxyServerUrl);
		YSystem.out(2, "Request Header Map : " +  headerMap);             

		// Creating a URL object to feed server
		try {
			feedConnUrl = new URL(feedServerUrl);
		} catch (MalformedURLException e) {
			YSystem.printStackTrace(e);
			throw new GeneralException("feedServerUrl is Malformed");
		}

		if(proxyServerUrl != null)
		{
			// Creating a Proxy object reflecting the proxy server and all its settings
			String proxyHost = proxyServerUrl.substring(proxyServerUrl.indexOf("://") + 3, proxyServerUrl.lastIndexOf(':'));
			int proxyPort = Integer.parseInt(proxyServerUrl.substring(proxyServerUrl.lastIndexOf(':') + 1));
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, addr);           
		}       

		// Getting a connection to the feed server from feed server URL object through the already created proxy
		try {
			if(proxy != null){
				urlConnection = feedConnUrl.openConnection(proxy);
				urlConnection.setConnectTimeout(connectTimedout);
			}else{
				urlConnection = feedConnUrl.openConnection();
				urlConnection.setConnectTimeout(connectTimedout);
			}
		} catch (SocketTimeoutException s) {            
			YSystem.printStackTrace(s);
			throw new GeneralException("Connection timed out");            
		} catch (IOException e) {            
			YSystem.printStackTrace(e);
			throw new GeneralException("IOException while opening connection to feed server through proxy");            
		}

		// If it comes here it means the connection to the feed server is successfully created using the proxy server.
		// Below we will set the header fields and send a post request to the feed server and retrieve the output.        

		// specify that we will send output and accept input
		urlConnection.setDoInput(true);

		// set the header from the user supplied headerMap
		for(Iterator<Map.Entry<String,String>> headerIter = headerMap.entrySet().iterator();headerIter.hasNext();)
		{
			Map.Entry<String, String> entry = headerIter.next();
			urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
		}  

		// reading the response
		InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream(),"UTF8");
		StringBuilder buf = new StringBuilder();
		char[] cbuf = new char[ 2048 ];
		int num;

		while ( -1 != (num=reader.read( cbuf )))
		{
			buf.append( cbuf, 0, num );
		}
		contents = buf.toString();
		reader.close();
		TextSanitizer ts = new TextSanitizer();        
		String sanitizedConetent = ts.sanitizeFeedContents(contents);
		YSystem.out(3," Sanitized Response from server after POST : \n" );
		YSystem.out(3,sanitizedConetent);
		return contents; 
	}

	@Override
	public String getFeedContentsThroughProxy(String feedServerUrl,
			String proxyServerUrl, Map<String, String> headerMap,
			String requestXML, InputStream errorStream) throws Exception {
		URL feedConnUrl = null;
		URLConnection urlConnection = null ;        
		String contents = null;
		Proxy proxy = null ;
		//get the timeout value
		Properties sysProps = System.getProperties();

		int connectTimout = 5000; 
		try {
			connectTimout  = Integer.parseInt(sysProps.getProperty(
					"yodlee.gatherer.waitForDocumentComplete"));
		}
		catch (NumberFormatException nfe) {
			YSystem.out("1^WaitForDocumentComplete: cannot parse "
					+ "yodlee.gatherer.waitForDocumentComplete "
					+ sysProps.getProperty("yodlee.gatherer.waitForDocumentComplete"));

			YSystem.out("1^Will be setting to the default value of 5000");          

		}
		YSystem.out(2, "feedServerUrl = "+feedServerUrl+",proxyServerUrl = "+proxyServerUrl);
		YSystem.out(2, "Request Header Map : " +  headerMap);
		if(requestXML != null){
			YSystem.out(2, "Length of Request Payload (Payload is not printed for security reasons ): " + requestXML.length());
		}

		// Creating a URL object to feed server
		try {
			feedConnUrl = new URL(feedServerUrl);
		} catch (MalformedURLException e) {
			YSystem.printStackTrace(e);
			throw new GeneralException("feedServerUrl is Malformed");
		}

		if(proxyServerUrl != null)
		{
			// Creating a Proxy object reflecting the proxy server and all its settings
			String proxyHost = proxyServerUrl.substring(proxyServerUrl.indexOf("://") + 3, proxyServerUrl.lastIndexOf(':'));
			int proxyPort = Integer.parseInt(proxyServerUrl.substring(proxyServerUrl.lastIndexOf(':') + 1));
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, addr);           
		}       

		// Getting a connection to the feed server from feed server URL object through the already created proxy
		try {
			if(proxy != null){
				urlConnection = feedConnUrl.openConnection(proxy);
				urlConnection.setConnectTimeout(connectTimout);
			}else{
				urlConnection = feedConnUrl.openConnection();
				urlConnection.setConnectTimeout(connectTimout);
			}
		}  catch (SocketTimeoutException s) {            
			YSystem.printStackTrace(s);
			throw new GeneralException("Connection timed out");            
		}catch (IOException e) {            
			YSystem.printStackTrace(e);
			throw new GeneralException("IOException while opening connection to feed server through proxy");            
		}

		// If it comes here it means the connection to the feed server is successfully created using the proxy server.
		// Below we will set the header fields and send a post request to the feed server and retrieve the output.        

		// specify that we will send output and accept input
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);

		// set the header from the user supplied headerMap
		for(Iterator<Map.Entry<String,String>> headerIter = headerMap.entrySet().iterator();headerIter.hasNext();)
		{
			Map.Entry<String, String> entry = headerIter.next();
			urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
		}  

		// tell the web server what we are sending
		OutputStreamWriter writer = null;
		InputStreamReader reader = null;
		try{

			if(requestXML != null ){
				writer = new OutputStreamWriter(urlConnection.getOutputStream(),"UTF8");
				writer.write(requestXML);
				writer.flush();
			}

			// reading the response
			reader = new InputStreamReader(urlConnection.getInputStream(),"UTF8");
			StringBuilder buf = new StringBuilder();
			char[] cbuf = new char[ 2048 ];
			int num;

			while ( -1 != (num=reader.read( cbuf )))
			{
				buf.append( cbuf, 0, num );
			}
			contents = buf.toString();

			TextSanitizer ts = new TextSanitizer();
			String sanitizedConetent = ts.sanitizeFeedContents(contents);
			YSystem.out(3," Sanitized Response from server after POST : \n" );
			YSystem.out(3,sanitizedConetent);
		}
		catch(Exception ex){
			YSystem.out(3,"Got exception while getting feed content:");
			YSystem.printStackTrace(ex);
			if(urlConnection instanceof HttpURLConnection ){
				HttpURLConnection httpurlConnection = (HttpURLConnection)urlConnection;
				errorStream = httpurlConnection.getErrorStream();
			}
		}
		finally{
			if(writer != null){
				writer.close();
			}
			if(reader != null){
				reader.close();
			}
		}

		return contents; 
	}

	@Override
	public Map<String, Map<String, String>> getCobrandPropertyBag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getMemberPropertyBag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getMemItemPropertyBag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, String>> getPropertyBagForType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	/**
	 * Get the value of a property in a particular type of property bag. Returns NULL if the property
	 * bag type or property does not exist.
	 * 
	 * @param propBagType type of property bag
	 * @param propKey the key name of the property
	 * @return the value of the property
	 * @throws IllegalArgumentException If propBagType or propKey is empty or null
	 * 
	 */
	public String getPropertyValue(String propBagType, String propKey) {
		if (StringUtil.isEmpty(propBagType) || StringUtil.isEmpty(propKey)) {
			throw new IllegalArgumentException(
					"The propBagType and propKey cannot be empty or null");
		}

		if (propBagType.equals(PropertyBagManager.MEM_PROPERTY_BAG) || propBagType.equals(PropertyBagManager.MEM_ITEM_PROPERTY_BAG)) {
			throw new IllegalArgumentException(
					"A prefOrder must be supplied for MEM_PREF and MEM_ITEM_PREF property bag types");
		}

		return this.m_pGathererRequest.getPropertyBags().getPropertyValue(
				propBagType, propKey, "0");
	}

	@Override
	public boolean isPropertyWritable(String propBagType, String propKey) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPropertyValidationRule(String propBagType, String propKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePropBagPropertyValue(String propBagType, String propKey,
			String propNewValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean dispatchEvent(IHTMLElement element,
			EventType eventType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dispatchEvent(IHTMLElement element, Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean iseBillDocumentRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStatementDocumentRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTrasnactionDocumentRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInputWithEvents(String field, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputWithEvents(String formName, String field, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputAnyWithEvents(String field, String value) {
		// TODO Auto-generated method stub
		setInputAny(field, value);


	}

	public void selectOptionWithEvents(String attributeName,
			String optionAttribute, HiddenString optionValue,
			boolean caseSensitive, boolean matchExactly) {
		if (optionValue != null)
			selectOption("name", attributeName, optionAttribute,
					HiddenStringExtractor.extractHiddenString(optionValue),
					matchExactly);
		else
			YSystem.out(2, "optionValue is NULL");

	}

	@Override
	public void selectOptionWithEvents(String attributeName,
			String optionAttribute, String optionValue, boolean caseSensitive,
			boolean matchExactly) {
		selectOption("name", attributeName, optionAttribute, optionValue,
				matchExactly);
	}

	@Override
	public void selectOptionWithEvents(String attribute, String value,
			String optionAttribute, String optionValue) {
		selectOption(attribute, value, optionAttribute, optionValue, false);
	}

	@Override
	public void selectOptionWithEvents(String attribute, String value,
			String optionAttribute, HiddenString optionValue) {
		if (optionValue != null)
			selectOption(attribute, value, optionAttribute,
					HiddenStringExtractor.extractHiddenString(optionValue),
					false);
		else
			YSystem.out(2, "optionValue is NULL");

	}

	public void selectOptionWithEvents(String attribute, String value, int optionnum) {
		// TODO Auto-generated method stub
		selectOption(true, attribute, value, optionnum);
	}


	private void selectOption(boolean b, String attribute, String value,
			int optionnum) {

		String path = pathBuilder("select", attribute, value, false);
		WebElement ele =  getWebElement(path);
		MySelect select=new MySelect(ele);
		select.selectByIndex(optionnum);
	}

	@Override
	public DownloadResponse clickForDownload(IHTMLElement element) {
		DownloadResponse dr = null;
		WebElement webele=element.getWebElement();
		//String tagName=webele.getTagName();

		//webele.click();
		String path=fetchWebElement1(webele);
		dr=downloadFile(path);
		return dr;

	}

	public  boolean isPageNull()
	{
		Boolean  isnull=false;
		String source=driver.getPageSource();
		if(source.equalsIgnoreCase("null"))
		{
			isnull=true;
		}
		return isnull;
	}

	public void executeScript(IYodRobot robot, int frameIndex, String script) {
		// TODO Auto-generated method stub
		if(frameIndex!=-1)
		{
			List<WebElement> findIFrames = webElementFinder("//*[contains(local-name(), 'frame')]");
			WebElement frame=findIFrames.get(frameIndex);
			WebElement frameBody=frame.findElement(By.tagName("body"));
			((JavascriptExecutor) BrowserManager.get().getWebDriver()) .executeScript(script,frameBody);
		}
		else
		{
			WebElement body=driver.findElement(By.tagName("body"));
			((JavascriptExecutor) BrowserManager.get().getWebDriver()) .executeScript(script,body);
		}

		setPageText(null);
	}


	public void switchtoWindow(int i) throws Exception {
		// TODO Auto-generated method stub
		Robot.switchWindow(i);
	}

	@Override
	public DownloadResponse clickWithEventsForDownload(IHTMLElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickForDownload(String tag, String attrib,String val) {

		DownloadResponse dr = null;
		String path = pathBuilder(tag, attrib, val, false);
		traverseAllFrames(path, true);//Switching to correct frame
		dr=downloadFile(path);
		return dr;

	}
	public DownloadResponse downloadFile(String path){
		DownloadResponse dr = null;
		boolean isDownloaded = true;
		setPageText(null);
		try {
			if(path.startsWith("//")){
				isDownloaded = Robot.downloadFile(path, ElementsType.BY_XPATH);
			}else{
				isDownloaded = Robot.downloadFile(path, ElementsType.BY_CSSSELECTOR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isDownloaded){
			String fileName = (String) AgentContext.getGlobalParams(GlobalParamConstants.DOWNLOAD_FILE_NAME);
			String fileLocation = (String) AgentContext.getGlobalParams(GlobalParamConstants.DOWNLOAD_FILE_LOCATION);
			String fileExtension = (String) AgentContext.getGlobalParams(GlobalParamConstants.DOWNLOAD_FILE_EXT);
			Path source = Paths.get(fileLocation);
			String mimeType = null;
			double latency = (double) AgentContext.getGlobalParams(GlobalParamConstants.DOWNLOAD_FILE_TIME);
			try {
				mimeType = Files.probeContentType(source);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dr = new YDownloadResponse(fileName, fileLocation, mimeType,latency*1000);

		}
		return dr;
	}

	@Override
	public DownloadResponse clickWithEventsForDownload(String tag,
			String attrib, String val) {
		return clickForDownload(tag,attrib,val);
	}

	@Override
	public DownloadResponse clickLinkwithTextForDownload(String value) {
		// TODO Auto-generated method stub
		DownloadResponse dr=null;
		String path = pathBuilder("a", "innertext", value, false);
		dr=downloadFile(path);
		return dr;
	}

	@Override
	public DownloadResponse clickLinkwithTextWithEventsForDownload(String value) {
		return clickForDownload("a", "innertext", value);
	}

	@Override
	public DownloadResponse clickButtonWithValueForDownload(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickButtonWithValueWithEventsForDownload(
			String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickForDownload(Proxy proxy, IHTMLElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickWithEventsForDownload(Proxy proxy,
			IHTMLElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickForDownload(Proxy proxy, String tag,
			String attrib, String val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickWithEventsForDownload(Proxy proxy, String tag,
			String attrib, String val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickLinkwithTextForDownload(Proxy proxy,
			String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickLinkwithTextWithEventsForDownload(Proxy proxy,
			String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickButtonWithValueForDownload(Proxy proxy,
			String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse clickButtonWithValueWithEventsForDownload(
			Proxy proxy, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse getFile(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadResponse getFile(Proxy proxy, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBrowserVersion() {
		return "Chrome 25.0";
	}

	@Override
	public void setDownloadTimeout(long timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void expectBrowserRedirection(boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void expectBrowserRedirection(boolean val, long timeoutMills) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHTMLDumpLevel(int dumpLevel) {
		// TODO Auto-generated method stub

		switch(dumpLevel){
		case DISABLE_HTML_DUMPING:
			YSystem.out(3,"disabling HTML Dumping as set by agent");
			YSystem.setDocDumpLevel(0);
			break;
		case DUMP_ON_PAGELOAD_ONLY:
			dumpOnlyOnPageLoad = true;
			YSystem.out(3,"Set HTML Dumping only of final page");
			break;		 
		default:
			break;
		}		 
	}

	@Override
	public void setProcessMaxTime(long seconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isIgnoreCookielist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getFilterCookieList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCookieFilerList(List<String> listofCOokies, boolean bIgnore) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSendLogs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPreExecuteLogs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendPreExecuteLogs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean sendPartialSummaryResponse(List<Container> container) {
		return JController.sendPartialSummaryResponse(container);
	}

	@Override
	public boolean isIntermediateIAVResponseRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendIAVIntermediateResponse(List<Container> container) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetNumNavigations() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumNavigations() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clickMessageBox(int buttonToClick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetMessageBoxClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public DownloadResponse clickWithEventsForDownload(String tag,
			String attrib, String val, String cancelMessage, boolean isTexOnPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRealTimeMFAForQandA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clickRadioButton(IHTMLElement element, boolean wait) {
		// TODO Auto-generated method stub
		boolean clickable = Robot.clickWebElement(element.getWebElement());
		YSystem.out("clickable is : "+ clickable);

		//now using for local testing,will remove later
		/*WebElement e=element.getWebElement();
		WebElement ele1 = getWebElement(fetchWebElement1(e));
		if (ele1.isEnabled()) {
			try{
				ele1.click();

			}catch(Exception ex){
			}

		}
		setPageText(null);*/

	}

	@Override
	public void click(IHTMLElement element, boolean wait) {
		// TODO Auto-generated method stub
		boolean clickable = Robot.clickWebElement(element.getWebElement());
		YSystem.out("clickable is : "+ clickable);
		setPageText(null);

		//now using for local testing,will remove later
		/*WebElement e=element.getWebElement();
		WebElement ele1 = getWebElement(fetchWebElement1(e));
		if (ele1.isEnabled()) {
			try{
				ele1.click();

			}catch(Exception ex){
			}

		}
		setPageText(null);*/

	}


	/**
	 * 
	 * This API will return the innertext of the given element using Selenium Webdriver, i.e. webElement.getText() will be returned. 
	 * 
	 * @param Path of the element. Either CSS Selectors or Xpath
	 * 
	 * @return InnerText of the element
	 */
	public String getTextFromElement(String path){
		String value = "";
		if(path != null && path.length() > 0){
			WebElement elem = findWebElement(path);
			value = elem.getText();
		}

		return value;
	}

	@Override
	public int fillForm(IHTMLDocument2 m_pDoc, String formid,
			Hashtable pVariables, String submitid, int flags, boolean doCrypt) {
		// TODO Auto-generated method stub
		return 0;
	}
	public boolean isTraverseIframes() {
		return false;
	}

	public boolean isTraverseFrames() {
		return false;
	}

	@Override
	public String parseMoney(IHTMLElement money) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseMoney(IHTMLElement money, boolean frac) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Vector getTableRowCellElements(IHTMLTable e, int rownum, int cellnum) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Enumeration<IHTMLElement> getElementsContaining(String elemtag,
			String attrib, String value) {
		Vector<IHTMLElement> elemcollection = new Vector<IHTMLElement>();

		long start = System.currentTimeMillis();
		String path = pathBuilder(elemtag, attrib, value,false);
		//ArrayList<IHTMLElement> webEleMap = null;

		/*if (isTraverseFrames() || isTraverseIframes()) {
			webEleMap = traverseAllFramesHash(path, false,webEleMap);
		} else {*/
		List<WebElement> list = webElementFinder(path);
		if (list!=null && !list.isEmpty()) {
			/*if (webEleMap == null) {
					webEleMap = new ArrayList<IHTMLElement>();
				}*/
			for (WebElement element : list) {
				//webEleMap.put(element, element.getAttribute("outerHTML"));
				elemcollection.add(new IHTMLElement(element));
			}
		}
		//}

		/*if(webEleMap!=null && webEleMap.size()>0){
			for(IHTMLElement ele: webEleMap){
				elemcollection.add(ele);
			}
		}*/
		System.out.println("total time taken in getting elements containing: "+(System.currentTimeMillis()-start));
		return elemcollection.elements();
	}

	private void selectOption(String attributeName, String attributeValue, 
			String optionAttribute, String optionValue, boolean matchExactly) {

		String path = pathBuilder("select", attributeName, attributeValue,matchExactly);
		// FIXME change to ScriptConstants
		WebElement ele = null;
		//ele = getWebElement(path);
		try{
			ele = getWebEle(path);
		}catch(NoSuchElementException ex){
			ex.printStackTrace();
			throw new GeneralException("Select element not found on page with attribute name "+attributeName+ " and attribute value "+attributeValue);
		}
		// FIXME Come here and resolve this MySelect
		MySelect select = new MySelect(ele);
		boolean isSelected = false;
		switch (optionAttribute) {

		case "innerText":
			YSystem.out("Selecting with innerText : " + optionValue);
			if (!matchExactly) {
				isSelected = select.selectWithText(optionValue, false);
			} else {
				isSelected = select.selectWithText(optionValue, true);
			}

			if (isSelected) {
				YSystem.out("Select sucess");
			}
			break;
		case "value":
			YSystem.out("Selection with value :" + optionValue);
			select.selectByValue(optionValue);
			YSystem.out("Select done by value");

		}

		//setPageText(null);

	}

	/**
	 * The HTML source code of the page being viewed. The page source returned
	 * is a representation of the underlying DOM
	 * 
	 * @return String of current HTML source
	 */
	public List<String> getPageContent() {
		long start = System.currentTimeMillis();
		//WebDriver driver = BrowserManager.get().getWebDriver();
		driver.switchTo().defaultContent();
		List<String> pageSourcesList = new ArrayList<String>();
		pageSourcesList.add(driver.getPageSource());
		/*if(isTraverseFrames() || isTraverseIframes()){*/
		pageSourcesList = getFramesSource(driver,false,pageSourcesList);
		//}
		YSystem.out("Total time taken in getting page source: " + (System.currentTimeMillis()-start));
		return pageSourcesList;
	}

	public String getPageSource(){
		return driver.getPageSource();
	}

	/**      *       * @return String of current visible text      */
	public String getPageText() {
		long start = System.currentTimeMillis();
		if(pageText==null){
			driver.switchTo().defaultContent();
			StringBuilder builder = new StringBuilder(); 
			builder = getVisibleText(builder, driver);             
			/*if(isTraverseFrames() || isTraverseIframes()){*/             
			builder = getFramesText(driver,builder,false);  
			//}
			System.out.println("Total time taken in getting page text: " +(System.currentTimeMillis()-start));             
			this.pageText = builder.toString();         
		}       
		return pageText;    
	}

	public void setPageText(String pageText){
		this.pageText = pageText;
		getPageText();
	}

	public StringBuilder getVisibleText(StringBuilder builder, WebDriver driver){
		try{
			WebElement bodyElement = driver.findElement(By.tagName("body"));
			String visibleText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText || arguments[0].textContent;", bodyElement);
			if(visibleText!=null){
				builder.append(" ").append(visibleText);
			}
		}catch(WebDriverException e){
			//YDataLogger.out("Error while getting text of body tag of page");
			return builder;
		}
		return builder;
	}

	public StringBuilder getFramesText(WebDriver driver,StringBuilder builder,boolean isParentFrame){
		List<WebElement> frames = driver.findElements(By.cssSelector("frame,iframe"));
		for(WebElement ele: frames){
			WebDriver frame = driver.switchTo().frame(ele);
			builder = getVisibleText(builder, frame);
			builder = getFramesText(frame, builder, true);
			if(isParentFrame){
				driver.switchTo().parentFrame();
			}else{
				driver.switchTo().defaultContent();
			}
		}
		return builder;
	}


	public List<String> getFramesSource(WebDriver driver,boolean isParentFrame,List<String> pageSourcesList){
		List<WebElement> findElements = driver.findElements(By.tagName("iframe"));
		List<WebElement> findElements2 = driver.findElements(By.tagName("frame"));
		if(findElements.size()==0 && findElements2.size()==0){
			return pageSourcesList;
		}else{
			for (WebElement webElement : findElements) {
				WebDriver frame = driver.switchTo().frame(webElement);
				pageSourcesList.add(frame.getPageSource());				
				pageSourcesList = getFramesSource(frame,true,pageSourcesList);
				if(isParentFrame){
					driver.switchTo().parentFrame();
				}else{
					driver.switchTo().defaultContent();
				}

			}

			for (WebElement webElement : findElements2) {
				WebDriver frame = driver.switchTo().frame(webElement);
				pageSourcesList.add(frame.getPageSource());				
				pageSourcesList = getFramesSource(frame,true,pageSourcesList);
				if(isParentFrame){
					driver.switchTo().parentFrame();
				}else{
					driver.switchTo().defaultContent();
				}

			}
			return pageSourcesList;
		}
	}

	public HashMap<String, HashMap<Bill, DownloadResponse>> getBillUploadDetails() {
		return billUploadDetails;
	}

	public void setBillUploadDetails(String accountID, Bill bill,
			DownloadResponse downloadResponse) {

		if ( accountID != null && bill != null ) {

			if (getBillUploadDetails() == null) {
				billUploadDetails = new HashMap<String, HashMap<Bill, DownloadResponse>>();
			}
			HashMap billOfAccount = billUploadDetails.get(accountID);

			if (billOfAccount == null) {
				billOfAccount = new HashMap<Bill, DownloadResponse>();
				billUploadDetails.put(accountID, billOfAccount);
			}
			billOfAccount.put(bill, downloadResponse);
		}
	}

	public void resetBillUploadDetails(){
		billUploadDetails = null;
	}

	public void removeFromBillUpload(String accountNum, Bill bill) {
		if ((accountNum != null)) {
			if (getBillUploadDetails() != null) {
				HashMap<String, HashMap<Bill, DownloadResponse>> hmBillAccount = getBillUploadDetails();
				if (hmBillAccount.containsKey(accountNum)) {
					HashMap<Bill, DownloadResponse> hmBill = hmBillAccount
							.get(accountNum);
					if (hmBill.containsKey(bill)) {
						hmBill.remove(bill);
					}
					if (hmBill.isEmpty()) {
						hmBillAccount.remove(accountNum);
					}
				}
			}
		}
	}

	private void resetDownloadStatus(){
		this.billDownloadStatus= null;
	}

	public HashMap<Bill, String> getBillDownloadStatus(){
		return billDownloadStatus;
	}

	public void setBillDownloadStatus(Bill bill,String status) {

		if ( status != null && bill != null ) {

			if (getBillDownloadStatus() == null) {
				billDownloadStatus = new HashMap<Bill, String>();
			}
			billDownloadStatus.put(bill, status);
		}
	}


	public boolean setDownloadDocInfo(boolean isPropertyPresent) {
		boolean docAvailable = true;

		if (getBillUploadDetails() != null) {
			HashMap<String, HashMap<Bill, DownloadResponse>> hmBill = getBillUploadDetails();
			for (HashMap<Bill, DownloadResponse> hmValues : hmBill.values()) {
				for (Bill bill : hmValues.keySet()) {
					DownloadResponse dr = hmValues.get(bill);
					DocumentInfo docInfo = new DocumentInfo();
					if (dr != null) {
						docInfo.setDocumentName(dr.getFilename());
						//Setting to default value since link node of statements is overrided for some other use at agent
						docInfo.setDocumentSource("javascript: void(0)");
						//Doing the below looping since most of the cases downloadresponse.extension is coming as NULL. Hence identifying the extension from content type
						//Content type will be in the format Application/*ext* or *ext*/HTML
						String contentType = "";
						boolean supported=true;
						if((dr.getContentType() != null && !dr.getContentType().equals(""))||(dr.getFileExtension() != null && !dr.getFileExtension().equals(""))){
							YSystem.out(2,"^^Download response's Content type is :-  "+dr.getContentType());
							YSystem.out(2,"^^Download response's Extension is :-  "+dr.getFileExtension());
							contentType = (dr.getContentType() != null && !dr.getContentType().equals(""))?dr.getContentType():dr.getFileExtension();
							if(contentType.toLowerCase().contains("pdf")){contentType="pdf";}
							else if(contentType.toLowerCase().contains("csv")){contentType="csv";}
							else if(contentType.toLowerCase().contains("text")){contentType="txt";}
							else if(contentType.toLowerCase().contains("ofx")){contentType="ofx";}
							else if(contentType.toLowerCase().contains("excel")){contentType="xls";}
							else if(contentType.toLowerCase().contains("sheet")){contentType="xlsx";}
							else if(contentType.toLowerCase().contains("xml")){contentType="xml";}
							else {
								supported = false;
							}
						} else{
							supported = false;
						}
						if(!supported){
							DocumentStatus docStatus = docInfo.new DocumentStatus();
							docStatus.setDocumentStatus(DocumentConstants.DOCUMENT_TYPE_NOT_SUPPORTED);
							docStatus.setErrorCode("578");
							docStatus.setExceptionStackTrace("CDATA[DOCUMENT_TYPE_NOT_SUPPORTED]");
							docInfo.setDocStatus(docStatus);
							YSystem.out(2,"Content type is not PDF/CSV/TEXT/OFX/XLS/XML ..!!Hence not uploading.");
							supported = true;
							docAvailable = false;
						}
						docInfo.setDocumentExtension(contentType);
						docInfo.setDocumentSize(""+ (dr.getDownloadedSize() / 1000));
						double latency = dr.getDownloadLatency();
						docInfo.setDocumentDownloadLatency(latency+"");

					} else {
						if(getBillDownloadStatus() != null && getBillDownloadStatus().containsKey(bill) && getBillDownloadStatus().get(bill) != null){
							String docStatus = getBillDownloadStatus().get(bill);
							if(docStatus != null && docStatus.length() > 0){
								if(docStatus.contains(DocumentConstants.DOCUMENT_NOT_AVAILABLE)){
									DocumentStatus docStatusNode = docInfo.new DocumentStatus();
									docStatusNode.setErrorCode("570");
									docStatusNode.setExceptionStackTrace("CDATA[DOCUMENT_NOT_AVAILABLE]");
									docStatusNode.setDocumentStatus(DocumentConstants.DOCUMENT_NOT_AVAILABLE);
									docInfo.setDocStatus(docStatusNode);
								}else if(docStatus.contains(DocumentConstants.DOCUMENT_DOWNLOAD_FAILED)){
									DocumentStatus docStatusNode = docInfo.new DocumentStatus();
									docStatusNode.setErrorCode("571");
									docStatusNode.setExceptionStackTrace("CDATA[DOCUMENT_DOWNLOAD_FAILED]");
									docStatusNode.setDocumentStatus(DocumentConstants.DOCUMENT_DOWNLOAD_FAILED);
									docInfo.setDocStatus(docStatusNode);
								}
							}						
							docStatus=null;//reseting

						}else{   //Reaches here if the agent didnt set the download status. Since the download response is NULL,
							//setting the default value as doc download failed.
							DocumentStatus docStatus = docInfo.new DocumentStatus();
							docStatus.setErrorCode("571");
							docStatus.setExceptionStackTrace("CDATA[DOCUMENT_DOWNLOAD_FAILED]");
							docStatus.setDocumentStatus(DocumentConstants.DOCUMENT_DOWNLOAD_FAILED);
							docInfo.setDocStatus(docStatus);
						}
						//						docInfo.setDocumentRetry("true");
						docAvailable = false;
					}
					bill.setDocumentInfo(docInfo);
				}
			}
		}

		return docAvailable;
	}

	public void uploadDocument(){
		if(getBillUploadDetails() != null){
			HashMap<String, HashMap<Bill, DownloadResponse>> hmBill = getBillUploadDetails();
			for(HashMap<Bill, DownloadResponse> hmValues : hmBill.values()){
				for(Bill bill: hmValues.keySet()){
					DownloadResponse dr = hmValues.get(bill);
					if(dr == null){
						YSystem.out("2^^Download Response is NULL");
						break;
					}
					String randNumber = UUID.randomUUID().toString();
					DocumentUploader docUploader = new DocumentUploader(randNumber, dr.getFilepath());
					DocumentInfo docInfo = bill.getDocumentInfo();
					if(docInfo == null){
						docInfo = new DocumentInfo();
					}
					try {
						docUploader.storeDocument(dr.getFilepath(),dr.getDownloadedSize(), randNumber,docInfo);
					} catch (Exception e) {
						YSystem.out(2,"Exception while uploading document:");
						YSystem.printStackTrace(e);
						DocumentStatus docStatus = docInfo.new DocumentStatus();
						docStatus.setDocumentStatus(DocumentConstants.DOCUMENT_UPLOAD_FAILED);
						docStatus.setErrorCode("576");
						docStatus.setExceptionStackTrace(e.getMessage());
						docInfo.setDocStatus(docStatus);
						//						docInfo.setdocUploadStatus("1");
					}
					bill.setDocumentInfo(docInfo);
				}
			}
		}
		resetDownloadStatus();
	}	

}
