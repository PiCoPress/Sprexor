package sprexor.v2;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import sprexor.SOutputs;
import sprexor.v0.GlobalData;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;
import sprexor.v2.components.SParameter;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface Activation_need { 
	/*
	 * This Annotation is used to let when something method need to activate.
	 */
}
/*
 * Sprexor : Command Executor 1.0.0
 */
public class SManager {
	static String resourcePath = ClassLoader.getSystemClassLoader().getResource(".").getPath();
	public static final String[] PARSE_OPTION = {"BASIC", "USE_VARIABLE", "USE_COMMENT", "WRAP_NAME", "DEBUG", "ALIAS"};
	public Impose impose = new Impose() {};
	public Object label;
	public GlobalData SystemVar;
	private int configType = 0;
	protected HashMap<String, SCommand> cmd = null;
	protected char comment = '#';
	protected boolean doBasicSyn = true;
	protected Vector<String> vec = new Vector<String>();
	protected boolean isInit = true;
	protected boolean ignoreCase = false;
	protected boolean doLog = false;
	protected IOCenter iostream;
	protected ArrayList<String> listObject;
	protected HashMap<String, Object> envVar;
	protected HashMap<String, String> desc;
	protected HashMap<String, String> version;
	protected StringBuilder log;
	
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
			@Override
			public void printf(String msg, Object... obj) {
				System.out.print(String.format(msg, obj));
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
	public SManager() {
		 cmd = new HashMap<String, SCommand>();
		 envVar = new HashMap<String, Object>();
		 desc = new HashMap<String, String>();
		 version = new HashMap<String, String>();
		 SystemVar = new GlobalData();
		 SystemVar.putData("CURRENT_DIRECTORY", "");
		 SystemVar.putData("ALIAS", "");
		 log = new StringBuilder();
	}
	/**
	 * importSprex is get command context(s) that created with class in this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param cmp : Other class that implemented with CommandProvider
	 * @since 0.2.3
	 */
	public void use(Class<? extends SFrame> sf) {
		if(configType != 0)return;
		
		SCommand[] sc;
		SFrame sframe;
		try {
			sframe = (sf.getDeclaredConstructor().newInstance());
			sc = sframe.references();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String n;
		for(SCommand s : sc) {
			n = s.name();
			// System.out.println(n);
			if(cmd.containsKey(n)) {
				System.out.printf("conflict : %s\n", n);
				continue;
			}
			cmd.put(n, s);
			version.put(n, s.version());
		}
		desc.put(sframe.PackageName(), sframe.getDescription());
	}
	/**
	 * check whether name is exist.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param s - command name.
	 * @return return true if command name(s) is exist, other case false.
	 * @since 0.2.0
	 */
	@Activation_need
	public boolean isExist(String s) {
		if(configType != 2)return false;
		return cmd.containsKey(s);
	}
	/**
	 * get a description
	 * @param cmdName
	 * @return string
	 */
	@Activation_need
	public String getCommandDescription(String cmdName) {
		if(desc.containsKey(cmdName) && configType == 2) return desc.get(cmdName);
		return null;
	}
	/**
	 * If all configure finished, then be able to run command line. This means settings of sprexor prevent to modify secretly.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public void setup() {
		if(configType != 0 || !isInit) return;
		iostream = impose.InOut();
		if(configType == 0)configType = 2;
	}
	/**
	 * get list of usable commands
	 * @return string array
	 */
	@Activation_need
	public String[] getList() {
		return cmd.keySet().toArray(new String[cmd.size()]);
	}
	/**
	 * @deprecated
	 * This method Register a command. Registring command name shouldn't be included special character like *, ^ etc...
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @param str : command name that should not be contained special characters. 
	 * @param cp : CommandProvider
	 * @param hd : helping message is used in basic command - help.
	 * @since 0.1
	 */
	@Deprecated
	public void register(String str, SCommand cp, String hd) {
		if(configType != 0)return;
		else if(isExist(str) || str.contentEquals(""))return;
		else if(ignoreCase) str.toLowerCase();
		listObject.add(str);
		cmd.put(str, cp);
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
	
	public SManager copySprexor() {
		SManager newInstance = new SManager();
		newInstance.cmd = cmd;
		newInstance.comment = comment;
		newInstance.desc = desc;
		newInstance.doBasicSyn = doBasicSyn;
		newInstance.envVar = envVar;
		newInstance.iostream = iostream;
		newInstance.label = label;
		return newInstance;
	}
	
	public void putVariable(String key, String value) {
		if(envVar.containsKey(key)) return;
		envVar.put(key, value);
	}
	
	public void setVariable(String key, String newValue) {
		if(envVar.containsKey(key)) envVar.replace(key, newValue);
		else envVar.put(key, newValue);
	}
	
	public boolean deleteVariable(String key) {
		return null != envVar.remove(key);
	}
	
	public boolean ExistsVariable(String key) {
		return envVar.containsKey(key);
	}
	
	public boolean useVariableExpression() {
		return doBasicSyn;
	}
	
	public void setLogging(boolean bool) {
		doLog = bool;
	}
	
	public String getLog() {
		return log.toString();
	}
	
	public String getVersion(String cmdName) {
		return version.get(cmdName);
	}
	/**
	 * execute command as non-parse.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param id : command name
	 * @param args : arguments
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	@Activation_need
	public int exec(String id, String[] args) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
		
		if(!isExist(id))throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		SCommand obj = cmd.get(id);
		IOCenter res = iostream;
		iostream.component = new SParameter(args);
		return obj.main(res, this);
	}
	/**
	 * run a command line lightly.
	 * <br> and it will be run that you configured. 
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param com : command string to execute
	 * @throws SprexorException 
	 * @since 0.1
	 */
	@Activation_need
	public int exec(String com) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
		com = com.trim();
		String id = split(com, ' ')[0].trim();
		if (ignoreCase) id = id.toLowerCase();
		if(com.isBlank()) {
			return -1;
		}else if(!isExist(id)) {
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		com = com.substring(split(com, ' ')[0].length());
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
										SOutputs.syn);
								}
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
											SOutputs.syn);
								}
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
		final IOCenter res = impose.InOut();
		args = trimArr(args);
		res.component = new SParameter(args);
		return cmd.get(id).main(res, this);
	}
	/**
	 * It run a line with powerful string parser but, unstable.
	 * <br> And it will be run that configured. 
	 * @param input
	 * @param options string split by semicolon (;)
	 * BASIC : use basic way, and this option shouldn't along with other options.
	 * @return int : exit code
	 * @since 0.2.18
	 * @throws SprexorException
	 */
	@Activation_need
	synchronized public int run(String input, String options) throws SprexorException {
		if(configType != 2) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
		if(options.contentEquals("BASIC")) return exec(input);
		input = input.trim();
		String[] lists = split(options, ';');
		if(lists.length > 1 && indexOf("BASIC", lists)) throw new SprexorException(SprexorException.INTERNAL_ERROR, SOutputs.bsp);
		boolean vari = indexOf("USE_VARIABLE", lists);
		boolean com = indexOf("USE_COMMENT", lists);
		boolean wrap = indexOf("WRAP_NAME", lists);
		boolean debug = indexOf("DEBUG",lists);
		boolean alias = indexOf("ALIAS", lists);
		
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
											SOutputs.syn);
								}
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
											SOutputs.syn);
								}
							}else if(c == '\''){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
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
									else throw new SprexorException(SprexorException.VARIABLE_ERR, SOutputs.nv);
									sb.setLength(0);
								} else break;
								
							}
							String tmp = sb.toString();
							if(args[count] == null) args[count] = "";
							if(!tmp.matches("^[a-zA-Z0-9_]+$"))throw new SprexorException(SprexorException.EXPRSS_ERR, String.format(SOutputs.iv, tmp));
							if(envVar.containsKey(tmp)) args[count ++] += envVar.get(tmp).toString();
							else throw new SprexorException(SprexorException.VARIABLE_ERR, SOutputs.nv);
							continue;
						}
					}else if(c == comment) { // comment (note)
						if(com) {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == ';' || c == '\n') {
									args = trimArr(args);
									iostream.component = new SParameter(args);
									if(cmd.containsKey(id)) return cmd.get(id).main(iostream, this);
									iostream.reset();
								}
							}
						}
					}
				cache.append(c);
		}
		if(count != 0 || cache.length() != 0) args[count ++] = cache.toString();
		args = trimArr(args);
		iostream.component = new SParameter(args);
		int exitCode = 0;
		if(id.isBlank()) return 0;
		if(cmd.containsKey(id)) {
			if(!debug) try {
				cmd.get(id).main(iostream, this); 
			} catch(Exception e) {
				iostream.out.printf(SOutputs.st, id, e.getStackTrace()[0].getLineNumber());
			}
			
			else cmd.get(id).main(iostream, this);
		}
		else if(alias) {
			String Aliass = SystemVar.getData("ALIAS").toString();
			String[] tmpArr = split(Aliass, '\n');
			String[] k;
			for(String s : tmpArr) {
				k = split(s, '=');
				if(k[1].contentEquals(id)) {
					if(!debug) try {
						cmd.get(k[1]).main(iostream, this); 
					} catch(Exception e) {
						iostream.out.printf(SOutputs.st, k[0], e.getStackTrace()[0].getLineNumber());
					}
					break;
				}
			}
		} else {
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		iostream.reset();
		return exitCode;
	}
}