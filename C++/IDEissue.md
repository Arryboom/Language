#VS2013关键字没有高亮、没有提示、关键字及函数无法跳转（非设置问题）
现象及原因： 
有些时候我们使用VS2013编写工程时，刚开始没有以上问题，但是当我们的工程逐渐变大时，突然有一个文件出现以上问题，这并不是设置提示的问题，因为当你打开别的工程时该问题不会出现。这其实是配置缓存的问题，而VS2013控制功能的应用是“ intellisence”，只要删除它的缓存即可。

解决方法： 
1、关闭该项目所有打开的文件； 
2、关闭VS； 
3、去C:\Users\< your users name>\AppData\Local\Microsoft\VisualStudio\12.0\ComponentModelCache文件夹下删除所有文件及文件夹； 
4、重新打开VS即可。
--------------------- 
作者：FadeFarAway 
来源：CSDN 
原文：https://blog.csdn.net/fadefaraway/article/details/54983952?utm_source=copy 
版权声明：本文为博主原创文章，转载请附上博文链接！