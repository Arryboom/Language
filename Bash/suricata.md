>https://redmine.openinfosecfoundation.org/projects/suricata/wiki/CentOS_Installation
>https://www.openinfosecfoundation.org/download/suricata-5.0.3.tar.gz


#install

##centos

```
sudo yum -y install gcc libpcap-devel pcre-devel libyaml-devel file-devel \
  zlib-devel jansson-devel nss-devel libcap-ng-devel libnet-devel tar make \
  libnetfilter_queue-devel lua-devel PyYAML libmaxminddb-devel rustc cargo \
  lz4-devel
```
not works on centos8,try

```
$ sudo dnf config-manager --set-enabled PowerTools
$ sudo dnf -y install gcc libpcap-devel pcre-devel libyaml-devel file-devel zlib-devel jansson-devel nss-devel libcap-ng-devel libnet-devel tar make libnetfilter_queue-devel lua-devel python3-PyYAML libmaxminddb-devel rustc cargo lz4-devel python3
```
instead

```
wget https://www.openinfosecfoundation.org/download/suricata-5.0.3.tar.gz

tar -xvzf suricata-5.0.3.tar.gz

cd suricata-5.0.3

./configure --prefix=/usr --sysconfdir=/etc --localstatedir=/var --enable-nfqueue --enable-lua
```
if you use dnf install prefix before,change the last line to

```
$ ./configure --libdir=/usr/lib64 --prefix=/usr --sysconfdir=/etc --localstatedir=/var --enable-nfqueue --enable-lua
$ sudo make install-full
```

to initial the base rules:
```
suricata-update
```

##ubuntu


```
sudo apt-get install software-properties-common
sudo add-apt-repository ppa:oisf/suricata-stable
sudo apt-get update
sudo apt-get install suricata 
```




#configuration

>https://suricata.readthedocs.io/en/suricata-5.0.3/command-line-options.html

###turn off offload feature of your network cards

Network cards, drivers and the kernel itself have various techniques to speed up packet handling. Generally these will all have to be disabled.

LRO/GRO lead to merging various smaller packets into big ‘super packets’. These will need to be disabled as they break the dsize keyword as well as TCP state tracking.

Checksum offloading can be left enabled on AF_PACKET and PF_RING, but needs to be disabled on PCAP, NETMAP and others.

---




>https://suricata.readthedocs.io/en/suricata-5.0.3/performance/packet-capture.html


#change default network card that suricata listen





#Failure when trying to set feature via ioctl for 'eth0': Operation not supported (95)

check offload options with

```
ethtool -k eth0
```
disable the offload:
```
Display: ethtool -k ethX | grep tcp-segmentation
Enable (recommended): ethtool -K ethX tso on
Disable: ethtool -K ethX tso off
```

>https://blog.csdn.net/chenzhjlf/article/details/79228323


##test

To test the IDS functionality of Suricata it’s best to test with a signature. The signature with ID `2100498` from the ET Open ruleset is written specific for such test cases.

2100498:
```
alert ip any any -> any any (msg:"GPL ATTACK_RESPONSE id check returned root"; content:"uid=0|28|root|29|"; classtype:bad-unknown; sid:2100498; rev:7; metadata:created_at 2010_09_23, updated_at 2010_09_23;)
```
The syntax and logic behind those signatures is covered in other chapters. This will alert on any IP traffic that has the content within its payload. This rule can be triggered quite easy. Before we trigger it, we will tail on the `fast.log` so we see the result.

Ruletrigger:
```
sudo tail \-f /var/log/suricata/fast.log
curl http://testmyids.com/
```
The following output should now be seen in the log:

```
[1:2100498:7] GPL ATTACK_RESPONSE id check returned root [**] [Classification: Potentially Bad Traffic] [Priority: 2] {TCP} 217.160.0.187:80 -> 10.0.0.23:41618
```

The more advanced output is the EVE Json output which is explained in detail in [Eve JSON Output](https://suricata.readthedocs.io/en/suricata-5.0.3/output/eve/eve-json-output.html#eve-json-output). To see what this looks like it’s recommended to use `jq` to parse the JSON output.

Alert:
```
sudo tail \-f /var/log/suricata/eve.json | jq 'select(.event\_type=="alert")'
```
This will also show much more details and meta-data that are related to the triggered alert.
Stats:
```
sudo tail -f /var/log/suricata/eve.json | jq 'select(.event_type=="stats")|.stats.capture.kernel_packets'
```

---

Without doing any configuration the default operation of suricata-update is use the Emerging Threats Open ruleset.
```
suricata-update
```
This command will:

Look for the suricata program in your path to determine its version.

Look for /etc/suricata/enable.conf, /etc/suricata/disable.conf, /etc/suricata/drop.conf, and /etc/suricata/modify.conf to look for filters to apply to the downloaded rules.These files are optional and do not need to exist.

Download the Emerging Threats Open ruleset for your version of Suricata, defaulting to 4.0.0 if not found.

Apply enable, disable, drop and modify filters as loaded above.
Write out the rules to /var/lib/suricata/rules/suricata.rules.
Run Suricata in test mode on /var/lib/suricata/rules/suricata.rules.

Suricata-Update takes a different convention to rule files than Suricata traditionally has. The most noticeable difference is that the rules are stored by default in /var/lib/suricata/rules/suricata.rules.

One way to load the rules is to the the -S Suricata command line option. The other is to update your suricata.yaml to look something like this:

default-rule-path: /var/lib/suricata/rules
rule-files:
 - suricata.rules
This will be the future format of Suricata so using this is future proof.



First update the rule source index with the update-sources command:Advertisements.large-leaderboard-2{text-align:center; padding-top:10px !important;padding-bottom:10px !important;padding-left:0px !important;padding-right:0px !important;width:100% !important;box-sizing:border-box !important;background-color:#eeeeee !important;border: 1px solid #dfdfdf}eval(ez\_write\_tag(\[\[250,250\],'howtoforge\_com-large-leaderboard-2','ezslot\_17',112,'0','0'\]));

```
suricata-update update-sources
```

Will look like this:

[![Running suricata-update](https://www.howtoforge.com/images/suricata_with_elk_and_web_front_ends_n_ubuntu_bionic_beaver_1804_lts/Suricata-update11.jpg?ezimgfmt=rs:550x167/rscb1/ng:webp/ngcb1)](https://www.howtoforge.com/images/suricata_with_elk_and_web_front_ends_n_ubuntu_bionic_beaver_1804_lts/big/Suricata-update11.jpg)

This command will updata suricata-update with all of the available rules sources.

```
suricata-update list-sources
```

Will look like this:

Now we will enable all of the (free) rules sources, for a paying source you will need to have an account and pay for it of course. When enabling a paying source you will be asked for your username / password for this source. You will only have to enter it once since suricata-update saves that information.

suricata-update enable-source ptresearch/attackdetection
suricata-update enable-source oisf/trafficid
suricata-update enable-source sslbl/ssl-fp-blacklist
Will look like this:


And update your rules again to download the latest rules and also the rule sets we just added.

suricata-update
Will look something like this:

To see which sources are enable do:

suricata-update list-enabled-sources
This will look like this:




### Disable a Source

Disabling a source keeps the source configuration but disables. This is useful when a source requires parameters such as a code that you don’t want to lose, which would happen if you removed a source.

Enabling a disabled source re-enables without prompting for user inputs.

```
suricata-update disable-source et/pro
```

### Remove a Source

```
suricata-update remove-source et/pro
```

This removes the local configuration for this source. Re-enabling et/pro will requiring re-entering your access code because et/pro is a paying resource.

>https://www.howtoforge.com/tutorial/suricata-with-elk-and-web-front-ends-on-ubuntu-bionic-beaver-1804-lts/



---

###intergate with logstash

Make sure logstash can read the log file

```
usermod -a -G adm logstash
```

There is a bug in the mutate plugin so we need to update the plugins first to get the bugfix installed. However it is a good idea to update the plugins from time to time. not only to get bugfixes but also to get new functionality.

```
/usr/share/logstash/bin/logstash-plugin update
```

Now we are going to configure logstash. In order to work logstash needs to know the input and output for the data it processes so we will create 2 files.

```
nano /etc/logstash/conf.d/10-input.conf
```

And paste the following in to it.

```
input { file { path => ["/var/log/suricata/eve.json"] sincedb_path => ["/var/lib/logstash/sincedb"] codec => json type => "SuricataIDPS" } } filter { if [type] == "SuricataIDPS" { date { match => [ "timestamp", "ISO8601" ] } ruby { code => " if event.get('[event_type]') == 'fileinfo' event.set('[fileinfo][type]', event.get('[fileinfo][magic]').to_s.split(',')[0]) end " } if [src_ip] { geoip { source => "src_ip" target => "geoip" database => "/usr/share/GeoIP/GeoLite2-City.mmdb" #==> Change this to your actual GeoIP.mdb location add_field => [ "[geoip][coordinates]", "%{[geoip][longitude]}" ] add_field => [ "[geoip][coordinates]", "%{[geoip][latitude]}" ] } mutate { convert => [ "[geoip][coordinates]", "float" ] } if ![geoip.ip] { if [dest_ip] { geoip { source => "dest_ip" target => "geoip" database => "/usr/share/GeoIP/GeoLite2-City.#==> Change this to your actual GeoIP.mdb location add_field => [ "[geoip][coordinates]", "%{[geoip][longitude]}" ] add_field => [ "[geoip][coordinates]", "%{[geoip][latitude]}" ] } mutate { convert => [ "[geoip][coordinates]", "float" ]        }      }    }  }}}
```

```
nano 30-outputs.conf
```

Paste the following config into the file and save it. This sends the output of the pipeline to Elasticsearch on localhost. The output will be sent to an index for each day based upon the timestamp of the event passing through the Logstash pipeline.

output {  
elasticsearch {  
hosts => localhost
index => "logstash-%{+YYYY.MM.dd}" }  
\# stdout { codec => rubydebug }  
              }  
       }

Getting all the service to start automatically

```
systemctl daemon-reloadsystemctl enable kibana.servicesystemctl enable elasticsearch.servicesystemctl enable logstash.service
```

After this each of the services can be started and stopped using the systemctl commands like for example:Advertisements.large-mobile-banner-1{text-align:center; padding-top:10px !important;padding-bottom:10px !important;padding-left:0px !important;padding-right:0px !important;width:100% !important;box-sizing:border-box !important;background-color:#eeeeee !important;border: 1px solid #dfdfdf}eval(ez\_write\_tag(\[\[336,280\],'howtoforge\_com-large-mobile-banner-1','ezslot\_1',115,'0','0'\]));

```
systemctl start kibana.service
```

```
systemctl stop kibana.service
```


##evebox

# Evebox Installation

Evebox is a web frontend that displays the Suricata alerts after being processed by ELK.

First we will add the Evebox repository:

```
wget -qO - https://evebox.org/files/GPG-KEY-evebox | sudo apt-key add -echo "deb http://files.evebox.org/evebox/debian stable main" | tee /etc/apt/sources.list.d/evebox.listapt-get update
```

```
apt-get install eveboxcp /etc/evebox/evebox.yaml.example /etc/evebox.yaml
```

And to start evebox at boot:

```
systemctl enable evebox
```

We can now start evebox:eval(ez\_write\_tag(\[\[250,250\],'howtoforge\_com-leader-3','ezslot\_5',117,'0','0'\]));eval(ez\_write\_tag(\[\[250,250\],'howtoforge\_com-leader-3','ezslot\_6',117,'0','1'\]));

```
service evebox start
```

Now we can go to [http://localhost:5636](http://localhost:5636/) and we see the following:

To run Evebox behind apache2 proxy add this to your virtualhost:

```
ProxyPass /evebox/ http://localhost:5601/ ProxyPassReverse /(.*) http://localhost:5601/(.*)
```

```
nano /etc/evebox/evebox.yml
```

And set the following:

```
reverse-proxy: true
```

And of course reload evebox for the changes to take effect:

```
service evebox force-reload
```

Enable mod-proxy and mod-proxy-http in apache2

```
a2enmod proxya2enmod proxy_httpservice apache2 restart
```


#intergate with filebeat

Let's install filebeat:

```
apt install filebeat
```

Than we need to edit the filebeat configuration and tell it what we want filebeat to monitor.

nano /etc/filebeat/filebeat.yml

And change the following to enable our suricata log to be transmitted:

```
- type: log    # Change to true to enable this input configuration.   enabled: true   # Paths that should be crawled and fetched. Glob based paths.   paths:     - /var/log/suricata/eve.json    #- c:\programdata\elasticsearch\logs\* 
```

And set the following to send the output to logstash and comment out the eleasticsearch output.

```
#-------------------------- Elasticsearch output ------------------------------ # output.elasticsearch:   # Array of hosts to connect to. # hosts: ["localhost:9200"]    # Optional protocol and basic auth credentials.   #protocol: "https"   #username: "elastic"   #password: "changeme"  #----------------------------- Logstash output -------------------------------- output.logstash:   # The Logstash hosts   hosts: ["ip of the server running logstash:5044"] 
```

Now we need to tell logstash there is a filebeat input coming in so the filebeat will start a listening service on port 5044:

Do the following on the remote server:

nano /etc/logstash/conf.d/10-input.conf

And add the following to the file:

```
input {   beats {     port => 5044     codec => json     type => "SuricataIDPS"   } }
```

Now you can start filebeat on the source machine:

service filebeat start

And restart logstash on the remote server:

```
service logstash stop service logstash start
```


##debug

```
suricata -c /etc/suricata/suricata.yml --dump-config
```
to get global runtime enviroment variable log


###service not running but could be run by manually.

1.may caused by wrong interface name.
check ``/etc/default/suricata`` file,edit the line interface name.

2.mya caused by wrong runas user.
check ``/etc/default/suricata`` file,edit the run_as user,may null by default.



##eve.json log rotate

## 15.1.1.8. Date modifiers in filename[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#date-modifiers-in-filename "Permalink to this headline")

It is possible to use date modifiers in the eve-log filename.

outputs:
  \- eve\-log:
      filename: eve\-%s.json

The example above adds epoch time to the filename. All the date modifiers from the C library should be supported. See the man page for `strftime` for all supported modifiers.

## 15.1.1.9. Rotate log file[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#rotate-log-file "Permalink to this headline")

Eve-log can be configured to rotate based on time.

outputs:
  \- eve\-log:
      filename: eve\-%Y\-%m\-%d\-%H:%M.json
      rotate\-interval: minute

The example above creates a new log file each minute, where the filename contains a timestamp. Other supported `rotate-interval` values are `hour` and `day`.

In addition to this, it is also possible to specify the `rotate-interval` as a relative value. One example is to rotate the log file each X seconds.

outputs:
  \- eve\-log:
      filename: eve\-%Y\-%m\-%d\-%H:%M:%S.json
      rotate\-interval: 30s

The example above rotates eve-log each 30 seconds. This could be replaced with `30m` to rotate every 30 minutes, `30h` to rotate every 30 hours, `30d` to rotate every 30 days, or `30w` to rotate every 30 weeks.

## 15.1.1.10. Multiple Logger Instances[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#multiple-logger-instances "Permalink to this headline")

It is possible to have multiple ‘EVE’ instances, for example the following is valid:

outputs:
  \- eve\-log:
      enabled: yes
      type: file
      filename: eve\-ips.json
      types:
        \- alert
        \- drop

  \- eve\-log:
      enabled: yes
      type: file
      filename: eve\-nsm.json
      types:
        \- http
        \- dns
        \- tls

So here the alerts and drops go into ‘eve-ips.json’, while http, dns and tls go into ‘eve-nsm.json’.

With the exception of `drop`, you can specify multiples of the same logger type, however, `drop` can only be used once.

Note

The use of independent json loggers such as alert-json-log, dns-json-log, etc. has been deprecated and will be removed by June 2020. Please use multiple eve-log instances as documented above instead. Please see the [deprecation policy](https://suricata-ids.org/about/deprecation-policy/) for more information.

## 15.1.1.11. File permissions[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#file-permissions "Permalink to this headline")

Log file permissions can be set individually for each logger. `filemode` can be used to control the permissions of a log file, e.g.:

outputs:
  \- eve\-log:
      enabled: yes
      filename: eve.json
      filemode: 600

The example above sets the file permissions on `eve.json` to 600, which means that it is only readable and writable by the owner of the file.

## 15.1.1.12. JSON flags[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#json-flags "Permalink to this headline")

Several flags can be specified to control the JSON output in EVE:

outputs:
  \- eve\-log:
      json:
        \# Sort object keys in the same order as they were inserted
        preserve\-order: yes

        \# Make the output more compact
        compact: yes

        \# Escape all unicode characters outside the ASCII range
        ensure\-ascii: yes

        \# Escape the '/' characters in string with '\\/'
        escape\-slash: yes

All these flags are enabled by default, and can be modified per EVE instance.






#integrate with wazuh


after install,

```
cd /etc/suricata/
wget https://rules.emergingthreats.net/open/suricata-5.0/emerging.rules.tar.gz
tar -zxvf emerging.rules.tar.gz
mv suricata.yml suricata.bak
wget -O /etc/suricata/suricata.yaml http://www.branchnetconsulting.com/wazuh/suricata.yaml
```

Suricata is configured to write alerts to `/var/log/suricata/eve.json` which Wazuh does not monitor by default. Both of our Linux agents need an additional `<localfile>` config section like this:

configure this in agent's group share conf.

```
> <localfile>
>     <log\_format>json</log\_format>
>     <location>/var/log/suricata/eve.json</location>
> </localfile>

```


```
Put our Suricata-specific Wazuh agent config into the shared agent.conf file belonging to the “linux” agent group. In wazuh-manager, edit this file: /var/ossec/etc/shared/linux/agent.conf. Make it look like this:


<agent_config>
    <localfile>
        <log_format>json</log_format>
        <location>/var/log/suricata/eve.json</location>
    </localfile>
</agent_config>
```

make sure you verify conf before push to clients

```
[root@wazuh-manager centos]# /var/ossec/bin/verify-agent-conf
verify-agent-conf: Verifying [/var/ossec/etc/shared/linux/agent.conf]
verify-agent-conf: OK
```
restart to push shareconf to client.
```
service wazuh-manager restart
```


bydefault,the log in es's 
``rule.id: 86601``



#eve.json logrotate


###1 version
1) logrotate configuration at /etc/logrotate.d/suricata  

```

/var/log/suricata/*.log
/var/log/suricata/*.json
{
        rotate 14
        missingok
        compress
        create
        sharedscripts
        postrotate
                /bin/kill -HUP $(cat /var/run/suricata.pid)
        endscript
}
```
>https://redmine.openinfosecfoundation.org/issues/1938



###2 version

I set logrotate to run the suricata job every hour. It uses a file size of 500 MB which seems to get hit pretty much every hour. I also found that I need to stop and start suricata in order for the eve.json file to get reset to 0 bytes. I tried to run this as a restart but that did not seem to work, so I made it a stop start. Unfortunately this causes suricata to be offline for about 10 seconds while it restarts. Once the eve.json file grows beyond about 2-3G, it takes so long to copy it for compression that the job never finishes. This sensor is on the outside of our firewall and gets a lot of traffic. I've still not been able to get the events to the Ossim server. That is next on the list. Here is the conf file I used for suricata: 
kenooutsidesensor:
```
/var/log/suricata
# cat /etc/logrotate.d/alienvault-suricata/var/log/suricata/eve.json
 { size 500M compress 
rotate 2 
missingok 
notifempty 
postrotate 
	service suricata stop 
	service suricata start 
endscript}
```


>https://success.alienvault.com/s/question/0D50Z00008oGsQ0SAK/renamed-evejson-file-grows-too-large-to-rotate



###3 official version

## 15.1.1.9. Rotate log file[¶](https://suricata.readthedocs.io/en/latest/output/eve/eve-json-output.html#rotate-log-file "Permalink to this headline")

Eve-log can be configured to rotate based on time.
```
outputs:
  - eve-log:
      filename: eve-%Y-%m-%d-%H:%M.json
      rotate-interval: minute
```
The example above creates a new log file each minute, where the filename contains a timestamp. Other supported `rotate-interval` values are `hour` and `day`.

In addition to this, it is also possible to specify the `rotate-interval` as a relative value. One example is to rotate the log file each X seconds.
```
outputs:
  - eve-log:
      filename: eve-%Y-%m-%d-%H:%M:%S.json
      rotate-interval: 30s
```
The example above rotates eve-log each 30 seconds. This could be replaced with `30m` to rotate every 30 minutes, `30h` to rotate every 30 hours, `30d` to rotate every 30 days, or `30w` to rotate every 30 weeks.




###get key log
```
curl http://testmyids.com/
```


fast.log by default

```
sudo tail -f /var/log/suricata/eve.json | jq 'select(.event_type=="alert")'
```



###disable rules

生产环境中部署了suricata，日常规则更新使用suricata-update，如果想禁用某些规则，可以在配置文件/etc/suricata/disable.conf中添加，比如：
```
2018959     #禁用规则号 2018959
group:dshield.rules    #禁用组 dshield.rules 
re:heartbledd   #禁用与heartblead相关的规则
```

如ET中误报较高规则在/etc/suricata/rules/下

```
group:stream-events.rules
2200036
```
更新这些文件后，再次重新运行suricata-update：



默认情况下，suricata-update会将所有规则合并到一个文件“/var/lib/suricata/rules/suricata.rules”中。要启用默认禁用的规则，请使用'/etc/suricata/enable.conf':

同样，要禁用规则，请使用/etc/suricata/disable.conf,

更新这些文件后，再次重新运行suricata-update：


>https://www.jianshu.com/p/9458f47bccc1
