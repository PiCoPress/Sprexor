# Sprexor - 0.2.7 Beta
String Parser & Executor<br>
S...........p.r.............ex........or
- Sprexor
  - Sprexor spr = new Sprexor(); : Sprexor constructor.
  - boolean isExist(String) : check if command is exist.
  - String ~~getList()~~ : return list. @Deprecated
  - void setComment(String) : set comment.
  - void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)
  - void exec(String com) throws unknownCommand: execute command "com".
  - void exec(String id, String[] args)
  - void useSyntax(boolean b) : whether check basic syntax.
  - void ~~initScope()~~ : @Deprecated because merged with constructor.
  - void importSprex(sprexor.CommandProvider t | sprexor.CommandProvider[] t) : import Sprex to this(above) from t class.
  - void send(String, IOCenter.TYPE) : send instant message.
  - void activate() : should activate by this before use "exec" method. and if activate, it cannot setting properties.
  - void call(String key) : control flow of system to key. Available keys : entry_on, entry_off

 - IOCenter
   - enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, custom1, custom2, custom3}
   - IOCenter ioc = new IOCenter(Sprexor sp); // IOCenter is load output from sp.
   - Object[] getMessage() : return recent message. index 0 : output, index 1 : TYPE
   - Vector<Object[]> getOuput() : return all of printed message.
   - void ExitEntry() : exit entry mode.
 
  - CommandProvider
    - public Object code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove data that saved in GlobalData.)
    - public default Object emptyArgs() : this method will be called when argument of Sprexor.exec is empty. (redefinable)
    - public default error(Exception) : this method will be call when accur error in your code(from Sprexor.register). (redefinable)
    - public default getCommandName() : for importSprex.
    - public default help() : for importSprex.
    - void EntryMode() : exit entry mode.
    
  - Tools
    - String Processer(String opt) : use subprocess (window : exe, linux : sh)
    - byte AnalOption(String, boolean[])
    - boolean OptionPrs(String, String, byte) throws Exception
    - String[] binder(String[] ar, int start) : bind String Array from start index to end.
    
  - GlobalData : if last character of key name is '_', this data would be read-only.
    - GlobalData gd = new GlobalData()
    - void putData(String key, Object value)
    - Object getData(String key)
    - void removeData(String key)
    - boolean existData(Obejct data)
    - boolean existKey(String key)
    - boolean modify(String key, Object data)
    - boolean reset()
    - boolean forceReset()
    
  - sprexor.cosmos.BasicPackages : let know how to use this and it will be added many features.
    - "example" command exists.

## basic feature
  - @(name) :  load value that called (name) from memory.
  - var (name) (value) : define variable
  - echo
  - help
  - delete (name) : remove variable.


---
* entry mode is useful feature. It is similar concept that type command "cat" in the linux.
