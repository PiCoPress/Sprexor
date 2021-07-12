package sprexor.v3.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

/**
 * Iterable class for manage parameters of command.
 * <p>
 * There is many methods about command option moreover, 
 * can setting each parameter via {@link sprexor.v2.components.SParameter.Unit Unit} class. 
 * 
 * @since 0.2
 */
public final class SParameter implements Iterable<String> {
	private String[] v;
	private String[] origin;
	private Object[] reserved;
	
	/**
	 * original string before parsing.
	 * <p>
	 * It might not be pure value, so use {@link #toString()} method.
	 */
	public String raw = "";
	
	/**
	 * Unit class has a parameter information.
	 */
	public final class Unit{
		String k = "";
		int index;
		public Object label;
		protected Unit(String k, int index) {
			this.k = k;
			this.index = index;
		}
		
		@Override
		public String toString() {
			return k;
		}
		/**
		 * get a element of previous index
		 * <p>
		 * If array is {"a", "b", "c"} and pointing index is 1 : "b",
		 * <br>
		 * prev() pointing 0 : "a"
		 * 
		 * @return Unit If present index is valid, else null (index = 0)
		 */
		public Unit prev() {
			if(index <= 0) return null;
			return new Unit(v[index - 1], index - 1);
		}
		/**
		 * get a element of next index
		 * <p>
		 * If array is {"a", "b", "c"} and pointing index is 1 : "b",
		 * <br>
		 * next() pointing 2 : "c"
		 * 
		 * @return Unit If present index is valid, else null (index = last index)
		 * @return Unit
		*/
		public Unit next() {
			if(index + 1 >= v.length) return null;
			return new Unit(v[index + 1], index + 1);
		}
	}
	
	public SParameter(String[] v) {
		if(v == null) {
			String[] s = {""};
			v = s;
		}
		this.v = v;
		this.origin = v;
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
	 * Add new element at last index.
	 * 
	 * @param value parameter value
	 */
	public void add(String value) {
		if(value == null) return;
		v = Arrays.copyOf(v, v.length + 1);
		v[v.length - 1] = value;
	}
	
	/**
	 * remove a element in all parameters
	 * 
	 * @param index to remove
	 */
	public void remove(int index) {
		v[index] = null;
		String[] tmp = new String[v.length - 1];
		int i = 0;
		for(String s : tmp) {
			if(s != null) tmp[i ++] = s; 
		}
		v = tmp;
	}
	
	/**
	 * When arguments need to recover because of methods 'add' and 'remove', it is helpful.
	 * 
	 */
	public void recover() {
		v = origin;
	}
	
	/**
	 *get all arguments including options. 
	 *
	 * @return string array
	 */
	public String[] getArrays() {
		return v;
	}
	
	/**
	 * get a unit selected index
	 * 
	 * @param i - index to get unit of arguments.
	 * @return Unit Class
	 */
	public Unit getUnit(int i) {
		Unit u = new Unit(v[i], i);
		u.label = reserved[i];
		return u;
	}
	
	/**
	 * get a element selected index
	 * 
	 * @param i
	 * @return String
	 */
	public String getElement(int i) {
		if(i < v.length) return v[i];
		else return "";
	}
	
	/**
	 * get a element selected index excluding options
	 *
	 * @param i index
	 * @return , if param 'i' is out of boundary, returns last element
	 */
	public String getValidElement(int i) {
		int c = 0;
		for(String s : v) {
			if(!s.startsWith("-")) {
				if(c == i) return s;
				c ++;
			}
		}
		return v[v.length - 1];
	}
	
	/**
	 * get a element selected index excluding options
	 *
	 * @param i index
	 * @param prefix
	 * @return String, if param 'i' is out of boundary, returns last element
	 */
	public String getValidElement(int i, String prefix) {
		int c = 0;
		for(String s : v) {
			if(!s.startsWith(prefix)) {
				if(c == i) return s;
				c ++;
			}
		}
		return v[v.length - 1];
	}
	
	/**
	 * get all options
	 * 
	 * @return String array
	 */
	public String[] getAllOption() {
		
		ArrayList<String> arr = new ArrayList<String>();
		for(String s : v) {
			if(s.startsWith("-")) arr.add(s);
		}
		if(arr.size() < 1) return strarray("");
		return arr.toArray(new String[arr.size()]);
	}
	
	/**
	 * get all options with specific prefix
	 * 
	 * @param prefix
	 * @return string array
	 */
	public String[] getAllOption(String prefix) {
		ArrayList<String> arr = new ArrayList<String>();
		for(String s : v) {
				if(s.startsWith(prefix)) arr.add(s);
			}
		return arr.toArray(new String[arr.size()]);
	}
	
	/**
	 * check option is exist at selected optIndex
	 * 
	 * @param names String Array to apply
	 * @param optIndex option index
	 * @return boolean true if exist, else false
	 */
	public boolean findOption(String[] names, int optIndex) {
		String[] arr = getAllOption();
		for(String u : arr) {
			for(String n : names) {
				if(u.contentEquals(n)) return true;
			}
		}
		return false;
	}
	
	/**
	 * check option is exist at selected optIndex
	 * 
	 * @param names String Array to apply
	 * @param optIndex option index
	 * @return booelan boolean true if exist, else false
	 */
	public boolean findOption(String[] names, int optIndex, String prefix) {
		String[] arr = getAllOption(prefix);
		for(String u : arr) {
			for(String n : names) {
				if(u.contentEquals(n)) return true;
			}
		}
		return false;
	}
	
	/**
	 * the lagacy parser.
	 * 
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
	 * get size
	 * 
	 * @return int size
	 */
	public int length() {
		return v.length;
	}
	
	/**
	 * @return boolean true if argument is empty, otherwise false.
	 */
	public boolean isEmpty() {
		return v.length == 0;
	}
	
	/**
	 * get parameters without option
	 * 
	 * @return string array
	 */
	public String[] getValidParameters() {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = 0; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith("-")) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	
	/**
	 * get parameters selected start index
	 * 
	 * @param startAt 
	 * @return string array
	 */
	public String[] getValidParameters(int startAt) {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = startAt; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith("-")) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	
	/**
	 * get parameters selected start index with prefix
	 * 
	 * @param startAt 
	 * @param prefix option delimiter
	 * @return string array
	 */
	public String[] getValidParameters(int startAt, String prefix) {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = startAt; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith(prefix)) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	
	/**
	 * get parameters with prefix
	 * 
	 * @param startAt 
	 * @param prefix option delimiter
	 * @return string array
	 */
	public String[] getValidParameters(String prefix) {
		Vector<String> v_ = new Vector<String>();
		String tmp;
		for(int i = 0; i < v.length; i ++) {
			tmp = v[i];
			if(tmp.startsWith(prefix)) v_.add(tmp);
		}
		return v_.toArray(new String[v_.size()]);
	}
	
	/**
	 * get unparsed string {@link #raw}
	 * 
	 * @return string raw
	 */
	public String getRaw() {
		return raw;
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
	
	private String[] strarray(String...s) {
		return s;
	}
}