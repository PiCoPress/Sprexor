# Nere 
- Nere
  - Nere nere = new Nere(); : init Nere.
  - boolean nere.isExist(String) : check if command is exist.
  - String nere.getList() : return list.
  - void setComment(String) : set comment.
  - void mkcmd(String str, CommandProvider cp, String hd) : str( command name), cp(refer below), hd(message to help)
  - void exec(String com) throws unknownCommand: execute command "com".

 - IOCenter
   - IOCenter ioc = new IOCenter(Nere nere); // IOCenter is load output from nere.
   - Object[] getMessage() : return recent message.
 
  - CommandProvider
    - public Object apply(String[] args, boolean[] isWrapped) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or ")
    - public default Object no_arg_apply() : this method will be called when argument of nere.exec is empty. (redefinable)
    - public default ErrorEventListener(Exception) : this method will be call when accur error in your code(from nere.mkcmd). (redefinable)
    
  - Lib
    - String Processer(String opt) : use subprocess (window : exe, linux : sh)
    - byte AnalOption(String, boolean[])
    - boolean OptionPrs(String, String, byte) throws Exception
