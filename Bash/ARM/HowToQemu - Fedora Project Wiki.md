[![Language.png](https://fedoraproject.org/w/uploads/3/39/Language.png)](https://fedoraproject.org/wiki/Fedora_Project_Wiki:Translating "Fedora Project Wiki:Translating")

## [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Introduction) Introduction

[QEMU](http://fabrice.bellard.free.fr/qemu/) is a well-known emulator that supports ARM platforms, and can be used to run the Fedora-ARM distribution. This provides a convenient platform to try out the distribution as well as for development and customization.

The howto describes a process to get the Fedora-ARM distribution running under QEMU. Although we have tested this on Fedora 12, most of the process should work on any other Linux system as well. We assumes that you can run commands as root (or using sudo) whenever necessary.

The QEMU system is set up to get its root file system from a local loopback block device or over NFS from the host system (requires networking between the host system and the QEMU guest). The host's networking can then be configured to get its IP address using DHCP.

## [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Using_QEMU_with_libvirt) Using QEMU with libvirt

[libvirt](http://libvirt.org/) is a virtualization management framework and toolkit. At the tool level, it provides the `virsh` virtualization shell as well as the `virt-manager` GUI tool for command-line VM management (plus additional tools).

By using libvirt to manage ARM VMs, you can leverage its capabilities (such as domain autostart, network setup with NAT and DHCP, and console disconnect/reconnect), and manage your ARM and x86 VMs in a consistent manner.

Here is a quick-start guide to setting up ARM QEMU emulation under libvirt management:

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Installing_and_starting_the_virtualization_software) Installing and starting the virtualization software

These steps install libvirt and related tools, if not installed already, plus the ARM emulator, and then start the libvirt daemon:

yum groupinstall virtualization
yum install qemu-system-arm
service libvirtd start

[![Important.png](https://fedoraproject.org/w/uploads/thumb/f/ff/Important.png/35px-Important.png)](https://fedoraproject.org/wiki/File:Important.png)

**Fedora 13/14**  
libvirt 0.7.6 starts qemu-system-arm 0.12.3 with the wrong arguments. [This script](http://cdot.senecac.on.ca/arm/FEDORA13-WRAPPER-qemu-system-arm) provides a temporary workaround ([alternate version](http://scotland.proximity.on.ca/arm/qemu-system-arm-wrapper)).

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Installing_the_ARM_root_filesystem_and_XML) Installing the ARM root filesystem and XML

These steps download a 4GB pre-built ext3 root image, ARM kernel, and libvirt XML domain definition, then define a VM to use them:

cd /var/lib/libvirt/images
wget [http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/zImage-versatile-2.6.24-rc7.armv5tel](http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/zImage-versatile-2.6.24-rc7.armv5tel) \\
     [http://cdot.senecac.on.ca/arm/arm1.xml](http://cdot.senecac.on.ca/arm/arm1.xml) \\
     [http://cdot.senecac.on.ca/arm/arm1.img.gz](http://cdot.senecac.on.ca/arm/arm1.img.gz)
gunzip arm1.img.gz
restorecon \*
virsh define arm1.xml

Note that the virtual machine definition cannot be performed using virt-manager at this time (F12 - F20) -- use virsh as shown above.

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Booting_the_VM) Booting the VM

[![Idea.png](https://fedoraproject.org/w/uploads/thumb/a/a4/Idea.png/35px-Idea.png)](https://fedoraproject.org/wiki/File:Idea.png)

**SELinux**  
QEMU ARM emulation uses memory in a way SELinux considers more dangerous than usually needed. To loosen the SELinux restrictions on QEMU's memory usage run: `setsebool -P virt_use_execmem 1`

You can now boot and access the VM using the virt-manager tool (Applications>System Tools>Virtual Machine Manager on the menu), or you can control it from the command line:

virsh start arm1

To view the graphical display without using virt-manager, use the virt-viewer command:

virt-viewer arm1

Or, alternately, use `virsh vncdisplay arm1` and then use the vncviewer program (from the _tigervnc_ package) to view the VM console.

[![Note.png](https://fedoraproject.org/w/uploads/thumb/c/cc/Note.png/35px-Note.png)](https://fedoraproject.org/wiki/File:Note.png)

**Root Password**  
The initial root password for the ARM VM image is `fedoraarm`.

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Using_networking_in_the_VM) Using networking in the VM

You can get a DHCP address for the VM using `dhclient eth1`, or set up a static IP configuration. Once you have IP configured, you can:

-   Use ssh instead of the console to access a shell on the VM (faster, and more flexible)
-   Use `yum` to install and remove software

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Creating_additional_ARM_VMs) Creating additional ARM VMs

For each additional ARM VM you wish to create:

-   Make a new copy of the `arm1.img` file under a different name in `/var/lib/libvirt/images`
-   Edit the XML, making the following changes:
    1.  Change the UUID (you can use `uuidgen` to generate a new one)
    2.  Change the image filename (in the `source` tag in the `devices` section) to point to the new image file you just created.
-   Use `virsh define _nameOfXMLFile_` to define the new VM from the modified XML file.

## [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Using_QEMU_without_libvirt) Using QEMU without libvirt

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Install_QEMU) Install QEMU

If you are running Fedora 7/8, you can just install qemu using yum.

yum install qemu

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Setup_Networking) Setup Networking

You can skip this section if you are going to use a local loopback device for your root file system. However that may prevent you from using yum to install new packages on your Fedora-ARM guest.

Networking is setup between the host system and the QEMU guest to allow the guest to get its IP address using DHCP.

The networking setup uses host TAP devices to connect to QEMU. In recent kernels, this requires CAP\_NET\_ADMIN capability. The host system needs to have TUN/TAP networking enabled (CONFIG\_TUN=m or CONFIG\_TUN=y). You can verify this using:

grep CONFIG\_TUN= /boot/config-\`uname -r\`

Also make sure that /dev/net/tun exists. If not, you can make it as follows:

mknod /dev/net/tun c 10 200

Now, we need to set up a network bridge interface. Install some utilities to configure a ethernet bridge:

\# yum install bridge-utils

/usr/sbin/brctl addbr br0
/sbin/ifconfig eth0 0.0.0.0 promisc up
/usr/sbin/brctl addif br0 eth0
/sbin/dhclient br0
/sbin/iptables -F FORWARD

Also, create a script qemu-ifup as follows. This will be needed when we boot into QEMU.

#!/bin/sh
/sbin/ifconfig $1 0.0.0.0 promisc up
/usr/sbin/brctl addif br0 $1

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Setup_Kernel_Image) Setup Kernel Image

You can either simply use a pre-built kernel image or build your own from source.

#### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Pre-built_Kernel_Image) Pre-built Kernel Image

You can get one of the following pre-built kernel images for ARM:

\- [zImage-qemu-versatile-3.0.8-4.fc17.armv5tel](http://jcapik.fedorapeople.org/files/ARM/Qemu/zImage-qemu-versatile-3.0.8-4.fc17.armv5tel) (right click -> Save as ...)

\- [zImage-versatile-2.6.24-rc7.armv5tel](http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/zImage-versatile-2.6.24-rc7.armv5tel)

\- [zImage-versatile-2.6.23-rc4](http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/zImage-versatile-2.6.23-rc4)

\- [zImage-versatile-2.6.22](http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/zImage-versatile-2.6.22)

  
The README file has documentation as to how to build these particular zImage-versatile: [\[1\]](http://ftp.linux.org.uk/pub/linux/arm/fedora/qemu/00README.txt)

#### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Build_Kernel_Image_From_Source) Build Kernel Image From Source

You will need to have an ARM cross-compiler. If you do not have one, download one from CodeSourcery's web-site, install it and ensure that is it in your path.

export ARCH=arm
export CROSS\_COMPILE=arm-none-linux-gnueabi-

You can also use the Fedora [cross toolchain](https://fedoraproject.org/w/index.php?title=Architectures/ARM/CrossToolchain&action=edit&redlink=1 "Architectures/ARM/CrossToolchain (page does not exist)") that we provide.

Download Linux kernel (I have tested it with 2.6.21 and 2.6.22) and build it for ARM Versatile board. But, first you will have to customize the defconfig for it to work correctly.

cp arch/arm/configs/versatile\_defconfig .config
make menuconfig

Enable DHCP Support (CONFIG\_IP\_PNP\_DHCP). It is under Networking -> Networking Support -> Networking Options ->
TCP/IP Networking -> IP: Kernel Level autoconfiguration.

Enable Universal Tun/Tap Driver Support (CONFIG\_TUN). It is under Device Drivers -> Network Device Support ->
Network Device Support.

Enable ARM EABI Support (CONFIG\_AEABI). It is under Kernel Features.

Enable tmpfs support (CONFIG\_TMPFS). It is under File Systems -> Pseudo File Systems.

If you will be booting from a file system image (not NFS), then the following steps should also be taken:

Enable PCI support (CONFIG\_PCI).  It is under Bus Support.

Enable SCSI Device Support.  It is under Device Drivers -> SCSI Device Support.

Enable SCSI Disk Support.  It is under Device Drivers -> SCSI Device Support.

Enable SYM53C8XX Version 2 SCSI Support.  It is under Device Drivers -> SCSI Device Support -> SCSI low-level drivers

Optionally you may enable: 
Device Drivers --> Serial ATA and Parallel ATA drivers  ---> Marvell SATA support
(I believe this supports the Marvell SATA drivers found on Marvell processor based Plug computers Sheeva, OpenRD, Guruplug, etc.) 

Build the kernel:

make all

make zImage
(the kernel image will be located in arch/arm/boot named zImage )

make modules\_install INSTALL\_MOD\_PATH=$TARGETDIR 
($TARGETDIR  needs to be an alternate directory.  These are the kernel modules you copy to /lib/modules/kernel\_version\_number on your rootfs.) 

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Setup_Root_File_System) Setup Root File System

Download the [latest root file system tarball](http://ftp.linux.org.uk/pub/linux/arm/fedora/rootfs/).

#### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Root_File_System_On_Loopback_Device) Root File System On Loopback Device

Create a loopback device -- 4GB is a reasonable size.

dd if=/dev/zero of=rootfs-f10-dev bs=1024k count=4096

Create a file system.

mkfs.ext3 rootfs-f10-dev -L arm

or for newer rootfs version, e.g. F17:

mkfs.ext3 rootfs-f10-dev -L rootfs

The label or UUID must be the same as the LABEL= or UUID= for / in /etc/fstab inside the root file system, otherwise the read-write remount of / will fail during boot.

Prepare the root file-system. This assumes that the loopback device is mounted under /mnt/ARM\_FS.

mount rootfs-f10-dev /mnt/ARM\_FS -o loop
tar -xjf rootfs-f10.tar.bz2 -C /mnt/ARM\_FS
mv /mnt/ARM\_FS/rootfs-f10/\* /mnt/ARM\_FS
rm -rf /mnt/ARM\_FS/rootfs-f10

copy your kernel modules from $TARGETDIR from the kernel build to 
/lib/modules/kernel\_number ie /lib/modules/2.6.33.8

umount rootfs-f10-dev

#### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Root_File_System_Over_NFS) Root File System Over NFS

Download the latest root filesystem tarball from [http://ftp.linux.org.uk/pub/linux/arm/fedora/rootfs/](http://ftp.linux.org.uk/pub/linux/arm/fedora/rootfs/) and untar it.

This assumes that you untarred the root file system in /mnt/ARM\_FS. We need to export it through NFS. Add the following in your /etc/exports.

/mnt/ARM\_FS/ \*(rw,sync,no\_root\_squash)

Now, restart the NFS service.

/sbin/service nfs restart

### [🔗](https://fedoraproject.org/wiki/Architectures/ARM/HowToQemu#Boot_into_QEMU) Boot into QEMU

Now you are ready to boot into QEMU. Replace <host-ip> with the IP address of the host machine.

qemu-system-arm -M versatilepb -kernel zImage-versatile -append root="/dev/nfs nfsroot=<host-ip>:/mnt/ARM\_FS rw ip=dhcp" \\
-net nic,vlan=0 -net tap,vlan=0,ifname=tap0,script=./qemu-ifup

If you're using the raw image instead of NFS, try this instead:

qemu-system-arm -M versatilepb -kernel zImage-versatile -hdc rootfs-f10-dev -append root="0800" \\
-net nic,vlan=0 -net tap,vlan=0,ifname=tap0,script=./qemu-ifup