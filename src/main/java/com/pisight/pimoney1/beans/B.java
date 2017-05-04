package com.pisight.pimoney1.beans;

import java.util.ArrayList;
import java.util.Arrays;

public class B {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		B b = new B();
		
		ArrayList<Integer> a = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
		ArrayList<Integer> c = new ArrayList<Integer>(Arrays.asList(1, 2, 2, 3, 4));
		
		System.out.println(b.diffPossible(c, 0));

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
