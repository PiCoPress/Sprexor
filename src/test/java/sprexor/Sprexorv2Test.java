package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v2.cosmos.Std;
import sprexor.v2.cosmos.utils.CTools;
import sprexor.v2.SManager;

public class Sprexorv2Test {
    @Test
    public void t() {
        Scanner s = new Scanner(System.in);
		SManager n = new SManager("C:\\Dev");
		n.use(Std.class, CTools.class);
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
