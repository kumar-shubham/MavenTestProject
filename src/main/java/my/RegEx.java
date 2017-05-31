package my;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inputString = "my account number is 10096 and my name is kumar shubham and 20010 ";

		String regex = " (\\d{5}) ";

		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(inputString);

		while(matcher.find()){
			inputString = inputString.replaceAll(matcher.group(1), buildStringWithStars(matcher.group(1).length()));
		}
		
		System.out.println(inputString);

	}

	private static String buildStringWithStars(int i) {
		// TODO Auto-generated method stub
		CharSequence[] array = new CharSequence[i];
		Arrays.fill(array, "*");
		return String.join("", array);
	}

}
