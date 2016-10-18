package com.pisight.pimoney1.beans;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HDFCCard extends HDFCBase {
	
public void execute(WebDriver driver, List<? extends Container> accountList){
	System.out.println();
	System.out.println("test card execute 1");
		for(Container account: accountList){
			System.out.println("test card execute 2");
			if(account instanceof CardAccount){
				System.out.println("test card execute 3");
				CardAccount ca = (CardAccount)account;
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
				boolean exists = driver.findElements( By.cssSelector("select[name='selCard']") ).size() != 0;
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				if(!exists){
					navigateToCardTransactions(driver);
					System.out.println("test card execute 4");
				}
				selectAccountForTransactions(driver, ca);
				System.out.println("test card execute 5");
				getAccountTransactions(driver, ca);
				System.out.println("test card execute 6");
			}
		}
	}

private void getAccountTransactions(WebDriver driver, CardAccount ca) {
	// TODO Auto-generated method stub
	
	System.out.println();
	System.out.println("test card getAcctTrans 1");
	driver.switchTo().defaultContent();
	driver.switchTo().frame("main_part");
	
	WebElement tableEle = driver.findElement(By.cssSelector("table[class='datatable borTop']"));
	
	List<WebElement> rowEles = tableEle.findElements(By.tagName("tr"));
	System.out.println("test card getAcctTrans 2");
	for(WebElement row: rowEles){
		System.out.println("test card getAcctTrans 3");
		String transDate = "";
		String transDesc = "";
		String amount = "";
		String transType = "";
		
		String rowText = row.getText();
		if(rowText.contains("Transaction Date")){
			System.out.println("test card getAcctTrans 4");
			continue;
		}
		
		List<WebElement> colEles = row.findElements(By.tagName("td"));
		int count = 0;
		for(WebElement col: colEles){
			System.out.println("test card getAcctTrans 5");
			String value = col.getText();
			if(count == 0){
				transDate = value;
			}
			else if(count == 1){
				transDesc = value;
			}
			else if(count == 2){
				amount = value;
			}
			else if(count == 3){
				transType = value;
			}
			count++;
		}
		
		CardTransaction ct = new CardTransaction();
		
		ct.setTransDate(transDate);
		ct.setDescription(transDesc);
		ct.setAmount(amount);
		if(transType.contains("Dr")){
			ct.setTransactionType(CardTransaction.TRANSACTION_TYPE_DEBIT);
		}
		else{
			ct.setTransactionType(CardTransaction.TRANSACTION_TYPE_CREDIT);
		}
		ca.addTransaction(ct);
		
		System.out.println("test card getAcctTrans 6");
		
	}
	
}

private void selectAccountForTransactions(WebDriver driver, CardAccount ca) {
	// TODO Auto-generated method stub
	System.out.println();
	System.out.println("test card selAcctTrans 1");
	driver.switchTo().defaultContent();
	driver.switchTo().frame("main_part");
	
	WebElement acctTypeEle = driver.findElement(By.cssSelector("select[name='selCard']"));
	Select acctType = new  Select(acctTypeEle);
	
	acctType.selectByValue(ca.getAccountNumber());
	sleep(1);
	System.out.println("test card selAcctTrans 2");
	
	
	WebElement viewEle = driver.findElement(By.xpath("//img[contains(@src, 'default/gif/view.gif')]//parent::a"));
	System.out.println("test card selAcctTrans 3");
	viewEle.click();
	System.out.println("test card selAcctTrans 4");
	sleep(5);
	System.out.println("test card selAcctTrans 5");
}

private void navigateToCardTransactions(WebDriver driver) {
	// TODO Auto-generated method stub
	System.out.println();
	System.out.println("test card navAcctTrans 1");
	navigateToCardAcounts(driver);
	System.out.println("test card navAcctTrans 2");
	driver.switchTo().defaultContent();
	driver.switchTo().frame("left_menu");
	
	WebElement toggle = driver.findElement(By.xpath("//strong[contains(text(), 'Enquire')]//parent::a"));
	System.out.println("test card navAcctTrans 3");
	toggle.click();
	System.out.println("test card navAcctTrans 4");
	WebElement enquiryEle = driver.findElement(By.id("UNB_nohref"));
	
	enquiryEle.click();
	System.out.println("test card navAcctTrans 5");
	sleep(2);
	System.out.println("test card navAcctTrans 6");
}

}
