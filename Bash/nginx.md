#Proxy for ssl website  


###generate ssl keypair
```
$ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/certs/cert.key -out /etc/nginx/certs/cert.crt
```
optional(ECC):
```
openssl ecparam -genkey -name secp256r1 | openssl ec -out ecc.key
openssl req -new -key ecc.key -out ecc.csr
```
| 对称加密 Key 长度	| RSA Key 长度 |	ECC Key 长度
|  ----  | ----  |
|80	|1024	|160
|112	|2048	|224
|128|	3072|	256
|192|	7680	|384
|256|	15360	|521

generate a ssl keypair  
>https://imququ.com/post/optimize-tls-handshake.html


>when it ask for info
>example:  


```
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout test.com.key -out test.com.crt
Generating a 2048 bit RSA private key
..............................................................................+++
......................+++
writing new private key to 'test.com.key'
-----
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [XX]:cn
State or Province Name (full name) []:Hong Kong
Locality Name (eg, city) [Default City]:Hong Kong
Organization Name (eg, company) [Default Company Ltd]:yui
Organizational Unit Name (eg, section) []:yui
Common Name (eg, your name or your server's hostname) []:*.yuick.com
Email Address []:yuick@yuick.com
```


```
一键生成

C  => Country

ST => State

L  => City

O  => Organization

OU => Organization Unit

CN => Common Name
```
```
openssl req -x509 -nodes -sha256 -newkey rsa:2048 \
-keyout yuick.key \
-subj "/C=US/ST=New Jersey/L=Parsippany/O=Yuick, Inc/OU=Yuick/CN=yuick.com" \
-reqexts SAN \
-config <(cat /etc/pki/tls/openssl.cnf \
<(printf "[SAN]\nsubjectAltName=DNS:www.yuick.com,DNS:test.yuick.com")) \
-out yuick.csr
```
```
openssl ca -in yuick.csr \
-extensions SAN \
-config <(cat /etc/pki/tls/openssl.cnf \
<(printf "[SAN]\nsubjectAltName=DNS:www.yuick.com,DNS:test.yuick.com")) \
-out yuick.crt
```



>https://blog.yuick.com/2018/11/05/ziqianming.html



###edit nginx conf



```
vi /etc/nginx/conf.d/default.conf
```
>or you can add one line to default conf to include /etc/nginx/conf.d conf files.

```
    include /etc/nginx/conf.d/*.conf;
```
add to /etc/nginx/nginx.conf(in http section or out)




create a site.com.conf at /etc/nginx/conf.d/

```
server {
    listen 443 ssl;
    #proxy_redirect off;
    server_name umod.org;
    ssl_certificate /etc/nginx/certs/cert.crt;
    ssl_certificate_key /etc/nginx/certs/cert.key;
    ssl_session_cache    shared:SSL:10m;
    ssl_session_timeout  10m;
    keepalive_timeout    60;
    ssl on;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_pass  https://umod.org;
    }
    access_log on;
    error_log on;
    access_log /var/log/nginx/logs/umod_access.log;
    error_log /var/log/nginx/logs/umod_error.log error;
}
```

use ```nginx -t``` to validate


>multi domains

```
server {
    listen 443 ssl;
    #proxy_redirect off;
    server_name umod.org;
    ssl_certificate /etc/nginx/certs/cert.crt;
    ssl_certificate_key /etc/nginx/certs/cert.key;
    ssl_session_cache    shared:SSL:10m;
    ssl_session_timeout  10m;
    keepalive_timeout    60;
    ssl on;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_pass  https://umod.org;
    }
    access_log on;
    error_log on;
    access_log /var/log/nginx/logs/umod_access.log;
    error_log /var/log/nginx/logs/umod_error.log error;
}
server {
    listen 443 ssl;
    #proxy_redirect off;
    server_name assets.umod.org;
    ssl_certificate /etc/nginx/certs/cert.crt;
    ssl_certificate_key /etc/nginx/certs/cert.key;
    ssl_session_cache    shared:SSL:10m;
    ssl_session_timeout  10m;
    keepalive_timeout    60;
    ssl on;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_pass  https://assets.umod.org;
    }
    access_log on;
    error_log on;
    access_log /var/log/nginx/logs/assets_umod_access.log;
    error_log /var/log/nginx/logs/assets_umod_error.log error;
}


>http://www.ttlsa.com/nginx/how-to-nginx-ssl-reverse-proxy/







####some seems to be more secure conf
```
server {
    listen 80;
    return 301 https://$host$request_uri;
}

server {
    listen 443;
    server_name linuxtechlab.com;
    ssl_certificate /etc/nginx/ssl/cert.crt;

    ssl_certificate_key /etc/nginx/ssl/cert.key;
    ssl on;
    ssl_session_cache builtin:1000 shared:SSL:10m;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers HIGH:!aNULL:!eNULL:!EXPORT:!CAMELLIA:!DES:!MD5:!PSK:!RC4;
    ssl_prefer_server_ciphers on;
    access_log /var/log/nginx/access.log;
    
    location / {
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_pass http://localhost:8080;
        proxy_read_timeout 90;
        proxy_redirect http://localhost:8080 https://linuxtechlab.com;
    }
}
```


###about proxy_redirect


```
proxy_redirect
语法：proxy_redirect [ default|off|redirect replacement ] 
默认值：proxy_redirect default 
使用字段：http, server, location 
如果需要修改从被代理服务器传来的应答头中的"Location"和"Refresh"字段，可以用这个指令设置。
假设被代理服务器返回Location字段为： http://localhost:8000/two/some/uri/
这个指令：
proxy_redirect http://localhost:8000/two/ http://frontend/one/;
将Location字段重写为http://frontend/one/some/uri/。
在代替的字段中可以不写服务器名：
proxy_redirect http://localhost:8000/two/ /;
这样就使用服务器的基本名称和端口，即使它来自非80端口。
如果使用“default”参数，将根据location和proxy_pass参数的设置来决定。
例如下列两个配置等效：
location /one/ {
proxy_pass http://upstream:port/two/;
proxy_redirect default;
}
 
location /one/ {
proxy_pass http://upstream:port/two/;
proxy_redirect http://upstream:port/two/ /one/;
}
在指令中可以使用一些变量：
proxy_redirect http://localhost:8000/ http://$host:$server_port/;
这个指令有时可以重复：
proxy_redirect default;
proxy_redirect http://localhost:8000/ /;
proxy_redirect http://www.example.com/ /;
参数off将在这个字段中禁止所有的proxy_redirect指令：
proxy_redirect off;
proxy_redirect default;
proxy_redirect http://localhost:8000/ /;
proxy_redirect http://www.example.com/ /;
利用这个指令可以为被代理服务器发出的相对重定向增加主机名：
proxy_redirect / /;
 
将被代理服务器发出的重定向http协议的location改为https协议：proxy_redirect ~^http://([^:]+)(:\d+)?(.*)$ https://$1$2$3;
```


>https://www.cnblogs.com/cangqinglang/p/10265367.html
>https://blog.csdn.net/tototuzuoquan/article/details/70156234
>https://blog.csdn.net/u010391029/article/details/50395680

#How to import cert into centos  

```
openssl x509 -inform pam -in ws.cer -out ws.pem
cat ws.pem >> /etc/pki/tls/certs/ca-bundle.crt

cat /etc/pki/tls/certs/ca-bundle.crt //这里会看到证书已经追加到末尾了
```


>https://zixuephp.net/article-420.html
>above is pro one

#avoid ssl cert check for curl
add -k params to curl

```
curl -k -****************************
```


someone says
```
 Or simply use --cacert /Path/to/file with the contents of your trusted self-signed cert file.
```
```


    -k, --insecure

    (TLS) By default, every SSL connection curl makes is verified to be secure. This option allows curl to proceed and operate even for server connections otherwise considered insecure.

    The server connection is verified by making sure the server's certificate contains the right name and verifies successfully using the cert store.

    See this online resource for further details: https://curl.haxx.se/docs/sslcerts.html

    See also --proxy-insecure and --cacert.

```

>https://unix.stackexchange.com/questions/451207/how-to-trust-self-signed-certificate-in-curl-command-line



#Nginx Multi sub domain reverse proxy

```
server {
		...
		server_name localhost;			
		...
}
```


```
    #add_header Pragma "no-cache";

    #add_header Cache-Control "no-store, no-cache, must-revalidate, post-check=0, pre-check=0";

    location / {

      if ($host = 'www.yourdomain.com' ) {

          proxy_pass http://web-node1:8080; break;

      }

      if ($host = 'yourdomain.com' ) {

          proxy_pass http://web-node1:8080; break;

      }



      if ($host = 'passport.yourdomain.com' ) {

          proxy_pass http://web-node1:8080; break;

      }

      if ($host = 'www.passport.yourdomain.com' ) {

          proxy_pass http://web-node1:8080; break;

      }



      if ($host = 'mp.yourdomain.com' ) {

          proxy_pass http://web-node1:8091; break;

      }

      if ($host = 'www.mp.yourdomain.com' ) {

          proxy_pass http://web-node1:8091; break;

      }



      if ($host = 'm.yourdomain.com' ) {

          proxy_pass http://web-node1:8091; break;

      }

      if ($host = 'www.m.yourdomain.com' ) {

          proxy_pass http://web-node1:8091; break;

      }



      if ($host = 'image.yourdomain.com' ) {

          proxy_pass http://web-node1:8083; break;

      }

      if ($host = 'www.image.yourdomain.com' ) {

          proxy_pass http://web-node1:8083; break;

      }



      if ($host = 'wx.yourdomain.com' ) {

          proxy_pass http://web-node1:9090; break;

      }

      if ($host = 'www.wx.yourdomain.com' ) {

          proxy_pass http://web-node1:9090; break;

      }



      if ($host = 'im.yourdomain.com' ) {

          proxy_pass http://sink-node1:10031; break;

      }

      if ($host = 'www.im.yourdomain.com' ) {

          proxy_pass http://sink-node1:10031; break;

      }



      if ($host = 'ms.yourdomain.com' ) {

          proxy_pass http://sink-node1:29088; break;

      }

      if ($host = 'www.ms.yourdomain.com' ) {

          proxy_pass http://sink-node1:29088; break;

      }



      if ($host = 'kibana.yourdomain.com' ) {

          proxy_pass http://sink-node1:5601; break;

      }

      if ($host = 'www.kibana.yourdomain.com' ) {

          proxy_pass http://sink-node1:5601; break;

      }
      # Other pan parsing by cid.

      proxy_pass http://web-node1:8082;

    }
```


#GZIP enable
use it in server{} section
```
    ### gzip Compressed transmission. ###

    gzip on;

    gzip_min_length 1k;  # min is 1kib

    gzip_buffers 16 64K;

    gzip_http_version 1.1;

    gzip_comp_level 6;

    gzip_types text/plain application/x-javascript text/css application/xml application/javascript;

    gzip_vary on;

    #

```



#hosts 

配合dnsmasq完成通配符子域名转发，hosts不支持通配符
>https://www.cnblogs.com/sunsky303/p/9238669.html


```
#############################################################################
#
#        DNS 选项
#
##############################################################################
# 不加载本地的 /etc/hosts 文件
#no-hosts
# 添加读取额外的 hosts 文件路径，可以多次指定。如果指定为目录，则读取目录中的所有文件。
#addn-hosts=/etc/hosts
# 读取目录中的所有文件，文件更新将自动读取
#hostsdir=<path>
# 例如，/etc/hosts中的os01将扩展成os01.example.com
#expand-hosts
 
##############################################################################
# 缓存时间设置，一般不需要设置
# 本地 hosts 文件的缓存时间，通常不要求缓存本地，这样更改hosts文件后就即时生效。
#local-ttl=3600
# 同 local-ttl 仅影响 DHCP 租约
#dhcp-ttl=<time>
# 对于上游返回的值没有ttl时，dnsmasq给一个默认的ttl，一般不需要设置，
#neg-ttl=<time>
# 指定返回给客户端的ttl时间，一般不需要设置
#max-ttl=<time>
# 设置在缓存中的条目的最大 TTL。
#max-cache-ttl=<time>
# 不需要设置，除非你知道你在做什么。
#min-cache-ttl=<time>
# 一般不需要设置
#auth-ttl=<time>
 
##############################################################################
# 记录dns查询日志
#log-queries
# 设置日志记录器，‘-‘ 为 stderr，也可以是文件路径。默认为：DAEMON，调试时使用 LOCAL0。
#log-facility=<facility>
#log-facility=/var/log/dnsmasq/dnsmasq.log
# 异步log，缓解阻塞，提高性能。默认为5，最大100。
#log-async[=<lines>]
#log-async=50
 
##############################################################################
# 指定用户和组
#user=nobody
#group=nobody
 
##############################################################################
# 指定DNS的端口，默认53，设置 port=0 将完全禁用 DNS 功能，仅使用 DHCP/TFTP
#port=53
# 指定 EDNS.0 UDP 包的最大尺寸，默认为 RFC5625 推荐的 edns-packet-max=4096
#edns-packet-max=<size>
# 指定向上游查询的 UDP 端口，默认是随机端口，指定后降低安全性、加快速度、减少资源消耗。
# 设置为 0 有操作系统分配。
query-port=53535
# 指定向上游查询的 UDP 端口范围，方便防火墙设置。
#min-port=<port>
#max-port=<port>
# 指定接口，指定后同时附加 lo 接口，可以使用‘*‘通配符
#interface=wlp2s0
# 指定排除的接口，排除高优先级，可以使用‘*‘通配符
#except-interface=
# 启用 DNS 验证模式
#auth-server=<domain>,<interface>|<ip-address>
# 仅接收同一子网的 DNS 请求，仅在未指定 interface、except-interface、listen-address 
# 或者 auth-server 时有效。
#local-service
# 指定不提供 DHCP 或 TFTP 服务的接口，仅提供 DNS 服务
#no-dhcp-interface=enp3s0
# 指定IP地址，可以多次指定。下面两行跟指定接口的作用类似。
#listen-address=192.168.10.254
#listen-address=127.0.0.1
# 绑定接口，开启此项将仅监听指定的接口。
#bind-interfaces
# 对于新添加的接口也进行绑定。
#bind-dynamic
 
##############################################################################
# 如果 hosts 中的主机有多个 IP 地址，仅返回对应子网的 IP 地址。
#localise-queries
# 如果反向查找的是私有地址例如192.168.X.X，仅从 hosts 文件查找，不再转发到上游服务器
#bogus-priv
# 对于任何被解析到此IP的域名，使其解析失效，可以多次指定
#bogus-nxdomain=64.94.110.11
# 忽略指定的 DNS 响应服务器地址，例如 1.1.1.1 为伪造的 dns 服务器并且响应速度非常快，
# 指定 ignore-address=1.1.1.1 可以忽略它的响应信息，从而等待正确的响应结果。
#ignore-address=<ipaddr>
#filterwin2k
 
##############################################################################
# 指定 resolv-file 文件路径，默认/etc/resolv.conf
#resolv-file=/etc/resolv.conf
# 不读取 resolv-file 来确定上游服务器
#no-resolv
# 在编译时需要启用 DBus 支持。
#enable-dbus[=<service-name>]
# 严格按照resolv.conf中的顺序进行查找
#strict-order
# 向所有上有服务器发送查询，而不是一个。
#all-servers
#dns-loop-detect
 
##############################################################################
# 这项安全设置是拒绝解析包含私有 IP 地址的域名，
# 这些IP地址包括如下私有地址范围：10.0.0.0/8、172.16.0.0/12、192.168.0.0/16。
# 其初衷是要防止类似上游DNS服务器故意将某些域名解析成特定私有内网IP而劫持用户这样的安全攻击。
# 直接在配置文件中注销 stop-dns-rebind 配置项从而禁用该功能。
# 这个方法确实可以一劳永逸的解决解析内网 IP 地址的问题，但是我们也失去了这项安全保护的特性，
# 所以在这里我不推荐这个办法。
# 使用 rebind-domain-ok 进行特定配置，顾名思义该配置项可以有选择的忽略域名的 rebind 行为
#stop-dns-rebind
#rebind-localhost-ok
#rebind-domain-ok=[<domain>]|[[/<domain>/[<domain>/]
#rebind-domain-ok=/.dinghuangjie.com/.dhj.com/.harlinsu.com/.example.com/.test.com/.esderive.com/
 
##############################################################################
# 也不要检测 /etc/resolv.conf 的变化
#no-poll
# 重启后清空缓存
#clear-on-reload
# 完整的域名才向上游服务器查找，如果仅仅是主机名仅查找hosts文件
#domain-needed
 
##############################################################################
# IP地址转换
#alias=[<old-ip>]|[<start-ip>-<end-ip>],<new-ip>[,<mask>]
##############################################################################
#local=[/[<domain>]/[domain/]][<ipaddr>[#<port>][@<source-ip>|<interface>[#<port>]]
#server=[/[<domain>]/[domain/]][<ipaddr>[#<port>][@<source-ip>|<interface>[#<port>]]
#server=/example.com/192.168.10.252
#rev-server=<ip-address>/<prefix-len>,<ipaddr>[#<port>][@<source-ip>|<interface>[#<port>]]
 
# 将任何属于 <domain> 域名解析成指定的 <ipaddr> 地址。
# 也就是将 <domain> 及其所有子域名解析成指定的 <ipaddr> IPv4 或者 IPv6 地址，
# 通常用于屏蔽特定的域名。
# 一次只能指定一个 IPv4 或者 IPv6 地址，
# 要同时返回 IPv4 和IPv6 地址，请多次指定 address= 选项。
# 注意： /etc/hosts 以及 DHCP 租约将覆盖此项设置。
#address=/<domain>/[domain/][<ipaddr>]
 
#ipset=/<domain>/[domain/]<ipset>[,<ipset>]
#mx-host=<mx name>[[,<hostname>],<preference>]
#mx-target=<hostname>
 
# SRV 记录
#srv-host=<_service>.<_prot>.[<domain>],[<target>[,<port>[,<priority>[,<weight>]]]]
 
# A, AAAA 和 PTR 记录 
#host-record=<name>[,<name>....],[<IPv4-address>],[<IPv6-address>][,<TTL>]
 
# TXT 记录
#txt-record=<name>[[,<text>],<text>]
 
# PTR 记录 
#ptr-record=<name>[,<target>]
 
#naptr-record=<name>,<order>,<preference>,<flags>,<service>,<regexp>[,<replacement>]
 
# CNAME 别名记录
#cname=<cname>,<target>[,<TTL>]
 
 
#dns-rr=<name>,<RR-number>,[<hex data>]
#interface-name=<name>,<interface>[/4|/6]
#synth-domain=<domain>,<address range>[,<prefix>]
#add-mac[=base64|text]
#add-cpe-id=<string>
#add-subnet[[=[<IPv4 address>/]<IPv4 prefix length>][,[<IPv6 address>/]<IPv6 prefix length>]]
##############################################################################
 
##############################################################################
# 缓存条数，默认为150条，cache-size=0 禁用缓存。
cache-size=1000
# 不缓存未知域名缓存，默认情况下dnsmasq缓存未知域名并直接返回为客户端。
#no-negcache
# 指定DNS同属查询转发数量
dns-forward-max=1000
 
# 启用连接跟踪，读取 Linux 入栈 DNS 查询请求的连接跟踪标记，
# 并且将上游返回的响应信息设置同样的标记。
# 用于带宽控制和防火墙部署。
# 此选项必须在编译时启用 conntrack 支持，并且内核正确配置并加载 conntrack。
# 此选项不能与 query-port 同时使用。
#conntrack
 
 
##############################################################################
#
#        DHCP 选项
#
##############################################################################
# 设置 DHCP 地址池，同时启用 DHCP 功能。
# IPv4 <mode> 可指定为 static|proxy ，当 <mode> 指定为 static 时，
# 需用 dhcp-host 手动分配地址池中的 IP 地址。
# 当 <mode> 指定为 proxy 时，为指定的地址池提供 DHCP 代理。
#dhcp-range=[tag:<tag>[,tag:<tag>],][set:<tag>,]<start-addr>[,<end-addr>][,<mode>][,<netmask>[,<broadcast>]][,<lease time>]
#dhcp-range=172.16.0.2,172.16.0.250,255.255.255.0,1h
#dhcp-range=192.168.10.150,192.168.10.180,static,255.255.255.0,1h
 
# 根据 MAC 地址或 id 固定分配客户端的 IP 地址、主机名、租期。
# IPv4 下指定 id:* 将忽略 DHCP 客户端的 ID ，仅根据 MAC 来进行 IP 地址分配。
# 在读取 /etc/hosts 的情况，也可以根据 /etc/hosts 中的主机名分配对应 IP 地址。
# 指定 ignore 将忽略指定客户端得 DHCP 请求。
#dhcp-host=[<hwaddr>][,id:<client_id>|*][,set:<tag>][,<ipaddr>][,<hostname>][,<lease_time>][,ignore]
#dhcp-hostsfile=<path>
#dhcp-hostsdir=<path>
# 读取 /etc/ethers 文件 与使用 dhcp-host 的作用相同。IPv6 无效。
#read-ethers
 
# 指定给 DHCP 客户端的选项信息，
# 默认情况下 dnsmasq 将发送：子网掩码、广播地址、DNS 服务器地址、网关地址、域等信息。
# 指定此选项也可覆盖这些默认值并且设置其他选项值。
# 重要：可以使用 option:<option-name>或者 option号 来指定。
# <option-name> 和 option号的对应关系可使用命令：
# dnsmasq --help dhcp 以及 dnsmasq --help dhcp6 查看，这点很重要。
# 例如设置网关参数，既可以使用 dhcp-option=3,192.168.4.4 也可以使用 dhcp-option = option:router,192.168.4.4。
# 0.0.0.0 意味着当前运行 dnsmasq 的主机地址。
# 如果指定了多个 tag:<tag> 必须同时匹配才行。
# [encap:<opt>,][vi-encap:<enterprise>,][vendor:[<vendor-class>],] 有待继续研究。
#dhcp-option=[tag:<tag>,[tag:<tag>,]][encap:<opt>,][vi-encap:<enterprise>,][vendor:[<vendor-class>],][<opt>|option:<opt-name>|option6:<opt>|option6:<opt-name>],[<value>[,<value>]]
#dhcp-option-force=[tag:<tag>,[tag:<tag>,]][encap:<opt>,][vi-encap:<enterprise>,][vendor:[<vendor-class>],]<opt>,[<value>[,<value>]]
#dhcp-optsfile=<path>
#dhcp-optsdir=<path>
#dhcp-option=3,1.2.3.4
#dhcp-option=option:router,1.2.3.4
#dhcp-option=option:router,192.168.10.254
#dhcp-option=option:dns-server,192.168.10.254,221.12.1.227,221.12.33.227
 
##############################################################################
# (IPv4 only) 禁用重用服务器名称和文件字段作为额外的 dhcp-option 选项。
# 一般情况下 dnsmasq 从 dhcp-boot 移出启动服务器和文件信息到 dhcp-option 选项中。
# 这使得在 dhcp-option 选项封包中有额外的选项空间可用，但是会使老的客户端混淆。
# 此选项将强制使用简单并安全的方式来避免此类情况。可以认为是一个兼容性选项。
#dhcp-no-override
 
##############################################################################
# 配置 DHCP 中继。
# <local address> 是运行 dnsmasq 的接口的 IP 地址。
# 所有在 <local address> 接口上接收到的 DHCP 请求将中继到 <server address> 指定的远程 DHCP 服务器。
# 可以多次配置此选项，使用同一个 <local address> 转发到多个不同的 <server address> 指定的远程 DHCP 服务器。
# <server address> 仅允许使用 IP 地址，不能使用域名等其他格式。
# 如果是 DHCPv6，<server address> 可以是 ALL_SERVERS 的多播地址 ff05::1:3 。
# 在这种情况下必须指定接口 <interface> ，不能使用通配符，用于直接多播到对应的 DHCP 服务器所在的接口。
# <interface> 指定了仅允许接收从 <interface> 接口的 DHCP 服务器相应信息。
#dhcp-relay=<local address>,<server address>[,<interface>]
 
##############################################################################
# 设置标签
#dhcp-vendorclass=set:<tag>,[enterprise:<IANA-enterprise number>,]<vendor-class>
#dhcp-userclass=set:<tag>,<user-class>
#dhcp-mac=set:<tag>,<MAC address>
#dhcp-circuitid=set:<tag>,<circuit-id>
#dhcp-remoteid=set:<tag>,<remote-id>
#dhcp-subscrid=set:<tag>,<subscriber-id>
#dhcp-match=set:<tag>,<option number>|option:<option name>|vi-encap:<enterprise>[,<value>]
#tag-if=set:<tag>[,set:<tag>[,tag:<tag>[,tag:<tag>]]]
 
#dhcp-proxy[=<ip addr>]......
 
##############################################################################
# 不分配匹配这些 tag:<tag> 的 DHCP 请求。
#dhcp-ignore=tag:<tag>[,tag:<tag>]
#dhcp-ignore-names[=tag:<tag>[,tag:<tag>]]
#dhcp-generate-names=tag:<tag>[,tag:<tag>]
# IPv4 only 使用广播与匹配 tag:<tag> 的客户端通信。一般用于兼容老的 BOOT 客户端。
#dhcp-broadcast[=tag:<tag>[,tag:<tag>]] 
 
##############################################################################
# IPv4 only 设置 DHCP 服务器返回的 BOOTP 选项，
# <servername> <server address> 可选，
# 如果未设置服务器名称将设为空，服务器地址设为 dnsmasq 的 IP 地址。
# 如果指定了多个 tag:<tag> 必须同时匹配才行。
# 如果指定 <tftp_servername> 将按照 /etc/hosts 中对应的 IP 地址进行轮询负载均衡。  
#dhcp-boot=[tag:<tag>,]<filename>,[<servername>[,<server address>|<tftp_servername>]]
# 根据不同的类型使用不同的选项。
# 使用示例：
#        dhcp-match=set:EFI_x86-64,option:client-arch,9
#        dhcp-boot=tag:EFI_x86-64,uefi/grubx64.efi
#        #dhcp-match=set:EFI_Xscale,option:client-arch,8
#        #dhcp-boot=tag:EFI_Xscale,uefi/grubx64.efi
#        #dhcp-match=set:EFI_BC,option:client-arch,7
#        #dhcp-boot=tag:EFI_BC,uefi/grubx64.efi
#        #dhcp-match=set:EFI_IA32,option:client-arch,6
#        #dhcp-boot=tag:EFI_IA32,uefi/grubx64.efi
#        #dhcp-match=set:Intel_Lean_Client,option:client-arch,5
#        #dhcp-boot=tag:Intel_Lean_Client,uefi/grubx64.efi
#        #dhcp-match=set:Arc_x86,option:client-arch,4
#        #dhcp-boot=tag:Arc_x86,uefi/grubx64.efi
#        #dhcp-match=set:DEC_Alpha,option:client-arch,3
#        #dhcp-boot=tag:DEC_Alpha,uefi/grubx64.efi
#        #dhcp-match=set:EFI_Itanium,option:client-arch,2
#        #dhcp-boot=tag:EFI_Itanium,uefi/grubx64.efi
#        #dhcp-match=set:NEC/PC98,option:client-arch,1
#        #dhcp-boot=tag:NEC/PC98,uefi/grubx64.efi
#        dhcp-match=set:Intel_x86PC,option:client-arch,0
#        dhcp-boot=tag:Intel_x86PC,pxelinux.0
 
##############################################################################
# DHCP 使用客户端的 MAC 地址的哈希值为客户端分配 IP 地址，
# 通常情况下即使客户端使自己的租约到期，客户端的 IP 地址仍将长期保持稳定。
# 在默认模式下，IP 地址是随机分配的。
# 启用 dhcp-sequential-ip 选项将按顺序分配 IP 地址。
# 在顺序分配模式下，客户端使租约到期更像是仅仅移动一下 IP 地址。
# 在通常情况下不建议使用这种方式。
#dhcp-sequential-ip
 
##############################################################################
# 多数情况下我们使用 PXE，只是简单的允许 PXE 客户端获取 IP 地址，
# 然后 PXE 客户端下载 dhcp-boot 选项指定的文件并执行，也就是 BOOTP 的方式。
# 然而在有适当配置的 DHCP 服务器支持的情况下，PXE 系统能够实现更复杂的功能。
# pxe-service 选项可指定 PXE 环境的启动菜单。
# 为不同的类型系统设定不同的启动菜单，并且覆盖 dhcp-boot 选项。
# <CSA> 为客户端系统类型：x86PC, PC98, IA64_EFI, Alpha, Arc_x86, Intel_Lean_Client, 
# IA32_EFI, X86-64_EFI, Xscale_EFI, BC_EFI, ARM32_EFI 和 ARM64_EFI，其他类型可能为一个整数。
# <basename> 引导 PXE 客户端使用 tftp 从 <server address> 或者 <server_name> 下载文件。
#     注意："layer" 后缀 (通常是 ".0") 由 PXE 提供，也就是 PXE 客户端默认在文件名附加 .0 后缀。
#     示例：pxe-service=x86PC, "Install Linux", pxelinux         （读取 pxelinux.0 文件并执行）
#           pxe-service=x86PC, "Install Linux", pxelinux, 1.2.3.4（不适用于老的PXE）
#     <bootservicetype> 整数，PXE 客户端将通过广播或者通过 <server address> 
#           或者 <server_name> 搜索对应类型的适合的启动服务。。
#     示例：pxe-service=x86PC, "Install windows from RIS server", 1
#           pxe-service=x86PC, "Install windows from RIS server", 1, 1.2.3.4
#     未指定 <basename>、<bootservicetype> 或者 <bootservicetype> 为 “0”，将从本地启动。
#     示例：pxe-service=x86PC, "Boot from local disk"
#           pxe-service=x86PC, "Boot from local disk", 0
# 如果指定 <server_name> 将按照 /etc/hosts 中对应的 IP 地址进行轮询负载均衡。  
#pxe-service=[tag:<tag>,]<CSA>,<menu text>[,<basename>|<bootservicetype>][,<server address>|<server_name>]
# 在 PXE 启动后弹出提示，<prompt> 为提示内容，<timeout> 为超时时间，为 0 则立即执行。
# 如果未指定此选项，在有多个启动选项的情况下等待用户选择，不会超时。
#pxe-prompt=[tag:<tag>,]<prompt>[,<timeout>]
# 根据不同的类型使用不同的菜单，使用示例：
#        #pxe-prompt="What system shall I netboot?", 120
#        # or with timeout before first available action is taken:
#        pxe-prompt="Press F8 or Enter key for menu.", 60
#        pxe-service=x86PC, "Now in x86PC (BIOS mode), boot from local", 0
#        pxe-service=x86PC, "Now in x86PC (BIOS mode)", pxelinux
#        pxe-service=PC98, "Now in PC98 mode", PC98
#        pxe-service=IA64_EFI, "Now in IA64_EFI mode", IA64_EFI
#        pxe-service=Alpha, "Now in Alpha mode", Alpha
#        pxe-service=Arc_x86, "Now in Arc_x86 mode", Arc_x86
#        pxe-service=Intel_Lean_Client, "Now in Intel_Lean_Client mode", Intel_Lean_Client
#        pxe-service=IA32_EFI, "Now in IA32_EFI mode", IA32_EFI
#        pxe-service=X86-64_EFI, "Now in X86-64_EFI (UEFI mode), boot from local", 0
#        pxe-service=X86-64_EFI, "Now in X86-64_EFI (UEFI mode)", grub/grub-x86_64.efi
#        pxe-service=Xscale_EFI, "Now in Xscale_EFI mode", Xscale_EFI
#        pxe-service=BC_EFI, "Now in BC_EFI mode", BC_EFI
#        # CentOS7 系统不支持下列两个选项
#        #pxe-service=ARM32_EFI,"Now in ARM32_EFI mode",ARM32_EFI
#        #pxe-service=ARM64_EFI,"Now in ARM64_EFI mode",ARM64_EFI
 
##############################################################################
# 默认为150，即最多分配150个ip地址出去，最大1000个ip
#dhcp-lease-max=150
# (IPv4 only) 指定DHCP端口，默认为67和68。如果不指定则为1067和1068，单指定一个，第二个加1
#dhcp-alternate-port[=<server port>[,<client port>]]
# 谨慎使用此选项，避免 IP 地址浪费。(IPv4 only) 允许动态分配 IP 地址给 BOOTP 客户端。
# 注意：BOOTP 客户端获取的 IP 地址是永久的，将无法再次分配给其他客户端。
#bootp-dynamic[=<network-id>[,<network-id>]]
# 谨慎使用此选项。
# 默认情况下 DHCP 服务器使用 ping 的方式进行确保 IP 未被使用的情况下将 IP 地址分配出去。
# 启用此选项将不使用 ping 进行确认。
#no-ping
 
##############################################################################
# 记录额外的 dhcp 日志，记录所有发送给 DHCP 客户端的选项（option）以及标签（tag）信息
#log-dhcp
# 禁止记录日常操作日志，错误日志仍然记录。启用 log-dhcp 将覆盖下列选项。
#quiet-dhcp
#quiet-dhcp6
#quiet-ra
 
# 修改 DHCP 默认租约文件路径，默认情况下无需修改
#dhcp-leasefile=/var/lib/dnsmasq/dnsmasq.leases
# (IPv6 only)
#dhcp-duid=<enterprise-id>,<uid>
 
##############################################################################
#dhcp-script=<path>
#dhcp-luascript=<path>
#dhcp-scriptuser=root
#script-arp
#leasefile-ro
 
#bridge-interface=<interface>,<alias>[,<alias>]
 
##############################################################################
# 给 DHCP 服务器指定 domain 域名信息，也可以给对应的 IP 地址池指定域名。
#     直接指定域名
#     示例：domain=thekelleys.org.uk
#     子网对应的域名
#     示例：domain=wireless.thekelleys.org.uk,192.168.2.0/24
#     ip范围对应的域名
#     示例：domain=reserved.thekelleys.org.uk,192.68.3.100,192.168.3.200
#domain=<domain>[,<address range>[,local]]
# 在默认情况下 dnsmasq 插入普通的客户端主机名到 DNS 中。
# 在这种情况下主机名必须唯一，即使两个客户端具有不同的域名后缀。
# 如果第二个客户端使用了相同的主机名，DNS 查询将自动更新为第二个客户端的 IP 地址。
# 如果设置了 dhcp-fqdn 选项，普通的主机名将不再插入到 DNS 中去，
# 仅允许合格的具有域名后缀的主机名插入到 DNS 服务器中。
# 指定此选项需同时指定不含 <address range> 地址范围的 domain 选项。
#dhcp-fqdn
# 通常情况下分配 DHCP 租约后，dnsmasq 设置 FQDN 选项告诉客户端不要尝试 DDNS 更新主机名与 IP 地址。
# 这是因为  name-IP 已自动添加到 dnsmasq 的 DNS 视图中的。
# 设置此选项将允许客户端 DDNS 更新，
# 在 windows 下允许客户端更新 windows AD 服务器是非常有用的。
# 参看  RFC 4702 。
#dhcp-client-update
 
#enable-ra
#ra-param=<interface>,[high|low],[[<ra-interval>],<router lifetime>]
 
 
##############################################################################
#
#        TFTP 选项
#
##############################################################################
# 对于绝大多数的配置，仅需指定 enable-tftp 和 tftp-root 选项即可。
# 是否启用内置的 tftp 服务器，可以指定多个逗号分隔的网络接口
#enable-tftp[=<interface>[,<interface>]]
#enable-tftp
#enable-tftp=enp3s0,lo
# 指定 tftp 的根目录，也就是寻找传输文件时使用的相对路径，可以附加接口，
#tftp-root=<directory>[,<interface>]
#tftp-root=/var/lib/tftpboot/
# 如果取消注释，那么即使指定的 tftp-root 无法访问，仍然启动 tftp 服务。
#tftp-no-fail
# 附加客户端的 IP 地址作为文件路径。此选项仅在正确设置了 tftp-root 的情况下可用，
# 示例：如果 tftp-root=/tftp，客户端为 192.168.1.15 请求 myfile.txt 文件时，
# 将优先请求 /tftp/192.168.1.15/myfile.txt 文件， 其次是 /tftp/myfile.txt 文件。
# 感觉没什么用。
#tftp-unique-root
# 启用安全模式，启用此选项，仅允许 tftp 进程访问属主为自己的文件。
# 不启用此选项，允许访问所有 tftp 进程属主可读取的文件。
# 如果 dnsmasq 是以 root 用户运行，tftp-secure 选项将允许访问全局可读的文件。
# 一般情况下不推荐以 root 用户运行 dnsmasq。
# 在指定了 tftp-root 的情况下并不是很重要。
#tftp-secure
# 将所有文件请求转换为小写。对于 Windows 客户端来说非常有用，建议开启此项。
# 注意：dnsmasq 的 TFTP 服务器总是将文件路径中的“\”转换为“/”。
#tftp-lowercase
# 允许最大的连接数，默认为 50 。
# 如果将连接数设置的很大，需注意每个进程的最大文件描述符限制，详见文档手册。
#tftp-max=<connections>
#tftp-max=50
# 设置传输时的 MTU 值，建议不设置或按需设置。
# 如果设定的值大于网络接口的 MTU 值，将按照网络接口的 MTU 值自动分片传输（不推荐）。
#tftp-mtu=<mtu size>
# 停止 tftp 服务器与客户端协商 "blocksize" 选项。启用后，防止一些古怪的客户端出问题。
#tftp-no-blocksize
# 指定 tftp 的连接端口的范围，方便防火墙部署。
# tftp 侦听在 69/udp ，连接端口默认是由系统自动分配的，
# 非 root 用户运行时指定的连接端口号需大于 1025 最大 65535。
#tftp-port-range=<start>,<end>
###############################################################################
#conf-dir=<directory>[,<file-extension>......]
#conf-file=/etc/dnsmasq.more.conf
conf-dir=/etc/dnsmasq.d
#servers-file=<file>
```


#nginx ssl extend


nginx的所有网站配置里面都要加入 ssl_protocols      TLSv1.1 TLSv1.2 TLSv1.3;
只要一个网站的server{ ... }配置里没加入 ssl_protocols      TLSv1.1 TLSv1.2 TLSv1.3; 就有可能导致所有网站开启tls1.3失效，注意这里是“可能”



#####easy setup

>https://github.com/virusdefender/dockerfiles/blob/master/nginx/Dockerfile

>此外，如不能指定 TLS 1.3 加密套件顺序，尝试打这个 patch https://github.com/Hardrain980/nginx-tls13-chacha20-patch 后编译

>这是 OpenSSL 用了一个新的函数来定义 TLS 1.3 加密套件导致的。

#####check
```
Test Nginx TLS 1.2 support

Run the curl command as follows:
curl -I -v --tlsv1.2 --tls-max 1.2 https://www.cyberciti.biz/
Test Nginx TLS 1.3 support

curl -I -v --tlsv1.3 --tls-max 1.3 https://www.cyberciti.biz/
```

>https://www.cyberciti.biz/faq/configure-nginx-to-use-only-tls-1-2-and-1-3/
>https://www.ssllabs.com/ssltest/index.html
![E53FA9BF-A2D5-964F-8E9B-E159E17E0037]

```
Nginx开启TLSv1.3：

#HTTP/2添加配置
listen 443 ssl http2;
#TLSv1.3添加配置
ssl_protocols TLSv1.1 TLSv1.2 TLSv1.3;
ssl_ciphers TLS13-AES-128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
新增的加密套件：

TLS13-AES-256-GCM-SHA384
TLS13-CHACHA20-POLY1305-SHA256
TLS13-AES-128-GCM-SHA256
TLS13-AES-128-CCM-8-SHA256
TLS13-AES-128-CCM-SHA256
查看HTTP/2：Chrome浏览器：chrome://net-internals/#http2、控制台、查看Nginx日志。
查看TLSv1.3：Chrome浏览器：chrome://flags/#tls13-variant修改后重启。
Chrome，将 chrome://flags/ 中的 Maximum TLS version enabled 改为 TLS 1.3（Chrome 62 中需要将 TLS 1.3 改为 Enabled (Draft)，感谢 @TsuranSonoda 指出）；
Firefox，将 about:config 中的 security.tls.version.max 改为 4；

还有一个网站：https://myssl.com/可以检测效果：
```
可能有参考价值的2段

```
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2; #按照这个协议配置
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;#按照这个套件配置
        ssl_prefer_server_ciphers on;
```
These are the exact same (TLS vs TLS13) as of today. I prefer to use TLS13 since it's more explicit. As you can see, non-PFS and non-AEAD ciphers have been dropped, so no more DHE or AES CBC.

Here is the cipher suite I recommend:
```
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers TLS13-CHACHA20-POLY1305-SHA256:TLS13-AES-256-GCM-SHA384:TLS13-AES-128-GCM-SHA256:EECDH+CHACHA20:EECDH+AESGCM:EECDH+AES;
```
It enables TLS 1.3 ciphers as well as, for TLS 1.2, AES CBC/GCM 128/256 bits, CHACHA20, ECDSA/RSA and EECDH.

You should be able to reload/restart your Nginx server, and if everything went well, you now have TLS 1.3 support!

tls 1.2参考

```
ssl_ciphers ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:DHE-RSA-AES256-SHA;
ssl_prefer_server_ciphers on;
```


>https://angristan.xyz/2018/11/how-to-enable-tls-13-nginx/
>https://www.linuxidc.com/Linux/2018-11/155252.htm




#TLS与SSL区别

TLS1.0=SSL 3.1

>https://www.cnblogs.com/chaiyu2002/p/9551622.html


####TLS1.3 extend


```
OpenSSL has implemented support for five TLSv1.3 ciphersuites as follows:

TLS_AES_256_GCM_SHA384
TLS_CHACHA20_POLY1305_SHA256
TLS_AES_128_GCM_SHA256
TLS_AES_128_CCM_8_SHA256
TLS_AES_128_CCM_SHA256
```

```
For the OpenSSL command line applications there is a new "-ciphersuites" option to configure the TLSv1.3 ciphersuite list. This is just a simple colon (":") separated list of TLSv1.3 ciphersuite names in preference order. Note that you cannot use the special characters such as "+", "!", "-" etc, that you can for defining TLSv1.2 ciphersuites. In practice this is not likely to be a problem because there are only a very small number of TLSv1.3 ciphersuites.

For example:

$ openssl s_server -cert mycert.pem -key mykey.pem -cipher ECDHE -ciphersuites "TLS_AES_256_GCM_SHA384:TLS_CHACHA20_POLY1305_SHA256"

This will configure OpenSSL to use any ECDHE based ciphersuites for TLSv1.2 and below. For TLSv1.3 the TLS_AES_256_GCM_SHA384 and TLS_CHACHA20_POLY1305_SHA256 ciphersuites will be available.

Note that all of the above applies to the "ciphers" command line application as well. This can sometimes lead to surprising results. For example this command:

$ openssl ciphers -s -v ECDHE

Will list all the ciphersuites for TLSv1.2 and below that support ECDHE and additionally all of the default TLSv1.3 ciphersuites. Use the "-ciphersuites" option to further configure the TLSv1.3 ciphersuites.
```

```
In TLSv1.3 the client selects a “group” that it will use for key exchange. OpenSSL only supports ECDHE groups for this. The client then sends “key_share” information to the server for its selected group in the ClientHello.
```

```
In practice most clients will use X25519 or P-256 for their initial key_share. For maximum performance it is recommended that servers are configured to support at least those two groups and clients use one of those two for its initial key_share. This is the default case (OpenSSL clients will use X25519).
```

```
DSA certificates are no longer allowed in TLSv1.3. From OpenSSL 1.1.0 and above ciphersuites for TLSv1.2 and below based on DSA are no longer available by default (you must compile OpenSSL with the "enable-weak-ssl-ciphers" option, and explicitly configure the ciphersuites at run time). If your server application is using a DSA certificate and has made the necessary configuration changes to enable the ciphersuites then TLSv1.3 will never be negotiated when that certificate is used for a connection (the maximum version will be TLSv1.2).

Please use an ECDSA or RSA certificate instead.
```

>https://wiki.openssl.org/index.php/TLS1.3
>https://www.acgist.com/article/541.html





#nginx日志切割

```
最后要说明一下的是安装完成后Nginx的日志不会自动切割，需要自己添加日志切割任务，创建文件/etc/logrotate.d/nginx，配置：

/var/log/nginx/*.log {
        daily
        missingok
        rotate 52
        compress
        delaycompress
        notifempty
        create 640 nginx adm
        sharedscripts
        postrotate
                if [ -f /var/run/nginx.pid ]; then
                        kill -USR1 `cat /var/run/nginx.pid`
                fi
        endscript
}
```

配置中具体指令的含义可以查看手册。配置好之后，可以手动执行一下，看是否正常：

sudo /usr/sbin/logrotate -f /etc/logrotate.d/nginx
如果一切无误，后续 Nginx 的访问日志就会自动按天切分，并以年月日做为文件后缀，一目了然。

在我的 Ubuntu 16.04.1 LTS 上，/etc/logrotate.d/ 目录中的日志切分任务会由 /etc/cron.daily/logrotate 来确保每天执行一次。查看 /etc/crontab 会发现 cron.daily 任务会在每天 6:25 执行，这就是 logrotate 每天切分日志的时机。

如果想要让日志正好在零点被切分，可以修改 cron.daily 的执行时机，也可以把自己的 logrotate 配置文件放在 /etc/logrotate.d/ 之外，再手动配置 crontab 规则。

>https://www.acgist.com/article/541.html


#nginx 配合花生壳等动态解析

resolver在server节内

关键：每次反向代理都动态请求域名的解析


```
resolver 202.102.134.68 114.114.114.114 valid=5 ipv6=off;
set $skyneturl "http://dev.abc.com:10077";
location /applyrecord/aladinnApplyrecord { 
    proxy_pass $skyneturl;
}
```
```
Syntax: resolver address ... [valid=time] [ipv6=on|off];
Default:    —
Context:    http, server, location
 
Configures name servers used to resolve names of upstream servers into addresses, for example:
```

```
server{
        resolver 8.8.8.8;
        resolver_timeout 30s; 
        listen 82;
        location / {
                proxy_pass http://$http_host$request_uri;
                proxy_set_header Host $http_host;
                proxy_buffers 256 4k;
                proxy_max_temp_file_size 0;
                proxy_connect_timeout 30;
                proxy_cache_valid 200 302 10m;
                proxy_cache_valid 301 1h;
                proxy_cache_valid any 1m;
        }
}
```


>http://www.rootop.org/pages/4307.html






#nginx 正向代理

```
server{
        resolver 8.8.8.8;
        resolver_timeout 30s; 
        listen 82;
        location / {
                proxy_pass http://$http_host$request_uri;
                proxy_set_header Host $http_host;
                proxy_buffers 256 4k;
                proxy_max_temp_file_size 0;
                proxy_connect_timeout 30;
                proxy_cache_valid 200 302 10m;
                proxy_cache_valid 301 1h;
                proxy_cache_valid any 1m;
        }
}
1、不能有hostname。 
2、必须有resolver, 即dns，即上面的8.8.8.8，超时时间（30秒）可选。 
3、配置正向代理参数，均是由 Nginx 变量组成。
proxy_pass $scheme://$host$request_uri;  
proxy_set_header Host $http_host;  
4、配置缓存大小，关闭磁盘缓存读写减少I/O，以及代理连接超时时间。
proxy_buffers 256 4k;  
proxy_max_temp_file_size 0;  
proxy_connect_timeout 30;  
5、配置代理服务器 Http 状态缓存时间。
proxy_cache_valid 200 302 10m;  
proxy_cache_valid 301 1h;  
proxy_cache_valid any 1m; 
配置好后，重启nginx，以浏览器为例，要使用这个代理服务器，则只需将浏览器代理设置为http://+服务器ip地址+:+82（82是刚刚设置的端口号）即可使用了。
```

>https://www.cnblogs.com/gbq-dog/p/10653054.html


#nginx反向代理标准示例

```
http {
#    省略了前面一般的配置，直接从负载均衡这里开始
#    设置地址池，后端3台服务器
    upstream http_server_pool {
        server 192.168.1.2:8080 weight=2 max_fails=2 fail_timeout=30s;
        server 192.168.1.3:8080 weight=3 max_fails=2 fail_timeout=30s;
        server 192.168.1.4:8080 weight=4 max_fails=2 fail_timeout=30s;
    }
#    一个虚拟主机，用来反向代理http_server_pool这组服务器
    server {
        listen       80;
#        外网访问的域名        
        server_name  www.test.com; 
        location / {
#           后端服务器返回500 503 404错误，自动请求转发到upstream池中另一台服务器
            proxy_next_upstream error timeout invalid_header http_500 http_503 http_404;
            proxy_pass http://http_server_pool;
            proxy_set_header Host www.test.com;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For  $proxy_add_x_forwarded_for;
        }
        access_log  logs/www.test.com.access.log  combined;
    }
}
最简单的反向代理演示（在一台服务器上做代理服务器，将http请求转发到另一台IIS服务器上，通过二级域名形式访问。）编辑vim nginx.conf
server {
    listen    80;
    server_name test.zhoumengkang.com;
    location / {
        proxy_pass http://121.199.**.*:80;
    }
}
参考：http://www.blogjava.net/xiaomage234/archive/2011/09/08/358247.html
```


#nginx透明代理示例

```
# cat /etc/nginx/sites-enabled/proxy
       server {
                resolver        8.8.8.8;
                access_log      off;
                listen  [::]:8080;
                location / {
                        proxy_pass      $scheme://$host$request_uri;
                        proxy_set_header Host $http_host;
                        proxy_buffers   256 4k;
                        proxy_max_temp_file_size        0k;
                        }
                }
 
iptables -t nat -A PREROUTING -s 10.8.0.0/24 -p tcp --dport 80 -j DNAT --to 192.168.0.253:8080
RAW Paste Data
# cat /etc/nginx/sites-enabled/proxy
       server {
                resolver        8.8.8.8;
                access_log      off;
                listen  [::]:8080;
                location / {
                        proxy_pass      $scheme://$host$request_uri;
                        proxy_set_header Host $http_host;
                        proxy_buffers   256 4k;
                        proxy_max_temp_file_size        0k;
                        }
                }

iptables -t nat -A PREROUTING -s 10.8.0.0/24 -p tcp --dport 80 -j DNAT --to 192.168.0.253:8080
```
>https://www.cnblogs.com/gbq-dog/p/10653054.html

#一个较完整的优化与安全考虑的conf
```
server {
    listen               443 ssl http2 fastopen=3 reuseport;

    # 如果你使用了 Cloudflare 的 HTTP/2 + SPDY 补丁，记得加上 spdy
    # listen               443 ssl http2 spdy fastopen=3 reuseport;

    server_name          www.imququ.com imququ.com;
    server_tokens        off;

    include              /home/jerry/www/nginx_conf/ip.blacklist;

    # https://imququ.com/post/certificate-transparency.html#toc-2
    ssl_ct               on;
    ssl_ct_static_scts   /home/jerry/www/scts;

    # 中间证书 + 站点证书
    ssl_certificate      /home/jerry/www/ssl/chained.pem;

    # 创建 CSR 文件时用的密钥
    ssl_certificate_key  /home/jerry/www/ssl/domain.key;

    # openssl dhparam -out dhparams.pem 2048
    # https://weakdh.org/sysadmin.html
    ssl_dhparam          /home/jerry/www/ssl/dhparams.pem;

    # https://github.com/cloudflare/sslconfig/blob/master/conf
    ssl_ciphers                EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;

    # 如果启用了 RSA + ECDSA 双证书，Cipher Suite 可以参考以下配置：
    # ssl_ciphers              EECDH+CHACHA20:EECDH+CHACHA20-draft:EECDH+ECDSA+AES128:EECDH+aRSA+AES128:RSA+AES128:EECDH+ECDSA+AES256:EECDH+aRSA+AES256:RSA+AES256:EECDH+ECDSA+3DES:EECDH+aRSA+3DES:RSA+3DES:!MD5;

    ssl_prefer_server_ciphers  on;

    ssl_protocols              TLSv1 TLSv1.1 TLSv1.2;

    ssl_session_cache          shared:SSL:50m;
    ssl_session_timeout        1d;

    ssl_session_tickets        on;

    # openssl rand 48 > session_ticket.key
    # 单机部署可以不指定 ssl_session_ticket_key
    ssl_session_ticket_key     /home/jerry/www/ssl/session_ticket.key;

    ssl_stapling               on;
    ssl_stapling_verify        on;

    # 根证书 + 中间证书
    # https://imququ.com/post/why-can-not-turn-on-ocsp-stapling.html
    ssl_trusted_certificate    /home/jerry/www/ssl/full_chained.pem;

    resolver                   114.114.114.114 valid=300s;
    resolver_timeout           10s;

    access_log                 /home/jerry/www/nginx_log/imququ_com.log;

    if ($request_method !~ ^(GET|HEAD|POST|OPTIONS)$ ) {
        return           444;
    }

    if ($host != 'imququ.com' ) {
        rewrite          ^/(.*)$  https://imququ.com/$1 permanent;
    }

    location ~* (robots\.txt|favicon\.ico|crossdomain\.xml|google4c90d18e696bdcf8\.html|BingSiteAuth\.xml)$ {
        root             /home/jerry/www/imququ.com/www/static;
        expires          1d;
    }

    location ^~ /static/uploads/ {
        root             /home/jerry/www/imququ.com/www;
        add_header       Access-Control-Allow-Origin *;

        set              $expires_time max;

        valid_referers   blocked none server_names *.qgy18.com *.inoreader.com feedly.com *.feedly.com www.udpwork.com theoldreader.com digg.com *.feiworks.com *.newszeit.com r.mail.qq.com yuedu.163.com *.w3ctech.com;
        if ($invalid_referer) {
            set          $expires_time -1;
            return       403;
        }

        expires          $expires_time;
    }

    location ^~ /static/ {
        root             /home/jerry/www/imququ.com/www;
        add_header       Access-Control-Allow-Origin *;      
        expires          max;
    }

    location ^~ /admin/ {
        proxy_http_version       1.1;

        add_header               Strict-Transport-Security "max-age=31536000; includeSubDomains; preload";

        # DENY 将完全不允许页面被嵌套，可能会导致一些异常。如果遇到这样的问题，建议改成 SAMEORIGIN
        # https://imququ.com/post/web-security-and-response-header.html#toc-1
        add_header               X-Frame-Options DENY;

        add_header               X-Content-Type-Options nosniff;

        proxy_set_header         X-Via            QingDao.Aliyun;
        proxy_set_header         Connection       "";
        proxy_set_header         Host             imququ.com;
        proxy_set_header         X-Real_IP        $remote_addr;
        proxy_set_header         X-Forwarded-For  $proxy_add_x_forwarded_for;

        proxy_pass               http://127.0.0.1:9095;
    }

    location / {
        proxy_http_version       1.1;

        add_header               Strict-Transport-Security "max-age=31536000; includeSubDomains; preload";
        add_header               X-Frame-Options deny;
        add_header               X-Content-Type-Options nosniff;
        add_header               Content-Security-Policy "default-src 'none'; script-src 'unsafe-inline' 'unsafe-eval' blob: https:; img-src data: https: http://ip.qgy18.com; style-src 'unsafe-inline' https:; child-src https:; connect-src 'self' https://translate.googleapis.com; frame-src https://disqus.com https://www.slideshare.net";
        add_header               Public-Key-Pins 'pin-sha256="YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg="; pin-sha256="aef6IF2UF6jNEwA2pNmP7kpgT6NFSdt7Tqf5HzaIGWI="; max-age=2592000; includeSubDomains';
        add_header               Cache-Control no-cache;

        proxy_ignore_headers     Set-Cookie;

        proxy_hide_header        Vary;
        proxy_hide_header        X-Powered-By;

        proxy_set_header         X-Via            QingDao.Aliyun;
        proxy_set_header         Connection       "";
        proxy_set_header         Host             imququ.com;
        proxy_set_header         X-Real_IP        $remote_addr;
        proxy_set_header         X-Forwarded-For  $proxy_add_x_forwarded_for;

        proxy_pass               http://127.0.0.1:9095;
    }
}

server {
    server_name       www.imququ.com imququ.com;
    server_tokens     off;

    access_log        /dev/null;

    if ($request_method !~ ^(GET|HEAD|POST)$ ) {
        return        444;
    }

    location ^~ /.well-known/acme-challenge/ {
        alias         /home/jerry/www/challenges/;
        try_files     $uri =404;
    }

    location / {
        rewrite       ^/(.*)$ https://imququ.com/$1 permanent;
    }
}
```

>https://imququ.com/post/my-nginx-conf.html


#HPKP配置

>https://imququ.com/post/http-public-key-pinning.html

#Nginx监听IPV6	

```
server {

  listen 443 ssl http2;
  listen [::]:443 ssl http2;
```

[E53FA9BF-A2D5-964F-8E9B-E159E17E0037]:data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAoAAAADgCAYAAACTptdQAAAgAElEQVR4Xuy9B5QlV3nv+z+5w+TRjDTKEkooRxBCGVBAQiBElsAYYWxs8L3GmLfeu9fhvmXf57Cwwb7GZJORSEIEE4QyKCKUJZSzNJJGM9O5T6p667dP/3uKQ3fv1pzpUaBqrV7n9KmqHb7439+3Q2H9+vXp4OCgms2mGo2G+vr61G63Va/XVa1WlaapSqWSkiQJv/P/8uXLtX79elUqFXEVi0UVCoVwjz9ffKfs8fHx8AzlU05/f38of2BgIHxSJ+/ThsnJyVAvz/vid/53GXxSt9vEu/xxuR1+l7ImJiZC23mHvrgf5XI51M8nvy1ZsiQ8OzIyEvrIb9Qx1+V653xojptZem1uGXO9Rx9oo3lEf/gzDVutVk/VUj70o45s2a6P+71c5pf7Yf66TzH6xfgTe7+XtvMutEF+s3QyTegbf8g8tENW+R+eWO96bR910wb+4Allm+fU1St9KAO9dv/QMeqxLLgvpgXtoE/8Ttt4njb4uawtiLXN+j4Xj3qlX6/8hy7Qw7SmPZZlyzb3sZG1Wi3QA1pAJ35DJnq5TMNuOsz2+7OtizZjQ22jzU/4n+2r7Tfl+x7foQV0oL9cvMP9rE3375YR+wae9Xuxdts32Z67zl7tX6xe64Lrz8oC76I77muWJ9n22h/SVtpP261D9teUZbpY9+ajH7H2mx9ut+ulrdblucqwr+G9bhnx/7E29HKf+qHb0qVLg5zSbvz7okWLwu9Z3ODvlk/7mKzMZG0bfYrZKPtG5Ny8Nm6ifngJ5gB7GJdQx9jYWGgjNFrIqzAyMhIQmxsHOOOy47BhMkiCiDZSVurZACDl8D5CChC0AkPQoaGhILCLFy+eNgKUCxFMCIM22gKh7MD4dHuywMACnzV2OFeMlA0pZZnxlGOHg1DYePEMdIAxMQMTE4AY8xbaQRlkWADpM/Tgf4SM/vdyGbBQBt+5bFSzIH5z66As+GJnAj9oM7/T/piDjPFnoelP/VmH7/9NL/SOPpl2HnRYt3oF0JRjZ2Jjbj2x3s7Fmxh9PIDzgMw6bn5heA32snLh/pkellOeQe/43wPEudr3XPM3JtdZvmJXDFqycoE+uh8G0x4MLLQDiLU/dp/2Grjadhuw2X53A5EsAMwO5LO/Zx2z9Sc70LT+xAboBkMGLAYt0JW/mP2I9T92Hzk3yKWv1IlP5IJu3bTJlpcdBGYH1/aF/OYARtaueLCX1fdYO2e7j631ADYbRDD9Y/apO/CQ7a8HR911Z21OTL9j/YLGyGgWVEE3+gU2cEAhW6e/Z4MNvEM53ENmjAti8kcd8N2y6/baPtp/0k4wCL9j93iPewaLsX5u7v3C8PBw6g57lJqNEnj0YUbYiBu4Yax9ZUcw/u4y6RDCCrDiNzps40gZHiFAUDt5g9IsALQyOfJgFJ41GFlm0l73weCP8h0NhOgIw+joaBAG2mdj8Xw3vvNhejeAd6TGfIkpcKwOaAtfrRgomiO9HqnGypjrPnKDrCEvfAf02bHYEPZS/kK/a1k04LHiU6/BGd/t/DzShob0l1FgL1e3gfOo1vrSK/953wAGOcjyhr6iV1l9o147s2x0wYM73scA8r91s5f+Px/edaQPntr20S7bN9PM4Bl6OQrYa/uzzqwbXMwFPuZbLzYS+wm/4L8j2VmglfULrjPrc7LAMRspNACyzzFYs72ZTwSKd1y+I4vZKI590Hz7+2yfc7DB0Ttkm2AIfSAIwv9ZUNDNE2cQHEnrHlQ7a2d/6AghNKfOXgfhjqCZF6ZXt9+djS4O5tju2Na5n9nyuumwJeQT+hhE4QuJtNn+GDxn66Wd9it8xwZbvpF13s1mLWIANWvj3XfzMpsNsc5Tt0E3v1H/Ql6FjRs3pg6H0zCj8mzo0iMAExLCQUgbaDPZoMKo2KMfOmBDQecef/zx4BQcGeA3ynNnIapH/93RRgNABBDgiILz/GwAkPsGtLyTjTy4XdTlyAt9oB02yjEGLyRztkTZjpRmQS10gw70u9cUiKNL3cDCv/faB/hn0Ac453/LF33LDkB6rWuh3rd8GvBk9cPyaaPQPQjr1UEZSNgR2PFmR6C99NvylU3PZY064NB2pTvCY3BK/QZJjr5TrsFjL+17rt+1Pe3WDzshp1CRA/rOc3z3VJheB6GzOeqs3vZCI/PNdsSRXAP9LAjpDjRYBq0f2Yh0dkDUHZ15NhGirMP2NCfqc1Sm1wFQjHbUD+CjbwAQZ9C6pz5k7aXpYf3IAgbqc5TY9MrS3DbGgDOWwYq1v3sAa77O174bbNv/8J5tRXeAoLtM++tYG+e6D3+z00qge3agmq0jO0DIym+37rq+WPSP56zXtgNZmfd0GexcNhuJT0NWsQ298i9GuxABNBijkTSYit1QG3AKMpqFgGYeRDAyzoam/bwFwMDt4Ycf1k9+8pMAPhBkC7SdOcrCddhhh+nQQw+dBm92YFYGhGh4eFjLli2bEwA6smDDalSfDT9nQa9HUpQP4JgPk2NEfq7vZ/ljEG8H1CvAcAoIumZD5AbXvQpw1uDMFEl4vgP0bvBnh2zjbZp5EIYsetTqVEAv8mOdpr7s9Ac7ii2RArOT93QCz7k1mM06fsubozJul+0O73ruG9FP24heaPBcvtttf9A7wB008WDGgAQ6Ot3p+70ClKx+dOsPdMmCqc2hE2V6Lpf9Bv3z/CZPb7A/oY7u9NlMOmI98UDcfoJnqcdyE4twOUhhWtvJu++x9zeHJt3vONWIP6Ff/J8dgBv4mBdZAJgFJQ6oOKpoOlqe0D/0Kwv+toT/ymaLstM4+H0+5Rtk2ZZ7UEN/3IdukG8a9mrfDcA8pY1yHbDCvmBr3IZstNKRT6awEIDgfWcNzROXPZeMePBr/GGa2Sc7Qk3/nUkxfy3jW0IGZytjGgA6Vw0AW7t2re67776AlK3INNAhSVJyXHvvvXcAYI6Yee6BjZYFFiLYwN955526+uqrdfrpp4d3KRcCOzXMs//1X/8VIoInn3xyiPCYIFYUpxtYiLLtttvOCQDdFoNTl0F77Pw2btwYmGuwmkXtMQHcEgZ0IRlM/+EXfbMgZw1or3VbiT0QyBp4862XOhxt9gDBgIYy5zNHM8afGH97aXvWiBlwGehxD5rRr6ye2UA4It3rHBBHQAzI+fT0C+5R91xXjD52AO6Xo+3oEDqNzHFlyzEtbBxxXKYBsuppBdmI4WxtfD7wdy76wV+DfEd9DPKc3eB9ogCOcNvm8luvc3Q9aO+mk0HGfBx4TAfs4A12raMMYOxgeSY7B9L/Z4GZQV82BecINvc8GMjO1Y4BOMr3u91AtTsFGOvn5ty3X6EN5quDDPZ7LjcLALN1Odhi+lGmy6X/jhhlM3DZQcXmtNvvZCNXBn+u39myucr3YlLbNfpo/hqM9dK+2LvQwTaIhZ2ec+05oMYmxiqmm+2lU/C2m9mBdEz2/I55b3+YBdTQF8wFhjJ/uY/uOFMX62Mv90MKGIZCGBAxHb7qqqt02223abvttptOCRtB01BA1z333KMjjzxS++233zRI82gvq+iONNEhOnbDDTfoV7/6lX7/939/eoWLFRGC0oZvfvOboR1nnnlmMCA2mHTUAgRxnn76aW2//fZzAkCet6Gl7ZTr6CNzMJx6XrVqVWjPunXrtGLFilDmhg0bAnPmumIOKMacmIONvR+77+hudhRmcOHQdqyMue7joIjEOsXvKIAjtb1GMDDg8NpRMc/HcAQp1vYYfxaa/lnj7kGUR+me8mC980jeNPRiiFgfY/LpgY11wQMcAIYnos9Wxnzog4zBb0+29mR3DC9GlzLshO3E4aOBHzrGs/SXQSHPIFPzmf/4fOHvXADV9M8OhOE19gdeYG+wNUQbeIbv22yzTaDBloigZwE37aQ9jkDH6BeTPXhrefYcR/jqSKedoCOF/O9FXZ6/5sg05WTnkxo8GLg5lZedcxyzLzjdbJnmgVPXsf71et+0phxHePnutO1cg2TzxtMsTF9npvCNvI/+eKABPTx9y4Czlz5YTlxGNnOBvsYGKPQBfwv/HczJBiCyEVnq8MAkazd7aT/vUgftRNcee+wxrVy5MnxH/7yAKTsY9rQy6AfNHZxyZNuBrvnQ17udZPufjdRCH9tjL5RF7z0POibfvdJmGgB6KTINuuyyy8KI9Iwzzpg2QB6JWSk/97nPaY899tBxxx33WyFShzchFAR2jhsi3HLLLXrggQf0zne+U0899VTovOeiEa4HXP7oRz8KhD/11FN/YwWSO4sBQfh5f82aNVEACANpE06Gunj/jjvuCJFIQCRA94QTTgiC8uSTT2qfffYRgBBDHIvAbAkD2isT53r/5ptvDqAber3iFa/QEUccEYwP/2dHYpvbBgzCI488EpQb3uHAPcdiS6XQobGjSQxOfvnLX2qHHXYIPINPMQA01/35AJzNpU32vWykjDqhP/L+85//XEcddZR23333IHvMj8UQINdbIgVsg2rQb2eC3FPXwQcfPGf3YvTxHBsbUC+i+vWvfx34hGzsueeeoZ5dd911eoKznTi89QgZAERU/4c//GEY9L3tbW+LTmKP6V+s/VuCt3OVgd2Bn9DHq9ZxAPD67rvvDnYO/nPxO78xuH7Zy14WAHCvUzTsAD0QzwLA+UQwYvTBL3hVvoMI2PT7779fN954YwgScN9yR50edBgAOvrLe56jhW1/5plnAjj2bgUM9nkXBwlfsTWO7szWziuvvFIveclLgux5MOkpC91gI9bXzbkP4PjGN76hRx99VK973ev06le/OhSD/tN+Lz7MtsU6YeCejWLy3YNuMnX80b/Vq1dP213khnehk3f12Jy2W3asQx7IQXNsxxNPPKFjjz12zqIt/3xic+ivwTc0wNdmyzcd/FuvEWoHDpAr5Om6664L9giaORpIB/BfZAKdnndGi+cIEtHnBx98MPANvAC9nYmciwC/+MUvguwhu44yOooILQh2UReycPTRR4dsHbLCAHCnnXZa8CkwhQ0bNqQ2BB5xX3755YFYb3rTm0IDaKiVzaP1K664IozuMVSOxjiHbiNjgaH8Sy+9VJRLhA1Dz2+77babzj777Om9BxFaCPDd7343GM3XvOY1wUk6tQATbch4FuE3AHQfrDwWJBPbbXO04t577w3A6OUvf3loDwwF+GIcEGqUDOWh/7QBIfAEXowQBop3uPiOUDOycArM0TVoB534hL58AixJhcNwgxs+PUmZMucbwnd/vMcR/fUCmptuukm33nprAOmUh0HYeeedg1G1YlIXQo/wI9hc9BlDA69osyelU4YnrcMLaEq/b7/99kDLt7zlLdOrHD2y82DA7fOcUuhkftqJ85k1AAZKfELbiy66KPQBMIHx4bcdd9wxyAzveksSaEyf6CdGB2WindlVpeYT9HKUl7L533N17LTtiCnTUa5sumUuA+CRuR2l+8j/yOB5550XoujnnHNOkA3mx+I0aYenNzgVCo8AwPDTffOqNtrGd0a19MerppFjL6LxaBy6Mwj7wQ9+oDe/+c3T0zMcyclGXOazCIH3PNL1wiwGegAApnFg0B566KGgz+gC8gStaQft5jfLPpkHnj/kkEOC4bQ+I2uU7dF8NvLkiKnn6cAjdAB+Ixv+5L557GibgQnAE1p5d4Os7Dtihd7Sdqe+qGM+9KHtjkLZlsBr7Bz8eutb3xoGMvTvmmuuCU6K7AcDbA/MkQdssuch8UlbsnYI+aA9lEX7kXvzHhn1PqfIN46Me8iK+e4pCaZ5d3RmNjnnfeq1/kEX7BtAnja85z3vCXz2QJw+2Y84mgJ9bCv4ZOCArAOa0HODQdsmDxo8xQVAgpMFcDHoRYco04EL6qRd/NFv+k+58JRPR7P43xEa6vrtAURRStmjln1n+d7Zy20TWEpVUmfrJ/rxlfO/qgMPPDD4KWwWtgteGjTTxqGhkfD+0qWdqVX1emfvN+pujHW2IuM7n/Q7yNNAn9Y9+XSgOTKLvmETAFTFSkkTo50Bfrm/ND3wYEvbzvz9zt6bARA3O6tMQ4+mtvDl07vwJvXOKu+JRl2lSlkN9tRbvizYrUcff0wnHn+C9t1336BX+EPoR7/5jk7BZ0/pQD7BAWAGfucZ/BO8OOigg6aDTbQNvtmWzGVfYwM82xhogb29+OKLg88guGUfYCAOwGMARn/wadCSP/tmbCaDswMOOCD4UUd3nYL31ATq4jv9/+pXvzotw/DaOotPIRNKmQRmGDAhlwBOBs7gEvyC9YrnsRUOtKCb/A9vsv7z2QL930gBu4GAO4zwG97whqAwEAYGE3WB4E6XZhUkO2pxIwwYENjvf//7YdQLYTpCPxSM1wc/+MFQhx0jRurCCy8Mv20JAGiQArOow1Ev+gRAwgF6Of5dd90V5j6+/vWvD0YEB8Y8R/7oK8qDEvM8kTTep9y99tprejRHZBEjzyIWGxWMEwYZgw6q/+lPfxqMPP1DGHEGCB5RIN7zCBfme57ibIxFiD1CzI4wqA8D+spXvjIAO9qP0KB48BaBQ5gRdp4BKEEThBBji5LCb+gAnzDeGC8UHOWArigOz0ETFOu0004LdUBH+gTA32WXXQK/Pd/LYN4AkH7NBQB53pECjAf9Yv4oDtwGgvpoO2XSZpQIHvAe3+Epz6K0PIchou3QG8OJs0Kp6R90AYRwHyWkH5RPeZRFnwxMaXuMP07/QS9PRbAhp13IAQ4InTr88MOF7iEjL33pSwNoAlhzH7nAYEBneMZ9nCt0wOkh1/QHOcLZQCf4hAwhn/vvv38YtV9//fXhHdqCHJx44onBUCHbADGDZUdxYylIp4Qw4h4UoMOmIw4cw4scYXSRJWhOGwC4gDzob5CG/iBPRP+hA/RBH5Aj+gxPkVUijPCMfiOvvAMAwFBSF7yCHvxO/VzoAe8ARngfe0Rb4Sl6SDuon7bBe/rOoIM+0k6e8yACPeL7fPjvgSPPIk+e8wPYRwaOP/744AQAPdgc2svgG1mm/cg98sEz0Jh3KAPnySASmUG/+M5z9ItnuI9uA0A8r9v7skJP9IFFecgKMmIAaJs5n+iLgwOeagJteQ+5ddTFEU5HvflEB7Gh0Bb+QiPaC99wuugpf+gEbSRqA1+I6CHH2FL4TT22NcgHZWBf6fOrXvWqIBvQCj7zLGUiB9he7kEr6qdMZIN6PB2B92ZOwU3Bo7SoVJ2Nvg0Ai2LT/U7aEd377Oc/F6J+lAuNLD8eNL/iFa8M4Acb+OMf/zjINr4P2vD+brvsGvoHUEAf8D3YBfoD/wwQ/u3f/i3cZ+DEPYOy+x64O/yODSkWOwcepGnnYIAwtaqDXTuAMwMAN/2WiqMQPFhvJe2gw+iKo7DwCBsCn6Ahz0JrvkMHZBGbCkBFf7Gz9JF+oV+8h7zTH+wG9gzeAoB6jfDbPmH/r7322tAe+H3MMccEAIr9QA/hObaPNkF7BmLoK76Le6997WuDbiI/fOc3eIh+wj/0ibK4j/5CG8rBByPT2DrogF9BdrmwNfyR6eA9MpLIPrQFe1GOA1zoKDRBv7DhXNAXfe8JAI6NjYU5gN6YGeGh83QMcETFKOsXvvCFMNKgQowlHcJAOV8+UwjbDhLH/K1vfSsYcIjCsygCowicAkbb0UKUjpExgAPF8Ski2YiRIyLziQBakB2lNFjCCWFY6S+CifFE+RBUhINPCEv/WY2M8gJMMR7QCmPEH3SCoUQ6YCDKiVJyD+NLX2xIaQPP4cipH8OPQKAElIECeGENigKzYykgz2lx/2zEaTfGErBk2tEm+kjkB/p6zhmOBYGm/YAAeEM/EWycCzzA+RF5ou1OXcEzgAXGBcOLUUWBEWpHhCgb+nqeQxbwOXrZreRZWYKWBpsYafqE4UaWAJ+07ZJLLgk8sRxh/DFStIFoLu9QBm393ve+F2QY2aV+DCt1wGPagSJ6axn6jG4AQmgT/IRPfIfOBrNzjbo86HAfqcspMBQco4AhApAgG8gPvIEPtJuLvgFI0EOMGAaFMqA7RoE0A3IMPbiPbsJneOp5Q/ABwAefMBzIAoAD52DDAxikDt6hDGgbM8AGtvQLWXVEm/5At9/7vd8LhpJ+whcGHdDYTpm+oFcYOmiL3uGUGUzAZ/gDfaAL9IfH6BS85Hf6gMPAJvEs9KAdyApGFD5RP4adPuGsyERgZNE16y+yTgYA5wDtcaQ8w/voErYPe0j/HAWezyIkRws9v413rHu0AznF8WAH4SOOlP4DXpBf6Ig+0g4cIu0gqoCt4EKGs/OYkVnsN5EB2o19weaa99z3nGvqoI/cQ/ZCJGhqwOVBSiyiYL2GbvDU03OgJXYUZ0X/kF/sDrYDuUWmAZ20jSk/9Bse4mShM+1HVuELTpS28gz3aT/6irzQfp6DpjyLzOPQAVwMALBFyBKDON5FZuAfco+sfOc73wm09IIbfI5TqLRhZgA4dTpUIVGadMAUchHsfLEzJ9IRyttuuT3Yd2zOH/7hHwZbTOCAdtGORqMV5BRZx9ZCdw9IAFFvPOusoDvINjL57W9/O5RN/9E5aML3f/zHfwz6BRCBFwAqZPvKKy6ZnipTLndOlaFOZwgqpU0nbqWdw7TkT76XKhVNjI1N7wwCwMVeIFfYUNqMbUEuAd5EXrEt6DS8xcfwDAMo/DX9J7qNjEJ/5J/7b3/72wPdGOBCK6b3YBN6XaRGwIJysGu0m/ahd8gaMkmbsSEAUmhJe8lK8gy/I2PYEmwjuot/w87aDyFP/M5vyDkLWKEttoZ64SF8wxaiH+i5I+uWZfSAOvhDZj1wA59QL3RGTqE573p9Bu2Hhj0BwHXr1oWj4CC0V6HhUOmomYIg/NM//VNQXs9noXNEJWBUNoefdfB2Bgj1Zz/72UBwjwoQ3o9//OMh9YUBg/mOUGHgUT6U3jn5zQWAdoAOnXsyKAYBJmII7DwhPIYJg4sA4GRQPsAGAJGUxh//8R+HEQujNVKeNtAo3qc//enAeBwICvvGN74x0AynAxNRCo/icUonnXRSMH4YYNJAjhZBB0cqYyFup8LoZxjRTaVpAQD04ayzzpreMsFpUBQO8ImioRQ4HAQJwYKftAMgAX/hBb/hqKENxgwjSX+JtEBHjC10whB96UtfCsqEkcfZk/5BSWxwnMowyDM4thB399cLCpy+5HkcJeAJA4MDgtZ8pz/wBTlG4eEFMkcfoQ+jYgYdRKVoI31BHnGsyOjXv/710GbaiuGgPxgzwAdAktEZRoELucFgxCJAtNejZ95D7j030s6XsnGUdhzQmgECbaF8+EMbMV7w6JRTTgl95h2cHqCc/mBIaJ+BNYCc7/AQp4ZzpA70CpkjKo/MQA94aF2D1nx3anUuEOBJ5+afoyHIBvKHM8LYIut8/9nPfhacBfxBF6kbQwcAwRFjKKE94AF94T30E52DJoA2nAdRegYWGEb6xKj8a1/7WjDYyC18pv+8iz2jPfTpT//0T4PM0l7kGv2DRxhX+PyOd7wj3KO+L37xi9NpQ3iAo4bvBkfYQuRqrsuRFg8EPGkfWtAuHCO0gD/oLDRAl5BL2gm9cOwGtzhPABFOhXbjMIkgoA/QB0eEXKHf0AebZZsNXRkQUg9zT7FFvEcbeQ9+w09P1zAgjPXPKTvrHbRGDnF2yCiydu655wZ541nagNNE35Bp7C4DTnQYIIRdRq4BSfyGLaK/6DE6jF7Cc2iI7NMHaALvsLXnn39+kA/6TZ0MHCgTW4CcY3upE7uOjDIfHb5gO6AJAIzy0ZdNA6DppKhU4PhMwmUAwc5xiu3pIzY7p/okhc52I8sGlwbZw+ZCZ+rCRmJ70Ff6gD4iAzh75AH58jvv/+AHAg1pM3oPzaDJMUcfo2uvuzbQ6N3vfre+8pWvBB+F72GQi+9C3p95cm0ot8PLzgEIY2MTQQfDQCbpHNVJT7IA0Aeglvs7c/jVTtRXrak+MaEf/fC/Qjt3WLO9fn7VL6YBILIK/bD9pDEBM7Qb3kJ7Bn7wgfbDM3SZwRt2ADngnn0QPMC+eseR2WQw5h89VQc9oS58MvTElxHQwJdgW+EFYA25pS3YGHwctsTZQL5zn74D4vH/yAcy6OwmPMbuONL+mc98JvQDfUTHnD1C17Dv3MdGURf2yAMfdBfsQFvAWgyo+M1zxJ1Gz067iw3WZ6JhYWhoKEQAHbHB4TjVxkgMh4VwIsAoCBWCdBlVQUBHS6ZHQJla6Czl0vD//M//DKFfhIIy+Y2oIsKJ0NppwXAMP8qLYngByeYCQOrynA6Y4ogZoyMMJgLhBREIoOcPQWjqtwNHUREWACvGxsYUWmH4EHqcl8PB3nIFQ4vw02+ioAAtHCLGH2PuFB6OCCeOEXOKks9YCs4O2KDRwIr2IzQYAugOfxht0T+EF9ABYKJPKAMgDYED9NNXQAPOGcdEOThE+sazlInD5DcGCoBbFB0hp48YYRyYnQnPckFnA0BHcWdT4Kwwe5EQDhoHCT89H5D/UTBkkd8vuOCC0DanFOknyo+cwgeMFKAYeQIkwH94R9swnLyL0tFPFA/6o/TIPsqNTvAM8sPnzBGCTUpAPwzs/YmOUT/GG2f9rne9KxhHZIJ24zjhBbRF3gyWeA9Dz32ACM4d+mFAKA/+YjjNa9KoXEREkDPKRlahFeCC/kMzHLIBoOVtvgDQAB1+0gbPn6T9OGzo69Q8usCoF0MHzQ2uMaCAK3QeXlE3aUNkCfvguV0YaJy208m0HTlFn5BxdNET4tEndA49QLb5wykBKLFl2B9kg7p5hrIAH4z+uTDoOGpohdxDW+pxqhsaz2f+H885NW5jjY0jOudUJToJEMEJoTt2Sp50juOh/fAaneM9IqvOWGCnAI2enwS4AVBTDzJi+aYeQAS2CVo4XUv5yCby7HlRfHpwORcAxMHaflrvAGDoBzJBG9BPBs60jzbTXmhncAsYALhjV+gzMoCu8iXvd/sAACAASURBVDzADwCIPeETx4fDpk54Bz3wRdAEPUA+kDN0HIePIwcA2g47aoi94llkimdpD4s1kDtHV73PbGeu3xQALACNWlKhHYBgIenMz2unnLGeqlCcAoDqpFjrY+NhIEr5X/7yl4MM4XcA9eiB575it+ADeoi88EzYLeO979W9990bBioAC9rNu4Aq+gYv6T+DH2h06CGH6rLLLws2C3069MADgk7gr4gAdkB6Z4V9sLGtznzD2QDgWKNzGlHaamtR/6DuvON2ffub3+oEKFRQvdkI7UbX4B36RZuIVKI3yIFT0+gZfILG9IP34B90BhSik9hpZBaZef/7398Bn3NcMQCIXENjBnPe9cADYYAzNgp6Qnd0gsEK/2NjkAPsCvJpGYSugDWAKr4AXvEe/XTQBt1zQO2jH/1osLvYYsrlQrd41jYeeYbGyAk8hzbILDoNSAZwwl/4CMB29hJd8PSBbOBtToJ13Sw0Go0URc2mGwEyGENG1Vw4Hjr8yU9+MhgJGouwYUxcMcLkP37LMgZBJG2BgtNg6kOBiQoCeLxVBETjWQAgdWAoel0E4siiASrtQiAgMsxH6KgLRnMPBsB4mI4zxEl5/hQABIeGgab9MAknjjGHiQArRuooOf8DXDAwMBGQ5BQrRgiFZzSL40E5YTYMhqaEynm3M2rbFKKfi7FOxTnSQPtwBAgLDhQeYjRwBjhAnjMtiLbAbxSYSCSgBOOCweE3hB5B96gc5fSEV/cBugEAcfAoGryEBkTNMDoGg57EbmeYlRNPA8jKFLLCs04zM3rz1gM4RWgJP7ylCs/Tbow/iuntNHgWPtJODBAjMO4DsriHw+Ed5MJTHTAKGCR4DI8wcvQJR4MMZdO5c/HGsgcPPL+K3+A57QF00376Ab+QAU8N8CR9eES9GFYMCLpJu3CSGBx4AgDiXeQZHlMXbeR/HClyCl/oq+eIIps4R/oK2IJ3vIcx+s0IyMw9RL8AFgY5Tm9i7LEZ8AJ+cR85wrlTLs4XueTygijexdDSRpwD0XX6CDBCr+Af/ETGnHai7bZH0A/do1wGaXyis4B5L5rBWVIugx8MMoMb7mNk4T18QOcBYrTfi0GgB/qOnmAXqBPHFdsGA1p6wr9BFf2kTtpLG506wgnSDtqEXqEP6CEDAGiAjeI99JpBALqJ/HihDU4IfnrKA/VgP6CVp2V40RvywgCAOtFbbI4BoJ2m50/NJduO4LuPtIE2kSUwwIeuzu4gA/QXXaOvDLZYCQnPaTtAmIExbcEJo998ApYAC8gKsmSQ5rllAHRkBnuDs4S/yAs2F57jbLHP8BE+8D60xwagW9AKQEoZ3HNQpN0GIAEAK1NLJaAG4K8RQGCpPTXAA0sRFCyUwiKLJOz7l+iC73wjyDt8wwcQmaRN+AXszjbbrAh10i9kn7ppN3oPEBodHwu2Dx2jnfAcOwzt8NPQBNBFeh0akPrFttEXPs98fWceP3JbrXYWNo6MjE1HsUtTad9gc2dIARfZNq3Wp6ee7uzYceftdwQbQRQWPsJr2o58esoE9QKkGNiif9js7CAEGjCgpgz8FO/BN55BLrCB0MoD2F4AIDpEe7GHDPiQafSXwTa2iz8Ge9AbG8+FrGEjPAePZ7Cf8BH5xQegl9Db0we4D44hBQy9sUP88Rx2hj7CQ4Iq+Bf8CrIL/9BlaEu78Ee0DXBJ9Jr2MmjCLqHnlI0uQ19sKLrdUwqYVcBUjsA7XQFTEVDPc4NwPsEDhfXEeBwixOoGfFk0iuJjHFByFA6D43QHQotyQjg6gTDQQToOkTAiEN1zrZxKezZzADHAXqyC8BtY4VAwpJTP7xgfGEY/MToYDE90RUE9pwZmIii8DwMweDAIw8p3lBinizBzH+NNubSZ+9RPfzFqGCKcOgIPLTFO0Bzj5wUgsSgDwun5fwgQggI/PfLGYNAPDCwGBEePYHuyLYrLSAIlgK8oorcIwYgiCwgrvMMB8hxC7Xli0IN+QBOMEk4RBw0dUWJoR3vMM8/BdGTVEbQsEMxGk6EPwg7NeBajh7NiFMYgBFrxG/3kPeiIjNEOwAGOjhEokTHkjncBwnbg8AGjY+eB4qOY8BsnTD+hFe2GdgAJr8CkPfQ9ZqDcR6+g5nnKh84YZoA/MkhbAXQYFaKPjjTzO/JFu72AB57RDqfCKYeoHjSB9zg7jC8yQfnQCzBDfygbOYGXyCaf6AG0RAYp1yuePV9xtj46EubVs7wL7XkfWqNXfIcGPIMOQGPeg3bwCkNK+9ANgCOy4W0t0CfaRt+hC/3hD9rhKOkzso0c4sy9qAM6ekEEAAT+0TfagzOmbnjJABMdQ0bgM3YPOqAryD3yQZugE795QGkwH0sBZ1O/2Uiw08zYCdqMDCLjyAWyzO/IKHYInqMfXohisEbdyAY85xm+Y3PgJ7JDv6ARDgt7BV3RD4NxIsP0CZrwbnbVrAfzsQgLZdpWGTSZXtDWK23pH3Ugk8gB7xF5ph7ACvzwPF1sEPYEWYFHvIfu8SwRJPjnVeLYTq/cRT7gI33lXU89gY7di0AAF8gddgq6Ug92C1ADH6Z5VUqktCyltc5nuBKp0AwRwGqr2ZkDqOI0AFSprHZChLitJ9Y+GEAtNAKoeSERdhX/s+22q0IEEv0nuk9f4YdTppdcfHHouxe9YMvR31123VW/vvPO4DMBUvQZ2cVm4UOIGPLeka94xfRAEGDa8Red+YohO1HehABnWgRCyndw8eJQBvr50CMPB155Sx5PF6Iu7Au+wUEiBlG2n+i8ddj+jTbwG/ynzYAwZJ8yDdZic+Bj8klbvXgTPfHKd+QROwU/AFzYKHTDawIAesiv9wq2j/TcVS+GRBcpF/tAu2k/A3vaDQ8dKECu4A9yxkDOO294YOCpakgXNo36AHoetHiLNe7Rduylp0L0BAA5Cs6RFwryVgFUZDBBg1FqlAWDgvHD6fIbqJSOZ+es2YE7LYlwE/52VM1L/lHAP/qjPwpGgjZ4KwtGB/yGwvQaAURwjfohslfm0k8LD0wyAMWIeTRLe7zVA33gefpsgwK4c3QKg0OZCLH3OvJka55HsRFy6ITw8CwXbcKhGdwioL6cPpwLYDiczPv8UTZtQtCon7o9HwE64BzgH0aO+/CCtnkLIOhNO+krbXE7oQW/eUm/Ix+eIkB93sDSxoLyTQP3gTZ4hSllOIIwEwC0YHdGrSOBF3z3HCWn9Om354pAe6f9qctOzQteeI73oJvrd0rW9PD/Tt9Rt1fIu3w/Mx8DBT3oH/Xxvuec0SYPLOgXl0eCNtBeheb5WU6he3K0I29evGBAQt8NUpAv5N5pCcrgOeuhecPvTuW6nmwqfiY5dNnwhjq86ME6TT0Gl140Qb38blmCF5RjQG2e0QavIHck3HTxohPqc2TNWyKgo94fkjJsn2g/BhrnxTuU4dWI9BMdoHyvaKW96IkHtN4qhvv0KTb/0wNj98u2xFFZR5BxAtDLkXWvMOZ92u7FTdgb5NdpQ8qjvcgn79B/bwvDM07PW65NZ/pF6twri6Gh5dnRXMtCzMGah92RfNrtSKIHPDhabAuOi7bRbujoBQE4PdrqrVlsu6kD++ptqkLKdWpgT//537S1/fJ9eEX/bPcpi+/U6227qA9Q4gwUemnb32pPZgBgpTP1D8wEAFRb/c26CuVSAIDtlChaCaap2e4AwHKtFdpmO0HdtI12dOSns/E3l2XN06q4XyoUSadpbGpT8BrRvKeemk7Z8x66z7PWEUAxEa8PfOADaqkzVaHj0zxNo7O6OWRXQkp79m1gasVyeD8tFlTtq2mM9qdJ6NPw6EhoH/KJXYO3zkRRJjS2LiGfyLmjX9znN9qOj0H/bJMsr4Ar+6XZfGBMPpF5678Hp6a1t2/x4gr7KvtR2mebzAAbeYEWXvBkmiMr1MP/WXuDvjrgZBvgbamgiwdOXklvsMozyATy4HJNy6zt5JnsPO2YrZ6JhgVWAftFGu8O+BOmIChWNKcFeMeObS6A4nQHaNaTJfnNE5NRDISTch0p8ApURkKeN+a22UFBGL7bcc7VhrnuuV8WpJloESt7NvoY3Mz1vg1DN/Nigh1rk+87+jTb854kmx0EOD1sQ5t9t3u0gbJ0389G8AxCuunqd2I0slEz/+1UTZ9YBC5W/pai83z58Wyfi/UvVl5sCkHsfqz8mHzFyo/R33bGYMDyGpzX1NY6tDErl9nvBoUux7pqIGkDPVs/N8eoxmiWvR+TT/qbbUP3d9+3Qw/OfGpAbv3F2Xj+E+8zeCdaQ/SVaNJCX57mQpSKi6gLzs1REKKXOHqiKPY7BmrzGWB126QsjbL2ld+ztonvMRBfTKsivlcpADRTNdOmJtuJklpVpVq/0rGnQ58KKTPipFLaUjVpq5S0EEqN1VbOSd5+dRY/wr9u3vJ/bA64+jsrez1AJ2JE1JqoFFG04cn1Kk5t70J7HOWb/i2clBGkRqUi8xmRt0KY8wjtRqd2BCwqUUHscZiqEGYMgoOTAHznumLyHZO9mH2I3Y+VH9Pv2P1Y+bH7zijYHtm/eQBl+2rZ7fZ/sf5H77MIxI20k81+2riaEDbobtB8DLxTgB7ZANrouCM5Bh8elVOnUz02/G6TFdgK0w1AYgTvvh8DgDEH7BWB3QA562jmalM38MzyYj59iSlYjD6MQrK07QZq3Qa4WyEMFt1Wy0VWPrqFN/tsTEC7I4R+3p+x/sdoGJPf2PsLfT/mAGMGKgbQYvSP9S9Gv1j9Mf458mn9ejYAMLi1rjm03fSK6XesfzH6xO7H+DsT/bJ9yE6pyOquB+rYXM/tw94CxohgMLB2mj3Wxl7uZxcGZe0/csf/RH9I59MuIki0y8B+PtvsuJ92jNlPf+8eHHQDxjn7VykoadRVaLZUUWf+WFIsa6KdarzRVHvZiunXAVX8VdJE5aSlUtpWq9EBaLNdyWIAYAeEsSaj4Hl4RBP5IXItSdkgmrOkO6loZ7ngealUDFnr7tSu/wfHFctsDD21bU3SngaiYY0z0dVyRcW0E2UN9DT4m/o/1sbY/RjAjelnr/ar1/bF+BObIuIB6kwAkL7ZPna3c74BkBh9wipgK8pMD2cBAs9lHXKscJ6HgU5PenTnjnGPjtnIORXK/c4eSZ2d2LsBKeXGGBdjjO/HAGCsj2ZcN3Ay6IkBsGxfsmV0G6/Z+hOjQ+x+N+/dbtN8JgeUpYkdcnf7XK/pO9N9nok52G6A+WwBYKz8GACZrxxt7nMx+YoBqBh/YwY29n6sX7H2xQDOfPqfNY5ZAMjvHoDNpEfZ37ptSLccxfq5UPd7pV+3/XE73V9An1Nz3uvO2xDB+9gc41777YFwNg3vaCW/YR89RcVpYdrlVFdsDqrbn+XnbDKVlXV/j+lHozim/nJV5URq11tSUlGptkhpqaxmIg1XUiVTYKgDAFOVAgBsh2hgo782Nwm7In/Zge187COrdJcMLAmRST9fCVBV2jjBWdKdxYThmgKU0wCQiGVSCZFLAGi72eAME1XKUo3UOdnn+pgS0tBTCzuzEb8sQJmtkzH9jgG8mP3oVT5j78f0M/Z+TL6y/e/2/91lm5ZZOe6VPiEFPBsIsEA5HMn/Njg0pjOhtLOP0GyX54EY7Pk5g72ZDJCBYjba6DZ2f/bqwGMAMNa/mIDHBGS20Wiv5bre+QCgbF3dADDb/pmcpgXYDqe7v9DPTjtbNs/NBg6768y2r5suMf7H6Bjjb4x/vd6PtS/Gv14BXIx+sf7FDFCs/Fj/7GQ8ELCc2RbFInzd9H229Iq1P0af2P1Y/y2fc+lA1ia6vizAyc4LzWYqrI+xNvZynxS7p/a4r7TBU3+Ys2fARx951vOw8Q0x/ez2Ed32tJu+3fIQK7+ZjGmgb1DlYlmNeqJmUlRdVT0z3tS6jSNKVjFPjARpceo4tSQAQKJ/fA71Z5bZzkDIgXZmEcbUfFXzxXSai/4bWp1Ff46Eet41NAzRygDvMlfym1HF/uIipU1poCStGJCWlKVik7+6WB9SSyfCy/QvJH/DnMTO8SGAwZRU9xxXTL5jshXT19j9WPkx+xu732v5MwXUuvWye/Dq+/Ppe+yZwsMPPxwWgcwEAgwAUVYYyf9OvXbnqGcjhCepUz4CyafnDvK/J15Oh6FbrRA1dMO7U85ZYzefEUiMQTDAypM1HgahMQMxE+1cJ/diDAiKNDUy6x4BzOf9XgU061gNykzjbP0zgT9+y6bxZ2oL9HUU1HzNytN8+JOlZ/fzMQcdo0/sfqx9C30/1r7Y/Rh9em1/zMDH5D/WPkf4LKeWzVi57pftVree+f1Y/bH+9Uq/GP+651l362E3Xbr7CTgg8senF1B5MQh9i0Vgeu1f1r5kAaCdWHYOMn11+pff5xOdnI2/poPr7Kaz/4/Z94o6i5PqrbbSclWFRUu0brKla26/S9fefIse6lvTmR0HMJraK5DIH5FAYKE62+zNerVrnfN97UeyNIrJJoUuLnX4Sxlh0UhmV4jO/MYO4AsDpuyWL04vtx5SOUm01w5rdNwhB2m/HXbQAOf+ToxroFJWuzUc3jcATIudBS+hzLA9ztwBoJj8xOQ/9v587cBs5fRaf6x98+GhsYbb4sGt8VfWH5uX8+137LnCfffdN+NEg6yBnAkcdgOf2QjhiKFTAJTrJf8WfD+D8AIWshMju1M82Q7NByDFGNQNgLIgjPJjEY7uFLkNW7dCxwSwm1HzFcwYg2MGfqZFIFnnmX1/pjZ106/7GRt0ngMs2lDFJl+7DTEFitEpRp/Y+zH56fV+rP6YE4wBlNj9Xtsfc6CxFEiMPzH+x96fqX9Zmi80fWL0jdFvJvuTbb/tj52EHcS0058a4HoetncdoFyv+I21sZf76Dw2nTa7rx7sexU05WfTwvAEvvNbLAXsaULZ/mdB8kzy82z431/unPZSJxpZKaswsFgbE+mOxx7R3Q89Im2ze5gZ1y6UpxcIhzlzQCZAYKGzun+2q5FuWvxhn8FnNtM21/vltDA9zQp6om8Afa82VrkToQt+aQoA+pQPfluxuK7J4WGt7O/Tgbvtop0WL1U6tEHpyLgGONGkxArgTQcGA/q68UAv8hGT/17tR6xtMfsb85+x8mP4weV326FsZDAr267Pdq9X+hRGRkamAWB2NOlKrayu2EJqoBMzoAhlNsLGdxrtkRvle/GHo3HdEbluIm+O0Z+NUUbZWTBpOtCO2CROR0L9fhYAci/mwLOoP9vG2X6PCVz3/ZgAmj9ZwcqCuiwAn6numRxQtxMKqYipzb8p29u52NDP1adu+dpcoDwX/58tTbfm8zH9irUlZmBjBjBWfqx9sfpj7/favm6b9mxtyZa0NTPRMtb/mQBMtk0x/Xa0j7odgXdE3uAsxuNe7mczOO6L7TvlOiWNnNiW+nf+n4/89CIjsQGGCkWO9w2BtCRN1EylZqnIVtDhr6okRMf4no2kGDKNTC2amI2GSzpxwuksUPCvUw9TXuwYgA1qqqSSaipqQi21k7YGizVNqOmkdAcAKu3UM1V2e+rbUpU13pbSZkOLqlUNkOFttwWw5JzgOgtFUkNGqZidQNh14MNMfYzpT0x+Y/zvhfdZPzUbf2Lt60U3tsS7vdJnehuY7shaFuh5dOaImEco85nDRbk2OAZDjvD5f2+sy7Oe90EdBonZEd2WIFq2jFgEMGYgsoaLsrIjt6yhm6vdsylJFpTO9v7mOJDussxrG14+/ZsFLNvG7PdsH2cbGXovKO/bx5YPbAM0n/7NpeAx4zIfWenVgMynjl6e6bWPC92/mH7E6p9P/7oHnZbPbnmdic4zyVi3/M7Fn1j7e+HtfBzQbPXPRDf/ln3H3z1Qc8QgBox77Zffx34APLH5rtsZIE8Jsj+xs3Uq2Cuc58sf9z/7GaNfzMFPqh3gXTUgp7bUbClhG5hiSaVKVQXm1DHVR+XpxRJBPkNiOFEhslG8qlPz6aY2Zs7a4nnJR1pUY2RE1VpNLSKAbMvFiUPj4yrValKxbxPqm44AboKqhXqiQoW9CxX290vZF7DcaRObvCRTaW2AKK+HhS5TIDCsCJ6aD7i58jIf/Z8v/zenDb3WH6szZj/mU7+DStm6uuVktnbE6v+NCGCsM/n9nALdRgkBM0jMRm7tYLITmfnORrXsau7TAOajADnVcwrkFMgp8HykQGwAHnPAC92nmH1Nm3Mv4khZCpxfL1oK5ADwRcvahetY1qj4RASiuB6pZKOCTkH5Hnt+AQB96sJzbSAXjkp5yTkFcgr8rlPgubZvMQBYaG9K787EqxwAvrglOAeAL27+LkjvskbFKwud6ifKx1YOfPp4qWzaKQsAt1YaakGIkBeaUyCnwO88BaIAyzs7P0eUirYvttd0mACZXy9WCuQA8MXK2QXsV7dRYW4fc2lYscf5hJw3zOazHBDO/9mVSgBAjvrzOYsL2My86JwCOQVyCuQUmIsCXfsC/tajOQB8UctPDgBf1OxdmM51A0COswEAcnj3jTfeqMcff1w777yzDjrooBAFzAHgwvAhLzWnQE6BnAI9USAHgD2R74X+cg4AX+gcfA7anwWApHuZz8f5orfeeqvuueeesOqPRR4Aw5NPPnkaAPJengJ+DhiWV5lTIKdAToEZKBBNET/HKeycaQtLgRwALix9X5Slz7SNBgCQdC+TngF/PMNvO+yww/R5rd0A0Ps+viiJlHcqp0BOgZwCz3MK5ADwec6gBW5eDgAXmMAvxuKzRsNHNvHJghAuNs/mGdK/rBL2XlszbQMT20bhxUi/vE85BXIK5BTIKZBT4LmmQO8AcGqS6KybFcZWGUUoEFtGHxvBPNcEfrHXD/3ZB9Dn+3rT1+6zPNmIlpTwAw88oN122y0sAuneSHwmXo+NjGpw8aJARjYmDccdsQlrqcTW/EqLbbWTVIViJTzDrgZpmihpcah8SeViLdTDld1Q2HzhNwAr5Xohi9vhTb6zz77Y+Zn3L6dAToGcAjkFfjco0DMAxClnr986tSM2yTQHgC9oSYPf2eP+srv+87v3B+Q7EcL7779fu+yySzgOLgsA5wL6nGvJsVWkln2GKfWE84QLLTUbbSVpSWlaCCuRy+XOTvxARqlz2PpsW85kN6o2I7o3tn5BMyhvfE6BnAI5BXIK5BSYgQJbDADO5MBxusXpkw03j/55BHDz6La13mKFL+DMETZH0nz8m+8hC8uWLdNDDz0UVgg7XRxr5+joaFhkkgVn2eOb0rTdOZx86siiZqMVwF652jlKMHuaZjZabLniN9rodhKljB1AH2tzfj+nQE6BnAI5BXIKPN8p0DMA7AZojrb4M3bWYoxAOQCMUei5vU9Ej3l+juYRmYNnAEB+A0wBxAB8LAh54oknQgQQoDXXNQ3WArjrRPA8mAhnVqbhUEolrbbazVbYfHrx4KDKnH85dTVbzdCOLNhzJDCb5s3KqM8hdf05GHxu5SuvPadAToGcAjkFFoYCPQPA2ZplBxoDcLFuxd7P5wDGKLiw9wGAIyMjYesXgJ7TpwaApG9JA7Mx9B577BHOAt5xxx0DAIwd1E7LG0lb1VJZnD+etlqhnjKp31IxJHi5OLq8PjGpWq0DKuv1SVX7aqq3WyqlCvVzISvZlK//p81euALgC6ll5hNOzW1cWArmpecUyCmQUyCnQE6BrU+BngFg2nWWIE7dZ8HyvZW0e+pVDgB7It+CvwwAHBoa6gCzcjmAJoAVUTXAlucEkv7de++9wykhnAUMKENOuiPEWUDP94YStSbrKqUF9fcPTAG5RKOT4wHgFStVLe0fUKs+oVq1E/1LiBAWyxprNlRqdBafUE83AOwGeLQ323bSwt2Rypg8LjjB8wpyCuQUyCmQUyCnwBagQM8AsFQoTjtWr7Y0AAwRoXTuw6ZjfYg53DwCGKPgwt5/8MEHtXHjxgD8AHXIgPf3szzAIyKABoCrVq0Kz/I30xQCR+v4nCwk6i90IoBDTz6t7373u/rBRT9R36IBHfzyI/Tmt52t7Vas0OTokIppElLNzSTVE+s36meXXqY3nXJKmEPoxSjZCCDg9FOf+pQOP/xwHXbYYQHAcqwd6WQWqQD+nNKmLV5FHJPJhaV4XnpOgZwCOQVyCuQU6J0CPQPAgb7+4PxxlPw5BWhnWSyToNv8K+ZscwC4+bTdEm+yrQsbQMN3UqfeFgbw74gan6zgfelLXxpSwKtXrw5Abab5oeanPx9b/7S232ZbrX3oYX3jq18P9bz29a/T4MrluuaG63XpVdfo/eeeq/333EMlJao36ipWa0pU1p0PPqAdFy0OdZPa7Y4AsmCFzappi9O+tMkyR/rax9hNy3OxOGvqekvQMy8jp0BOgZwCOQVyCmwNCkQBYKlS1IahEQ0OLlZfoabWyLj6Bvo0VmrpofVP6IHbb9NYvaGTTz1FSyqL9PRja7VmzXYanaiH6E1JFTWak1qyqF/t+rgWDQxqaMOwli5dprSVaLw5MR1ZwSHjqEm9+TgxO2E+2TvOKUYvOuDZJUuWhHQiz+DsOW6MKBNn0xL9IarD/3yyqpRy/B5zv4gOsZCBqA//AwJ4FmCQA8yOGAKAsgt8HOVlVS+gzv/DF0CTVwPzDrxcsWKF9tlnHz322GPabrvtAq1nWzmeraesx6TyUl3y06v1k59epb/8q7/RosUltdNxNduj+uIF39HE+kH9yR+8XY8+fKcefXhETzz9iPY5dG+1Wzuq0j+g/bdN9PT6DfrSN7+jvdcs056HHKeH1rV00qF7aO0dl0q7vlz9fRXdfduNSpZup0sv/Ir2WVPTKe/4gKSaagSxxxsqFNvSQE1tFdVkgQtgcGtoaV5HToGcAjkFcgrkFNjCFIgCwEIhVVIsqVbr19jGURVaTS1euVT3r39UV97ySz217mmNjY9rj9330OuOO1m1Rlu1pKSNw6NqFaXl26zUxFhd/RqcOQAAIABJREFUzcaEFg/WdPmll+nSiy/T2sfW6h3veIdOOu3kACAAZZ6nBfgiIgNI86R80nL8D3AAXHiuIeAP8Ab44+/iiy/WqaeeGlKOXN5/DuAHeAQgAlZYuEAdYZ5iq7N1CN95htQfQJC6uZdfzx0AVPKYknZN5339h5qsl/T2d5yjgcGyCqW6CoWGbr7/Xl30/Zt0zltP09/97UfUmOzXq04+RocdfbD+/d++r9e/+WwdvLqtL55/njbWE62qtXTtbY9q9e6H6p/+/Fz9699/SDsffZZOfc1x+oNzf0+Ltt9DB+2yTHdf81Ntd+Axeu8HPiJmHpbrTRXTlgpTAJDNp6tTm6Dn8pFTIKdAToGcAjkFXmgUiALAAIwq1QCWRoeGVesrKekv6pJbrtZPfnWlTjvnHXr88cd1942366wTTtXei7dVbTxROS2pUqtquCmNjg5ru9Ur9F8//J5uuelmvfSl+6m/NqD77rtPS5cv0Tvf+c5AN+aSAcx22mmn8D+pRaJwjtIB3ABl/A9YAzACHony8RxA8JFHHgn7zBnI0W7K9GR+6uA7AJEI4fLly8N7LFLgOSJYnvvlhQwvNKYuRHufswhgeUj1RqrPf/Y8bb/dbnr9G07TRL2uZosTQqr6xa9u1tf+82f6h7/7v/Tvn/jfeuWRp+gVxxwklVr6n3/9SZ3xpvdo7J4rdP0tN+sDf/E/tKy/rU//5wW64a4n9an//ef6xEc/on1efY4OO2h//cV/e79Of9u5Ou2V++v6H39DV931pN7whx/Uqr4B9bNApDmpUq0aFpiwBLmYSoXeZjgsBKvyMnMK5BTIKZBTIKdAlAJRAFgsljUxOalyrapSqaBKX0m3rv21vnjRBbr96Qe0/TFHSvWm7r7uZp2878v07mNepzXFQfUn5U5UrjwQ3hsaXq/PfOrfdeaZZ+qwQw4PDfv1HXfp6+d/TW9605sCYNtvv/1Civbhhx/W9ttvH0DdrbfeGgAaW4iwj9y99947fb4sqduXvOQlAbCxGIH6mF/mNPJdd92ldevW6YADDtDKlStD+nHfffcNmxET2eNd3gP07b777mE+GJe3MHFUMErF34EHnisAOFZfq4G+Jfr2N76vh+57TB/6iw8rSVpqF+oqltq6+OdX6aG7JvSed5+lv/lff6Z3nv1H2nHXZdo48YS+/MVLdNQJb1H9gav01NCwzj73vSo3R/XTK27SFb+6X3/7wXfpox/9kI54w/u13col+trnP6Wz3vV+7bpUWvfra/S9K2/T8b//Pu2wdLmWFopK6uMqVSvh2DkWpRRb7DGTJ4F/B8Q/72JOgZwCOQVedBSIAsBKpabRsbEQASxWpXYl1YXX/Fj/8K1Pa8NAW483x6Vyv/rHEx24eEd94r//tV46sJ0q9VSNyaZKK1YoSVLdeMN1uu32mwLYK6qkyclGAFo///kVYc4eAPBtb3tbAIDf+MY3dMopp+jmm2/WNddcE+b4nXjiiSEy+LGPfSwAQeaUAeLOPvvsAOwuu+yycMYsUcX3ve994d2LLroopHZ33XXX8HfTTTfpDW94Q/idDYn/5E/+RJdeeqnWrFmjQw89dBpYZuezveg4vpkdeq4AoMpNlVTWLTffph9c8MOQ3j/0ZYcpLbT0wCMP6p8/9i9651s/rJcdsZs+/Bcf0Nvecq4Of9leGm6s1d/9v5/TyW94v5oPXaOrf3Wj/uz/+SstK0/qo//xdd3zxIQ++b8+pH//P/9DLznhrXr5IQfqo3/7Vzrpje/Qsfvvol9f8X39+Pq7ddx7/kg7LV2hZSxqqU+oWCkHABiif+xwVN5Mguav5RTIKZBTIKdAToHnkAJRAMj5qsVSKSyUmJgcUzpQ0HlX/VB/+ZWPq75Nv9Jtl6s+NK7lSU0rhqWv//XH9ZLqSlUnU9UWLVYjkcYnxnX//ffoF1ddrrPOOkuLFy/VosFFeuihR3T5ZZeE6Bvg7LjjjgtgDtDGXL3Pf/7zAfSRqgWkHXzwwbrhhhv013/912HvufPOO0+nn356iOgxR5Co4QUXXBCA5N///d+HeYVE95g7+LrXvS6AS4Ae4JBU8UknnaQbb7wxbANCGwChPsLMmxp3jhPLr+cKAI4k4yq2C1raP6hfXnO9vvCFLygpJOobHNDiFUt06IH76oRj3qpKra1PfeZftM9eh+roYw9UsTKqf/mnr+iEM96r3Wrr9akvfkn3PvaUDnnJdnpyrKRWbVv98//8gP7zU3+ngb2O1WEH7qsvfPLjetmJr9UZRx+ku678gS6++T6d8O4/1ppFS7Q44djhulQuqVAsK2mnKrEEJE8B58qRUyCnQE6BnAIvQApEAWCrnYboX6lY1NjYiAZWLNP5N/xY//1T/5+08zLVlag92dKypKLtGv368t/8i3brW6XGhgnVyhW1CyVV+yoBAH7py5/XBz7wAS1ZskzlUjXMHfz+976rc889Vz/72c9CPczRe/WrXx2A3uWXX64zzjgjzPVjFS8A7cILL9SHPvShMA/wiiuuCOli7hMhJFVMyhhA9+lPf1onn3xyWHHKfRaPXHnllQEMMkeQ1DC/cb3mNa8Jn55X6DmG+UkQmyT6uQKArXJZT6x9TMtqfVq+bLnuuu12rR8Z0sj4mPoWDeroIw/UxOgSNoDR0MgTWjSwWoXSiBYvSnXXnRuULNlJ+62RWs2WbrnnQS3WqC666k6Naqk+8ntv0JMP3ybtcqgWV6WnHntIWrxK2xSHtai1Tg+PllTedk8tq5TV10rDIhBVWIEspc1UZbY4yscHL0Czlzc5p0BOgZwCOQWiALBUKatRbylptzuraLdZoa9d91/6g4/9pap7rVFrYkyL+xaptW5MOxWW6nN/+U86ZNs9pHqisbFxFQolDQ72q5009K3vnC/m5R1++Ms0Mjyme+6/T/vtvVcAeZwQ8YlPfCKcEvGe97wnpIW/8pWvaK+99grgj/QuAO+SSy7ROeecEyKEzNljI1+ig2984xtDJPDnP//5dJqXiJ/fBxTecsst+td//Vd95CMfCfMEP/zhD4d0MQDwmWeeCdE/QKhPtfA+drmYPHergB8Z3qAdVqySWnWNPPOMVq5erYmJyTAXr1iuqaj1ak6sULVfarbHVSkOaOPIk1q8hJ0A14hZna1Hf61f3XGHktoiafgJffdn1+ndf/JXOnzHQRVrLT0+sVS1clsD5ZZa5T7VWhtUTTaoXlqu8dLysAq41kw5HE4qF8UK4KSVqlIp5wAwV46cAjkFcgrkFHhBUiAKAMcbda1asVJj42MhnVpeNKBf3PRLXXDRj/Tk0HqN9E9qsNanxoYx7bfTHjrn9W/VXmt21fi6IVWLJRX7ygFQeTUvUTu2aiH69vKXvzykfflOmvfuu+8OwI5FG7xzxx136Ktf/WoAazzLPnJE8Jj/B+gjDUx5nDDBJsMcS8bfUUcdFSKE//iP/xieBzySegbcXXfddaEcooDf//73w7OU7+PC6KP3GnTU67c5u+l0k5S94dKCCmlRhbQzIYz5YSp0to9pFzohorQgtdM0nIySlAqdVGKpqOpEQ+VCMayabhcUts4Ji09aTRXrLRUGB0J7DEZJSTstTZksRtga19aKAAZapemmv+KQlJaktCqlnTN9VWhKhUaYhFdsQfOykrQmkrIBphXZuqhzf7BQ0n13363zv/ltrX3qKS1etFRnn/Nu7bDzTkHuKs2G2oWyWoVioD1Z3XLSUlkcP9dUq9ThKQOZQsj3+nOqKVuD+HkdOQVyCuQUyCmQU2ALUyAKABtJW4sGBjQ6PBJAzJLly7V2dIPuX/uYVC0rLYxr8eASjY+OaaDUp9132F01lTUyMqqB/n6Vm53TQQAtPiqMvfn4H0BG1I3IHoAPEMbvzOcDBADM+COSx1YvXIA+ysF5e59AwCPPsaULK4Gpz9vFMA/Qp1PwPnVTF3XzzPr160MbKDMAtqmzbAOQK3aOufvta1Per6CWUhUD0OOTNaEcSVZUK3wm5WoAb0rSANZKhc6qUYOcwcWDGh4d00SzEdoEaE5a7Q44HKypOVqfrj4J8KYDJgFkwNDSix0AFkYC6ArgbwpgB3ANCFRbhRb0zALAgpJiEhaJJMWWao2mKn0DSpNEhWJJExNN9fX3BzoG+jU2AUAAeJjWlxoAttQKJ4N0JvoVwooPeLxp4l++BngLW6S8uJwCOQVyCuQU2CoUiAJA4E2lVFKr0QyurzbYr0YhVbOQqlSsqdwaVrXM0VtFTbYbaiVpOBmkEaJ+g1rS7Gyw7KPiAF3ezBlg58tROAAYgBCABCAEFLExtC8iewZs3hia6KJP+aBsyvKZr9TBb+wpSJnMCeRZUr5sDZPd6iUb/WPeYAcAstTTgI/w3FSYaKpB5SRVq1hQo0T0jnuJSmlbZUBEAB190wCwDIRICyqDPNpJSKsPqa50Sb+SvorKrUQD421VVdREWRotttU30ggrTzlSLw2gLw2QExBI+0pMSNsK13MRAeyA3dEp4AcInFpyG6Kr7U6UNeF3In9ECIuBLkmB9wCJLWmyEXhcZDPzPqKpqeqNzrGF5XJVA9WCkkKZ0tSeYnMp5QSbzsbP7elFQEg/D+QAcCuIW15FToGcAjkFcgosMAWiADAtFSVSvzi+KSBHGpPfW4VUfQ0iVJ2UJhGwRtoOJ4cAWriWFDvnw/rYLyJwADf+B9zxP0AP8AUwA9QQ7QMIEu0DzDnlCSjzma6853fY1JnnXRYgEeBHVI93KI9PfsPxs60M9bPgBPDoy6lW/neqdXYA2AGDgDyAX72sAAIBHuW0rWq781lu9QdQ4khiiAICJdhHLpWai8pal4xrpDWupUlZa9IBKSlqYmxY941u0JqBzjm2pb5qAIHNNAl/RAhVKqrc3JSOXkhZ2doAMIC/ANyI9AG8iLoZiAcE3YnhpYUAjAGH6dTBbABAhWgpcptqsj6p1hSdFi1aIoK6U4HYEJkN6XmOupsiIN9KaqmgNjHGTsh1Kg0cPvMI4EKKWl52ToGcAjkFcgpsBQpEASCnedSZdF8oBvcLyCqTqqxW1Wg1wybPpGzLUxGzdovVkeUQNQSIEa0iLcvl4954HkAB+KIcH/MG6OJdrpA2nQJi/AZo9IkgADWncQF5jgoClLjn83y5B4jghA/qAgQS+aNOQKMBJ23JzvebBmvTKMGccASwAwO4Sglzx4pqgpP5K7SmI4BEAquTFaWlglqlQoiaBvBcLKhaKoe+jtfH1SwlqvWVtayeKnlknTY8tFbDhVTFFUu1cvUqlSsVVftqYe6gAWDAJKWiKmxGvBWurQkADf46oAugFyShCwB2A1+g3qbLd5NmXWxlFOS10Umzj41NTJ/2Uqt1Uv8duLippk6CPVHyW6f9/uay3/w04K0gfHkVOQVyCuQUyCmwxSkQBYDT++GpA5KIBob5cp6rVysH0MXcNaJbTcBiwm4Z5TDvqrhkYDr6B6gzsLOTN5ijbM8RxFFTL5FBgB9O2+8C4jrAoBAiY4A7r94ldQyoI7XLu8zvy87jc5oZ8Jjd6sUA0FFKt20+J4GwgKAYFoF0QEohZcO4qfQk6eBmMQC3RlFqFjvp2zAfslgKoDndOKKliwalkRE9cvvteuS+B1TpH9ROu++p1TvupNZAX4j8AfYAfU1WYwMiYUWY/bZ1ZqEtFACcDWxvmnuZhWVZ+e/8nhb4zKbof5Me7Xaicrlzf3KiHmSM6Qx9/bWp9+fWqfQ3YOVvP5sDwC1uk/ICcwrkFMgpkFNgK1AgCgBZkIDTBAAE4DG1QrPd7ICoEgs9ymV2x+ik09qJCiF1VwwgcMNYJ/rGMzxPBBEQxx9lAvB8H2DmBSO8D+jzghHeN5jzQg3u8y4RRp8TbHAX4NgUiAznGI+OhpQyUUhHDanfEceZwF7nNwDndDxJCoBjU7xoslxRpVVUf6uoSrsYbqfFRM1SK0T2iPY1tSltC02YA1hotlVotDSQVJXccbfuufoaPfz0o+p/6c7a89ijtO1Ou4MYVc8APOb/0abp2Fdx64A/A+7s6lyflsLWO5zHnD09BZqaz7wDT1i5zeprNvpmHibzMrNR1yz4ttx36qOPs6W5AcWd1dabrk1RWt6st1mM0znerx0W/5Q77JPUGK+rPFidXc2mUr+dEcfMkdYcAG4FK5VXkVMgp0BOgZwCW5wCUQBYaCeq1GpqtluduXukHUvlAOACkBqrq1AudbYqUVukjFsANxxvsajBSi2keJ2y9Tm7PM9FGZQLmOO7I45exOH7Bgg+rYP6+e7Nm71yGEDo+YB890ITvofTTCYmputymY4uzgQCvQI0gJDpxQed9CBIYqRSU3+roEX1osqNcufnotSqAgClicFCSJWHVc6lihZX2GdO0sYxNYfH9OufXK5119+i1Umi/U4/Xjr5MI0OSqOjLa0sLlI9KU9HP91Xg2foR0Rxa1xbOwI43z4hdbMmZVPm9nVKCs+wgTN/7aRzlBuDjLLpx8Cl82TAfdPz/jrvzAQCkRemRuRXToGcAjkFcgrkFHihUaAwOjq6dRDE85QysX30HIEKCzkKidrFRK0CKVjmh0mtRk21UlkD5aoqaUFJvak2K6antr0ZKTTU30qUNBuarDRVWSz1r3tS9R9ernXfu1SPj67XmpNO1OrTz1B5l33UblaVTiYqVIpq9hdVSjatgJ6JhN4GxsGqAEunQM+W2CcQcGygnN0Sh9/o4wMPPBA27fZCnbBXZLmz96N/mysCOFOffnPrHVaQt1UuFsIq3GadVb3M0Cuob6AzX3RyvKU+5o4a5VXYnEesr1Y17axALySp+moVTY53ttXp6+ukgAGFk3VSwpVwwkezyRZCJbUbaWclOUs+CG+H/X06Uz9ZRcwgo7+/puZkPfSTeZqNqa18KJZNyllsxIDDC42Iinq7Ia9C51k/Q30MZHxCjee2mo7c8/ZJvJPdvmg29TItu1PtM/E0OwCaKSL7bFTY9dEnR4fnM6Xi2dSRP5tTIKdAToGcAptPgcLw8HAOAOegX0g7ZwAV59B2zoQghJRocXGZ2vWG2pMNtdstpeWiCjXOmi2FhSHtcllJfVwrGg0NrN0gXXmN7rv4Mj2z4XFVlvXpgFNOVXnffaR99lW9f4VaY6kK9UTtSqJGJVWtzSrY2S8AYBb8BUyzBQGg50e6BVkgAW3YeHshASDgrMpqbtLdEH5KWiemtncJUWNWVldZgcMcyZYqtXIAgGxFRISuyorpALTqGuivdVYBw8NWolKpqLHhUdUG+lWqElWeChMmU0e9tdOwXU9CtI/5p1NzMSkv7BAT2sSS7k37O0IzR5UBacxTZYNzIt2UkV3o5CkQAMowl7Zand6Lkmeht+fEdkBnf0ivcxlMbr769/6m29INGC0nz4c29t7LvIScAjkFcgq8+CiQRwAj8NebL08DK078mEZDRZUn0pD2Zou6ptpqlVOlFRZ9tFVv11VtpurfMCxdf6tGLrxYo1fdpGJ/SYtPOESDx+yvwVccJy1erLH+AY03iqpMpOpTSe1yoolSQwPenG4W2ctujJIFf1tKVLOAz3MADQq3BgBkexZmYTK3cmJ0XP19fQF5tQvtAPZKaaFDf1aoJ2kATwFkAcLarRDd87QAonLLVy4PzzYazQCuBotVNdnKqNzZuohoIVeNTc5Zy8Nim0bntzDzk4gW+zJOLYKq9VenpzB4Fbq3OGJBkvegZJNyr0J2VJX7fO8Gh1741NkGpzMX1oul6AuLnfjMLnCajd/dC5t4bjaw5jJmemc+5c9H5mZfYT+ft/NncgrkFMgpkFNgS1Hgdx4AxgiZJDh/zwtjKxxWOxfC9i9h3UurrXZfUWOVluqFlqolaRmbDo9OKN04qokbb9TIxder/YubVE3aqu+1vSYP30fbHnOUFh10iIbSVPViQfWkoFIj0eKUdHJR7WJLY8mkauqLNbEDThZoPYhXbWfnAGYB4EKngOuFVBXo30o6q3cHahprjKtYDbspdiJ5SjXebKivMhCeJWLH/owV5ueVS2o0p7aSKRU1PjkRon2NJpHFqqoTdZXYYidNwnzKerOz0Xi1VNHoyLCWDg5OHRnSWX3eDBuRV0XR9cmGCpVOtI93ADfMj+UCpLHgiMtpUJ8xzW/MTfV9Az4Anbc6Mr3536faEG0jQuj0L+/FImxZvgU5mZrQOBfI873sIp3ZhNBp3ZlW0vOOo5h+34MIzyF2enteQp4/lFMgp0BOgZwCW4wCv/Mp4Dglme8HAOxMAiumRZXSoioJW75IY+m4Jqup6v1Ea1ItT1oafHidGtfdoY033aWxX1yk1eOAu1RP7raNmm99tVaeeYb6+7bT+MNDmlyzKKQQS6T/WokWEdEqsl9gSxMJ59lOzVWbpaHsPcgVm8sY7+fsT9i523nzpDfKXugU8HjaUq3Y2asPDkykTX3voh/osaEnpo4HLqk9CeCr6ujDX6mDd32pUjYXFyvNO3P3JibrSth7sVbVRZddqkNffoRqRBILRS1mfmC5rPWjQ+GcYHauHBoe0qolKzVaH1UfMDPM5+ycLtNstFUtVVWpdADhRKuhcrGkUrGk+uSE+vv6NToyElLjBx54YIi2sfIZ2hEFpAyAH2AOYGcwyLZFzPEDGDnKZ+BHFNGgj09Aof//zfmSM/Mwu3p7Ps9nAWBsLmAWAHquX7YVjmBaZtwWgCx9zm7E3ouM5u/mFMgpkFMgp8Czo8DvfAQwFjkrMAcsbPK86TQItnFhwUfYBLo8oVKVWFRb7fVPa/z2uzV69a80du1tqt/7sHYZKOiZwbLKhx+obc44Vcn++2p90q+yBrXdosV6hmllAEA21W43FY4hK6ThODOuVrrp3NmZWNuaAoBOBS8UEJwtAnjfffct6BzARoGFGCW12lK7JD2w/jH9z3/+G9380B3i0JSNpZbK40VtV16mD77lfXr7SWeqVG+pUkjCli/1iVbYvqhYrYRTaj7x2U/rzW9/m5YtWxFS9sskPbj2ET346CM67PAjNJk2deONN2qPPfbQqiXbaGBqjXE9aaiQFNRfHgwRRyKBtSobj3f2rKwW2OaorjILY+67X4888oiOOvIVYQ/Ha6+9Vttss03YBocooIEf372imxQvANBRQY4uBDByOYLId9LGzCnkFBu21gEMxq6wdVCyaQun7sU8vN/9m4FfLEJn0Jf9DGB9ahN1b+3kFexuB+CPvpoWsT7k93MK5BTIKZBTYMtSIAeAkdRp2uRouw4AZDpeoZBOn+dbThPV2Ebksaek2+7WyI23av0tN2vskYdVK7a1bMmgxleuVP+JL1f1pGNV3WFX9TX6pcmSkmKiDaWGSoVFKiatcPRYW001mexWLKhUrIYUZsH7mMzCd7aaMejrBoH8HgO4MXF6rlcB19Nm2ItxopmoXS3r0cbT+rN/+L91xzP3qbxNvx4rN1QbSrTtSE0feeP79PsnvjGk0lvNCRXLBfWXBgIAFKuqJX3sk/+uI487WqpV1D8woB1WLNZV11+rG2+9Ra869eSwZc9Fl1yslx9+hA444AClY+NKWmk4S3rpoiXabbs9NdoYE/sfMh3gJTusEXtiAnTWrFwd5hPectPNWr50mfbcc089+fRTAiSz9yGrggGCgEOA3/bbbx/2UDRgWrVqVQB2pId33HHHcM+n1ey88866++67QxlcRBB9pvZcPMwC95lA4EwRvmwEMAYAvaG6I8LdqWCvCPeqcS+OIfLnIxpjMpjfzymQUyCnQE6BLU+Bwvr163+nVwHHAFKpVVC7nIYVvWmJjaFTFVO2F+FTat12m4auvlXJFb/SNg8+pSXNSbX6mhpdUdDkiqpWveXdSg7YW+O77KLJ8YIWbWhrkCPdFrf0eG1MKybWMGtNKjRUL7c1XkrVZOFDUlI1raqvNfdZvwDAACCmuBh2Kpn6viUAoMFJ91wyR3wAMwu5CrgdNt4uaYwIWamox9Jn9P6/+7Bu3XCPtKyiiVU1FYakbR5L9Renvlt/fOo71McrbNPD/MF2SUPDw+pfulgT7ab+z+c+rXapoLXr12mHXXfWKce/Updf9XNdcdUvdOyrTwwp3UsvvVTHH3+8Dj30UF3905+FBRjDG0e068676qhXHK2Nzwzr+muvU73e1JmvelUAaRNj43r1cSdq3VNP6obrf6kTjjteA4ODuu7660KqkzLuuOMOnX766fr6178eAOAf/MEf6MILL9QTTzyhNWvW6MQTT9QPf/jDUN673vUunX/++eH7tttuq5NOOklf/epXtd9++wVwCBD0HpdzmQUvFMnuddmdyu9OC2dXfsdSwJYPA0DLRRZEAo7DnolTe3dCC2hCH7wiesubtrzEnAI5BXIK5BSYM0DwYgeAsTlPSycTPVlNAigYaJVULxW1cXFJ5aSgFesbqqUFNfpKGi0lIT27A6eijA9r7FfX6o7rrtbgDTeqND6pbSbbWjleUGuoqUeXDqpw2rFa9fpXq7373oH+7F3X2WGkoGQKdfLdx5Q9X8U07KE3dVZyto12+A8//LDWrVvX2SJlKpXok0C2xD6ALLhpNBPVS21tTMb0lMb0vr/5cz3eGpKW1LS272ktShar//5J/dWbP6j3vuosTY5OaHBRv4gesoqaldws8JhMW/rk5z+rAw47RAcccrDO++Y3tM/LDtKK5ct17dXX6E2vPVOTzXFd+O0LdNaZZ2qg1q+PffLjOv2kU7TL9jvpsquuUrvWp5GJST3+0KP64Lnv1yK19PQTa1Vsp9ptx5112aWXhnOm999//7Cg5Jqrrg5AkmMJDSz55DrttNN09dVXh7mCl19+eQCH3/rWtwI4Agzeeuut06uAKe+iiy7Sa1/72rAQxADLJ+cYoDvKB+29AMOALxsBNC+zCz1m2itwpnl95nO2jO7IX3bA4Hb4GWSKxSuksGMRxuerXuTtyimQUyCnwAudAoWhoaHoFcTEAAAgAElEQVQXdQQw5mD62wWND5Y1mta1tFDVgAp64vHH1Rgd0y6rttdktS1Va6oB/IaGNX7rnXry2uu08a7b1dj4tHYemtCStKz2yKg2cA7yXrtr29e+RpVjj5S2Xa3JEitBO8nZsHdg2gEk3U70+SxIWQDYPen/3nvvXXAAONlohK11klJJT2lY7/sff6Zbn7hf6UBZ42uKKm9sackzRf3l2f9N57zqjHDEG5HaUl9R1WYhzMNLi0UNT47p81/5ko4/6dXafec99bmvf15HHH+UxkZGQ3r1zNNfr2fWr9MVl10eAGCpUNKXvvFlnfaqk7XLyh31oyt/ppFGS4cc8TLdccutuunaG3Tmq07U3nvtpeUDSzQ5PqbLL71MRxxxhFavXq3b77xDI0PDOvLII0PK9rzzzgvAjdQvn/fcc4/23XdfHXLIIQEcOvWLzN58880h2kca+frrrw/HGO6+++4iFezTdACClJtdpGOQ55TrTCngmWRvpiggzxnYZ8FeVlY9RSBbX/Z+NvXrdnpD7BwAPp+1Pm9bToGcAi92CkSPgnuxE0CFkpqVVEOtUa3oq2rJ2hHde/73tHHdOh3+2hOkQ1+ixrr1Gn9orZp3PqTx627W2G23q39iRCuXDKg6WVR9YlITA1Wlh++rwdefqMGjj1Q6sFz18baKLVZ6dtK4nYUdrCp+4VA16+CzwMERwIUGgNCqc0LGuIr9NT01/Iw+fd6X9fAza5VWy3paw0rH2lpZXKw3v+oNeu1Rx6rIri9JQ9VKWZNjk2olbZUqZW0YH9UF37tQrzz+WO226x76zBc+o5cdc2RYSEG07eCDDw6Rqeuuu0577713AGpf+9Z52mvn3bTditV65NHHtXj1Sh13/Ilav3adLv7Bj7T9qhXafrs1Wr5oiZJGU8Mbh3TcCceH1O11v7xe265aHcqCXj/96U91zTXX6K1vfWuo55//+Z/13ve+N8w15Pef/OQn4X+2d/mP//gPnXPOOaFN3/3ud3XLLbeElDG08ObR2UUkjgh6wYf/d/RttkUgcw1EKCN7VjbPZlO7YVAzdRxjFgBm08aOIPr8bv73djYscslTwC8cW5C3NKdAToEXFwVe9NvAxFLArcE+qVVXXZNaVkjUf9mNuvtfPq+xkfU64OyTVT54bz15+9164tpbVfz1o9p+pKVtihw71pTShja0+zSxbED9rzxEy99yipKD9tVjKqkxmmhRu6rBdmdfOOakpSEQ2DlGruMkpw4Ofh7LVHYRSDcApA/sA7iQKeCJ1rj62YKlzfzLQji/d7Q+oXKtplRljWlSJUHjkoqJ1M8CGrZnGRkKR7/9/+ydB5Qc5Zm1b8WOk5QDSIgscgYhQGCwwWQwtnFeB3BcB2z41wG8GK8jDjgv63VcB1jbmGDAsCZnTA6SQAIJZc2MZqZ7OlT+z32rv5nSMOrClmWEqO8cqae7q6u+equ66un7JssqwnEdKRpt2jYeefJxTJ0xHV0TevDwo4/IMTjsoMNw1fVXSxbuKaeeinvuuxeLFi3CmWeeidsfuBvrl6+GU63huONeh332OwB33XcP7r/9LkzId+JNbz0LfevWw286yNs5dJY7sMOOc0AwXrFqpWQCEzDp1mX2LkHvqKOOkuxXgh3/Zozf6tWrRfU74ogjpMQLM5EPPPBASRrh64yzXLBggSSb0N58nbGDBEkFX+o0Sp7zKgYwmQmcPI5JcByr3PG5qjOYjBtMgqACwLFJIGpdSbcvXyOQqr7g3d3dL1IYt+KvQja1zAKZBTILbFMW2OYBMC2IvS8KMClnoai7aCxehOZvr0N07R0w7QD6gduhaeRQXdOLaNWg1PPrtMpkDbheFeudCoy9D0H3MQejcMJ81HbYDoOehqAOdOpllHM23HpcpkPTWdolTiJJAqC0HtuKR1LxGZs8wPeY4bolAdD1G8hZNpjI67PlnsfyLra41ZuOA9vOSws3HpNKnX2CQ5SoxrIvr0UwbPULdppS+88LmWsdK7I66/cJAoZoeg40Q4ehW3HMYOTD8T1878f/iY+87/2YYHWiUq+gUOxAza1BdyNMLndIyzn2CCEA0t1Lm0yaPBkrVq8U0Jkze4eRTF5CIJVBPqpOIvxbFYgmTBH+VCkYZXsqflTKlDuWRaZ5LNhPOKmgJV31yVNqbB3A8QBxPCjk+pKFnMdC4FjIS4Jo8rzh3yqWVLmv+UgATPt+bsVfjWxqmQUyC2QWeEVbYJt3AafdYFbVKth18iRgfR/WXvE7VK+6HnMqwzDLIZYH/bD9Egw/RLfVAZs14OouVjk1NGZ2o7zbLMw46Qxoc+egOmsahkIDxlCIScjDMnMIQg8OmEQRQx5dwDEAjrrSVKeNrfksSqpECh6Ua29LA2C9PhxnkOo6CvlSqx9bq75Nqzaj23Alzk+zdehG7F/33CZMS0dloIbunh40naZASKlclq4fw/U6yqUymo3huBafacCRLh+mdPpg3T/G5P3u+j/h2MOPxKwp0+PyPDbbzkVsNIyo4SCwDORtW3oOe44rQEPVrOm7yOULaLY6ghDi6Ppk6RMqgpwLy7hwMEGE7lB+llA3ceJEWVapdkqFU+3g+Lm1a9cKCFO1VO5d+aGRSNhRx2psEkgS9lSM7KZiANU6k4/q8/xMMut3PABV0DrSos80R9r1MQs4rZPJ1vy9yOaWWSCzQGaBV7IFtvk6gGmAxazfSVSUbr8fa/7717AXL8TETg2+WcPA4DqUrSlx1q6eg6db6I80VKdPwoSjD8X2Cw6Gtuc+qDV8NFx28MijaBRghhFcZxiu14ReNKSOnQJA+oFVDOCmFJut6YQaWwdQwYS68W9pFzCzgH0/gNnqvBFJPGBTwEN6/toWGg0HWhQiX2h1TWHcICI0WJg50OOuH3QL1+uw8zn5rGqjZhixOiXKFAs8t1rEEfIIgJ5loitXAss91ht1DDVq6OzpRlG3pD+xuPWjCG7TGdmO67C3sA7DtOA1m6L4cSSVvGRbOAKhUvJUhxCqfPwcz5FkH2DVT1i1mmNyCNeVLLiszh+VsCPn3iYKQSeXTZ53Y+P4aGv+SwLjWAAc77xVy6tsZQIf95GDPZCzGMCt6duezSWzQGaBV5MFXvUKoM9eYX29WP+LPyB/1a3YXndRLw9jg7MOM3N5eFEHmgAqHjBs5WDP2RETFsxD6ahDoO00E0vDCNODIjobBaAB1FnMuRAgZ4XSxbcZOq3zidm/cTu5qJUVLH9HrT61W+lZN1ZRSvYG5s19SyuAYSOU+L18OYem60EzNeiGNOWD4zmo+z66CkUYIesysl8w+/Oa0E0bQ/VhdBc6pTUb3arcl5GuGpoGwmSIQMCGUBL4PvKFgrxOyGSh6CaPUdNF3pBqjYBlSg53dWgDunJF1MMA5WJJ+hRLDUZNA7OW86Uias0GyvnCCKCpdm98JDwpsOPzZA/gZB9hBY4EJ5VAwX1QIMVYQq6HcEkITAKdqr2XdAGP/UGknrfLAlZ9e7nNZFZwMiN87OmbXJ/aVwXt3Fe+lnUB2Uq/9Nm0MgtkFnhVWGBEAUzG9yRjefgLnXFJvEmpmwxvpir4W5V04HN1Y+Ijn6t4n3Y3B76nIGOsq1HdYNRNm+vja9wmbyZ0gUl/1pa7TLmT1I1Qtm9EyLObRoMqSQjf0uHqnlSGy1smisMm/N//CQN/uB72qmUolgGty4QbehLXVaRLMLKx3gPCg/fBtPecjWi3XTBcCdChl9G02adWE1WPgBDq7BziI2KSCHyYPsvAvPIHj00SJJQCyI4Y7FihnvN4qDqAKn6Mx4MlP9gKjX1x2RWDbsx27vmk0qhgYrzlx4LLWEunhQCkHZk0BTmtzFDa+l/u9zfXfi/35/8Z9ksqqdyeymjO1Mt/hvWzbWQWyCywpSywEQAmN6JunKqKP2ErWVJC9SBN/qrnhVFlJfKiqQCt3U1iUzd39bpqecVtE0T5XLmiFIRSSeDySlmIkwQgzx09QD7SUTByMEwbLkL4mgebXSIYw/TYQ3j293+C/fCTmBn4CPxhhGYIyzTRrAyjNuygs9gFTJ2O4glHA286CUOTujFY9TGrawacSr1V1U+XGL+QHSh0KjEBIj2EKWVgXvnj5QbAsSCmzo+t/SacBkhpZ8bmAmza+tPml7b9Lf35tPlv6feVapxUwtW5+EqH/y1tu2z9mQUyC2zdFhAX8NiLvLqo8zEOio9VQJUVqFQ/ghazGvmoFBvV+5PwpUpDKBOkKTjjwSCBksojYY/bSap+SZUx2flAxa3JXPQQgePCMCx4EVBt1JE3NXQaBsKhKqo//y6evflWTB9yMGdCDxpD/fC1CB3lHmCoAVh5+KGJgUIe9jGHoesdZ6C64/boHXZR1ovoEA8u3YOjsX3M8iUIchhsJLwNjJcLAGm65HmzqXN1UyZOA5S0Q7O5ALSlt582/7T30+a3ufu/uZ9Pm/+Wfp8/NpPdVdL2Z0vPJ1t/ZoHMApkF/lEW2AgAky5Y9SuXgMcMxWTgPWEwGbSuAJCTIrARzJRSp9Y59sKZfD3pfk7CIpehAqkuwCoQnctwGwoAk+tOdkLg62y15jOwP8fWYHFiwJSOMrSGiw1PLkTn9y/F+meeRYcPdJTKGBoektZjXcVOmJ6Oqs6SIDpWOC4ae+yEnf/lzZh81Hw0NAvNShP5otEq8Kxi+/gYDyqCbB+3LYyXCwCTKouKJeMjjzP/FQqFtuZNB5y0MjyvdIBPO/9e6fu35b9d6nxLhsZs+a1mW8gskFkgs8CWtYDUAWwXg0fQo4uVAKjaThHEFPSpeDxV2kK5irlOfo4B7e1GslBtcjk1J8Im64VxHlwX56G2oTILVVA9YZUXa/Wcy+u+D9swUSp3otEC086cjaFnluLua2/A9n+4EgW6kJkgYJtAsQDPsGBaeXSWu4DuPHomTIJXKKIysQfF/fZEx647AUYemq/DMeIkD8nslZTQ+F+c6KFBQ5zx+EofLxcAqq4Rm1KP02L00u2+ZQFpc92EW3r/dBY1bzM2f/vpRyDt+rB5a9i8Tyd/FHNNmQK4efbMPp1ZILPA1mOBEQBsd3FjFwIG7RPoVEZgpVIRGGMtLxWDp4Kl+bqCMcJbcmxKkRn7urrQEvaYZEAlcPLkyTIPrlvVUSN4so4aB7sjcC4s38HBMhP1yhDyBDrdQqPVFqxkaFi9+Bk8cPMtmFsfwIxZ2yPIWVLyo3vGTETFPBxNR9fESfALgJkrAvkCUCrAsUz0NxqIPA1dhRIDDePda7l8Ywg0WzDIgn8ZAG5OEogqe8LzQf3ISLYXS4sBTLthh1Hr+G3iO6mBST6bHukK4+b1/Utbf+qlRBvNDB5vWV2L42U3NdIAMG1+afZP+/zmAnSqfVIWUIlt451nY5NDNndb2eczC2QWyCzwz7TARp1A1MVa3WA5kaeeegoPPfSQNKzfd999BbAIf3feeae0ojrhhBOkxAYvkOpizdpkhDLesNnsPhlTmHT3Ji+gm4rtokuZbbmo5u20004CgWypxbhAbpewuWLFCnEH7rDDDlJod2BgQJ4TCKthU7KAbU9D6EewcrYUDMbwMBq9vSiYPlBmdw/Cmg6wrEYYoa4F0Is5REZR6r9xfTbrshkmGo4D3QtQzOWhO5L7G/8bgUCqKjoQGRkARpHA+uZkAfM8pNrLc4rHXrn5ud40QEgFkDSFNmqvkKV9WdMAJ+3zafNP+zxSABBbeP/S5r+59knd/81cgOctrzMMNVDeiqQqmLZ/m7n57OOZBTILZBbYYhYQAEwmX6g4FwWBVN9+/vOfY5999sHhhx8uqhoVtltvvRUEPTa2T8YAEgR5syb8JWP2FPipeBruEf+WLgyJ7gVqT9XyXBehjoPgyde7urrkucr6VYkidBfzQq3iwtiizOm2UQg05B3Ajkxx1dadYeihj45SESgYUidONywU7Rz8mosg8KCV8/BMDX7QEQNH4CFgP1lDQ95k39kIoePBt1ntL4DGpmAJFVCTGysTQ9orMFvsyP6DV/xyuYBVWRnCHs87ng9UgdNi/1767qcdn80DwJc+jy215La+f1vKbvF6ly1bJtcbXvdUFYTxrldbdhbZ2jMLZBbILPCPt8BIIejkDV5d4AiBVNOuvPJKUd6OO+44SfJ49tln8cwzz0hyyCGHHCK9YG+88UZ5TsWPSuHDDz8szwmE7BZBkORyp59++sjrXD/duWx2T6DcbrvtcNpppwlQUvXj53ij5w1/xowZmD17trx+++23yzo4J0IpFUAuw9ceffRRDA4Ogm7rI488EjvsOCfORmYR5jCCqeswWUk4CBF4PoYLsQuM3doM/qOQR3SL4tcicQEyq5dLhYhardxGsny3kSSPtFPrnwmASVUoqbaw5iBDChh2oNQY33flnOSPEd6oVRFlpQ5quo5GM65jyXOACiJv5hv6++V8CVs/QlTtSPWDRCU9sdewamNGAOWPkWQ/X9qNavTYosaq04f6UaLiZ1UtS5WpHgWhgAW3ly/GCS2O9DG2pWWdoYVwHROBvh6a4SKnbwf2j3a81SjmZkDTAlQrFelW09XVI/vPbdi52HWthYEo2iyM7VABNzU0601YRhT3SnYsOIVBVOsWJqGEyOjFcE6HgYkoNgKgoMV1N02DhY1gaZZ8PyqDVXTyx2CzH+XSRAwODKOzqwxd91AdHkK53AUtshCEgfxoUxUBqtUhsb+q7dls+sjn42oCLIpeKhXkeBXyBWm5p4WmKMhcB9V7KZht26jVa/Kd130XMC3UHB8+p1u0Jfq2Wa/Ctgw4zQgdHSXQ1a9R5IcmxzwM9JfUhYTXIJ5vPLfUsRO7cmXZyCyQWSCzwCvYAqkAyJ6jjzzyiNxkCWMHHHCAQBYvhqtXr8ZrX/tauZjzNd6EqdIR8m677TbMmTNHnvO9/fbbT25yvFnyM1wXgZDwx/W85jWvkeV4Izj55JPxox/9CDNnzhSY5Ov8m8B31113CWTSLXPdddeJAnn33XcLHHKZq6++GnvuuafcVFl0+KTTTo1L2ei6AB3/8W/CYOQHqOfsGPZaB1GB3ygEJi/07OPbShpgqReV/PEKPgFe6tS3VgAklHNuCsJ4/nAQDgh7xXIJuqYLMPDY87wh/E2YOFGW47mrCp0TNAhyXIfq0+u6cZs4AoyCFgWEfK7AU62HoEJg4FDAyO8AXyNIcRs89wX42CKObYUdB3YuJ+BDwCl1xIq3oRsYGupDd+ckQKug4Q3CjGaRd6BpFQCdGBjsRU/3RFkv94Pz5PoJglJsO/ChWzYizUDDZXF2A2xMaOghIs+F5tnoM9ahnJuOPMNVrUEMRj7y2iTk3QC+5oMQzT7I7GzCHtcFy0bgEVz5uoPBwQa6u7rlS+T5tD/raOYRhSY83x3pWWxZcSs5Hi/Vvi6fL0qBdsIf3w+jeD9KxZJ8Vo/iH2iGaWLDwAb5AUBAzufyGK4Ng1haKJYRaoZk3w/XmhjY0Icdtp/RUuYtOW6OSzCOWzJqDPWITDSbLsrluB/zpgYBkPDH45cB4Eu9WmTLZRbILPBKsEAqAFJ1ocK26667CgjyAsyLN+PxGBt41FFHYd26dfjrX/8qMVq8eF9wwQX47W9/K9DH53yfsYIrV67EPffcg7e97W1yc+YN8YEHHhCgI8BRJbz22msxb9482Sbjbw4++GBRB3nj4LafeOIJvOUtb5GLOpelAklVkO/xH2MT3/SmN8l2b7jhBhx34glx/UDTghZFAn6iBsZ3fwRUA9sMs6UItj4gsBjH/MXD5U3wVTC2VgAkOCg4U4eBMKhcxOzvq7rFFHJxyzWeD81GQ6Cxq7t75DUFeDw3qfTxPLesuNUawY4AwM/w82MVICrlhEu+RwWay1Nh5OB5PWXKFPlbgZ+aB1VoqXGZywlA8X3Vu7jpNNl5DqaWB/QKvKCK0JkBzYigWQPQwliVYsgCv0uq53B8aodxfGQUwnU9RJoF0zZ5+ov6beoEZ/5tomoMoVnLo8c34BvLEXbMQN0poDOowZb+yux0o0mtS37eqTeQMy1YbIunN7F+3RCmTpkKz6dG2EQYEqSLcBoa8oX4+6FsSjDlj0mCdqyS2nCcuLg7jyX3xc6Z0DUNnu/AMspyrDiUQspdcFwHOTtHPU+OCdssGlYOBkF1uIrOcknA2rILceiGycz8ODSFEJuW/azOJbqACX8ZAL4KLnLZLmYWeJVZIBUAlyxZInB31llnCfBR2TvxxBMFzh588EEBw8WLF+PAAw+UJIyf/OQn8v7ChQul5RehkBf6o48+WuJp7r//frzxjW8cUVn+9Kc/YeLEiQJyVF6o5s2dO1eUQSaeEOruvfdeuaGqBJDXve51ApU//elPceqpp6K/v1/e502bSSsERKqL119/PY4/5SS5MUp3EKp+Qdx7V0Eg3b4K5xgfKOVckoinhS3oi9XDWBmMP8G/fe2VHiP20s74rRUA6QJWLQd5cyf48Xzj8ef513SdkZqUxXxBFOlioQDTil2kw8NxIXOV7aqy3Hk+cX0EFcIL95+KmnIVKxe0KkukrKgKkqvkFP4QUUCmYmVVZqlAWy4vdSq5ja6ebgFA1/fQ1dkF12M8qgOvkYPjr8CUyZ2AOxleFMLDeljGlLjMkhZKPKthaPIZuo8DUdUiUBdrsIeykYOdt9FoerB0DYYWiHvYMHIYjOooWB2wqg3oHf3o9cqwrG50w0NzuC7hD37AHswWcsWCAGCpWMDghg2wC0Ah3w3XCaVHs21HcL0mDMNGFOQwXO+T40M7WpQuocucCb38XloWXexsH6jLsaA72DSBSnUApqXDRKy8yXfW1DAwMITOri75ztOV7PlxTVIqiSNw3XTQbNTQ3dODhjNakL7RqKGjzM8EsiyB3aAa2GbwB7ACQBUDKN/9zAX80i4c2VKZBTILbLUWSAVAKhtU7Y455hhRNqjGnXHGGaKwPf7449hrr70EBFVMIG+SBDeCI1VC3uwIa1Ty6C4joBH2qIjwOdfzm9/8ZqTEB+P2qCoy7pCq4KGHHoqrrrpK3DBcL4GRKiDBkzeRBQsWiKuX2506daoAJt3J3C7dxfMXHNW60cSuIKp+cjPR9TiuKNgY+hQEKij0mCXcihFMxgbKOiK6lTMF8B/dC/hviQGk2qS6NfBcUjUrqUAzcWm3ubujs6MTN918E9atWSvn1h+vugrnnnuugJ/j+iPuXlHfcrmR3tJcr+poQ1hRNS+TaiBfp7LN+FUOnuvMPudn6QLm+0o55HNCiyqWLmATAYMDAwIUBEAOAqAqe1Ms5NEY1mDbG2DawMCaHHomlxGaFWjoRMC41VZ8qu8TvAzcefsdWLlyDc487UyJEcyXyuICrjux2ljK5wQAh4cGcfMNN+L/Hn4E/3b+lzCz0EAjfBZf+e+bcOqpH8IOk2vosTthWjaqDRdBqCNfKopNOoo5DA1uQL5gIGd3wnMjGCbj6+gmNtBourDNDjjeAAqFksTeDVUqAraEQAL6pElT8Je/3Ir+vgEJ+6BdInjQjRCNZgX5vIU//u4v+N3vfic//vY7YH/xHuQLhPKaxPZV6hUU7Bw8x5X4Qc91EUWa7KfYMgjl+HPQ5UvAvO5P10mMMH8oUhVuN1544QUBQJUEwmWz8i9b7f0sm1hmgcwCf4MFUgGQF3tVjFnFOan4KAIhFTqCHEvCEKoIYVT0eLPjcuoz/LVNlUMFg/NGS9cNb6rqxsnld9xxR1ECqRLw5slH3qh5c6DCx/VKdq/jCOBRTeTFnsoMXW68sPMGzPd5QzcsUz6rIEEF3ytFywqY2BFbjEqH6guhXnPMUJQ/NUaBLwY/O77PbPNja1UAlQuYB0B1jeH5+pWvfEXOh/M+9UmJpTv/gvNF7fnoRz+KK377W1G0mVik6XRBOigWYxBgggXP0+7uTvlb0+IEBg7VDpHnpUoyUe5nPo/XUxR4E0Wvq0uSqHjeqkx1lSwy4oquN+Q7UiyVJKZNlMZSSWIWY+h0AL+IQqmGpx+7H3ffugxnvul0TJzBJJGyAGCj6aCY16FTrdaAW/5yK1avWIWzzjob+RKB1kcQGfDCCMWCDbfpQoePnGngG5d+FV/6wc9wyYXfwIfe+1osX3YrLvjqtXjPuf+Ow/fPo0MrIfJCeKEJK29h+Yq1uPHPN+C1xy7ArFkzZJuBb4rq2HRcAUBCa73BOXUCWgN9/RvkmsBYP8dxUciXpDUjv+c//vFP8MjDj+E73/kuOjryqNUb8IM6Ojtj9+5b3vwhiQOmB+HGP/9Zfjh+8MPnwnXDOKFnQhkm7T20Ad1dXXDqddm/jp6JGOjbgM7JE+BQAdUY6qHDtnR8+StfxapVK/C9734HhqSMbHooAFSJRxkAbvOXumwHMwu8aiyQCoB0mfHizV+9vMHxZkb3C9UL/k3FgzdSDgWEBC8CoYqB4nJ8jfBGtxxvflyWbmQVq8T1Ed6UOsL1cbsqfoo3C5VJTADkHAiVVAC5HbqaVZwgt0UopWrQqNVHbuBKFWBGowBfGMKIYoUvzvIdHQoAR16JVK/fuO+vGttKq7e0M35rBUA68FVLwCQEXnHFFRK68OWvfkUg7Mtf/jLe8uazRUW++667JKOdyQ133HGPnJc8h3feeWcJc6BazfORoQfz588T0Hj66aclzEGBHDPRCXj8TlAdpxrOf3vvvbeEMPAHx3PPPSeKN895/tDhe+rHEtfF8/35pc9h/hFHSCmiW2+/Tda3+x5zMX36dAHOW2+/HRM7ZmK/fabgyt/8DFdd+QAuvezr2G6XbixfUcXixcux+25zcNBBe2N4uF/cuht6N2C42sAuO+2Opxc9gUqtjhAm/Ag45pijUB2soJAzkM/n8L3Lvo5fX3cPDt3/GFxw7gIMh8/i0p89htPO/BiOPNhE36KVWL5iDTSzhF3n7om7770fX/ryJTj77LPw+uOPRb1WwaGHHIVnnzZgfr4AACAASURBVHke69avwRFHHIynnn4C3d0TMXXybNx175/Q3z+AqVOmY968+QKA99x9HyZOnCx2X7VqDR595HFcfPEXxYswaVIPuifkYeeYuFHFRz94iVQGOOmkE/GDH/5IKgN85WtflWPD44q8hoP33w+Te7rw0P33SXmmtev7MHf3vbDL3D0Q6BHuufc+rF69HpMnTcGCBYfjF7/8NRYtehIXXvRZlHOltqc+Y5GVCzir/5d2lcjezyyQWeCVZIFUAORFj/BF5UICqFtKHCGMYKdcvLzJqcxg3ryo2vFmx5gswh4vomMzLgmVXI43Qt6AecPjMlxWLu6IszZ5A+Z6eBNlli9vroTDI444Qj7P+XGenBs/r2rFCWSKyymGPSknIf2B45giAcJN1OmLkz0AJoHErd3ix1CL/+Yjx7ZS5y/tpN1aAZAKHYFfqbwqJo8hCN/5znfwvnPPkXPpmmuuwde/+jUpT3TJF74gYQd//vOf8eySZXJe8Zw46aST8M1vfhNf/OIXJYb1kksuwU9/+t8Cdvfdd5+oirTD0qVLcf7558s5eOaZZ0r2OcMk+KOE26frmefmD3/4Q/lhsssuuwiMvv/975cSSlz2Bz/4gazzG1+/VOpsMqzh3vvvi78r3V3yw+bmm29G03UxfeIcnHnKIfjVL3+Mn//4FnzzO19DXe/DLbc9io6OyTh8/kE4/nULYMBH4Dv44fd+iJUvrMZFF34B7//g+zBrzo6wciUseX4ZPvzhD2PvPeaio5xHZUM/fnT5ZfDzszC5axaGXrgBH/jE6fjEf9yEM95wHnad04u7fncTVqxZDzcwsefeB2Bt73r8z69+jlNPOwHbz5yGu+64HZf/58/x+YsuQW/fOnz3e5fiC1/4dxx44CHYac6e+PFPvyHVAB555DGcdOIpmDfvCHzyvPPR0dGFc845B/fccx+qlRpOPPEUsdc557wHh88/AE13EGHUxP8771IJ9/jABz6Ab37rWwLd3KdrrrlegHHRssU4dsFR2G+vPfCZT30KrzvuOAwOVNA/UMFF/34xHn/2aTxw/18xPNxEzs7j2GOPxV1334labQifOO9jKNnts4DHA8C070r2fmaBzAKZBV4JFtAqwwMJB+eLp6yBPk5CDx0yrEfGvxlaHve8zQW+ZNeakQsjYkFk5l1SLYuzBmtMY0yMjZU1Zim2b5UWkdE0TVQ8gp9yxykgaXoudLrpEMGM8xShhR40JmpIVmFOgvwi+sYSwdt0CXHH63b7Vl85PyXGT5WF2dTRDtv7iIPW6jkfqb8YaKK2MmFZAHSkQM34G+DxkcROVrZpqZiEV9Yz5CPTBdqOlCzofH4IXr0AI+yWsh9eMIRId6CbBURBHs8tW4j1vf2IDFs6qUjHFMtE6DakaDbr0/2tnUCSMYDJeCsG5BMA+E+5XhnYzx8JyqWqyp8Q7i+88ELst+8hcL061veuwAc/dI4kGd1w/c046cQz8av/uUISKAh/LHd00UWfk8ShBQuOlNAD/tg46w1vkzJFnZ0dOOPMUyQ9aMnSZ/CFL1ws8WhHzD9GYlS//e1v4/jjjxcFkQBItY/g+KUvfx5hYOGC8z+Lt771LVj2wiLcd9+9uPTrl+H/br4TP/35Zbjwws/j29/6PipDdfT0TMDatetxyimn4O6778SCow+X+DjGoHHuv/71rwVS+TdLJf3bBV/EYfP2imvnFXS4fhP/dfl/Y/36fnz4Qx/DV778H9hp553x7nPOxcWXfFFg7Jz3/IuUT/G8Jr72te+hXCzhA+e+B//2uU9ir313x+refhx2xElYvbaG73z5Ezjl5NPx6COLsM/e+0uJpx/+6Hu4+AufRld3GZ/5f/+Ot7/97QKzBLK3vvWtEhPMrP8vfelLOOfcd2P33XcXyPv85y/GO9/5TvzqV7/Bp//ts/Kj7/e/uwa//OUvMXuHmTj3/e/FIYccgCCkK9mV8+Y/Lvmm1P3kOgjSLPvE2GCGf9C+t9xxO3bdaRbe9/az8ekLPoXLLrsM07abhXed+yF84MMfx9X/exUefuxx7LTbXDz65EK85a1vReeEMh59+EFccsnF6BDX8MYjef4xvlPFAKprTxYD+Eq4tWVzzCyQWSDNApo/sLotAEp3i1aiQ8jMN3GFEtxicqmZRRgs9Br5okCoQSQjjlnBxoCljUTZxUsqbW1TExWRrtUpZLy2X74sEJd2MbRIwE+LWLaDMMq6Zi0AIlS1AEku8K0nXhTXXNvUSE/yGC0JM946aIV2I9QZHh8PcTExsSTRGSVENWV+gYCf+keLjtQy5JFVUuYm1mKkJLEM5irQgiLyeicsK0LgDiAMalJI1/csrFxXfVkBkEkgBDiqwFSJVZFm/lj44x//iHvveUhq5b3t7W/CgqPm47EnHsPXv/YtfOyjn8KFn7sY733fuyW+LI5z7RSw+POfb5BEJa5v6ZKVqNWG8Z73vAczt5sOx6mhf0MvPvvZz+KDH/wA5h12FD7+8Y8LgLL8EQfjAKkwUs373IXno14L8PGPnY9//deP4PnlC/H73/8O//PLK3DzTXfgj9f+DO98x7/gsm//AKeccgaOf93rMTAwiOnTpwkA/eGqKwX2PvKRj4iSSZWM7m26iplV/7OfXAE7p+Mzn/0k7JyGIPTw/e/9EJVKDR/58Efx+YsuwmHz5uHNb307Lvj0p2Vun/n0+fJjKQw8fOmL30DgBvjif3wBd99zC779g0sR6RYuvPjbuOOuR/DQXVfhogsvQVfnRKxZ3Sdz+vkvfox/effbsedeu+KXP7tC4I/2oQJKVZWKHUsxXXTRRTj9jJNln5588klcfvmP8a53vQuXXfZdScI5Yv7h+PnPf4srr/ytlGr51Pnn4eijj5BsYkbjum4TnzzvMwKKjPWlZ4HzJ2Tz2L7vfe/Dr674Lbo7CjjlhOPwqY9/FJdffjl6Jk/FO9/7frz33A/hd7++EjO2n4UP/uvHsKa3T4pJX3vt1Xjs8Ydx+fe/Dxa5GTsyAEy7bWTvZxbILLAtWEAbGHbbA2ACEJJhcoorQqpBRL3I3wjmWJeLMOIkfmCLOkdYbEERNcIUgQyRMQqQSTek3CIiBulvrOypgzLS3g5dI72Ix77H5zlrqO1xDFLKRCRrAv49AOgxil/2ZVQp5CZVwdzAizMYNzW497FLWhtxS8d9iFsu6rB9KzALaWVsCLBGq4i2Cw115LQAFvLQojyeeuEpAUBNZ004QxRAJhf4nvNPUQCZn0EooeKnwhRoO0IhY0bPOOONmDx5In7y0//EhJ5u3Hb7bfjKV76Gy//zp7jxxptGysUwXvTQQw+W+Z9wwutEqaLL9l3vPEdcvJd95xstZTbC2rWrcckXL5Y4whOOP1niA1mSiAXIGQrBGEDO59JLL8UBB+6JjvIk3HfvQ/jwhz+EhkOI+wEOO/RIPLd0Fe6+7xrccsut+PWvfofHHn0S++xzgMQicl8GBzeIu5nuaAIT9+mTn/wk3v3ud8s2CEJPP/UsSuU8Pv/5T6PeHEIxn8ePLr8cy55fiQ996MP40he/jHmHH46zzn4Lvvr1S0VF/38XnCdqPbN1P3fB56XW4ecvuRiN4T587gufwfU33ozfXPknuG4e1/7xv2AaOczdfW8ccsg8OQ0vvvgiTJ8xGZ/93L/hLzfdLuBLZZLrpov5gx/8oGTYMhP74Yf/KoporKR1izJI+xPojj/+tfje934gyiFteeON1+Md73gHDj74wJFwkk9/+rPituXrKhyFtUO/9a1viR3uue9elIo2Xnf0Anz3sm/gG9/4BorlbnzqM5/Fm978NlQHqrjj7nuw3azZmDFrNg48+CDcdPONWPLMM3IMO6240HRyZAC4Ldzasn3ILJBZIM0CWm9TORvHX1TRoSqBQvDTCV6yOJ3Cw+JGUwWSFZAIhLAArN4zkjLB9xQAios2rsXcdiQBMAaleHF1kWbBWDX41tg6fsnkjrgVFJW2xCbdtAmktHxKpgiPsycpAhw0U7mY6fbduNMIV8cbdbvBpl0KAGPXu2D2yEdCqfy76ZFWz6zsxjZtBgGCyJPCxJZmwoxM5EzgyaVPSgynqLNaDIBswcW4OlU3bUu6gLlZ5f5Ve5ks3HzddTdiwoRuHDbvIPi+J6rc/fc/gFNOPgOVyrCUG+IgAB599FGiMP3P//xC2gjOmjUL/3vl1ZgxcxqOOWbBSCwswYxF0adNn4K99txP9vWWW24RWKN7mooioZElk55ftgiTJ22HW2+5C2ed9QYcctg+uOmmP+O5pSswY/ocBNpanH7amajVmrju2j9jzep12HPPvXHggfuLykV3MOMBmQnLZCe+xjhDlp1hSaQjj1yAXXaZg+22nw7DjItVEwyrlQYOPvgQPHj/XzFx0iRJiHjkscflHJt3yEFo1ivI5yw8cs8j8LwA+x14AEwrwmC1F7+/+hq84cx3odwxAY89djcefPAhlEvdsq0ddpiN2267FcuWL8Fpp50CQ7MlVlGSajRNXOjz588XVzOTPFiYnZ18WCP07LPPFlc7XbqqlNTixQtlf+bPP1Jc7rTbYYcdhqGhqrjd//Sn62S/+RrnLueXbUtpGKqgjJfs6ihjj912xt133oHXv/718h343z9cjXnzj8COs3bEH/54FZYufwFTZkwXAGVcX++6NTj2mNcgN04h9wwA024b2fuZBTILbAsW0BxvsC0hMLKOg8DGjFeJ7BMXagxODTN2ocYxgS8eudAZKascRwUqgmuBV9S+E0egxTFxyZGElrixFVE07lIgyRkS3xfPh/1TlUrH7ce8NprHG4QvVgCS27JUkN6mjnZaDGDaWUICJpG22m3FLvdIIimlkG8wnLKGlv2UW17Yj6TbOh7t+Q9OKzZyUxshvGl5A03NR6iZMFBA3W0gaDgo5wtY/ezyEQBkLCZv0KYV19L7ZwAgk0CSWcCqzI/qwRuX/wD6+tZLhwnWhGOdOLZ4I/hIj2D2im6ZK24DF5eEidu8xd08+Dq3w2QT/uM5FO8fixMPSEY7t8nXCChq+9XhPlhmWVzOp556Ck46+VhpcVYbdlvt0wZBFdgy8vCkV29c+ohxjXTn6lrcdUS1TlPJLqodXVz82MZQZQBdnZ3YMNCHXK6AUrEc99LlD4QoQtPn3G359kWRB79Zl9jZPGzALMB3PZgFHZ7PTh38ThTQqAF20YfeKnZOm9l2fD1gyRrW3WvW49I3hDEBss5OeVRFuaXWphWXfFHlo9S+xKWg4tqNtFexGF9LCH8EWX6O21Pt9uSbqzqctFrtsTuIKedwiND14h8ipiF1AssdXQjZss6KrzoDwxV0lpmMFsjrBODxRgaAaRet7P3MApkFtgULaLeuarZFBPIPoSnulxungqjSJ+yIESDXyozVEbtLYw0wXj6EZvaO2qklz22UCBK1d0GyfdPYwsDJGDnOjbBEl2eMp4SpVqyirkH3eTNvKZStxBDp5EHQYmeQfHuFzwxSFMAxMY1jTwrOqN0IXUfmy5usJKpQRZN6hDH45sLxopRG1xhE2kgvY6WqJjuaKFfwpubgjiiQ4y9hBhEiC6hHPkIjB9Mw4VUb6LRd7Da9C2uefBp9vetAIYXYLaV17BxcL5DOEUzE2dIKoIIhaQlGF3Qubt/G84SQx+fxORNDtYJCtccsEExwYM09ggeXI7AQwuge5XpVZrnqNqFamjFDndnAKvuc62HmL1+TotCTO1CtuLjqD9fhoIMOxHazJmHihAlwnBCGnpOWbuvX9WHChMnIWWX09W0QgCsU4u2WSh2ipHH+3AaTQTiYmELoJMAyfEB1MkmqnwKspiln6IYKaxvGMFurV9BJyI0C1AaHUe5ir2ENtXoVjl+VbXpNDeViWWCfIMZjyK4lbMHGfZSexvkC6i1YVZCchLy4TV1sc3b6UOWfpkyZJPZU/+LeyG7cmq+ra6Sc1OAgeyiXZXtcljbnsrQv6wHSJnyN0N3f3yvq4dDgICzbFpgkVHqOj2K5AD/00PTibHEWcM/bTGKKu4uMHRkAbgu3tmwfMgtkFkizgLbXt59trwBqyqEYQ5S6XKp2aOWgIgAYSFi5ESeIjEBihIY2cSTVIy6hQoyMM4glcSForwBaWuwCHQ8C5Y2WAhdnzlLz2DiJAkFffPOPZydsSOWDfxMea277MhDKlb1pQ6YkgaS4iAvspxpqUqSXGbMhQVbTJaaOOqVm1doeQ58JMBHL1RDMY0AXJJbEEMZYpmQxS3uuTY+uwERohKj7NRiBiXyoIV9fjwN2LuC01+6Lzr5nBQCtVhHiWAG00fSjfyoAKuWPkEHwoApHFZIAJwXBDUNu/oQCnicECcIbFTxCCyFjeLgiAEL7EUbi0kK5kaLmXIdS51S5GS6jOojIvrdKDI1a1EG9ThArSoszxumVikXU6x6KhSKgDQugVasOAh/o6eH3Je4fTDVNgShfU11KuE21Hdbp5OssrMy/6e5mweUYkibF5wIVZc2AQzWXIBk4yBn80cEi1waGq00USl1Y178GU1g42WsCvoVSvijnETOMaUsCsbSxMy2wTzEHO2nQJqq3L+fA8lAqc18Du3vE/Zotq6VGNusCkOzkwTx3rpvrYFF51g9lsgdV1Z6erlb3DraLG23fJhDbaifH7XLdqkYp973ZrI/UKJ06eSoajToCzRO78woxXB1G3mLf5ty4X+8MANNuG9n7mQUyC2wLFtB2/F4tJUhs4zIiMc+MQgXr4EmRWd1AQFcRkw8kJjDW4wohi9AyjowKHdUtE3Trxk5Y5u6mlGHR2NopVpbGVQL1+HX1T5iwlUUrj44PilzsAmGZGiyD/3R5jVv3N1EHUB1cb5wYoY0P/OYBYE6L4AUhGk4Axw2k5yrtJEkVmoFhFNqeZ6ERK7R2q6i1LjGDscIZCby3VxA1v/38K7qJXF5DFDrIhRpK9RqKQ0vwmr1tnHPmIai3FMCtAQAJeIQJ/iPUxWpdDPiNhiOqmoo5VS5fRhfQA09woErFz7K0DOFLgR3hQ9fNESVKdfxQ6xzropR6k1RvcznU6htQKk5AvcZag4YUOF6zdjWmTtkOnssfTusl/m/yxOmo1djOrDQyV9XuTHU4IdQScHleq166av5sxWZZhNFAtkPYrNUc5PMaBoeG0D1xMqqtTGlTi+A2h1HI09XLOp8eNCsvXXN6B1ZiUs8E2ChiaKCBcidd046ofwQ5cdUWyvBZ/ol9NJh531LiCH4xdMeKoWp1R/uzSwhd1Ryx29eC53sIPAv5ggnHofsW8hkCHAF99ZqVmDF9lkCwgnrlWo8BsUfAT5J+LALwsCiGtFtnRylWe0MNfsiyRbECXG/WUcyXoUUGgmYAo/Di608GgNvCrS3bh8wCmQXSLKDt9INqWwB0JYkghG1asYtV19H0NNT9AB2dBXQ5NRRzBqq1CipugNIklsrw4FUH0Z3T4Q27sEpToZdrqNb60KnNge/XkO+som/Ix8TcZHiegVpQgWZGsPUi9KgJM6xD8w1EhTBulVUsY2gwLolSLJZQqQyJO8wPbQz5/Sh151EZqGNmx44wfA2VRg2+2YCh21KXrpi3UcqbyJsQ+NMEKEPkg3pbG/Em136BjQFqbFKFRlmnzdCpQkQ63CgUtymL6RAAG64nN75GSyGNKx1SHWSpD0jcmECAHm9fdFLGu1E51FjlLXYZ2iOhgHGWsCrPEyu1bGVXGck4ToK2qrkYhSE0LwfLiOAYDVEo7UoNR82u4xNvmo3wiYWo9K5Cji3BqDJpNnQzL1nAthZIxrfrh+iaOBm77r6X9G2ePm0KSnRBSjZOe4VSZXrzkaoPYYuPKhYsLYkl9QugJ5JsRjLeE3NK/kAY7/2UDSjQjBdT6x1dvx+oMj/M3I7blSWzuNkXt91QmfHJZZIAMzZ+9kXr4v7JfrV+vMl5xG368pj2A21j+7fmmoiLTcvy1xJZUqPftVb87jjxv2nHc+z7qfufskICOf/xWqNc0SrkgN+RbGQWyCyQWeCVaoFUAOSOOV4A07bi4ruBAy3w4DaGJJDc1iwUC5bE/9W1HJxcN9woD0YedRYMdASDaHg5RPl+OM0hGPXtJPYo3+HCD7rRqFfR2dEFL2xA030pOSHuN0SwrRI0PRC3EON7WEyWIEglhO47KizMMfF5E2dTXt9FUK3AYkigCeQ6SujunCDztqm+6BEMyQRmMkjr3ziFYJMHU7WP+1sOcPKmaMZotsmRswlteqxy0t+mm9AMDa5PpcSRFBwyON9nKy+BPz+UJvecW3W4JrAnbnUmv0ihbh2MDZR4OItAqbqWtAp6t0CQkzIjJunEqmlSRVVlaKTXrkcXYwRvDACe96bZ8J9YjKHeVWJbKsAubGhWDqHbhK0RZ4PNAkCxgTmqvlFhihW5uDB4GgCmve8HsStTxhYAwI3LBL0YACPwB0grdla2vzEA8vx/KWM80FHw3PbzGhV+Bp3yTFPzo4LMHy5MSmkfIkGlb3S8GAAZ3tBuUFUcOUYJ+6cdt5diEzm/X+SS3/iTaYCoEozi2NCNvyPJWOSXOp9sucwCmQUyC2wtFtDmpCiAOeioMrtOMgNDGM4AdpuWw57T8jCdflh6AYapIbSKWOdYeHBZFeubFgy7AxbVwvoLCF0DM2YFmDq5gL6VRTRrHjy/joI5Aw1tAMOVdWAsXFe5hN6BPkRGGROmzMHqvn6UGCfVAhTX8cQFxnpiBABmC04vllBzNZg5HaY2jA6zH7vvPBXPPrcKmjUBHaUOiZFjXq2QVKsWoboxe2b7XqAjdWU2ccTCZP2+jerLxB9QsZKbOuB5PY7LUvvIGw07brB8C2GnQ4/didRi6K2N4S9OrCAMVrwYHkUhZAXdlrKnFEC6ljmoHCr1T1hHZQkzEablYk+6zmUZ+hf18QHw6Fl1fPzNs+E++SwGe1eL0kgFsAm6Em3pBJLTPFiRt1kASBsoGE1CgfpbJT9syr5pIKG1YkxbR6u1mqQqmQScFwNc+hc5qeCNp3aqEAulhqbEbKZv8G9cgj8AVPJWAgDj4kKIovZZ8uMqgBslPqXtz3j2SfvMS9/FNMBLe5/nXvL7kSxGn8xIfukzypbMLJBZILPA1mGBVAC0ggD1UINR7ETk1aEPPY+T952I9x47DVMtoJMlJfwQgZ3D0/3Af133GBb2m/DzU+D5GtyyDcsNsd/eJvbfp4wnH3GwbOlK9K5fAzvYDjN3LWPuLhPw2L2PgF3LDpq/N55ZPoSHFg6iNLmERjOSbMelS5dIOygGt1cqVRTyHSgUSvCra9FwiujpnoJGZQnmHzoJRxw2CX+46lkEmI6cFQPOCNC06gCqG5djpABgqosyUcB5HABMyyE2W91T2L2EMYlkOEmjYbBa6KMAL1YeoMMPI7ButGRPSsKIhihXbgFgvCWBQFENYwWwbyhOIlHuX+VGlmXB1mFxPJkCKaWq8bkkVlghdJfZv6MKYG6ohqPpAn7zbDSeXIIBJoEYEXzNFgVQtyxETh15zYMdOZsFgOprkowBVUqlqgnX7quUdoNny7GRsQUUQMadjY7xFMBYgW0dvDEAGidUtRtJINmUCth+BY2W8qkgMD4zVBypjvbfj40B/MUKYFoWetIFrJZNZmmnAX7aZXS87kHJz6T/QIi/HyqrPC4B1DpciUL0afPI3s8skFkgs8DWZgFthx+2jwE0XQeekYdZKkEPHETrn8JZ+5Rx3km7YpK/LhacfA+wu7GsZuMLv7gVTw52IOzcAfXAgp8rIezrxwG71HD8a3fCs4s34PkXlsMb3gCvvxOHzN8DBx3UjXtuehw2SnjNSTvhjgf6cP2tz6N7OxvPrxnG9tvPxNDQACrVQcnuY1uqes2F5/kodxOOJqBa8aG5a3HWqQdACx3cedcymLld4fqDAlCGHmcHj94Q4puVzx62bQbLWLQfozfoF8X/cbspnUSMXF6qYYurVVrZsa6Nz/RoATudQVR67Ook9PmM/WvBm2R0tsrFjNyUWi5tBUwDtVhhUhnYCgTj27wOxzdGbnB8TaltCgCDXCQAKICnNxCFcQwgAfC8N89GvQWABEdPs+BKDKANzWX6ivMPBUA1P5l7S7VMi8NKA8DY1anu6OMofGnvp5wd4wPg6IcCqZOZdD/LXo4s8FIBZux+jv3Rs8lp6psHgBuv9++PAZTvzjgu4NTjt5lX1DQApALNoa4dY4E77fObOb3s45kFMgtkFthiFkgFwJwewQ0tOJGGnNZAufoc3rBXCR8/YVd01Z5Hw+hBEHgwyxPwXN3E5356GxY1JsPr3gWNyEZXLoS2rhf7z+jD28/eE4MVYM369Zg+sYg1Tw9ht11nontSBc88XsWUiTMxbTbw6NO9qFQnIz9pENN36sbS5yLMmqXht1fchjk77IR99tkeq1dFaNQdoDuPhgP83//diX122x4nLNgBN1z3GOr+JLjaVHhBXEPNMDSYLK8iZQpbrmAqamneppReuaoMzXjwJzeOlCQSI8/M1Ng9rbMeIAvaEqgZv6QDzTCOf4vj+uJ4zCBstcAzTInHVIpYcg4K4AZrzkhR7PEg0Nfi7at/Cq7UOgNbGwFAxgDSYFQAj5ldx8fPpgL4LDb0rpd4PFfPEfnimD2ngoLmbLYLWM1HfQPG2nnjGLQXf0/SbtCMbRwd47l4t7QLOKUTTYoCPd6VIQlNafsPtHcBB0H7Op0bA7jalzS3d4K5EzGyY5NA/hFXvbQY3pcCmHGh8I0vFOpz6fb9R+xFto7MApkFMgv84y2gzf5RigKo8Z5voVqroWx4mOCuxul7lPCJE3ZFBwbho5vIIokGywPgwp/cjYXNKagVZ6NOd6A2iEm1APtOWI83nDwXK9e6WLzkCRx92B5Yev9KwLZx0PwO/OHKp2AaM3HcaTNw911Po9LXjTPfsSPuuXsNbrjxWrzvnHdhwgR2DAD6eodwx+33eWZjjQAAIABJREFUob9vEAe/9s3YUBvAE0/dh5OOW4DJpSKuueY+FCbugAYKUrw4VgCZ5xjGtc+kRA1LRATwxlzYx5rYMFIKVXNd47l+W6+ltWKzGT/Zqk0iN1NCaRDHvfF5MzBiANR0yfylIuEHobh6+T7r2LYDwA2VWlsADA1r5PPK1aXUQ+6XZ5sCgEzyGAuAnzh7NppPLEZ/33qp+ZcEQNMZFAVwc5NAkscjmaSiXk9TyNK+MhHjDtTYAi7g8ZNAEu0LpaPOpiFwbNHqNOAb2ZVW2EMqoKQkgeha+zJCGwPU35cEIj+UxiiAI6+lHcCU91P3/+9cvzoXN/f8+zs3n30ss0BmgcwCm22BVABkTS42V3c8F2XdQbn6PI6ZbeETp+yL6VYdOrMHIwe+ZmJF3cDnf3ETnnEmY4M9Aw2UsKFYw1yjB/t29uOYQ2bj4SdX4dGnHsKH330q1j1Rxfp6HQccVcZvfvU0coWdceIbe/CXGx7FhtV5vOO9u+GeW5di4aInsd/+e2KvvXbBjTfejIMPOhzlUgmPP/Yc/vKAg0Dvxa57dOO1R++DhQ/3Y+mSfmhd3bAmdmKgao9k/ZpaAEtjczsWhJZqe2iMA29Jq6bdgEO6a1tjPBWQNRLbjQ4zzmrlUK3T6Hbmuph53XSD+PVWDCBLqkg8UgsA3XC0VV6y64hAZeCjUm+vAEYJwKWaprJsOR8psGvFAMgYwIDuwoQCSAB0nlg4CoBaDk0tD9vURQEsornZZWBUgWdl27HKS5qCk/b+Rlm2WwAA01zA4wLUyAnFtoDtz580wEnbf3GBy34bceifkBf/C+Jakiw02WaMmwSSbI+YoqCnxki2b1WeegFM2/80+0mYhYqHFU/CqD247rTPp04wWyCzQGaBzAIvkwW0HX9UaRvkJkkKATvu2ggbQyjrAzh87xKOOnASpvV4mD0Uoeqb8DpKeGoNcOWtz2JxrwO7ZyaciDFhAYLVT+LYPSfgrBPm4uZbH8Oadevw5je8DsufeRrDw8BBB+6Bv9xyJwKjiNefdCAeva8XvUsW4Z2nH4l7nnoCi5aswutOOQFPLOrDUwsXo1npw0mvnY8dtp+Exxfx3uWjETQwZUYHrrjqYWjFHmiFAgI9QE6fisDQ4OuAxzg6KQCtx57bEChHiTIg4xyE0YLBGyeTjIJI+0LW+kZZpi/eQI75ybzh8kYjiaC8Gefi2D4NcDlPvsTkD8Yskuskqzkuo8fVyyMryIx6t0duWn3VRJKDUloS0+g0bVRrw9I1xLQ1eOzzy5Zouiag3A+gp9iE4VfQcCzY+ekIhnux34xhnHf2PLgLn0Fl/SrkxaAGXOShtXrLWjphuzbSCm633feQOoBTp81AsViI5/0ynfjZZjMLZBbILJBZILPAq9kC2vY/qrcvBK3XYRgl6EYegQMEjRAdlocOq47IG8JMvQ81N0RYmoya1oEVgw5cvYxCZxeaPjDR8lHyNmDniTr23W0SFi9dj4HBfhy631wMrFuOZatfwAnHH4ne3iE89Njj2HGXXTGlu4hgYCX23WESguIErFgzCE8v45a7H8Ruc+dih+0mImzWsOr5xZgyYzacwMfEqSz9MoS773sW228/VzJm83lL6gVKFq0eA5UmcYB0AMejkChEu6kTYWxAfdIVmaYQGimt4DpycRIKuyoH7LXAcMCQCR8GpHWxdDCIMYnuZJ+N7KnuRXQLU6QhAcZJInFSCLumxFnEnOeG2miW6XhqxbCXg8leuWzp5QWoOR4sOx93khgOkfN1lAxHjrXjW8jneqDX1uGw7YZx/tt2QuXpRVIHsEAAjAw4BECriMB1YOkMDHAyAHw1X2Gyfc8skFkgs0Bmga3SAtpu31/fFgC90IFmluAjj4g1/ywgdAG2AtXDED2dA2g4PhzKV2YeppWPs0DDSGr2+RtWYEpHHpof92P1mczgOuiyAdMfhqNNh9eoo7uTjec9cHslu44dp4R444n74p4Hm7j5zvsQ2t3w9AIKpTKcWhWaX0cxB7hWDXo+j5332hdPLV6Fwf4A07omoGB46CoyC3dYYv7YxsyKYkhiizRPiwQKfS8lCD/Ra5hH8EUlZVJa2RkpCmDezMe1Cc1W6Q3W8yNLke7igjBy4hDtCHXiLg7iDGEBOlEIuU+GdGkJWDAaBEgWlo4w3BhVOMcDwLxfx2DdQ5DvhFaehOGwgKrL41RgGWD4kY6C4QjIRRGzgW0Yg0swb1o/PvXmA9FYsgSV3pUCgMQ9D3nAKiB0HbDlGF2MdC2zlE+mAG6V14BsUpkFMgtkFsgs8Cq0gDb7B+1dwBPDDnjwMdioINAiFDqLkqTgs0hxRwfq9SrsPPuFEk4i2LaJMPAQui7ytgWHpVo6u1B3Qmyouih1skA04FeH0JUz4BZMrF+1Dj0d3bDzbPE2gLw1hH13KuLYw2bg1nsdPLV4GaJSD/R8N+qOC4vZskEDbKKBgW6EOQOOASmQnLc1WE4/5u09GXNns5N9DZYfoegBBUIVCyzDh8vOFcwMzpXbHnbWKUvGEb0opiglRivNBew0A6n9Z+rMAo49wJrqCEFgDeM6gOxcIuViAl+KWstzGYwPJASyF7Mubm5GJXIpxg2yj2tyjIXAqXoFVTdCXe/ABr+IRavreH59Ey46YeU70MuetnBh6D7CiKEAHnLVJTiGCuAbDsGGp5ZgaD0VQDqo9VYruGLcCo66Jo91BoCvwktLtsuZBTILZBbILLA1W0Cb+t32WcDFsAjdCFB3h6AZLop5A/VaFX7DQVdnJwYHKujq6YEbaHCCCFbOjhMJPC9W9bwG6pGNwOpC1QFsy0KBrc5qVZTyeQz6AyhZBUSODitXgseYdLqWu30UtUGsGiiga9J0rBmsoVppoDhpMgo5oH/VWnSVC9ghMOEbAdYNb0C5u0uURrvZiz22t3Dw3jNw6A5d0FygIABIuIoLX7hsvZoDmo32hydZxm+8gPK0IPAUDzDMVgght6Pi4cSb2uI7vRXbJ/FyJL0ohM76gKwZSJdvEKuaBK2gFUdIAAxaK5P1jgm0Sz6vVGqYNLOEug785a8e/u/ehVhfswB7Alhlh3UKDc+FEfloUn2MmuiK1uO4HS386+kHo/H8QlR7VyCv0YHNWt5UAPMjLmBCaQaAW/MlIJtbZoHMApkFMgu8Gi2gfeC3i9q6gKu6A1PKjeiwdRM2laY63ZBAOVeCH/bCKpRQdwI0Aw1WoQzX96SPLXv1dmr96CdklSZDM0wEDfYPJuWZoD851HPIwcDA+gFMmDQT+XwR69auQ6fhwAqGEJWnwo2oLJnId3RL3Nvg+vXQPRdztp+BtUP3wkeIQlc3oJUw1FvFdhMmom/lYuyx0zQctfss+E0Xhh/C0ixxl7phhNDUBW70IFnn7cWnQDLLd7wTJA0A00IMG1KFpJWR0nI3x/XQ4p6wvu+OtkJruYO5TmZPxi7gVgcQumBboEcbjRxUtc4xFKjm7VQrsMsdaMDG0lUbsHT1gPRy1uxONOounKKJfBChZNtwggCO5sHwBrBrl4bXH7A7mqueb8UAxnZ0pBB0PlYAtVBUyQwAX42XlmyfMwtkFsgskFlga7aA5jBYrM1w6PoTFDGlpy49iny0rbiecugNIzIIB6xNZ8LOm6JkMVOV6lU5qqGu0YloUhuC4VVhhB7CXBnDgQ1Lq6LEWDy/jobjIjLzKObKsOACzSH4+U70DgzB6uiCaebhBA7KhoYiNFSHetHomgVH8MVGzYvQ39vEjjO60D8UYEqXgWrvMOqNhsAL5+lF7KcbQTMt2LkcrMZA2+MTMsliEyMN/vixtGUMsyAu5jgFJO7Ry/xb6Gw+b6HhN0cSPJj4wSSPeL0GNNYK5H4hGHEJ62BHkZbLmADmq+U3lgHVvKYVQqzfMAAzZ2PytGmw8jlx5ZtS8gJY4TdRjIBuKw8XEWrQUIs8MH17h44eLHySMYArUNBbAAgbWksBJOiz+HQGgFvzJSCbW2aBzAKZBTILvBotoPXX2/c6KwQRHGaGmjkYpgbHHYZdCGHlA9Tqg9C16QIjEnMWMe4skLg1U2PR5QANNw+7s4zBGmAHdXQbNRiBDy/XiQ2ujS7DQm2oimlTinD8BgYbjqiAcJvosHX0N3QUuzsQ6sBQI0CecYOVIXRZOuwwQFDvRi4fwvEGke8ooVF1wNp2keWj4VTR0zETQ40mhr0ATV1HIwSajg+diqJdwESt+ncfd4GoZM2zcdbEqLl2o94I4Ec+PN+HG/jwA8bvMa6PddlMhHYrC5hJHpEWt4Ejebcyf40oEODWgwCSYhMFcZ1DScmIBCjHjiSUVlxTuqRMmNiJrk4doRcg9OooGBFsXUPONGHUGgA7kPBc6CijaRXRqEboMjQsWvkcqutXoCCwB+kFrFk5+F5TFECODAD/7lMs+2BmgcwCmQUyC2QW2CIW0DZQNmszLKb8thmOnoMZebDDpjwSSAKNCQmmPDopnTRs1j1pt36mHVM5DCMYjH8jHkWhPOeoGZ3yyNQHUb9aj3EqBEPmOgRMQsbJsauJpss/yQeBjo6gfR3AZHHlLXEE6hF10dHetoQlVZCZWb9NzWbPEqn/x9qA7AdMqONrnD87bbQbbCun+vsmiymrMjF5U0NnuYiusok8beLUofkNAcCcZWIwzAvkUpfUIk/KzjABxZCSOhqWLF+D3t7ekVZZXC9bwXEf1HYZE9rT04Pdd98dK1euxPTp01EsFlPV0S1h72ydmQX+mRbQJJs/Hok/E6+17zWeFkP8z9yXbFuZBTILbFsW0IaHh9tegeJyJJseYwEwrkRHyIoB0NPbdzIwW23QNrUFV7ck6UFUrhYAxq3c+DxC3SjH7s9WizcBwNZzvh6gEF98OScmfhCiWgDI1/OJTh7jzWFLAyBzZTnGwpkCNMOypfyL6wVwfB+O60s/YNY5lGX09oWoc5YxUj6GyiGhjIBmWZY82oYGLpM3dVFtjcAR0JOOKTpQCXNxJxVxK8fqLu0q/VERpQIgt6kUwLlz52L16tWYMWMG8vkYfLORWWBbtoAUbB+7g4krbsrlNSuUvi2fHNm+ZRZ4mS2gudX+9jGAErm36eFqlqhQZuTCokIUhaJQ0SHJ+K+0VmhpgMVeEvEg9I3WxFP18XwtBihJjEi8r5IvCHtqEAJlTXJFjv9uFVHZ5A6mzW9zjx9buRGukipdstZgQfPoVJf6fyy1I//YLziMXe6+GQPupobF2oAB3bdx7UBuh/DHBB3TNKRHcsT3Ak/6I1uEQJaj4XEMA2ntRluzbR4iroOZwIF0CuG8ly5b1VYB5LwIgEoBZCeQadOmjSiAqg/y5tox+3xmga3RAur6o65H0vSH8dGtq65vtPeAZArg1nhUszllFtg2LKC5lYH2AKi1V/B8AmAUu17jx7ggsLgoCV8pvUBTfwFLnT1CZQyBCgbVc24jvpjqcZmU1gVWGr6xfVorOUEdrrjGXmIwU+VlHEoAHZssorqNmFGrjl8LZCO91fWjpax6Kb2GCXFik1YW8Njt2KyHGAQCaRyEPymWzULS7IFq2DFc8/MjABjXLCQULl2+8iUBIAtBKxcwAbBQiMGVQJqNzALbqgV89b0b+ZGqyjm1rmRG5gLeVo99tl+ZBbZ2C7wEF3D7C5RS1UZcrwrRJE5tFMo2ZYh0AFSAoCBQxdJs/Ms5hj+17Oh2I5acaQ2FesnSLCGrMG/BkdaM3hiTpJFsMyeKncHa1WML+cVzFlBLcWGrjiHswpIEO0IfAS/X2n2B9daIk3laz7V4AWYWU/1T8+O8WST7+WUvtAVAboNwOXHiROyxR9wLmDGACgC3oOmzVWcWeNktoK4+qq+PfG9HvmjjxwUmJ/3y/jx92c2XTSCzQGaBLWgBbbDmtCU8I2yfJDEKXaMu2BF4aLUwazf/5LLjLadUvbEMpJ6PdsQYqyQln4/C4sYuXca2tQfAtPmlHZs0ABzPxZNU6QLDGoGuJByq7VqQQoKbHOPNP7l+dm0hHPIfB3Ny1JxjYIxvQQoe1RyUy3rZsudekgJIF/Buu+2GFStWYMqUKeKCHm9/0uyZvZ9Z4JVkgeT3T9XpTM6fHpN2I3MBv5KOdjbXzAKvLAto1eF6isTXHjCIB8mLnPpbgRb77ra/wLV3AZI/NnUR5OtxmZNNbYM0E8cIqnIto/OJPyOxbX8jQP0thzgNANmajiMJXQq4CF2tMn4jvX9HEjKYlDG2xcc4E6N6mAQtSeZIxBwyQ1c9H5kHkz1ayyVd1Go9KpaQy6QBoOxDKwZw1113xfLlyzFp0iQBQKqDL2Uf/hZ7Z8tmFtiaLMCQE/VjVbp9M5JitH47rJQkuAwAt6ajmc0ls8C2ZQGtNty+F3DYvk604JOCvrFqk2QEbyYABjpbn9Ho48f4xa7LTccIaqFKkmBNPF6A464bKobQTvFBb2kF0DTtjRQ+BWEjMXussNg6BpLd3FLj1PtMImk3WFMwCXhqWZV8QeVPYv1C2rmV7TsSt8Sewy1A10dBMmLiiqHLepcvW9JWAVQJKIQ+ZgGrJBDlAs4AcNu6oGR7M8YC/MpK5kfcnYcRua2+P7JgLsVgmQs4O6MyC2QW2FIW0Gq1WpTMOk1uSLn9+EilqFQqoV6vy9+dnZ3yyPdc1x0p9TE8PCyvd3d3Y3BwUGK9CAEDAwOYNWsW+vr6JAOUreL4qACBmamEkFqtJuoQP8dlms2mlAzhdvicj11dXQIddCvyfb7e0dEhU280GvJ5uihVTb1yuSzLKHjio23bqFQq4vrkvnBeLIvCeVerVdkHLsM5qX3g8kxm4Lo4b+UG5TpoF9qHtuT73B8Vfzf24CXt/UoBoE25gKnorWdrPj0GQlUHkPajXZJlYJgEMjYLeEud2Nl6MwtsDRZw0YDn+tB8Dfl8WXLimghQixy4WojJUUFqbDIXRH7oMv6WoRcGc/LjppDZyCyQWSCzwJawwAgAjueqVO47AhTBjtCj4EpBIWGJdd0IR4QgAhKhjCBAKCMIqExPvqZgjaDHwe0SFLhuDoITB+GN7yk4I5hxPf39/bJeroufIXwywYCwye1zOW5DAatKduC6CHQcfI/b4T/+TWgk5BEAh4aGBDi5z4RCQibhlfvL5RUU8m/OkftPIOXftAH/5ty4HD/DdSt3qnpUIJrqHt4SR/zvXGcGgH+n4bKPvaotMDi0Dt1dE6WrDwX8esORNuhmvlXgvgV5hEAJB5EELGQA+Ko+a7Kdzyzwz7HASBbwWBhRyhRBhwBEQCIUEW4IPVIIuPWP4EXgoaJHsCOUEZwIRFyG6+Z7GzZskHVwnQQjgh8fCXkK6AhhBDgCVRKsCJYKsgho/KcUSALh2rVrBeDoauQ6uSznLN00ms0RIONzfo7b4DpUwWWlQI7GvsUAq1RCBW0KTPmcMMzPETK5L7QLAZTbV1CpVMskBGYAOFoH8J9zmmdbySzwMlkg8hG5AZp+AMsuwmxV1QrdAK7TgN1ZEgWw1eI7brDOwZCL8YpIv0y7kW02s0BmgW3PAgKAm1L/lIJ13XXXYc8998RBBx0kSuCTTz4p7s7tt99+RFXjOghWhC5CoOo0oYBJwSJBkHDEZQlg/JvLKrWRryv3IZclTBKqOAiQCuq4Xr6vgFSpdpxfcp0EUwKgUvsIiVwHX1NuS85dCi23XNpsV7ZmzRrstNNOomhyEOSYvcrlHn74YQHDBQsWyL7yPdqDn+f6lTqq5jr2tMlcwBkAbnuXkmyPxrVA4AO6iVCL4PohIj+CFWkwW+WnWKd0JEZXam3GscqiuGcAmJ1UmQUyC2xBC2jVavVFWcBJV+W6devwi1/8Qkp4HHPMMaLs3X///QI7fE4QU2oXFTCCENUwghBhjI+EM8aJJTM/uRxhiu/xc1xP3J3CHIFKxvnRvau2QZgjVPE5oUuBFLc/efJkgUH+rWLxVHwiFUoOzosgSPDjtrkt7gffp9rH7XO9hE4uq0BRJSwkwZbb4XL8HOehXNd0ISs3MJcZb2QAmAHgFvxOZ6veiizQcJqwc3mBuVqjBi0I0ZnvEMUvGnbg26Fca5hUJdXVW4XpXymxwVuRqbOpZBbILPA3WmAEAJMJC0l3JePg7rjjDoEqKnGHH344nnvuOSxbtgwnnHACXnjhBdx2223S3mv27NmYOnWqKHWEKi5DMGL2Jz9LgOPrdPkyZu+RRx4RMNxrr72kSwSB78EHHxTA22effbDjjjvimWeewaJFiwTGDjjgAInLoxJH8Fq8eDHmzJkjIMZkBH6ez7nsU089JSodIZXbJezxM/z8woULZR4Exv3220+gdsmSOJuV8MZlub/8mxB3yy23CKhyjtw/zp3rUsktjEvk+ljomMDKWnd0SfO1o446KosB9H1RUrMkkL/x25ktvsUtkBaHu7kg5iNEvVGDEYUoFUoCfo2BCvp6B+F5AWbPnhln1NvmiwEwkwC3+PHPNpBZ4NVsAa1SGS0Doy52SQBk1iZBi4kezz//vLhFOQhuVAXvvfdeUcAIO/wle9pppwkwEuQOOeQQgUBC2cknnyyKIF2vhMHHH38cN910kwAY68MR4rgdqoEENoIUgfKBBx4QUCNA7LLLLqI+EkIJYjfffLNAIj/z0EMPSZbx3nvvLZmmnJ/K2j300EPFXUzlj/D32GOPSSwiP8euFARLzoWwN3/+fAFY7hPBlfCnXLxUAAl5fCTEcs7cLgGSUMi5vOY1r8HVV18t79FmdJtnSSAZAL6aLzJb875vLgCmfb7hDCNoNtHB1o0hMLhiNZYsW45cRydm77I7OjvLcf9Fy5DHUDXgYftFAuDL3Kpyaz522dwyC2QW2DwLbASAXNVYWKEyRjXtpJNOwhNPPCHKFtU+qmO8+DFe7rjjjhPV7IYbbsDMmTNFIeQ4/fTT5bOMGZw3bx6uueYaAaP9999fVD4qbgcffLCAHUGKQKeyZ/l5KkZ00Z544omyfiajEK44FwIcYxMPO+wwmRO3ydc5nx//+MfYbrvt/j977wFmV3We+7+nzpmmGXVAFIEpoogiikQXTXQDNi4UY5tr7ITY2DfxPzfxvTchjp3Y/seJHSexk2DTbDBguundCISw6KIjigRCBbXpc+p9fuvMO9o6jHQGhJAQ+/CIc2aXVb699lrver826GRy3nnnBcYOpg5GkXbDzAFIqXPixIl67rnnAngE4AFqDVQBi1/5yldCf6688soA6ACYgELUzg899JC+/vWvh7IBiwDIq666KgBb+ux4e9HHFKuAYxXw+r228d0flATqAbh6DOBQ73e0bcX+LmWVVMe81/XUQ7O0avkKbTdpV+0yZYqy47eQCARtAEjqdOIEEo9zAAAG1XD8iSUQSyCWwAaQwJA2gNF6AGkPP/xwAGEwaI888khQ3cLoMXm+/PLL+tSnPhVuufrqqwMLBggzswbTN2/evHA9LJnDqKDGhWnjHgAlYI9yp0+fHtg6ACbgDKAFsGMiRt0MINtxxx0DO3fxxRcHVTHtALABuqiLNsLEASxR08JOGtjCTGLrBzgDOKK+hgHkmz4CHAGyfACz9B+wCBCFJYSVhAGkPEAl9R1//PGhL9wH6IW9fPLJJ3XrrbfqtNNOC2wljCFtpByH0qFvTsG2AZ7tB1rkphQGZs1Fuxw2FciVTQHnYHIZQ3wqZbzQB0SxRlTdalDtZLL6TfCNaLmDbHiZcB0VlRIVlQcCZOO1GUz0yxUVSkVlM9kQ63fVipVV+09VwlgPDkSVEruqwWdhwGBD/+RApLeh6v5AH+BmWti60xhJBZWrXrYDmcIDq1auPusQQj5VCoFY1gB6ZO8YGA8FJZXhdqJUFaqB7SuZlAqpivIqq628QoVySvlCWmXl1JjJKk3wvnxe+Z5OZf/4oB6fNUtPv/6Gxk3aXVOOOkZbTNpdamoRNYfg69UBuHqcwPoNOAbHgaA304EbdyuWwCYggboAEHs2QNwhhxwSgAwA7Oabbw5/w4Zde+21YcEFHKLahQ2cM2dOsJkDhD3wwAPCkQSQhD0gx2HLAF+UhdMEKmDUt6h7Wcxh+/baa68AFh988MFQNqAKsIdKGbaOxRVAePbZZwf2DwAGeGRiv+OOOwZDwcDqmXGE8eNa2kfZLNYAUdTagFz6Qx8BiZwHmFIWgBP2EbCH3SNgg3ZjJ8ixc845J6jH77777qACR5VNX7gPUIpcACTcB/NpR5RojMRNYCysswmbEgCMNrS7u+p4xAdwxfhhjPHhdzZT/R0+Q66mxcHT1Qhsa34SlUzwzEQ1BwYA+lUTO1QCS+OwQg2AwAhbUyyXgonByLYRaxT4bqBXDZMU/dRjnTb1sfJhtq8eACwOZLcxAAxYC3kPpGBMJCqD4M9yX83QJ9RXLKgh26CK002WS0oWi0qSo7FU1vJyWpm2XDhLW1oKPUovXqbKnKe16PEndc3j92ibnXbU5AMP1o77HiBN2EZKZ1WopFQhB3ciwvZ5o8B3DAA/zGEU1xVL4GMpgboAENYKWzri69krFoCD3RthYGD7AD8shKhSYd1w2gDoAOwATtjjAbKYYO0IArAEzMGMweYB8BYuXBhCrLBwwwiijgV8AvRY5AFygDjUyjBnlIU9IPex2GKTCHikfZTvjCWwffQD5o37UPHiNAKIBGhSN/cA+GCRAKeUTdtoI+U71h9tom+OQ8hv2E7KRzWMTFCV0x4AIGXYCYWFxflvHR/xozLqNlUAiGEVjCxy9QZjEPxlsyoVyVCyGvyx4AcsGA5Gk3KtfhJRIBgAIIs7cXojD2tQMVeoAsiQ8SuVVH8hXw1rlEwFFik9BKiMOlxVhkhFGAPA4b8V9QBgUKeSoSZSJAytQV5mAHwHmRuAEYrFx5MVFYsFFfPVnOiBvU9B8bHZ6Fd/doQKvVKyt1Mjeper99lntPCeB9Q79yU19/brjZMP1c6TJ2vryXtJrW0qlKR8MqNEtlGgssu/AAAgAElEQVTJTDWNJf8nE0jKY2EAAA5fCvGVsQRiCcQSeO8SqAsAASqOpQfIcbgUwBXgzrHxHMcP8MYCCNDiegCVGUJUsUysZsMck4/zLOL2wKUsp5QDaKIqZYEHbHGOSdiLpNV9Vv1RJr8BbLSVe/iN9280mLVZIurhN/VzL+3mHjMFnKc+mD7sB3Fsga10SBvkA0DEHtDBoGkr7XbaO+oPKifCPJiBCOrHd7M/7/0Rfjh3bKoAMOQwHpCrQwPxHBkX1cDlq+WzJtuzbugwCADI3TVgiF/N5br6vsAGdvcp2QjLWBEenwC/7p6qiQBPuymcW00+Um4YCwOAIwoAo8AwBoHDG9f1AGCFATDw/MzS8QwBgXwaS4lqXvAkIHHALCC8pKXA8KKmTSdSVVu9/ry68z3KpxMqt6D2zyr51kq1d/VKTz+r5XfdozefflKFbFJN22+thq3HaftvnK9ES6uUzqivVFY5lVE21xxq6itIyUw5GAHwL41KOOwkBqjqWP87vEEQXxVLIJbA+5JAXQAIqAHUsThFgyqHXXW5HIAQIIlFGOAHyDOQQ00aFsJkMoArPk4nx2LNvVxvkOlF12pgA0n+5hoDSecJ5jzADaBHOwFctBG2EPAJ0ISJ48Nv6uNv2sTfXO/4fYBA5wumDNsOohqGMcSTGVtEwB/2Xc5Y4jR21OmsH5RJG6nL5Tp3MP1w9hHnyn1fT+5DvmlTBYCIwQG8aaNtKh2U2wDr3czfugVooCYsyBKJwQ1HqVKuskMBdCaU7isE9XBXX68SmWrMSz7pRNXyrKAiFmZVxbGZpQF2iSsqMIVDqIBjADi8AT4cABjG7gCDaxAfbPwCAEwFu74SjydRBYBAQRg5AGBvKR3sCEvJsrIpicy9qVJRpUVL1b10uUb88S4tnf20Ss+8prbehPqIebr9Nmo9bYa2Ov0k5Xv7lMw2iBQgpURSpURaSiSDaWipWFGyoRIDwOE96viqWAKxBD5gCdQFgAZNfANqAD+AGX47gwdAx7HzHF8PoLN6Ea7a2fh6ftshAhWpnSEcXsV2cpTlDB7UzwfwZLsrgJoBmLN6OEcvoNS5e51RBFW2A05zr++JhnkxkHWaOvpFObQXIOrMIM53jCwMGO2I4FRzlEG7+ZvrDIZXgwurIj/gp7oBittUAGAtWApOlKmE+vurKjoHFwefLV9ejdeImm01oBr4PbjYV7PQRAGXn09Q2auiVKKa9hC7MWz7ABMV7mHTU6yoo6tTre1twUawv9CvUn9elUJJba0jlMfJAGA6kOs1tJ/81/zNJirxbi9PtyUGgfUH8nABYCD1Bhi1oCEYAN1ZpQIbV0iUVA7nUcmWlQHjK6FSIqtuVbRKPcLNY1xPv1LPL5DufkwrZj+jxjfvUV9/UV2lpDR+W40+9DA1zjhWmryrOlqbNCLwitSXCECS2H8Av1w6o4ZsasBJJVYB13/S8RWxBGIJfNASqAsAzfQxadqD1Y0AEAHGAINRBs1ZNqzuBSxxLBoywSnhKAOQBINm8AjTR3kGaE4jRxmANdt6cZ3bxTW0w+DLnraASoeW4VrqoU8cNzNJOVzvb6e0C04E2WwAfs4ugj0jwA6W02ymWUbKMNNnlfSgN+oA+xMFgYDR2At4+EM6CszCgl6phI1HPl8MzjrEn8RelFA8f/InfxLGG7gtCv6qFM9qvXAykV0j9JE3GbbXLKqiDJltVFXX83zxCK4QvDeZUqZYViKdUm8xr+WrVmqL0eP02ssv69Zrb9Aeu+6mQ085dg0vVBwHkoOoJaHygPra/YmCvxgA1h8b7wUADph/hueYMOuaGACA2PolgPDlYLdZBYAVqUiA5orUtUodzz2rJTNnq/+Pz6jxlbfUuLxTY0ZltKwxo+LkXTVmxlHKHXCAiu3t6uKpp9JqSmerDr5pPI2r/UHda8/fEpsTh3yJnUDqP/D4ilgCsQQ+MAnUBYDUxCLrXLl4tAJ4zHZZXQuYgXXDocLA0Isa5wyguA9ACJMWZfhsOwgoM6tnmyizPs4dzN8wOzB6ZhXtpAGIM0DDRpH24iEMEONa2sjCSp8MLM1AUgbthOW0naIBG32wjSDHaC/f3Avw5LdlRD3OhWzbQs7bhtF2ipbdB/Y0N2BBmwIDaFAWZVBTqYz+4z/+QzNnztTnPve54MUNSCdPMzaAGQLshs+abgAWFeaeZgANuFxPsDuNAEBAQyEKAFMp5ZQM11x25W8097lndeH//r9a+OobuvB/fUdfPOMsHX3maYOew4HrK1cXfIf9cGiZQZvDARAQs4DDG8zDBYAGf9WhsNoLWKkqAOxPVVQKHC42eWU1hDAxCfW98aZ6XnpdXbOeVOGJuUovXazGZF6VhpLyyZJ6iltozP57a/SMI1TeZ1f1jWoLrB5+wQ0qqVxsJBVweNxsVMqVojKpdDARYDMQNhJhoozDwAzvicdXxRKIJfBBSaAuAMwn02EDnK5gF1NdvLCVKRMPKyHlUlXWK/yrYINVValhe1UslZQcsP1bveN23LVqFyrlqmOHP7ULX71AqyGe2sAqwHewzOJ74FgvG3hsvLEFj0AB7LaG9OqsgQtpO2oMxA7jdGhjckBtWFxdyqCKKWq8Xa6qAKPgwmVU2dV1P8palaev9nGDWLNTBtBcB9gebO/AjbXyXVv5rscMpVXv9mLmOHXhAQ7gMlvLeedYto0jwPf9poLzJmA1YCuGsVWtX3r2mef0jW/8mb76ta/opJNOUEtLc1CtFvL5APxnP/bHANYnbLW1pk2dpneWLQ/ZYCh3yZJ3tNP2u2jvKU1a8HqLnnjsTbVt+aLeWpDX/pO/qC0ndqilKas7b71X6WSDWlqatOteO6mhtUWPvvCsnn5+nnbLjtKIceP113//PXX253XB+X+i4w8+SG8997S2GztazTtur/tXdWrBHbdpp+YWVfY/UPtP2FKrHp6lOeWEEh0rtKq7S9OOOExjx4zXqu5VamrIqTXdUAUqcSaIdb8gQ7w/QUU/8P5Xkv1aWu5Xa7JJzX1p4Ya7vKmkxlRGjT1FrWrKqFFlZUvF6iSRKEqlHhUWvKXe+W+rfMnD0rzX1Pz2G8ok+6SWtJamkupMNSqRbVP5y2dph/2mKLHT9upOS8v6+5RrSKs1WValv0eN2bZ1tz8y9w11YewH8kEtdXE5sQRiCdRKoC4ALCaTAVBVwxRUZ9sA/gZsakr9q23bDHSqQW6rwVUrWDuHz9oi2q85g9cDJPUeYXSnT415nDjXAQDdqjUYgoi9UPT44O9IwnaSuweZDDSsFgSmwmJU9fwcCggOFQYk2sd68oh6wUbBmVXjdr6hzKHKsgft2uQaZdtq2885wgB9+ACwEjYaAMA7brtdP/nJT/TDH/2j9txrclj5ly17JzCz//7v/675i94O5gQLFy7S97///RAX8DOf+YxmzJihxYuXqq+jR9/9wTl69MFV+rd/vVLHnDJaTzw2X9uMO1oXXfpXuuPOm3Xf3Q9pZMso3X//vfrWX35dW+64rS78xx+qOy+duu+h2nXPvXXhD36kF15+Rf/w99/VfpN21l/96Xn65+99VyP32EWnX/iP2rMhreaeTi0YtZV+9Bd/oRt//I/61aNP67ipU/T4009p0p576H/9n+9odNtolUsFJYtlNaTSqkYVjj9rlYApwLAxrV4V5iYfL3ZL2QYV8iVVuvJKZtLqreRV6e5SezmlwrgRSgUbzbwK/V0qLntHhadeVN+9c9T7+Atq6FquVKKk5nRCTaWKOrsLWjGiTY3TD9LYow9XZedJSrSPkpqb1J3A6UfKJaUsG9t8j9SADeo6PjEAjAd3LIFYAhtJAnUBoKFNVU0xEMYEL7YABKVKsVRVjaars2+pUFR5gNUL7FTFcdJCrIWBMjhe7XEZZLkWcPJ+ZBINq2ZQti4AmB64qDYc22pWcaB9NY4CBo5RUFULAmk/sYHNnlmdbLvKANjQD63jU8uA1V4aPe/fDi9jtbXvqQcmh2pG1AaU3zBvVgfTfmzuPkwAGA2pQz/vu/du/c3f/I1++9vfhpBEPT1damtv16OzH9Gf/umf6uobb9Lo0WNDOj/iPH7hC1/UN7/5Tf3gH38UMr2cdsIJ+qefflML5mV0y+9n6vJr/lo33nC/fv7TO/R3//AVXfGbS/Tm/EXaa/d9ddfdt+nkT52oNxa9oUIqq//4+cVKrexWc3ur/uZ7P9S99z+g22+6SSvenK+vnfE5/eU3vqFF2Yr+7vLrNOf6q9TQuVI7n/w5/d03L1D6rXn6+d0P66HbbtC//ORfdNPtt+qH//T/h1iWTdmcMviiFmHQYwBYbx6IAr6qI8fqDVmiu1uZhkYVi/1CG9Cazmjp43P1+pzHlU4ltc/njpd6CtKbi5R/6mkt+uNj6n/pVY1c2a1RhYSSuSUqpDPqVlad5aw0eoJapk1VywmHKzNlN5WUVb5UVl8BwJ5TLp1QMmiSC0EVUUlX40iu9RMDwHqPNz4fSyCWwAaSQF0AmDKAw2aFFEkhQn0yeC8GsxW85rDbY7ILRvKFgThnVcYrWy5UgWLwrWMxA/zBKiaVLCfVn6qqKfkMZfdUD7RUVdLV+wdDPRDywQbX5SrY9L+wOAyc5ztbWn3tYBzWAXDKPSGOGEzmgMp3sI3lSrDdKUdCeLhOt4XvZNkM6EAba0J+YAO0rs9QALBWZW6WLgoAQxsigaejMo7eX0/FDrh3DEZfCwj0P+IjfpgAkM1FlE198YVn9cUvfklf+9qf6txzzwsOHsVCQddee7V+8IMf6NJrrtWknSbpgv/5reDk86Uvnqvvfve7+t73vh9iNf7F+efpgv/5Bb3yfJeuuOIm3fPgL3X//XN04f/+hf7zoh/oJz/5B/V2lfTTf/6Zcrmseood+tvvXajufFG/uujXIujLW4vf0T/+6Ed68YWXdcct12ve3Bf0rf/xP3Thd/5adz33pO55/i3dednPVepaqSPPOl9fPeN0rXzpKT3w0tu6+Gc/0qWXX6Zf//ZK/ebKK0KqwRShRsIDXFv2kg00G3wEizXR57es9m/ev3RXvwrJgtSSU0NPl17/5XV68c77VG5M6dhPHque1xao59mXlVy4ULne7uDtWyGAD7FExybU1VFUV2akNHlvNc6YrtzUvZUYPUqVRFLd6VR1E1xOqzXVEOaZUqGgRKKkZAMhX+ps8OrIPFYBfwQHZdzkWAIfEQnUBYDpCibugCQAXxUAAgQdviIJwCvD+g0EUU1XbcO8B8+VCiJCAveWCKhaSSo1AABTAMD06lRcBi1R2UXBylAyXV8A2EA8rhDWo/rNxzHA+A79YpeeSgYQGEAdXnvYZwF4a2boqBoq9KdYGLBXW+1FCoCx7Z4D0r7f8WKHmSgzZlUtdXB8bR+3Y7gA1LaeQUZ4xqbTIaPKhgSAa7atGoMPc/0BOB1M5H550a90zTXX6Zijjw3xGhtyGe288476/ve/p/Hbbq/29lEhNd8FF1ygTLpB559/vi6++JLwbL/wuVN04d/9tbpWpfWbK36ny674J91+x336+wt/pmuvu0J33nmD7rj9Xv3lX3xH5UpBY8a3af6br+uy31ytaVMP00EH7q999t1PN934e1166aU6/6tf036TJ+uU44/Vv/3kp2rYerQu+N8/1udPmq7+zqW6Z/Y8/ejv/6/uuv5SPfDEG7r21/+t//7lRbroV7/UpZdfHgBgpVRSU65RjbnGtaSve7+jZfO7rx4ADNvLjpVqDgl9e7Tsnoe14te3qTxvvlLtTRrd0a9yoqBUsqRMsqRkf58SRfI7NyjV1KTX+vtUbB2pkdMO1phPnSLtvbNWgssLvWpTWp2ZpLLhv0TVuRzH4Uox5AkmdmBrMpKKcAjx13NiiQHg5jdm4x7FEthUJFAXAGL3V2XwAEkDbBUAbqAHLcmyCqWiCuUBu8BUOqTEwjeCmGkt5Wp8rWKS7KlBGRvYP1SvAKlyQxUYRf85UDJVRG3YhiO0d6uAq04s62IAAXGA1CgATBFfbiA8QwCwSWKCRQBiuSKuKXIuMkubBfTEniJ3KHaUA6qeQaCMejyZVLGYH0631rDfi7KihJ5B1Wv7vyhgNsg0sLZco7KuZwNo7+paZxC3YUPbANYCQDYWITpfpZpdpatzhdraxujOO+7VQzNnh8ubmxv1l3/1F3r11Zf066uuDZsOUvSd84VztHJVh6677jodf/yJAcTefvM12m+//ZVUsx574nGd9YXT9Mqr83TTDXfpzDPP0ujROV1z9Q1aMH+R+vp6dcZZp2nXSZN00a8u0WNz5mqX3XbUl770JXV1dOi//+uX2m7bbXXqJ0/W7bfeqkMOOlgTdtxGd9z9hB6dfZuS6texx31RB+y7i2679WotWSKdddZpIfc0Ace/8tXz1DaibfDdwonHgcqHNUg+phetSwXcUepVWyqpTL5XfY/O1oLf3KCmx1/QuERGlcaEkiOatOqdJSp3dWp0Oqdkrjls7N6uFPSOyhqxy4EaecQBaj76YBUnbCO2q5kCit/qBFFJAv2qwK+CSzmB3pn/JHVX+jUqUY1futZNWJ1nFgPAj+mgjrsdS+BDkEBdAAhYq6p9q84ffACFBkcNxe4QOrWSSoesBsVUOrCDBXbCZam1WArgqkgU/AH8CHACAFJGIb16D2xQEQU49QBKrYze7QSybgBIOwBtBWz1BtqHM2BmAKBm6BMKoQRxwqptTZYqof2kbspDakYaMQgAB2bu9EDgajtrOPxMVV1eGnSsqfeso6DNgM4A2UGmHRLH6e+CCj6bHQSPQ8m3rop9ILyNAaZD4diucUPbAA4NAKuAFyYwlayokC8rm2lVV2efWlqb1dnRpUwW9F5SOpMLXsBNjdWUhATiJfg4oYyqQaL71dcj5XINIcRLpVJQIl1Rvh8WrkWdXcs0onW0Vq7oVqVSVtvIBvV2d6u5eWR10U9UTR0Apn3Ek8w1afHixRo7fpySiaTeWb5AY9u2UXe+Q02NSRV6W7Ry1VKNHJ9SpjxK+VI11qQ/b7/9dgB9hBYCoDqzSL3x8bE9X8cJpL+3U03NaRVeflErfnODyvc9rBFdncpmpFKlqNebympPpjVeDcSCkZZ36ZVEWUt2mqDC7ttrv5M/q+bJO6vY3KKVxYIqnUWNbR0ZvLN78t1qyOTC8/f7ELzTQ65gqVAuKZtctw1nzAB+bEdu3PFYAhtdAnUBoCrpqm1dCPtSDQMDACQsDKza8tdeVLaxSbmmViWbWlTJZFVOZVVKwwSmlO7sD8xZMVUJbFkItBoBgH3lKgMWDfpsFSbf61JhBiBUNwzMugEgYDQA1IF/AVS5fQBBYiCqon6i+A/YOXLeAJG8oP4MFQaGZPPOegJ4cjxCQAmeqk0DYXLqjYRahtQg0Gn6AA3Okcwxx2J0EOyoWtjOKPXqNMAkdiL/+NB+/ln1/GEAwKrZJECLH9VYbaVK1da0t2eVWltGq1hIqpBPKJlIKZ0hBFFPUAUzQELaQdSpsDJdPYNxGyvlhPLFVSrlm5XJppXNcS2OF9VsscX+JjXk+tXXV1FjLkcGLyVUVF9vj3INrSr2V5TOJdXf3yM2Cp093cEBBaZ78fJ31DZqtFRYrkR3u7JtReVLHcpVxqI/VF9iuXLl8aqkKyHu5Mj2kSo5MHjIEzycpxNfs0YspwFxRMPAJHFKK67U8zdcr8KvrtOuS1aqoTmpzkSHMuQVL2dUSLERTaivJK0oJtW39QSNO/E4jTvuSC3acoSSfd1q7UmosWmklGtWD/UUE2pSNU1cMh0JMEWqwP5SCByuVEaq4wMSA8B4DMcSiCWwsSRQFwAWir1qyDYrX0aFmxBramOxS51PP6nZN9+gt/7wiBrHjNbOhx6gXY6focbddtcbnf1KZ9uVLGWV6E9pTLvUtXKpSupX+8jxWtXZpXShrLmPzVGutS2o57KptJqbmpRubtbypUvU1Naqzq4uNbS3KpfJadHSRRo/drwWL3o7MDf2dE1XCmppHqFlS5ZoRMsIpSqpECC6ffRYda5cqUpTVulcg1blewOAa21sVZ7fnT0a0zZSmd4e5YsF9ZUKyjU1BbV1OplSd2dniF331psLA0O4xbZba2XHqhDRv71lhJYvWaqR7e0q9vQpTQ7kQr+ampuVLxWrga7b2tUDU5TNhZzD8155RWNHjdb2201UoT+vXEuLSt096ks0qHlks9588w2NI7tIb7+UalDjmHYt7u5WceXKEJyagNa33XZb6Pu0adO0dOlSPfvss+HcwoULtfPOO2urrbYaDJD90EMPBaYLAAjAOOigg0JcQBwfaJ9zGXNs7NixoTyAnbOjwERtueWWgY0i1t/PfvYzbb311vrsZz8byuADkHz11VfDvdE4gE7f90HEARzqxVjD87q8pg1p7fW1cRZrbUqjf9djQ6MOOdGQOAbj/o4yrfaId7tqHZ0Sq9OCCEBaPb86//B7ZcA31kSy0epFzZCWOvsLSuQrasnkQqqNUrKgnlRFWfXotYuvUOOND2jbZctV6H1HPYlutTQ0KJ0vi/kt0zRSS/srWjVijEYeeqiapu6n1C6fUHb7CSTshSqsqgnYASQzqqSyqmBfm6iL7zaaWOKKYwnEEoglUE8CdQFgMs2ilFJXT178Ht3SoOVPP6FZv75Mb8x6WFNaRmhxZ4dW5rKadPJxmnruuVpeyahfTQEE9XVJ/V3L1JorqbG9SStWdCqTzqmlqUWP3HGbepuyOmL6EaGdpb7+kFcVkNXU2hIYxJULFwdggkos39enXOsIqVgKjhl9HR1qyKRExo9RW2+tQme3Ojq7NXrCVioXCgPOKFJXX6+6S3kpmw7AB6+9XCWploZGlQs9KicTSjc0aEXHysAqBQcHwp1UpDfnPKe3ly7R4cfNUL63V9nGnPq6e5QbNUqFZcuVGdemUrmkYqWs3ny/+gvV1HaNDbngKLKso0OjRo4KdkUhw0hDte8rV64I/cp2FNXZ160RY0ZK6YxKy1Yp1dikjmJBpaacmhLJwOZx7zPPPKP99tsvALuXX345AN0999wzgEA7fNB2VMCART6krOO+T37ykwFo0z/u4zoyZfBZtGhRCJECkOOY8yGTqQWA+Prrr+uJJ57QOeecE+5D3tSJ2gsv4I0JAFfHmRx6qA8VZ7HWizoK4FxKLRj03+8CcAO2nWZoa8HgYBilIYKdBxA9AABD+djWRgCgQXa9l/jjfL5TpWB/nE5mguc0jFqB+HsV7CfTyj/5qN644gY1P/SEJpbySmb7VWwsK412oaNHvSWpvXG0CoWcVu2wnUadcZqSR03TqlyDuvuL2irdFCIBJAYBYNXUpZIi/kEC7Bl/YgnEEogl8JGUQF0AiKVVOpsLjh7lUr9GZhN69Zbf656f/IvGdPVqi5XLlRzRqhe7u9Q6dX+d9t2/kyZM1Mq+ihqyrUqWsXXqUrLSozffeFXX33SrcrlmzZg+XR1LFmvR6wuCKnnVipU6Yvp0jRw/Xi8+96xef2uBtpwwQXvusZfuu/12LV+2TBPGbaFp06ere/ESvfj8C2ptatazy97S9COP0B8efEjd+T4dNeMYtbWP1NuLF+nBhx5SflWPDjjkIE0+7KCgVnvnnaWB+VO+rJcff1JP3nmvmltbtOeUfZRragxs3aQjjtD8WY+EOGzPPPFkcAo4csYx2mvqAXrluef01FNPacvxW+iggw/WW6/P07KVK/TSq6+oaUSrTvjkJ6VkSvfcfHMAVlMOOkQ77LCDnpk7V/tNm6ZSsaBbbrlFMGzbbrutjj/iaKm5UY889AetWr5KK5et0I677qE9DjpYz81/PYBJPFvJXkFauSlTpgQQ99hjjwVWkNR7Tz75pCZPnhyCMl9++eUBxMHW7bjjjtp111111113BdAHkIMJPPvss/Xcc8+F3LkLFiwIKlKO7b333rrzzjtD+1D5nnXWWdpnn31COJVZs2aF/Lonnniifve734W+fetb3wrs4MYEgGvmcxnqHVwzzE49r/JaADgUK7iucEW1dpaordfFOrr9+DY7ziYbrvgzPAngkat8QY0wczCBKqhRCbUUC9Ki5Vrxy4vV+eiTapv/ttoyJRVy/eptJPZTRfmVncqpTS1qV3FVUQu23VIj/uRzGv35k9SjBpX7Cspm09X0faVKCBgdGFkcPQYyCWEHHH9iCcQSiCXwUZRAXQBYLFUCI5dralB/zyqNKBe0+J67df8//1RbdvdrhwzxtUZo7soVKuy8k074/76t1M67qpdUSelGdS9drtFNKS2d/7JefvEFjd1iGzW1jtCIhgbNn/eiFs99OYCMpYuXaMtx44O37ZzHH9PWE7fTzntO1k233qxJkyapqbFRfZ3dyvf16xPbTdTMPzwYAvlWWhs1c+bDOuqYY7Rk0SK1NDVr0s676NabbtaknXbWkuWr1KuSdthjV40YM0rL3nlH40aMVGMpodkPPqTtmquq5lQuq9332UdPP/mExowcpd6eHu0wcXu9+MargfUaPW5sAEVvzJ8fGDjUroCrbKFfs2Y/olNOPVUvvvKyevp6tf++++mRhx7WpF12Uft2n5DKJd131106bPrhevSxOcpks9rv0EM064EHNLK1Rdvv/AldfcN1OmjqgUooo2dffk1TjjxC8xYvUuvIUUG9i5foJz7xiaCWxpkB9S/sH04Cv/jFL3TccccFb1JAIdcTGPnAAw8MYPDWW28N5wEngMVTTjklhG8ByH3jG9/QA7Rj5MhQ3tVXX63DDjsssHtPP/10KIPfs2fP1vHHHx8YP8DjaaedFkKWvPTSSxsVAFbtAtf1WXecxeG8tLUgcF0g8t3X1mvfUAkJ17/Nw+nX5nBNAXvc3qppaKHYp45MQaNbG6U3lmjJTfeq97fXq6GjW2OwzMyVtTjZoe5cJdiHJskOkmlVe0O7VvUm9GZLs7IzDtGWxx+lpm1vSQ4AACAASURBVAlb48WhVFM2YPRUpaI0KQhDIIPKoOf/6ngIm4M04z7EEogl8HGSQF0AmEpng8fkiJEjVOjrVGu5X5o3T/f/7D+0cOZsbTsypUUrupQfOUq7n3qq9vrSF9Xb1KT+bFOwl2mpJISfw5K5j+vhmQ/p1NPPkFrbofz0wpOPqjtR0b5HHaUXZs3SineWaeIndtAr8+bpkCMO1+IlSzR33kuafvjhwS5v/muv68nHHteekyfr+Wef0/EnnaRVC5YEdmzajOO09OmntejVNzR5x0l69N4H1LVshfraWjThExO13R6T9Oqb8wNwmrrH3tqipV23XXuDctuPC7Y8jS3NOu7EE0Jmi4suukj/48vnaiKs2sxHgyPKdvvvr1k33hhYtkyuQf35vKZOnaplxVXKNTZq932n6Pm5zwRZbbfV1nrm6aeDunfXXXbXiLY2PfjwQ9p50i5a2dWp7bafqFxzUwCer90/W3tM2VsPzp6l6YdNVzLXpAcefFifmLKftt5tF1KXBpZu7ty5mj59erDpg4FEzQub9+KLL2rOnDlBTQxD+PWvfz0APQAbYI3MEoC0z3/+8wHI/eY3vwnA2WrgY445Ri+88IIef/xxjR8/PjAcJ510UngHrrjiigB+YRJhAL/85S/rv//7v3XyySdrzJgxoS0bWwVcDwBGVcDDYf/eC9gb3kRRJ9lzCGszABIHMu3EDODwJMtVhSIAEI9vPHGLWpbsVUuyou7ZT+uJy2/Q6PlL1N7QoLYRTSq1prWqsahyW5OaW5uUS6aV2mlLNYwdr8qIkSpkRyjXNkbpUWPUm06qW1LLQCgoIhag7gUAWptfVskhu4ff4PjKWAKxBGIJbCISGBYA7AoOEe3q7etUpbdLreWSFs+cpecfmKlXnn1UqYZmTdr/QO174onK7rO3VvX1SdmcOjq61N7YoErXSvUuWaSn5jypA6YeqvbRY6RSvx6b/aCKuZym7n+AXnj2ucDubTtxO93/4B908qmnBK/Ke6+5UUcddZTattlGb859NgQd3m7rbQIo2nvKFL22supgcfTRRweV7tJXF2iPbSdq4fOvaNKBh0gTt1J55TIlx46UmhrUvfQdNWcapWUdeuTOezXtMyeqTKy+0aOk/l49+8wzQb28x667aeddd9Wrc58L6s4DD5ga7O5QER90wvFVL81cg56eeV/IL7rHPnvr7TffDGDq0GkHKd06Qo/ee68WvjJfxxw7Q08+87RGjx2jhYsWadIeu2mr7bbTW/Pn66VHn9ARxx+jex+4T4ceeqgyjW268867NH6XnYNqva21PahZYf5QGQNiYOxgRVHzAuSw94MdfOSRR3TuuecGwHrJJZcE8IcjB2DwU5/6VACAMHyHH354AG8AR64HFKMO3mOPPYJqGXtBVMtXXXVVKJd6UAv/+Z//uf7hH/5BO+20k04//fQAOFErb0wV8HsBgH7naoHg2uz9uH79nTCqAHCtNoU1NoBc6zzam8gcsUk3A9/0Yl9e2VRG5UxCHcpLvd2qLHhbXc+9ql222EbKZCS89XNpqb1JGtGEe39QA5d7l6unuUH5dLNyalIT8aB6y+pJltSZqaglm6tGPoBpTATt76CHNmFkUnUyfWzSwosbF0sglsDHWgJ1AWCQTrEU4qj1l/Pq6O1Ua0NWTcWiehYv0ZI3X9PYkePVPH5CSLrek81oVSGvsaNHq6ejS/mGsprLJTX092nW/Q/ryaeeU2NTi/beazeNHpFTvrusnXbaWQvfqOaUxSN1/ttv6cAjj1Qp36e35zyrR2bPDmpPmKuDDzs0OFrA1O2x9956sfedwIJ98tRPasFrr2nhq69p6pT99fyDs/TMY0/o7URZk/aerH2nH6LWsaPU39unEZlGlRYt1+MzZ+nxOX9U28h27XPAfuru7Q1gZsaMGZo9a5a2GDdeyZbmAIrGtI8MAAl7OpxGeot5TTvwQKW7evXO8mU68LjjNHfWrGBPt+8+U3TtFb/VqpUrteduu2q/Y4/VH++4Q/sfcrA6V63U73//+8Agop497lOfVlNbs2689SadfOJJIW7d7XfeqXE7fkLd6YS6l6wMal5s/wB2tO+tt94K6lq8bTn3wx/+UGeeeWZgQq+55ppB1g5AibMGxyxbWEMAIKpggB+2f3zDAJ566qm6+eabg4oXJvOII44I6mKAL8AWNTIOIGS8wJnm29/+9kYHgO/FBrAeAzgUSDMArHUCGf6ssSYAfBfDWMcJZPj1fDyvxAe8q5hXP/Z5qQz5hpQoFdSSTCgb0ldW2dVifyHEJk02NnFFCN/Cxm9kiCdZVk+5omQpoXR/RRmYWMIdpKQ+nOAC+5cg2qkSAQFWQWCJeKgOjv/xFH/c61gCsQQ+whKoCwBDJo58Ufl8UdmWJuWzSXUX88qlU0oUCmpNpZXMJ1ToLam/LDW0NWl5Z6dGNjYpXSprfmWV2hJSS7GkbKJBby98R82tI1Qq9qi1KaWuRINGNLeEILp88PZduGSx2keNVKFS1oh0g5avWBHCqxDfa4sJW4UcnQsXvR0AYXFlhxLZdDDK7uhapVxDg1oyGXUvWx7i+fWksipmU0q0NamcTamYL6g9kVVbJSP19Gvh0kWDep1ca/NgG1YtX6HW5hY1J7PKd/UEdrKlvV0qFLRg4VvBZnD8hK3UU8wri0q4WAghWLo7OrX8nWUa2TpCvR1dGrfVWNKZaOGCBdpq2231ztIlg2nzYOS2GL21evo6VSgX1ZjNBDmMGD1Gq0pFdaqoFa+/HcAWjBzlA8QAjqhrsUWEnbzyyisDazdx4sTA6iEXPIxh6LgPD2KAKaAOgM29y5YtC+ARFS/evgBFPhy3NzD3cp52AsBhE/EKhn3l2KZgA1gPAL4fdWoUpAEa16UWrr323XPBahXwUIG4wROxCng9ZtDuvNSYVk8yoUJ/UY2VpDLFUsgxXiHLULmgXBqPfOwEk0oQqJ4MPqWKMjCG/Z3KllNqzDaqmEmocyCwYBPxSvP9KqcyAQAmAZiwffb6JrZppaxMDADX4+HFt8YSiCWwMSVQFwDaxj4EVx3IlxtSww04vzVUozuv41PPBmrdd9djbcjEwSfk52Wi5vdAlfwm9p9Tvdkcn6Y71Vs9I26fHyp0CPX25fuDWjYaaNkx8TgG4FzXJ5pGbqjrAG6UA0ADfAHECO0CaMMW8Prrrw+MHmwfamIHhjZwqRdIu97gi8a+i4Y6cR+jXsCUBTAEsGKjaPaM37QNtTXsJSFnAKj1nm29tm0O55Epz8gpD3l+/A04Rz6AeGcE4RwfmF/kjFy5xplEGB8cd/o45M848WaAb847iHc0RSFlRzOSMM7YjPFxphk/W47VAl/qoc2U6awYjjWJJ3t4VwjjlMuFPvFhTFM211sGHjt8Uyb9czu4x+3CIcubFq7xuKeuqEzrxXZ0asa1jSWnQNwcxlrch1gCsQRiCUQl8J4B4MBGOpQBeNkUAKBj6VazlFSBYACFAwAw5GgfyPfLccDfIFisF8ahvGYYj1rQQuDoaFiQ2hAhldK6AXA9AEh5LFIsbCxyLIowgCz2qGZh43DSgP1jAQcwhj4Sx3Dg3vUZ8jEAXB/pDe9egxSn8DNgCs5XI0ZUM5k0NAQGl2cfNhbFYgCNBluMB4ASZXGMccJ5gyfAl8GRg6j7WrO8lMEYM7DiOHUC2gCVUQDKvZQTBY7UyXX887ihnZRLmykHhhlmmQ/2tDgTca37CvuMRzrHXCf3Ugb3U57HOf1xvmTnuXbwcQPKegCw3hOKNyn1JBSfjyUQS+CjKoFhAcBoijODKee8zTrB71olsGEZQBOQBnR8RwEgeXwNVt2SYMc9ABLrTfC1AO5dMd1CXIi1G/nXYxiHAwBZZL2oAgZYoFnoWCxtBxhlhQL4TTqo8PoNzRgArp/86t0NgAGoRcE6z458wqj7YUwBM3iAO5C380oDhsz2GeABwgBI3jRQP6yZNwV8+37KBSByr4N/ux2Ms+XLlwebT+JQmq0z8PT4YnxQPmXgYY49KWYJsNTEv8Q2FUch7Ge5lmw1sMbElqQdhBki3qTzWAMEsVedOXOmxo0bpxNOOCG0HTMG+oVMyErD2P/Od76jvfbaa5B5NCjl+pBnO5V6l/q+9nnUe//rPb/4fCyBWAKxBD6qEqgLAKMdM1gxkOJv7Ow25KfeBI16NwCegUZEAWAUmNUCrcHwccl1B3IdropoKPsu6q/X/nqyo1xAQkgyn0wOMiAshgBDq8y8qHsBf/9OC2u2KAaA9Z7Q+p3nGfJsa1WNr7zySnA+IiQPAIrA4TgCoUr3uDYAZHzwnBgTeIXjIMU4wAuc8RFV53KdNxSMJwNHrjfT6B4RPogwP4Q7itbrMW2QZRaOEEm0Ey91YnsefPDBoe3YpZK+EKaaz09/+tNwHtaae8huQ9tR69Iewg9Rzje/+c3AeCIbs5sAY+onDifxLQl7hGrbHwPi6LhdvycU3x1LIJZALIHNUwLvCQCGhacGL9WNw7uecqsHoGhPbRuif9dVAdUBgHXvX0v/6rV7uGJxajfbGLLo247LANN2fiyiBolc43uGW9dQ18UAcH2kN7x7bQfH8wqpCiuVwPzh3Q5QIrg3YXgASoA5WDnUn4TjgRGDMYNxg2UDxN1+++1h03DkkUcGFTIgjuM4ERFKiG8CgQOiGC/cR72k+wMcwrwB1gBnpBHExpRyDPjcK+qgXKtlDcQuvvjiwOwRQogy7r333sBg4jQEq/jzn/88xJIE0PKx2QLlAfjoI/VyLZ7ngEKze7SBemAEYRyJTYlq2Kpw2zF647b+YXyG9wzjq2IJxBKIJfBRk8B7BoDv7mA9CrCek8i6RVYPSNUDoCVScUUcRKjNamzA4/tR0a4BCsurMz3U2v8NBZhre1uv/bVAznZbXhCp0yDNv734fRAG7DEA3PCvNACKj+02ATkAJwAgWVlQdz766KMh8PeDDz4YwB7sHgAOMAh4s1MN6lqcgwA+ZKrhXlhBHC8AjYA7vMQJLM4xQBmAkOvsdAEAQ92M2hfwCAA0UDPYijprcI57YekAcQRSB+Ch7qV9xKkkxBD9xL4Pu1Wy1wA2iS1JPTB3VkczfmENsQ8EiHKOPtBvvgGQ1E9sSzzaDTyjAJXfjF0712z4pxjXEEsglkAsgY+WBN43AFztHbxpA0C8gG3zV+vRTMvr5fKsZTxrVb1RADckWK3DMNYDgLAfZjWiC5pVvmYD7Rhgj00zH/UAdL3hGgPAehJav/OwXwCcKFOFzFEBk9qPD89w3333DSn+COQNKII9IyYlQBBWj3LuINbk/vsHGzwYO5wtUMcC4GARUZ+iVuY4IA9Wjg/qVkAjuaABVMSEBBxSLipcrgPYAeAAaVYVM9Zw6iC0EGAOgAlAQ4ULc4eKF0cl2kBsTcqjXKuqYSoBjmeccUZg/riXMvhHTmrAIuyk5cNYh/l79dVXQxvuu+8+nXfeeQEoWvUbNl0DjjDILWYA1298xnfHEoglsPlK4H0BwDVUrCHA1ro+G5YBxOvXn6Fa0peqDIZ8iQJAh4ZpsDfLWrow6AAzEFC2ViVcyyDWnk8MOImsTUL1ACCLLAsi31GHAX7zsbG/VcVR9TD3rO8CGAPADfvyR+3uAGU8RwASKmDyP8P6Ad5gyQCFMIOAIv5GTQr4gcUDgGETB9gjhzOhdnbbbbegDiaTDuMAMAe4gyUEuB177LEB1AHKyCJjAIjtH/VTNvUB3sxEw6jZ8zfKuLkfq1at0rXXXhsAKvVTDkHHiVPJh/7h/QsIpUyYQFTbqLopFyDIN0AVxo8yAIkAP8AnoJAPxy6//PIgH4Al10bBn+vasE8vLj2WQCyBWAIfXQnUBYDrciaIet2tTQTv14ZuuCKtbV+tGnZtThzcN5z2D7cdm/t1Vi9H5cYxbMmwQbNXqGPF2XGF6+M4gGsfHXagMJh3HD0cOWDPYPQARQAvVL6ohfGuRY0LUIMNM/sL4wYY5D6AF+wgwAx2D9CHkwVp/bgf9hAAh0oYdS8gEIDJs4LR4zhgi6wwgDAApQGWx4Lb7HAxqJJhHwGiqK5hKvH4veyyywQwRCVNm/gNKISlZHNDDEuu5TfAjkw5d999d6gPJhEnFJhPVL4AQ/pHX3bfffegWkZmdpJaX8Z7c3+P4/7FEoglEEvAEhgWABzKti26GKxLnBsaANbWXbsAmLmovW5dwDYeHu+WQAwAN8yosCqf0qMewYRDgaUD0GEGAPsFOEONi80b4IxrAHqAQ4AknroAN+z5uBd1Ls8NFS+MIXXBvAG8rEZFdQrg4xxlUweMI/+oEwaQcrD9o312VKl9zwB1AEDuBzSirnYAalTTdlQBtAFoUW9TH+pf6udDfwwOAaPcD/AkJSGsIe24//77g5cz/YDBBDQCAP0vBoAbZpzGpcYSiCWw+UlgWACQbtuj1BNsrS3cxhJN7YRfCzjjcBAfzJOJAeAHI8ehNiLI1tkwDLCiGyzYLoCaAzlH4//B8NkhAxYsmlHEsfUoC2aN+6OOQQBLPhw3i4gamr9RQ0ftSflt1e9QzkWURRnc61iB0WDVDhrN+2imj2P2XK+tq9aBwxlSuJ76HdqG41HwFwPADTNO41JjCcQS2PwkUBcAWoVaCwAtig+b4av3COoBQoPZTbX99fq3sc7HAHDDSN4hfKKBu6Oe33YCcpgTgzrbhMLMGSw6G4ZBYNQTNloOv2HhauP4UQ5MolWqqIVhDnnHDbTsbAG4s92pA1lTLsd8DfdQHvXADjrcjVPERbOUULczkjgjSjRgNYwnoM/gl2/3PTo3xQBww4zTuNRYArEENj8JvGcAGBXBpsCuRQHoUCB1bUylr60X6Hnze+Tvr0cxAHx/cqt311Aeq4xJ5/l1XmCzeDBgAKgoALTzCADKadcAYdF8zDw/7okCTgegBuiZYTRrR32OOWm7ROq1U5G9lw3SOO64gPYUdlo4GEw+tN0e7WYfAXGUZfbSbXa6OtrtOIDRnMCUh9cwQDa6qYsBYL0RF5+PJRBLIJZAVQJ1AeDabOWGy/xt6Am5nhNILXAxaI0B4Ht7BWIA+N7k9V6uNgisZQE9Vg3A7OEdZQOpx2pgM2i1gcKtunW8QYBcNAOIc/w6dZrjERoEOucwf9MWA8lo0GXX6XAwViUDCp3bmPujdVmVHN1I0hf+5hz1OAQM5WETyHkHjI56Ib/L+z6x7gw/7+X5xNfGEoglEEtgc5RAXQAYjScXZdPsDVpPKBsaAJopcDuiCwG/WaS8wFpFZsbAoKZeH+LzVRtQP3N7T3Ms9gL+4EZHbQxHl+yQP1a1msUzYKv3jhkoRTdLVuFGbXqjjKFDCNm+zmDQoC0KMjmHjSEAzXVF41RSh1XI7oPLM+NYL2Cz2UgAIfIw+IxKPwaBH9xYjEuKJRBLYPOXQF0AWBtgNQoChgMC6y1O6ytiLxwGedFvynYOUdsfrY3RXN92bO73xwBwwzxhs3OUzthknHpMRz2Efd7gm7+dQs0MW9QGz5sdnpvTA7rcqHMGZVi1Gg1J49/R9kXVwzad8P3UbU/hqPOJgR7lABBpv1XOgDjPD1Z7R+0XzQLaMYbUcHg6G6g6Bd1Qmz9v8jbMU4tLjSUQSyCWwEdfAomOjo5KFKTV/iYAKyEciNvFOZLTM5E7KLEnYSZ2Jn7sffiN8TiLAhM7vwnvwH1M8KSHYrGwqomFAAYh6l1IXZRFgFcWRV/jBcV/syhQHnHEHOqCclhkuNfBc0lMb6N17iWsBIFubWtF22kv92GwTvucc/Wj/5jXvwebKgB0AGKeG0DGjDVjx/Zh9iDlGTtkitlhe5N6HJhVM+CybRv3GpBFPWrNlCEfp1IzoOLdcYBizkdZN7e7FqhRr98tyvN7ZIeHWs9aro86e0RtA91eq2PdhuimLqpSdZ/5dugZl2+TCdfv8eDrDD79DhPSxX2PglXk72DQvH+u04yiy+d5Or0dYWy23HLLcK0dU7ieOYVnbHtHq6odi9KgdP1Hf1xCLIFYArEENj8JvIsBrGXsmHz/8Ic/hMTuTLh8E3CWRY5Jnphijk/GhO4wECw6LJ4sYCxkTMosYjZY57iBYNTgmwUM427ikHliZ8FgQWeCZ8LnPhYuYoQB5AiOC8DjPsAdCz4gjnoBr7SPdnOcf7SF8p0yyzZNtI/2s3BwHX20zdLm9+jfW482VQAYXeR53nY8oHcGd1Fmy4CD/thD1fltPfbtlEBZAEXbn7lMrjPbxXjjPQCs2AmDv7mXMegNi5m7qN2b7eXoA9ebpTbY8718O9SLgSrvjNtn1jBqE2cgyPUGwlFZ0UeXYdAZDebtd9J2hQbWZv1tl+jwLmb9oowhAJj5wfdyDlny7pvp453lGt5vZBgF3MgRWVC239uobDyCuY5nQj9pn8H/htY+vLc3KL46lkAsgVgCm5YEEl1dXZFkalU1VPRD5H2StxOw1aCIiZ2Jm0WEyXbSpEkBLAEQifzPBO98oyyQAEPKYHInnAOBaqmH43wDMlmoOEcCec6zeDKBUxcZECiTQLCUR70s9hwHjFIOQWUpi3axsLOooC5iwYAlZCHjHtqA3dq0adMGU0zZm5A2e4HjehvXb1qPbOO0ZlMFgLWskAEJ44Qx5edpW1A2DwAF/jZApIxadaulHD0eVYFSrgFGFFhF7e3skRu1e4vm1HUdvEO8P7YB9MbHqk7aWgtiaRfj03H0aAvsGN8AqWjfuI6yqaOWqXTbuZ53hfPug8u3DNxfM44Gsx4bQwEu+kDZVkXTNtsLUq4dPSwL5hjmAwJgc96MvD2BrRamrZQTZX39fPjm31BgceO8PXGtsQRiCcQS2PQkkOju7q7UOk64md5NX3fddSFdFCmhSCUF6wYwI1cpE/hee+0V0jyRZ/Shhx4KwIscnWQbIPUTuUvvvffesOsH2PEbgMZxJnBylwLCYPSOP/74sFiwCBLxn/IAltRDzs9jjjkmJIoHaE6ePDlcx72kjLr55psDKHS+UvKPAjxZDGg3YJbrqe+AAw4IrCHsJgwi58ksQCYFM5ab3uPaeC3alAEgYxFgARgiAwYgkHHFcYADmwoDQKtVo5IEdHjMmKnmPH02+LETA++EQYhZOd/DOGSz4bRp0Rh9VpVGPWjNQFJONIBzbR3UB8vlevwdBVycpwwHSnYqvlr7P94lZGMTCs5HgVLUFs8evG6zw8ZEVc4GpsgVcI0MmAt458giQpuYD3j3eGd579i0AeyYP/imft57+o2pCe8wfWMOYdNHthPmHFLYAd45B0gkKwjzyfTp08Nxm3Xw7JAHm0azjxvvzYlrjiUQSyCWwKYpgboAkImVifyBBx4IjAOJ5ZlcySWKGpWFkYmbRZbFg7RNzl0KcMQ2b6eddgqgj3ykzk162mmnhYn85z//+eCCycQPwCM5PB9ygrKgkAOUReLKK6/UnnvuqUcffTQsDEcffXRYWFjU+HvOnDn68pe/HBaff/3XfxV1cD9qYNpw/fXXByaTBYwy3K5Pf/rTAVwCHrwYs7CxgMQq4OrA3VQBoG3p7FzAGEDdzzOeN29eADr77LPPYJw5QAygIJq6zIDKDDLjN2qPCkOHaQCbB4cpYTxGVa4AvGeffTaUe+SRRwaZ+d2w/OzBatVvNOwL4zSa1oxrYc5oE+CWD79tz0j93A9bTiq1qBOG7SHdRp4d9xnocd5MutXIDu/CdzRsi6etaL9rnVWQA2UCym655RadddZZofxLL700vM8AQ+YL3i9SxJ144onhvbzooouCnSAbuZNPPjkAxP/8z/8Mx9AG8F7yfe2114aNHGVNmTIlvN833HBDyAnMs2KTiW0y8rKdp+2GN81pN25VLIFYArEENr4E6qqAmVSZkJnQ2bGzy2ZSnjVrlg455JCwsMAOApief/75ALpYjLhu5syZYfFlggYMwoyweMDsnX766WFSZ8EAvAEOYQq4BhbFqmHutV0fzB/lkWuU61noAXIs1rA9gFDYSOoHsHINiw+LJCwBC8mXvvSl0FbAJGCWRfGmm24KfSIpPfXCkNBn2jFUuImN/9g+/BZsqgDQaj+eKc/utttuC+PAqkzYKNhmq0eRHM+c8QnbywdbUI9xgzKut0OTWWHGGEwx4AXGi7HKMT6MQTYwjLUDDzwwjEE7JTnoMW2KOojwm7IY9waclMc7xeaKvgH+AJVRe1jK5WNbN8qnXhwl2JzZ9hCZcI6xTdsAuhMmTHhXVg+rWm3HaBWqbfAoD9lGHUOiXsIGxoBE3nPnIKbdsPC007K+8cYbg8kG5hYPPvigzj777CATygOwM2dwD88DIMr8A2PP3MK7D1gE+MHcn3POOUH+v/3tb3XqqacGtbEBcm3Q6A//jYlrjCUQSyCWwKYtgbpOIIAtFgQWHdQzgD1UMahfmIxZqJjQUfcyebPgMhGz6M6dOzeAKFjARx55JOz+WYyw1wNssUDADKAKZkFgIWIxZPJnMaG+J554IoA6FhkWAdhBmAKuY6HlPAsCbQEgHn744WERoH0nnHBCYGlQ8aK+5trPfOYzARTefffdQcVEua+//npoP4CSdrHoAApY7B1qY9N+jBu+dZsqADTTZhbv9ttvD+AMEAJgANTwLBk7Blz2/maMYr/KuOU3GwZ73TJ2De4YFwBDxhll2oaVccpGCHDERoXyGed2BpkxY0Z4Nxh3sOiAHGxPGevUydgEZME+c5xrnnrqqXAdH47vv//+oUyO8S7BeGG6YLtAxjpl0SbaQxsYw7CRL730UmAHef8om3+cp8+AV94v2+9yPSDNTCb3ARp5N7nGGTvMDtrZw04cBoTUAcgD9P3Zn/1ZaK/tEdn4sWGjTAA27D/vIurfM888M/Thb//2b8M7uffee4dj9JM2I2faxDv5zDPPhH9f+cpXgpz+5V/+JbCKzC98bEtZGyZmw78lcQ2xBGIJxBL46EiggAwV/QAAIABJREFUbhgYJuqrrroqLHJMvrB+TMSo2piUWcD23XffsCih4rnmmmvChA344vvWW28NQI/FCTUN16CSBag5xAwLGCCMhYsFz0bzLASwBDCPsBvY6LF4slixsLCwAQZZqGD4AHWwBSxugEzUvSwwLFZeOCifNqOuoiyYBDuy0A97IdqbOfYkrA7mTRUAerG38wfjjTEG4GeDwAc7UsYJtmeMPTYxdvJBpchmAfAHgLDqmI0PY4DrACaMV2zRGLOMPTYP9913X3gXKBfgyDvBRgJ1JuOZujCJwOmINnGOetg0GXyxkQH0sWlizPI3gItxCXCiTVzLfQBAxjLvCfUBjv74xz8GFhEASX38TVtfeOGF8MywuaP/AGHGN/3yeAeYUhfvBhsx3ifOcfyUU04JvwFsvNsAaup2CBmrsX3MIZ9o08UXXxyuPe+884IMmTt452Bn6fthhx0W5gbeTdqIuQbHeIf5MF9cdtll4T2GDaQMzDeQMwAcOTAn8VwApsj2/PPPD8+Fd5qPgelHZyqOWxpLIJZALIEPVwJ1A0Hb8NvhIGwAzoIb4vip6slXLJXCZD6YraBSPZ7MNlR7VK5UPYzL5fDbxxKp5Dp77BAdNkR3AFqDErcLps5xx1hQWUxhI616WlsljvtG2yjbcdlcTwwAN20ACIiAVbYnLZsFPvwN0wRAA0wB3AAQgAnAE4AIUMi9bHJgmW33CfsH6wY4ZBwAejgPwAMsXnDBBUGFDJhiQ+FxR8xJNhYANMAewA/Agp0q48w2fYAuwCT2rN4k8Zv7qRMQA2vJ/TCTADJUt7Sb9wBQCNjhHcS2FhUtYJc20W5+Uz9jn38ASNhEyqANmF3QHwAUDCW2sAA9e9TCoAIcAWKAXNg4ZBf9OGSN2UqH37nkkktCHcwDgFKeAe8WAJP+fPWrXw0aBT52fAHsAUIBmmYY2cwBaD/72c+G62gzm0DkQ51sAukXZfHuowK2iQnX2zbyw51O49piCcQSiCXw0ZFAXQBIV2w4b+Nve0ZyPF8pDMYvAywFW6YB78lqQNasEpWqAXuiUgWBq/+WUpn0OqVl5sngzBdz3CEtYCdgXLzIsOizgLEQwjys6xPNJMJ1Dhvy0XmEH05LN1UG0M8MIABwAbBgJsB4gfmyTSjn2RAAouxlC1iEPQYAcV3Yp5TLAXgAulD9Wq0IywTocIxJABGAEOCCSQQACJUr9+IEAgOHmhJmDzAHY8g5fgPGuBYVMW3BXpVjAC4AGwAQJhuQiGoZULn77ruHczwHQBIADNYNuzlAMIwnIJc2AjpRt9JG7rH6FKDqEE5Tp04N7wv9o+1smmAZbaMLsKUO5IDcbFfpTZ5HnecCQDRtBhTjkU9fAaM4hKASpo2f//znQ11mVtmkAVoBcgBgnhvAjWPIDmCL3GgHzwowDHtr214AMDaHvO/I0qF/eKetdfhw3o64llgCsQRiCXz0JFAXAHqCd2BYM2QGe4VEaZA5c6wvh88IADCZCVKB54uCvyiQW5fYaoGHgZ+/7bQB68AiTZ02IHcctffyWGLGb2hpbcoAEFCDapEPYAeGDbCA+QBsEX8DTABSgAqABsAM4EW/AB8cD0x2sRiYNQAHmwdUiQBExhXgEQBCuCPKAPRQh+1gDcxgzAB63AuwgzXk4+DlgDkAIuAMhhsQCuhj/NJO1MD0iXsBQKh1aTvgExYMVs1By6kDoEvbrY5FlQrAZDNGW2DesIEEcNFW+gBIxAmLtnGMPtuMA8aUtlE2/TT7GI1r6FFioAVwA8gBSrHH429scmkv8oLRox6A5J//+Z8H2f7iF78IcwLqZjx82cT95Cc/Ce3k73PPPTfIBicPHLgcqgk7XlhDysXMg+fD8+QTjWE4lDfze5kL4mtjCcQSiCWwOUugLgCk8wZbZkiYtAdDWCSrsdICuCPvqKrMnFWqSiaC/GD9hgJ9UZXuUII2IAvq40hbokAQdgFmAeBn70+udSDgdT1AFozadsUg8N0S21QBIKDKTkq0GgDi/LQwQlYRc4wx4rhw3AMoAljBFJsZ9CYGsGPHBoAU5di+DPUy9QAyHU/PMfiiqel8jDHuTBfUC1DjXo8zpzSjHqdUQyVL/bTN/WGsOpVa1CPXDhmofXHCANw5sLqDLgO4KD8aUoYyKA+5OIwMwBGZAAY5VxssO7rxcvu9SeQ7mlvYYXAAYtHftI2+mbGzt6+z/aCW58M1nmf83GBFAaSARJh/gDaq39q6a0PVbM6TeNy3WAKxBGIJvB8J1AWAUfBHBQZig2CsNAD+BgAeVoFW8XJNPlVtVqWKAwdB3FC/h+qA64vWG73OqbG8oLKYeuHld71sADHYG96w2VQBoEGaVcCMgzDu8vkAMKI2qx7LqBjx6AUcYVNmwGH2CObLacnsFAQQchpCgKDDrbh8ynbWC7OR0VRz0dzE/AZwUo/HHyAItSmqWMAYmxmYPmz07HjhjZYBFO2BMYQlc7YPwBHOHvztcqLMnQNGY+9HeU7NZsALM8m9lOHg2LxDvH+WdfSdcQgZ2uLNlINJAzgdz5D+wmrCtMKc8pzcZns0O3SL84JznLqQl8Pk8O3QTM5xzDOKZkHxiDYwHd4Ij6+KJRBLIJbAx0sCdQFgPXEkSwkZ2yUrVaaPf3bt6E+XQxHV/6MLjiDBeoVHvE99aS0gZOFhgWDx8YLL4smixYLjxXNtVXlhGQpomtUcRjM3+0s2VQCI4M32eDNgpg8AxJhwfDsAF6CGv1E3wuqhIvX4oY+MHQdKBkA4z6+ZM4M2jzsAjeu1Z7FZK+53wGUAHvVwzv9qY0yyYaFNABtAqePpUb7vjQJcjlMujJ0ZPMCn20Y93gz5/UBeBnRmFO1ghdwch5C6+Zg5NdB1m6PgChlTj1MnGjjyzbOh7XaUoUyXZftbA1HOOQwP/ardnFk9j5yQj0M0UQ5t8GbP18VZQDb7aSnuYCyBWALrIYFhAcBa1i8KxiqptALwwykDfFeqDAJC2MCSSoPsn0Gg2cDwbY/gtXTCi0B0MYiCNS8CLMQOGM0xey/b43A4Mlobyzicezf3azZlAAhj5MwYgA0+gCB/AHR8ojl5+dvgzGpVjpnFAwxFAQWMIkCFcg1+ACIAQ8rnegdojjJTjvUHMIumJ+MaAIrBKu2mPoMiAzYzdFxnltGAy4ymASj3UmY0awdto10cI3wKNnkOywKYpCwDJtvMmcHjOrNwtaCN++wc5vtpD/fyDSBFNs4nzN+2UeQ4bUEmBq5ud9Ruj75TryMBWAZWS9MGt5Uyo6Yn0Swrm/u7GfcvlkAsgVgC70cCdQGgJ1UXXgvEelNVm7/A/MHYlStKDSC9JGFhCPsSUQHzV1lVfTEAMJVYdxgYx3nj+qHUtQ7jEjXgj8Yq84K6NuGsDdy6vhgUViW3qQJAqyU9TgAHgAHbjwFADMzoh9kugI3DlZhBpI+2zwNU8jfjyxk5XBfl4LCBKtgAxmUbGDEGAV+Mv9rgyXZaoS5n2jGoob3Ua3UqalE7ePg4bapl8YaygbOdHWALx5LwzlUqQfXt+HwGxdxv1Svn+Nshlgw8+XbqOGTiDCBRJtN10kfabvs+q5zNygF4uY8yaJ9laXtBs6TUyW+D6uBYlk4Pgm7KMMitlQFtrPf+v59JM74nlkAsgVgCm4ME6gLAvkRVjeXQGZV8UaX+onKpjCrlsrrTFZVQdeUa1FvIhwmfRbEhm1W5UFQukw2LajqRVKJSViqRCAs0qjQm7KZkldngXzAgb67u5G0YXxgwfGcR9OLI4ufFmnq9cLHQo0KDWeBaLxbUzzkYGBYTmA8bnFMOi1ZoYzo9yCLa65PFxUwFi5CZRRYusyLIxlkHDBjcH4NWGEr3GRYJGdn2ycyNWR/Kc1y1KKAxGLV9G9c4DtuGHoybKgDc0P3e2OXbfs+AyGA16jzFMYM2B2g2m8kYcUo3fpuF87iMgiR++50xm+f+1zJqdvKyCYUBM0DPjjBmSp2jl3s8hj32De54JxzTj3eV35TtjZ3zGlO21fFR+08DPcf/i9blPMgGvpRp4Mi3N5EO+o3dpcG21ez0xc+Ce1xPlAmN2nna+cYBqSmHucSq8FrnGs+v0VR+Zp397CmDf8jK10U3JbbjtHz8HDb2GI7rjyUQS2DTlEBdAEicPia/UqFYde4olQLDl1ZaeYDemBHq7e9TGW9gVZRuyAaVMN7AgJxEomq0nUpUwi4+39c/CKi8CCAaq3cGF6Zy1TvXEy8TPIuLGQzbV9Emq8TMDHhS59vej5TLeav4omo/MzQsDFbLMfnaWJ928DcTK2Uyqdu2zG206ssTeZTxsIqQMlhkqM9glPts72QnBAM7qwUNLqmLdts708zchzG0YgD4YUj53XUY6EXt2WptU82m2enFziJ+ZmYwKb36TlaZTX+bPR2qjlqg6THna3kPDGYYt/aQBkTZO98OInb+8GaOd8TvO+8UbTJbSx9pK2Uy3jnPfdj+UScMLN6/BkkARPpp2dA+zvFt0xDKoF1sLgFosI62e6RfdgbCSYgNm+cC2kHdtM3mBFY5Uyflsam0N7WvNSCMmgTQBj5Wy/MbORiI0y7u9wbQm0EchBzU3uDS7LNtnS072kgZlFvPBnrjjOq41lgCsQQ2BQnUBYDFfCE4dDQ25JRRUmVAUiZLdGipr1/FRFF9hXxQ5yYyVWPsbCYj7P+4N52rehuG3fNA1g8WhI6Vq8IkuLKna5AB9ETtSZEdrHe/UaDFb9sflfqr6am4zkygVVVmQ1gcbK9lmyxUYky6Tu1FW5io7SjgsCAGhZTt0Bu2N6NeyuCcbcZs60T7WLisvrOHIxO6PUs9QQMGuY5FhXucVsusDnIwq+lF0SE8PixD9xgAbvzXNWzEBsIWmTV3hhH+9njxNbxzzu5B6zluVs7Mncv0uPR4MsNXCyDMevu9NAiBEaMsO48YpDhOYNTukPeS94b3iLHPe2+wxXGDF9psG0W/P35f7fhhBo5rvZHjGsrkfapVeSMn2uZMKgavBpyeVww6oxtTq80BiASfjsaf9CbRYJq+OWQNQJByHCfSrGKUsYuaEgDg3N9oyj1k5MgG9MP947jbz+84/uHGf1fjFsQS+ChIoC4AhNVLlqWWXKP6e3rV3d2pRDqlMrZ/yaQSKSmVzYSMHkyeXR2dyiSSyiZSSsMylBLqzferWC4p19ykVDobJvWgQhk9RuVMdeJ2GIeQXK5cDhNgMPIuVgGTjb3NgnnS8yJntTFAKbprth2S1SFWHdNW6gQIYoMU1NEDC4YXSSZz2saCQTmAQs6xaLFwOHiwr4val3khsyOB+2BGxQuF48nRr6iK2As7x71g2FDedmWUFWVoNuSAiwHghpTu8Mq2itbqWKtTzaLZZs8gzuyYbfWoxZsleyzbyYNzUecRgy/XVcsOmmWMjn2OMYZtosHfAC2bLFBmlK30exYFUg7BY6Dm2IW0jXfd6fDcVtpvda4zjHisWo1twEf9vPNcxzuE9zTt899WR1tVblUq7bQzC2VYJo5t6OsNZKkvyrpSF3MGz8vqbeTGcWsyvPG0YxJzi+Xmuctqbeo3cI7aOBrUO+Wg2z+80RVfFUsglsDHTQJ1AWAvXrzFkpobcioXC0o1NqiUTmhJ7yqlG3OqqBzUwWWVRMi/ZjUoWS6q0tMfgGNrukHKNaiiit4hvlpIxZVQT1e3RhJyo6+622XSwmawIVO1N9SAvVAuVQ0iy2QemLdUSoV8ftAep5isqok96TOBAujYoVvlai9RL1q200NtwzHq9o7ak709Kw1OuZZybNPjMmwAH52UmXhtb8S31VFmKczUOL6Z1boefLbzog0sGLQtujDapgk2wEzBhh64MQDc0BIeunybBUQXf19p9svjxeCBsWKgYVbZ9zCmbM7AWDfDZJs312fwZCaQ+81quyyz41aLAqaiTjkGJJznHUP9yjftNJtuEGiTCANSv1dsFFHNGmhStzdktNFqVs5bxWw1t20jfZ03eTYFqbUbRGXNHMM7ZbMNOwtRFtebESQQtTUBDgXE/fTZDjfWNth0pFYbQF+itoD87dSVlMk8Rt9pN3KgXINNZGB1uFXPtg+kHJ4r/6Le8BtnBMe1xhKIJbCpSqAuAEw2Nynf26NKqaz+cl5qzeqPrzyry2+7TsvzPcr396ohmVZjKqMdxk/QF0/9jLZvH68cdoJlqXt5h1INWb3T1aVZj83RHnvvowkTtlEmmVLnqi6V1Bd2y2NGjQ7ArrerOzBreAcHdVNfJUy0nkwBUw76zOTfPmrk4KTJokd6K2K8kb+UCZLAukzc5HW1OosFATUO13si9WTsSd4qKSZe8qFivM0iwMJAKi9SWtk2j2so2wwiddKOQw45JBzjn+2TrNLimBc1yrT3ZNS5xKCSid/2TGZRvBjHKuBN9dX64Ntl+zYzbQAOxhp/846QD9g2qqRGM4tuUwXG4CuvvBLG/OTJk8P5KEtowOaxGlX/GoBa9UzvoraIjgFILmJyADv121/91V8FEEKaODKUMJa5llR75BumLYSmufrqq8P7SB5h2sSHDQ65na+66qqwofvWt74V+kfga+I38k4S0Jv3j7K80eJ6yiSdHG2E6SOdHKnpeHcvvPDCIK/ose985zsBTHLcjld8/+hHPwqp9qZNm6ZPfepToV7ad+utt4Z/gG3azDnmm5tuuinMDaTQ43PvvfcGVfEnP/nJ8I6TqpBg3Twzcicz3xx11FGh75xDDpw77LDDQu5k2vTrX/9ac+fODXmbzzzzzDCf0F6yvlDn1772tVCf5yc2mLHt3wf//sUlxhLY3CRQFwASxYVJP5kkq0dZlVyjfvfknfo///FjrVCfWlublSpVlOkramLbWP3zX1+oHVrHKNtdUCZfVvPo0eovlfT2ymX63e9v0SFHHqlx47bQmNzIAChfePPFkLf0sIMPCQ4iTz3xhPbYbXdtt8W26u3vVlOmWRk8hbEpVNU+KDh0JNPVY51VG0KDKfKDwgCQ35WJmpRa/GZB5G8zZiwi5HEl1Ra5Ubmf+7xjNsvAA/+3f/u3kG/06KOPDouXPYHtgcuiZDaBRYPFDQDIBO4YZnYoYXFl4WbCdwgOq4lpnw3ArTK2zR+TOvex6HHOYUgc925DD8yYAdzQEq5fvtX9jDHGLzZmgALGEWMHpwjGJIADWzyrGf3OMG4BHQArAE0UJJid8gaI1pgh5DqDQzOC1TmhGsLJrDjfl1xyiSZOnBg2YIA2Nlq8H7fccotOO+204MhA+6+88sqQMxigBoDjPsr88pe/HJwzaCeAiN8HHHBAAI5cN2fOnHAd4I53geuYPwBRgCEymQCieKfOP//8AJYAifSLjePs2bMHPZ1xrDjiiCMCyKQe0t95s2VNAmVxDe8bOZm/8IUvBFCH/D/72c+GtgBued/JXXzjjTcGmdA3ygBwk2+Z/MXMKeRZBoQil5kzZwZACYDkw3ODWTSw/9znPhfu5ZlOnTo1yIM+Mp+RG/nrX/962Pwih6itJHMU/6LBt+uPrviKWAKxBD5uEqgLANOFqt1QoVxQZ7lPlfZmXT77Vn330n9T84RxQYWbU1LJzj6NTzTq0h/8RFulmtXcU1KT0urNJNWnkrrKJf3qyiu0/c6TtGzZCu2562Rtu/U2uu4Pv9c9d92tz3/2c8Gr+IbrrteMo47W4YceFibEnmVdgdFj4TjooIPC4sGkyGTK5DmmlAk7ahYmwBWT8Y477qgddtgh7JpZNEhrxW9YCSb6Y489NkycP/7xj7XtttuGyZmyYOJYQNjxU9706dO1/fbb6/rrrw9l0gbuYwJn8YBpYBFk989Cw+TPRGy1DQsH9bMIMLHDUsC8WJ3EtQ888EBI5wUIZdFgoWFxZEE++eSTw2T/zDPPhHpZ7GgHCz11sXiyCH0YnxgAfhhSfncdBm+cMfvG4v7GG2+E9wIgB7ABANojl2sBMuQGBkxwHPUrY5mxBHPEOGJswsqx6eE3QIqNxqRJk8I7P3/+/EGGnLFfayNLe2wbRz3kVp43b57OOOOMNfILk6+X8QpT5Q/vDiAOoAYQYkNEu3iXYc4BWwA3GC87knAvrBvA9KSTTgp9pmzeGVg33m3AGcCHfvLeOy+y3zk2hIA12o48SAX42GOPBZb/K1/5yhqOHby3HIeNA1DDGDJfUMYpp5wS7ufDvPHLX/4y9JtNJfKcMWPGYKiWiy66KMwvzGfIi/ecPtHf1157LfSFc9ZucO6//uu/dMEFF+iKK64I894uu+yim2++OWxgkdE//dM/hfqZy5hTrCUxUHcGlw9LQ7Bx3o641lgCsQTWRwJ1AWBvql9Nqcagii0Uy0qPatWls6/X//rF99U4cbS6kwk1ZhtV7ujRFoWcrvz+z7S1WpTpK6u1uUWdybI6OjvVXy7q17+9Utjsjd96K73wysv6/JlnaNaTj+mVF1/S1H33w01Rj856RPvss1eYaG+65ffasmmktt9pR7348kuadsghYbJ/5fmXtcPE7XXg/gdowRMvKJdt0EEHTA0LHovYIYcdqlQmo4dmPaxtt9gqgEEWSxYaFgaAGSDtmmuuCfWwyLDwwVhQBouIcwqfffbZ+v3vfx9UyIBOJnsWlTvuuCOAOhZLducwCy+++GJgP1ggWDRgQZj8+Qa4UuZ5550XJnD+BmjeddddYTGmHBZpyrVRPwvd6aefHkAiAJZ+sCix0NnLEPAJCHAoCBYAFnx7adojdH0GicGH1YD2GKWdABHAsA3jabM9NB1fDWAOIKGPsFYAWIfRWN92fRzut10efbXaFUaNDQXADOYN0ME5GDLkz+aAjRIbCzsXMU4AP7wDjDnAEwwi446NE+8C5xlflMt7wFhl7AM2eMb2JLa3rL1XYaIBKGxYGO8hpmdjY3g8gEJUqWxiKM8sN2pM2soGCzDIuw3gOuuss8ImiLYBnOywwjuKGhf2C8DI+8QHhhBwyTgDpPHu8X7RT4AaYNaOGbB9tIFN16GHHhrkhHyuu+46nXPOOYMxBhnHzBkAV1hJ2sXmEDCG2p1yAZrUgwwAppQHcMWEBQDIXOH3FIYTmbKp5JnQft5r7uUYbCH1/e53vwvPEVDIXHLZZZeF87w7zBeAcjZ99IO5jueFehlNRm1swai6/uPwnsR9jCUQS+C9SaAuACynSsoorUpe6iuXlRzZol/PuUn/99Ifqzgqq2RDS4gR2LesQzu2jNMlF/5YO2W3ULG/RypVg0gTJqZQKevq636n3fbZSztN2kWXXfkbTT/ySPUU83ps9qM69zNf1ptLX9c999yjsz9/tvLq1y8vuVgnHXaMdthhJ91x351a2dkZJtF5L83TQdMO1LZbTdDW2XYV+vMqF4ua+/QzIVzNAdOm6q23F+rhRx7R/vtMCRMpAI1JngmUXf+3v/1t/exnPwssG4ubFzIWQtrATh6gwiLFosMiyMTMhMtvFpCvfvWrAWyhzmHBYtJm0QLUAXTYtTPJf/rTnw4LNIsJO3Y8lVlgWGApi/awgAIcAVQsOJyHJbFXM/WwSLJAAJ5OPfXUYBtEG1lsHIjXj99G/O9tOKz96pgB/KAk+f7KscevnSwYY2xY2IxwzkwcbBZACFaIsYdtGOwV4AEgZScL7jMzDUgBuGAmYdMCxiWABMBjZwfqjjpWuCeMVTYgABJYO0wfQkSArq6wsWKDAJMFc05baCvvD4ALUEVfMK9gQ8X7xyYJM4r/196ZB1tVXWn8u+dO79038RgUGQREZoTGiBjERlGj4ARpK7b+Yce2K5rq/ied6uqqdP+RTpXpVHdSpXZHk5hEUUGNUVEjoHEiUaPtBDgxOIAIyhMQeNMdz+n67csixxf0IjzkKfta+N47w977fHcP3/nW2mtRLn3bXDzo9xAfCBjmWogrf0PiuB+SxljlpQlljfF09dVXuzEC2cWESrsgTD/72c/cixs4oFyCD8ST50NZRFVlE8aPfvQj10YI2JVXXukIK+MWUzDXMT75MFYhv6j0zDeMafPnRV296aabHL5gY+GuaC/+icxBfGdcj/8kRBl8GO+4tFDW5MmTtXz5ckd6GfvmowzJB1/KAGtT/Cw7yoH1Nn+XR8AjcCQgUJMAJlOkdiOsc9qRtY6cdM8Lj+q/7rpRW0s7lYzqVC6FigolTTt2nH5w9T/rxKHjpFLFTWpNqcBlCvmoY7cW3XWn5l5wnkYNH6v/ufl/9ZXpJ6n/wKP0yPLluuLvLtfuXbv0u6W/0/wFC5Ssy+jmW2/WhWfP05ghY7Tk0QfUVcjrwvPma/v2D7X49kWaNOkEnTn5JGVTae3avkNbN7+/V0lbuXqVI0UsYCx0OGdDxFD4WCSZ7JlQZ8+e7SZxVAmUACZyTDkoVphZvvOd7zjixoIIiWOBwx+QxYQFgWOQNogZiwLqybJlyxwBZFGirjlz5riFBTXQzGMsHhBSFkXzAWLxYuEyUxlv/7TDfKwgmDfccIM7xkJy3XXXOdWG+02ds4CyLJr83lsmIE8AD890YOqfBTu3XbsofZAuVD2UNn6HtLAJAwKBcgY5ov/TX/gdZYoPLzuoVZAM+jPKMmQHFcliyEGY8FczFwMLaxQPoG5BkOnzfFDc6NP43kFO6ZOUQ30QFcqy+JqofrSBOiCAXGuq4re//W3Xb1EUeckykkUbeTHj3Ny5c/eGk4KgofpBeBnTXAdRYmxwPy9iKHSQT9RJlDvqhCRBpigT8olpNr6r3qwBkGlIJuVR9s033+zKmTdvnps3GOPgztimXBQ+6rQA74wd5hCIH+3mAzmmjaijmKotFI35ARtB5YWT7weiCh60HRO57aRmrjFSa98thN1nATk849XX6hH4IiFQkwCWw5KL5VefzSpKJFWuD7Rm2wY9vuoZbSvuVko5JYj3F0Ua2W+wZo7/Kx3bMlDlYgWnJYWFLmVQAT7argeXLdWJM6ZrxHGj9Nv77tXHxdi6AAAcDUlEQVSESROVq6vXE48+pmHDhmnipPF6YsUKpXNZnTj9JD30yHI1ZXJu0kNlu2jB151CsX7tm27yZpEZ2dDf7UKOSmX1a2zWxPHj3RvxG2vXOjI4eMgxbnK/5ppr3ALAQgmBgwxCCiFcTNyYh1nI8GXizZwJlvIxD7OoYDJjkkZBxOTERM/iyWQPwUMt4T4LHzN16lRnauKNnrdzVAhMzqgAlMECwWKKyYqFGSLIMZRKykUNYOLHcR7fRdpOmUuWLHFYoeywIKA2YArkw0JjRMHMtKYcHWyn9ATwYBE8sPuNAJpCbQSfPk0/4h8vIfQX+oWZeyEs9Gn6neUkRvHiZQVFihcfyIeNI9tcQZ83tRuXBlQtlDfL2hHf+WshXKyNtAklDTWR8UqfNl89lCzUb4uliasDYwqljxcY2s4zMCbZYIHyxz2MBUgXPn4QRkgT5IbfGTfcz5iGpEGyOM444cM51DraAWmDaPF8qPTMARwDV4gdY5j5wkJO8Ww8A6QScy/tNvUVdwzGPy+WlEe7IeJsROElD9xoG+XyzIxdXu5MxadOyoX8GvEGJ8MO9xSwYd5AVWUjCnVYecyBkFbIId8vJJZ5xVw/zN/xwHqcv8sj4BE4UhCoSQAryUhRKVQySihRCZVOJ5WqT6sryitIJ1UMieKfVqjQbQZJd1eULFXjiykVaHepQ00tzc4MvOn9LWpqbVE6m3Um2n4D+iuXrneLWFvbB84ks3HLu3pn00YdN2Gslj28XKOPHaW2D7bqnHPmuoVo3brqhgrI2rQTT5I+3KkPt7apu71DQ4462pEuFpAP2tp0/NgxThVBEUElQJVjMobIsbChTqBMMEmzsHAOPxoWThZQSCCLAuUx0aIo8ubOAgLJxDeQiR6lD1WCSZ6yeQNHgYHgQSgpi0XLMgNYbDOILfXzNxM4Zi1UBzNVQTQheCw4LIAsDBBWC4NjO5fNrGZhP0z9Ay/bqXywHdoTwINF8ODut8wa8VAw9BtMiBxjPPCPfki/pT9DxDCT0gd4mcB9go0H9Ev6M9fQP+ljHIfwoCZBakxhQj00Jcs2WvEk8R3EFsPPso7wwgI5IiwNyhz9lDFOnaawQURR9hjLjCd7Pv6mLZA12g2J417aju8u8wrXMKbspcfy4nIcksQYZVyCDwSLf4w9jkEWUdAgnIwrSBd18UJl8QzBh/bwoQzGF2MVUkhdPAPtt01mkEcIGx/mGczAYAkpZR6gXuoHB9rHB1M3dYM1+NIGSDjlgxNzAd8F53jR4zhtBCszZ9tGOOq2YPG03XKWWyzFg+t5/m6PgEfgy4pATQIYpgKXAzgoh1KhJFXKSiekTCrN/5QvVBSmA0WJhMsAQjiYRIj7X0WVZELlbNIphJUoVKYuq0KlrEK55AJClyplBaSwCjLa1bFLTY0NxNlXQUW9v6NNd9/zW51/7vkaO/x4dRU7VZeuUzqRVnuxWyUS1yekIfWNLvtIoTuvuj1ZAbryeRXLJdXl6l38Qt7eWbAs/Aq/M6GinEBsbAGzHby8odsGBn63oLosECxaTLCWOs42PZhju2VdYKGy3KEu5d3Ona5MzlvgagsAbaE1+Ek5FsGfuq0uI3IWVsYcvi0+oPn8mNkXwsnCw8/e+HgC2BsoHlgZ9BeLOcnv1udMgTN10NwAIIEoZ/i28XLTM9c13yXkAYJgmSh6tiyevYNzpiTbBiXrq5yzTT+WqcRy0lr77FoLYG7hbDjOeLDMFZRlfR8l0wIsxwkndTCOLLizZQKBFFkYHDMlm1rKyxpEGFwoFwJnpNGILHPDbbfd5vwGuQ/yygYrfO8ssHQ8xVpPksX4hGhaKjwzwVoweptPTGE0cmaqYhx/U1QZ02Bkoaks/qP5BfM35dJ2y2Ns7iK02TKTHFiv83d5BDwCX3YEahJAW1wIzJxJBcIbsJwvKEl8QPKLBkkVk1IYSFGpUs38US65VHGlRKR0shr1n1RwKH+FSkkhE31jgyMnuzra1a+p2RE1lyWgLq1cY5Pe27pJK55+SrNOnql+zS2uztbmVqeQTZg4SV3lkjo6O5UrV3cb26YHiGYxrLi8xAoSSpQqbuJnMkUhsWCvFlnfJkoWBVNAmGDNgZ02sigxsTIZc44Jlw/lMenbQmULGcchfEYY7a2c62xnIG1ygaz79XNl2wLPcSNxlrHA5U7evXtvbC8m/rhDPgsG7YQcctzS11loiN7oxJ4A9gaKn72MOKmLEydKstRv/G7E0HZ+ou6hOrHBCUJmqdPiBAslmQ/n7KXEYlLauOe4kT8LMWKE0MiNEbx4zm0jhvy0DQ729HFiSD3033j8TVMwzbeQPs019vLDGOI57PkZMxYqxoiduWJw3DJzWAxPi9lJu6nfdrAzfuObqSCUfDC5ogByH+MdjI1wcZ6xbiknbROJC521ZyxakOw4fqY28kwcpw4wsHmAe+JZPIzYxU3w9t3xPZh/p23isXb4vMCffcz5OzwCRwoCNQlgphIoSAfqLOSVSCdcHuASBDARKJlMO5xKqYSUkro7u9Qvm3M5g5UJFKWTauiuEppsrt6RQJTCZLb61l8OQ6VzOSXCilPwcnV1yue7VEnITbgdBIJO1bmsIdu3bteA1lYlkxlt/bDNmY+LmKTz+b1J5WlLtjGnbnYdQz5zdQrbq2qdqRNM3ky+mJQsSK2FiWDCtQXBVBZb7Ez5MCLJT8o0lY8FwBzlbUFm4WFBscwGLLhM0Cw4TNgsVtRjPlwQQv6mPSw2pt7FlUkWQiOJlsnB1AULl2Fmb1vYeqMzewLYGyh+9jLi8dzoR5aKzH43c6ftAuZ7MvUO0sD19BMjklxHv7CXBFOiTMXjfgsszXVGlmwcGCE0Fc6eiPFgYyBueqReU6fNNcGCtsdVRrvGCFxPYsXf8ec3YmmkmDrjRNHOmzkXHEztM0zjL0s8K2TM2mSBleOBsU11NxUyHjzeCBfPaM9vL2pGwix/L38bwTYSR9ssk4k9C3WbL298U4ephpRhAZ8xf1uquJ4k8bP3On+HR8AjcCQgUJMA1kWBKmQBUVVZQ7/DJAwpY8LKlQKVFbksIWEmqUpaLi9wdRKSgq6Se8PNZXLq6uh2m0VydQ0q5ksKMC1z3x6ko4TEP0eg9hzLVkJ3XfVTbUslEbh4gpiAMyEZiK2AapaQRCLaq4jYQmWhJCwunu1mZDK3xc0WSa61bB92fXwzRfx3I192zMqgTFvwbOejmZJMCbBsH3avZQZh4aDevjSRewLYt6eDuFLHd9Vbu7/79lMfWOtsjO7P+Opp+jaF9cBq9nd5BDwCHoG+g0BNAhjmiwpSSWdS5R8Li5sUyxUX/w+/vGKlrHxYVDlRUSIZOAJWcUnnCxrUf6B27WxXKpkU/2FCTkAqiyU1NjarVO7+GOGLk0BOpCtVKggJDBP4Gn6cACYKoWsTiiTOh9UJO9wbTNlMQT0XRFPH4uftGvO5goTty5E6TgDNdBZfGOILTFyx4bipAea7Z+2gLvPzs9Rbpm70he7iCWBf+BY+uQ2eAO7/9xMfn/tzl6n/PX/uz73+Go+AR8Aj0FcRqEkAIWxOkYqqpKpK4CIX5w+/PaWTbrMFGz0KpaqvXypIKs3mEVJFRZG6Ozod8cEf0BG1ipxptC6bdWph/BNXAzmeIhnx3k+VAJYTpgRKmVJV7XCTc6LqIB9FVSWwpzoXd2A3nylzhDeyZQ7kpgqaQmiK3r6+SCvXzsXrMbJo4VnAAbJnvj7gYAFcza/PTD92T1/oPJ4A9oVv4ZPb0FN57tkn+3brP9/WxbGKv8zVGtte/ft8vydfm0fAI3BoEahJAN0EGVZtsMk9BDCF/1+iSrryHZ3K1Gfchgv8BNl9i1IIkYIMBkHK7RjGP6izvcOV09TYqEI+7zJ4JNPVhPL7+mDiRfnD5Fz9BM4/ECWwlJQ43KjqTsaqOSdSKMy/VbOukcC4g7ypfJAw80Xi3n0pKGbC7Uns4mTQyqMMI4tGIjlmvj5WlpmiOYfPke04NlO1tXVfdR7arvDppXsCeDjRr113TyLjCeAnYxZ316iF7Ce93NW6z5/3CHgEPAJ9HYGaBNDt4EtWCR1UDbNv3D+t0W0EiVxol3IUKshllazPKh+W1dmVVy4MnPM1/9hBF5YrH9tBVwlLf4HRn33+ep4KnG9gmZyke3wA+1Uyyhf37GJMVVW/SlTe6z9nhMr8fcz3z1QACxVhzu6mztn1cdNxfDGw37k+fi/3mW+fEUILfhvfcWjn2M1ouzB7Kn+2q7cvdCJPAPvCt+Db0FsI9PTt661yfTkeAY+AR+CLgkBNAuhMtrEwE5iCI/z8Mikl0ylFXZ1V/74g7YghCmAxjFwYmAQ76ypJp4xBdCJV9oSbqObbhOCUy8W9WO3Z/xFT/DgVfHwTyB4fQCOA/csZR6Agn+lsSkoGjgA69S+dUrlYzV/aU1H7s2rIppFqzXadxeuDkLHLsue98S/XTLmOeFaq5mxT+eLhH2wnMLt/KdeFxtmTqs1+WsBdSwfVl0xOngB+UYa0b2ccgU8iep4A+n7iEfAIHOkI1CSAmH/NPy6RTkrZtCqpQHmFKkRl5RMlsQ+3UWk1KqugXFa5q6hUVI1i314uqVDorm5+CCJHfpLJaiiWIKmP7VZ0Aaf3cLU/k8EqAXWbQPTxTSD4Aw4oZ1xQaEfi0gmVCClTyjvyZ2ZnyJntsGXiN/OvmWEhYLYZxMy3phzuazdlT3MbZVssPtoKsbVjpihagGd8Dnl2/PssPI3FMzM1Eqz43fwD+0In9QSwL3wLvg2fFYFPI3qm8n/WMv31HgGPgEfgy4BATQKYUqIaQywZuH+7i91668MtWv3mWr2zeZNKwxrUsXWHmsqBTh41UTPHTtXw5gEKCpHCQlH5gU1O7erq7tDWbVsdKRs+fKhTA11MMqL670Ey7u9nRDCI/pIAOhPwnpAxLfnqedTGMAi1u6tTHV3tSmczLpbglvc2O0IG2YqntDLFz0zBEDbIGUSH4LGkoyKDwMyZMz+mDsaVQn435Q+lkLhrEEaCOxOLz2L5WcBcnp2yCWZNvaTnoo3USUxC0nphKictHO2lzL4SzsMTwC/DcD/ynsErfUfed+6f2CPgEdg/BGoSQII5R6WyMkFK7YVupVsbdM/zy/UvP/mBotaMdqaLSjQ3K+iuaEr6KN38j/+hqS3DlS9FKjfUSaVIhVSoYjbUf/73D3XVlX/vfADDIKlMokHvf7Ber695Q7NPP0OFclGP/f5R/fWsWTqqqb/CqKKUsiIUTWOmTh+171Y5lVBzY6vCsOiyfNQl652Zt9DdrZbW/nr5xRe04d13dfbcc5y/4NMr/uCCPpNr03LxQsIwSUOuLMAqJNWZqfds3ICQQRwhZCR+J9n78OHDnSqHOogpF4IGAeQn15FcniT2JJqHTFrOYvOZtGC7+EJaQGmuoyyuJen79OnTNW3aNEceqcsIpoWbMfWS1FYcM3JpZmz+5rhtKrF0cyiO/OMZuQayyu/7G2rmYAkg3zl5lfkOyONq6cJq7cLcv27sr/IIeAQ8Ah4Bj4BH4LMgUJMAdpWLLsdvLpVRvlxSqqVBd698VP/602sUHNWk3Y2RI3PF7e2amBige793vcY0DVa5s6jOgHAveTUN6q83t23Qb+65S+ecOcdlwBgxZozIM/z8iie1+rVXdcacOS7e4LJly3TStBN10VkX6O1316sp16ptm7Zo8KCjNHzYsWrr3Kk1b65Tpaug0cNHqCHVoObGRtVlstqxbbteeeUVl+h9/KSJemnVSrVted8lpH/rrbec0kYKOBK0o7aRlQNSN2TIEJdgHXICmSMPKGohJOr111/XHXfcodmzZ7u8qpDGd955xyl2KHjk/oREUe5DDz3kksJDKknyPmPGDFcP5yB4XEtuUeqA+ECKtmzZ4pLCk3aKpPcke6dN1E85kEDKsvygPAPEbcmSJe5+1EISzL/88ssuRd3QoUMd0YJg8jf/UDIhwLQDfEg0T1ts88n+dJgDJYCUDfEkS8G4ceOcssqzWkqyeHqs/WmHv8Yj4BHwCHgEPAIegYNHoCYBLKeleqUVFMrqKHQrNahJC59bqu/96ifKjhyobWGnUuk6Be1FjSrk9Jt/v1bHZ1ol4vNl67Rp24dqHXqU89/74U+vUTKq5vwdcPRgnbfgIj294kk9+eSTmn3WHEekXnjhBUdgTjl5hp5a8QfVB3Va/8prGjF0uP72sku1YesWLVu+XMePHKVpEybr/bc3a/jQYTrlxOna/O67enPdep0wdYpTmFauXuU2lKCq3XjjjU71glBBPji/du1ap9ZdccUVTuV7/PHHNWnSJM2fP98RP65FrXvsscccYfrGN77hjkHYyP952mmnOWIFAYNk/frXv3bfCKSL8ikHpQ6y9tJLL2nq1Knu2PPPP+9IIETsgQce0Lp16xzJQw2cM2eOMxFT39lnn+3atHHjRnef5R+95ZZbXNJ6UshddtlljuyBG3VwjCT2PDPXoWzy3Kh+EDDuGzlypL71rW/t8ceMZVL5lP50oATQfCDJ6wqxNgUQczgf2zhz8F3Zl+AR8Ah4BDwCHgGPwP4iUJMA5hNlNaXqlcyXXVw/tTZq4f8t1b/deq3CwU1qTxRUn6lXpjvU8FKdFn7vxxqmJmXDpDJ1WVWSKb3Xvl2FTKjbFi/UWafP1nGjjtMtixZpyoyT1Jyr14svvaT5539db25+yxGv2XPOcCrhgw8+qNNPmqVZE2bo/mX3uJ2+3ZWSNm/9QP/0D1epSXXqau9Qx0e71NLYpNdWrlZTfU5f/epXHbl7e8M7mjXzVEfWfvnLXzrCNWXKFF1//fWOJEHcFi1a5JS9J554wvknzp0715ko77rrLmcCxqS7cOFC9xPl6rvf/a47jkK3fv16XXvttU5BhKRBACFt1P/UU0+585BGiNmf/vQnvfHGG454oYahSEL4UMQ4BulDabz44ou1Zs0ap1bSXpQ9ztFeyCTtBKNLL710bwDsG264wSmU+Ctu2rRJ9957r+bNm+eeAZWS57vuuuu0YMECVze40kbIa3yX86d1moMhgJA86p0wYYJ7XsgtGKKC7ivTyv52Xn+dR8Aj4BHwCHgEPAIHhkBNAri71KWWTIMyFcKbJFVqymjhivv1/Vv/RztSRUV1gcJiWfWFhMa2HKNfff/HGt08WKlC5Hzztu5qV/3AZr236wM9/PtlOu/cczRk0BDdcuftGj9tCrFXtGHDBl107nxtbW/Tg0sf0vyL/8ZtDll052LNP3ueWrI5vb1mnTZs3KizzjtXH7S16e7bF+uCs8/VzGmnuKwkpXxBzz/zrCaPn6DRo0c7wlUoFTVl8glOOUMBPP30052JFKJ1xhlnOLVs8eLFuvzyyx3hhKBBzLgOP0GII2bc++67TxdddJFTA3/xi184kmY7iFEQUQUhZxBFfAVR+qgfVQ5zLoonbXjmmWc0a9Yspw6uXr1ao0aNcmZo1D3MpHfeeafz/8MUDBk9//zztXz5cqfwXXjhhe5aiC2E8qqrrnJKJqbohx9+WCeccIIzL6M+3nTTTTrzzDPds3AcdRHSh1qIyXnFihWOMOJ7aKbYWt3nQAmgBcBmYwzmbb5rsOqZgaVW/f68R8Aj4BHwCHgEPAK9h0BNAlgOQmUIAV2OhAAY1if1zJurteTp32tXlFc5GSoqlpULUxrWPEiXL7hEA+tblC6FbgNIU2OL8slIm3Zs0b33/1anzJiuMaOP16K779bU6V9Rv+YWR06GjzhWM089VbctXqSho0bolNNO1W13LNbxQ0eota5BH2390Pm7jTthkl5fu0bvvL5WzXU5Hd04UIMGDFRrY7PLSjJ+7DhH/J599llnCuYcpAeid8kll7gNIbfeeqsjRpgkb7/9dqf6oVKh2K1cudKphJijMZ9CmlD5IGQXXHCBli5d6kK0QLZoD8QNMy3kBtMupMoCP0PwXnzxRWd+5XoIJmZYFEbqwhT7yCOPOJLJB+XOiB0bSiCLKICUB6GDuEFEaTPECsUPsglBhcCiaOLLyM5izNOohTwLJPTnP/+5UwDxYcTPkmehzYdaAeS5wAsCzLOjUKIAoqBCotk84z8eAY+AR8Aj4BHwCHy+CNQkgFEqVFSOlIgCRQqUztUJUrirsFtBJukyciTDsJqyLUyoPp1TuVRSUAqVTaQV7S5pR6FdDYNb9PKqFzV08NHOlLriqad1zMhjNXL4CEdIUMnOOudrWvXaK3p/xzaNmzpZ9z5wv0YcM1SFne2aMHqMM1m+/Noreu655zTx+LGaNn6SGoKctrW1addHO3XM0YOdTx9+Zhs3b9LkKVM0qLW/IzkobihP1L1q1SpH3iB1mFchZJA4NkhAiszXDyWO6/DRo85vfvObThXEtw4ixbX47LEpBPKFugWBhHBCLiFrkD/Mz6iEtA3fPMyftAlC9Mc//tEROggi5zCTQvjwG0Qx5BrUSlQ8zMyUwYYS1EZUNJRBNrVQBr6JbHhBUUStpD1sSsH8ChnEPA2p5VlRAE3F3J8ud6AKoKXIMxMw2GAyd3Eh/ccj4BHwCHgEPAIegcOCQE0CqKCiKEooSGVULJbdztH6bFqZNEl5Q2cCZkduoiKXAq69WFAUJJWqSOkooeZKTqpPalvXDjW3NimZiNxu3daBR6utfYeicqhBrYMUKVJHvlP1dY3aFXVpR2e7Hli+VF8740wdO2CwUlGk9o4OZRtzSiaSKufzagjSSpaTLj1dx+7damlqVra+Tm3btikMpFxjo4pd3U71g3BBrCBkkLf45gNIisUBdAGqg2qeY1OnUPkgbdwLgSLGn4V+sZAr4BLf2cpxrsHMDPmjbsqkfD6QN0gv5cZ/pw6uQUm0+rnfsoXQbtrHee43E6sFn+Z+ex77nfogXBanEIWQazAf22aMWr3vQAkgu3zBDxOwKYB+F3AttP15j4BHwCPgEfAIHFoEahPAQ1u/21gCcYLYQHQgQxAf1LdXX31VJ598siMvEBwjO3HChp/hp318nLne+QIPlADGFUCLA2gEMJ5buXda6UvxCHgEPAIeAY+AR2B/EDjsBJB0bhABFDNIoBFAyCAbGlDbIIQodHxQwkwFQwkL9uYR2ffjegK4P92g9jWeANbGyF/hEfAIeAQ8Ah6BLwoCh50AkrPXslcYyTOzKqofJlfIB2QPMmc5eo3YpUgo/CkfTwB7pyt6Atg7OPpSPAIeAY+AR8Aj0BcQOOwEEBAsywUEEIUPfzp8xzAN4z9mSdshIWY2hAjuTzJ3TwB7p5t5Atg7OPpSPAIeAY+AR8Aj0BcQOOwEEPUP4oeZN04yDBzLo8vfnLcct6YG9gUQj4Q2eAJ4JHzL/hk9Ah4Bj4BH4EhB4LATwKgS7vXvQ+2DaOAHCMEzcsjvpuTFNw64Y0HiSPmuDutzegJ4WOH3lXsEPAIeAY+AR6BXETjsBJBNHJALyxlrKh/kzsKv2BPHiaApgmwi8Z9Dj4AngIceY1+DR8Aj4BHwCHgEPi8EDjsBDMvEGYzcJg/Mu/yM+/fxu/n+xQmg2wEcBNX8xP5zyBHwBPCQQ+wr8Ah4BDwCHgGPwOeGwP8DAEKU1G6kXEAAAAAASUVORK5CYII=