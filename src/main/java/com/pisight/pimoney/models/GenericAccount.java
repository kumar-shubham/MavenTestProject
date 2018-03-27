package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class GenericAccount  extends Container implements Serializable{

	private static final long serialVersionUID = -6570665576797972165L;


	public GenericAccount(){
		setTag(Constants.TAG_GENERIC);
	}

	public GenericAccount(HashMap<String, String> properties){
		setTag(Constants.TAG_GENERIC);
		setProperties(properties);
		String bankId = "" + properties.get(Constants.USER_ID) + properties.get(Constants.INSTITUTION_CODE);
		setBankId("manual-" + bankId.hashCode());
	}
	
	private String accountNumber = "";
	
	private String billDate = "";
	
	private String sheetName = "";
	
	private HashMap<String, DataField> accountDataFields = new HashMap<String, DataField>();
	
	private List<GenericChild> childData = new ArrayList<GenericChild>();
	

	@Override
	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public int getUsability() {
		return 0;
	}

	@Override
	public void setUsability(int usability) {
	}

	/**
	 * @return the billDate
	 */
	public String getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	
	public void clearDataMap() {
		accountDataFields.clear();
	}
	
	public void clearChildData() {
		childData.clear();
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setHash() {
		String hash = AccountUtil.generateHash(this, getProperties());
		setAccountHash(hash);
	}

	/**
	 * @return the accountDataFields
	 */
	public HashMap<String, DataField> getAccountDataFields() {
		return accountDataFields;
	}

	/**
	 * @param accountDataFields the accountDataFields to set
	 */
	public void setAccountDataFields(HashMap<String, DataField> accountDataFields) {
		if(accountDataFields != null) {
			this.accountDataFields = accountDataFields;
		}
	}
	
	public void addAccountDataField(String key, DataField dataField) {
		this.accountDataFields.put(key, dataField);
	}
	
	public void addAccountDataField(String key, String value) {
		this.accountDataFields.put(key, new DataField(key, value));
	}
	
	public void addAccountDataField(String key, String value, String type) throws Exception {
		this.accountDataFields.put(key, new DataField(key, value, type));
	}

	/**
	 * @return the childData
	 */
	public List<GenericChild> getChildData() {
		return childData;
	}

	/**
	 * @param childData the childData to set
	 */
	public void setChildData(List<GenericChild> childData) {
		if(childData != null) {
			this.childData = childData;
		}
	}
	
	public void addChildData(GenericChild childData) {
		this.childData.add(childData);
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
