package sprexor.v0;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;


/*
 * Nere : Command Executor 1.0.0
 */
public class Nere {
	protected Vector<Object[]> MessageLog = null;
	private int configType = 0;
	private HashMap<String, CommandProvider> cmd = null;
	private HashMap<String, String> helpDB;
	private String list = "";
	private String comment = "#";
	private GlobalData gd = null;
	private boolean doBasicSyn = true;
	private HashMap<String, Object> envVar = new HashMap<String, Object>();
	private Vector<String> vec = new Vector<String>();
	protected Object[] recentMessage = null;
	
	private String[] trimArr(String[] in) {
		vec.clear();
		for(String eval : in) {
			if(eval == null)break;
			vec.add(eval);
		}
		return Arrays.copyOf(vec.toArray(), vec.size(), String[].class);
	}
	private boolean[] trimArr(boolean[] in, int num) {
		boolean[] tmp = new boolean[num];
		for(int i = 0; i < num; i ++)tmp[i] = in[i];
		
		return tmp;
	}
	private boolean fil(String ch, String...tar) {
		for(String t : tar) {
			if(ch.indexOf(t) != -1)return true;
		}
		return false;
	}
	private void logger(Object oo, IOCenter.TYPE ii) {
		Object[] eo = new Object[2];
		eo[0] = oo;
		eo[1] = ii;
		MessageLog.add(eo);
		recentMessage = eo;
	}
	public Nere() {
		MessageLog = new Vector<Object[]>();
		 cmd = new HashMap<String, CommandProvider>();
		 recentMessage = new Object[2];
		 
		 cmd.put("help",new CommandProvider() {

			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				String Result = "";
				if(isExist(args[0])) {
						Result = helpDB.get(args[0]);
				}else {
					Result = "command not found.";
				}
				return Result.trim();
			}
			 
			@Override
			public Object error(Exception e) {
				return 1;
			}
			@Override 
			public Object emptyArgs() {
				return "blanked name.";
			}
		 });
		 
		 cmd.put("var", new CommandProvider() {

			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				if(!doBasicSyn) return "Syntax is not permitted.";
				
					if(args.length == 2) {
					envVar.put(args[0],args[1]);
					return args[1];
				}else if(args.length == 1) {
					envVar.put(args[0],IOCenter.NO_VALUE);
					return "NO_VALUE";
				}else {
					return emptyArgs();
				}
			}
			
			@Override
			public Object emptyArgs() {
				return "USAGE : var [NAME] [VALUE*]\n\tDEFAULT VALUE : \"NO_VALUE\"";
			}
		 });
		 
		 cmd.put("echo", new CommandProvider() {

			@Override
			public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
				String sum = "";
				for(String str : args) {
					sum += str;
				}
				return sum;
			}
			@Override
			public Object emptyArgs() {
				return "";
			}
		});
		 helpDB = new HashMap<String, String>();
		 helpDB.put("help", "print this message.");
		 helpDB.put("var","Define or declare a variable.");
		 helpDB.put("echo", "print text.");
		 list += "help\nvar\necho\n";
		 
	}
	
	public void initScope() {
		if(configType != 0)return;
		if(gd != null)return;
		gd = new GlobalData();
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
	
	public void send(String str, IOCenter.TYPE type) {
		if(configType != 2)return;
		IOCenter.log(str, type);
		MessageLog.add(recentMessage);
	}
	
	public void activate() {
		if(configType == 0)configType = 2;
	}
	
	@Deprecated
	public String getList() {
		//return list;
		return "";
	}
	
	public void register(String str, CommandProvider cp, String hd) {
		if(configType != 0)return;
		if(isExist(str) || fil(str, " ", "'", "\"", "\\", "$", "*", "^", "(", ")", "{", "}", ":", "?", ";", "<", ">", ",", ".", "!", "#", "@", "&", "%", "~", "`", "[", "]") || str.contentEquals(""))return;
		cmd.put(str, cp);
		list += str + "\n";
		helpDB.put(str, hd);
	}
	
	public void setComment(String str) {
		if(configType != 0)return;
		if(!str.isEmpty())comment = str;
	}
	
	public void useSyntax(boolean b) {
		if(configType != 0)return;
		doBasicSyn = b;
	}
	
	public void exec(String id, String[] args) {
		if(configType != 2) {
			recentMessage[0] = "Nere was not prepared yet.";
			recentMessage[1] = IOCenter.ERR;
			MessageLog.add(recentMessage);
			return;
		}
		
		Object res = null;
		CommandProvider obj = cmd.get(id);
		args = trimArr(args);
		
		try {
			res = obj.code(args, new boolean[args.length], gd);
		}catch(Exception e) {
			res = obj.error(e);
		}
		
		logger(res,IOCenter.STDOUT);
	}
	
	public void exec(String com) throws CommandNotFoundException {
		if(configType != 2) {
			recentMessage[0] = "Nere was not prepared yet.";
			recentMessage[1] = IOCenter.ERR;
			MessageLog.add(recentMessage);
			return;
		}
		
		boolean varMode = false;
		com = com.trim();
		if(com.contentEquals(""))return;
		else if(com.startsWith(comment))return;
		else if(com.contentEquals(";")) {
			exec(com.substring(1));
			return;
		}
		String id = com.split(" ")[0];
		if(!isExist(id))throw new CommandNotFoundException(id, com);
		
		if(com.indexOf(" ") == -1) {
			logger(cmd.get(id).emptyArgs(), IOCenter.STDOUT);
			return;
		}
		
		if(gd == null) {
			logger("Err : GlobalData isn't formed.", IOCenter.ERR);
			return;
		}
		com = com.substring(id.length() + 1) + " ";
		
		if(id.indexOf("#") != -1) {
			Object res = null;
			
			CommandProvider obj = cmd.get(id);
			try {
				res = obj.code(new String[0], new boolean[0], gd);
			}catch(Exception e) {
				res = obj.error(e);
			}
			
			logger(res,IOCenter.STDOUT);
			return;
		}
		
		String[] comar = com.split("");
		String[] args = new String[comar.length];
		boolean[] wra = new boolean[comar.length];
		short mod = 0, count = 0, pr = 0, smod = 0;
		int allCount = 0;
		
		String nextStr = "";
		boolean nextFlag = false;
		
		boolean cmtMode = false;
		
		String cache = "";
		for(;allCount < comar.length; allCount ++) {
			
			String c = comar[allCount];
			
			if(cmtMode) {
				if(c.contentEquals(";")) {
					cmtMode = false;
					nextFlag = true;
					nextStr = com.substring(allCount + 1);
					break;
				}
				continue;
			}
			
			if(c.contentEquals(";")) {
				nextStr = com.substring(allCount + 1);
				nextFlag = true;
				if(varMode) {
					if(envVar.get(cache.trim().substring(1)) != null) {
						args[count ++] = envVar.get(cache.trim().substring(1)) + "";
						wra[count] = false;
						cache = "";
					}else {
						if(cache.trim().contentEquals("@")) {
							logger("variable name is empty.",IOCenter.ERR);
							return;
						}
						logger("could not find variable : " + cache.trim().substring(1),IOCenter.ERR);
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
						wra[count] = true;
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
						wra[count] = true;
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
				
					if(c.contentEquals(comment) && mod == 0) {
						cmtMode = true;
						args[count++] = cache;
						wra[count] = false;
						cache = "";
						continue; // ---------------- comment
					}
					
					if(c.contentEquals("@") && !varMode) {			//----------------- variable 
						varMode = true;
					}else {
						if(varMode == true && c.contentEquals("@")) {
							logger("invalid value : '@@', parse stopped : " + count,IOCenter.ERR);
							return;
						}else if(varMode){
							if(c.contentEquals(" ")) {
								varMode = false;
								if(envVar.get(cache.trim().substring(1)) != null) {
									
									args[count ++] = envVar.get(cache.trim().substring(1)) + "";
									wra[count] = false;
									cache = "";
								}else {
									if(cache.trim().contentEquals("@")) {
										logger("variable name is empty.",IOCenter.ERR);
										return;
									}
									logger("could not find variable : " + cache.trim().substring(1),IOCenter.ERR);
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
			
			logger("Error : invalid literal syntax.", IOCenter.ERR);
			return;
		}
		Object res = null;
		
		CommandProvider obj = cmd.get(id);
		args = trimArr(args);
		wra = trimArr(wra, count);
		try {
			res = obj.code(args, wra, gd);
		}catch(Exception e) {
			res = obj.error(e);
		}
		
		logger(res,IOCenter.STDOUT);
		if(nextFlag) {
			exec(nextStr);
		}
	}
}
