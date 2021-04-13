Sprexor - 0.2.19
=================
[Updated Things](./changeLog/0.2.19.md)


**index**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [CommandFactory](#commandfactory)
7. [cosmos.BasicPackages](#the-cosmos)
8. [Exception](#exception)
9. [Component](#component)
10. [SprexorOstream](#sprexorostream)
11. [SprexorIstream](#sprexoristream)
12. [Standard Commands](#standard-commands)
13. [See example code](./test.java)


#### Sprexor

>**Methods**

>- boolean isExist(String) Check if the Command is exist.

>- void setComment(String) : Set a comment character.

>- void register(String str, CommandProvider cp, String hd) : str(command name), cp(refer below), hd(message to help)

>- void exec(String com) throws CommandNotFoundException: Execute command "com".

>- void exec(String id, String[] args)

>- void importSprex(sprexor.CommandProvider t) : Import Sprex to this(above) from T class.

>- void activate() : It should activate by this before use "exec" method. and if activate, it cannot setting properties.

>- void run(String) : Execute parameter a line for more improved performance and functions.

>**Fields**

>- VERSION

>- CODENAME

>- LIST

>- Impose impose

>- Reflection reflect

>- Object label
	
  
#### IOCenter

>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, UNKNOWN, WARN}

>Component getComponent() : Get arguments.

>SprexorIstream in

>SprexorOstream out

    
  
#### CommandProvider

>>public int code(IOCenter) 

>>public String error(Exception) : This method will be called when occurred error.


#### CommandFactory

>CommandFactory is used for make to Class Command(s) will be imported to Sprexor.

>>public int code(IOCenter, Sprexor) :

>>public default String getCommandName() : For the ImportSprex.

>>public default String help() : For the ImportSprex.
	
  
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

>String[] getValidParameters() : get valid array

>String[] getValidParameters(int startAt) : get valid array 

>void add(String)

>boolean isEmpty() : If argument is empty, return true.

>int length() : Return the size of arguments.

>*Unit Class*

>>- String toString()

>>- Object label

>>Unit next()

>>Unit prev()


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

	
#### Standard Commands

>@(name) :  Load saved value.

>var [ACTION] args...

>echo

>help : Print message to help.

>delete (name) : Delete variable.

>commands : Print the Command list.