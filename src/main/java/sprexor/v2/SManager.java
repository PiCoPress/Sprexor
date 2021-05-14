package sprexor.v2;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import sprexor.SOutputs;
import sprexor.Space;
import sprexor.v0.GlobalData;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.SParser;
import sprexor.v2.components.annotations.*;
import sprexor.v2.standard.DefaultParser;

/*
 * Sprexor : Command Executor 1.0.0
 */
public class SManager {
	
	static String resourcePath = ClassLoader.getSystemClassLoader().getResource(".").getPath();
	public static final String[] PARSE_OPTION = {"BASIC", "USE_VARIABLE", "USE_COMMENT", "WRAP_NAME", "DEBUG", "ALIAS"};
	public Object label;
	public GlobalData SystemVar;
	private boolean open = false;
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
	protected HashMap<String, String> versionMap;
	protected SParser sparser;
	protected FileOutputStream logf;
	protected boolean belog = false;
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
	
	public void logger(String str) {
		if(belog)
			try {
				logf.write((new Date().toString().concat(" ] ") + str + "\n").getBytes());
			} catch (IOException e) { }
	}
	//
	//public methods.
	//
	public SManager() {
		 cmd = new HashMap<String, SCommand>();
		 envVar = new HashMap<String, Object>();
		 desc = new HashMap<String, String>();
		 versionMap = new HashMap<String, String>();
		 SystemVar = new GlobalData();
		 SystemVar.putData("CURRENT_DIRECTORY", "");
		 SystemVar.putData("ALIAS", "");
	}
	
	public SManager(String logPath) {
		 cmd = new HashMap<String, SCommand>();
		 envVar = new HashMap<String, Object>();
		 desc = new HashMap<String, String>();
		 versionMap = new HashMap<String, String>();
		 SystemVar = new GlobalData();
		 SystemVar.putData("CURRENT_DIRECTORY", "");
		 SystemVar.putData("ALIAS", "");
		 try {
			logf = new FileOutputStream(new File(logPath + ((logPath.endsWith(File.separator))? "" : File.separator).concat("sprexor.log")));
			belog = true;
			logf.write(String.format("\t | | |  Initializing Sprexor Logger %s  | | |%n", Space.Version).getBytes());
			this.logPath = logPath.concat("sprexor.log");
		} catch (IOException e) {
			System.out.println("cannot logging");
		}
		 
	}
	
	public void setOut(SprexorOstream spo) {
		if(open) return;
		iostream.out = spo;
	}
	
	public void setIn(SprexorIstream spi) {
		if(open) return;
		iostream.in = spi;
	}
	
	public void setParser(SParser sp) {
		if(open) return;
		sparser = sp;
	}
	/**
	 * importSprex is get command context(s) that created with class in this sprexor.
	 * <br><b><span style="color:ff00ff">SIGN : It can be used after activate.</span></b>
	 * @param cmp : Other class that implemented with CommandProvider
	 * @since 0.2.3
	 */
	public void use(Class<? extends SFrame> sf) {
		if(open)return;
		
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
		version v;
		target t;
		Class<? extends SCommand> cl;
		for(SCommand s : sc) {
			cl = s.getClass();
			n = s.getClass().getAnnotation(name.class).value();
			if(cmd.containsKey(n)) {
				logger(String.format("%s conflicted '%s'", "< ERROR >", n));
				continue;
			}else if((t = cl.getAnnotation(target.class)) != null) {
				int i = Space.Version.compareTo/*<*/(t.value());
				if(i < 0) {
					logger(String.format(" %s  not supported '%s' (current version : %s, require version : %s)", "< ERROR >", n, Space.Version, t.value()));
					continue;
				}
			}
			cmd.put(n, s);
			logger("< INFO > Command imported : ".concat(sframe.PackageName()).concat("::").concat(n));
			versionMap.put(n, (v = cl.getAnnotation(version.class)) != null? v.value() : "0.0.1");
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
	public boolean isExist(String s) {
		if(!open)return false;
		return cmd.containsKey(s);
	}
	/**
	 * get a description
	 * @param cmdName
	 * @return string
	 */
	public String getCommandDescription(String cmdName) {
		if(desc.containsKey(cmdName) && open) return desc.get(cmdName);
		return "";
	}
	/**
	 * If all configure finished, then be able to run command line. This means settings of sprexor prevent to modify secretly.
	 * <br><b><span style="color:ff00ff">SIGN : It cannot be used after activate.</span></b>
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public void setup() {
		if(open || !isInit) return;
		if(sparser == null) {
			logger("< INFO > parser was not selected. switching to default parser...");
			sparser = new DefaultParser();
		}
		if(!open)open = !open;
		logger("< INFO > Sprexor setup successful");
	}
	/**
	 * get list of usable commands
	 * @return string array
	 */
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
		if(open) return;
		else if(isExist(str) || str.isEmpty()) return;
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
		if(open) return;
		if(c != '\0') comment = c;
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
	
	public String getVersion(String cmdName) {
		return versionMap.get(cmdName);
	}
	
	public boolean merge(SManager sm) {
		if(!open) return false;
		if(this.hashCode() == sm.hashCode()) return false;
		cmd.putAll(sm.cmd);
		logger(String.format("< INFO > Sprexor merged with : %d", sm.hashCode()));
		return true;
	}
	
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
	 * execute command line lightly.
	 * @param id : command name
	 * @param args : arguments
	 * @throws SprexorException 
	 * @since 0.2.3
	 */
	public int exec(String id, String[] args) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, "");
		
		if(!isExist(id)) {
			logger("< CRITICAL > Command not found : " + id);
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		SCommand obj = cmd.get(id);
		int i = obj.main(iostream, new SParameter(args), this);
		return i;
	}
	/**
	 * run a command line lightly.
	 * <br> and it will be run that you configured.
	 * @param com : command string to execute
	 * @throws SprexorException 
	 * @since 0.1
	 */
	public int exec(String com) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
		
		String id = sparser.id(com);
		if(isExist(id)) {
			logger("< CRITICAL > Command not found : " + id);
			throw new SprexorException(SprexorException.CMD_NOT_FOUND, id);
		}
		com = com.substring(id.length() + 1);
		SParameter component = new SParameter(sparser.processing(com));
		return cmd.get(id).main(iostream, component, this);
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
	synchronized public int run(String input, String options) throws SprexorException {
		if(!open) throw new SprexorException(SprexorException.ACTIVATION_FAILED, SOutputs.act);
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
									logger("< CRITICAL > invalid expression  : " + cache.toString());
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										SOutputs.syn);
								}
							}else if(c == '"'){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) {
							logger("< CRITICAL > invalid expression  : " + cache.toString());
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
						}
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
									logger("< CRITICAL > invalid expression  : " + cache.toString());
									throw new SprexorException(SprexorException.EXPRSS_ERR, 
										SOutputs.syn);
								}
							}else if(c == '\''){
								bl = 0;
								break;
							}else cache.append(c);
						}
						if(bl == 1) {
							logger("< CRITICAL > invalid expression  : " + cache.toString());
							throw new SprexorException(SprexorException.EXPRSS_ERR, 
								SOutputs.syn);
						};
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
					}else if(c == comment) { // comment (note)
						if(com) {
							while(++ allCount < comar.length) {
								c = comar[allCount];
								if(c == ';' || c == '\n') {
									args = trimArr(args);
									SParameter component = new SParameter(args);
									if(cmd.containsKey(id)) return cmd.get(id).main(iostream, component, this);
								}
							}
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
	
	{
		iostream = new IOCenter();
		this.iostream.out = new SprexorOstream((buf) -> {for(String s : buf)System.out.print(s);}) {

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
		};
		
		this.iostream.in = new SprexorIstream() {

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
		};
	}
	
}
