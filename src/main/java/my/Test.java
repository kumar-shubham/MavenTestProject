package my;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

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
//		aCalendar.set(Calendar.YEAR, 2011);
//		aCalendar.set(Calendar.MONTH, 11);
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
		
//		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
//		
//		Date dob = sdf.parse("12201991");
//		
//		LocalDate today = LocalDate.now();
//		LocalDate birthday = new java.sql.Date( dob.getTime() ).toLocalDate();
//
//		Period p = Period.between(birthday, today);
//		
//		System.out.println(p.getYears());
//		System.out.println(p.getMonths());
//		System.out.println(p.getDays());
		
//		System.out.println(formatDecimal(".111122", 3));
		
		test1();
		
		
	}
	
	private static void test1() {
		ZoneId america = ZoneId.of("Asia/Singapore");
		LocalDateTime localtDateAndTime = LocalDateTime.now();
		ZonedDateTime dateAndTimeInNewYork  = ZonedDateTime.of(localtDateAndTime, america );
		System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);
		System.out.println(dateAndTimeInNewYork.toLocalDateTime());
		System.out.println(dateAndTimeInNewYork.withFixedOffsetZone());
		 



		     
	}
	
	public static final String formatDecimal(String number, int noOfDecimals) {
		if(StringUtils.isEmpty(number)) {
			return null;
		}
		number = number.replace(",", "");
		if(number.indexOf(".") < 0 || number.substring(number.indexOf(".")+1).length() <= noOfDecimals ) {
			return number;
		}
		Double num = Double.valueOf(number);
		String str = buildStringWithChar(noOfDecimals, "0");
		NumberFormat formatter = new DecimalFormat("#0."+str);    
		return formatter.format(num);
	}
	
	private static String buildStringWithChar(int i, String s) {
		CharSequence[] array = new CharSequence[i];
		Arrays.fill(array, s);
		return String.join("", array);
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
