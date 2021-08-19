package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v1.*;

public class ST {
	@Test
	public void test() {
		Core s = new Core();
		Scanner n = new Scanner(System.in);
		try {
			s.activate();
			String str;
			while(true) {
				str = n.nextLine();
				if(str.trim().contentEquals("exit"))break;
				s.exec(str);
			}
		} catch (CommandNotFoundException se) {
			se.printStackTrace();
		}
		n.close();
	}
}
