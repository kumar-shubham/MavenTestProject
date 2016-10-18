package com.pisight.pimoney1.beans;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HDFCBank extends HDFCBase {
	
	public void execute(WebDriver driver, List<? extends Container> accountList){
		System.out.println("test bank execute 1");
		for(Container account: accountList){
			System.out.println("test bank execute 2");
			if(account instanceof BankAccount){
				System.out.println("test bank execute 3");
				BankAccount ba = (BankAccount)account;
				navigateToAccountTransactions(driver);
				System.out.println("test bank execute 4");
				selectAccountForTransactions(driver, ba);
				System.out.println("test bank execute 5");
				getAccountTransactions(driver, ba);
				System.out.println("test bank execute 6");
			}
		}
	}
	

	private void getAccountTransactions(WebDriver driver, BankAccount account){
		
		System.out.println();
		System.out.println("test bank getAccTrans 1");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		boolean isNextPresent = false;
		int pageCount = 1;
		do{
			if(isNextPresent){
				pageCount++;
				System.out.println("test bank getAccTrans 1_1");
				WebElement nextEle = driver.findElement(By.id("l_next"));
				nextEle.click();
				sleep(2);
			}
			WebElement tableEle = driver.findElement(By.xpath("//table[@class='datatable' and @id='" + pageCount + "']"));
			
			List<WebElement> rowEles = tableEle.findElements(By.tagName("tr"));
			System.out.println("test bank getAccTrans 2");
			
			for(WebElement row: rowEles){
				System.out.println("test bank getAccTrans 3");
				String transDate = "";
				String transDesc = "";
				String postDate = "";
				String debit = "";
				String credit = "";
				String runningBalance = "";
				
				String rowText = row.getText();
				if(rowText.contains("Date")){
					System.out.println("test bank getAccTrans 4");
					continue;
				}
				
				List<WebElement> colEles = row.findElements(By.tagName("td"));
				int count = 0;
				for(WebElement col: colEles){
					System.out.println("test bank getAccTrans 5");
					String value = col.getText();
					if(count == 0){
						transDate = value;
					}
					else if(count == 1){
						transDesc = value;
					}
					else if(count == 2){
						transDesc += " " + value;
					}
					else if(count == 3){
						postDate = value;
					}
					else if(count == 4){
						debit = value;
					}
					else if(count == 5){
						credit = value;
					}
					else if(count == 6){
						runningBalance = value;
					}
					count++;
				}
				
				BankTransaction bt = new BankTransaction();
				
				bt.setTransDate(transDate);
				bt.setPostDate(postDate);
				bt.setDescription(transDesc);
				if(debit.contains(".")){
					bt.setAmount(debit);
					bt.setTransactionType(BankTransaction.TRANSACTION_TYPE_DEBIT);
				}
				else if(credit.contains(".")){
					bt.setAmount(credit);
					bt.setTransactionType(BankTransaction.TRANSACTION_TYPE_CREDIT);
				}
				bt.setRunningBalance(runningBalance);
				account.addTransaction(bt);
				System.out.println("test bank getAccTrans 6");
			}
			
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			isNextPresent = driver.findElements( By.id("l_next") ).size() != 0;
			if(isNextPresent){
				System.out.println("test bank getAccTrans 7");
				isNextPresent = driver.findElement(By.id("l_next")).isDisplayed();
			}
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
		}while(isNextPresent);
			
			
	}
	
	private void navigateToAccountTransactions(WebDriver driver) {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.println("test bank navAccTrans 1");
		navigateToAccountSummryPage(driver);
		
		System.out.println("test bank navAccTrans 2");
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("left_menu");
		
		WebElement toggle = driver.findElement(By.id("enquiryatag"));
		
		toggle.click();
		System.out.println("test bank navAccTrans 3");
		WebElement enquiryEle = driver.findElement(By.id("SIN_nohref"));
		
		enquiryEle.click();
		System.out.println("test bank navAccTrans 4");
		sleep(2);
		System.out.println("test bank navAccTrans 5");
	}

	private void selectAccountForTransactions(WebDriver driver, BankAccount account) {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.println("test bank selAccTrans 1");
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main_part");
		
		WebElement acctTypeEle = driver.findElement(By.cssSelector("select[name='selAccttype']"));
		Select acctType = new  Select(acctTypeEle);
		
		acctType.selectByValue("SCA");
		sleep(1);
		System.out.println("test bank selAccTrans 2");
		WebElement acctNameEle = driver.findElement(By.cssSelector("select[name='selAcct']"));
		Select acctName = new Select(acctNameEle);
		
		acctName.selectByValue(account.getAccountNumber() +  "  ");
		System.out.println("test bank selAccTrans 3");
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("jQuery('#hideradio input')[0].click()");
		WebElement fromEle = driver.findElement(By.id("frmDatePicker"));
		WebElement toEle = driver.findElement(By.id("toDatePicker"));
		fromEle.sendKeys("01/08/2016");
		toEle.sendKeys("30/08/2016");
		
		System.out.println("test bank selAccTrans 4");
		WebElement numEle = driver.findElement(By.cssSelector("select[name='cmbNbrStmt']"));
		Select num = new Select(numEle);
		num.selectByValue("40");
		
		WebElement viewEle = driver.findElement(By.cssSelector("a[class='viewBtn']"));
		
		viewEle.click();
		System.out.println("test bank selAccTrans 5");
		sleep(5);
		System.out.println("test bank selAccTrans 6");
	}

}
