package com.pisight.pimoney.models;

import java.io.Serializable;
import java.util.HashMap;

public abstract class TransactionBase implements Cloneable, Serializable{
	
	private static final long serialVersionUID = -7878045669075607735L;

	private String fingerprint = null;
	
	private String transactionHash = null;

	private HashMap<String, String> properties = new HashMap<String, String>();
	
	/**
	 * @return the fingerprint
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * @param fingerprint the fingerprint to set
	 */
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	/**
	 * @return the properties
	 */
	public HashMap<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return the transactionHash
	 */
	public String getTransactionHash() {
		return transactionHash;
	}

	/**
	 * @param transactionHash the transactionHash to set
	 */
	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}
	
	public abstract String getTag();
	
	public void setTag(String tag) {};
	
}
