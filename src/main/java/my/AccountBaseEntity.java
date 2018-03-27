package my;

import java.util.Date;
import java.util.UUID;

/**
 * @author kumar
 *
 */

public abstract class AccountBaseEntity {
	
	protected UUID id;
	
	private String accountNumber = null;

	private String accountName = null;

	protected String accountHolder = null;
	
	protected String branch = null;
	
	protected String currency = null;
	
	protected String bankId = null;

	protected String accountHash = null;
	
	protected String fingerprint = null;

	protected Date createdAt;

	protected Date updatedAt;

	protected boolean status;

	protected boolean confirmed;
	
	protected String convertedBalance;
	

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the accountHolder
	 */
	public String getAccountHolder() {
		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 * @return the branch
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the bankId
	 */
	public String getBankId() {
		return bankId;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the accountHash
	 */
	public String getAccountHash() {
		return accountHash;
	}

	/**
	 * @param accountHash the accountHash to set
	 */
	public void setAccountHash(String accountHash) {
		this.accountHash = accountHash;
	}

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
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the confirmed
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * @param confirmed the confirmed to set
	 */
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	
	
	/**
	 * @return the convertedBalance
	 */
	public String getConvertedBalance() {
		return convertedBalance;
	}

	/**
	 * @param convertedBalance the convertedBalance to set
	 */
	public void setConvertedBalance(String convertedBalance) {
		this.convertedBalance = convertedBalance;
	}
	
	
	public abstract String getTag();
	
	
	public abstract String getInstitutionName();

	public abstract boolean isManuallyAdded();

	public abstract void setManuallyAdded(boolean manuallyAdded);
}
