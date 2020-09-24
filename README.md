Sprexor - 0.2.10
=================

[if you want to see Korean, chlick here.](./KOREAN.md "한국어로 보기")

**index**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [Tools](#tools)
5. [GlobalData](#globaldata)
6. [cosmos.BasicPackages](#basicpackages)
7. [Exception](#exception)


#### Sprexor
	Sprexor(void | boolean) : Sprexor constructor. If argument is true, basic command(help, echo, var delete) will not be defined.
	boolean isExist() check if command is exist.
	String ~~getList()~~ : return list. @Deprecated
	void setComment(String) : set comment.   
	void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)
	void exec(String com) throws unknownCommand: execute command "com".
	void exec(String id, String[] args)
	void useSyntax(boolean b) : whether check basic syntax.
	void ~~initScope()~~ : @Deprecated because merged with constructor.
	void importSprex(sprexor.CommandProvider t | sprexor.CommandProvider[] t) : import Sprex to this(above) from t class.
	void send(String, IOCenter.TYPE) : send instant message.
	void activate() : should activate by this before use "exec" method. and if activate, it cannot setting properties.
	void call(String key) : control flow of system to key. Available keys : entry_on, entry_off. 
	void error_strict() : If it is called, throw SprexorException instead of print at IOCenter.
  <br>
  
#### IOCenter
    enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, custom1, custom2, custom3}
    IOCenter ioc = new IOCenter(Sprexor sp); // IOCenter is load output from sp.
    Object[] getMessage() : return recent message. index 0 : output, index 1 : TYPE
    Vector<Object[]> getOuput() : return all of printed message. And the scope is 'constructor'.
    void ExitEntry() : exit entry mode.
    Vector<Object[]> getBlockMessage() : return all of output that printed from 'exec' method that called once. So, it means the scope is 'method'.
  <br>
  
#### CommandProvider
    public Object code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove data that saved in GlobalData.)
    public default Object emptyArgs() : this method will be called when argument of Sprexor.exec is empty. (redefinable)
    public default error(Exception) : this method will be call when accur error in your code(from Sprexor.register). (redefinable)
    public default getCommandName() : for importSprex.
    public default help() : for importSprex.
    void EntryMode() : exit entry mode.
  <br>
  
#### Tools
	String Processer(String opt) : use subprocess (window : exe, linux : sh)
	byte AnalOption(String, boolean[])
	boolean OptionPrs(String, String, byte) throws Exception
	String[] binder(String[] ar, int start) : bind String Array from start index to end.
  <br>
  
#### GlobalData 
	if last character of key name is '_', this data would be read-only.
	GlobalData gd = new GlobalData()
	void putData(String key, Object value)
	Object getData(String key)
	void removeData(String key)
	boolean existData(Obejct data)
	boolean existKey(String key)
	boolean modify(String key, Object data)
	boolean reset()
	boolean forceReset()
  <br>
  
#### BasicPackages
	Let you know how to use this and it will be added many features.
	"example" command exists.
  <br>
  
#### Exception
	SprexorException
	CommandNotFoundException
	
---
*entry mode is useful feature. It is similar concept that type command "cat" in the linux.*
