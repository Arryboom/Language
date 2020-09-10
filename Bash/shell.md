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

- find按照时间搜索

查找所有此时间之后修改创建的文件
```
find ./* -newermt '2020-08-31 00:31:00'
```
>可以输出到tt.txt然后使用cat tt.txt | grep -v "/proc" | grep -v "/sys" | grep -v "/var/lib" | grep -v "/run" | grep -v "/run" | grep -v "/dev"快速查看

```
find -type f
```
>仅搜索文件类型(不包括目录)

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

>https://unix.stackexchange.com/questions/304021/how-can-i-implement-a-whitelist-on-a-specific--using-iptables

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




$random

to generate a random number between 1 and 10

```
$ echo $((1 + RANDOM % 10))
3
```

>https://stackoverflow.com/questions/1194882/how-to-generate-random-number-in-bash



#read
```
选项说明：
-a：将分裂后的字段依次存储到指定的数组中，存储的起始位置从数组的index=0开始。
-d：指定读取行的结束符号。默认结束符号为换行符。
-n：限制读取N个字符就自动结束读取，如果没有读满N个字符就按下回车或遇到换行符，则也会结束读取。
-N：严格要求读满N个字符才自动结束读取，即使中途按下了回车或遇到了换行符也不结束。其中换行符或回车算一个字符。
-p：给出提示符。默认不支持"\n"换行，要换行需要特殊处理，见下文示例。例如，"-p 请输入密码："
-r：禁止反斜线的转义功能。这意味着"\"会变成文本的一部分。
-s：静默模式。输入的内容不会回显在屏幕上。
-t：给出超时时间，在达到超时时间时，read退出并返回错误。也就是说不会读取任何内容，即使已经输入了一部分。
```



#sshkeygen

SSH supports several public key algorithms for authentication keys. These include:

rsa - an old algorithm based on the difficulty of factoring large numbers. A key size of at least 2048 bits is recommended for RSA; 4096 bits is better. RSA is getting old and significant advances are being made in factoring. Choosing a different algorithm may be advisable. It is quite possible the RSA algorithm will become practically breakable in the foreseeable future. All SSH clients support this algorithm.

dsa - an old US government Digital Signature Algorithm. It is based on the difficulty of computing discrete logarithms. A key size of 1024 would normally be used with it. DSA in its original form is no longer recommended.

ecdsa - a new Digital Signature Algorithm standarized by the US government, using elliptic curves. This is probably a good algorithm for current applications. Only three key sizes are supported: 256, 384, and 521 (sic!) bits. We would recommend always using it with 521 bits, since the keys are still small and probably more secure than the smaller keys (even though they should be safe as well). Most SSH clients now support this algorithm.

ed25519 - this is a new algorithm added in OpenSSH. Support for it in clients is not yet universal. Thus its use in general purpose applications may not yet be advisable.
The algorithm is selected using the -t option and key size using the -b option. The following commands illustrate:
```
ssh-keygen -t rsa -b 4096
ssh-keygen -t dsa
ssh-keygen -t ecdsa -b 521
ssh-keygen -t ed25519
```


#screen

screen用法：

打开新的会话窗口：screen
结束当前会话：exit
在新会话中执行程序（程序关闭时会话自动结束）：screen vi test.c
打开新会话并起个名字：screen -S myname
暂时离开会话（经常用）：Ctrl+a 然后 d
查看会话列表: screen -ls
恢复之前离开的会话：screen -r 会话名或进程号
清除dead状态的会话：screen -wipe
启动一个开始就是Detached状态的会话：screen -dmS 名字 命令


#crontab

/etc/crontab文件和crontab -e命令区别

1、格式不同

前者

按 Ctrl+C 复制代码
```
# For details see man 4 crontabs

# Example of job definition:
# .---------------- minute (0 - 59)
# |  .------------- hour (0 - 23)
# |  |  .---------- day of month (1 - 31)
# |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
# |  |  |  |  .---- day of week (0 - 6) (Sunday=0 or 7) OR sun,mon,tue,wed,thu,fri,sat
# |  |  |  |  |
# *  *  *  *  * user-name command to be executed
```
后者
```
#50 1 * * *  command
```

2、使用范围

修改/etc/crontab这种方法只有root用户能用，这种方法更加方便与直接直接给其他用户设置计划任务，而且还可以指定执行shell等等，crontab -e这种所有用户都可以使用，普通用户也只能为自己设置计划任务。然后自动写入/var/spool/cron/usename

3、服务管理区别
```
/etc/init.d/crond restart
service crond restart```
1、crontab会进行语法检查，vi不会

2、有些os的crond不会重读配置，所以用service重启

crontab -e是某个用户的周期计划任务；/etc/crontab是系统的周期任务

crontab -e与/etc/crontab修改语法格式不一样，后者多一个user指定

不管用crontab -e或者/etc/crontab都不需要重新启动crond服务
```
01 22 * * * /sbin/shutdown -h now```
执行的结果都是一样, 一般Linux都用shutdown -h now也可以init 0，init 0在UNIX用得比较多，关机需要root的身份在可以执行

可以选取方法操作：

方法1：

换到root 的身份
```
su - root

crontab -e (按a增加下)

01 22 * * * /sbin/shutdown -h now   (或01 22 * * * /sbin/init 0)
```
保存退出即可以

方法2：

vi /etc/crontab在里面插入
```
01 22 * * * root   /sbin/shutdown -h now
或 
01 22 * * * root   /sbin/init 0 ```
crontab  -e是针对用户的cron来设计的，如果是系统的例行性任务，该怎么办？是否还是需要以crontab -e来管理例行性命令？当然不需要，只需要编辑/etc/crontab文件就可以了。需要注意的是：crontab -e的作用其实是/usr/bin/crontab这个执行文件，但是/etc/crontab是个纯文本文件，可以root的身份编辑这个文件。

基本上，cron服务的最低检测时间单位是分钟，所以cron会每分钟读取一次/etc/crontab与/var/spool/cron中的数据内容，因此，只要您编辑完/etc/crontab文件并且保存之后，crontab时设定就会自动执行。

注意：在Linux下的crontab会自动帮我们每分钟重新读取一次/etc/crontab的例行工作事项，但是某些原因或在其他的unix系统中，由于crontab是读到内存中，所以在您修改完/etc/crontab之后可能并不会马上执行，这时请重新启动crond服务。
```
/etc/rc.d/init.d/crond   restart
```

7、crontab的限制

/etc/cron.allow：将可以使用 crontab 的帐号写入其中，若不在这个文件内的使用者则不可使用 crontab；

/etc/cron.deny：将不可以使用 crontab 的帐号写入其中，若未记录到这个文件当中的使用者，就可以使用 crontab 。

以优先顺序来说， /etc/cron.allow 比 /etc/cron.deny 要优先， 而判断上面，这两个文件只选择一个来限制而已，因此，建议你只要保留一个即可， 免得影响自己在配置上面的判断！一般来说，系统默认是保留 /etc/cron.deny ， 你可以将不想让他运行 crontab 的那个使用者写入 /etc/cron.deny 当中，一个帐号一行！

9、crontab的原理

当使用者使用crontab这个命令来创建工作排程之后，该项工作就会被纪录到/var/spool/cron/里面去了，而且是以帐号来作为判别的喔！举例来说， blue使用crontab后， 他的工作会被纪录到/var/spool/cron/blue里头去！但请注意，不要使用vi直接编辑该文件， 因为可能由於输入语法错误，会导致无法运行cron！另外， cron运行的每一项工作都会被纪录到/var/log/cron这个登录项中，所以，如果你的Linux不知道有否被植入木马时，也可以搜寻一下 /var/log/cron这个登录项！

crond服务的最低侦测限制是“分钟”，所以“ cron会每分钟去读取一次/etc/crontab与/var/spool/cron里面的数据内容 ”，因此，只要你编辑完/etc/crontab这个文件，并且将他储存之后，那么cron的配置就自动的会来运行了！

备注：在Linux底下的crontab会自动的帮我们每分钟重新读取一次/etc/crontab的例行工作事项，但是某些原因或者是其他的Unix系统中，由于crontab是读到内存当中的，所以在你修改完/etc/crontab之后，可能并不会马上运行， 这个时候请重新启动crond这个服务！“/etc/init.d/crond restart” 

>https://www.cnblogs.com/EasonJim/p/8308717.html





#卸载内核模块



List loaded kernel modules and try to unload them manually with
```
 modprobe -r <module-name> 
```
command.

Likely issue is caused by usage some modules inside the containers.

Other reason is wrong order of unloading of the modules.


#屏蔽内核模块


Use lsmod command. It shows loaded kernel modules. To prevent loading modules use blacklist.
 Just create any file with .conf extension under /etc/modprobe.d/ directory and insert into it lines with blacklist <module-name> content. 

Something uses this module. There is a --force flag, but it's very dangerous and can cause the kernel panic. Paste the output of lsmod and new information into the question. Also, flush the firewall rules before unloading.


#persistent DNS configuration


```

Either way, it's better not to write /etc/resolv.conf directly but instead properly configure /etc/sysconfig/network and/or /etc/sysconfig/network-scripts/ifup-* files.

There is another way, no need to turn off NetworkManager.service. Just set ifcfg-idevice:

PEERDNS=no
DNS1=10.165.74.2
DNS2=OTHERDNS
DOMAIN=DEMO.COM

You don't need to set /etc/NetworkManager/NetworkManager.conf settings, like:

[main]
dns=none

```


```


You can tell NetworkManager to ignore the DHCP provided DNS servers, by putting this in the network's config file (/etc/NetworkManager/system-connections/my-essid if saved to the system).

The important bit is ignore-auto-dns=true

[ipv4]
ignore-auto-dns=true
dns=8.8.8.8;
method=auto


```


```


Here is the command to modify an existing connection.

nmcli con mod <connectionName> ipv4.dns "8.8.8.8 8.8.4.4"

connectionName can be found by command: nmcli con. In the question case, it will be "System eth0"

If you want to ignore automatically configured nameservers and search domains, ie the settings passed from DHCP.

nmcli con mod <connectionName> ipv4.ignore-auto-dns yes

Finally, to enable the changes, bring the connection down then up:

nmcli con down <connectionName>
nmcli con up <connectionName>

Verify with cat /etc/resolv.conf. You should not edit /etc/resolv.conf manually as it is generated by NetworkManager service, it is likely to get overridden at any given time.
```


```


In addition to setting the ipv4.dns property described above...

To exclude the DHCP provided DNS servers...set the ipv4.ignore-auto-dns property to yes.

nmcli con mod <connectionName> ipv4.ignore-auto-dns yes

To enable the changes, bring the connection down then up:

nmcli con down <connectionName>
nmcli con up <connectionName>

Verify with cat /etc/resolv.conf

```


```


    Show available device to configure: nmcli c s

    You will need the NAME or the UUID of that list. Let's assume the connection name is eth0.

    Show the current IPv4 DNS settings of eth0: nmcli c s eth0 | grep ipv4.dns:
    Set IPv4 DNS settings to Google DNS entries 8.8.8.8 and 8.8.4.4: sudo nmcli c m eth0 ipv4.dns "8.8.8.8 8.8.4.4"
    Save changes to system files (like /etc/resolv.conf, etc.) with: sudo nmcli c up eth0

Bonus:

    Do the whole thing to a remote server over ssh: ssh remote-server 'sudo nmcli c m eth0 ipv4.dns "8.8.8.8 8.8.4.4" && sudo nmcli c up eth0'


```


```


One way to stop Network Manager from adding dns-servers to /etc/resolv.conf file is to do this:

First open the nm conf file /etc/NetworkManager/NetworkManager.conf:

sudo vim /etc/NetworkManager/NetworkManager.conf

And add this to the [main] section:

dns=none

Save and exit.

```


>https://unix.stackexchange.com/questions/163831/nameservers-erased-after-systemctl-restart-network-service
>https://unix.stackexchange.com/questions/323459/how-to-make-networkmanager-add-a-dns-server-at-the-top-of-etc-resolv-conf
>https://unix.stackexchange.com/questions/146463/specifying-dns-settings-to-override-those-of-dhcp
>https://serverfault.com/questions/810636/how-to-manage-dns-in-networkmanager-via-console-nmcli
>https://serverfault.com/questions/690559/how-do-i-force-networkmanager-to-update-etc-resolv-conf
>https://askubuntu.com/questions/623940/network-manager-how-to-stop-nm-updating-etc-resolv-conf



#DNF
DNF的主要命令
```
$ dnf --help
usage: dnf [options] COMMAND

List of Main Commands:

alias                     List or create command aliases
autoremove                remove all unneeded packages that were originally installed as dependencies
check                     check for problems in the packagedb
check-update              check for available package upgrades
clean                     remove cached data
deplist                   List package's dependencies and what packages provide them
distro-sync               synchronize installed packages to the latest available versions
downgrade                 Downgrade a package
group                     display, or use, the groups information
help                      display a helpful usage message
history                   display, or use, the transaction history
info                      display details about a package or group of packages
install                   install a package or packages on your system
list                      list a package or groups of packages
makecache                 generate the metadata cache
mark                      mark or unmark installed packages as installed by user.
module                    Interact with Modules.
provides                  find what package provides the given value
reinstall                 reinstall a package
remove                    remove a package or packages from your system
repolist                  display the configured software repositories
repoquery                 search for packages matching keyword
repository-packages       run commands on top of all packages in given repository
search                    search package details for the given string
shell                     run an interactive DNF shell
swap                      run an interactive dnf mod for remove and install one spec
updateinfo                display advisories about packages
upgrade                   upgrade a package or packages on your system
upgrade-minimal           upgrade, but only 'newest' package match which fixes a problem that affects your system
```
DNF最后的这个upgrade-minimal命令，貌似Yum没有，这个命令看着很不错。总之，能用DNS，就不要再用Yum了。



#检查服务是否启用/正在运行

不需要grep


```
At this point, we can confirm that the daemon is active by running:

$ systemctl is-active docker
active

Similarly, we can check that it is enabled at boot, by running:

$ systemctl is-enabled docker
enabled

```



#Centos list all service


使用 systemctl list-unit-files 可以查看所有服务 ，

因为用chkconfig --list命令不包含原生systemd服务

过滤查询可以systemctl list-unit-files | grep enable 过滤查看启动项如下



#Check All available shell

想知道你的系统有几种shell，可以通过以下命令查看：

cat /etc/shells
显示如下：

/bin/bash
/bin/csh
/bin/ksh
/bin/sh
/bin/tcsh
/bin/zsh



#socat

###file transfer

send node:
```
socat -u file:node4.tar.gz tcp:192.168.1.1:6666
```
receive node:
```
socat -u tcp-listen:6666 open:node4.tar.gz,create
```


If out.txt already exists, though, then this setup might behave unexpectely. If out.txt is longer than test.txt, the last part of out.txt will remain, since socat is overwriting the file byte by byte instead of making sure the file is empty. There are a few ways to fix this, depending on what you want to do:

OPEN:out.txt,creat,trunc will delete all the bytes in out.txt before writing to it. This option mimics what you'd expect from cp, and is probably what you want.
OPEN:out.txt,creat,excl will refuse to write out.txt if it already exists. Use this option for extra safety.
OPEN:out.txt,creat,append will append data to out.txt.
I also like to run md5sum on the source and destination files whenever I cobble something like this together, because of these sorts of corner cases.



---

Server sending file:
```
server$ socat -u FILE:test.dat TCP-LISTEN:9876,reuseaddr
client$ socat -u TCP:127.0.0.1:9876 OPEN:out.dat,creat
```
Server receiving file:
```
server$ socat -u TCP-LISTEN:9876,reuseaddr OPEN:out.txt,creat && cat out.txt
client$ socat -u FILE:test.txt TCP:127.0.0.1:9876
```





#yum check version


```
[root@localhost ~]# yum info nodejs
Repository AppStream is listed more than once in the configuration
Repository extras is listed more than once in the configuration
Repository PowerTools is listed more than once in the configuration
Repository centosplus is listed more than once in the configuration
Last metadata expiration check: 0:02:51 ago on Thu 14 May 2020 11:05:24 PM EDT.
Available Packages
Name         : nodejs
Epoch        : 1
Version      : 10.19.0
Release      : 2.module_el8.1.0+296+bef51246
Architecture : x86_64
Size         : 9.0 M
Source       : nodejs-10.19.0-2.module_el8.1.0+296+bef51246.src.rpm
Repository   : AppStream
Summary      : JavaScript runtime
URL          : http://nodejs.org/
License      : MIT and ASL 2.0 and ISC and BSD
Description  : Node.js is a platform built on Chrome's JavaScript runtime
             : for easily building fast, scalable network applications.
             : Node.js uses an event-driven, non-blocking I/O model that
             : makes it lightweight and efficient, perfect for data-intensive
             : real-time applications that run across distributed devices.
```
this will expose the version of the software










#centos repo redirect to aliyun
sh

```
#! /bin/bash

#获取当前系统的发行版本
VERSION=$(cat /etc/centos-release)

#提取当前系统的版本号
V_NUM=${VERSION:21:1}

BASE_REPO="/etc/yum.repos.d/CentOS-Base.repo"
ALI_REPO="http://mirrors.aliyun.com/repo/Centos-${V_NUM}.repo"

echo "备份当前软件源..."
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
echo "备份完成: /etc/yum.repos.d/CentOS-Base.repo.backup"

echo "下载阿里云镜像源..."
wget -O ${BASE_REPO} ${ALI_REPO} || curl -o ${BASE_REPO} ${ALI_REPO}

#补丁程序, 防止出现 Couldn't resolve host 'mirrors.cloud.aliyuncs.com' 信息
sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo

echo "清除缓存..."
yum clean all
echo "缓存清除成功,OK"

echo "生成缓存..."
yum makecache
echo "生成缓存成功, OK"
```

with yum upgrade

```
#! /bin/bash
# ======================================
# author: qiuyeyijian
# ======================================

#获取当前系统的发行版本
VERSION=$(cat /etc/centos-release)

#提取当前系统的版本号
V_NUM=${VERSION:21:1}

BASE_REPO="/etc/yum.repos.d/CentOS-Base.repo"
ALI_REPO="http://mirrors.aliyun.com/repo/Centos-${V_NUM}.repo"

echo "备份当前软件源..."
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
echo "备份完成: /etc/yum.repos.d/CentOS-Base.repo.backup"

echo "下载阿里云镜像源..."
wget -O ${BASE_REPO} ${ALI_REPO} || curl -o ${BASE_REPO} ${ALI_REPO}

#补丁程序, 防止出现 Couldn't resolve host 'mirrors.cloud.aliyuncs.com' 信息
sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo

echo "清除缓存..."
yum clean all
echo "缓存清除成功,OK"

echo "生成缓存..."
yum makecache
echo "生成缓存成功, OK"

echo "更新软件..."
yum update -y
echo "软件更新完毕, OK"

echo "是否需要自动删除不需要的安装包"
read -p "Enter your choice: y/n(默认y):" CHOICE
case "${CHOICE}" in
	[yY] | [yY][eE][sS])
		yum autoremove -y
		;;
	[nN] | [nN][oO])
		echo "现在你可以飞快地安装软件了:)"
		;;
	*)
		yum autoremove -y
		;;
esac
```


#file recovery after rm

###common tools

- PhotoRec
- extundelete


```fdisk -l```
```df -h```
check basic status


###ext4
1.运用系统自带工具debugfs来修复
![aaaaa]

2.打开，刚刚被删除文件所在的分区
![bbbbb]

![6612E195-E768-A692-5B7F-55E99A50C677]


3.用ls 加-d参数显示刚刚删除文件所在的目录

![22BF2B48-285F-65D6-1982-1D7720ED6E01]

4.显示有<>尖括号的就是我们要找的文件Inode 号 执行logdump –I  <19662057>

![681964AE-0201-02F5-1F55-EA94631521A4]

5.执行完命令后，显示了一屏信息，我们需要的是下面这一行，并且要记住，后面的值

![B708C09A-A56E-5D26-BCC6-CD68FA0142A8]

6.退出dedugfs
```
q
```

7.bs与下图offset值一致，skip与block值一致

![A62C4080-39FA-1592-2D22-65E7AF25A7F6]

![A1B97A68-F8E4-6D27-97E5-7429DFCA8CCF]

8.以上结果表示恢复成功我们看下/opt/wb_1目录下到底有没有
![E1E22049-92C1-E495-7817-758B542437B1]


###extundelete

## 一、查看所删除的文件或者文件夹inode

    ls -id /
    df -l

如上述为查看根目录的inode值，并且查看磁盘分别挂载在哪个区下和挂载的磁盘名，因为接下来会用到

## 二、将磁盘卸载，即（umount）

    umount /data

这边假设文件或者文件夹所在的磁盘挂在在 _/data_ 之下

## 三、安装extundelete

    wget http://nchc.dl.sourceforge.net/project/extundelete/extundelete/0.2.4/extundelete-0.2.4.tar.bz2
    tar jxvf extundelete-0.2.4.tar.bz2
    cd extundelete-0.2.4
    ./configure
    make
    make install

到此安装成功

    extundelete --help

可以通过这个命令查看帮助

## 恢复单个文件

    extundelete /dev/sdc1 --inode 2

通过此命令查看哪些是已经删除的文件和文件夹，后面的Deleted status显示的内容即是删除的内容,_/dev/sdc1_为所在的磁盘名  
 extundelete /dev/sdc1 --restore-file filename  
 通过上述命令恢复文件

## 恢复文件夹

    extundelete /dev/sdc1 --restore-derectory /directoryname

通过上述恢复文件夹下的所有内容，不过效果不理想，因为很多子文件夹的名字都找不回来了，还出现了文件错乱的结果

## 恢复所有删除的数据

    extundelete /dev/sdc1 --restore-all

上句恢复所有的内容，谨慎使用

## 恢复某个时间段内的误删数据

    extundelete --after 1379244444 --restore-all 

可以用_date +%s_显示当前的时间值，该时间值是秒数，在1970-01-01 00:00:00 UTC时间之后算起的。若想恢复三个钟头之内的数据，可以通过_date +%s_来得到当前秒数然后减去3*60*60=10800来获取大概删除前的时间，上句1379244444为秒数。



##xfs

I deleted a python file that I knew had a specific fairly unique string in it. So I did the following:

    $ sudo strings -td /dev/mapper/vg01-lv_opt | grep "class Team("
    8648365228 class Team(object):
    26133651864 class Team(Account):
    26134147482 class Team(Account):

I now had the offsets in the lvol where that string was. I then did a dd around that area to recover the text:

    sudo dd if=/dev/mapper/vg01-lv_opt of=/tmp/recover skip=26134140000 count=1M bs=1

...then I brought that smaller file into vi and trimmed around the file, and voila! I had my content back.


---

https://github.com/ianka/xfs_undelete

---

https://xfs.org/index.php/XFS_FAQ#Q:_Does_the_filesystem_have_an_undelete_capability.3F


***issue when you use extundelete***

```
Configuring extundelete 0.2.4
configure: error: Can't find ext2fs library
```
1. 则使用yum -y install e2fsprogs e2fsprogs-devel来解决。

compile err
```
*** inode.i_dir_acl error ***
```
save below file as pappp.txt
```
--- a/src/insertionops.cc	2012-12-30 18:23:32.000000000 +0100
+++ b/src/insertionops.cc	2018-05-07 22:58:13.065868723 +0200
@@ -33,7 +33,7 @@
   os << "File flags: " << inode.i_flags << std::endl;
   os << "File version (for NFS): " << inode.i_generation << std::endl;
   os << "File ACL: " << inode.i_file_acl << std::endl;
-  os << "Directory ACL: " << inode.i_dir_acl << std::endl;
+  os << "Directory ACL: " << inode.i_size_high << std::endl;
   os << "Fragment address: " << inode.i_faddr << std::endl;
   os << "Direct blocks: ";
   for (int n = 0; n < EXT2_NDIR_BLOCKS; n++)

```

2. then open source dir,``patch -p1 < /path/to/pappp.txt``,then compile again(you may need yum install patch too)

>https://sourceforge.net/p/extundelete/tickets/5/

###extend
>https://serverfault.com/questions/913016/linux-recover-data-from-xfs
>https://serverfault.com/questions/697189/data-recovery-for-xfs-partition
>https://serverfault.com/questions/777299/proper-way-to-deal-with-corrupt-xfs-filesystems
>https://www.jianshu.com/p/662293f12a47




#添加开机启动项

###ubuntu
/etc/rc.local

from ubuntu 6.10,默认使用dash而不是bash，但Login Shell还是bash。于是查看了一下，发现 /bin/sh 链接到了 /bin/dash/ ,而rc.local脚本使用的正是/bin/sh，故脚本没有执行。/etc/init.d/rc.local执行了/etc/rc.local.

1.create /etc/rc.local if it doesn't exits,then chmod +x to give it execute auth.
2.using bash as interpreter,so the first line start with

```
#!/bin/bash
```

>方法一：将rc.local中的命令改成更加兼容的模式，将"#!/bin/sh"改为"#!/bin/bash"
>方法二(不推荐)：将/bin/sh重新链接到/bin/bash，有两种方法：
>1. 执行命令：
>sudo dpkg-reconfigure dash选择no
>3. 重新进行软连接，执行命令：
>sudo rm /bin/sh sudo ln -s /bin/bash /bin/sh




#centos cannot get dhcp
nmtui try to manaually start network showed below err:
```
the base network connection was interrupted
```
still doesn't get some good solution,the current way to temp fix it was by running 'dhclient' manually if you are under dhcp network now.

###some thing may useful here.
Try:
```
nmcli dev status
```
>type “nmcli d” command in your terminal for quick list ethernet card installed on your machine
Get you connection's device name and then try
```
nmcli con show <your device> | grep auto
```
to get the network card which start automatically.
```
[root@localhost network-scripts]# dhclient -v
Internet Systems Consortium DHCP Client 4.2.5 Copyright 2004-2013
Internet Systems Consortium. All rights reserved. For info, please visit https://www.isc.org/software/dhcp/ Listening on LPF/ens32/00:0c:29:68:22:e2
Sending on   LPF/ens32/00:0c:29:68:22:e2
Sending on   Socket/fallback DHCPDISCOVER on ens32 to 255.255.255.255 port 67 interval 4 (xid=0x433a9e33) DHCPREQUEST on ens32 to 255.255.255.255 port 67 (xid=0x433a9e33)
DHCPOFFER from 172.16.179.254 DHCPACK from 172.16.179.254 (xid=0x433a9e33) bound to 172.16.179.136 -- renewal in 822 seconds.
[root@localhost network-scripts]#
```

I also encountered similar problems. To modify the configuration files.(ifcfg-ensxxxx), as follows [enter image description here](https://i.stack.imgur.com/Ot6U3.png)

- List Commented out "UUID=xxxx-xxxx"
- Add new option: `NM_CONTROLLED=no`, it indicates that this interface will be set up using this configuration file, instead of being managed by Network Manager service.


Had this error after cloning a virtual server, the clone was given a new hardware address ( MAC ) and the network adapter config still had the old one.

The line looks like: HWADDR=00:00:00:00:00:00


---

[](https://serverfault.com/posts/764398/timeline "Timeline")

I too faced this today on a CentOS 7.2 cloned virtual machine. This is how I fixed it.

```
systemctl disable NetworkManager
systemctl enable network
```

Find MAC address of the interface through command `/sbin/ifconfig -a` and append that in `/etc/sysconfig/network-scripts/ifcfg-<interface_name>`. You can use the below commands for the first interface.

```
nic_file=`ls /etc/sysconfig/network-scripts/ifcfg-e*`
ifconfig -a | grep ether | awk '{ print $2 }' | sed 's/.*/HWADDR=&/' >> ${nic_file}
```

Then fire `reboot` to restart the server


Look at your gateway settings to confirm your gateways are set properly and things are what they need to be with `/etc/syscofig/network` and each of the `/etc/sysconfig/network-scripts/ifcfg-*` look for duplicate IP, routes set via `/etc/sysconfig/network-scripts/route-*` if memory serves me right gateways now can be set in both `ifcfg-*` and `route-*` files. So confirm there's no duplication or overlap.


#700 777 chmod file privilege


```php
    -rw------- (600)      只有拥有者有读写权限。
    -rw-r--r-- (644)      只有拥有者有读写权限；而属组用户和其他用户只有读权限。
    -rwx------ (700)     只有拥有者有读、写、执行权限。
    -rwxr-xr-x (755)    拥有者有读、写、执行权限；而属组用户和其他用户只有读、执行权限。
    -rwx--x--x (711)    拥有者有读、写、执行权限；而属组用户和其他用户只有执行权限。
    -rw-rw-rw- (666)   所有用户都有文件读、写权限。
    -rwxrwxrwx (777)  所有用户都有读、写、执行权限。
```

可以使用 chmod 命令来对目录设置权bai限：
chmod 可以用3个du数字来表达 用户，用户组，其zhi他用户：
如 chmod 777 /test （数字的第一个7代表的是用户权限
数字的第二个7代表的是用户组的权限
数字的第三个7代表的是其他用户的权限）
而数字7是特定用户的 读，写 ， 执行 权限：
[读取--用数字 4 表示]
[写--用数字 2 表示]
[执行--用数字 1 表示]
用这些数字相加得到权限：
如你想设置/test目录的权限为：
对用户可读可写, 4(读取）+ 2 （写入） = 6
对用户组可读可执行, 4（读取） + 1 （执行） = 5
对其他用户仅仅可读; 4（读取）
这样就可以用命dao令： chmod 654 /test 来设置权限～


**后九位解析：** 我们知道Linux权限总共有三个属组，这里我们给每个属组使用三个位置来定义三种操作（读、写、执行）权限，合起来则是权限的后九位。 上面我们用字符表示权限，其中 -代表无权限，r代表读权限，w代表写权限，x代表执行权限。

实际上，后九位每个位置的意义（代表某个属组的某个权限）都是固定的，如果我们将各个位置权限的有无用二进制数 1和 0来代替，则只读、只写、只执行权限，可以用三位二进制数表示为

```php
    r-- = 100
    -w- = 010
    --x = 001
    --- = 000
```

转换成八进制数，则为 r=4, w=2, x=1, -=0（这也就是用数字设置权限时为何是4代表读，2代表写，1代表执行）

实际上，我们可以将所有的权限用二进制形式表现出来，并进一步转变成八进制数字：

```php
    rwx = 111 = 7
    rw- = 110 = 6
    r-x = 101 = 5
    r-- = 100 = 4
    -wx = 011 = 3
    -w- = 010 = 2
    --x = 001 = 1
    --- = 000 = 0
```

由上可以得出，每个属组的所有的权限都可以用一位八进制数表示，每个数字都代表了不同的权限（权值）。如 最高的权限为是7，代表可读，可写，可执行。

故 如果我们将每个属组的权限都用八进制数表示，则文件的权限可以表示为三位八进制数

```php
    -rw------- =  600
    -rw-rw-rw- =  666
    -rwxrwxrwx = 777
```

**关于第一位最高位的解释：** 上面我们说到了权限表示中后九位的含义，剩下的第一位代表的是文件的类型，类型可以是下面几个中的一个：

```php
    d代表的是目录(directroy)    -代表的是文件(regular file)    s代表的是套字文件(socket)    p代表的管道文件(pipe)或命名管道文件(named pipe)    l代表的是符号链接文件(symbolic link)    b代表的是该文件是面向块的设备文件(block-oriented device file)    c代表的是该文件是面向字符的设备文件(charcter-oriented device file)
```

#fs-max、file-nr和nofile的关系

## 1\. file-max

/proc/sys/fs/file-max:  
这个文件决定了系统级别所有进程可以打开的文件描述符的数量限制，如果内核中遇到`VFS: file-max limit <number> reached`的信息，那么就提高这个值。  
设置方式：

```auto
# /etc/sysctl.conf
fs.file-max = 6553500
```

```bash
sysctl -p
```

## 2\. file-nr

这个是一个状态指示的文件，一共三个值，第一个代表全局已经分配的文件描述符数量，第二个代表自由的文件描述符（待重新分配的），第三个代表总的文件描述符的数量。

```bash
cat /proc/sys/fs/file-nr
```

## 3\. nofile

nofile全称`number of open files`，最大可打开的文件描述符数量，这个限制是针对用户和进程来说的。

### 3.1. 全局修改，永久生效，需要重启

```auto
# /etc/security/limits.conf
* soft nofile 65535
* hard nofile 65535
```

**注意:**对于ubuntu系统，还需要加载相应的pam模块才能生效

```auto
# /etc/pam.d/login
# Sets up user limits according to /etc/security/limits.conf
# (Replaces the use of /etc/limits in old login)
session    required   pam_limits.so
```

### 3.2. 临时调整

```bash
ulimit -HSn 655350
```


check dmesg /var/log/syslog /var/log/message



#grep转义

空格和[都需要转义
比如想要匹配``[Priority: 4]``需要转义为以下

```
cat fast.log | grep "\[Priority:\ 4\]"
```


#linux内核抢占
可以开启部分标记位使程序拥有更好的响应，低延时。


#ubuntu用户默认属组

 ubuntu安装好之后，用户默认属于哪几个用户组？
```
adm
cdrom
sudo
dip
plugdev
lpadmin
sambashare
```



#shadow文件第二行含义

shadow文件中第二列的格式，它是加密后的bai密码，它有du些玄机，不同的特zhi殊字符表示特殊的意义：
①.该列留空，即"::"，表示该用户没有密码。
②.该列为"!"，即":!:"，表示该用户被锁，被锁将无法登陆，但是可能其他的登录方式是不受限制的，如ssh公钥认证的方式，su的方式。
③.该列为"*"，即":*:"，也表示该用户被锁，和"!"效果是一样的。
④.该列以"!"或"!!"开头，则也表示该用户被锁。
⑤.该列为"!!"，即":!!:"，表示该用户从来没设置过密码。
⑥.如果格式为"$id$salt$hashed"，则表示该用户密码正常。其中$id$的id表示密码的加密算法，$1$表示使用MD5算法，$2a$表示使用Blowfish算法，"$2y$"是另一算法长度的Blowfish,"$5$"表示SHA-256算法，而"$6$"表示SHA-512算法，目前基本上都使用sha-512算法的，但无论是md5还是sha-256都仍然支持。$salt$是加密时使用的salt，hashed才是真正的密码部分。


#用户组操作_group action

delete a user from a group

```
# gpasswd -d user group
```
On Debian, the adduser package contains a deluser program which removes a user from a group if you pass both as arguments:
```
deluser user group
```
and:
```
usermod -G "" username

```
removes all secondary/supplementary groups from username, leaving them as a member of only their primary group. this worked in Solaris 5.9



add new group

```
groupadd mynewgroup
```
Add an Existing User Account to a Group
```
usermod -a -G examplegroup exampleusername
```

Change a User’s Primary Group
```
usermod -g groupname username
```
View the Groups a User Account is Assigned To

```
groups
```
To view the numerical IDs associated with each group, run the id  command instead:
```
id
```
To view the groups another user account is assigned to, run the groups command and specfy the name of the user account.
```
groups exampleusername
```

You can also view the numerical IDs associated with each group by running the id command and specifying a username.
```
id exampleusername
```

Create a New User and Assign a Group in One Command
```
useradd -G examplegroup exampleusername
```

Add a User to Multiple Groups
While assigning the secondary groups to a user account, you can easily assign multiple groups at once by separating the list with a comma.
```
usermod -a -G group1,group2,group3 exampleusername
```
For example, to add the user named geek to the ftp, sudo, and example groups, you’d run:
```
usermod -a -G ftp,sudo,example geek
```

View All Groups on the System
```
getent group
```
or
```
cat /etc/group
```


#clear all pyc file(python cache) in target dir
```
find /tmp -name "*.pyc" | xargs rm -rf
```



#allow port below 1024 to used by nonroot user
>允许非root用户使用低端口

1.程序root运行
2.If your server executable is stored on a filesystem that supports capabilities, you can give it the [`cap_net_bind_service`](http://www.lids.org/lids-howto/node46.html) [capability](http://www.friedhoff.org/posixfilecaps.html). Beware that capabilities are still relatively new and [still have a few kinks](http://www.sevagas.com/?POSIX-file-capabilities-the-dark).

```
setcap cap_net_bind_service=ep /path/to/server/executable
```

```
setcap 'cap_net_bind_service=+ep' /path/to/program
```

>https://unix.stackexchange.com/questions/10735/allowing-a-user-to-let-listen-to-a-port-below-1024



#开机自动挂载分区fstab


    自动挂载可以有两种方式。

1.修改/etc/fstab文件

  用vim打开 fstab文件，可以看到文件格式如下：
```
#  
# /etc/fstab  
# Created by anaconda on Tue Oct 20 11:50:19 2015  
#  
# Accessible filesystems, by reference, are maintained under '/dev/disk'  
# See man pages fstab(5), findfs(8), mount(8) and/or blkid(8) for more info  
#  
/dev/mapper/vg_minimal-LogVol00 /                       ext4    defaults        1 1  
UUID=f8066e4a-e1df-4815-9d6e-c74a6811ba5e /boot                   ext4    defaults        1 2  
tmpfs                   /dev/shm                tmpfs   defaults        0 0  
devpts                  /dev/pts                devpts  gid=5,mode=620  0 0  
sysfs                   /sys                    sysfs   defaults        0 0  
proc                    /proc                   proc    defaults        0 0  
```
可以很明显的看到文件有6列。
第1列是设备名或者卷标

第2列是挂载点（也就是挂载目录）

第3列是所要挂载设备的文件系统或者文件系统类型

第4列是挂载选项，通常使用defaults就可以

第5列设置是否使用dump备份，置0为不备份，置1，2为备份，但2的备份重要性比1小

第6列设置是否开机的时候使用fsck检验所挂载的磁盘，置0为不检验，置1，2为检验，但置2盘比置1的盘晚检验。


本文需将逻辑卷lv_study挂载到 /lvm_study ，文件系统为ext4 格式，不进行dump备份以及开机磁盘检查。
```
/dev/mapper/vg_study-lv_study  /lvm_study        ext4    defaults        0 0  
```
将上面的命令添加进fstab后，为了避免可能的错误，我们可以使用mount -a 命令来检验编辑的内容是否有错。执行mount -a命令后，用df -h查看会发现磁盘已经挂载成功，说明输入没有错误。下次重启的时候系统就可以自动进行挂载了。

注意：
（1）根目录必须优先于其他挂载点

（2）挂载点必须为已经存在的目录

（3）卸载时必须保证当前磁盘没有发生读写操作

2.修改/etc/rc.d/rc.local文件

  将mount命令添加进rc.local文件是另一种实现开机自动挂载的方式
```
sudo mount -t vboxsf uBuntuSharePath /pcshare

mount /dev/mapper/vg_study-lv_study /lvm_study  
```


#Gre隧道Linux配置命令
>https://blog.csdn.net/sinat_20184565/article/details/83280247

需要两台设备（Ubuntu操作系统）进行配置和测试。一台设备A的IP地址为192.168.1.113，另外一台设备B的IP地址为192.168.1.123。首先在设备A上配置GRE隧道，如下命令：

```
$ sudo ip tunnel add gre01 mode gre remote 192.168.1.123 local 192.168.1.113 ttl 255
$ sudo ip link set gre01 up
$ sudo ip addr add 10.1.1.1/24 dev gre01
```
以上ip tunnel命令创建gre01隧道设备，其本地地址为192.168.1.113，源端地址为192.168.1.123，发送数据包时添加的外层IP报头的源IP和目的IP地址将使用这两个地址，ttl字段使用255。隧道设备gre01自身的IP地址配置为10.1.1.1，掩码255.255.255.0。使用IP命令检查gre01接口的配置：
```
$ sudo ip addr list dev gre01
7: gre01@NONE: <POINTOPOINT,NOARP,UP,LOWER_UP> mtu 1476 qdisc noqueue state UNKNOWN group default qlen 1000
    link/gre 192.168.1.113 peer 192.168.1.123
    inet 10.1.1.1/24 scope global gre01
```
设备B的配置命令如下：
```
$ sudo ip tunnel add gre01 mode gre remote 192.168.1.113 local 192.168.1.123 ttl 255
$ sudo ip link set gre01 up
$ sudo ip addr add 10.1.1.2/24 dev gre01
```
至此，GRE隧道配置完成，使用ping命令测试连通性，在设备A上ping：
```
$ ping 10.10.10.2
PING 10.10.10.2 (10.10.10.2) 56(84) bytes of data.
64 bytes from 10.10.10.2: icmp_req=1 ttl=64 time=0.619 ms
```


最后，使用如下命令删除GRE隧道设备：
```
$ sudo ip link set gre0 down
$ sudo ip tunnel del gre0
```

Generic Routing Encapsulation (GRE)通用路由封装协议，基于IP网络层协议封装以太网报文，可用于在IPSec VPN网络间传输多播路由信息报文，或者在PPTP协议中，承载PPP数据报文。其在数据帧中的位置如下：
```

    |-------------------|----------------|----------------------|------------------|
    |  Outer IP Header  |   GRE Header   |    Inner IP Header   |    Payload       |
    |-------------------|----------------|----------------------|------------------|
```
GRE头部最小4个字节长度，其后为可选字段，第一个字节中的标志位决定是否存在后面的字段，最大长度为16个字节。以下为一个标准的GRE报头格式：

```
    |------------------------------------------------------------------------------|
    |    bit0-3   |      4-12      | 13-15 |                16 - 31                |
    |-------------|----------------|-------|---------------------------------------|
    | C | | K | S |    Reserved0   |  Ver  |            Protocol Type              |
    |--------------------------------------|---------------------------------------|
    |           Checksum(optional)         |         Reserved1(optional)           |         
    |------------------------------------------------------------------------------|
    |                                 Key(optional)                                |
    |------------------------------------------------------------------------------|
    |                           Sequence Number (optional)                         |
    |------------------------------------------------------------------------------|
```

C, K, and S      : 分别对应Checksum、Key和Sequence Number字段，置1表示存在相应的字段，否则无此字段。
Ver                   : GRE版本号（为0），对于PPTP GRE，此字段为1。
Protocol Type  : 封装的以太网协议类型（例如IPv4，此处为0x0800）
Checksum       : 如果C位为1，此字段包含由GRE头部开始的所有数据的校验和
Key                  : 如果K位为1，此字段包含秘钥信息
Sequence Number: 如果S位为1，此字段包含GRE数据包的序号




#paping port speed test TCP端口测速

```
wget https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/paping/paping_1.5.5_x86-64_linux.tar.gz
tar zxvf paping_1.5.5_x86-64_linux.tar.gz
```
### 使用方法

- \-p : --port N 指定被测试服务的 TCP 端口（必须）；
- \--nocolor : 屏蔽彩色输出；
- \-t : --timeout 指定超时时长，单位为毫秒，默认值为 1000；
- \-c : --count N 指定测试次数。

比如我要测试`www.moderatecontent.com` 443端口延迟情况，输入命令：

````
[root@ultravps ~]# ./paping -p 443 -c 4 www.moderatecontent.com
paping v1.5.5 - Copyright (c) 2011 Mike Lovell

Connecting to www.moderatecontent.com on TCP 443:

Connected to 66.96.149.31: time=73.29ms protocol=TCP port=443
Connected to 66.96.149.31: time=73.36ms protocol=TCP port=443
Connected to 66.96.149.31: time=73.57ms protocol=TCP port=443
Connected to 66.96.149.31: time=73.32ms protocol=TCP port=443

Connection statistics:
    Attempted = 4, Connected = 4, Failed = 0 (0.00%)
Approximate connection times:
    Minimum = 73.29ms, Maximum = 73.57ms, Average = 73.38ms
```


#Centos7 BBR
1.yum更新系统版本：
```
yum update
```
2、安装elrepo并升级内核：
```
rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm
yum --enablerepo=elrepo-kernel install kernel-ml -y
```
3.更新grub文件并重启系统：
```
[root@amber ~]# egrep ^menuentry /etc/grub2.cfg | cut -f 2 -d \'
CentOS Linux (4.18.5-1.el7.elrepo.x86_64) 7 (Core)
CentOS Linux (3.10.0-862.11.6.el7.x86_64) 7 (Core)
CentOS Linux (3.10.0-693.2.2.el7.x86_64) 7 (Core)
CentOS Linux (3.10.0-693.el7.x86_64) 7 (Core)
CentOS Linux (0-rescue-f0f31005fb5a436d88e3c6cbf54e25aa) 7 (Core)
[root@amber ~]# grub2-set-default 0
[root@amber ~]# reboot
```
4.重启完成后查看内核是否已更换为4.18版本：
```
[root@amber ~]# uname -r
4.18.5-1.el7.elrepo.x86_64
```
5.开启bbr:
```
vim /etc/sysctl.conf 
# 在文件末尾添加如下内容
net.core.default_qdisc = fq
net.ipv4.tcp_congestion_control = bbr
```
6.加载系统参数:
```
sysctl -p
```
7.确定bbr已经成功开启：
```
[root@amber ~]# sysctl net.ipv4.tcp_available_congestion_control
net.ipv4.tcp_available_congestion_control = reno cubic bbr
[root@amber ~]# lsmod | grep bbr
tcp_bbr                20480  1 
```

#linux端口号保留名单
cat /etc/bindresvport.blacklist
```
#
# This file contains a list of port numbers between 600 and 1024,
# which should not be used by bindresvport. bindresvport is mostly
# called by RPC services. This mostly solves the problem, that a
# RPC service uses a well known port of another service.
#
631 # cups
636 # ldaps
655 # tinc
774 # rpasswd
783 # spamd
873 # rsync
921 # lwresd
993 # imaps
995 # pops
```


#删除XX天之前的文件delete files before x days
```
# cd /etc/cron.daily
 
# vi logcron
 
输入如下内容
 
#!/bin/sh
 
find /logs -type f -ctime +30 | xargs rm -rf （这里实现了删除30天之前文件的命令）
 
然后保存该文件，最后执行如下命令给该文件服务可执行权限
 
# chmod +x /etc/cron.daily/logcron
```

another way

```
find /home/lifeccp/dicom/studies -mtime +21 -name "*.*" -exec rm -Rf {} \;
```
可以替换*.*为*.log来只删除log文件。


#查看当前目录大小
```
cd /targetdir
du -sh
```
#查看统计目录内部文件数
```
# 查看当前目录下的文件数量（包含子目录中的文件） 注意：R，代表子目录

ls -lR|grep "^-"| wc -l
```

```
# 查看当前目录下的文件数量（不包含子目录中的文件）

ls -l|grep "^-"| wc -l
```
```
# 查看当前目录下的文件夹目录个数（不包含子目录中的目录），同上述理，如果需要查看子目录的，加上R

ls -l|grep "^d"| wc -l
# 查询当前路径下的指定前缀名的目录下的所有文件数量
# 例如：统计所有以“20161124”开头的目录下的全部文件数量

ls -lR 20161124*/|grep "^-"| wc -l
对每个命令参数做一下说明备注：
ls -l
该命令表示以长列表输出指定目录下的信息（未指定则表示当前目录），R代表子目录中的“文件”，这个“文件”指的是目录、链接、设备文件等的总称

grep "^d"表示目录，"^-"表示文件

wc -l
表示统计输出信息的行数，因为经过前面的过滤已经只剩下普通文件，一个目录或文件对应一行，所以统计的信息的行数也就是目录或文件的个数
```


#shell中各种引号作用
单引号
目的: 为了保护文字不被转换.除了他本身. 就是说除去单引号外, 在单引号内的所有文字都是原样输出.

双引号
目的: 为了包含文字或者函数段. 除了本身,反引号内的函数,$开头的变量和\开头反转换的字符外, 其余都是直接输出.

反单引号
函数转换. 但单引号内其无作用.
比如a=`date` 就是把date当命令来执行 然后把结果付给a

$()  反单引号和这个的作用一样



#如何查看crontab的日志记录

1.  linux
看 /var/log/cron这个文件就可以，可以用tail -f /var/log/cron观察(不能用cat查看)
 
2.  unix
在 /var/spool/cron/tmp文件中，有croutXXX001864的tmp文件，tail 这些文件就可以看到正在执行的任务了。
 
3. mail任务
在 /var/spool/mail/root 文件中，有crontab执行日志的记录，用tail -f /var/spool/mail/root 即可查看最近的crontab执行情况。



#grep出带-的行

比如需要查找```-bash```文件（注意不是```bash```）
```
ls -la | grep -- "-bash"
```
```
wget --help | grep -- "-c"
```


#lsof查看端口占用状态

```
lthpc@lthpc:~$ lsof -i:10000
COMMAND   PID  USER   FD   TYPE   DEVICE SIZE/OFF NODE NAME
php     40446 lthpc    3u  IPv4 37381218      0t0  UDP *:10000 
```

#systemd服务|自己编写服务


>https://www.cnblogs.com/nxzblogs/p/11755972.html
>https://www.freedesktop.org/software/systemd/man/systemd.service.html
>https://www.jianshu.com/p/c6c8b55e9a9a



如果服务名是rot13那么文件名就叫rot13.service,如果有一个参数那么叫rot@.service.




写一个服务配置文件/etc/systemd/system/rot13.service
```
[Unit]
Description=ROT13 demo service
After=network.target
StartLimitIntervalSec=0
 
[Service]
Type=simple
Restart=always
RestartSec=1
User=ltpc
ExecStart=/usr/bin/env php /path/to/server.php
 
[Install]
WantedBy=multi-user.target
```
>一些文档提到新系统最好放在/var/lib/systemd/system/下而不是/etc/systemd/system/

有几点需要注意，为了使服务能够自动无限次重启，需要增加以下几个配置
```
StartLimitIntervalSec=0

Restart=always

RestartSec=1
```



---
带有restart和reload功能的服务实例

```
vim /lib/systemd/system/website.service

[Unit]
Description=website
After=network.target
 
[Service]
Type=forking
ExecStart=/home/monitor/website/start.sh
ExecReload=/home/monitor/website/restart.sh
ExecStop=/home/monitor/website/shutdown.sh
 
[Install]
WantedBy=multi-user.target
```

---
其他例子


```
[Unit]
Description=Job that runs your user script

[Service]
ExecStart=/some/command
Type=oneshot
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

带有接受配置参数命令的服务如frpc@.service

```
[Unit]
Description=Frp Client Service
After=network.target

[Service]
Type=idle
User=nobody
Restart=on-failure
RestartSec=5s
ExecStart=/usr/bin/frpc -c /etc/frp/%i.ini
ExecReload=/usr/bin/frpc reload -c /etc/frp/%i.ini

[Install]
WantedBy=multi-user.target
```
其中，%i是传入的参数


#常见systemctl错误码

|code	|desc|
|--|--|
|0|	命令成功结束|
|1|	通用未知错误|
|2|	误用shell命令|
|126|	命令不可执行|
|127|	没找到命令|
|128|无效退出参数|
|128+x|	Linux 信号x的严重错误|
|130|	Linux 信号2 的严重错误，即命令通过SIGINT（Ctrl＋Ｃ）终止|
|203|	缺失脚本执行器标识|
|255|	退出状态码越界|



#linux策略路由


简单路由

假设机器上外网出口网卡eth1,ip 192.168.1.10，网关192.168.1.1
那么希望与8.8.8.8的通信全部走192.168.1.10
则添加路由
```
ip route add 8.8.8.8 via 192.168.1.1
```
---
添加优先级的路由，比如系统存在多个网卡，希望外网走一块网卡(如vpn虚拟网卡，内网仍然走内网各网卡)，则需要通过metric控制，linux中路由表内路由记录的metric越小越优先，默认为0，如果同时存在目的地址相同的metric分别为50和100的路由记录，则优先走50.

如，当前存在一块eth1和vpn虚拟网卡

- eth1
  ip段为192.168.3.0,ip为192.168.3.10，网关为192.168.3.1，
- vpn
  ip段为10.88.88.0，ip为10.88.88.10，网关为10.88.88.1，为虚拟网卡，链路建立在物理网卡eth1之上.VPN对端IP 9.9.9.9


此时希望192.168.3.0和192.168.0.0/16的访问全部通过eth1，而0.0.0.0/1公网流量全部通过vpn网卡走出，则需要

1.手动增加vpn对端目的IP(公网)，网关为192.168.3.1,目的是保证跟对端VPN的联通(否则0.0.0.0/1路由添加后VPN隧道的对端也会走VPN，由于VPN底层连接走eth1出去，由VPN网卡出去会导致已建立的VPN断线)
```
ip route add 高防目的IP via 192.168.3.1
```
vpn连通后，删除默认添加的全局路由(不一定有)
```
ip route del default via 10.88.88.1
```
同时需要增加带有策略控制的default路由
```
ip route add default via 10.88.88.1 metric 1
```
确保你的route表看起来像如下

```
root@self3:/home/tianru# ip route
default via 10.88.88.1 dev vpn metric 1
10.88.88.0/24 dev vpn proto kernel scope link src 10.88.88.10
9.9.9.9 via 192.168.3.1 dev eth1
172.17.0.0/16 dev docker0 proto kernel scope link src 172.17.0.1
192.168.0.0/16 via 192.168.3.1 dev eth1
192.168.3.0/24 dev eth1 proto kernel scope link src 192.168.3.31
root@self3:/home/tianru#
root@self3:/home/tianru# route -n
Kernel IP routing table
Destination Gateway Genmask Flags Metric Ref Use Iface
0.0.0.0 10.88.88.1 0.0.0.0 UG 1 0 0 vpn
10.88.88.0 0.0.0.0 255.255.255.0 U 0 0 0 vpn
9.9.9.9 192.168.3.1 255.255.255.255 UGH 0 0 0 eth1
172.17.0.0 0.0.0.0 255.255.0.0 U 0 0 0 docker0
192.168.0.0 192.168.3.1 255.255.0.0 UG 0 0 0 eth1
192.168.3.0 0.0.0.0 255.255.255.0 U 0 0 0 eth1
root@self3:/home/tianru#

```


---

### 问题简述

一般地说，在Linux系统路由表内只能有一条默认路由。当出站数据包根据目的IP地址选路失败后，执行默认路由，交默认路由指向的下一跳路由器（默认网关）转发数据包。

现需要同时存在两条默认路由。数据包通过何种默认路由，由程序指定（或根据规则）。数据包通过特定的路由规则转发到对应的路由器。

在下文中，我们以如下的拓扑为例，介绍如何通过策略路由来实现上述需求。

*   服务器上安装有两块网卡，分别为p7p1和p7p2；

*   网卡p7p1：192.168.1.1/24，连接至路由器R1；

*   网卡p7p2：192.168.2.1/24，连接至路由器R2；

*   路由器R1：192.168.1.254/24；

*   路由器R2：192.168.2.254/24。

我们要实现的选路策略：

*   根据源IP地址选路，所有源IP地址为192.168.1.1的报文，通过eth0转发到路由器R1，所有源IP地址为192.168.2.1的报文，通过eth0转发到路由器R2；

*   进一步地，本机程序发送IP报文，由程序选择从何出口转发到对应的路由器。

### 实现思路

通过多张路由表和策略路由实现上述的配置需求。

*   路由表1：默认路由指向R1，即192.168.1.254；

*   路由表2：默认路由指向R2，即192.168.2.254

*   策略路由，优先级高于local路由表：

    *   源IP为192.168.1.1的报文，执行路由表1；

    *   源IP为192.168.2.1的报文，执行路由表2；

### 路由表配置

1.创建路由表

    # echo "10 eth1table" >> /etc/iproute2/rt_tables
    # echo "20 eth2table" >> /etc/iproute2/rt_tables

2.配置路由表，添加默认路由

    # 本机与默认网关的路由，否则会显示路由不可达
    # ip route add 192.168.1.0/24 dev eth1 table eth1table
    # ip route add 192.168.2.0/24 dev eth2 table eth2table
    # 默认网关
    # ip route add default via 192.168.1.254 table eth1table
    # ip route add default via 192.168.2.254 table eth2table

3.配置策略路由

    # ip rule add from 192.168.1.1/32 table eth1table
    # ip rule add from 192.168.2.1/32 table eth2table

### 测试

为了方便，通过静态ARP配置，模拟下一跳路由器。

    # arp -s 192.168.1.254 aa:bb:cc:dd:ee:ff
    # arp -s 192.168.2.254 11:22:33:44:55:66

利用NC工具发送UDP报文，设置源IP地址为192.168.1.1，即

    # nc -s 192.168.1.1 -u 202.202.202.202

在网卡p7p1上运行tcpdump命令捕包。

    # tcpdump -i p7p1 -e

结果：

    15:39:36.225020 e8:61:1f:18:ef:24 (oui Unknown) > aa:bb:cc:dd:ee:ff (oui Unknown), ethertype IPv4 (0x0800),
    length 51: 192.168.1.1.46399 > 202.202.202.202.31337: UDP, length 9

类似地，设置源IP地址为192.168.2.1，通过tcpdump在p7p2上捕获得

    15:42:11.157252 e8:61:1f:18:ef:25 (oui Unknown) > 11:22:33:44:55:66 (oui Unknown), ethertype IPv4 (0x0800), 
    length 48: 192.168.2.1.39107 > 202.202.202.202.31337: UDP, length 6

### 参考文献
https://networkengineering.stackexchange.com/questions/12414/routing-to-remote-host-via-two-separate-local-gateways



###Linux路由表部分补充

Linux最多可以支持255张路由表，其中有3张表是内置的：

　　表255 本地路由表（Local table）本地接口地址，广播地址，已及NAT地址都放在这个表。该路由表由系统自动维护，管理员不能直接修改。

　　表254 主路由表（Main table）如果没有指明路由所属的表，所有的路由都默认都放在这个表里，一般来说，旧的路由工具（如route）所添加的路由都会加到这个表。一般是普通的路由。

　　表253 默认路由表（Default table）一般来说默认的路由都放在这张表，但是如果特别指明放的也可以是所有的网关路由。

　　表 0 保留

　　路由配置命令的格式如下：

Usage: ip route list SELECTOR

ip route { change | del | add | append | replace | monitor } ROUTE

　　如果想查看路由表的内容，可以通过命令：

　　ip route list table table_number

　　对于路由的操作包括change、del、add 、append 、replace 、 monitor这些。例如添加路由可以用：

router># ip route add 0/0 via 192.168.0.4 table main

router># ip route add 192.168.3.0/24 via 192.168.0.3 table 1

　第一条命令是向主路由表（main table）即表254添加一条路由，路由的内容是设置192.168.0.4成为网关。

　　第二条命令代表向路由表1添加一条路由，子网192.168.3.0（子网掩码是255.255.255.0）的网关是192.168.0.3。

　　在多路由表的路由体系里，所有的路由的操作，例如网路由表添加路由，或者在路由表里寻找特定的路由，需要指明要操作的路由表，所有没有指明路由表，默认是对主路由表（表254）进行操作。而在单表体系里，路由的操作是不用指明路由表的。


在Linux里，总共可以定义232个优先级的规则，一个优先级别只能有一条规则，即理论上总共可以有条规则。其中有3个规则是默认的。命令用法如下：
首先我们可以看看路由表默认的所有规则：
```
root@netmonster# ip rule list

0: from all lookup local

32766: from all lookup main

32767: from all lookup default
```


　　规则0，它是优先级别最高的规则，规则规定，所有的包，都必须首先使用local表（254）进行路由。本规则不能被更改和删除。



　　规则32766，规定所有的包，使用表main进行路由。本规则可以被更改和删除。

　　规则32767，规定所有的包，使用表default进行路由。本规则可以被更改和删除。

　　在默认情况下进行路由时，首先会根据规则0在本地路由表里寻找路由，如果目的地址是本网络，或是广播地址的话，在这里就可以找到合适的路由；如果路由失败，就会匹配下一个不空的规则，在这里只有32766规则，在这里将会在主路由表里寻找路由;如果失败，就会匹配32767规则，即寻找默认路由表。如果失败，路由将失败。重这里可以看出，策略性路由是往前兼容的。

还可以添加规则：
```
router># ip rule add [from 0/0] table 1 pref 32800

router >#ip rule add from 192.168.3.112/32 [tos 0x10] table ２ pref 1500prohibit
```
　　第一条命令将向规则链增加一条规则，规则匹配的对象是所有的数据包，动作是选用路由表1的路由，这条规则的优先级是32800。

　　第二条命令将向规则链增加一条规则，规则匹配的对象是IP为192.168.3.112，tos等于0x10的包，使用路由表2，这条规则的优先级是1500，动作是。添加以后，我们可以看看系统规则的变化。
```
router># ip rule

0: from all lookup local

1500 from 192.168.3.112/32 [tos 0x10] lookup 2

32766: from all lookup main

32767: from all lookup default

32800: from all lookup 1
```
　　上面的规则是以源地址为关键字，作为是否匹配的依据的。除了源地址外，还可以用以下的信息：

　　From -- 源地址

　　To -- 目的地址（这里是选择规则时使用，查找路由表时也使用）

　　Tos -- IP包头的TOS（type of sevice）域

　　Dev -- 物理接口

　　Fwmark -- 防火墙参数



　　采取的动作除了指定表，还可以指定下面的动作：

　　　Table 指明所使用的表

　　　Nat 透明网关

　　　Action prohibit 丢弃该包，并发送 COMM.ADM.PROHIITED的ICMP信息

　　　Reject 单纯丢弃该包

　　　Unreachable丢弃该包，并发送 NET UNREACHABLE的ICMP信息



###参考资料
>https://www.cnblogs.com/iceocean/articles/1594488.html
>https://segmentfault.com/a/1190000010256165?utm_source=tag-newest





#获取文件大小
###stat命令

stat指令：文件/文件系统的详细信息显示。

stat命令主要用于显示文件或文件系统的详细信息，该命令的语法格式如下：

-f　　不显示文件本身的信息，显示文件所在文件系统的信息

-L　　显示符号链接

-t　　简洁模式，只显示摘要信息

```
kali@kali:/$ stat ~/java/jdk-8u60-linux-x64.tar.gz
File: '/home/kali/java/jdk-8u60-linux-x64.tar.gz'
Size: 181238643 Blocks: 353984 IO Block: 4096 regular file
Device: 808h/2056d Inode: 261742 Links: 1
Access: (0666/-rw-rw-rw-) Uid: ( 1000/ kali) Gid: ( 1000/ kali)
Access: 2017-02-01 17:36:43.177892508 +0800
Modify: 2015-10-02 12:43:29.853291000 +0800
Change: 2016-12-26 23:33:34.619480450 +0800
Birth: -
```

示例：
```
stat --format=%s $filename
或
stat -c "%s" filename

```

###wc命令

wc命令用来计算数字。利用wc指令我们可以计算文件的Byte数、字数或是列数，若不指定文件名称，或是所给予的文件名为“-”，则wc指令会从标准输入设备读取数据。

wc -c filename 参数-c表示统计字符, 因为一个字符一个字节, 所以这样得到字节数
```
kali@kali:/$ wc -c ~/java/jdk-8u60-linux-x64.tar.gz
181238643 /home/kali/java/jdk-8u60-linux-x64.tar.gz
```
示例：
```
wc -c filename | awk '{print $1}'
wc -c < filename
```
###du命令

du命令也是查看使用空间的，但是与df命令不同的是Linux du命令是对文件和目录磁盘使用的空间的查看，还是和df命令有一些区别的。

du -b filepath 参数-b表示以字节计数
```
kali@kali:/$ du -b ~/java/jdk-8u60-linux-x64.tar.gz
181238643 /home/kali/java/jdk-8u60-linux-x64.tar.gz
```
示例：
```
du -b filename | awk '{print $1}'
```
或者
```
du -h filepath 直接得出人好识别的文件大小
```

```
kali@kali:/$ du -h ~/java/jdk-8u60-linux-x64.tar.gz
173M /home/kali/java/jdk-8u60-linux-x64.tar.gz
```
示例：
```
du -bh ./shadowsocks-all.sh | awk '{print $1}'
```
###ls命令

ls命令用来显示目标列表，在Linux中是使用率较高的命令。ls命令的输出信息可以进行彩色加亮显示，以分区不同类型的文件。

ls -l filepath 第五列为文件字节数
```
kali@kali:/$ ls -l ~/java/jdk-8u60-linux-x64.tar.gz
-rw-rw-rw- 1 kali kali 181238643 10月 2 2015 /home/kali/java/jdk-8u60-linux-x64.tar.gz
```
示例：
```
ls -l filename | awk '{print $5}'
ls -h filepath h表示human, 加-h参数得到人好读的文件大小
```

```
kali@kali:/$ ls -lh ~/java/jdk-8u60-linux-x64.tar.gz
-rw-rw-rw- 1 kali kali 173M 10月 2 2015 /home/kali/java/jdk-8u60-linux-x64.tar.gz
```
示例：
```
ls -lh filename | awk '{print $5}'
```




#shell网络压力测试

```
dd if=/dev/zero bs=10MB count=1 | nc -n -N 8.8.8.8 80
```



#清空文件内容的几种方法

```
$ : > filename 
$ > filename 
$ echo "" > filename 
$ echo > filename 
```


1和2 两种方式，是0kb

3和4 两种方式，是1kb

 

还有一种方式为：``cat /dev/null > file.log``












---
[E1E22049-92C1-E495-7817-758B542437B1]:data:image/png;base64,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ARAAARAAARAAARDoKQGIlD1tGBQLBEAABEAABEAABEAABEAABEAABEAABEAABIZCACLlUFoa9QQBEAABEAABEAABEAABEAABEAABEAABEACBnhKASNnThkGxQAAEQAAEQAAEQAAEQAAEQAAEQAAEQAAEQGAoBCyRkjvdl7u+7dhk/VbzQzq8PMoro5945as/d11n0nd+qD/af8j2v+3+C+UHARAAARAAARAAARAAARAAARAAge0lAJHSaDsp0i1PjWh+65CkTsmJkNz17RMpUX+0/zDtf3sdOUoOAiAAAiAAAiAAAiAAAiAAAiAAAttOACKlJVKev2tG7959m9789HU62h8PTqRE/dH+w7X/bXfnKD8IgAAIgAAIgAAIgAAIgAAIgAAIbC8BiJSWSDk5taDHb95Hzzz0PI1euEP7Yy5SkruuZ7ANy71Rf7T/UO1/ex05Sg4CIAACIAACIAACIAACIAACIAAC204AIqVHpJwe79Po6lnamZ2h5fGUrp1akPjbOFv6PSHaP6YsyDL7nTyREvVH+w/T/rfdnaP8IAACIAACIAACIAACIAACIAACILC9BCBSBkTKMa3o6s4uXRldoItLGpxIifqj/Ydn/9vryFFyEAABEAABEAABEAABEAABEAABENh2AhApgyIlEa32aOfcjFbvXaT9LJKSaDk5TVNa5PtVZn+ge2a79PrhZRp5Iy31DLZjubeMGkX90f5Ds/9td+coPwiAAAiAAAiAAAiAAAiAAAiAAAhsLwGvSLm06nPXeF87RGZC4evbC0KWXF+6Lf+yypd9K5GS8gjL2epOXtlxIWDK52N8uOub5of6iz1JC5EW7T8w+990/0P+IAACIAACIAACIAACIAACIAACIDBcApZIOVwQqDkIgAAIgAAIgAAIgAAIgAAIgAAIgAAIgAAIgMBmCECk3Ax35AoCIAACIAACIAACIAACIAACIAACIAACIAACIJATgEgJUwABEAABEAABEAABEAABEAABEAABEAABEAABENgoAYiUG8Xf98xXtFz+GY3Hj2gFXdGKRjSKFn1J1/7kp+jRBz5g3bWin7++ot+8lB3Lgx8IgAAIgAAIgAAIgAAI9IfAckKnLq1ofuuQLscmu+JwzbNv0PxOfthkf2qAkrROoO8Hn7ZeYSvBNdW/OLD2OMv/faN5fjBt1/VD+gWBVP+3CWSw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

[A1B97A68-F8E4-6D27-97E5-7429DFCA8CCF]:data:image/png;base64,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
[A62C4080-39FA-1592-2D22-65E7AF25A7F6]:data:image/png;base64,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

[B708C09A-A56E-5D26-BCC6-CD68FA0142A8]:data:image/png;base64,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

[681964AE-0201-02F5-1F55-EA94631521A4]:data:image/png;base64,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

[22BF2B48-285F-65D6-1982-1D7720ED6E01]:data:image/png;base64,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

[6612E195-E768-A692-5B7F-55E99A50C677]:data:image/png;base64,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

[aaaaa]:data:image/png;base64,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



[bbbbb]:data:image/png;base64,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