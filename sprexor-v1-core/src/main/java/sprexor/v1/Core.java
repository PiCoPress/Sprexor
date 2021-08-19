package sprexor.v1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public final class Core {
	
	public static final String version = "1.0.0";
	private int configType = 0;
	private HashMap<String, CommandProvider> cmd = null;
	private HashMap<String, String> helpDB;
	private String list = "";
	private boolean doBasicSyn = true;
	private HashMap<String, Object> envVar = new HashMap<String, Object>();
	private Vector<String> vec = new Vector<String>();
	
	private String[] trimArr(String[] in) {
		vec.clear();
		for(String eval : in) {
			if(eval == null)break;
			vec.add(eval);
		}
		return Arrays.copyOf(vec.toArray(), vec.size(), String[].class);
	}
	
	public Core() {
		 cmd = new HashMap<String, CommandProvider>();
		 cmd.put("help",new CommandProvider() {

			@Override
			public String code(String[] args) {
				String Result = "";
				if(isExist(args[0])) {
						Result = helpDB.get(args[0]);
				}else {
					Result = "command not found.";
				}
				return Result.trim();
			}
			 
			@Override
			public String error(Exception e) {
				return "1";
			}
			@Override 
			public String emptyArgs() {
				return "blank";
			}
		 });
		 
		 cmd.put("var", new CommandProvider() {

			@Override
			public String code(String[] args) {
				if(!doBasicSyn) return "X";
				
					if(args.length == 2) {
					envVar.put(args[0],args[1]);
					return args[1];
				}else if(args.length == 1) {
					envVar.put(args[0],"NO_VALUE");
					return "NO_VALUE";
				}else {
					return emptyArgs();
				}
			}
			
			@Override
			public String emptyArgs() {
				return "USAGE : var [NAME] [VALUE*]\n\tDEFAULT VALUE : \"NO_VALUE\"";
			}
		 });
		 
		 cmd.put("echo", new CommandProvider() {

			@Override
			public String code(String[] args) {
				String sum = "";
				for(String str : args) {
					sum += str;
				}
				return sum;
			}
			@Override
			public String emptyArgs() {
				return "";
			}
		});
		 helpDB = new HashMap<String, String>();
		 helpDB.put("help", "print this message.");
		 helpDB.put("var","Define or declare a variable.");
		 helpDB.put("echo", "print text.");
		 list += "help\nvar\necho\n";
		 
	}
	
	public boolean isExist(String s) {
		if(s.trim().contentEquals(""))return false;
		String[] arr = list.split("\n");
		boolean trace = false;
		for(String compareStr : arr) {
			if(s.contentEquals(compareStr)) {
				trace = true;
				break;
			}
		}
		if(trace)return true;
		return false;
	}
	
	public void activate() {
		if(configType == 0) configType = 2;
	}
	
	public String getList() {
		return list;
	}
	
	public void register(String str, CommandProvider cp, String hd) {
		if(configType != 0)return;
		if(isExist(str) || str.isBlank()) return;
		cmd.put(str, cp);
		list += str + "\n";
		helpDB.put(str, hd);
	}
	
	public void useSyntax(boolean b) {
		if(configType != 0)return;
		doBasicSyn = b;
	}
	
	public void exec(String id, String[] args) {
		if(configType != 2) {
			System.out.println("X");
			return;
		}
		
		Object res = null;
		CommandProvider obj = cmd.get(id);
		args = trimArr(args);
		
		try {
			res = obj.code(args);
		}catch(Exception e) {
			res = obj.error(e);
		}
		
		System.out.println(res);
	}
	
	public void exec(String com) throws CommandNotFoundException {
		if(configType != 2) {
			System.out.println("X");
		}
		
		boolean varMode = false;
		com = com.trim();
		if(com.contentEquals(""))return;
		else if(com.contentEquals(";")) {
			exec(com.substring(1));
			return;
		}
		String id = com.split(" ")[0];
		if(!isExist(id))throw new CommandNotFoundException(id, com);
		
		if(com.indexOf(" ") == -1) {
			System.out.println(cmd.get(id).emptyArgs());
			return;
		}
		com = com.substring(id.length() + 1) + " ";
		String[] comar = com.split("");
		String[] args = new String[comar.length];
		short mod = 0, count = 0, pr = 0, smod = 0;
		int allCount = 0;
		String cache = "";
		for(;allCount < comar.length; allCount ++) {
			
			String c = comar[allCount];
			if(c.contentEquals(";")) {
				if(varMode) {
					if(envVar.get(cache.trim().substring(1)) != null) {
						args[count ++] = (String) envVar.get(cache.trim().substring(1));
						cache = "";
					}else {
						if(cache.trim().contentEquals("@")) {
							System.out.println("blank");
							return;
						}
						System.out.println("no variable : " + cache.trim().substring(1));
						return;
					}
				}
				break;
			}
			if(!varMode) {
				if(mod + smod == 1 && c.contentEquals("\\") && pr == 0) {
					pr = 1;
					continue;
				}
			
				if(c.contentEquals(" ") && smod == 0 && mod == 0) {
					if(cache.isEmpty())continue;
					args[count ++] = cache;
					cache = "";
					continue;
				}
				// escape
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
						}
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
						}
						smod = 0;
					}
					continue;
				}else if(pr == 1) {
					cache += "\\";
					pr = 0;
					continue;
				}
			}
			if(doBasicSyn) {
				if(smod + mod + pr == 0) {
					if(c.contentEquals("@") && !varMode) {			//----------------- variable 
						varMode = true;
					}else {
						if(varMode == true && c.contentEquals("@")) {
							System.out.println("strange : " + count);
							return;
						}else if(varMode){
							if(c.contentEquals(" ")) {
								varMode = false;
								if(envVar.get(cache.trim().substring(1)) != null) {
									
									args[count ++] = (String) envVar.get(cache.trim().substring(1));
									cache = "";
								}else {
									if(cache.trim().contentEquals("@")) {
										System.out.println("blank");
										return;
									}
									System.out.println("no variable : " + cache.trim().substring(1));
									return;
								}
							}
						}
					}
				}
			}
			cache += c;
		}
		if(mod != 0 || smod != 0 || pr != 0) {
			System.out.println("X");
			return;
		}
		Object res = null;
		CommandProvider obj = cmd.get(id);
		args = trimArr(args);
		try {
			res = obj.code(args);
		}catch(Exception e) {
			res = obj.error(e);
		}
		System.out.println(res);
	}
}
