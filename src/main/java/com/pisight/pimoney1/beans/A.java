package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bouncycastle.asn1.isismtt.x509.DeclarationOfMajority;

public class A {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		A a = new A();

		//		a.reverse(1);
		//		int b = a.findMinXor(new ArrayList(Arrays.asList(12, 4, 6, 2)));
		//		System.out.println(b);
		System.out.println(a.divide(25, 3));

		ArrayList<Integer> b = new ArrayList<Integer>(Arrays.asList(4, 1, 1, 2, 1, 3));
		ArrayList<Integer> c = new ArrayList<Integer>(Arrays.asList(1));
		//		a.merge(b, c);
		System.out.println(b);
		a.removeElement(b, 1);
		System.out.println(b);

	}

	public int removeElement(ArrayList<Integer> a, int b) {

		int index = 0;
		int n = a.size();

		if (a == null || a.size() == 0)
			return 0;

		for (int i = 0; i < n; i++) {

			if (a.get(i).intValue() != b) {
				int temp = a.get(index);
				a.set(index, a.get(i));
				index++;
			}
		}
		return index;
	}

	public void merge(ArrayList<Integer> a, ArrayList<Integer> b) {


		int j = 0;
		int size = a.size();
		for(int i = 0; i<b.size(); i++, j++){

			int num = b.get(i);

			if(j<a.size() && num > a.get(j)){
				i--;

			}
			else{
				a.add(j, num);
			}

		}
	}


	public int divide(int dividend, int divisor) {

		int current = 1;
		int denom = divisor;
		// This step is required to find the biggest current number which can be
		// divided with the number safely.
		while (denom <= dividend) {
			current <<= 1;
			denom <<= 1;
		}
		// Since we may have increased the denomitor more than dividend
		// thus we need to go back one shift, and same would apply for current.
		denom >>= 1;
			current >>= 1;
			int answer = 0;
			// Now deal with the smaller number.
			while (current != 0) {
				if (dividend >= denom) {
					dividend -= denom;
					answer |= current;
				}
				current >>= 1;
		denom >>= 1;
			}
			return answer;
	}

	public long reverse(long a) {

		int[] result = new int[32];

		for(int i = 0; i<32; i++){
			result[i] = (int) (1 & (a >> i));
		}

		System.out.println(Arrays.toString(result));
		System.out.println(convertToDecimal(result));
		return 0l;
	}

	public long convertToDecimal(int[] binary){

		long result = 0;

		for(int i = 0; i<binary.length; i++){

			result += binary[(binary.length-1-i)] * Math.pow(2, i);
		}

		return result;
	}

	public int findMinXor(ArrayList<Integer> A) {

		Collections.sort(A);

		int[] xor = new int[A.size()-2];

		for(int i = 1; i<A.size()-1; i++){
			int xor1 = A.get(i-1)^A.get(i);
			int xor2 = A.get(i+1)^A.get(i);

			if(xor1<xor2){
				xor[i-1] = xor1;
			}
			else{
				xor[i-1] = xor2;
			}

		}

		int min = Integer.MAX_VALUE;
		for(int i =0; i<xor.length;i++){

			if(xor[i] < min){
				min = xor[i];
			}
		}
		return min;
	}
}

