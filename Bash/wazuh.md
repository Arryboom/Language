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
>https://www.elastic.co/guide/en/beats/filebeat/6.8/beats-basic-auth.html



well,follow this doc will be current great,alzo it's not very careful on process some detail.
>https://documentation.wazuh.com/3.12/installation-guide/installing-elastic-stack/protect-installation/xpack.html

##port
on manager,
```
1514/udp remotelogd
1515/tcp registerd
```
by default is nessary to open.


