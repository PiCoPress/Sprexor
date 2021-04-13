# 스프렉서 (Sprexor) - 0.2.19

[0.2.19 수정사항](./changeLog/0.2.19.md)

**목차**
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
13. [예시코드보기](../test.java)


#### Sprexor

>**Classes**

>- boolean isExist(String) : 이 커맨드가 존재하는지 확인합니다.

>- void setComment(String) : 주석을 사용할 문구를 정합니다.

>- void register(String str, CommandProvider cp, String hd) : str( 커맨드 이름), cp(아래 참조), hd(help 명령어에서 사용될 도움말)

>- void exec(String com) throws CommandNotFoundException: com 이라는 문자열을 파싱 후, 자동으로 실행해줍니다.

>- void exec(String id, String[] args)

>- void importSprex(CommandProvider t) : t라는 클래스로부터 스프렉서 를 임폴트합니다. t 클래스는 CommandProvider 를 상속받아야 합니다.

>- void activate() : 이것이 실행되어야만 exec메소드를 포함한 몇가지 메서드들를 실행할 수 있으며, 만일 실행되었을 경우 상세 설정을 할 수 없습니다. (충돌 방지)

>- void run(String input, String options) : 더 향상된 성능과 기능들로 파라미터 한 줄을 실행합니다.

>**Fields**

>- Impose impose

>- Reflection reflect

>- Object label
	
	
#### IOCenter

>Sprexor의 입출력을 담당합니다.

>>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, UNKNOWN, WARN}

>>Component getComponent() : 매개변수를 가져옵니다.

>>SprexorIstream in

>>SprexorOstream out


	
#### CommandProvider

>public int code(IOCenter io) 


#### CommandFactory

>CommandFactory는 Sprexor에서 임폴트될 명령어를 만들기 위해 사용됩니다.

>>public int code(IOCenter)

>>public default String getCommandName() : 임폴트 전용 메소드

>>public default String help() : 임폴트 전용 메소드

    
#### The Cosmos

##### BasicBackages

>이걸 어떻게 사용할 지 알려주고, 미래에 많은 기능이 추가될 것입니다.

>>"find" : 리눅스의 grep과 유사합니다. 버그 수정됨.
>>"for" : 입력된 문자를 반복합니다.
	
	
#### Exception

>SprexorException

#### Component

>String[] get() : 인자를 문자열 배열로 반환합니다.

>Unit get(int) : 아래에 있는 부수 클래스를 반환합니다.

>String getsf(int) : 문자열로 해당 원소를 반환합니다.

>String gets(int) : 옵션을 무시하고 유효성이 있는 원소를 반환합니다.

>String[] getAllOption() : 모든 옵션을 순서대로 가져옵니다.

>String[] Parse(String)

>String[] getValidParameters() : 유효한 매개변수 배열을 가져옵니다.

>String[] getValidParameters(int startAt) : 유효한 매개변수 배열을 가져옵니다.

>void add(String)

>boolean isEmpty() : 매개변수가 비었는지 확인합니다.

>int length() : 인수의 길이를 반환합니다.

>*Unit Class*

>>- String toString()

>>- Object label

>>Unit next() : 다음 파라미터를 가져옵니다.

>>Unit prev() : 이전 파라미터를 가져옵니다.


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

>@(name) :  저장된 값을 불러옵니다.

>var [ACTION] args...

>echo

>help : 도움말을 출력합니다.

>delete (name) : 변수를 삭제합니다.

>commands : 사용 가능 커맨드를 출력합니다. 넣으면 알아서 파싱 후 잘 실행해 주고, 만일 다른 parser를 사용할 경우나 직접 인자를 넣어 실행하려면 매개변수에 String id, String[] args를 넣으면 됩니다.