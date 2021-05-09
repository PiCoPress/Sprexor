package sprexor;

import java.util.Scanner;
import org.junit.Test;

import sprexor.v2.cosmos.BasicPackages;
import sprexor.v2.SManager;

public class Sprexorv2Test {
    @Test
    public void shouldAnswerWithTrue() {
        Scanner s = new Scanner(System.in);
		SManager n = new SManager();
		n.use(BasicPackages.class);
		n.setup();
		try {
			while(true) {
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.run(ss, "USE_VARIABLE;USE_COMMENT;WRAP_NAME;DEBUG");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.close();
    }
}
