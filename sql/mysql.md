# 重置自增字段 reset autoincrement
```
alter table TABLENAME AUTO_INCREMENT=123456;
```
>但是经过实际测试， 单机的Mysql没有问题， Mysql Cluster下是无效的，可能在主键上的机制，还是有所不同，有时间研究一下


#运维不小心赋予了某开发账号所有的权限，怎么恢复之前的权限


在开发测试用的服务器上 mysql
```
GRANT ALL PRIVILEGES ON *.* TO '开发账号'@'%';
```
这个开发账号，相当于变成了 root 账号了。

短时间内应该不会出现问题，但这个会留下很大隐患！该怎么补救。




谢谢大家，问题已解决

误操作：
给某开发账号授予所有库所有特权
GRANT ALL PRIVILEGES ON *.* TO '开发账号'@'%';

补救措施：
可以单独把这个权限收回来，不影响原本已经有的权限
REVOKE ALL PRIVILEGES ON *.* FROM '开发账号' @'%';


