package my;

import java.util.Stack;

public class Stack1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Stack1 s = new Stack1();
		
		
	}
	
	public ListNode addTwoNumbers(ListNode a, ListNode b) {
	    
		 if(a == null){
		        return b;
		    }
		    if(b == null){
		        return a;
		    }
		    
		    boolean carry = false;
		    
		    ListNode temp1 = a;
		    ListNode temp2 = b;
		    ListNode prev = a;
		    while(temp1 != null && temp2 != null){
		        int x = temp1.val;
		        int y = temp2.val;
		        
		        int val = x+y;
		        if(carry){
		            val++;
		        }
		        if(val>9){
		            carry = true;
		            temp1.val = val%10;
		        }
		        else{
		            carry = false;
		            temp1.val = val;
		        }
		        prev = temp1;
		        temp1 = temp1.next;
		        temp2 = temp2.next;
		    }
		    
		    if(temp2 != null){
		        prev.next = temp2;
		        temp1 = prev;
		    }
		    
		    while(temp1 != null){
		        int x = temp1.val;
		        if(carry){
		            x++;
		        }
		        if(x>9){
		            carry = true;
		            temp1.val = x%10;
		        }
		        else{
		            carry = false;
		            temp1.val = x;
		        }
		        prev = temp1;
		        temp1 = temp1.next;
		    }
		    
		    if(carry){
		        ListNode node = new ListNode(1);
		        prev.next = node;
		        
		    }
		    
		    
		    return a;
	    
	}
	
	

}
