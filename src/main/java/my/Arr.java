package my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Arr {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Arr a = new Arr();
		ArrayList<Integer> A = new ArrayList<Integer>(Arrays.asList(-1, -7, 5, 7, -10, 8, 1, -1, -1, -3, -2, 2, 5, -7, -6, -1, 0, -8, -10, -9, -1, -4, 2, -9, -8, -10, 7, -7, -9, -9, -1, 0, -5, 6, -3, 7, 4, 0, -4, -6, 7, 4, -2, -5, 8, 2, -4, -10, -4, -4, 4, 6, 2, 8, -1, -4, 0, -3, 0, 1, -10, 1, 3, 7, -3, 2, -4, 4, 5, 2, 0, 2, 9, 0, -1, -1, 4, 5, -9, -10, 3, -3, -2, 8, -4, 0));
		
		System.out.println(a.solve(A));

	}
	
	
public int solve(ArrayList<Integer> A) {
        
        if(A == null || A.size() == 0){
            return -1;
        }
        
        Collections.sort(A);
        
        System.out.println(A);
        
        int prev = A.get(A.size()-1);
        if(prev == 0){
        	return 1;
        }
        for(int i = 1; i<A.size();i++){
            int num = A.get(A.size()-1-i);
            System.out.println(i + " : " + (A.size()-1-i) + " : " + num + " : " + prev);
            if(i == num){
                if(num != prev){
                    return 1;
                }
            }
            else if(i > num){
                int count = i;
                for(int j = A.size()-i; j<A.size();j++){
                    if(num == A.get(j)){
                        count--;
                    }
                    else{
                        if(count == num){
                            return 1;
                        }
                        else{
                            continue;
                        }
                    }
                }
            }
            prev = num;
        }
        
        
        return -1;
    }

}
