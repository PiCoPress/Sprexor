# 스프렉서 (Sprexor) - 0.2.16

**목차**
1. [Sprexor](#sprexor)
2. [IOCenter](#iocenter)
3. [CommandProvider](#commandprovider)
4. [Tools](#tools)
5. [GlobalData](#globaldata)
6. [cosmos.BasicPackages](#standard-library)
7. [Exception](#exception)
8. [Basic Features](#basic-feature)
9. [예시코드보기](./test.java)
10. [메뉴얼](#menual)


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

>void setInterruptChar(String|char) : 파싱중에 설정된 문자가 있을 경우 그 문자를 기준으로 강제 종료합니다.

>void unSemicolon() : 세미콜론 사용을 하지 않습니다.

>void unBasicFeatures() : 기본 명령어 등록을 하지 않습니다.

>void bound(Sprexor.dec) : 재정의된 인터페이스(dec) 를 통하여 커맨드가 발견되지 않았을 때의 처리방식을 설정합니다. dec.notfound, dec.out : 출력을 할 때마다 이 함수가 호출됩니다.

>void ignoreUpperCase() : 대소문자를 무시하여 실행합니다.
	
	
#### IOCenter

>코드의 꼬임을 방지하기 위해 만든 클래스

>>enum IOCenter.TYPE {STDOUT, CMT, ERR, NO_VALUE, custom1, custom2, custom3}

>>IOCenter ioc = new IOCenter(Sprexor sp); sp로부터 출력 결과를 가져옴

>>Object[] getMessage() : 최근 메세지를 반환합니다.. 목차 0 : 출력, 목차 1 : TYPE

>>Vector<Object[]> getOuput() : 출력된 모든 메세지를 반환합니다. 생성자 단위로 저장됩니다.

>>exitEntry() : 엔트리 모드를 종료합니다. 

>>Vector<Object[]> getBlockMessage() : 'exec'메서드가 실행되는 동안 출력된 모든 결과를 반환합니다. 즉, 함수 단위로 저장됩니다.
	
	
#### CommandProvider

>public IOcenter code(String[] args, boolean[] isWrapped, GlobalData gd) : arg(인자), isWrapped [i] (arg[i]가 ' 또는 "로 묶였는지 여부), gd (GlobalData로부터 저장하거나 삭제할 수 있습니다. )

>public default Object emptyArgs() : exec에서 들어온 매개변수가 없으면 실행됩니다. (재정의 가능)

>public default Object error(Exception) : 오버라이드된 code 메소드에서 오류날 경우 이 메소드가 실행됩니다.(register로부터). (재정의 가능)

>public default String getCommandName() : 임폴트 전용 메소드

>public default String help() : 임폴트 전용 메소드

>public default Object EntryMode(String msg) : 이 메서드의 반환 값이 null이 아닐 경우 그 엔트리 모드가 끝날 때까지 이 메서드가 실행됩니다. (확장 용이성)
    
    
#### Tools

>String Processer(String opt) : 서브프로세스 사용 (window : exe, linux : sh)

>byte AnalOption(String, boolean[]) : 옵션을 분석하며 그에 맞는 byte 를 반환합니다.

>boolean OptionPrs(String, String, byte) throws Exception : 분석된 옵션을 읽어 첫번쨰 매개변수와 일치한지 아닌지를 반환힙니다.

>String[] binder (문자열 배열, 시작값) : 문자열 배열에서 사직값부터 마지막 값까지 묶어 반환합니다.

>CommandProvider[] toCPClass(CommandProvider...) : 매개변수들을 배열로 반환합니다. (CommandProvider 의 referenceClass 전용)

>String arg2String(String[]) : 문자열 배열을 문자열로 반환합니다.

>String arg2String(String[], String) : 배열 사이에 두번째 매개변수를 넣어 반환합니다.

>String[] excludeArr(String[], int) : 배열에서 두번째 매개변수의 인덱스 값을 제외한 나머지를 반환합니다.

>String[] cutArr(String[], int startIndex) : excludeArr 의 상위 호환, String.substring 과 비슷합니다.

>void smooth(String[] all, String[] optList, Class cl) : all 매개변수는 탐색할 문자열 배열, optList 매개변수는 같은 방식의 처리를 할 옵션 이름들, 마지막 매개변수는 이 클래스에서 Option(String name, String value) 이라는 메서드를 찾아 실행합니다.
    
    
#### GlobalData

>이것은 CommandProvider의 code 메소드의 매개변수 중 하나입니다. 각 명령어마다 접근할 수 있는 하나의 스코프이며, 규칙은 어느 데이터를 삭제하거나 바꾸고자 할 경우 그 키의 이름의 마지막 글자가 _이면 읽기 전용이라고 정했습니다. 따라서 삭제나 변경은 불가능합니다.

>>GlobalData gd = new GlobalData()

>>void putData(String key, Object value)

>>Object getData(String key)

>>void removeData(String key)

>>boolean existData(Obejct data)

>>boolean existKey(String key)

>>boolean modify(String key, Object data)

>>boolean reset() : 스코프를 초기화하지만 읽기 전용 데이터가 있으면 실행되지 않습니다.

>>boolean forceReset() : 스코프에 읽기 전용인 데이터가 있어도 무시하고 전부 초기화합니다.
    
    
#### Standard library

##### BasicBackages

>이걸 어떻게 사용할 지 알려주고, 미래에 많은 기능이 추가될 것입니다.

>>"find" : 리눅스의 grep과 유사합니다. 버그 수정됨.
>>"for" : for(txt | code) (count) Str...
	
	
#### Exception

>SprexorException

>CommandNotFoundException

#### basic feature

>@(name) :  저장된 값을 불러옵니다.

>var (name) (value) : 이름=값 으로 정의합니다.

>echo

>help : 도움말을 출력합니다.

>delete (name) : 변수 삭제함

>commands : 사용 가능 커맨드 출력함
	
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
    - **setInterruptChar** : 강제로 종료할 문자 1글자를 설정합니다.
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