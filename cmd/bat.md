#Path Short cut(when space in path)
>https://blog.csdn.net/u013991521/article/details/52491075

D:\\IBM\\WCDE\_E~1  
一直以为这种路径是随机的，今天才发现原来这类路径也是有效的，它其实是有缩写规则的，美其名曰“DOS 8.3命名规则”，详情可见微软官方文档。

这种命名规则简单说来是用8个字符缩写来代替文件（或目录）全名，对于目录，可以写头六个字母（略去空白），另加波浪号和1；如果首字母不足六个字母，略去空格，用了第二个词的字母，凑成六个，再按规则继续处理。

例如：

```
Documents and Settings
可表示为
DOCUME~1
```

又如：

```
Local Settings
可表示为
LOCALS~1
```

如果多个文件前6字符一样，则按dir中的输出顺序累计下去。

假设下面是你的C盘根目录中的文件夹：

```
Program Files
Program Files (x86)

则两个目录分别表示为：
PROGRA~1
PROGRA~2
```

当然，用“dir /x”命令可以方便地帮助您查看系统对目录或文件名的缩写，如：

```
D:\>dir /x 
 Volume in drive D is Software
 Volume Serial Number is 0E98-DF31

 Directory of D:\

01/06/2012 09:18 PM <DIR>                  cygwin
01/17/2012 04:07 PM <DIR>                  develop
01/19/2012 04:13 PM <DIR>                  email
01/18/2012 03:06 PM <DIR>                  games
01/18/2012 01:24 PM <DIR>                  IBM
12/01/2006 11:37 PM       904,704          msdia80.dll
01/16/2012 02:06 PM <DIR>         PROGRA~1 Program Files
01/19/2012 08:15 AM <DIR>         PROGRA~2 Program Files (x86)
12/29/2011 09:56 PM <DIR>                  server
               1 File(s) 904,704 bytes
               8 Dir(s) 18,078,625,792 bytes free
```