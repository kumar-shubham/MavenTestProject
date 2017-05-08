package my;

import java.util.Stack;

public class Stack1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Stack1 s = new Stack1();
		
		s.pop();
		s.push(644643544);
		System.out.println(s.stack);
		System.out.println(s.getMin());
		System.out.println(s.stack);
		System.out.println(s.top());
		System.out.println(s.stack);
		System.out.println(s.top());
		System.out.println(s.stack);
		System.out.println(s.top());

	}
	
	Stack<Integer> stack = new Stack<Integer>();
    Stack<Integer> aux = new Stack<Integer>();
    
    public void push(int x) {
        stack.push(x);
        int top = Integer.MAX_VALUE;
        if(!aux.isEmpty()){
            top = aux.peek();
        }
        if(x < top){
            aux.push(x);
        }
        else{
            aux.push(top);
        }
    }

    public void pop() {
        if(!stack.isEmpty()){
            stack.pop();
            aux.pop();
        }
        
    }

    public int top() {
    	System.out.println(stack.isEmpty());
        if(!stack.isEmpty()){
            stack.peek();
        }
        return -1;
    }

    public int getMin() {
        if(!aux.isEmpty()){
            return aux.peek();
        }
        return -1;
    }

}
