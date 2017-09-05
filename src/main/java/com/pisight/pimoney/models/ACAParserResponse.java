package com.pisight.pimoney.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pisight.pimoney.constants.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ACAParserResponse {

	private boolean isEncrypted = false;

	private boolean isPswdCorrect = false;

	private String xmlTrans = null;

	private String xmlAsset = null;

	private String xmlAssetPSX = null;

	private String xmlAssetSCX = null;

	private String portfolioNumber = null;

	private String statementDate = null;

	private String statementType = Constants.STATEMENT_TYPE_DEFAULT;

	private String statementCurrency = null;

	private int errorCode  = 0;

	private String status = null;

	private String message = null;

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

}
