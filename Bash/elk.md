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

** status/baseinfo **

>https://www.elastic.co/guide/en/elasticsearch/reference/current/cat.html


req
```
/_cat/master?v
```
res
```
id                     host      ip        node
u_n93zwxThWHi1PDBJAGAg 127.0.0.1 127.0.0.1 u_n93zw
```


req
```
GET /_cat/nodes?h=ip,port,heapPercent,name
```
res
```
127.0.0.1 9300 27 sLBaIGK
```


req
```
GET /_cat/indices?bytes=b&s=store.size:desc&v
```
res
```
health status index    uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   twitter  u8FNjxh8Rfy_awN11oDKYQ   1   1       1200            0      72171         72171
green  open   twitter2 nYFWZEO7TUiOjLQXBaYJpA   1   0          0            0        230          230
```


req
```
http://192.168.40.234:9200/_search?pretty
```
res
```
{
  "took" : 3,
  "timed_out" : false,
  "_shards" : {
    "total" : 8,
    "successful" : 8,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 80,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : ".kibana_1",
        "_type" : "_doc",
        "_id" : "space:default",
        "_score" : 1.0,
        "_source" : {
          "space" : {
            "name" : "Default",
            "description" : "This is your default space!",
            "color" : "#00bfb3",
            "disabledFeatures" : [ ],
            "_reserved" : true
          },
          "type" : "space",
          "references" : [ ],
          "migrationVersion" : {
            "space" : "6.6.0"
          },
          "updated_at" : "2020-05-15T05:23:27.656Z"
        }
      },
```


- req
```
http://192.168.40.234:9200/_xpack
```
- res
```
{"build":{"hash":"Unknown","date":"Unknown"},"license":{"uid":"10bdea8f-f3fc-421d-9354-b47315d5ba47","type":"platinum","mode":"platinum","status":"active","expiry_date_in_millis":2524579200999},"features":{"analytics":{"available":true,"enabled":true},"ccr":{"available":true,"enabled":true},"enrich":{"available":true,"enabled":true},"flattened":{"available":true,"enabled":true},"frozen_indices":{"available":true,"enabled":true},"graph":{"available":true,"enabled":true},"ilm":{"available":true,"enabled":true},"logstash":{"available":true,"enabled":true},"ml":{"available":true,"enabled":true,"native_code_info":{"version":"7.6.2","build_hash":"e06ef9d86d5332"}},"monitoring":{"available":true,"enabled":true},"rollup":{"available":true,"enabled":true},"security":{"available":true,"enabled":true},"slm":{"available":true,"enabled":true},"spatial":{"available":true,"enabled":true},"sql":{"available":true,"enabled":true},"transform":{"available":true,"enabled":true},"vectors":{"available":true,"enabled":true},"voting_only":{"available":true,"enabled":true},"watcher":{"available":true,"enabled":true}},"tagline":"You know, for X"}
```

- req
```
http://localhost:9200/_cat/health
http://localhost:9200/_cat/nodes
http://localhost:9200/_cluster/health
```


license

- req
```
GET /_license
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


##file beat test

```
[root@localhost filebeat]# filebeat test output
elasticsearch: http://192.168.40.234:9200...
  parse url... OK
  connection...
    parse host... OK
    dns lookup... OK
    addresses: 192.168.40.234
    dial up... OK
  TLS... WARN secure connection disabled
  talk to server... OK
  version: 7.6.2
```



```
/usr/share/filebeat/bin/filebeat test config -e
```


> 2018/02/21 16:15:40.900903 log.go:116: INFO File is inactive: /opt/data/auth.log. Closing because close\_inactive of 5m0s reached.

If you look at the look, I see that your configuration correctly it tries to read `auth.log` in `/opt/data/auth`, but filebeat close it because it cannot find new data written to the file.

I think the registry has some information about this file so I doesn't detect new content.

Question:

- Does this file has content?
- I think there is no more writes in that file?

Can you add the content of the following file:

```
cat /var/lib/filebeat/registry
```
The registry does have some content.

```
tabitha@CaseV-611-ESML:~$ sudo cat /var/lib/filebeat/registry
[sudo] password for tabitha: 
[{"source":"/home/tabitha/Data/auth.log","offset":797638,"FileStateOS": {"inode":4729038,"device":2049},"timestamp":"2018-02-15T10:02:24.10256429-05:00","ttl":- 2},{"source":"/var/log/data/testdata-auth.log","offset":797638,"FileStateOS":{"inode":17705737,"device":2049},"timestamp":"2018-02-16T11:51:33.488626541-05:00","ttl":-2},{"source":"/opt/data/auth.log","offset":797637,"FileStateOS":{"inode":18612227,"device":2049},"timestamp":"2018-02-21T14:03:38.875454424-05:00","ttl":-1},{"source":"/var/log/auth.log","offset":47979,"FileStateOS":{"inode":17706302,"device":2049},"timestamp":"2018-02-20T11:57:32.43111871-05:00","ttl":-2},{"source":"/var/log/auth.log.1","offset":72313,"FileStateOS":{"inode":17696223,"device":2049},"timestamp":"2018-02-20T11:31:17.394728752-05:00","ttl":-2},{"source":"/opt/data/auth.log","offset":797637,"FileStateOS":{"inode":18612228,"device":2049},"timestamp":"2018-02-21T14:03:38.875456235-05:00","ttl":-1}]
```

When I was trying to figure out why it was not working. I moved the file auth.log from another directory (/home/tabitha/Data/), renamed it (testdata-auth.log), and of course have tried it several times since then.

Question:

- So how can I clear the registry to start over reading the file, auth.log?


with xpack enable,the default rule of beats_system 
```
beats_system
Grants access necessary for the Beats system user to send system-level data (such as monitoring) to Elasticsearch.

This role should not be assigned to users as the granted permissions may change between releases.
This role does not provide access to the beats indices and is **not suitable** for writing beats output to Elasticsearch.
```

to fix this

>https://www.elastic.co/guide/en/beats/filebeat/7.x/feature-roles.html#privileges-to-publish-events
another backup>https://www.elastic.co/guide/en/beats/filebeat/6.8/beats-basic-auth.html



##filebeat debug log

In the bottom of `/etc/filebeat/filebeat.yml` you should see a "logging" section. You will need to uncomment it and fill in the appropriate fields for you. Maybe some logging output would be helpful in formulating your question.

```
logging:
  to_files: true
  files:
    path: /var/log/filebeat
    name: filebeat.log
    rotateeverybytes: 10485760 # = 10MB
    keepfiles: 7
  level: debug
```

newversion 7.7

```
logging.level: info
logging.to_files: true
logging.files:
  path: /var/log/filebeat
  name: filebeat
  keepfiles: 7
  permissions: 0644
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

###4
Configure jvm memory limit
by default elasticsearch will use 1GB memory,if you got more you can add more for performance

edit /etc/elasticsearch/jvm.options,add -Xms and -Xmx options,here is a 8GB example
```
# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms8g
-Xmx8g
```


---

simple with kafka
>https://www.jianshu.com/p/934c457a333c


simple structure image
>https://www.jianshu.com/p/cb696e0cc382


logstash tag
>https://www.jianshu.com/p/934c457a333c




















#crack_7.6.2_xpack

>https://blog.espnlol.com/?p=504
>https://www.codetd.com/en/article/7049729


```
/usr/share/elasticsearch/jdk/bin/javac -cp "./lib/*" ./XPackBuild.java
/usr/share/elasticsearch/jdk/bin/javac -cp "./lib/*" ./LicenseVerifier.java
```

>for testing.it should be 
```
[root@ssl elasticsearch]# /usr/share/elasticsearch/jdk/bin/javac -cp "./lib/*:./modules/x-pack-core/*" ./LicenseVerifier.java
```



破解方法是网上现有6.\*的版本破解方法，反编译校验许可证方法和jar包校验方法。

> 此文章只记录使用破解方式 ，具体操作步骤不记录。

1. 找到以下jar包

```
elasticsearch/modules/x-pack/x-pack-core/x-pack-core-6.3.2.jar
```

2. 反编译以下两个class文件

- org/elasticsearch/license/LicenseVerifier.class

```java
package org.elasticsearch.license; import java.nio.*;import org.elasticsearch.common.bytes.*;import java.security.*;import java.util.*;import org.elasticsearch.common.xcontent.*;import org.apache.lucene.util.*;import org.elasticsearch.core.internal.io.*;import java.io.*; public class LicenseVerifier{    public static boolean verifyLicense(final License license, final byte[] publicKeyData) { /*               byte[] signedContent = null;        byte[] publicKeyFingerprint = null;        try {            final byte[] signatureBytes = Base64.getDecoder().decode(license.signature());            final ByteBuffer byteBuffer = ByteBuffer.wrap(signatureBytes);            final int version = byteBuffer.getInt();            final int magicLen = byteBuffer.getInt();            final byte[] magic = new byte[magicLen];            byteBuffer.get(magic);            final int hashLen = byteBuffer.getInt();            publicKeyFingerprint = new byte[hashLen];            byteBuffer.get(publicKeyFingerprint);            final int signedContentLen = byteBuffer.getInt();            signedContent = new byte[signedContentLen];            byteBuffer.get(signedContent);            final XContentBuilder contentBuilder = XContentFactory.contentBuilder(XContentType.JSON);            license.toXContent(contentBuilder, (ToXContent.Params)new ToXContent.MapParams((Map)Collections.singletonMap("license_spec_view", "true")));            final Signature rsa = Signature.getInstance("SHA512withRSA");            rsa.initVerify(CryptUtils.readPublicKey(publicKeyData));            final BytesRefIterator iterator = BytesReference.bytes(contentBuilder).iterator();            BytesRef ref;            while ((ref = iterator.next()) != null) {                rsa.update(ref.bytes, ref.offset, ref.length);            }            return rsa.verify(signedContent);        }        catch (IOException ex) {}        catch (NoSuchAlgorithmException ex2) {}        catch (SignatureException ex3) {}        catch (InvalidKeyException e) {            throw new IllegalStateException(e);        }        finally {            if (signedContent != null) {                Arrays.fill(signedContent, (byte)0);            }        }*/        return true;    }        public static boolean verifyLicense(final License license) {        /*        byte[] publicKeyBytes;        try {            final InputStream is = LicenseVerifier.class.getResourceAsStream("/public.key");            try {                final ByteArrayOutputStream out = new ByteArrayOutputStream();                Streams.copy(is, (OutputStream)out);                publicKeyBytes = out.toByteArray();                if (is != null) {                    is.close();                }            }            catch (Throwable t) {                if (is != null) {                    try {                        is.close();                    }                    catch (Throwable t2) {                        t.addSuppressed(t2);                    }                }                throw t;            }        }        catch (IOException ex) {            throw new IllegalStateException(ex);        }        //return verifyLicense(license, publicKeyBytes);        */        return true;    }}
```

- org/elasticsearch/xpack/core/XPackBuild.class

```java
package org.elasticsearch.xpack.core; import org.elasticsearch.common.io.*;import java.net.*;import org.elasticsearch.common.*;import java.nio.file.*;import java.io.*;import java.util.jar.*; public class XPackBuild{    public static final XPackBuild CURRENT;    private String shortHash;    private String date;        @SuppressForbidden(reason = "looks up path of xpack.jar directly")    static Path getElasticsearchCodebase() {        final URL url = XPackBuild.class.getProtectionDomain().getCodeSource().getLocation();        try {            return PathUtils.get(url.toURI());        }        catch (URISyntaxException bogus) {            throw new RuntimeException(bogus);        }    }        XPackBuild(final String shortHash, final String date) {        this.shortHash = shortHash;        this.date = date;    }        public String shortHash() {        return this.shortHash;    }        public String date() {        return this.date;    }        static {        final Path path = getElasticsearchCodebase();        String shortHash = null;        String date = null;        Label_0109: {/*            if (path.toString().endsWith(".jar")) {                try {                    final JarInputStream jar = new JarInputStream(Files.newInputStream(path, new OpenOption[0]));                    try {                        final Manifest manifest = jar.getManifest();                        shortHash = manifest.getMainAttributes().getValue("Change");                        date = manifest.getMainAttributes().getValue("Build-Date");                        jar.close();                    }                    catch (Throwable t) {                        try {                            jar.close();                        }                        catch (Throwable t2) {                            t.addSuppressed(t2);                        }                        throw t;                    }                    break Label_0109;                }                catch (IOException e) {                    throw new RuntimeException(e);                }            }*/            shortHash = "Unknown";            date = "Unknown";        }        CURRENT = new XPackBuild(shortHash, date);    }}
```

3. 重新打包 x-pack-core-6.3.2.jar 后替换原有jar
4. 申请试用 [地址](https://license.elastic.co/registration)
5. 修改license type改为platinum、expiry\_date\_in\_millis改为253402271999000（9999年）
6. 在kibana导入license 即可



---



# **elasticsearch7.0 Cluster Setup and x-pack crack**

## **surroundings**

Centos7  
Elasticsearch-7.0.1  
Kibana-7.0.1

## **Download, unzip es**

```
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.0.1-linux-x86_64.tar.gz
解压elasticsearch.tar.gz
tar zxvf elasticsearch-7.0.1-linux-x86_64.tar.gz
```

## **Crack x-pack**

```
1. 下载java反编译工具
这个反编译的工具蛮多，推荐Luyten，下载地址： https://github.com/deathmarine/Luyten/releases
下载对应的windows版本到本地，然后安装。
2. 将elasticsearch-7.0.1-linux-x86_64.tar.gz解压后复制elasticsearch-7.0.1/modules/x-pack-core/x-pack-core-7.0.1.jar文件
到windows机器，使用反编译工具打开
3. 找到org.elasticsearch.license.LicenseVerifier 打开并且复制里面的内容，然后在我们的服务器上新建一个LicenseVerifier.java的文件，将复制的内容粘贴进去
在LicenseVerifier中有两个静态方法，这就是验证授权文件是否有效的方法，我们把它修改为全部返回true. 并且将不需要的代码注释掉  这里使用/*  */来注释
下面是修改后的内容：
```

```
package org.elasticsearch.license;

import java.nio.*;
import org.elasticsearch.common.bytes.*;
import java.security.*;
import java.util.*;
import org.elasticsearch.common.xcontent.*;
import org.apache.lucene.util.*;
import org.elasticsearch.core.internal.io.*;
import java.io.*;

public class LicenseVerifier
{
    public static boolean verifyLicense(final License license, final byte[] publicKeyData) {
/*  #这里添加注释
        byte[] signedContent = null;
        byte[] publicKeyFingerprint = null;
        try {
            final byte[] signatureBytes = Base64.getDecoder().decode(license.signature());
            final ByteBuffer byteBuffer = ByteBuffer.wrap(signatureBytes);
            final int version = byteBuffer.getInt();
            final int magicLen = byteBuffer.getInt();
            final byte[] magic = new byte[magicLen];
            byteBuffer.get(magic);
            final int hashLen = byteBuffer.getInt();
            publicKeyFingerprint = new byte[hashLen];
            byteBuffer.get(publicKeyFingerprint);
            final int signedContentLen = byteBuffer.getInt();
            signedContent = new byte[signedContentLen];
            byteBuffer.get(signedContent);
            final XContentBuilder contentBuilder = XContentFactory.contentBuilder(XContentType.JSON);
            license.toXContent(contentBuilder, (ToXContent.Params)new ToXContent.MapParams((Map)Collections.singletonMap("license_spec_view", "true")));
            final Signature rsa = Signature.getInstance("SHA512withRSA");
            rsa.initVerify(CryptUtils.readPublicKey(publicKeyData));
            final BytesRefIterator iterator = BytesReference.bytes(contentBuilder).iterator();
            BytesRef ref;
            while ((ref = iterator.next()) != null) {
                rsa.update(ref.bytes, ref.offset, ref.length);
            }
            return rsa.verify(signedContent);
        }
        catch (IOException ex) {}
        catch (NoSuchAlgorithmException ex2) {}
        catch (SignatureException ex3) {}
        catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
        finally {
            if (signedContent != null) {
                Arrays.fill(signedContent, (byte)0);
            }
        }
*/  ##这里添加注释
        return true;    #这里添加返回true
}
    
    public static boolean verifyLicense(final License license) {
/*   #这里添加注释
        byte[] publicKeyBytes;
        try {
            final InputStream is = LicenseVerifier.class.getResourceAsStream("/public.key");
            try {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                Streams.copy(is, (OutputStream)out);
                publicKeyBytes = out.toByteArray();
                if (is != null) {
                    is.close();
                }
            }
            catch (Throwable t) {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Throwable t2) {
                        t.addSuppressed(t2);
                    }
                }
                throw t;
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        return verifyLicense(license, publicKeyBytes);
    */ #这里添加注释
        return true;  #这里添加返回true
    }
}

```

1. Find org.elasticsearch.xpack.core.XPackBuild open and copy the contents inside, and then create a XPackBuild.java files on our server, copy and paste the contents into it  
    we will try in the final block of code in a static part XPackBuild delete all, here is a comment by the method of this chapter to verify that the jar package has been modified.  
    the following is the modified content:

```
package org.elasticsearch.xpack.core;

import org.elasticsearch.common.io.*;
import java.net.*;
import org.elasticsearch.common.*;
import java.nio.file.*;
import java.io.*;
import java.util.jar.*;

public class XPackBuild
{
    public static final XPackBuild CURRENT;
    private String shortHash;
    private String date;
    
    @SuppressForbidden(reason = "looks up path of xpack.jar directly")
    static Path getElasticsearchCodebase() {
        final URL url = XPackBuild.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            return PathUtils.get(url.toURI());
        }
        catch (URISyntaxException bogus) {
            throw new RuntimeException(bogus);
        }
    }
    
    XPackBuild(final String shortHash, final String date) {
        this.shortHash = shortHash;
        this.date = date;
    }
    
    public String shortHash() {
        return this.shortHash;
    }
    
    public String date() {
        return this.date;
    }
    
    static {
        final Path path = getElasticsearchCodebase();
        String shortHash = null;
        String date = null;
        Label_0109: {
/*  #这里添加注释
            if (path.toString().endsWith(".jar")) {
                try {
                    final JarInputStream jar = new JarInputStream(Files.newInputStream(path, new OpenOption[0]));
                    try {
                        final Manifest manifest = jar.getManifest();
                        shortHash = manifest.getMainAttributes().getValue("Change");
                        date = manifest.getMainAttributes().getValue("Build-Date");
                        jar.close();
                    }
                    catch (Throwable t) {
                        try {
                            jar.close();
                        }
                        catch (Throwable t2) {
                            t.addSuppressed(t2);
                        }
                        throw t;
                    }
                    break Label_0109;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
*/  #这里添加注释
            shortHash = "Unknown";
            date = "Unknown";
        }
        CURRENT = new XPackBuild(shortHash, date);
    }
}
```
>[root@ssl elasticsearch]# ./jdk/bin/javac -cp "/usr/share/elasticsearch/lib/*:/usr/share/elasticsearch/modules/x-pack-core/*" ./LicenseVerifier.java


```
5. 使用javac将刚刚创建的LicenseVerifier.java和XPackBuild.java文件编译成class文件,这里需要注意自己的目录结构
javac -cp "/data/soft/elasticsearch-7.0.1/lib/elasticsearch-7.0.1.jar:/data/soft/elasticsearch-7.0.1/lib/lucene-core-8.0.0.jar:/data/soft/elasticsearch-7.0.1/modules/x-pack-core/x-pack-core-7.0.1.jar:/data/soft/elasticsearch-7.0.1/modules/x-pack-core/netty-common-4.1.32.Final.jar:/data/soft/elasticsearch-7.0.1/lib/elasticsearch-core-7.0.1.jar" ./LicenseVerifier.java
javac -cp "/data/soft/elasticsearch-7.0.1/lib/elasticsearch-7.0.1.jar:/data/soft/elasticsearch-7.0.1/lib/lucene-core-8.0.0.jar:/data/soft/elasticsearch-7.0.1/modules/x-pack-core/x-pack-core-7.0.1.jar:/data/soft/elasticsearch-7.0.1/modules/x-pack-core/netty-common-4.1.32.Final.jar:/data/soft/elasticsearch-7.0.1/lib/elasticsearch-core-7.0.1.jar" ./XPackBuild.java

执行完后当前目录下会生成两个class文件
LicenseVerifier.class
XPackBuild.class

6. 将x-pack-core-7.0.1.jar 拷贝到一个空目录中解压x-pack-core-7.0.1.jar
$ jar -xvf x-pack-core-7.0.1.jar
然后替换class文件
cp -a ../XPackBuild.class  org/elasticsearch/xpack/core/
cp -a ../LicenseVerifier.class org/elasticsearch/license/

7. 打包新x-pack-core-7.0.1.jar文件
进入到在刚刚x-pack-core-7.0.1.jar解压的目录中删除x-pack-core-7.0.1.jar源文件，然后重新打包：
jar cvf x-pack-core-7.0.1.jar .

8. 将新生成的x-pack-core-7.0.1.jar文件替换到es中
cp -a x-pack-core-7.0.1.jar /data/soft/elasticsearch-7.0.1/modules/x-pack-core/

9. 配置elasticsearch安全协议
完成以上所有操作在启动elasticsearch前，我们需要配置elasticsearch的SSL/TLS安全协议,如果不配置的话，需要禁止security才能配置License。
当License配置完成后我们需要再开启security，并开启SSL\TLS。

# 加载License到elasticsearch之前操作
$ echo "xpack.security.enabled: false" >> /data/soft/elasticsearch-7.0.1/config/elasticsearch.yml
$ ./bin/elasticsearch -d   # 后台方式启动elasticsearch

10. 申请license
登录elastic官网申请一个license, [License申请地址](https://license.elastic.co/registration)，申请完成后，下载下来的License格式为json格式。并将该License的`type`、`expiry_date_in_millis`、`max_nodes`分别修改成`platinum`、`2524579200999`、`1000`。如下：
没有7的选项，下载6的也可以，时间的转换是毫秒级
license的内容如下：
{
    "license":{
        "uid":"10bdea8f-f3fc-421d-9354-b47315d5ba47",
        "type":"platinum",
        "issue_date_in_millis":1565481600000,
        "expiry_date_in_millis":2524579200999,
        "max_nodes":1000,
        "issued_to":"wang xiao (???????)",
        "issuer":"Web Form",
        "signature":"AAAAAwAAAA1nyWcAXdBAA2klkzrZAAABmC9ZN0hjZDBGYnVyRXpCOW5Bb3FjZDAxOWpSbTVoMVZwUzRxVk1PSmkxaktJRVl5MUYvUWh3bHZVUTllbXNPbzBUemtnbWpBbmlWRmRZb25KNFlBR2x0TXc2K2p1Y1VtMG1UQU9TRGZVSGRwaEJGUjE3bXd3LzRqZ05iLzRteWFNekdxRGpIYlFwYkJiNUs0U1hTVlJKNVlXekMrSlVUdFIvV0FNeWdOYnlESDc3MWhlY3hSQmdKSjJ2ZTcvYlBFOHhPQlV3ZHdDQ0tHcG5uOElCaDJ4K1hob29xSG85N0kvTWV3THhlQk9NL01VMFRjNDZpZEVXeUtUMXIyMlIveFpJUkk2WUdveEZaME9XWitGUi9WNTZVQW1FMG1DenhZU0ZmeXlZakVEMjZFT2NvOWxpZGlqVmlHNC8rWVVUYzMwRGVySHpIdURzKzFiRDl4TmM1TUp2VTBOUlJZUlAyV0ZVL2kvVk10L0NsbXNFYVZwT3NSU082dFNNa2prQ0ZsclZ4NTltbU1CVE5lR09Bck93V2J1Y3c9PQAAAQAGLq3NLsOn2u1mkOfdXR6oyixJl8/kZu/godGNtR1F6fvutn4mMKdvPPB4n8pQxa4kAFJn731D5I5kUwNqxmuTsGlvf+V8G4bj1O3nyMK7p3vxy0TzO0vEb+WskvGoJr8axPOPU7h8xh4POTYJVplaABtjqaR+1SGS5ki422xIhXCNah99YuY8fct83M0U1iEJaSST26Ew3PCQ6n5yKrht8zk+yBIG32hgOhbhzgPOa4cVy1rQr7Z7ZxJIY8OyN6sfqItt8dzB2m+G8Eu/xP6Z9nmpG6y+Ty9BpJhre7TExrgObTju313IcFo1ZAkY70UscLamYbpRDJuS2EY7Qmow",
        "start_date_in_millis":1565481600000
    }
}
我们将过期时间写到2050年，type改为platinum 白金版，这样我们就会拥有全部的x-pack功能。
11. 加载License到elasticsearch

$ curl -XPUT -u elastic 'http://10.0.0.4:9200/_xpack/license' -H "Content-Type: application/json" -d @license.json
Enter host password for user 'elastic':           # 提示输入elastic用户密码，当前无密码，所以直接回车
{"acknowledged":true,"license_status":"valid"}    # license写入成功

12. 修改配置，重启es
echo "xpack.security.transport.ssl.enabled: true" >> /data/soft/elasticsearch-7.0.1/config/elasticsearch.yml
sed -i 's/xpack.security.enabled: false/xpack.security.enabled: true/g' /data/soft/elasticsearch-7.0.1/config/elasticsearch.yml
kill -9 13023 && ./bin/elasticsearch -d   # 重启elasticsearch


此时的配置如下：
[root@heaven-01 soft]# cat elasticsearch-7.0.1/config/elasticsearch.yml

path.data: /data/soft/elasticsearch-7.0.1/data
path.logs: /data/soft/elasticsearch-7.0.1/logs
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true


查看License
[root@heaven-01 soft]# curl -XGET -u elastic  http://10.0.0.4:9200/_license  #提示输入elastic用户密码，当前无密码，所以直接回车
{
  "license" : {
    "status" : "active",
    "uid" : "10bdea8f-f3fc-421d-9354-b47315d5ba47",
    "type" : "platinum",
    "issue_date" : "2019-08-11T00:00:00.000Z",
    "issue_date_in_millis" : 1565481600000,
    "expiry_date" : "2049-12-31T16:00:00.999Z",
    "expiry_date_in_millis" : 2524579200999,
    "max_nodes" : 100,
    "issued_to" : "wang xiao (???????)",
    "issuer" : "Web Form",
    "start_date_in_millis" : 1565481600000
  }
}
破解成功

12. 设置密码
执行下列命令设置其他组建连接es所需账号及密码：
bin/elasticsearch-setup-passwords interactive

```

## es cluster deployment and kibana (es here to three nodes for example):

If we deploy es is the above configuration to a single node, if it is es cluster will also need the following operations:

```
1. 由于在es的集群中如果使用x-pack的安全功能则必须要基于TLS/SSL的安全传输，因此需要配置认证文件
bin/elasticsearch-certutil cert -out config/elastic-certificates.p12 -pass ""

这个时候会在config目录下生成elastic-certificates.p12文件，修改文件权限
chown elk:elk config/elastic-certificates.p12

2. 将破解好的es-01复制两个分别命名为es-02  es-03，然后清理掉es-02 es-03中的data及logs目录（很中要，尤其是data目录否则会报错）
3. 修改es配置

es-01的配置如下：

[root@heaven-01 soft]# cat es-01/config/elasticsearch.yml  | grep -v "#"
cluster.name: es-cluster
node.name: node-master-01
node.master: true
node.data: true
path.data: /data/soft/es-01/data
path.logs: /data/soft/es-01/logs
network.host: 10.0.0.4
http.port: 9200
transport.tcp.compress: true
http.cors.enabled: true
http.cors.allow-origin: "*"

bootstrap.memory_lock: true
discovery.seed_hosts: ["10.0.0.4:9300","10.0.0.4:9301","10.0.0.4:9302"]
cluster.initial_master_nodes: ["10.0.0.4:9300"]

xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: /data/soft/es-01/config/elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: /data/soft/es-01/config/elastic-certificates.p12


es-02的配置如下：

[root@heaven-01 soft]# cat es-02/config/elasticsearch.yml  | grep -v "#"
cluster.name: es-cluster
node.name: node-data-01
node.master: false
node.data: true
path.data: /data/soft/es-02/data
path.logs: /data/soft/es-02/logs
network.host: 10.0.0.4
http.port: 9201
transport.tcp.compress: true
http.cors.enabled: true
http.cors.allow-origin: "*"

bootstrap.memory_lock: true
discovery.seed_hosts: ["10.0.0.4:9300","10.0.0.4:9301","10.0.0.4:9302"]
cluster.initial_master_nodes: ["10.0.0.4:9300"]

xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: /data/soft/es-02/config/elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: /data/soft/es-02/config/elastic-certificates.p12

es-03的配置如下：
[root@heaven-01 soft]# cat es-03/config/elasticsearch.yml  | grep -v "#"
cluster.name: es-cluster
node.name: node-data-02
node.master: false
node.data: true
path.data: /data/soft/es-03/data
path.logs: /data/soft/es-03/logs
network.host: 10.0.0.4
http.port: 9202
transport.tcp.compress: true
http.cors.enabled: true
http.cors.allow-origin: "*"

bootstrap.memory_lock: true
discovery.seed_hosts: ["10.0.0.4:9300","10.0.0.4:9301","10.0.0.4:9302"]
cluster.initial_master_nodes: ["10.0.0.4:9300"]

xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: /data/soft/es-03/config/elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: /data/soft/es-03/config/elastic-certificates.p12


4. 分别启动三个es
sudo -u elk /data/soft/es-01/bin/elasticsearch -d
sudo -u elk /data/soft/es-02/bin/elasticsearch -d
sudo -u elk /data/soft/es-03/bin/elasticsearch -d

5. 下载配置kibana

wget   https://artifacts.elastic.co/downloads/kibana/kibana-7.0.1-x86_64.rpm
rpm -ivh kibana-7.0.1-x86_64.rpm

修改配置如下：

cat /etc/kibana/kibana.yml
server.port: 5601
server.host: "10.0.0.4"
elasticsearch.hosts: ["http://10.0.0.4:9200"]
elasticsearch.username: "elastic"
elasticsearch.password: "123456"
xpack.security.encryptionKey: "something_at_least_32_characters"

启动kibana

/etc/init.d/kibana start

6.  访问（使用设置的的elastic用户及密码登录）
http://10.0.0.4:5601
```















#elasticsearch reset password

```
[esuser@esuser-oracle-9e96168-prd bin]$ ./elasticsearch-setup-passwords interactive                                                                          
Initiating the setup of passwords for reserved users elastic,apm_system,kibana,logstash_system,beats_system,remote_monitoring_user.
You will be prompted to enter passwords as the process progresses.
Please confirm that you would like to continue [y/N]y


Enter password for [elastic]:
Reenter password for [elastic]:
Enter password for [apm_system]:
Reenter password for [apm_system]:
Enter password for [kibana]:
Reenter password for [kibana]:
Enter password for [logstash_system]:
Reenter password for [logstash_system]:
Enter password for [beats_system]:
Reenter password for [beats_system]:
Enter password for [remote_monitoring_user]:
Reenter password for [remote_monitoring_user]:
Changed password for user [apm_system]
Changed password for user [kibana]
Changed password for user [logstash_system]
Changed password for user [beats_system]
Changed password for user [remote_monitoring_user]
Changed password for user [elastic]

```

for generate password automatically:
```
[esuser@esuser-oracle-9e96168-prd bin]$ ./elasticsearch-setup-passwords auto                                                                        

```



#Es 端口9300 9200
1.9300端口： ES节点之间通讯使用
2.9200端口： ES节点 和 外部 通讯使用
9300是tcp通讯端口，集群间和TCPClient都走的它；9200是http协议的RESTful接口





----

#issue


##kibana port 5601 in use

kill every process with node and running under kibana privelege

```
ps -ef | grep node
ps -ef | grep kibana
```

stop the service 

```
service kibana stop
```

restart it.

```
systemctl start kibana
```



###{"type":"audit", "timestamp":"2020-05-29T19:50:00,619+0800", "node.id":"s9cmGA6ZSF6LgU9zngYAQA", "event.type":"transport", "event.action":"access_denied", "user.name":"elastic", "user.realm":"reserved", "user.roles":["superuser"], "origin.type":"rest", "origin.address":"192.168.40.243:46532", "request.id":"aU8oObU3Sv2ez_WNnUSw0g", "action":"indices:data/read/field_caps", "request.name":"FieldCapabilitiesRequest", "indices":["wazuh-alerts-3.x-*"]}

The Kibana index is created once an object has been persisted, in your case it was the index pattern. Since you have disabled this, and the Kibana index has been successfully created, you should be able to again disable this setting. It's also possible to use a pattern based white/black list for this setting. For example `action.auto_create_index: +.kibana*, -*` (+ meaning allowed, and - meaning disallowed)


the doc for es6.2
>https://www.elastic.co/guide/en/elasticsearch/reference/6.2/installing-xpack-es.html

lead you to wrong way.

things like:
```
action.auto_create_index: .monitoring*,.watches,.triggered_watches,.watcher-history*,.ml*,wazuh-alerts-3.x-*,wazuh-monitoring-3.x-*
```

won't work,and cause certain outside tool like filebeat failed to create wazuh-alerts-3.x-YYYY.DD.MM index.



###and another thing may need to notice here.

Ok, thanks! That seems to affect both

```
"persistent": {
        "action.auto_create_index": "false" 
    }
```

and

```
"transient": {
        "action.auto_create_index": "false" 
    }
```

so perhaps that was the answer I was looking for.

In other words: this is not enough:

```
PUT _cluster/settings
{
    "persistent": {
        "action.auto_create_index": "false" 
    }
}
```

This should be correct:

```
PUT _cluster/settings
{
    "persistent": {
        "action.auto_create_index": "false" 
    },
  "transient": {
        "action.auto_create_index": "false" 
    }
}
```

thanks!




#enable es logging for debug

Let’s see more details about your deployment:

Journal for the user elasticsearch
```
journalctl -u elasticsearch > /tmp/output.txt && cat /tmp/output.txt
```
Logs from Elasticsearch
```
tail -80 /var/log/elasticsearch/<elasticsearch|cluster-name>.log
```

Add this line to your logging settings:
```
echo 'rootLogger.level = debug' >> /etc/elasticsearch/log4j2.properties
```
Now restart Elasticsearch.

Now if you can send me a file with the content of at least 200 lines of your Elasticsearch log it would be helpful.

With the above modification the log file will be more verbose, so please after your node is crashed again, execute the next command:
```
tail -200 /var/log/elasticsearch/elasticsearch.log > /tmp/output.txt
```
Then, send me the /tmp/output.txt content or the file so I can look for other technical logs that may help here.







#elastic search HA

>https://logz.io/blog/elasticsearch-cluster-tutorial/

Unless you are using [Elasticsearch](https://logz.io/blog/elasticsearch-tutorial/) for development and testing, creating and maintaining an Elasticsearch cluster will be a task that will occupy quite a lot of your time. Elasticsearch is an extremely powerful search and analysis engine, and part of this power lies in the ability to scale it for better performance and stability.

This tutorial will provide some information on how to set up an Elasticsearch cluster, and will add some operational tips and best practices to help you get started. It should be stressed though that each Elasticsearch setup will likely differ from one another depending on multiple factors, including the workload on the servers, the amount of indexed data, hardware specifications, and even the experience of the operators.

## What is an Elasticsearch cluster?

As the name implies, an Elasticsearch cluster is a group of one or more Elasticsearch nodes instances that are connected together. The power of an Elasticsearch cluster lies in the distribution of tasks, searching and indexing, across all the nodes in the cluster.

The nodes in the Elasticsearch cluster can be assigned different jobs or responsibilities:

- Data nodes — stores data and executes data-related operations such as search and aggregation
- Master nodes — in charge of cluster-wide management and configuration actions such as adding and removing nodes
- Client nodes — forwards cluster requests to the master node and data-related requests to data nodes
- Ingest nodes — for pre-processing documents before indexing
- _\*Note: Tribe nodes, which were similar to cross-cluster or federated nodes, were deprecated with Elasticsearch 5.4_

By default, each node is automatically assigned a unique identifier, or name, that is used for management purposes and becomes even more important in a multi-node, or clustered, environment.

When installed, a single Elasticsearch node will form a new single-node cluster entitled “elasticsearch,” but as we shall see later on in this article it can also be configured to join an existing cluster using the cluster name. Needless to say, these nodes need to be able to identify each other to be able to connect.

## Installing an Elasticsearch Cluster

As always, there are multiple ways of setting up an Elasticsearch cluster. You can use a configuration management tool such as Puppet or Ansible to automate the process. In this case, though, we will be showing you how to manually set up a cluster consisting of one master node and two data nodes, all on Ubuntu 16.04 instances on AWS EC2 running in the same VPC. The security group was configured to enable access from anywhere using SSH and TCP 5601 (Kibana).

### Installing Java

Elasticsearch is built on Java and requires at least Java 8 (1.8.0\_131 or later) to run. Our first step, therefore, is to install Java 8 on all the nodes in the cluster. Please note that the same version should be installed on all Elasticsearch nodes in the cluster.

**Repeat the following steps on all the servers designated for your cluster.**

First, update your system:

sudo apt-get update

Copy

Then, install Java with:

sudo apt-get install default-jre

Copy

Checking your Java version now should give you the following output or similar:

openjdk version "1.8.0\_151"
OpenJDK Runtime Environment (build 1.8.0\_151-8u151-b12-0ubuntu0.16.04.2-b12)
OpenJDK 64-Bit Server VM (build 25.151-b12, mixed mode)

Copy

### Installing Elasticsearch nodes

Our next step is to install Elasticsearch. **As before, repeat the steps in this section on all your servers.**

First, you need to add Elastic’s signing key so that the downloaded package can be verified (skip this step if you’ve already installed packages from Elastic):

wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -

Copy

For Debian, we need to then install the apt-transport-https package:

sudo apt-get install apt-transport-https

Copy

The next step is to add the repository definition to your system:

echo "deb https://artifacts.elastic.co/packages/6.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-6.x.list

Copy

All that’s left to do is to update your repositories and install Elasticsearch:

sudo apt-get update 
sudo apt-get install elasticsearch

Copy

## Configuring the Elasticsearch cluster

Our next step is to set up the cluster so that the nodes can connect and communicate with each other.

**For each node**, open the Elasticsearch configuration file:

sudo vim /etc/elasticsearch/elasticsearch.yml

Copy

This file is quite long, and contains multiple settings for different sections. Browse through the file, and enter the following configurations (replace the IPs with your node IPs):

#give your cluster a name.
cluster.name: my-cluster

#give your nodes a name (change node number from node to node).
node.name: "es-node-1"

#define node 1 as master-eligible:
node.master: true

#define nodes 2 and 3 as data nodes:
node.data: true

#enter the private IP and port of your node:
network.host: 172.11.61.27
http.port: 9200

#detail the private IPs of your nodes:
discovery.zen.ping.unicast.hosts: \["172.11.61.27", "172.31.22.131","172.31.32.221"\]

Copy

Save and exit.

## Running your Elasticsearch cluster

You are now ready to start your Elasticsearch nodes and verify they are communicating with each other as a cluster.

**For each instance**, run the following command:

sudo service elasticsearch start

Copy

If everything was configured correctly, your Elasticsearch cluster should be up and running. To verify everything is working as expected, query Elasticsearch from any of the cluster nodes:

curl -XGET 'http://localhost:9200/\_cluster/state?pretty'

Copy

The response should detail the cluster and its nodes:

{
  "cluster\_name" : "my-cluster",
  "compressed\_size\_in\_bytes" : 351,
  "version" : 4,
  "state\_uuid" : "3LSnpinFQbCDHnsFv-Z8nw",
  "master\_node" : "IwEK2o1-Ss6mtx50MripkA",
  "blocks" : { },
  "nodes" : {
    "IwEK2o1-Ss6mtx50MripkA" : {
      "name" : "es-node-2",
      "ephemeral\_id" : "x9kUrr0yRh--3G0ckESsEA",
      "transport\_address" : "172.31.50.123:9300",
      "attributes" : { }
    },
    "txM57a42Q0Ggayo4g7-pSg" : {
      "name" : "es-node-1",
      "ephemeral\_id" : "Q370o4FLQ4yKPX4\_rOIlYQ",
      "transport\_address" : "172.31.62.172:9300",
      "attributes" : { }
    },
    "6YNZvQW6QYO-DX31uIvaBg" : {
      "name" : "es-node-3",
      "ephemeral\_id" : "mH034-P0Sku6Vr1DXBOQ5A",
      "transport\_address" : "172.31.52.220:9300",
      "attributes" : { }
    }
  },
 …

Copy

## Elasticsearch cluster configurations for production

We already defined the different roles for the nodes in our cluster, but there are some additional recommended settings for a cluster running in a production environment.

### Avoiding “Split Brain”

A “split-brain” situation is when communication between nodes in the cluster fails due to either a network failure or an internal failure with one of the nodes. In this kind of scenario, more than one node might believe it is the master node, leading to a state of data inconsistency.

For avoiding this situation, we can make changes to the _discovery.zen.minimum\_master\_nodes_ directive in the Elasticsearch configuration file which determines how many nodes need to be in communication (quorum) to elect a master.

A best practice to determine this number is to use the following formula to decide this number: N/2 + 1. N is the number of master eligible nodes in the cluster. You then round down the result to the nearest integer.

In the case of a cluster with three nodes, then:

discovery.zen.minimum\_master\_nodes: 2

Copy

### Adjusting JVM heap size

To ensure Elasticsearch has enough operational leeway, the default JVM heap size (min/max 1 GB) should be adjusted.

As a rule of the thumb, the maximum heap size should be set up to 50% of your RAM, but no more than 32GB (due to Java pointer inefficiency in larger heaps). Elastic also recommends that the value for maximum and minimum heap size be identical.

These value can be configured using the Xmx and Xms settings in the _jvm.options_ file.

On DEB:

sudo vim /etc/elasticsearch/jvm.options

-Xms2g
-Xmx2g

Copy

### Disabling swapping

Swapping out unused memory is a known behavior but in the context of Elasticsearch can result in disconnects, bad performance and in general — an unstable cluster.

To avoid swapping you can either disable all swapping (recommended if Elasticsearch is the only service running on the server), or you can use _mlockall_ to lock the Elasticsearch process to RAM.

To do this, open the Elasticsearch configuration file on all nodes in the cluster:

sudo vim /etc/elasticsearch/elasticsearch.yml

Copy

Uncomment the following line:

bootstrap.mlockall: true

Copy

Next, open the /etc/default/elasticsearch file:

sudo vim /etc/default/elasticsearch

Copy

Make the following configurations:

MAX\_LOCKED\_MEMORY=unlimited

Copy

Restart Elasticsearch when you’re done.

### Adjusting virtual memory

To avoid running out of virtual memory, increase the amount of limits on mmap counts:

sudo vim /etc/sysctl.conf

Copy

Update the relevant setting accordingly:

vm.max\_map\_count=262144

Copy

On DEB/RPM, this setting is configured automatically.

### Increasing open file descriptor limit

Another important configuration is the limit of open file descriptors. Since Elasticsearch makes use of a large amount of file descriptors, you must ensure the defined limit is enough otherwise you might end up losing data.

The common recommendation for this setting is 65,536 and higher. On DEB/RPM the default settings are already configured to suit this requirement but you can of course fine tune it.

sudo vim  /etc/security/limits.conf

Copy

Set the limit:

  - nofile 65536

Copy

## Elasticsearch Cluster APIs

Elasticsearch supports a large number of cluster-specific API operations that allow you to manage and monitor your Elasticsearch cluster. Most of the APIs allow you to define which Elasticsearch node to call using either the internal node ID, its name or its address. 

Below is a list of a few of the more basic API operations you can use. For advanced usage of cluster APIs, [read this blog post](https://logz.io/blog/elasticsearch-cheat-sheet/). 

### Cluster Health

This API can be used to see general info on the cluster and gauge its health:
```
curl -XGET 'localhost:9200/\_cluster/health?pretty'

```

Response:
```
{
  "cluster\_name" : "my-cluster",
  "status" : "green",
  "timed\_out" : false,
  "number\_of\_nodes" : 3,
  "number\_of\_data\_nodes" : 3,
  "active\_primary\_shards" : 0,
  "active\_shards" : 0,
  "relocating\_shards" : 0,
  "initializing\_shards" : 0,
  "unassigned\_shards" : 0,
  "delayed\_unassigned\_shards" : 0,
  "number\_of\_pending\_tasks" : 0,
  "number\_of\_in\_flight\_fetch" : 0,
  "task\_max\_waiting\_in\_queue\_millis" : 0,
  "active\_shards\_percent\_as\_number" : 100.0
}

```

### Cluster State

This API can be sued to see a detailed status report on your entire cluster. You can filter results by specifying parameters in the call URL.
```
curl -XGET 'localhost:9200/\_cluster/state?pretty'
```
Response:
```
{
  "cluster\_name" : "my-cluster",
  "compressed\_size\_in\_bytes" : 347,
  "version" : 4,
  "state\_uuid" : "uMi5OBtAS8SSRJ9hw1-gUg",
  "master\_node" : "sqT\_y5ENQ9SdjHiE0oco\_g",
  "blocks" : { },
  "nodes" : {
    "sqT\_y5ENQ9SdjHiE0oco\_g" : {
      "name" : "node-1",
      "ephemeral\_id" : "-HDzovR0S0e-Nn8XJ-GWPA",
      "transport\_address" : "172.31.56.131:9300",
      "attributes" : { }
    },
    "mO0d0hYiS1uB--NoWuWyHg" : {
      "name" : "node-3",
      "ephemeral\_id" : "LXjx86Q5TrmefDoq06MY1A",
      "transport\_address" : "172.31.58.61:9300",
      "attributes" : { }
    },
    "it1V-5bGT9yQh19d8aAO0g" : {
      "name" : "node-2",
      "ephemeral\_id" : "lCJja\_QtTYauP3xEWg5NBQ",
      "transport\_address" : "172.31.62.65:9300",
      "attributes" : { }
    }
  },
  "metadata" : {
    "cluster\_uuid" : "8AqSmmKdQgmRVPsVxyxKrw",
    "templates" : { },
    "indices" : { },
    "index-graveyard" : {
      "tombstones" : \[ \]
    }
  },
  "routing\_table" : {
    "indices" : { }
  },
  "routing\_nodes" : {
    "unassigned" : \[ \],
    "nodes" : {
      "it1V-5bGT9yQh19d8aAO0g" : \[ \],
      "sqT\_y5ENQ9SdjHiE0oco\_g" : \[ \],
      "mO0d0hYiS1uB--NoWuWyHg" : \[ \]
    }
  },
  "snapshots" : {
    "snapshots" : \[ \]
  },
  "restore" : {
    "snapshots" : \[ \]
  },
  "snapshot\_deletions" : {
    "snapshot\_deletions" : \[ \]
  }
}
```

### Cluster Stats

Extremely useful for monitoring performance metrics on your entire cluster:
```
curl -XGET 'localhost:9200/\_cluster/stats?human&pretty'

```

Response:
```
{
  "\_nodes" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "cluster\_name" : "my-cluster",
  "timestamp" : 1517224098451,
  "status" : "green",
  "indices" : {
    "count" : 0,
    "shards" : { },
    "docs" : {
      "count" : 0,
      "deleted" : 0
    },
    "store" : {
      "size" : "0b",
      "size\_in\_bytes" : 0
    },
    "fielddata" : {
      "memory\_size" : "0b",
      "memory\_size\_in\_bytes" : 0,
      "evictions" : 0
    },
    "query\_cache" : {
      "memory\_size" : "0b",
      "memory\_size\_in\_bytes" : 0,
      "total\_count" : 0,
      "hit\_count" : 0,
      "miss\_count" : 0,
      "cache\_size" : 0,
      "cache\_count" : 0,
      "evictions" : 0
    },
    "completion" : {
      "size" : "0b",
      "size\_in\_bytes" : 0
    },
    "segments" : {
      "count" : 0,
      "memory" : "0b",
      "memory\_in\_bytes" : 0,
      "terms\_memory" : "0b",
      "terms\_memory\_in\_bytes" : 0,
      "stored\_fields\_memory" : "0b",
      "stored\_fields\_memory\_in\_bytes" : 0,
      "term\_vectors\_memory" : "0b",
      "term\_vectors\_memory\_in\_bytes" : 0,
      "norms\_memory" : "0b",
      "norms\_memory\_in\_bytes" : 0,
      "points\_memory" : "0b",
      "points\_memory\_in\_bytes" : 0,
      "doc\_values\_memory" : "0b",
      "doc\_values\_memory\_in\_bytes" : 0,
      "index\_writer\_memory" : "0b",
      "index\_writer\_memory\_in\_bytes" : 0,
      "version\_map\_memory" : "0b",
      "version\_map\_memory\_in\_bytes" : 0,
      "fixed\_bit\_set" : "0b",
      "fixed\_bit\_set\_memory\_in\_bytes" : 0,
      "max\_unsafe\_auto\_id\_timestamp" : -9223372036854775808,
      "file\_sizes" : { }
    }
  },
  "nodes" : {
    "count" : {
      "total" : 3,
      "data" : 3,
      "coordinating\_only" : 0,
      "master" : 3,
      "ingest" : 3
    },
    "versions" : \[
      "6.1.2"
    \],
    "os" : {
      "available\_processors" : 3,
      "allocated\_processors" : 3,
      "names" : \[
        {
          "name" : "Linux",
          "count" : 3
        }
      \],
      "mem" : {
        "total" : "10.4gb",
        "total\_in\_bytes" : 11247157248,
        "free" : "4.5gb",
        "free\_in\_bytes" : 4915200000,
        "used" : "5.8gb",
        "used\_in\_bytes" : 6331957248,
        "free\_percent" : 44,
        "used\_percent" : 56
      }
    },
    "process" : {
      "cpu" : {
        "percent" : 10
      },
      "open\_file\_descriptors" : {
        "min" : 177,
        "max" : 178,
        "avg" : 177
      }
    },
    "jvm" : {
      "max\_uptime" : "6m",
      "max\_uptime\_in\_millis" : 361766,
      "versions" : \[
        {
          "version" : "1.8.0\_151",
          "vm\_name" : "OpenJDK 64-Bit Server VM",
          "vm\_version" : "25.151-b12",
          "vm\_vendor" : "Oracle Corporation",
          "count" : 3
        }
      \],
      "mem" : {
        "heap\_used" : "252.1mb",
        "heap\_used\_in\_bytes" : 264450008,
        "heap\_max" : "2.9gb",
        "heap\_max\_in\_bytes" : 3195076608
      },
      "threads" : 63
    },
    "fs" : {
      "total" : "23.2gb",
      "total\_in\_bytes" : 24962703360,
      "free" : "19.4gb",
      "free\_in\_bytes" : 20908818432,
      "available" : "18.2gb",
      "available\_in\_bytes" : 19570003968
    },
    "plugins" : \[ \],
    "network\_types" : {
      "transport\_types" : {
        "netty4" : 3
      },
      "http\_types" : {
        "netty4" : 3
      }
    }
  }
}

```

check load status:
```
# curl -X GET "localhost:9200/_cat/thread_pool"
```

```
bjzw_74_118 analyze             0 0       0
bjzw_74_118 ccr                 0 0       0
bjzw_74_118 fetch_shard_started 0 0       0
bjzw_74_118 fetch_shard_store   0 0       0
bjzw_74_118 flush               0 0       0
bjzw_74_118 force_merge         0 0       0
bjzw_74_118 generic             3 0       0
bjzw_74_118 get                 0 0       0
bjzw_74_118 index               0 0       0
bjzw_74_118 listener            0 0       0
bjzw_74_118 management          1 0       0
bjzw_74_118 ml_autodetect       0 0       0
bjzw_74_118 ml_datafeed         0 0       0
bjzw_74_118 ml_utility          0 0       0
bjzw_74_118 refresh             0 0       0
bjzw_74_118 rollup_indexing     0 0       0
bjzw_74_118 search              0 0       0
bjzw_74_118 search_throttled    0 0       0
bjzw_74_118 security-token-key  0 0       0
bjzw_74_118 snapshot            0 0       0
bjzw_74_118 warmer              0 0       0
bjzw_74_118 watcher             0 0       0
bjzw_74_118 write               0 0 6696180
bjzw_74_106 analyze             0 0       0
bjzw_74_106 ccr                 0 0       0
bjzw_74_106 fetch_shard_started 0 0       0
bjzw_74_106 fetch_shard_store   0 0       0
bjzw_74_106 flush               0 0       0
bjzw_74_106 force_merge         0 0       0
bjzw_74_106 generic             4 0       0
bjzw_74_106 get                 0 0       0
bjzw_74_106 index               0 0       0
bjzw_74_106 listener            0 0       0
bjzw_74_106 management          1 0       0
bjzw_74_106 ml_autodetect       0 0       0
bjzw_74_106 ml_datafeed         0 0       0
bjzw_74_106 ml_utility          0 0       0
bjzw_74_106 refresh             0 0       0
bjzw_74_106 rollup_indexing     0 0       0
bjzw_74_106 search              0 0       0
bjzw_74_106 search_throttled    0 0       0
bjzw_74_106 security-token-key  0 0       0
bjzw_74_106 snapshot            0 0       0
bjzw_74_106 warmer              0 0       0
bjzw_74_106 watcher             0 0       0
bjzw_74_106 write               0 0       0
bjzw_74_117 analyze             0 0       0
bjzw_74_117 ccr                 0 0       0
bjzw_74_117 fetch_shard_started 0 0       0
bjzw_74_117 fetch_shard_store   0 0       0
bjzw_74_117 flush               0 0       0
bjzw_74_117 force_merge         0 0       0
bjzw_74_117 generic             5 0       0
bjzw_74_117 get                 0 0       0
bjzw_74_117 index               0 0       0
bjzw_74_117 listener            0 0       0
bjzw_74_117 management          1 0       0
bjzw_74_117 ml_autodetect       0 0       0
bjzw_74_117 ml_datafeed         0 0       0
bjzw_74_117 ml_utility          0 0       0
bjzw_74_117 refresh             1 0       0
bjzw_74_117 rollup_indexing     0 0       0
bjzw_74_117 search              0 0       0
bjzw_74_117 search_throttled    0 0       0
bjzw_74_117 security-token-key  0 0       0
bjzw_74_117 snapshot            0 0       0
bjzw_74_117 warmer              0 0       0
bjzw_74_117 watcher             0 0       0
bjzw_74_117 write               0 0 7198014
bjzw_74_116 analyze             0 0       0
bjzw_74_116 ccr                 0 0       0
bjzw_74_116 fetch_shard_started 0 0       0
bjzw_74_116 fetch_shard_store   0 0       0
bjzw_74_116 flush               0 0       0
bjzw_74_116 force_merge         0 0       0
bjzw_74_116 generic             5 0       0
bjzw_74_116 get                 0 0       0
bjzw_74_116 index               0 0       0
bjzw_74_116 listener            0 0       0
bjzw_74_116 management          1 0       0
bjzw_74_116 ml_autodetect       0 0       0
bjzw_74_116 ml_datafeed         0 0       0
bjzw_74_116 ml_utility          0 0       0
bjzw_74_116 refresh             0 0       0
bjzw_74_116 rollup_indexing     0 0       0
bjzw_74_116 search              0 0       0
bjzw_74_116 search_throttled    0 0       0
bjzw_74_116 security-token-key  0 0       0
bjzw_74_116 snapshot            0 0       0
bjzw_74_116 warmer              0 0       0
bjzw_74_116 watcher             0 0       0
bjzw_74_116 write               2 0 6356860
bjzw_74_107 analyze             0 0       0
bjzw_74_107 ccr                 0 0       0
bjzw_74_107 fetch_shard_started 0 0       0
bjzw_74_107 fetch_shard_store   0 0       0
bjzw_74_107 flush               0 0       0
bjzw_74_107 force_merge         0 0       0
bjzw_74_107 generic             1 0       0
bjzw_74_107 get                 0 0       0
bjzw_74_107 index               0 0       0
bjzw_74_107 listener            0 0       0
bjzw_74_107 management          1 0       0
bjzw_74_107 ml_autodetect       0 0       0
bjzw_74_107 ml_datafeed         0 0       0
bjzw_74_107 ml_utility          0 0       0
bjzw_74_107 refresh             1 0       0
bjzw_74_107 rollup_indexing     0 0       0
bjzw_74_107 search              0 0       0
bjzw_74_107 search_throttled    0 0       0
bjzw_74_107 security-token-key  0 0       0
bjzw_74_107 snapshot            0 0       0
bjzw_74_107 warmer              0 0       0
bjzw_74_107 watcher             0 0       0
bjzw_74_107 write               7 0     261

```

You can also target specific groups of nodes with node filters.

### Nodes Stats

If you want to inspect metrics for specific nodes in the cluster, use this API. You can see info for all nodes, a specific node, or ask to see only index or OS/process specific stats.

All nodes:
```
curl -XGET 'localhost:9200/\_nodes/stats?pretty'

```

A specific node:
```
curl -XGET 'localhost:9200/\_nodes/node-1/stats?pretty'

```

Index-only stats:
```
curl -XGET 'localhost:9200/\_nodes/stats/indices?pretty'

```
You can get any of the specific metrics for any single node with the following structure:
```
curl -XGET 'localhost:9200/\_nodes/stats/ingest?pretty'

```

Or multiple nodes with the following structure:
```
curl -XGET 'localhost:9200/\_nodes/stats/ingest,fs?pretty'

```
Or all metrics with either of these two formats:
```
curl -XGET 'localhost:9200/\_nodes/stats/\_all?pretty'

curl -XGET 'localhost:9200/\_nodes/stats?metric=\_all?pretty'
```

### Nodes Info

If you want to collect information on any or all of your cluster nodes, use this API.

Retrieve for a single node:  
```
curl -XGET ‘localhost:9200/\_nodes/?pretty’

```

Or multiple nodes:
```
curl -XGET ‘localhost:9200/\_nodes/node1,node2?pretty’

```
Retrieve data on plugins or ingest:
```
curl -XGET ‘localhost:9200/\_nodes/plugins
```
```
curl -XGET ‘localhost:9200/\_nodes/ingest

```

Information about ingest processors should appear like this (with many more than the three types shown in the example):
```
{
  "\_nodes": …
  "cluster\_name": "elasticsearch",
  "nodes": {
   "toTaLLyran60m5amp13": {
	"ingest": {
	   "processors": \[
	     {
	       "type": "uppercase"
	     },
	     {
	       "type": "lowercase"
	     },
	     {
	       "type": "append"
	     }
	   \]
	 }
    }
  }
}

```

### Pending Cluster Tasks

This API tracks changes at the cluster level, including but not limited to updated mapping, failed sharding, and index creation.

The following GET should return a list of tasks:  
```
curl -XGET ‘localhost:9200/\_cluster/pending\_tasks?pretty’
```

### Task Management

Similar to the Pending Cluster Tasks API, the Task Management API will get data on currently running tasks on respective nodes.

To get info on all currently executing tasks, enter:  
```
curl -XGET "localhost:9200/\_tasks

```

To get current tasks by specific nodes, AND additionally cluster-related tasks, enter the node names as such and then append &actions to the GET:  
```
curl -XGET ‘localhost:9200/\_tasks?nodes=node1,node2&actions=cluster:\*&pretty’

```

Retrieve info about a specific task (or its child tasks) by entering \_tasks/ and then the task’s individual ID:
```
curl -XGET ‘localhost:9200/\_tasks/43r315an3xamp13’

```

And for child tasks:  
```
curl -XGET ‘localhost:9200/\_tasks?parent\_task\_id=43r315an3xamp13’

```

This API also supports reindexing, search, task grouping and task cancelling.

### Remote Cluster Info

Get remote cluster info with:
```
curl -XGET 'localhost:9200/\_remote/info?pretty'

```

### Voting Configuration Exclusions

This will remove master-eligible nodes.  
Remove all exclusions by:
```
curl -X DELETE ‘localhost:9200/\_cluster/voting\_config\_exclusions?pretty’

```

Or add a node to the exclusions list:
```
curl -X POST ‘localhost:9200/\_cluster/voting\_config\_exclusions/node1?pretty’

```

## What next?

“When all else fails, read the fuc%^&\* manual” goes the famous saying. Thing is, the manual in question, and the technology it documents, are not straightforward to say the least.

More on the subject:

- [Elasticsearch Tutorial](https://logz.io/blog/elasticsearch-tutorial/)
- [Logstash Tutorial](https://logz.io/blog/logstash-tutorial/)
- [Kibana Tutorial](https://logz.io/blog/kibana-tutorial/)

This tutorial made a brave attempt to provide users with the basics of setting up and configuring their first Elasticsearch cluster, knowing full well that it is virtually impossible to provide instructions that suit every environment and use case.

Together with this tutorial, I strongly recommend doing additional research. Other than Elastic’s official documentation, here are some additional informative resources:

- [Elasticsearch cluster setup and update: read, plan and test](https://logz.io/blog/elasticsearch-cluster-setup/)
- [Elasticsearch performance monitoring](https://logz.io/blog/elasticsearch-performance-monitoring/)
- [Designing the perfect Elasticsearch cluster](https://thoughts.t37.net/designing-the-perfect-elasticsearch-cluster-the-almost-definitive-guide-e614eabc1a87)

Good luck!




#es cluster with xpack

###best way to start with it

> Strangely my head plugin which is installed as chrome extension is asking for the password to connect with ES before installing x-pack it was not asking for password.
> 
> Why ES is not starting properly and which password I am supposed to put in head so that it can fetch the ES information?

Because by installing X-Pack with the defaults you have secured the REST API that your plugin previously spoke to unsecured.

> I am not able to read the content of mentioned file.

This is by design, it's an encrypted Java keystore meant to protect secrets.

You can check which keys (but not their passwords) the keystore contains by entering

`bin/elasticsearch-keystore list`

in the path for that node (note it is _not_ the `x-pack` directory within `bin/`.

As this is on your laptop, your easiest option is probably to stop your cluster, remove X-Pack from all 7 nodes with

`bin/elasticsearch-plugin remove x-pack`

double check a keystore is not present in any config path, then reinstall to _all_ nodes with

`bin/elasticsearch-plugin install x-pack`

then start the cluster back up and run

`bin/x-pack/setup-passwords interactive`

###if you already setup one node with xpack,now you gonna convert it into cluster

edit the elasticsearch.yml,
```
cluster.name: wazuh-clusteres
node.name: es_datanode-1
network.host: 192.168.40.112
discovery.seed_hosts: ["192.168.40.243", "192.168.40.112"]
cluster.initial_master_nodes: ["wazuh-ssl", "es_datanode-1"]
```
cp the certs folder and other xpack settings you created on master node to this data node.
then start service and check.

###configure kibana to use multi backend es.
kibana.yml
```
elasticsearch.hosts: ["https://192.168.40.243:9200","https://192.168.40.112:9200","https://192.168.40.129:9200"]
elasticsearch.sniffOnStart: true
```
you may wanna config Related configurations include elasticsearch.sniffInterval, and elasticsearch.sniffOnConnectionFault too,but here I don't care.

>https://www.elastic.co/guide/en/kibana/7.7/settings.html



seems with xpack installed the kibana cannot swith es backend normally,error like below

```
{"type":"log","@timestamp":"2020-06-03T02:11:09Z","tags":["error","elasticsearch","telemetry-fetcher"],"pid":3911,"message":"Request error, retrying\nGET https://192.168.40.129:9200/_nodes/_all/http?filter_path=nodes.*.http.publish_address%2Cnodes.*.name%2Cnodes.*.hostname%2Cnodes.*.host%2Cnodes.*.version => Hostname/IP does not match certificate's altnames: IP: 192.168.40.129 is not in the cert's list: 192.168.40.243"}
```
waitting for solve.



#kibana search time out 
```
elasticsearch.shardTimeout:

Time in milliseconds for Elasticsearch to wait for responses from shards. Set to 0 to disable. Default: 30000
```

#6.x es default node master 

  候选主节点的设置方法是设置node.master为true，默认情况下，node.master和node.data的值都为true，即该节点既可以做候选主节点也可以做数据节点。由于数据节点承载了数据的操作，负载通常都很高，所以随着集群的扩大，建议将二者分离，设置专用的候选主节点。当我们设置node.data为false，就将节点设置为专用的候选主节点了。



##secure a cluster with xpack SSL part


###1.generate a **root CA** for our cluster.

>https://www.elastic.co/guide/en/elasticsearch/reference/current/configuring-tls.html#node-certificates

The recommended approach for validating certificate authenticity in an Elasticsearch cluster is to trust the certificate authority (CA) that signed the certificate. By doing this, as nodes are added to your cluster they just need to use a certificate signed by the same CA and the node is automatically allowed to join the cluster.


-  **Create a certificate authority for your Elasticsearch cluster.**
    
For example, use the `elasticsearch-certutil ca` command:
```
bin/elasticsearch-certutil ca
```
You can configure the cluster to trust all nodes that have a certificate that has been signed by this CA.

The command outputs a single file, with a default name of `elastic-stack-ca.p12`. This file is a PKCS#12 keystore that contains the public certificate for your CA and the private key that is used to sign the certificates for each node.

The `elasticsearch-certutil` command also prompts you for a password to protect the file and key. If you plan to add more nodes to your cluster in the future, retain a copy of the file and remember its password.


- **Generate a certificate and private key for each node in your cluster.**

For example, use the `elasticsearch-certutil cert` command:
```
bin/elasticsearch-certutil cert --ca elastic-stack-ca.p12
```
The output is a single PKCS#12 keystore that includes the node certificate, node key, and CA certificate.

You are also prompted for a password. You can enter a password for your certificate and key, or you can leave the password blank by pressing Enter.

By default `elasticsearch-certutil` generates certificates that have no hostname information in them (that is, they do not have any Subject Alternative Name fields). This means that you can use the certificate for every node in your cluster, but you must turn off hostname verification as shown in the configuration below.

If you want to use hostname verification within your cluster, run the `elasticsearch-certutil cert` command once for each of your nodes and provide the `--name`, `--dns` and `--ip` options.
>When you generate the Logstash certificate with `certutil` you should pass `-ip 10.203.207.76`

- **Generate additional certificates specifically for encrypting HTTP client communications.**

For example, use the elasticsearch-certutil http command:
```
bin/elasticsearch-certutil http
```
>configure to use the CA cert we generated before.

This command guides you through the process of generating the appropriate certificates for use in Elasticsearch and Kibana. If you created a CA for your cluster, you can re-use it by supplying its location when prompted.

Copy the node certificates to the appropriate locations.

Copy the applicable files into the Elasticsearch configuration directory on each node.

For each additional Elastic product that you want to configure, copy the certificates to the relevant configuration directory.

>this will generate a PKCS12 cert package zip later will be used by http client like kibana,wazuh etc.


###2.setting up transport ssl(elasticsearch node internal communication encryption):
>https://www.elastic.co/guide/en/elasticsearch/reference/current/configuring-tls.html#tls-transport

The transport networking layer is used for internal communication between nodes in a cluster. When security features are enabled, you must use TLS to ensure that communication between the nodes is encrypted.

1. [Generate node certificates](https://www.elastic.co/guide/en/elasticsearch/reference/current/configuring-tls.html#node-certificates "Generating node certificates").
2. Enable TLS and specify the information required to access the node’s certificate.
    
    - If the signed certificate is in PKCS#12 format, add the following information to the `elasticsearch.yml` file on each node:
```
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate 
xpack.security.transport.ssl.keystore.path: elastic-certificates.p12 
xpack.security.transport.ssl.truststore.path: elastic-certificates.p12 
```

Enable TLS and specify the information required to access the node’s certificate. For example:

Update the elasticsearch.yml file on each node with the location of the certificates.

If the certificates are in PKCS#12 format:
```
xpack.security.http.ssl.enabled: true
xpack.security.http.ssl.keystore.path: "http.p12"
```
If you have certificates in PEM format:
```
xpack.security.http.ssl.enabled: true
xpack.security.http.ssl.key:  /home/es/config/node1_http.key 
xpack.security.http.ssl.certificate: /home/es/config/node1_http.crt 
xpack.security.http.ssl.certificate_authorities: [ "/home/es/config/ca.crt" ] 
```


###Request error, retrying\nHEAD https://xxxxxx:9200/ => Hostname/IP doesn't match certificate's altnames: "Host: xxxxx.eu-west-2.elb.amazonaws.com. is not in the cert's altnames: DNS:xxxxxx""

by default even certificates from trusted CAs must contain the correct hostname. If you want to explicitly disable the hostname check, try setting `xpack.monitoring.elasticsearch.ssl.verificationMode` to `"certificate"` instead of `"full"`.

sometimes this could caused by wrong conf in elasticsearch.yml
for example
```
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.enabled: true
```
double line in this conf file.

>**remember to give right file access of certs for elasticsearch chown,chmod



###configure filebeat to using https 

You can specify SSL options when you configure:

- [outputs](https://www.elastic.co/guide/en/beats/filebeat/current/configuring-output.html "Configure the output") that support SSL
- the [Kibana endpoint](https://www.elastic.co/guide/en/beats/filebeat/current/setup-kibana-endpoint.html "Configure the Kibana endpoint")

Example output config with SSL enabled:
```
output.elasticsearch.hosts: ["https://192.168.1.42:9200"]
output.elasticsearch.ssl.certificate_authorities: ["/etc/pki/root/ca.pem"]
output.elasticsearch.ssl.certificate: "/etc/pki/client/cert.pem" 
output.elasticsearch.ssl.key: "/etc/pki/client/cert.key"
```
Also see [_Secure communication with Logstash_](https://www.elastic.co/guide/en/beats/filebeat/current/configuring-ssl-logstash.html "Secure communication with Logstash").

Example Kibana endpoint config with SSL enabled:
```
setup.kibana.host: "https://192.0.2.255:5601" setup.kibana.ssl.enabled: true
setup.kibana.ssl.certificate_authorities: ["/etc/pki/root/ca.pem"]
setup.kibana.ssl.certificate: "/etc/pki/client/cert.pem" setup.kibana.ssl.key: "/etc/pki/client/cert.key"
```

- if your cert was generated followed elasticsearch docs,it will be pkcs12 format by default.which you need to convert to pem for use

to disable hostname verify in kibana,add this line in kibana.yml

```
xpack.monitoring.elasticsearch.ssl.verificationMode: certificate
```


##PKCS12 to PEM

Try:

```
openssl pkcs12 -in path.p12 -out newfile.crt.pem -clcerts -nokeys
openssl pkcs12 -in path.p12 -out newfile.key.pem -nocerts -nodes
```

After that you have:

- certificate in newfile.crt.pem
- private key in newfile.key.pem

To put the certificate and key in the same file use the following

```
openssl pkcs12 -in path.p12 -out newfile.pem
```

If you need to input the PKCS#12 password directly from the command line (e.g. a script), just add `-passin pass:${PASSWORD}`:

```
openssl pkcs12 -in path.p12 -out newfile.crt.pem -clcerts -nokeys -passin 'pass:P@s5w0rD'
``` 

>https://stackoverflow.com/questions/15144046/converting-pkcs12-certificate-into-pem-using-openssl