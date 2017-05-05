package my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LL {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LL l = new LL();

		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(36,46,26,92,73,42,89,67,70,52,27,68,84,50,31,32,47,16,63,101,64,77,44,96,76,4,58,30,8,33,43,22,79,13,51,82,6));

		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(20,97,63,89,34,82,95,4,70,14,41,38,83,49,32,68,56,99,52,33,54));

		ListNode root = l.create(list1);
		l.printList(root);
		ListNode ans = l.reverseBetween(root, 13, 15);
		l.printList(ans);

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
