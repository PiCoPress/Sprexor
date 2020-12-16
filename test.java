import sprexor.*;
import sprexor.cosmos.BasicPackages;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Sprexor n = new Sprexor();
		n.register("add", new CommandProvider() {
			@Override
			public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope) {
				int sum = 0;
				for(String s : args) {
					sum += Integer.parseInt(s);
				}
				scope.putData("1_", sum);
				return new IOCenter(sum + "", IOCenter.STDOUT);
			}
			
			@Override
			public Object error(Exception e) {
				return e;
			}
		}, "add number");
		
		n.register("optprs", new CommandProvider() {

			@Override
			public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope) {
				
				return null;
			}
			
		}, "Option parser test.");
		
		n.register("sum", new CommandProvider() {
			@Override
			public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope) {
				String a = "";
				int c = 0;
				n.call("entry_on"); // It will be enter the EntryMode.
				for(String str : args) {
					a += str + " : " + c + "\n";
					c ++;
				}
				n.send("sum running...", IOCenter.CMT);
				return null;
			}
			@Override
			public Object EntryMode(String msg) {
				
				return msg;
			}
		}, "view detail");
		
		n.importSprex(new BasicPackages());
		
		n.bound(new Sprexor.dec(){
			@Override
			public String notfound(String id) { // redefine command_not_found error and "out" method.
				return "This command is not usable : " + id;
			}
			@Override
			public void out(String msg, IOCenter.TYPE ii) {
				if(ii.equals(IOCenter.ERR)) System.err.println(msg);
				else System.out.println(msg);
			}
		});
		
		n.activate(); // activate

		IOCenter i = new IOCenter(n);
		try {
			while(true) {
				System.out.print("root@test-pc:~$ ");
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.exec(ss);
				/*for(Object[] o : i.getBlockMessage()) {
					System.out.println(o[0] + " - " + o[1]);
				}*/
			}
		} catch (CommandNotFoundException | SprexorException e) {
			System.out.println(e);
		}
	}
}