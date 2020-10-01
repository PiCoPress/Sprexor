Sprexor - 0.2.11
=================

[if you want to see Korean, chlick here.](./KOREAN.md "한국어로 보기")

**index**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [Tools](#tools)
5. [GlobalData](#globaldata)
6. [cosmos.BasicPackages](#standard-library)
7. [Exception](#exception)
8. [see example code](./test.java)
9. [Menual](#menual)


#### Sprexor

>Sprexor(void | boolean) : Sprexor constructor. If argument is false, basic command(help, echo, var delete) will not be defined.

>boolean isExist() check if command is exist.

>void setComment(String) : set comment.

>void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)

>void exec(String com) throws CommandNotFoundException: execute command "com".

>void exec(String id, String[] args)

>void exec(String id, String[] args, boolean[] isWrapped)

>void useSyntax(boolean b) : whether check basic syntax.

>void importSprex(sprexor.CommandProvider t) : import Sprex to this(above) from t class.

>void send(String, IOCenter.TYPE) : send instant message.

>void activate() : should activate by this before use "exec" method. and if activate, it cannot setting properties.

>void call(String key) : control flow of system to key. Available keys : entry_on, entry_off.

>void error_strict() : If it is called, throw SprexorException instead of print at IOCenter.

>void setInterruptChar(String|char) : Interrupt Sprexor during parsing if detected this char.

>void unSemicolon() : semicolon is not abled to use.

>void unBasicFeatures() : basic commands is not registered.

	
  
#### IOCenter

>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, custom1, custom2, custom3}

>IOCenter ioc = new IOCenter(Sprexor sp); // IOCenter is load output from sp.

>Object[] getMessage() : return recent message. index 0 : output, index 1 : TYPE

>Vector<Object[]> getOuput() : return all of printed message. And the scope is 'constructor'.

>void ExitEntry() : exit entry mode.

>Vector<Object[]> getBlockMessage() : return all of output that printed from 'exec' method that called once. So, it means the scope is 'method'.

    
  
#### CommandProvider

>public Object code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove data that saved in GlobalData.)

>>public default Object emptyArgs() : this method will be called when argument of Sprexor.exec is empty. (redefinable)

>>public default error(Exception) : this method will be call when occurred error in your code(from Sprexor.register). (redefinable)

>>public default getCommandName() : for importSprex.

>>public default help() : for importSprex.

>>public default Object EntryMode(String msg) : be run if in the entry mode.
    
  
#### Tools

>String Processer(String opt) : use subprocess (window : exe, linux : sh)

>byte AnalOption(String, boolean[])

>boolean OptionPrs(String, String, byte) throws Exception

>String[] binder(String[] ar, int start) : bind String Array from start index to end.

>CommandProvider[] toCPClass(CommandProvider...) : It return CommandProvider array to arguments. (for referenceClass of CommandProvider)

>String arg2String(String[]) : It return String to array.

>String[] excludeArr(String[], int) : It return array that excluded index of second parameter.

	
  
#### GlobalData 

>This is one of parameter of CommandProvider->code. Also it is scope that can access each the registered command. If last character of key name is '_', this data would be read-only.

>>GlobalData gd = new GlobalData()

>>void putData(String key, Object value)

>>Object getData(String key)

>>void removeData(String key)

>>boolean existData(Obejct data)

>>boolean existKey(String key)

>>boolean modify(String key, Object data)

>>boolean reset()

>>boolean forceReset() : Although scope has read-only data, erase all of data.
	
  
#### Standard Library

##### BasicPackages

>Let you know how to use this and it will be added many features.

>>"find" : It is feature like linux command 'grep' .
	
  
#### Exception

>SprexorException

>CommandNotFoundException
	
	
---
	*entry mode is useful feature. It is similar concept that type command "cat" in the linux.*
---
# Menual
1. *create object by 'new constructor'*<br>
**java Sprexor sp = new Sprexor()** 또는 **Sprexor sp = new Sprexor(false)** <span style="color:green">// If parameter is false, does not register basic commands are help, echo, var, and delete.</span><br>   
2. *property settings*<br>
    - **useSyntax** : If parameter is false, cannot use syntax(variable, comment etc..) and true, explicitly can use syntax.
    - **setComment** : set character to use comment. If 'useSyntax' is false, so non-effect anything. 
    - **importSprex** : put in class by 'implement CommandProvider' and allowed Class array. please refer to the above that necessary implement method.
    - **error_strict** : When you didn't called,that occurred error during parsing or other methods is saved MessageLog, throw exception.
    - **register** : first parameter is name of command(used by exec), second parameter is 'new CommandProvider(){/*TODO*/}', *code* - programming to run(returned value would be last message, sp.send is able to print with msg type)<br>, *emptyArgs* is called when argument of command is empty<br>, *error* - be called when caught in method 'code'. last parameter is string that provide in 'help'
     command.
    - **setInterruptChar** : set a char to interrupt parsing.
    <br>   
3. *calling activate*<br>
If this method is called, then you cannot set property settings, and prepare to run command.<br>   
4. *receiving message by IOCenter*<br>
IOCenter io = new IOCenter(sp) <span style="color:green">//it read all of message, set message type to send, and let out from entry mode to force.
    - **getMessage** : return the message to Object[2] first index: msg, second : msg type(STDOUT, ERR, CMT, NO_VALUE, custom1~3). And unit is 'command'.
    - **getBlockMessage** : return the message to Vector. And unit is 'method'.
    - **getOutput** : return the message to Vector. And unit is 'constructor'.
    - **exitEntry** : exit entry mode.<br>   
5. *execute*<br>
input the parameter(string), then parse string and run automatically, or input String(id), String array(args) either using other parser or evaluate command.
