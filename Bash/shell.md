#Linux Shell中的延时函数 sleep
在 linux shell 脚本中经常需要做一些延时处理。
所以经常要用到 sleep 或 usleep 函数。
下面来说一下 sleep 和 usleep 的区别：

sleep : 默认以秒为单位。
usleep : 默认以微秒为单位。
1s = 1000ms = 1000000us
sleep 不但可以用秒为单位，还可以指定延迟的单位，例如：
sleep 1s 表示延迟一秒
sleep 1m 表示延迟一分钟
sleep 1h 表示延迟一小时
sleep 1d 表示延迟一天

#需要输入到文件的情况
使用base64编码后输出
echo "9tbWFu=" | base64 -d >>/tmp/t.tmp


#IF判断
shell 编程中使用到得if语句内判断参数

　　–b 当file存在并且是块文件时返回真

　　-c 当file存在并且是字符文件时返回真

　　-d 当pathname存在并且是一个目录时返回真

　　-e 当pathname指定的文件或目录存在时返回真

　　-f 当file存在并且是正规文件时返回真

　　-g 当由pathname指定的文件或目录存在并且设置了SGID位时返回为真

　　-h 当file存在并且是符号链接文件时返回真，该选项在一些老系统上无效

　　-k 当由pathname指定的文件或目录存在并且设置了“粘滞”位时返回真

　　-p 当file存在并且是命令管道时返回为真

　　-r 当由pathname指定的文件或目录存在并且可读时返回为真

　　-s 当file存在文件大小大于0时返回真

　　-u 当由pathname指定的文件或目录存在并且设置了SUID位时返回真

　　-w 当由pathname指定的文件或目录存在并且可执行时返回真。一个目录为了它的内容被访问必然是可执行的。

　　-o 当由pathname指定的文件或目录存在并且被子当前进程的有效用户ID所指定的用户拥有时返回真。

　　UNIX Shell 里面比较字符写法：

　　-eq   等于

　　-ne    不等于

　　-gt    大于

　　-lt    小于

　　-le    小于等于

　　-ge   大于等于

　　-z    空串

　　=    两个字符相等

　　!=    两个字符不等

　　-n    非空串

　　-------------------------------------------------------------------------

　　更为详细的说明：

　　运算符                     描述                          示例

　　文件比较运算符

　　-e filename     如果 filename 存在，则为真            [ -e /var/log/syslog ]

　　-d filename     如果 filename 为目录，则为真          [ -d /tmp/mydir ]

　　-f filename     如果 filename 为常规文件，则为真      [ -f /usr/bin/grep ]

　　-L filename     如果 filename 为符号链接，则为真      [ -L /usr/bin/grep ]

　　-r filename     如果 filename 可读，则为真            [ -r /var/log/syslog ]

　　-w filename     如果 filename 可写，则为真            [ -w /var/mytmp.txt ]

　　-x filename     如果 filename 可执行，则为真          [ -L /usr/bin/grep ]

　　filename1 -nt filename2 如果 filename1 比 filename2 新，则为真 [ /tmp/install/etc/services -nt /etc/services ]

　　filename1 -ot filename2   如果 filename1 比 filename2 旧，则为真  [ /boot/bzImage -ot arch/i386/boot/bzImage ]

　　字符串比较运算符 （请注意引号的使用，这是防止空格扰乱代码的好方法）

-z string               如果 string 长度为零，则为真               [ -z $myvar ]

　　-n string                      如果 string 长度非零，则为真        [ -n $myvar ]

　　string1 = string2         如果 string1 与 string2 相同，则为真     [ $myvar = one two three ]

　　string1 != string2        如果 string1 与 string2 不同，则为真     [ $myvar != one two three ]

　　算术比较运算符

　　num1 -eq num2              等于         [ 3 -eq $mynum ]

　　num1 -ne num2              不等于       [ 3 -ne $mynum ]

　　num1 -lt num2               小于        [ 3 -lt $mynum ]

　　num1 -le num2            小于或等于     [ 3 -le $mynum ]

　　num1 -gt num2             大于          [ 3 -gt $mynum ]

　　num1 -ge num2             大于或等于    [ 3 -ge $mynum ]

　　脚本示例：

　　#!/bin/bash

　　# This script prints a message about your weight if you give it your

　　# weight in kilos and hight in centimeters.

　　if [ ! $# == 2 ]; then

　　echo "Usage: $0 weight_in_kilos length_in_centimeters"

　　exit

　　fi

　　weight="$1"

　　height="$2"

　　idealweight=$[$height - 110]

　　if [ $weight -le $idealweight ] ; then

　　echo "You should eat a bit more fat."

　　else

　　echo "You should eat a bit more fruit."

　　fi

　　# weight.sh 70 150

　　You should eat a bit more fruit.

　　# weight.sh 70 150 33

　　Usage: ./weight.sh weight_in_kilos length_in_centimeters

　　位置参数 $1， $2,..., $N，$#代表了命令行的参数数量， $0代表了脚本的名字，

　　第一个参数代表$1，第二个参数代表$2，以此类推，参数数量的总数存在$#中，上面的例子显示了怎么改变脚本，如果参数少于或者多余2个来打印出一条消息。

　　执行，并查看情况。

　　# bash -x tijian.sh 60 170

　　+ weight=60

　　+ height=170

　　+ idealweight=60

　　+ '[' 60 -le 60 ']'

　　+ echo 'You should eat a bit more fat.'

　　You should eat a bit more fat.

　　其中-x用来检查脚本的执行情况。



---

Linux_shell条件判断if中的-a到-z的意思
 

[ -a FILE ]  如果 FILE 存在则为真。  
[ -b FILE ]  如果 FILE 存在且是一个块特殊文件则为真。  
[ -c FILE ]  如果 FILE 存在且是一个字特殊文件则为真。  
[ -d FILE ]  如果 FILE 存在且是一个目录则为真。  
[ -e FILE ]  如果 FILE 存在则为真。  
[ -f FILE ]  如果 FILE 存在且是一个普通文件则为真。  
[ -g FILE ] 如果 FILE 存在且已经设置了SGID则为真。 [ -h FILE ]  如果 FILE 存在且是一个符号连接则为真。  
[ -k FILE ]  如果 FILE 存在且已经设置了粘制位则为真。  
[ -p FILE ]  如果 FILE 存在且是一个名字管道(F如果O)则为真。  
[ -r FILE ]  如果 FILE 存在且是可读的则为真。  
[ -s FILE ]  如果 FILE 存在且大小不为0则为真。  
[ -t FD ]  如果文件描述符 FD 打开且指向一个终端则为真。  
[ -u FILE ]  如果 FILE 存在且设置了SUID (set user ID)则为真。  
[ -w FILE ]  如果 FILE 如果 FILE 存在且是可写的则为真。  
[ -x FILE ]  如果 FILE 存在且是可执行的则为真。  
[ -O FILE ]  如果 FILE 存在且属有效用户ID则为真。  
[ -G FILE ]  如果 FILE 存在且属有效用户组则为真。  
[ -L FILE ]  如果 FILE 存在且是一个符号连接则为真。  
[ -N FILE ]  如果 FILE 存在 and has been mod如果ied since it was last read则为真。  
[ -S FILE ]  如果 FILE 存在且是一个套接字则为真。  
[ FILE1 -nt FILE2 ]  如果 FILE1 has been changed more recently than FILE2, or 如果 FILE1 exists and FILE2 does not则为真。  
[ FILE1 -ot FILE2 ]  如果 FILE1 比 FILE2 要老, 或者 FILE2 存在且 FILE1 不存在则为真。  
[ FILE1 -ef FILE2 ]  如果 FILE1 和 FILE2 指向相同的设备和节点号则为真。  
[ -o OPTIONNAME ]  如果 shell选项 “OPTIONNAME” 开启则为真。  
[ -z STRING ]  “STRING” 的长度为零则为真。  
[ -n STRING ] or [ STRING ]  “STRING” 的长度为非零 non-zero则为真。  
[ STRING1 == STRING2 ]  如果2个字符串相同。 “=” may be used instead of “==” for strict POSIX compliance则为真。  
[ STRING1 != STRING2 ]  如果字符串不相等则为真。 
[ STRING1 < STRING2 ]  如果 “STRING1” sorts before “STRING2” lexicographically in the current locale则为真。  
[ STRING1 > STRING2 ]  如果 “STRING1” sorts after “STRING2” lexicographically in the current locale则为真。  
[ ARG1 OP ARG2 ] “OP” is one of -eq, -ne, -lt, -le, -gt or -ge. These arithmetic binary operators return true if “ARG1” is equal to, not equal to, less than, less than or equal to, greater than, or greater than or equal to “ARG2”, respectively. “ARG1” and “ARG2” are integers.

 

=====================================================================

基本上和其他脚本语言一样。没有太大区别。不过值得注意的是。[]里面的条件判断。

1、字符串判断

str1 = str2　　　　　　当两个串有相同内容、长度时为真 
str1 != str2　　　　　 当串str1和str2不等时为真 
-n str1　　　　　　　 当串的长度大于0时为真(串非空) 
-z str1　　　　　　　 当串的长度为0时为真(空串) 
str1　　　　　　　　   当串str1为非空时为真

2、数字的判断

int1 -eq int2　　　　两数相等为真 
int1 -ne int2　　　　两数不等为真 
int1 -gt int2　　　　int1大于int2为真 
int1 -ge int2　　　　int1大于等于int2为真 
int1 -lt int2　　　　int1小于int2为真 
int1 -le int2　　　　int1小于等于int2为真

3、文件的判断

-r file　　　　　用户可读为真 
-w file　　　　　用户可写为真 
-x file　　　　　用户可执行为真 
-f file　　　　　文件为正规文件为真 
-d file　　　　　文件为目录为真 
-c file　　　　　文件为字符特殊文件为真 
-b file　　　　　文件为块特殊文件为真 
-s file　　　　　文件大小非0时为真 
-t file　　　　　当文件描述符(默认为1)指定的设备为终端时为真

4、复杂逻辑判断

-a 　 　　　　　 与 
-o　　　　　　　 或 
!　　　　　　　　非

结尾

语法虽然简单，但是在SHELL里使用的时候，他的功能变得强大了。

#带参数函数
$1 是第一个参数
myfunc () {
    echo "params length: $#" #“$#”会显示传给该函数的参数个数
    echo "all params   : $@" #“$@”会显示所有传给函数的参数
    echo "/$1: $1"
    echo "/$2: $2"
    echo "/$3: $3"
    echo "/$4: $4"
    echo "/$5: $5"
    echo "/$6: $6"
    echo "/$7: $7"
    echo "/$8: $8"
    echo "/$9: $9"
    echo "/$0: $0"
}
myfunc `grep china file.txt`

运行结果
```
-bash-3.00$ ./funcTest.sh
params length: 9
all params   : chen china engineer lee china leader lynn china programmer
$1: chen
$2: china
$3: engineer
$4: lee
$5: china
$6: leader
$7: lynn
$8: china
$9: programmer
$0: ./funcTest.sh
```

#取字符串长度
存在变量I="abc"
${#I}输出为3


#变色输出
```
color_print(){
if [ $1 = 1 ];then
#green
	echo -e "\033[32;49;1m $2 \033[39;49;0m"
elif [ $1 = 2 ];then
#red
	echo -e "\033[31m $2 \033[39;49;0m" 
fi
}
```


#快速过滤注释行  
$空行  
```
cat abc.txt | egrep -v "^$|^#"
```


#Seems could auto add missing
automake --add-missing 


#Static Compile
```
gcc -v -static -O3 -o prog prog.c
gcc -static
```
https://stackoverflow.com/questions/8692128/static-option-for-gcc
https://stackoverflow.com/questions/36823012/how-to-compile-gcc-with-static-library
https://stackoverflow.com/questions/20068947/how-to-static-link-linux-software-that-uses-configure
https://stackoverflow.com/questions/32224494/is-it-possible-to-compile-statically-with-gcc-or-g-on-linux-based-systems
https://stackoverflow.com/questions/809794/use-both-static-and-dynamically-linked-libraries-in-gcc
https://stackoverflow.com/questions/4156055/static-linking-only-some-libraries
```
gcc -lsome_dynamic_lib code.c some_static_lib.a
```

>TonyD suggested a method of checking:

```
ldd a.out 
not a dynamic executable

ldd b.out
linux-vdso.so.1 =>  (0x00007fff3d5ac000)
libstdc++.so.6 => /usr/lib/x86_64-linux-gnu/libstdc++.so.6 (0x00007fce5e34a000)
libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6 (0x00007fce5df85000)
libm.so.6 => /lib/x86_64-linux-gnu/libm.so.6 (0x00007fce5dc7e000)
/lib64/ld-linux-x86-64.so.2 (0x00007fce5e677000)
libgcc_s.so.1 => /lib/x86_64-linux-gnu/libgcc_s.so.1 (0x00007fce5da67000)
```
when it use ./configure
```
./configure LDFLAGS="-static"
```
https://stackoverflow.com/questions/20068947/how-to-static-link-linux-software-that-uses-configure
You could also use ld option -Bdynamic
```
gcc <objectfiles> -static -lstatic1 -lstatic2 -Wl,-Bdynamic -ldynamic1 -ldynamic2
All libraries after it (including system ones linked by gcc automatically) will be linked dynamically.
```
>-Wl,-Bdynamic requires GNU ld, so this solution doesn't work on systems where gcc uses the system ld (e.g. Mac OS X).
issue with glibc libc:
```
yum install glibc-static
```
https://stackoverflow.com/questions/26304531/compiling-with-static-libgcc-static-libstdc-still-results-in-dynamic-depende
https://www.systutorials.com/5217/how-to-statically-link-c-and-c-programs-on-linux-with-gcc/
#compile error
/usr/bin/ld: cannot find -lc
libc.a
https://stackoverflow.com/questions/45938875/linker-error-usr-bin-ld-cannot-find-lc
https://superuser.com/questions/846768/gcc-unrecognized-command-line-options-v-and-qversion-with-autoconf
gcc unrecognized command line options '-V' and '-qversion' with autoconf

run strace -f ./foob so we can see what the "No such file or directory" is about. 


#find
```
find  ./ -name "util*"
```

#GCC include 
```
gcc -I option flag
gcc -I adds include directory of header files.

Syntax
$ gcc -Idir [options] [source files] [object files] [-o output file]

Example
proj/src/myheader.h:

// myheader.h
#define NUM1 5

 

myfile.c:

// myfile.c
#include <stdio.h>
#include "myheader.h"
 
void main()
{
    int num = NUM1;
    printf("num=%d\n", num);
}
 

Build myfile.c without include directory proj/src :

$ gcc myfile.c -o myfile
myfile.c:2:22: fatal error: myheader.h: No such file or directory
compilation terminated.
$

 

Build myfile.c with include directory proj/src :

$ gcc -Iproj/src myfile.c -o myfile
$ ./myfile
num=5
$
```

gcc test.c -I/path/to/myinclude -L/path/to/mylib -lfoo

where
-I adds a directory to the list of dirs where #include <> files are found
-L adds a directory to the list of dirs where .a files are found
-l specifies the name of a library to search ("lib" and ".a" are assumed


#16to10 Hex to bin
```
[root@localhost procps]# echo $((16#FF))
255
[root@localhost procps]# echo $((16#FFAA))
65450
[root@localhost procps]# echo $((16#FFAAFF))
16755455
```
```
echo $((0x2f))
47

hexNum=2f
echo $((0x${hexNum}))
47
```
https://stackoverflow.com/questions/13280131/hexadecimal-to-decimal-in-shell-script
#Ascii2Bin ASCII to Binary
```
$ echo -n "A" | xxd -b
0000000: 01000001                                               A

$ echo -n "A" | xxd -b | awk '{print $2}'
01000001
$ echo "obase=2; ibase=16; A" | bc
1010

$ echo "obase=16; ibase=2; 1010" | bc
A
```
```
bin2ascii() { { tr -cd 01 | fold -w8; echo; } | sed '1i obase=8; ibase=2' | bc | sed 's/^/\\/' | tr -d '\n' | xargs -0 echo -e; }
```


#找不到某个函数的依赖库
```
 yum whatprovides
```
```
ex:
 yum whatprovides “*/perl(Term::ReadKey)”
 yum whatprovides “*/perl(Locale::gettext)”
 yum whatprovides “*/perl(RPC::XML)”
```

>https://gitlab.com/apparmor/apparmor/wikis/Distro_CentOS


#iptables限制其他用户进程访问网络
```
$ iptables -A OUTPUT -p tcp --dport 992 -d localhost -m owner ! --uid-owner root -j REJECT
```

This rule tells the kernel to reject packets sent to the local TCP port 992 unless they're sent by one of root's processes.

>https://unix.stackexchange.com/questions/67351/is-it-possible-to-whitelist-a-specific-program-in-iptables

>https://unix.stackexchange.com/questions/304021/how-can-i-implement-a-whitelist-on-a-specific-port-using-iptables

#linux kernel enable apparmor

If AppArmor should be selected as the default security module then set:
```
CONFIG_DEFAULT_SECURITY="apparmor"
CONFIG_SECURITY_APPARMOR_BOOTPARAM_VALUE=1
```


Build the kernel

If AppArmor is not the default security module it can be enabled by passing security=apparmor on the kernel’s command line.

If AppArmor is the default security module it can be disabled by passing apparmor=0, security=XXXX (where XXXX is valid security module), on the kernel’s command line.

For AppArmor to enforce any restrictions beyond standard Linux DAC permissions policy must be loaded into the kernel from user space (see the Documentation and tools links).

>https://www.kernel.org/doc/html/v4.15/admin-guide/LSM/apparmor.htm


#Linux关闭ASLR


关闭内核地址随机化（KALSR）：
>内核是通过grup启动的，所以在grup配置文件中，内核启动参数里加上nokaslr 

$ cat /etc/default/grub |grep -v "#" | grep CMDLI
GRUB_CMDLINE_LINUX_DEFAULT="nokaslr"
GRUB_CMDLINE_LINUX=""
$ sudo update-grub


#带符号的内核：

$ cat /etc/apt/sources.list.d/ddebs.list
deb http://ddebs.ubuntu.com/ bionic main
deb http://ddebs.ubuntu.com/ bionic-updates main
$ sudo apt install linux-image-4.15.0-20-generic-dbgsym
$ ls /usr/lib/debug/boot/vmlinux-4.15.0-20-generic


#Iptables

Set your default policy. Packets matching none of the below given rules will be dropped by default:
```
iptables -P INPUT DROP
```
Create a custom chain. We are going to pass packets fulfilling given conditions (for example having source address 10.0.0.1) to this chain:
```
iptables -N CUSTOM
```
Accept connections that are already up and accept connections on the loopback interface:
```
iptables -A INPUT -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
iptables -A INPUT -i lo -j ACCEPT
```
Pass new TCP connections from 10.0.0.1 (arriving on interface eth0) to the CUSTOM chain:

``
iptables -s 10.0.0.1 -i eth0 -p tcp -m conntrack --ctstate NEW -j CUSTOM
Append rules to the custom chain that which accept specific ports:
``
``
iptables -A CUSTOM -p tcp -m multiport --dports 22,80 -j ACCEPT``

dd a new chain for your policy:
``
iptables -N MYCHAIN
``
Define a port list in the INPUT chain for the destination port you want to process:
``
iptables -A INPUT -p <protocol, either tcp or udp> -m <again, protocol, match the '-p' switch value> --dport <portnum> -j MYCHAIN``

From your description, this is a fairly simple problem in iptables. I would approach it in three steps.

Add a new chain for your policy:
``
iptables -N MYCHAIN``
Define a port list in the INPUT chain for the destination port you want to process:
``
iptables -A INPUT -p <protocol, either tcp or udp> -m <again, protocol, match the '-p' switch value> --dport <portnum> -j MYCHAIN``
What this does is to define the specific ports and then "jumps" to the rules for the named chain (MYCHAIN, in this case).

Now, set up rules in MYCHAIN to allow/deny traffic to the described ports.  
```  
iptables -A MYCHAIN -s <source_IP> -j ACCEPT  
iptables -A MYCHAIN -s <other_source_IP> -j ACCEPT  
iptables -A MYCHAIN -J DROP  
```
In the last rule, you could, of course, use REJECT, instead of DROP. The user manual ( man iptables ) should show you additional parameters you can use to log or limit the traffic you're seeing. There are also several online tutorials available for more advanced rules.



#ubuntu New apt
different with apt-get
![https://s2.ax1x.com/2019/08/01/eUW24I.png](data:image/png;base64,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)
![https://s2.ax1x.com/2019/08/01/eUWVBQ.png](data:image/png;base64,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88472YkTJ/Lr1q1bt9gdsHXI95s3b2YR6yn4isT0UP7VuLl48eISCja+RPD4Ykp2jI4cObKkbGiX0feo371Wv/ftXsrOt8T7HXfckce52183xlWcaV/l3zaNQB9By47oiy++mKcebNu2bbFjGrJV6nJ34V977bUFEy3v8vCNLetn9xr1heRh2h136ZMIFN3ps35z+99TOHubqSOmfbuOLseiue7jjz9exJ+yfP/99/Pdbjv/uR12fezGiMagfGbFvfTppZdeWjLvFvUzJha1T6E8XHujEYo/N458dYViy9Zvudi4EnvPnz+fbd++PTtw4MBiHLrj2NZhx6AdA775wq1DbJMxLuM0tNOtXJ599tnslVdeWaQT+cZgmY3SXzu+fOuomzOtT1hC84jU6RujvvnTXlvGq2hOGXIuoG0IlBHoXUz7HqPbSdAd/CoedGH46KOPbllmDf6HAAAgAElEQVSEt27dymPVMk+3/H1IaAzlX7vA+oSET+Ba8SMLiYrdUJqHL/3ElosR01J/6DoVL67o1IXmySefXMS78r799tuzb3zjG4sb0hhb3YVLx5kupNK+CgNl644zX1+sSPYJS1uXFUtFPFoO4dLq6ohpn6Bzd6ptnLhznfJ2/aKiJcTFMrd+8cWzry7bb1vO+rIoj7iKmJaX32RTRePP9t/XV41RVxTauLJPe3x9UvHn1hO6yfC9MBjypTtOQv75whe+UCimhYv86SaT2rd69erFjVGMjWr3Aw88sLipdd/RsFxC41r6ojfFIZbuzZn6yL35L+Plm2f6fmemdFLgAggECPQupn39sALMN6h0J8198zh0Hd7unkCVNI8+/OsTDWWCQCjpYv3www/nL47FiGkfXSuwm4rpUD98osb3Nr5PyFpb6+76if3yAp7ddbfj0X3CpCcFuLEgu7S+eoqEWveR/X8tVBXTKs7cGxPfjZkrEHxzWGhHOfbGz71u2bJlOWP3vRLrl1Csuf2skopUVUzbkySssNcdZN1dl39/8skn2b/9278tXhT23cTbWLE2xNzsSR2x18m1wvLVV1/NBa8+VbBPk8pypkP8YuYm18aiJxPKxm3r7rvv9o5H35jztePOB3b+C63VLi+5qer7fZU+5xPamjeBwcS07zG5TjqhgRdarNmZ7j9Iy8R03/4NiWl9cUhFhRWAugOsJ8LELFgubftos2wn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)



#vi

- show linenumber
```:set number```
- to End of file
```GG```
- to Start of file
```gg```
- search
```/thekeyword```
  - Next search result  ``n`` 
- keyword highlight
  - move cursor to keyword,shift+#(maybe only works on vim)

#get system version
- centos
```cat /etc/centos-release```
- ubuntu
``cat /etc/issue``
``lsb_release -a``
``cat /proc/version``


#ubuntu check software location
ubuntu下如何查看软件安装目录以及安装版本

apt list | grep 软件名 查看软件准确名称

1.查询版本

1)aptitude show 软件名
>可以查看软件的依赖包

例如：aptitude show kde-runtime 



2)dpkg -l软件名

 

 例如：dpkg -l gedit 



2.查询安装路径

1)dpkg -L 软件名

 

例如：dpkg -L gedit  



2)whereis 软件名

 

例如：whereis gedit



例如:

$whereis vim

 

vim: /usr/bin/vim.basic /usr/bin/vim /usr/bin/vim.tiny /etc/vim /usr/share/vim /usr/share/man/man1/vim.1.gz


dpkg -P来卸载软件,或者 dpkg --purge完全删除,包括配置文件

#Centos Check software installed

1. rpm包安装的，可以用rpm -qa看到，如果要查找某软件包是否安装，用 rpm -qa | grep "软件或者包的名字"。

2. yum方法安装的，可以用yum list installed查找，如果是查找指定包，命令后加 | grep "软件名或者包名"； 
        例如：yum list installed openssl |grep openssl
                openssl.x86_64                     1:1.0.2k-8.el7                      @anaconda

        如果没有返回值则表示没有安装
3. 如果是以源码包自己编译安装的，例如.tar.gz或者tar.bz2形式的，这个只能看可执行文件是否存在了， 
   上面两种方法都看不到这种源码形式安装的包。如果是以root用户安装的，可执行程序通常都在/sbin:/usr/bin目录下



#check alias
``type cat``

```
jjj@jjj-virtual-machine:~/Downloads/zlib-1.2.11$ type ls
ls is aliased to `ls --color=auto'
```


#reverse_proxy

``mkfifo backpipe; nc -l 12345 0<backpipe | nc www.google.com 80 1>backpipe``
``socat STDIO TCP:www.google.com:80``