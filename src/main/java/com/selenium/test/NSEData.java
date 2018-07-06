package com.selenium.test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class NSEData {

	static ACADriver driver = null;

	public static void main(String[] args) throws Exception {
		
		SymbolStore.createSymbolList();

		driver = new ACADriver();
		Sherlock sherlock = new Sherlock();
		sherlock.setDriver(driver.getDriver());
		sherlock.setAcaDriver(driver);
		
		List<String> symbolList = new ArrayList<String>();  

		System.out.println("fetching symbol List");
		try {
			symbolList = SymbolStore.getSymbolList();
			getReport(sherlock, symbolList);
			System.out.println(symbolList);

		} finally {
			sherlock.getDriverObject().quit();
		}
	}

	private static void getReport(Sherlock sherlock, List<String> symbolList) throws Exception {
		
		System.out.println("inside getReport");
		sherlock.openURL("https://nseindia.com/products/content/equities/equities/eq_security.htm");
		
		WebElement selectEle = sherlock.findElementById("dateRange");
		Select select = new Select(selectEle);
		select.selectByValue("24month");
		
		int i = 0;
		for(String symbol : symbolList) {
			System.out.println("going for symbol => " + symbol);
			i++;
			if(i > 5) {
				break;
			}
			
			WebElement symbolInput = sherlock.findElementById("symbol");
			
			sherlock.sendInput(symbolInput, symbol);
			
			
			WebElement getBtn = sherlock.findElementById("get");
			
			getBtn.click();
			
			String loaderName = "/images/loading_trades.gif";
			
			String loaderXPath = "//img[@src = '/images/loading_trades.gif']";
			
			int loopCount = 0;
			while(true) {
				if(loopCount > 20) {
					break;
				}
				loopCount++;
				if(Watson.checkIfElementPresentOnThePage(sherlock, By.xpath(loaderXPath), true)) {
					Watson.sleep(1);
				}
				else {
					break;
				}
			}
			
			WebElement downloadEle = sherlock.findElementByXPath("//a[text() = 'Download file in csv format']");
			
			Watson.downLoadFile(sherlock, downloadEle, ".csv");
			
		}
		
	}

}
