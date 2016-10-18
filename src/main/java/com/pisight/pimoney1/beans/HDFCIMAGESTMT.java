package com.pisight.pimoney1.beans;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HDFCIMAGESTMT {

	public void tempHDFCCARDIMAGE(WebDriver driver){
		WebElement page = driver.findElement(By.id("PDF_TO_HTML"));

		CardAccount ca = new CardAccount();

		WebElement date1 = null;
		WebElement date2 = null;
		WebElement accNumEle = null;
		try{
			date1 = page.findElement(By.xpath("//td[contains(text(), 'Cardmember')]/../following-sibling::tr[1]"));
			date2 = page.findElement(By.xpath("//td[contains(text(), 'Cardmember')]/../following-sibling::tr[2]"));
			accNumEle = page.findElement(By.xpath("//td[contains(text(), 'Cardmember')]/../following-sibling::tr[3]"));
		}catch(NoSuchElementException e){
			System.out.println("Cardmember not found");
			date1 = page.findElement(By.xpath("//td[contains(text(), 'Due Date')]/../following-sibling::tr[2]"));
			date2 = page.findElement(By.xpath("//td[contains(text(), 'Due Date')]/../following-sibling::tr[3]"));
			accNumEle = page.findElement(By.xpath("//td[contains(text(), 'Due Date')]/../following-sibling::tr[4]"));
		}

		String stmtDate = date1.getText().replace(" ", "");
		String dueDate = date2.getText().replace(" ", "");
		String accNum = accNumEle.getText().trim();

		stmtDate = formatDate(stmtDate);
		dueDate = formatDate(dueDate);

		WebElement amount1 = null;
		WebElement amount2 = null;
		try{
			amount1 = page.findElement(By.xpath("//td[contains(text(), 'Available Cash Limit')]/../following-sibling::tr[1]"));
			amount2 = page.findElement(By.xpath("//td[contains(text(), 'Available Cash Limit')]/../following-sibling::tr[2]"));
		}
		catch(NoSuchElementException e){
			System.out.println("Available Cash Limit not found");
			amount1 = page.findElement(By.xpath("//td[contains(text(), 'able Credit Limit')]/../following-sibling::tr[2]"));
			amount2 = page.findElement(By.xpath("//td[contains(text(), 'able Credit Limit')]/../following-sibling::tr[3]"));
		}

		String creditLimit = amount1.getText().replace(" ", "");
		String availableLimit = amount2.getText().replace(" ", "");

		WebElement amount3 = null;
		WebElement amount4 = null;
		try{
			amount3 = page.findElement(By.xpath("//td[contains(text(), 'Total Dues')]/../following-sibling::tr[5]"));
		}
		catch(NoSuchElementException e){
			System.out.println("Total Dues not found");
			amount3 = page.findElement(By.xpath("//td[contains(text(), 'Purchase and Debits')]/../following-sibling::tr[5]"));
		}
		try{
			amount4 = page.findElement(By.xpath("//td[contains(text(), 'Minimum Amount Due')]/../following-sibling::tr[5]"));
		}
		catch(NoSuchElementException e){
			System.out.println("Minimum Amount Due");
			amount4 = page.findElement(By.xpath("//td[contains(text(), 'Current Dues')]/../following-sibling::tr[5]"));
		}

		String amountDue = amount3.getText().replace(" ", "");
		String minPayment = amount4.getText().replace(" ", "");

		amountDue = formatAmount(amountDue);
		minPayment = formatAmount(minPayment);


		ca.setAccountNumber(accNum);
		ca.setBillDate(stmtDate);
		ca.setDueDate(dueDate);
		ca.setTotalLimit(creditLimit);
		ca.setAvailableCredit(availableLimit);
		ca.setAmountDue(amountDue);
		ca.setMinAmountDue(minPayment);

		String dateRegex = " ?(\\d{2} ?. ?\\d{2} ?. ?\\d{2}) ?";
		String amountRegex = "(.* )?((\\d*,)*\\d+(\\.)\\d+( Cr)?)";

		Pattern pDate = Pattern.compile(dateRegex);
		Pattern pAmount = Pattern.compile(amountRegex);
		Matcher m = null;

		List<WebElement> transEle = page.findElements(By.xpath("//td[contains(text(), 'Amount')]/../following-sibling::tr"));

		if(transEle.size() == 0 || !transEle.get(0).getText().contains("Merchant")){
			transEle = page.findElements(By.xpath("//td[contains(text(), 'Merchant')]/../following-sibling::tr"));
		}

		CardTransaction lastTrans = null;
		for(WebElement ele: transEle){

			String text = ele.getText().trim();

			m = pDate.matcher(text);

			if(m.matches()){
				CardTransaction ct = new CardTransaction();
				String transDate = m.group(1);
				transDate = formatDate(transDate);

				ct.setTransDate(transDate);
				ct.setAccountNumber(accNum);
				ct.setCurrency("INR");
				lastTrans = ct;
			}
			else{

				if(lastTrans != null){
					m = pAmount.matcher(text);
					if(m.matches()){
						String amount = m.group(2);
						String transType = null;
						String desc = lastTrans.getDescription();
						if(amount.toLowerCase().contains("cr")){
							transType = CardTransaction.TRANSACTION_TYPE_CREDIT;
							if(desc == null || desc.equals("")){
								lastTrans.setDescription("credit");
							}
						}
						else{
							transType = CardTransaction.TRANSACTION_TYPE_DEBIT;
							if(desc == null || desc.equals("")){
								lastTrans.setDescription("debit");
							}
						}
						amount = amount.replace("Cr", "");
						amount = amount.replace("CR", "");
						lastTrans.setAmount(amount);
						lastTrans.setTransactionType(transType);
						ca.addTransaction(lastTrans);
						lastTrans = null;
					}
					else{
						String desc = lastTrans.getDescription() + " " + text;
						lastTrans.setDescription(desc.trim());
					}
				}
			}

			if(text.toLowerCase().contains("reward points") || text.toLowerCase().contains("opening balance")){
				break;
			}
		}
	}
	
	private static String formatDate(String date){

		date = date.replace("/", "");
		date = date.replace(" ", "");

		if(date.length() == 6){

			date = date.substring(0, 2) + "-" + date.substring(2,4) + "-20" + date.substring(4); 
		}
		else if(date.length() == 8){
			date = date.substring(0, 2) + "-" + date.substring(2,4) + "-" + date.substring(4); 
		}

		return date;
	}

	private static String formatAmount(String amount){

		if(!amount.contains(".")){
			amount = amount.substring(0,amount.length()-2) + "." + amount.substring(amount.length()-2);
		}
		return amount;
	}
}
