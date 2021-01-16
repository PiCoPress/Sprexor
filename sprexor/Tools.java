package sprexor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.Vector;

public class Tools{
	
	private static boolean isOnlyEnglish(String a) {
		String[] arr = a.split("");
		for(String str : arr) {
			if(!str.matches("[a-zA-Z]"))return false;
			
		}
		return true;
	}
	
	public static final String VOID = "";
	public static String Processor(String opt) {
		String res = "";
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(opt);
			InputStream in = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				res += line;
			}
			in.close();
		}catch(IOException e) {
			res = e + "";
		}
		return res;
	}
	
	public static byte AnalOption(String str, boolean[] isWrapped) {
		if(str.startsWith("--") && !isWrapped[1]) {
			if(isOnlyEnglish(str.substring(2)))
				return 2;
		}else if(str.startsWith("-") && !isWrapped[1]) {
			if(isOnlyEnglish(str.substring(1)))
				return 1;
		}
		return 0;
	}
	
	public static boolean OptionPrs(String compare, String to, byte i)throws Exception {
		if(i == 1) {
			to = to.substring(1);
			if(compare.length() != to.length())return false;
			for(int j = 0; j < to.length(); j ++) {
				if(compare.indexOf(to.split("")[j]) == -1)return false;
			}
			return true;
		}else if(i == 2) {
			to = to.substring(2);
			if(compare == to)return true;
		}else {
			throw new Exception(to + ":" + i + "# is not allowed token.");
		}
		return false;
	}
	
	public static String[] binder(String[] a, int start) {
		Vector<String> v = new Vector<String>();
		String tmp = "";
		for(int i = 0; i < a.length; i ++) {
			if(i >= start) {
				tmp += a[i];
			}else {
				v.add(a[i]);
			}
		}
		String[] strarr = new String[v.size()];
		int i = 0;
		for(Object obj : v.toArray()) {
			strarr[i ++] = obj.toString();
		}
		if(tmp.trim().isEmpty())return strarr;
		v.add(tmp);
		return strarr;
	}
	
	public static CommandFactory[] toCFClass(CommandFactory  ... cp) {
		return cp;
	}
	
	public static String arg2String(String[] args) {
		String tmp = "";
		for(String str : args) tmp += str;
		return tmp;
	}
	
	public static String arg2String(String[] args, String ch) {
		String tmp = "";
		for(String str : args) tmp += str + ch;
		return tmp.substring(0 , tmp.length() - 1);
	}
	
	public static String[] excludeArr(String[] arg, int a) {
		String[] tmp = {"",};
		int le = arg.length;
		int count = 0;
		
		for(int i = 0; i < le; i ++) {
			if(a == i) continue;
			tmp[count] = arg[i];
			count ++;
		}
		if(arg.length <= 1)tmp[0] = arg[0];
		return tmp;
	}
	
	public static String[] cutArr(String[] arr, int startIndex) {
		if(startIndex == 0)return arr;
		String[] tmp = new String[arr.length - startIndex];
		for(int i = startIndex; i < arr.length; i ++) {
			tmp[i - startIndex] = arr[i];
		}
		return tmp;
	}
	/*
	public static void smooth(String[] all, String[] optList, Class cl) throws NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int la = all.length, lo = optList.length;
		for(int i = 0; i < la; i ++) {
			for(int j = 0; j < lo; j ++) {
				if(all[i].contentEquals(optList[j])) {
					Method[] aa = cl.getMethods();
					for(int k = 0; k < aa.length; k ++) if(aa[k].getName().contentEquals("option")) aa[k].invoke(null, optList[j]);
				}
			}
			if(ch == ']') {
				toFront = true;
				toIndex = cursorIndex;
				cursorIndex --;
				open = false;
				continue;
			}
			st.push(ch);
			if(ch == '[') open = true;
			cursorIndex ++;
			if(!open) result += ch;
		}
		return result;
	}*/
	/**
	 * 
	 * since 0.2.18
	 */
	public static class ByteStack {
		private int size = 1024;
		private int cursor = 0;
		private byte[] head;
		private byte[][] elements;
		
		public ByteStack(int k) { elements = new byte[1024][k]; }
		public void push(byte[] t) {
			head = t;
			elements[cursor ++ ] = t;
		}
		public Object top() {
			return head;
		}
		public void pop() {
			head = elements[-- cursor];
		}
		public boolean empty() {
			return cursor == 0;
		}
		public int getSize() {
			return size;
		}
		public int current() {
			return cursor;
		}
		public byte[][] getBytes() {
			return elements;
		}
	}
	public static String SMT_FORM(String v) {
		if(v.length() <= 1 || v.indexOf("]") == -1)return v;
		Stack<Character> st = new Stack<Character>();
		char[] Characters = v.toCharArray();
		int cursorIndex = 0,
				toIndex = 0,
				count = 0,
				leng = v.length();
		char ch;
		String buffer = "",
				result = "";
		boolean toFront = false,
				open = false;;
		while(v.length() > cursorIndex) {
			ch = Characters[cursorIndex];
			if((count ++) > leng * 3 + 100) {
				System.out.println("stopped.");
				break;
			}
			if(toFront) {
				if(ch != '[') {
					buffer = st.peek().toString() + buffer;
					st.pop();
					cursorIndex --;
					continue;
				}else {
					if(buffer.matches("^[^nt]+[^nt]*$")) {
						result += "[" + buffer + "]";
						buffer = "";
						toFront = false;
						cursorIndex = toIndex;
						cursorIndex ++;
						continue;
					}
					toFront = false;
					cursorIndex = toIndex;
					result += buffer.replaceAll("n", "\n").replaceAll("t", "\t");
					buffer = "";
					cursorIndex ++;
					continue;
				}
			}
			if(ch == ']') {
				toFront = true;
				toIndex = cursorIndex;
				cursorIndex --;
				open = false;
				continue;
			}
			st.push(ch);
			if(ch == '[') open = true;
			cursorIndex ++;
			if(!open) result += ch;
		}
		return result;
	}
	//
	protected class s_label {
		String value;
		boolean first = true;
		s_label(){
			value = "STRINGLABEL:";
		}
		public void add(String l) {
			if(first) {
				first =  false;
				value += l;
			}else value += "/" + l.toUpperCase();
		}
		public String get(String l) {
			l = l.toUpperCase();
			String[] a = value.split(":")[1].split("/");
			for(String s : a) {
				if(l.contentEquals(s)) return l;
			}
			return null;
		}
		public boolean inc(String l) {
			return l.contains(l.toUpperCase());
		}
	}
}
