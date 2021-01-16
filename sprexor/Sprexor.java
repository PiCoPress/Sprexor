package sprexor;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;
import java.util.StringTokenizer;
import sprexor.IOCenter.TYPE;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface Activate_need { 
	/*
	 * This Annotation is used to let if something method need to activate.
	 */
}
/*
 * Sprexor : the String Parser & Executor 0.2.18-beta1
 *  copyright(c)  2020  by PICOPress, All rights reserved.
 */
public class Sprexor {
	//
	// Internal Variables.
	//
	@Deprecated protected Vector<Object[]> MessageLog = null;
	@Deprecated protected Object[] recentMessage = null;
	@Deprecated protected Vector<Object[]> blockMessage = new Vector<Object[]>();
	public static final String VERSION = "0.2.18-be";
	public static final String CODENAME = "Venom";
	public Reflection reflect;
	public Impose impose = new Impose() {};
	public Object label;
	private int configType = 0;
	private HashMap<String, CommandProvider> cmd = null;
	private HashMap<String, String> helpDB = null;
	private HashMap<String, CommandFactory> cmdlib = null;
	private HashMap<String, String> helplib = null;
	private String list = "";
	private char comment = '#';
	private boolean doBasicSyn = true;
	private Vector<String> vec = new Vector<String>();
	private boolean dosem = true;
	private boolean isInit = true;
	private boolean ignoreCase = false;
	private boolean canReflect = false;
	private boolean doParse = false;
	private int ClpCode = 0;
	protected HashMap<String, Object> envVar;
	protected HashSet<CommandFactory> ImportedPackages;
	/**
	 * The Impose class is used to customize working.
	 * <br> Methods to implement : 
	 * <br> - IdNotFound : This method will be invoked when could not find a command to run.
	 * <br> - out : This method will be invoked when print any message.
	 */
	public interface Impose {
		public default IOCenter InOut() {
		return new IOCenter(new SprexorOstream((buf) -> {for(String s : buf)System.out.print(s);}) {

			@Override
			public void print(String msg) {
				System.out.print(msg);
			}
			@Override
			public void print(String msg, int Color) {
				print(msg);
			}
			@Override
			public void clear() {
				
			}
			@Override
			public void println(String msg) {
				System.out.println(msg);
			}
			@Override
			public void println(String msg, int Color) {
				println(msg);
			}
		},
		new SprexorIstream() {

			@Override
			public void prompt() {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void prompt(String msg) {
				char ch;
				try {
					System.out.println(msg);
					while((ch = (char) System.in.read()) != delimiter) {
						buffering(ch + "");
					}
				} catch (IOException e) {
					
				}
			}
			@Override
			public void prompt(String msg, int Color) {
				prompt(msg);
			}
			@Override
			public String getln() {
				Scanner scan = new Scanner(System.in);
				return scan.nextLine();
			}
			@Override
			public String getln(String msg) {
				System.out.println(msg);
				return getln();
			}
			@Override
			public String getln(String msg, int Color) {
				return getln(msg);
			}
		}
	);
		}
	}
	/**
	 * <br><b>void token(String, String)</b> : It will be invoked every for-loop repeatition, and parameter is token that is stacked character.
	 * <br>
	 * <br><b>void strmode_start()</b> : It will be invoked when string wrapper(' or ") is detected first.
	 * <br>
	 * <br><b>void strmode_tasking()</b> : It will be invoked when parsing the string.
	 * <br>
	 * <br><b>void strmode_end()</b> : It will be invoked when string wrapper is detected second.
	 * <br>
	 * <br><b>void variable_replacing(String name)</b> : It will be invoked when replace the existing Variable.
	 * <br>
	 * <br><b>void entrymode(String id, String context)</b> : It will be invoked when in the EntryMode.
	 * 
	 * @since 0.2.18
	 */
	public interface Reflection {
		public default void token(String value, String id) { }
		//
		public default void strmode_start() { }
		public default void strmode_end() { }
		public default void strmode_tasking() { }
		public default void variable_replacing(String name) { }
		public default void entrymode(String id, String context) { }
	}
	//
	// private methods.
	//
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
	private Object autoSelect(String id) {
		if(cmd.containsKey(id)) return cmd.get(id);
		else if(cmdlib.containsKey(id)) return cmdlib.get(id);
		else return null;
	}
	//
	//public methods.
	//
	public Sprexor() {
		 cmd = new HashMap<String, CommandProvider>();
		 envVar = new HashMap<String, Object>();
		 helpDB = new HashMap<String, String>();
		 cmdlib = new HashMap<String, CommandFactory>();
		 helplib = new HashMap<String, String>();
		 ImportedPackages = new HashSet<CommandFactory>();
	}
	/**
	 * importSprex is get command context(s) that created with class in this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param cmp : Other class that implemented with CommandProvider
	 * @see sprexor.CommandProvider
	 * @see sprexor.cosmos.BasicPackages
	 * @since 0.2.3
	 */
	public void importSprex(CommandFactory cmp) {
		if(configType != 0)return;
		if(cmd.containsKey(cmp.getCommandName())) return;
		if(cmp.referenceClass() == null) {
			cmdlib.put(cmp.getCommandName(), cmp);
			String hlpMsg = cmp.help();
			
			if(hlpMsg.startsWith("**USE_SMT_FORM**")) hlpMsg = Tools.SMT_FORM(hlpMsg.substring(16));
			helplib.put(cmp.getCommandName(), hlpMsg);
			list += cmp.getCommandName() + "\n";
			ImportedPackages.add(cmp);
		}else {
			CommandFactory[] re = cmp.referenceClass();
			for(CommandFactory cp : re) {
				String tmpname = cp.getCommandName();
				
				if(isExist(tmpname))return;
				else if(tmpname.matches("\n|\r|#|@"))return;
				if(cmd.containsKey(cp.getCommandName())) continue;
				
				cmdlib.put(tmpname, cp);
				String hlpMsg = cp.help();
				if(hlpMsg.startsWith("**USE_SMT_FORM**")) hlpMsg = Tools.SMT_FORM(hlpMsg.substring(16));
				
				helplib.put(tmpname, hlpMsg);
				list += tmpname + "\n";
				ImportedPackages.add(cp);
			}
		}
	}
	/**
	 * check whether name is exist.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param s - command name.
	 * @return return true if command name(s) is exist, other case false.
	 * @since 0.2.0
	 */
	@Activate_need
	public boolean isExist(String s) {
		if(configType != 2)return false;
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
	/**
	 * basic features are not assigned.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @since 0.2.11
	 */
	public void unBasicFeatures() {
		if(configType != 0)return;
		isInit = false;
	}
	
	/**
	 * send a instant message.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param str : message to send at IOCenter.
	 * @param type : message type
	 * @since 0.2.3
	 * @see sprexor.IOCenter.TYPE
	 */
	@Deprecated
	@Activate_need
	public void send(String str, IOCenter.TYPE type) {
		if(configType != 2)return;
		//impose.out(str, type);
	}
	/**
	 * send a instant message.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param str : message to send at IOCenter.
	 * @since 0.2.3
	 * @see sprexor.IOCenter.TYPE
	 */
	@Deprecated
	@Activate_need
	public void send(String str) {
		if(configType != 2)return;
		//impose.out(str, IOCenter.STDOUT);
	}
	/** Ignore upper or lower case of character. 
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * ex ) input : ABcD
	 *		detect : abcd
	 *@since 0.2.14
	 */
	public void ignoreUpperCase() {
		ignoreCase = true;
	}
	/**
	 * Acivate to use method exec, and cannot set value of properties of this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public void activate() throws SprexorException {
		if(configType != 0) return;
		if(!isInit)return;
		 cmd.put("help",new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					String Result = "";
					Component args = io.getComponent();
					if(isExist(args.gets(0))) {
							Result = helpDB.get(args.gets(0));
							if(Result == null) Result = helplib.get(args.gets(0));
					}else {
						Result = " ";
					}
					io.out.print(Result.trim());
					return 0;
				}
			 });
			 cmd.put("var", new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					if(!doBasicSyn) { 
						io.out.setType(TYPE.ERR);
						io.out.println("Syntax is not permitted.");
						return 1;
					}
					Component args = io.component;
					if(args.length() == 2) {	
						if(envVar.containsKey(args.gets(0)))envVar.replace(args.gets(0), args.gets(1));
						else envVar.put(args.gets(0), args.gets(1));
						io.out.println(args.gets(1));
					}else if(args.length() == 1) {
						if(envVar.containsKey(args.gets(0)))envVar.replace(args.gets(0), IOCenter.NO_VALUE);
						else envVar.put(args.gets(0),IOCenter.NO_VALUE);
						io.out.println("NO_VALUE");
					}else {
						io.out.println("USAGE : var [NAME] [VALUE*]\n\tDEFAULT VALUE : \"NO_VALUE\"");
					}
					return 0;
				}
			 });
			 cmd.put("echo", new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					String sum = "";
					String[] args = io.component.get();
					for(String str : args) {
						sum += str + " ";
					}
					io.out.println(sum);
					return 0;
				}
			});
			 
			 cmd.put("delete", new CommandProvider() {
					@Override
					public int code(IOCenter io) {
						int i = 0;
						Component args = io.component;
						if(args.isEmpty()) {
							io.out.println("");
							return 0;
						}
						for(String name : args) {
							Object returned = envVar.remove(name);
							if(returned == null)io.out.println(args.gets(i) + " : not existed variable.");
							i ++;
						}
						io.out.println("variable(s) deleted.");
						return 0;
					}
				});
			 
			 cmd.put("commands", new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					io.out.println(list);
					return 0;
				}
			 });
			 helpDB.put("help", "print this message.");
			 helpDB.put("var","Define or declare a variable.");
			 helpDB.put("echo", "print text.");
			 helpDB.put("delete", "usage : delete (var1) (var2)....(var n)");
			 helpDB.put("commands", "print all commands.");
			 list += "help\nvar\necho\ndelete\ncommands\n";
			if(!ImportedPackages.isEmpty()) {
				try {
					for(CommandFactory cf : ImportedPackages) {
						if(cf.getClass().getPackageName().startsWith("sprexor.cosmos")) {
							try {
							cf.getClass().getDeclaredField("cc").set(cf, cmd);
							cf.getClass().getDeclaredField("cl").set(cf, cmdlib);
							}catch(NoSuchFieldException nsfe) { }
						}
					}
				}catch(IllegalAccessException iae) {
					iae.printStackTrace();
					throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
				}
			}
			if(configType == 0)configType = 2;
	}
	
	@Deprecated
	public String getList() {
		//return list;
		return "";
	}
	/**
	 * This method Register a command. Registring command name shouldn't be included special character like *, ^ etc...
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @param str : command name that should not be contained special characters. 
	 * @param cp : CommandProvider
	 * @param hd : helping message is used in basic command - help.
	 * @since 0.1
	 * @see sprexor.CommandProvider
	 */
	public void register(String str, CommandProvider cp, String hd) {
		if(configType != 0)return;
		else if(isExist(str) || fil(str, " ", "'", "\"", "\\", "$", "*", "^", "(", ")", "{", "}", ":", "?", ";", "<", ">", ",", ".", "!", "#", "@", "&", "%", "~", "`", "[", "]", "\s") || str.contentEquals(""))return;
		else if(ignoreCase) str.toLowerCase();
		cmd.put(str, cp);
		list += str + "\n";
		helpDB.put(str, hd);
	}
	/**
	 * If this function is run, ignore ';' when parsing.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @since 0.2.11
	 */
	public void unSemicolon() {
		if(configType != 0)return;
		dosem = false;
	}
	
	/**
	 * 
	 * @param c : define annotate identifier character.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @since 0.1.5
	 */
	public void setComment(char c) {
		if(configType != 0)return;
		if(c != '\0')comment = c;
	}
	
	/**
	 * 
	 * @param b : true - on, false - off.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @since 0.2.0
	 */
	public void useSyntax(boolean b) {
		if(configType != 0)return;
		doBasicSyn = b;
	}
	
	/**@deprecated
	 * It is method to control this system. 
	 * <hr>--- Key Names ---
	 * <ul>
	 * <li>entry_on : Enter the EntryMode.</li>
	 * <li>entry_off : Exit the EntryMode.</li>
	 * <li>break_parse : Stop parsing. This Key recommends to use for reflection.</li>
	 * </ul>
	 * <hr>
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param value : the key value.
	 * @since 0.2.7
	 */
	@Deprecated
	@Activate_need
	public void call(String value) {
		if(configType != 2)return;
		switch(value) {
		case "break_parse" :
			doParse = false;
			ClpCode = 1;
		default:
			break;
		}
	}
	/**set Property to use reflection.
	 * If not allow to use reflection, Sprexor won't use Reflection resource although you implemented methods.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 */
	public void allowReflection() {
		if(configType != 0) return;
		canReflect = true;
	}
	/**
	 * execute command as non-parse.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param id : command name
	 * @param args : arguments
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	@Activate_need
	public void exec(String id, String[] args) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
		
		if(!isExist(id))throw new SprexorException(SprexorException.CMD_NOTFOUND, id);
		CommandProvider obj = cmd.get(id);
		CommandFactory cfObj = cmdlib.get(id);
		IOCenter res = impose.InOut();
		args = trimArr(args);
		if(obj != null) obj.code(res);
		else cfObj.code(res, this);
	}
	/**
	 * It can execute line with powerful string parser,
	 * <br> and it will be run that you configure. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param com : command string to execute
	 * @throws SprexorException 
	 * @since 0.1
	 */
	@Activate_need
	public int exec(String com) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		String commentStr = comment + "";
		boolean varMode = false;
		com = com.trim();
		String id = com.split(" ")[0].trim();
		if (ignoreCase) id = id.toLowerCase();
		if(com.trim().contentEquals("")) {
			return -1;
		}else if(com.startsWith(commentStr)) {
			return -1;
		}else if(!isExist(id)) {
			throw new SprexorException(SprexorException.CMD_NOTFOUND, com);
		}else if(id.indexOf(";") != -1) {
			String[] tmpo = com.split(";");
			if(tmpo.length > 1)exec(com.split(";")[1]);
			if(tmpo.length == 0) {throw new SprexorException(SprexorException.EXPRSS_ERR, "cannot paese : " + com);
		}
		return cmd.get(id) != null ? cmd.get(tmpo[0]).code(impose.InOut()) : cmdlib.get(tmpo[0]).code(impose.InOut(), this);
	}
		com = com.substring(com.split(" ")[0].length());
		String[] comar = com.split(""),
				args = new String[comar.length];
		short mod = 0,
				count = 0,
				pr = 0,
				smod = 0;
		int allCount = 0;
		boolean[] wra = new boolean[comar.length];
		boolean lastSpace = false;
		boolean cmtMode = false;
		String cache = "";
		doParse = true;
		//------------------Parse start---------------------
		for(;allCount < comar.length && doParse; allCount ++) {
			String c = comar[allCount];
			if(canReflect) reflect.token(c, id);
			if(mod + smod == 1 && canReflect) reflect.strmode_tasking();
			if(cmtMode) {
				if(c.contentEquals(";") && dosem) {
					cmtMode = false;
					
					if((!lastSpace) && !cache.trim().contentEquals("")) {
						args[count ++] = cache;
						cache = "";
					}
					break;
				}
				continue;
			}
			
			if(c.contentEquals(";") && mod + smod == 0 && dosem) {
				
				if(varMode) {
					if(envVar.get(cache.trim().substring(1)) != null) {
						args[count++] = envVar.get(cache.trim().substring(1)) + "";
						wra[count] = false;
						cache = "";
					}else {
						if(cache.trim().contentEquals("@")) {
							throw new SprexorException(SprexorException.EXPRSS_ERR, "variable name is empty.");
						} else throw new SprexorException(SprexorException.VARIABLE_ERR, cache.trim());
					}
				}
				
				if((!lastSpace) && !cache.trim().contentEquals("")) {
					args[count ++] = cache;
					cache = "";
				}
				break;
			}
			
			if(!varMode) {
				if(mod + smod == 1 && c.contentEquals("\\") && pr == 0) {
					pr = 1;
					continue;
				}
				
				if(c.contentEquals(" ") && smod == 0 && mod == 0) {
					if(cache.trim().isEmpty())continue;
					args[count ++] = cache.trim();
					cache = "";
					lastSpace = true; 
					continue;
				}
				
				
				/*if(c.contentEquals("\"")) {
					boolean asdf = false;
					while(allCount < comar.length) {
						mod = 1;
						allCount ++;
						c = comar[allCount];
						if(c == "\\") {
							if(asdf) {
								cache += "\\";
							}else {
								asdf = true;
								continue;
							}
						}else if(c == "\"") {
							break;
						}
					}
				}else if(c.contentEquals("'")) {
					boolean asdf = false;
					while(allCount < comar.length) {
						
					}
				}*/
				
				
				// escape
				if(c.contentEquals("\"")) {
					if(mod == 0 && smod == 0) {
						wra[count] = true;
						mod = 1;
						if(canReflect) reflect.strmode_start();
					}else {
						if(pr == 1) {
							pr = 0;
							cache += c;
							if(canReflect) reflect.strmode_end();
							continue;
						}else if(smod == 1) {
							cache +=  c;
							mod = 0;
							continue;
						}
						mod = 0;
					}
					continue;
				}else if(c.contentEquals("'")) {
					if(mod == 0 && smod == 0) {
						wra[count] = true;
						smod = 1;
						if(canReflect) reflect.strmode_start();
					}else {
						if(pr == 1) {
							pr = 0;
							cache += c;
							if(canReflect) reflect.strmode_end();
							continue;
						}else if(mod == 1) {
							cache +=  c;
							smod = 0;
							continue;
						}
						smod = 0;
					}
					continue;
				}else if(pr == 1) {
					if(c.contentEquals("n")){
						if(mod == 0 && smod == 0) {
							wra[count] = true;
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
							wra[count] = true;
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
					}
					
					else {
						cache += "\\";
						pr = 0;
						continue;
					}
				}
			}
			
			lastSpace  = false;
				//---------------------------------------------------------------------------------------------
				//---------------------------------------------------------------------------------------------
				//--------------------------	Syntax	-------------------------------------------------------
			if(doBasicSyn) {
				if(smod + mod + pr == 0) {
					if(c.contentEquals(comment + "") && mod == 0) {
						cmtMode = true;
						args[count++] = cache;
						wra[count] = false;
						cache = "";
						continue; // ---------------- comment
					}
					if(c.contentEquals("@") && !varMode) {			//----------------- variable 
						varMode = true;
					}else {
						if(varMode && c.contentEquals("@")) {
							throw new SprexorException(SprexorException.EXPRSS_ERR, "Invalid expression detected - " + count);
						}else if(varMode){
							if(c.contentEquals(" ")) {
								varMode = false;
								if(envVar.get(cache.trim().substring(1)) != null) {
									System.out.println(11);
									args[count++] = (envVar.get(cache.trim().substring(1)) + "").trim();
									if(canReflect) reflect.variable_replacing(cache.trim().substring(1));
									wra[count] = false;
									cache = "";
								}else {
									if(cache.trim().contentEquals("@")) {
										throw new SprexorException(SprexorException.EXPRSS_ERR, "Variable name cannot be voided.");
									}
									else throw new SprexorException(SprexorException.VARIABLE_ERR, "This is not exist : " + cache.trim().substring(1));
								}
							}
						}
					}
				}
			}
			cache += c;
		} 									// Parse Ended.
		doParse = false;
		switch(ClpCode) {
		case 1 : 
			ClpCode = 0;
		break;
		default : break;
		}
		if(mod != 0 || smod != 0 || pr != 0) {
			throw new SprexorException(SprexorException.EXPRSS_ERR, 
					"invalid literal syntax.");
		}
		if(!cache.isBlank()) {
			if(varMode) {
				String iii = envVar.get(cache.trim().substring(1)) != null ? (args[count] = envVar.get(cache.trim().substring(1)).toString()) : "";
			} else args[count] = cache;
		}
		IOCenter res = impose.InOut();
		args = trimArr(args);
		wra = trimArr(wra, count);
		res.component = new Component(args, wra);
		return (cmd.containsKey(id) ? cmd.get(id).code(res) : cmdlib.get(id).code(res, this));
	}
	/**
	 * It run a line with powerful string parser.
	 * <br> And it will be run that configured. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param input
	 * @return 
	 * @since 0.2.18
	 * @throws SprexorException
	 */
	@Activate_need
	synchronized public int run(String input) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		boolean varMode = false;
		String commentStr = comment + "";
		input = input.trim();
		if(input.isEmpty()) return 0;
		String id = "";
		if(input.startsWith(commentStr)) {
			return 0;
		}
		char[] chars = input.toCharArray();
		String[] args = new String[chars.length];
		short mod = 0,
				count = 0,
				pr = 0,
				smod = 0;
		int allCount = 0;
		int le = chars.length;
		StringBuilder cache = new StringBuilder();
		int capacity = cache.capacity();
		boolean[] wra = new boolean[le];
		boolean lastSpace = false;
		boolean cmtMode = false;
		boolean firstTime = true;
		doParse = true;
		for(;allCount < le && doParse; allCount ++) {
			char element = chars[allCount];
			if(canReflect) reflect.token(element + "", id);
			if(mod + smod == 1 && canReflect) reflect.strmode_tasking();
			if(cmtMode) {
				if(element == ';' && dosem) {
					cmtMode = false;
					if((!lastSpace) && !cache.toString().isEmpty()) {
						args[count ++] = cache.toString();
						cache.delete(0, cache.capacity());
					}
					break;
				}
				continue;
			}
			if(element == ';' && mod + smod == 0 && dosem) {
				if(varMode) {
					if(envVar.get(cache.substring(1)) != null) {
						args[count++] = envVar.get(cache.substring(1)) + "";
						wra[count] = false;
						cache.delete(0, cache.capacity());
					}else {
						if(cache.toString().contentEquals("@")) {
							throw new SprexorException(SprexorException.EXPRSS_ERR, "variable name is empty.");
						}
						else throw new SprexorException(SprexorException.VARIABLE_ERR, cache.toString());
					}
				}
				
				if((!lastSpace) && !cache.toString().isEmpty()) {
					args[count ++] = cache.toString();
					cache.delete(0, cache.capacity());
				}
				break;
			}
			if(!varMode) {
				if(mod + smod == 1 && element == '\\' && pr == 0) {
					pr = 1;
					continue;
				}
				if(element == ' ' && smod == 0 && mod == 0) {
					if(cache.toString().isEmpty())continue;
					if(firstTime) {
						id = cache.toString();
						if(ignoreCase) id = id.toLowerCase();
						if(!isExist(id)) {
							throw new SprexorException(SprexorException.CMD_NOTFOUND, input);
						}
						if(id.indexOf(";") != -1 && dosem) {
							StringBuilder sb = new StringBuilder();
							StringTokenizer tkn = new StringTokenizer(input, ";");
							while(tkn.hasMoreTokens()) {
								sb.append(tkn.nextToken());
							}
							run(sb.toString());
							return 0;
						}
						firstTime = false;
					} else args[count ++] = cache.toString();
					cache.delete(0, capacity);
					lastSpace = true; 
					continue;
				}
				if(element == '\"') {
					if(mod == 0 && smod == 0) {
						wra[count] = true;
						mod = 1;
						if(canReflect) reflect.strmode_start();
					}else {
						if(pr == 1) {
							pr = 0;
							cache.append(element);
							if(canReflect) reflect.strmode_end();
							continue;
						}else if(smod == 1) {
							cache.append(element);
							mod = 0;
							continue;
						}
						args[count ++] = cache.toString();
						cache.delete(0, capacity);
						mod = 0;
					}
					continue;
				}else if(element == '\'') {
					if(mod == 0 && smod == 0) {
						wra[count] = true;
						smod = 1;
						if(canReflect) reflect.strmode_start();
					}else {
						if(pr == 1) {
							pr = 0;
							cache.append(element);
							if(canReflect) reflect.strmode_end();
							continue;
						}else if(mod == 1) {
							cache.append(element);
							smod = 0;
							continue;
						}
						args[count ++] = cache.toString();
						cache.delete(0, capacity);
						smod = 0;
					}
					continue;
				}else if(pr == 1) {
					if(element == 'n'){
						if(mod == 0 && smod == 0) {
							wra[count] = true;
							smod = 1;
						}else {
							if(pr == 1) {
								pr = 0;
								cache.append('\n');
								continue;
							}else if(mod == 1) {
								cache.append('\n');
							}
							smod = 0;
						}
						continue;
					}else if(element == 't'){
						if(mod == 0 && smod == 0) {
							wra[count] = true;
							smod = 1;
						}else {
							if(pr == 1) {
								pr = 0;
								cache.append(element);
								continue;
							}else if(mod == 1) {
								cache.append(element);
							}
							smod = 0;
						}
						continue;
					}
					
					else {
						cache.append('\\');
						pr = 0;
						continue;
					}
				}
			}
			lastSpace  = false;
			if(doBasicSyn) {
				if(smod + mod + pr == 0) {
					
					if(element == comment && mod == 0) {
						cmtMode = true;
						args[count++] = cache.toString();
						wra[count] = false;
						cache.delete(0, capacity);
						continue;
					}
					if(element == '@' && !varMode) {
						varMode = true;
					}else {
						if(varMode == true && element == '@') {
							throw new SprexorException(SprexorException.EXPRSS_ERR, "@@");
						}else if(varMode){
							if(element == ' ') {
								varMode = false;
								if(envVar.get(cache.substring(1)) != null) {
									args[count++] = (envVar.get(cache.substring(1)) + "").trim();
									if(canReflect) reflect.variable_replacing(cache.substring(1));
									wra[count] = false;
									cache.delete(0, capacity);
								}else {
									if(cache.toString().isEmpty()) {
										throw new SprexorException(SprexorException.EXPRSS_ERR, "Variable name cannot be voided.");
									}
									throw new SprexorException(SprexorException.VARIABLE_ERR,cache.toString());
								}
							}
						}
					}
				}
			}
			cache.append(element);
		}
		doParse = false;
		if(count == 0) {
			id = cache.toString();
			if(id.indexOf(";") != -1 && dosem) {
				StringBuilder sb = new StringBuilder();
				StringTokenizer tkn = new StringTokenizer(input, ";");
				while(tkn.hasMoreTokens()) {
					sb.append(tkn.nextToken());
				}
				return run(sb.toString());
			}
			throw new SprexorException(SprexorException.CMD_NOTFOUND, input);
			} else args[count ++] = cache.toString();
		switch(ClpCode) {
		case 1 : 
			ClpCode = 0;
			break;
		default : break;
		}
		if(mod != 0 || smod != 0 || pr != 0) {
			throw new SprexorException(SprexorException.EXPRSS_ERR, 
					"invalid literal syntax.");
		}
		
		IOCenter res = impose.InOut();
		args = trimArr(args);
		wra = trimArr(wra, count);
		res.component = new Component(args, wra);
		if(cmd.containsKey(id)) return cmd.get(id).code(res);
		else return cmdlib.get(id).code(res, this);
	}
	/**
	 * eval returns outcome to string.
	 * @param com - string
	 * @return result by string.
	 */
	@Activate_need
	public int eval(String com) {
		String[] args = Component.Parse(com);
		Object obj = autoSelect(args[0]);
		IOCenter res = impose.InOut();
		res.component = new Component(Tools.cutArr(args, 1));
		if(obj instanceof CommandProvider) {
			return cmd.get(args[0]).code(res);
			
		} else if(obj instanceof CommandFactory) {
			return cmdlib.get(args[0]).code(res, this);
		}
		return -1;
	}
}