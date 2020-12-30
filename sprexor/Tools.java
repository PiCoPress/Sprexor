package sprexor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.Vector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Tools{
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	/**
	 * This method binds Target array from the startIndex, and binded value be a last unit of returning array.
	 * <br> Other Units are not modified or moved.
	 * @param a - Target array.
	 * @param start - The Integer for bind above array.
	 * @return String array
	 */
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
	/**
	 * @param cf - The Variable Argument.
	 * @return CommandFactory array.
	 */
	public static CommandFactory[] toCFClasses(CommandFactory  ... cf) {
		return cf;
	}
	/**
	 * It can Transform String Array to String.
	 * @param args - Target String array.
	 * @return String.
	 */
	public static String arg2String(String[] args) {
		String tmp = "";
		for(String str : args) tmp += str;
		return tmp;
	}
	/**
	 * It can Transform String Array to String by inserting ch.
	 * @param args - Target String array.
	 * @param ch - Insert between above parameter with.
	 * @return String
	 */
	public static String arg2String(String[] args, String ch) {
		String tmp = "";
		for(String str : args) tmp += str + ch;
		return tmp.substring(0 , tmp.length() - 1);
	}
	/**
	 * Exclude a unit of array.
	 * @param arg - Target Array.
	 * @param a - To exclude a index.
	 * @return String Array.
	 */
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
	/**
	 * The value(s) that smaller than startIndex cut away, and other will be returned.
	 * @param arr
	 * @param startIndex
	 * @return String array.
	 */
	public static String[] cutArr(String[] arr, int startIndex) {
		if(startIndex == 0 || arr.length - startIndex <= 0)return arr;
		String[] tmp = new String[arr.length - startIndex];
		for(int i = startIndex; i < arr.length; i ++) {
			tmp[i - startIndex] = arr[i];
		}
		return tmp;
	}
	/**
	 * @deprecated
	 * @param all
	 * @param optList
	 * @param cl
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Deprecated
	public static void smooth(String[] all, String[] optList, Class cl) throws NoSuchMethodException,
	IllegalAccessException, InvocationTargetException {
		Method me = cl.getDeclaredMethod("Option", String.class);
		boolean mod = false;
		for(String st : all) {
			for(String opt : optList) {
				if(opt.contentEquals(st)) { mod = true; continue; }
				if(mod) { mod = false; me.invoke(null, opt, st); }
			}
		}
	}
	/**
	 * Convert the certain tag to any character. It is used to make template of Helping message.
	 * <br>
	 * <br>The text that formed only n or t in square parenthesis [] will be replaced :
	 * <br>n -> Enter (\n)
	 * <br>t -> Tab (\t)
	 * @param v
	 * @return String
	 */
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
}
