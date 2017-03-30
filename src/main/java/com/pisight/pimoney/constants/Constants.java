package com.pisight.pimoney.constants;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	
	public static final String TAG_BANK = "Bank";
	public static final String TAG_CARD = "Card";
	public static final String TAG_LOAN = "Loan";
	public static final String TAG_FIXED_DEPOSIT = "Fixed Deposit";
	public static final String TAG_INVESTMENT = "Investment";
	
	public static final String DATEFORMAT_MM_SPACE_DD_SPACE_YYYY = "MM dd yyyy";
	public static final String DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY = "dd MMM yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_COMMA_YYYY = "MMM dd,yyyy";
	public static final String DATEFORMAT_DD_DASH_MM_DASH_YYYY = "dd-MM-yyyy";
	public static final String DATEFORMAT_DD_DASH_MMM_DASH_YYYY = "dd-MMM-yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_YYYY = "MMMM dd,yyyy";
	public static final String DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY = "MMMM dd, yyyy";
	public static final String DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY = "MMM dd yyyy";
	public static final String DATEFORMAT_DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";
	public static final String DATEFORMAT_YYYY_DASH_MM_DASH_DD  = "yyyy-MM-dd";
	public static final String DATEFORMAT_DD_DOT_MM_DOT_YYYY  = "dd.MM.yyyy";
	public static final String DATEFORMAT_DD_DOT_MM_DOT_YY  = "dd.MM.yy";
	public static final String DATEFORMAT_DD_MMM_YY = "ddMMMyy";
	public static final String DATEFORMAT_DD_SPACE_MMMM_SPACE_YYYY = "dd MMMM yyyy";
	public static final String DATEFORMAT_DD_DASH_MMM_DASH_YY = "dd-MMM-yy";
	public static final String DATEFORMAT_DD_SPACE_MMM_SPACE_YY = "dd MMM yy";
	public static final String DATEFORMAT_DD_SLASH_MM_SLASH_YY = "dd/MM/yy";
	
	public static final String DATEFORMAT_MM_SPACE_DD = "MM dd";
	public static final String DATEFORMAT_DD_SPACE_MMM = "dd MMM";
	public static final String DATEFORMAT_MMMM_SPACE_DD = "MMMM dd";
	public static final String DATEFORMAT_DD_SLASH_MM  = "dd/MM";
	
	public static final String MFA_TYPE_OTP = "OTP";
	public static final String MFA_TYPE_IMAGE = "IMAGE";
	public static final String MFA_TYPE_SQ = "SQ";
	public static final String MFA_TYPE_RADIO = "RADIO";
	
	public static final String FIELD_TYPE_RADIO_WITH_INPUT = "radio_with_input";
	public static final String FIELD_TYPE_RADIO_WITH_LABEL = "radio_with_label";
	
	public static final String PROCESS_ADD = "add";
	public static final String PROCESS_REFRESH = "refresh";
	public static final String PROCESS_EDIT = "edit";
	public static final String PROCESS_DELETE = "delete";
	
	public static final String FIELD_TYPE_TEXT = "text";
	public static final String FIELD_TYPE_PASSWORD = "password";
	public static final String FIELD_TYPE_OPTION = "option";
	public static final String FIELD_TYPE_MFA_OPTION = "mfa_option";
	
	public static final String SELENIUM_HUB = "SELENIUM_HUB";
	public static final String SELENIUM_HUB_URL = "SELENIUM_HUB_URL";
	public static final String ENVIRONMENT = "ENVIRONMENT";
	public static final String ENVIRONMENT_NAME = "ENVIRONMENT_NAME";
	public static final String ENVIRONMENT_TEST = "TEST";
	
	public static final String USER_ID = "userId";
	public static final String INSTITUTION_CODE = "institutionCode";
	
	
	public static final String MFA_SEPERATOR = "&&";
	
	public static List<String> dateFormatList = new ArrayList<String>();
	
	public static final String FAILED = "Failed";

	public static final String SUCCESS = "Success";
	
	public static final int SUCCESS_CODE = 1;
	
	public static final int ERROR_CODE = 0;
	
	public static final String PATH_SCREENSHOT = "/home/kumar/aca/logs/screenshots/";
	
	static{
		dateFormatList.add(DATEFORMAT_MM_SPACE_DD_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_DD_SPACE_MMM_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_MMM_SPACE_DD_COMMA_YYYY);
		dateFormatList.add(DATEFORMAT_DD_DASH_MM_DASH_YYYY);
		dateFormatList.add(DATEFORMAT_DD_DASH_MMM_DASH_YYYY);
		dateFormatList.add(DATEFORMAT_MM_SPACE_DD_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_MMMM_SPACE_DD_COMMA_YYYY);
		dateFormatList.add(DATEFORMAT_MMMM_SPACE_DD_COMMA_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_MMM_SPACE_DD_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_DD_SLASH_MM_SLASH_YYYY);
		dateFormatList.add(DATEFORMAT_YYYY_DASH_MM_DASH_DD);
		dateFormatList.add(DATEFORMAT_DD_DOT_MM_DOT_YYYY);
		dateFormatList.add(DATEFORMAT_DD_DOT_MM_DOT_YY);
		dateFormatList.add(DATEFORMAT_DD_MMM_YY);
		dateFormatList.add(DATEFORMAT_DD_SPACE_MMMM_SPACE_YYYY);
		dateFormatList.add(DATEFORMAT_DD_DASH_MMM_DASH_YY);
		dateFormatList.add(DATEFORMAT_DD_SPACE_MMM_SPACE_YY);
		dateFormatList.add(DATEFORMAT_DD_SLASH_MM_SLASH_YY);
	}

}
