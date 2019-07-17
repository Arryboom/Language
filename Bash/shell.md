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