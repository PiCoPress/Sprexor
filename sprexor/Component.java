package sprexor;

import java.util.Iterator;

public class Component implements Iterable<String> {
	String[] v;
	boolean[] isw;
	/**
	 * This Unit Class provide methods for a unit of Arguments.
	 * @since 0.2.18
	 * @version 1.0
	 */
	public class Unit{
		String k = "";
		boolean b;
		protected Unit(String k, boolean b) {
			this.k = k;
			this.b = b;
		}
		@Override
		public String toString() {
			return k;
		}
		public boolean isWrapped() {
			return b;
		}
	}
	protected Component(String[] v, boolean[] isw) {
		this.v = v;
		this.isw = isw;
	}
	public String[] get() {
		return v;
	}
	public Unit get(int i) {
		return new Unit(v[i], isw[i]);
	}
	public String gets(int i) {
		return v[i];
	}
	public int length() {
		return v.length;
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
