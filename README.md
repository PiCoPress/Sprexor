<!--Stable mark : <b><span style="color:78a094">Stable</span></b>-->
<!--Latest mark : <b><span style="color:f478cd">Latest</span></b>-->

<img src='https://raw.githubusercontent.com/PiCoPress/Sprexor/master/docs/img/Sprexor_mono.png' width='400'>

# Sprexor Application Framework
Sprexor is Framework that manages and runs command line in Java.
It has a lot of libraries, functions, and customizing tools to use in many of purpose easily.

# Downloads
- <b><span style="color:f478cd">Latest</span></b> **0.2.19** `2021.4.13`
    + [Install](https://github.com/PiCoPress/Sprexor/releases/download/0.2.19/Sprexor.0.2.19-full.jar)
    + [Download binary only](https://github.com/PiCoPress/Sprexor/releases/download/0.2.19/Sprexor.0.2.19-bin.jar)
    + [source](https://github.com/PiCoPress/Sprexor/archive/refs/tags/0.2.19.zip)


- **0.2.16** `2020.11.16`
    + [Install](https://github.com/PiCoPress/Sprexor/releases/download/0.2.16/Sprexor.0.2.16-full.jar)
    + [Download binary only](https://github.com/PiCoPress/Sprexor/releases/download/0.2.16/Sprexor.0.2.16.binary.jar)
    + [source](https://github.com/PiCoPress/Sprexor/archive/0.2.16.zip)
    
    
- **0.2.14** `2020.11.13`
    + [source](https://github.com/PiCoPress/Sprexor/archive/0.2.14a.zip)


- [Download ohter versions](./docs/other.md)


# See Documentation
- [English](./docs/docs_en.md)
- [Korean](./docs/docs_ko.md)


## How to use 
`Requirement : JDK-14.0.2`
<a href="https://www.oracle.com/kr/java/technologies/javase/jdk14-archive-downloads.html" target="_blank">Download JDK14</a>

There are two ways how to use Sprexor.

```java
import sprex.*;
import sprexor.cosmos.*;

public class Test {
	//In main
	public static void main(String[] args) {
		
		//new constructor
		Sprexor sprexorObject = new Sprexor();
		
		
		//Define Sprexor's input and output. Default IOCenter uses System.in and System.out
		sprexorObject.impose = new Sprexor.Impose() {
			
			@Override
			public IOCenter InOut() {
				//Returned value should be `new IOCenter(new SprexorOstream(lambda), new SprexorIstream())`
			}
		};
		
		
		//reflection's methods are called when parsing.
		sprexorObject.reflect = new Sprexor.Reflection(){
			
		}
		
		//Label can assign any Object.
		sprexorObject.label = "MyFirstSprexorApplication";
		
		
		//If it's called, then Sprexor's reflection is usable.
		sprexorObject.allowReflection();
		
		
		//It adds Command that Implemented with CommandFactory class.
		sprexorObject.importSprex(class CommandFactory)
		
		
		//Set comment character like this : "//"
		sprexorObject.setComment('#');
		
		
		//It registers command Immediately.
		sprexorObject.register("ThisIsExample", new CommandProvider(){ 
			
			@Override
			//This is main Method to run.
			public int code(IOCenter io) {
				
				//io.getComponent() gets arguments.
				Component args = io.getComponent();
				
				io.out.println(args.toString());
				return 0;
			}
		},
		 "ThisIsExample : To sees How to use it.");
		
		
		//If it is called, Methods below are usable, but above Methods are blocked to use.
		sprexorObject.activate();
		
		
		//It executes command line. 
		//Expected : "Hello World!"
		sprexorObject.exec("ThisIsExample Hello World!");
		
		
		//It executes command line like above exec method, but not stable.
		sprexorObject.run("ThisIsExample Hello World!");
	}
}
```

---
Create with class;

```java
import sprexor.*;

public class ExampleSprexorApplication implements CommandFactory {

	@Override
	public String getCommandName() {
		return "MyApplication";
	}
	
	@Override
	public String help() {
		return "To sees how to make Application.";
	}
	
	@Override
	public CommandFactory[] referenceClass() {
		
		//If no other application or This Application is not main, returned value can null.
		return null;
	}
	
	@Override
	//Main Method
	public int code(IOCenter io, Sprexor SprexorInstance) {
		io.out.println("Hello World!");
		return 0;
	}
}
```
