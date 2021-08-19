package sprexor.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import sprexor.v2.IOCenter.TYPE;
import sprexor.v2.lib.Utils;
import sprexor.v2.lib.Smt;


public class Sprexor {
	
	public static final String version = "2.0.0";
	
	public static final String[] LIST = {"help", "commands", "echo", "var"};
	public static final String[] PARSE_OPTION = {"BASIC", "USE_VARIABLE", "USE_COMMENT", "WRAP_NAME"};
	public Reflection reflect;
	public Impose impose = new Impose();
	public Object label;
	private int configType = 0;
	private HashMap<String, CommandProvider> cmd = null;
	private HashMap<String, String> helpDB = null;
	private HashMap<String, CommandFactory> cmdlib = null;
	private HashMap<String, String> helplib = null;
	private char comment = '#';
	private boolean doBasicSyn = true;
	private Vector<String> vec = new Vector<String>();
	private boolean isInit = true;
	private boolean ignoreCase = false;
	private boolean canReflect = false;
	private String[] included = {};
	private IOCenter iostream;
	protected ArrayList<String> listObject;
	protected HashMap<String, Object> envVar;
	protected HashSet<CommandFactory> ImportedPackages;
	/**
	 * The Impose class is used to customize working.
	 * <br> Methods to implement : 
	 * <br> - IdNotFound : This method will be invoked when could not find a command to run.
	 * <br> - out : This method will be invoked when print any message.
	 */
	public class Impose {
		public IOCenter InOut() {
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
						buffering(ch);
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
				char ch;
				StringBuffer sb = new StringBuffer();
				try {
					while((ch = (char) System.in.read()) != '\n') {
						sb.append(ch);
					}
				} catch (IOException e) {
					return "";
				}
				return sb.toString();
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
	 * @since 0.2.18
	 */
	public interface Reflection {
		public default void token(char value, String id) { }
		//
		public default void strmode_start() { }
		public default void strmode_end() { }
		public default void strmode_tasking() { }
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
	private boolean indexOf(String e, String[] arr) {
		for(String i : arr) if(i.contentEquals(e)) return true;
		return false;
	}
	private boolean fil(String ch, String...tar) {
		for(String t : tar) {
			if(ch.indexOf(t) != -1)return true;
		}
		return false;
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
		 listObject = new ArrayList<String>();
	}
	/**
	 * importSprex is get command context(s) that created with class in this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param cmp : Other class that implemented with CommandProvider
	 * @since 0.2.3
	 */
	public void importSprex(CommandFactory cmp) {
		if(configType != 0)return;
		if(cmd.containsKey(cmp.getCommandName())) return;
		if(cmp.referenceClass() == null) {
			cmdlib.put(cmp.getCommandName(), cmp);
			String hlpMsg = cmp.help();
			if(hlpMsg.startsWith("**USE_SMT_FORM**")) hlpMsg = Smt.SMT_FORM(hlpMsg.substring(16));
			helplib.put(cmp.getCommandName(), hlpMsg);
			listObject.add(cmp.getCommandName());
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
				if(hlpMsg.startsWith("**USE_SMT_FORM**")) hlpMsg = Smt.SMT_FORM(hlpMsg.substring(16));
				
				helplib.put(tmpname, hlpMsg);
				listObject.add(tmpname);
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
	public boolean isExist(String s) {
		if(configType != 2)return false;
		if(listObject.isEmpty())return false;
		return listObject.contains(s);
	}
	/**
	 * It sets what use basic commands.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @param Arrlst String array
	 * @since 0.2.18
	 */
	public void include(String...Arrlst) {
		if(configType  != 0) return;
		included = Arrlst;
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
		 if(indexOf("help", included)) {
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
			 helpDB.put("help", "print this message.");
			 listObject.add("help");
		 }
		 if(indexOf("var", included)) {
			 cmd.put("var", new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					if(!doBasicSyn) { 
						io.out.setType(TYPE.ERR);
						io.out.println("var : can not be used (basicsyn : false)");
						return 1;
					}
					Component args = io.component;
					int leng_arg = args.length();
					if(leng_arg >= 1) switch(args.gets(0)) {
					case "delete" :
						if(leng_arg < 2) return 0;
						String[] Names = Utils.cutArr(args.get(), 1);
						for(String strs : Names) {
							envVar.remove(strs);
						}
						break;
					
					case "set" :
						if(leng_arg < 2) return 0;
						String[] prm = Utils.cutArr(args.get(), 1);
						//var set a=7 a=9...
						try {
						for(String assignment : prm) {
							String[] kk = assignment.split("=");
							if(envVar.containsKey(kk[0])) envVar.replace(kk[0], kk[1]);
							else envVar.put(kk[0], kk[1]);
						}
						}catch(Exception e) {
							io.out.setType(TYPE.ERR);
							io.out.println("invalid declaring format");
						}
						break;
					
					case "help" :
						io.out.println(Smt.SMT_FORM("(!) : no action[n]" + 
								"usage : var [ACTION] ...[nnt]" +
								"delete : delete variables[nnt]" + 
								"set : set variables, (ex) abc=1 def=\"123\" ...[nnt]" +
								"help : show this message"));
						break;
						
					default : 
						io.out.println("unknown param : " + args.gets(0));
						return 1;
					}
					else {
					io.out.println(Smt.SMT_FORM("(!) : no action[n]" + 
					"usage : var [ACTION] ...[nnt]" +
					"delete : delete variables[nnt]" + 
					"set : set variables, (ex) abc=1 def=\"123\" ...[nnt]" + 
					"help : show this message"));
					}
					return 0;
				}
			 });
			 helpDB.put("var","manage variable");
			 listObject.add("var");
		 }
		 if(indexOf("echo", included)) {
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
			 helpDB.put("echo", "print text.");
			 listObject.add("echo");
		 }
		 if(indexOf("commands", included) || indexOf("list", included)) {
			 cmd.put("commands", new CommandProvider() {
				@Override
				public int code(IOCenter io) {
					io.out.println(Utils.arg2String(listObject.toArray(new String[listObject.size()]), "\n"));
					return 0;
				}
			 });
			 helpDB.put("commands", "print all commands.");
			 listObject.add("commands");
		 }
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
			iostream = impose.InOut();
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
	 */
	public void register(String str, CommandProvider cp, String hd) {
		if(configType != 0)return;
		else if(isExist(str) || fil(str, " ", "'", "\"", "\\", "$", "*", "^", "(", ")", "{", "}", ":", "?", ";", "<", ">", ",", ".", "!", "#", "@", "&", "%", "~", "`", "[", "]") || str.contentEquals(""))return;
		else if(ignoreCase) str.toLowerCase();
		cmd.put(str, cp);
		listObject.add(str);
		helpDB.put(str, hd);
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
	public Sprexor copySprexor() {
		Sprexor newInstance = new Sprexor();
		newInstance.canReflect = canReflect;
		newInstance.cmd = cmd;
		newInstance.cmdlib = cmdlib;
		newInstance.comment = comment;
		return newInstance;
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
	public void exec(String id, String[] args) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
		
		if(!isExist(id))throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
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
	public int exec(String com) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		com = com.trim();
		String id = split(com, ' ')[0].trim();
		if (ignoreCase) id = id.toLowerCase();
		if(com.trim().contentEquals("")) {
			return -1;
		}else if(!isExist(id)) {
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		com = com.substring(split(com, ' ')[0].length());
		char[] comar = com.toCharArray();
		String[] args = new String[comar.length];
		byte count = 0;
		int allCount = 0;
		StringBuilder cache = new StringBuilder();
		
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
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '"'){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						continue;
					}else if(c == '\'') {
						byte bl = 1;
						while(++ allCount < comar.length) {
							c = comar[allCount];
							if(c == '\\') {
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '\''){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						continue;
					}
				cache.append(c);
			
		}
		if(cache.length() != 0) args[count] = cache.toString();
		IOCenter res = impose.InOut();
		args = trimArr(args);
		res.component = new Component(args);
		return (cmd.containsKey(id) ? cmd.get(id).code(res) : cmdlib.get(id).code(res, this));
	}
	/**
	 * It run a line with powerful string parser but, unstable.
	 * <br> And it will be run that configured. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param input
	 * @param options string split by semicolon (;)
	 * BASIC : use basic way, and this option shouldn't along with other options.
	 * @return int : exit code
	 * @since 0.2.18
	 * @throws SprexorException
	 */
	synchronized public int run(String input, String options) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "Activation failed.");
		if(options.contentEquals("BASIC")) return exec(input);
		input = input.trim();
		String[] lists = split(options, ';');
		if(lists.length > 1 && indexOf("BASIC", lists)) throw new SprexorException(SprexorException.INTERNAL_ERROR, "'BASIC' must use separately");
		boolean vari = indexOf("USE_VARIABLE", lists);
		boolean com = indexOf("USE_COMMENT", lists);
		boolean wrap = indexOf("WRAP_NAME", lists);
		
		int nn = 0;
		String id = "";
		if(wrap) {
			int ii = 0;
			char c;
			char[] inputs = input.toCharArray();
			StringBuffer sb = new StringBuffer();
			while(ii < inputs.length) {
				c = inputs[ii];
				if(c == '"') {
					ii += 1;
					while(ii < inputs.length) {
						if(inputs[ii] == '"') {
							ii ++;
							break;
						}
						sb.append(inputs[ii ++]);
					}
					continue;
				}else if(c == '\'') {
					ii += 1;
					while(ii < inputs.length) {
						if(inputs[ii] == '\'') {
							ii ++;
							break;
						}
						sb.append(inputs[ii ++]);
					}
					continue;
				}else if(c == ' ') break;
				sb.append(c);
				ii ++;
			}
			nn = ii;
			id = sb.toString();
		}else {
			id = input.split(" ")[0];
			nn = id.length();
		}
		char[] comar = input.substring(nn).trim().toCharArray();
		String[] args = new String[comar.length];
		byte count = 0;
		int allCount = 0;
		StringBuffer cache = new StringBuffer();
		
		if(canReflect) { //If use reflection
			char c;
			for(;allCount < comar.length; allCount ++) {
				c = comar[allCount];
				reflect.token(c, id);
					if(c == ' ') {
						if(cache.length() == 0)continue;
						args[count ++] = cache.toString();
						cache.setLength(0);
						continue;
					}else if(c == '"'){
						byte bl = 1;
						reflect.strmode_start();
						while(++ allCount < comar.length) {
							c = comar[allCount];
							reflect.strmode_tasking();
							if(c == '\\') {
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '"'){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						reflect.strmode_end();
						continue;
					}else if(c == '\'') {
						byte bl = 1;
						while(++ allCount < comar.length) {
							c = comar[allCount];
							if(c == '\\') {
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '\''){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						continue;
					/*
					 * variable
					 */
					}else if(c == '@') {
						if(vari) {
							StringBuffer sb = new StringBuffer();
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c !=  ' ' && c != '@') {
									sb.append(c);
								}else if(c == '@'){
									if(args[count] == null) args[count] = "";
									String tmp = sb.toString();
									if(envVar.containsKey(tmp)) args[count] += envVar.get(tmp).toString();
									else throw new SprexorException(SprexorException.VARIABLE_ERR, "no variable");
									sb.setLength(0);
								}else break;
							}
							String tmp = sb.toString();
							if(args[count] == null) args[count] = "";
							if(!tmp.matches("^[a-zA-Z0-9_]+$"))throw new SprexorException(SprexorException.EXPRSS_ERR, "Invalid variable name : " + tmp);
							if(envVar.containsKey(tmp)) args[count ++] += envVar.get(tmp).toString();
							
							else throw new SprexorException(SprexorException.VARIABLE_ERR, "no variable");
							continue;
						}
					}else if(c == comment) { // comment (note)
						if(com) {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == ';' || c == '\n') {
									IOCenter res = impose.InOut();
									args = trimArr(args);
									res.component = new Component(args);
									if(cmd.containsKey(id)) return cmd.get(id).code(res);
									else return cmdlib.get(id).code(res, this);
								}
							}
						}
					}
				cache.append(c);
			}
		}else { // non-reflection
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
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '"'){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						continue;
					}else if(c == '\'') {
						byte bl = 1;
						while(++ allCount < comar.length) {
							c = comar[allCount];
							if(c == '\\') {
								try {
								char nextChar = comar[++ allCount];
								cache.append(switch (nextChar) {
								case 't' -> '\t';
								case 'n' -> '\n';
								case '"' -> '"';
								case '\\' -> '\\';
								default -> nextChar;
								});
								continue;
								}catch(Exception e) {
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										"invalid literal syntax.");
								}
							}else if(c == '\''){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								"invalid literal syntax.");
						continue; 
					} else if(c == '@') {
						if(vari) {
							StringBuffer sb = new StringBuffer();
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c !=  ' ' && c != '@') {
									sb.append(c);
								}else if(c == '@'){
									if(args[count] == null) args[count] = "";
									String tmp = sb.toString();
									if(envVar.containsKey(tmp)) args[count] += envVar.get(tmp).toString();
									else throw new SprexorException(SprexorException.VARIABLE_ERR, "no variable");
									sb.setLength(0);
								} else break;
								
							}
							String tmp = sb.toString();
							if(args[count] == null) args[count] = "";
							if(!tmp.matches("^[a-zA-Z0-9_]+$"))throw new SprexorException(SprexorException.EXPRSS_ERR, "Invalid variable name : " + tmp);
							if(envVar.containsKey(tmp)) args[count ++] += envVar.get(tmp).toString();
							else throw new SprexorException(SprexorException.VARIABLE_ERR, "no variable");
							continue;
						}
					}else if(c == comment) { // comment (note)
						if(com) {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == ';' || c == '\n') {
									IOCenter res = impose.InOut();
									args = trimArr(args);
									res.component = new Component(args);
									if(cmd.containsKey(id)) return cmd.get(id).code(res);
									else return cmdlib.get(id).code(res, this);
								}
							}
						}
					}
				cache.append(c);
			}
		}
		if(count != 0 || cache.length() != 0) args[count ++] = cache.toString();
		args = trimArr(args);
		iostream.component = new Component(args);
		if(id.isBlank()) return 0;
		else if(cmd.containsKey(id)) return cmd.get(id).code(iostream);
		else if(cmdlib.containsKey(id)) return cmdlib.get(id).code(iostream, this);
		else throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
	}
}