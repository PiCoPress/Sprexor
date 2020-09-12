import nere.*;

public class test {
	public static void main(String[] args) {
		Nere n = new Nere();
		n.mkcmd("add", new CommandProvider() {
			@Override
			public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) {
				int sum = 0;
				for(String s : args) {
					sum += Integer.parseInt(s);
				}
				gd.putData("1", sum);
				return sum;
			}
			
			@Override
			public Object ErrorEventListener(Exception e) {
				return e;
			}
		}, "add number");
		
		n.mkcmd("sum", new CommandProvider() {
			@Override
			public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) {
				String a = "";
				int c = 0;
				for(String str : args) {
					a += str + " : " + c + "\n";
					c ++;
				}
				return a;
			}
		}, "view detail");
		n.initRegistry();
		
		IOCenter i = new IOCenter(n);
		try {
			//n.useSyntax(false);
			n.exec("add 12 1 1 11");
			System.out.println(i.getMessage()[0]);
			n.exec("var a 1444 ;var b 6");
			System.out.println(i.getMessage()[0]);
			n.exec("sum 1 21 @a @b;add @a @b");
		} catch (CommandNotFoundException e) {
			System.out.println(e);
		}
		System.out.println(i.getMessage()[0]);
		System.out.println("----\n" + i.getOutput().get(1)[0]);
	}
}