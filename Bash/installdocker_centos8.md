在RHEL 8/CentOS 8上安装Docker和Docker Compose的文章已经很多了, 不过都是要求禁用firewalld, 因为默认安装的 firewalld 会导致Docker容器DNS解析失败, 其实只需要修改一下 firewalld 的配置就可以正常工作的.

　　在CentOS 8上安装Docker CE的步骤如下:

　　1. 准备工作，CentOS 8的工作实例和具有sudo特权的用户

　　2. 添加必要的Docker存储库

　　我们将使用dnf config-manager实用程序添加Docker存储库。
```
$ sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
```
　　3. 直接安装Docker CE会失败, 因为其要求 containerd.io 版本比较高, 然后会CentOS 8默认安装冲突, 所以需要手动安装 containerd.io 。
```
$ sudo dnf install https://download.docker.com/linux/centos/7/x86_64/stable/Packages/containerd.io-1.2.10-3.2.el7.x86_64.rpm
```
　　4. 这时候就可以直接安装Docker CE了。
```
$ sudo dnf install docker-ce
```
　　5. 为了强制DNS解析在Docker容器中起作用，必须禁用firewalld。
```
$ sudo systemctl disable firewalld
```
　　6. 启用docker守护进程
```
$ sudo systemctl enable --now docker
```
　　7. 为了无需使用 sudo 来启动 docker 我们需要把当前用户加入 docker 组.
```
$ sudo usermod -aG docker $USER
```
　　以上就是CentOS 8上安装Docker CE的方法，不过这里面有个步骤就是要禁用firewalld，但是这个在生产环境下是绝对不可以的, 经查资料发现只需要开启伪装IP的功能就可以, 无需禁用firewalld。
```
$ sudo firewall-cmd --zone=public --add-masquerade --permanent
$ sudo firewall-cmd --reload
$ sudo systemctl restart docker```
　　正常安装未禁用firewalld下在Docker容器中无法解析域名.
```
$ docker run -it  --rm centos

[root@7ec3691c37d7 /]# ping 192.168.0.5
PING 192.168.0.5 (192.168.0.5) 56(84) bytes of data.
64 bytes from 192.168.0.5: icmp_seq=1 ttl=63 time=0.283 ms
^C
--- 192.168.0.5 ping statistics ---
1 packets transmitted, 1 received, 0% packet loss, time 0ms
rtt min/avg/max/mdev = 0.283/0.283/0.283/0.000 ms

[root@7ec3691c37d7 /]# ping www.baidu.com
ping: www.baidu.com: Name or service not known
```
　　启用伪装IP后即可正常访问.
```
$ docker run -it  --rm centos
[root@7ec3691c37d7 /]# ping www.baidu.com
PING www.a.shifen.com (14.215.177.38) 56(84) bytes of data.
64 bytes from 14.215.177.38 (14.215.177.38): icmp_seq=1 ttl=54 time=22.3 ms
64 bytes from 14.215.177.38 (14.215.177.38): icmp_seq=2 ttl=54 time=21.9 ms
^C
--- www.a.shifen.com ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 2ms
rtt min/avg/max/mdev = 21.915/22.122/22.330/0.255 ms
```


>https://blog.tintinlabs.com/install-docker-ce-on-the-centos-8/