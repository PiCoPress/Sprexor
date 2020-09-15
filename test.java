import nere.*;

import nere.loadNere.BasicPackages;

public class test {
	public static void main(String[] args) {
		Nere n = new Nere();
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
				for(String str : args) {
					a += str + " : " + c + "\n";
					c ++;
				}
				n.send("sum running...", IOCenter.CMT);
				return a;
			}
		}, "view detail");
		
		n.importNere(BasicPackages.call());
		n.activate();
		
		IOCenter i = new IOCenter(n);
		try {
			//n.useSyntax(false);
			n.exec("add 12 1 1 11");
			System.out.println(i.getMessage()[0]);
			n.exec("var a 1444 ;var b 6");
			System.out.println(i.getMessage()[0]);
			n.exec("sum 1 21 @a @b;add @a @b");
			n.exec("help example");
		} catch (CommandNotFoundException e) {
			System.out.println(e);
		}
		System.out.println(i.getMessage()[0] + "\n--------\n");
		
		for(Object[] o : i.getOutput()) {
			System.out.println(o[0]);
		}
	}
}