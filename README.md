Sprexor - 0.2.18-alpha3 Venom
=================
[Updated Things](./Update.md)

[if you want to see Korean, chlick here.](./KOREAN.md "한국어로 보기")

**index**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [CommandFactory](#commandfactory)
5. [Tools](#tools)
6. [GlobalData](#globaldata)
7. [cosmos.BasicPackages](#the-cosmos)
8. [Exception](#exception)
9. [Component](#component)
10. [Basic Features](#basic-feature)
11. [See example code](./test.java)
12. [Menual](#menual)


#### Sprexor

>Sprexor() : Sprexor constructor.

>boolean isExist(String) Check if the Command is exist.

>void setComment(String) : Set a comment character.

>void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)

>void exec(String com) throws CommandNotFoundException: Execute command "com".

>void exec(String id, String[] args)

>void exec(String id, String[] args, boolean[] isWrapped)

>void useSyntax(boolean b) : Whether check basic syntax.

>void importSprex(sprexor.CommandProvider t) : Import Sprex to this(above) from T class.

>void send(String, IOCenter.TYPE) : Send instant message.

>void activate() : It should activate by this before use "exec" method. and if activate, it cannot setting properties.

>void call(String key) : control flow of system to key. Available keys : entry_on, entry_off.

>void error_strict() : If it is called, throw SprexorException instead of print at IOCenter.

>void setInterruptChar(String|char) : Interrupt Sprexor during parsing if detected this char.

>void unSemicolon() : Semicolon is not abled to use.

>void unBasicFeatures() : Basic commands are not registered.

>void ignoreUpperCase() : Ignore Upper case of character.

>Impose impose

>Reflection reflect
	
  
#### IOCenter

>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, UNKNOWN, WARN}

>IOCenter ioc = new IOCenter(Sprexor sp); // IOCenter lead to load output from sp.

>void ExitEntry() : Exit the EntryMode.

>@Deprecated ~getBlockMessage()~

>@Deprecated ~getMessage()~

>@Deprecated ~getOuput()~

    
  
#### CommandProvider

>>public IOCenter code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove a data that saved in GlobalData.)

>>public default Object emptyArgs(GlobalData) : This method will be called when argument of Sprexor.exec is empty. (redefinable)

>>public default Object error(Exception) : This method will be called when occurred error in your code(from Sprexor.register). (redefinable)

>>public default Object EntryMode(String msg) : Run if in the entry mode.

#### CommandFactory

>CommandFactory is used for make to Class Command(s) will be imported to Sprexor.

>>public IOCenter code(String[] args, boolean[] isWrapped, GlobalData gd, Sprexor) : arg(arguments), isWrapped[i](whether arg[i] is wrapped by ' or "), gd (you can get or put or remove a data that saved in GlobalData.), Sprexor has this instance.

>>public default Object emptyArgs(GlobalData, Sprexor) : This method will be called when argument of Sprexor.exec is empty. (redefinable), Sprexor has this instance.

>>public default Object error(Exception) : This method will be called when occurred error in your code(from Sprexor.register). (redefinable)

>>public default String getCommandName() : For the ImportSprex.

>>public default String help() : For the ImportSprex.

>>public default Object EntryMode(String msg) : Run if in the entry mode.
    
  
#### Tools

>String Processer(String opt) : use subprocess (window : exe, linux : sh)


>String[] binder(String[] ar, int start) : bind String Array from start index to end.

>CommandFactory[] toCFClass(CommandFactory...) : It return CommandProvider array to arguments. (for referenceClass of CommandProvider)

>String arg2String(String[]) : It return String to array.

>String arg2String(String[], String) : Return the String by joining second argument.

>String[] excludeArr(String[], int) : It return array that excluded index of second parameter.

>String[] cutArr(String[], int startIndex) : Advanced excludeArr. It is similar to "String.substring".

>String SMT_FORM(String) : Replace certain tag in the String to tab or newline char.

>@Deprecated ~smooth(String[] all, String[] optList, Class cl)~ 

>@Deprecated ~AnalOption(String, boolean[])~

>@Deprecated ~OptionPrs(String, String, byte)~ throws Exception

	
  
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
	
  
#### The Cosmos

##### BasicPackages

>Let you know how to use this and it will be added many features.

>>"find" : This command finds content with unit of line. Bug fixed.
>>"repeat" : Repeat the text.
	
  
#### Exception

>SprexorException

>CommandNotFoundException

#### Component

>String[] get() : Return string array.

>Unit get(int) : Return Unit Class.

>String gets(int) : Return a unit of argument to string.

>String getsWithoutOption(int) : Return a unit by ignoring options.

>String[] getAllOption() : Get all of options.

>String[] Parse(String)

>int length() : Return the size of arguments.

>*Unit Class*

>>- String toString()

>>- boolean isWrapped() : If it is wrapped with string, return true, otherwise false.
	
#### Basic Feature

>@(name) :  Load saved value.

>var (name) (value) : Define (name) = (value).

>echo

>help : Print message to help.

>delete (name) : Delete variable.

>commands : Print the Command list.
	
---
	*entry mode is useful feature. It is similar concept that type command "cat" in the linux.*
---
# Menual
1. *create object by 'new constructor'*<br>
**java Sprexor sp = new Sprexor()** or **Sprexor sp = new Sprexor(false)** <span style="color:green">// If parameter is false, does not register basic commands are help, echo, var, and delete.</span><br>   
2. *property settings*<br>
    - **useSyntax** : If parameter is false, cannot use syntax(variable, comment etc..) and true, explicitly can use syntax.
    - **setComment** : set character to use comment. If 'useSyntax' is false, so non-effect anything. 
    - **importSprex** : put in class by 'implement CommandProvider' and allowed Class array. please refer to the above that necessary implement method.
    - **error_strict** : When you didn't called,that occurred error during parsing or other methods is saved MessageLog, throw exception.
    - **register** : first parameter is name of command(used by exec), second parameter is 'new CommandProvider(){ }', *code* - programming to run(returned value would be last message, sp.send is able to print with msg type)<br>, *emptyArgs* is called when argument emptied<br>, *error* - be called when caught in method 'code'. last parameter is String to show with 'help'
     command.
    - **setInterruptChar** : set a char to interrupt parsing.
    <br>   
3. *calling activate*<br>
If this method is called, then you cannot set property settings, and prepare to run command.<br>   
4. *receiving message by IOCenter*<br>
IOCenter io = new IOCenter(sp) <span style="color:green">//it read all of message, set message type to send, and let out from entry mode to force.
    - **getMessage** : return the message to Object[2] first index: msg, second : msg type(STDOUT, ERR, CMT, NO_VALUE, UNKNOWN, custom1~3). And unit is 'command'.
    - **getBlockMessage** : return the message to Vector. And unit is 'method'.
    - **getOutput** : return the message to Vector. And unit is 'constructor'.
    - **exitEntry** : exit entry mode.<br>   
5. *execute*<br>
input the parameter(string), then parse string and run automatically, or input String(id), String array(args) either using other parser or evaluate command.
