>https://www.cnblogs.com/clsn/p/8428257.html

#install
```
yum -y install logrotate crontabs
```


```
[root@clsn6 ~]# rpm -ql  logrotate
/etc/cron.daily/logrotate
/etc/logrotate.conf  # 主配置文件
/etc/logrotate.d   # 配置目录
``` 
logrotate的配置文件是/etc/logrotate.conf，通常不需要对它进行修改。日志文件的轮循设置在独立的配置文件中，它（们）放在/etc/logrotate.d/目录下。


#example

3.1 测试logrotate如何管理日志
　　这里我们将创建一个10MB的日志文件/var/log/log-file。我们将展示怎样使用logrotate来管理该日志文件。

我们从创建一个日志文件开始吧，然后在其中填入一个10MB的随机比特流数据文件。


```
[root@clsn6 ~]# touch /var/log/log-file
[root@clsn6 ~]# head -c 10M < /dev/urandom > /var/log/log-file 
```


由于现在日志文件已经准备好，我们将配置logrotate来轮循该日志文件。让我们为该文件创建一个配置文件。

```
[root@clsn6 ~]# vim /etc/logrotate.d/log-file 
/var/log/log-file {
    monthly
    rotate 5
    compress
    delaycompress
    missingok
    notifempty
    create 644 root root
    postrotate
        /usr/bin/killall -HUP rsyslogd
    endscript
}
```
上面的模板是通用的，而配置参数则根据你的需求进行调整，不是所有的参数都是必要的。也可以通过man手册中的例子进行配置。


#配置文件说明

**monthly**

日志文件将按月轮循。其它可用值为'daily'，'weekly'或者'yearly'。

**rotate 5**

一次将存储5个归档日志。对于第六个归档，时间最久的归档将被删除。

**compress**

在轮循任务完成后，已轮循的归档将使用gzip进行压缩。

**delaycompress**

总是与compress选项一起用，delaycompress选项指示logrotate不要将最近的归档压缩，压缩将在下一次轮循周期进行。这在你或任何软件仍然需要读取最新归档时很有用。

**missingok**

在日志轮循期间，任何错误将被忽略，例如“文件无法找到”之类的错误。

**notifempty**

如果日志文件为空，轮循不会进行。

**create 644 root root**

以指定的权限创建全新的日志文件，同时logrotate也会重命名原始日志文件。

**postrotate/endscript**

在所有其它指令完成后，postrotate和endscript里面指定的命令将被执行。在这种情况下，rsyslogd 进程将立即再次读取其配置并继续运行。


#手动运行logrotate
　　logrotate可以在任何时候从命令行手动调用。要调用为/etc/lograte.d/下配置的所有日志调用logrotate：

```
[root@clsn6 ~]# logrotate /etc/logrotate.conf
```

要为某个特定的配置调用logrotate,执行一次切割任务测试

```
[root@clsn6 ~]# ll /var/log/log-file 
-rw-r--r-- 1 root root 10485760 Feb  7 18:50 /var/log/log-file
[root@clsn6 ~]# logrotate -vf /etc/logrotate.d/log-file 
[root@clsn6 ~]# ll /var/log/log-file* 
-rw-r--r-- 1 root root        0 Feb  7 19:17 /var/log/log-file
-rw-r--r-- 1 root root 10485760 Feb  7 18:50 /var/log/log-file.1
```

　　即使轮循条件没有满足，我们也可以通过使用‘-f’选项来强制logrotate轮循日志文件，‘-v’参数提供了详细的输出。


#Logrotate的记录日志
　　logrotate自身的日志通常存放于/var/lib/logrotate/status目录。如果处于排障目的，我们想要logrotate记录到任何指定的文件，我们可以指定像下面这样从命令行指定。


```
[root@clsn6 ~]# logrotate -vf -s /var/log/logrotate-status /etc/logrotate.d/log-file
reading config file /etc/logrotate.d/log-file
reading config info for /var/log/log-file 

Handling 1 logs

rotating pattern: /var/log/log-file  forced from command line (5 rotations)
empty log files are not rotated, old logs are removed
considering log /var/log/log-file
  log does not need rotating
not running postrotate script, since no logs were rotated
```

#Logrotate定时任务
　　logrotate需要的cron任务应该在安装时就自动创建了，我把cron文件的内容贴出来，以供大家参考。

```
[root@clsn6 ~]# cat /etc/cron.daily/logrotate 
#!/bin/sh

/usr/sbin/logrotate /etc/logrotate.conf
EXITVALUE=$?
if [ $EXITVALUE != 0 ]; then
    /usr/bin/logger -t logrotate "ALERT exited abnormally with [$EXITVALUE]"
fi
exit 0
```
默认最小执行单位为每天一次，欲更小周期执行，自行添加crontab

#附录


### 5.1关于USR1信号解释

　　摘自： http://www.xuebuyuan.com/323422.html

> USR1亦通常被用来告知应用程序重载配置文件；例如，向Apache HTTP服务器发送一个USR1信号将导致以下步骤的发生：停止接受新的连接，等待当前连接停止，重新载入配置文件，重新打开日志文件，重启服务器，从而实现相对平滑的不关机的更改。内容摘自wiki：http://zh.wikipedia.org/wiki/SIGUSR1%E5%92%8CSIGUSR2　　

　　对于USR1和2都可以用户自定义的，在POSIX兼容的平台上，SIGUSR1和SIGUSR2是发送给一个进程的信号，它表示了用户定义的情况。它们的符号常量在头文件signal.h中定义。在不同的平台上，信号的编号可能发生变化，因此需要使用符号名称。
```
kill -HUP pid 或者 killall -HUP pName：
```
　　其中pid是进程标识，pName是进程的名称。  
　　如果想要更改配置而不需停止并重新启动服务，可以使用上面两个命令。在对配置文件作必要的更改后，发出该命令以动态更新服务配置。根据约定，当你发送一个挂起信号(信号1或HUP)时，大多数服务器进程(所有常用的进程)都会进行复位操作并重新加载它们的配置文件。


#常见配置参数小结

![](/pics/9966.png)

**配置参数**

**说明**

**compress** 

通过gzip压缩转储以后的日志

**nocompress** 

不压缩

**copytruncate** 

用于还在打开中的日志文件，把当前日志备份并截断

**nocopytruncate** 

备份日志文件但是不截断

**create _mode owner group_** 

转储文件，使用指定的文件模式创建新的日志文件

**nocreate** 

不建立新的日志文件

**delaycompress** 

和 compress 一起使用时，转储的日志文件到下一次转储时才压缩

**nodelaycompress** 

覆盖 delaycompress 选项，转储同时压缩。

**errors address** 

专储时的错误信息发送到指定的Email 地址

**ifempty** 

即使是空文件也转储，这个是 logrotate 的缺省选项。

**notifempty** 

如果是空文件的话，不转储

**mail _address_** 

把转储的日志文件发送到指定的E-mail 地址

**nomail** 

转储时不发送日志文件

**olddir _directory_** 

转储后的日志文件放入指定的目录，必须和当前日志文件在同一个文件系统

**noolddir** 

转储后的日志文件和当前日志文件放在同一个目录下

**prerotate/endscript** 

在转储以前需要执行的命令可以放入这个对，这两个关键字必须单独成行

**daily** 

指定转储周期为每天

**weekly** 

指定转储周期为每周

**monthly** 

指定转储周期为每月

**rotate count** 

指定日志文件删除之前转储的次数，0 指没有备份，5 指保留5 个备份

**tabooext \[+\] list**

让logrotate不转储指定扩展名的文件，缺省的扩展名是：.rpm-orig, .rpmsave, v, 和 ~

**size size** 

当日志文件到达指定的大小时才转储，bytes(缺省)及KB(sizek)或MB(sizem)

**missingok**

在日志轮循期间，任何错误将被忽略，例如“文件无法找到”之类的错误。




#ELK filebeat with logrotate
>https://pohsienshih.github.io/2019/ELK-Filebeat-With-Log-Rotate/
>https://github.com/elastic/beats/issues/7633
>https://discuss.elastic.co/t/log-rotation-and-filebeat/140285
>https://www.elastic.co/guide/en/beats/filebeat/current/file-log-rotation.html
>https://blog.csdn.net/neosmith/article/details/78831668



>To prevent any confusion: Filebeat support rotated/renamed files. Identification of files is not based on the filename but inode + device id. Let's continue the discussion on the discuss topic.

>Let me try to clear up the confusion here:

>Filebeat supports following rotated files. Meaning if logrotate renames a file to .1 Filebeat is able to understand that log was rotated and continues reading.
Filebeat does not support rotating / renaming log files of an other service. Meaning Filebeat can't be used as a replacement for logrotate or other similar tools.
I think what you are looking for falls under the first use case and is supported. Instead of defining 1,2,3 just use a * pattern.


---


>Can you share why you use copytruncated? With copytruncate there is a small time frame where you will use log lines (not because of Filebeat) so I would not recommend this mechanism for log rotation if it's important to keep all log lines. logrotate supports quite a few other mechanisms which don't have this problem.


>All the options that rename the file and add a postfix to the name and create a new one to write the data in. Anything that does a copy or truncates the file I would stay away from.

>For starters, I think you should lower the [scan\_frequency 155](https://www.elastic.co/guide/en/beats/filebeat/current/filebeat-input-log.html#filebeat-input-log-scan-frequency) value (default is 10s). Try with 1s, we don't recommend using values smaller than that.

In order to provide further tunning, I'd like to have more information from you:

- Your current config.
- How fast are files created.
- How fast are files rotated (that is, how long until they reach 200M)


![](/pics/screencapture-pohsienshih-github-io-2019-ELK-Filebeat-With-Log-Rotate-2020-11-26-13_17_48.png)



###suricata rotate


```
/var/log/suricata/*.log /var/log/suricata/*.json {
        daily
        missingok
        notifempty
        rotate 5
        nocompress
        maxsize 3000M
    sharedscripts
    postrotate
        /bin/kill -HUP `cat /var/run/suricata.pid 2> /dev/null` 2> /dev/null || true
    endscript
}
```