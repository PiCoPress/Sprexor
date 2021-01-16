import sprexor.*;
import sprexor.cosmos.BasicPackages;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Sprexor n = new Sprexor();
		n.importSprex(new BasicPackages());
		n.register("ee", new CommandProvider() {
			
			@Override
			public int code(IOCenter io) {
				Component args = io.getComponent();
				if(args.isEmpty())io.out.build().add("73\n").send();
				io.in.delimiter = ';';
				io.in.prompt(">");
				io.out.println(io.in.flush());
				return 0;
			}
			
		}, "");
		try {
			n.activate();
		} catch (SprexorException e1) {
			e1.printStackTrace();
		} // activate
		try {
			while(true) {
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.exec(ss);
			}
		} catch (SprexorException e) {
			e.printStackTrace();
		}
		s.close();
	}
}