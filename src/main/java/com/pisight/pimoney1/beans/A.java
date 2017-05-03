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
		//		System.out.println(a.divide(25, 3));

		ArrayList<Integer> b = new ArrayList<Integer>(Arrays.asList(1, 0, 0, 1, 1, 0, 0, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0, 0, 2, 0, 2, 2, 2, 0, 0, 1, 1, 1, 2, 2, 0, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 1, 2, 1, 1, 0, 0, 1, 2, 1, 1, 0, 1, 0, 2, 0, 2, 1, 0, 1, 1, 0, 0, 1, 2, 0, 1, 0, 2, 1, 0, 1, 0, 1, 0, 1, 2, 2, 2, 0, 1, 1, 0, 2, 2, 2, 0, 0, 0, 0, 1, 1, 2, 1, 0, 1, 0, 1, 2, 2, 1, 0, 2, 0, 0, 1, 2, 1, 0, 2, 1, 0, 2, 0, 2, 1, 1, 1, 1, 1, 0, 1, 2, 0, 0, 1, 0, 1, 2, 0, 1, 1, 2, 1, 0, 2, 0, 0, 0, 2, 0, 1, 0, 2, 1, 1, 0, 1, 2, 1, 0, 0));
		ArrayList<Integer> c = new ArrayList<Integer>(Arrays.asList(1,0,2,0,1,2));
		ArrayList<Integer> d = new ArrayList<Integer>(Arrays.asList(2, 1, 0, 2, 2, 1, 0, 0, 1, 1, 0, 2, 0, 0, 2, 1, 0, 1, 2, 1, 0, 0, 0, 2, 2, 2, 1, 2, 2, 0, 2, 1, 2, 2, 2, 0, 1, 0, 2, 1, 1, 2, 0, 2, 0, 1, 0, 2, 1, 2, 2, 1, 1, 1, 1, 2, 1, 0, 0, 2, 0, 2, 0, 2, 0, 2, 2, 2, 1, 2, 0, 0, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 1, 1, 1, 0, 2, 1, 0, 0, 0, 2, 0, 2, 1, 2, 2, 2, 0));
		ArrayList<Integer> e = new ArrayList<Integer>(Arrays.asList(0, 0, 2, 2, 2, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 2, 1, 1, 1, 0, 1, 1, 2, 2, 2, 2, 1, 1, 1, 0, 2, 0, 2, 2, 1, 2, 1, 1, 1, 2, 0, 2, 0, 0, 0, 1, 2, 0, 1, 0, 1, 1, 1, 2, 0, 2, 1, 2, 1, 2, 0, 1, 2, 2, 0, 2, 0, 2, 2, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 2, 2, 0, 2, 2, 1, 1, 1, 0, 0, 2, 1, 1, 1, 2, 1, 0, 1, 0, 0, 1, 0, 1, 1, 2, 2, 2, 1, 1, 1, 2, 1, 0, 1, 2, 1, 0, 1, 0, 0, 2, 1, 0, 2, 2, 0, 2, 1, 2, 2, 0, 2, 0, 2, 0, 0));

		//		a.merge(b, c);
//		System.out.println(b);
		//		a.removeElement(b, 1);
		a.sortColors1(e);
		System.out.println(e);

	}
	
	public void sortColors1(ArrayList<Integer> a) {
		
		int size = a.size();
		int index0 = 0;
		int index2 = a.size()-1;
		while(a.get(index0) == 0){
			index0++;
		}
		for(int i = 1; i<size; i++){
			if(index0>=size){
				break;
			}
			int num = a.get(i);
			if(num == 0){
				a.set(i, a.get(index0));
				a.set(index0, num);
				index0++;
			}
			System.out.println(a);
		}
		System.out.println(a);
		while(a.get(index2) == 2){
			index2--;
		}
		for(int i = index0; i<size; i++){
			int num = a.get(i);
			if(i>= index2){
				break;
			}
			if(num == 2){
				a.set(i, a.get(index2));
				a.set(index2, num);
				index2--;
				while(a.get(index2) == 2){
					index2--;
				}
			}
		}
	}

	public void sortColors(ArrayList<Integer> a) {

		int size = a.size();
		int index0 = 0;
		int index1 = 0;
		for(int i = 1; i<size; i++){

			int num = a.get(i);
//			System.out.println("i -> " + i + " :: ith -> " + num);
//			System.out.println("Index0 :-> " + index0 + " :: index1 :-> " + index1);
			if(num == 0){
				int temp = num;
				if(a.get(index0) == 1 && a.get(index1) != 0){
					int temp1 = a.get(index1);
					a.set(index1, 1);
					a.set(i, temp1);
					a.set(index0, 0);
					index1++;
					while(a.get(index1) == 1 && index1<size){
						index1++;
					}
					
				}
				else{
					a.set(i, a.get(index0));
					a.set(index0, num);
				}
				index0++;
				while(a.get(index0) == 0 && index0<size){
					index0++;
				}
				if(index1 <= index0){
					index1++;
					while(a.get(index1) == 1 && index1<size){
						index1++;
					}
				}

			}
			else if(index1 < size && num == 1){
				int temp = num;
				a.set(i, a.get(index1));
				a.set(index1, num);
				index1++;
			}
//			System.out.println(a);
		}


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

