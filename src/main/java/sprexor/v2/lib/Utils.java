package sprexor.v2.lib;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import sprexor.SOutputs;
import sprexor.v2.components.SCommand;

public class Utils {
	/**
	 * get an instance for SCommand
	 * @param sc
	 * @return SCommand
	 */
	public static SCommand g(Class<? extends SCommand> sc) {
		try {
			return sc.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
		return null;
		}
	}
	/**
	 * get property of key
	 * <br>
	 * format :<br>
	 * <pre>
	 * a=b
	 * c=d
	 * e=f
	 * ...
	 * </pre>
	 * @param raw
	 * @param name
	 * @return string value
	 */
	public static String keymap(String raw, String name) {
		String[] tmpArr = raw.split("\n");
		String[] k;
		for(String s : tmpArr) {
			k = s.split("=");
			if(k[0].contentEquals(name)) return k[1];
		}
		return "";
	}
	/**
	 * @param args string array
	 * @return string text
	 */
	public static String join(String[] args) {
		StringBuilder tmp = new StringBuilder();
		if(args == null) return "";
		for(String str : args) tmp.append(str);
		return tmp.toString();
	}
	/**
	 * @param args string array
	 * @param ch join with
	 * @return string text
	 */
	public static String join(String[] args, String ch) {
		StringBuilder tmp = new StringBuilder();
		for(String str : args) tmp.append(str).append(ch);
		return tmp.substring(0 , tmp.length() - 1);
	}
	/**
	 * @param arg objec array
	 * @param a index of exclude element
	 * @return object array
	 */
	public static Object[] excludeArr(Object[] arg, int a) {
		Object[] tmp = {"",};
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
	/**
	 * @param arr
	 * @param startIndex
	 * @return object array
	 */
	public static Object[] cutArr(Object[] arr, int startIndex) {
		if(startIndex == 0)return arr;
		Object[] tmp = new String[arr.length - startIndex];
		for(int i = startIndex; i < arr.length; i ++) {
			tmp[i - startIndex] = arr[i];
		}
		return tmp;
	}
	/**
	 * @param a string array
	 * @param start string appending at
	 * @return String array
	 */
	public static String[] bind(String[] a, int start) {
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
	
	public static String[] ots(Object[] arr) {
		String[] sa = new String[arr.length];
		int i = 0;
		for(Object obj : arr) {
			sa[i ++] = obj.toString();
		}
		return sa;
	}
}
