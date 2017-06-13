package my;

import java.util.ArrayList;
import java.util.Arrays;

public class Sudoku {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Sudoku s = new Sudoku();
		
//		ArrayList<ArrayList<Character>> a = new ArrayList<ArrayList<Character>>(Arrays.asList("53..7....", "6..195...", ".98....6.", "8...6...3", "4..8.3..1", "7...2...6", ".6....28.", "...419..5", "....8..79"));
//		
//		for(int i = 0; i< 9; i++){
//			ArrayList<Character> temp = new ArrayList<Character>();
//			
//		}

	}
	
	
public void solveSudoku(ArrayList<ArrayList<Character>> a) {
	    
	    solve(0, 0, a);
	    
	}
	
	
	private void solve(int i, int j, ArrayList<ArrayList<Character>> a){
	    
	    if(i == 9){
	        i = 0;
	        if(++j == 9){
	            return;
	        }
	    }
	    
	    if(a.get(i).get(j) != '.'){
	         solve(i+1, j, a);
	         
	         return;
	    }
	    
	    for(int val = 1; val<=9; val++){
	        if(isValid(i, j, val, a)){
	            a.get(i).set(j, Character.forDigit(val, 10));
	            solve(i+1, j, a);
	            return;
	        }
	    }
	    
	   // a.get(i).set(j, '.');
	    
	}
	
	
	private boolean isValid(int i, int j, int val, ArrayList<ArrayList<Character>> a){
	    
	    // col
	    for(int k = 0; k<9; k++){
	        if(val == Character.getNumericValue(a.get(i).get(k))){
	            return false;
	        }
	    }
	    
	    // row
	    for(int k = 0; k<9; k++){
	        if(val == Character.getNumericValue(a.get(k).get(j))){
	            return false;
	        }
	    }
	    
	    int boxRowOffset = (i/3)*3;
	    int boxColOffset = (j/3)*3;
	    
	    // square
	    for (int k = 0; k < 3; ++k){
            for (int m = 0; m < 3; ++m){
                if (val == Character.getNumericValue(a.get(boxRowOffset+k).get(boxColOffset+m))){
                    return false;
                }
            }
	    }

        // no violations, so it's legal
        return true; 
	}

}
