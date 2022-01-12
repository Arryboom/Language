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







#Above is not good enough


---


>https://azeria-labs.com/arm-on-x86-qemu-user/
>https://ownyourbits.com/2018/06/13/transparently-running-binaries-from-any-architecture-in-linux-with-qemu-and-binfmt_misc/
>https://github.com/FEX-Emu/FEX
>https://mac.getutm.app/

```
apt install debootstrap
debootstrap --arch=amd64 stable /stable-chroot http://deb.debian.org/debian/
```






What? you can do that in Linux? It turns out you can!

First, let’s see it in action. Here I retrieve a binary from my Raspberry Pi which is an ARM binary and execute it in my _x86\_64_ machine transparently.

![](https://ownyourbits.com/wp-content/uploads/2018/05/arm-echo.gif)

If you try to do this… it won’t work right away.

$ ./echo
zsh: exec format error: ./echo

First we have a couple things to set up. We will be using [QEMU](http://qemu.org/) in a slightly unconventional way in a combination with a kernel feature called _binfmt\_misc_.

#### QEMU user mode

Obviously our CPU is not able to run foreign machine code instructions. We said we would be using QEMU, but in a slightly unconventional way.

We all know QEMU as a virtual machine, where we load a virtual (fake) hard drive with an operating system and we setup fake hardware to interface with it: a fake CPU, fake keyboard, fake network adapter and so on. This look like this

![](https://ownyourbits.com/wp-content/uploads/2018/06/emul-qemu.png)

But there is also another _mode_ of use in QEMU, called _user emulation_.

When we write a program, we interact with the system through [_system calls_](https://en.wikipedia.org/wiki/System_call). We need to do this in order to interact with the keyboard, terminal, screen, filesystem and so on. This means that when we execute a program, the code that we write is executed in _user space_, and then the kernel does the interacting with the system part for us. We just request things from the kernel such as writing to a file.

In QEMU _system emulation_ this looks like this

![](https://ownyourbits.com/wp-content/uploads/2018/06/qemu-system.png)

In _user mode_, QEMU doesn’t emulate all the hardware, only the CPU.  It executes foreign code in the emulated CPU, and then it captures the syscalls and  forwards them to the host kernel. This way, we are interfacing the native kernel in the same way as any native piece of software. This looks like this

![](https://ownyourbits.com/wp-content/uploads/2018/06/qemu-user.png)

This has many benefits, because we are not emulating all the hardware, which is slow, and also we are not emulating the kernel which is a decent part of the computation that takes place. Actually we don’t even _need_ a kernel. We can understand now why this runs much faster than full system emulation.

As an example, let’s crosscompile a static ARM binary

#include <stdio.h>

int main(int argc, char\*\* argv) {
    printf("hello world\\n");
    return 0;
}

we need to install the toolchain to crosscompile from _x86_ to _armhf_, for instance

\# apt-get install gcc-arm-linux-gnueabihf

, or in Arch Linux

$ pacaur -S aur/arm-linux-gnueabihf-gcc

Then we generate the binary

$ arm-linux-gnueabihf-gcc hello.c -o hello\_arm\_static -static
$ file hello\_arm\_static
hello\_arm\_static: ELF 32-bit LSB executable, ARM, EABI5 version 1 (SYSV), statically linked, for GNU/Linux 3.2.0, BuildID\[sha1\]=69ff53a55d64975f87b9ea3543d26bcbae31de9f, with debug\_info, not stripped

Now we can run it with _qemu-arm_. We need to install the package _qemu-user_

\# apt-get install qemu-user

, and now we can run

$ qemu-arm hello\_arm\_static
hello world

This isn’t yet very useful because most programs are _dynamically linked_. We still have some work to do.

#### Running ARM executables transparently

Recall from the [last post](https://ownyourbits.com/2018/05/23/the-real-power-of-linux-executables/) on Linux executables what happens when we execute a file and how we can use _binfmt\_misc_ to set up our own interpreters. Now we have all the pieces and we want to put them together. We need to setup _binfmt\_misc_ in order to use QEMU user mode as an interpreter for our _binary format_.

We can do it ourselves manually, or install the _qemu-user-binfmt_ package, normally installed automatically with _qemu-user_. We end up with the _binfmt\_misc_ entries

ls -l /proc/sys/fs/binfmt\_misc
total 0
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-aarch64
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-alpha
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-arm
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-armeb
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-cris
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-m68k
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-microblaze
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-mips
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-mipsel
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-ppc
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-ppc64
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-ppc64abi32
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-s390x
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-sh4
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-sh4eb
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-sparc
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-sparc32plus
-rw-r--r-- 1 root root 0 Jun  7 11:36 qemu-sparc64
--w------- 1 root root 0 Jun  7 11:36 register
-rw-r--r-- 1 root root 0 Jun  7 11:36 status

Now we can substitute

$ qemu\_arm hello\_arm\_static
hello world

for

$ ./hello\_arm\_static
hello world

, because we have an active entry in _binfmt\_misc_

$ cat /proc/sys/fs/binfmt\_misc/qemu-arm
enabled
interpreter /usr/bin/qemu-arm-static
flags: OC
offset 0
magic 7f454c4601010100000000000000000002002800
mask ffffffffffffff00fffffffffffffffffeffffff

The kernel recognizes the ARM [ELF magic](http://man7.org/linux/man-pages/man5/elf.5.html), and uses the interpreter /usr/bin/qemu-arm-static , which is the correct QEMU binary for the architecture. 0x7F ‘ELF’  in hexadecimal is 7f 45 4c 46, so we can see how the magic and the mask work together, considering the structure of the ELF header

typedef struct {
    unsigned char e\_ident\[EI\_NIDENT\];   /\* 0x7F 'ELF' four byte ELF magic for any architecture \*/
    uint16\_t e\_type;
    uint16\_t e\_machine;                 /\* architecture code, 40=0x28 in the case of ARM \*/
    uint32\_t e\_version;
    ElfN\_Addr e\_entry;
    ElfN\_Off e\_phoff;
    ElfN\_Off e\_shoff;
    uint32\_t e\_flags;
    uint16\_t e\_ehsize;
    uint16\_t e\_phentsize;
    uint16\_t e\_phnum;
    uint16\_t e\_shentsize;
    uint16\_t e\_shnum;
    uint16\_t e\_shstrndx;
} ElfN\_Ehdr;

At the end of the day, we want our code to tell the kernel to print _hello world._ Let’s compare the kernel interactions of the real

$ strace ./hello\_static 2>&1 | grep -e execve -e readlink -e write
execve("./hello\_static", \["./hello\_static"\], 0x7ffd3d83b2b0 /\* 41 vars \*/) = 0
readlink("/proc/self/exe", "/home/nacho/srctest/hello\_static", 4096) = 32
write(1, "hello world\\n", 12hello world

and the emulated code

$ strace ./hello\_arm\_static 2>&1 | grep -e execve -e readlink -e write
execve("./hello\_arm\_static", \["./hello\_arm\_static"\], 0x7ffd4b19b5c0 /\* 41 vars \*/) = 0
readlink("/proc/self/exe", "/usr/bin/qemu-arm-static", 4096) = 24
write(1, "hello world\\n", 12hello world

The _execve()_ syscall is the same, and the _write()_ call too so we get the same behaviour. We can also see that a read to _/proc/self/exe_ reveals that the binary being run natively is in fact _qemu-arm-static_, the interpreter.

Again, most of the work is being done natively by the kernel, so this actually runs much faster than in QEMU full emulation because the part of the kernel execution would need to be emulated too, as well as the virtual hardware. It is also much easier to setup.

This is still not _that_ useful yet, because very few programs are statically linked. Let’s create _x86_ and _amrhf_ versions of _hello.c_

$ gcc hello.c -o hello
$ gcc hello.c -o hello\_static -static
$ arm-linux-gnueabihf-gcc hello.c -o hello\_arm
$ arm-linux-gnueabihf-gcc hello.c -o hello\_arm\_static -static

ARM binaries take much more space, because being a RISC architecture it has a smaller instruction set and so it needs more machine code to perform many common operations. Code density can be improved by using the [THUMB instruction set](https://www.embedded.com/electronics-blogs/beginner-s-corner/4024632/Introduction-to-ARM-thumb).

$ dutree
\[ crosshello 4.64 MiB \]
├─ hello\_arm\_static  │          ███████████████████████████████████████████████│  84%      3.91 MiB
├─ hello\_static      │                                                   ██████│  15%    724.89 KiB
├─ hello\_arm         │                                                         │   0%     15.62 KiB
├─ hello             │                                                         │   0%      8.16 KiB
└─ hello.c           │                                                         │   0%          97 B

Let’s try this

$ ./hello\_arm
/lib/ld-linux-armhf.so.3: No such file or directory

Dynamically linked executables provide the path of the runtime linker ( a.k.a ELF interpreter ) hardcoded at compile time.

$ file hello\_arm
hello\_arm: ELF 32-bit LSB pie executable ARM, EABI5 version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux-armhf.so.3, for GNU/Linux 3.2.0, BuildID\[sha1\]=2be332452ae4987fa763b6e75c359e08793572aa, with debug\_info, not stripped

$ file hello
hello: ELF 64-bit LSB pie executable x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2, for GNU/Linux 3.2.0, BuildID\[sha1\]=d11d6a23094a98009919746b13a1c064450aa944, not stripped

So the code fails because it cannot find the linker that it requires _/lib/ld-linux-armhf.so.3_. This normally comes with the cross-toolchain.

We _could_ be tempted to do something really dirty like

\# ln -s /usr/arm-linux-gnueabihf/lib/ld-linux-armhf.so.3 /lib/ld-linux-armhf.so.3

We would need to do this not only for _ld-linux-armhf.so_, but also for _libc.so_ and everything else our binary might need, and we don’t want to have a mix of libraries of different architectures in the same place, right?

We can tell QEMU where to look for the linker and libraries with

$ qemu-arm -L /usr/arm-linux-gnueabihf hello\_arm
hello world

but we want _transparent_ execution, so we can add this to _.bashrc_ or _.zshrc_

export QEMU\_LD\_PREFIX=/usr/arm-linux-gnueabihf

, or configure it system wide at _/etc/qemu-binfmt.conf_

EXTRA\_OPTS="-L /usr/arm-linux-gnueabihf"

Now it works transparently!

$ ./hello\_arm
hello world

This is still not _that_ useful. The reason is that we now need to have a copy of all the ARM libraries required by our ARM binaries.

Our example works because everything _hello.c_ needs is so basic that comes with the toolchain.

$ ldd hello
linux-vdso.so.1 (0x00007ffd71ab5000)
libc.so.6 => /usr/lib/libc.so.6 (0x00007f536fe48000)
/lib64/ld-linux-x86-64.so.2 => /usr/lib64/ld-linux-x86-64.so.2 (0x00007f5370406000)

The situation is not too bad in Debian, where you can install libraries from other architectures, for instance

\# apt-get install libstdc++6:armhf

#### Emulating full ARM rootfs

Most often in real situations we need to work in the final system where the binary is supposed to run. It makes more sense to have the whole ARM environment with its ARM libraries and all. Enter [chroot](https://en.wikipedia.org/wiki/Chroot).

chroot, for _change root_ is a [system call](https://linux.die.net/man/2/chroot) and corresponding command wrapper that changes the root directory location of a process and its children. Given a directory with a different root filesystem, we can execute anything in it so that their _view_ of the filesystem has been moved to the new root directory. For this reason it is often called a _chroot jail_. This is the predecessor of filesystem namespaces, a key component that makes containers possible.

As an example, let’s execute _echo_ inside an x86 jail. I have prepared a whole Debian filesystem in new\_root\_folder .

\# chroot new\_root\_folder /bin/echo "hello world"
hello world

This _echo_, or whatever  binary we run does not see anything outside of the jail. It is impossible for instance to remove or read a file outside of the new root folder.

We can get an existing ARM rootfs to work with, or we can generate one. In Debian we can use [_debootstrap_](https://wiki.debian.org/Debootstrap) with the –arch  switch to generate a Stretch ARM rootfs.

$ debootstrap --arch=armhf stretch new\_root\_folder

What we want to do now is to use chroot to make the binaries inside the jail view the filesystem just like they expect it. By using chroot we already have _/etc_, _/bin_ and all the regular folders in place. Next, we need to add the virtual filesystems

\# mount -t proc proc     new\_root\_folder/proc/
# mount -t sysfs sys     new\_root\_folder/sys/
# mount -o bind /dev     new\_root\_folder/dev/
# mount -o bind /dev/pts new\_root\_folder/dev/pts

Finally, we will copy the _qemu-user-static_ binary inside the ARM filesystem.

\# cp /usr/bin/qemu-arm-static new\_root\_folder/usr/bin

This little intruder will be the only x86 binary in an ARM filesystem, he’s surrounded!

We have everything in place! What will happen when we try to execute some ARM executable from the jail?

-   The chroot command will call _execve()_ on the ARM binary
-   The ARM binary will be handled by the _binfmt\_misc_ binary handler, according to its configured ARM ELF magic.
-   The entry in _binfmt\_misc_ instructs the kernel to use _/usr/bin/qemu-arm-static_ as an interpreter, that is why we had to copy it inside the jail. Remember that by chroot magic /usr/bin is really inside _new\_root\_folder_.
-   _qemu-arm-static_ will interpret the ARM binary in user mode. We are using the _static_ version of qemu-arm because we need the interpreter to be standalone, as it is the only x86 binary in the jail and will not have access to any x86 libraries.
-   Any ARM library that is expected by the programs inside the jail will be there, as provided by the ARM rootfs.

Let’s see all this in action, opening a _bash_ shell in a Raspbian rootfs

![](https://ownyourbits.com/wp-content/uploads/2018/06/qemu-chroot2.gif)

I had to configure the PATH variable to match the one Raspbian expects. Naturally, our original environment from _zsh_ will be inherited by _chroot_ and _arm-bash_. We have talked about full system [QEMU Raspbian emulation](https://ownyourbits.com/2017/02/06/raspbian-on-qemu-with-network-access/) before, and this runs so much faster.

Things will work as long as the ARM binaries see what they expect to see. Binaries can _execve()_ other executables and everything will mostly work perfectly well. An exception would be programs that use exotic system calls that QEMU user mode still has not implemented yet, for instance for using the pseudo random generator. As QEMU user mode becomes more mature, it is getting more strange to see this happen and normally libraries have fallback options for these situations anyway. In those cases you will see something like

qemu: Unsupported syscall: 384

Remember that we are still using our host kernel, so we can use networking, install packages with _apt_ and all the rest. This is really useful for things like

-   Manipulating rootfs images for other architectures transparently from our x86 workstation.
-   Compiling ARM binaries more easily. Cross-compiling is hard because you need to isolate the libraries and tools that the cross-compiling environment needs with the ones from your workstation. One way of saving some headaches is to emulate native compiling instead of cross compiling. You host can then help _natively_ by setting up a [_distcc_](https://en.wikipedia.org/wiki/Distcc) system between jail and host.






----


Running Arm Binaries on x86 with QEMU-User

Ever wanted to play around with Arm assembly without an Arm board and the hassle of setting up a full-system QEMU emulation?

This blog post is a quick and straight-forward way to compile, debug, and run Arm 32- and 64-bit binaries directly on your x86\_64 Linux host system. Full system emulation has its benefits, especially if you want a dedicated environment to tinker around with things like firmware emulation. If you are looking for a quicker way to play around with Arm assembly, run your Arm binaries, and perform simple debugging tasks, the QEMU user mode emulation is more than sufficient.

FYI: If you are looking for a full-system emulation and want to save time, you can download my [Lab VM 2.0](https://azeria-labs.com/lab-vm-2-0/) which contains an Armv7-A emulation. But keep in mind that this is an emulation of a 32-bit architecture.

Executing ARM64 binaries (C to Binary)

FYI: In this tutorial, I’m using an [Ubuntu 20.04.1 LTS](https://ubuntu.com/download/desktop) VM as a host system.

Since processors don’t understand high-level source code directly, we need to convert our C code into machine-code using a compiler. However the GCC compiler you have on your system compiles your code for the architecture of the system it runs on, in this case x86\_64. In order to compile our code for the Arm architecture, we need to use a cross-compiler.

Let’s start with Arm64 and install the following packages:

azeria@ubuntu:~$ sudo apt update -y && sudo apt upgrade -y
azeria@ubuntu:~$ sudo apt install qemu-user qemu-user-static gcc-aarch64-linux-gnu binutils-aarch64-linux-gnu binutils-aarch64-linux-gnu-dbg build-essential

Once installed, create a file containing a simple C program for testing, e.g.

#include <stdio.h>

int main(void) { 
    return printf("Hello, I'm executing ARM64 instructions!\\n"); 
}

To compile the code as a static executable, we can use _aarch64-linux-gnu-gcc_ with the -static flag.

azeria@ubuntu:~$ aarch64-linux-gnu-gcc -static -o hello64 hello.c

But what happens if we run this Arm executable on a different architecture? Executing it on an x86\_64 architecture would normally result in an error telling us that the binary file cannot be executed due to an error in the executable format.

azeria@ubuntu:~$ ./hello64 
bash: ./hello64: cannot execute binary file: Exec format error

We can’t run our Arm binary on an x84\_64 architecture because instructions are encoded differently on these two architectures.

Lucky for us, we can bypass this restriction with the QEMU user emulator which allows us to run binaries for other architectures on our host system. Let’s try it out.

Below you can see that our host is a x86\_64 GNU/Linux system. The binary we have previously compiled is ARM aarch64.

azeria@ubuntu:~$ uname -a 
Linux ubuntu 5.4.0-58-generic #64-Ubuntu SMP Mon Dec 29 08:16:25 UTC 2020 x86\_64 x86\_64 x86\_64 GNU/Linux 
azeria@ubuntu:~$ file hello64 
hello64: ELF 64-bit LSB executable, ARM aarch64, version 1 (GNU/Linux), statically linked, BuildID\[sha1\]=66307a9ec0ecfdcb05002f8ceecd310cc6f6792e, for GNU/Linux 3.7.0, not stripped

Let’s execute it!

azeria@ubuntu:~$ ./hello64 
Hello, I'm executing ARM64 instructions!

Voilà, our statically linked aarch64 binary is running on our x86\_64 host thanks to _qemu-user-static_. But can we execute a dynamically linked Arm executable? Yes, we can. This time, the package that makes this possible is _qemu-user_.

First, compile the C code without the _\-static_ flag. In order to run it, we need to use _qemu-aarch64_ and supply the aarch64 libraries via the _\-L_ flag.

azeria@ubuntu:~$ aarch64-linux-gnu-gcc -o hello64dyn hello64.c
azeria@ubuntu:~$ qemu-aarch64 -L /usr/aarch64-linux-gnu ./hello64dyn
Hello, I'm executing ARM64 instructions!

Nice. Works like a charm. Moving on to Arm32!

Executing ARM32 binaries (C to Binary)

The same procedure applies to Arm 32-bit binaries, but we need to install different packages (in addition to the previously installed _qemu-user_ packages).

$ sudo apt install gcc-arm-linux-gnueabihf binutils-arm-linux-gnueabihf binutils-arm-linux-gnueabihf-dbg

We’ll use the same simple C program as before and call it hello32.c:

#include <stdio.h>

int main(void) { 
    return printf("Hello, I am an ARM32 binary!\\n"); 
}

Now we compile this program as a statically linked Arm32 executable using _arm-linux-gnueabihf-gcc_ with the _\-static flag_ and run it:

azeria@ubuntu:~$ arm-linux-gnueabihf-gcc -static -o hello32 hello32.c
azeria@ubuntu:~$ ./hello32 
Hello, I am an ARM32 binary!

Now let’s compile it as a dynamically linked executable.

azeria@ubuntu:~$ arm-linux-gnueabihf-gcc -o hello32 hello32.c
azeria@ubuntu:~$ qemu-arm -L /usr/arm-linux-gnueabihf ./hello32
Hello, I am an ARM32 binary!

Executing ARM binaries (Assembly to binary)

Now that we know how to compile code for the Arm architecture and run it on an x86\_64 host, let’s try this with assembly source code.

Since processors only understand machine code and not assembly language directly, we need a program to convert our hand-written assembly instructions into their machine-code equivalents. The programs that perform this task are called _assemblers_.

There are different assemblers available on different platforms, such as the [GNU assembler “as”](https://sourceware.org/binutils/docs/as/GNU-Assembler.html#GNU-Assembler) which is also used to assemble the Linux kernel, the ARM Toolchain assembler “[armasm](https://developer.arm.com/documentation/dui0473/j/using-the-assembler?lang=en)”, or the Microsoft assembler with the same name (“[armasm](https://docs.microsoft.com/en-us/cpp/assembler/arm/arm-assembler-reference?view=msvc-160)”) included in Visual Studio. Here we will use the GNU assembler.

Suppose we want to assemble the following hello world assembly program:

.section .text
.global \_start

\_start:
/\* syscall write(int fd, const void \*buf, size\_t count) \*/
    mov x0, #1     
    ldr x1, =msg 
    ldr x2, =len 
    mov w8, #64 
    svc #0

/\* syscall exit(int status) \*/
    mov x0, #0 
    mov w8, #93 
    svc #0

msg:
.ascii "Hello, ARM64!\\n"
len = . - msg

Normally we would assemble and link it with the native _AS_ and _LD_. But the native assembler can only interpret instructions of the architecture it was build to interpret, e.g. x86\_64. Trying to assemble Arm instructions would result in errors:

azeria@ubuntu:~$ as asm64.s -o asm64.o && ld asm64.o -o asm64-2
asm64.s: Assembler messages:
asm64.s:6: Error: expecting operand after ','; got nothing
asm64.s:7: Error: no such instruction: \`ldr x1,=msg'
asm64.s:8: Error: no such instruction: \`ldr x2,=len'
asm64.s:9: Error: expecting operand after ','; got nothing
asm64.s:10: Error: no such instruction: \`svc '
asm64.s:13: Error: expecting operand after ','; got nothing
asm64.s:14: Error: expecting operand after ','; got nothing
asm64.s:15: Error: no such instruction: \`svc

That’s why we need to use a cross-assembler and linker specifically for the instruction set of our program. In this case, A64:

azeria@ubuntu:~$ aarch64-linux-gnu-as asm64.s -o asm64.o && aarch64-linux-gnu-ld asm64.o -o asm64
azeria@ubuntu:~$ ./asm64
Hello, ARM64!

Let’s do the same for the A32 version of this program:

.section .text
.global \_start

\_start:
/\* syscall write(int fd, const void \*buf, size\_t count) \*/
    mov r0, #1 
    ldr r1, =msg 
    ldr r2, =len 
    mov r7, #4 
    svc #0

/\* syscall exit(int status) \*/
    mov r0, #0 
    mov r7, #1 
    svc #0

msg:
.ascii "Hello, ARM32!\\n"
len = . - msg

Assemble and link and…

azeria@ubuntu:~$ arm-linux-gnueabihf-as asm32.s -o asm32.o && arm-linux-gnueabihf-ld -static asm32.o -o asm32
azeria@ubuntu:~$ ./asm32 
Hello, ARM32!

Voilà!

Disassemble Arm binaries on x86\_64

Now that we can compile and run Arm binaries on our host system, let’s take them apart.

The easiest way to look at the disassembly of an ELF binary is with a tool called [_objdump_](https://sourceware.org/binutils/docs/binutils/objdump.html). This is especially useful for small binaries.

But what happens if we use the native _objdump_ from our host system to disassemble an Arm binary?

azeria@ubuntu:~$ objdump -d hello32
hello32: file format elf32-little
objdump: can't disassemble for architecture UNKNOWN!

Since the native _objdump_ expects a binary compiled for the architecture it is running on (x86\_64 in this case), it does not recognize the architecture of the Arm binary we supplied and refuses to disassemble it. But the _objdump_ binary itself does not need to be compiled for the Arm architecture, it only needs to be able to interpret Arm machine code. So all we need is a cross-built of it. If you type “arm-linux” in your terminal and double-tap, you will see all the utilities we have already installed with the _binutils-arm-linux-gnueabihf_ package, and _objdump_ is included!

azeria@ubuntu:~$ arm-linux-gnueabihf-
arm-linux-gnueabihf-addr2line      arm-linux-gnueabihf-gcov-9
arm-linux-gnueabihf-ar             arm-linux-gnueabihf-gcov-dump
arm-linux-gnueabihf-as             arm-linux-gnueabihf-gcov-dump-9
arm-linux-gnueabihf-c++filt        arm-linux-gnueabihf-gcov-tool
arm-linux-gnueabihf-cpp            arm-linux-gnueabihf-gcov-tool-9
arm-linux-gnueabihf-cpp-9          arm-linux-gnueabihf-gprof
arm-linux-gnueabihf-dwp            arm-linux-gnueabihf-ld
arm-linux-gnueabihf-elfedit        arm-linux-gnueabihf-ld.bfd
arm-linux-gnueabihf-gcc            arm-linux-gnueabihf-ld.gold
arm-linux-gnueabihf-gcc-9          arm-linux-gnueabihf-nm
arm-linux-gnueabihf-gcc-ar         arm-linux-gnueabihf-objcopy
arm-linux-gnueabihf-gcc-ar-9       **arm-linux-gnueabihf-objdump**
arm-linux-gnueabihf-gcc-nm         arm-linux-gnueabihf-ranlib
arm-linux-gnueabihf-gcc-nm-9       arm-linux-gnueabihf-readelf
arm-linux-gnueabihf-gcc-ranlib     arm-linux-gnueabihf-size
arm-linux-gnueabihf-gcc-ranlib-9   arm-linux-gnueabihf-strings
arm-linux-gnueabihf-gcov           arm-linux-gnueabihf-strip

Now all we have to do is use _arm-linux-gnueabihf-objdump_. Let’s try this with the asm32 binary:

azeria@ubuntu:~$ arm-linux-gnueabihf-objdump -d asm32

asm32: file format elf32-littlearm

Disassembly of section .text:

00010054 <\_start>:
10054: e3a00001 mov r0, #1
10058: e59f1024 ldr r1, \[pc, #36\] ; 10084 <msg+0x10>
1005c: e59f2024 ldr r2, \[pc, #36\] ; 10088 <msg+0x14>
10060: e3a07004 mov r7, #4
10064: ef000000 svc 0x00000000
10068: e3a00000 mov r0, #0
1006c: e3a07001 mov r7, #1
10070: ef000000 svc 0x00000000

00010074 <msg>:
10074: 6c6c6548 .word 0x6c6c6548
10078: 41202c6f .word 0x41202c6f
1007c: 32334d52 .word 0x32334d52
10080: 00000a21 .word 0x00000a21
10084: 00010074 .word 0x00010074
10088: 0000000e .word 0x0000000e

…and it works!

Debugging Arm binaries on x86\_64

We can also debug these binaries on our host system, but not with the native GDB installation. For our Arm binaries, we will use _gdb-multiarch_.

azeria@ubuntu:~$ sudo apt install gdb-multiarch qemu-user

We can also compile our C code with the _\-ggdb3_ flag which produces additional debugging information for GDB. Let’s compile a statically linked executable for this example:

azeria@ubuntu:~$ arm-linux-gnueabihf-gcc -ggdb3 -o hello32-static hello32.c -static

One of the ways we can debug this binary is to use the _qemu-user_ emulator and have tell GDB to connect to it through a TCP port. To do this, we run _qemu-arm_ with the _-g_ flag and a port number on which it should wait for a GDB connection. The _\-L_ flag sets the ELF interpreter prefix to the path we supply.

azeria@ubuntu:~$ qemu-arm -L /usr/arm-linux-gnueabihf -g 1234 ./hello32-static

Open another terminal window and use the following command:

azeria@ubuntu:~$ gdb-multiarch -q --nh -ex 'set architecture arm' -ex 'file hello32-static' -ex 'target remote localhost:1234' -ex 'layout split' -ex 'layout regs'

The _–nh_ flag instructs it to not read the _.gdbinit_ file (it can get buggy if you have a GDB wrapper installed), and the _\-ex_ options are the commands we want _gdb-multiarch_ to set at the start of the session. The first one sets the target architecture to arm (use arm64 for 64-bit binaries), then we provide the binary itself, tell it where to find the binary running in our _qemu-arm_ emulation. The final two commands are used to split and display the source, disassembly, command, and register windows.

[![](https://azeria-labs.com/wp-content/uploads/2020/12/gdb-multiarch-1024x763.png.pagespeed.ce.jO1vBh7Koi.png)](https://azeria-labs.com/wp-content/uploads/2020/12/gdb-multiarch.png)

Perfect! Now we can debug our Arm binary and step through the individual instructions.

FYI: The terminal I use here is [Terminator](https://terminator-gtk3.readthedocs.io/en/latest/) (apt install terminator), which let’s you split the terminal into multiple windows, e.g. CTRL+Shift+O for horizontal split.

For AArch64, you need to run it with qemu-aarch64 and set the target architecture in _gdb-multiarch_ to arm64:

Terminal 1:

azeria@ubuntu:~$ qemu-aarch64 -L /usr/aarch64-linux-gnu/ -g 1234 ./hello64

Terminal 2:

azeria@ubuntu:~$ gdb-multiarch -q --nh -ex 'set architecture arm64' -ex 'file hello64' -ex 'target remote localhost:1234' -ex 'layout split' -ex 'layout regs'

For dynamically linked binaries gdb-multiarch will complain about missing libraries. If this happens, run this command in gdb-multiarch and provide the path to the libraries:

For AArch64:
(gdb) set solib-search-path /usr/aarch64-linux-gnu/lib/

For AArch32:
(gdb) set solib-search-path /usr/arm-linux-gnueabihf/lib/

Happy debugging!



---

mknod: /root/deroot/test-dev-null: Operation not permitted
E: Cannot install into target '/root/deroot' mounted with noexec or nodev


```
mount -i -o remount,exec,dev /stable-chroot
```






---------------
# using virt


compile qemu-x86_64
```
yum install virt-manager
yum install libvirt
yum install virt-install
service libvirtd start

```
###system

```
wget https://cloud.centos.org/centos/7/images/CentOS-7-x86_64-GenericCloud-2111.qcow2
qemu-img convert -f qcow2 -O raw CentOS-7-x86_64-GenericCloud-2111.qcow2 CentOS-7-x86_64-GenericCloud-2111.raw
qemu-img create -b focal-server-cloudimg-amd64.raw -f qcow2 -F qcow2 hal9000.img 10G
#virt-install --name=rusts --ram=16384 --vcpus=4 --arch=x86_64 --import --disk path=/data/osimage/rusthd.img,format=qcow2 --os-variant=centos7.0 --network bridge=virbr0,model=virtio --graphics vnc,listen=0.0.0.0
virt-install --name=rusts --ram=16384 --vcpus=4 --arch=x86_64 --import --disk path=/data/osimage/rusthd.img,format=qcow2 --os-variant=centos7.0 --network bridge=virbr0,model=virtio --virt-type qemu --graphics none  --noreboot --noautoconsole --extra-args='console=ttyS0'
```

>virt-install --name=rusts --ram=16384 --vcpus=4 --arch=x86_64 --import --disk path=/data/osimage/rusthd.img,format=qcow2 --os-variant=centos7.0 --network bridge=virbr0,model=virtio --nographic


>qemu-system-x86_64 \
-drive if=virtio,file=/home/oc/VM/img/netbsd.image,index=0,media=disk \
-M q35,accel=kvm -m 250M -cpu host -smp $(nproc) \
-nic user,hostfwd=tcp:127.0.0.1:5555-:22,model=virtio-net-pci,ipv6=off  \
-daemonize -display none  -vga none \
-serial mon:telnet:127.0.0.1:6665,server,nowait \
-pidfile /home/oc/VM/pid/netbsd-pid \


---
unnessary
```
$ virt-customize -a MY-CLOUD-IMAGE.qcow2 \
    --root-password password:SUPER-SECRET-PASSWORD \
    --uninstall cloud-init
virt-install --install fedora32,cloud=yes
```

>https://blog.wikichoon.com/2020/09/virt-install-cloud-init.html
>https://sumit-ghosh.com/articles/create-vm-using-libvirt-cloud-images-cloud-init/
>https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu



s it possible to disable KVM support for qemu? My system doesn't support VT, so i don't need and want KVM.

When trying to start my VM, qemu complains:

# qemu-kvm
Could not access KVM kernel module: No such file or directory
failed to initialize KVM: No such file or directory
No accelerator found!

# qemu-system-x86_64 1
Could not access KVM kernel module: No such file or directory
failed to initialize KVM: No such file or directory
No accelerator found!

Got the solution:

There's a (undocumented) -no-kvm CMDline switch.

In my opinion, there should be a KVM useflag for qemu, being disabled at default.
kvm should be opt-in.


$ qemu-kvm --help | grep no-kvm
-no-kvm disable KVM hardware virtualization
-no-kvm-irqchip disable KVM kernel mode PIC/IOAPIC/LAPIC
-no-kvm-pit disable KVM kernel mode PIT
-no-kvm-pit-reinjection





To see a printout of all the supported machines use:

 qemu-system-arm -M help

or

 qemu-system-aarch64 -M help

## Build Directions

 ./configure --target-list=arm-softmmu,aarch64-softmmu && make

## Suggested command-lines

Using virtio-mmio (and the virt machine)

 qemu-system-aarch64 -m <memory size> -M virt -drive if=none,file=<hard drive file name>,id=hd0 \\
   -device virtio-blk-device,drive=hd0 -netdev type=tap,id=net0 -device virtio-net-device,netdev=net0

However for more featureful guests it's recommended to use discoverable buses. So for example the same as above but now using a PCI bus on which sits a SCSI controller with a blockdevice and networks card (using userspace networking this time). The transport is still virtio

 qemu-system-aarch64 -m <memory size> -M virt -device virtio-scsi-pci \\
   -device scsi-hd,drive=hd0 -blockdev node-name=hd0,filename=<hard drive file name> \\
   -netdev user,id=unet -device virtio-net-pci,netdev=unet



>https://wiki.qemu.org/Documentation/Platforms/ARM



 dnf install qemu-system-aarch64

https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/235471261/Virtually+Install+CentOS+and+Fedora+on+Zynq+UltraScale


###deattach qemu
**`Ctrl-A X`**

For `-nographic` just enter:

```
Ctrl-A X
```

which means

1. first press <kbd>Ctrl + A</kbd> (A is just key <kbd>a</kbd>, not the <kbd>alt</kbd> key),
2. then release the keys,
3. afterwards press <kbd>X</kbd>.

Alternatively:

- enter the QEMU monitor with `Ctrl-A C` and then type:
    
    ```
    quit
    ```
    
    and press enter. See also: [https://stackoverflow.com/questions/14165158/how-to-switch-to-qemu-monitor-console-when-running-with-curses](https://stackoverflow.com/questions/14165158/how-to-switch-to-qemu-monitor-console-when-running-with-curses)
    
- use the QEMU monitor (same as `Ctral-A C`) with `telnet`:
    
    ```
    qemu-system-x86_64 -monitor telnet::45454,server,nowait -serial mon:stdio
    ```
    
    and on a host terminal:
    
    ```
    telnet localhost 45454
    ```
    
    and then `quit` from there.
    
    `-serial mon:stdio` is required to keep Ctrl+C working: [https://stackoverflow.com/questions/49716931/how-to-run-qemu-with-nographic-and-monitor-but-still-be-able-to-send-ctrlc-to/49751144#49751144](https://stackoverflow.com/questions/49716931/how-to-run-qemu-with-nographic-and-monitor-but-still-be-able-to-send-ctrlc-to/49751144#49751144)
    
- shut down the VM from guest normally, e.g. with a `powerdown` command from a Linux guest shell if you are able. This or course goes through the normal shutdown sequence instead of immediately killing the VM, but sometimes it is just the simplest approach.
    
    It does not work for all machines however: [https://stackoverflow.com/questions/31990487/how-to-cleanly-exit-qemu-after-executing-bare-metal-program-without-user-interve](https://stackoverflow.com/questions/31990487/how-to-cleanly-exit-qemu-after-executing-bare-metal-program-without-user-interve)
    

Tested in Ubuntu 17.10, QEMU 2.10.1.







```
#!/bin/bash 
PORT=4500
while [[ $PORT -le 4530 ]]; do
        echo The counter is $PORT
        qemu-system-arm -enable-kvm -M virt -cpu host \
          -kernel zImage -initrd core-image-minimal-qemuarm.cpio.gz \
          -nographic -monitor none -m 24 -smp 1 \
          -chardev socket,host=127.0.0.1,telnet,port=$PORT,server,nowait,id=char0 \
          -serial chardev:char0 -append "console=ttyAMA0,115200 quiet" &
        PORT=$((PORT+1))
        sleep 10
done
```
>https://falstaff.agner.ch/2016/08/23/using-kvm-with-qemu-on-arm/



```
virsh shutdown rusts
virsh start rusts
virsh console rusts
virsh help console
# virsh net-list
# virsh net-info default
virsh net-dhcp-leases default
virsh domifaddr freebsd11.1
virsh undefine VM_NAME
```
To exit a virsh console session, type ```CTRL+Shift followed by ]``` or ```CTRL+Shift+5```

s
###reset password

>https://www.cyberciti.biz/faq/how-to-reset-forgotten-root-password-for-linux-kvm-qcow2-image-vm/


```
yum install libguestfs-tools
```



###extend


>www.linux-kvm.com/content/tip-how-run-headless-guest-machine-using-vnc-kvm
>www.linux-kvm.com/content/running-kvm-nographics-no-console-output
>https://serverfault.com/questions/257962/kvm-guest-installed-from-console-but-how-to-get-to-the-guests-console

```
qemu -enable-kvm                     \
     -smp 4                          \
     -cpu host                       \
     -m 1G                           \
     -drive file=$VDISK_FILENAME     \
     -cdrom grub-img.iso             \
     -boot order=c,once=d,menu=on    \
     -net nic,netdev=net0            \
     -netdev user,id=net0            \
     -device ac97                    \
     -vga std                        \
     -serial mon:stdio               \
     -name "fedora-16"
```

 Meaning of the command line options

-enable-kvm: enable full KVM virtualization support. On some hardware, it may be necessary to add the undocumented -machine smm=off option in order to enable KVM.

-smp <N>: enable symmetric multiprocessing with <N> CPUs.

-cpu <model>: simulate CPU <model>. the list of supported models can be obtained with -cpu help.

-drive file=<filename>: defines a virtual disk whose image is stored in <filename>.

-cdrom grub-img.iso: defines an iso formated file to use as a cdrom. Here we use a grub rescue disk, which may turn handy when something goes wrong at boot time.

-boot order=c,once=d,menu=on: defines the boot order for the virtual BIOS.

-net nic,netdev=<netid>: defines a network card connected to the network device with id <netid>.

-netdev user,id=<netid>: defines the network “user” device. This is a virtual local network with addresses 10.0.2.0/24, where the host has address 10.0.2.2 and acts as a gateway to internet, and with a name server at address 10.0.2.3, and an smb server at address 10.0.2.4. A builtin DHCP server can allocate addresses between 10.0.2.15 and 10.0.2.31.

-soundhw <model>: defines the soundcard model. The list may be obtained with -soundhw help.

-vga <type>: defines the type of vga card to emulate.

-serial mon:stdio: sends the serial port of the guest (/dev/ttyS0 on linux guests), multiplexed with the qemu monitor, to the standard input and output of the qemu process.

-name <name>: sets the name of the guest. This name is displayed in the guest window caption. It may be useful if you run several guests at the same time.

---




**elf2dmp**

Converts files from elf to dmp format

**qemu-edid**

is a test tool for the qemu EDID generator

**qemu-ga**

implements support for QMP (QEMU Monitor Protocol) commands and events that terminate and originate respectively within the guest using an agent built as part of QEMU

**qemu-img**

provides commands to manage QEMU disk images

**qemu-io**

is a diagnostic and manipulation program for (virtual) memory media. It is still at an early stage of development

**qemu-keymap**

generates qemu reverse keymaps from xkb keymaps, which can be used with the qemu "-k" command line switch

**qemu-nbd**

exports Qemu disk images using the QEMU Disk Network Block Device (NBD) protocol

**qemu-pr-helper**

Implements the persistent reservation helper for QEMU

**qemu-storage-daemon**

allows to modify disk images using the QEMU Monitor Protocol (QMP) without running a VM

**qemu-system-x86\_64**

is the QEMU PC System emulator



---

### console

I copied --extra-args='console=ttyS0' somewhere from Internet last time, it works!

    No need for two "console" commands, that just opens two consoles instead of one
    No need to manually specify the baud rate.
    "--serial" option has been deprecated
    The RedHat console device is "/dev/ttyS0", not "/dev/tty0"
