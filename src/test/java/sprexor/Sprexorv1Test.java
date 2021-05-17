package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v1.Sprexor;

public class Sprexorv1Test {
	@Test
	public void test() {
		Sprexor s = new Sprexor();
		Scanner sc = new Scanner(System.in);
		s.include(Sprexor.LIST);
		try {
			s.activate();
			String l;
			while(true) {
				l = sc.nextLine();
				if(l.trim().contentEquals("exit"))break;
				s.exec(l);
			}
		} catch (SprexorException se) {
			se.printStackTrace();
		}
		sc.close();
	}
}
