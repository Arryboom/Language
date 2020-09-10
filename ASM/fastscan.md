#handy

>https://www.freebuf.com/articles/system/198767.html



### 1.masscan开放端口扫描

主要分为两个模块：开放端口扫描+扫描结果处理

**a.开放端口扫描**

```
masscan -p 1-65535 --rate 100000 --open-only -iL /data/portscan/iplist/allip.txt --excludefile /data/portscan/iplist/ip.exclude
```

这里主要提下--rate参数，--rate参数是指发包速率，需要根据你的实际带宽来配置，高了容易误报，低了影响扫描速率。

**b.扫描结果处理**

扫描结果处理是为下一步使用nmap进行服务探测做准备的。服务探测有两种方式可选择，“单ip单端口并发扫描”或者“单ip多端口并发扫描”。上一小节思路重难点的第2点已经提过，“在存在大量ip与开放端口的情况下，尽可能减少nmap初始化次数，以便能有效得节约系统资源”，所以最终选择了“单ip多端口并发扫描”方式。相应得，masscan端口扫描结果也要按此方式进行处理。

下面上代码，一系列sed/awk……

```
#扫描完后只保留存活ip与端口信息，并写在 port.list文件中

masscan -p 1-65535 --rate 100000 --open-only -iL /data/portscan/iplist/allip.txt --excludefile /data/portscan/iplist/ip.exclude   | sed  's/\/tcp//g' |awk -F " " {'print $6,$4'}>/data/portscan/portresult/port.list 

#对port.list中结果去重后保存在port.list.tmp文件中

sort /data/portscan/portresult/port.list | uniq > /data/portscan/portresult/port.list.tmp

#将port.list.tmp文件中结果按“ip 端口1，端口2，端口3，……”方式处理后保存在port.list.nmap中

awk '{a[$1]=$2","a[$1]}END{for(i in a){print i,a[i]}}' /data/portscan/portresult/port.list.tmp | sed "s/,$//g" > /data/portscan/portresult/port.list.nmap
```

### 2.服务版本探测

这里主要考虑两个方面，nmap参数调优和队列控制并发扫描。

**a.nmap参数调优**

```
nmap -T4 -Pn -sV -n -p $port $ip
```

\-T4：将扫描延迟降低到10ms；

\-sV：对服务的版本信息进行探测，加上这个参数后更耗时，但是探测结果可以细致到版本号，主要还是看各位需求。

**b.队列控制并发扫描**

队列控制可以使系统达到较好的利用率，线程数可以自己根据需要进行调整，我目前使用一台8核24G的虚拟机跑1w+个ip,40w+个端口，12个小时内完成所有的端口与应用识别。上代码：

```
#创建有名管道

[ -e /tmp/fd1 ] || mkfifo /tmp/fd1 

 #创建文件描述符，以可读（<）可写（>）的方式关联管道文件，这时候文件描述符3就有了有名管道文件的所有特性

exec 3<>/tmp/fd1                  

 #关联后的文件描述符拥有管道文件的所有特性,所以这时候管道文件可以删除，我们留下文件描述符来用就可以了

rm -rf /tmp/fd1

#并发执行的进程数量

thread=30          

for ((i=1;i<=$thread;i++))

do

 #&3代表引用文件描述符3，这条命令代表往管道里面放入了一个"令牌"    

        echo >&3                     

done

#总的ip数目

ipnum=`wc -l /data/portscan/iplist/port.list.nmap | awk {'print $1'}`

for ((i=1;i<=$ipnum;i++))

do

 #代表从管道中读取一个令牌    

read -u3                             

{

        #提取ip地址和端口号

        ip=`sed -n  ""$i"p" /data/portscan/portresult/port.list.nmap| awk {'print $1'}`

        port=`sed -n  ""$i"p" /data/portscan/portresult/port.list.nmap| awk {'print $2'}`

        #当开放端口数量超过500时，直接扫全量端口，这样反而速度会更快 

        if [[ `echo $port | awk -F "," {'print NF'}` -gt 500 ]] ; then

                port="1-65535"

        fi

        #扫描完成后立即将结果按“ip地址 nmap 端口号 开放状态 开放服务 版本号”方式进行处理 

        /usr/bin/nmap -T4 -Pn -sV -n -p $port $ip | grep -v "Nmap" |grep -v "SUBMIT INDIVIDUALLY"|grep -v "MAC Address"| grep -v "Host is up" | grep -v "Service Info"| grep -v -E "SF:" | grep -v "SF-"|grep -v "please    submit" | grep -v -E "PORT\s+STATE" |grep -v "Service detection"|sed '/^\s*$/d' | sed 's/^/'"$ip"' nmap /g'|sed 's/\/tcp//g' | sed 's/\s\+/ /g' >>/var/log/nmap 2>> /data/portscan/servicescan.log

        #命令执行到最后，把令牌放回管道

        echo >&3                   

} 2>> /data/portscan/servicescan.log &

done

wait

exec 3<&-                       #关闭文件描述符的读

exec 3>&-                       #关闭文件描述符的写
```

## **四、结语**

接下来考虑使用扫描结果进行高危端口监控和服务识别了，我是结合splunk来做分析的，各位是愿意接入数据分析平台还是直接撸脚本就看手上的资源和自己的兴趣了。另外，有兴趣的小伙伴还可以试着用python来实现。