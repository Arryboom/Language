I have Debian Linux VM running on KVM. I think I forgotten the password for the root account and I am no longer able to run ‘su -‘ command. How do I reset the password for the root account for KVM VM which is in qcow2 format?  
  
You can modify images with guestfish. It is a shell and command-line tool for examining and editing virtual machine filesystems. It uses libguestfs and exposes all of the functionality of the guestfs API. This page shows how to use guestfish to change the root account password.

## How to install guestfish

If you are using CentOS/RHEL use [yum command](https://www.cyberciti.biz/faq/rhel-centos-fedora-linux-yum-command-howto/ "See Linux/Unix yum command examples for more info"):  
`$ sudo yum install libguestfs-tools`  
Fedora Linux user run dnf command:  
`$ sudo dnf install libguestfs-tools`  
Debian/Ubuntu Linux user run [apt command](https://www.cyberciti.biz/faq/ubuntu-lts-debian-linux-apt-command-examples/ "See Linux/Unix apt command examples for more info")/[apt-get command](https://www.cyberciti.biz/tips/linux-debian-package-management-cheat-sheet.html "See Linux/Unix apt-get command examples for more info"):  
`$ sudo apt install libguestfs-tools`

## Step 1 – Shutdown guest VM

Run the following virsh command:  
`# virsh list`  
Sample outputs:

 Id    Name                           State
----------------------------------------------------
 2     debian9-vm1                    running

To shutdown the VM named debian9-vm1:  
`# virsh shutdown 2`  
OR  
`# virsh shutdown debian9-vm1`  
Sample outputs:

Domain debian9-vm1 is being shutdown

## Step 2 – Find location of KVM VM image

Type the following command to get location of qcow2 image:  
`# virsh dumpxml debian9-vm1 | grep 'source file'`  
Sample outputs:

<source file='/var/lib/libvirt/images/debian9-vm1.qcow2'/>

## Step 3 – Reset/change the root password using guestfish

First generate new root user account password by typing the following command:  
`# openssl passwd -1 mySecretRootAccountPasswordHere  
$1$M1bf5Y3T$p2CYEz8vlUD2R.fXydTLt.`  
Please copy $1$M1bf5Y3T$p2CYEz8vlUD2R.fXydTLt. password. You need to use this one in next few steps.

#### How to reset forgotten root password for Linux KVM vm

Let us start the procedure by running the following guestfish command:  
`# guestfish --rw -a /var/lib/libvirt/images/debian9-vm1.qcow2`  
You will see a prompt as follows:

\><fs>

To launch the backend either type ‘run’ or ‘launch’ command:

\><fs> launch

To list partitions type:

\><fs> list-filesystems

Now mount whatever disk you found it. For example, I found /dev/sda1, so I run the following mount command:

  

Patreon supporters only guides 🤓

\><fs> mount /dev/sda1 /

Edit the [/etc/shadow file](https://www.cyberciti.biz/faq/understanding-etcshadow-file/) using a text editor such as vi command or emacs command:

\><fs> vi /etc/shadow

[![How to reset forgotten root password for Linux KVM qcow2 image vm](https://www.cyberciti.biz/media/new/faq/2018/02/How-to-reset-forgotten-root-password-for-Linux-KVM-qcow2-image-vm.jpg)](https://www.cyberciti.biz/media/new/faq/2018/02/How-to-reset-forgotten-root-password-for-Linux-KVM-qcow2-image-vm.jpg)  
Find root account entry and delete encrypted password. From:

root:$6$FU5Nl9oxxxx:17572:0:99999:7:::

To (replace as follows from above openssl command):

root:$1$M1bf5Y3T$p2CYEz8vlUD2R.fXydTLt.:17572:0:99999:7:::

[Save and close the file in vi/vim](https://www.cyberciti.biz/faq/linux-unix-vim-save-and-quit-command/). Run sync command:

\><fs> flush

Finally, quit guestfish:

\><fs> quit

## Step 4 – Start VM

It is time to test your new root password. So start the VM:  
`# virsh start debian9-vm1`  
Sample outputs:

Domain debian9-vm1 started

You can now login using console or ssh:  
`# ssh vivek@debian9-vm1  
$ su -`  
OR use console command:  
`# virsh list  
# virsh console debian9-vm1`  
[![Login as root user using ssh or vish console command](https://www.cyberciti.biz/media/new/faq/2018/02/Login-as-root-user-using-ssh-or-vish-console-command.jpg)](https://www.cyberciti.biz/media/new/faq/2018/02/Login-as-root-user-using-ssh-or-vish-console-command.jpg)

## A note about virt-customize command

If you find above method difficult try the following simple command:  
`# virsh shutdown debian9-vm1  
# virt-customize -a /var/lib/libvirt/images/debian9-vm1.qcow2 --root-password password:NewRootUserPasswordHere --uninstall cloud-init`  
Sample outputs:  
[![virt-customize reset password](https://www.cyberciti.biz/media/new/faq/2018/02/virt-customize-reset-password.jpg)](https://www.cyberciti.biz/media/new/faq/2018/02/virt-customize-reset-password.jpg)  
Start the VM and test new root password with ssh/console command:  
`# virsh start debian9-vm1`  
For more info read man page of [guestfish](http://libguestfs.org/guestfish.1.html):  
`$ man guestfish  
$ man virt-customize`

## See also

-   [Linux reset forgotten root password](https://www.cyberciti.biz/faq/linux-reset-forgotten-root-password/)
-   [FreeBSD Reset or Recover Root Password](https://www.cyberciti.biz/tips/howto-freebsd-reset-recover-root-password.html)
-   [How to: OpenBSD reset root password](https://www.cyberciti.biz/tips/reset-forgotten-openbsd-root-password.html)
-   [MySQL Reset Root Password](https://www.cyberciti.biz/faq/mysql-reset-lost-root-password/)
-   [How To Recover Linux Grub Boot Loader Password](https://www.cyberciti.biz/tips/howto-recovering-grub-boot-loader-password.html)

  

ADVERTISEMENT  

  
  

Category

List of Unix and Linux commands

Download managers

[wget](https://www.cyberciti.biz/tips/linux-wget-your-ultimate-command-line-downloader.html "See Linux/Unix wget command examples for more info examples and syntax")

Documentation

[help](https://bash.cyberciti.biz/guide/Help_command "help command examples and syntax") • [mandb](https://bash.cyberciti.biz/guide/Mandb_command "mandb command examples and syntax") • [man](https://bash.cyberciti.biz/guide/Man_command "man command examples and syntax") • [pinfo](https://www.cyberciti.biz/open-source/command-line-hacks/linux-command-pinfo-for-colorful-info-pages/ "pinfo – Read Linux Info Documentation in Colors examples and syntax")

Disk space analyzers

[df](https://www.cyberciti.biz/faq/df-command-examples-in-linux-unix/ "df Linux/Unix examples and syntax") • [duf](https://www.cyberciti.biz/open-source/command-line-hacks/duf-disk-usage-free-utility-for-linux-bsd-macos-windows/ "duf – Disk Usage/Free Utility for Linux, BSD, macOS R_DES Windows examples and syntax") • [ncdu](https://www.cyberciti.biz/open-source/install-ncdu-on-linux-unix-ncurses-disk-usage/ "ncdu Linux/Unix examples and syntax") • [pydf](https://www.cyberciti.biz/tips/unix-linux-bsd-pydf-command-in-colours.html "pydf Unix/Linux examples and syntax")

File Management

[cat](https://www.cyberciti.biz/faq/linux-unix-appleosx-bsd-cat-command-examples/ "cat command examples and syntax") • [cp](https://www.cyberciti.biz/faq/copy-command/ "cp command examples and syntax") • [less](https://bash.cyberciti.biz/guide/Less_command "less command examples and syntax") • [mkdir](https://www.cyberciti.biz/faq/linux-make-directory-command/ "mkdir command examples and syntax") • [more](https://bash.cyberciti.biz/guide/More_command "more command examples and syntax") • [tree](https://www.cyberciti.biz/faq/linux-show-directory-structure-command-line/ "tree command examples and syntax")

Firewall

[Alpine Awall](https://www.cyberciti.biz/faq/how-to-set-up-a-firewall-with-awall-on-alpine-linux/ "Awall firewall on Alpine Linux examples and syntax") • [CentOS 8](https://www.cyberciti.biz/faq/how-to-set-up-a-firewall-using-firewalld-on-centos-8/ "firewalld on CentOS 8 examples and syntax") • [OpenSUSE](https://www.cyberciti.biz/faq/set-up-a-firewall-using-firewalld-on-opensuse-linux/ "firewalld on OpenSUSE Linux examples and syntax") • [RHEL 8](https://www.cyberciti.biz/faq/configure-set-up-a-firewall-using-firewalld-on-rhel-8/ "firewalld on RHEL (Red Hat Enterprise Linux) 8 examples and syntax") • [Ubuntu 16.04](https://www.cyberciti.biz/faq/howto-configure-setup-firewall-with-ufw-on-ubuntu-linux/ "ufw on Ubuntu 16.04 LTS examples and syntax") • [Ubuntu 18.04](https://www.cyberciti.biz/faq/how-to-setup-a-ufw-firewall-on-ubuntu-18-04-lts-server/ "ufw on Ubuntu 18.04 LTS examples and syntax") • [Ubuntu 20.04](https://www.cyberciti.biz/faq/how-to-configure-firewall-with-ufw-on-ubuntu-20-04-lts/ "ufw on Ubuntu 20.04 LTS examples and syntax")

Linux Desktop apps

[Skype](https://www.cyberciti.biz/faq/how-to-install-skype-application-on-linux/ "Install Skype on Linux examples and syntax") • [Spotify](https://www.cyberciti.biz/faq/how-to-install-spotify-application-on-linux/ "Install Spotify on Linux examples and syntax") • [VLC 3](https://www.cyberciti.biz/faq/how-to-install-vlc-3-application-vetinari-on-linux/ "Install VLC 3 on Linux examples and syntax")

Modern utilities

[bat](https://www.cyberciti.biz/open-source/bat-linux-command-a-cat-clone-with-written-in-rust/ "bat Linux command – A cat clone with written in Rust examples and syntax") • [exa](https://www.cyberciti.biz/open-source/command-line-hacks/exa-a-modern-replacement-for-ls-written-in-rust-for-linuxunix/ "exa a modern replacement for ls command in Rust examples and syntax")

Network Utilities

[NetHogs](https://www.cyberciti.biz/faq/linux-find-out-what-process-is-using-bandwidth/ "NetHogs - Monitor per process network bandwidth usage under Linux examples and syntax") • [dig](https://www.cyberciti.biz/faq/linux-unix-dig-command-examples-usage-syntax/ "dig command examples and syntax") • [host](https://www.cyberciti.biz/faq/linux-unix-host-command-examples-usage-syntax/ "host command examples and syntax") • [ip](https://www.cyberciti.biz/faq/linux-ip-command-examples-usage-syntax/ "ip command in Linux examples and syntax") • [nmap](https://www.cyberciti.biz/security/nmap-command-examples-tutorials/ "nmap command in Linux examples and syntax")

OpenVPN

[CentOS 7](https://www.cyberciti.biz/faq/centos-7-0-set-up-openvpn-server-in-5-minutes/ "CentOS 7 Set Up OpenVPN Server In 5 Minutes examples and syntax") • [CentOS 8](https://www.cyberciti.biz/faq/centos-8-set-up-openvpn-server-in-5-minutes/ "CentOS 8 OpenVPN server in 5 mintues examples and syntax") • [Debian 10](https://www.cyberciti.biz/faq/debian-10-set-up-openvpn-server-in-5-minutes/ "Debian 10 Set Up OpenVPN Server In 5 Minutes examples and syntax") • [Debian 8/9](https://www.cyberciti.biz/faq/install-configure-openvpn-server-on-debian-9-linux/ "OpenVPN server on Debian 9/8 examples and syntax") • [Ubuntu 18.04](https://www.cyberciti.biz/faq/ubuntu-18-04-lts-set-up-openvpn-server-in-5-minutes/ "Ubuntu 18.04 LTS Set Up OpenVPN Server In 5 Minutes examples and syntax") • [Ubuntu 20.04](https://www.cyberciti.biz/faq/ubuntu-20-04-lts-set-up-openvpn-server-in-5-minutes/ "Ubuntu 20.04 LTS OpenVPN server in 5 mintues examples and syntax")

Package Manager

[apk](https://www.cyberciti.biz/faq/10-alpine-linux-apk-command-examples/ "apk command in Apline Linux examples and syntax") • [apt](https://www.cyberciti.biz/faq/ubuntu-lts-debian-linux-apt-command-examples/ "apt command in Debian/Ubuntu Linux examples and syntax")

Processes Management

[bg](https://www.cyberciti.biz/faq/unix-linux-bg-command-examples-usage-syntax/ "bg command examples and syntax") • [chroot](https://www.cyberciti.biz/faq/unix-linux-chroot-command-examples-usage-syntax/ "chroot command examples and syntax") • [cron](https://www.cyberciti.biz/faq/how-do-i-add-jobs-to-cron-under-linux-or-unix-oses/ "cron and crontab command examples and syntax") • [disown](https://www.cyberciti.biz/faq/unix-linux-disown-command-examples-usage-syntax/ "disown command examples and syntax") • [fg](https://www.cyberciti.biz/faq/unix-linux-fg-command-examples-usage-syntax/ "fg command examples and syntax") • [glances](https://www.cyberciti.biz/faq/linux-install-glances-monitoring-tool/ "Keep An Eye On Your System With Glances Monitor examples and syntax") • [gtop](https://www.cyberciti.biz/howto/gtop-awesome-system-monitoring-dashboard-for-terminal/ "Awesome system monitoring dashboard for Linux/macOS Unix terminal examples and syntax") • [iotop](https://www.cyberciti.biz/hardware/linux-iotop-simple-top-like-io-monitor/ "Linux iotop command examples and syntax") • [jobs](https://www.cyberciti.biz/faq/unix-linux-jobs-command-examples-usage-syntax/ "jobs command examples and syntax") • [killall](https://www.cyberciti.biz/faq/unix-linux-killall-command-examples-usage-syntax/ "killall command examples and syntax") • [kill](https://www.cyberciti.biz/faq/unix-kill-command-examples/ "kill command examples and syntax") • [pidof](https://www.cyberciti.biz/faq/linux-pidof-command-examples-find-pid-of-program/ "pidof command examples and syntax") • [pstree](https://www.cyberciti.biz/faq/unix-linux-pstree-command-examples-shows-running-processestree/ "pstree command examples and syntax") • [pwdx](https://www.cyberciti.biz/faq/unix-linux-pwdx-command-examples-usage-syntax/ "pwdx command examples and syntax") • [time](https://www.cyberciti.biz/faq/unix-linux-time-command-examples-usage-syntax/ "time command examples and syntax") • [vtop](https://www.cyberciti.biz/faq/how-to-install-and-use-vtop-graphical-terminal-activity-monitor-on-linux/ "vtop command examples and syntax")

Searching

[ag](https://www.cyberciti.biz/open-source/command-line-hacks/ag-supercharge-string-search-through-directory-hierarchy/ "ag command can recursively search for PATTERN in PATH. Like grep or ack, but faster examples and syntax") • [grep](https://www.cyberciti.biz/faq/howto-use-grep-command-in-linux-unix/ "grep command examples and syntax") • [whereis](https://www.cyberciti.biz/faq/unix-linux-whereis-command-examples-to-locate-binary/ "whereis command examples and syntax") • [which](https://www.cyberciti.biz/faq/unix-linux-which-command-examples-syntax-to-locate-programs/ "which command examples and syntax")

Shell builtins

[compgen](https://www.cyberciti.biz/open-source/command-line-hacks/compgen-linux-command/ "compgen command examples and syntax") • [echo](https://bash.cyberciti.biz/guide/Echo_Command "echo command examples and syntax") • [printf](https://bash.cyberciti.biz/guide/Printf_command "printf command examples and syntax")

Text processing

[cut](https://bash.cyberciti.biz/guide/Cut_command "cut command in Linux examples and syntax") • [rev](https://bash.cyberciti.biz/guide/Rev_command "rev command in Linux examples and syntax")

User Information

[groups](https://www.cyberciti.biz/faq/unix-linux-groups-command-examples-syntax-usage/ "groups command examples and syntax") • [id](https://www.cyberciti.biz/faq/unix-linux-id-command-examples-usage-syntax/ "id command examples and syntax") • [lastcomm](https://www.cyberciti.biz/faq/linux-unix-lastcomm-command-examples-usage-syntax/ "lastcomm command examples and syntax") • [last](https://www.cyberciti.biz/faq/linux-unix-last-command-examples/ "last command examples and syntax") • [lid/libuser-lid](https://www.cyberciti.biz/faq/linux-lid-command-examples-syntax-usage/ "lid/libuser-lid command examples and syntax") • [logname](https://www.cyberciti.biz/faq/unix-linux-logname-command-examples-syntax-usage/ "longname command examples and syntax") • [members](https://www.cyberciti.biz/faq/linux-members-command-examples-usage-syntax/ "members command examples and syntax") • [users](https://www.cyberciti.biz/faq/unix-linux-users-command-examples-syntax-usage/ "users command examples and syntax") • [whoami](https://www.cyberciti.biz/faq/unix-linux-whoami-command-examples-syntax-usage/ "whoami command examples and syntax") • [who](https://www.cyberciti.biz/faq/unix-linux-w-command-examples-syntax-usage-2/ "who command examples and syntax") • [w](https://www.cyberciti.biz/faq/unix-linux-w-command-examples-syntax-usage-2/ "w command examples and syntax")

WireGuard VPN

[Alpine](https://www.cyberciti.biz/faq/how-to-set-up-wireguard-vpn-server-on-alpine-linux/ "Alpine Linux set up WireGuard VPN server examples and syntax") • [CentOS 8](https://www.cyberciti.biz/faq/centos-8-set-up-wireguard-vpn-server/ "CentOS 8 set up WireGuard VPN server examples and syntax") • [Debian 10](https://www.cyberciti.biz/faq/debian-10-set-up-wireguard-vpn-server/ "Debian 10 set up WireGuard VPN server examples and syntax") • [Firewall](https://www.cyberciti.biz/faq/how-to-set-up-wireguard-firewall-rules-in-linux/ "WireGuard Firewall Rules in Linux examples and syntax") • [Ubuntu 20.04](https://www.cyberciti.biz/faq/ubuntu-20-04-set-up-wireguard-vpn-server/ "Ubuntu 20.04 set up WireGuard VPN server examples and syntax")