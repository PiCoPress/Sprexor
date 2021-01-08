package sprexor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
/**
 * Sprexor : the String Parser & Executor 0.2.18-alpha6 Venom
 *  copyright(c)  2020  by PICOPress, All rights reserved.
 */
public class Sprexor {
	//
	// Internal Variables.
	//
	@Deprecated protected Vector<Object[]> MessageLog = null;
	@Deprecated protected Object[] recentMessage = null;
	@Deprecated protected Vector<Object[]> blockMessage = new Vector<Object[]>();
	public static final String VERSION = "0.2.18-alpha6";
	public static final String CODENAME = "Venom";
	public Reflection reflect;
	public Impose impose;
	public Object label;
	private int configType = 0;
	private HashMap<String, CommandProvider> cmd = null;
	private HashMap<String, String> helpDB = null;
	private HashMap<String, CommandFactory> cmdlib = null;
	private HashMap<String, String> helplib = null;
	private String list = "";
	private char comment = '#';
	private boolean nextFlag = false;
	private boolean doBasicSyn = true;
	private Vector<String> vec = new Vector<String>();
	private boolean errorIn = false;
	private boolean dosem = true;
	private boolean isInit = true;
	private boolean ignoreCase = false;
	private boolean canReflect = false;
	private boolean doParse = false;
	private int ClpCode = 0;
	protected HashMap<String, Object> envVar;
	protected boolean entryMode = false;
	protected boolean doEntry = false;
	protected String entryId = "";
	protected HashSet<CommandFactory> ImportedPackages;
	
	/**
	 * The Impose class is used to customize working.
	 * <br> Methods to implement : 
	 * <br> - IdNotFound : This method will be invoked when could not find a command to run.
	 * <br> - out : This method will be invoked when print any message.
	 */
	public interface Impose {
		public default String IdNotFound(String a) { return ""; }
		public default void out(String str, IOCenter.TYPE type) { }
	}
	/**
	 * <b>Component before_invoked(String, Component)</b> : It will be invoked before run the parsed String.
	 * <br>
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
		public default Component before_invoked(String id, Component comp) { return comp; }
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
	
	private void logger(String oo, IOCenter.TYPE ii) {
		impose.out(oo.trim(), ii);
	}
	private void logger(IOCenter ioc) { impose.out(ioc.Msg, ioc.status); }
	private Object autoSelect(String id) {
		if(cmd.containsKey(id)) return cmd.get(id);
		else if(cmdlib.containsKey(id)) return cmdlib.get(id);
		else return null;
	}
	//
	//public methods.
	//
	public Sprexor() {
		MessageLog = new Vector<Object[]>();
		 cmd = new HashMap<String, CommandProvider>();
		 recentMessage = new Object[2];
		 envVar = new HashMap<String, Object>();
		 helpDB = new HashMap<String, String>();
		 cmdlib = new HashMap<String, CommandFactory>();
		 helplib = new HashMap<String, String>();
		 ImportedPackages = new HashSet<CommandFactory>();
	}
	
	/**
	 * importSprex is get command context(s) that created with class in this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
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
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
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
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @since 0.2.11
	 */
	public void unBasicFeatures() {
		if(configType != 0)return;
		isInit = false;
	}
	
	/**
	 * send a instant message.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param str : message to send at IOCenter.
	 * @param type : message type
	 * @since 0.2.3
	 * @see sprexor.IOCenter.TYPE
	 */
	@Activate_need
	public void send(String str, IOCenter.TYPE type) {
		if(configType != 2)return;
		impose.out(str, type);
	}
	/**
	 * send a instant message.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param str : message to send at IOCenter.
	 * @since 0.2.3
	 * @see sprexor.IOCenter.TYPE
	 */
	@Activate_need
	public void send(String str) {
		if(configType != 2)return;
		impose.out(str, IOCenter.STDOUT);
	}
	/** Ignore upper or lower case of character. 
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * ex ) input : ABcD
	 *		detect : abcd
	 *@since 0.2.14
	 */
	public void ignoreUpperCase() {
		ignoreCase = true;
	}
	/**
	 * Acivate to use method exec, and cannot set value of properties of this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public void activate() throws SprexorException {
		if(configType != 0) return;
		if(!isInit)return;
		 cmd.put("help",new CommandProvider() {
				@Override
				public IOCenter code(Component args) {
					String Result = "";
					if(isExist(args.gets(0))) {
							Result = helpDB.get(args.gets(0));
							if(Result == null) Result = helplib.get(args.gets(0));
					}else {
						Result = " ";
					}
					return new IOCenter(Result.trim(), IOCenter.STDOUT);
				}
				@Override
				public String error(Exception e) {
					return "1";
				}
				@Override 
				public IOCenter emptyArgs() {
					return new IOCenter("blanked name.");
				}
			 });
			 cmd.put("var", new CommandProvider() {
				@Override
				public IOCenter code(Component args) {
					if(!doBasicSyn) return new IOCenter("Syntax is not permitted.", IOCenter.ERR);
					if(args.length() == 2) {	
						if(envVar.containsKey(args.gets(0)))envVar.replace(args.gets(0), args.gets(1));
						else envVar.put(args.gets(0), args.gets(1));
						return new IOCenter(args.gets(1), IOCenter.STDOUT);
					}else if(args.length() == 1) {
						if(envVar.containsKey(args.gets(0)))envVar.replace(args.gets(0), IOCenter.NO_VALUE);
						else envVar.put(args.gets(0),IOCenter.NO_VALUE);
						return new IOCenter("NO_VALUE", IOCenter.NO_VALUE);
					}else {
						return new IOCenter(emptyArgs().toString(), IOCenter.CMT);
					}
				}
				
				@Override
				public IOCenter emptyArgs() {
					return new IOCenter("USAGE : var [NAME] [VALUE*]\n\tDEFAULT VALUE : \"NO_VALUE\"");
				}
			 });
			 cmd.put("echo", new CommandProvider() {
				@Override
				public IOCenter code(Component args) {
					String sum = "";
					for(String str : args) {
						sum += str + " ";
					}
					return new IOCenter(sum, IOCenter.STDOUT, "STRINGLABEL:FS_OUT/STDOUT");
				}
				
				@Override
				public IOCenter emptyArgs() {
					return new IOCenter("", "STRINGLABEL:FS_OUT/STDOUT");
				}
			});
			 
			 cmd.put("delete", new CommandProvider() {
					@Override
					public IOCenter code(Component args) {
						int i = 0;
						for(String name : args) {
							Object returned = envVar.remove(name);
							if(returned == null)logger(args.gets(i) + " : not existed variable.", TYPE.WARN);
							i ++;
						}
						return new IOCenter("variable(s) deleted.");
					}
					
					@Override
					public IOCenter emptyArgs() {
						return new IOCenter("variable name is blank.");
					}
				});
			 
			 cmd.put("commands", new CommandProvider() {

				@Override
				public IOCenter code(Component args) {
					return new IOCenter(list.trim());
				}
				
				@Override
				public IOCenter emptyArgs() {
					return new IOCenter(list.trim());
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
	 * If called this method, Error is thrown as a Exception rather than console print.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @see sprexor.SprexorException
	 */
	public void error_strict() {
		if(configType != 0)return;
		errorIn = true;
	}
	/**
	 * This method Register a command. Registring command name shouldn't be included special character like *, ^ etc...
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
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
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @since 0.2.11
	 */
	public void unSemicolon() {
		if(configType != 0)return;
		dosem = false;
	}
	
	/**
	 * 
	 * @param str : define annotate identifier character.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @since 0.1.5
	 */
	public void setComment(char c) {
		if(configType != 0)return;
		if(c != '\0')comment = c;
	}
	
	/**
	 * 
	 * @param b : true - on, false - off.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 * @since 0.2.0
	 */
	public void useSyntax(boolean b) {
		if(configType != 0)return;
		doBasicSyn = b;
	}
	
	/**
	 * It is method to control this system. 
	 * <hr>--- Key Names ---
	 * <br><li>entry_on : Enter the EntryMode.
	 * <br><li>entry_off : Exit the EntryMode.
	 * <br><li>break_parse : Stop parsing. This Key recommends to use for reflection.
	 * <hr>
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param value : the key value.
	 * @since 0.2.7
	 */
	@Activate_need
	public void call(String value) {
		if(configType != 2)return;
		switch(value) {
		case "entry_on":
			doEntry = true;
		break;
		case "entry_off":
			doEntry = false;
			entryId = "";
		break;
		case "break_parse" :
			doParse = false;
			ClpCode = 1;
		default:
			break;
		}
	}
	/**set Property to use reflection.
	 * If not allow to use reflection, Sprexor won't use Reflection resource although you implemented methods.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span><b>
	 */
	public void allowReflection() {
		if(configType != 0) return;
		canReflect = true;
	}
	/**
	 * execute command as non-parse.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param id : command name
	 * @param args : arguments
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	@Activate_need
	public void exec(String id, String[] args) throws SprexorException {
		if(configType != 2) {
			if(!errorIn)logger("sprexor was not prepared yet.", IOCenter.ERR);
			else throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
			return;
		}
		
		if(!isExist(id))throw new SprexorException(SprexorException.CMD_NOTFOUND, id);
		IOCenter res;
		CommandProvider obj = cmd.get(id);
		CommandFactory cfObj = cmdlib.get(id);
		args = trimArr(args);
		try {
			if(obj != null) res = obj.code(new Component(args, new boolean[args.length]));
			else res = cfObj.code(new Component(args, new boolean[args.length]), this);
			if(!res.Msg.isBlank()) logger(res);
		}catch(Exception e) {
			logger(obj != null ? obj.error(e) : cfObj.error(e), IOCenter.ERR);
		}
	}
	@Activate_need
	public void exec(String id, String[] args, boolean[] isWrapped) throws SprexorException {
		if(configType != 2) {
			if(!errorIn)logger("sprexor was not prepared yet.", IOCenter.ERR);
			else throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
			return;
		}
		
		if(!isExist(id))throw new SprexorException(SprexorException.CMD_NOTFOUND, id);
		IOCenter res;
		CommandProvider obj = cmd.get(id);
		CommandFactory cfObj = cmdlib.get(id);
		Component comval = new Component(args, isWrapped);
		args = trimArr(args);
		try {
			if(obj != null) res = obj.code(comval);
			else res = cfObj.code(comval, this);
			if(!res.Msg.isBlank()) logger(res);
		}catch(Exception e) {
			logger(obj != null ? obj.error(e) : cfObj.error(e), IOCenter.ERR);
		}
	}
	
	/**
	 * It can execute line with powerful string parser,
	 * <br> and it will be run that you configure. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param com : command string to execute
	 * @throws SprexorException 
	 * @since 0.1
	 */
	@Activate_need
	public void exec(String com) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		if(entryMode) {
			if(!doEntry) {
				entryMode = false;
				entryId = "";
			}else {
				reflect.entrymode(entryId, com);
				logger(cmd.get(entryId) != null ? cmd.get(entryId).EntryMode(com) : cmdlib.get(entryId).EntryMode(com), IOCenter.STDOUT);
				return;
			}
		}
		String commentStr = comment + "";
		boolean varMode = false;
		com = com.trim();
		String id = com.split(" ")[0].trim();
		if (ignoreCase) id = id.toLowerCase();
		if(com.contentEquals("")) {
			return;
		}else if(com.startsWith(commentStr)) {
			return;
		}else if(!isExist(id)) {
			if(!errorIn) { //how to throw error
				if(impose.IdNotFound("").isEmpty()) logger(id + " : command not found.", IOCenter.ERR);
				else logger(impose.IdNotFound(id), IOCenter.ERR); // It can be defined by "bound" Function.
			}
			else throw new SprexorException(SprexorException.CMD_NOTFOUND, com);
			return;
		}else if(id.indexOf(";") != -1) {
			String[] tmpo = com.split(";");
			if(tmpo.length > 1)exec(com.split(";")[1]);
			if(tmpo.length == 0) {
				if(!errorIn) logger("cannot parse : " + com, IOCenter.ERR);
				else throw new SprexorException(SprexorException.EXPRSS_ERR, "cannot paese : " + com);
				return;
			}
			logger(cmd.get(id) != null ? cmd.get(tmpo[0]).emptyArgs() : cmdlib.get(tmpo[0]).emptyArgs(this));
			return;
		}
		
		if(com.indexOf(" ") == -1) {
			logger(cmd.get(id) != null ? cmd.get(id).emptyArgs().Msg : cmdlib.get(id).emptyArgs(this).Msg, IOCenter.STDOUT);
			return;
		}
		com = com.substring(id.length() + 1) + " ";
		if(id.indexOf(comment) != -1) { // When encountered Comment.
			IOCenter res = null;
			CommandProvider obj = cmd.get(id);
			CommandFactory cfObj = cmdlib.get(id);
			try {
				if(obj != null) res = obj.emptyArgs();
				else res = cfObj.emptyArgs(this);
				logger(res);
			}catch(Exception e) {
				logger(obj != null ? obj.error(e) : cfObj.error(e), IOCenter.ERR);
			}
			return;
		}
		String[] comar = com.split(""),
				args = new String[comar.length];
		String nextStr = "";
		short mod = 0,
				count = 0,
				pr = 0,
				smod = 0;
		int allCount = 0;
		if(nextFlag) nextFlag = false;
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
					nextFlag = true;
					nextStr = com.substring(allCount + 1);
					
					if((!lastSpace) && !cache.trim().contentEquals("")) {
						args[count ++] = cache;
						cache = "";
					}
					break;
				}
				continue;
			}
			
			if(c.contentEquals(";") && mod + smod == 0 && dosem) {
				nextStr = com.substring(allCount + 1);
				nextFlag = true;
				
				if(varMode) {
					if(envVar.get(cache.trim().substring(1)) != null) {
						args[count++] = envVar.get(cache.trim().substring(1)) + "";
						wra[count] = false;
						cache = "";
					}else {
						if(cache.trim().contentEquals("@")) {
							if(!errorIn)logger("variable name is empty.",IOCenter.ERR);
							else throw new SprexorException(SprexorException.EXPRSS_ERR, "variable name is empty.");
							return;
						}
						if(!errorIn)logger("could not find variable : " + cache.trim().substring(1),IOCenter.ERR);
						else throw new SprexorException(SprexorException.VARIABLE_ERR, cache.trim());
						return;
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
						args[count ++] = cache.trim();
						cache = "";
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
						args[count ++] = cache.trim();
						cache = "";
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
						if(varMode == true && c.contentEquals("@")) {
							if(!errorIn)logger("Invalid expression detected - " + count,IOCenter.ERR);
							else throw new SprexorException(SprexorException.EXPRSS_ERR, "@@");
							return;
						}else if(varMode){
							if(c.contentEquals(" ")) {
								varMode = false;
								if(envVar.get(cache.trim().substring(1)) != null) {
									args[count++] = (envVar.get(cache.trim().substring(1)) + "").trim();
									if(canReflect) reflect.variable_replacing(cache.trim().substring(1));
									wra[count] = false;
									cache = "";
								}else {
									if(cache.trim().contentEquals("@")) {
										if(!errorIn)logger("Variable name cannot be voided.",IOCenter.ERR);
										else throw new SprexorException(SprexorException.EXPRSS_ERR, "Variable name cannot be voided.");
										return;
									}
									if(!errorIn)logger("This is not exist : " + cache.trim().substring(1),IOCenter.ERR);
									else throw new SprexorException(SprexorException.VARIABLE_ERR,cache.trim());
									return;
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
		case 0: break;
		case 1 : 
			if(!errorIn) logger("Warning : Sprexor wasn't closed normally.", IOCenter.WARN);
			else {
				logger("Warning : Sprexor wasn't closed normally.", IOCenter.WARN);
				ClpCode = 0;
				return;
			}
			break;
		default : break;
		}
		if(mod != 0 || smod != 0 || pr != 0) {
			if(!errorIn) logger("Error : invalid literal syntax.", IOCenter.ERR);
			else throw new SprexorException(SprexorException.EXPRSS_ERR, 
					"invalid literal syntax.");
			return;
		}
		
		IOCenter res = null;
		CommandProvider obj = cmd.get(id);
		CommandFactory cfObj = cmdlib.get(id);
		args = trimArr(args);
		wra = trimArr(wra, count);
		Component comval = new Component(args, wra);
		try {
			if(canReflect)comval = reflect.before_invoked(id, comval);
			if(!entryMode) {
				if(obj != null) res = obj.code(comval);
				else res = cfObj.code(comval, this);
				if(doEntry) {
					entryMode = true;
					entryId = id;
				}
			}
			if(obj != null) {
				if(obj.EntryMode(null) != null || doEntry) {
					entryMode = true;
					entryId = id;
					doEntry = true;
				}
			}else {
				if(cfObj.EntryMode(null) != null || doEntry) {
					entryMode = true;
					entryId = id;
					doEntry = true;
				}
			}
			
		}catch(Exception e) {
			if(obj != null)res = new IOCenter(obj.error(e), IOCenter.ERR);
			else res = new IOCenter(cfObj.error(e), IOCenter.ERR);
		}
		
		if(res == null)res = new IOCenter("", IOCenter.UNKNOWN);
		else if(res.status == null)res.status = IOCenter.UNKNOWN;
		
		if(res != null) if(!res.Msg.isBlank()) logger(res.Msg,res.status);
		if(nextFlag) {
			exec(nextStr);
		}
	}
	/**
	 * It run a line with powerful string parser.
	 * <br> And it will be run that configured. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span><b>
	 * @param input
	 * @since 0.2.18
	 * @throws SprexorException
	 */
	@Activate_need
	synchronized public void run(String input) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		if(entryMode) {
			if(!doEntry) {
				entryMode = false;
				entryId = "";
			}else {
				reflect.entrymode(entryId, input);
				logger(cmd.get(entryId) != null ? cmd.get(entryId).EntryMode(input) : cmdlib.get(entryId).EntryMode(input), IOCenter.STDOUT);
				return;
			}
		}
		boolean varMode = false;
		String commentStr = comment + "";
		input = input.trim();
		if(input.isEmpty()) return;
		String id = "";
		if(input.startsWith(commentStr)) {
			return;
		}
		char[] chars = input.toCharArray();
		String[] args = new String[chars.length];
		String nextStr = "";
		short mod = 0,
				count = 0,
				pr = 0,
				smod = 0;
		int allCount = 0;
		int le = chars.length;
		StringBuilder cache = new StringBuilder();
		int capacity = cache.capacity();
		if(nextFlag) nextFlag = false;
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
					nextFlag = true;
					nextStr = input.substring(allCount + 1);
					if((!lastSpace) && !cache.toString().isEmpty()) {
						args[count ++] = cache.toString();
						cache.delete(0, cache.capacity());
					}
					break;
				}
				continue;
			}
			if(element == ';' && mod + smod == 0 && dosem) {
				nextStr = input.substring(allCount + 1);
				nextFlag = true;
				
				if(varMode) {
					if(envVar.get(cache.substring(1)) != null) {
						args[count++] = envVar.get(cache.substring(1)) + "";
						wra[count] = false;
						cache.delete(0, cache.capacity());
					}else {
						if(cache.toString().contentEquals("@")) {
							if(!errorIn)logger("variable name is empty.",IOCenter.ERR);
							else throw new SprexorException(SprexorException.EXPRSS_ERR, "variable name is empty.");
							return;
						}
						if(!errorIn)logger("could not find variable : " + cache.substring(1),IOCenter.ERR);
						else throw new SprexorException(SprexorException.VARIABLE_ERR, cache.toString());
						return;
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
							if(!errorIn) {
								if(impose.IdNotFound("").isEmpty()) logger(id + " : command not found.", IOCenter.ERR);
								else logger(impose.IdNotFound(id), IOCenter.ERR);
							} else throw new SprexorException(SprexorException.CMD_NOTFOUND, input);
						}
						if(id.indexOf(comment) != -1) {
							IOCenter res = null;
							CommandProvider obj = cmd.get(id);
							CommandFactory cfObj = cmdlib.get(id);
							try {
								if(obj != null) res = obj.emptyArgs();
								else res = cfObj.emptyArgs(this);
								logger(res);
							}catch(Exception e) {
								logger(obj != null ? obj.error(e) : cfObj.error(e), IOCenter.ERR);
							}
							return;
						}
						if(id.indexOf(";") != -1 && dosem) {
							if(id.charAt(0) == ';') 
								if(!errorIn) 
									logger(" " + input, IOCenter.ERR);
								//
							//
							else throw new SprexorException(SprexorException.EXPRSS_ERR, "Expression incorrect : " + input);
							StringBuilder sb = new StringBuilder();
							StringTokenizer tkn = new StringTokenizer(input, ";");
							logger(cmd.containsKey(id) ? cmd.get(id).emptyArgs() :
								cmdlib.get(id).emptyArgs(this));
							while(tkn.hasMoreTokens()) {
								sb.append(tkn.nextToken());
							}
							run(sb.toString());
							return;
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
							if(!errorIn)logger("Invalid expression detected - " + count,IOCenter.ERR);
							else throw new SprexorException(SprexorException.EXPRSS_ERR, "@@");
							return;
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
										if(!errorIn)logger("Variable name cannot be voided.",IOCenter.ERR);
										else throw new SprexorException(SprexorException.EXPRSS_ERR, "Variable name cannot be voided.");
										return;
									}
									if(!errorIn)logger("This is not exist : " + cache.substring(1),IOCenter.ERR);
									else throw new SprexorException(SprexorException.VARIABLE_ERR,cache.toString());
									return;
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
			if(id.indexOf(comment) != -1) {
				IOCenter res = null;
				id = id.split(comment + "")[0];
				CommandProvider obj = cmd.get(id);
				CommandFactory cfObj = cmdlib.get(id);
				try {
					if(obj != null) res = obj.emptyArgs();
					else res = cfObj.emptyArgs(this);
					logger(res);
				}catch(Exception e) {
					logger(obj != null ? obj.error(e) : cfObj.error(e), IOCenter.ERR);
				}
				return;
			}
			if(id.indexOf(";") != -1 && dosem) {
				if(id.charAt(0) == ';') 
					if(!errorIn) 
						logger(" " + input, IOCenter.ERR);
					//
				//
				else throw new SprexorException(SprexorException.EXPRSS_ERR, "Expression incorrect : " + input);
				StringBuilder sb = new StringBuilder();
				StringTokenizer tkn = new StringTokenizer(input, ";");
				logger(cmd.containsKey(id) ? cmd.get(id).emptyArgs() :
					cmdlib.get(id).emptyArgs(this));
				while(tkn.hasMoreTokens()) {
					sb.append(tkn.nextToken());
				}
				run(sb.toString());
				return;
			}
			if(cmdlib.containsKey(id)) logger(cmdlib.get(id).emptyArgs(this));
			else if(cmd.containsKey(id)) logger(cmd.get(id).emptyArgs());
			else if(!errorIn) {
					if(impose.IdNotFound("").isEmpty()) logger(id + " : command not found.", IOCenter.ERR);
						else logger(impose.IdNotFound(id), IOCenter.ERR);
					//
				} else throw new SprexorException(SprexorException.CMD_NOTFOUND, input);
			//
			return;
		} else args[count ++] = cache.toString();
		switch(ClpCode) {
		case 0: break;
		case 1 : 
			if(!errorIn) logger("Warning : Sprexor wasn't closed normally.", IOCenter.WARN);
			else {
				logger("Warning : Sprexor wasn't closed normally.", IOCenter.WARN);
				ClpCode = 0;
				return;
			}
			break;
		default : break;
		}
		if(mod != 0 || smod != 0 || pr != 0) {
			if(!errorIn) logger("Error : invalid literal syntax.", IOCenter.ERR);
			else throw new SprexorException(SprexorException.EXPRSS_ERR, 
					"invalid literal syntax.");
			return;
		}
		
		IOCenter res = null;
		CommandProvider obj = cmd.get(id);
		CommandFactory cfObj = cmdlib.get(id);
		args = trimArr(args);
		wra = trimArr(wra, count);
		Component comval = new Component(args, wra);
		try {
			if(canReflect)comval = reflect.before_invoked(id, comval);
			if(!entryMode) {
				if(obj != null) res = obj.code(comval);
				else res = cfObj.code(comval, this);
				if(doEntry) {
					entryMode = true;
					entryId = id;
				}
			}
			if(obj != null) {
				if(obj.EntryMode(null) != null || doEntry) {
					entryMode = true;
					entryId = id;
					doEntry = true;
				}
			}else {
				if(cfObj.EntryMode(null) != null || doEntry) {
					entryMode = true;
					entryId = id;
					doEntry = true;
				}
			}
			
		}catch(Exception e) {
			if(obj != null)res = new IOCenter(obj.error(e), IOCenter.ERR);
			else res = new IOCenter(cfObj.error(e), IOCenter.ERR);
		}
		
		if(res == null)res = new IOCenter("", IOCenter.UNKNOWN);
		else if(res.status == null)res.status = IOCenter.UNKNOWN;
		
		if(res != null) if(!res.Msg.isBlank()) logger(res);
		if(nextFlag) {
			run(nextStr);
		}
	}
	/**
	 * eval returns outcome to string.
	 * @param com - string
	 * @return result by string.
	 */
	@Activate_need
	public String eval(String com) {
		String[] args = Component.Parse(com);
		Object obj = autoSelect(args[0]);
		IOCenter toRes = null;
		Component comp = new Component(Tools.cutArr(args, 1));
		if(canReflect) comp = reflect.before_invoked(args[0], new Component(Tools.cutArr(args, 1)));
		
		if(obj instanceof CommandProvider) {
			toRes = cmd.get(args[0])
					.code(comp);
			
		} else if(obj instanceof CommandFactory) {
			toRes = cmdlib.get(args[0])
					.code(comp, this);
		}
		return toRes.Msg;
	}
}