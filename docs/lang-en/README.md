Sprexor - 1.0.0
===============

**Index**
1. [V0](#v0)
2. [V1](#v1)
3. [V2](#v2)


- [Space](./Space.md)


## V0
>V0 is a legacy version, and it is simple to make command by CommandProvider that has some event functions such as 'emptyArgs' and 'ErrorEventListener'. so, It can deal easily many situations. 
>- [Nere](./V0/Nere.md)
>- [IOCenter](./V0/NIOCenter.md)
>- [GlobalData](./V0/GlobalData.md)
>- [Lib](./V0/Lib.md)
>- [CommandProvider](./V0/CommandProvider.md)
>- [Example Code](./V0/ex.md)

| Name | Commands |
|------|----------|
| help | get command's help |
| echo | print a message |
|  var | define a variable |


## V1
>V1 is to improve many problems of V0. Much better performance than V0, and more simple usage than V0. It has sprexor-like IOStream - SprexorIstream, SprexorOstream : some features about input and output, and binding them is IOCenter. There is CommandProvider like V0, too. In other hand, CommandProvider is more simple. CommandFactory is resemble CommandProvider, but purpose and features are different.


## V2
>V2 is to fix previous versions' problems that could not extensible, no comaptible about IOStream, and lack of standard commands. 
