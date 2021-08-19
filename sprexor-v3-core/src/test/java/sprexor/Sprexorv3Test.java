package sprexor;

import java.util.Scanner;
import org.junit.Test;
import sprexor.v3.cosmos.Std;
import sprexor.v3.cosmos.utils.CTools;
import sprexor.v3.SManager;

public class Sprexorv3Test {
    @SuppressWarnings("unchecked")
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
				n.run(ss, "USE_VARIABLE;WRAP_NAME;DEBUG");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.close();
    }
}
