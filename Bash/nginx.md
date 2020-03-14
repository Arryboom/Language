#Proxy for ssl website  


###generate ssl keypair
```
$ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/certs/cert.key -out /etc/nginx/certs/cert.crt
```

generate a ssl keypair  

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