package my;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test2 {
	
	public static void main(String args[]) {
		
		int startYear = 1997;
		int startMonth = 4;
		
		int endYear = 2000;
		int endMonth = 7;
		
		System.out.println(checkQuery1(startYear, startMonth, endYear, endMonth));
		
		Calendar cal = Calendar.getInstance();
		
		System.out.println(cal.get(Calendar.MONTH));
		
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(cal.get(Calendar.MONTH));
		System.out.println(cal.getTime());
		cal.add(Calendar.MONTH, 2);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		System.out.println(cal.get(Calendar.MONTH));
		System.out.println(cal.getTime());
		
		getLastDate(12, 2017);
	}
	
	private static Date getLastDate(int month, int year) {
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		
		Date result = cal.getTime();
		System.out.println(result);
		DateFormat df = new SimpleDateFormat("MMddyyyy");
		String result1 = df.format(result);
		System.out.println(result1);
		
		return result;
	}
	
	
	private static boolean checkQuery1(int startYear, int startMonth, int endYear, int endMonth) {
		
		int year = 1997;
		int month = 4;
		
		if(((startYear < year) || (startYear == year && month >= startMonth)) && 
				((endYear > year) || (endYear == year && endMonth >= month))) {
			return true;
		}
		
		return false;
	}

}
