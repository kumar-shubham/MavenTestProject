package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HDFCBase {
	
	public int login(WebDriver driver){
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 20);
		String username = "*******";
		String password = "*******";
		
		js.executeScript("window.location='https://netbanking.hdfcbank.com/netbanking'");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("frame[name='login_page']")));
		
		
		driver.switchTo().frame("login_page");
		WebElement userEle = driver.findElement(By.cssSelector("input[name='fldLoginUserId']"));
		
		userEle.sendKeys(username);
		
		WebElement nextEle = driver.findElement(By.xpath("//img[contains(@src, '/gif/continue')]//parent::a"));
		
		nextEle.click();
		sleep(2);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("frame[name='login_page']")));
		driver.switchTo().defaultContent();
		driver.switchTo().frame("login_page");
		
		WebElement passEle = driver.findElement(By.cssSelector("input[name='fldPassword']"));
		
		passEle.sendKeys(password);
		
		WebElement checkEle = driver.findElement(By.id("chkrsastu"));
		
		checkEle.click();
		sleep(1);
		
		WebElement loginEle = driver.findElement(By.xpath("//img[contains(@src, '/gif/login')]//parent::a"));
		
		loginEle.click();
		sleep(3);
		
		return 1;
		
	}
	
	public List<?extends Container> getAccounts(WebDriver driver){
		
		List<Container> accountList = new ArrayList<Container>();
		
		getBankAccounts(driver, accountList);
		getCardAccounts(driver, accountList);
		getLoanAccounts(driver, accountList);
		
		return accountList;
	}

	private void getBankAccounts(WebDriver driver, List<Container> accountList) {
		// TODO Auto-generated method stub
		navigateToAccountSummryPage(driver);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		WebElement toggleEle = null;
		try{
			toggleEle = driver.findElement(By.xpath("//img[contains(@src, 'arrowIcon.png')]//parent::a"));
			toggleEle.click();
		}
		catch(NoSuchElementException e){
			System.out.println("already open");
		}
		
		
		List<WebElement> rowElements = driver.findElements(By.xpath("//tr[@class='unhideSavingAccts']"));
		String accountNumer = "";
		String accountHolder = "";
		String branch = "";
		String accountBalance = "";
		for (WebElement webElement : rowElements) {
			List<WebElement> colElements = webElement.findElements(By.tagName("td"));
			
			int count = 0;
			WebElement inner = null;
			for(WebElement colEle : colElements){
				if(count == 0){
					inner = colEle.findElement(By.tagName("a"));
					accountNumer = inner.getText().trim();
				}
				else if(count == 1){
					inner = colEle.findElements(By.tagName("span")).get(1);
					branch = inner.getText().trim();
				}
				else if(count == 2){
					inner = colEle.findElements(By.tagName("span")).get(1);
					accountHolder = inner.getText().trim();
				}
				else if(count == 3){
					inner = colEle.findElements(By.tagName("span")).get(1);
					accountBalance = inner.getText();
					accountBalance = accountBalance.replace("INR", "").trim();
				}
				count++;
			}
			
			BankAccount ba = new BankAccount();
			ba.setAccountNumber(accountNumer);
			ba.setAccountBalance(accountBalance);
			ba.setBranch(branch);
			ba.setAccountHolder(accountHolder);
			accountList.add(ba);
		}	
		
		
	}

	private void getCardAccounts(WebDriver driver, List<Container> accountList) {
		// TODO Auto-generated method stub
		navigateToAccountSummryPage(driver);
		navigateToCardAcounts(driver);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		
		String sourceText = driver.getPageSource();
		
		if(sourceText.contains("Credit Cards not mapped to this use")){
			System.out.println("No Card Accounts");
			return;
		}
		
		if(!sourceText.contains("Available Credit")){
			System.out.println("Card details not found");
		}
		
		WebElement toggleEle = null;
		try{
			toggleEle = driver.findElement(By.xpath("//img[contains(@src, 'arrowIcon.png')]//parent::a"));
			toggleEle.click();
		}
		catch(NoSuchElementException e){
			System.out.println("already open");
		}
		
		
		List<WebElement> rowElements = driver.findElements(By.xpath("//tbody[@class='unhideActiveCreditCards']"));
		String cardNumber = "";
		String accountName = "";
		String accountHolder = "";
		String availableCredit = "";
		String lastStatementBalance = "";
		for (WebElement webElement : rowElements) {
			List<WebElement> colElements = webElement.findElements(By.tagName("td"));
			
			int count = 0;
			WebElement inner = null;
			for(WebElement colEle : colElements){
				if(count == 0){
					inner = colEle.findElement(By.tagName("a"));
					cardNumber = inner.getText().trim();
				}
				else if(count == 1){
					inner = colEle.findElements(By.tagName("span")).get(1);
					accountName = inner.getText().trim();
				}
				else if(count == 2){
					inner = colEle.findElements(By.tagName("span")).get(1);
					accountHolder = inner.getText().trim();
				}
				else if(count == 5){
					availableCredit = colEle.getText();
					availableCredit = availableCredit.replace("Available Credit", "").trim();
					
				}
				else if(count == 6){
					lastStatementBalance = colEle.getText();
					lastStatementBalance = lastStatementBalance.replace("*Statement Balance", "").trim();
					
				}
				count++;
			}
			
			CardAccount ca = new CardAccount();
			ca.setAccountNumber(cardNumber);
			ca.setAccountHolder(accountHolder);
			ca.setAccountName(accountName);
			ca.setAvailableCredit(availableCredit);
			ca.setLastStatementBalance(lastStatementBalance);
			
			accountList.add(ca);
		}
		
		
	}

	public void navigateToCardAcounts(WebDriver driver) {
		// TODO Auto-generated method stub
		WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("common_menu1");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Cards']")));
		
		WebElement cardEle = driver.findElement(By.cssSelector("a[title='Cards']"));
		cardEle.click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Active Credit Cards')]")));
		}
		catch(NoSuchElementException e){
			System.out.println("No Card Elements");
		}
		
	}

	private void getLoanAccounts(WebDriver driver, List<Container> accountList) {
		// TODO Auto-generated method stub
		navigateToLoanAccounts(driver);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		WebElement tbodyEle = driver.findElement(By.xpath("//th[contains(text(),'Loan Account No')]/../.."));
		List<WebElement> rowEle = tbodyEle.findElements(By.tagName("tr"));
		for(WebElement row: rowEle){
			
			List<WebElement> colEle = row.findElements(By.tagName("td"));
			String accountNumber = "";
			String principalAmount = "";
			String tenureMonths = "";
			String loanEMI = "";
			String loanType = "";
			int count = 0;
			System.out.println("row text:: " + row.getText());
			if(row.getText().contains("Loan Account No")){
				continue;
			}
			for(WebElement col: colEle){
				String value = col.getText().trim();
				System.out.println("col text :: " + value);
				if(count == 0){
					accountNumber = value;
				}
				else if(count == 1){
					principalAmount = value;
				}
				else if(count == 2){
					tenureMonths = value;
				}
				else if(count == 3){
					loanEMI = value;
				}
				else if(count == 5){
					loanType = value;
				}
				count++;
			}
			
			LoanAccount la = new LoanAccount();
			la.setAccountNumber(accountNumber);
			la.setPrincipalAmount(principalAmount);
			la.setTenureMonths(tenureMonths);
			la.setLoanEMI(loanEMI);
			la.setLoanType(loanType);
			accountList.add(la);
		}
		
		
	}

	public void navigateToLoanAccounts(WebDriver driver) {
		// TODO Auto-generated method stub
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("common_menu1");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Loans']")));
		
		WebElement cardEle = driver.findElement(By.cssSelector("a[title='Loans']"));
		cardEle.click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		
		try{
			System.out.println("before waiting loan");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Loan Account No')]")));
			System.out.println("after waiting loan");
		}
		catch(NoSuchElementException e){
			System.out.println("No Loans Found");
		}
		
		
		
	}

	public void navigateToAccountSummryPage(WebDriver driver) {
		// TODO Auto-generated method stub
		WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("common_menu1");
		WebElement homePageEle = driver.findElement(By.xpath("//img[@name='img_acct']//parent::a"));
		homePageEle.click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Account Summary')]")));
		
		
		
		
	}

	public void logout(WebDriver driver) {
		// TODO Auto-generated method stub
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("common_menu1");
		
		WebElement logoutEle = driver.findElement(By.cssSelector("a[title='Log Out']"));
		logoutEle.click();
		
	}
	
	public void sleep(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
