# 스프렉서 (Sprexor) - 0.2.18-alpha5 Venom

[0.2.18 수정사항](./Update.md)

**목차**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [CommandFactory](#commandfactory)
5. [Tools](#tools)
7. [cosmos.BasicPackages](#the-cosmos)
8. [Exception](#exception)
9. [Component](#component)
9. [Basic Features](#basic-feature)
10. [예시코드보기](./test.java)
11. [메뉴얼](#menual)


#### Sprexor

>Sprexor s = new Sprexor(); : 스프렉서 초기화

>boolean isExist(String) : 이 커맨드가 존재하는지 확인합니다.

>void setComment(String) : 주석을 사용할 문구를 정합니다.

>void register(String str, CommandProvider cp, String hd) : str( 커맨드 이름), cp(아래 참조), hd(help 명령어에서 사용될 도움말)

>void exec(String com) throws CommandNotFoundException: com 이라는 문자열을 파싱 후, 자동으로 실행해줍니다.

>void exec(String id, String[] args)

>void exec(String id, String[] args, boolean[] isWrapped)

>void useSyntax(boolean b) : 기본 문법을 사용할건지의 여부

>void importSprex(CommandProvider t) : t라는 클래스로부터 스프렉서 를 임폴트합니다. t 클래스는 CommandProvider 를 상속받아야 합니다.

>void send(String, IOCenter.TYPE) : 인스턴트 메세지를 전송합니다.

>void activate() : 이것이 실행되어야만 exec메소드를 포함한 몇가지 메서드들를 실행할 수 있으며, 만일 실행되었을 경우 상세 설정을 할 수 없습니다. (충돌 방지)

>void call(문자열 키) : 키 값을 통해 정해진 명령을 실행합니다. 사용 가능 키: entry_on, entry_off

>void error_strict() : 이 메서드가 실행된다면 애러를 IOCenter에 저장하는것 대신 오류를 던집니다.

>void unSemicolon() : 세미콜론 사용을 하지 않습니다.

>void unBasicFeatures() : 기본 명령어 등록을 하지 않습니다.

>void ignoreUpperCase() : 대소문자를 무시하여 실행합니다.

>String eval(String)

>Impose impose

>Reflection reflect

>Object label
	
	
#### IOCenter

>Sprexor의 입출력을 담당합니다.

>>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, UNKNOWN, WARN}

>>exitEntry() : 엔트리 모드를 종료합니다. 

>>@Deprecated ~getBlockMessage()~ 

>>@Deprecated ~getMessage()~ 

>>@Deprecated ~getOuput()~ 
	
	
#### CommandProvider

>public IOcenter code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(인자), isWrapped [i] (arg[i]가 ' 또는 "로 묶였는지 여부), gd (GlobalData로부터 저장하거나 삭제할 수 있습니다. )

>public default IOCenter emptyArgs(GlobalData) : exec에서 들어온 매개변수가 없으면 실행됩니다. (재정의 가능)

>public default Object error(Exception) : 오버라이드된 code 메소드에서 오류날 경우 이 메소드가 실행됩니다.(register로부터). (재정의 가능)

>public default Object EntryMode(String msg) : 이 메서드의 반환 값이 null이 아닐 경우 그 엔트리 모드가 끝날 때까지 이 메서드가 실행됩니다. (확장 용이성)


#### CommandFactory

>CommandFactory는 Sprexor에서 임폴트될 명령어를 만들기 위해 사용됩니다.

>>public IOcenter code(String[] args, boolean[] isWrapped, GlobalData gd, Sprexor) : arg(인자), isWrapped [i] (arg[i]가 ' 또는 "로 묶였는지 여부), gd (GlobalData로부터 저장하거나 삭제할 수 있습니다. ), Sprexor는 현재 인스턴스에 대한 오브젝트입니다.

>>public default IOCenter emptyArgs(GlobalData, Sprexor) : exec에서 들어온 매개변수가 없으면 실행됩니다. (재정의 가능), Sprexor는 현재 인스턴스에 대한 오브젝트입니다.

>>public default Object error(Exception) : 오버라이드된 code 메소드에서 오류날 경우 이 메소드가 실행됩니다.(register로부터). (재정의 가능)

>>public default String getCommandName() : 임폴트 전용 메소드

>>public default String help() : 임폴트 전용 메소드

>>public default Object EntryMode(String msg) : 이 메서드의 반환 값이 null이 아닐 경우 그 엔트리 모드가 끝날 때까지 이 메서드가 실행됩니다. (확장 용이성)
    
    
#### Tools

>String Processer(String opt) : 서브프로세스 사용 (window : exe, linux : sh)

>String[] binder (문자열 배열, 시작값) : 문자열 배열에서 사직값부터 마지막 값까지 묶어 반환합니다.

>CommandFactory[] toCFClass(CommandFactory...) : 매개변수들을 배열로 반환합니다. (CommandProvider 의 referenceClass 전용)

>String arg2String(String[]) : 문자열 배열을 문자열로 반환합니다.

>String arg2String(String[], String) : 배열 사이에 두번째 매개변수를 넣어 반환합니다.

>String[] excludeArr(String[], int) : 배열에서 두번째 매개변수의 인덱스 값을 제외한 나머지를 반환합니다.

>String[] cutArr(String[], int startIndex) : excludeArr 의 상위 호환, String.substring 과 비슷합니다.

>String SMT_FORM(String) : 문자열의 특정 형식에 맞는 태그를 탭이나 개행문자로 변환합니다.

>@Deprecated ~smooth(String[] all, String[] optList, Class cl)~

>@Deprecated ~AnalOption(String, boolean[])~

>@Deprecated ~OptionPrs(String, String, byte)~ throws Exception
    
    
#### The Cosmos

##### BasicBackages

>이걸 어떻게 사용할 지 알려주고, 미래에 많은 기능이 추가될 것입니다.

>>"find" : 리눅스의 grep과 유사합니다. 버그 수정됨.
>>"repeat" : 입력된 문자를 반복합니다.
	
	
#### Exception

>SprexorException

>CommandNotFoundException

#### Component

>String[] get() : 인자를 문자열 배열로 반환합니다.

>Unit get(int) : 아래에 있는 부수 클래스를 반환합니다.

>String getsf(int) : 문자열로 해당 원소를 반환합니다.

>String gets(int) : 옵션을 무시하고 유효성이 있는 원소를 반환합니다.

>String[] getAllOption() : 모든 옵션을 순서대로 가져옵니다.

>String[] Parse(String)

>void add(String)

>int length() : 인수의 길이를 반환합니다.

>*Unit Class*

>>- String toString()

>>- boolean isWrapped() : 문자열로 감싸져 있는 원소인지 확인합니다.

>>- Object label

#### basic feature

>@(name) :  저장된 값을 불러옵니다.

>var (name) (value) : 이름=값 으로 정의합니다.

>echo

>help : 도움말을 출력합니다.

>delete (name) : 변수를 삭제합니다.

>commands : 사용 가능 커맨드를 출력합니다.
	
---
	*엔트리 모드는 유용한 기능입니다. 리눅스에서 'cat'명령어를 입력하는 것과 유사합니다.*
---
# Menual

1. *new 생성자로 객체 생성*<br>
**java Sprexor sp = new Sprexor()** 또는 **Sprexor sp = new Sprexor(false)** <span style="color:green">// 매개변수가 false 이면 help, echo, var, delete 와 같은 기본 명령어가 등록되지 않습니다.</span><br>   
2. *여러가지의 내부 설정*<br>
    - **useSyntax** : 매개변수가 false이면 기본 문법을 사용하지 않도록 설정하고, true라면 기본 문법을 사용하도록 설정합니다.
    - **setComment** : 주석으로 사용할 문자를 정합니다. 문법 사용이 false 라면 소용이 없습니다. 
    - **importSprex** : 매개변수에는 CommandProvider 를 implement 한 클래스를 집어넣습니다.클래스 배열도 들어올 수 있습니다. 그 클래스의 필수 구현 요소는 위를 참조하세요.
    - **error_strict** : 호출하지 않았을 경우 파싱중 발생한 오류나 내부 다른 메소드들의 오류가 MessageLog 에 쌓이지만 호출할 경우, try-catch 구문을 사용하여 예외를 처리해야 합니다.
    - **register** : 첫번쨰 매개변수는 명령어의 이름을 지정하고(exec 에서 사용됩니다), 두번쨰 매개변수는 new CommandProvider(){ } 에서 오버라이딩 할 메소드에서 *code*는 실행시킬 코드를 작성(리턴값은 마지막에 출력할 메세지, sp.send 로 메세지의 타입을 지정하여 출력 가능)<br>, *emptyArgs* 는 명령어의 인자가 없을 경우 호출<br>, *error*는 code메소드 내에서 오류가 발생했을 경우 호출됩니다. 마지막 매개변수는 help 명령어에서 이 명령어에 대한 도움말을 제공하는 문자열입니다.
    <br>   
3. *activate 호출*<br>
이 메소드가 실행되면 위의 내부 설정을 더 이상 사용할 수 없으며, 명령어를 실행할 수 있도록 설정합니다.<br>   
4. *IOCenter 생성자로 메세지를 받아오기*<br>
IOCenter io = new IOCenter(sp) <span style="color:green">//명령어에서 출력한 모든 메세지를 읽거나, 출력에 대한 타입을 지정할 수 있고, 엔트리모드에서 강제로 나갈 수 있습니다.
    - **getMessage** : 가장 최근에 출력된 메세지를 Object[2] 로 반환합니다. 인덱스 1은 메세지, 2번째는 메세지 타입(STDOUT, ERR, CMT, NO_VALUE, UNKNOWN, custom1~3)입니다. 단위는 명령어입니다.
    - **getBlockMessage** : exec으로 실행했을 때 출력된 메세지를 벡터로 반환합니다. 단위는 메소드 입니다.
    - **getOutput** : sp 객체에서 출력된 모든 메세지를 백터로 반환합니다. 단위는 생성자 입니다.
    - **exitEntry** : 엔트리모드를 나갑니다.<br>   
5. *실행하기*<br>
exec 메소드의  매개변수에 String을 넣으면 알아서 파싱 후 잘 실행해 주고, 만일 다른 parser를 사용할 경우나 직접 인자를 넣어 실행하려면 매개변수에 String id, String[] args를 넣으면 됩니다.