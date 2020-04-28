#Center
**pre**
```
yum install zlib-devel pcre2-devel make gcc libevent-devel openssl-devel
```
**install**
```
wget https://github.com/ossec/ossec-hids/archive/3.6.0.tar.gz
# tar -zxvf ossec-hids-*.tar.gz (or gunzip -d; tar -xvf)
# cd ossec-hids-*
# ./install.sh
```

**run**

```
 - To start OSSEC HIDS:
      /var/ossec/bin/ossec-control start

 - To stop OSSEC HIDS:
      /var/ossec/bin/ossec-control stop

 - The configuration can be viewed or modified at /var/ossec/etc/ossec.conf 
```



#Client


```
# /var/ossec/bin/manage_agents
then Import key
then 
ossec-control restart
```











#Conf

add_agent
```
# /var/ossec/bin/manage_agents
```
then edit /var/osssec/etc/ossec.conf

```
ossec_config>
  <client>
    <server-ip>192.168.40.191</server-ip>
    <config-profile></config-profile>
  </client>
```
change 192.168.40.191 to your center ip.

then restart both server and agent




#port 

Communication between agents and the OSSEC server generally occurs on port 1514/udp in secure mode. If using the syslog mode for ossec-remoted, then port 514 is the default (both UDP and TCP are supported). 






#tips

**Can an OSSEC manager have more than 256 agents?**

**By default OSSEC limits the number of agents to 256 per manager**. This limitation is set in the code, but can be modified at compile time. Depending on the event load, a manager running on modern hardware can handle many more agents. Some users have more than 1000 agents on a single manager. To change the maximum number of agents, cd into the src directory and run the following command:

make setmaxagents
You should be prompted for the number of agents to allow.

One issue you may face after changing this setting is the number of files allowed to be open for a single user. The users ossec and ossecr both open at least 1 file (syscheck database and rids file) per agent. Raising this limit is operating system specific.

Some Linux distributions support a /etc/security/limits.conf. Set the limits to be at least a few files above what the max agents is set to.
```
ossec            soft    nofile          2048
ossec            hard    nofile          2048
ossecr           soft    nofile          2048
ossecr           hard    nofile          2048
```



```
Increase maximum number of allowed agents

To increase the number of agents, before you install (or update OSSEC), just do:

#cd src; make setmaxagents (it will ask how many do you want.. )

Specify maximum number of agents: 2048 (to increase to 2048)
Maximum number of agents set to 20.

#cd ..; ./install.sh
Increase your system’s limits

Most systems have limits regarding the maximum number of files you can have. A few commands you should try are (to increase to 2048):

# ulimit -n 2048
# sysctl -w kern.maxfiles=2048
```


**Where are OSSEC’s logs stored?**
On OSSEC server and local installs there are several classes of OSSEC logs. There are the logs created by the OSSEC daemons, the log messages from the agents, and the alerts. Agent installs do not have logs from other agents or alerts, but do have logs created by the OSSEC processes.

All logs are stored in subdirectories of /var/ossec/logs. OSSEC’s log messages are stored in /var/ossec/logs/ossec.log.

**Log messages from the agents are not stored by default.** After analysis they are deleted unless the <logall> option is included in the manager’s ossec.conf. If set all log messages sent to the manager are stored in /var/ossec/logs/archives/archives.log and rotated daily.

Alerts are stored in /var/ossec/logs/alerts/alerts.log, and rotated daily.


**Where can I view the logs sent to an OSSEC manager (or on a local install)?**
OSSEC does not store the logs sent to it by default. If a log does not trigger an alert it is discarded, and logs that do trigger alerts are stored with the alerts in /var/ossec/logs/alerts.

The <log-all> option can be added to the <global> section (see: ossec.conf: Global options) of the manager’s ossec.conf. The manager’s OSSEC processes should be restarted. The raw logs will then be saved to files, organized by date, in /var/ossec/logs/archives.

The headers attached to these log messages are in the format of "YYYY Month dd HH:MM:ss agent_name->/path/to/log/file ".

2011 Aug 04 00:00:01 server->/var/log/local7 Aug  4 00:00:26 server named[29909]: client 192.168.1.7#39323: query: fake.example.net IN AAAA +

**Do the rules get pushed to the agents automatically?**
The rules only exist on the manager. All analysis is done on the manager. Agents do not send alerts to the manager, they only send the raw logs.

**with zero mq **

>https://www.ossec.net/docs/docs/syntax/head_ossec_config.global.html#ossec-config-global

```
zeromq_output
Enable ZeroMQ Output

Warning

ZeroMQ is experimental and will likely change drastically from version to version.

Allowed: yes/no

zeromq_uri
This is zeromq URI that the publisher socket will bind to.

Warning

This URI format is defined by the ZeroMQ project.

<zeromq_uri>tcp://localhost:11111/</zeromq_uri>
This will listen for zeromq subscribers on ip address 127.0.0.1 port 11111

<zeromq_uri>tcp://eth0:21212/</zeromq_uri>
This will listen for zeromq subscribers on the ip address assigned to eth0 port 21212

<zeromq_uri>ipc:///alerts-zmq</zeromq_uri>
This will listen for zeromq on the Unix Domain socket /alerts-zmq.

geoip_db_path
The full path to the GeoIP IPv4 database file location.

Example:

<geoip_db_path>/etc/GeoLiteCity.dat</geoip_db_path>
```


**ossec-logcollector(PID): ERROR: Unable to open file ‘/queue/ossec/.agent_info’**
Ensure there is a <server-ip> configured in the agent’s /var/ossec/etc/ossec.conf, and that the IP is correct.


**The OSSEC agent is unable to resolve hostnames from /etc/hosts**
By default OSSEC chroots many of its daemons to /var/ossec. When this happens the /etc/hosts file is unreadable. To resolve this issue, copy /etc/hosts to /var/ossec/etc/. A hardlink to /etc/hosts can be used if the system is does not have a separate /var/ partition.


**Using a hostname for the server does not work.**
ossec-agentd chroots to /var/ossec by default. In order to resolve hostnames, the agent will need some support. It could be as simple as copying /etc/resolv.conf to /var/ossec/etc/resolv.conf.



**How do you monitor for usb storage?**
The first step is to configure the agents to check a registry entry with the reg command:
```
<agent_config os="Windows">
  <localfile>
    <log_format>full_command</log_format>
    <command>reg QUERY HKLM\SYSTEM\CurrentControlSet\Enum\USBSTOR</command>
    <alias>usb-check</alias>
  </localfile>
</agent_config>
```
Next create a local rule for that command:
```
<rule id="140125" level="7">
  <if_sid>530</if_sid>
  <match>ossec: output: 'usb-check':</match>
  <check_diff />
  <description>New USB device connected</description>
</rule>
```
When a USB drive is inserted into a Windows machine, an alert will not be triggered. The alert will contain a diff of the registry entry before the USB device was inserted and after.

Originally from: ‘http://dcid.me/2010/03/detecting-usb-storage-usage-with-ossec/‘

Additional data from: ‘http://blog.rootshell.be/2010/03/15/detecting-usb-storage-usage-with-ossec/‘


**Why do I see alerts for agent2 in an email about agent1?**
When an email is being prepared alerts will be grouped together. The only real criteria for grouping alerts together is the timeframe. To prevent alerts from being grouped together you can set maild.groupping to 0 in /var/ossec/etc/internal_options.conf. If this is set, alerts will be sent out individually. By default OSSEC will only send 12 emails per hour. To increase this limit, modify or add the <email_maxperhour> setting in the <global> section of the ossec.conf. (see: email_maxperhour .)
```
<global>
  <email_maxperhour>100</email_maxperhour>```


**set the <email_alert_level> to 10, why do I keep seeing rules with lower levels?**
Some rules have an option set to force OSSEC into sending an alert email. This option is <options>alert_by_email</options>. One of these rules is 1002. To ignore these rules you will have to create a rule to specifically ignore it, or overwrite the rule without the alert_by_email option.


**I keep getting log messages that start with --MARK, what do I do?**
Example:

OSSEC HIDS Notification.
2014 Sep 21 08:36:11

Received From: (services01.qrios.com) 10.10.0.01->ossec-keepalive
Rule: 1002 fired (level 2) -> "Unknown problem somewhere in the system."
Portion of the log(s):

--MARK--: cB82L!#'zr%lQfGUE))-7Q#Tj4fp+KG=@Wbq^wXiN)7L;ha!JB0kA_mJT5g-j[v_R@TAbk-,/fHEnHjerroRgAIh?OLWJ6LIL/q[Wg-cl#H#n/&(3NDr$@#v8r*l;!b*qru$@YxEE4Zak=(wEqN0JuDlLo!*HtlXKF(3.kQ&wj&+aklF%YNsA&*#Mef)xq'qd@)P+Dz0[jP]70%Q6vqbfbv?fA)D?#bc?_R+6y.i+MdXUzhucx9V#MV($-3uk4!ja!MocJx;D%P=We0Y^DoV&r+fha$rmRA1v$^y4U4cD1'H5T?OF1R3(Hq'H.YcO'3soM/(P2_A@w7K^6G2C=z2#.W2[24=VBXrvVe!5;eKotCM[8W!hE_CB;/!Vk1k'sCov_H[u'(=no*VEH$'@1vVU7zj*I7s0RHD)w@ipX?&@y)X50Q'w#OyN$+$pW?xW_0JYFRwK/g3wIuKc!D#Q*eMg'79/oaURi.j].))JIQ6&!k(O]ZN#lHATidRwRTvhQFQ]6DFiBT;fltbg(OALDi/LlPqkcL5bypK?axVByOGJp+.P(@[p)'j
This is a keep alive message, sent from an OSSEC agent to the manager. This prevents the manager from marking the agent as disconnected. These log messages should be filtered out, but sometimes one slips through (this one has the string erroR which may be the cause). These should be safe to ignore.



**Debug Logging**

You can also enable debugging mode on ossec to extract more data about what is going on. To do so, you will need to modify the file /var/ossec/etc/internal_options.conf (or C:\Program Files\ossec-agent\internal_options.conf on Windows) and change the debug level from the default “0” to “1” or “2”.


If this is on an OSSEC server you can enable debug by running:
```
# /var/ossec/bin/ossec-control enable debug```
Enable debug mode and restart the OSSEC processes to view more verbose logs.


**Fixing Duplicate Errors**
Ossec agents and server keep a counter of each message sent and received in files in .../ossec/queue/rids. This is a technique to prevent replay attacks. If the counters between agent and server don’t match you’ll see errors like this in the agents ossec.log file:

2007/10/24 11:19:21 ossec-agentd: Duplicate error:  global: 12, local: 3456, saved global: 78, saved local: 91011
2007/10/24 11:19:21 ossec-agentd(<pid>): Duplicated counter for '<host name>'.
2007/10/24 11:19:21 ossec-agentd(<pid>): Problem receiving message from www.xxx.yyy.zzz.
This normally happens when you restore the ossec files from a backup or you reinstall server or agents without performing an upgrade, this can also be caused by duplicate agent ID’s. The fix for this problem is:

On every agent:
stop ossec
go to: .../ossec/queue/rids (or ossec-agent/rids on Windows) and remove every file in there.
Go to the server:
Stop ossec
Remove the rids file with the same name as the agent id that is reporting errors.
Restart the server
Restart the agents.
To avoid this problem from ever happening again, make sure to:
Always use the update option (when updating). Do not remove and reinstall the ossec server, unless you plan to do the same for all agents.
Do not re-use the same agent key between multiple agents or the same agent key after you remove/re-install an agent. If you use the “update” options everything should just work.

>https://www.ossec.net/docs/docs/faq/unexpected.html