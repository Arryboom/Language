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

#`需要输入到文件的情况
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