package sprexor.v2.lib;

import java.util.Stack;

/**
 * Sprexor > V3 > lib > VersionManager (2021)
 * @author PICOPress
 * Github link : https://github.com/PiCoPress
 *
 * VersionManager source code can be used by any users, and this sign should be included arccording to MIT license.
 */
public class Smt {
	public static String SMT_FORM(String v) {
		if(v.length() <= 1 || v.indexOf("]") == -1)return v;
		Stack<Character> st = new Stack<Character>();
		char[] Characters = v.toCharArray();
		int cursorIndex = 0,
				toIndex = 0,
				count = 0,
				leng = v.length();
		char ch;
		StringBuilder buffer = new StringBuilder(),
				result = new StringBuilder();
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
					buffer.insert(0, st.peek().toString());
					st.pop();
					cursorIndex --;
					continue;
				}else {
					if(iont(buffer.toString())) {
						result.append("[" + buffer + "]");
						buffer.setLength(0);
						toFront = false;
						cursorIndex = toIndex;
						cursorIndex ++;
						continue;
					}
					toFront = false;
					cursorIndex = toIndex;
					result.append(buffer.toString().replaceAll("n", "\n").replaceAll("t", "\t"));
					buffer.setLength(0);
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
			if(!open) result.append(ch);
		}
		return result.toString();
	}
	private static boolean iont(String s) {
		char[] caa = s.toCharArray();
		for(char c : caa) {
			if(c != 't' && c != 'n') return true;
		}
		return false;
	}
}
