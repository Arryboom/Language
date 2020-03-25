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






#security firewalld



After spending a couple of days looking at logs and configurations for the involved components, I was about to throw in the towel and revert back to Fedora 30, where this seems to work straight out of the box.

Focusing on firewalling, I realized that disabling firewalld seemed to do the trick, but I would prefer not to do that. While inspecting network rules with iptables, I realized that the switch to nftables means that iptables is now an abstraction layer that only shows a small part of the nftables rules. That means most - if not all - of the firewalld configuration will be applied outside the scope of iptables.

I was used to be able to find the whole truth in iptables, so this will take some getting used to.

Long story short - for this to work, I had to enable masquerading. It looked like dockerd already did this through iptables, but apparently this needs to be specifically enabled for the firewall zone for iptables masquerading to work:
```
# Masquerading allows for docker ingress and egress (this is the juicy bit)
firewall-cmd --zone=public --add-masquerade --permanent

# Specifically allow incoming traffic on port 80/443 (nothing new here)
firewall-cmd --zone=public --add-port=80/tcp
firewall-cmd --zone=public --add-port=443/tcp

# Reload firewall to apply permanent rules
firewall-cmd --reload
```
Reboot or restart dockerd, and both ingress and egress should work.



I have seen professional blogs only recommending to disable firewalld. Not going to do that. After some digging, I found people recommending to add the docker0 interface to the trusted zone. That does not seem much better, but I will admit to not knowing if that could be made viable under some environments. Using masquerade seems like a good solution, but I unfamiliar with the term outside of NetworkManager DNS caching. Does it simply mean that the zone is allowed to configure forwarding? – Kevin Oct 18 '19 at 20:05


---



What's missing from the answers before is the fact that you first need to add your docker interface to the zone you configure, e.g. public (or add it to the "trusted" zone which was suggested here but I doubt that's wise, from a security perspective). Because by default it's not assigned to a zone. Also remember to reload the docker daemon when done.
```
# Check what interface docker is using, e.g. 'docker0'

ip link show

# Check available firewalld zones, e.g. 'public'

sudo firewall-cmd --get-active-zones

# Check what zone the docker interface it bound to, most likely 'no zone' yet

sudo firewall-cmd --get-zone-of-interface=docker0

# So add the 'docker0' interface to the 'public' zone. Changes will be visible only after firewalld reload

sudo nmcli connection modify docker0 connection.zone public

# Masquerading allows for docker ingress and egress (this is the juicy bit)

sudo firewall-cmd --zone=public --add-masquerade --permanent

# Optional open required incomming ports (wasn't required in my tests)

# sudo firewall-cmd --zone=public --add-port=443/tcp

# Reload firewalld

sudo firewall-cmd --reload

# Reload dockerd

sudo systemctl restart docker

# Test ping and DNS works:

docker run busybox ping -c 1 172.16.0.1
docker run busybox cat /etc/resolv.conf
docker run busybox ping -c 1 yourhost.local
```

###funny details

 have changed the FirewallBackend variable to iptables again and it works for me.

    With this update, the nftables filtering subsystem is the default firewall backend for the firewalld daemon. To change the backend, use the FirewallBackend option in the /etc/firewalld.conf file.

Link: Centos8 Deprecated_functionality

I don't have too much information about this behavior change. Some of the iptables rules that Docker tries to use are not working according to the CentOS8 logs:

    WARNING: COMMAND_FAILED: '/usr/sbin/iptables -w10 -D FORWARD -i docker0 -o docker0 -j DROP' failed: iptables: Bad rule (does a matching rule exist in that chain?).



>https://serverfault.com/questions/987686/no-network-connectivity-to-from-docker-ce-container-on-centos-8


---
#and don't forgot


I want to explicitly open ports on my centos 7 machine, so I've configured firewalld with drop as the default zone and my external zone on my public facing interface. When I run python -m SimpleHTTPServer 8000 and hit the box on port 8000 it fails. But if I add the port to the external zone. It works. All as expected.

However, when I start a docker container on port 8000, and I hit the box externally, I can get to the service. Which is not what I want to happen. I want that to only be accessible if I open port 8000 on zone external.

Even if I bind the docker container to the public address of the box, it still get around the firewall. I can provide more information if needed like route tables and interface configuration, but I don't quite know what's useful. Looking to learn.

The box has two physical interfaces on it, eth0 which has a public ip assigned to it and eth1 which is connected to the private network, and I want to have accessible.
```
EDIT SOLVED added --iptables=false to the docker options.
```

>
>how did you solve the issue? I'm also stuck at this very problem. After --iptables=false, now docker does not >temper explicitly with iptables but now I'm unable to connect to any host of the internet from inside my >container. I have tried everything found on the internet but still out of luck

---

>https://serverfault.com/questions/713748/firewalld-not-blocking-docker-container-ports

>https://blog.tintinlabs.com/install-docker-ce-on-the-centos-8/