package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BitShiftTest {

	public static void main(String args[]) {

		int number = 8; //0000 1000
		System.out.println("Original number : " + number);

		//left shifting bytes with 1 position
		number = number<<1; //should be 16 i.e. 0001 0000

		//equivalent of multiplication of 2
		System.out.println("value of number after left shift: " + number);

		number = -8;
		//right shifting bytes with sign 1 position
		number = number>>1; //should be 16 i.e. 0001 0000

		//equivalent of division of 2
		System.out.println("value of number after right shift with sign: " + number);

		number = -8;
		//right shifting bytes without sign 1 position
		number = number>>>1; //should be 16 i.e. 0001 0000

		//equivalent of division of 2
		System.out.println("value of number after right shift with sign: " + number);


		List<Integer> A = new ArrayList<Integer>(Arrays.asList(1, 2, 4, 3, 3, 2, 2, 3, 1, 1));

		int [] bits;
	    bits = new int[32];
	    
	    for (int num : A) {
	        
	        for (int i = 0; i < 32; i++) {
	        	System.out.println("num -> " + num + " :: i ->" + i + " :: (num>>i) -> " + (num>>i) + " :: (1 & (num >> i)) -> " + (1 & (num >> i)));
	            bits[i] += (1 & (num >> i));
	            bits[i] %= 3;
	        }
	        
	    }
	    
	    number = 0;
	    
	    for (int i = 31; i >= 0; i--) {
	        number = number * 2 + bits[i];
	    }

	}  



}
