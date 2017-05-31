package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class B {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		B b = new B();
		
		ArrayList<Integer> a = new ArrayList<Integer>(Arrays.asList(1, 4, 10));
		ArrayList<Integer> c = new ArrayList<Integer>(Arrays.asList(2, 15, 20));
		ArrayList<Integer> d = new ArrayList<Integer>(Arrays.asList(10, 12));
		
//		System.out.println(b.diffPossible(c, 0));
		
//		System.out.println(b.maxone(a, 2));
		
//		System.out.println(b.minimize(a, c, d));
		
		System.out.println("kumar  ".hashCode());
		System.out.println("kumar  ".hashCode());
		System.out.println("kuma r ".hashCode());
		System.out.println("k".hashCode());
		System.out.println("u".hashCode());
		System.out.println("m".hashCode());
		System.out.println("a".hashCode());
		System.out.println("r".hashCode());

	}
	
	
public int minimize(final List<Integer> a, final List<Integer> b, final List<Integer> c) {
	    
	    int minDiff = Integer.MAX_VALUE;
	    
	    int i = a.size()-1;
	    int j = b.size()-1;
	    int k = c.size()-1;
	    
	    boolean loop = true;
	    
	    while(loop){
//	    	System.out.println("test");
	    	
	        int A = a.get(i);
	        int B = b.get(j);
	        int C = c.get(k);
//	        System.out.println(A + " : " + B  + " : " + C );
	        
	        int diff = findDiff(A,B,C);
	        if(diff < minDiff){
	            minDiff = diff;
	        }
	        
	        if(A >= B && A >= C){
	            i--;
	            if(i<0){
	                loop = false;
	            }
	        }
	        else if(B >= C && B >= A){
	            j--;
	            if(j<0){
	                loop = false;
	            }
	        }
	        else if(C >= A && C >= B){
	            k--;
	            if(k < 0){
	                loop = false;
	            }
	        }
	    }
	    
	    return minDiff;
	    
	}
	
	
	private int findDiff(int a, int b, int c){
	    return Math.max(Math.max(Math.abs(a-b), Math.abs(b-c)), Math.abs(c-a));
	}
	
public ArrayList<Integer> maxone(ArrayList<Integer> a, int b) {
	    
	    int start = 0;
	    int end = 0;
	    int flipLeft = b;
	    int max = 0;
	    int tempStart = 0;
	    for(int i = 0; i<a.size();i++){
	    	System.out.println(i + " :  " + start + " : " + end + " : " + flipLeft + " : " + max );
	        int digit = a.get(i);
	        if(digit == 0){
	            if(flipLeft > 0){
	                flipLeft--;
	            }
	            else if(b > 0){
	                if(a.get(tempStart) == 0){
	                	tempStart++;
	                    System.out.println("start1 -> " + start);
	                }
	                else{
	                    while(a.get(tempStart) == 1){
	                    	System.out.println("start2 -> " + start);
	                    	tempStart++;
	                    }
	                    tempStart++;
	                }
	            }
	            else{
	            	tempStart = i+1;
	            }
	        }
	        
	        if((i-tempStart+1) > max){
	            end = i;
	            start = tempStart;
	            max = i-tempStart+1;
	        }
	        
	    }
	    
	    System.out.println(start);
	    ArrayList<Integer> result = new ArrayList<Integer>();
	    
	    for(int i =start; i<=end;i++){
	        result.add(i);
	    }
	    
	    return result;
	}
	
public int diffPossible(ArrayList<Integer> a, int b) {
	    
	    if(a.size() == 1){
	        return 0;
	    }
	    
	    for(Integer num: a){
	        
	        int diff = b + num;
	        if(diff < num){
	           return 0; 
	            
	        }
	        if(isPresent(a, diff)){
	        	if(b == 0){
	        		int start = getStart(a, diff);
	        		int end = getEnd(a, diff);
	        		
	        		if(start<end){
	        			return 1;
	        		}
	        	}
	        	else{
	        		return 1;
	        	}
	            
	        }
	    }
	    
	    return 0;
	    
	}

	private int getStart(ArrayList<Integer> a, int b){
		
		int start = 0;
	    int end = a.size()-1;
	    int index = 0;
	    while(start <= end){
	        
	        int mid = start + (end-start)/2;
	        
	        int num = a.get(mid);
	        if(num == b){
	        	if(mid > 0 && a.get(mid-1) == num ){
	        		end = mid-1;
	        	}
	        	else{
	        		 index = mid;
	        		 break;
	        	}
	           
	        }
	        else if(b > num){
	            start = mid+1;
	        }
	        else{
	            end = mid-1;
	        }
	    }
	    
	    return index;
	}
	
private int getEnd(ArrayList<Integer> a, int b){
		
		int start = 0;
	    int end = a.size()-1;
	    int index = 0;
	    while(start <= end){
	        
	        int mid = start + (end-start)/2;
	        
	        int num = a.get(mid);
	        if(num == b){
	        	if(mid < a.size()-1 && a.get(mid+1) == num ){
	        		start = mid+1;
	        	}
	        	else{
	        		 index = mid;
	        		 break;
	        	}
	           
	        }
	        else if(b > num){
	            start = mid+1;
	        }
	        else{
	            end = mid-1;
	        }
	    }
	    
	    return index;
	}
	
	
	private boolean isPresent(ArrayList<Integer> a, int b){
	    
	    int start = 0;
	    int end = a.size()-1;
	    
	    while(start <= end){
	        
	        int mid = start + (end-start)/2;
	        
	        int num = a.get(mid);
	        if(num == b){
	            return true;
	        }
	        else if(b > num){
	            start = mid+1;
	        }
	        else{
	            end = mid-1;
	        }
	    }
	    
	    return false;
	}

}
