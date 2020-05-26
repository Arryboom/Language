>https://www.elastic.co/guide/en/elasticsearch/reference/7.7/security-settings.html


after installed xpack


1.enable xpack security feature in elastic.yml(/etc/elasticsearch/)


```
xpack.security.enabled: true
```

Set to true to enable Elasticsearch security features on the node.
2.enable auto index
Some commercial features automatically create indices within Elasticsearch. By default, Elasticsearch is configured to allow automatic index creation, and no additional steps are required. However, if you have disabled automatic index creation in Elasticsearch, you must configure [`action.auto_create_index`](docs-index_.html#index-creation "Create indices automatically") in `elasticsearch.yml` to allow the commercial features to create the following indices:


```
action.auto_create_index: .monitoring*,.watches,.triggered_watches,.watcher-history*,.ml*
```
