package my;

public class Student implements Comparable<Student> {

	public int age;
	
	public int marks;
	
	@Override
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		System.out.println("inside comapreTo");
		
		return this.age - o.age;
	}
	
	public String toString() {
		return this.age+"";
	}

}
