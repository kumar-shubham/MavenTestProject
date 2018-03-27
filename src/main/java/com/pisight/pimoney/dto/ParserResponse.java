package com.pisight.pimoney.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.models.BankAccount;
import com.pisight.pimoney.models.CardAccount;
import com.pisight.pimoney.models.Container;
import com.pisight.pimoney.models.FixedDepositAccount;
import com.pisight.pimoney.models.GenericAccount;
import com.pisight.pimoney.models.InvestmentAccount;
import com.pisight.pimoney.models.LoanAccount;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ParserResponse implements Cloneable, Serializable{

	private static final long serialVersionUID = 2056145228881096246L;

	public ParserResponse(){
	}

	public ParserResponse(HashMap<String, String> properties){
		this.properties = properties;
	}

	@SuppressWarnings("unused")
	private HashMap<String, String> properties = new HashMap<String, String>();

	private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();

	private List<CardAccount> cardAccounts = new ArrayList<CardAccount>();

	private List<LoanAccount> loanAccounts = new ArrayList<LoanAccount>();

	private List<FixedDepositAccount> fdAccounts = new ArrayList<FixedDepositAccount>();

	private List<InvestmentAccount> investmentAccounts = new ArrayList<InvestmentAccount>();
	
	private List<GenericAccount> genericAccounts = new ArrayList<GenericAccount>();
	
	private List<Object> scxObjects = new ArrayList<Object>();

	private boolean isEncrypted = false;

	private boolean isPswdCorrect = false;

	private String xmlTrans = null;

	private String xmlAsset = null;

	private String xmlAssetPSX = null;

	private String xmlAssetSCX = null;
	
	private String xlsxByteTrans = null;
	
	private String xlsxByteAssets = null;

	private String portfolioNumber = null;

	private String statementDate = null;

	private String statementType = Constants.STATEMENT_TYPE_DEFAULT;
	
	private String statementCurrency = null;
	
	private String institutionName = null;
	
	private String optionalId = null;
	
	private int errorCode  = 0;
	
	private String status = null;
	
	private String message = null;
	
	private String objectByte = null;
	
	private boolean fromObject = false;

	/**
	 * @return the statementType
	 */
	public String getStatementType() {
		return statementType;
	}

	/**
	 * @param statementType the statementType to set
	 */
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}

	/**
	 * @return the xmlTrans
	 */
	public String getXmlTrans() {
		return xmlTrans;
	}

	/**
	 * @param xmlTrans the xmlTrans to set
	 */
	public void setXmlTrans(String xmlTrans) {
		this.xmlTrans = xmlTrans;
	}

	/**
	 * @return the xmlAsset
	 */
	public String getXmlAssetPSX() {
		return xmlAssetPSX;
	}

	/**
	 * @param xmlAssetSCX the xmlAssetSCX to set
	 */
	public void setXmlAssetSCX(String xmlAssetSCX) {
		this.xmlAssetSCX = xmlAssetSCX;
	}

	/**
	 * @return the xmlAssetSCX
	 */
	public String getXmlAssetSCX() {
		return xmlAssetSCX;
	}

	/**
	 * @param xmlAsset the xmlAsset to set
	 */
	public void setXmlAssetPSX(String xmlAssetPSX) {
		this.xmlAssetPSX = xmlAssetPSX;
	}

	/**
	 * @return the xmlAsset
	 */
	public String getXmlAsset() {
		return xmlAsset;
	}

	/**
	 * @param xmlAsset the xmlAsset to set
	 */
	public void setXmlAsset(String xmlAsset) {
		this.xmlAsset = xmlAsset;
	}

	/**
	 * @return the xlsxByteTrans
	 */
	public String getXlsxByteTrans() {
		return xlsxByteTrans;
	}

	/**
	 * @param xlsxByteTrans the xlsxByteTrans to set
	 */
	public void setXlsxByteTrans(String xlsxByteTrans) {
		this.xlsxByteTrans = xlsxByteTrans;
	}

	/**
	 * @return the xlsxByteAssets
	 */
	public String getXlsxByteAssets() {
		return xlsxByteAssets;
	}

	/**
	 * @param xlsxByteAssets the xlsxByteAssets to set
	 */
	public void setXlsxByteAssets(String xlsxByteAssets) {
		this.xlsxByteAssets = xlsxByteAssets;
	}

	/**
	 * @return the bankAccounts
	 */
	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	/**
	 * @param bankAccounts the bankAccounts to set
	 */
	public void addBankAccount(BankAccount bankAccount) {
		bankAccounts.add(bankAccount);
	}

	public void addBankAccount(BankAccount bankAccount, boolean withoutHash){
		if(withoutHash){
			bankAccounts.add(bankAccount);
		}
		else{
			addBankAccount(bankAccount);
		}
	}

	/**
	 * @return the cardAccounts
	 */
	public List<CardAccount> getCardAccounts() {
		return cardAccounts;
	}

	/**
	 * @param cardAccounts the cardAccounts to set
	 */
	public void addCardAccount(CardAccount cardAccount) {
		cardAccounts.add(cardAccount);
	}

	public void addCardAccount(CardAccount cardAccount, boolean withoutHash){
		if(withoutHash){
			cardAccounts.add(cardAccount);
		}
		else{
			addCardAccount(cardAccount);
		}
	}

	/**
	 * @return the loanAccounts
	 */
	public List<LoanAccount> getLoanAccounts() {
		return loanAccounts;
	}

	/**
	 * @param loanAccounts the loanAccounts to set
	 */
	public void addLoanAccount(LoanAccount loanAccount) {
		loanAccounts.add(loanAccount);
	}

	public void addLoanAccount(LoanAccount loanAccount, boolean withoutHash){
		if(withoutHash){
			loanAccounts.add(loanAccount);
		}
		else{
			addLoanAccount(loanAccount);
		}
	}

	/**
	 * @return the fdAccounts
	 */
	public List<FixedDepositAccount> getFdAccounts() {
		return fdAccounts;
	}

	/**
	 * @param fdAccounts the fdAccounts to set
	 */
	public void addFdAccount(FixedDepositAccount fdAccount) {
		fdAccounts.add(fdAccount);
	}

	public void addFdAccount(FixedDepositAccount fdAccount, boolean withoutHash){
		if(withoutHash){
			fdAccounts.add(fdAccount);
		}
		else{
			addFdAccount(fdAccount);
		}
	}

	/**
	 * @return the investmentAccounts
	 */
	public List<InvestmentAccount> getInvestmentAccounts() {
		return investmentAccounts;
	}

	/**
	 * @param investmentAccounts the investmentAccounts to set
	 */
	public void addInvestmentAccount(InvestmentAccount investmentAccount) {
		investmentAccounts.add(investmentAccount);
	}

	public void addInvestmentAccount(InvestmentAccount investmentAccount, boolean withoutHash){
		if(withoutHash){
			investmentAccounts.add(investmentAccount);
		}
		else{
			addInvestmentAccount(investmentAccount);
		}
	}

	/**
	 * @return the isEncrypted
	 */
	public boolean isEncrypted() {
		return isEncrypted;
	}

	/**
	 * @param isEncrypted the isEncrypted to set
	 */
	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	/**
	 * @return the isPswdCorrect
	 */
	public boolean isPswdCorrect() {
		return isPswdCorrect;
	}

	/**
	 * @param isPswdCorrect the isPswdCorrect to set
	 */
	public void setPswdCorrect(boolean isPswdCorrect) {
		this.isPswdCorrect = isPswdCorrect;
	}

	/**
	 * @return the portfolioNumber
	 */
	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	/**
	 * @param portfolioNumber the portfolioNumber to set
	 */
	public void setPortfolioNumber(String portfolio) {
		this.portfolioNumber = portfolio;
	}

	/**
	 * @return the statementDate
	 */
	public String getStatementDate() {
		return statementDate;
	}

	/**
	 * @param statementDate the statementDate to set
	 */
	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}

	/**
	 * @return the statementCurrency
	 */
	public String getStatementCurrency() {
		return statementCurrency;
	}

	/**
	 * @param statementCurrency the statementCurrency to set
	 */
	public void setStatementCurrency(String statementCurrency) {
		this.statementCurrency = statementCurrency;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}

	/**
	 * @return the institutionName
	 */
	public String getInstitutionName() {
		return institutionName;
	}

	/**
	 * @param institutionName the institutionName to set
	 */
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	/**
	 * @return the genericAccounts
	 */
	public List<GenericAccount> getGenericContainers() {
		return genericAccounts;
	}

	/**
	 * @param genericAccounts the genericAccounts to set
	 */
	public void addGenericContainer(GenericAccount genericAccount) {
		this.genericAccounts.add(genericAccount);
	}

	/**
	 * @return the optionalId
	 */
	public String getOptionalId() {
		return optionalId;
	}

	/**
	 * @param optionalId the optionalId to set
	 */
	public void setOptionalId(String optionalId) {
		this.optionalId = optionalId;
	}
	
	public List<Container> getAllAccounts() {
		List<Container> allAccounts = new ArrayList<Container>();

		allAccounts.addAll(this.getBankAccounts());
		allAccounts.addAll(this.getCardAccounts());
		allAccounts.addAll(this.getLoanAccounts());
		allAccounts.addAll(this.getInvestmentAccounts());
		allAccounts.addAll(this.getGenericContainers());

		return allAccounts;
	}

	/**
	 * @return the scxObjects
	 */
	public List<Object> getScxObjects() {
		return scxObjects;
	}

	/**
	 * @param scxObjects the scxObjects to set
	 */
	public void setScxObjects(List<Object> scxObjects) {
		this.scxObjects = scxObjects;
	}

	/**
	 * @param bankAccounts the bankAccounts to set
	 */
	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	/**
	 * @param cardAccounts the cardAccounts to set
	 */
	public void setCardAccounts(List<CardAccount> cardAccounts) {
		this.cardAccounts = cardAccounts;
	}

	/**
	 * @param loanAccounts the loanAccounts to set
	 */
	public void setLoanAccounts(List<LoanAccount> loanAccounts) {
		this.loanAccounts = loanAccounts;
	}

	/**
	 * @param fdAccounts the fdAccounts to set
	 */
	public void setFdAccounts(List<FixedDepositAccount> fdAccounts) {
		this.fdAccounts = fdAccounts;
	}

	/**
	 * @param investmentAccounts the investmentAccounts to set
	 */
	public void setInvestmentAccounts(List<InvestmentAccount> investmentAccounts) {
		this.investmentAccounts = investmentAccounts;
	}

	/**
	 * @param genericAccounts the genericAccounts to set
	 */
	public void setGenericContainers(List<GenericAccount> genericAccounts) {
		this.genericAccounts = genericAccounts;
	}

	/**
	 * @return the objectByte
	 */
	public String getObjectByte() {
		return objectByte;
	}

	/**
	 * @param objectByte the objectByte to set
	 */
	public void setObjectByte(String objectByte) {
		this.objectByte = objectByte;
	}

	/**
	 * @return the fromObject
	 */
	public boolean isFromObject() {
		return fromObject;
	}

	/**
	 * @param fromObject the fromObject to set
	 */
	public void setFromObject(boolean fromObject) {
		this.fromObject = fromObject;
	}
	
}
