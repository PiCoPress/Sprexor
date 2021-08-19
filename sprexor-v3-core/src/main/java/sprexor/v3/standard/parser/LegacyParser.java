package sprexor.v3.standard.parser;

import java.util.Vector;

import sprexor.v3.SprexorException;
import sprexor.v3.components.SParser;

public class LegacyParser implements SParser {

	@Override
	public String id(String str) {
		return str.split(" ")[0];
	}

	@Override
	public String[] processing(String str) throws SprexorException {
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

}
