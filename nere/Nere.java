package nere;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Nere {
	public final String VERSION = "0.1.2";
	
	//protected static Vector<String> msg = new Vector<String>();
	//protected static Vector<String> err = new Vector<String>();
	protected Vector<Object[]> MessageLog = null;
	private HashMap<String, CommandProvider> cmd;
	private HashMap<String, String> helpDB;
	private String list = "";
	private String comment = "#";
	
	private GlobalData gd = null;
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
		IOCenter.recentMessage = eo;
	}
	
	private String join(String t, String[] obj) {
		String sumStr = "";
		for(String str : obj) {
			sumStr += "\t" + str + t;
		}
		return sumStr;
	}
	public Nere() {
		MessageLog = new Vector<Object[]>();
		 cmd = new HashMap<String, CommandProvider>();
		 cmd.put("help",new CommandProvider() {

			@Override
			public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) {
				String[] helpl = list.split("\n");
				String Result = "";
				int count = helpl.length - 1;
				for(String key : helpDB.keySet()) {
					Result += helpl[count] + " - " + helpDB.get(key) + "\n";
					count --;
				}
				return Result.trim();
			}
			 
			@Override
			public Object ErrorEventListener(Exception e) {
				return 1;
			}
			@Override 
			public Object no_arg_apply() {
				return apply(new String[0], new boolean[0], null);
			}
		 });
		 
		 cmd.put("var", new CommandProvider() {

			@Override
			public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) {
				if(!doBasicSyn) return "Syntax is not permitted.";
				
					if(args.length == 2) {
					envVar.put(args[0],args[1]);
					return args[1];
				}else if(args.length == 1) {
					envVar.put(args[0],IOCenter.NO_VALUE);
					return "NO_VALUE";
				}else {
					return no_arg_apply();
				}
			}
			
			@Override
			public Object no_arg_apply() {
				return "USAGE : var [NAME] [VALUE*]\n\tDEFAULT VALUE : \"NO_VALUE\"";
			}
		 });
		 
		 cmd.put("echo", new CommandProvider() {

			@Override
			public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) {
				String sum = "";
				for(String str : args) {
					sum += str;
				}
				return sum;
			}
			@Override
			public Object no_arg_apply() {
				return "";
			}
		});
		 helpDB = new HashMap<String, String>();
		 helpDB.put("help", "print this message.");
		 helpDB.put("var","Define or declare a variable.");
		 helpDB.put("echo", "print text.");
		 list += "help\nvar\necho\n";
		 
	}
	
	public void initRegistry() {
		if(gd != null)return;
		gd = new GlobalData();
	}
	
	public boolean isExist(String s) {
		String[] arr = list.split("\n");
		for(String compareStr : arr) {
			if(s.contentEquals(compareStr))return true;
		}
		return false;
	}
	
	@Deprecated
	public String getList() {
		//return list;
		return "";
	}
	
	public void mkcmd(String str, CommandProvider cp, String hd) {
		if(isExist(str) || fil(str, " ", "'", "\"", "\\", "$", "*", "^", "(", ")", "{", "}", ":", "?", ";", "<", ">", ",", ".", "!", "#", "@", "&", "%", "~", "`", "[", "]", "\s") || str.contentEquals(""))return;
		cmd.put(str, cp);
		list += str + "\n";
		hd = join("\n", hd.split("\n"));
		helpDB.put(str, hd);
	}
	
	public void setComment(String str) {
		if(!str.isEmpty())comment = str;
	}
	
	public void useSyntax(boolean b) {
		doBasicSyn = b;
	}
	
	public void exec(String com) throws CommandNotFoundException {
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
			logger(cmd.get(id).no_arg_apply(), IOCenter.STDOUT);
			return;
		}
		
		if(gd == null) {
			logger("Err : GlobalData isn't formed.", IOCenter.ERR);
			return;
		}
		com = com.substring(id.length() + 1) + " ";
		
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
			
			
			
				//---------------------------------------------------------------------------------------------
				//---------------------------------------------------------------------------------------------
				//--------------------------	Syntax	-------------------------------
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
			res = obj.apply(args, wra, gd);
		}catch(Exception e) {
			res = obj.ErrorEventListener(e);
		}
		logger(res,IOCenter.STDOUT);
		
		if(nextFlag) {
			exec(nextStr);
		}
	}
}
