package sprexor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	
	public static CommandFactory[] toCFClasses(CommandFactory  ... cf) {
		return cf;
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
		if(startIndex == 0 || arr.length - startIndex <= 0)return arr;
		String[] tmp = new String[arr.length - startIndex];
		for(int i = startIndex; i < arr.length; i ++) {
			tmp[i - startIndex] = arr[i];
		}
		return tmp;
	}
	
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
}
