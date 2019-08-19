#socket()

SOCKET(2)                 Linux Programmer's Manual                SOCKET(2)
NAME         top
       socket - create an endpoint for communication
SYNOPSIS         top
       #include <sys/types.h>          /* See NOTES */
       #include <sys/socket.h>

       int socket(int domain, int type, int protocol);
DESCRIPTION         top
       socket() creates an endpoint for communication and returns a file
       descriptor that refers to that endpoint.  The file descriptor
       returned by a successful call will be the lowest-numbered file
       descriptor not currently open for the process.

       The domain argument specifies a communication domain; this selects
       the protocol family which will be used for communication.  These
       families are defined in <sys/socket.h>.  The formats currently
       understood by the Linux kernel include:

       Name         Purpose                                    Man page
       AF_UNIX      Local communication                        unix(7)
       AF_LOCAL     Synonym for AF_UNIX
       AF_INET      IPv4 Internet protocols                    ip(7)
       AF_AX25      Amateur radio AX.25 protocol               ax25(4)
       AF_IPX       IPX - Novell protocols
       AF_APPLETALK AppleTalk                                  ddp(7)
       AF_X25       ITU-T X.25 / ISO-8208 protocol             x25(7)
       AF_INET6     IPv6 Internet protocols                    ipv6(7)
       AF_DECnet    DECet protocol sockets
       AF_KEY       Key management protocol, originally
                    developed for usage with IPsec
       AF_NETLINK   Kernel user interface device               netlink(7)
       AF_PACKET    Low-level packet interface                 packet(7)
       AF_RDS       Reliable Datagram Sockets (RDS) protocol   rds(7)
                                                               rds-rdma(7)
       AF_PPPOX     Generic PPP transport layer, for setting
                    up up L2 tunnels (L2TP and PPPoE)
       AF_LLC       Logical link control (IEEE 802.2 LLC)
                    protocol
       AF_IB        InfiniBand native addressing
       AF_MPLS      Multiprotocol Label Switching
       AF_CAN       Controller Area Network automotive bus
                    protocol
       AF_TIPC      TIPC, "cluster domain sockets" protocol
       AF_BLUETOOTH Bluetooth low-level socket protocol
       AF_ALG       Interface to kernel crypto API
       AF_VSOCK     VSOCK (originally "VMWare VSockets")       vsock(7)
                    protocol for hypervisor-guest
                    communication
       AF_KCM       KCM (kernel connection multiplexor)
                    interface
       AF_XDP       XDP (express data path) interface

       Further details of the above address families, as well as information
       on several other address families, can be found in
       address_families(7).

       The socket has the indicated type, which specifies the communication
       semantics.  Currently defined types are:

       SOCK_STREAM     Provides sequenced, reliable, two-way, connection-
                       based byte streams.  An out-of-band data transmission
                       mechanism may be supported.

       SOCK_DGRAM      Supports datagrams (connectionless, unreliable
                       messages of a fixed maximum length).

       SOCK_SEQPACKET  Provides a sequenced, reliable, two-way connection-
                       based data transmission path for datagrams of fixed
                       maximum length; a consumer is required to read an
                       entire packet with each input system call.

       SOCK_RAW        Provides raw network protocol access.

       SOCK_RDM        Provides a reliable datagram layer that does not
                       guarantee ordering.

       SOCK_PACKET     Obsolete and should not be used in new programs; see
                       packet(7).

       Some socket types may not be implemented by all protocol families.

       Since Linux 2.6.27, the type argument serves a second purpose: in
       addition to specifying a socket type, it may include the bitwise OR
       of any of the following values, to modify the behavior of socket():

       SOCK_NONBLOCK   Set the O_NONBLOCK file status flag on the open file
                       description (see open(2)) referred to by the new file
                       descriptor.  Using this flag saves extra calls to
                       fcntl(2) to achieve the same result.

       SOCK_CLOEXEC    Set the close-on-exec (FD_CLOEXEC) flag on the new
                       file descriptor.  See the description of the
                       O_CLOEXEC flag in open(2) for reasons why this may be
                       useful.

       The protocol specifies a particular protocol to be used with the
       socket.  Normally only a single protocol exists to support a
       particular socket type within a given protocol family, in which case
       protocol can be specified as 0.  However, it is possible that many
       protocols may exist, in which case a particular protocol must be
       specified in this manner.  The protocol number to use is specific to
       the “communication domain” in which communication is to take place;
       see protocols(5).  See getprotoent(3) on how to map protocol name
       strings to protocol numbers.

       Sockets of type SOCK_STREAM are full-duplex byte streams.  They do
       not preserve record boundaries.  A stream socket must be in a
       connected state before any data may be sent or received on it.  A
       connection to another socket is created with a connect(2) call.  Once
       connected, data may be transferred using read(2) and write(2) calls
       or some variant of the send(2) and recv(2) calls.  When a session has
       been completed a close(2) may be performed.  Out-of-band data may
       also be transmitted as described in send(2) and received as described
       in recv(2).

       The communications protocols which implement a SOCK_STREAM ensure
       that data is not lost or duplicated.  If a piece of data for which
       the peer protocol has buffer space cannot be successfully transmitted
       within a reasonable length of time, then the connection is considered
       to be dead.  When SO_KEEPALIVE is enabled on the socket the protocol
       checks in a protocol-specific manner if the other end is still alive.
       A SIGPIPE signal is raised if a process sends or receives on a broken
       stream; this causes naive processes, which do not handle the signal,
       to exit.  SOCK_SEQPACKET sockets employ the same system calls as
       SOCK_STREAM sockets.  The only difference is that read(2) calls will
       return only the amount of data requested, and any data remaining in
       the arriving packet will be discarded.  Also all message boundaries
       in incoming datagrams are preserved.

       SOCK_DGRAM and SOCK_RAW sockets allow sending of datagrams to
       correspondents named in sendto(2) calls.  Datagrams are generally
       received with recvfrom(2), which returns the next datagram along with
       the address of its sender.

       SOCK_PACKET is an obsolete socket type to receive raw packets
       directly from the device driver.  Use packet(7) instead.

       An fcntl(2) F_SETOWN operation can be used to specify a process or
       process group to receive a SIGURG signal when the out-of-band data
       arrives or SIGPIPE signal when a SOCK_STREAM connection breaks
       unexpectedly.  This operation may also be used to set the process or
       process group that receives the I/O and asynchronous notification of
       I/O events via SIGIO.  Using F_SETOWN is equivalent to an ioctl(2)
       call with the FIOSETOWN or SIOCSPGRP argument.

       When the network signals an error condition to the protocol module
       (e.g., using an ICMP message for IP) the pending error flag is set
       for the socket.  The next operation on this socket will return the
       error code of the pending error.  For some protocols it is possible
       to enable a per-socket error queue to retrieve detailed information
       about the error; see IP_RECVERR in ip(7).

       The operation of sockets is controlled by socket level options.
       These options are defined in <sys/socket.h>.  The functions
       setsockopt(2) and getsockopt(2) are used to set and get options.
RETURN VALUE         top
       On success, a file descriptor for the new socket is returned.  On
       error, -1 is returned, and errno is set appropriately.
ERRORS         top
       EACCES Permission to create a socket of the specified type and/or
              protocol is denied.

       EAFNOSUPPORT
              The implementation does not support the specified address
              family.

       EINVAL Unknown protocol, or protocol family not available.

       EINVAL Invalid flags in type.

       EMFILE The per-process limit on the number of open file descriptors
              has been reached.

       ENFILE The system-wide limit on the total number of open files has
              been reached.

       ENOBUFS or ENOMEM
              Insufficient memory is available.  The socket cannot be
              created until sufficient resources are freed.

       EPROTONOSUPPORT
              The protocol type or the specified protocol is not supported
              within this domain.

       Other errors may be generated by the underlying protocol modules.
CONFORMING TO         top
       POSIX.1-2001, POSIX.1-2008, 4.4BSD.

       The SOCK_NONBLOCK and SOCK_CLOEXEC flags are Linux-specific.

       socket() appeared in 4.2BSD.  It is generally portable to/from non-
       BSD systems supporting clones of the BSD socket layer (including
       System V variants).
NOTES         top
       POSIX.1 does not require the inclusion of <sys/types.h>, and this
       header file is not required on Linux.  However, some historical (BSD)
       implementations required this header file, and portable applications
       are probably wise to include it.

       The manifest constants used under 4.x BSD for protocol families are
       PF_UNIX, PF_INET, and so on, while AF_UNIX, AF_INET, and so on are
       used for address families.  However, already the BSD man page
       promises: "The protocol family generally is the same as the address
       family", and subsequent standards use AF_* everywhere.
EXAMPLE         top
       An example of the use of socket() is shown in getaddrinfo(3).
SEE ALSO         top
       accept(2), bind(2), close(2), connect(2), fcntl(2), getpeername(2),
       getsockname(2), getsockopt(2), ioctl(2), listen(2), read(2), recv(2),
       select(2), send(2), shutdown(2), socketpair(2), write(2),
       getprotoent(3), address_families(7), ip(7), socket(7), tcp(7),
       udp(7), unix(7)

       “An Introductory 4.3BSD Interprocess Communication Tutorial” and “BSD
       Interprocess Communication Tutorial”, reprinted in UNIX Programmer's
       Supplementary Documents Volume 1.
COLOPHON         top
       This page is part of release 5.02 of the Linux man-pages project.  A
       description of the project, information about reporting bugs, and the
       latest version of this page, can be found at
       https://www.kernel.org/doc/man-pages/.

Linux                            2019-03-06                        SOCKET(2)\


>Check all avilable protocls in **/etc/protocols**


#ioctl()

IOCTL(2)                  Linux Programmer's Manual                 IOCTL(2)
NAME         top
       ioctl - control device
SYNOPSIS         top
       #include <sys/ioctl.h>

       int ioctl(int fd, unsigned long request, ...);
DESCRIPTION         top
       The ioctl() system call manipulates the underlying device parameters
       of special files.  In particular, many operating characteristics of
       character special files (e.g., terminals) may be controlled with
       ioctl() requests.  The argument fd must be an open file descriptor.

       The second argument is a device-dependent request code.  The third
       argument is an untyped pointer to memory.  It's traditionally char
       *argp (from the days before void * was valid C), and will be so named
       for this discussion.

       An ioctl() request has encoded in it whether the argument is an in
       parameter or out parameter, and the size of the argument argp in
       bytes.  Macros and defines used in specifying an ioctl() request are
       located in the file <sys/ioctl.h>.
RETURN VALUE         top
       Usually, on success zero is returned.  A few ioctl() requests use the
       return value as an output parameter and return a nonnegative value on
       success.  On error, -1 is returned, and errno is set appropriately.
ERRORS         top
       EBADF  fd is not a valid file descriptor.

       EFAULT argp references an inaccessible memory area.

       EINVAL request or argp is not valid.

       ENOTTY fd is not associated with a character special device.

       ENOTTY The specified request does not apply to the kind of object
              that the file descriptor fd references.
CONFORMING TO         top
       No single standard.  Arguments, returns, and semantics of ioctl()
       vary according to the device driver in question (the call is used as
       a catch-all for operations that don't cleanly fit the UNIX stream I/O
       model).  See ioctl_list(2) for a list of many of the known ioctl()
       calls.  The ioctl() system call appeared in Version 7 AT&T UNIX.
NOTES         top
       In order to use this call, one needs an open file descriptor.  Often
       the open(2) call has unwanted side effects, that can be avoided under
       Linux by giving it the O_NONBLOCK flag.
SEE ALSO         top
       execve(2), fcntl(2), ioctl_console(2), ioctl_fat(2),
       ioctl_ficlonerange(2), ioctl_fideduperange(2), ioctl_getfsmap(2),
       ioctl_iflags(2), ioctl_list(2), ioctl_ns(2), ioctl_tty(2),
       ioctl_userfaultfd(2), open(2), sd(4), tty(4)
COLOPHON         top
       This page is part of release 5.02 of the Linux man-pages project.  A
       description of the project, information about reporting bugs, and the
       latest version of this page, can be found at
       https://www.kernel.org/doc/man-pages/.

Linux                            2017-05-03                         IOCTL(2)





#memcmp()
- 描述
C 库函数 int memcmp(const void *str1, const void *str2, size_t n)) 把存储区 str1 和存储区 str2 的前 n 个字节进行比较。

- 声明
下面是 memcmp() 函数的声明。
``int memcmp(const void *str1, const void *str2, size_t n)``
- 参数
str1 -- 指向内存块的指针。
str2 -- 指向内存块的指针。
n -- 要被比较的字节数。
- 返回值
如果返回值 < 0，则表示 str1 小于 str2。
如果返回值 > 0，则表示 str2 小于 str1。
如果返回值 = 0，则表示 str1 等于 str2。



#pragma

pragma指令简介

在编写程序的时候，我们经常要用到#pragma指令来设定编译器的状态或者是指示编译器完成一些特定的动作。

一.message参数

message它能够在编译消息输出窗口中输出相应的消息，这对于源代码信息的控制非常重要的，使用方法为：
```
#pragma message（“消息文本”）
```
当编译器遇到这条指令时就在编译输出窗口中将消息文本打印出来。当我们在程序中定义了许多宏来控制源代码版本的时候，我们自己有可能都会忘记有没有正确的设置这些宏，此时我们可以用这条指令在编译的候进行检查，假设我们希望判断自己有没有源代码的什么地方定义了_X86这个宏可以用下面的方法：
```
#ifdef _x86

#pragma message("_x86 macro activated!")

#endif
```
当我们定义了_X86这个宏以后，应用程序在编译时就会在编译输出窗口里显示"_x86 macro activated!"。

二. 另一个使用得比较多的#pragma参数是code_seg。格式如：
```
#pragma code_seg([[{push|pop},][identifier,]]["segment-name"][,"segment-class"])
```
该指令用来指定函数在.obj文件中存放的节，观察OBJ文件可以使用VC自带的dumpbin命令行程序,函数在.obj文件中默认的存放节 
为.text节 
如果code_seg没有带参数的话,则函数存放在.text节中 
push (可选参数) 将一个记录放到内部编译器的堆栈中,可选参数可以为一个标识符或者节名 
pop(可选参数) 将一个记录从堆栈顶端弹出,该记录可以为一个标识符或者节名 
identifier (可选参数) 当使用push指令时,为压入堆栈的记录指派的一个标识符,当该标识符被删除的时候和其相关的堆栈中的记录将被弹出堆栈 
"segment-name" (可选参数) 表示函数存放的节名 
例如: 
```
//默认情况下,函数被存放在.text节中 
void func1() { // stored in .text 
} 

//将函数存放在.my_data1节中 
#pragma code_seg(".my_data1") 
void func2() { // stored in my_data1 
} 

//r1为标识符,将函数放入.my_data2节中 
#pragma code_seg(push, r1, ".my_data2") 
void func3() { // stored in my_data2 
} 

int main() { 
} 
```
三. #pragma once（比较常用）

这是一个比较常用的指令,只要在头文件的最开始加入这条指令就能够保证头文件被编译一次

``#pragma once``用来防止某个头文件被多次include，#ifndef，#define，#endif用来防止某个宏被多次定义。

``#pragma once``是编译相关，就是说这个编译系统上能用，但在其他编译系统不一定可以，也就是说移植性差，不过现在基本上已经是每个编译器都有这个定义了。

``#ifndef，#define，#endif``这个是C++语言相关，这是C++语言中的宏定义，通过宏定义避免文件多次编译。所以在所有支持C++语言的编译器上都是有效的，如果写的程序要跨平台，最好使用这种方式

四. #pragma hdrstop表示预编译头文件到此为止，后面的头文件不进行预编译。 

BCB可以预编译头文件以加快链接的速度，但如果所有头文件都进行预编译又可能占太多磁盘空间，所以使用这个选项排除一些头文件。 
有时单元之间有依赖关系，比如单元A依赖单元B，所以单元B要先于单元A编译。你可以用#pragma startup指定编译优先级， 
如果使用了#pragma package(smart_init) ，BCB就会根据优先级的大小先后编译。 



五. #pragma warning指令 

该指令允许有选择性的修改编译器的警告消息的行为 


指令格式如下: 
```
#pragma warning( warning-specifier : warning-number-list [; warning-specifier : warning-number-list...] 
#pragma warning( push[ ,n ] ) 
#pragma warning( pop ) ```

主要用到的警告表示有如下几个: 

once:只显示一次(警告/错误等)消息 
default:重置编译器的警告行为到默认状态 
1,2,3,4:四个警告级别 
disable:禁止指定的警告信息 
error:将指定的警告信息作为错误报告 

如果大家对上面的解释不是很理解,可以参考一下下面的例子及说明 
```
#pragma warning( disable : 4507 34; once : 4385; error : 164 ) ```
等价于： 
```
#pragma warning(disable:4507 34) // 不显示4507和34号警告信息 
#pragma warning(once:4385) // 4385号警告信息仅报告一次 
#pragma warning(error:164) // 把164号警告信息作为一个错误。 ```
同时这个pragma warning 也支持如下格式： 
```
#pragma warning( push [ ,n ] ) 
#pragma warning( pop ) 
这里n代表一个警告等级(1---4)。 
#pragma warning( push )保存所有警告信息的现有的警告状态。 
#pragma warning( push, n)保存所有警告信息的现有的警告状态，并且把全局警告 
等级设定为n。 
#pragma warning( pop )向栈中弹出最后一个警告信息，在入栈和出栈之间所作的 
一切改动取消。例如： 
#pragma warning( push ) 
#pragma warning( disable : 4705 ) 
#pragma warning( disable : 4706 ) 
#pragma warning( disable : 4707 ) 
#pragma warning( pop ) 
```
在这段代码的最后，重新保存所有的警告信息(包括4705，4706和4707) 

在使用标准C++进行编程的时候经常会得到很多的警告信息,而这些警告信息都是不必要的提示, 
所以我们可以使用#pragma warning(disable:4786)来禁止该类型的警告 

在vc中使用ADO的时候也会得到不必要的警告信息,这个时候我们可以通过 
``#pragma warning(disable:4146)来消除该类型的警告信息 ``




六. pragma comment(...) 
该指令的格式为 
``#pragma comment( "comment-type" [, commentstring] ) ``


该指令将一个注释记录放入一个对象文件或可执行文件中, 
comment-type(注释类型):可以指定为五种预定义的标识符的其中一种 
五种预定义的标识符为: 

compiler:将编译器的版本号和名称放入目标文件中,本条注释记录将被编译器忽略 
如果你为该记录类型提供了commentstring参数,编译器将会产生一个警告 
例如:``#pragma comment( compiler ) ``

exestr:将commentstring参数放入目标文件中,在链接的时候这个字符串将被放入到可执行文件中, 
当操作系统加载可执行文件的时候,该参数字符串不会被加载到内存中.但是,该字符串可以被 
dumpbin之类的程序查找出并打印出来,你可以用这个标识符将版本号码之类的信息嵌入到可 
执行文件中! 

lib:这是一个非常常用的关键字,用来将一个库文件链接到目标文件中 


常用的lib关键字，可以帮我们连入一个库文件。 
例如: 
``#pragma comment(lib, "user32.lib") ``
该指令用来将user32.lib库文件加入到本工程中 


linker:将一个链接选项放入目标文件中,你可以使用这个指令来代替由命令行传入的或者在开发环境中 
设置的链接选项,你可以指定/include选项来强制包含某个对象,例如: 
``#pragma comment(linker, "/include:__mySymbol") ``

你可以在程序中设置下列链接选项 

/DEFAULTLIB 
/EXPORT 
/INCLUDE 
/MERGE 
/SECTION 
这些选项在这里就不一一说明了,详细信息请看msdn! 

user:将一般的注释信息放入目标文件中commentstring参数包含注释的文本信息,这个注释记录将被链接器忽略 
例如: 
``#pragma comment( user, "Compiled on " __DATE__ " at " __TIME__ ) ``


补充一个 



``#pragma pack(n) ``

控制对齐 如 

```
#pragma pack(push) 
#pragma pack(1) 
struct s_1{ 
char szname[1]; 
int a; 
}; 
#pragma pack(pop) 
struct s_2{ 
char szname[1]; 
int a; 
}; 
```
则 

```
printf("s_1 size : %d\n", sizeof(struct s_1)); 
printf("s_2 size : %d\n", sizeof(struct s_2)); 
```
得到5，8。


```
#pragma once是一个比较常用的C/C++杂注，只要在头文件的最开始加入这条杂注，就能够保证头文件只被编译一次。

#pragma once是编译器相关的，有的编译器支持，有的编译器不支持，具体情况请查看编译器API文档，不过现在大部分编译器都有这个杂注了。

#ifndef，#define，#endif是C/C++语言中的宏定义，通过宏定义避免文件多次编译。所以在所有支持C++语言的编译器上都是有效的，如果写的程序要跨平台，最好使用这种方式。
```
 

具体写法：

方式一：
```
#ifndef _SOMEFILE_H_
#define _SOMEFILE_H_
.......... // 
一些声明语句
#endif
```
方式二：
```
#pragma once
... ... // 一些声明语句
```