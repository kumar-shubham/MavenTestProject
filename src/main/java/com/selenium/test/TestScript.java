package com.selenium.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.exception.LoginException;
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.HoldingAsset;
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
			int loginStatus = login(sherlock);
			getAccounts(sherlock, accountList);

			for(Container container: accountList){
				InvestmentAccount account = (InvestmentAccount) container;
				getAssets(sherlock, account);
			}
		}
		finally{
			sherlock.getDriverObject().quit();
		}	
	}

	static int login(Sherlock sherlock) throws ParseException, LoginException{

		sherlock.openURL("https://ebanking2.ca-indosuez.sg/login/doLogin"); // Remove

		WebElement userIdEle = sherlock.findElementById("username_textbox");
		WebElement pinEle = sherlock.findElementById("pincode_textbox");
		WebElement passCodeEle = sherlock.findElementById("passcode_textbox");
		WebElement passwordEle = sherlock.findElementById("password_textbox");
		WebElement loginButtonEle = sherlock.findElementById("bnext");

		userIdEle.sendKeys("123456789");
		pinEle.sendKeys("123456");
		passCodeEle.sendKeys("123456");
		passwordEle.sendKeys("123456");
		//		loginButtonEle.click();

		sherlock.openURL("file:///home/kumar/kumar/taurus/CA/home.html");
		//For invalid login details
		if(Watson.checkIfTextPresentOnPage1(sherlock, "The information you have given are not valid for access", false)){
			throw new LoginException("The information you have given are not valid for access. Please enter the right information.");
		}


		return Constants.SUCCESS_CODE;
	}

	static void getAccounts(Sherlock sherlock, List<Container> accountList) throws Exception{

		WebElement portfolioEle = sherlock.findElementByXPath("//a[div[text() = 'Portfolio']]");
		portfolioEle.click();

		WebElement acctListEle = sherlock.findElementByXPath("//select[contains(@id, ':selectRelation')]");

		Select accSelEle = new Select(acctListEle);

		List<WebElement> options = accSelEle.getOptions();

		for(WebElement option: options){
			String text = option.getText().trim();
			System.out.println(text);
			String accNum = text.substring(0, text.indexOf(" "));
			System.out.println(accNum);
			InvestmentAccount account = new InvestmentAccount(null);
			account.setAccountNumber(accNum);
			accountList.add(account);
		}
	}

	static void getAssets(Sherlock sherlock, InvestmentAccount account){

		List<HoldingAsset> assets = new ArrayList<HoldingAsset>();

		WebElement acctListEle = sherlock.findElementByXPath("//select[contains(@id, ':selectRelation')]");

		Select accSelEle = new Select(acctListEle);

		accSelEle.selectByValue(account.getAccountNumber());

		getShares(sherlock, account);

		getBondNotes(sherlock, account);

		getCurrentAccounts(sherlock, account);

		getLoans(sherlock, account);

	}

	private static void getShares(Sherlock sherlock, InvestmentAccount account) {

		List<WebElement> rows = sherlock.findElementsByXPath("//div[b[text()='Shares']]/table/tbody/tr");

		for(WebElement row: rows){

			System.out.println(row.getText());
			List<WebElement> cols = row.findElements(By.tagName("td"));
			int count = 0;
			HoldingAsset tempAsset = null;
			boolean validAsset = true;
			for(WebElement col: cols){
				String text = col.getText().trim();

				text = text.replace("'", "");
				System.out.print(text + "::");

				if(count == 1){
					tempAsset = new HoldingAsset();
				}
				else if(count == 2){
					tempAsset.setHoldingAssetDescription(text);
				}
				else if(count == 3){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetQuantity(text, true);
				}
				else if(count == 5){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrency(text);
				}
				else if(count == 6){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrentValue(text, true);
				}
				else if(count == 7){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetIndicativePrice(text, true);
				}
				else if(count == 8){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetAverageUnitCost(text, true);
				}
				else if(count == 12){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetISIN(text);
				}
				count++;
			}
			if(validAsset){
				System.out.println("~~~~~~~~ADDED~~~~~~~~~~");
				tempAsset.setHoldingAssetAccountNumber(account.getAccountNumber());
				tempAsset.setAccountCurrency(account.getCurrency());
				tempAsset.setHoldingAssetCategory(HoldingAsset.CATEGORY_EQUITY);
				account.addAsset(tempAsset);
			}
			tempAsset = null;
		}

		System.out.println("TOTAL SHARES ->>> " + account.getAssets().size());

	}

	private static void getBondNotes(Sherlock sherlock, InvestmentAccount account) {

		List<WebElement> rows = sherlock.findElementsByXPath("//div[b[text()='Bonds/Notes']]/table/tbody/tr");

		for(WebElement row: rows){

			System.out.println(row.getText());
			List<WebElement> cols = row.findElements(By.tagName("td"));
			int count = 0;
			HoldingAsset tempAsset = null;
			boolean validAsset = true;
			for(WebElement col: cols){
				String text = col.getText().trim();

				text = text.replace("'", "");
				System.out.print(text + "::");

				if(count == 1){
					tempAsset = new HoldingAsset();
				}
				else if(count == 2){
					tempAsset.setHoldingAssetDescription(text);
				}
				else if(count == 3){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetQuantity(text, true);
				}
				else if(count == 5){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrency(text);
				}
				else if(count == 6){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrentValue(text, true);
				}
				else if(count == 7){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetAccruedInterest(text, true);
				}
				else if(count == 9){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetAverageUnitCost(text, true);
				}
				else if(count == 12){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetYield(text, true);
				}
				else if(count == 15){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetISIN(text);
				}
				count++;
			}
			if(validAsset){
				System.out.println("~~~~~~~~ADDED~~~~~~~~~~");
				tempAsset.setHoldingAssetAccountNumber(account.getAccountNumber());
				tempAsset.setAccountCurrency(account.getCurrency());
				tempAsset.setHoldingAssetCategory(HoldingAsset.CATEGORY_BOND);
				account.addAsset(tempAsset);
			}
			tempAsset = null;
		}

		System.out.println("TOTAL BONDS ->>> " + account.getAssets().size());

	}

	private static void getCurrentAccounts(Sherlock sherlock, InvestmentAccount account) {

		List<WebElement> rows = sherlock.findElementsByXPath("//div[b[text()='Current accounts']]/table/tbody/tr");

		for(WebElement row: rows){

			System.out.println(row.getText());
			List<WebElement> cols = row.findElements(By.tagName("td"));
			int count = 0;
			HoldingAsset tempAsset = null;
			boolean validAsset = true;
			for(WebElement col: cols){
				String text = col.getText().trim();

				text = text.replace("'", "");
				System.out.print(text + "::");

				if(count == 1){
					tempAsset = new HoldingAsset();
				}
				else if(count == 2){
					tempAsset.setHoldingAssetDescription(text);
				}
				else if(count == 3){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrency(text);
				}
				else if(count == 4){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrentValue(text, true);
					tempAsset.setHoldingAssetQuantity(text, true);
				}
				count++;
			}
			if(validAsset){
				System.out.println("~~~~~~~~ADDED~~~~~~~~~~");
				tempAsset.setHoldingAssetAccountNumber(account.getAccountNumber());
				tempAsset.setAccountCurrency(account.getCurrency());
				tempAsset.setHoldingAssetCategory(HoldingAsset.CATEGORY_CASH);
				account.addAsset(tempAsset);
			}
			tempAsset = null;
		}

		System.out.println("TOTAL Current accounts  ->>> " + account.getAssets().size());

	}

	private static void getLoans(Sherlock sherlock, InvestmentAccount account) {
		
		List<WebElement> rows = sherlock.findElementsByXPath("//div[b[text()='Loans']]/table/tbody/tr");

		for(WebElement row: rows){

			System.out.println(row.getText());
			List<WebElement> cols = row.findElements(By.tagName("td"));
			int count = 0;
			HoldingAsset tempAsset = null;
			boolean validAsset = true;
			for(WebElement col: cols){
				String text = col.getText().trim();

				text = text.replace("'", "");
				System.out.print(text + "::");

				if(count == 1){
					tempAsset = new HoldingAsset();
				}
				else if(count == 2){
					tempAsset.setHoldingAssetDescription(text);
				}
				else if(count == 3){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrency(text);
				}
				else if(count == 4){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetCurrentValue(text, true);
					tempAsset.setHoldingAssetQuantity(text, true);
				}
				else if(count == 5){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetAccruedInterest(text, true);
				}
				else if(count == 7){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetIndicativePrice(text, true);
				}
				else if(count == 8){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetAverageUnitCost(text, true);
				}
				else if(count == 12){
					if(StringUtils.isEmpty(text)){
						validAsset = false;
						break;
					}
					tempAsset.setHoldingAssetISIN(text);
				}
				count++;
			}
			if(validAsset){
				System.out.println("~~~~~~~~ADDED~~~~~~~~~~");
				tempAsset.setHoldingAssetAccountNumber(account.getAccountNumber());
				tempAsset.setAccountCurrency(account.getCurrency());
				tempAsset.setHoldingAssetCategory(HoldingAsset.CATEGORY_EQUITY);
				account.addAsset(tempAsset);
			}
			tempAsset = null;
		}

		System.out.println("TOTAL SHARES ->>> " + account.getAssets().size());

	}

}
