package my;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Calendar cal = Calendar.getInstance();
		int date =	cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int firstMonday = getFirstMonday(year, cal.get(Calendar.MONTH));
		
		System.out.println(date);
		System.out.println(year);
		System.out.println(firstMonday);
		
		
	}
	
	public static int getFirstMonday(int year, int month) {
		
		Calendar cacheCalendar = Calendar.getInstance();
		
	    cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
	    cacheCalendar.set(Calendar.MONTH, month);
	    cacheCalendar.set(Calendar.YEAR, year);
	    return cacheCalendar.get(Calendar.DATE);
	}

}
