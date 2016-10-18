package com.pisight.pimoney1.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.pisight.pimoney1.parsers.Parser;

public class ParserFactory {
	
	private static HashMap<String, String> instituteClassMap = new HashMap<String, String>();
	
	static{
		instituteClassMap.put("Amex (SG) -Manual", "AMEX");
	}

	public Parser getParser(String name, String container, String locale, String type) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if(container.equalsIgnoreCase("card") || container.equalsIgnoreCase("CreditCards")){
			container = "Card";
		}
		else if(container.equalsIgnoreCase("bank") || container.equalsIgnoreCase("Banks")){
			container = "Bank";
		}
		
		
		String className = "com.pisight.pimoney.beans." + instituteClassMap.get(name) + locale.toUpperCase() + container + type.toUpperCase() + "Scrapper";
		Class<?> cls = Class.forName(className);
		Object object = cls.newInstance();
		Parser parser = (Parser)object;
		return parser;
	}

}
