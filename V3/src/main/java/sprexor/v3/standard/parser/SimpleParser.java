package sprexor.v3.standard.parser;

import sprexor.SprexorException;
import sprexor.v3.components.SParser;

public class SimpleParser implements SParser {

	private String[] split(String s) {
		char[] c = s.toCharArray();
		int n = 0;
		for(char i : c) {
			if(i == ' ')n ++;
		}
		String[] tmp = new String[n];
		n = 0;
		StringBuilder sb = new StringBuilder();
		for(char i : c) {
			if(i == ' ') {
				tmp[n ++ ] = sb.toString();
				sb.setLength(0);
			}
			else sb.append(i);
		}
		return tmp;
	}
	@Override
	public String id(String str) {
		return split(str)[0];
	}

	@Override
	public String[] processing(String str) throws SprexorException {
		return split(str);
	}

}
