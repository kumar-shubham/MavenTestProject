package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.List;

public class XMLFormat {
	
	public List<InvestmentAccount> investmentAccount = new ArrayList<InvestmentAccount>();

	/**
	 * @return the accounts
	 */
	public List<InvestmentAccount> getinvestmentAccount() {
		return investmentAccount;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setinvestmentAccount(List<InvestmentAccount> accounts) {
		this.investmentAccount = accounts;
	}
	
	
	public void addAccount(InvestmentAccount account){
		investmentAccount.add(account);
	}
	

}
