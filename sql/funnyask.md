#有没有什么数据库适合量大、更新频繁、字段变动频繁的数据？

>https://www.v2ex.com/t/705343#reply34


上亿的数据，两周更新一轮，每天还有几万条新数据入库，这种数据用啥库比较好维护啊
还需要全文搜索


>目前是 hbase+es 二级索引，维护起来太累了


>8 万一台的 dell R740 双 E5，512G 内存，ssd 和万转 sas，加上 Microsoft sqlserver 5 台做 cluster 。授权费一台加 Windows 也就 5 万。
再上去就是 oracle 的
别看千多，阿里的 DRDS 比这还贵


>sqlserver 能用好就很不错了。
SQLServer2005：
Database size 1,048,516 TB
Databases per instance of SQL Server 32,767
Filegroups per database 32,767 （ SQL2000 是 256 ）
Files per database 32,767
File size (data) 16 TB （ SQL2000 是 32TB ）
File size (log) 2 TB （ SQL2000 是 4TB ）
SQL2012 设计极限：
524 PB(536,576TB/549,453,824G)




>http://oncedb.com
基于 redis 二次开发的的全文搜索数据库


>Hudi
https://hudi.apache.org/


>大量的 update 会不会出现什么问题?有什么问题，没什么问题，无非磁盘寿命用的快点，SSD 只做 CACHE 让 SAS 硬盘做数据写入，Microsoft sql server 水平扩展非常方便。


>上亿：几乎所有数据库都没问题。
每天几万条入库：几乎所有数据库都没问题。
全文搜索：似乎 nosql 不行。
字段频繁更新：似乎 mysql 不行。
所以。。oracle 硬钢？


> sqlserver 你查下就知道了，毫无压力，每秒几万个事物都没啥压力


>update 和 insert 应该都算在入库里，你这个不该算每天几万条新数据，应该说每天几千万新数据。。当然这个压力 oracle 肯定也是没问题的，一千 qps 在 oracle 设计范围内，很轻松可以搞定，相当于每小时 360 万呗。当然我也很好奇频繁改字段该用什么数据库，我想象不到什么业务需要频繁修改字段的，没做过


---



#上线之后数据库 CPU 升高,找不到原因

>https://www.v2ex.com/t/709301#reply5

数据库使用的是 aws 的 rds;

上线后从它的监控面板看到 cpu 使用率大概增加了一倍 做了些工作,但是还是找不到原因

说一下自己已经做过的工作:

排查了慢查询,正常,和上线前差不多
数据库的连接数方差变大(变大 20-30%),其他监控曲线波动不大(如 IOPS,网络的 I/O,内存和队列等)
检查了业务的服务器监控,曲线也是稳定的
用 show processlist 看了下没有什么毛病
有大佬能提供一下排查思路吗?感激不尽

---

top 查看 mysql 哪个线程 CPU 使用率高，然后 select * from performance_schema.threads where THREAD_OS_ID=xxx;


更改了 SQL 语句吗？
可能是由于某些计算量大的聚合操作导致的，比如说 max min


如果是 rds 的话可能只能用点巧劲儿，把所有的 SQL 日志都打出来，分析下，(执行频率高 and 执行时间很长 )做一个并集，就是这个 SQL，再去找属于哪个应用和业务