#DbgView无输出
在Win7下默认DbgPrint输出信息后，使用DbgView看不到内容。

新建一个reg文件，双击导出就行了。

```

Windows Registry Editor Version 5.00

[HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Session Manager\Debug Print Filter] 

"DEFAULT"=dword:0000000f

```
　　1.1、在注册表 "HKEY_LOCAL_MACHINE / SYSTEM / CurrentControlSet / Control / Session Manager /" 中新建key（项），名字为Debug Print Filter ，然后在此key下新建一个DWORD value（32位的就OK），名字为DEFAULT，然后设置值为0x00000008，重启电脑后会生效。


导入成功之后重启，DbgPrint就能输出信息了。



#驱动程序签名关闭
组策略-用户控制-系统-驱动-驱动程序签名-(启用)-忽略/警告

gpupdate

恢复签名验证，CMD中
```
bcdedit.exe -set loadoptions DDISABLE_INTEGRITY_CHECKS
bcdedit.exe -set loadoptions ENABLE_INTEGRITY_CHECKS
Bcdedit /set testsigning off
bcdedit -set TESTSIGNING ON
```
补充一下，我自己测试是下面两个命令同时执行以后才可以：
bcdedit -set loadoptions DDISABLE_INTEGRITY_CHECKS
bcdedit /set TESTSIGNING ON

```
bcdedit /set loadoptions DDISABLE_INTEGRITY_CHECKS
```
>into command prompt will do the job, but it doesn’t work on Windows 7 x64. It is believed this command was rendered useless with the release of Vista x64 SP1 and definitely doesn’t work on Windows 7 or 8. There was also a number of separate security patches on Vista which caused the command to not work. The Windows boot manager editor EasyBCD has an option which uses a command equivalent to DDISABLE_INTEGRITY_CHECKS, but as this is no longer useful, we would recommend you ignore this option in EasyBCD.
Read More: https://www.raymond.cc/blog/loading-unsigned-drivers-in-windows-7-and-vista-64-bit-x64

>https://www.raymond.cc/blog/loading-unsigned-drivers-in-windows-7-and-vista-64-bit-x64/

---
Disabling Driver Signature Enforcement In Windows 8 Permanently
Step 1.  Open the Windows command promt as “Run as Administrator”.
Step 2.  Run “bcdedit -set loadoptions DISABLE_INTEGRITY_CHECKS” (without the “”).
Step 3.  To finalize the process run “bcdedit -set TESTSIGNING ON” (without the “”).
Step 4.  Reboot and you’re done.
To disable it do step 1 and run these commands on step 2 and 3:
Step 2. “bcdedit -set loadoptions ENABLE_INTEGRITY_CHECKS” (without the “”)
Step 3. “bcdedit -set TESTSIGNING OFF” (without the “”)
Then do step 4 and you’re done.


--------------------- 
作者：Sagittarius_Warrior 
来源：CSDN 
原文：https://blog.csdn.net/sagittarius_warrior/article/details/51088742?utm_source=copy 
版权声明：本文为博主原创文章，转载请附上博文链接！


>https://social.technet.microsoft.com/Forums/en-US/71fa9761-c7e6-485d-85b4-37df4f9f4a75/permanently-boot-in-quotdisable-driver-signature-enforcementquot-mode?forum=win10itprogeneral

I share your problem. I wanted to install a capture card device to watch TV but couldn't due to this problem. I found your question as I was looking to turn on fast boot option with ready boot option. Turns out you can't (I haven't found such solution yet).

Here's what you can try.

First turn off Fast Boot option on your system from Control Panel. (right click Start, click Power, on the right click choose what the power buttons do >Change settings that are currently unavailable. In Shutdown settings  uncheck Turn on Fast startup.
Next disable the Modern UI Boot Loader here. (Click this link) (but this worked for me: bcdedit /set {current} bootmenupolicy legacy) Also you may have to replace boot.bin from this site (link)
Install ReadyDriver Plus 1.1 (Run in Administrator mode)
You are done! When you PC restarts, it will automatically select Disable Driver Signature Enforcement.


>http://www.askvg.com/how-to-disable-new-metro-boot-loader-and-bring-back-windows-vista-and-7-style-boot-loader-in-windows-8/
>http://uhlik.sk/?page=swreadydriver

>https://citadelindustries.net/readydriver-plus



###remember to add the test sign cert to your root trust storge if you enable test signing in vs.

---

First of all thanks raymond for showing me a bunch of methods i was unaware of.  
For those who will need to do this on a regular basis or are simply too lazy :D ive just made 6th option: a simple batch file which will “automate” the process somewhat.  
\* make a new text document by clicking your desktop > New > Text Document  
\* Press enter twice  
\* Now that the document is opened in notepad copy paste the text at the bottom of this post in it (you could remove the line called “pause” if you dont want a confirmation, then the program will do what you’ve asked and automatically close itself.  
\* Save your document and call it anything you like.  
\* Change the file extension from .txt to .bat (note: if you cant change or see the file extension this means that “Hide extension for known file types” is checked under Folder Options).  
\* Its ready to use! (Run with Admin privilege, not sure whetever it is necessary to turn UAC (User Account Control) off first. Correct me if im wrong here!

!!Note!!  
Dont forget changes are active after rebooting windows and more importantly!!! Like JSSmith said when you are done doing whatever it is you need change the setting back to default because theres a good reason for windows not allowing unsigned drivers!!! Better be safe then sorry right :)
```
@echo off  
echo 1=Allow the installation of unsigned drivers  
echo 2=Block the installation of unsigned drivers (windows 7 Default)  
echo X=Exit  
choice /C:12X

if errorlevel 3 goto End  
if errorlevel 2 goto Block  
if errorlevel 1 goto Allow

:Allow  
bcdedit /set nointegritychecks OFF  
Echo Installation of unsigned drivers is now ALLOWED!  
goto end

:Block  
bcdedit /set nointegritychecks ON  
Echo Installation of unsigned drivers is now BLOCKED!

:end  
pause  
cls
```


#VS  Could not find WindowsSDKDir variable from the registry

右击项目->[属性]->[链接器]->[输入]->[附加依赖项]->[宏] 
在检索栏中输入windowsSDKDir或者sdk查看变量是否有值
在
https://developer.microsoft.com/zh-cn/windows/downloads/sdk-archive
下载windows8.1的sdk，注意不是wdk是windows sdk。
```
Windows Registry Editor Version 5.00

[HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Microsoft SDKs\Windows\v8.1]

"CurrentInstallFolder"="C:\\Program Files (x86)\\Windows Kits\\8.1\\"
```
>https://social.msdn.microsoft.com/Forums/vstudio/en-US/48a4500c-2c48-43dd-822b-10e83258d18b/warning-msb8003-could-not-find-windowssdkdir-variable-from-the-registry-targetframeworkversion-or?forum=visualstudiogeneral
>https://stackoverflow.com/questions/22188919/windows-sdk-registry-variable-not-found




#编译错误
Solution右键属性->C/C++->General->Waring level改到3，treat all waring as error关闭
info2cat，solution下方package右键->属性->configration properties->info2cat->runinf2cat-关闭




