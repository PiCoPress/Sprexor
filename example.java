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
				return sum;
			}
		}, "add numbers\n11");
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
		}, "lol");
		IOCenter i = new IOCenter(n);
		try {
			n.exec("var a 1444");
			System.out.println(i.getMessage()[0]);
			n.exec("sum @a");
		} catch (unknownCommand e) {
			System.out.println(e);
		}
		System.out.println(i.getMessage()[0]);
	}
}
