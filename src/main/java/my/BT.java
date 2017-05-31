package my;

import java.util.ArrayList;

public class BT {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BT b = new BT();

		
		int a = 3;
        ArrayList<String> result = generateParenthesis(a);
        System.out.println("*********Result*******");
        for(String s: result)
            System.out.println(s);


	}

	public static ArrayList<String> generateParenthesis(int a) {                
        ArrayList<String> result = new ArrayList<String>();
        for(int i =1; i <= a; i++)
            Parenth("", 0, 0, i, result, a);
        
        return result;
    }
    private static void Parenth(String output, int open, int close, int pairs, ArrayList<String> result, int a){
        if(open == pairs && close == pairs && output.length() == 2*a){
            result.add(output);
        }
        else{
            if(open < pairs){
                Parenth(output + "(", open + 1, close, pairs, result, a);
            }
            if(close < open){
                Parenth(output + ")", open, close + 1, pairs, result, a);
            }
        }
    }

}
