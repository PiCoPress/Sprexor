package sprexor.v2.lib;

import java.util.Vector;
import sprexor.v2.CommandFactory;

public class Utils {

	public static CommandFactory[] toCFClass(CommandFactory  ... cp) {
		return cp;
	}
	
	public static String arg2String(String[] args) {
		String tmp = "";
		if(args == null) return "";
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
		if(arg.length <= 1) return null;
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
}
