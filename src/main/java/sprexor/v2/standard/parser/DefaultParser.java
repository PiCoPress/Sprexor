package sprexor.v2.standard.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import sprexor.SOutputs;
import sprexor.SprexorException;
import sprexor.v2.components.SParser;

public class DefaultParser implements SParser {

	@Override
	public String[] processing(String com) throws SprexorException {
		if(com.isBlank()) return null;
		com = com.trim();
		final char[] comar = com.toCharArray();
		String[] args = new String[comar.length];
		byte count = 0;
		int allCount = 0;
		final StringBuilder cache = new StringBuilder();
		
		char c;
		for(;allCount < comar.length; allCount ++) {
			c = comar[allCount];
			if(c == ' ') {
					if(cache.length() == 0)continue;
					args[count ++] = cache.toString();
					cache.setLength(0);
					continue;
				}else if(c == '"'){
					byte bl = 1;
					while(++ allCount < comar.length) {
						c = comar[allCount];
						if(c == '\\') {
							char nextChar = comar[++ allCount];
							cache.append(switch (nextChar) {
							case 't' -> '\t';
							case 'n' -> '\n';
							case '"' -> '"';
							case '\\' -> '\\';
							default -> nextChar;
							});
							continue;
						}else if(c == '"'){
							bl = 0;
							break;
						}else cache.append(c);
					}
					if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
							SOutputs.syn);
					continue;
				}else if(c == '\'') {
					byte bl = 1;
					while(++ allCount < comar.length) {
						c = comar[allCount];
						if(c == '\\') {
							char nextChar = comar[++ allCount];
							cache.append(switch (nextChar) {
							case 't' -> '\t';
							case 'n' -> '\n';
							case '"' -> '"';
							case '\\' -> '\\';
							default -> nextChar;
							});
							continue;
						}else if(c == '\''){
							bl = 0;
							break;
						}else cache.append(c);
					}
					if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
							SOutputs.syn);
					continue;
				}
			cache.append(c);
		}
		
		if(cache.length() != 0) args[count] = cache.toString();
		args = trimArr(args);
		return args;
	}
	
	@Override
	public String id(String str) {
		return split(str, ' ')[0].trim();
	}
	
	private String[] split(String v, char deli) {
		char[] seq = v.toCharArray();
		ArrayList<String> arr = new ArrayList<String>();
		int index = 0;
		String buffer = "";
		for(char e : seq) {
			if(e == deli) {
				arr.add(buffer);
				buffer = "";
				continue;
			}
			buffer += e;
		}
		arr.add(buffer);
		return arr.toArray(new String[index + 1]);
	}
	
	private String[] trimArr(String[] in) {
		Vector<String> vec = new Vector<String>();
		for(String eval : in) {
			if(eval == null)break;
			vec.add(eval);
		}
		return Arrays.copyOf(vec.toArray(), vec.size(), String[].class);
	}
}
