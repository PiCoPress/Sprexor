package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v0.CommandNotFoundException;
import sprexor.v0.IOCenter;
import sprexor.v0.Nere;

public class Nerev0Test {
	@Test
	public void test() {
		Nere s = new Nere();
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
