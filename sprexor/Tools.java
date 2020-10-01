package sprexor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		if(tmp.trim().isEmpty())return (String[]) v.toArray();
		v.add(tmp);
		return (String[]) v.toArray();
	}
	
	public static CommandProvider[] toCPClass(CommandProvider...cp) {
		return cp;
	}
	
	public static String arg2String(String[] args) {
		String tmp = "";
		for(String str : args) tmp += str;
		return tmp;
	}
	
	public static String[] excludeArr(String[] arg, int a) {
		String[] tmp = {};
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
}
