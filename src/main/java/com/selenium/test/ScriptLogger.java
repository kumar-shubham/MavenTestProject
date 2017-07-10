package com.selenium.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.WebElement;

public class ScriptLogger {

	private static Logger logger = Logger.getLogger(ScriptLogger.class);
	
	

	private static void initializeLoggerAppender(){

		PatternLayout layout = new PatternLayout();

		String conversionPattern = "[%p] %d [%t] - %m%n";
		layout.setConversionPattern(conversionPattern);

		DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
		rollingAppender.setFile(System.getProperty("user.home") + "/logs/acalog/script.log");
		rollingAppender.setDatePattern("'.'yyyy-MM-dd");
		rollingAppender.setLayout(layout);
		rollingAppender.activateOptions();

		logger.setLevel(Level.DEBUG);
		logger.addAppender(rollingAppender);


	}

	static{
		initializeLoggerAppender();
	}

	public static void writeInfo(Object message){

		logger.info(getCallerDetail() + "  " + message);
	}

	public static void writeInfo(Object message, Throwable t){

		logger.info(message, t);
	}


	public static void writeWarning(Object message){

		logger.warn(message);
	}

	public static void writeWarning(Object message, Throwable t){

		logger.warn(message, t);
	}

	public static void writeError(Object message){

		logger.error(message);
	}

	public static void writeError(Object message, Throwable t){

		logger.error(message, t);
	}
	
	public static void writeDebug(Object message){

		logger.debug(message);
	}

	public static void writeDebug(Object message, Throwable t){

		logger.debug(message, t);
	}
	
	public static void printPage(Sherlock sherlock, ArrayList<String> regexList){
		logger.info("-------->>>>>>> Inside printPage <<<<<<<--------");
		if(sherlock == null){
			logger.error("Driver object is null");
		}
		
		String page = "^^^^^ PRINTING HTML PAGE ^^^^^^" + "\n" 
						+ "FOR THE URL (" + sherlock.getDriverObject().getCurrentUrl() + ")\n"
						+ sherlock.getDriverObject().getPageSource() 
						+ "\n\n\n" + "^^^^^^ END OF PAGE PRINT ^^^^^^";
		
		page = maskString(page, regexList);
		
//		System.out.println(page);
		
//		logger.info(page);
		
	}
	
	
	public static void printEle(WebElement element, ArrayList<String> regexList){
		if(element == null){
			logger.error("element is null");
		}
		
		String page = "^^^^^ PRINTING HTML ELEMENT ^^^^^^" + "\n" 
						+ element.getAttribute("outerHTML")
						+ "\n\n\n" + "^^^^^^ END OF HTML ELEMENT ^^^^^^";
		
		page = maskString(page, regexList);
		
		logger.info(page);
		
	}
	
	private static String getCallerDetail(){
		 StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		 StackTraceElement e = stacktrace[3];//maybe this number needs to be corrected
		 return "[" + e.getFileName() + "." + e.getMethodName()  + "::"+ e.getLineNumber() + "]" ;
	}
	
	private static String maskString(String string, ArrayList<String> regexList){
		
		for(String regex: regexList){
			Pattern p = Pattern.compile(regex);
			Matcher matcher = p.matcher(string);

			while(matcher.find()){
				string = string.replaceAll(matcher.group(1), buildStringWithStars(matcher.group(1).length()));
			}
		}
		
		return string;
	}
	
	private static String buildStringWithStars(int i) {
		// TODO Auto-generated method stub
		CharSequence[] array = new CharSequence[i];
		Arrays.fill(array, "*");
		return String.join("", array);
	}
}
