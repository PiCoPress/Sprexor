package sprexor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
/**
 * The Component that Iterable class provides functions for manage the arguments.
 * @since 0.2.18
 */
public class SParameter implements Iterable<String> {
	private String[] v;
	private Object[] reserved;
	enum A{
		Default,
		Str,
		SetType1,
		SetType2
	}
	/**
	 * This Unit Class provide methods for a unit of Arguments.
	 * @since 0.2.18
	 * @version 1.0
	 */
	public class Unit{
		String k = "";
		int index;
		public Object label;
		public final A ArgumentType;
		protected Unit(String k, int index) {
			this.k = k;
			this.index = index;
			if(k.startsWith("--")) ArgumentType = A.SetType2;
			else if(k.startsWith("-")) ArgumentType = A.SetType1;
			else ArgumentType = A.Default;
		}
		@Override
		public String toString() {
			return k;
		}
		/**
		 * get a element of previous index
		 * @return Unit
		 */
		public Unit prev() {
			if(index <= 0) return null;
			return new Unit(v[index - 1], index - 1);
		}
		/**
		 * get a element of next index
		 * @return Unit
		*/
		public Unit next() {
			if(index + 1 >= v.length) return null;
			return new Unit(v[index + 1], index + 1);
		}
	}
	protected SParameter(String[] v) {
		this.v = v;
		reserved = new Object[1024];
	}
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String str : v) {
			sb.append(str).append(" ");
		}
		return sb.toString();
	}
	/**
	 * Add new argument at last index.
	 * @param value - param values
	 */
	public void add(String value) {
		v = Arrays.copyOf(v, v.length + 1);
		v[v.length - 1] = value;
	}
	/**
	 *Return all of arguments to string array. 
	 * @return
	 */
	public String[] get() {
		return v;
	}
	/**
	 * It returns the class Unit that manage each of elements.
	 * @param i - index to get unit of arguments.
	 * @return Unit Class
	 */
	public Unit get(int i) {
		Unit u = new Unit(v[i], i);
		u.label = reserved[i];
		return u;
	}
	/**
	 * 
	 * @param i - The index to get a element of arguments.
	 * @return String
	 */
	public String getsf(int i) {
		if(i < v.length) return v[i];
		else return "";
	}
	/**
	 * It gets String by Ignoring option.
	 * @param i
	 * @return String
	 */
	public String gets(int i) {
		int c = 0;
		for(String s : v) {
			if(!s.startsWith("-")) {
				if(c == i) return s;
				c ++;
			}
		}
		return "";
	}
	/**
	 * Return Options to Array in order.
	 * @return String[]
	 */
	public String[] getAllOption() {
		ArrayList<String> arr = new ArrayList<String>();
		for(String s : v) {
			if(s.startsWith("-") && arr.contains(s)) {
				arr.add(s.substring(s.startsWith("--") ? 2 : 1));
			}
		}
		arr.sort(new Comparator<String>() {
			@Override
			public int compare(String s, String ss) {
				return s.compareTo(ss);
			}
		});
		return arr.toArray(new String[arr.size()]);
	}
	/**
	 * the lagacy parser.
	 * @param str string line
	 * @return string array
	 */
	public static String[] Parse(String str) {
		String[] pops = str.split(""),
				units = new String[pops.length];
		short mod = 0,
				count = 0,
				pr = 0,
				smod = 0;
		int allCount = 0;
		String cache = "";
		for(;allCount < pops.length; allCount ++) {
			String c = pops[allCount];
			if(mod + smod == 1 && c.contentEquals("\\") && pr == 0) {
					pr = 1;
					continue;
				}
				if(c.contentEquals(" ") && smod == 0 && mod == 0) {
					if(cache.trim().isEmpty())continue;
					units[count ++] = cache.trim();
					cache = "";
					continue;
				}
				if(c.contentEquals("\"")) {
					if(mod == 0 && smod == 0) {
						mod = 1;
					}else {
						if(pr == 1) {
							pr = 0;
							cache += c;
							continue;
						}else if(smod == 1) {
							cache +=  c;
							mod = 0;
							continue;
						}
						units[count ++] = cache.trim();
						cache = "";
						mod = 0;
					}
					continue;
					
				}else if(c.contentEquals("'")) {
					if(mod == 0 && smod == 0) {
						smod = 1;
					}else {
						if(pr == 1) {
							pr = 0;
							cache += c;
							continue;
						}else if(mod == 1) {
							cache +=  c;
							smod = 0;
							continue;
						}
						units[count ++] = cache.trim();
						cache = "";
						smod = 0;
					}
					continue;
				}else if(pr == 1) {
					if(c.contentEquals("n")){
						if(mod == 0 && smod == 0) {
							smod = 1;
						}else {
							if(pr == 1) {
								pr = 0;
								cache += "\n";
								continue;
							}else if(mod == 1) {
								cache +=  "\n";
							}
							smod = 0;
						}
						continue;
					}else if(c.contentEquals("t")){
						if(mod == 0 && smod == 0) {
							smod = 1;
						}else {
							if(pr == 1) {
								pr = 0;
								cache += "\t";
								continue;
							}else if(mod == 1) {
								cache +=  "\t";
							}
							smod = 0;
						}
						continue;
					}else {
					cache += "\\";
					pr = 0;
					continue;
				}
			}
			cache += c;
		}
		Vector<String> vec = new Vector<String>();
		for(String st : units) {
			if(st == null)break;
			vec.add(st);
		}
		return vec.toArray(new String[vec.size()]);
	}
	/**
	 * Return the size of arguments.
	 * @return size
	 */
	public int length() {
		return v.length;
	}
	public boolean isEmpty() {
		return v.length == 0;
	}
	/**
	 * get parameters without option starting
	 * @return string array
	 */
	public String[] getValidParameters() {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = 0; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith("-") && !tmp.contains(" ")) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	/**
	 * get parameters without option starting
	 * @param startAt 
	 * @return string array
	 */
	public String[] getValidParameters(int startAt) {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = startAt; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith("-") && !tmp.contains(" ")) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	
	protected void setLabel(int index, Object obj) {
		reserved[index] = obj;
	}
	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			int cursor = 0;
			@Override
			public boolean hasNext() {
				return cursor >= v.length ? false : true;
			}
			@Override
			public String next() {
				return v[ cursor ++ ];
			}
		};
	}
}