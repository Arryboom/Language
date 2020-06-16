#auditd doesn't log chmod at all
>https://serverfault.com/questions/817759/auditd-doesnt-log-chmod-at-all


Amazon Linux instances come with a line in the /etc/audit/audit.rules file that requires removal/commenting out in order to enable auditing.: -a never,task

So if you don't comment that line out, even if auditctl -l shows the rule, it will not get logged. The same line is in the /etc/audit/rules.d/audit.rules.default


---
#auditd log permission


Hey,

Ilike the Linux Auditd app and the basic idea. Now the audit.log is owned by root (kinda logic I guess) and the Splunk forwarder does not run as root (kinda logic i guess). So what would be the best way to handle this?

Hello sir! Couple ideas, you could add the splunk user to a group, and set group permissions on the log so that the user could read it. Also, you could forward out the events via syslog to a system that does have a forwarder running in such a way as it can read the logs.

---

#tips1

## Issue

- How to monitor the permission change and ownership change of a particular directory or file?
- How to configure `auditd` to find how a file was modified in Red Hat Enterprise Linux?
- What tool can audit files at a directory level?
- How do I monitor files or directories using auditd in Red Hat Enterprise Linux ?
- How do I monitor a file or directory to see which user or program has accessed or modified data ?

## Resolution

- The Linux Audit system (`audit` package) can be used to accomplish this task.
- Ensure the `auditd` service is running, and set to start on boot with `chkconfig auditd on`
- Set a watch on the required file to be monitored by using the `auditctl` command:

[Raw](https://access.redhat.com/solutions/10107#)

```
# auditctl -w /etc/hosts -p war -k monitor-hosts
```

- where:
    - `auditctl` is the command used to add entries to the audit database.
    - `-w` inserts a watch for the file system object at path, i.e. `/etc/shadow`.
    - `-p` sets permissions filter for a file system watch.
    - The permission are any one of the following:  
               r - read of the file  
               w - write to the file  
               x - execute the file  
               a - change in the file's attribute
    - `-k` sets a filter key on an audit rule. The filter key is an arbitrary string of text that can be up to 31 bytes long. It can uniquely identify the audit records produced by a rule.

**Note:** In order for these rules to persist after a reboot, the below must be added to the relevant rule files,

[Raw](https://access.redhat.com/solutions/10107#)

```
-w /etc/hosts -p a -k monitor-hosts
```

which are:

[Raw](https://access.redhat.com/solutions/10107#)

```
RHEL 4: /etc/audit.rules
RHEL 5: /etc/audit/audit.rules
RHEL 6: /etc/audit/audit.rules
RHEL 7: /etc/audit/rules.d/audit.rules
```

- Please see the man pages for "auditctl" and "audit.rules" for further information on syntax and swtiches.
    - The auditd service must be restarted after any changes are made, also ensure that it is set to run on boot.

[Raw](https://access.redhat.com/solutions/10107#)

```
# service auditd restart
# chkconfig --list auditd
# chkconfig auditd on
```

- In this example, a watch is placed on the `/etc/hosts` file for any syscalls which perform a write, read, or attribute change (`-p war`). This is logged with the key `monitor-hosts`. This key can be used to search through the audit logs to find these actions, using the `ausearch` command:

[Raw](https://access.redhat.com/solutions/10107#)

```
# ausearch -ts today -k monitor-hosts
----
time->Sat Feb  3 07:32:20 2007
type=PATH msg=audit(1170451940.872:34): item=0 name="/etc/hosts" inode=1308742 dev=fd:00 mode=0100644 ouid=0 ogid=0 rdev=00:00 obj=system_u:object_r:etc_t:s0
type=CWD msg=audit(1170451940.872:34): cwd="/root"
type=SYSCALL msg=audit(1170451940.872:34): arch=40000003 syscall=226 success=yes exit=0 a0=867c4b8 a1=458bcc4f a2=8686800 a3=1c items=1 ppid=3544 pid=3558 auid=0 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=pts0 comm="vim" exe="/usr/bin/vim" subj=root:system_r:unconfined_t:s0-s0:c0.c1023 key="monitor-hosts"
```

- From this trace, it can be seen that the file `/etc/hosts` was edited using the `/usr/bin/vim` command. The user that ran the command was running with the `root:system_r:unconfined_t:s0-s0:c0.c1023` SELinux context. Also, the timestamp can be converted into readable form.

[Raw](https://access.redhat.com/solutions/10107#)

```
# date -d @1170451940
Sat Feb  3 05:32:20 CST 2007
```

- Specifying a `-i` to `ausearch` also interprets numeric entities into text, making the logs more readable.
- You can search for an event based on the given key string:

[Raw](https://access.redhat.com/solutions/10107#)

```
# ausearch -k monitor-hosts
```

- You can also generate a report about the audit rule keys by running:

[Raw](https://access.redhat.com/solutions/10107#)

```
# aureport -k
```


---
#tips2

So the 2 commands to generate reports are

**\# /sbin/aureport –input-logs -m -ts today**

\-m will tell you what system files got edited

**\# /sbin/aureport –input-logs -au -ts today**

\-au will tell you whom logged in.

–input-logs is need if the commands are ran from cron.

So create a file with those 2 command in /var/log/audit/auditreport.sh

Then make it executable by chmod 700 /var/log/audit/auditreport.sh

Then edit crontab to run it and mail the output by “crontab -e” and add the line below.

**59 23 \* \* \* /var/log/audit/auditreport.sh | mail -s “servername Audit Report” customer-email@email.com**



---
#How to configure auditd to change the default permissions on the /var/log/audit/audit.log from 0600 to 0640 and also changing the group ownership of the file?

**Question**: How to configure auditd to change the default permissions on the /var/log/audit/audit.log from 0600 to 0640 and also changing the group ownership of the file?

By default it’s not possible to change permissions on the /var/log/audit/audit.log file using ACLs, instead “**log\_group**” parameter can be set under the file **/etc/audit/audit.conf**.

## The Steps

In this example, we would like to change default permissions on the /var/audit/audit.log from **600** to **640** and also changing group from **root** to **splunk**.

.square-responsive{width:336px;height:280px}@media (max-width:450px){.square-responsive{width:300px;height:250px}} (adsbygoogle=window.adsbygoogle||\[\]).push({});

1\. Check current permissions on the /var/audit/audit.log file, mostly it’s root:root with 0600

\# ls -l /var/log/audit/audit.log
-rw------- 1 root root 3531590 Jun 1 00:20 /var/log/audit/audit.log

2\. Edit **/etc/audit/auditd.conf** file and change **log\_group** to **splunk**.

Before change:

\# cat /etc/audit/auditd.conf | grep log\_group
log\_group = root

After change:

\# cat /etc/audit/auditd.conf | grep log\_group
log\_group = splunk

3\. Restart audit service and check.

\# service audit restart

4\. Check permissions on the /var/log/audit/audit.log.

\# ls -l /var/log/audit/audit.log
-rw-r----- 1 root splunk 3532862 Jun 1 00:24 /var/log/audit/audit.log

**Note**: In this example splunk user and group taken for demo, may be in your setup there could be a different user and group name.