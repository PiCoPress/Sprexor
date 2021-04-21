import sprexor.*;
import sprexor.cosmos.BasicPackages;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Sprexor n = new Sprexor();
		n.importSprex(new BasicPackages());
		n.include(Sprexor.LIST);
		n.register("ex", new CommandProvider() {
			
			private char prog(int i) {
				return switch(i % 4) {
				case 0 -> '|';
				case 1 -> '/';
				case 2 -> '—';
				case 3 -> '\\';
				default -> '\0';
				};
			}
			
			@Override
			public int code(IOCenter io) {
				try {
					for(int i = 0; i < 30; i ++) {
						io.out.printf("%c turning dash...\r", prog(i));
						Thread.sleep(100);
					}
				} catch(Exception e) {
				}
				String[] s=new String[1];
				io.out.println("finished.      ");
				return 0;
			}}, "");
		try {
			n.activate();
		} catch (SprexorException e1) {
			e1.printStackTrace();
		} // activate
		try {
			while(true) {
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.run(ss, "USE_VARIABLE;USE_COMMENT;WRAP_NAME");
			}
		} catch (SprexorException e) {
			e.printStackTrace();
		}
		s.close();
	}
}