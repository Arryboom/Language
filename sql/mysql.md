# 重置自增字段 reset autoincrement
```
alter table TABLENAME AUTO_INCREMENT=123456;
```
>但是经过实际测试， 单机的Mysql没有问题， Mysql Cluster下是无效的，可能在主键上的机制，还是有所不同，有时间研究一下