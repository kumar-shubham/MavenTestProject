package my;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;

public class Test3 {

	public static void main(String ars[]) {
		
		MutableBoolean b = new MutableBoolean(true);
		Test3 t3 = new Test3();
		t3.check1(b);
		t3.check(b);
		
		
		
	}
	
	void check1(MutableBoolean b) {
		b.setValue(false);
		System.out.println(b);
	}
	
	
	void check(MutableBoolean b) {
		
		System.out.println(b);
	}
	
}



