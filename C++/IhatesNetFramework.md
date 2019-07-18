#C#如何编译为本地软件，摆脱.NET框架？
就是不用下载.NET框架运行c#程序，我需要编译器的下载地址。这个叫本地编译器，静态编译器。
找不到 C# .net native code中文版编译器。

- 现阶段不可能。
.NET 下你用的所有 CLR 方法也好类也好都是封装在一个个 .NET Framework 的 DLL 里的，程序运行时必须这些文件，除非你一个 CLR 类和方法都没用过（那不就是纯 C 语言而且是只有标准库的 C 了么）。
有些类似于 Remote Soft's Salamander .NET Native Compiler、Infralution Globalizer 这些工具，也不是把代码编译成机器语言了，而是把需要的类库等直接写入了 PE，会导致程序体积大大增加，而且只支持 .NET 2.0，最重要的是还是收费软件。
微软自己倒是新推出了个 .NET Native，不过可惜的是目前只支持 .NET 4.5 下的 Windows 8 App 程序。



- .NET Core单文件发布静态编译AOT CoreRT，将.NET Core应用打包成一个可执行文件并包含运行时。

>支持Windows, MacOS and Linux x64 w/ RyuJIT codegen。

>示例项目：

>https://github.com/dotnet/corert/tree/master/samples/WebApi






>https://www.cnblogs.com/linezero/p/CoreRT.htm