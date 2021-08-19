package sprexor.v3;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

//import sprexor.v3.GlobalData;
import sprexor.v3.components.Importable;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.SParser;
import sprexor.v3.components.annotations.*;
import sprexor.v3.standard.parser.DefaultParser;


public class SManager {
	
	public static final String version = "3.0.0";
	
	/**
	 * directory that sprexor is started
	 */
	static String resourcePath = ClassLoader.getSystemClassLoader().getResource(".").getPath();
	
	/**
	 * parse options using in {@link #run(String, String) run(String command, String options)}
	 * <pre>
	 * list : BASIC : call {@link #exec(String)} method, and it must independent.
	 * <br> USE_VARIABLE : enable to use variable expression
	 * <br> WRAP_NAME : command ID will support string wrap expression.
	 * <br> DEBUG : error in command will not be catch.
	 * <br> ALIAS : use alias
	 * </pre>
	 */
	public static final String[] PARSE_OPTION = {"BASIC", "USE_VARIABLE", "WRAP_NAME", "DEBUG", "ALIAS"};
	public Object label;
	
	/**
	 * public registry
	 * @see sprexor.v0.GlobalData
	 */
	public GlobalData SystemVar;
	private boolean open = false;
	protected ArrayList<String> listObject;
	protected Vector<String> vec = new Vector<String>();
	protected HashMap<String, SCommand> cmd = null;
	protected HashMap<String, Object> envVar;
	protected HashMap<String, String> desc;
	protected HashMap<String, String> versionMap;
	protected char varChar = '@';
	protected boolean isInit = true;
	protected boolean ignoreCase = false;
	protected boolean belog = false;
	protected SParser sparser;
	protected IOCenter iostream;
	protected FileOutputStream logf;
	protected String logPath = "";
	
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
	private String findv(String k) throws SprexorException{
		if(ExistsVariable(k)) return envVar.get(k).toString();
		logger("< CRITICAL > variable not found  : " + k);
		throw new SprexorException(SprexorException.VARIABLE_ERR, SOutputs.nv);
	}
	
	/**
	 * logging method
	 * <p>
	 * NOTE : It is available only that sprexor instance is created by {@link #SManager(String)} constructor.
	 * 
	 * @param str
	 */
	public void logger(String str) {
		if(belog)
			try {
				logf.write((new Date().toString().concat(" ] ") + str + "\n").getBytes());
			} catch (IOException e) { }
	}
	
	public SManager() {
		 cmd = new HashMap<String, SCommand>();
		 envVar = new HashMap<String, Object>();
		 desc = new HashMap<String, String>();
		 versionMap = new HashMap<String, String>();
		 SystemVar = new GlobalData();
		 SystemVar.putData("CURRENT_DIRECTORY", "");
		 SystemVar.putData("ALIAS", "");
		 iostream = new IOCenter();
	}
	
	/**
	 * this constructor is used for logging 
	 * 
	 * @param logPath directory to logging
	 */
	public SManager(String logPath) {
		 cmd = new HashMap<String, SCommand>();
		 envVar = new HashMap<String, Object>();
		 desc = new HashMap<String, String>();
		 versionMap = new HashMap<String, String>();
		 SystemVar = new GlobalData();
		 SystemVar.putData("CURRENT_DIRECTORY", "");
		 SystemVar.putData("ALIAS", "");
		 iostream = new IOCenter();
		 try {
			logf = new FileOutputStream(new File(logPath + ((logPath.endsWith(File.separator))? "" : File.separator).concat("sprexor.log")));
			belog = true;
			logf.write(String.format("\t | | |  Initializing Sprexor Logger %s  | | |%n", version).getBytes());
			this.logPath = logPath.concat("sprexor.log");
		} catch (IOException e) {
			System.out.println("cannot logging");
		}
		 
	}
	
	/**
	 * set Output (PrintStream)
	 * <p>
	 * default : {@link System#out}
	 * 
	 * @param spo
	 */
	public void setOut(PrintStream spo) {
		if(open) return;
		iostream.out = spo;
	}
	
	/**
	 * set Input (Scanner)
	 * <p>
	 * default :  {@linkplain Scanner Scanner(System.in)}
	 * 
	 * @param spi
	 */
	public void setIn(Scanner spi) {
		if(open) return;
		iostream.in = spi;
	}
	
	/**
	 * set parser
	 * <p>
	 * default : {@linkplain sprexor.v2.standard.parser.DefaultParser DefaultParser}
	 * 
	 * @param sp
	 */
	public void setParser(SParser sp) {
		if(open) return;
		sparser = sp;
	}
	
	/**
	 * set variable prefix
	 * <p>
	 * default : '@'
	 * 
	 * @param prefix
	 */
	public void setVariablePrefix(char prefix) {
		varChar = prefix;
	}
	
	/**
	 * import classes that implemented 
	 * {@linkplain sprexor.v2.components.Importable Importable} interface.
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to import.
	 * <p>
	 * If command is overlapped, that command is not import again.
	 * <p>
	 * If required version of command is higher than this Sprexor version, that command is not import.
	 * <p>
	 * If can't access to command, that command is not import.
	 * 
	 * @param imp Imporable.class
	 */
	public void use(Class<? extends Importable> imp) {
		if(open) return;
		
		Spackage sp = imp.getAnnotation(Spackage.class);
		
		String packname = sp.packageName();
		String t, name;
		Class<? extends SCommand>[] sc = sp.ref();
		CommandInfo ci;
		
		for(Class<? extends SCommand> s : sc) {
			ci = s.getDeclaredAnnotation(CommandInfo.class);
			name = ci.name();
			if(cmd.containsKey(name)) {
				logger(String.format("%s conflicted '%s'", "< ERROR >", name));
				continue;
			}else if((t = ci.targetVersion()) != null) {
				int i = version.compareTo/*<*/(t);
				if(i < 0) {
					logger(String.format(" %s  not supported '%s' (current version : %s, require version : %s)", "< ERROR >", name, version, t));
					continue;
				}
			}
			try {
				cmd.put(name, s.getConstructor().newInstance());
			} catch (Exception e) {
				logger("< ERROR > cannot access command : ".concat(packname.concat(name)));
				continue;
			}
			logger("< INFO > Command imported : ".concat(packname).concat("::").concat(name));
			versionMap.put(name, ci.version());
		}
		desc.put(packname, sp.description());
	}
	
	/**
	 * import classes that implemented 
	 * {@linkplain sprexor.v2.components.Importable Importable} interface.
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to import.
	 * <p>
	 * If command is overlapped, that command is not import again.
	 * <p>
	 * If required version of command is higher than this Sprexor version, that command is not import.
	 * <p>
	 * If can't access to command, that command is not import.
	 * <p>
	 * <b>problem : Type safety: Potential heap pollution via varargs parameter impa</b>
	 * 
	 * @param imp Imporable.class array
	 */
	public void use(Class<? extends Importable>...impa) {
		if(open)return;
		
		for(Class<? extends Importable>  cc : impa) {
			Spackage sp = cc.getAnnotation(Spackage.class);
			
			String packname = sp.packageName();
			String t, name;
			Class<? extends SCommand>[] sc = sp.ref();
			CommandInfo ci;
			
			for(Class<? extends SCommand> s : sc) {
				ci = s.getDeclaredAnnotation(CommandInfo.class);
				name = ci.name();
				if(cmd.containsKey(name)) {
					logger(String.format("%s conflicted '%s'", "< ERROR >", name));
					continue;
				}else if((t = ci.targetVersion()) != null) {
					int i = version.compareTo/*<*/(t);
					if(i < 0) {
						logger(String.format(" %s  not supported '%s' (current version : %s, require version : %s)", "< ERROR >", name, version, t));
						continue;
					}
				}
				try {
					cmd.put(name, s.getConstructor().newInstance());
				} catch (Exception e) {
					logger("< ERROR > cannot access command : ".concat(packname.concat(name)));
					continue;
				}
				logger("< INFO > Command imported : ".concat(packname).concat("::").concat(name));
				versionMap.put(name, ci.version());
			}
			desc.put(packname, sp.description());
		}
	}
	
	/**
	 * import classes that implemented 
	 * {@linkplain sprexor.v2.components.Importable Importable} interface dynamically.
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to import.
	 * <p>
	 * If command is overlapped, that command is not import again.
	 * <p>
	 * If required version of command is higher than this Sprexor version, that command is not import.
	 * <p>
	 * If can't access to command, that command is not import.
	 * 
	 * @param imp Imporable.class
	 */
	public void useR(Class<? extends Importable> imp) {
		if(!open) return;
		Spackage sp = imp.getAnnotation(Spackage.class);
		String packname = sp.packageName();
		String t, name;
		Class<? extends SCommand>[] sc = sp.ref();
		CommandInfo ci;
		for(Class<? extends SCommand> s : sc) {
			ci = s.getDeclaredAnnotation(CommandInfo.class);
			name = ci.name();
			if(cmd.containsKey(name)) {
				continue;
			}else if((t = ci.targetVersion()) != null) {
				int i = version.compareTo(t);
				if(i < 0) {
					continue;
				}
			}
			try {
				cmd.put(name, s.getConstructor().newInstance());
			} catch (Exception e) {
				continue;
			}
			versionMap.put(name, ci.version());
		}
		desc.put(packname, sp.description());
	}
	
	/**
	 * If {@linkplain #setup()} was not invoked, refuse to check.
	 * 
	 * @return boolean true if command is exist, else false.
	 */
	public boolean isExist(String s) {
		if(!open)return false;
		return cmd.containsKey(s);
	}
	
	/**
	 * get a command description, if description not defined, default value is blank.
	 * <p>
	 * see {@linkplain sprexor.v2.components.annotations.CommandInfo CommandInfo}
	 * 
	 * @param cmdName
	 * @return string
	 */
	public String getCommandDescription(String cmdName) {
		if(desc.containsKey(cmdName) && open) return desc.get(cmdName);
		return "";
	}
	
	/**
	 * If all configure finished, then be able to run command line. This means settings of sprexor prevent to modify secretly.
	 * <p>
	 * If {@linkplain #setup()} was invoked, refuse to setup.
	 * 
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public void setup() {
		if(open || !isInit) return;
		if(sparser == null) {
			logger("< INFO > parser was not selected. switching to default parser...");
			sparser = new DefaultParser();
		}
		if(iostream.out == null) {
			logger("< INFO > outputstream was not selected. switching to system.out...");
			iostream.out = System.out;
		}
		if(iostream.in == null) {
			logger("< INFO > inputstream was not selected. switching to system.in...");
			iostream.in = new Scanner(System.in);
		}
		if(iostream.err == null) {
			iostream.err = System.err;
		}
		open = !open;
		logger("< INFO > Sprexor setup successful");
	}
	
	/**
	 * get list of usable commands
	 * 
	 * @return string array
	 */
	public String[] getList() {
		return cmd.keySet().toArray(new String[cmd.size()]);
	}
	
	/**
	 * @deprecated
	 * replaced by {@link SManager#use(Class)} 
	 * <p>
	 * register a command quickly.
	 * <p>
	 * If {@linkplain #setup()} was invoked, refuse to register.
	 * 
	 * @since 0.1
	 */
	@Deprecated
	public void register(String str, SCommand cp, String hd) {
		if(open) return;
		else if(isExist(str) || str.isEmpty()) return;
		else if(ignoreCase) str.toLowerCase();
		cmd.put(str, cp);
	}
	
	/**
	 * copy sprexor with deep copy.
	 * 
	 * @return new SManager instance
	 */
	public SManager copySprexor() {
		SManager newInstance = new SManager();
		newInstance.cmd = cmd;
		newInstance.desc = desc;
		newInstance.envVar = envVar;
		newInstance.iostream = iostream;
		newInstance.label = label;
		newInstance.iostream = iostream;
		return newInstance;
	}
	
	/**
	 * put a variable
	 * <p>
	 * If already exists, not put a variable.
	 * 
	 * @param key
	 * @param value
	 */
	public void putVariable(String key, String value) {
		if(envVar.containsKey(key)) return;
		envVar.put(key, value);
	}
	
	/**
	 * set a value of variable
	 * <p>
	 * If variable is not exist, put a variable instead.
	 * 
	 * @param key
	 * @param newValue
	 */
	public void setVariable(String key, String newValue) {
		if(envVar.containsKey(key)) envVar.replace(key, newValue);
		else envVar.put(key, newValue);
	}
	
	/**
	 * delete a variable
	 * 
	 * @param key key name
	 * @return boolean true if variable was deleted, else false.
	 */
	public boolean deleteVariable(String key) {
		return null != envVar.remove(key);
	}
	
	/**
	 * check existence of variable
	 * 
	 * @param key key name
	 * @return true if variable is exist, else false.
	 */
	public boolean ExistsVariable(String key) {
		return envVar.containsKey(key);
	}
	
	/**
	 * get command version
	 * 
	 * @param cmdName command name
	 * @return String version
	 */
	public String getVersion(String cmdName) {
		return versionMap.get(cmdName);
	}
	
	/**
	 * merge with other sprexor
	 * 
	 * @param sm
	 * @return true if merged successful, else false.
	 */
	public boolean merge(SManager sm) {
		if(!open) return false;
		if(this.hashCode() == sm.hashCode()) return false;
		cmd.putAll(sm.cmd);
		logger(String.format("< INFO > Sprexor merged with : %d", sm.hashCode()));
		return true;
	}
	
	/**
	 * clear log file
	 * <p>
	 * NOTE : It is available only that sprexor instance is created by {@link #SManager(String)} constructor.
	 */
	public void clearLog() {
		File f = new File(logPath);
		if(belog) {
			f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) { }
		}
	}
	
	/**
	 * execute a command line lightly.
	 * 
	 * @param com SCommand 
	 * @param args string arguments
	 * @return exit code
	 * @since 1.0
	 */
	public int exec(SCommand com, String[] args) {
		return com.main(iostream, new SParameter(args), this);
	}
	
	/**
	 * execute a command line lightly.
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to execute.
	 * 
	 * @param id command name
	 * @param args string arguments
	 * @throws SprexorException activation_failed : setup was not invoked.
	 * <br> cmd_not_found : command is not found.
	 * 
	 * @since 0.2
	 */
	public int exec(String id, String[] args) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
		
		if(isExist(id) == false) {
			logger("< CRITICAL > Command not found : " + id);
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		SCommand obj = cmd.get(id);
		int i = obj.main(iostream, new SParameter(args), this);
		return i;
	}
	
	/**
	 * execute a command line lightly.
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to execute.
	 * 
	 * @param com : command string to execute
	 * @throws SprexorException activation_failed : setup was not invoked.
	 * <br> cmd_not_found : command is not found.
	 * 
	 * @since 0.1
	 */
	public int exec(String com) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
		
		String id = sparser.id(com);
		if(!isExist(id)) {
			logger("< CRITICAL > Command not found : " + id);
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		com = com.substring(id.length()).trim();
		SParameter component = new SParameter(sparser.processing(com));
		return cmd.get(id).main(iostream, component, this);
	}
	
	/**
	 * run a command line with powerful string parser and many features.
	 * <p>
	 * <p>
	 * If {@linkplain #setup()} was not invoked, refuse to run.
	 * 
	 * @param input
	 * @param options string split by semicolon (;), see {@linkplain #PARSE_OPTION}
	 * @return int : exit code
	 * @throws SprexorException activation_failed : setup was not invoked.
	 * <br> cmd_not_found : command is not found.
	 * <br> internal_error
	 * <br> express_error : if syntax is wrong, this message will be showed.
	 * <br> variable_error : no variable
	 * 
	 * @since 0.2
	 */
	synchronized public int run(String input, String options) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
		if(options.contentEquals("BASIC")) return exec(input);
		input = input.trim();
		options = options.toUpperCase();
		String[] lists = split(options, ';');
		if(lists.length > 1 && indexOf("BASIC", lists)) throw new SprexorException(SprexorException.INTERNAL_ERROR, SOutputs.bsp);
		boolean vari = indexOf("USE_VARIABLE", lists);
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
			id = split(input, ' ')[0];
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
						if(cache.length() == 0) continue;
						args[count ++] = cache.toString();
						cache.setLength(0);
						continue;
					}else if(c == '"'){
						byte bl = 1;
						boolean varm = false;
						StringBuilder sb = new StringBuilder();
						try {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == '\\') {
									if(varm) {
										varm = false;
										cache.append(findv(sb.toString()));
										sb.setLength(0);
									}
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
									if(varm) cache.append(findv(sb.toString()));
									varm = false;
									break;
								}else if(c == varChar) {
									if(!varm) {
										varm = true;
									} else {
										cache.append(findv(sb.toString()));
										sb.setLength(0);
									}
								}else if(((c + "").matches("\\W")) && varm) {
									cache.append(findv(sb.toString())).append(c);
									sb.setLength(0);
									varm = false;
								}else {
									if(varm) sb.append(c); else cache.append(c);
								}
							}
							
						}catch(Exception e) {
							e.printStackTrace();
							logger("< CRITICAL > invalid expression  : " + cache.toString().concat(" # " + allCount));
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
							}
						if(bl == 1) {
							logger("< CRITICAL > invalid expression  : " + cache.toString().concat("# " + allCount));
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
						}
						continue;
					}else if(c == '\'') {
						byte bl = 1;
						boolean varm = false;
						StringBuilder sb = new StringBuilder();
						try {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == '\\') {
									if(varm) {
										varm = false;
										cache.append(findv(sb.toString()));
										sb.setLength(0);
									}
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
									if(varm) cache.append(findv(sb.toString()));
									varm = false;
									break;
								}else if(c == varChar) {
									if(!varm) {
										varm = true;
									} else {
										cache.append(findv(sb.toString()));
										sb.setLength(0);
									}
								}else if(((c + "").matches("\\W")) && varm) {
										cache.append(findv(sb.toString())).append(c);
										sb.setLength(0);
								} else {
									if(varm) sb.append(c); else cache.append(c);
								}
							}
						}catch(Exception e) {
							logger("< CRITICAL > invalid expression  : " + cache.toString().concat(" # " + allCount));
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
						}
						if(bl == 1) {
							logger("< CRITICAL > invalid expression  : " + cache.toString().concat(" # " + allCount));
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
						};
						continue; 
					} else if(c == varChar) {
						if(vari) {
							StringBuffer sb = new StringBuffer();
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c !=  ' ' && c != varChar) {
									sb.append(c);
								}else if(c == varChar){
									if(args[count] == null) args[count] = "";
									String tmp = sb.toString();
									if(envVar.containsKey(tmp)) args[count] += envVar.get(tmp).toString();
									else {
											logger("< CRITICAL > variable not found  : " + tmp);
										throw new SprexorException(SprexorException.VARIABLE_ERR, SOutputs.nv);
									}
									sb.setLength(0);
								} else break;
								
							}
							String tmp = sb.toString();
							if(args[count] == null) args[count] = "";
							if(!tmp.matches("^[a-zA-Z0-9_]+$")) {
									logger("< CRITICAL > invalid expression : " + cache.toString());
								throw new SprexorException(SprexorException.EXPRSS_ERR, String.format(SOutputs.iv, tmp));
							}
							if(envVar.containsKey(tmp)) args[count ++] += envVar.get(tmp).toString();
							else {
								logger("< CRITICAL > variable not found  : " + cache.toString());
								throw new SprexorException(SprexorException.VARIABLE_ERR, SOutputs.nv);
							}
							continue;
						}
					}
				cache.append(c);
		}
		if(count != 0 || cache.length() != 0) args[count ++] = cache.toString();
		args = trimArr(args);
		SParameter component = new SParameter(args);
		component.raw = input;
		int exitCode = 0;
		if(id.isBlank()) return 0;
		if(cmd.containsKey(id)) {
			if(!debug) try {
				cmd.get(id).main(iostream, component, this); 
			} catch(Exception e) {
				iostream.out.printf(SOutputs.st, id, e.getStackTrace()[0].getLineNumber());
			}
			
			else cmd.get(id).main(iostream, component, this);
		}
		else if(alias) {
			String Aliass = SystemVar.getData("ALIAS").toString();
			String[] tmpArr = split(Aliass, '\n');
			String[] k;
			for(String s : tmpArr) {
				k = split(s, '=');
				if(k[1].contentEquals(id)) {
					if(!debug) try {
						cmd.get(k[1]).main(iostream, component, this); 
					} catch(Exception e) {
						iostream.out.printf(SOutputs.st, k[0], e.getStackTrace()[0].getLineNumber());
					}
					break;
				}
			}
		} else {
			logger("< CRITICAL > Command not found : " + id);
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		logger(String.format("< INFO > Program '%s' exit code : %d", id, exitCode));
		return exitCode;
	}
	
}
