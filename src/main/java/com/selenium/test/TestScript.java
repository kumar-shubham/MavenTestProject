package com.selenium.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.exception.LoginException;
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.InvestmentAccount;

public class TestScript {

	static ACADriver driver = null;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		driver = new ACADriver();
		Sherlock sherlock = new Sherlock();
		sherlock.setDriver(driver.getDriver());

		List<Container> accountList = new ArrayList<Container>();  
		try{
						login(sherlock);
						getAccounts(sherlock, accountList);
						getStatements(sherlock, (InvestmentAccount)accountList.get(0));
						
		}
		finally{
			sherlock.getDriverObject().quit();
		}	
	}


	private static void getStatements(Sherlock sherlock, InvestmentAccount account) throws Exception {
		
		WebElement sidemenuDocLink = sherlock.findElementById("Documents-sidebar");
		sidemenuDocLink.click();
		
		closePopup(sherlock);
		
		WebElement statementLink = sherlock.findElementByXPath("//td[contains(text(), 'STATEMENTS')");
		statementLink.click();
		
		closePopup(sherlock);
		
		WebElement dropdownButton = sherlock.findElementByCSSSelector("button.select");
		dropdownButton.click();
		
		WebElement optionList = sherlock.findElementByCSSSelector("li[value='lastMonth']");
		optionList.click();
		
		WebElement pdfEle = sherlock.findElementByCSSSelector("div.downloadlink");
		
	}


	private static void getAccounts(Sherlock sherlock, List<Container> accountList) {
		
		closePopup(sherlock);
		
		WebElement accNumSpan = sherlock.findElementByCSSSelector("div.acc_description+span");
		String accountNum = accNumSpan.getText().trim();
		accountNum = accountNum.substring(0, accountNum.indexOf(" "));
		
		System.out.println(accountNum);
		InvestmentAccount account = new InvestmentAccount(null);
		account.setAccountNumber(accountNum);
		accountList.add(account);
		
	}
	
	private static void closePopup(Sherlock sherlock){
		
		if(Watson.checkIfElementPresentOnThePage(sherlock, By.id("citi-modal-close"), false)){
			System.out.println("Popup found. closing...");
			WebElement popupClose = sherlock.findElementById("citi-modal-close");
			popupClose.click();
		}
		
	}


	static int login(Sherlock sherlock) throws ParseException, LoginException{

		sherlock.openURL("https://www.privatebank.citibank.com/"); // Remove

		WebElement userIdEle = sherlock.findElementByName("user");
		WebElement passwordEle = sherlock.findElementByName("password");
		WebElement loginButtonEle = sherlock.findElementByName("logon");

		userIdEle.sendKeys("123456789");
		passwordEle.sendKeys("123456");
		loginButtonEle.click();

		if(Watson.checkIfElementPresentOnThePage(sherlock, By.id("citiLoginErrorBarPopup"), true)){
			WebElement errorBox = sherlock.findElementById("citiLoginErrorBarPopup");
			
			if(errorBox.isDisplayed()){
				throw new LoginException("We are unable to validate your login information. Please re-enter your Username and Password.");
			}
		}

		sherlock.openURL("file:///home/kumar/kumar/taurus/Citi%20Private%20Bank/overview_1.html");
		
		System.out.println("SUCCESS");
		return Constants.SUCCESS_CODE;
	}

}
