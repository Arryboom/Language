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

>https://www.cnblogs.com/qianjinyan/p/9013810.html

**example**
```
declare -l params
params=$1
if [[ -z $params || "$params" = "-h" || "$params" = "-help" || "$params" = "--help" ]];then
echo -e "This will hide the proc by PID you provided.\nusage:$0 <PID>\nexample:$0 1000"
exit
fi
```

>I researched, and this is quite hairy:

>-a is deprecated, thus isn't listed in the manpage for /usr/bin/test anymore, but still in the one for bash. Use -e . For single '[', the bash builtin behaves the same as the test bash builtin, which behaves the same as /usr/bin/[ and /usr/bin/test (the one is a symlink to the other). Note the effect of -a depends on its position: If it's at the start, it means file exists. If it's in the middle of two expressions, it means logical and.

>[ ! -a /path ] && echo exists doesn't work, as the bash manual points out that -a is considered a binary operator there, and so the above isn't parsed as a negate -a .. but as a if '!' and '/path' is true (non-empty). Thus, your script always outputs "-a" (which actually tests for files), and "! -a" which actually is a binary and here.

>For [[, -a isn't used as a binary and anymore (&& is used there), so its unique purpose is to check for a file there (although being deprecated). So, negation actually does what you expect.
>https://stackoverflow.com/questions/321348/bash-if-a-vs-e-option
>https://askubuntu.com/questions/598080/bash-if-and-syntax
>for some double[[]] and space above link.
>http://tldp.org/LDP/abs/html/comparison-ops.html
>details ! eq etc above 

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
``# yum whatprovides [file_name]``
ex:
```
# yum whatprovides libstdc++
Loaded plugins: langpacks, product-id, search-disabled-repos, subscription-manager
This system is not registered to Red Hat Subscription Management. You can use subscription-manager to register.
libstdc++-4.8.5-4.el7.x86_64 : GNU Standard C++ Library
Repo        : @repo
```

```
# yum whatprovides *bin/ls
Loaded plugins: langpacks, product-id, search-disabled-repos, subscription-manager
This system is not registered to Red Hat Subscription Management. You can use subscription-manager to register.
coreutils-8.22-15.el7.x86_64 : A set of basic GNU tools commonly used in shell scripts
Repo        : @repo
Matched from:
Filename    : /bin/ls
Filename    : /usr/bin/ls
```

Finding file and libraries provided by a particular package
To find all the files (binaries and library files) provided by a package, use the below command.
```
# rpm -ql bash
/etc/skel/.bash_logout
/etc/skel/.bash_profile
/etc/skel/.bashrc
/usr/bin/alias
/usr/bin/bash
/usr/bin/bashbug
/usr/bin/bashbug-64
/usr/bin/bg
```


1. Method 1 : using rpm command
1. Use below rpm commands to find which rpm package provide a particular file.
```
# rpm -q --whatprovides [file name]```
For example, to find which rpm package provides /etc/hosts file, use the command below:
```
# rpm -q --whatprovides /etc/hosts
setup-2.8.14-23.el6.noarch```
To find the rpm package which provides the library file /usr/lib/gcc/x86_64-redhat-linux/4.4.4/libgomp.so, use the command below.
```
# rpm -qf /usr/lib/gcc/x86_64-redhat-linux/4.4.4/libgomp.so
gcc-4.4.7-18.el6.x86_64```
Similarly, you can also use the command below command as well.
```
# rpm -qf [file name]```
For example,
```
# rpm -qf /etc/hosts
setup-2.8.14-23.el6.noarch
```
>https://gitlab.com/apparmor/apparmor/wikis/Distro_CentOS
>https://www.thegeekdiary.com/how-to-find-which-rpm-package-provides-a-specific-file-or-library-in-rhel-centos/


对应的pip
```pip search```

ubuntu对应的apt-file 和'dpkg -S filename'

```
$ apt-file update
$ apt-file list mysqladmin
kmysqladmin: /usr/bin/kmysqladmin

$ apt-file search mysqladmin
autoconf-archive: /usr/share/aclocal/ac_prog_mysqladmin.m4
autoconf-archive: /usr/share/autoconf-archive/html/ac_prog_mysqladmin.html
bash-completion: /etc/bash_completion.d/mysqladmin
kmysqladmin: /usr/bin/kmysqladmin

$ apt-file search mysqladmin
mysql-admin: /usr/share/mysql-gui/administrator/mysqladmin_health.xml
mysql-admin: /usr/share/mysql-gui/administrator/mysqladmin_startup_variables_description.dtd
mysql-admin: /usr/share/mysql-gui/administrator/mysqladmin_startup_variables_description.xml
mysql-admin: /usr/share/mysql-gui/administrator/mysqladmin_status_variables.xml
mysql-admin: /usr/share/mysql-gui/administrator/mysqladmin_system_variables.xml
mysql-client-5.1: /usr/bin/mysqladmin
mysql-client-5.1: /usr/share/man/man1/mysqladmin.1.gz
mysql-cluster-client-5.1: /usr/bin/mysqladmin
mysql-cluster-client-5.1: /usr/share/man/man1/mysqladmin.1.gz
mysql-testsuite: /usr/lib/mysql-testsuite/r/mysqladmin.result
mysql-testsuite: /usr/lib/mysql-testsuite/t/mysqladmin.test


>https://stackoverflow.com/questions/4471327/ubuntu-equivalent-of-yums-whatprovides-to-find-which-package-provides-a-file

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


#lowercase/uppercase

以前写Bash Shell脚本，大小写转换通常这样做：

str="This is a Bash Shell script."

newstr=`tr '[A-Z]' '[a-z]' <<<"$str"`

今天看bash的man page，发现有更简单的方法

转小写，只需要将变量名字declare -l 后，再给变量赋值，变量的内容即为小写
转大写，只需要将变量名字declare -u后，再给变量赋值，变量的内容即为大写

例如:
m="abc"
echo $m # 输出为abc
declare -u m
echo $m # 输出为abc,
m="cde"
echo $m # 输出为CDE
declare -l m="HELL"
echo $m # 输出为hell

注意： 
1，declare 不影响变量当前值，而是影响declare之后的赋值

2，通过declare -l/-u进行设置过的变量，可以通过declare +l/+u来取消设置。

3，Bash版本比较低的不行……

```
#! /bin/bash
# 注意:脚本第一行一定要注明脚本解释器是bash.不能是sh,或dash
# 因为sh软连接有可能指向的是dash
var="Hello,Word"
# 把变量中的第一个字符换成大写 
echo ${var^} 
# 把变量中的所有小写字母，全部替换为大写
echo ${var^^}   
# 把变量中的第一个字符换成小写
echo ${var,}
# 把变量中的所有大写字母，全部替换为小写
echo ${var,,}
```



#echo换行
echo -e "text1\ntext2"
-e表示开启转义字符。



#linux multipe line result to variable

赋值
两种方式 ：

1、$()方式 
```
data=$(ls -l)
```
2、单反引号方式
```
data=`ls -l`

```
3、 Read
```
date +%s| read tm
echo $tm
```

>Read方式centos测试不成功，原因可能为
>https://blog.csdn.net/Rainnnbow/article/details/47000055

---
最近在将ksh转成bash运行的时候出现了问题。代码如下：

    echo $1 | sed 's/\..*$/''/' | read FILE_NAME

当使用ksh执行的时候没有问题，FILE_NAME能获取到正确的值。但当使用bash执行时，FILE_NAME值为空。

找了半天也不知道什么原因，最后找到了一种解决方法。代码如下：

    read FILE_NAME <<< ` echo $1 | sed 's/\..*$/''/' `

![https://img-blog.csdn.net/20150722112109453](data:image/png;base64,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)
上面的代码有两点需要注意：

①使用了 <<< ，这个叫“here string"，中文不知道叫什么，具体用法可以参考：http://www.360doc.com/content/10/0303/18/155970_17452864.shtml

②<<<之后的代码使用了反单引号括了起来，否则执行会报错。

参考：http://bbs.chinaunix.net/thread-1821214-1-1.html

**linux中查看现在使用的shell是ksh还是bash？以及怎样修改？**

**查看系统支持的shell： cat  /etc/shells**

![https://img-blog.csdn.net/20150722112147710](data:image/png;base64,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)

**查看现在使用的shell： **

![https://img-blog.csdn.net/20150722112155434](data:image/png;base64,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)

修改默认shell：

![https://img-blog.csdn.net/20150722112201665](data:image/png;base64,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)

具体解释参考：http://blog.chinaunix.net/uid-20722281-id-160012.html

另外，修改了系统默认shell之后不会立即生效，之后再次登录系统修改的shell才会生效。

---


读取
（1） 如果是echo $data，输出结果为一行，没有换行符:

echo $data
total 132 drwxrwxr-x 3 faster faster 4096 Mar 31 06:11 client drwxrwxr-x 2 faster faster 4096 Mar 31 06:11 common drwxrwxr-x 2 faster faster 4096 Sep 1 11:34 conf -rw-rw-r-- 1 faster faster 35067 Dec 29 2016 COPYING-3_0.txt -rw-rw-r-- 1 faster faster 2959 Dec 29 2016 fastdfs.spec -rw-rw-r-- 1 faster faster 32463 Dec 29 2016 HISTORY drwxrwxr-x 2 faster faster 4096 Dec 29 2016 init.d -rw-rw-r-- 1 faster faster 7755 Dec 29 2016 INSTALL -rwxrwxr-x 1 faster faster 5548 Dec 29 2016 make.sh drwxrwxr-x 2 faster faster 4096 Dec 29 2016 php_client -rw-rw-r-- 1 faster faster 2380 Dec 29 2016 README.md -rwxrwxr-x 1 faster faster 1768 Dec 29 2016 restart.sh -rwxrwxr-x 1 faster faster 1680 Dec 29 2016 stop.sh drwxrwxr-x 4 faster faster 4096 Mar 31 06:11 storage drwxrwxr-x 2 faster faster 4096 Dec 29 2016 test drwxrwxr-x 2 faster faster 4096 Mar 31 06:11 tracker

（2） 如果是echo "$data"，输出结果为多行，有换行符:

echo "$data"
total 132
drwxrwxr-x 3 faster faster  4096 Mar 31 06:11 client
drwxrwxr-x 2 faster faster  4096 Mar 31 06:11 common
drwxrwxr-x 2 faster faster  4096 Sep  1 11:34 conf
-rw-rw-r-- 1 faster faster 35067 Dec 29  2016 COPYING-3_0.txt
-rw-rw-r-- 1 faster faster  2959 Dec 29  2016 fastdfs.spec
-rw-rw-r-- 1 faster faster 32463 Dec 29  2016 HISTORY
drwxrwxr-x 2 faster faster  4096 Dec 29  2016 init.d
-rw-rw-r-- 1 faster faster  7755 Dec 29  2016 INSTALL
-rwxrwxr-x 1 faster faster  5548 Dec 29  2016 make.sh
drwxrwxr-x 2 faster faster  4096 Dec 29  2016 php_client
-rw-rw-r-- 1 faster faster  2380 Dec 29  2016 README.md
-rwxrwxr-x 1 faster faster  1768 Dec 29  2016 restart.sh
-rwxrwxr-x 1 faster faster  1680 Dec 29  2016 stop.sh
drwxrwxr-x 4 faster faster  4096 Mar 31 06:11 storage
drwxrwxr-x 2 faster faster  4096 Dec 29  2016 test
drwxrwxr-x 2 faster faster  4096 Mar 31 06:11 tracker

>https://blog.csdn.net/hongweigg/article/details/77948664



#read

Linux read命令用于从标准输入读取数值。

read 内部命令被用来从标准输入读取单行数据。这个命令可以用来读取键盘输入，当使用重定向的时候，可以读取文件中的一行数据。

### 语法

    read [-ers] [-a aname] [-d delim] [-i text] [-n nchars] [-N nchars] [-p prompt] [-t timeout] [-u fd] [name ...]

**参数说明:**

* -a 后跟一个变量，该变量会被认为是个数组，然后给其赋值，默认是以空格为分割符。
* -d 后面跟一个标志符，其实只有其后的第一个字符有用，作为结束的标志。
* -p 后面跟提示信息，即在输入前打印提示信息。
* -e 在输入的时候可以使用命令补全功能。
* -n 后跟一个数字，定义输入文本的长度，很实用。
* -r 屏蔽\，如果没有该选项，则\作为一个转义字符，有的话 \就是个正常的字符了。
* -s 安静模式，在输入字符时不再屏幕上显示，例如login时输入密码。
* -t 后面跟秒数，定义输入字符的等待时间。
* -u 后面跟fd，从文件描述符中读入，该文件描述符可以是exec新开启的。
### 实例

**1、简单读取**

    #!/bin/bash
    
    #这里默认会换行  
    echo "输入网站名: "  
    #读取从键盘的输入  
    read website  
    echo "你输入的网站名是 $website"  
    exit 0  #退出

测试结果为：

    输入网站名: 
    www.runoob.com
    你输入的网站名是 www.runoob.com

**2、-p 参数，允许在 read 命令行中直接指定一个提示。**

    #!/bin/bash
    
    read -p "输入网站名:" website
    echo "你输入的网站名是 $website" 
    exit 0

测试结果为：

    输入网站名:www.runoob.com
    你输入的网站名是 www.runoob.com

**3、-t 参数指定 read 命令等待输入的秒数，当计时满时，read命令返回一个非零退出状态。**

    #!/bin/bash
    
    if read -t 5 -p "输入网站名:" website
    then
        echo "你输入的网站名是 $website"
    else
        echo "\n抱歉，你输入超时了。"
    fi
    exit 0

执行程序不输入，等待 5 秒后：

    输入网站名:
    抱歉，你输入超时了

4、除了输入时间计时，还可以使用 **-n** 参数设置 **read** 命令计数输入的字符。当输入的字符数目达到预定数目时，自动退出，并将输入的数据赋值给变量。

    #!/bin/bash
    
    read -n1 -p "Do you want to continue [Y/N]?" answer
    case $answer in
    Y | y)
          echo "fine ,continue";;
    N | n)
          echo "ok,good bye";;
    *)
         echo "error choice";;
    
    esac
    exit 0

该例子使用了-n 选项，后接数值 1，指示 read 命令只要接受到一个字符就退出。只要按下一个字符进行回答，read 命令立即接受输入并将其传给变量，无需按回车键。

只接收 2 个输入就退出：

    #!/bin/bash
    
    read -n2 -p "请随便输入两个字符: " any
    echo "\n您输入的两个字符是:$any"
    exit 0

执行程序输入两个字符：

    请随便输入两个字符: 12
    您输入的两个字符是:12

5、**-s** 选项能够使 **read** 命令中输入的数据不显示在命令终端上（实际上，数据是显示的，只是 **read** 命令将文本颜色设置成与背景相同的颜色）。输入密码常用这个选项。

    #!/bin/bash
    
    read  -s  -p "请输入您的密码:" pass
    echo "\n您输入的密码是 $pass"
    exit 0

执行程序输入密码后是不显示的：

    请输入您的密码:
    您输入的密码是 runoob

**6.读取文件**

每次调用 read 命令都会读取文件中的 "一行" 文本。当文件没有可读的行时，read 命令将以非零状态退出。

通过什么样的方法将文件中的数据传给 read 呢？使用 cat 命令并通过管道将结果直接传送给包含 read 命令的 while 命令。

测试文件 test.txt 内容如下：

    123
    456
    runoob

测试代码：

    #!/bin/bash
      
    count=1    # 赋值语句，不加空格
    cat test.txt | while read line      # cat 命令的输出作为read命令的输入,read读到>的值放在line中
    do
       echo "Line $count:$line"
       count=$[ $count + 1 ]          # 注意中括号中的空格。
    done
    echo "finish"
    exit 0

执行结果为：

    Line 1:123
    Line 2:456
    Line 3:runoob
    finish

使用 **-e** 参数，以下实例输入字符 **a** 后按下 **Tab** 键就会输出相关的文件名(该目录存在的)：

    $ read -e -p "输入文件名:" str 
    输入文件名:a
    a.out    a.py     a.pyc    abc.txt  
    输入文件名:a


 published from :[Linux read 命令 | 菜鸟教程](https://www.runoob.com/linux/linux-comm-read.html)

---



#Openssl

- enc
openssl中enc 中salt相关参数
需要注意的就是，当仅设置-K时，还需要设置IV。

```
-pass arg
the password source. For more information about the format of arg see the PASS PHRASE ARGUMENTS section in openssl(1).
 
-salt
use a salt in the key derivation routines. This is the default.
 
-nosalt
don't use a salt in the key derivation routines. This option SHOULD NOT be used except for test purposes or compatibility with ancient versions of OpenSSL and SSLeay.
 
-k password
the password to derive the key from. This is for compatibility with previous versions of OpenSSL. Superseded by the -pass argument.
 
-kfile filename
read the password to derive the key from the first line of filename. This is for compatibility with previous versions of OpenSSL. Superseded by the -pass argument.
 
-nosalt
do not use a salt
 
-salt
use salt (randomly generated or provide with -S option) when encrypting (this is the default).
 
-S salt
the actual salt to use: this must be represented as a string of hex digits.
 
-K key
the actual key to use: this must be represented as a string comprised only of hex digits. If only the key is specified, the IV must additionally specified using the -iv option. When both a key and a password are specified, the key given with the -K option will be used and the IV generated from the password will be taken. It probably does not make much sense to specify both key and password.
 
-iv IV
```

- manually passwd for linux

```
引言:在Linux系统中我们要向手动生成一个密码可以采用opensll passwd来生成一个密码作为用户账号的密码。Linux系统中的密码存放在/etc/shadow文件中，并且是以加密的方式存放的，根据加密方式的不同，所产生的加密后的密码的位数也不同。

openssl passwd的作用是用来计算密码hash的，目的是为了防止密码以明文的形式出现。

语法格式： openssl passwd [option] passwd

openssl passwd常用的选项如下：

-1：表示采用的是MD5加密算法。

-salt：指定salt值，不使用随机产生的salt。在使用加密算法进行加密时，即使密码一样，salt不一样，所计算出来的hash值也不一样，除非密码一样，salt值也一样，计算出来的hash值才一样。salt为8字节的字符串。 

示例：

[tom@localhost ~]$ openssl passwd -1 -salt '12345678'  ##注意‘12345678’不是密码而是密码的长度

Password:   ##这里输入的是密码

$1$12345678$1qWiC4czIc07B4J8bPjfC0   ##这是生成的密文密码



将生成的密码串，手动添加到/etc/shadow中就可用作用户的登陆密码了。
```



>https://www.cnblogs.com/gordon0918/p/5317701.html

- EXAMPLE:

1、只对文件进行base64编码，而不使用加解密
```
/*对文件进行base64编码*/
openssl enc -base64 -in plain.txt -out base64.txt
/*对base64格式文件进行解密操作*/
openssl enc -base64 -d -in base64.txt -out plain2.txt
/*使用diff命令查看可知解码前后明文一样*/
diff plain.txt plain2.txt
```
2、不同方式的密码输入方式

```
/*命令行输入，密码123456*/
openssl enc -aes-128-cbc -in plain.txt -out out.txt -pass pass:123456
/*文件输入，密码123456*/
echo 123456 > passwd.txt
openssl enc -aes-128-cbc -in plain.txt -out out.txt -pass file:passwd.txt
/*环境变量输入，密码123456*/
passwd=123456
export passwd
openssl enc -aes-128-cbc -in plain.txt -out out.txt -pass env:passwd
/*从文件描述输入*/ 
openssl enc -aes-128-cbc -in plain.txt -out out.txt -pass fd:1  
/*从标准输入输入*/ 
openssl enc -aes-128-cbc -in plain.txt -out out.txt -pass stdin 
```
3、固定salt值加密

```
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt -pass pass:123456 -P
salt=32F5C360F21FC12D
key=D7E1499A578490DF940D99CAE2E29EB1
iv =78EEB538897CAF045F807A97F3CFF498
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt -pass pass:123456 -P
salt=DAA482697BECAB46
key=9FF8A41E4AC011FA84032F14B5B88BAE
iv =202E38A43573F752CCD294EB8A0583E7
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt -pass pass:123456 -P -S 123
salt=1230000000000000
key=50E1723DC328D98F133E321FC2908B78
iv =1528E9AD498FF118AB7ECB3025AD0DC6
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt -pass pass:123456 -P -S 123
salt=1230000000000000
key=50E1723DC328D98F133E321FC2908B78
iv =1528E9AD498FF118AB7ECB3025AD0DC6
xlzh@cmos:~$ 
```
可以看到，不使用-S参数，salt参数随机生成，key和iv值也不断变化，当slat值固定时，key和iv值也是固定的。

4、加解密后过程使用base64编解码

```
/*使用-a参数加密后使用base64编码*/
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -a -out encrypt.txt -pass pass:123456
/*使用-a参数解密前使用base64解码*/
xlzh@cmos:~$ openssl enc -aes-128-cbc -in encrypt.txt -d -a -out plain1.txt -pass pass:123456
/*文件一样*/
xlzh@cmos:~$ diff plain.txt plain1.txt 
/*加密后文件使用了base64编码*/
xlzh@cmos:~$ cat encrypt.txt 
U2FsdGVkX19KbCj9GMI1TBOQjP8JJcefIUH1tHwf/Z4=
```
 5、手动指定Key和IV值

```
/*手动指定key和iv值，salt固定*/
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt  -K 1223 -iv f123 -p
salt=0B00000000000000
key=12230000000000000000000000000000
iv =F1230000000000000000000000000000
/*指定pass密码，不起作用，注意Key和IV值是16进制*/
xlzh@cmos:~$ openssl enc -aes-128-cbc -in plain.txt -out encrypt.txt  -K 1223 -iv f123 -p -pass pass:123456
salt=F502F4B8DE62E0E5
key=12230000000000000000000000000000
iv =F1230000000000000000000000000000
```

- 证书管理
>https://segmentfault.com/a/1190000014963014

#xxd hex2str

　　　　		关于xxd的使用，就最上面的示例的几个参数做下解释：

　　　　　　-ps ，只打印16进制的信息，默认会打印位偏移、十六进制和ascii结果，如果只要16进制的转化结果就使用这个参数

　　　　　　-u ，16进制大写显示

　　　　　　-c，换行宽度，如果生成的16进制过长默认是会换行的，如果不希望结果被分成两行显示就把换行宽度设大一点

example:
``echo -n "${data}" | openssl des-cbc  -iv 31313131312D2D2D -K 31313131312D2D2D -nosalt  | xxd -ps -u -c100``



#Shell enc

1、linux自带的加密命令 gzexe  
使用该命令，主要是防止一些菜鸟和一些不太懂得加密的新手。这种加密不是很安全，但是能满足一般的加密用途(隐藏敏感信息)。它不但加密而且还是压缩文件。

[root@super ~]# which gzexe
/usr/bin/gzexe

[root@super ~]# rpm -qf /usr/bin/gzexe
gzip-1.3.12-18.el6.x86_64

    新建一个脚本进行测试，脚本如下：
```
[root@super ~]# vim hello.sh 


#!/bin/bash
echo hello world .
```
    使用gzexe 进行加密：

[root@super ~]# gzexe hello.sh 
hello.sh:	19.4%
[root@super ~]# ll hello.sh*
-rw-r--r-- 1 root root 861 Jun 15 15:43 hello.sh
-rw-r--r-- 1 root root  31 Jun 15 15:42 hello.sh~

    加密后，会变成两个文件，其中 hello.sh 为加密后的文件，hello.sh~为源文件。

[root@super ~]# sh hello.sh
hello world .
[root@super ~]# sh hello.sh~ 
hello world .

两个执行的结果都是一致的。


但是使用gzexe解密也是非常简单的，这就是使用gzexe的弊端。
```
[root@super ~]# cat hello.sh 
#!/bin/sh
skip=44


tab='	'
nl='
```
这是加密后脚本的一部分。如果我们要解密。只需要执行 gzexe -d 。

ll hello.sh*
-rw-r--r-- 1 root root  31 Jun 15 15:48 hello.sh
-rw-r--r-- 1 root root 861 Jun 15 15:43 hello.sh~

也是会生成两个文件，这是源文件为：hello.sh  加密文件为：hello.sh~


二、使用SHC加密。

SHC代表shell script compiler, 即shell脚本编译器。通过SHC编译过后的脚本程序对普通用户来说，一般都是不可读的。因此想要正真保护代码就得用到SHC加密。

    1.下载并编译SHC

[root@super bash]# wget http://www.datsi.fi.upm.es/~frosal/sources/shc-3.8.7.tgz            #下载压缩包

[root@super bash]# tar zxvf shc-3.8.7.tgz

[root@super bash]# cd shc-3.8.7[root@super shc-3.8.7]# make

[root@super shc-3.8.7]# ./shc -v
shc parse(-f): No source file specified

shc Usage: shc [-e date] [-m addr] [-i iopt] [-x cmnd] [-l lopt] [-rvDTCAh] -f script

    2.建立一个bash脚本测试。
```
[root@super shc-3.8.7]# vim hello.sh  
#!/bin/bash
echo hello world .
```
    3.使用SHC加密bash脚本

[root@super shc-3.8.7]# ll hello.sh*
-rw-r--r-- 1 root root    31 Jun 15 15:57 hello.sh
-rwx--x--x 1 root root 11624 Jun 15 16:01 hello.sh.x
-rw-r--r-- 1 root root  9411 Jun 15 16:01 hello.sh.x.c

这样会产生三个文件。
hello.sh                    是原始未加密的脚本。
hello.sh.x                是加密的二进制格式的bash脚本。
hello.sh.x.c                是原始脚本的C源代码，该文件是从源bash脚本中转换而来的。SHC就是通过将bash脚本转为C语言在编译加密的。

[root@super shc-3.8.7]# file hello.sh*
hello.sh:     Bourne-Again shell script text executable
hello.sh.x:   ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.18, stripped
hello.sh.x.c: ASCII C program text

    4.执行bash脚本。

[root@super shc-3.8.7]# ./hello.sh.x
hello world .

二、SHC的其他功能
    1、设置脚本使用期限

可以通过SHC设置有限期，当过了有限期，用户再次使用时，就会收到报错信息。SHC使用-e  dd/mm/yyyy来实现该功能

[root@super shc-3.8.7]# ./shc -e 14/6/2015 -f hello.sh
[root@super shc-3.8.7]# ./hello.sh.x
./hello.sh.x: has expired!
Please contact your provider
[root@super shc-3.8.7]# date
Mon Jun 15 16:11:15 CST 2015

    2.创建可灵活使用的脚本

可以使用./shc --h  来查看帮助。通常把以下选项进行连用：
-r ：允许该脚本在同操作系统的不同硬件上运行。
-T：运行然后调试命令来跟踪脚本。
-v：输出详细信息。

[root@super shc-3.8.7]# ./shc -v -T -r -f hello.sh 
shc shll=bash
shc [-i]=-c
shc [-x]=exec '%s' "$@"
shc [-l]=
shc opts=
shc: cc  hello.sh.x.c -o hello.sh.x
shc: strip hello.sh.x
shc: chmod go-r hello.sh.x

[root@super shc-3.8.7]# ll hello.sh*
-rw-r--r-- 1 root root   30 Jun 15 16:17 hello.sh
-rwx--x--x 1 root root 9336 Jun 15 16:17 hello.sh.x
-rw-r--r-- 1 root root 9630 Jun 15 16:17 hello.sh.x.c

[root@super shc-3.8.7]# file !$   
file hello.sh*
hello.sh:     Bourne-Again shell script text executable
hello.sh.x:   ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.18, stripped
hello.sh.x.c: ASCII C program text





3、Myway

z.sh
```
jack=$(echo U2FsdGVkX1/ihjjKVCgouVg5bhbOVmg/Y0YCrMA0qw4= | openssl enc -aes-256-cbc -a -d -salt -pass pass:thisispass)
echo $jack
$jack
```



#linux passwd generate 


- pwgen
yum install pwgen
epel release source needed.
example:
```
\#pwgen 10 1
Thah9haiC8
```
- makepasswd
seems like a ubuntu tool,not tested.
example:
生成一个长度为 50 个字符的随机密码。
``$ makepasswd --char50``

- mkpasswd
a tool that generate password with salt
```
$ mkpasswd tecmint
everytime you will got a echo back with random salt added to passwd 'tecmint'.
```

- openssl enc 
```# echo Tecmint-is-a-Linux-Community | openssl enc -aes-256-cbc -a -salt -pass pass:tecmint```
by default openssl using random salt.so -salt is redundant.
- openssl dec
``` echo U2FsdGVkX18Zgoc+dfAdpIK58JbcEYFdJBPMINU91DKPeVVrU2k9oXWsgpvpdO/Z | openssl enc -aes-256-cbc -a -d -salt -pass pass:tecmint```








#vi


- ：u
撤销
恢复撤销：Ctrl + r
- GG
跳到文件末尾
- gg
跳到文件开头
- :set nu
设置行号
- :3
跳到第三行
- :s/who/me/
替换整个文件中的who为me
``[range]s/s1/s2/ [option]``
[range] 表示检索范围，省略时表示当前行。下面是一些检索范围的例子。
1,10表示从第 1 行到 10 行。
%表示整个文件，同1, $。
. ,$从当前行到文件尾。
s 为替换命令。
s1 要被替换的串，s2 为替换的串。
option 表示选项：
/g表示在全局文件中进行替换。
/c表示在每次替换之前需要用户进行确认。
省略时仅对每行第一个匹配串进行替换。

- 清空文件
Go to command mode in the editor by pressing ESC key on the keyboard.
Press gg. It will take to the first line of the file.
Then press dG. This will delete from the first line to the last line.





#/bin,/sbin,/usr/sbin,/usr/bin 目录
     这些目录都是存放命令的，首先区别下/sbin和/bin：

    从命令功能来看，/sbin 下的命令属于基本的系统命令，如shutdown，reboot，用于启动系统，修复系统，/bin下存放一些普通的基本命令，如ls,chmod等，这些命令在Linux系统里的配置文件脚本里经常用到。

    从用户权限的角度看，/sbin目录下的命令通常只有管理员才可以运行，/bin下的命令管理员和一般的用户都可以使用。

    从可运行时间角度看，/sbin,/bin能够在挂载其他文件系统前就可以使用。

   而/usr/bin,/usr/sbin与/sbin /bin目录的区别在于：

    /bin,/sbin目录是在系统启动后挂载到根文件系统中的，所以/sbin,/bin目录必须和根文件系统在同一分区；

    /usr/bin,usr/sbin可以和根文件系统不在一个分区。

    /usr/sbin存放的一些非必须的系统命令；/usr/bin存放一些用户命令，如led(控制LED灯的)。

    转下一位网友的解读，个人认为诠释得很到位：

    /bin是系统的一些指令。bin为binary的简写主要放置一些系统的必备执行档例如:cat、cp、chmod df、dmesg、gzip、kill、ls、mkdir、more、mount、rm、su、tar等。
    /sbin一般是指超级用户指令。主要放置一些系统管理的必备程式例如:cfdisk、dhcpcd、dump、e2fsck、fdisk、halt、ifconfig、ifup、 ifdown、init、insmod、lilo、lsmod、mke2fs、modprobe、quotacheck、reboot、rmmod、 runlevel、shutdown等。
    /usr/bin　是你在后期安装的一些软件的运行脚本。主要放置一些应用软体工具的必备执行档例如c++、g++、gcc、chdrv、diff、dig、du、eject、elm、free、gnome*、 gzip、htpasswd、kfm、ktop、last、less、locale、m4、make、man、mcopy、ncftp、 newaliases、nslookup passwd、quota、smb*、wget等。

    /usr/sbin   放置一些用户安装的系统管理的必备程式例如:dhcpd、httpd、imap、in.*d、inetd、lpd、named、netconfig、nmbd、samba、sendmail、squid、swap、tcpd、tcpdump等。
    如果新装的系统，运行一些很正常的诸如：shutdown，fdisk的命令时，悍然提示：bash:command not found。那么
    首先就要考虑root 的$PATH里是否已经包含了这些环境变量。
    可以查看PATH，如果是：PATH=$PATH:$HOME/bin则需要添加成如下：
    PATH=$PATH:$HOME/bin:/sbin:/usr/bin:/usr/sbin



#linux获取文件大小

centos ``stat -c%s "/usr/bin/ls"``

``size=$(stat -c%s $filename)``
```
size=$(stat -f%z $filename) # BSD stat

size=$(stat -c%s $filename) # GNU stat?
```




#交互式 Bash Shell 获取进程 pid
在已知进程名(name)的前提下，交互式 Shell 获取进程 pid 有很多种方法，典型的通过 grep 获取 pid 的方法为（这里添加 -v grep是为了避免匹配到 grep 进程）：
```
ps -ef | grep "name" | grep -v grep | awk '{print $2}'
```
或者不使用 grep（这里名称首字母加[]的目的是为了避免匹配到 awk 自身的进程）：
```
ps -ef | awk '/[n]ame/{print $2}'
```
如果只使用 x 参数的话则 pid 应该位于第一位：
```
ps x | awk '/[n]ame/{print $1}'
```
最简单的方法是使用 pgrep：
```
pgrep -f name
```
如果需要查找到 pid 之后 kill 掉该进程，还可以使用 pkill：
```
pkill -f name
```
如果是可执行程序的话，可以直接使用 pidof
```
pidof name
```

>pgrep使用-d指定分隔符


#bash list

list loop
```
strings=(
    string1
    string2
    "string with spaces"
    stringN
)
for i in "${strings[@]}"; do
    echo "$i"
done

```


#char split
```
#!/bin/bash
a="hello,world,nice,to,meet,you"
#要将$a分割开，先存储旧的分隔符
OLD_IFS="$IFS"

#设置分隔符
IFS="," 

#如下会自动分隔
arr=($a)

#恢复原来的分隔符
IFS="$OLD_IFS"

#遍历数组
for s in ${arr[@]}
do
echo "$s"
done
```

功能就是sh文件分割字符串到数组并遍历引用，示例代码如下：
```
CURLURL="1;2;5;9"
IFS=";"
read -r -a array <<< "$CURLURL"
for i in "${!array[@]}"; do
    echo -e ${array[i]}"\n"
done
```
#declare

bash提供了declare命令来声明变量，该命令的基本语法如下：
```
declare attribute variable
      其中，attribute表示变量的属性，常用的属性有如下所述。
            -p  显示所有变量的值。
            -i   将变量定义为整数，在之后可以对表达式求值，结果只能是整数。如果求值失败或不为整数则置0。
            -r   将变量声明为只读变量。
            -a  将变量声明为 数组变量。
            -f   显示所有自定义函数，包括名称和函数体。
            -x   将变量设置成环境变量，这样可以在随后的脚本和程序中使用。
```




#get arry length
```
echo ${#nums[*]}
echo ${#nums[@]}
```


#uuid in bash
```
#!/bin/bash

psd="/proc/sys/kernel/random/uuid"
echo $(cat $psd)
UUID=$(cat /proc/sys/kernel/random/uuid)
echo $UUID
```



#multiLine echo
```
__usage="
Usage: $(basename $0) [OPTIONS]

Options:
  -l, --level <n>              Something something something level
  -n, --nnnnn <levels>         Something something something n
  -h, --help                   Something something something help
  -v, --version                Something something something version
"
```
Then I can simply use it as
```
echo "$__usage"
```
or even better, when parsing parameters, I can just echo it there in a one-liner:
```
levelN=${2:?"--level: n is required!""${__usage}"}
```

---

Here documents are often used for this purpose.
```
cat << EOF
usage: up [--level <n>| -n <levels>][--help][--version]

Report bugs to: 
up home page:
EOF
```
>https://stackoverflow.com/questions/10969953/how-to-output-a-multiline-string-in-bash




#bash escape

```
[root@localhost tmp]# echo "_______________$$$$$$" | rev
242012420124201_______________
[root@localhost tmp]# echo $$
10242
[root@localhost tmp]# echo $$$
10242$
[root@localhost tmp]# echo $$$$
1024210242
```
escape
```
[root@localhost tmp]# echo "_______________\$\$\$\$\$\$" | rev
$$$$$$_______________
```


#char split

获取当前shell脚本所在的路径(目录), 支持软链接.

```
DIR=`S=\`readlink "$0"\`; [ -z "$S" ] && S=$0; dirname $S`
```
$0: 获取当前脚本的名称
$#: 传递给脚本的参数个数
$$: shell脚本的进程号
$1, $2, $3...：脚本程序的参数
Linux 的字符串截取很有用。有八种方法。

假设有变量 var=http://www.aaa.com/123.htm.

1. \# 号截取，删除左边字符，保留右边字符。

 

复制代码代码如下:

``echo ${var#*//}``
 

其中 var 是变量名，# 号是运算符，*// 表示从左边开始删除第一个 // 号及左边的所有字符
即删除 http://
结果是 ：www.aaa.com/123.htm

2. \#\# 号截取，删除左边字符，保留右边字符。

 

复制代码代码如下:

``echo ${var##*/}``
 

\#\#*/ 表示从左边开始删除最后（最右边）一个 / 号及左边的所有字符
即删除 http://www.aaa.com/

结果是 123.htm

3. %号截取，删除右边字符，保留左边字符

 

复制代码代码如下:

``echo ${var%/*}``
 

%/* 表示从右边开始，删除第一个 / 号及右边的字符

结果是：http://www.aaa.com

4. %% 号截取，删除右边字符，保留左边字符

 

复制代码代码如下:

``echo ${var%%/*}``
 

%%/* 表示从右边开始，删除最后（最左边）一个 / 号及右边的字符
结果是：http:

5. 从左边第几个字符开始，及字符的个数

 

复制代码代码如下:

``echo ${var:0:5}``
 

其中的 0 表示左边第一个字符开始，5 表示字符的总个数。
结果是：http:

6. 从左边第几个字符开始，一直到结束。

 

复制代码代码如下:

``echo ${var:7}``
 

其中的 7 表示左边第8个字符开始，一直到结束。
结果是 ：www.aaa.com/123.htm

7. 从右边第几个字符开始，及字符的个数

 

复制代码代码如下:

``echo ${var:0-7:3}``
 

其中的 0-7 表示右边算起第七个字符开始，3 表示字符的个数。
结果是：123

8. 从右边第几个字符开始，一直到结束。

 

复制代码代码如下:

``echo ${var:0-7}``
 

表示从右边第七个字符开始，一直到结束。
结果是：123.htm

注：（左边的第一个字符是用 0 表示，右边的第一个字符用 0-1 表示）


---

                   命令的2种替换形式 $()和 ``  
 示例：截断字符串   
a):  
 #截取文件名称  
 var1=$(basename /home/aimybbe/bash/test.sh)  
 echo $var1  

 #截取目录  
 var2=$(dirname /home/aimybbe/bash/test.sh)  
 echo $var2  
 b):  
 var1=`basename /home/aimybbe/bash/test.sh`  
 echo $var1  

 var2=$(dirname /home/aimybbe/bash/test.sh)  
 echo $var2  

 更专业的字符串截取方法：  

 示例：testfile.tar.gz  

 a)获取后缀名  
 你想截取 tar.gz  
 filename=testfile.tar.gz  
 file=${filename#*.}  
 echo $file  
 你想截取 gz  
 filename=testfile.tar.gz  
 file=${filename##*.}  
 echo $file  
 说明：  
 这里的${filename##*.}什么意思呢？在 ${ } 中输入环境变量名称，两个##(或一个#)，然后是通配符 ("*.")。  
 然后，bash 取得 filename，找到从字符串 "testfile.tar.gz"开始处开始、且匹配通配符 "*."的最长子字符串(或最短)，然后将其从字符串的开始处截去。  
<span style="color:#ff0000;">注意：</span>  
 1.#意思是从字符串的开始处开始截取。  
 2.两个##代表匹配的最大长度，一个#代表匹配的最小长度(也就是说这里不是一个#匹配一个‘.’)  

 b)获取文件名称(也就是去除后缀名)  
 你想截取testfile.tar  
 filename=testfile.tar.gz  
 file=${filename%.*}  
 echo $file  
 你想截取testfile  
 filename=testfile.tar.gz  
 file=${filename%%.*}  
 echo $file   
<span style="color:#ff0000;">注意：</span>  
 1.这个方法和上面原理相同%就是从末尾字符串开始截取，%%就是最大长度,%就是最小长度  

 c)截取任意的字符  
 你想截取file  
 filename=testfile.tar.gz  
 file=${filename:4:4}  
 echo $file  

 你想截取test  
 filename=testfile.tar.gz  
 file=${filename:0:4}  
 echo $file  
 说明：  
 格式为${filename::}第一个':'后面的数字是字符串的索引从左边开始，索引从0开始，第二个':'后面的数字是长度，两处的数字都是十进制数值。



>http://c.biancheng.net/view/1120.html
>https://www.iteye.com/blog/gavin2lee-2352908
>http://www.360doc.com/content/18/0704/19/54185769_767728959.shtml
>https://www.cnblogs.com/xwdreamer/p/3823463.html




#sleep延时

在 linux shell 脚本中经常需要做一些延时处理。
所以经常要用到 sleep 或 usleep 函数。
下面来说一下  sleep 和 usleep 的区别：

sleep : 默认以秒为单位。

usleep : 默认以微秒为单位。

1s = 1000ms = 1000000us

sleep 不但可以用秒为单位，还可以指定延迟的单位，例如：

sleep 1s 表示延迟一秒

sleep 1m 表示延迟一分钟

sleep 1h 表示延迟一小时

sleep 1d 表示延迟一天

