import sprexor.*;
import sprexor.cosmos.BasicPackages;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Sprexor n = new Sprexor();
		n.importSprex(new BasicPackages());
		
		n.bound(new Sprexor.dec(){
			@Override
			public String notfound(String id) { // Redefine command_not_found error and "out" method.
				return "This command is not usable : " + id;
			}
			@Override
			public void out(String msg, IOCenter.TYPE ii) { // This method will be invoked when print any message.
				if(ii.equals(IOCenter.ERR)) System.err.println(msg);
				else System.out.println(msg);
			}
		});
		n.activate(); // activate
		try {
			while(true) {
				System.out.print("root@test-pc:~$ ");
				String ss = s.nextLine();
				if(ss.trim().contentEquals("exit"))break;
				n.exec(ss);
			}
		} catch (CommandNotFoundException | SprexorException e) {
			System.out.println(e);
		}
	}
}