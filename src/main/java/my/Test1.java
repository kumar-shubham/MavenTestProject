package my;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;

public class Test1 {
	
		public static void main(String args[]) {
			
//			String a = "aabc";
//			String b = "fasdfasdfa sdfsd";
//			
//			System.out.println(b.replaceFirst("\\w{2}", "zz"));
//			
//			B b = new B();
//			
//			b.print();
			
//			LocalDate bday = LocalDate.of(1955, Month.MAY, 19); 
//			LocalDate today = LocalDate.now(); 
//			Period age = Period.between(bday, today);
//			int years = age.getYears(); 
//			int months = age.getMonths(); 
//			System.out.println("number of years: " + years); 
//			System.out.println("number of months: " + months);
//			System.out.println("number of months total: " + (years*12 + months));
			
			System.out.println(getExcelColumnNumber("BB"));
			
			
		}
		
		public static int getExcelColumnNumber(String column) {
	        int result = 0;
	        for (int i = 0; i < column.length(); i++) {
	            result *= 26;
	            result += column.charAt(i) - 'A' + 1;
	        }
	        return result;
	    }
		
}



class A{
	
	public String a  = "kumar";
	
	public String getA() {
		return a;
	}
	public void print(){
		System.out.println(getA());
	}
}

class B extends A{
	
	public String a = "shubham";
	
	public String getA() {
		return a;
	}
	
	public void print() {
		super.print();
		System.out.println(getA());
	}
	
}

