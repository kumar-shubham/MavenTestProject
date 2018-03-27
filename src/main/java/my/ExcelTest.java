package my;

public class ExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(toName(27));

	}
	
	public static String toName(int number) {
        StringBuilder sb = new StringBuilder();
        while (number-- > 0) {
            sb.append((char)('A' + (number % 26)));
            number /= 26;
        }
        return sb.reverse().toString();
    }

}
