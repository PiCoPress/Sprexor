# Nere 
- Nere
  - Nere nere = new Nere(); : init Nere.
  - boolean isExist(String) : check if command is exist.
  - String getList() : return list. @Deprecated
  - void setComment(String) : set comment.
  - void mkcmd(String str, CommandProvider cp, String hd) : str( command name), cp(refer below), hd(message to help)
  - void exec(String com) throws unknownCommand: execute command "com".
  - void execSyntax(boolean b) : whether check basic syntax.
  - void initRegistry()

 - IOCenter
   - IOCenter ioc = new IOCenter(Nere nere); // IOCenter is load output from nere.
   - Object[] getMessage() : return recent message.
 
  - CommandProvider
    - public Object apply(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd ()
    - public default Object no_arg_apply() : this method will be called when argument of nere.exec is empty. (redefinable)
    - public default ErrorEventListener(Exception) : this method will be call when accur error in your code(from nere.mkcmd). (redefinable)
    
  - Lib
    - String Processer(String opt) : use subprocess (window : exe, linux : sh)
    - byte AnalOption(String, boolean[])
    - boolean OptionPrs(String, String, byte) throws Exception
    
  - GlobalData
    - GlobalData gd = new GlobalData()
    - void putData(String key, Object value)
    - Object getData(String key)
    - void removeData(String key)
    - boolean existData(String key);
