#server

1.main rpm
```
rpm --import https://packages.wazuh.com/key/GPG-KEY-WAZUH

cat > /etc/yum.repos.d/wazuh.repo <<\EOF
[wazuh_repo]
gpgcheck=1
gpgkey=https://packages.wazuh.com/key/GPG-KEY-WAZUH
enabled=1
name=Wazuh repository
baseurl=https://packages.wazuh.com/3.x/yum/
protect=1
EOF
```
2.manager
```
yum install wazuh-manager
service start wazuh-manager 
yum install nodejs
yum install wazuh-api
service start wazuh-api
```
3.filebeat
```
rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch

cat > /etc/yum.repos.d/elastic.repo << EOF
[elasticsearch-7.x]
name=Elasticsearch repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
EOF

yum install filebeat-7.7.0

curl -so /etc/filebeat/filebeat.yml https://raw.githubusercontent.com/wazuh/wazuh/v3.12.3/extensions/filebeat/7.x/filebeat.yml

chmod go+r /etc/filebeat/filebeat.yml

curl -so /etc/filebeat/wazuh-template.json https://raw.githubusercontent.com/wazuh/wazuh/v3.12.3/extensions/elasticsearch/7.x/wazuh-template.json

chmod go+r /etc/filebeat/wazuh-template.json

curl -s https://packages.wazuh.com/3.x/filebeat/wazuh-filebeat-0.1.tar.gz | sudo tar -xvz -C /usr/share/filebeat/module


```
6. Edit the file `/etc/filebeat/filebeat.yml` and replace `YOUR_ELASTIC_SERVER_IP` with the IP address or the hostname of the Elasticsearch server. For example:

> Copied to clipboard
> 
> output.elasticsearch.hosts: \['http://YOUR\_ELASTIC\_SERVER\_IP:9200'\]


```
systemctl enable filebeat.service
systemctl start filebeat.service
```


##elk


```
rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch

cat > /etc/yum.repos.d/elastic.repo << EOF
[elasticsearch-7.x]
name=Elasticsearch repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
EOF

yum install elasticsearch-7.7.0
```

- Elasticsearch will only listen on the loopback interface (localhost) by default. Configure Elasticsearch to listen to a non-loopback address by editing the file `/etc/elasticsearch/elasticsearch.yml` and uncommenting the setting `network.host`. Change the value to the IP you want to bind it to:
    
    
    network.host: <elasticsearch\_ip>


---
- Further configuration will be necessary after changing the `network.host` option. Add or edit (if commented) the following lines in the file `/etc/elasticsearch/elasticsearch.yml`:
    
    
    node.name: <node\_name>
    cluster.initial\_master\_nodes: \["<node\_name>"\]
---

For Systemd:
```
systemctl daemon-reload
systemctl enable elasticsearch.service
systemctl start elasticsearch.service
```

---
```
filebeat setup --index-management -E setup.template.json.enabled=false
```


##kibana

```
yum install kibana-7.7.0

cd /usr/share/kibana/

sudo -u kibana bin/kibana-plugin install https://packages.wazuh.com/wazuhapp/wazuhapp-3.12.3_7.7.0.zip
```

3. Kibana will only listen on the loopback interface (localhost) by default, which means that it can only be accessed from the same machine. To access Kibana from the outside, make it listen on its network interface IP by editing the file `/etc/kibana/kibana.yml`, uncomment the setting `server.host`, and change the value to:

```
> server.host: "<kibana\_ip>"
```

---

4. Set the URL or the IP of the Elasticsearch node by editing the file `/etc/kibana/kibana.yml`:

```
> elasticsearch.hosts: \["http://<elasticsearch\_ip>:9200"\]
```
---

5. For installations on Kibana 7.6.X versions it is recommended to increase the heap size of Kibana to ensure the Kibana’s plugins installation:

```
cat >> /etc/default/kibana << EOF
NODE_OPTIONS="--max_old_space_size=2048"
EOF
```

```
systemctl daemon-reload
systemctl enable kibana.service
systemctl start kibana.service
```


#agent