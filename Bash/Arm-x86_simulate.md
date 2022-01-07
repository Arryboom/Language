# 用 qemu-user 在arm linux机器上运行amd64/x86程序

>https://www.cnblogs.com/bamanzi/p/run-x86-linux-progs-with-qemu-user-on-arm.html

## 1\. qemu-user 是什么

本来, 对于 QEmu, 我只知道它是一个模拟器, 可以像 VirtualBox/VMWare 那样跑一个操作系统, 只不过 QEmu 可以在 AMD64 上面跑针对 PowerPC, ARM 的操作系统, 当然, CPU 指令是解释执行的, 相对来说比较慢．

但是前几天折腾 CentOS/Fedora 上面的rpm构建工具[mock](https://github.com/rpm-software-management/mock)时才发现, 原来 QEmu 还有一种运行方式, 那就是跟`wine`的运行方式相同: 直接运行程序文件．在这种模式下, 这个针对 PowerPC或者ARM编译的程序, 就比较像一个本地程序, 它跟本机的Linux内核打交道, 进行系统调用, 访问本地文件（其实是通过qemu进行）和本地设备．

在 QEmu 的术语中, 前面那种运行整个操作系统的方式, 称为"full system emulation", 在 Ubuntu/CentoS 由软件包 `qemu-system-xxx` (比如`qemu-system-ppc`, `qemu-system-aarch64`, `qemu-system-arm`)提供功能；后面这种运行单个程序文件的方式, 称为"user mode emulation", 由软件包`qemu-user`或者`qemu-user-static`提供功能（注意没有细分为`qemu-user-ppc`, `qemu-user-arm`, 不过这也许只是因为这些模拟器文件都不大, 就揉到了一个包里面．至于`qemu-user`和`qemu-user-static`的区别, 现在只需要知道后者是静态链接版本, 至于在什么场景下需要用到哪一种, 以后再来说）．

### 1.1. 举个例子

这里举个例子说明一下应用场景：在树莓派 2 (CPU是armv7) 上面跑针对 i386 编译的linux程序.

我在命令行上工作是, 喜欢用一个叫做 [fzf](https://github.com/junegunn/fzf) 的小程序 (这类程序我以前介绍过: [命令行上的narrowing（随着输入逐步减少备选项）工具 - 巴蛮子 - 博客园](https://www.cnblogs.com/bamanzi/p/cli-narrowing-tools.html) ), 但它的早期有个问题: 我日常用的比较多的Linux是在树莓派上的Raspbian 8, 但[fzf自己提供的预编译版本](https://github.com/junegunn/fzf-bin/release)在Linux上只有`amd64`和`386`两个版本, 没有针对`arm`的．这个工具又是用Go语言写的, 我对这个语言不熟, 也不想去折腾安装工具链在树莓派上自行编译．于是就可以试试这条路: 跑i386的版本

```
$ sudo apt install qemu-user

$ wget -c 'https://github.com/junegunn/fzf-bin/releases/download/0.16.3/fzf-0.16.3-linux_386.tgz'

$ tar xvf fzf-0.16.3-linux_386.tgz

$ file fzf-0.16-3-linux_386
fzf-0.16.3-linux_386: ELF 32-bit LSB executable, Intel 80386, version 1 (SYSV), statically linked, not stripped

$ qemu-i386 fzf-0.16.3-linux_386 -h
usage: fzf [options]

  Search
    -x, --extended        Extended-search mode
                          (enabled by default; +x or --no-extended to disable)
[...]

$ history | qemu-i386 fzf-0.16.3-linux_386
```

上面倒数第三个命令是检查程序文件`fzf-0.16.3-linux_386`的类型, 从结果看它的确是针对386的ELF文件, 并且是静态链接的；倒数 第二个命令 `qemu-i386 fzf-0.16.3-linux_386 -h`是试着运行一下, 程序成功地跑起来, 打印除了帮助信息．最后一个命令`history | qemu-i386 fzf-0.16.3-linux_386`是真正在使用fzf这个程序的功能．

## 2\. 用binfmt\_misc机制来让启动运行更方便

上面虽然把这个程序运行起来了, 但命令行上需要将`qemu-i386`放在前面, 也就是说实际启动的`qemu-i386`这个程序, 它再把`fzf`跑起来．这样并不太方便, 尤其`fzf`这个程序一般都不是直接使用, 而是通过[fzf-tmux](https://github.com/junegunn/fzf), [fkill](https://github.com/atweiden/fzf-extras)等封装脚本来使用, 脚本里面准备好备选数据后再调用`fzf`程序文件来让用户挑选, 我们要一一修改这些脚本就太麻烦了．

perl/python脚本就不需要这样, 只要第一行是`#!/usr/bin/perl`或者`#!/usr/bin/env python`就可以了．我们能借用这个方法吗？`fzf-0.16.3-linux_386`是个二进制的可执行程序, 我们没办法去修改所谓的＂第一行＂；

对了, 有没有注意到, 安装wine之后, 命令行上直接输入`notepad.exe`也是可以直接启动＂记事本＂程序的, 并不一定需要`wine notepad.exe`才能启动, 这是怎么实现的呢？

这就需要一种叫做`binfmt_misc`的机制．

`binfmt_misc`是Linux内核说提供的一种扩展机制, 使得更多类型的文件得以成为＂可执行＂文件．Linux内核本身支持ELF、a.out、脚本（也就是上面所说的第一行`#!`指定解释器的方式）这集中＂可执行文件＂．但它还提供了一个称为`binfmt_misc`的内核模块, 通过这个模块可以动态注册一些＂可执行文件格式＂,注册之后我们就可以直接＂执行＂这个程序文件了．

其实上面用`apt install qemu-user-static`安装这个包时, 它的postinstall脚本已经在`binfmt_misc`中注册了相应的配置, 我们可以通过下面的方式检查一下:

```
$ lsmod | grep binfmt
binfmt_misc             6306  1

$ ls /proc/sys/fs/binfmt_misc/
python2.7   qemu-cris        qemu-mips    qemu-ppc64abi32  qemu-sh4eb        qemu-x86_64
python3.4   qemu-i386        qemu-mipsel  qemu-ppc64le     qemu-sparc        register
qemu-alpha  qemu-m68k        qemu-ppc     qemu-s390x       qemu-sparc32plus  status
qemu-armeb  qemu-microblaze  qemu-ppc64   qemu-sh4         qemu-sparc64

$ cat /proc/sys/fs/binfmt_misc/qemu-i386
enabled
interpreter /usr/bin/qemu-i386-static
flags: OC
offset 0
magic 7f454c4601010100000000000000000002000300
mask fffffffffffefefffffffffffffffffffeffffff

$ xxd fzf-0.16.3-linux_386 | head -2
0000000: 7f45 4c46 0101 0100 0000 0000 0000 0000  .ELF............
0000010: 0200 0300 0100 0000 d090 0908 3400 0000  ............4...

$ ./fzf-0.16.3-linux_386 -h
usage: fzf [options]

  Search
    -x, --extended        Extended-search mode
                          (enabled by default; +x or --no-extended to disable)
[....]
```

解释一下上面几条命令:

1.  `lsmod | grep binfmt`: 这是检查内核模块`binfmt_misc`是否已经加载, 有内容输出说明已经加载了．如果没有加载, 则可以用`modprobe binfmt_misc`来加载它（在当前的很多Linux发行版中, 一般可以通过`sudo systemctl restart systemd-binfmt`来启动/重启它, 修改了注册配置也可以通过这条命令来重新加载）
2.  `ls /proc/sys/fs/binfmt_misc/`: 这是检查内核中目前注册了哪些格式（`register`和`status`这两个除外）
3.  `cat /proc/sys/fs/binfmt_misc/qemu-i386`: 这是在检查我们所关心的与qemu-i386相关的配置, 从输出中可以看到, 对于以`7f454c4601010100000000000000000002000300`开头的文件, 可以调用`/usr/bin/qemu-i386-static`来执行（各字段的详细解释可以参见[binfmt\_misc - Wikipedia](https://en.wikipedia.org/wiki/Binfmt_misc)）
4.  `xxd fzf-0.16.3-linux_386 | head -2`: 这是检查一下我们所想运行的程序文件的开头几个字节是怎样的, 从输出可以看出, 它与上面所注册的信息是匹配的
5.  `./fzf-0.16.3-linux_386 -h`: 这是直接运行了这个i386程序, 可以看到它能够正确打印出帮助信息

关于`binfmt_misc`的一些相关链接:

-   [binfmt\_misc - Wikipedia](https://en.wikipedia.org/wiki/Binfmt_misc)
-   [How programs get run - LWN.net](https://lwn.net/Articles/630727/)
-   [Linux binfmt\_misc - LuaJIT Wiki](http://wiki.luajit.org/Linux-binfmt_misc): 如何直接执行LuaJIT字节码
-   [Using Go as a scripting language in Linux](https://blog.cloudflare.com/using-go-as-a-scripting-language-in-linux/): 因为Go语言不允许第一行用`#!/usr/bin/env go`, 所以需要另外找一个方法来将\*.go作为脚本运行
-   [binfmt\_misc for Java - ArchWiki](https://wiki.archlinux.org/index.php/Binfmt_misc_for_Java)

## 3\. 补充说明：现实并没有那么简单/美好

虽然在上面我们成功运行了`fzf-0.16.3-linux_386`, 但如果你多实验几个程序, 就会发现失败几率是比较高的．因为大多数程序都会环境有很多依赖, 比如动态库依赖、数据文件/配置文件、子进程调用、CPU扩展指令集、环境变量、设备文件等等, 它们的缺失或者错误都可能导致程序无法正常运行．很少有只需要单个程序文件就能跑起来的（上面运行的`fzf-0.16.3-linux_386`是个静态链接版本, Go语言写的工具一般都是静态链接的）．

对于动态库依赖、数据文件/配置文件这类文件系统层面的问题, 虽然表面上可以想办法把文件补齐, 比如Debian/Ubuntu考虑了多架构并存, 但其它Linux发行版并没有考虑这个问题（有的考虑了x86\_64与x86并存）, 混合安装也会给问题定位带来诸多困难．所以在实际使用中, `qemu-user`大都是通过[chroot](https://en.wikipedia.org/wiki/Chroot)在一个独立的文件系统中运行的．关于qemu与chroot配合的话题下次再展开吧

> Debian/Ubuntu的动态库都安装在`.../lib/<target>-<vendor>-<abi>`目录下, 比如同样一个动态库`libncurses.so.5.9`, 通过`libncurses5:armhf`包提供的动态库安装在`/lib/arm-linux-gnueabihf/libncurses.so.5.9`,通过`libncurses5:i386`包提供的动态库安装在`/lib/i386-linux-gnu/libncurses.so.5.9`  
> （通过`dpkg --add-architecture armhf && apt-get update && apt-get install libc6:armhf`这种方式可以并行安装多种架构的包）

> `qemu-user`有一个`-L path`选项, 可以用来变更动态库查找路径(/set the elf interpreter prefix to 'path'/): 将程序所需要的动态库都放置到 `/home/bamanzi/i386-libs/lib` 目录下, 然后用`qemu-user -L /home/bamanzi/i386-libs ./prog`来启动程序, 就会优先到`/home/bamanzi/i386-libs/lib`查找`prog`所需要的动态库, 而不是主机里面`/etc/ld.so.conf`里面设定的路径




#second way

wine


#third way

https://github.com/AndreRH/hangover