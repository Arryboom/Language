



#basic


```-   .\\john.exe "--format=krb5tgs" "ticket.txt" "--wordlist=”rockyou.txt" "--progress-every=3"```
report progress every 3 seconds


>https://linuxconfig.org/password-cracking-with-john-the-ripper-on-linux

# Installing John

Although, at least on the distributions we tried, the package in named simply "john" with Gentoo making an exception and naming it "johntheripper", we will make it easy for you and show you how to install it on several known distributions.

## Debian

Debian differs from other distributions that offer John in their repositories because it offers a nice manual page, although upstream doesn't have one. To install, simply type

 # aptitude install john 

* * *

_**SUBSCRIBE TO NEWSLETTER**  
Subscribe to Linux Career [NEWSLETTER](https://bit.ly/2X5D30q) and receive latest Linux news, jobs, career advice and tutorials._

* * *

* * *

## Fedora

On Fedora, it's also as simple as doing

 # yum install john 

## Arch Linux

 # pacman -S john 

## OpenSuse Linux

\# zypper install john

## Gentoo

As we said, Gentoo's package is named differently from what others offer, so here you will have to run

 # emerge johntheripper

## Slackware

Although there doesn't seem to be a john package in the official repositories, there is a slackbuild that gets John installed on your system (this was tested on Slackware 13.37).

Although we gave you just a few examples on how you can get John on your Linux system, many of the examples presented will run if you have other OS installed: besides source code, the project offers the program for BeOS, Microsoft Windows, Solaris or MacOS X. But for our article, as the title says, we tested the examples on Linux.

# Using John the Ripper

You need not worry about cryptic configuration files, as John is ready to use with the appropriate command-line flags with no other effort on your part. One word of warning, though: as you already noticed, we tell our readers when they should use root privileges and when they shouldn't. Except when noted, you are strongly recommended to use your normal everyday user (or another, if you prefer, but it shouldn't have super user rights). On my Debian system, John is available as /usr/sbin/john, so if you don't find it we recommend you use whereis and type the whole path when running john unprivileged (or you can simply create an alias).

* * *

* * *

The simplest way to get your feet wet is to type

 $ /usr/sbin/john --test 

for doing some tests and benchmarks on John's capabilities. If you have no idea what Kerberos, MD5, DES or Blowfish are, we recommend you start reading some basic security books, because, like we said before, you need some security/administration background. Now, let's create a text file in password format (<user>:<hash>) with a valid hash, of course, and get John to work. You can simply copy a user from /etc/shadow, but we recommend something simpler, because we presume you want to see the results as fast as you can. So create a file named password.txt somewhere inside your /home and put this in it:

myuser:AZl.zWwxIh15Q

Save the file, then simply feed it to John with no arguments (for now):

 $ /usr/sbin/john password.txt 

We must repeat our warning: password cracking is a CPU-intensive and long process, so depending on your system, that might take quite a while. However, this also depends on what you want to achieve, because if your powerful CPU has been crunching at the password(s) for days with no outcome, it's only safe to say that it's a good password. But if the password is really critical, leave the system until John finishes its' work to make sure everything is alright. Like we said before, this could take many days.

Now, if you have a powerful box with the sole purpose of testing passwords, which is always a good thing given the means, you can try your real-life passwords with John. One way is to use /etc/shadow directly, but we recommend you take a somewhat different course. Note that this applies to systems using shadow passwords, and all the modern Linux distributions do. John offers a nifty utility called unshadow, which we will use to create a file from our passwd and shadow files:

 # unshadow /etc/passwd /etc/shadow > mypasswd.txt 

Now make sure that mypasswd.txt is available to your normal user and do

 $ /usr/sbin/john mypasswd.txt 

John will try single crack mode first, then wordlist mode, then incremental. In John's terms, a mode is a method it uses to crack passwords. As you know, there are many kinds of attacks: dictionary attacks, brute force attacks, and so on. Well, this is roughly what John's modes are. As some of you might have realized, wordlist mode is basically a dictionary attack. Besides these three modes enumerated above, John also supports another one called external mode. You can select what mode to use with, for example, --single, --external and so on. We recommend you check out the documentation over at openwall.com for a good but brief description of every mode. But of course we will tell you, in short, what every mode does.

John the Ripper's documentation recommends starting with single crack mode, mostly because it's faster and even faster if you use multiple password files at a time. Incremental mode is the most powerful mode available, as it will try various combinations when cracking, and you can choose what kind of mode (mode applied to the incremental option) to use, including your own. External mode, as the name implies, will use custom functions that you write yourself, while wordlist mode takes a word list specified as an argument to the option (it can be a file with a list of words written one per line, or stdin) and tries a simple dictionary attack on passwords.

If John is succesful in cracking one of the passwords, it will write to ~/.john/john.pot. However, that file isn't human-readable, so you can read cracked passwords with

 $ /usr/sbin/john --show mypasswd.txt

To check if the root password got cracked, filter by UID:

 $ /usr/sbin/john --show --users=0 mypasswd.txt

Of course, John knows about wildcards and multiple files:

 $ /usr/sbin/john --show --users=0 \*passwd\*

* * *

* * *

Just as you can filter by user, you can also filter by group, by using the --groups flag, and that filtering is available also when cracking. Going further to wordlist mode, here's how you can use it with the built-in mangling rules enabled:

 $ /usr/sbin/john --wordlist=passwd.lst --rules passwd.txt

John also allows you to create multiple named sessions, which is practical, because since John can take lots of time to complete a task, you can later view all sessions running to decide which one to kill. The option for named sessions is --session=taskname and you can use --status or --status=taskname to see all or certain sessions. But there's more: you can restore sessions or particular ones by name using --restore or --restore=taskname. A few examples:

 $ /usr/sbin/john --session=allrules --wordlist=all.lst --rules mypasswd.txt
 $ /usr/sbin/john --status=allrules
 $ ps aux | grep john #get the PID of the john session you want to kill
 $ kill HUP $PID\_of\_john\_session\_to\_kill
 $ /usr/sbin/john --restore=allrules

Here's some examples of using incremental mode with John:

 $ /usr/sbin/john --incremental mypasswd.txt
 $ /usr/sbin/john --incremental=alpha mypasswd.txt

Of course, this isn't a replacement of John's documentation. Although, as we said, it doesn't offer a manual page, you will find lots of documentation on its' page, as well as a useful wiki. For example, you will notice that even if you're running John on a multiprocessor machine, it will use only one core, usually the first. You can address this problem by reading the documentation and following the instructions there.



#enable CUDA acc

>https://blog.sleeplessbeastie.eu/2015/11/02/how-to-crack-password-using-nvidia-gpu/


While this is not my primary area of expertise, I have been using [John the Ripper](https://www.openwall.com/john/) more frequently lately, so I began to wonder how to take advantage of the powerful Nvidia GPU...

### Prerequisites

I have used the following software during this brief tutorial.

-   Ubuntu 15.04 _Vivid Vervet_
-   John the Ripper _1.8.0-jumbo-1_ source code

I have tested it on _Lenovo Z50-70_ notebook equipped with _Intel i7-4510U @ 2.00GHz_ and _Nvidia GeForce 840M_.

You can use _live USB image_ with _persistent storage_ to create system specialized for this purpose.

### Installation

Enable _multiverse_ repository as it will contain packages required to use _OpenCL_ and _CUDA_.

$ sudo apt-add-repository multiverse

Update the package index files.

$ sudo apt-get update

Install _Nvidia driver_ and _CUDA toolkit_.

$ sudo apt-get install nvidia-340 nvidia-cuda-toolkit

Reading package lists...
Building dependency tree...
Reading state information...
The following extra packages will be installed:
  bbswitch-dkms ca-certificates-java default-jre default-jre-headless dkms
  fonts-dejavu-extra java-common lib32gcc1 libatk-wrapper-java
  libatk-wrapper-java-jni libbonobo2-0 libbonobo2-common libc6-i386
  libcublas6.5 libcuda1-340 libcudart6.5 libcufft6.5 libcufftw6.5
  libcuinj64-6.5 libcurand6.5 libcusparse6.5 libdrm-dev libgconf2-4 libgif4
  libgl1-mesa-dev libgl1-mesa-glx libglapi-mesa libgnome2-0 libgnome2-bin
  libgnome2-common libgnomevfs2-0 libgnomevfs2-common libjansson4 libnppc6.5
  libnppi6.5 libnpps6.5 libnvblas6.5 libnvtoolsext1 libnvvm2 liborbit-2-0
  libpthread-stubs0-dev libthrust-dev libvdpau-dev libvdpau1 libx11-dev
  libx11-doc libx11-xcb-dev libxau-dev libxcb-dri2-0-dev libxcb-dri3-dev
  libxcb-glx0-dev libxcb-present-dev libxcb-randr0-dev libxcb-render0-dev
  libxcb-shape0-dev libxcb-sync-dev libxcb-xfixes0-dev libxcb1-dev
  libxdamage-dev libxdmcp-dev libxext-dev libxfixes-dev libxnvctrl0
  libxshmfence-dev libxxf86vm-dev mesa-common-dev nvidia-cuda-dev
  nvidia-cuda-doc nvidia-cuda-gdb nvidia-opencl-dev nvidia-opencl-icd-340
  nvidia-prime nvidia-profiler nvidia-settings nvidia-visual-profiler
  ocl-icd-libopencl1 opencl-headers openjdk-7-jre openjdk-7-jre-headless
  screen-resolution-extra tzdata tzdata-java x11proto-core-dev
  x11proto-damage-dev x11proto-dri2-dev x11proto-fixes-dev x11proto-gl-dev
  x11proto-input-dev x11proto-kb-dev x11proto-xext-dev
  x11proto-xf86vidmode-dev xorg-sgml-doctools xtrans-dev
Suggested packages:
  bumblebee equivs libbonobo2-bin desktop-base libgnomevfs2-bin
  libgnomevfs2-extra gamin fam gnome-mime-data libvdpau-doc
  nvidia-vdpau-driver vdpau-driver libxcb-doc libxext-doc libcupti-dev
  icedtea-7-plugin icedtea-7-jre-jamvm sun-java6-fonts fonts-ipafont-gothic
  fonts-ipafont-mincho ttf-wqy-microhei ttf-wqy-zenhei fonts-indic
Recommended packages:
  libcuda1 libcuda-6.5-1 libnvcuvid1 nvidia-opencl-icd
The following NEW packages will be installed:
  bbswitch-dkms ca-certificates-java default-jre default-jre-headless dkms
  fonts-dejavu-extra java-common lib32gcc1 libatk-wrapper-java
  libatk-wrapper-java-jni libbonobo2-0 libbonobo2-common libc6-i386
  libcublas6.5 libcuda1-340 libcudart6.5 libcufft6.5 libcufftw6.5
  libcuinj64-6.5 libcurand6.5 libcusparse6.5 libdrm-dev libgconf2-4 libgif4
  libgl1-mesa-dev libgnome2-0 libgnome2-bin libgnome2-common libgnomevfs2-0
  libgnomevfs2-common libjansson4 libnppc6.5 libnppi6.5 libnpps6.5
  libnvblas6.5 libnvtoolsext1 libnvvm2 liborbit-2-0 libpthread-stubs0-dev
  libthrust-dev libvdpau-dev libvdpau1 libx11-dev libx11-doc libx11-xcb-dev
  libxau-dev libxcb-dri2-0-dev libxcb-dri3-dev libxcb-glx0-dev
  libxcb-present-dev libxcb-randr0-dev libxcb-render0-dev libxcb-shape0-dev
  libxcb-sync-dev libxcb-xfixes0-dev libxcb1-dev libxdamage-dev libxdmcp-dev
  libxext-dev libxfixes-dev libxnvctrl0 libxshmfence-dev libxxf86vm-dev
  mesa-common-dev nvidia-340 nvidia-cuda-dev nvidia-cuda-doc nvidia-cuda-gdb
  nvidia-cuda-toolkit nvidia-opencl-dev nvidia-opencl-icd-340 nvidia-prime
  nvidia-profiler nvidia-settings nvidia-visual-profiler ocl-icd-libopencl1
  opencl-headers openjdk-7-jre openjdk-7-jre-headless screen-resolution-extra
  tzdata-java x11proto-core-dev x11proto-damage-dev x11proto-dri2-dev
  x11proto-fixes-dev x11proto-gl-dev x11proto-input-dev x11proto-kb-dev
  x11proto-xext-dev x11proto-xf86vidmode-dev xorg-sgml-doctools xtrans-dev
The following packages will be upgraded:
  libgl1-mesa-glx libglapi-mesa tzdata
3 upgraded, 92 newly installed, 0 to remove and 243 not upgraded.
Need to get 606 MB/606 MB of archives.
After this operation, 1,395 MB of additional disk space will be used.
\[...\]

Reboot system to use installed driver.

$ sudo reboot

Verify installed driver.

$ nvidia-smi

Sat Sep  5 21:30:30 2015       
+------------------------------------------------------+                       
| NVIDIA-SMI 340.76     Driver Version: 340.76         |                       
|-------------------------------+----------------------+----------------------+
| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |
| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |
|===============================+======================+======================|
|   0  GeForce 840M        Off  | 0000:03:00.0     N/A |                  N/A |
| N/A   44C    P8    N/A /  N/A |    292MiB /  4095MiB |     N/A      Default |
+-------------------------------+----------------------+----------------------+
                                                                               
+-----------------------------------------------------------------------------+
| Compute processes:                                               GPU Memory |
|  GPU       PID  Process name                                     Usage      |
|=============================================================================|
|    0            Not Supported                                               |
+-----------------------------------------------------------------------------+

Install packages required to build an application from source code.

$ sudo apt-get install build-essential libssl-dev

Reading package lists...
Building dependency tree...
Reading state information...
build-essential is already the newest version.
The following extra packages will be installed:
  libssl-doc libssl1.0.0 zlib1g-dev
The following NEW packages will be installed:
  libssl-dev libssl-doc zlib1g-dev
The following packages will be upgraded:
  libssl1.0.0
1 upgraded, 3 newly installed, 0 to remove and 242 not upgraded.
Need to get 3,074 kB of archives.
After this operation, 8,417 kB of additional disk space will be used.
\[...\]

Download community-enhanced version.

$ wget http://www.openwall.com/john/j/john-1.8.0-jumbo-1.tar.gz

Extract downloaded archive.

$ tar xfz john-1.8.0-jumbo-1.tar.gz

Change working directory.

$ cd john-1.8.0-jumbo-1/src/

Execute configuration script.

$ ./configure

Verify _OpenCL_ and _CUDA_ support.

\[...\]

Configured for building John the Ripper 1.8.0-jumbo-1:

Target CPU .................................. x86\_64 AVX2, 64-bit LE
AES-NI support .............................. depends on OpenSSL
Target OS ................................... linux-gnu
Cross compiling ............................. no
Legacy arch header .......................... x86-64.h
OpenMPI support (default disabled) .......... no
Fork support ................................ yes
OpenMP support .............................. yes
OpenCL support .............................. yes
CUDA support ................................ yes
Generic crypt(3) format ..................... yes

Optional libraries found:
Rexgen (extra cracking mode) ................ no
GMP (performance for SRP formats) ........... no
PCAP (vncpcap2john and SIPdump) ............. no
BZ2 (gpg2john extra decompression logic) .... yes

Development options (these may hurt performance when enabled):
Memdbg memory debugging settings ............ disabled
AddressSanitizer ("ASAN") ................... disabled

Install missing libraries to get any needed features that were omitted.

Configure finished.  Now 'make clean && make -s' to compile.

Compile source code.

$ make -s -j 4

Compiled software is available in the _run_ directory.

$ cd ../run

You can execute commands directly from that directory.

### Usage

List _OpenCL_ devices.

$ sudo ./john --list=opencl-devices

Platform #0 name: NVIDIA CUDA
Platform version: OpenCL 1.1 CUDA 6.5.45
	Device #0 (0) name:	GeForce 840M
	Device vendor:		NVIDIA Corporation
	Device type:		GPU (LE)
	Device version:		OpenCL 1.1 CUDA
	Driver version:		340.76
	Native vector widths:	char 1, short 1, int 1, long 1
	Preferred vector width:	char 1, short 1, int 1, long 1
	Global Memory:		3.0 GB
	Global Memory Cache:	48.0 KB
	Local Memory:		47.0 KB (Local)
	Max memory alloc. size:	1023.10 MB
	Max clock (MHz):	1124
	Profiling timer res.:	1000 ns
	Max Work Group Size:	1024
	Parallel compute cores:	3
	Stream processors:	384  (3 x 128)
	Warp size:		32
	Max. GPRs/work-group:	65536
	Kernel exec. timeout:	yes
	PCI device topology:	03:00.0
	NVML id:		0
	Temperature:		45°C
	Utilization:		n/a

List _OpenCL_ devices.

$ sudo ./john --list=cuda-devices

1 CUDA device found:

CUDA Device #0
	Name:                          GeForce 840M
	Type:                          discrete
	Compute capability:            5.0 (sm\_50)
	Number of stream processors:   384 (3 x 128)
	Clock rate:                    1124 Mhz
	Memory clock rate (peak)       900 Mhz
	Memory bus width               64 bits
	Peak memory bandwidth:         14 GB/s
	Total global memory:           3.0 GB
	Total shared memory per block: 48.0 KB
	Total constant memory:         64.0 KB
	L2 cache size                  1024.0 KB
	Kernel execution timeout:      Yes
	Concurrent copy and execution: One direction
	Concurrent kernels support:    Yes
	Warp size:                     32
	Max. GPRs/thread block         65536
	Max. threads per block         1024
	Max. resident threads per MP   2048
	PCI device topology:           03:00.0
	NVML id:                       0
	Fan speed:                     n/a
	GPU temp:                      45°C
	Utilization:                   n/a

$ sudo ./john --format=sha512crypt-opencl shadow

List _OpenCL_ supported formats.

$ sudo ./john --list=formats --format=opencl

sha1crypt-opencl, oldoffice-opencl, PBKDF2-HMAC-SHA1-opencl, rar-opencl, 
RAR5-opencl, lotus5-opencl, agilekeychain-opencl, bcrypt-opencl, 
blockchain-opencl, md5crypt-opencl, sha256crypt-opencl, sha512crypt-opencl, 
descrypt-opencl, dmg-opencl, encfs-opencl, gpg-opencl, keychain-opencl, 
keyring-opencl, krb5pa-md5-opencl, krb5pa-sha1-opencl, mscash2-opencl, 
mysql-sha1-opencl, ssha-opencl, nt-opencl, ntlmv2-opencl, o5logon-opencl, 
ODF-opencl, ODF-AES-opencl, office2007-opencl, office2010-opencl, 
office2013-opencl, PBKDF2-HMAC-SHA256-opencl, pbkdf2-hmac-sha512-opencl, 
phpass-opencl, pwsafe-opencl, RAKP-opencl, Raw-MD4-opencl, Raw-MD5-opencl, 
Raw-SHA1-opencl, Raw-SHA256-opencl, Raw-SHA512-opencl, 7z-opencl, 
strip-opencl, sxc-opencl, wpapsk-opencl, XSHA512-opencl, zip-opencl

List _CUDA_ supported formats.

$ sudo ./john --list=formats --format=cuda

md5crypt-cuda, sha256crypt-cuda, sha512crypt-cuda, mscash-cuda, mscash2-cuda, 
phpass-cuda, pwsafe-cuda, Raw-SHA512-cuda, wpapsk-cuda, xsha512-cuda, 
Raw-SHA224-cuda, Raw-SHA256-cuda

Decrypt password using _OpenMP_.

$ sudo ./john ~/shadow

Warning: detected hash type "sha512crypt", but the string is also recognized as "sha512crypt-opencl"
Use the "--format=sha512crypt-opencl" option to force loading these as that type instead
Warning: detected hash type "sha512crypt", but the string is also recognized as "sha512crypt-cuda"
Use the "--format=sha512crypt-cuda" option to force loading these as that type instead
Warning: detected hash type "sha512crypt", but the string is also recognized as "crypt"
Use the "--format=crypt" option to force loading these as that type instead
Loaded 1 password hash (sha512crypt, crypt(3) $6$ \[SHA512 64/64 OpenSSL\])
Will run 4 OpenMP threads
Press 'q' or Ctrl-C to abort, almost any other key for status
0g 0:00:05:09  3/3 0g/s 742.9p/s 742.9c/s 742.9C/s buro15..bitaka
\[...\]	

Decrypt password using _OpenCL_.

$ sudo ./john --format=sha512crypt-opencl ~/shadow

Device 0: GeForce 840M
Local worksize (LWS) 32, global worksize (GWS) 2304
Loaded 1 password hash (sha512crypt-opencl, crypt(3) $6$ \[SHA512 OpenCL\])
Press 'q' or Ctrl-C to abort, almost any other key for status
0g 0:00:08:44  3/3 0g/s 5005p/s 5005c/s 5005C/s GPU:67°C ljm234..devvey
\[...\]	

Decrypt password using _CUDA_.

$ sudo ./john --format=sha512crypt-cuda ~/shadow

Loaded 1 password hash (sha512crypt-cuda, crypt(3) $6$ \[SHA512 CUDA (inefficient, please use sha512crypt-opencl instead)\])
Press 'q' or Ctrl-C to abort, almost any other key for status
0g 0:00:05:37 73.47% 2/3 (ETA: 22:43:44) 0g/s 348.0p/s 348.0c/s 348.0C/s GPU:59°C Morris...Macross.
\[...\]	

_SHA512 CUDA_ is inefficient currently, but do not treat it as a rule.

### Additional notes

You can verify installed driver using GUI application.

$ nvidia-settings

[![](/assets/uploads/2015/11/nvidia-settings.png)](/assets/uploads/2015/11/nvidia-settings.png)

* * *

Install `nvidia-opencl-dev` package instead of `nvidia-cuda-toolkit` if you plan to use only _OpenCL_. It has minimal dependencies compared to the whole _CUDA toolkit_.

$ sudo apt-get install nvidia-340 nvidia-opencl-dev

Reading package lists...
Building dependency tree...
Reading state information...
The following extra packages will be installed:
  bbswitch-dkms dkms lib32gcc1 libc6-i386 libcuda1-340 libdrm-dev
  libgl1-mesa-dev libgl1-mesa-glx libglapi-mesa libjansson4
  libpthread-stubs0-dev libvdpau1 libx11-dev libx11-doc libx11-xcb-dev
  libxau-dev libxcb-dri2-0-dev libxcb-dri3-dev libxcb-glx0-dev
  libxcb-present-dev libxcb-randr0-dev libxcb-render0-dev libxcb-shape0-dev
  libxcb-sync-dev libxcb-xfixes0-dev libxcb1-dev libxdamage-dev libxdmcp-dev
  libxext-dev libxfixes-dev libxnvctrl0 libxshmfence-dev libxxf86vm-dev
  mesa-common-dev nvidia-opencl-icd-340 nvidia-prime nvidia-settings
  ocl-icd-libopencl1 opencl-headers screen-resolution-extra x11proto-core-dev
  x11proto-damage-dev x11proto-dri2-dev x11proto-fixes-dev x11proto-gl-dev
  x11proto-input-dev x11proto-kb-dev x11proto-xext-dev
  x11proto-xf86vidmode-dev xorg-sgml-doctools xtrans-dev
Suggested packages:
  bumblebee nvidia-vdpau-driver vdpau-driver libxcb-doc libxext-doc
Recommended packages:
  nvidia-opencl-icd
The following NEW packages will be installed:
  bbswitch-dkms dkms lib32gcc1 libc6-i386 libcuda1-340 libdrm-dev
  libgl1-mesa-dev libjansson4 libpthread-stubs0-dev libvdpau1 libx11-dev
  libx11-doc libx11-xcb-dev libxau-dev libxcb-dri2-0-dev libxcb-dri3-dev
  libxcb-glx0-dev libxcb-present-dev libxcb-randr0-dev libxcb-render0-dev
  libxcb-shape0-dev libxcb-sync-dev libxcb-xfixes0-dev libxcb1-dev
  libxdamage-dev libxdmcp-dev libxext-dev libxfixes-dev libxnvctrl0
  libxshmfence-dev libxxf86vm-dev mesa-common-dev nvidia-340 nvidia-opencl-dev
  nvidia-opencl-icd-340 nvidia-prime nvidia-settings ocl-icd-libopencl1
  opencl-headers screen-resolution-extra x11proto-core-dev x11proto-damage-dev
  x11proto-dri2-dev x11proto-fixes-dev x11proto-gl-dev x11proto-input-dev
  x11proto-kb-dev x11proto-xext-dev x11proto-xf86vidmode-dev
  xorg-sgml-doctools xtrans-dev
The following packages will be upgraded:
  libgl1-mesa-glx libglapi-mesa
2 upgraded, 51 newly installed, 0 to remove and 244 not upgraded.
Need to get 77.1 MB/77.1 MB of archives.
After this operation, 365 MB of additional disk space will be used.
\[...\]

There is slightly more to it than that, but it is a good start.

























#JWT

john has been supporting JWT natively as of this commit https://github.com/magnumripper/JohnTheRipper/commit/85aa7b3e3f8204360683ddc5ec9734bf793d07cf (2015).

So you can just put a JWT hash in the pw file without any changes nowadays, without having to do any transformations.
```
$ cat jwt.john 
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiYXNkZiJ9.GwJ7_ZrnpRLXXSBYzB9VkM4n7j2iSJEkdjhckeaXQ-U
$ john jwt.john
# Wait a few hours, then:
$ john --show jwt.john
```