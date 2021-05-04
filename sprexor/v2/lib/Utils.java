package sprexor.v2.lib;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import sprexor.v2.components.SCommand;

public class Utils {
	/**<pre>
	 * quick array creator
	 * <strong>issue : Type safety: Potential heap pollution 
	 * 			via varargs parameter sc </strong></pre>
	 * @param p 
	 * @return Objects
	 */
	public static <T> T[] a(T...p) {
		return p;
	}
	/**<pre>
	 * quick array creator for SCommand
	 * <strong>issue : Type safety: Potential heap pollution 
	 * 			via varargs parameter sc </strong></pre>
	 * @param p 
	 * @return Objects
	 */
	public static SCommand[] a(Class<? extends SCommand>...sc) {
		SCommand[] arr = new SCommand[sc.length];
		int i = 0;
		for(Class<? extends SCommand> cl : sc) {
			arr[i ++ ] = g(cl);
		}
		return arr;
	}
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
	 * @param args string array
	 * @return string text
	 */
	public static String join(String[] args) {
		String tmp = "";
		if(args == null) return "";
		for(String str : args) tmp += str;
		return tmp;
	}
	/**
	 * @param args string array
	 * @param ch join with
	 * @return string text
	 */
	public static String join(String[] args, String ch) {
		String tmp = "";
		for(String str : args) tmp += str + ch;
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
