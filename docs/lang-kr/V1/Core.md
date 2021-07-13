```java
public final class Core
```
>Sprexor가 작동될 수 있도록 여러 일을 수행하는 클래스입니다.

- 상수 *version*
- 상수 *codename*

1.[initScope](#public-initscope)

2.[isExist](#public-isexist)

3.[send](#public-send)

4.[activate](#public-activate)

5.[getList](#public-getlist)

6.[register](#public-register)

7.[setComment](#public-setcomment)

8.[useSyntax](#public-usesyntax)

9.[exec](#public-exec)

## `public initScope`
>현재 인스턴스의 GlobalData 값을 초기화합니다.
>
>*반환* : 없음

---

## `public isExist`
> *str*이라는 이름을 가진 명령어가 있는지 확인합니다.
> 
>*반환* : boolean / 
>존재하면 *true*, 그렇지 않으면 *false*
>
>|Arguments|Type|Detail|
>|--|--|--|
>|str|String|확인할 문자열 값|

---

## `public send`
>Sprexor의 내부 변수에 *str*을 저장합니다.
>
>*반환* : 없음
>
>|Arguments|Type|Detail|
>|--|--|--|
>|str|String|메세지 내용|
>|type|sprexor.v1.IOCenter.TYPE| 메세지 내용의 출력 타입|

---

## `public activate`
>여러 내부 설정을 마치고, 명령어를 실행할 준비를 합니다.
>
>이 함수 호출에 따라 사용 가능한 메서드 :
>|before|after|
>|----------|-------|
>|initScope|send|
>|activate|exec|
>|register| . |
>|setComment| . |
>|useSyntax| . |
>
>*반환* : 없음

---

## `public getList`
>*@Deprecated*
>등록된 명령어 목록을 출력합니다.
>
>*반환* : String / 
>빈 문자열을 출력함.

---

## `public register`
>중복되지 않는 새로운 명령어를 등록합니다.
>
>*반환* : 없음
>
>|Arguments|Type|Detail|remarks|
>|--|--|--|--|
>|str|String|명령어 이름|특수기호는 사용할 수 없음|
>|cp|sprexor.v1.CommandProvider|실행할 명령어 구현체|.|
>|hd|String|출력할 도움말|.|

---

## `public setComment`
>사용할 주석 문자를 설정합니다.
>
>*반환* : 없음
>
>|Arguments|Type|Detail|
>|--|--|--|
>|str|String|문자|

---

## `public useSyntax`
>지원하는 문법을 사용 여부를 설정합니다.
>
>*반환* : 없음
>
>|Arguments|Type|Detail|
>|--|--|--|
>|b|Boolean|사용 여부|

---

## `public exec`
>명령어를 실행합니다.
>
>*반환* : 없음
>
>|Arguments|Type|Detail|
>|--|--|--|
>|id|String|명령어 이름|
>|args|String Array|매개변수|
>또는
>|Arguments|Type|Detail|
>|--|--|--|
>|com|String|명령어|
>*예외* : CommandNotFoundException

---

## `private trimArr`
>배열에서 양 끝의 빈 문자열을 제거합니다.
>
>*반환* : String[] 또는 Boolean[]
>
>|Arguments|Type|Detail|
>|--|--|--|
>|in|String Array| .|
>또는
>|Arguments|Type|Detail|
>|--|--|--|
>|in|boolean Array| .|
>|num|int|새 배열의 크기|

---

