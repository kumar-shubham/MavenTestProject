package my;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Test {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		/*Calendar cal = Calendar.getInstance();
		int date =	cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int firstMonday = getFirstMonday(year, cal.get(Calendar.MONTH));
		
		System.out.println(date);
		System.out.println(year);
		System.out.println(firstMonday);*/
		
//		Calendar aCalendar = Calendar.getInstance();
//		aCalendar.set(Calendar.DATE, 1);
//		aCalendar.add(Calendar.DAY_OF_MONTH, -1);
//		Date lastDateOfPreviousMonth = aCalendar.getTime();
//		aCalendar.set(Calendar.DATE, 1);
//		Date firstDateOfPreviousMonth = aCalendar.getTime();
//
//		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		String firstDate = format.format(firstDateOfPreviousMonth);
//		String lastDate = format.format(lastDateOfPreviousMonth);
//		System.out.println("firstDate--" + firstDate);
//		System.out.println("lastDate--" + lastDate);
//		
//		
//		UUID id = UUID.fromString("d3b6bad0-fbf2-41e3-bf7d-6abe79b06a31");
//		
//		System.out.println(id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		
		Date dob = sdf.parse("12201991");
		
		LocalDate today = LocalDate.now();
		LocalDate birthday = new java.sql.Date( dob.getTime() ).toLocalDate();

		Period p = Period.between(birthday, today);
		
		System.out.println(p.getYears());
		System.out.println(p.getMonths());
		System.out.println(p.getDays());
		
		
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
