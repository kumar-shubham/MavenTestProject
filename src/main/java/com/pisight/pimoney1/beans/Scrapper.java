package com.pisight.pimoney1.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Scrapper {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Path p = Paths.get(System.getProperty("user.home"), ".m2", "chromedriver.exe");
		
		System.setProperty("webdriver.chrome.driver",p.toString() );

		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		HDFCBank bank = new HDFCBank();
		HDFCCard card = new HDFCCard();
		HDFCLoan loan = new HDFCLoan();
		int code = 0;
		try{
		code = bank.login(driver);
		List<? extends Container> accountList = bank.getAccounts(driver);
		
		System.out.println("test main 1");
		bank.execute(driver, accountList);
		System.out.println("test main 2");
		card.execute(driver, accountList);
		System.out.println("test main 3");
		loan.execute(driver, accountList);
		System.out.println("test main 4");
		printAccountDetails(accountList);
		System.out.println("test main 5");
		bank.logout(driver);
		}
		catch(Exception e){
			if(code == 1){
				bank.logout(driver);
			}
			System.out.println("Exception raised:: " + e);
			bank.sleep(4);
			driver.close();
			throw e;
		}
		try {
			System.out.println("waiting");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.close();
		System.out.println("Chrome");
	}

	private static void printAccountDetails(List<? extends Container> accountList) {
		
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		for(Container account: accountList){
			System.out.println();
			System.out.println("Account Type ::: " + account.getTag());
			if(account instanceof BankAccount){
				BankAccount ba = (BankAccount) account;
				System.out.println("Account Number  :: " + ba.getAccountNumber());
				System.out.println("Account Bramch  :: " + ba.getBranch());
				System.out.println("Account Holder  :: " + ba.getAccountHolder());
				System.out.println("Account Balance :: " + ba.getAccountBalance());
				
				List<BankTransaction> transactions = ba.getTransactions();
				
				for(BankTransaction bt: transactions){
					
					System.out.println();
					System.out.println("  >> Transaction Date :: " + bt.getTransDate());
					System.out.println("  >> Post Date        :: " + bt.getPostDate());
					System.out.println("  >> Description      :: " + bt.getDescription());
					System.out.println("  >> Amount           :: " + bt.getAmount());
					System.out.println("  >> Running Balance  :: " + bt.getRunningBalance());
					System.out.println("  >> Transaction Type :: " + bt.getTransactionType());
				}
			}
			else if(account instanceof CardAccount){
				CardAccount ca = (CardAccount)account;
				System.out.println("card Number        :: " + ca.getAccountNumber());
				System.out.println("Account Name       :: " + ca.getAccountName());
				System.out.println("Account Holder     :: " + ca.getAccountHolder());
				System.out.println("Available Credit   :: " + ca.getAvailableCredit());
				System.out.println("Last Stmnt Balance :: " + ca.getLastStatementBalance());
				
				List<CardTransaction> transactions = ca.getTransactions();
				
				for(CardTransaction ct: transactions){
					System.out.println();
					System.out.println("  >> Transaction Date :: " + ct.getTransDate());
					System.out.println("  >> Description      :: " + ct.getDescription());
					System.out.println("  >> Amount           :: " + ct.getAmount());
					System.out.println("  >> Transaction Type :: " + ct.getTransactionType());
				}
			}
			else if(account instanceof LoanAccount){
				LoanAccount la = (LoanAccount)account;
				
				System.out.println("Account Number :: " + la.getAccountNumber());
				System.out.println("Loan Amount    :: " + la.getPrincipalAmount());
				System.out.println("Tenure Months  :: " + la.getTenureMonths());
				System.out.println("Loan EMI       :: " + la.getLoanEMI());
				System.out.println("Loan Type      :: " + la.getLoanType());
				
				List<LoanTransaction> transactions = la.getTransactions();
				for(LoanTransaction lt: transactions){
					System.out.println();
					System.out.println("  >> Transaction Date :: " + lt.getTransDate());
					System.out.println("  >> Description      :: " + lt.getDescription());
					System.out.println("  >> Amount           :: " + lt.getAmount());
					System.out.println("  >> Transaction Type :: " + lt.getTransactionType());
				}
			}
		}
		
		try {
			mapper.writeValue(new File("h:\\workspace\\file.json"), accountList);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
