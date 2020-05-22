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
/usr/share/filebeat/bin/filebeat test config
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

