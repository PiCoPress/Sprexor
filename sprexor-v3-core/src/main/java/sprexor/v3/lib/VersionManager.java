package sprexor.v3.lib;


/**
 * Sprexor > V3 > lib > VersionManager (2021)
 * @author PICOPress
 * Github link : https://github.com/PiCoPress
 *
 * VersionManager source code can be used by any users, and this sign should be included arccording to MIT license.
 */
public final class VersionManager {
	short[] ver = new short[3];
	
	short[] parse(String s) {
		String[] m = s.split("\\.");
		short[] v = new short[3];
		for(int i = 0; i < 3; i ++) {
			if(m.length <= i) v[i] = 0;
			else v[i] = Short.parseShort(m[i]);
		}
		return v;
	}
	
	static public boolean valid(String v) {
		char[] crr = v.toCharArray();
		boolean dot = false;
		boolean anything = false;
		int maj = 0;
		for(char c : crr) {
			if(dot || maj >= 3) return false;
			else if(c == '.'){
				dot = true;
				if(!anything) return false;
				maj ++;
				anything = false;
				continue;
			}
			if(0x30 <= c && c <= 0x39) {
				anything = true;
				dot = false;
			} else return false;
		}
		return true;
	}
	
	public VersionManager(String s) {
		if(!valid(s)) return;
		ver = parse(s);
	}
	
	public int compare(String s) {
		if(!valid(s)) throw new NumberFormatException();
		short[] sh = parse(s);
		for(int i = 0; i < 3; i ++) {
			if(sh[i] < ver[i]) return -1;
			else if(sh[i] > ver[i]) return 1;
		}
		return 0;
	}
}
