package com.pisight.pimoney.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.beans.ParserUtility;

public class HoldingAccount {
	
	public HoldingAccount(){
	}
	
	public HoldingAccount(HashMap<String, String> properties){
		this.properties = properties;
	}
	
	private HashMap<String, String> properties = new HashMap<String, String>();

	private String portfolioHoldingId;
	private String portfolioHoldingName;
	private String portfolioHoldingAcctCategory;
	private String portfolioHoldingAcctSubcategory;
	private String portfolioHoldingValuationCurrency;
	private String portfolioHoldingTotalCost;
	private String portfolioHoldingTotalCurrentValue;
	private String portfolioHoldingLastUpdatedDateTime;
	private String portfolioHoldingTotalProfit;
	private String portfolioHoldingTotalProfitPerc;
	private String portfolioHoldingRelationshipManager;
	
	private String holdingAccountHash;
		
	private List<HoldingAsset> portfolioHoldingsAssetList = new ArrayList<HoldingAsset>();
	private InvestmentPortfolio portfolio;
	/**
	 * @return the portfolioHoldingId
	 */
	public String getPortfolioHoldingId() {
		return portfolioHoldingId;
	}
	/**
	 * @param portfolioHoldingId the portfolioHoldingId to set
	 */
	public void setPortfolioHoldingId(String portfolioHoldingId) {
		this.portfolioHoldingId = portfolioHoldingId;
	}
	/**
	 * @return the portfolioHoldingName
	 */
	public String getPortfolioHoldingName() {
		return portfolioHoldingName;
	}
	/**
	 * @param portfolioHoldingName the portfolioHoldingName to set
	 */
	public void setPortfolioHoldingName(String portfolioHoldingName) {
		this.portfolioHoldingName = portfolioHoldingName;
	}
	/**
	 * @return the portfolioHoldingAcctCategory
	 */
	public String getPortfolioHoldingAcctCategory() {
		return portfolioHoldingAcctCategory;
	}
	/**
	 * @param portfolioHoldingAcctCategory the portfolioHoldingAcctCategory to set
	 */
	public void setPortfolioHoldingAcctCategory(String portfolioHoldingAcctCategory) {
		this.portfolioHoldingAcctCategory = portfolioHoldingAcctCategory;
	}
	/**
	 * @return the portfolioHoldingAcctSubcategory
	 */
	public String getPortfolioHoldingAcctSubcategory() {
		return portfolioHoldingAcctSubcategory;
	}
	/**
	 * @param portfolioHoldingAcctSubcategory the portfolioHoldingAcctSubcategory to set
	 */
	public void setPortfolioHoldingAcctSubcategory(String portfolioHoldingAcctSubcategory) {
		this.portfolioHoldingAcctSubcategory = portfolioHoldingAcctSubcategory;
	}
	/**
	 * @return the portfolioHoldingValuationCurrency
	 */
	public String getPortfolioHoldingValuationCurrency() {
		return portfolioHoldingValuationCurrency;
	}
	/**
	 * @param portfolioHoldingValuationCurrency the portfolioHoldingValuationCurrency to set
	 */
	public void setPortfolioHoldingValuationCurrency(String portfolioHoldingValuationCurrency) {
		this.portfolioHoldingValuationCurrency = portfolioHoldingValuationCurrency;
	}
	/**
	 * @return the portfolioHoldingTotalCost
	 */
	public String getPortfolioHoldingTotalCost() {
		return portfolioHoldingTotalCost;
	}
	/**
	 * @param portfolioHoldingTotalCost the portfolioHoldingTotalCost to set
	 */
	public void setPortfolioHoldingTotalCost(String portfolioHoldingTotalCost) {
		this.portfolioHoldingTotalCost = portfolioHoldingTotalCost;
	}
	/**
	 * @return the portfolioHoldingTotalCurrentValue
	 */
	public String getPortfolioHoldingTotalCurrentValue() {
		return portfolioHoldingTotalCurrentValue;
	}
	/**
	 * @param portfolioHoldingTotalCurrentValue the portfolioHoldingTotalCurrentValue to set
	 */
	public void setPortfolioHoldingTotalCurrentValue(String portfolioHoldingTotalCurrentValue) {
		this.portfolioHoldingTotalCurrentValue = portfolioHoldingTotalCurrentValue;
	}
	/**
	 * @return the portfolioHoldingLastUpdatedDateTime
	 */
	public String getPortfolioHoldingLastUpdatedDateTime() {
		return portfolioHoldingLastUpdatedDateTime;
	}
	/**
	 * @param portfolioHoldingLastUpdatedDateTime the portfolioHoldingLastUpdatedDateTime to set
	 */
	public void setPortfolioHoldingLastUpdatedDateTime(String portfolioHoldingLastUpdatedDateTime) {
		this.portfolioHoldingLastUpdatedDateTime = portfolioHoldingLastUpdatedDateTime;
	}
	/**
	 * @return the portfolioHoldingTotalProfit
	 */
	public String getPortfolioHoldingTotalProfit() {
		return portfolioHoldingTotalProfit;
	}
	/**
	 * @param portfolioHoldingTotalProfit the portfolioHoldingTotalProfit to set
	 */
	public void setPortfolioHoldingTotalProfit(String portfolioHoldingTotalProfit) {
		this.portfolioHoldingTotalProfit = portfolioHoldingTotalProfit;
	}
	/**
	 * @return the portfolioHoldingTotalProfitPerc
	 */
	public String getPortfolioHoldingTotalProfitPerc() {
		return portfolioHoldingTotalProfitPerc;
	}
	/**
	 * @param portfolioHoldingTotalProfitPerc the portfolioHoldingTotalProfitPerc to set
	 */
	public void setPortfolioHoldingTotalProfitPerc(String portfolioHoldingTotalProfitPerc) {
		this.portfolioHoldingTotalProfitPerc = portfolioHoldingTotalProfitPerc;
	}
	/**
	 * @return the portfolioHoldingRelationshipManager
	 */
	public String getPortfolioHoldingRelationshipManager() {
		return portfolioHoldingRelationshipManager;
	}
	/**
	 * @param portfolioHoldingRelationshipManager the portfolioHoldingRelationshipManager to set
	 */
	public void setPortfolioHoldingRelationshipManager(String portfolioHoldingRelationshipManager) {
		this.portfolioHoldingRelationshipManager = portfolioHoldingRelationshipManager;
	}
	/**
	 * @return the portfolioHoldingsAssetList
	 */
	public List<HoldingAsset> getPortfolioHoldingsAssetList() {
		return portfolioHoldingsAssetList;
	}
	/**
	 * @param portfolioHoldingsAssetList the portfolioHoldingsAssetList to set
	 */
	public void addPortfolioHoldingsAsset(HoldingAsset portfolioHoldingsAsset) {
		String hash = ParserUtility.generateHash(portfolioHoldingsAsset, properties);
		portfolioHoldingsAsset.setHoldingAssetHash(hash);
		portfolioHoldingsAssetList.add(portfolioHoldingsAsset);
	}
	
	public void addPortfolioHoldingsAsset(HoldingAsset portfolioHoldingsAsset, boolean withoutHash){
		if(withoutHash){
			portfolioHoldingsAssetList.add(portfolioHoldingsAsset);
		}
		else{
			addPortfolioHoldingsAsset(portfolioHoldingsAsset);
		}
	}
	/**
	 * @return the portfolio
	 */
	public InvestmentPortfolio getPortfolio() {
		return portfolio;
	}
	/**
	 * @param portfolio the portfolio to set
	 */
	public void setPortfolio(InvestmentPortfolio portfolio) {
		this.portfolio = portfolio;
	}

	/**
	 * @return the holdingAccountHash
	 */
	public String getHoldingAccountHash() {
		return holdingAccountHash;
	}

	/**
	 * @param holdingAccountHash the holdingAccountHash to set
	 */
	public void setHoldingAccountHash(String holdingAccountHash) {
		this.holdingAccountHash = holdingAccountHash;
	}
	
	
	

}
