package sprexor.v2.lib;

import java.util.Stack;

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
		String buffer = "",
				result = "";
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
					buffer = st.peek().toString() + buffer;
					st.pop();
					cursorIndex --;
					continue;
				}else {
					if(buffer.matches("^[^nt]+[^nt]*$")) {
						result += "[" + buffer + "]";
						buffer = "";
						toFront = false;
						cursorIndex = toIndex;
						cursorIndex ++;
						continue;
					}
					toFront = false;
					cursorIndex = toIndex;
					result += buffer.replaceAll("n", "\n").replaceAll("t", "\t");
					buffer = "";
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
			if(!open) result += ch;
		}
		return result;
	}
}
