[![Build Status](https://travis-ci.org/tomasbublik/scripthon-tool.svg?branch=master)](https://travis-ci.org/tomasbublik/scripthon-tool)
[![codecov](https://codecov.io/gh/tomasbublik/scripthon-tool/branch/master/graph/badge.svg)](https://codecov.io/gh/tomasbublik/scripthon-tool)
[![codecov](https://codecov.io/gh/tomasbublik/scripthon-tool/branch/master/graph/sunburst.svg)](https://codecov.io/gh/tomasbublik/scripthon-tool)

# scripthon-tool
Scripthon is a DSL to describe the Java source pattern (code snippet) which might be recognised by the searching algorithm traversing the Java AST. 


To execute, run: 

```bash
./gradlew clean fatJar && java -jar scripthon-tool/build/libs/scripthon-tool-all-0.1.2.jar
```

To execute tests:

```bash
./gradlew test
```

**First motivation example:** 

Consider the Java source as follows:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
```

This sample could be found by teh following Scripthon code:

```java
Meth()
```

Unfortunately, this will find a lot of miscellaneous results. With
Scripthon, we can search only methods named with a certain name:

```java
Meth(Name="main")
```

Moreover, we want just the public and static ones:

```java
Meth(Name="main"; Rest=[public, static])
```
 
Even more, the most specific searching criteria for the main method are:

```java
Meth(Name="main"; Rest=[public, static]; Ret=void; ParNum=1;ParTypes=[String[]]; ParNames=["args"])
```

With Scripthon, we can define a method call inside the main method:

```java
Meth(Name="main"; Rest=[public, static])
    MethCall(Name="System.out.println";Params=["args"])
```

Scripthon supports also a block of the code description. We can define the block properties in the following way:

```java
Class(Name="HelloWorld")
    Meth(Name="main")
        Block(StmtNum=1;Order=false)
```
        
Again, this example corresponds to the **Hello, World!** example. There is one statement
inside the main method, and the order of statements does not matter.

Another interesting Scripthon's keyword is the word **Any**. It is useful for an indefinite searching, i.e. the searched statements can include anything or be empty. To describe
the code above, we can write:

```java
Meth(Name="main")
    Any()
```
    
But the desired code can look like this:

```java
public static void main (String[] args) {
    int i = 1;
    i++;
    System.out.println(args);
}
```

And we can find this by the following script:

```java
Meth(Name="main")
    Any()
    MethCall(Name="System.out.println")
```

There are several more keywords supported by Scripthon. For example, Init() for a variable initialization, Loop() for a common loop, etc. Finally, with the presented examples, it is easy to find a singleton in the code:

```java
Class() class
    Block(Order=false;Consecutive=false)
        Meth(Name=class.Name;Rest=private)
            MethCall(Ret=class.Name;Rest=[public, static])
```

The only unmentioned parameter here is the "Consecutive" parameter. It means that the statements inside the block must not be consecutive, they just need to be contained somewhere in the given block.