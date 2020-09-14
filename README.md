# Nere (Ne - r) 0.2.3
- Nere
  - Nere nere = new Nere(); : init Nere.
  - boolean isExist(String) : check if command is exist.
  - String getList() : return list. @Deprecated
  - void setComment(String) : set comment.
  - void register(String str, CommandProvider cp, String hd) : str( command name), cp(refer below), hd(message to help)
  - void exec(String com) throws unknownCommand: execute command "com".
  - void exec(String id, String[] args)
  - void useSyntax(boolean b) : whether check basic syntax.
  - void initScope()
  - void importNere(nere.loadNere.BasicPackages.ImportTemplate t) : import Nere to nere(above) from t.
  - void send(String, IOCenter.TYPE) : send instant message.
  - void activate() : should activate by this before use "exec" method. and if activate, it cannot setting properties.

 - IOCenter
   - enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, custom1, custom2, custom3}
   - IOCenter ioc = new IOCenter(Nere nere); // IOCenter is load output from nere.
   - Object[] getMessage() : return recent message. index 0 : output, index 1 : TYPE
   - Vector<Object[]> getOuput() : return all of printed message.
 
  - CommandProvider
    - public Object code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove data that saved in GlobalData.)
    - public default Object emptyArgs() : this method will be called when argument of nere.exec is empty. (redefinable)
    - public default error(Exception) : this method will be call when accur error in your code(from nere.register). (redefinable)
    
  - Lib
    - String Processer(String opt) : use subprocess (window : exe, linux : sh)
    - byte AnalOption(String, boolean[])
    - boolean OptionPrs(String, String, byte) throws Exception
    
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
    
  - nere.loadNere.BasicPackages : let know how to use this and it will be added many features.
    - ImportTemplate : constructor(HashMap<String, CommandProvider> cmds, HashMap<String, Object> variables, String cmdLists - must be end of "\n", HashMap<String, String> help)
    - "example" command exists.

## basic feature
 - @(name) :  load value that called (name) from memory.
 - var (name) (value) : define name is value.
 - echo
 - help : feature improved.
