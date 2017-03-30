package com.pisight.pimoney1.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ParserUtility {
	
	public static final String DATEFORMAT_MM_SPACE_DD_SPACE_YYYY = "MM dd yyyy";
	public static final String DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY = "dd MMM yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_COMMA_YYYY = "MMM dd,yyyy";
	public static final String DATEFORMAT_DD_DASH_MM_DASH_YYYY = "dd-MM-yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_YYYY = "MMMM dd,yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY = "MMMM dd, yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY = "MMM dd yyyy";
	public static final String DATEFORMAT_DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";
	
	public static final String DATEFORMAT_MM_SPACE_DD = "MM dd";
	public static final String DATEFORMAT_DD_SPACE_MMM = "dd MMM";
	public static final String DATEFORMAT_MMMM_SPACE_DD = "MMMM dd";
	public static final String DATEFORMAT_DD_SLASH_MM  = "dd/MM";
	
	public static String getYear(String rawDate, String dateFormat, String reference, String referenceFormat) throws Exception{
		
		Date date = getDate(referenceFormat, reference);
		Date date1 = getDate(dateFormat, rawDate);
		if(date == null || date1 == null){
			throw new Exception("Invalid date Format");
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		c.setTime(date1);
		int month1 = c.get(Calendar.MONTH);
		int day1 = c.get(Calendar.DAY_OF_MONTH);
		
		if((month1 > month) || (month1 == month && day1 > day)){
			c.set(Calendar.YEAR, year-1);
		}
		else{
			c.set(Calendar.YEAR, year);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(referenceFormat);
		String result = sdf.format(c.getTime());
		
		System.out.println();
		System.out.println("Raw date       :: " + rawDate);
		System.out.println("Raw format     :: " + dateFormat);
		System.out.println("Ref date       :: " + reference);
		System.out.println("Ref format     :: " + referenceFormat);
		System.out.println("New Date       :: " + result);
		
		return result;
		
	}
	
	private static Date getDate(String format, String value){
		
		Date date = null;
		try {
		    SimpleDateFormat sdf = new SimpleDateFormat(format);
		    date = sdf.parse(value);
		} catch (ParseException ex) {
		    ex.printStackTrace();
		}
		if (date == null) {
		    // Invalid date format
		} else {
		    // Valid date format
		}
		
		return date;
		
		
	}
}
	
