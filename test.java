import sprexor.*;
import sprexor.cosmos.BasicPackages;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Sprexor n = new Sprexor();
		n.register("add", new CommandProvider() {
			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				int sum = 0;
				for(String s : args) {
					sum += Integer.parseInt(s);
				}
				scope.putData("1_", sum);
				return sum;
			}
			
			@Override
			public Object error(Exception e) {
				return e;
			}
		}, "add number");
		
		n.register("sum", new CommandProvider() {
			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				String a = "";
				int c = 0;
				n.call("entry_on"); // It will be enter the EntryMode.
				for(String str : args) {
					a += str + " : " + c + "\n";
					c ++;
				}
				n.send("sum running...", IOCenter.CMT);
				return a;
			}
			@Override
			public Object EntryMode(String msg) {
				
				return msg;
			}
		}, "view detail");
		
		n.importSprex(BasicPackages.call());
		n.activate();
		
		IOCenter i = new IOCenter(n);
		try {
			while(true) {
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.exec(ss);
				for(Object[] o : i.getBlockMessage()) {
					System.out.println(o[0] + " - " + o[1]);
				}
			}
		} catch (CommandNotFoundException | SprexorException e) {
			System.out.println(e);
		}
	}
}