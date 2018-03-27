package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.HashMap;

public class GenericChild implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -7363528044127375282L;
	
	private HashMap<String, DataField> childDataFields = new HashMap<String, DataField>();
	
	/**
	 * @return the childDataFields
	 */
	public HashMap<String, DataField> getChildDataFields() {
		return childDataFields;
	}

	/**
	 * @param childDataFields the childDataFields to set
	 */
	public void setChildDataFields(HashMap<String, DataField> childDataFields) {
		if(childDataFields != null) {
			this.childDataFields = childDataFields;
		}
	}
	
	public void addChildDataField(String key, DataField dataField) {
		this.childDataFields.put(key, dataField);
	}
	
	public void addChildDataField(String key, String value) throws Exception {
		DataField f = childDataFields.get(key);
		if(f != null) {
			f.setValue(value);
		}
		else {
			this.childDataFields.put(key, new DataField(key, value));
		}
	}
	
	public void addChildDataField(String key, String value, String type) throws Exception {
		DataField f = childDataFields.get(key);
		if(f != null) {
			f.setType(type);
			f.setValue(value);
		}
		else {
			this.childDataFields.put(key, new DataField(key, value, type));
		}
	}
	
	public DataField getDataField(String key) {
		return childDataFields.get(key);
	}
	
	public String getValue(String key) {
		DataField f = childDataFields.get(key);
		if(f != null) {
			return f.getValue();
		}
		return null;
	}
	
	public void setValue(String key, String value) throws Exception {
		DataField f = childDataFields.get(key);
		if(f != null) {
			f.setValue(value);
		}
		else {
			this.childDataFields.put(key, new DataField(key, value));
		}
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}  

}
