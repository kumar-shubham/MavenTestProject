package com.pisight.pimoney.models;

/**
 * @author kumar
 * <br/><br/>
 * 
 * 
 * This class has different transaction code constants to be used for different type of transactions 
 *
 */

public class TransCode {
	
	/**
	 * code = "pa"<br/><br/>
	 * accrued interest for purchased fixed-income securities (pa)
	 */
	public static String PURCHASE_ACCRUED_INTEREST = "pa";
	
	/**
	 * code = "sa"<br/><br/>
	 *  accrued interest for sold fixed-income securities (sa)
	 */
	public static String SELL_ACCRUED_INTEREST = "sa";
	
	/**
	 * code = "ac"<br/><br/>
	 * Reduce the cost basis of a security without changing the number of units held, as in spinoffs,
	 * in which the number of shares of the original security does not change.<br/>
	 * (for long positions)
	 */
	public static String ADJUST_COST_LONG = "ac";
	
	/**
	 * code = "as"<br/><br/>
	 *  Reduce the cost basis of a security without changing the number of units held, as in spinoffs,
	 * in which the number of shares of the original security does not change.<br/>
	 * (for short positions)
	 */
	public static String ADJUST_COST_SHORT = "as";
	
	/**
	 * code = "by"<br/><br/>
	 * Establish a long position.<br/>
	 * Exchange foreign currency.
	 */
	public static String BUY = "by";
	
	/**
	 * code = "lc"<br/><br/>
	 * Change the pledge status, location, or custodian of a position.<br/>
	 * (for long positions)
	 */
	public static String CHANGE_LONG = "lc";
	
	/**
	 * code = "sc"<br/><br/>
	 *Change the pledge status, location, or custodian of a position.<br/>
	 * (for short positions) 
	 */
	public static String CHANGE_SHORT = "sc";
	
	/**
	 * code = "cs"<br/><br/>
	 * Close some or all shares of a short position.
	 */
	public static String COVER_SHORT = "cs";
	
	/**
	 * code = "li"<br/><br/>
	 * Deliver a position or cash into a portfolio.<br/>
	 * (for long positions)
	 */
	public static String DELIVER_IN_LONG = "li";
	
	/**
	 * code = "si"<br/><br/>
	 * Deliver a position or cash into a portfolio.<br/>
	 * (for short positions)
	 */
	public static String DELIVER_IN_SHORT = "si";
	
	/**
	 * code = "lo"<br/><br/>
	 * Deliver a position or cash out of a portfolio.<br/>
	 * (for long positions)
	 */
	public static String DELIVER_OUT_LONG = "lo";
	
	/**
	 * code = "so"<br/><br/>
	 * Deliver a position or cash out of a portfolio.<br/>
	 * (for short positions)
	 */
	public static String DELIVER_OUT_SHORT = "so";
	
	/**
	 * code = "dp"<br/><br/>
	 * Move cash between cash accounts of the same currency within a portfolio. 
	 * Record portfolio and management fees.
	 */
	public static String DEPOSIT = "dp";
	
	/**
	 * code = "dv"<br/><br/>
	 * Record dividend income for stock or dividend expenses for short stock.
	 */
	public static String DIVIDEND = "dv";
	
	/**
	 * code = "dr"<br/><br/>
	 * Reclaim the foreign withholding tax on a dividend from an issuer country.
	 */
	public static String DIVIDEND_RECLAIM = "dr";
	
	/**
	 * code = "dw"<br/><br/>
	 * Write off the foreign withholding tax reclaim amounts on a dividend from an issuer country.
	 */
	public static String DIVIDEND_RECLAIM_WRITEOFF = "dw";
	
	/**
	 * code = "in"<br/><br/>
	 * Record interest income (in) for cash, an interest expense, and bond interest payments.
	 */
	public static String INTEREST = "in";
	
	/**
	 * code = "ai"<br/><br/>
	 * Enter negative interest (ai), such as for a margin account.
	 */
	public static String INTEREST_NEGATIVE = "ai";
	
	/**
	 * code = "ir"<br/><br/>
	 * Reclaim the foreign withholding tax on interest from an issuer country.
	 */
	public static String INTEREST_RECLAIM = "ir";
	
	/**
	 * code = "iw"<br/><br/>
	 * Write off the foreign withholding tax reclaim amounts on interest from an issuer country.
	 */
	public static String INTEREST_RECLAIM_WRITEOFF = "iw";
	
	/**
	 * code = "pd"<br/><br/>
	 * Pay down principal on asset-backed securities, reducing the current face value and cost basis.<br/>
	 * (for long positions)
	 */
	public static String PAYDOWN_LONG = "pd";
	
	/**
	 * code = "ps"<br/><br/>
	 * Pay down principal on asset-backed securities, reducing the current face value and cost basis.<br/>
	 * (for short positions)
	 */
	public static String PAYDOWN_SHORT = "ps";
	
	/**
	 * code = "cc"<br/><br/>
	 * Capital call (cc), modify commitment (mc), valuation (va), record commitments, and distributions. 
	 * (Note: The tran codes cc, mc, and va are for private equity security types only.)
	 */
	public static String PRIVATE_EQUITY_CC = "cc";
	
	/**
	 * code = "mc"<br/><br/>
	 * Capital call (cc), modify commitment (mc), valuation (va), record commitments, and distributions. 
	 * (Note: The tran codes cc, mc, and va are for private equity security types only.)
	 */
	public static String PRIVATE_EQUITY_MC = "mc";
	
	/**
	 * code = "va"<br/><br/>
	 * Capital call (cc), modify commitment (mc), valuation (va), record commitments, and distributions. 
	 * (Note: The tran codes cc, mc, and va are for private equity security types only.)
	 */
	public static String PRIVATE_EQUITY_VA = "va";
	
	/**
	 * code = "rc"<br/><br/>
	 * Reduce the cost basis and number of units held (quantity) of a security across all tax lots, 
	 * as in exchanges (e.g. preferred stock for common stock).<br/>
	 * (for long positions)
	 */
	public static String RETURN_OF_CAPITAL_LONG = "rc";
	
	/**
	 * code = "rs"<br/><br/>
	 * Reduce the cost basis and number of units held (quantity) of a security across all tax lots, 
	 * as in exchanges (e.g. preferred stock for common stock).<br/>
	 * (for short positions)
	 */
	public static String RETURN_OF_CAPITAL_SHORT = "rs";
	
	/**
	 * code = "sl"<br/><br/>
	 * Selling a security
	 */
	public static String SELL = "sl";
	
	/**
	 * code = "ss"<br/><br/>
	 * Open a short position.
	 */
	public static String SELL_SHORT = "ss";
	
	/**
	 * code = "ti"<br/><br/>
	 * Transfer in a position or cash into a portfolio from another portfolio.<br/>
	 * (for long positions)
	 * 
	 */
	public static String TRANSFER_IN_LONG = "ti";
	
	/**
	 * code = "ts"<br/><br/>
	 * Transfer in a position or cash into a portfolio from another portfolio.<br/>
	 * (for short positions)
	 */
	public static String TRANSFER_IN_SHORT = "ts";
	
	/**
	 * code = "to"<br/><br/>
	 * Transfer a position or cash out of a portfolio into another portfolio.<br/>
	 * (for long positions)
	 */
	public static String TRANSFER_OUT_LONG = "to";
	
	/**
	 * code = "tr"<br/><br/>
	 * Transfer a position or cash out of a portfolio into another portfolio.<br/>
	 * (for short positions)
	 */
	public static String TRANSFER_OUT_SHORT = "tr";
	
	/**
	 * code = "wd"<br/><br/>
	 * Move cash between accounts within a portfolio. Establish a margin account balance.
	 */
	public static String WITHDRAWAL = "wd";

}
