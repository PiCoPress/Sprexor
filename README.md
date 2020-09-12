# Nere 0.2.1
- Nere
  - Nere nere = new Nere(); : init Nere.
  - boolean isExist(String) : check if command is exist.
  - String getList() : return list. @Deprecated
  - void setComment(String) : set comment.
  - void mkcmd(String str, CommandProvider cp, String hd) : str( command name), cp(refer below), hd(message to help)
  - void exec(String com) throws unknownCommand: execute command "com".
  - void useSyntax(boolean b) : whether check basic syntax.
  - void initRegistry()

 - IOCenter
   - enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE}
   - IOCenter ioc = new IOCenter(Nere nere); // IOCenter is load output from nere.
   - Object[] getMessage() : return recent message. index 0 : output, index 1 : TYPE
   - Vector<Object[]> getOuput() : return all of printed message.
 
  - CommandProvider
    - public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove data that saved in GlobalData.)
    - public default Object no_arg_apply() : this method will be called when argument of nere.exec is empty. (redefinable)
    - public default ErrorEventListener(Exception) : this method will be call when accur error in your code(from nere.mkcmd). (redefinable)
    
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

## basic feature
 - @(name) :  load value that called (name) from memory.
 - var (name) (value) : define name is value.
 - echo
 - help
