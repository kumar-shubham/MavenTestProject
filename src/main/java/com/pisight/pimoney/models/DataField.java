package com.pisight.pimoney.models;

import java.io.Serializable;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class DataField implements Serializable, Cloneable{

	private static final long serialVersionUID = -4687565462717602446L;

	private String name;

	private String value;

	private String type = Constants.FIELD_TYPE_STRING;
	
	public DataField() {
		
	}

	public DataField(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public DataField(String name, String value, String type) throws Exception {
		checkAndSetType(type, value);
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 * @throws Exception 
	 */
	public void setValue(String value) throws Exception {
		if (type != null) {
			checkAndSetType(this.type, value);
		}
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 * @throws Exception 
	 */
	public void setType(String type) throws Exception {
		if (value == null) {
			this.type = type;
		}
		else {
			checkAndSetType(type, this.value);
		}
	}

	private void checkAndSetType(String type, String value) throws Exception {
		try {
			if (Constants.FIELD_TYPE_BOOLEAN.equals(type)) {
				Boolean.valueOf(value);
			} else if (Constants.FIELD_TYPE_DATE.equals(type)) {
				AccountUtil.convertToDefaultDateFormat(value);
			} else if (Constants.FIELD_TYPE_INTEGER.equals(type)) {
				Integer.valueOf(value);
			} else if (Constants.FIELD_TYPE_DOUBLE.equals(type)) {
				Double.valueOf(value);
			} else {
				type = Constants.FIELD_TYPE_STRING;
			}
			this.type = type;

		} catch (Exception e) {
			throw new Exception("Field type is not correct for the value");
		}
	}
	
	public String toString() {
		return "{name : " + this.name + ", value : " + this.value + ", type : " + this.type + "}";
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  


}
