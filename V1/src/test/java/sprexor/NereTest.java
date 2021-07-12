package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v1.*;

public class NereTest {
	@Test
	public void test() {
		Core s = new Core();
		Scanner n = new Scanner(System.in);
		IOCenter io = new IOCenter(s);
		s.initScope();
		try {
			s.activate();
			String str;
			while(true) {
				str = n.nextLine();
				if(str.trim().contentEquals("exit"))break;
				s.exec(str);
				System.out.println(io.getMessage()[0]);
			}
		} catch (CommandNotFoundException se) {
			se.printStackTrace();
		}
		n.close();
	}
}
