package com.pisight.pimoney.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Currency {
	
	public static final HashMap<String, String> CURRENCY_MAP = new HashMap<String, String>();
	
	static{
		ArrayList<String> list = new ArrayList<String>(Arrays.asList("AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN",
				"BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTC","BTN","BWP","BYR","BZD","CAD","CDF",
				"CHF","CLF","CLP","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EEK","EGP","ERN","ETB",
				"EUR","FJD","FKP","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR",
				"ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD",
				"KZT","LAK","LBP","LKR","LRD","LSL","LTL","LVL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO",
				"MTL","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN",
				"PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP",
				"SLL","SOS","SRD","STD","SVC","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH",
				"UGX","USD","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XDR","XOF","XPD","XPF",
				"XPT","YER","ZAR","ZMK","ZMW","ZWL","CNH","SSP"));
		
		for(String currency : list){
			CURRENCY_MAP.put(currency, null);
		}
		list = null;
	}
	
	
	public static boolean isValid(String currency){
		return CURRENCY_MAP.containsKey(currency);
	}
	
	

}
