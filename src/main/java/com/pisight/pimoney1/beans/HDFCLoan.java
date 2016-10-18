package com.pisight.pimoney1.beans;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HDFCLoan extends HDFCBase {
	
public void execute(WebDriver driver, List<? extends Container> accountList){
		System.out.println();
		System.out.println("test loan execute 1");
		for(Container account: accountList){
			System.out.println("test loan execute 2");
			if(account instanceof LoanAccount){
				System.out.println("test loan execute 3");
				LoanAccount la = (LoanAccount)account;
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
				boolean exists = driver.findElements(By.cssSelector("select[name='selLoan']")).size() != 0;
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				if(!exists){
					navigateToLoanTransactions(driver);
					System.out.println("test loan execute 4");
				}
				selectAccountForTransactions(driver, la);
				System.out.println("test loan execute 5");
				getAccountTransactions(driver, la);
				System.out.println("test loan execute 6");
			}
		}
	}

private void getAccountTransactions(WebDriver driver, LoanAccount la) {
	// TODO Auto-generated method stub
	System.out.println();
	System.out.println("test loan getAcctTrans 1");
	driver.switchTo().defaultContent();
	driver.switchTo().frame("main_part");
	
	WebElement tableEle = driver.findElement(By.cssSelector("table[class='datatable']"));
	
	List<WebElement> rowEles = tableEle.findElements(By.tagName("tr"));
	System.out.println("test loan getAcctTrans 2");
	for(WebElement row: rowEles){
		System.out.println("test loan getAcctTrans 3");
		String transDate = "";
		String amount = "";
		
		String rowText = row.getText();
		if(rowText.contains("EMI Date") || rowText.contains("Installment Details")){
			System.out.println("test loan getAcctTrans 4");
			continue;
		}
		
		List<WebElement> colEles = row.findElements(By.tagName("td"));
		int count = 0;
		System.out.println("test loan getAcctTrans 5");
		for(WebElement col: colEles){
			System.out.println("test loan getAcctTrans 6");
			String value = col.getText();
			if(count == 1){
				transDate = value;
			}
			else if(count == 2){
				amount = value;
			}
			count++;
		}
		System.out.println("test loan getAcctTrans 7");
		LoanTransaction lt = new LoanTransaction();
		
		lt.setTransDate(transDate);
		lt.setDescription("EMI Installment");
		lt.setAmount(amount);
		lt.setTransactionType(LoanTransaction.TRANSACTION_TYPE_CREDIT);
		la.addTransaction(lt);
		System.out.println("test loan getAcctTrans 8");
		
		
	}
	
}

private void selectAccountForTransactions(WebDriver driver, LoanAccount la) {
	// TODO Auto-generated method stub
	
	System.out.println();
	System.out.println("test loan selAcctTrans 1");
	driver.switchTo().defaultContent();
	driver.switchTo().frame("main_part");
	
	WebElement acctTypeEle = driver.findElement(By.cssSelector("select[name='selLoan']"));
	Select acctType = new  Select(acctTypeEle);
	System.out.println("test loan selAcctTrans 2");
	acctType.selectByValue(la.getAccountNumber());
	sleep(1);
	
	System.out.println("test loan selAcctTrans 3");
	
	WebElement viewEle = driver.findElement(By.xpath("//img[contains(@src, 'default/gif/view.gif')]//parent::a"));
	System.out.println("test loan selAcctTrans 4");
	viewEle.click();
	System.out.println("test loan selAcctTrans 5");
	sleep(5);
	System.out.println("test loan selAcctTrans 6");
}

private void navigateToLoanTransactions(WebDriver driver) {
	// TODO Auto-generated method stub
	System.out.println();
	System.out.println("test loan navAcctTrans 1");
	navigateToLoanAccounts(driver);
	
	driver.switchTo().defaultContent();
	driver.switchTo().frame("left_menu");
	
	WebElement toggle = driver.findElement(By.xpath("//strong[contains(text(), 'Enquire')]//parent::a"));
	System.out.println("test loan navAcctTrans 2");
	toggle.click();
	System.out.println("test loan navAcctTrans 3");
	WebElement enquiryEle = driver.findElement(By.id("LTH_nohref"));
	
	enquiryEle.click();
	System.out.println("test loan navAcctTrans 4");
	sleep(2);
	System.out.println("test loan navAcctTrans 5");
}

}
