## Adding the Wazuh repository[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#adding-the-wazuh-repository "Permalink to this headline")

The first step to setting up Wazuh is to add the Wazuh repository to your server. If you want to download the wazuh-manager package directly, or check the compatible versions, click [here](https://documentation.wazuh.com/3.12/installation-guide/packages-list/index.html#packages).

To set up the repository, run this command:

> Copied to clipboard
> 
> \# rpm --import https://packages.wazuh.com/key/GPG-KEY-WAZUH
> \# cat > /etc/yum.repos.d/wazuh.repo <<\\EOF
> \[wazuh\_repo\]
> gpgcheck=1
> gpgkey=https://packages.wazuh.com/key/GPG-KEY-WAZUH
> enabled=1
> name=Wazuh repository
> baseurl=https://packages.wazuh.com/3.x/yum/
> protect=1
> EOF

## Installing the Wazuh manager[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#installing-the-wazuh-manager "Permalink to this headline")

The next step is to install the Wazuh manager on your system:

> Copied to clipboard
> 
> \# yum install wazuh-manager

Once the process is complete, you can check the service status with:

> - For Systemd:
>     
>     Copied to clipboard
>     
>     \# systemctl status wazuh-manager
>     
> - For SysV Init:
>     
>     Copied to clipboard
>     
>     \# service wazuh-manager status
>     

## Installing the Wazuh API[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#installing-the-wazuh-api "Permalink to this headline")

1. NodeJS >= 4.6.1 is required in order to run the Wazuh API. If you do not have NodeJS installed or your version is older than 4.6.1, we recommend that you add the official NodeJS repository like this:

> Copied to clipboard
> 
> \# curl --silent --location https://rpm.nodesource.com/setup\_10.x | bash -
> 
> and then, install NodeJS:
> 
> Copied to clipboard
> 
> \# yum install nodejs

2. Install the Wazuh API. It will update NodeJS if it is required:

> Copied to clipboard
> 
> \# yum install wazuh-api

3. Once the process is complete, you can check the service status with:

> - For Systemd:
>     
>     Copied to clipboard
>     
>     \# systemctl status wazuh-api
>     
> - For SysV Init:
>     
>     Copied to clipboard
>     
>     \# service wazuh-api status
>     

Note

Now that the Wazuh API is installed, check out the section [Securing the Wazuh API](https://documentation.wazuh.com/3.12/installation-guide/securing_api.html#securing-api) to set up some additional settings.

4. (Optional) Disable the Wazuh repository:

> It is recommended that the Wazuh repository be disabled in order to prevent accidental upgrades. To do this, use the following command:
> 
> Copied to clipboard
> 
> \# sed -i "s/^enabled=1/enabled=0/" /etc/yum.repos.d/wazuh.repo

## Installing Filebeat[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#installing-filebeat "Permalink to this headline")

Filebeat is the tool on the Wazuh server that securely forwards alerts and archived events to Elasticsearch. To install it:

1. Add the Elastic repository and its GPG key:

> Copied to clipboard
> 
> \# rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch
> \# cat > /etc/yum.repos.d/elastic.repo << EOF
> \[elasticsearch-7.x\]
> name=Elasticsearch repository for 7.x packages
> baseurl=https://artifacts.elastic.co/packages/7.x/yum
> gpgcheck=1
> gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
> enabled=1
> autorefresh=1
> type=rpm-md
> EOF

2. Install Filebeat:

> Copied to clipboard
> 
> \# yum install filebeat-7.6.2

3. Download the Filebeat configuration file from the Wazuh repository. This is pre-configured to forward Wazuh alerts to Elasticsearch:

> Copied to clipboard
> 
> \# curl -so /etc/filebeat/filebeat.yml https://raw.githubusercontent.com/wazuh/wazuh/v3.12.3/extensions/filebeat/7.x/filebeat.yml
> \# chmod go+r /etc/filebeat/filebeat.yml

4. Download the alerts template for Elasticsearch:

> Copied to clipboard
> 
> \# curl -so /etc/filebeat/wazuh-template.json https://raw.githubusercontent.com/wazuh/wazuh/v3.12.3/extensions/elasticsearch/7.x/wazuh-template.json
> \# chmod go+r /etc/filebeat/wazuh-template.json

5. Download the Wazuh module for Filebeat:

> Copied to clipboard
> 
> \# curl -s https://packages.wazuh.com/3.x/filebeat/wazuh-filebeat-0.1.tar.gz | sudo tar -xvz -C /usr/share/filebeat/module

6. Edit the file `/etc/filebeat/filebeat.yml` and replace `YOUR_ELASTIC_SERVER_IP` with the IP address or the hostname of the Elasticsearch server. For example:

> Copied to clipboard
> 
> output.elasticsearch.hosts: \['http://YOUR\_ELASTIC\_SERVER\_IP:9200'\]

7. Enable and start the Filebeat service:

> - For Systemd:
>     
>     Copied to clipboard
>     
>     \# systemctl daemon-reload
>     \# systemctl enable filebeat.service
>     \# systemctl start filebeat.service
>     
> - For SysV Init:
>     
>     Copied to clipboard
>     
>     \# chkconfig --add filebeat
>     \# service filebeat start
>     

8. (Optional) Disable the Elastic repository:

> It is recommended that the Elastic repository be disabled in order to prevent accidental upgrades. To do this, use the following command:
> 
> Copied to clipboard
> 
> \# sed -i "s/^enabled=1/enabled=0/" /etc/yum.repos.d/elastic.repo

## Next steps[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#next-steps "Permalink to this headline")

Once you have installed the manager, API and Filebeat, you are ready to install [Elastic Stack](https://documentation.wazuh.com/3.12/installation-guide/installing-elastic-stack/index.html#installation-elastic).

## Uninstall[¶](https://documentation.wazuh.com/3.12/installation-guide/installing-wazuh-manager/linux/centos/wazuh_server_packages_centos.html#uninstall "Permalink to this headline")

To uninstall the Wazuh manager and Wazuh API:

> Copied to clipboard
> 
> \# yum remove wazuh-manager wazuh-api

There are files marked as configuration files. Due to this designation, the package manager doesn’t remove those files from the filesystem. The complete files removal action is a user responsibility. It can be done by removing the folder `/var/ossec`.

To uninstall filebeat:

> Copied to clipboard
> 
> \# yum remove filebeat






##bash history monitor

# Keep watch for malicious command execution[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#keep-watch-for-malicious-command-execution "Permalink to this headline")

Linux systems have a powerful auditing facility called **auditd** which can give a very detailed accounting of actions and changes in a system, but by default, no auditd rules are active so we tend to miss out on this detailed history. In this lab, we will configure auditd on a Linux machine to account for all commands executed by a given user (it should be “centos” if you built the lab following this guide), including commands run by this user in a sudo command or after sudo-ing to root. After causing some audit events to be generated, we will look them over in Kibana. Then we will set up several custom Wazuh rules to alert on especially suspicious command calls, making use of the CDB list lookup capability that allows rules to look up decoded field values in various lists and to use the results as part of the alert criteria.

The Linux auditd system is an extensive auditing tool, which we will only touch on here. Consider reading the [Monitoring system calls](https://documentation.wazuh.com/3.12/user-manual/capabilities/system-calls-monitoring/index.html#system-call-monitoring) section to get a broader picture of the ways you can take advantage of it.

## Turn on program call auditing on linux-agent[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#turn-on-program-call-auditing-on-linux-agent "Permalink to this headline")

1. Having already sudo-ed to root on our linux-agent machine, append the following audit rules to `/etc/audit/rules.d/audit.rules`
    
    > Copied to clipboard
    > 
    > \[root@linux-agent centos\]# echo "-a exit,always -F auid=1000 -F egid!=994 -F auid!=-1 -F arch=b32 -S execve -k audit-wazuh-c" >> /etc/audit/rules.d/audit.rules
    > \[root@linux-agent centos\]# echo "-a exit,always -F auid=1000 -F egid!=994 -F auid!=-1 -F arch=b64 -S execve -k audit-wazuh-c" >> /etc/audit/rules.d/audit.rules
    
    Where `auid=1000` represents the user ID. If unsure, you may verify this value by running: `grep centos /etc/passwd` (replacing `centos` if you have a different user name).
    
2. Then reload the rules and confirm they are in place:
    
    > Copied to clipboard
    > 
    > \[root@linux-agent centos\]# auditctl -R /etc/audit/rules.d/audit.rules
    > ...
    > \[root@linux-agent centos\]# auditctl -l
    > 
    > Output
    > 
    > \-a always,exit -F arch=b32 -S execve -F auid=1000 -F egid!=994 -F auid!=-1 -F key=audit-wazuh-c
    > -a always,exit -F arch=b64 -S execve -F auid=1000 -F egid!=994 -F auid!=-1 -F key=audit-wazuh-c
    

## Trigger a few audit events[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#trigger-a-few-audit-events "Permalink to this headline")

1. Dropping from **root** back to the unpriviledged user, run a ping.
    
    > Copied to clipboard
    > 
    > \[root@linux-agent centos\]# exit
    > 
    > Output
    > 
    > logout
    > 
    > Copied to clipboard
    > 
    > \[centos@linux-agent ~\]$ ping -c1 8.8.4.4
    > 
    > Output
    > 
    > PING 8.8.4.4 (8.8.4.4) 56(84) bytes of data.
    > 64 bytes from 8.8.4.4: icmp\_seq=1 ttl=51 time=1.09 ms
    > 
    > --- 8.8.4.4 ping statistics ---
    > 1 packets transmitted, 1 received, 0% packet loss, time 0ms
    > rtt min/avg/max/mdev = 1.093/1.093/1.093/0.000 ms
    
2. While still **centos**, use sudo to run a privileged commands
    
    > Copied to clipboard
    > 
    > \[centos@linux-agent ~\]$ sudo cat /etc/shadow
    > 
    > Output
    > 
    > root:!!:17497:0:99999:7:::
    > bin:\*:17110:0:99999:7:::
    > ...
    
3. Now sudo back to root and run another commands
    
    > Copied to clipboard
    > 
    > \[centos@linux-agent ~\]$ sudo su -
    > 
    > Output
    > 
    > Last login: Thu Nov 14 12:27:12 UTC 2019 on pts/0
    > 
    > Copied to clipboard
    > 
    > \[root@linux-agent ~\]# df
    > 
    > Output
    > 
    > Filesystem     1K-blocks    Used Available Use% Mounted on
    > /dev/xvda1       8377344 1616824   6760520  20% /
    > devtmpfs          486604       0    486604   0% /dev
    > tmpfs             507288       0    507288   0% /dev/shm
    > tmpfs             507288   12956    494332   3% /run
    > tmpfs             507288       0    507288   0% /sys/fs/cgroup
    > tmpfs             101460       0    101460   0% /run/user/1000
    

## Look over the audit events[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#look-over-the-audit-events "Permalink to this headline")

1. On the monitored linux machine, inspect the content of `/var/log/audit/audit.log`. Auditd writes events here, but it is not very readable. Thankfully the Linux Wazuh agents already monitors this file by default.
    
2. Search Kibana for `rule.id:80792` in the Kibana Discover area. That will catch all auditd command audit events.
    
3. Pick the following Kibana fields for columnar display:
    
    > - data.audit.command
    > - data.audit.auid
    > - data.audit.euid
    > - full\_log
    
4. Explore the audit records, finding and examining your unprivileged ping, and your privileged cat and df calls. They will be mingled with other commands.
    
5. The **centos** user has uid 1000. User **root** has uid 0. Notice the `auid` (audited user identity) always traces back to the **centos** user, even though the `euid` effective user identity is sometimes 0 and sometimes 1000 depending on whether privileges were escalated. This allows you to see who actually ran the command with sudo or while sudo-ed to **root**.
    

## Look over the relevant Wazuh rule[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#look-over-the-relevant-wazuh-rule "Permalink to this headline")

1. Here is Wazuh rule 80792:
    
    > Copied to clipboard
    > 
    > <rule id="80792" level="3"\>
    >     <if\_sid>80700</if\_sid>
    >     <list field="audit.key" lookup="match\_key\_value" check\_value="command"\>etc/lists/audit-keys</list>
    >     <description>Audit: Command: $(audit.exe)</description>
    >     <group>audit\_command,</group>
    > </rule>
    > 
    > Parent rule 80700 catches all auditd events, while this rule focuses on auditd command events. Notice how the `<list>` line in this rule takes the decoded `audit.key` value which all our auditd rules set to “audit-wazuh-c” presently, and looks this up in a CDB list called `audit-keys` to see if the `audit.key` value is listed with a value of “command”.
    
2. Look over the key-value pairs in the lookup file. The file is `/var/ossec/etc/lists/audit-keys`.
    
    > Copied to clipboard
    > 
    > audit-wazuh-w:write
    > audit-wazuh-r:read
    > audit-wazuh-a:attribute
    > audit-wazuh-x:execute
    > audit-wazuh-c:command
    > 
    > This CDB list contains keys and values separated colons. Some lists only contain keys, in which case each key exists on a line of its own and is directly followed by a colon.
    
3. Notice that in addition to the text file `/var/ossec/etc/lists/audit-keys`, there is also a binary `/var/ossec/etc/lists/audit-keys.cdb` file that Wazuh uses for actual lookups.
    

## Create a list of commands that Wazuh will watch for[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#create-a-list-of-commands-that-wazuh-will-watch-for "Permalink to this headline")

Wazuh allows you to maintain flat file CDB lists (key only or key:value) which are compiled into a special binary format to facilitate high-performance lookups in Wazuh rules. Such lists must be created as files, added to the Wazuh configuration, and then compiled. After that, rules can be built to look up decoded fields in those CDB lists as part of their match criteria. Right now, we want to create a list of commands that Wazuh will use to give us special alerts when executed.

1. On wazuh-manager, create `/var/ossec/etc/lists/suspicious-programs` with this content:
    
    > Copied to clipboard
    > 
    > ncat:
    > nc:
    > tcpdump:
    > ping:
    
2. On wazuh-manager, add this to the `<ruleset>` section of ossec.configuration in `/var/ossec/etc/ossec.conf`:
    
    > Copied to clipboard
    > 
    > <ruleset>
    >   <list>etc/lists/suspicious-programs</list>
    >   ....
    > 
    > Note
    > 
    > Before Wazuh v3.11.0 it was necessary to run /var/ossec/bin/ossec-makelists after changing CDB lists. After v3.11.0 the lists are already compiled when the manager is started.
    
3. Now let’s add a new rule that uses this list as part of its criteria to do so add the following to `/var/ossec/etc/rules/local_rules.xml` on the Wazuh Manager.
    
    > Copied to clipboard
    > 
    > <group name="audit"\>
    >   <rule id="100200" level="8"\>
    >       <if\_sid>80792</if\_sid>
    >       <list field="audit.command" lookup="match\_key"\>etc/lists/suspicious-programs</list>
    >       <description>Audit: Suspicious Command: $(audit.exe)</description>
    >       <group>audit\_command,</group>
    >   </rule>
    > </group>
    > 
    > In this case we are simply checking to see if the decoded `audit.command` value appears in our new CDB lists at all, without checking its value.
    
4. Compile the CDB list (if your version is inferior to v3.11.0):
    
    > Copied to clipboard
    > 
    > \[root@wazuh-manager centos\]# /var/ossec/bin/ossec-makelists
    
5. Restart the Wazuh manager:
    

> 1. For Systemd:
> 
> > Copied to clipboard
> > 
> > \# systemctl restart wazuh-manager
> 
> 2. For SysV Init:
> 
> > Copied to clipboard
> > 
> > \# service wazuh-manager restart

6. On the linux computer monitored agent, install and run `tcpdump` to trip our new rule:
    
    > Copied to clipboard
    > 
    > \[root@linux-agent ~\]# yum -y install tcpdump
    > \[root@linux-agent ~\]# tcpdump --version
    

6. Search Kibana for `data.audit.command:tcpdump` and expand the record, where you should see a `rule.id` of 100200.

## Make a smarter list and rule[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#make-a-smarter-list-and-rule "Permalink to this headline")

Let’s make this list a little smarter by including values that indicate how alarmed we should be about a given program being run.

1. On The manager, replace the content of `/var/ossec/etc/lists/suspicious-programs` with the following:
    
    > Copied to clipboard
    > 
    > ncat:red
    > nc:red
    > tcpdump:orange
    > ping:yellow
    
2. Now that our `suspicious-programs` list is more granular, let’s create a higher severity rule to fire specifically on instances when a “red” program is executed.
    
    Add this new rule to `/var/ossec/etc/rules/local_rules.xml` on wazuh-manager, directly after rule 100200 and before the closing `</group>` tag:
    
    > Copied to clipboard
    > 
    > <rule id="100210" level="12"\>
    >     <if\_sid>80792</if\_sid>
    >     <list field="audit.command" lookup="match\_key\_value" check\_value="red"\>etc/lists/suspicious-programs</list>
    >     <description>Audit: Highly Suspicious Command executed: $(audit.exe)</description>
    >     <group>audit\_command,</group>
    > </rule>
    
3. Compile the CDB list (if your version is inferior to v3.11.0):
    
    > Copied to clipboard
    > 
    > \[root@wazuh-manager centos\]# /var/ossec/bin/ossec-makelists
    
4. Restart the Wazuh manager:
    

> 1. For Systemd:
> 
> > Copied to clipboard
> > 
> > \# systemctl restart wazuh-manager
> 
> 2. For SysV Init:
> 
> > Copied to clipboard
> > 
> > \# service wazuh-manager restart

45 On the monitored linux agent install and run a “red” program (netcat):

> Copied to clipboard
> 
> \[root@linux-agent ~\]# yum -y install nmap-ncat
> \[root@linux-agent ~\]# nc -v

6. Search Kibana for `data.audit.command:nc` and expand the record, noting especially the rule.description of “Audit: Highly Suspicious Command executed: /usr/bin/ncat”

## Make an exception[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#make-an-exception "Permalink to this headline")

You have `ping` in your CDB list, but perhaps you have several systems that routinely ping 8.8.8.8 as a connectivity check and you don’t want these events to be logged. Another child rule of `80297`, with a level of “0” can provide such an exception.

1. Add this new rule to `/var/ossec/etc/rules/local_rules.xml` on wazuh-manager, directly after rule 100210 and before the closing `</group>` tag.:
    
    > Copied to clipboard
    > 
    > <rule id="100220" level="0"\>
    >     <if\_sid>80792</if\_sid>
    >     <description>Ignore pings of 8.8.8.8</description>
    >     <field name="audit.command"\>^ping$</field>
    >     <match>\="8.8.8.8"</match>
    >     <group>audit\_command,</group>
    > </rule>
    > 
    > This rule does not do a lookup, it just checks any auditd command records in which the `ping` command is called and the target IP address 8.8.8.8 is mentioned.
    
2. Restart the Wazuh manager:
    

> 1. For Systemd:
> 
> > Copied to clipboard
> > 
> > \# systemctl restart wazuh-manager
> 
> 2. For SysV Init:
> 
> > Copied to clipboard
> > 
> > \# service wazuh-manager restart

3. In you linux-agent, test the rule by pinging both 8.8.8.8 and 8.8.4.4.
    
    > Copied to clipboard
    > 
    > \[root@linux-agent ~\]# yum -y install tcpdump
    > \[root@linux-agent ~\]# ping -c1 8.8.8.8
    > \[root@linux-agent ~\]# ping -c1 8.8.4.4
    
4. Search Kibana for `data.audit.command:ping`. Notice that only the ping event involving 8.8.4.4 shows up, because the other one was ignored by this exception rule.
    

## Observe the order in which our child rules are evaluated[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#observe-the-order-in-which-our-child-rules-are-evaluated "Permalink to this headline")

1. On your linux-agent, run a mundane command not listed in our CDB.
    
    > Copied to clipboard
    > 
    > \[root@linux-agent ~\]# sleep 1
    
2. Search Kibana for `data.audit.command:sleep` to find the resulting event. Copy the `full_log` value.
    
3. Run `/var/ossec/bin/ossec-logtest -v` on the Wazuh Manager and paste in the `full_log` value from above.
    
4. Carefully note the order in which child rules of “80792 - Audit: Command” were evaluated:
    
    > Output
    > 
    > ...
    > Trying rule: 80789 - Audit: Watch - Execute access: $(audit.file.name)
    > Trying rule: 80792 - Audit: Command: $(audit.exe)
    >    \*Rule 80792 matched.
    >    \*Trying child rules.
    > Trying rule: 100220 - Ignore pings of 8.8.8.8
    > Trying rule: 100210 - Audit: Highly Suspicious Command: $(audit.exe)
    > Trying rule: 100200 - Audit: Suspicious Command: $(audit.exe)
    
5. Remember that when a rule matches, if it has multiple child rules, they are not evaluated in order of ID nor in the order they appear in the rule file. Instead, child rules of level “0” are checked first since they are for making exceptions. Then any remaining child rules are checked in the order of highest severity to lowest severity. Keep this in mind as you build child rules of your own.
    

Warning

**Why does my new rule never fire?**

Sometimes a new rule never matches anything because of a flaw in its criteria. Other times it never matches because it is never even evaluated. Remember, `ossec-logtest -v` is your friend. Use it to see if your rule is being evaluated at all, and if not, what rule might be overshadowing it.

## Remember to set your settings back to normal[¶](https://documentation.wazuh.com/3.12/learning-wazuh/audit-commands.html#remember-to-set-your-settings-back-to-normal "Permalink to this headline")

When testing different things, it is recommendable that you reverse the changes to keep your testing Lab clean. So new tests don’t interfere with previous ones.

You would need to delete the line we wrote in the `<ruleset>` section of Wazuh manager’s configuration, `ossec.conf`:

> Copied to clipboard
> 
> <list>etc/lists/suspicious-programs</list>

In the linux-agent, delete the two lines we added to `/etc/audit/rules.d/audit.rules`:

> Copied to clipboard
> 
> \-a always,exit -F arch=b32 -S execve -F auid=1000 -F egid!=994 -F auid!=-1 -F key=audit-wazuh-c
> \-a always,exit -F arch=b64 -S execve -F auid=1000 -F egid!=994 -F auid!=-1 -F key=audit-wazuh-c

Now you would need to reload the auditd rules and restart the manager for changes to take effect:

> Copied to clipboard
> 
> \[root@linux-agent centos\]# auditctl -R /etc/audit/rules.d/audit.rules
> 
> Copied to clipboard
> 
> \[root@wazuh-manager centos\]# systemctl restart wazuh-manager



>https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html


#Audit rules

### Manager[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#manager "Permalink to this headline")

Audit generates numerous events and it is hard to distinguish if those events correspond to a _write access_, _read access_, _execute access_, _attribute change_, or _system call rule_, using Wazuh decoders and rules. This is why we use the _key_ argument in audit rules to facilitate the processing of events by Wazuh. As previously explained, each audit rule has the option to add a descriptive _key_ value to identify what rule generated a particular audit log entry. We will use a CDB list to determine the types of audit rule that has fire. This list will have the following syntax:

Copied to clipboard

key\_name:value

where:

> - **Key\_name** is the string you used in the argument _\-k_ of a _file system rule_ or a _call system rule_.
> - **Value** is one of the following values:
> 
> > - write: File system rules with -p **w**.
> > - read: File system rules with -p **r**.
> > - execute: File system rules with -p **x**.
> > - attribute: File system rules with -p **a**.
> > - command: System call rules.

By default, OSSEC includes a CDB list with the following keys:

Copied to clipboard

\# cat /var/ossec/etc/lists/audit-keys

audit-wazuh-w:write
audit-wazuh-r:read
audit-wazuh-a:attribute
audit-wazuh-x:execute
audit-wazuh-c:command

You can add your own key with its value to the list like this:

Copied to clipboard

\# echo "my\_key\_write\_type:write" >> /var/ossec/etc/lists/audit-keys

Each time you modify a CDB list, you must compile it:

Copied to clipboard

\# /var/ossec/bin/ossec-makelists

### Agent[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#agent "Permalink to this headline")

#### Installing Audit[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#installing-audit "Permalink to this headline")

In order to use the Audit system, you must have the audit package installed on your system. If you do not have this package installed, execute the following command as the root user to install it.

Red Hat, CentOS and Fedora:

Copied to clipboard

\# yum install audit

Debian and Ubuntu based Linux distributions:

Copied to clipboard

\# apt-get install auditd

#### Editing ossec.conf[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#editing-ossec-conf "Permalink to this headline")

Wazuh must be aware of the events detected by Audit. So, it is needs to be configured to read the audit log file:

Copied to clipboard

<localfile\>
  <log\_format\>audit</log\_format\>
  <location\>/var/log/audit/audit.log</location\>
</localfile\>

#### Restarting Wazuh[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#restarting-wazuh "Permalink to this headline")

Finally, we must restart Wazuh agent in order to apply the changes:

1. For Systemd:

> Copied to clipboard
> 
> \# systemctl restart wazuh-agent

2. For SysV Init:

> Copied to clipboard
> 
> \# service wazuh-agent restart

Now everything is ready to process audit events. You only need to create the proper audit rules (via _auditctl_ or _/etc/audit/audit.rules_). In the next section we will describe some good use cases.




## Monitoring accesses to a directory[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#monitoring-accesses-to-a-directory "Permalink to this headline")

In this example, we are going to monitor every kind of access under the _/home_ directory:

Copied to clipboard

auditctl \-w /home \-p w \-k audit\-wazuh\-w
auditctl \-w /home \-p a \-k audit\-wazuh\-a
auditctl \-w /home \-p r \-k audit\-wazuh\-r
auditctl \-w /home \-p x \-k audit\-wazuh\-x

Now we start getting alerts on account of the new audit rules:

Copied to clipboard

\*\* Alert 1487891035.24299: \- audit,audit\_configuration,
2017 Feb 23 15:03:55 localhost\->/var/log/audit/audit.log
Rule: 80705 (level 3) \-> 'Auditd: Configuration changed'
type\=CONFIG\_CHANGE msg\=audit(1487891033.538:2936): auid\=1000 ses\=346 subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 op\="add\_rule" key\="audit-wazuh-w" list\=4 res\=1
audit.type: CONFIG\_CHANGE
audit.id: 2936
audit.key: audit
audit.list: 4
audit.res: 1

\*\* Alert 1487891043.24730: \- audit,audit\_configuration,
2017 Feb 23 15:04:03 localhost\->/var/log/audit/audit.log
Rule: 80705 (level 3) \-> 'Auditd: Configuration changed'
type\=CONFIG\_CHANGE msg\=audit(1487891041.427:2937): auid\=1000 ses\=346 subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 op\="add\_rule" key\="audit-wazuh-a" list\=4 res\=1
audit.type: CONFIG\_CHANGE
audit.id: 2937
audit.key: audit
audit.list: 4
audit.res: 1

\*\* Alert 1487891047.25161: \- audit,audit\_configuration,
2017 Feb 23 15:04:07 localhost\->/var/log/audit/audit.log
Rule: 80705 (level 3) \-> 'Auditd: Configuration changed'
type\=CONFIG\_CHANGE msg\=audit(1487891045.481:2938): auid\=1000 ses\=346 subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 op\="add\_rule" key\="audit-wazuh-r" list\=4 res\=1
audit.type: CONFIG\_CHANGE
audit.id: 2938
audit.key: audit
audit.list: 4
audit.res: 1

\*\* Alert 1487891049.25592: \- audit,audit\_configuration,
2017 Feb 23 15:04:09 localhost\->/var/log/audit/audit.log
Rule: 80705 (level 3) \-> 'Auditd: Configuration changed'
type\=CONFIG\_CHANGE msg\=audit(1487891049.144:2939): auid\=1000 ses\=346 subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 op\="add\_rule" key\="audit-wazuh-x" list\=4 res\=1
audit.type: CONFIG\_CHANGE
audit.id: 2939
audit.key: audit
audit.list: 4
audit.res: 1

Note

While it would be possible to define the previous rules as one single rule that specifies _\-p warx_, we intentionally separate them out so each rule has its own unique **key** value that is important for analysis.

Let’s see what happens when we execute the following commands:

New File

> Command:

Copied to clipboard

 # touch /home/malware.py

Alert::

 \*\* Alert 1487891161.28457: - audit,audit\_watch\_write,audit\_watch\_create,
 2017 Feb 23 15:06:01 localhost->/var/log/audit/audit.log
 Rule: 80790 (level 3) -> 'Audit: Created: /home/malware.py'
 type=SYSCALL msg=audit(1487891161.190:2942): arch=c000003e syscall=2 success=yes exit=3 a0=7ffce677b7b7
 a1=941 a2=1b6 a3=7ffce6779690 items=2 ppid=60621 pid=60761 auid=1000 uid=0 gid=0 euid=0 suid=0
 fsuid=0 egid=0 sgid=0 fsgid=0 tty=pts0 ses=346 comm="touch" exe="/usr/bin/touch"
 subj=unconfined\_u:unconfined\_r:unconfined\_t:s0-s0:c0.c1023 key="audit-wazuh-w" type=CWD
 msg=audit(1487891161.190:2942):  cwd="/" type=PATH msg=audit(1487891161.190:2942): item=0
 name="/home/" inode=16777403 dev=fd:00 mode=040755 ouid=0 ogid=0 rdev=00:00
 obj=system\_u:object\_r:home\_root\_t:s0 objtype=PARENT type=PATH msg=audit(1487891161.190:2942):item=1
 name="/home/malware.py" inode=18369115 dev=fd:00 mode=0100644 ouid=0 ogid=0 rdev=00:00
 obj=unconfined\_u:object\_r:home\_root\_t:s0 objtype=CREATE
 audit.type: SYSCALL
 audit.id: 2942
 audit.syscall: 2
 audit.success: yes
 audit.exit: 3
 audit.ppid: 60621
 audit.pid: 60761
 audit.auid: 1000
 audit.uid: 0
 audit.gid: 0
 audit.euid: 0
 audit.suid: 0
 audit.fsuid: 0
 audit.egid: 0
 audit.sgid: 0
 audit.fsgid: 0
 audit.tty: pts0
 audit.session: 346
 audit.command: touch
 audit.exe: /usr/bin/touch
 audit.key: audit-wazuh-w
 audit.cwd: /
 audit.directory.name: /home/
 audit.directory.inode: 16777403
 audit.directory.mode: 040755
 audit.file.name: /home/malware.py
 audit.file.inode: 18369115
 audit.file.mode: 0100644

Write Access

Command:

Copied to clipboard

\# nano /home/malware.py

Alert:

Copied to clipboard

\*\* Alert 1487891353.48010: \- audit,audit\_watch\_write,
2017 Feb 23 15:09:13 localhost\->/var/log/audit/audit.log
Rule: 80781 (level 3) \-> 'Audit: Watch - Write access: /home/malware.py'
type\=SYSCALL msg\=audit(1487891353.291:2956): arch\=c000003e syscall\=2 success\=yes exit\=3 a0\=9e2e80
a1\=441 a2\=1b6 a3\=63 items\=2 ppid\=60621 pid\=60819 auid\=1000 uid\=0 gid\=0 euid\=0 suid\=0 fsuid\=0 egid\=0
sgid\=0 fsgid\=0 tty\=pts0 ses\=346 comm\="nano" exe\="/usr/bin/nano"
subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 key\="audit-wazuh-w"
type\=CWD msg\=audit(1487891353.291:2956):  cwd\="/" type\=PATH msg\=audit(1487891353.291:2956): item\=0
name\="/home/" inode\=16777403 dev\=fd:00 mode\=040755 ouid\=0 ogid\=0 rdev\=00:00
obj\=system\_u:object\_r:home\_root\_t:s0 objtype\=PARENT type\=PATH msg\=audit(1487891353.291:2956): item\=1
name\="/home/malware.py" inode\=18369115 dev\=fd:00 mode\=0100644 ouid\=0 ogid\=0 rdev\=00:00
obj\=unconfined\_u:object\_r:home\_root\_t:s0 objtype\=NORMAL
audit.type: SYSCALL
audit.id: 2956
audit.syscall: 2
audit.success: yes
audit.exit: 3
audit.ppid: 60621
audit.pid: 60819
audit.auid: 1000
audit.uid: 0
audit.gid: 0
audit.euid: 0
audit.suid: 0
audit.fsuid: 0
audit.egid: 0
audit.sgid: 0
audit.fsgid: 0
audit.tty: pts0
audit.session: 346
audit.command: nano
audit.exe: /usr/bin/nano
audit.key: audit\-wazuh\-w
audit.cwd: /
audit.directory.name: /home/
audit.directory.inode: 16777403
audit.directory.mode: 040755
audit.file.name: /home/malware.py
audit.file.inode: 18369115
audit.file.mode: 0100644

Change Permissions

> Command:

Copied to clipboard

 # chmod u+x /home/malware.py

Alert::

 \*\* Alert 1487891409.49498: - audit,audit\_watch\_attribute,
 2017 Feb 23 15:10:09 localhost->/var/log/audit/audit.log
 Rule: 80787 (level 3) -> 'Audit: Watch - Change attribute: /home/malware.py'
 type=SYSCALL msg=audit(1487891408.563:2957): arch=c000003e syscall=268 success=yes exit=0 a0=ffffffffffffff9c
 a1=22f50f0 a2=1e4 a3=7fffe879a7d0 items=1 ppid=60621 pid=60820 auid=1000 uid=0 gid=0 euid=0
 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=pts0 ses=346 comm="chmod" exe="/usr/bin/chmod"
 subj=unconfined\_u:unconfined\_r:unconfined\_t:s0-s0:c0.c1023 key="audit-wazuh-a" type=CWD
 msg=audit(1487891408.563:2957):  cwd="/" type=PATH msg=audit(1487891408.563:2957): item=0
 name="/home/malware.py" inode=18369115 dev=fd:00 mode=0100644 ouid=0 ogid=0 rdev=00:00
 obj=unconfined\_u:object\_r:home\_root\_t:s0 objtype=NORMAL
 audit.type: SYSCALL
 audit.id: 2957
 audit.syscall: 268
 audit.success: yes
 audit.exit: 0
 audit.ppid: 60621
 audit.pid: 60820
 audit.auid: 1000
 audit.uid: 0
 audit.gid: 0
 audit.euid: 0
 audit.suid: 0
 audit.fsuid: 0
 audit.egid: 0
 audit.sgid: 0
 audit.fsgid: 0
 audit.tty: pts0
 audit.session: 346
 audit.command: chmod
 audit.exe: /usr/bin/chmod
 audit.key: audit-wazuh-a
 audit.cwd: /
 audit.file.name: /home/malware.py
 audit.file.inode: 18369115
 audit.file.mode: 0100644

Read access

Command:

Copied to clipboard

\# /home/malware.py

Alert:

Copied to clipboard

\*\* Alert 1487891459.53222: \- audit,audit\_watch\_read,
2017 Feb 23 15:10:59 localhost\->/var/log/audit/audit.log
Rule: 80784 (level 3) \-> 'Audit: Watch - Read access: /home/malware.py'
type\=SYSCALL msg\=audit(1487891458.283:2960): arch\=c000003e syscall\=2 success\=yes exit\=3 a0\=14d1e20
a1\=0 a2\=ffffffffffffff80 a3\=7ffdd01083d0 items\=1 ppid\=60621 pid\=60821 auid\=1000 uid\=0 gid\=0 euid\=0
suid\=0 fsuid\=0 egid\=0 sgid\=0 fsgid\=0 tty\=pts0 ses\=346 comm\="bash" exe\="/usr/bin/bash"
subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 key\="audit-wazuh-r" type\=CWD
msg\=audit(1487891458.283:2960):  cwd\="/" type\=PATH msg\=audit(1487891458.283:2960): item\=0
name\="/home/malware.py" inode\=18369115 dev\=fd:00 mode\=0100744 ouid\=0 ogid\=0 rdev\=00:00
obj\=unconfined\_u:object\_r:home\_root\_t:s0 objtype\=NORMAL
audit.type: SYSCALL
audit.id: 2960
audit.syscall: 2
audit.success: yes
audit.exit: 3
audit.ppid: 60621
audit.pid: 60821
audit.auid: 1000
audit.uid: 0
audit.gid: 0
audit.euid: 0
audit.suid: 0
audit.fsuid: 0
audit.egid: 0
audit.sgid: 0
audit.fsgid: 0
audit.tty: pts0
audit.session: 346
audit.command: bash
audit.exe: /usr/bin/bash
audit.key: audit\-wazuh\-r
audit.cwd: /
audit.file.name: /home/malware.py
audit.file.inode: 18369115
audit.file.mode: 0100744

Delete file

Command:

Copied to clipboard

\# rm /home/malware.py

Alert:

Copied to clipboard

\*\* Alert 1487891497.54463: \- audit,audit\_watch\_write,audit\_watch\_delete,
2017 Feb 23 15:11:37 localhost\->/var/log/audit/audit.log
Rule: 80791 (level 3) \-> 'Audit: Deleted: /home/malware.py'
type\=SYSCALL msg\=audit(1487891496.026:2961): arch\=c000003e syscall\=263 success\=yes exit\=0
a0\=ffffffffffffff9c a1\=13b00c0 a2\=0 a3\=7ffe1b582dc0 items\=2 ppid\=60621 pid\=60824 auid\=1000
uid\=0 gid\=0 euid\=0 suid\=0 fsuid\=0 egid\=0 sgid\=0 fsgid\=0 tty\=pts0 ses\=346 comm\="rm" exe\="/usr/bin/rm"
subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 key\="audit-wazuh-w"
type\=CWD msg\=audit(1487891496.026:2961):  cwd\="/" type\=PATH msg\=audit(1487891496.026:2961): item\=0
name\="/home/" inode\=16777403 dev\=fd:00 mode\=040755 ouid\=0 ogid\=0 rdev\=00:00
obj\=system\_u:object\_r:home\_root\_t:s0 objtype\=PARENT type\=PATH msg\=audit(1487891496.026:2961): item\=1
name\="/home/malware.py" inode\=18369115 dev\=fd:00 mode\=0100744 ouid\=0 ogid\=0 rdev\=00:00
obj\=unconfined\_u:object\_r:home\_root\_t:s0 objtype\=DELETE
audit.type: SYSCALL
audit.id: 2961
audit.syscall: 263
audit.success: yes
audit.exit: 0
audit.ppid: 60621
audit.pid: 60824
audit.auid: 1000
audit.uid: 0
audit.gid: 0
audit.euid: 0
audit.suid: 0
audit.fsuid: 0
audit.egid: 0
audit.sgid: 0
audit.fsgid: 0
audit.tty: pts0
audit.session: 346
audit.command: rm
audit.exe: /usr/bin/rm
audit.key: audit\-wazuh\-w
audit.cwd: /
audit.directory.name: /home/
audit.directory.inode: 16777403
audit.directory.mode: 040755
audit.file.name: /home/malware.py
audit.file.inode: 18369115
audit.file.mode: 0100744





## Monitoring user actions[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#monitoring-user-actions "Permalink to this headline")

Here we choose to audit all commands run by a user who has admin privileges. The audit configuration for this is quite simple:

Copied to clipboard

\# auditctl -a exit,always -F euid\=0 -F arch\=b64 -S execve -k audit-wazuh-c
\# auditctl -a exit,always -F euid\=0 -F arch\=b32 -S execve -k audit-wazuh-c

If the root user executes nano, the alert will look like this:

Copied to clipboard

\*\* Alert 1487892032.56406: \- audit,audit\_command,
2017 Feb 23 15:20:32 localhost\->/var/log/audit/audit.log
Rule: 80792 (level 3) \-> 'Audit: Command: /usr/bin/nano'
type\=SYSCALL msg\=audit(1487892031.893:2963): arch\=c000003e syscall\=59 success\=yes exit\=0 a0\=14e4990
a1\=14e4a30 a2\=14d4ef0 a3\=7ffdd01083d0 items\=2 ppid\=60621 pid\=60840 auid\=1000 uid\=0 gid\=0 euid\=0
suid\=0 fsuid\=0 egid\=0 sgid\=0 fsgid\=0 tty\=pts0 ses\=346 comm\="nano" exe\="/usr/bin/nano"
subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 key\="audit-wazuh-c" type\=EXECVE
msg\=audit(1487892031.893:2963): argc\=1 a0\="nano" type\=CWD msg\=audit(1487892031.893:2963):
cwd\="/" type\=PATH msg\=audit(1487892031.893:2963): item\=0 name\="/bin/nano" inode\=18372489 dev\=fd:00
mode\=0100755 ouid\=0 ogid\=0 rdev\=00:00 obj\=system\_u:object\_r:bin\_t:s0 objtype\=NORMAL type\=PATH
msg\=audit(1487892031.893:2963): item\=1 name\="/lib64/ld-linux-x86-64.so.2" inode\=33595530 dev\=fd:00
mode\=0100755 ouid\=0 ogid\=0 rdev\=00:00 obj\=system\_u:object\_r:ld\_so\_t:s0 objtype\=NORMAL
audit.type: SYSCALL
audit.id: 2963
audit.syscall: 59
audit.success: yes
audit.exit: 0
audit.ppid: 60621
audit.pid: 60840
audit.auid: 1000
audit.uid: 0
audit.gid: 0
audit.euid: 0
audit.suid: 0
audit.fsuid: 0
audit.egid: 0
audit.sgid: 0
audit.fsgid: 0
audit.tty: pts0
audit.session: 346
audit.command: nano
audit.exe: /usr/bin/nano
audit.key: audit\-wazuh\-c
audit.cwd: /
audit.file.name: /bin/nano
audit.file.inode: 18372489
audit.file.mode: 0100755

## Privilege escalation[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#privilege-escalation "Permalink to this headline")

By default, Wazuh is able to detect privilege escalation by analyzing the corresponding log in _/var/log/auth.log_. The below example shows the homer user executing a root action:

Copied to clipboard

\# homer@springfield:/# sudo ls /var/ossec/etc

Wazuh detects the action, extracting the _srcuser_, _dstuser_ and _command_ among other fields:

Copied to clipboard

\*\* Alert 1487892460.79075: \- syslog,sudo,pci\_dss\_10.2.5,pci\_dss\_10.2.2,
2017 Feb 23 15:27:40 localhost\->/var/log/secure
Rule: 5402 (level 3) \-> 'Successful sudo to ROOT executed'
User: root
Feb 23 15:27:40 localhost sudo:    rromero : TTY\=pts/0 ; PWD\=/home/rromero ; USER\=root ; COMMAND\=/bin/ls /var/ossec/etc
tty: pts/0
pwd: /home/rromero
command: /bin/ls

However, you may find this level of detail inadequate, in which case you can use Audit.

If you have created a rule to monitor root actions, like in the previous use case, every action with _sudo_ will be logged, but the **auid** field will inconveniently be 0 (root user) instead of that of the actual user who initiated the escalated action. You generally want to know who originally initiated a command, regardless of if it was escalated or not.

In order to keep the track of the user after sudo, it is necessary to configure _PAM_.

Warning

Be very careful with PAM configuration, as a bad configuration could make your system inaccessible.

Add the following line to every PAM service that needs it:

Copied to clipboard

session required        pam\_loginuid.so

A common configuration should include: _login_, _common-session_, _cron_ and _sshd_:

Copied to clipboard

\# grep -R "pam\_loginuid.so" /etc/pam.d/

/etc/pam.d/login:session    required     pam\_loginuid.so
/etc/pam.d/common-session:session required        pam\_loginuid.so
/etc/pam.d/cron:session    required     pam\_loginuid.so
/etc/pam.d/sshd:session    required     pam\_loginuid.so

After configuring PAM, if we execute the previous command with the user _homer_ we will see that the field _auid_ is 1004, the id of the user homer.

Copied to clipboard

\# homer@springfield:/# sudo ls /var/ossec/etc

Copied to clipboard

\*\* Alert 1487892803.121460: \- audit,audit\_command,
2017 Feb 23 15:33:23 localhost\->/var/log/audit/audit.log
Rule: 80792 (level 3) \-> 'Audit: Command: /usr/bin/ls'
type\=SYSCALL msg\=audit(1487892802.652:3054): arch\=c000003e syscall\=59 success\=yes exit\=0 a0\=7f711f7d4ef8
a1\=7f711f7d6358 a2\=7f711f7df2e0 a3\=7 items\=2 ppid\=60910 pid\=60911 auid\=1000 uid\=0 gid\=0 euid\=0 suid\=0
fsuid\=0 egid\=0 sgid\=0 fsgid\=0 tty\=pts0 ses\=346 comm\="ls" exe\="/usr/bin/ls"
subj\=unconfined\_u:unconfined\_r:unconfined\_t:s0\-s0:c0.c1023 key\="audit-wazuh-c" type\=EXECVE
msg\=audit(1487892802.652:3054): argc\=2 a0\="ls" a1\="/var/ossec/etc" type\=CWD msg\=audit(1487892802.652:3054):
cwd\="/home/rromero" type\=PATH msg\=audit(1487892802.652:3054): item\=0 name\="/bin/ls" inode\=16912203 dev\=fd:00
mode\=0100755 ouid\=0 ogid\=0 rdev\=00:00 obj\=system\_u:object\_r:bin\_t:s0 objtype\=NORMAL type\=PATH
msg\=audit(1487892802.652:3054): item\=1 name\="/lib64/ld-linux-x86-64.so.2" inode\=33595530 dev\=fd:00
mode\=0100755 ouid\=0 ogid\=0 rdev\=00:00 obj\=system\_u:object\_r:ld\_so\_t:s0 objtype\=NORMAL
audit.type: SYSCALL
audit.id: 3054
audit.syscall: 59
audit.success: yes
audit.exit: 0
audit.ppid: 60910
audit.pid: 60911
audit.auid: 1000
audit.uid: 0
audit.gid: 0
audit.euid: 0
audit.suid: 0
audit.fsuid: 0
audit.egid: 0
audit.sgid: 0
audit.fsgid: 0
audit.tty: pts0
audit.session: 346
audit.command: ls
audit.exe: /usr/bin/ls
audit.key: audit\-wazuh\-c
audit.cwd: /home/rromero
audit.file.name: /bin/ls
audit.file.inode: 16912203
audit.file.mode: 0100755




#security configuration



If you don’t need to to access to the API externally, you should bind the API to localhost using the option config.host in the configuration file /var/ossec/api/configuration/config.js.

```
config.host = "localhost";
```
ELK xpack enable with ssl and password enabled


##wazuh
Setting up credentials for Filebeat. Add the next two lines to /etc/filebeat/filebeat.yml.
```
output.elasticsearch.username: "elastic"
output.elasticsearch.password: "password_generated_for_elastic"
```

with xpack enabled,filebeat configuration
> https://www.elastic.co/guide/en/beats/filebeat/6.8/beats-basic-auth.html



well,follow this doc will be current great,alzo it's not very careful on process some detail.
> https://documentation.wazuh.com/3.12/installation-guide/installing-elastic-stack/protect-installation/xpack.html

> https://documentation.wazuh.com/3.7/user-manual/kibana-app/configure-xpack/defining-xpack-users.html


##detail about audit systemcall

```
https://documentation.wazuh.com/3.12/user-manual/capabilities/system-calls-monitoring/how-it-works.html
```



Audit uses a set of rules to define what is to be captured in the log files. There are three types of Audit rules that can be specified:

- **Control rules** allow the Audit system’s behavior and some of its configuration to be modified.
- **File system rules**, also known as file watches, allow the auditing of access to a particular file or a directory.
- **System call rules** allow logging of system calls that specified programs makes.

Audit rules can be specified interactively with the _auditctl_ command-line utility, but to make changes persistent, edit _/etc/audit/audit.rules_.

Warning

All commands that interact with the Audit service and the Audit log files require root privileges, so you will need to be root or use sudo to execute these commands.

## Control rules[¶](https://documentation.wazuh.com/3.12/user-manual/capabilities/system-calls-monitoring/how-it-works.html#control-rules "Permalink to this headline")

Some examples that illustrate how to modify the behavior of the Audit system:

  

**auditctl -b**

Set the maximum amount of existing Audit buffers in the kernel.

**auditctl -e**

Enable/disable the Audit system or lock its configuration.

**auditctl -s**

Report the status of the Audit system.

**auditctl -l**

List all currently loaded Audit rules.

**auditctl -D**

Delete all currently loaded Audit rules.

## File System Rules[¶](https://documentation.wazuh.com/3.12/user-manual/capabilities/system-calls-monitoring/how-it-works.html#file-system-rules "Permalink to this headline")

To define a file system rule, use the following syntax:

Copied to clipboard

\-w <path\> \-p <permissions\> \-k <key\_name\>

where:

  

**\-w <path>**

Specify what file or directory to audit with <path>

**\-p <permissions>**

<permissions> are the permissions that are to auditing, including the following:

Values

r

read access to a file or a directory.

w

write access to a file or a directory.

x

execute access to a file or a directory.

a

change in the file’s or directory’s attribute.

**\-k <key\_name>**

<key\_name> is an optional string to identify which rule/set of rules generates a particular log line.

This argument is **required by Wazuh** in order to analyze the logs more accurately.

For example, to define a rule that logs all write access to, and every attribute change of, the _/etc/passwd_ file, execute the following command:

Copied to clipboard

\# auditctl -w /etc/passwd -p wa -k passwd\_changes

## System Call Rules[¶](https://documentation.wazuh.com/3.12/user-manual/capabilities/system-calls-monitoring/how-it-works.html#system-call-rules "Permalink to this headline")

To define a system call rule, use the following syntax:

Copied to clipboard

\-a action,filter \-S system\_call \-F field\=value \-k key\_name

where:

  

**\-a <action>, <filter>**

Tells the kernel’s rule matching engine to append a rule at the end of the rule list.

We must specify which rule list to append it to and what action to take when it triggers.

<action>

always

read access to a file or a directory.

never

write access to a file or a directory.

The <filter> value specifies which kernel rule-matching filter is applied to the event

<filter>

task

Only audit events fork or clone syscalls.

This is rarely used in practice.

exit

All syscall and file system audit requests are evaluated.

user

This is used to remove some events that originate in user space.

By default, any event originating in user space is allowed.

exclude

This is used to exclude certain events from being logged.

_msgtype_ is used to tell the kernel which message to filter out.

For more granular control over which events to audit:

use the user and exit filters instead.

**\-S <system\_call>**

This specifies which _system\_call_ to audit. Multiple system calls can be specified in a single rule.

A list of all system calls can be found with the command `ausyscall --dump`.

**\-F <field=value>**

Use _field=value_ to specify additional criteria to narrow down which events to audit, based on:

architecture, group ID, process ID, etc…,

Multiple -F options can be used in a single rule.

**\-k <key\_name>**

<key\_name>is an optional string to identify which rule/set of rules generates a particular log line.

This argument is required by Wazuh in order to analyze the logs more accurately.

For example, to define a rule that creates a log entry every time a file is deleted or renamed by a system user whose ID is 500 or larger, use the following. Note that the _\-F auid!=4294967295_ option is used to exclude users whose login UID is not set.

Copied to clipboard

\# auditctl -a always,exit -S unlink -S unlinkat -S rename -S renameat -F auid>\=500 -F auid!\=4294967295 -k delete

It is also possible to define a file system rule using the system call rule syntax. The following command creates a rule for system calls that is analogous to the **\-w /etc/shadow -p wa** file system rule:

Copied to clipboard

\# auditctl -a always,exit -F path\=/etc/shadow -F perm\=wa



---

##port
on manager,
```
1514/udp remotelogd
1515/tcp registerd
```
by default is nessary to open.



#my conf
monitor command execution history

```
echo "-a exit,always -F egid!=994 -F auid!=-1 -F arch=b32 -S execve -k audit-wazuh-c" >> /etc/audit/rules.d/audit.rules
echo "-a exit,always -F egid!=994 -F auid!=-1 -F arch=b64 -S execve -k audit-wazuh-c" >> /etc/audit/rules.d/audit.rules
auditctl -R /etc/audit/rules.d/audit.rules
auditctl -l
```

### Agent[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#agent "Permalink to this headline")

#### Installing Audit[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#installing-audit "Permalink to this headline")

In order to use the Audit system, you must have the audit package installed on your system. If you do not have this package installed, execute the following command as the root user to install it.

Red Hat, CentOS and Fedora:

```

\# yum install audit
```
Debian and Ubuntu based Linux distributions:
```


\# apt-get install auditd
```
#### Editing ossec.conf[¶](https://documentation.wazuh.com/3.7/user-manual/capabilities/system-calls-monitoring/audit-configuration.html#editing-ossec-conf "Permalink to this headline")

Wazuh must be aware of the events detected by Audit. So, it is needs to be configured to read the audit log file:

```
<localfile>
  <log_format>audit</log_format>
  <location>/var/log/audit/audit.log</location>
</localfile>
```


#shareconf on manager

>https://documentation.wazuh.com/3.11/learning-wazuh/suricata.html


#status check


Instead of adding the above directly to `ossec.conf` on each Linux agent, this time let’s use Wazuh manager’s centralized agent configuration facility. This is a powerful feature that makes it feasible to manage the varied configurations of a diverse fleet of systems running Wazuh agent. Search the online documentation for “Centralized Configuration” for the full details about this. In short, groups of agents share common configuration content served up to them by Wazuh manager. Agents automatically pick up and apply changes made to this content on the manager, and merge the shared configuration with their local configuration.

1. Add elastic-server and linux-agent to a new agent group called “linux”. Go to wazuh-manager and:
    
    > - Create an agent group called “linux” which will cover all shared Linux agent configuration elements.
    >     
    >     > Copied to clipboard
    >     > 
    >     > \[root@wazuh-manager centos\]# /var/ossec/bin/agent\_groups -a -g linux -q
    >     > 
    >     > Output
    >     > 
    >     > Group 'linux' created.
    >     
    > - List the registered agents on wazuh-manager with the `manage_agents -l` command. Note the id numbers of the Linux agents.
    >     
    >     > Copied to clipboard
    >     > 
    >     > \[root@wazuh-manager centos\]# /var/ossec/bin/manage\_agents -l
    >     > 
    >     > Output
    >     > 
    >     > Available agents:
    >     > ID: 001, Name: linux-agent, IP: any
    >     > ID: 002, Name: elastic-server, IP: any
    >     > ID: 003, Name: windows-agent, IP: any
    >     
    > - Add each Linux agent to this new agent group by its ID number:
    >     
    >     > Copied to clipboard
    >     > 
    >     > \[root@wazuh-manager centos\]# /var/ossec/bin/agent\_groups -a -i 001 -g linux -q
    >     > 
    >     > Output
    >     > 
    >     > Group 'linux' set to agent '001'.
    >     > 
    >     > Copied to clipboard
    >     > 
    >     > \[root@wazuh-manager centos\]# /var/ossec/bin/agent\_groups -a -i 002 -g linux -q
    >     > 
    >     > Output
    >     > 
    >     > Group 'linux' set to agent '002'.
    >     
    
2. Put our Suricata-specific Wazuh agent config into the shared agent.conf file belonging to the “linux” agent group. In wazuh-manager, edit this file: `/var/ossec/etc/shared/linux/agent.conf`. Make it look like this:
    
    > Copied to clipboard
    > 
    > <agent\_config>
    >     <localfile>
    >         <log\_format>json</log\_format>
    >         <location>/var/log/suricata/eve.json</location>
    >     </localfile>
    > </agent\_config>
    
3. Confirm this shared config is valid by running `verify-agent-conf` on wazuh-manager. Always run this after changing agent conf to prevent accidental deployment of a broken agent config to your agents.
    
    > Copied to clipboard
    > 
    > \[root@wazuh-manager centos\]# /var/ossec/bin/verify-agent-conf
    > 
    > Output
    > 
    > verify-agent-conf: Verifying \[/var/ossec/etc/shared/default/agent.conf\]
    > verify-agent-conf: OK
    > 
    > verify-agent-conf: Verifying \[/var/ossec/etc/shared/linux/agent.conf\]
    > verify-agent-conf: OK
    
4. Since the config is proven valid, restart Wazuh manager to deploy the new configuration to the agents.
    

1. For Systemd:

> Copied to clipboard
> 
> \# systemctl restart wazuh-manager

2. For SysV Init:

> Copied to clipboard
> 
> \# service wazuh-manager restart

Each agent should pull down and apply this additional configuration almost immediately. You can find the fetched configuration on each agent at `/var/ossec/etc/shared/agent.conf`.




---

##agent

```
root@device:/var/ossec/logs# /var/ossec/bin/ossec-control status
wazuh-modulesd is running...
ossec-logcollector is running...
ossec-syscheckd is running...
ossec-agentd is running...
ossec-execd is running...
```
```
service wazuh-agent status
```



# trouble shootter


##kibana

>https://documentation.wazuh.com/3.12/user-manual/kibana-app/troubleshooting.html

- log dir
```
[root@wazuh kibana]# ls
kibana.stderr  kibana.stdout
[root@wazuh kibana]# pwd
/var/log/kibana
```

- app dir
```
[root@wazuh kibana]# pwd
/usr/share/kibana
```


- lib dir

```
[root@wazuh kibana]# ls
headless_shell-linux  optimize  uuid
[root@wazuh kibana]# pwd
/var/lib/kibana
```

***chrome headless error*** in kibana.stdout

```yum install nss```



***The Reporting plugin encountered issues launching Chromium in a self-test. You may have trouble generating reports.*** in kibana.stdout


```
yum install -y fontconfig freetype
```



#no alert log in kibana-wazuh

```
[root@wazuh log]# lsof /var/ossec/logs/alerts/alerts.json
COMMAND    PID  USER   FD   TYPE DEVICE SIZE/OFF     NODE NAME
filebeat  1750  root   10r   REG  253,0  4736703 17361606 /var/ossec/logs/alerts/alerts.json
ossec-ana 2311 ossec   11w   REG  253,0  4736703 17361606 /var/ossec/logs/alerts/alerts.json
```



check wazuh-api service status

```
service wazuh-api status
```

***wazuh app plugin(in kibana)***

```
[root@wazuh kibana]# cat /usr/share/kibana/optimize/wazuh/logs/wazuhapp
wazuhapp.log        wazuhapp-plain.log
[root@wazuh kibana]# cat /usr/share/kibana/optimize/wazuh/logs/
```



```
there seem to be an issue with the data not reaching Elasticsearch.

Whenever I face this problem I think about the data flow.

First thing we should check is whether or not the Wazuh manager is
generating alerts.

You can do that by checking */var/ossec/logs/alerts/alerts.json* is not
empty (you can tail it and see the last alerts).

Next thing depends on your architecture; I'll suppose a distributed one, so
Filebeat is the place to go.

Check its configuration file in */etc/filebeat/filebeat.yml*, it should be
reading from the alerts.json generated by the Wazuh manager.

Filebeat sends the data to Logstash.

You can find Logstash configuration in
*/etc/logstash/conf.d/01-wazuh-remote.conf*

It determines the index name where data will be stored in Elasticsearch
along with some other filters performed over data.

By the way, you can check what indices does your Elasticsearch have using
the command line by typing:

*curl localhost:9200/_cat/indices?v*

If you are using the default configuration you should see
wazuh-alerts-3.x-DATE indices listed.

If everything above stated is in place then you could check the logs of
every one of its parts and connectivity between all of the instances in the
architecture.

Wazuh logs: */var/ossec/logs/ossec.log*
Filebeat logs:* /var/log/filebeat/**
Logstash logs: */var/log/logstash/**
Elasticsearch logs: */var/log/elasticsearch/**

```




## filebeat 

- lib dir
```
[root@wazuh filebeat]# pwd
/var/lib/filebeat
[root@wazuh filebeat]# ls
filebeat.lock  meta.json  registry
```

- app dir
```
[root@wazuh filebeat]# ls
bin  kibana  LICENSE.txt  module  NOTICE.txt  README.md
[root@wazuh filebeat]# pwd
/usr/share/filebeat
```


Wazuh logs: */var/ossec/logs/ossec.log*
Filebeat logs:* /var/log/filebeat/**
Logstash logs: */var/log/logstash/**
Elasticsearch logs: */var/log/elasticsearch/**











##test wazuh

```
yes, we already have some sample alert ready for Vulnerabilities, AWS, VirusTotal and some others here:

curl https://raw.githubusercontent.com/wazuh/wazuh/3.2/extensions/elasticsearch/alert_sample.json | curl -XPUT "http://localhost:9200/wazuh-alerts-3.x-"`date +%Y.%m.%d`"/wazuh/sample" -H 'Content-Type: application/json' -d @-

You can find an updated installation guide (for the Elastic Stack part of the Wazuh installation) here: https://documentation.wazuh.com/current/installation-guide/installing-elastic-stack/index.html
```
 check wazuh-alerts-3.x,remember to change the date field