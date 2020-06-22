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




#Windows下创建硬链接和软链接(符号链接)

首先简单理解一下硬链接和符号链接(软链接)的区别(此文中的符号链接和软链接指同一概念)：

硬连接指向的是节点(inode)，而软连接指向的是路径(path) 。

最初的文件名与所有的硬链接地位是对等的，比如为文件 a 建立了硬链接 b、c、d。那么a、b、c、d之中只要有一个文件未删除，这个文件就可通未删除的名称访问的。你也可以认为每个文件都可认为至少有一个硬链接，就是说a也是一个硬链接。

软链接特性上有些类似于快捷方式，比如为原文件 a 建立了软链接 b、c、d。删除b、c 或 d 访问到 a，但是只要删除了 a，软链接就不可用了。但是 windows 下的快捷方式只能在资源管理器中有用，它只是一个 lnk 文件，如果是一个目录的快捷方式，它是不能通过 cd 命令或路径进入。


### 一： Windows 下创建硬链接，只能适用于 NTFS 文件系统。使用命令 fsutil hardlink

语法

`fsutil hardlink create NewFileName ExistingFileName`

参数

`create 建立现有文件和新文件之间的 NTFS 硬链接。NTFS 硬链接与 POSIX 硬链接相似。  
NewFileName 指定要将创建硬链接的文件。  
ExistingFileName 指定要从中创建硬链接的文件。`

当然，如果你想在自己的程序里创建硬链接，那也是很容易的，只需要一个很简单的 API 函数:

`BOOL CreateHardLink(  
    LPCTSTR lpFileName,  
    LPCTSTR lpExistingFileName,  
    LPSECURITY_ATTRIBUTES lpSecurityAttributes  
);`

适用于 Win2000 及以上版本的系统，前两个参数的意思就不用解释了，最后一个参数的用途暂时保留，必须为 NULL。

### 二：Windows 下创建软链接

NTFS只支持对目录的软链接，微软把它称作 junction。但是对于文件的软链接，微软也有提供解决方案，那就是快捷方式(Shortcut，.lnk 文件)。不过软链接和快捷方式不是一个层次上的东西，前者是底层文件系统的功能，后者是应用层的功能。Windows 下目录的快捷方式用 dir 看起来是个文件。

在 [http://www.microsoft.com/technet/sysinternals/FileAndDisk/Junction.mspx](http://www.microsoft.com/technet/sysinternals/FileAndDisk/Junction.mspx "http://www.microsoft.com/technet/sysinternals/FileAndDisk/Junction.mspx") 下载 junction.exe。junction 的命令语是：

`junction  LinkDirectory ExistingDirectory`

例如：

`junction d:\link c:\winnt`

将为c:\\winnt 建立一个链接目录 d:\\link，C和D分区都要是 NTFS 格式，在资源管理器和 dir 列示中 d:\\link 都以目录的面目存在的。d:\\link 就像是 c:\\winnt 的一个引用一般，删除 d:\\link 目录中的内容也就是删除了 c:\\winnt 中的内容，但删除 d:\\link 本身是不会影响到 c:\\winnt 的。

---

新链接和现有文件必须在同一个卷上。
junction命令还可以把空文件夹删除掉：
D:\>md TestJunctionDir
D:\>junction TestJunctionDir /d
Deleted TestJunctionDir.
如果用于非空文件夹就会出错：
C:\Temp\test>md TestJunctionDir
C:\Temp\test>dir > TestJunctionDir\test.txt
C:\Temp\test>junction TestJunctionDir /d
Junction v1.05 - Windows junction creator and reparse point viewer
Copyright (C) 2000-2007 Mark Russinovich
Systems Internals
Error deleting TestJunctionDir: ???????
【注意事项】
使用junction做文件夹连接需要注意的是：不能直接在资源管理器里面删除文件夹的连接，要用下面的命令来删除，否则源文件夹里面的内容也会被删除，切记切记。
junction 文件夹连接名 /d 在Vista和Win7下面，命令简化了，是mklink，功能比XP中的fsutil强了很多。


相应的，在程序中也有一个 API 函数 CreateSymbolicLink 支持创建软链接，不过来得太晚了，要 Windows VISTA 和 Windows Server 2008 那样的版本才支持，先还是别想了，API 原型是：

`BOOL WINAPI CreateSymbolicLink(  
  __in  LPCWSTR lpSymlinkFileName,  
  __in  LPCWSTR lpTargetFileName,  
  __in  DWORD dwFlags  
);`

参数：

`lpSymlinkFileName 要创建的符号链接名称.  
lpTargetFileName 符号链接所对应目标的名称.  
dwFlags 标识目标是文件还是目录. 取值0x0 代表是文件，SYMBOLIC_LINK_FLAG_DIRECTORY或0x1 代表是目录`

### 三：其他方法

也可以使用 GNU utilities for Win32 中的 ln 来创建硬链接。这是一些 GNU 工具的 Win32 移植版本，非常好用。另外 Cygwin 里的 ln 不但可以创建硬链接也可以创建符号链接(在 Windows 里就是快捷方式 .lnk 文件)。

实际需求引出：Web 应用中上传文到 WEB 下的某个子目录中，这样可以直接通过网页链接的方式访问到这些文件。但是会出现的问题就是，每当完全重新部署应用时，如果忘了把存上传文件的目录进行备份，那么原有上传文件就全没了。原来项目部署在 Unix 下的做法是，把那个上传目录作为另一个目录的符号链接，实际存储文件的目录不在 WEB 应用目录下，重新部署时只要重建这个符号链接即可，不会有覆盖文件的危险。当然在 Unix/Linux 是好解决，只要用 ln -s 命令就行，然而对于 Windows 系统却要想点办法，为目录建立快捷的方式是行不通的，目录的链接只会当 lnk 文件对待，在 Explorer 中可以双击打开，但对于网页链接或者 cd 命令是无法正确定位的。于是思考起如何在 Windows 下创建符号连接的问题，才有了上文。

题外：对于以上的需求，可以在 Web 应用外部事先建立好一个目录，赋上相应的权限。然后在应用的配置文件中记下这个目录的绝对路径，上传时往其中写文件没问题，关键浏览时，因为文件在应用之后，不能直接通过网址浏览到，就需要通过一个程序去读取相应的文件，发送到浏览器之前必须设置根据文件类型设置响应 MINE 类型，这个 MINE 类型可以在上传时记载在库的。

现在觉得这种方法还优于用符号链接的方式，至为无需每次完整发布后重创建符号链接，而且实际中也出现过完全重部署后，目标目录中文件完全丢失的情况。



###>=win7

MKLINK [[/D] | [/H] | [/J]] Link Target
/D 创建目录符号链接。默认为文件符号链接。
/H 创建硬链接，而不是符号链接。
/J 创建目录联接。
Link 指定新的符号链接名称。
Target 指定新链接引用的路径

mklink/H Link.txt Target.txt

---
下面的图片向我们展示了在windows系统中创建符号链接，硬链接和快捷方式有什么不同。

符号链接（Symbolic link）

- 执行命令 mklink link\_name target\_name
- 创建链接后的图标和快捷方式很像, 都有一个箭头的标志
- 在系统中不占用空间
- 在文件系统中不是一个单独的文件
- 在操作系统层解析（！？）
- 如果源文件被删除了，链接就没用了
- 移除源文件不会影响符号链接
- 移除链接文件也不会影响源文件
- win10\_x64\_build10565上测试不可以右键修改图标和设置管理员运行
- 文件大小为0字节和不占用空间
- 文件属性的创建时间和修改时间都是软链接创建和修改时的时间
- 文件类型是.SYMLINK
- 可以在cmd下运行软链接(假如链接的是程序, 且运行命令是XXX即可)(win10\_x64\_build10565上测试通过)

硬链接（Hard link）

- 执行命令 mklink /H link\_name target\_name
- 在系统中占用的空间与源文件相同，但在系统中引用的是相同的对象（不是拷贝）
- 在操作系统层解析（！？）
- 图标和创建快捷方式的图标不同(没有快捷方式的小箭头)
- 移除源文件不会影响硬链接
- 移除硬链接不会影响源文件
- 如果源文件被删除，它的内容依然通过硬链接存在
- 硬链接文件的任何更改都会影响到源文件
- 文件大小, 占用空间, 创建和修改时间跟原原文件一样
- 可以在cmd下运行硬连接(假如链接的是程序)

快捷方式（Shortcut）

- 在选择的源文件上鼠标右键，通过下拉菜单创建
- 快捷方式在系统中跟源文件是完全分离的
- 只有那些懂得快捷方式的程序知道它们
- 如果源文件删除，链接就没用了
- 移除源文件不会移除快捷方式
- 移除快捷方式不会影响到源文件
- 可以右键更改图标或者设置管理员运行
- 文件属性的创建时间和修改时间都是快捷方式创建和修改时的时间
- 文件大小仅有几百字节, 跟原文件大小无关
- 文件类型是.lnk
- 可以在cmd下运行快捷方式(假如链接的是程序, 且运行命令是XXX.LNK)(win10\_x64\_build10565上测试通过)

---


和符号链接一样，硬链接中所做的任何修改，都会自动应用到目标文件上。但是硬链接具有以下一些不同的地方。

(1)硬链接必须引用同一个分区或者卷中的文件，而符号链接可以指向不同分区或者共享文件夹上的文件或者文件夹。

(2) 硬链接只能引用文件，而符号链接可以引用文件或者文件夹。

(3)Windows会自动维护硬链接，即使把硬链接复制到其他文件夹，硬链接和目标都可以继续访问。

(4)删除目标文件，硬链接可以继续保留。只有把目标文件和所有的硬链接都删除，才能把该文件彻底删除。

(5)如果win7把符号链接的目标文件删除，然后用一个同名文件替换，则符号链接会指向新的目标文件；而把硬链接的目标文件删除’再用同名文件替换，则硬链接还是会继续引用原始文件。

(6)也就是说，硬链接和目标文件的地位相等。事实上，原始的目标文件本身也相当于硬链接，新建硬链接，只是相当于增加一个目录路後而已。

(7)硬链接看上去和真的文件一模一样(实际上就是真实的文件)，不像符号链接那样有一个快捷方式的小箭头，但是硬链接并不会增加磁盘空间的占用。

(8)对硬链接进行NTFS权限的修改，会同时影响到目标文件(因为两者等价)，而符号链接和目标文件可以设置不同的NTFS权限。


目录符号链接和目录联接（看原文即目录的硬链接）的区别在于：

目录联接在创建时会自动引用目标目录的绝对路径，而符号链接允许相对路径的引用。

如分别用 mklink /D dira tdir 和 mklink /J dirb tdir 创建 dira、dirb 对相对目录的 tdir 的符号链接和目录联接，之后将 dira、dirb 移动到其它目录下，则访问 dira 时会提示“位置不可用”，访问 dirb 时仍然正常指向 tdir；

且win10_x64_build10565的cmd下dir命令查看会发现, dira符号链接(软链接)链接到的是相对路径下的tdir文件(不管是否存在tdir文件), 且文件类型是symlink, dirb目录联接则链接到绝对(全)路径下的tdir文件, 且文件类型是junction(可能是系统自动把相对路径转换为全路径)



而分别用 mklink /D dira c:\demo\tdir 和 mklink /J dirb c:\demo\tdir 创建 c:\demo\tdir 的符号链接和目录联接，再将这两个目录链接移动到其它目录下，则 dira 和 dirb 均可正常指向 c:\demo\tdir；

由此可见当创建目录链接时对目标目录使用绝对路径，D 和 J 两个参数实现的目录链接效果是一样的；
