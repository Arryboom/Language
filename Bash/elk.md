#Setup

#主节点

```
[root@elk-node1 ~]# rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch
vi /etc/yum.repos.d/elasticsearch.repo
```
*elasticsearch.repo *
>[elasticsearch-2.x]
name=Elasticsearch repository for 2.x packages
baseurl=http://packages.elastic.co/elasticsearch/2.x/centos
gpgcheck=1
gpgkey=http://packages.elastic.co/GPG-KEY-elasticsearch
enabled=1

```
yum install elasticsearch java
``` 


--- 
##新版

#Cluster

```
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.0.1-linux-x86_64.tar.gz
tar -zxvf elasticsearch-7.0.1-linux-x86_64.tar.gz
cd elasticsearch-7.0.1
cd bin
./elasticsearch
```
配置多主机，绑定地址
```
[root@elk2 config]# cat elasticsearch.yml | grep -v "^#"
cluster.name: elkcluster2
network.host: 192.168.52.130
discovery.seed_hosts: ["192.168.52.130","192.168.52.131"]
```
>所有可以参与主节点竞选的主机ip必须在discovery.seed_hosts内，包括本节点，可以以host:port方式写入。
>network.host 所监听的地址，设置为0.0.0.0可能存在风险
>cluster.name cluster的名称，多个Node组成一个cluster，cluster是一个group name

> ============================================================================
> 后台运行 ./elasticsearch -d

>Running as a daemonedit
To run Elasticsearch as a daemon, specify -d on the command line, and record the process ID in a file using the -p option:

>./bin/elasticsearch -d -p pid
Log messages can be found in the $ES_HOME/logs/ directory.

>To shut down Elasticsearch, kill the process ID recorded in the pid file:

>pkill -F pid



#Command

** 检索**
- bank index名称
- q 搜索关键字，默认全字匹配，如mattie想要用matt匹配，则需要输入matt*

```
GET /bank/_search?q=*&sort=account_number:asc&pretty
```
>https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-search-API.html

*post方式*

```
POST /bank/_search
{
  "query": { "match_all": {} },
  "size": 10
}
```
example:
```
GET /bank/_search
{
  "query": { "match_all": {} },
  "from": 10,
  "size": 10
}
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": { "balance": { "order": "desc" } }
}
GET /bank/_search
{
  "query": { "match": {"*":"phelpsparrish*"} },
  "size": 10
}
```



#issue

```
Caused by: java.lang.RuntimeException: can not run elasticsearch as root
        at org.elasticsearch.bootstrap.Bootstrap.initializeNatives(Bootstrap.jav
a:102) ~[elasticsearch-7.0.1.jar:7.0.1]
        at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:169) ~[ela
sticsearch-7.0.1.jar:7.0.1]
        at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:325) ~[elas
ticsearch-7.0.1.jar:7.0.1]
        at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:159
) ~[elasticsearch-7.0.1.jar:7.0.1]
```

>不能以root用户运行
可以添加低权限普通用户

```
adduser elastic   //添加用户
passwd ***  //给用户赋值
```

添加完用户之后：

用root用户执行 ： chown -R 文件夹名 用户名

将这几个压缩包所在的文件夹及解压完的文件夹权限给你新建的用户。之后再使用新用户启动就OK了。

** 或者 **
```
创建elsearch用户组及elsearch用户

groupadd elastic
useradd elastic -g elastic -p 123456

更改elasticsearch文件夹及内部文件的所属用户及组为elastic:elastic

cd /opt
chown -R elastic:elastic  elasticsearch
切换到elastic用户再启动

su elastic 
cd elasticsearch/bin
./elasticsearch
启动后打印信息如下
```

---


#centos系统添加/删除用户和用户组
 
  在centos中增加用户使用adduser命令而创建用户组使用groupadd命令，这个是不是非常的方便呀，其实复杂点的就是用户的组与组权限的命令了，下面来给各位介绍一下吧。
 
1、建用户：

adduser phpq                         //新建phpq用户
passwd phpq                          //给phpq用户设置密码

2、建工作组
groupadd test                        //新建test工作组

3、新建用户同时增加工作组
useradd -g test phpq               //新建phpq用户并增加到test工作组

注：：-g 所属组 -d 家目录 -s 所用的SHELL

4、给已有的用户增加工作组

usermod -G groupname username

或者：gpasswd -a username groupname 

 

（注意：添加用户到某一个组 可以使用usermod -G groupname username这个命令可以添加一个用户到指定的组，但是以前添加的组就会清空掉。

所以想要添加一个用户到一个组，同时保留以前添加的组时，请使用gpasswd这个命令来添加操作用户）

5、临时关闭

在/etc/shadow文件中属于该用户的行的第二个字段（密码）前面加上*就可以了。想恢复该用户，去掉*即可。

或者使用如下命令关闭用户账号：

passwd peter –l

重新释放：

passwd peter –u

6、永久性删除用户账号

userdel peter

groupdel peter

usermod –G peter peter   （强制删除该用户的主目录和主目录下的所有文件和子目录）

7、从组中删除用户

编辑/etc/group 找到GROUP1那一行，删除 A 或者用命令 gpasswd -d A GROUP

8、显示用户信息

id user
cat /etc/passwd

补充:查看用户和用户组的方法

用户列表文件：/etc/passwd
用户组列表文件：/etc/group

查看系统中有哪些用户：cut -d : -f 1 /etc/passwd
查看可以登录系统的用户：cat /etc/passwd | grep -v /sbin/nologin | cut -d : -f 1
查看某一用户：w 用户名
查看登录用户：who
查看用户登录历史记录：last

---

#Vm Virtual Memory too low
```
ERROR: [1] bootstrap checks failed
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```
解决办法：
1.切换到root用户下

su root

2.修改配置sysctl.conf

vi /etc/sysctl.conf 

3.末尾添加如下配置

vm.max_map_count=655360

4 执行命令

sysctl -p


>another way
1、修改max_map_count值

>$ sudo sysctl -w vm.max_map_count=262144
2、查看是否修改为262144

>$ more /proc/sys/vm/max_map_count
262144
3、重新启动ElasticSearch


---

#Max file descriptors too low

[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]

```
vi /etc/security/limits.conf
* soft nofile 65536
* hard nofile 65536
```

---


#为性能考虑禁用内存交换（虚拟内存）

###1
On Linux systems, you can disable swap temporarily by running:

sudo swapoff -a
This doesn’t require a restart of Elasticsearch.

To disable it permanently, you will need to edit the /etc/fstab file and comment out any lines that contain the word swap.
###2
Configure swappinessedit
Another option available on Linux systems is to ensure that the sysctl value vm.swappiness is set to 1. This reduces the kernel’s tendency to swap and should not lead to swapping under normal circumstances, while still allowing the whole system to swap in emergency conditions.
###3
Enable bootstrap.memory_lockedit
Another option is to use mlockall on Linux/Unix systems, or VirtualLock on Windows, to try to lock the process address space into RAM, preventing any Elasticsearch memory from being swapped out. This can be done, by adding this line to the config/elasticsearch.yml file:

bootstrap.memory_lock: true




---



simple structure image
>https://www.jianshu.com/p/cb696e0cc382
