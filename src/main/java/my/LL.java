package my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class LL {

	public static void main(String[] args) {
		Stack stack = new Stack();
		// TODO Auto-generated method stub

		LL l = new LL();

		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(36,46,26,92,73,42,89,67,70,52,27,68,84,50,31,32,47,16,63,101,64,77,44,96,76,4,58,30,8,33,43,22,79,13,51,82,6));

		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1,1,2,3,4,5));

		ListNode root = l.create(list1);
		//		l.printList(root);
		//		ListNode ans = l.reverseBetween(root, 13, 15);
		//		l.printList(ans);

//		ListNode ans = l.removeNthFromEnd(root, 82);
//		l.printList(ans);
		ListNode ans = l.rotateRight(root, 2);
		l.printList(ans);

	}

	public ListNode rotateRight(ListNode a, int b) {

		if(a == null){
	        return null;
	    }
	    
	    ListNode temp1 = a;
	    ListNode temp2 = a;
	    ListNode ans = a;
	    int count = 0;
	    while(count < b){
	       temp1 = temp1.next;
	       if(temp1 == null){
	           temp1 = a;
	       }
	       count++;
	    }
	    
	    while(temp1.next != null){
	        temp1 = temp1.next;
	        temp2 = temp2.next;
	    }
	    System.out.println(temp1.val);
	    temp1.next = a;
	    if(temp2.next != null){
	        ans = temp2.next;
	    }
	    temp2.next = null;
	    
	    return ans;

	}

	public ListNode removeNthFromEnd(ListNode a, int b) {

		if(a == null ){
			return a;
		}
		if(a.next == null && b == 1){
			return null;
		}

		ListNode temp1 = a;
		ListNode temp2 = a;
		ListNode prev = a;
		int count = 0;
		while(temp1 != null){
			if(count < b){
				System.out.println(temp1.val);

				temp1 = temp1.next;
				if(temp1 == null){
					return a.next;
				}
			}
			else{
				prev = temp2;
				temp1 = temp1.next;
				temp2 = temp2.next;
			}
			count++;
		}

		prev.next = temp2.next;

		return a;


	}


	public ListNode reverseBetween(ListNode a, int m, int n) {

		ListNode prev = null;
		ListNode current = a;
		ListNode next = null;
		ListNode pivot = null;
		ListNode root = a;
		int i = 0;
		while(current != null && i<n){

			if(i>=m-1){
				next = current.next;
				current.next = prev;
				prev = current;
				current = next;
			}
			else{
				pivot = current;
				current = current.next;
			}
			i++;
		}
		a = prev;
		ListNode temp1 = a;
		while(temp1 != null){
			if(temp1.next == null){
				temp1.next = current;
				break;
			}
			temp1 = temp1.next;
		}
		if(pivot != null){
			pivot.next = a;
		}
		else{
			pivot = a;
			root = a;
		}

		return root;
	}


	public void printList(ListNode root){

		ListNode temp = root;
		while(temp != null){
			System.out.print(temp.val + " -> ");
			temp = temp.next;
		}
		System.out.println("null");
	}

	public ListNode create(List<Integer> list){
		int i = 0;
		ListNode root = null;
		ListNode current = null;
		for(Integer num: list){
			i++;
			if(i>1){
				ListNode node = new ListNode(num);

				if(root == null){
					root = node;
				}
				else{
					current.next = node;
				}
				current = node;
			}
		}
		return root;
	}

}
