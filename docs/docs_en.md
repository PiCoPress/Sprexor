Sprexor - 0.2.18-beta1
=================
[Updated Things](./Update.md)


**index**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [CommandFactory](#commandfactory)
5. [Tools](#tools)
7. [cosmos.BasicPackages](#the-cosmos)
8. [Exception](#exception)
9. [Component](#component)
10. [SprexorOstream](#sprexorostream)
11. [SprexorIstream](#sprexoristream)
12. [Basic Features](#basic-feature)
13. [See example code](./test.java)


#### Sprexor

>**Classes**

>- boolean isExist(String) Check if the Command is exist.

>- void setComment(String) : Set a comment character.

>- void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)

>- void exec(String com) throws CommandNotFoundException: Execute command "com".

>- void exec(String id, String[] args)

>- void useSyntax(boolean b) : Whether check basic syntax.

>- void importSprex(sprexor.CommandProvider t) : Import Sprex to this(above) from T class.

>- void activate() : It should activate by this before use "exec" method. and if activate, it cannot setting properties.

>- void unSemicolon() : Semicolon is not abled to use.

>- void unBasicFeatures() : Basic commands are not registered.

>- void ignoreUpperCase() : Ignore Upper case of character.

>- String eval(String)

>- void run(String) : Execute parameter a line for more improved performance and functions.

>**Fields**

>- Impose impose

>- Reflection reflect

>- Object label
	
  
#### IOCenter

>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, UNKNOWN, WARN}

    
  
#### CommandProvider

>>public int code(IOCenter) 

>>public String error(Exception) : This method will be called when occurred error.


#### CommandFactory

>CommandFactory is used for make to Class Command(s) will be imported to Sprexor.

>>public int code(IOCenter, Sprexor) :

>>public default String getCommandName() : For the ImportSprex.

>>public default String help() : For the ImportSprex.
    
  
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
	
  
#### The Cosmos

##### BasicPackages

>Let you know how to use this and it will be added many features.

>>"find" : This command finds content with unit of line. Bug fixed.
>>"for" : Repeat the text.
	
  
#### Exception

>SprexorException


#### Component

>String[] Parse(String)

>String[] get() : Return string array.

>Unit get(int) : Return Unit Class.

>String getsf(int) : Return a unit of argument to string.

>String gets(int) : Return a unit by ignoring options.

>String[] getAllOption() : Get all of options.

>void add(String)

>boolean isEmpty() : If argument is empty, return true.

>int length() : Return the size of arguments.

>*Unit Class*

>>- String toString()

>>- boolean isWrapped() : If it is wrapped with string, return true, otherwise false.

>>- Object label


#### SprexorOstream

>void setType(IOCenter.TYPE)

>void print()

>void print(String msg)

>void print(String msg, int value)

>void println()

>void println(String msg)

>void println(String msg, int value)

>void clear()

>SprexorOstream build()

>SprexorOstream add(String msg)

>SprexorOstream add(String msg, int value)

>void send()

>>**Field**
 - IOCenter.TYPE type


#### SprexorIstream

>- void prompt()

>- void prompt(String msg)

>- void prompt(String msg, int value)

>- String flush()

>- String getln()

>- String getln(String msg)

>- String getln(String msg, int value)

>- void buffering(String value)

	
#### Basic Feature

>@(name) :  Load saved value.

>var (name) (value) : Define (name) = (value).

>echo

>help : Print message to help.

>delete (name) : Delete variable.

>commands : Print the Command list.