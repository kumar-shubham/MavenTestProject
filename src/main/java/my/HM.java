package my;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HM h = new HM();

		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(-5, 1, 4, -7, 10, -7, 0, 7, 3, 0, -2, -5, -3, -6, 4, -7, -8, 0, 4, 9, 4, 1, -8, -6, -6, 0, -9, 5, 3, -9, -5, -9, 6, 3, 8, -10, 1, -2, 2, 1, -9, 2, -3, 9, 9, -10, 0, -9, -2, 7, 0, -4, -3, 1, 6, -3));

		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(4, 7, -4, 2, 2, 2, 3, -5, -3, 9, -4, 9, -7, 7, -1, 9, 9, 4, 1, -4, -2, 3, -3, -5, 4, -7, 7, 9, -4, 4, -8));

		ArrayList<String> list2 = new ArrayList<String>(Arrays.asList("....5..1.", ".4.3.....", ".....3..1", "8......2.", "..2.7....", ".15......", ".....2...", ".2.9.....", "..4......"));


		//		System.out.println(h.twoSum(list1, 0));

//		System.out.println(h.isValidSudoku(list2));
		
		System.out.println(h.lengthOfLongestSubstring("dadbc"));
	}


	public int lengthOfLongestSubstring(String a) {


		HashMap<Character, Integer> map = new HashMap<Character, Integer>();

		String maxStr = "";
		String subStr = "";

		for(int i = 0; i<a.length();i++){

			char c = a.charAt(i);

			if(map.containsKey(c)){
				int temp = i;
				i = map.get(c);
				map = new HashMap<Character, Integer>();
//				map.put(c, temp);
//				subStr = c+"";
				subStr = "";
			}
			else{
				subStr += c;
				map.put(c, i);
			}
//			System.out.println(map);
			System.out.println(i + " : " + c + " : " + subStr + " : " + maxStr);
			if(subStr.length() > maxStr.length()){
				maxStr = subStr;
			}
		}

		return maxStr.length();
	}


	public int isValidSudoku(final List<String> a) {

		HashMap<Integer, HashMap<Integer, Character>> vMap = new HashMap<Integer, HashMap<Integer, Character>>();
		HashMap<Integer, HashMap<Integer, Character>> hMap = new HashMap<Integer, HashMap<Integer, Character>>();
		HashMap<Integer, HashMap<Integer, Character>> sMap = new HashMap<Integer, HashMap<Integer, Character>>();

		for(int i = 0; i<a.size(); i++){

			vMap.put(i, new HashMap<Integer, Character>());
			hMap.put(i, new HashMap<Integer, Character>());
			sMap.put(i, new HashMap<Integer, Character>());
		}



		for(int i = 0; i<a.size(); i++){

			String str = a.get(i);

			for(int j = 0; j<a.size(); j++){

				char c = str.charAt(j);

				if(c != '.'){
					int val = Character.getNumericValue(c);

					if(vMap.get(j).containsKey(val)){
						return 0;
					}
					else{
						vMap.get(j).put(val, c);
					}


					if(hMap.get(i).containsKey(val)){
						return 0;
					}
					else{
						hMap.get(i).put(val, c);
					}

					int index = (3 * (i/3)) + j/3;
					System.out.println(i + " : " + j + " : " + index);
					if(sMap.get(index).containsKey(val)){
						return 0;
					}
					else{
						sMap.get(index).put(val, c);
					}
				}
			}	        

		}


		return 1;


	}

	public ArrayList<Integer> twoSum(final List<Integer> a, int b) {

		ArrayList<Integer> result = new ArrayList<Integer>();

		ArrayList<Integer> list = null;

		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();

		int index1 = -1;
		int index2 = -1;

		for(int i = 1; i<a.size();i++){
			if(!map.containsKey(b-a.get(i))){
				list = new ArrayList<Integer>();
			}
			else{
				list = map.get(b-a.get(i));
			}
			list.add(i);
			map.put(b-a.get(i), list);
		}

		//		System.out.println(map);
		for(int i = 0; i<a.size()-1;i++){
			if(map.containsKey(a.get(i))){
				//				System.out.println(i + " : " + a.get(i)  + " : : " + map.get(a.get(i)) + " : " + a.get(map.get(a.get(i))));
				if(index1 == -1){
					index1 = i;
					index2 = map.get(a.get(i)).get(0);
				}

				list = map.get(a.get(i));
				for(Integer num: list){
					if(num < index2 && i < num){
						index1 = i;
						index2 = num;
					}
				}

				//				if(map.get(a.get(i)) < index2 && i < map.get(a.get(i))){
				//					
				////					if(index2 == map.get(a.get(i)) && i < index2 )
				//					index1 = i;
				//					index2 = map.get(a.get(i));
				//					System.out.println(index1 + " < " + index2);
				//				}
			}
		}

		if(index1 != -1){

			result.add(index1+1);
			result.add(index2+1);
		}

		return result;
	}

}
