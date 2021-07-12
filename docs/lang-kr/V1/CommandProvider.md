```java
public interface CommandProvider
```
>이 인터페이스는 명령어를 구동시키기 위한 양식입니다.

1.[error](#public-error)

2.[emptyArgs](#public-emptyargs)

3.[code](#public-code)

## `public error`

>code 메소드에서 오류가 발생할 경우, 이 함수가 호출됩니다.
>구현 여부 : 가능
>
>*반환* : Object
>반환된 값은 출력에 사용되먀, 기본 반환값은 null 입니다.
>
>|Arguments|Type|Detail|
>|--|--|--|
>|err|java.lang.Exception|예외 클래스|

---

## `public emptyArgs`
>실행될 명령어 입력의 매개변수가 비었다면, 이 함수가 호출됩니다.
>구현 여부 : 가능
>
>*반환* : Object
>반환된 값은 출력에 사용되며, 기본 반환값은 *`empty argument`* 입니다.

---

## `public code`
>명령어가 실행될 주 메소드입니다.
>구현 여부 : 강제
>
>*반환* : Object
>반환된 값은 출력에 사용됩니다.
>
>|Arguments|Type|Detail|
>|--|--|--|
>|args|String Array|명령어 인자 배열|
>|isWrapped|boolean Array|문자열로 묶여있는 매개변수인지 확인합니다.|
>|scope|sprexor.v1.GlobalData|생성된 클래스에서 실행되는 모든 명령어들이 공통으로 읽기/쓰기를 수행할 수 있는 클래스입니다.|

---

