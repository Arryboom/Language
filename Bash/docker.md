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










---

#On windows port map error,not localhost
If you’re using Docker Toolbox then any port you publish with docker run -p will be published on the Toolbox VM’s private IP address. docker-machine ip will tell you. It is frequently 192.168.99.100.

This isn’t prominent in Docker’s documentation, but it does at least show up in the excellent Get Started, Part 2: Containers 502 tutorial. (I couldn’t find this rather important note anywhere around Toolbox Overview 66, for instance.)
https://docs.docker.com/get-started/part2/#run-the-app