``docker ps -as``  
查看全部container，包括exited的容器，并列出容器的大小。

```docker start ng```  
启动已有容器

```docker run -itd -p 8099:80 --name ng nginx```  
将container80端口映射到主机8099，起别名为ng
>on windows,it's reverse,means container 80 map to host 8099

```docker logs nginx```   
查看容器日志


```docker container run -itd --restart=always nginx```  
重要服务如数据库等需要持续运行的，当内部服务崩溃导致container退出时，将自动重启(退出策略)

```docker container run -itd -h hostname nginx```  
设置容器主机名

```docker top nginx```  
查看Nginx容器中运行的进程

```docker exec -it nginx bash```  
-i交互-t伪终端
为容器临时分配一个伪终端并执行bash


```docker commit nginx ddd```  
将名为nginx的container提交为一个名为ddd的镜像

```docker container exec nginx ls /root```  
在nginx容器内执行ls /root

```docker container cp test.md nginx:/root```  
复制宿主机test.md到nginx的root目录内
反过来就是容器复制到宿主机


```docker port nginx```  
查看容器nginx的端口映射


```docker rm -f $(docker ps -q -a)```  
删除所有创建的容器



```docker volume ls```  
查看所有创建的数据卷


```docker run -d -it --name=nginx-test --mount type=bind,src=/data/work,dst=/usr/share/nginx/html nginx```  
将宿主机的/data/work目录映射到容器nginx内的/usr/share...目录


```mount```  
查看所有映射关系，docker默认一般在/var/lib/docker/volume/容器名/xx下或者/var/lib/docker/overlay2/容器id/目录下。ls /var/lib/docker/volumes/mysql-vol/_data/

> 多个容器可以共用一个数据卷
> 数据卷一般是宿主机上的目录映射到container内，这样容器重启异常等数据不会丢失



``` docker network create lnmp```  
创建一个名为lnmp的网络

同一个自定义网络内可以通过主机名相互通信
如--name=mysql，IP地址为172.0.0.2
那么可以通过mysql来访问主机也可以通过IP地址来访问。





```
docker image build -t bulletinboard:1.0 .
```
在有DockerFile的目录下时(DockerFile已经编写好)可以直接创建镜像，-t后面跟标签名和版本号



#容器的启动/停止命令：
　　1：停止命令

　　 Docker stop

        Docker kill

　　这两个都可以停止容器

　　区别：

　　stop 发送一个命令等待容器停止： 有等待时间

　　Kill   直接停止容器：没等待时间

　　2：启动命令

　　Docker start    ID/名称  启动一个未运行(已停止)的容器

　　Docker start -i  ID/名称  以交互的方式启动容器(直接会进入容器)

　　这个就不演示了，在使用-I 的时候不是每次都会成功的会卡住。这点注意了
>https://www.cnblogs.com/szlblog/p/10612280.html

#容器的退出命令
　　1：exit

　　2：ctrl+d        退出和exit 效果一样。

　　3：ctrl+p ctrl+q  这种组合的退出方式只适用与交互式容器

 　　以上三个命令是在容器中使用的





---

#On windows port map error,not localhost
If you’re using Docker Toolbox then any port you publish with docker run -p will be published on the Toolbox VM’s private IP address. docker-machine ip will tell you. It is frequently 192.168.99.100.

This isn’t prominent in Docker’s documentation, but it does at least show up in the excellent Get Started, Part 2: Containers 502 tutorial. (I couldn’t find this rather important note anywhere around Toolbox Overview 66, for instance.)
https://docs.docker.com/get-started/part2/#run-the-app




#docker 增加端口映射

>https://blog.csdn.net/chouzhou9701/article/details/86725203

简述：
这几天研究了一下docker, 发现建立完一个容器后不能增加端口映射了，因为 docker run -p 有 -p 参数，但是 docker start 没有 -p 参数，让我很苦恼，无奈谷歌了一番，终于让我找到了解决办法

解决办法：
一种是将原来的容器提交成镜像，然后利用新的建立的镜像重新建立一个带有端口映射的容器，不推荐这种办法
另一种就是改容器配置文件（也是我推荐的解决办法）：配置文件在 /var/lib/docker/containers/[hash_of_the_container]/hostconfig.json 还有 config.v2.json 这俩文件
hash_of_the_container 可以通过 docker inspect 容器名字 来查看

![5088D12D-D5B0-6DC4-EAC7-9A0FFD0A5369]

id 就是 容器的 hash 数值，在 hostconfig.json 里有 "PortBindings":{} 这个配置项，可以改成 "PortBindings":{"80/tcp":[{"HostIp":"","HostPort":"8080"}]} 这里 80 是容器端口， 8080 是本地端口， 然后在 config.v2.json 里面添加一个配置项 "ExposedPorts":{"80/tcp":{}} , 将这个配置项添加到 "Tty": true, 前面，我不知道添加到别的地方会不会有影响，因为经过对比正常的端口映射配置项是在这个位置，这个就是将容器内部端口暴露出来，如果不加这一句端口映射不会成功的，最后重启 docker的守护进程 service docker restart
这里有个问题就是重启后 用docker ps -a 是看不到端口映射的，但实际已经映射好了，我正常在新建一个带有端口映射容器的时候，重启 docker的守护进程，端口映射也不会显示出来，但是通过docker inspect 容器名 可以看到配置项已经修改成功了。
---
*another way*

https://stackoverflow.com/questions/19335444/how-do-i-assign-a-port-mapping-to-an-existing-docker-container#
[How do I assign a port mapping to an existing Docker container? - Stack Overflow](https://stackoverflow.com/questions/19335444/how-do-i-assign-a-port-mapping-to-an-existing-docker-container#)

> This answer is not useful
> 
> The question owner accepted this as the best answer Dec 15 '19 at 14:36.
> 
> [](https://stackoverflow.com/posts/38783433/timeline)
> 
> Show activity on this post.
> 
> You can change the port mapping by directly editing the `hostconfig.json` file at `/var/lib/docker/containers/[hash_of_the_container]/hostconfig.json` or `/var/snap/docker/common/var-lib-docker/containers/[hash_of_the_container]/hostconfig.json`, I believe, if You installed Docker as a snap.
> 
> You can determine the \[hash\_of\_the\_container\] via the `docker inspect <container_name>` command and the value of the "Id" field is the hash.
> 
> 1.  Stop the container (`docker stop <container_name>`).
> 2.  Stop docker service (per Tacsiazuma's comment)
> 3.  Change the file.
> 4.  Restart your docker engine (to flush/clear config caches).
> 5.  Start the container (`docker start <container_name>`).
> 
> So you don't need to create an image with this approach. You can also change the restart flag here.
> 
> _P.S. You may visit [https://docs.docker.com/engine/admin/](https://docs.docker.com/engine/admin/) to learn how to correctly restart your docker engine as per your host machine. I used `sudo systemctl restart docker` to restart my docker engine that is running on Ubuntu 16.04_.

---

*and another way*

Thanks. For the sake of completeness, I had to run the following 3 `iptables` commands to get it to open to the outside world.

```
HOST> iptables -t nat -A DOCKER -p tcp --dport 443 -j DNAT --to-destination 172.17.0.2:443
HOST> iptables -t nat -A POSTROUTING -j MASQUERADE -p tcp --source 172.17.0.2 --destination 172.17.0.2 --dport https
HOST> iptables -A DOCKER -j ACCEPT -p tcp --destination 172.17.0.2 --dport https
```
https://forums.docker.com/t/how-to-expose-port-on-running-container/3252/16

https://github.com/docker/docker.github.io/issues/4942


Tried and tested.

- Stop your container using `docker stop <container-name/container-id>`.
    
- Edit `hostconfig.json` and `config.v2.json` files of the respective container by adding your port to `PortBindings` key and `ExposedPorts` key respectively.
    
- You'll require `sudo` access, or as `root` user. Then run `systemctl stop docker` and then run `systemctl start docker`.
    
- Finally start your container using `docker start <container-name/container-id>`.


#docker crontab

>https://blog.csdn.net/lizhihua0925/article/details/52370479

构建含有crontab的docker image镜像。 

sudo docker build –rm -t xiaorui.cc/docker-crontab .

启动这个容器。

sudo docker run -t -i xiaorui.cc/docker-crontab

后来在StackOverflow看到有人说，docker下是支持crontab的
```
ADD crontab /var/spool/cron/xiaorui/nima
CMD cron -f 
```


#Centos8 network not accessable

on host execute
```
firewall-cmd --zone=public --add-masquerade --permanent
firewall-cmd reload
service docker restart
```

or

```
firewall-cmd --permanent --zone=public --add-rich-rule=‘rule family=ipv4 source address=172.17.0.0/16 accept’ && firewall-cmd --reload
```

or 

Best Solution: add the docker networks/interfaces to a trusted zone.

In the host:

1: Find the network interfaces used by docker - run the command:
```
nmcli
```
and will show all network interfaces in the computer. The docker interfaces are: docker0 and br-xxxxxxxx (i think that depends on how many networks you have in the cluster)

For me, was 3 networks.

2- Add all interfaces to trusted zone with the command:
```
firewall-cmd --zone=trusted --change-interface=docker0 --permanent
firewall-cmd --zone=trusted --change-interface=brxxxxx --permanent
firewall-cmd --zone=trusted --change-interface=bryyyyy --permanent
```
3- Reload the firewall with the command:
```
firewall-cmd --reload
```
And will work :slight_smile:

If you want to test if the problem is the firewall or not you can disable the all the firewall with:
```
systemctl disable firewalld
```
>https://forums.docker.com/t/mysql-2002-no-route-to-host/90217/14



#docker export





#docker flatten


>https://tuhrig.de/flatten-a-docker-container-or-image/

# Flatten a Docker container or image

Docker containers and respectively images can become fairly large. I recently worked with a Docker image which was over 7 GB big. However, it is pretty easy to flatten an image at the end.

# Difference between save and export

As I described in my last post ([http://tuhrig.de/difference-between-save-and-export-in-docker](http://tuhrig.de/difference-between-save-and-export-in-docker)), there are **two ways to persist a Docker images or container**:

- A Docker _image_ can be _saved_ to a tarball and _loaded_ back again. This will preserve the history of the image.
    
    
    \# save the image to a tarball
    
    docker save <IMAGE NAME\> \> /home/save.tar
    
    \# load it back
    
    docker load < /home/save.tar
    
- A Docker _container_ can be _exported_ to a tarball and _imported_ back again. This will _not_ preserve the history of the container.
    
    
    \# export the container to a tarball
    
    docker export <CONTAINER ID\> \> /home/export.tar
    
    \# import it back
    
    cat /home/export.tar | docker import \- some\-name:latest
    

# No history

We can use this mechanism to flatten and shrink a Docker container. If we save an image to the disk, its whole history will be preserved, but if we export a container, its history gets lost and the resulting tarball will be much smaller.

We can see the history of a image be running `docker tag <LAYER ID> <IMAGE NAMEgt;`:



vagrant@ubuntu\-13:~$ sudo docker images \--tree

├─f502877df6a1 Virtual Size: 2.489 MB Tags: busybox\-1\-export:latest

└─511136ea3c5a Virtual Size: 0 B

 └─bf747efa0e2f Virtual Size: 0 B

 └─48e5f45168b9 Virtual Size: 2.489 MB

 └─769b9341d937 Virtual Size: 2.489 MB

 └─227516d93162 Virtual Size: 2.489 MB Tags: busybox\-1:latest

So if we export a container (either an already running one or just start a new one from an image) it will lose its history and all previous layers. This will make it impossible to make a rollback to a certain layer, but it will also shrink the image. My >7 GB image is now >3 GB large, which saves more than 50% of disk space.

# Flatten a Docker container

So it is only possible to “flatten” a Docker container, not an image. So we need to start a container from an image first. Then we can export and import the container in one line:

1

docker export <CONTAINER ID\> | docker import \- some\-image\-name:latest

# What else?

You can use some common Linux tricks to shrink Docker images. One simple trick is to clear the cache of the package manager. So depending on which base image you use you can do something like this (for an Ubuntu/Debian system, for more see [here](http://unix.stackexchange.com/questions/75932/what-stuff-can-be-safely-removed-for-disk-space-sake)):


\# clean apt cache

apt\-get clean

# Resources

- [http://stackoverflow.com/questions/22713551/how-to-flatten-a-docker-image](http://stackoverflow.com/questions/22713551/how-to-flatten-a-docker-image)
- [http://docs.docker.io/en/latest/reference/commandline/cli](http://docs.docker.io/en/latest/reference/commandline/cli)
- [http://unix.stackexchange.com/questions/75932/what-stuff-can-be-safely-removed-for-disk-space-sake](http://unix.stackexchange.com/questions/75932/what-stuff-can-be-safely-removed-for-disk-space-sake)

**Best regards,**  
Thomas


---

>https://blog.csdn.net/qq_37212970/article/details/84376897


### 1.导出export 导入import

查看本机已用的容器：docker ps -a 

![](https://img-blog.csdnimg.cn/20181123104925697.png)

使用容器36b155b315d8，将这个容器保存为exportsso.tar

**导出**：docker export container

```
docker export 36b155b315d8 > /opt/docker/exportsso.tar
```

导出成功

**![](https://img-blog.csdnimg.cn/20181123105609981.png)**

**导入：**

```
 cat exportsso.tar |docker import - exportsso
```

导入成功

![](https://img-blog.csdnimg.cn/20181123110152487.png)

### 2.保存save -加载load

查看本机镜像:docker images

![](https://img-blog.csdnimg.cn/20181123110854511.png)

使用镜像sso :4e15896e1299,将这个镜像保存为savesso.tar

```
docker save  4e15896e1299  > /opt/docker/savesso.tar
```

保存成功

![](https://img-blog.csdnimg.cn/20181123111303575.png)

加载保存的镜像

```
docker load < /opt/docker/savesso.tar
```

导入成功

![](https://img-blog.csdnimg.cn/20181123111846622.png)

### 3.docker save和docker export区别

1. docker save保存的是镜像（image），docker export保存的是容器（container）；
2. docker load用来载入镜像包，docker import用来载入容器包，但两者都会恢复为镜像；
3. docker load不能对载入的镜像重命名，而docker import可以为镜像指定新名称。
4. docker export导出的镜像文件大小  小于 save保存的镜像
5. docker save 没有丢失镜像的历史，可以回滚到之前的层（layer）。（查看方式：docker images --tree）docker export 再导入时会丢失镜像所有的历史，所以无法进行回滚操作（docker tag <LAYER ID> <IMAGE NAME>）



---

1、导出容器export

注意：export导出的是容器，不是镜像。

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. docker export <CONTAINER ID > > my\_container.tar  

  
docker export 命令会把容器的文件系统以tar包的格式导出到标准输出。将容器保存到本地后，我们就可以通过网络等方式将tar包分享给他人。

2、导入容器为镜像import

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. cat my\_container.tar |docker import - image\_name:tag  

  
3、查看导入的镜像

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. docker images  

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. \[root@localhost ~\]# docker images  
2. REPOSITORY                            TAG                 IMAGE ID            CREATED             VIRTUAL SIZE  
3. clj\_mysql                             5.6.28              d397fa7d51e3        About an hour ago   322.4 MB  
4. daocloud.io/library/mysql             5.6.28              6992b0d06649        3 weeks ago         324.2 MB  
5. index.tenxcloud.com/tenxcloud/mysql   latest              ba0c7864ab1e        3 months ago        470.9 MB  

  
4、运行导入的镜像

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. docker run --name clj\_mysql\_3 -e MYSQL\_ROOT\_PASSWORD=123456  -d -p 33062:3306 clj\_mysql:5.6.28 /entrypoint.sh mysqld  

  
注意：运行导入的镜像的时候必须带command，否则启动报如下错误

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. FATA\[0000\] Error response from daemon: No command specified  

具体的command需要在导出容器的时候通过docker ps查看到。

**\[plain\]** [view plain](http://blog.csdn.net/clj198606061111/article/details/50450793# "view plain") [copy](http://blog.csdn.net/clj198606061111/article/details/50450793# "copy")

1. \[root@localhost ~\]# docker ps  
2. CONTAINER ID        IMAGE                              COMMAND                CREATED             STATUS              PORTS                     NAMES  
3. f90c4887c320        clj\_mysql:5.6.28                   "/entrypoint.sh mysqld   4 seconds ago       Up 4 seconds        0.0.0.0:33062->3306/tcp   clj\_mysql\_3  
4. 8ec92a7f18db        daocloud.io/library/mysql:5.6.28   "/entrypoint.sh mysqld   59 minutes ago      Up 25 minutes       0.0.0.0:33061->3306/tcp   clj\_mysql



---



#Rrror response from daemon: No command specified

docker export import后，导入镜像，启动时的错误，


运行导入的镜像必须带command，否则启动会报错：Error response from daemon: No command specified

``docker run -d -p 9999:9093 sso:latest java -Djava.security.egd=file:/dev/./urandom -jar /app.jar``
具体的command需要在导出容器的时候通过docker ps 查看到

看完整的command的内容： ``docker ps  --no-trunc ``



#docker connection between container容器间通信

### 1. docker run --link的作用

docker run --link可以用来链接2个容器，使得源容器（被链接的容器）和接收容器（主动去链接的容器）之间可以互相通信，并且接收容器可以获取源容器的一些数据，如源容器的环境变量。

**--link的格式：**
```
--link <name or id>:alias
```
其中，name和id是源容器的name和id，alias是源容器在link下的别名。

eg：

**源容器**

`docker run -d --name selenium_hub selenium/hub`

创建并启动名为selenium_hub的容器。

**接收容器**

`docker run -d --name node --link selenium_hub:hub selenium/node-chrome-debug`

创建并启动名为node的容器，并把该容器和名为selenium_hub的容器链接起来。其中：
```
--link selenium_hub:hub
```
selenium_hub是上面启动的1cbbf6f07804容器的名字，这里作为源容器，hub是该容器在link下的别名（alias），通俗易懂的讲，站在node容器的角度，selenium_hub和hub都是1cbbf6f07804容器的名字，并且作为容器的hostname，node用这2个名字中的哪一个都可以访问到1cbbf6f07804容器并与之通信（docker通过DNS自动解析）。我们可以来看下：
```
docker exec -it node /bin/bash

root@c4cc05d832e0:~# ping selenium_hub
PING hub (172.17.0.2) 56(84) bytes of data.
64 bytes from hub (172.17.0.2): icmp_seq=1 ttl=64 time=0.184 ms
64 bytes from hub (172.17.0.2): icmp_seq=2 ttl=64 time=0.133 ms
64 bytes from hub (172.17.0.2): icmp_seq=3 ttl=64 time=0.216 ms

root@c4cc05d832e0:~# ping hub
PING hub (172.17.0.2) 56(84) bytes of data.
64 bytes from hub (172.17.0.2): icmp_seq=1 ttl=64 time=0.194 ms
64 bytes from hub (172.17.0.2): icmp_seq=2 ttl=64 time=0.218 ms
64 bytes from hub (172.17.0.2): icmp_seq=3 ttl=64 time=0.128 ms
```
>docker automatically add hosts in target container .
>如果重启了源容器，接收容器的/etc/hosts会自动更新源容器的新ip。
>在docker的后续版本中，会取消docker run中的--link选项，但了解其如何在2个容器之间建立通信的原理是非常有用的，因为这有助于理解如何用官方推荐的所有容器在同一个network下来通信的方法，以及用docker-compose来链接2个容器来通信的方法。```docker-compose.yml中使用depends_on```
>你好。请问如果我想启动B容器的时候link到A容器，但是A容器还未启动。此时有什么解决办法吗？
>用wait-for-it.sh。docker官网里有讲这部分的：https://docs.docker.com/compose/startup-order/
>https://www.jianshu.com/p/21d66ca6115e



---
#Failed to built-in GetDriver graph btrfs /var/lib/docker

>https://docs.docker.com/storage/storagedriver/btrfs-driver/
>https://stackoverflow.com/questions/33357824/prior-storage-driver-aufs-failed-driver-not-supported-error-starting-daemon
>https://stackoverflow.com/questions/52617727/cannot-start-docker-daemon-graphdriver-issue
>https://stackoverflow.com/questions/45103803/not-able-to-start-docker-on-ubuntu-16-04-2-lts-error-initializing-graphdriver



service docker status showed ``Failed to built-in GetDriver graph btrfs /var/lib/docker``

Edit /etc/docker/daemon.json. If it doesn't exist yet: create it. Assuming that the file was empty, add the following contents to change storage driver:
```
{
"storage-driver": "overlay2"
}
```


I did some research and I found the answer. I was able to fix the issue by using the overlay2 as storage driver. I followed the below link for that: [https://docs.docker.com/engine/userguide/storagedriver/overlayfs-driver/](https://docs.docker.com/engine/userguide/storagedriver/overlayfs-driver/)

I took the followins steps to fix the issue:

1. Stop Docker.
    
    ```
    sudo systemctl stop docker
    ```
    
2. Copy the contents of `/var/lib/docker` to a temporary location.
    
    ```
    cp -au /var/lib/docker /var/lib/docker.bk
    ```
    
3. Edit `/etc/docker/daemon.json`. If it doesn't exist yet: create it. Assuming that the file was empty, add the following contents:
    
    ```
    {
      "storage-driver": "overlay2"
    }
    ```
    
4. Start Docker.
    
    ```
    sudo systemctl start docker
    ```
    
5. Verify that the daemon is using the overlay/overlay2 storage driver.
    
    ```
    sudo docker info
    ```
    

After this I was able to run docker container on my "16.04.2 LTS (Xenial Xerus)"

```
sudo docker run -dit ubuntu
```

# Docker CE

For Docker CE only some configurations are tested. Your operating system’s kernel may not support every storage driver. In general, the following configurations work on recent versions of the Linux distribution:

Linux distribution Supported storage drivers Docker CE on Ubuntu aufs, devicemapper, overlay2 (Ubuntu 14.04.4 or later, 16.04 or later), overlay, zfs

[https://github.com/moby/moby/issues/24023](https://github.com/moby/moby/issues/24023)




---


#Docker 所有容器都挤在 CPU0 工作是怎么回事？

>https://www.v2ex.com/t/734881#reply4

```
--cpuset-cpus string CPUs in which to allow execution (0-3, 0,1)
```
```
--cpuset-cpus="0-2,7,8"
```
关键字 docker container cpu affinity




#container freezing and connot start,

- 
*Assuming you have /bin/bash*
```docker run -it --entrypoint "/bin/bash" myimagename:myimagetag```
- 
the container seems to be crashing on boot for some reason. Try to start it using the pseudo-TTY "-dit" argument to see why its failing

``docker run -it MYCONTAINER /bin/sh``

That should give you an idea about why it is crashing.


#Fixed container IP
>https://www.cnblogs.com/xuezhigu/p/8257129.html

#### Docker安装后，默认会创建下面三种网络类型

```
$ docker network ls
NETWORK ID     NAME        DRIVER       SCOPE
9781b1f585ae    bridge       bridge       local
1252da701e55    host        host        local
237ea3d5cfbf    none        null        local
```

#### 启动 Docker的时候，用 --network 参数，可以指定网络类型

```
docker run -itd --name test1 --network bridge --ip 172.17.0.10 centos:latest /bin/bash
```

#### bridge：桥接网络

```
默认情况下启动的Docker容器，都是使用 bridge，Docker安装时创建的桥接网络，每次Docker容器重启时，会按照顺序获取对应的IP地址，这个就导致重启下，Docker的IP地址就变了
```

#### none：无指定网络

```
使用 --network=none ，docker 容器就不会分配局域网的IP
```

#### host： 主机网络

```
使用 --network=host，此时，Docker 容器的网络会附属在主机上，两者是互通的。
例如，在容器中运行一个Web服务，监听8080端口，则主机的8080端口就会自动映射到容器中。
```

#### 创建自定义网络：（设置固定IP）

```
启动Docker容器的时候，使用默认的网络是不支持指派固定IP的，如下
docker run -itd --net bridge --ip 172.17.0.10 centos:latest /bin/bash
6eb1f228cf308d1c60db30093c126acbfd0cb21d76cb448c678bab0f1a7c0df6
docker: Error response from daemon: User specified IP address is supported on user defined networks only.
```

#### 步骤1: 创建自定义网络

```
docker network create --subnet=172.18.0.0/16 mynetwork
➜ ~ docker network ls
NETWORK ID     NAME        DRIVER       SCOPE
9781b1f585ae    bridge       bridge       local
1252da701e55    host        host        local
4f11ae9c85de    mynetwork      bridge       local
237ea3d5cfbf    none        null        local
```

#### 步骤2: 创建Docker容器

```
docker run -itd --name networkTest1 --net mynetwork --ip 172.18.0.2 centos:latest /bin/bash
```

---

[5088D12D-D5B0-6DC4-EAC7-9A0FFD0A5369]:data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAA1cAAAFKCAYAAADxFaWcAAAgAElEQVR4XuydB5RURdbHf51znkBQQTEDooIBA5gwZ11zzq4rqCCSswQlGMCsa1yzH6IIKmIAUQyICUXBQJ7YOYf3nXrdPdM90zPTg2Ds53HP7s7relW3bt17/zeVgkXfSP2nD2dbPalUalsNVRqnRIESBUoUKFHgb0QBSdmF/kOGcG7saSbdvZzN0p9rcdtzfkqlsoXFJgjV1eCOaXGUuzBKfqpr/CSavi1JSChQKJr8weCgkzVFXZWXaLG/KTQTtZXyCjOaJn8r6fQ/F482nU3LfBXHV11DQGmlvMyMMh4n3zqTiAX8xDQmTDoV+WylRKNRNXxKCtWz2avAWWFGkciOIhH11xNS2bAb1SikBCGPl6TRiUWXHU2JVqdpMnbTFSQI1tYS1Nhx2fQ0frUVukth3JvriRpcdHDo5RcTgWqqfWocHZ0YFBD3VVMT0lHWwYb2d99CFbbjRjJmwHoWTniEt31Frep3n+Vf64Maul40nVv2XswTo+fyebKpIPxzrUZRAld/rg0pzaZEgRIFShT4u1Ig2ekshg49FN2ro5jybog/GbZie86vZSMYSIapr64nohIAx9IM4EAMb3UtIY2DDg5DAWM1TjScyDOekxEv3pASo92CviVcl8toCjV6fXNDuASu/tynsVW+iqeButruQOGtJ1Ss71tpoqyjHZ289CTB2ir8ahcd7On/J/uE6zfh15RTYRGQPA3mkpZOOAy5b6V5N9jMW9AOuiqNuDpk5wPJUB1VniTm8gqssjcgPUevwk5Hl1E+H78fuFLhOOhMjrRXUe0NE8OAufM+HHTwLji+vo/bn1qD988m6NpB+j/Pq/94cJUkEY2SUGjQa5ui9SieqgDaMhfG7Qjkk8Fa6hJWKgxhquqTWCucGJQS4foqfConlTYtxLxU1ycwV7gwtqp4xO+qiRgrcOibI2VxyGsCGlwV1gIKsb1sGcGzxYNk70DGGdPeAVpz9bS6jm34ocxQCSLhJFqDjmL0OmzPtbd3dY17bk3WUeOLy0agxlJOmS5EdV0cs/AwbxcebsKn7Z16O95PRXzU+0IIR6TWVoHT0NZOSSQiUVI6Pdq09trOtGjHYn7rq8kY4bgSg15d3Ei/w9qFHMvjPXPj3BpknDHyO+xBk30vjkIF3lLhOGEMY4+t4u3b72F+bWu0ThByu/FFE6A0YTPG8IZ1lJVbKHKH8r9flLxvz/zaIIKUJBaNI6l16NRpvdGqESzMw2iYmFKPQVNAz4TrqXYnMDUYk219P4y7qp6w1kGl01hcNKCFIVOp1nT6VjNDUT/Ml1FWlF7fdtKPWbUVwecLEI4l5CihWmvAbLXSklhIBGqpjegpKzNvHV8WRYXGl6RkjFhcQqnTkWWTtvgqEYuj0Gra5AEpGsAXU2Oy6PPXkghQXR1CV1aBTZskHktmnCISUV8dYbUDu1CGUoKg20PK7MIiKwhQKDVo1BCPREhkAEYi7MEf02GxGVqgWYKw10dEbcFhysZR1WgN2TUkCFRX48VEeUUmKpUMUlvlISoiuw3kkpCkppFeHfbKba27dXQ4+mouOKQjlXYDWilKsH49P3+xmAXvrGJTrC292k4m+Me+/o8HVykQiiWWQKFtFAAyPwgF51Ph3K6CKCWnWMRMldiV3hxwBYlIgKjCIIfAiwZXUgSPECzlAqA15+q/DLhqYx3b+ry2X+kkiAQioDe3qMi29RxbHC+HVtpwLTUhDQ6XFa1SgWK7G9W/F7hK4K+tJaS0YLdoUak1ZGzAlsnc1EDd7rT4vXY8JTseGhwvxXw2FSUYSqE1GRqMnGJ+1r53JKRUHH9dPTFDGWUN4CpHxql+B4BbFDBp38rafDviZosnicFuxahWoZZiBOMqjEZtkc6aJl9IZvbLYtgGTrA2Z59+IRUnGgeNTiPPuS0juOVRo3hkz78eh3AGtokuk4TdNdSHJPRWB+aModvi+Eo1upwUsKbvyZGrlnR6kaTYuteayigFieB21BGpCJ4aD3GdFZtZpKcliPg9+ONa7OV2CvhWScVCBBNqTFvLl1tBGCkRJZpSNziv28NXibCPsMKERd/UMyhS86rxpizNIqdRzxZqhQ7s6MIoQEy1h1gWwohUVUUOoMn935KEwlRGxybRLsJuNrlTLYMcKUT9Zg8pWwVlpgLMHvWwpTZIUiPmapVT/uL+amr8SswOCzrZTpNIhDx4I1qsDlOOjFai1rUNNLdiW0o/KVEgjwK/a1pg3FeDV+GgzNKmdtj6bRK5uNURDBUO9PF8cJU3aLEGQ9RDdUCLy1XY+/eXAVdtrGPrCV74l+0HV9t6Br9hvBxaIaIHIR2u8kyqznYHFL8XuBKpGvUkzBW4Wg/dNhLybwyuQnXV+NWZqPZvYJ1t/9MEgdpaIvoccJUr4xJ/T3CVCtVR7VfjrPwj6iW2/S6KEQsZwfFAHe5AphZGacSZMRYbZyAR9dRQFxTRcyUqJSiNdpwtev0hHqyj1hMhlWv0trQkSQKDk47OQqmGGYz4h9VRb4WM+g1blxLRQZ8Se6WddBWPsNFFBNAL9sqCmSu/4XPb7KfN+SpJqK4KT6YAT2Fw0FHO05OI+euo88fRWlw4LdqGKI8UcVNVH0XnbLLOZIi6KjcR9DngKoi+ogKzOp2KFzF2xGUU8aIons1ucHZA4KmIezNuhbM5uJLC1Iv3HB1xiuKopo8MvuJYyiuQsw3znvTa3BFJpJJkwFUUz5Y6wrr8lNnfLy1wm21laaC/EQV+R3AljAQ3KVs5lqSbKo+QV44cb1DaiAjryigX4CsZwe8PEIkmSIo8YZUavdGK1Zz2XMrGuwgtK6MEIklURgdlVi1EPFSFdJQ7DShjueCqUFpgHL1VSyIYIpZUoNLoMdtsGHKwX8yb9mhnvcaJsBdfIEIsKaHSGjGqY/gj2sa0QClOyOcjGImTlECp1mO0WjFrG8NeyWgAvz9ERORjKdTojGasFuEpa5Ial4rirXcTUVpxOY3pELqgi0hbiIr8euGFMWC2WDLRHuHRriaksaCJBQknFehtZdj1Cpquo+U5iI+kiAV9+EMR4klQqDToTFZsxkw+vgwwUphsSqKBsEwLhVqH0WLDolOm98afTqUDDebyMixqiXhIjBkllkgKUwOVVo/FasvMPX/t2f21aRIEQ1ESkgKV1oDFZqXR6ZYg4vMRCMfklAOZ1hYr5rTrqjCPWBQEvV5Cgq8K/Eb8LpdWcmpWM3AVQ2/TkQyGiCby156WDQnCPj/BSJRESlBBjUZvwmYzZqJDKaJ+L34x75SUpq/Ris0k6JvhU6UFiyJCMBwniQqN3ozN3lIaRSGJ1MoeCoVZ4yWWzQNXGHDm5LMXlG9iz4W3UP6jAq2tHJcmSHVdW7QQ++7FF4wSF3zSlJeKEqbZtWTH0KK3WLFlc3XaOHOt81Ka3p5ohhgit1826NvYwzyQnR4jqLahTwUJRdJnU6O35O/ZVp3dZHNwlSvj5Hm0tQdtnGda4cdC+y5SgBJp+dwgh/RGrFYzaTH32+RHzFtFXUNxiAKNtRy75KY2Ny0wFSPg86VpLctQE+qYl6guN8KXw1xNHQOJMB5vgGhcpDgJOapPp39lHfqJ3PSwjJy1WjJ/L3K/m/B2IXAlxcOEYilSsSD+qDavrkSWJKF6ajwJ9EYl4RCYrCqivhBJrRWny0JD34DMt5IRD3X1QeKSAp29Bc9/w7zEOjbjxrF14EoSZ0Touoz8FfrTasOYzVf7LTQsKKP0hPPS5pNE/D4Csn5QotabMCrC+JImKguAxainCnfSTLnL1JgilwpRVx1A5ajArkkQTynQqHOiOlIEd5VI1a/EWSB0le9EbE2up4mejPrx+8MNul9rMGG1ZuW60IF+sJogHCASTwmlht5ky+iGwsKyEF8lomHiKQGmPAQ1DjrlFEHFQ27cnjAY7DgcRjTyGuuJaO1UNEkhFVGrupCQjVrsDZGrLLiK46+uIdFQYxWmfpMfTQYUtQiuhI4TwCtpo6LM1CQ1MMOTieYRNLF6GQTWhVFqlCQwUlZhRSXqr9xJOWVWVHxknxK4Kkq5ll7aThT4/cBVMkidqFWpsKOT0648pKw5dR4JPzW1UQxlZZjVcfw1IpdXpCwZUCtSJCJ+PP44Omf6AKUN+ARqox2r8H4odWjVEPVUE9C4cJnSqX+t1lzVhUip9VhsIpScJOLzEkjkpgAk8Nd4UXXohD0ZJRLxUOOJobXYseiVpKIBvL4wCVH8KddcJQjW1eKXDNhsJrSKNKDwhlIYnOUI7CfmVFMfQWW2YTVoIBHG5wmQMpZRZkk01lxp08AqqrLidGSAlRTFU+MmrhUgU4eaJLGAD29MnUlbSIMrb1yFwWpFRNQVGh1qhViHBxwC5LQ1BzVxXy11YQUmm1CUkIwF8HkjYM4YLhmDS9KacdgtaFUpot563BF1g9cv4a+lNqqjrCxdI5GKuKmW03zsaaApjCOPh7DCSrkcFSwArjL7a7fp5bQgkR4VVNqoEMCZJOH6WrwpPTarCVHel5R5JIE+Q+tCPCIFhII14rAZ0SgkYkEPnpAKu4h0yoZhPq0KgqvaICm1AYvVjE6s3e/BJzp9yR7PtKfZHddis1sQGaipeAivO4hkEnusJu0hBYvDikGtIBXz4/ZE0Tgqseuyxr4CrcmGVeQAJUJ4PaFG+hchDNrcQ7lAXkSuynEVWzyWNVCz9WYyH4RQmu3YtGoUzWgBiWAttQEFRpsVo0YJ8TA+b4CUoEVODVFrS5L3MQBGuw2TRiEbKB5fVha0feZy+aAlXhKpxH61g0qbKNhuew/z680yexZTYbA5sAnvTCKELxhHoTJhFev8DWe3aeQqT8ZlzmLL/ChK41o/z63zYyadW9SnZvc9FcFdH0ap1WAwGlARJ+jxEMqc5dQ2kB+odJjsGqT6AMK2k/ewAVylHXEBjNjlc5wg5PUSiEloLMWAqzi+mjpiWjt24awTXc58bgIpM+VlJlTJMO5aLwk5PUwnr0+ACFk3lNnRZ+p3PXn7HcRd52+Vr1tL3xJypsqvzgNXibCbencYpaUclzrAZpFK1dGFIeajrs5PTG3E7nDIMlo2PKNeauoCJNQ61IkYmF04Wq2jlIh6a/EptwZcJQnV1+AT8tcmQHXakPdGNWldJCI+v5mGTWVUvo6I+WqoDyszDlGJeNCLJxQHnb0guCLmo7o+hqmsTNaNMtgJ1lETbLlmOi17lNjkuu3mUioXXClbletifzzUuGOoZd0vZESEgM+fbmIigxqxPjdhpR6bQ+heJYmIh3pPDL0r27ih+Rxa46uwe5MMnnPBVRrkeamTecWIURUhGNPhKHfmp5zKdUxB1DYdMW8CczNwFca9SYAyK64yC1q5eUYs/Z6ilciVmIDQJbVhtK50lKsREaUbcKjslQX0UmP3Q4c2hFvUusmR3jihYBKdKb/TYAlcFWEolF7ZbhT43cCVUOA1USOV9nTAXRgI7pQlYygLA6CGurgx7VWSEkRCMZQGY8YTKp9GvFX1JCzpNKa00FNj75ATwpcNRh8qpwBo6QPcekMLEQbP8XYI0FflIWnNpEolIlSeOYvhh3zCUyNfZEl1LX6Vg8ocaSCiHPVRfTpyJX8vLgvvRrsxXc/hRYACPTF3FR4pCxAyAj4iDAgdZkMSr/DMWeyowh4iKhtOR2O0Qjb0gxqc2SJO+ee5CkghgysfVipcxsbahESAWrmxh/DIS0Ram4M+KdNAatLgQP52QC0DCJ3sLY+g32kA5950AUepP+S5mU/z5i8x9Jm1N00LTMZCRFJaTDmVwemUSnVmPQXAVZP9zUsVygPj2fORWRt2Kh16ks14JA0+/SISmAWsiAJdEYVUp+mVRyuhfAtFrsJoZSCU+W7CT21tFF2ZAK8p4uGwrLRyC9Mj7i14FHY62PVp3g2psbvsDZ7wRDwOKi3qjOHmlSx5Hlb599gb2s62KhGy3tbW9rCBb4pNC1RiOepWxp/kZf7ts3mnXlQph6g4cybDD/mcp0e9km6NmkcL0cDGTTJzZrNzlve96JSv9BiJPDAmQLGfuMaMCX8bZ85AKo8PlFgGjGLCCdU8d8sUluBAS4YvGtIC297DQuAqfcYF8FfT5dxp3NzrA6bf+BwbLIY0H23N2aVpWmATGSefxVb4UZXxvLfCC6pW+TEHXGWa/yT2OJ8pV+/CNw+M5/k1GSs1EcYfVmAwg79Y+ZFrMGb4Ji0/NOx8wRRu7rGEh0fP5eukIh9cySA/jqm8DOFDSyOL9DqTWQdQ0wOSF7lKy5qk2YXTnInGpxJy9oJoP50UjqGwtkmKXoYPxfgmVbo5krk/l426Ii3/7pnLh+urG854ofNZPLgSLbLrqfdFURqdlNv1KLJ1KhnDVZy9+joP4ZQas9OFFT819UESagsul5ZQdV3RneEUW5MWKMvfCDpXeaZbm1ixyNqIojSZUYeKo2HjmUlTLFdONuq2rIzK0RG69H5jq2gAkJKpBydcdz791R/z4pxXWRFoioaE46yWaEP9YroFeCibLdNk00TjhXpvFLXVhbOFQrdcPUer50iks9XgV9mpEPuZ/VbcR02doKMAT+n1JfJ4ON/uaS9ftQSu5HHifmprfERbjHLGCQaS6HVx6rKgSa65ykSuUsK5FgF1gqTSgsMQp94TQW0po8KqbTktUP64cI5W406I6FM29Vc4yqrxJs0Z0NR0tUmC9W5SljL0kWpqwhlwJSXT2U1N9y9QR11Yi0MAv9zsQ6UK1Z+7i3er6r30x78GBX4ncCWM3mrChpyOe7lpNWobOx5yOIcoVvDy0k2Z+z2SxCNRorE4iWSSRCJGQtmBA867irMP3Z0drCkCG1bxxdtzef1rX/o3QkG7wZ7tKNUmuEph7eBozK+W23nWENCkay+SQR87XvkYI/Z9n/+OfJZ3NnlICi9ijqdfDlP7VDK4UrZgNMre+6BIHTQQqakjKuonCtadpT1XEZEcJkmojS7KbI150SJEXx+m+T0nkoS+/xjm3NADa3AJMwc/zc+6hqxx2TtXFzdTIaOBdFSwxTkUBC3ZznAxjML4kdJ1Hsb9rmHsbf3pykaW3Tmaez8Poysrl6NjhWquRCFuJBojHk+STCaIx+MkFdk0rALgqkl3MClcT5VPKQM8rfjvIqm82aUvEpLajLPvf5j8n/1xNROiSdY/N5ghr2xBqdGh1ZnZ87K7GNYvvwWxIvkzi++YzktrPAXTAvMMO6FwakJoZUNDzQ7nTOXWw5q0NE78xEujp/BB0Ag4OXDQGC7qVljCK79+gKumr5RTD1Ombhx03HEc1nMndrRpSAY2s+7Ld5g3fwW/Rpr8XmGm8sDjOeHw7uzewYZBClC/bhUr3p7HG9+HkDIpZPIeqttZz6DckUNvHsLZkScYO3sFPrlbYJhOF8xm+P7LeDwLrnJpoQhQW+snntfBSbaEkdBia7NTp+xaprYmgMZZmZfy0RZQazxzVhS5UQ+xjltu4/zYY/xn1GKiIpLeDFylR0/GIkRiMRIJIX/iMt+iF3n9+iadErOpnFnHi4ZdLpnJzXsuYtqNz7HeYiDWytlVGF1U2tRp4C+6mdpzb2VpAq6ayrjMnubzY5IuF85kcMULTJq1mG+UAxh315konxrC7BUZgzOXF5QRPMJQF435NFp0Wh16gwG9iDSKp0lKXXSvS7n32h1YOXs8T2fBVXZD2iE/TOWuRnCUCFJbKzqSCfnROrhSyEA1JxU7Y6wJ2R3OrU3L1cFN1iBqner9MSSFGrVOi16vR28Q2QBNDfzsIOk99iqFg0Qn/3d/12uYNuGYtPybNZnHv6zBLaUdO4VOdlHgqtxI3OvBF06gNjsb7/1pCq5kfRfGXe8mrnNRYUrg9kmYHGa0ioh8B5BwEhZsCNBAl3amBSrK2XXA0RwWX8JTC75ho0jtz9OfjQTPB0kt01BEzXKdlULHNdKwqYzK0REqAUpimUyXzHntfBZDb+1HF2kTy+6azHO/Nm/nmtZLmYwK4UirjTQ4BBtnL6Jgbur9cTSiNqlQU4XMy3l6TjTEaPEcxfBU1TdzNMl1SltEuYTI4ollMldy655EtKaOuKk8nY1T4NmayJUMbjx1uIMJWYWm0GJxubA2zTMV38uNSOWAK12ompqYiQqHEr9HWC1hwkktqkQCjaMMQ6SmcM1Vg6wQ6eke4noH5Q4DKbkpBRhd5dgLzSOtOkRWerqBRQZcSZ7N1AaL7TEPCqOzWSTvr2Gul2b5V6LA7wSuREQoiFaklTQ4k9KdgKKGclzanTh+4lBO2XAftzy2mpgUxVvrJowWg16LRqNBo62gxyWDub6nm68WfcAnv/rR9Tydfx2m5LsH7uDJ1SnigVrqk1Yqsom3bYIrCWte5CvtXQqonVTY1ITrPex24zMM67m4RXCF6GqVBVcixUBEYpoUYTcaekYiwnPWBriK6ew4DFHcHpH6VIYt43aJeLbgSZhxOYR3POdRWOh+9WSu76VAIQX59P7bePp7VabmSUTOaogaK0kHDTPeu5bm0A7jyFzRhV0OOoReym/5eNkPrK3JGkfNwZWoHajzJVAJI0ajQa3RoIp5qQtm6dU+cKURKRhesJbZmtUdoFAi7XEZE2/Yh/oFD/F+tUhdalTw0U3f8NWvfsLRKNFoih7XP8LwvT5n7vzvqQ74iGvtmNQBqlb/yPq6urYbWuSBKxU7nTOZm7t9wrOvfI1frUalVCGFNrH6240k9HpQGCjbszs76eLEE3ESiQSJhIR2txM5//idSCwYz03P12K3l7P3leO4dpd1fPHBMr6s0dKx15H06+VE/em9jH9mLf6Guikbu5x9C9cfpif07bss/HA9sfJd2GXvfej89QzuXhL5TeAqUXkag4cdjvTMYKZ9okinyMjgag7D9/+wFXAVRG13YWnWXlqBSlTnt/W0Ba7aPHP54Ap5Hf0wvTyYQS975DTW5uAqRcRTiyeqRKfXodVqUKvVcgTMm40eFqi5auw22BxcCaOx4NkV65c9qU2jZw0WSF7NlTDm8mRcgQYrktSBI4fdxrFr7mT086up6XI1Dw6p5OOpM3i1KhNpygPaaaslERPnIUI0GpXbPWtsZbiEx347gau86wy2AlzlN39IO8aKBVcydVMJeb0Rsd5IjKTKiKPMhiT2KhNlzjW4m4Irn3oneh19RFr+fbKRancuMGjO2EWBqzIdoXo/mBw5baihocNaNnLVIM6SSIqmF8BuH3AlKbtz0tjrOOXX2Qy872PWbwtwlb0aJbOe3wKuUJgp796Tbqxh1bc1+ArdLSTXcgXRlpVjCNdQG8tkyzRsV5KI140nLKG3O7E366qXv6/NnYgtnSOREdI8il8YXOVexZIGVzFTuRwx3SbgKhnF567HHwW9vQynPoG3zi13PTQ6nDhyi85bAldlFuJ1AhRm0vfkxhdelM4OmOM1MkjSqhPEta7mDS1yFpEMu6kRqa9qNcl4ApU1e2dW64ohF1xpReQqKRx2TfYmWE99WCNHrnL7YihU6lLkqi29W/r7b6bA7wOu5Bbsalwinz1nylnBZNbuzFkzRnP6+jkMemw1kYzh3JjnrEDV63LGXdqRb+6fwgs/poFUbawbZ0yaxAWhhxnz4BdsrK0jZq5oTNdqE1zF05GYhgYW6dQPuRZMH8NdnWDfIf/l1u4CXL3EkurmYX05nTGiaz0t0F2NV04FzKQFYqMy5yLIVKg+nYbiUhNoKNgVBl4NnrgeZ5lNDmunUyFVTRqBxAlIB3HD9EvZe9UyvtuzL92/e5BJT3yXvrhOpMtUh9FXOORby4URJacFtjQHp7rltB5/Jg2zUMe8POOoKbhKK4iI3kVF+sY/+REplTLtZDDaPnCVTU3MS+sUtTJ+L2GlCfO+VzHxhh6suec6nvgpezdOkkggSEon6n+ys1DS7aLp3Gh6gvEPLuenqlxatZQW2OSeq7xoTYJdLruHmyxPMvaBlbhliZ+pEVGl0/pE/WBYMmBpUGISYb+Ro0ZP5gLjAmaMfJLliXT0VHLtQrf4T3y3bgvulI1KV2f2vW4cV+28hKdHvsDyuNhUBYoeVzLmqj1RvT+LO/5vLesFH8neUBUKhbjvI7OW7B62Ky1QieXYUUw4vprnb57EYqks7SRpC1xp0l5ZOaUvJ1IrLjcVTWDMRTXoyKZj5RoXkgx+AkobZXpxl12BVNyGM5dJC5SjoDYc8jpqeHvMJB5bq0qnudKkFbvs0RagMOuQaL6HhdICWwNX6RTV5mc36AkiGUQTFtKRq2YdC3MjVyLtt4mMK3AWU4aDuWLiWRieG8oDn0bRHj6CqSeu5emxr/C5zC/5vKAuwI8RTxWeZCY1tQm4SvS8gruu7Mhns8fzXENaoEg9jmNyGojUtpBWXLT8aDstUOy5aJSzVWmBxAgG4qjNpkzb5kw6sNhzZyXmaDqlLf/uwtz01ExaYKvAYCvBVUuNZQpFrlpU/wJc1RESd/y0ZSK0o1tgLrga9NBnbGyWFhjDV+slZS7DHK/bBjRsJXKVTQvM6+KXdo76FLbCNVcyLdJnPaC2oov65G6pjff7CZ1bJztVzA5n2y3ss02TMvdc0eo50hMtMi0w/57LbQmukkSDPrzeEHHRaMrpzGkKFMVbU0cgrkRvFzXrOXCkQOQKbYpYSpRDCPmZtinq4+LeKQFkEoT9UZIJL35F6+AKKYavphZ/XAKVEWe5A0MR90fmgasWeLxUc9XW4S/9fXtSoBFcKZzsdMhhHLzXrnTtZMdmNmFQJYkFt/Dl03fw7A9ZBKLCuPuxnHz8AeyzowVdtJr1X77HgjeW82NQSeNt7ipMexwnv9drJwuaSDUbv3qf+a9/JL8nP5KdAwaN4+JdC6mABD89P5y7PoySShrpcc1Erja/wIipywgb1GmgEbWy/3WzuK3XEh4d/QqbTr6D0b0/5MEJb7AqpciruVLvcQmTri9jyXtYWaoAACAASURBVPjJLPaKtJ44+n1O5fwTD2b3Tlb0RAn76ti08v94at4avGEPVUETB9/8ADfvV8eWKgXWcgvaWD3rv36X+XPf49u6IF5vmLjSxREjHmZgx/9j8i1P83nSgN1mRnPwIGZdbOO98TfxzJYybCLUHQsQ7zyA884bQO+uNrSRKtYuX8ALr35OnSKVf4mwKByv8ZDQO9PpgfI9HG6iarPceU6rSBHxe5H6jmTOtS6+nj2Djw4cz8B9v+LFcU+zJKRMd08MaikvK6PyoFM5rd/edKs0o5OihP0e3N/P49GnPmOtx0/KUMkex1/Fvw6qoGOlSBGAuHcTP33+Fs/+7x3WSk7KLBokaXfOmnYDR4gMN2Hapzaw9I6xzFnRmBYo19X4FJidXdjrqCM4sFsXdt6pAqfNglGVJBrYxPv3DOPRr7WZTnVRYsb9Of2i0zl4NxeWlJeN3y3jvTcW8Vl12ieVmxaoI0GorhZfUlxGaEEvGm+EfXLTE629HHPPywuAqxQRdw2epF7eH61KQSquo/egO7gsdB+3PfQp6wWtcurVCtdctQKu1Cb2vX4SlwRmc+tTa0mIu24C+YXWKRFt8UsYM809xMWQqv1vZMqVnfjpkQnc+8FmvIUMNwGunGZ2+Nc0hh78BS+PfIoPIkpQuNj3+rFc1fl9npj4Ip9GlOkaxrBSbkpi0oqmGUE8oimGyUWZuNdq11O59pK+dKx6nScfXsKPsVbMMOVOHD54KOfF/svAEQuos9gx67SoU+nI1bBDApnzYUWXqOPXz9/i5dc+Zn1YidzYJCju23HSoddJnHp8H3p1saMV8uDrfHmQdB3Cvy48mv13tGOIVbN+5UrW2A/g4F2NxH5ZwitPvcZXPoUMTr2+OPb9zubs0w5l/12cmCU/tWuW887cN1jyswePqBVwpM9cQzOEyu4cKdYRf5IJUxfypVekmJaxyzlTGLrn20y4+SU2mozo1HHc1V5SBgeW3tcw+YYyvhg3goe+j4G+Ez1POJfT+u3JLhVWDIoYkYCHmpXPMf2JH1DKXWvSkaub9q1lwxaFHMkWdFn3+UKe/t9i1iOa3YizG6HHjY9wc5c3uHfyQlbWiKYaTsqPvpWZF5n5avZ4nlxDJnJVSZf9juH4AX3p3c3RIHfnz32LD38VzSbKqTxhFONPdrV4maki8RWvjn2AhTXhHF7QpIv68/gxit/tJa53UW414+w1gJOPOYAenc0o/JtYU29iz52TLJ4yglc2GVBKMULeANoe53DJOYen5X24il9WvsfC+ctY7c7/nqSvoHu/4zjqgD3Z0WVEmwoTqN/Ax0/M4PlfDYVrroIhrMePYsxZFjY+N4M7X1+DF6PcTMHc5VCOP+Ew+uxeiZUAdWuXs+j/Xue9n8PyvXmyDyOuwtn7SI46sBe77mDHajJiUInavS2sfHIyj6/w4w4q0o0LRKvoWi9JnQ27RSfXAAtZ6Y+JZg0OdNrenHX7xRyRaSMty7+ZU3nyq6ZpgSpcfS/mzIM60qnShd2gQgrXsum7D1g4bymr5Nza9BMPBDAecAEXn9ybbhVmWR8JvnJ/N48nXvyempBoT61g97Nv4aK+nehYbsOslUj4NvPzynd5660v+CmUHU+AKzfavU/n3LOPpM+uZVgkHzU/fsybL8/n46psCpVwRtUTsh3AiWefwCHdu9DZmd6PoHsTq+Y9xPPfJkjSgb43j+T8LnLyfZMnwfoXR3DHe4G0nBNNjTINLVqmYRmdD72Y03uX0aljpZx2l6XL3CdfYqlbOB9FaqUAV16MvS/ggtP7sm8XByZlnIi/npqNq3jviUeZv16FyXUUl824gv76TOwiuYGls6bywjoFsVBIvpjZLNf6ZvbjgHI6VJbjMKqRInVs/u4D3pj7Ad9Ui4YTUdQdjuSKMddzVGQud01/g9VRJUqVDl33Sxh67Z6Enh/PzGURuS1+buRK0eo50jQ0tNCIhlgGNYpkFL/XS0RhpcwlmlY16Rac5ozfELlKt0r3qhx0sivw1NQTFPeuGSzYRBOvpiBGbhpVKzdnspWXYc7iqwLgSqFXozY6sIkOilFvuu7TJeqQG3m65W6BsnuHWMiPzxeS7+wyGDUkQmnQZzBbsIj76FpRSSVwtT1hQWnsbUGBBnAlqfbllHGXMyD+DR99vobNnhCRpAa9TY3/03dYUS9OogJ198u49YqeODZ9xPufbsJn3YuDDutB5/rXeeiet/lWbtupQNPjcoZeuQ/2jct4670f8FceSL/+PehU9xoP3v0W38n1IlqMZS7spt055tqzOah6Hg8v2oxoZBwP+6n5cSXfVsVRmA5n4P3/psfiUfz7+Xoquh/LaWf046BuZYjyJ0VsKU/d9AifHTGNmaev5fnhz/GR8NDmRK60vW9g1kVBnrzlQb5S6FF1v4gxV/cguuINFn6yAU9Ch63DTuyu/pa5763HL1qwqztw4PV3c9O+G/lhyed8Wxsm5ejBYf32xP7jo9w2ZSlBbQpvyER/GVzN5e5J81np9hOMxlEdOozH/tOZj2eO5aV1wuhqpIvl1w9Y8MFago6eHHnsATL9Hrz7NT76RbR9bUwLSImwuVd0wMukB4r2ttkW9aLJt7aCw2+dzX92eIM5k95g5c4XMfPGHmz473DmrMy0YFeXUXnI9Yy5fBdSXy3i3W9q8UUlVCYXnWKf8+T8NWCyYDOb2VUYhb1+4vN5S/hSdPfboQ9HH9kdx6ZXuX/OO6yJCo2iwLDDDuzgtKHf63SuODzKx03AlWgZ73F7iLA/Vz0wnOPiX/Hu0m/4tdZLOKHFXGYk8OkbvP9TAkNZOba9zmDgNf3oVPsJ7y9dzaZEOfsdfzy9VEt46o4X+TSgbAKuBNpKtwIOiJQeuau9FoPFilWvJrHbpTK4+vmhW3j2Z3ODwSklg7ir6wlEEnKdnlK7J+fcOZEzQgt49u1vWFdTj6cuQDhjf7QXXFl0u3DUbYM40fsaj7+1murNv/LrlggKKSRfaJj2hmdb9kaRu9IbenHJjBGc7Huayfd8ws91Vc0utJXTZVI2yjvsz8m3/ZuTfE8y6Z5P2SKBpD2IcyZdSL9V93Lr4z8SyXhoRSdJXyiC3PFfpUFvsmIxigtNVXQ4cyLDjjChFIbhjKm8sL5ld2Giw+ncels/DPOGM/qVDXijSZl3yvWJNLjqW9twPnD15JDD9sD1y1PcMedztkiiLbef1F6XM+Gmw7GsW8KijzcRq9iHvofly4PEzhcydlB3ou++wNueAzjpjB44f1rIK8sV7HHisey1ehY3zFpOQKnD1ucaRtx4GJ1rl/PuklWsj3Wgz4nH0ZP3uWfU43yVatKSP6zD0fNihsnrGMnkNzayRa7fc1I2YBQTj/2e+666hw+iOjkyrIl68fjDcOAQHvuPmmdvmcOH/gjxfW5i1k09SK1cwKsfbSRuKsPhqqCj/30efrMau5ySnAFXvTbwxaJP+CUQh/L9OfTQbjjXPMqQcW+xUQK1xs6htz7KoM5zufv21sBVHaqDhzLphn0xr1vGhys247ftLcvdTnWvMmnkC6wzubCUdaFrmQ5z7/O4fP8NzHvofdarutH3spPYe9Vj3PXG96xb/StuKZcX0t789NUAGX5UqdHpzVisBqwH/odhF+yM+sf3eOOjDSQr96Z33z7sZq3hg8k3c99XCSRU2PoOYtLAA2V5//6nG/EYutH3yP2prH6VKeNf4idlmvckx8FcOPB8+lpq+WnlSn7YWI8vocPsNFG18L8sCYqmME0iVykzFf0vY+CpZVS9ci8PLa0nKrqNev2kdj2fscNOpFPNxyxc+DmbjD05/MQj6Cm9x9233sdy0s0QpOTOnDL5JgYkVrF8xRo21XlwB1JorWq2vP86KzziKgkrpuyVGYXaiGevvFBYcXY2Y9BYce5/dlr+FQRXWT74ha/e/lR2KqrKe9D30N2oXP8/7pz9KRtTonTKQ3j3q5h+c28iX7zNsu/rCaT0GB0V7JRYwSvvbSAa8cjZH71veZTBvX7iyzc/44ewCl3Hfel70M6UbZnH/bPf4Qchn4kSdJ7EiGEnsWPdJ7y7ZDVbVF3Z7+h+8vl4evrLsjwVBm6AXblk5CD62Wv5deUXfLfRLe+HyWUj9ulLvL9JTSqlxbHLrnQy70if807kwKyelhJEQwLMfsmq6jgqjSH/KpMWaSjoMoOb9vmFj+YvZ2Ncjbqip0yXsp/+y5DJH5OwCXAVJ9rpbMYMOYryLctZ8uka1olrlPqcwsk9InwxewwPrQjijxjYcZ9uVOiNGLqfwrXHqPlYBlcS/poauRW5aCTUeC5/4ZO57/JNSI+jy/4ccuhuVKx7hvHj5/FtOA3QtLtdwISxp2BbdifD7v+CUKcTuHbM5Ryy8QEGT/qAgCud8VK4FXvTc6Rv0D+io60vECYqhLJSjVZvwip3RBZf3ZbgKilH4erF/WgKHVbhFIiH0t0BRWvdFp84kagSfS7yaqHmqjHjRyIeS6DR5l9KFRGdCptGrpIxwqEgwWCYaBJU4ooRqxXROFm+3sHnwx8Wc1ahMRgxidpPffNLf0vgaluY/6UxticFmoCryzh61UyGPruOeIGvSspdOPK2mzhTMZ8HZixglSzIFah6XsXwy3clOW8cUxYHSSp34ahhN3OW8g3uu3N+w3vqfa5mxBW7kXh1LFPfDdHgP8vJ5RZpgbEm305WnsrNw/qje/E27vhyX/417FIO9S3ilde/JXTg1Vza8zP+N+wFPtp3EDMvjLJown284d2To4dcyRHfz2L8vGq0Rw5nyoBveWL0XL5Iqik/bSIj+6/h9bGPsMjfUu1HWjnesrdIC8x0Q0NDhzMmMLzfRhZNvpvXaoSg0rPXVTP4jwBXt7/FDyJqJkyWPoNyPNBqBP3aQ5diNl4yHsIF486l96d3MPzFjURN/bhs/Nns/+VdDHvqp4yhrfoN61Wi63M9Iy/qSnjuWKa+F8rJbVagPPhmpp+nYvnMqTy/rrnQFqD9tAlXyHw15JlfWuCrrhwxZDDnGBbxyPS5fBFM00/qeh633XQAqv8bydT3I81yqlujjwyuCjS0UNW8nrdHknIvjr7xDA5xWuS2zqJ9frJ2BW89/T8W/pyQW4nn3XPVxqa0NV7zMgAFqv1vYMIl5ax/bBwPftU8d7zhk0onu51/C//u7eaTOTN5dm16tKT9WK4fewJdFt/OmNdqM3dRtTHRzgO4+PwD6Fy9gKefWcEG0emv4KPEdtxoxh1fw6JJ9zC/LvcC8OLOR7F8HxPgauDOrJ8zjid+6skJY67jiJW3M3JuHRVnTmZEz8XMnriQ79l5K/il5XVIfQb+ieUG+N02Tpo2lXPUxchTDV0uvJMhnV9i+vSl/Gw8iisnnETZK7cxfakAQsU/kmovThx9AyeGX+bumYtZk0kplPoMYkZDZK0dck1hZreLx3Pjfpv45P67eeaHlubTyFePjXkf/2k3cNVBUdY89wCPf+YjLoz6GKgNu3JUg9x4nnfXph1T9j2by400uBrEkd/dxbD/FZZDxVMm+2Zb8i//fHwhCfmoofzUMQzvX80Hd8zi5V/8cnvunS+7n9uP/Zkn/j2VN0N6TAY9alU6rU/R0LRHw26X383g7u/y0NDn+TwhkryVGA8ayNjLdiHy6jimvR8mpezAoQNv5RzTuzxx12sN8jTV5VyGDupD/NkhTHzHj5TSsveVdzDkEDefPXAP//tR3PnV/Mlmo+SlBRbQ08XTr7DcqDx9nEyX96bOYG6V4Kuu9L91MOfq3+LBafP4KtPAJ6tXV8wcxZO/ZoGJrDFI9ryau65w8MmsQvqouO+m16HG1v9mbjvDQvULD/P2HjdybbcveWH6kyz1NOq59JUjesrKGh14xdNh27zZvJZPtNgXF0+nMDic6CMe3GHQiWitVtQAK+UmFo181TgP0Ugr+6+491MjltomuMr+PkE0HE9fXi0lCfvT9851sKmICUAVihCOivsvFfK9j2aLuSDQE3e/+f1+AgJkiaEF0DLacOakkZfA1bbhndIo248C7QJXyfJTGDjsCCoXTWL8AneDESepunPiqGs4qeoRht//JfVlp3DTiKPk98bOr8t77+Qx13HilocZ8cBX6ZogIRLbAFeJnc9n9KD9CT55C3dLA7njUger5ozniR+1lJ0ygdH9V/LssBdYuuNFjB3YhV/umcCTifMZNvhwumx5mRl3LCFyxhRGdJ3LlFkfsUl48nc9j8HX96Vj3ecsffdDPv7yJzbLUbfcp5AwhsS+/2bm5ZWsvn8kj34vjM3iwJWgX7voIu6PsGhRZu3eVAhffSgPfKb2uY4pV3Tg54dG88gqEcZzst+/J3Blxzd5ePw8vswYRL9lvZK6J6eOuYbjN+fvmwDW2wJcpcHzkZQvnsykhe4c0N2Dk0ZfyTE/zGLwkz81rFsqgi7pyFVPQgtnM3dtIyBQxOvZ8Gu9fHdO00fSOujU/QhOOetI9mEpT099jg+r6qkJaXC4rGj0NjmlrrX9yB2z0HgfN20PLKf0jeOqigU8cPt8vsnsV7PJKRzscvYgrj9UwaaXZzFniaeBHkn7cVw/9ni6Lr6d0bngStmFQ2+8njP8TzPxv980nLd2iRNlFw4fcivnxZ5k4t2fyJGyxqe481Es39d1zQVXe3PsyP8w4NvJDH+lBscpExjTZykPjn+Dr8pPaxe/yPNtZR1yxOxPKTfG8cQPEj5Nf0ZOP4cOxchTRQV9bhzD5YF7GfzYagLy2nZlw32jeOLHfM9yW3yQqDyVwcOOxPXWhDx539RpVOz+etR9OGvCJRy97hFGPvAVnhaRXqYhSM/VfLG2Iz27bGLZ44/w0upo2thKhakXaZu7XsqEicdTtmgio174GXHvlK3cjk6VLzeiyQSxqKjrHcxR39/FbS04edqiR/O/tyX/CoEriO9zHdMv78CPD47g0S+C1Ih6wAMuZ8h1B9Ox7jMWvfE2i5d9w68+EVtP10umHw37D3yM4ft9yLSrH+CzTJaewtyPq2cN5IQtjzDywa+prziOgbcOwPLWZGa968uTpyeMuJR+39zB1XO+JK47kKvuHcTR6x9jzENft7gfvw+4gkSv65lxRUd+eGA4j36nIVF2MoNGHk3nd29n9LxGp1GW/z6eNJA5P5hwWI2oleIuzCDxPrfywDU2Pino7GtBXjX5bsM+K8rY+/KhXLuPyDVws/rxaTy4MsODAsglxCX2dfhF/XKBS4vbz09b94tCjVLEVTfi3r4yObcvfd+mPxAlmkiQamSolj+o0OOodMmZQcWDK1FjW0swfdM8CpUBm8uJKN+K+aqpDYLeaMJoNKJvLecvO6tEjHA4SDAcR2MrT5dVZJ4SuNo6Xin96vejQLvAVbzr+YwauD/Sy8O588Mcr6NyBw65+TbO539MnbmMn3cSYKg3qZeGccfSjPdBNnB24NBbhjW8tyETumobXF3A6EH7EXxqCA+ZRzDldDdvT5zDG/X54GqZcQBXjzsO49O38lTlOEb39LK5IsXaGQ/y4ykzuDYyk8FPrs0YpQp0Ox3GgOP603fvciypejaufJf5r33AN55GZdY8cgXxva9i2jU7s/6R23jwmzS42vPKGdwo0nsmtRy5Suws1lE8XeL7XM/MK/fMNKIApXsh949/g28zkTGhbHc4dwpD9/+MF25/mU9kz54SQ7/BjD05yed3T+KZn7PAYuvXK/btkFuGcUFmf7P7tq3AlTBuxww6kA4tBBDVqx5h2INfN3R/apsuNKQF1j1+E7NXtseoVKDc9zomXN6V2meGMfPdGqp9aR7W9h/LIzd0b2U/Ch3c/PHu/iQ/MpW0H8M1o09i96VTGfF/VYWjTgobXc64iRv6qal+eSqjX1xLOPdT+kO48cGBHPD5HVx9z4qGO8qE5/fIoYM5x/coI+5f2bIxG/dTUxcs+G3FTucwZepppF4cxr0f5ZzljLFXzPloie8TYQOHDp8iy4MhI+axcdfLuHtcb76cOJBHf9id82eO5chvpzF+Xm0euPqyy0Xt4hcx1UTHM7h16OFySuCUd8N5HvqU9Zg/pdz44s6bmfFZHHa/lLsnHtemPN2IGqW2JyeNupQDl09m4kI3yT7/Ydo5IRbd/jCLPJBIpIqOXol9GzOoN8kmcrwpuCpWrq0zHMkVE09nn+XTGfr8+syVG4XOTCadbj8/wbgZU+xL3nr0Seb/3Mh/8gXuHc7hzrHHtSE3vmBTjbhwuA9XPXArA76b1WIEvf1qt3hw9fio/2NFKi3gYnteydRrurLh0WE89G1WNinQ7niorI8O3qusQR8tmL80Tx91vWi6nEkhMjDke+UyerXvzbfJ8nnarI/4eacLGD3wgBbpovnuUYY/9A0e45FcNv5UeT+Gvbixxf1oiGa04QQtnn6FQU58r6uYdm2jXm3gq5eHcceSnL3PZoTcNYLZK8KEY6KQAFRqPfbjxjH7Yk0LmRTFfbdxHQo0+/+bMZfujj22ivl33M/CmoySygD8mFKDye7EkneRUvGU+K1viuhToQhUe8cVe5z75I+ZJB6XUGvU4pIYEqKrn6bJVSPt/WDp/RIF/uYUaBe4aoxcTWT8Ak9eROqEUddwclXGc1aWjXC1/l5j5Govjht1HadtmM0tj/1ItAnRRWRj0NB+mF8dybTgtUy7UMNns6bxwnodzpPGyZGr50e8xEep7hw/6ioO/Pi/fLjf+ez19iw+OmQEJ615nA/3vYxDPh3F5HeCDZ68hs8YO7Nb734cM+BA9kwt5X8zXmS5nCqooZAya64cNXS7dCaDui3gkfHz+Sqj9JK9BzLjQhNfz5nAU2s1FEu/LF2S1p3ptbM1k48ttPIWfvquqgFkSMo9OHbEDZxUlnEV5dEtiXvBBCa+2ei53Nr1ilS340ddz6m1jzLq/i8zHfDEaAo46Camn6vk01nTeH59bspY+mvpWr7W002zdHF+cD9PrWxqwIMyUsO6qlADv7VFF/Hd+K6XMOHf3al/4mbmfJl7b1DbJ1q0Hb95aD+s80czZXFj+mox3y00ekvjyV3+DriRKeebWXXfBJ5semdQBsDYjriJoafa8b91D/e8WdU86iZHK0ZzifNNHps8j5WxtAEgwNURt94ig6uRD3zZSqSgJZoosR43mrHHVrF48uwmKYHiN8Wdj2L5vrbLBYy5sSsb7hvPUz/vyF4n9GfvTQv4vy+C2E8aJ0euHp64kK9cp8oR9GL5RTgcWluHpNz7byA3viV5zEjGneRstaHF6xMe4W1fEe24RLppxakMvK0/HRZPYtz8xkyFrZVrHvUBnDXhQo76+RFGPtxaJDWHr+7ZzA6XnMtRzl9Y9tiDvPBDrAEctkduFCOH2pYMTd9oS/4Vdz6afbed+qipfK7N6N+2zkdCU+x+ZGS5snU9XTz9iqOLcIgMHnIY9jfHM/GtRj3WlP9ywVDr+qi472bHS7mO4LKbT6P7hk9Y3elgetY9x91zlvFLos0ejMWTovRmiQIlCvwtKdAucCXXTmRqru6bvoDvZCNOgbL7FYy4cne55mrae+F0zVUR7zXWXHWl35BbODf+DJPvXi6n7eU+kqY3Z46/mMO+msltC3flouEns/uq/3LfS2tIDhjJ0EO+kMHVx0k7+1w7iYvt6/GZ1vLm7S/xxX6DmXy0mmqbGc9/h/PwqpajGMl9/820SzvzyyO38tC36cL04sCVCscJYxh9TD1Lp8/klc1p46WpEiiWfsVehyeUz5Ahh6JffB8vfpe+X082qhWd2O/Cf3GU+ykmzP6MTLO9Zgxc3HoVKHa/mNuu2wftwvFMesufB05FFEmkuPz8yCge+rZwzVVb4ErQ5YihN3Fm7EXunv0+P2XAwW85cVsPrhRoDx7E2HNdbHh8JPd/WcRdTK1OtLXxNOx03hQG77uc58Y+z0dyDWP+kyw/nn/feixdv5rDHc+szQG2ue8pUB/wH8Zd0JnwWzOYubAGUZfdGriSOg/g0gsOonPVfLnman2hmiuRSjd4COfGnmLSPU1TAosHV8XyfbRrDrham3tOVbITJQuuvqFb+/ilrXUobH8LuYG1M50OPIeBJ6RY8fgrfOR3ste/ruKk8IvMfu1XooSoX1+NryHy3foJk5S7cvSwgZyhWsSjM+ayUnQf/S1yTWFlj0vHcn2P9Sy//16eW1u4xqep3P1MvxdHXHEFZ+ywnmUP3ceLmd+1R25sH3AlUvxak3/F6o/C+7C18lno36LkadH7kQVXrevp4uV1cXSRNL04ZfSVHOd/iZl3vc/P2WsEmjgtc7+7rfZD0nTlsBsGco7hbR65+w1W7XQ5Q6/eG83iO7ljfk2mlrn4FZfeLFGgRIF/FgXaBa5yuwXaN3zI+59vwW/ZkwMP78kOmW6B6S6AjV0FW38vS2wtnc6ewJBDI2z8cDHLNyYQhkKHmvm8vEI0UDCx26UTubHbBzw+eR4/dL+Ua87ehx0N4ksSkm8RT46fLzeqEEbYyAFGEh/OZMyLGwmbD+fiUWdxoOYb3pj4IG/Kxagqyo6/ics6reGbHzZSHUiAsZJd+h7F4ZVrWSDC/7Xp4uPiwBUkO5zCwFuOoptnBR8s+4FNES2mvY7m5J5RVmYiV+2nS2vMqEB7+K1MOT3Kh9Nn8Mrm3KiRlp3On8zg/VbyyvgneT+oadd6B/UO4Vv9KZ+srsar24Xe/XqzS/R9npnxEp80qRlKOgZwzfCT6F73EW9/uA6/qTMV1W/wf1+kG18UZ9QoUO1xATdd1YeO7q/49PM1bPAmkHQWnC4Pq+cu58ciDcIsxYoDVypcR13DOZW/sGajh0BSj2XHXhzUpwuuLXOZc/d7/NhSDVTBrWnneMouHDZ4COclni7oVBB8ajt+NGOOjbJ63mt8klNILT6fqPqObzenPfmSspKel93E5T0lfN8uY9l3NfhUO7HvcYexz4bHmkSucO99pgAAIABJREFUVFScPolhR+hRyW2kp/HihuZRxyx4N7w2iqnvFWooUuz5KE4exIsEV9+mlO3il7bXkQZvf325IdYxljEHLOORCQv4WnUAZ46/gH3eG5fn+S9exaX3bcgV+1BRt5KlH//I5qgGwx5HclKPrZNrKVc/Lr7xTA4w1bJu5ed8vc5DMKVBZy3HtnkBr64UcqM5X0nGvTjmuqs41fk1r937GIuqMs2UipQbxcmh4imTfbN1+Vfs+WifPmpbPhcvT4vbj2L1dLH0K54u1iOGMOy0CpS/LOXdFdWEzTuwW+8D6OGsY0WDXm387rbZDx1lJw7ltiM8fHzvPby8XnR/MNL5rBEMPriWj++ZJbd5Lz0lCpQoUKJASxRoJ7gSw6gw7nk8px2fvvdEF6tlw9fvseD1ZazOM7yLfS/jFTPtwaFnn8Yxe1Vi1yaIeTaw5s0neWy5V46UpETnuBv3x7B4OnfOryastVNWbkaf8FFX4yWcMb6T+1zHtEsNrJh1Jy/JBqOBLueP55Zd3+K+299mtfyeGkfvszj5sN3YtZMDi1YiFfFRv+E7Vi6az5ursyloxSqBNF0Me57Mv045gO4dTOiIExE3hG9ZzYcvvsSHclfB9tCvDaZVmNnryslc33k+D05ayLd5kQcF9LmRqRe6WPvISB7+Vt+u9d7Uaz0//aKjrIu4P8hH7Y8f89bcN/msttCcNNh6n8uFJ/VkF7uSpGc9a956nEc/TqdxFG/UKNDscAgDBvRl326ifbIKKeKmdt2HvP74Yr7JdIoq9igXB67UOA86j9P7dqVLRzsWdYKoZzO/fP0Bi976nLUZT32x3xR81Z7xJHUfzphwEcf8OIch/822UM/9Wjrd9D/7JQpcBpoitHgqo+fVNKbnqsvo1v8kjjtod7q69GiSQbxV61m3/FWeXVqd421VoOx2JtdddgA7Vr3GYw8tLXDPlTIN7Aa0lBIo5tm+89GW3CgeXKUdOMXxSzHrgL+F3FA46HX9BK6O3c/wR1fh7ngmQwf3wvvwCB79vj11h7k8qELf7ShOPO5AenV1YVUniAU91G9axdIXX+ZD2QnVPrkmWffk4GP607d7Fzra9WilKKH6Daxd/AyPf+wh1QJfpZz9ufSWM+jj/T/uveu9TPfC4vigeDlU/GlPv9ma/Cv2fLRPHxUnn4uji1hB2/vRSBOpDT1dHPWKpYs45hY6HnYGZ/Tfi50dWghu4ie3hd12DPLJ3ZN57temfP3b9yPZ8VQG3nIYzkVTmPamu6FxUMrYh38Nu5jDa59uaKNf3HpLb5UoUKLAP40CjZcI/9lXrjDR6dRbGNRfQ/2yucxb+iPr/CoMVgXhLW6C7ekx/Gdf6x82v8JK7w+bTunDfxgFJGUX+g0ezLmxp7n9nk/Y/Bc9X3+XdfxhjFD68J+IAiX5LEfzjxvJmAHrWdiO+sE/0SaWplKiQIkC/wAK/HXAldgMhZWO/f/F+cf2YEej3E4B4st5edT/WFKgXuUfsH/beIkl5b2NCfqXHS7Z6SyGDOmLft5opr6X313vr7Sov8s6/ko0L811e1HgnyafVTgOOpMj7VVUe8PEMGDuvA8HHbwLjq/v4/an1mzd9RLba3tK45YoUKJAiQIZCvy1wFVm0pLShLOyDKsmRri+mppAS4XRpX1uHwX+acq7fdT557wtvMOj5C6B70yZzfza5vVYfw1a/F3W8degdmmW25sC/zT5rKPD0VdzwSEdqbQb5PTRYP16fv5iMQveWcWmbdD4aHvvWGn8EgVKFPhnUuAvCa7+mVtVWnWJAiUKlChQokCJAiUKlChQokCJAiUK/JkpUAJXf+bdKc2tRIESBUoUKFGgRIESBUoUKFGgRIESBf4yFCiBq7/MVpUmWqJAiQIlCpQoUKJAiQIlCpQoUKJAiQJ/ZgqUwNWfeXdKcytRoESBEgVKFPgLUyBJLBxD0hjQ/VVLF//C1C9NvUSBEgVKFPgjKJAHruS7gW7YD1f2fjwpxo+P38ycL7V/xNxK3yxR4B9GgSTxSBw0ejSZ64OkqB93QMLsstLsFCYjBIJxVEYLhjzDTSIWioDR0Pw37aFoIowvBEargWLsQinspsoLtkoHhtIdm+2hdOndvy0Fong215G0dcRl/A2HIlzPJr+KsgrbbzvTGTpLUpJUSkIS/4p/UhKpVAoJFVqDluztZX/bbSktrESBEgVKFNiOFGgGrib+uxs1rzzEG+vVIKWI1q1jS7A9SiGOv7oGv9JGZZnpdxLSf8Q3s7siEfXX4/VHiYsrJS3lVFjacWFnMkFSpc7Q6Y9cx3bkstaGzlt/8XOIeaupCapxdnJiKP5n2/5NKU44HOe3XQOlQGMwoCFE3WYPCkdHnFl0EvdTXeOHQnwV91FdE0Lr6oBdl7O0RIi6ajdxg5NyhwEVab7yxduepULvoKPLmL7mIOajujYA5jIqrG07WKRwPZvdEo6OrlbBlSRJiH9LT4kCfxUKKBQKxL/tf9oBrqQkSXHzeoEnFfFQ41fhKLOgbWEaSpUqc9l4glB9Lb6YOGc0njVx7gqMLa9LoUCpUKFU6rBW2NC3f6GlX5QoUKJAiQIlCmQoUABcdeHX+8bwxJp2AIQ8cv4RAOGP+GZm0VEvW2oDpDRGjDolar0Vs644JZyMeKhzp7B0cGaM0T9wHX/AkWi+/uInEfdXUxfUYOvg+GPBVQb8JIqfuvymABeNxpoaS3kFFnUBcAVEPVuoDamxV5Rhyg0htQSugESojmp3DL2zAqdBBckkCWFcxWMkVFo0yuyEJeJhP35/mITagMVqw6Jv9FvHfdXU+FMYXJU49K3zdbHgSnjIS89fnwJSIow/osRi1mWM+t9hTVKcoD+K2mRGt7XhlVSMcFSB3qBp17yVyoZD02yhyXiUREG2jhOo85E0ObDpC/9eqdWhUcTxVVfjFx66rXw0lgoqrGm9nYxHiCfF3ZAK5H+U4j8lIt5a/CkL5S4zGlVxemorp1P6WYkCJQqUKPCPpUAJXP3GrU+G6qhyJzGVV2Br27mfD0N91VQHVDgbPP3/LHAlDPf89f/Gzfir/Dzm5f/ZOw8wqaqzj//u9N2d3ZmtFEUpKlZAESuKjajYYu8GU4QvJnZEVFQQQaOQ2I0ao1FjErvYRexRQBQbBkFRUBbYMrPT+/2ec2d2d3Z3ZufMsgjomefJ8xj23HPO+3/fc+7537ec9Y3hrh4nPTe5IhmiaZ2HWEkVfStL2g+E3ZArSBBsXI83XkKVIKDGOSpJ2NOEJwx2ZzlltiRhn59wykppRQXlpbnCgWL41jcSwEltXTlEIiTyOJ1ECKNXhBG6y0l/XzBhLbF3CSlU5GpLMdQC80wEaBR2XFVL947NFGFPAy3RbMPRsFbUUF2awtfQREgQgbafhsNdh7sLmRf220hLspTqmnJ6+vmPuJ/GphBmVw2V4sOD5C8/udIJNdfjjeTpSHzUMEhOrp9GSVU/KoWrKIdnKRkN0OINkDSZSSTAYkmC1YW7oiTrA0mm36wx9HiESJeFGifo8ZMoceHK+oDScVYqLFDSHFQzhYBCQCGQFwFFrjbQOBLBRta3pKiorcNZ5Nu+K7lQ5GoD1bFlPF4suTIi9NbTGLJSmZ3PZPQTxV6Th9jHwwQTVspEQlYyQTyVPtym4iECviCRpI5mLaXS7cTSevLTLFjb/k8GzniMuMWGVYvRsr6RYDduOhGG1BY9pTlw11VR2un8KshVKh4lljRhc1jJ7w+AiGctXs1NX3dvBCpF8K71orv7pg+zG+mXCDTSGLZTU1sulau2kaZRRLc6iUiUlN2RO+QsGaSpwW+EPXf55SUOFpw1NTgNT2vKCFOLOetwZz5ACXv2maupKUvSst6PpbqasoydRLxrCTs6e0pTRLxNeMOJ7kNwNTuVfapI87IUiViCrg4lnUTIhz9hx1Xh6GJ/mvDsmsU66Wij3XmuWnFJRCPodkcW8csdFhiPhNHtJXlD/BDhxr4WWkIpHO4qXPiobzFT06eEuNeLL26mrLyC8jzet3igCU9HxircWcR1M1ZLdytOhQUWsXBUU4WAQkAhkBOBjUiuKqguSxH0h4gkUmgWG46yCtxltqwveCLx3kcgECWaSL8ETaJdaTkVTnt7vlYyil98YY/GjNALo68SJ+XljszhpZWUyIwpbwnJaBCfP0gkJl7oGhZ7CWXlLsqMoPcceSymUqr7VUrFq4eb19Acbv+Sq5VW07/SnMlXk5NDj4Vo8QeIRAV2necnL2drSyGvPxDM6q/UCBMrySKN3WMiekrjErBWUm2P4g+EiYovqBYbJVn6zy2/8flWyiY651zFWtbSGC2lymUiLGxO6EyzYC9xUuEu7fiVOxkl4PMTisQML4zJ4qCkvBxXlqDRlrU0RUupdCbxt4RJYKGssg5XvkN5MkY41uHzewcFaBYHDmuGwfSAXAlchA12+Pod9bK2KUZpbR2ZaKC8SjfwCiTaiU+X0ETxqI5uKaeurqJHXoHiwgJ1krEoCbMdezfhSVsiudIFqY2bKS21dUsci1+hG+kJYY/NCZx11ZTmOncngjQ2RiipqaQk83dRAEGEmrX+9FQKrTVsTo/gbQhiramlvFfIVZKItxlv1ExFdSWlncm/sNx4EI/HT9LuptqVIUzGvP2kzBayptotiKlEArEX1xoLqqONFiRXhtzNhEzl1FS35kZ1JVfJiIem5nDuXMZElFAwSCAYAUc5FRXlOASGoqCFIFd90wUt4mEfvpYAUcS+WkqpowR76/6SS8JkmOb1HhIid7PYr4AbyexUtwoBhYBC4KeKwEYjV74E4riPQ1Qss+jEQwFCcSip6pPOAREviMB6GloSWEqclDosmPQk0aBop2N311FjJJhkiFPKRqmzFLsJEtEA/lACi0i0N2LxMkRHYkxZRSbDHho8IVKWUpxldix6nEgoSDhhoayq1ghZScQiBhHwBKG0soISsxm7XS6OX8TERwJevGETzsoKHBY7dmsiXXhAQg492kJDU8DIkykrdWDVMtglzDira3FJ5n21HY4iHhqaQySy5A0LoqU7qKytolQ4PyQwadNXQkOz2CkTOjPrxIJ+/OEEjoz+c8uvSdoE5CJXDcEUJs2Co7ycEqsJPRrA64+glVW3ez6SYTwNHkIpC6XOMqM8ciIaJBBKYHPVUpM5eAhy1RjUjSRve4kdkw52QTTzRREJotMYImU2dTpQ66SSOpaKrEInPSJXOSw37GGNJ4krOxdL5MEEY5mv9WYcznRuisCrMVlOv6rWsMIwnjUtaFnFMPRQE/UBK7WCXBkhhwHSTqpMTphBsmP4G70kyqq7hFQVR67kVuKWSK7kJNuMWhUiV6koAX8Su0t8pEgQ8bXgi5hx1bkx6qjoUbwNXhK2MpzOMhyWJBF/FK2szNivDc9V03p8cVEUIi23yDm0lNdmPFfNhFMd/2Z3ZzxXqSh+j5dgyo6r0okWFlUwnWRHtSUiXrwiJq+0kqoKe/v6M8hVlLI+rTmthTEXXjO/qZVcdWxfkFwZL7UQTY0eolYXtTVOrHQkV+l9W7wwqqh2O9o/IMZDeFp8hKNJMFkpKa+gzN5aoEIkUUYJxU1Gnlh7umSSWChAMBwzvNKmkkpqq+zEmtbhjXaWNV3cIp2DVfjnyC6qU7i5aqEQUAgoBBQCWQhsPHIVF2Ehdbhaq5iJL2frmok4qjIHvCjetY2ErG76VGdXFQzjqW8mbM+0yxQMMLuzS9mmc0f8OKmqLMXS5kUqNKas7iN41jYRFl8g67JKYBtfJpsIGvkn6S+IGyMs0FcQuwzhxNlxfoik6AYClBv5MfJRiplkasqoqXVn8mXEQUFUqvOBUyRKJyUxaSW6Flx1dZmwIIF7u177V6Xr+3UNi5S0CXHE71QtUHiuGgIpSir7UNUWh5Yk1LQOT6yUmn7pg2BrcQhXbU1WGKdO1NtAY8hMZZ9qI4zNIFeBFI7KPlR3jmvLZUYZL5KzS3hojJa1jUTLep9cJQLrWe+zUtk/q6hH3E+TJ0xShEQlTOlCGdYMuYqXtH/V16P4Gv2Y3NU4M1+8U2EPTRFHmlzpcaLRJKlkmJaWuJFTmC6CKdZeA56IFVdtdYcCGxtOrsSh3EcgFCWhi3wtJ7akj6ApKywwEcHnCxA2vMkmw5vsFF/320hviljQhz8UJS7CHs02HOUVuIzP/53CAlNRWpo9REwVVFeJfQRSsQA+X9rbjmbG5nBS4WotRS+eF5UbHaSCQWLYcVVXdiHcHcICBUltSlHmMhENhImJOVnslJa7KE8zD0iE8QovRDyZkcmBs6IiI5MYU6zBUvRIKB3aabJR4qzoUHik+3kLNpMg7PMRbPXWWsUYLkrxs74xSNrnqhkfGHLbu04i7KfFHyJuclDhclGqRfAFktgrnNj1KEG/H38kicXwuJRiy/KCiY9rSZsz7YUxQlMjxOJxIkmL4dG1mBL4g1DmcmKKh0iYSw2SFmxqIKiVU1lZZnxAMjxYMTPlVZWUaTECPh+BqEaJ253RcdbiTEVo8cZwuO2EGzxEui1QacdVV4kl1EzQVIFbfE3q9JMiVwJqYUNRK85yEX3RkVzFQ14CiRLcFZ2LgIgwQOGtspP0NXbKT+vuvSVytvpQYY6R0EpwWEFPJkhkQoCNFRv20BwQ3LOyY0GcvN1qmKytFWxl35mqnUJAIaAQUAi0IrDxyJVBQLLv5mkP3aurcWbC+ZIkdTMdooIMz0IzYWslfapLMWeS+aOWUlwVThwOa47y7pkDvdSYEsrPeCGsOQ7WaW+b4BvpUKyNQq4KySFIz3o/VNRSm05qaPvF/Q00BOhEbArIHA+wXgglqk11KCOvk4gn0CxWzLG0Z6YwJq26EOFl2QRP/Pt6/CZ3W4n+3AUtJGwiH7kKWnD3qWnL3RBSp8mUnSqDgLSTt7qq0g52lDK+KIewVaXLoKc9V4IgtuaNFMBwE5CriGcNzfE8RNogxmFKsshVQ1CEBbZ+uU7fbYNm6uBN0C3OjmGBnfoxUDA8FU2EOpH4DSVXsZYGmiNmnG7hBRbeTpFzItzdlWnPo9gbGltI2CtwGWHD4kDqI5Cw4a5xI4qxGcRGHCTdLsqsGklRZMMXx14l8tKyyJUtTayi5orMBxqD7dPYFMHkrKBC5LKk4gRbvIRNLmqqREl78bwgY3bKXWnbtti75ox1IVeNQXSbk0p3OTZzimhLM56IsFU3DvFBpKGJmM2N22nDpCcI+TwEUk5qjass0mOGNUHk0h5kYavNnhjWqtp0EZ2C804Sam7Al3LgcjmxmXRifi8tUSvuWjeORCYssDb9YaHrL0W4eT0tMQuOigoqRLhjIoynuYWoEaZdRmUmDE6Ph2hp8RuEta6qJO1lyRz2O3ObZLgFb6KE6nIhRIpYoAV/0oHbJbAWnm8LFhGq2sHbkiLqbcITSdNBs6McV0VZByKXSwI9KT43dP6JSpk+fIEYJocLt7v7+9xyk6vMh768Vf5SJETZPpMZizlfrpOJksqato8XwcZ1+K3V9G37Mplv74ngWdOM3p2XybhOwU/C4qSy3N4hLLhrrxpmu6hcKPGOVE0UAgoBhYBCIC8CG41ciXuu6mrKshK6EwTWr8dHBXV1reQK4pEg4UiMeCJOMpEgkUzfxaHZsw7hIQ8ebwjjmh7NjNXhoKSklNK2yw7bC0HIjFnIHozwKE+c0ppO9weJByMe1jSFKanubyTFbwxyVRA7Yw6hbhK7zThr+8pXL4x6qW8MYa/qT8ap1PUYIo1JPl10LdaRr1qgjE3kDgu0dbn3yvBoBTP/blTj6+4LtqhglvYwpclV1/7y2s6PTq6ihkcs5KihX4dLrjIzzEGuigoLbBU0F7kyWGsL60V4U9YdWBtErvQInnVedJcoHd96CE0QaGwkYEl7rhJ+USjCRlWHjzZRvOs8JJw1RoiZ8d9lNdS0fXRIh6TGrU7KbLF0QYtyN+awl4jZRVVl64FaJ+JZZxTP6ON2tIdOJfxGvpHdyB9KE52EU4SP5r9WuavnKoKjOisvzugzisMo+JAmfElnNVXOTEhxKkEsqWG1CoqR+Xt5LdWtFR9ESW3vOry6mz6VdqKF5o2fBiFDdW1Wbl6ckC+KqcyJI1kg50roOxEnYbYahU9S4mJrbxDd4aaywkJERBEkbZS7RD6q0J1OShf3JqWNKBH04A13ykfUk8QTOiJNKyW8lF2KLGjYyquoyApvFt6ukAgbjyTQbKU4y51G+G+PfqkYgRYvAVEYosLdwQuYr798nqt4NJy7DLueJBLwGWHuaKIipwhnzM1cLI6SDKlJIsiVT9hmDu9Zx7mly7x3uBcvu0E8SFOjl0hKw2y1FMj/S3u6hee9UP5mj/BWDykEFAIKgZ8RAhuVXHW8RLgzucqUhQ4l0Kx2HDYLFqsNm00zXtYiFKjD8yLnKRwhEgkTicRJiveVvTWuPV+VvdyErpB+jUNic2yTkqtuscsQPFtFFeU5b5TUMNts7RXgCgksQ66kMcmnCxlyJW8TG0KuYg4XVWW5gyZNZjtWSyYsMNjq8SoEoOEiM4pLbIywQFGOOYKTsuyLgiMe1jaJA3OmjHPnKeYiV1E7lYaXQPxi+D1BzOWiQED6X4xS6rFMWGAhciV6CAXR7WVt9w1tELlKCAKQJhzpIgjpX9S7Dg8ug1zlzr/SCTevo0WEDpYnaWwIYK3qk+fDQsbzJG78ETk/pdXUuFoL7IjQ2EaCIvenE5YiV8VRKSoMylUb7EquRFhlezU8jFygUIawidzTJpr9MaMAi8Vuw+Fw4GgrYZ8OC9QMz1v7xNrHcBAqMG83HtZ5wd03T7GdQjlXrcPqIt/KS0s4hbW8ksqy1vzSJFGfF28ogdlRRrnI88vFPVNJ4vE4sWiYcCSBucyNuySF3+MjZrJTWuLAZrViySIgIjczGokQjkSItZUWb8/P6rIyRYVKwyPY/c+wpZST6iqntKdGNiwwPXKCsBHCCOZEnJTFio6dympXh3yxrrNMkyvxnIwDSdimKOXedul4pkM95qepyUdMM6OlTJQJr2d3VedTYbyirL4iVxKbvWqiEFAIKAS6R2DTkSuRiyBC28pqqHVnx59H8dQ3Eha5WEZYTJJ4LIkmyEKbLCL2vommYIoyw7vUu+TKOChLhcBtPM9Vt+SqFbuKzmF8ompWhEjKjM2eK3wyjzFk8toQlaQ6hAWKe5B8RE1luKwh1klhsgHkStom8hW0KOC5MvIfMnl+hm1l/US1v7iOxWo3yjCnPVdFkqvGIKm2OuRZfYtiiT0paJGMERSHWVFso63Ai+g3TmB9Ay0iNy/f/Wq5yFUwhSXLQ2CEfGbnVqQSJEzpcN426pnPc5XDlAyPrxcq2+5ty21vOe+5MshVBEdbhbn0s7GWdTTrvUuuYnY3lSVRPF4RLliDK1P907e+iaijkqoONzWn56GZzZgyXiTcfemuMnwucuXMDrnrRK6MAVIJotEIkWiUWCRG0lxKZY0Lu5aHXPkbaIw6qKkR5Kr7eWuRDSVXKWIhPz5/mDhWyoSHKkfsmKjYJ/LVRE0Gi81BSZkTp0Mn0OwlFBNheSYsNht2I/LA0eHjTyIaIhyOEI3HjegFQS2sznLMIR8xs4MSm044qOM0MMmzjyWCNHuSlGdIpChO0bO7p+y4+1Rmyrm3jyVNrkReoFeEdpZSVWUn0tBM0lVLacyDJ2KmvNJNed4bkNPkym+toW/BixMjRn6y7u5IruKZSI+k3UV1hY63wUdCgqrpuoXyTLi7OjgpBBQCCgGFQM8R2HTkKuN9sXfKa9LDHtY1h0i1hgVGPNQ3hbF1apcMNbLOk6DMCH/rZXJFvoIWItdEfN12GiVxhSNhg8IC/etZ7zcbh9FS48AgK4e4b6gh/fKuzU6oT99DFEiWSJeET5tOa0GL9kId6X8O0NjQQkoUY3Cl8hS06IxJEeSqs/yyNpE356oQuRJRnfU0hTtXVEyHhDWF0mEx4kzTI3LVFMMu8oVyRCqJcu+21q8DhaoFuutw4scrQmFJVw6raLt2QNQZWU+DLyHSOEjqdtw11XRxxPUgLNCw5aCtCHKVJG5UNxPGmyLmb8YbcWTy2/JvSjnJlSgWs85LqkNYoDhoNuDvFBZY3aFUfCYs0AgFzIQFirC9DiF0jQRE3lRFKuueK3FvUgPeuIOqGhc2LZNXpFdQK3I9W6efCOENJIyiGA5z2nPVq+TKFCMYiGMRlSvboiHF5bxBLFV9cGfyxBLlIuyx1YBEoZYGfGYRLmlL50N1N289V1hgDF9jCylnDW6Tr9tS7EYuXNSEQ6yLcBy9O5eKDraycsyJMHpJFe4SE6lElIDXQyjZ0StoKU9fIuxvbO5wibDwKlqd1VQ6s/LZjAuL4zjzed+Evjq3SSU7FHbItsior5Gg2Z2TSIs8xFz5UYXJlaiK22KEsKfsFVRVObFp2QUtUkR9TTQHkphLXVSK6otdsMyEBWrlOYtqdFxV4lLgAKYOOVcJQs3NBEQuocuBxcinDVHaobhQjrVp5DYHsCrPVc9PU+pJhYBCQCGQQWDTkatM9cCwyUF5Rfolk4yFCAWjGNdEWioyFe/aq8+ViDK/Vg09ISpTibLhHduIXKXuQxHl9W6UHRckz1pKeZlInk8QCQYIJ0yUVta2laHeEHKVDDayzhvDUlKK3VGGq5TMPVeF5dDbSqc7KCsV8fqtZexFCJOo+JU/JyQXCnrES0NzkGSmFLtZjxEKhIhS0kbg5DCRJ1dd5LcnjIqShW2ip54rcQAL0dzgIaxbO5Vij2MqraK2UiTT94Bcxf00ehKUVrWH2eW1tnzkKlOdUXxlFuE+JnsZblfHe8biwSYavRE0MdcKDV9TM6GUg8qadLGDdk7QTINXfHxI51DkLcXudpL0BYwLYlOpJFpJFX0rW8u1g7Czdc3xPHdppT2BwbZKARrm0ir6VGaYpSlMAAAgAElEQVTlLOUAISe5MopTNtIU0gzPSKkV4kEvLcE4elZBi+bGFsQXebeoxCYq4Pm9+GOiMENle0GLoEZZpqBFIuKnxRfD1rmghYgbS0XwiBLijiojPJBYC43NYbQSNy6juESMgNdLWFTlNIrwbARyZYniXe8hZqvAXS6uVBDVDlvwBDVcdVWUmDIFLYTHyO02SLQ4wLcEdUozIZR6wXlnyJhegtstDvzp4hFeUVRDFLRICfIVxlrhxmm3YekSPpa5Yy3uo6FZx9UnXSm160983BHks/Ol1uIS4QZiZX1oTQ+M+xpoMVVS49TxNbRgrqxpq2QnPE4hu7iyI4t59IRcdbPdi7BAv6WG2uwY1AKvh+7IlbjbzNfSQjAG9vLKtH0a/XW95yohCnl4A0R1KyVOJ86yUmxtmLeGBWqYC17Olb7mwZEjLLBNlEyxIuW5kn/3q5YKAYWAQmBDEchBrobQ+Mz9vPy9qOmaIrTuG9aGikkalvW+iByPgFGCOJwQyc4mTEaJ4nJsMRHyZ2336GRf+ioOcmaLQUbKXWWZG+7lxywGMJHr4mu9kNYo++ygrLycsvY34QZ5rkTVNV+Th0AsBTZRAMRGeH0DsiQxGQvi9wcJRw06alyGK/ATlc568usir0NcIlxBSdahvTAm8uSqq/xlmCVtokc5V23MI4LfHzAKqYg0jq6XUveAXBUDeNRDfWPUyOlrLwjW6j1LppPf3ZW4S7PuTNPjhLwevKE4msNNTXVZOnQv412MWUqpzJQFTxPlECmThm4VF5o6scTDRFJWStqSYZLEInEjLDAVixk5jEbp8bZcH1FS2keDqDQmKrfZyqluuxg1i8TFoulnxc9kwS5iKgv88pEr4f2KBkQZ9QiJlIbF4aSEAP5MzpXRba5S7OWZi1aNBulS7L6g6EOIZEt7/nKVYhetxUeUFnH/Wjo8UNi337j4XOxJZiyOUioqRIU90ffGIFfpUuwtLQEi8XR5ebNV7IMVmeIQmYIWpU7MsSARUQTC4qCsovXvabC7n7dxyy4hoxR7Ol/VbC3BKYi7sbYThDwefNEk5rJqajuEBmcpU5GrHJadIOxpxiOqWlpLcQkC3CEPtiu5MjoR+mgR1TCjpLBSXlNHRYaxxsMB4qYSSvOGDrZtZESDUVK2sg4XvXeYZMZz5VA5V4W2JfV3hYBCQCHQawh0IVfTz9+d6tYPhnqM5Q9ezJ2f5P5O2WuzUB0pBH7CCCQjAYNAm4yS0ili4fRl1B3vARNeIi8NARMV4lLprC/Z4qJQny9o5LJYnZVUtd27lAat1etoeHIrNVoafKRKqox8C0Hew4hiASXYLSY0UyY0q0N56/QFoyIcS/yHOOBbrQm8673E7W5qXGaCzSJ0K30puCg+I2Rpu5A0qy/RR+v/xNzaq6C1Kzg/ufoJG0GPRZMrotHj7ot5UJCrphDJvBfRivL+GiXVnQuKtF4i3HEwcQm84bla30SoU510myuX5ypN9PP/hO06qMwTOphMiMu1TWh6nICnhURpdlXJwkDk81wlwz78STvlxvUAnX95yFUbP4oSimmU2CGS+UhWeCZ5WmhWSsQVAtl/zpAra55w5bamegSfJ4ZDhQX2GH71oEJAIaAQaEWgA7lSsCgEFAIbAYFwM2uaw22l8zXNir3cRZW4d6bAcOl71dJfxStcFTjzfM0Wl5OGNCcuW4xmTwJnTeaOOcNjESAUjRJPpMStQQaR6u6nWUW4rY1QUxh7lTuT2J8gEggQCsWIJROIK7IMMtbdz1SaM/dPkatibGwzI1fNKZzVzjwXlCcINLWgVeYgV7nCAs2V1JSJsEAf5qr2aor5wwJjlGXCP3MimAjQ1JTIm5cl+vWE0zarWUtwV7oLVO7rOErhnKtcsypArlofEfdR5a2+IWkvZnHfWMaj3fqIQa58JC2FLgVWpdglUVbNFAIKAYVAQQQUuSoIkWqgENiUCCSIRXWs9k5fpDfllDZw7GzP1gZ29TN4fDMiVz8DtPOJaFy+nasS6M8YEyW6QkAhoBBQCORG4OdDroy7nIJ0ij7JgYo1f3nrQlb0Y4xRaA6tf9+c5iI7Z9VOIaAQUAgoBBQCCgGFgEJAIbAFI/DzIVfivqx4YWoldGmyFnFHVAfl/xhjyFrb5jQX2TmrdgoBhYBCQCGgEFAIKAQUAgqBLReBLY9caRVsdfCpnHTAELZy2bCxlgW3zuSx74orPb7lqkzNXCGgEFAIKAQUAgoBhYBCQCGgENgcEdjiyFV84OlMvWB3LO8/whMfthBOxgisXUNTrFBpgM0RfjUnhYBCQCGgEFAIKAQUAgoBhYBC4KeCwBZGrjTY60JuOTXKm7Pu4PnGwnfq/FQUpeRQCCgEFAIKAYWAQkAhoBBQCCgENm8Etjhype19ETef4ue16//KK15FrjZv81KzUwgoBBQCCgGFgEJAIaAQUAj8fBBQ5Orno2slqUJAIaAQUAgoBBQCCgGFgEJAIbAREdjiyJV5v0u56YRGXpn+AK/5TBsRGtW1QkAhoBBQCCgEFAIKAYWAQkAhoBCQR2CLIlda2WD2PfcPnFY+l9tums+KlCpiIa9q1VIhoBBQCCgEFAIKAYWAQkAhoBDYmAhsIeTKhHPsVVx3VBWW+Lf8985b+c+3GxMW1bdCQCGgEFAIKAQUAgoBhYBCQCGgECgOgS2EXIHJ2Ye+/XdhzGnHsufqe5jx4P/w6MUJq1orBBQCCgGFgEJAIaAQUAgoBBQCCoGNhcAWQ67SAGiUHHwFM45cyTPX/JN3IirnamMZhupXIaAQUAgoBBQCCgGFgEJAIaAQKA6BLY5cqVLsxSlYtVYIKAQUAgoBhYBCQCGgEFAIKAR+HAQUufpxcFajKAQUAgoBhYBCQCGgEFAIKAQUAj9xBBS5+okrWImnEFAIKAQUAgoBhYBCQCGgEFAI/DgIbGHkCpIj/8icM5K8fePtzG0w/zgoqVEUAgoBhYBCQCGgEFAIKAQUAgoBhUABBLY4cpXofzyXXrovrg//xZMLPYRTMXw/fE9jTN15paxdIaAQUAgoBBQCCgGFgEJAIaAQ2HQIbHHkCq2E2v1O5ZRDh7Kt246NtSy4dSaPfWfZdCiqkRUCCgGFgEJAIaAQUAgoBBQCCoGfPQJbHrn62atMAaAQUAgoBBQCCgGFgEJAIaAQUAhsjggocrU5akXNSSGgEFAIKAQUAgoBhYBCQCGgENjiEFDkaotTmZqwQkAhoBBQCCgEFAIKAYWAQkAhsDkioMjV5qiVbuakm2zYiRJLbdkFPLZEOUw2G6ZYjMQWZjOdp7u5y7G5z29D1L8l2v2GyPtze1bpd9Np/Ke8b2w6VDefkZV+Nx9dqJkURkCRq8IYSbYw4z7yGq494CMenvoMi0ZcyJwzYrw16w6eb+ydYhvJYb/jhrNrWX7PNB7+2pqZ18YfNzcAPR83txySMG+SZhYGnDSTi3Z/j39e8wyLk4WJrW6qYcjY4zl2r4H0q7CQCjbT9MMnvPH4Cyz2Fn+FQO/0V7wcPy7cm/v8NgyN7uy+d/S7YfNTT28YAr2xryk76IkOftr7Rk8Q+Wk9s/H0q9bbxraUnp8TN/bMNnb/ilz1GsImKg6fyrT9P+TBac+zaLc/cOvZcebfcDcvNbcfpuO7nMdNv90JZ6fzubn5Je6c8Qr/68YjlRx5AbPPLOOzO6dnkSu5cXtNzLaOej5ubjlkZ2ih/0kzmTTaSjaEWnIlb94yh6fr00Q2VTaEUWPHcsDwQQyoMJHw17Pqk9d5/qUlfBfJftKGa8QxHD92BDv2cWAK1vPtR6/w7Euf8UPMlJmUlYFn3cIlO8/noaky5MpMxdgpXD2uBN/H83lrmZ+Eo4rqfjHWPPs6H4WzxtdKqB4zkQuPgU/n3MITP+QiXjL9WSjb8XCOPXxPdtm6HEfcw/qvPuC1ua/zcVMrtsXKAaI6Z+XO+zN6z13YcWAtdcFXuWvOW6wUdmrqzz4XXclp28RzKs+67EGuuucTvLr4s4a53/6MO/4g9hrowhFZy7cfvsjTLy9lTds1CsXMT0ZvnadloXz/P3DZSQOwvnEjU59rIJlz5rLtZG023S6/3cvpV8bu5Wcko4+s3qTsVH70wi3l5ie/zjMjdmfPRhO5cXPNv4t+i1ofoscCdhDdqpfXm7x+i8a5WwVrWPrtxZhD9mbk0K2oK9NI+tYY++5zryzlh6KvVSlm35CxA8n5FaVfmfeW5LgbaKddVVPMuIVXrlhD8vqVWW890K/MNAutN+M9LfNeLWbfkH1vyeAiJWSmUW/bn0x/YuienxOLkW5zbKvIVa9pRcN+4GRuPGARd8+ax+c7/IYbfxPnjev+wRvB9sN0qmIQuw2qwGLenn1O35+h3zzHI+83k4rU8/Wy9fiNg2juX+7Dmdy4vSZmW0c9H3fDyJWVbc/4E5cMeo+nX/gKbyvFSgVY/9UK1gjipLnY6dfXct6g7/hs4Scsb9Zwbj+aA4ZVYll0O9c/tjKDs4ZtxAQmnzME+7LXeGVxM/G6PRlz8A5Uf34Ps/7xFR5DH0Vu7qZtGX3pZZyaeJQbb/2AH1J50K/YgVFHn8IJo6op1Vfxzuw85EqiP237s5g0cTgVy17j5UX1+J07ss/Y/dg5+gr33vwiXxhEsTg5dPsg9j77N5y8i5XY6k/56NOVrFm/kqWf1dMicBEH7qG7MLCso9Hq/Ubzy0O3RnvzFmY8u46oIBbu/TnrkpPZK/4xb7+zgsbKPTl49DY4PryDm/61Mt2f9Pxk9ZaNu4Zl6Jlc9NuRbG1NEZqfj1zJtit+ReW1ewn9CmwK2n0RU5LTR6ZDWTstYvxCTaXmJ73O06MVtGdpO5Xcn4tYH0aPheygiP6k8GsVo5B+i8S5kG4NAjvsN1x2WJjln6xkbcSBe4f92H+YG+3dOUx/cg2Rwp1ktejlfU12fkXoQ279yuNSlH4LYik/bsGujAby/cnJUZx+5eYosd6EJFLvVdn3m/x7Sw4XaUmNd2vh94e83uT6S9uCzLm4GEm2lLaKXPWiphIjzue2Az5g1h2LWdv/RCZNiPHGtOf4OEcYmW7Zm5NnnsY+C2/miifWSOXx5DucFTNuL4pLT8fdMHLlYOivb+ECx31cffcnGfLTVSq9ejBD4t/wjS/zN62KYROu5TcD3+afU59gQVx4XrZm34snc4b1KW69ZT4rEoIEm3EeOplrj4qwYM6feOJ74QkrbnPXTTtzxNUTOHb1HVz89+XEukxPwzpsPJeeNZy+sa9Y+G0lI3cO8X4eclW4vxK2H38jf9zmJe6d9RKfC9kE6Rw2genj+7P6/inct1SEkRYhh1bNjuMnM2HXZv73n7/y0AKP1IFHNw/kgAsv4hT7C9w1+xX+Z5A6M7XHTmPKgfW8Pfs2nqkX3jkbtcdcxZSDmninzeMoOT9pvbUDn3SP5qxLfsmOSxfw7R77sP27ucmVbLuerKN8dl9Yv2I0ObuXm5esPoqzU7mxZVrJzg+k1rnxjpexZ/lxc0khs6/lXh8Z8ldw38ixz23QepPXrzTOMurN1UarZo/zr2N8zYvcO/1FPi8qp1hy35C2gxwTlJxffv32cP3mHHfD7FRKRZLySvWVwb2rfmXlKEK/0hOCwvuu7HtVUg7p95Zkf0XI2uP3R147kLfnnp4TixJvM2ysyFUvKiXZ/1DO2XEp/55fT9wxgiPGJfnk6c9Ym8MbVZhc2anc/RiOGzucneocaIE1rGiuYOjAGEs6hAWC/LgapiEnMPFXoxiwfi4P3Psuy4sOwcg6sErJKy+H7hzCyEMPZr9dtmWrqlJsqTBBzxqWPncv//4iQTq6rIaRf7iOc/1/4ZIHV+QgLvkUaqbfibO4fJ+PeXrqo7wdMaFb9+GUG05jv4/nMOmxVW0EN9HnOC6+/ACcc6/mxjcj6BlSctHwVSxfbqZ6cB0ufKxf9h6vPpcVdtddmIjexMd3XstDK9K5csnavRm3m5+l73/Jmt0v5aaTknzQmVzJ9qe5GTZxBr+rfIpbb3ydFZmDSXy7s5n2+x1Y97fJ3PeFrY1cFZRDhFXucA5XTxyG7fVZzHyhSRJnDce+F3PNyWV887cZ/O0L3dCZbhrMwZdfxEnBh5h+x2LWZ9ZDot/xXHrZaJwvTGXW/BApSZzl9dZKrGvZ7beX8+uyZ7j1r3H2nHYae7w7q2tYoFa4nW7ahr3OPZMjBlXiLjWjRzw0fLOYN+e+woJ12S5KCbuX1W+Rdl9oHaWk9SFppxmYC42bVruGbcABHDFuNHsMrqJc99H09UJef/YVFmQMQ95ecq31rutctJKx5+LGldBvl+nlXh/dhtd22jc6drmh6604/XYcOzfO8uujEzhaObv8biYT+s7lnhmvsDSLXBW2q/The+Pta8Js88+vXZI8+i1y/XZAJse4xdlp4fWW842ZR97e1K+8HPL6ldlfpNeb5HtVVo6k5HmjmP1ZSt5etr/0Ni5/DpM/n/biYXwz6EqRq02khO7JlUbJqPOZfPpA7N++x1tLmoi6tmWnkbuzfUUDH3UiV/IimKn75QyuOMiBOfU97865iccNz8zG+snLkazch9P/eDr7lTfw3ZKP+fIHD76EnbJqF7FFT/DWmvQ8jY1s8kUcF3yJx99bTfP6VXz3g49wvtC7jGgp204cOWkiR/sf5obbPzQIr24ezjHX/pojVv+VKfcvbQvJ1MsO5pxpx7DzO7O4+lmRl5Pe3C8cGca//EM+/N9aPPbB7HHAHgyOvMnDc57iw4Dw0DhwDxpCf+cARp56JHs3zOW+efVp0qZHafn2K74PteZxtWKuYd4vD7mS7s9E+aFTmHq0lfpnb+evbzUR1OrYdfylnLfV2zxw8/N8YuSZycux7RkzuHj3pbx4w3282mLDTJJkNyGrhojWnTniigkc1fIwMzMYGwdb+2jOuP4E9lh4M1c+6aNqt90Y0Pghixv24MTrz2D0J3O4/LFVxCXnJ6+3zGF+5PlcfXKchX+5m7mN+3LyzFNzkCsNm0w7zcU2e+/BNqkWgjEds3swI8aMZpj+Gvfe+DyfG546WbuXtxdZu5dZR/L6yN4XurNTkBlX4GLa7hQuOG8ftm5ayBvvLGOteSC7H3ogu/EWj9zyJIsCpiLspeu+lWudi3UpY8/yuMjqt+P88q2Pnu0bvbHe5PXbGencOAvTl1kfWb2ZbNictQwYeTSnHLU1ybmzmfOWt+1Dl5xdbcR9rcD8snHJr98evLe6GbcYO5VZbx10W0jeXtSvvByy+pXbX+TXm9x7VVaOmOR5Iy79vpSVt3ftb0POYRvrpLk59qvI1SbSSnfkSjfvxBFX/R9HRZ7itj+/wQojzAtSIy/gli4FLYoTQO9/GGefPor+617kn48t4XuJynfFjdDeWloOzcmQM6/lj7uvYdE9t/HP5cm0lyrHTzftxCF/+CX7Vjpxu0qxaTrJxo947dHHeOXbPM+Zqhhy6kX8fqSHRXf9mX9909pxKducdi0X79nA548/yjOfeYnY3FQNOYyTz9idundu4qpn1rWRq84FLbQdzmGy8O68PI0Zr/pp5Xe6aReOnHoex666k4v+/lUBr0/3h9b0Rla4P93Sn91OO49zRpaRrF/GiuRgdnUt48W7/8Fr9a1o5g6v6CxH0jSIgyZdzAlV9axuLqW2zolNixBY/SkfvPAML30VbpO1XUUapt1/z7Sza/j+79O49zPadJh0/YIJ1xzONvOuZ+rSw5l84b4MWPcUc/7cxNAp53HMD/cw6f4vCeUJW+yKs6zeQC/dg19OOpPhC2Yw62UPMSMctyu5km3XwSQ1DU3XSY74PTee4+KL26fz6Eor0naf6UxKvzJ2L7mO5PUhefiWHFc3DWTMpZdwsuM1HpjzHB9nclFT257K5RfuieWZq7np7SgJaXvptEHkWee6pD3L4hLs0f6cf320SiFjB7273iT123kfzrufdmqYZ320tdLcDJ84g3OHxkFPEvr0Ie545FPWZN53SNpVvnDnDd7XCs2v42aQd/9L7+FFvLcKjCttp5Lrre1dW5S8gkjn3v9k9Ssrh+x7IVmkvDLrTea9Ki+H3HsrJrn/BYuQtzftr2h77ukBcgt/TpGrTaTA7shVos+xXHz5GKpfnc71r7S0VTSTienfROLkHFZWDt26J8dfdxaHrf4bV//1s0x1ucKS6LZK+u08hqNPOIhhvMs///QfPjA8SFk/rZKBJ/yR/9tfo/6pv3DXuy0dyE6qbCcOPusMjtpREAjxFtRJRGNgt9DywnXcMC/QFq7WmVyJDevwqydyXOMDHfK/ZDbt7ENSfs9VupVMfynncMZNPJOD4+8y70sb/YaPZFh/Hd8HD3L348toMMJscpOrznI0m3bnuOnjOSzyEfPmf8TydSESpQPY+bBxHNx3Ja/PuYO5aztVNdSqGT7xWn5T+2JW3ld6/kn3L5gwVZCrGVzz3lBOvuh4hi+/j5sft7PvVYJc3c2k+/+Xl1zlwllObxYqj7iSqXt/zD9uepYlIhQ0J7mSbSeSoN1sfcCxHLPfDmxTU4Y9GcAXsVJeHmfZfVdw71Irsnbfs0M15LN72XUkrw+5w7f0uH2O5cLLx1A7fyYzX23J+hixK0dedS5jl/+Fyx5eSVjaXuTWuW6Ws2dZXHw92Z+7WR89soNeWW9y+i1mP5VZH+39mSnpM4itayqp3W4vxoweTOU3j3HHfYtYldCQtauNt691P7+OuOTf/zq/yQq/t7ofV9ZO/ZLrrT0nWEJeif1PVr+ycuQjV53fC411cvtLq7y99V6Vl0NUMS583ohL7n/F6zdznih4bpKwgyyjLmzPhc9yP8UWilxtIq12R67iA09n6gV7kHpyCje/l8k16raU8yYSosCwsnKkyg5m/LRjGbbgFq54/Aep4h7Z5EQbPoFp47el6Z9TuG1Ru8dEhKgMOO4Czj/QQtPzt3PH/EbCOedswuKqo68rfR+Vd8eJXHdSBV/ddzX3L+2moEVrgir/5KY/v8/3GdeVzKbdq+RKczF0/FR+3+9V7pn9Ml9GTaCVUnPwRC48uor1j17DHYtTbbljXUrKd5JjtTaCY6edy5ErbueirLy2pHh5TR5D7bzrmf6St0MZ86T7MH571ZHs8O6NmVDKdglbwyZGLryZKU9kwiSNMMK9OClHWGCh+bXiLMq8dqe3VNl+nHHVCWz/5kxuflOQZNAtozjhuhMZ/v4tXP/COqKxJAnJdinMuMdewZRxpbQsfJFXl9TTHHdQstM4zjzExbf3X8G9X1iRtfseHarbYNXobPdJyXUkrw+5w7fs+o0PPIOpF4yib+fI2Mww1i//xpR7P8drS4eRFraXzIMF1rlulrPn1nCcQuOGerA/d7c+emIHvbPe5PTbbnKF9lO59ZH7taFh2/sCrj2tlh8euoq7lmjI2lXeQj29sK9l79Od55cth4x+O8rddf3mjtbIgUsmbKywncqtN1/OgbuOKwoTyex/0vqVlKM1XLzQe2HlNsXJW/A9LfleTUrL0YpMgfeWZH8hyf00n37znps6fjXosi7z6Veuv83zrLoxZqXI1cZAVaLP7siVOMReMHkMfefP4LoXPFus50pWDt06ihOnn8khK+/nqvs+z5TllgAx0yRdgOJAKtoKI4g/WHEddBGXH+vG/+pt3PbKOkIFcobEU7p5G0ZfeCmnuufz0A1Ps1gQlXweH/MuHHn1eRy97n6u+utnbfMuuGl32rw21HOlW/bixBmnMer91hyx9AC6dSTHTzub0R/fnCGteTxXneTwagMZM+kSTg08wNS7ltCcwU237c+pN5zE3otmM/k/32eRYA1t1B+ZdbqTpXdN5x8rOubxiXyhgy6/iJMlC1p08RDmwTkbxlx6i+1yHrf8didK89z5bAq9adxb9v6OE6TafagP4sDLLuGU1L+5ec57rMqQ6fjQc5kxYTD1f0uTK1m778mhOlvmznaflFxH8vqQO3zLrt9k7TFccMVBVL19Nw8viXcJ/TVFGlglvKTS9iK3zo1wRAl7jkuOGy96f+5+fRRvB7213uT0m25VeD8VOMusj3w7e6s9u16Yysz5IWTtWXZ/Ln5f6zjTzvNrT/OV029nuXO/t7qi0wUXWTuVXG+57/qDruP2sn4l5WgtdFTovdBcI7e/tMpb6D0t+16V3TdypYXnem/J7s/xXtJvx3NTYfvLl94ua8/yJ7stu6UiV5tIf93mXJm249ArLuB48zz+NvsZlmSKIPRGWKC+1Vh+dcbebLXuBR559CNWb8ycK1k5tAqG/upa/m/X1Sy4+3b+9XX+nKuu6tKw7XMh155azfcPXsXdn6Q/iydrj+D3k37BwE/v5E+Pfp23ZHuHA7qlhu2Om8jvRjtoemoWc94JZohtLlKiYR1+HpN/NZjkc9O46U1R7S5DaiRypNrH3fCcq3SBh3M5dOXdXPPgsrbCHEn3ofzuyqMY9JYg6c15csdyyWGl/4nXM2nUUp6d+Xfe9KVDAFNDzuTK80dAJjemnata2ea0WVw6YgH/uvbfvG8Q0uyfmZpjpnHlmHremn0bz2ZKsdcccxVXFizFnh/ntkNpHr3pZX0ZWFeKOYtcifK7B004hKGfPcz9b37H2tXN+Evl2gW0bTng0ss4hX9zy+x3+S4PudJl7b5H9tIqdQ67l15HsvqQPHxLjtt6aDgh9ji33vEW37Rd0t15VcvPT26dy9qz3LjF6lcc/rtfH8XuG4X6k5OjI+oFCpbI7KcmufWR+5WrYd/3Yq49pYrvH7ySu8Q+LmlXuclVb+xrHe2/y/za/lxIH7kkzv3eyvV+6zqunH7l11vu+XUZt7f1i5wcsvpNZsha4f1Fbr3Jv1dl5eiIs573vCHX34bqN9e5Sc7+NsSeuz9w66b+jPjVBE4fGmT5v27lgSXRvPn3m+joLj2sIlfSUPVOQ7lLhDUsu4znsjOUBEMAACAASURBVF8Po65pCe9+sJz6qJWSoQdz1K7RLqXY5Wf241cLlJUjVX0gZ//xBEaVNbJqyWI+W+UlmLJir6jFVf8Szy4JoWOm+pDzOKXPt6z4wUsg6aB8wHD23nNbqtc+w523vslyIxnajOuIqVzziyjLnpvLQm/H/KDEui/5oj5mLFqtagd2G1JLZd/B7LT7cIa6g9S/fj/3vrg6K/ertRTsN3zy8kK+DJop2WoE++wzhLqGF7jvttdYalTjk9u0jVZaFVvtOoBas4a2w9GcuU+S5c/NZYFXI7z6c5Y1tVOXQl/YwE6f465k0hiNhoWv8eaXHkKOrdl5zKHsV/MN8/58B3MNQiMvR7J2LL+7eBw7N73Lq++sYL11MCPHjmFY6g0evOVpPs6uepi5/PS0xCPMvHUBa3J82mq/FHGJcYlwc9VIxuyf+xLhi4YXxllOb11XRu6cK9l2ZiqPuJoph1vxLn6dt79sxp+0YB18CCceUMHKTFigqIona/di5ML6lbV7kFtH2ZdedqcPeTuVG1fDPPQMLvrtnvTzfMqixSv4viWBbi+nqtrLsmcWsDxTglvOXuTXuaw9y41bnH5bLwfubn3I2UHGTnttvcnqVxZn2fXhYPAvJ3CI5UtWrvUT0u04t9qNUaMGU7f+We669Q2+ylwRImdXvb2vyc/P0EhBfciuX/lxZe1Ubr3Jjtv7+pWTQ1a/8vuL3HqTfa/K76ey7y05XGTl7W37k+1P/mTa2jKdvzaOXc0prF/cz+X3LSVYfDebxROKXP3Iaojvch43/XYnnJ1ClczNL3HnjFf4X9v9HmYcQw5h3OF7MXxgNRWWBLGgl+Y1S3n38Sd5r7FTQQEpOTL3XI0fxYB1G37PldSQyMuhV+zIPoeNYd9dtqWf24FNjxJq/p6v5z/Kgx94SWGhau/T+OW+A9m2n5tyS4Kot55vP3ubea8u5uu2A7+VIb+awx92T4irdDv9UoTmt18gm9zzAm45tQ8xz1p+WPEFn3zwXxauinTIJxJkrXr/czllv/70r6mg1KqT9K/lu0/f5OWXFmWNK0+u0of80xlt6xyrGOf7/0xh9n/bvXeFD9/igF7N4EOO4+h9t2dApQ1L3E/zqs9Y+PILzPs6kvGqFSOHhnWbMRx19GhGDqykTG+hYfn7vPr0ayxu6gipbtmT46efxWHL7+Syvy/Pc9mwhrn/aI46/mBGbVuOI7qeVR++wFMvfcEPbXetyc9PTm+ypEm+nW6qYfChxzJu1ED6u8twaFGi4SC+plV89NTfeXV1a0hkEXZf0NMpa/cZ+yu4jgx2L6EPkaNWhJ1Kjmvdej/Gjt2XEUPqqM7cFda46j2ef3A+n7d9pJCZn/w6F/LK2bPMuAK/IvQrtT5kSHZGv1L9yckhp195nKXWh1bO1mOO58iRA9m2j5tSc4yI5we+/exd5s37iG8yVSTbPlYVtCv5fUPKDoqdX0F9SK7fosaV068hb6H1VsS4va9fGTmK1G8heYv4CCr3XpXfT+XfWzK4pMctqF/Zc5O0HUjas9wBsWMrrZohx/6Gs8b0pfyDOUz5z/fEe9LPZvCMIlebgRLUFBQCCgGFgEJAIaAQUAgoBBQCP1cEdEdftjvkTMYfFOfj2//CU6vzVD/aAgBS5GoLUJKaokJAIaAQUAgoBBQCCgGFgELgp4iAXnYAZ15zIqPiX7Hg3w/y+GehThFEW5bUilxtWfpSs1UIKAQUAgoBhYBCQCGgEFAI/HQQ0Fz0GVRGfPUamrfUWMAsbShy9dMxTSWJQkAhoBBQCCgEFAIKAYWAQkAhsAkRUORqE4KvhlYIKAQUAgoBhYBCQCGgEFAIKAR+OggocvXT0aWSRCGgEFAIKAQUAgoBhYBCQCGgENiECChytQnB/zkPrZts2IkSays9/3NGQ8muEFAIKAQUAgoBhYBCQCHwU0BAkate06IZ95HXcO0BH/Hw1GdYNOJC5pwR461Zd/B8Y+vdN7022BbdUXLY77jh7FqW3zONh7+2ZmTZVPhtqnG3aBWqySsEFAKbNQJqX9us1aMmpxBQCPykEVDkqtfUa6Li8KlM2/9DHpz2PIt2+wO3nh1n/g1381Jzpwt/tTJqR/6CIw8cxg59y7BFvTSu+ozF81/nja9DmQtfe21ixXek1TLksEMYHX+HR99cQ6L4Hrp9IjnyAmafWcZnd07PIldF4Ner85EbN1U2hFFjx3LA8EEMqDCR8Nez6pPXef6lJXzXdvGpmJiGud/+jDv+IPYa6MIRWcu3H77I0y8vZU3bZblZAmglVI+ZyIXHwKdzbuGJHzpfDm3DNeIYjh87gh37ODAF6/n2o1d49qXP+CHWkzsgent+MsrQsPTbizGH7M3IoVtRV6aR9K0x5HjulaVZlwjL4RLf7hymn7871V1viDY6MK97jr/cNJ+vs72iBXEuEpduxS5GXgtlOx7OsYfvyS5bl+OIe1j/1Qe8Nvd1Pu50WbMM0m1tCsrb2+PK4ifbLiOJVkLlzvszes9d2HFgLXXBV7lrzlusNHQrj7P8+pVZb/LjZusjvxyilYw+bOxw7p/5/fBYDlNIEXvnZqY8uTZTvlhuXyvKplRjhYBCQCGgEJBCQJErKZhkGmnYD5zMjQcs4u5Z8/h8h99w42/ivHHdP3gj+8Z5rYIBx13I+WOcxJb/l/9+1kDAXsNWO45g4LK/cPNrvk1OrnTTLhw59TyOXXUnF/39K3K9ymUQydcmN7mSxG9DBs75rMS4moudfn0t5w36js8WfsLyZg3n9qM5YFgllkW3c/1jK/Hr6c6T7v0565KT2Sv+MW+/s4LGyj05ePQ2OD68g5v+tZKWTDujccUOjDr6FE4YVU2pvop3ZncmVxq2EROYfM4Q7Mte45XFzcTr9mTMwTtQ/fk9zPrHV3iy+5PApnfnJzGg0UTDPOw3XHZYmOWfrGRtxIF7h/3Yf5gb7d05TH9yDZHsrgrgknJuyy5D3Ng10E3bs8/p+zP0m+d45P1mY+3o4TV8/VUDgVZsCuJcpN4Kii0vr7b9WUyaOJyKZa/x8qJ6/M4d2WfsfuwcfYV7b36RL3pCoCXk7e1xZe1Ktp2AWLcPYu+zf8PJu1iJrf6Ujz5dyZr1K1n6WX1mHUniLL1+Zdeb5LgZOyksB8jpQ6Ns2+FsX5m96E3Yd/klJ+8Jq/95A7ctipH+q8S+VtCOVQOFgEJAIaAQ6AkCilz1BLU8zyRGnM9tB3zArDsWs7b/iUyaEOONac/xcbL1E7uGtvOvueq3Q7G8/RdmP7um7VAuXoaapqMXeVjuxem3dbVpyBUUxm9jSCs3rl49mCHxb/jGl5mDVsWwCdfym4Fv88+pT7AgLnRspvbYaUw5sJ63Z9/GM/XCC2Wj9pirmHJQE+/cMoen60WIqIZ12HguPWs4fWNfsfDbSkbuHOL9zuTKtDX7XjyZM6xPcest81mRSI/hPHQy1x4VYcGcP/HE98WEnPby/DZEHVo1e5x/HeNrXuTe6S/yecYTIYVL1ri6ZW9Onnka+yy8mSue6OxllcRZWm8bIHBOeUvYfvyN/HGbl7h31kt8btiQBsMmMH18f1bfP4X7lraGzcqMLStvb48ra1ey7QQM1ew4fjITdm3mf//5Kw8t8HQk4PngyIkzSK3fDVlvecaVk6Pn+ki6RnP2ZSeyx6q/8af7P2d91vtjU+2nMpaq2igEFAIKgZ8yAopc9aJ2k/0P5Zwdl/Lv+fXEHSM4YlyST57+jLWtLzytmuETr+HX/ebzj5lPszjSTViXVsU2+41mn522Y2B/Ny5nGSXmJLHgWj555E889lXroVrDNuAAjhg3mj0GV1Gu+2j6eiGvP/sKC7LetLppG/Y690yOGFSJu9SMHvHQ8M1i3pz7CgvWie/9gKk/+1x0Jadtk+sGtwRrn7qam96Otn0ZlRkX7FTufgzHjR3OTnUOtMAaVjRXMHRgjCUdwgKhIH5tutIwDTmBib8axYD1c3ng3ndZnivkTlK38uNmd2im34mzuHyfj3l66qO8HTGhmwZz8OUXcVLwIabfsbjtoJPodzyXXjYa5wtTmTU/HfaZrN2bcbv5Wfr+l6zZ/VJuOinJB53IlW7dh1NuOI39Pp7DpMdWtYVnJvocx8WXH4Bz7tXc+GYko4/Cwvb2/AqP2E0LrZxdfjeTCX3ncs+MV1iaCeGTwSW71+7JlSTORegtPXYP7C+XvJqbYRNn8LvKp7j1xtdZkcEgvt3ZTPv9Dqz722Tu+8KWJW7hcaXwK3rc7jUta1fJInBO7XAOV08chu31Wcx8oUnee57HrrpKkGP9bsh6yzOulBw91YdWzU6CgG73Oc/N/jvzO4Wf92xf26BVrR5WCCgEFAIKAeOUMO9zfcwtUxQYPwICum1vTpp+Ogd+eTuTH/q62y+xunkEx1x3LmPjn/P+4hXUe0NEklYcLgv+Ra/zkfEi1TBtdwoXnLcPWzct5I13lrHWPJDdDz2Q3XiLR255kkWBDIHTXGyz9x5sk2ohGNMxuwczYsxohumvce+Nz/O5EX7kwD1oCP2dAxh56pHs3TCX++bVZw71KaL1y1jRLKiB7LgaJaPOZ/LpA7F/+x5vLWki6tqWnUbuzvYVDXzUiVzJq8BM3S9ncMVBDsyp73l3zk08XpQHR36kfC1Ttp04ctJEjvY/zA23f2gQ6JR9NGdcfwJ7LLyZK5/0UbXbbgxo/JDFDXtw4vVnMPqTOVz+2Co6UlcN8355yJV5OMdc+2uOWP1Xpty/tM3LqZcdzDnTjmHnd2Zx9bMNmRyLwjL19vwKj5ijhcmGzVnLgJFHc8pRW5OcO5s5b3lz5PXlx6UYctXeNn9/xeNShP11K6+J8kOnMPVoK/XP3s5f32oiqNWx6/hLOW+rt3ng5uf5pEM+XxHjilDMPHYFxY7bvaZl8YtKrw8H254xg4t3X8qLN9zHqy02zCRJdufVl7artCy51q/ek/XW7biycvREHxrm3X7LlecOIvrMDcx+Oyi9D/Ro3aqHFAIKAYWAQkAaAUWupKHa8IZJ9y+YMPVwBs6/gWtfaO72ZZgmV+M5dGmuA3l6LrppIGMuvYSTHa/xwJzn+DiT25Xa9lQuv3BPLM9ke5qy5q9paLpOcsTvufEcF1/cPp1HV7aHHxUKC5QdN2XeiSOu+j+OijzFbX9+gxVG2BOkRl7ALV0KWhSHr97/MM4+fRT9173IPx9bwvdtoZfF9dOj1qYqhpx6Eb8f6WHRXX/mX9+ke0m6fsGEaw5nm3nXM3Xp4Uy+cF8GrHuKOX9uYuiU8zjmh3uYdP+XhDoM2t0huJRtTruWi/ds4PPHH+WZz7xEbG6qhhzGyWfsTt07N3HVM+ukD1W9P78i0dPcDJ84g3OHxkFPEvr0Ie545FPWZOyiY28/HrnqCS5S9ichr27pz26nncc5I8tIio8XycHs6lrGi3f/g9fqu7IJqXENILvHr9hxu9O0LH5+yfURNA3ioEkXc0JVPaubS6mtc2LTIgRWf8oHLzzDS1+FO+alSuDcYf551i8Uud4KjKsXIUex+tAdu3H0pN8y1v8v5tz2X1apKy2K3IxUc4WAQkAhsPEQUORq42Hbpeek+3AmTP0F286/get6gVwl+xzLhZePoXb+TGa+2tJ24NBNu3LkVecydvlfuOzhlemQGpObrQ84lmP224FtasqwJwP4IlbKy+Msu+8K7s3K7ShErmTHDfU5losvH0P1q9O5/pWWNhKQu6DFj6iIDRlKq2TgCX/k//bXqH/qL9z1bktbyFIred5m3gyueW8oJ190PMOX38fNj9vZ9ypBru5m0v3/K4JcQapsJw4+6wyO2lEcMAWj1klEY2C30PLCddwwL9Cud1sF1eU2TK0pfqkQvubQRp2fQfILjNsOt5mSPoPYuqaS2u32YszowVR+8xh33LeIVUY+WfbvRyRXmY8exelNxogKy5tyDmfcxDM5OP4u87600W/4SIb11/F98CB3P76Mhh4fmrvHrzfHlbV7vyTOQfPuHDd9PIdFPmLe/I9Yvi5EonQAOx82joP7ruT1OXcwd212Vc3COLdpq5v1K9oUs95E/mN39qwXIUdx+rBSPe5KrjzUz8LbbuHf3/WkaqiM/ao2CgGFgEJAIdATBBS56glqPXwmZduX02ecyr6f3srkR1YS7aYfGc9VfOAZTL1gFH3zvFutX/6NKfd+jk834x57BVPGldKy8EVeXVJPc9xByU7jOPMQF9/efwX3fiHvuZIdt2nb05l6wR6knpzCze8l2nKDtlhypbkYcNwFnH+ghabnb+eO+Y2Es3TYGh41cuHNTHmiNZwSdOtenNSDsMD2rk1YXHX0dVlIBZvx7jiR606q4Kv7rub+pe0FLWK7TWT2r4dSkuEpJs/L/PX6l/kic0DfWPMrNG5uM9ew7X0B155Wyw8PXcVdSzYdueoZLsVuAjnk1VwMHT+V3/d7lXtmv8yXURNopdQcPJELj65i/aPXcMfilHROnTQ57eVxZfFrDQsstD5i5hEcO+1cjlxxOxc9uKL940DdsVw4eQy1865n+kvePB7bbuyqwPotdr11xruzPYs9XEqOIvWRrD2C/5v0CwZ9+BdmPL6K4GZQBKnY1aDaKwQUAgqBnzICilz9mNrV+rHXhVdylvtl7p/5PJ92U4RBhlwla4/hgisOourtu3l4SbzLIcwUaWCV+OprGsiBl13CKal/c/Oc91iVqV8RH3ouMyYMpv5vncnVThx+9USO+/4OLnlgeRcSKDturO5YLpg8hr7zZ3DdC54t3HNlxXXQRVx+rBv/q7dx2yvrCHU61IjE/oMuv4iTJQpatJudnIemtb1u3obRF17Kqe75PHTD0ywWB/LML1kxiOGDKrC08pTYWr75ch2+zDw31vwKjZtviaULcxyI64WpzMwU+igWl0IFLWT66xkuxW8cneVNWvbixBmnMer9jrlzunUkx087m9Ef38wVj//Qw3vmusnl6+VxZfETBS1k1kdShDtPuoRTAw8w9a4lNLfar21/Tr3hJPZeNJvJ//k+Ly657arw+s2l0e7WW+f2XfQrKUe8GH1obnYcfzX/t/1HPHHjw7zj63wvXvF2qZ5QCCgEFAIKgd5FQJGr3sWzQG8a1r0v5LrT+hKeN4c5L67vckBvP0TL5FylDysnxB7n1jve4pt8d+KYtuWASy/jFP7NLbPf5buC5CpNxk6NP8rMWxewJtO+bW6ZQ1KhcXXTdhx6xQUcb57H32Y/w5JQmgj0hudK32osvzpjb7Za9wKPPPoRqzdyzpX4Wvz7Sb9g4Kd38qdHv85zv5SZmmOmceWYet6afRvPZkqx1xxzFVd2KMWebSby5Eq31LDdcRP53WgHTU/NYs47xSaxb9z5FbeUNOz7Xsy1p1Tx/YNXctcnnd2vcrj0BrkS4V3F6q14++sqb7qAwrkcuvJurnlwWdZdaYfyuyuPYtBb4qNEx9xM+XELFUrpzXFl8ZNtZ6X/idczadRSnp35d97MEIjUkDO58vwRkC+X1DDA3HYlt347WnBx6y3XuHJypIqwg9TgM7jiDyOwvTiDWfN80vmWhdamburPiF9N4PShQZb/61YeWNJaFbbQk+rvCgGFgEJAIdAZAUWufmSb0E19Gf7rC/nVLhD8aiEfLF1Pi6mcmoG7sVPTI8x+rt6oJifjuTKS1oeewUW/3ZN+nk9ZtHgF37ck0O3lVFV7WfbMApYbIWFmKo+4mimHW/Eufp23v2zGn7RgHXwIJx5QwcpOYYHibqb+J03nsv0j/PDefBb8kICKrejb8AJPfhRClx5Xw7LLeC779TDqmpbw7gfLqY9aKRl6MEftGu1Sil1eFcVUTZPvNX9LM64jpnLNL6Ise24uC70dvxYn1n3JF/XpyzvbL0ldYlwi3Fw1kjH757hEWKtiq10HUGvW0HY4mjP3SbL8ubks8GqEV3/OsqbMVaBVO7DbkFoq+w5mp92HM9QdpP71+7n3xdV4exAO1Nvzk0PXweBfTuAQy5esXOsnpNtxbrUbo0YNpm79s9x16xt81erFlcSljeh3e8+VOGvL4SyNizFwIfuTlddOn+OuZNIYjYaFr/Hmlx5Cjq3Zecyh7FfzDfP+fAdzDYLe+is0rqy8vT+uLH7S7WrH8ruLx7Fz07u8+s4K1lsHM3LsGIal3uDBW57mY+NDjSzO8utXk1pvsuOKqwBk5JDUh1bLiIlXMn7Qcj548h3+l+W1Nmil/1s+/7qlUzVSuRWazpsbx67mFNYv7ufy+5YSlHtUtVIIKAQUAgqBTggocrUJTEI3VzPwgCM5fO+hDKotxZ4K0lL/NUvfmcvcD5uNEu1y5Cr9pda69X6MHbsvI4bUUZ25w6px1Xs8/+B8Ps+UctZNNQw+9FjGjRpIf3cZDi1KNBzE17SKj576O6+u7ngZrV42lP1POo7DduqD25Yg5v2eFa/8gwcWtBbOkBtXHEQdQw5h3OF7MXxgNRWWBLGgl+Y1S3n38Sd5r7EnYS2Z+37Gj2LAug2/56qwCVgZ8qs5/GH3hLjitdMvRWj+jUx9rrUkuoa5/2iOOv5gRm1bjiO6nlUfvsBTL33BD1lhoGmPy+mMtnVmSHG+/88UZv83mSZre17ALaf2IeZZyw8rvuCTD/7LwlWRDfhi3bvzK4ydMNFyth5zPP/P3r3H5Xz/fxx/XNdVSUnlmCIpRrbRkMNIvmhiw3dszGHD/L7Y4WuGGSNbvuYwhzH2tS9mNrPNbGbO57AaaZIJQw6jJJRTUV1d1+d3u+oqIfqUqxOv68/v93O9P+/3/f1ut+vpferc1IPa1Z2w06WTeiWOM4dC2bYtklPmUy5NZal1URuu1JenziXrvfmMv4K0V1sZz/bdeaFVPWo522Clv0HS2UPs27SebSdT7zwVL7/3FsBPsfB7M/+hR8W4L8hz1u7+PP9CG5p6OGOvXOPSiT1s+WUr+xPNva/aWf3fr6q/N9XvNf/3Ob92ZJ78mv84UKya0WNyf/xt8/5XFetjSxn/xcFC/aOL6bJjr26D6e/vgsPe2Yz7MbZQIU3Vfw/kIREQARF4xAUkXD3iHSzNEwEREAEREIEHCSi2LtRt34+B7fQcmDeHVefkBEIZMSIgAiJQWAEJV4WVk++JgAiIgAiIQBkXUOz96DexJ77644SvWMrKQzcfYma8jGNI9UVABETAAgISriyAKEWIgAiIgAiIQJkU0DhSvY49+nPnSTJt+JWPCIiACIjAQwlIuHooPvmyCIiACIiACIiACIiACIiACGQJSLiSkSACIiACIiACIiACIiACIiACFhCQcGUBRClCBERABERABERABERABERABCRcPTZjQIO2nA1WaWmkPyZt1trYoE1PJ6OUtrdk6vf4jYNS2v1SLREQAREQAREQgUdQQMKVxTpVh1PniXzoF8myoNVE+LzD7L7p7Jo6n3WX77xDymKvVF2QFbVensrIZ0L5Nmg1+w333tZ0u6iSakdh36ulQsfxBAdeZMfUz1mfaLK2otZLUxjxTBjfTcyvvaDYN6bzm31obxXGinm/sj/ZkscQP3z9VHdzvg+WhXGQbyPkAREQAREQAREQAREotQISrizWNVoqdgoiuPUfLA1eR8TTbzP3VT07Pl7AxqTsi3J1VOk4giH+VXG2L4eVJoOM5EQuxEQStmU74fFZF8cW+KOpilfH9rTR/8bynefzmKmxxqP/TEY23MHX+YYrNe0ocA1VfKGQ79XW4tl3x9An4xs+/iyCC5mABWkvGNx6MnqUHx7EsWfOVH44a8EwbIH6qcBT+UhBXArZHyprIo+JgAiIgAiIgAiIwKMoIOHKYr2qoVzb95nmF8GCqduIfmIw0wbrCfnoG0JSsmeKrHB9aSrvNY/h9x/DiTFaY+3kzlOtW9PY7igb53zBpoTsIKa+Yor2SToHDaHb2c8Z8dXxPJb9FeRHtZp2qK+b+icL996M6t15d0wbKqwNYtrOVHM4LUh7Tce6VKB601Y01h4mPOI81wqVcPNuqUXqpx4xnycL4lK4/rBYVaUgERABERABERABESiDAhKuLNhpGT5v8ZnfXqbO388F1568NzSdkOA1HMhZhpcVrsY0C2P5hF+IyMgKXYaqz/PW+x3w3PMJH/x8ocB7hCwbriD/dlgQLVdRBX+vlgoB4wnulHtJoKnAgoSIomlLVqmlrX4Fcyl4fxSlpZQtAiIgAiIgAiIgAqVfQMKVBfvI4NqB1xocYcWOePS2PgR2MXDwl0PmpWqmF+UdrhStN4ET3qDbhf8xdtFhbiigaN1pPqgfgXWccbLToaRe4dKp/excu5nwBKP5t7srLUd8wCvued38mMGFVROYvjsNxRw2RjyTSMJFLQ5VHCiXkUTCkV2sW72bIzfu3GOUfzuy0TRovXowbIAvtS6uZcnCUE6kP2g/14Ox1b/XXI7WnTYj3+MV/TdMzlkSeDtcjWh8lhMndFT2rIYj17l4LIwta7ZzIDHr+4pVM3pM7o+/bdZUlcYYS9in0/nx3N3LAnVUatmfHi1qUKNaJZzK61BuXeb80d1sWhPKkev3afND1i+zjmrGQWbl7ajSvCvd2zbEs1oFbEkjNfkqV46u4euVf3Ep13JJy48DC/4RSVEiIAIiIAIiIAIiUIYFJFwVa+flHa6Mdm3oP/Elmh2czfvfnyUzKmkccW/RBHfjNVLSFXROnvj4t6GRspWF09YRnW4KRLY41fHCtUItmvbuTItLa1m0Ld4882UkLf4YMUmmIJY1YzHCJ47jv+3nSGIqSpVnaN3ai2p/L2Pa55G5AmBBQHRU++dkxrazRWeMJXT2dFbGWnC/Uj5VyXD5J6Pea4Pd2gm5lgTeDlfvNL3FjRN/8MdfF7hSzpMmfk3wTN3Jstmr+MN0aIXGEZf6blTWaeCJ7rzul0Z4nuHK7Nf4DIe2/cHxZA26qk/RqnU9qp/7w3yliAAAIABJREFUjhnzI4gz593cVX7o+qkeBxq0jf5F0MA6KIe2ExJ9mWSjLXbO1XDPiGTVzljSMitWVOOgIGNGnhUBERABERABERCBR1dAwlWx9m12uNrHj5PXEmm0oXyVujTq1INu3skcXDCVb07kMQui0aBRFAw+bzLtNUcOz5vE8tPWOTUv3LJAa6p1/4hxbePYMW0eay8VfK+XqQKKa0de7eOLa8IGvvs+itgHnkRoSWwtFZ+bwIemJYFT5ptPCcwuP+/lb5onXuP9YY2w2RTM5C03uJ2HNGhajGBGby37HhCu7jwQxJqq3SYyzv8iuz/5lNUJd4dKS9bP3K77jgMdlbsGM94/hg3BS9h210zkbfW8XCwzDizZs1KWCIiACIiACIiACJRVAQlXxdpz5gMt2lhxO0IpGK+f5MDa7/gx4op5hsG0XceJmn7d6PrsE7hXsaecIZnrqdY4OOg5tmgsC488bLgCfeM3mDWwGse/mMCSY7fLK1aSwr5MW5s2o0bzSvrdSwJvz9DcfTqiafllpwnD6H55CRMWHORKzsEVhQlXoG80jJmDXDjxvw9Y8pfNnS2xVP3UjgOv3owc1hLXpEhCQ8II//M08TfvPpkj79BZpsdBYcePfE8EREAEREAEREAEikBAwlURoN6/SPPMVfPDbPvyN/7SZ5CenMjFy8mk3rGsTIdTwFjGdbHj2r4NbImKJ0lvS3nvLvRr78iZxWNZePjhw1W692Cm/cuD2C/vLK9YSQr5sowaLzJqdOs8lgTeP1yhrUmrd9+nL98x/dM9xOaYFy5cpTcYzLQheftZpn4FGQcabGq1JqCTPy29q+BgTCIuKoSN60OJvpqNnHe4KsvjoJDDR74mAiIgAiIgAiIgAkUiIOGqSFjvV2jee67uflrRetB29Eh6GVcwY3YYZ80hQF9/EJOHehJ/VxjKmZGJnc/IJSduz37lFHyfH9UPCAfFylLgl2XdwfThcwl5LAm8f7hSdE/SecIQXkhYzPj/Hcp15LoGWoxgZm8tEZ9OZ8U9B1oU1M8y9buqKdg4yGG0c6Ne07Z0DGhOA2Mo381aSXjmUsGCtqPAHSNfEAEREAEREAEREIHHWkDCVbF2v7pwhbY2fqNG04sVzJwVyt/5hqusH+G99cuZMjec8/ccrlB0P6oVtwAG9G2BW8J6vl0eybni2HNl9umdvozJn+3L4zCOvNqrwbrxEN4f4IlhTTDTd97MtefKtMTvjcwlfqcXT2Dh4bv3nxXQz1L1K+A4uHsom/boTR/gxpnF77HwsGnZYgHbUYC/DUXris+AofSpn8KJH+ayJMp0SqV8REAEREAEREAERODxEpBwVaz9rTJcocM5cALjOllzdf92dh9N4obBCmvP9vT0q8jpu5YFgg2uL01idOtU4sJ2EB6XARXdcLm0np8jb+YcxX73HqQHLWtTx1IypwWaltyNHt2a8vecEphd6+zT/U5xcNM+jqboKO/mQ8uWXlS7tJ5Fn23lSOqdB4cYnAMYMu55nkzcw9aws9ywd6PaxQ38cqDgfparn9pxoKNK4AgGusYQfTyOi8kZYFcdz1bt8at+ko2fLGDTZVNgLLpwZXB6jqFBXXhKZ8T68GLGLDpCirpBJE+JgAiIgAiIgAiIwCMjIOGqWLtSbbgy3W9UBc8O3eji64Grkz22mjTSbqVwPfEskau+YstdS9cU+/q0fqk7Hb2r42STQfrVWGI2f8OS8GsYi+xHtfmeq4G+1Ep4+Huu1HWFFsfAICYG3G9JoKkUHZVbD6LXs664VqmInbWC4cYF/v5zJ5s2RnDy5p33emW91xrHpr3p9/zTeDppMVw9R8yWpXy593oB/SxbP3XjwArnpj15oU096ro642CjYEy9TlLsUaK2rWfzsZsYzG00Hclv+ZBtujqgMl7dBtPf3wWHvbMZ92Ns1pUC8hEBERABERABERCBx0hAwtVj1NmPQlMVbW3ajhpF7/Rv+fizfcSXsrVnpb1+RTUGFFsX6rbvx8B2eg7Mm8Oqc3kF2KJ6u5QrAiIgAiIgAiIgAqVDQMJV6egHqYVKAYNrT0aPboXtmiCm7bxV6vb1lPb6qWQu0GOKvR/9JvbEV3+c8BVLWXkoe6asQMXIwyIgAiIgAiIgAiJQ5gUkXJX5LnycGqDDsdOEzFMCt0+dz/rLd1/cW9IWpb1+ReSjcaR6HXv0586TJGsBiwhZihUBERABERABESgLAhKuykIvSR1FQAREQAREQAREQAREQARKvYCEq1LfRVJBERABERABERABERABERCBsiAg4aos9JLUUQREQAREQAREQAREQAREoNQLSLgq9V0kFRQBERABERABERABERABESgLAhKuLNZLOpw6T+RDv0iWBa0mwucdZvdNZ9fU+awrdQcvPKjRJdcOfd3XmPTWM1TOvt9XSefE0nf5/KDN7QprnGg8bDKD6t8+OUGXsIY503dw0njnxcAW61opSAREQAREQAREQAREQARUCEi4UoGk7hEtFTsFEdz6D5YGryPi6beZ+6qeHR8vYGOS7p4ilMbDmDywPo63wlg+6UfCU0vLvUAFa4c6G3VPmcLVf9704tKqhWwwXZKsGElLPMuFlNyhSYdd9VpUtTVdcKXDrfM79HFeLeFKHbE8JQIiIAIiIAIiIAIiUIQCEq4shquhXNv3meYXwYKp24h+YjDTBusJ+egbQu4IB4DGgQaDJjG0EWiUFE58M4H/HqCU3NlUgHZYzC6roKxwVZu//zuRr2OsVZRujUf/mYyqJeFKBZY8IgIiIAIiIAIiIAIiUMQCEq4sCJzh8xaf+e1l6vz9XHDtyXtD0wkJXsMBw53L1YwO7RgY1J2GR/ZytEErmsQsZOKXR7hmmowpBR+17bB0VSVcWVpUyhMBERABERABERABEShOAQlXFtQ2uHbgtQZHWLEjHr2tD4FdDBz85RAX7ghNGnQtRvBx73JEfz6HfX5Tect7Hz8FL+e3m7mWBmoq4f5sG1p618XD1QnHCvaU1xlIT7nAwW8/4fvj2Rfo6rCt24EXAlvQ2N2ZCjo9aTcSuRh3lN9XrWZvonlJosaOKs270r1tQzyrVcCWNFKTr3Ll6Bq+XvkXl3LVUV07THAatF49GDbAl1oX17JkYSgn0gu/70nClQUHoxQlAiIgAiIgAiIgAiJQ7AISroqbXFORhoP/w7Aa61kwZQtHnn6DKQPciFv6Af89eDtcKTofun40iAB9NHv2xxB/9SapBmtsHa24EbGdyMx9XBp03q8xarAPVRP28VvEGS5llKeidzue875F1OeTWHbStLxOg7bRvwgaWAfl0HZCoi+TbLTFzrka7hmRrNoZS1qhHHRU++dkxrazRWeMJXT2dFbGZoe+ghco4argZvINERABERABERABERCB0iMg4aqY+0Kxe5ZXJr5M04hPGP9zPGn2bXntwx40OTiHD5afIdVcn6xwNZAOR2Yz5vuz3D4b73aFFa0HbUePpFe5zSyasY4/U7NmjQxNhzOrnz2HcsKVjspdgxnvH8OG4CVsu2G5wzMU14682scX14QNfPd9FLF3LYEsCK+Eq4JoybMiIAIiIAIiIAIiIAKlTUDCVTH3iOHpoUwZVJ0ziyby5VEr0FTC541gXnfZxJf/WcdBfVZAUhOuMqq8wL/H/QO3kI/5cF0SBnNb7g1XgFdvRg5riWtSJKEhYYT/eZr4m6Vkk5e53hKuinkwyutEQAREQAREQAREQAQsKiDhyqKc+RVmjevLHzOmyR/8NPUX9mXONGmx9RvJxOcziPzsY747k3VKnppwpffow4ThTVBWjWNGaEbOaYN5his02NRqTUAnf1p6V8HBmERcVAgb14cSfTW/ehfP/y/hqnic5S0iIAIiIAIiIAIiIAJFIyDhqmhc8yxV0dbnuQ/e4vkq2XNMuR8zcGXjJP6z+TpGleEqo8aLjBrdBqfNwfxnS9b3TJ+8w1Wud9m5Ua9pWzoGNKeBMZTvZq0k3IJLBQtLKuGqsHLyPREQAREQAREQAREQgdIgIOGqGHvBFIZGj26N7Y7/svLo7XutFI0rz/R7mfZXljFp/h9cVNTNXCnWjekaNJhON35i9pxdnDYvKcw3XGWHMJ83mT7AjTOL32PhYZtCSShuAQzo2wK3hPV8uzySc6Vsz5WidcVnwFD61E/hxA9zWRKVVkruEysUt3xJBERABERABERABESgFAtIuCq2ztFg4/ceU/+ZRtjMWayKz32qng3ufaYw6pkoVgV/w64UraplgaCjYrvRjO1eDe2ZUEIiL3KrQk3qNfXlqUqJROY60KJK4AgGusYQfTyOi8kZYFcdz1bt8at+ko2fLGDTZfOR7QXyKP2nBRqcnmNoUBee0hmxPryYMYuOkFKgNsrDIiACIiACIiACIiACIqBOQMKVOqeHf0pTAe/BU3jDbT3/m7yJw3fM8Gig2b+Z1q8yJxePZ9FhK5XhynTKugM12rzIi/7e1HG2gZTznLriQL1aKeybO4Uf/jbt4bLCuWlPXmhTj7quzjjYKBhTr5MUe5SobevZfOxmzmEYBWuo+Z6rgb7USiil91xpKuPVbTD9/V1w2DubcT/G5nnyYsHaLU+LgAiIgAiIgAiIgAiIwL0CEq4euVGhw7HTeCYGnGPTpMVsvV6YGamSQSmKPVeKrQt12/djYDs9B+bNYdU5yx1DXzJK8lYREAEREAEREAEREIHSKiDhqrT2jKp66XBu0YN/OCVw8dot0ilPBbdGtGjpifOh//Lxshiula7T1h/Yqqxw5cXl1YvZFGsNipGbCae4cDN3INJh51IbFztTUTpcO71Nb6fVzJm+g5PGrGPssz+KvR/9JvbEV3+c8BVLWXmosDN0qjpDHhIBERABERABERABEXjMBSRclekBUA6XDv+i77M1qO5UHhsljZSkc5w+sION249wPr1szdKYwtWkt56hcnZGUtI5sfRdPj+Y67ANjRONh01mUP3b1yrrEtbkGa7QOFK9jj36c+dJyusW5jLd91J5ERABERABERABERCB0iYg4aq09YjURwREQAREQAREQAREQAREoEwKSLgqk90mlRYBERABERABERABERABEShtAhKuSluPSH1EQAREQAREQAREQAREQATKpICEqzLZbVJpERABERABERABERABERCB0iYg4cpiPaLDqfNEPvSLZFnQaiJ83mF233R2TZ3Pusu5Lwy22AtLuKASbq+mIm7/6M1Lfl64OdpgwwXC507h+78fResS7mp5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nJt8q4QFMlxfZNSoVjj+8QM/77vCLWM61+NiuZxemJm1Em6MvF4EREAEREAEREAEROCREJBw9Uh042PYCE15qj7bm14d6lPbqRw2XCB87hS+//vOZZCPoYw0WQREQAREQAREQAREoIQEJFyVELy8VgREQAREQAREQAREQARE4NESkHD1aPWntEYEREAEREAEREAEREAERKCEBCRclRC8vFYEREAEREAEREAEREAERODREpBw9Wj1p7RGBMqUgKK1oRxppBfq/rQy1VSprAiIgAiIgAiIwGMgIOHqMejkrCbqcOo8kQ/9IlkWtJoIn3eY3TedXVPns+7y43wIREm5lNR7QbFvTOc3+9DeKowV835lf7K2RP4KDI3+xcevVuXEF8EsO2ltrkPJuZQIgrxUBERABERABETgkRKQcPVIdeeDGqOlYqcgglv/wdLgdUQ8/TZzX9Wz4+MFbEzKfU+UjvL1n6P7c814spYTdpo0bl2J5+yRHWxYF01sRq6jzjVV8erYnjb631i+8zwZD2tp6fJU1Ueti6rCCvBQSb0XDG49GT3KDw/i2DNnKj+cvU+4LuL+MDQdzqx+9hz6fFKucFVyLgXoPHlUBERABERABERABPIUkHD12AwMDeXavs80vwgWTN1G9BODmTZYT8hH3xCScjswGWu/zOi3W+FycR+/7Y3hQrodFWs2oEmVQ3y/aA+xxttgivZJOgcNodvZzxnx1XHSH9LS0uWpq446F3VlFeSpknovoKlA9aataKw9THjEea4pede7qPsj73BVgi4F6T55VgREQAREQAREQATyEJBw9RgNiwyft/jMby9T5+/ngmtP3huaTkjwGg4YssOVDpceHzO2xSF+mbSMXSkPXi5m6R/fli5Pbdfm76K2pII9V1LvVVvLou6PvMMVlHYXtX7ynAiIgAiIgAiIwOMnIOHqMepzg2sHXmtwhBU74tHb+hDYxcDBXw5xIWfmwgr3Vz5hlE84PwZ/T9it+4QrrSstR3zAK+76PPQyuLBqAtN3p2EqVtG603xQPwLrOONkp0NJvcKlU/vZuXYz4QnmaTDV5dnSYPBM3nL5hXlTt3HcfAhCnj/SNXZUad6V7m0b4lmtArakkZp8lStH1/D1yr+4lGu2Jn+X7GZq0Hr1YNgAX2pdXMuShaGcSM+1TLKAY0ndezXY1PIjsEsbmnhWwkG5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