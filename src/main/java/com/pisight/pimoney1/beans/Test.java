package com.pisight.pimoney1.beans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String regex = "(.*) (\\d{4}.\\d{4}.\\d{4}.\\d{4}) (.*) ((\\d*,)?\\d+(\\.)\\d+) ((\\d*,)?\\d+(\\.)\\d+)";
		String text = "UOB PRVI MILES 5522-5320-5010-6038 ANSHU SINHA 1,599.38 50.00";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		
		System.out.println(m.matches());

	}

}
