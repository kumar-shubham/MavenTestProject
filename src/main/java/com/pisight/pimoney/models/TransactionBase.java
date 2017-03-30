package com.pisight.pimoney.models;

import java.util.HashMap;

public abstract class TransactionBase {
	
	private String fingerprint = null;

	protected HashMap<String, String> properties = new HashMap<String, String>();
	
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
	
	
	

}
