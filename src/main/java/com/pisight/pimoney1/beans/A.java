package com.pisight.pimoney1.beans;

public class A {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		A a = new A();
		
		System.out.println(a.pow(71045970, 41535484, 64735492));

	}
	
	
	public int pow(int x, int n, int d) {
	    
//		    System.out.println(power(x, n,d));
		    Double result = power(x,n, d);
		    
		    if(result<0){
		        result = result + d;
		        return result.intValue();
		    }
		    else{
		        return result.intValue();
		    }
		    
		}
		
		public Double power(int x, int n, int d){
		    
		    Double temp;
		    
		    if(n == 0){
		        return new Double(1);
		    }
		    temp = power(x, n/2, d);
		    System.out.println(temp.intValue());
		    if(n%2 == 0){
		        return ((temp%d)*(temp%d))%d;
		    }
		    else{
		        if(n>0){
		            return ((x%d)*(temp%d)*(temp%d))%d;
		        }
		        else{
		            return ((temp%d)*(temp%d)/x)%d;
		        }
		    }
		}

}
