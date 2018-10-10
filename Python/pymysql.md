#error on '
```
pymysql.err.ProgrammingError: (1064, u'You have an error in your SQL syntax; che
ck the manual that corresponds to your MariaDB server version for the right synt
ax to use near \'d do was, I\'d pretend I was one of those deaf-mutes. ","level_
info":{"current_le\' at line 1')
```
 
二、python向mysql数据库插入数据时经常会碰到一些特殊字符，如单引号，双引号。

解决办法：
cur.execute(u‘‘‘update table set name = %s where id = %s;‘‘‘ , (name.decode(‘utf-8‘),index))

举例：
name="I‘mHere"
注意： cursor.execute()可以接受一个参数，也可以接受两个参数：
(1) cur.execute("insert into resource(cid,name) values(%s, %s)" , (12,name) );
    这种格式是接受两个参数，MySQLdb会自动替你对字符串进行转义和加引号，不必再自己进行转义，执行完此语句之后，resource表中多了一条记录： 12  I‘mHere
(2) cur.execute("insert into resource(cid,name) values(%s, %s)" % (12,name) );
    这种格式是利用python的字符串格式化自己生成一个query，也就是传给execute一个参数，此时必须自己对字符串转义和增加引号，即上边的语句是错误的，应该修改为：
    name = MySQLdb.escape_string(name);
    cursor.execute("insert into resource(cid,name) values(%s, ‘%s‘)" % (12,name) );
    这样插入的记录才和(1)一样：12 I‘mHere


其中遇到过一些小问题，值得记录一下，以便今后使用的时候注意到。

表格的建立，代码如下：
1
cursor.execute("create table %s(id char(100))" % tb_name)# 这样写可能报错
　　其中tb_name就是自己定的表格名称，在使用过程中发现，当表格名称的字符串中带有'-'的时候（如test-abc），会报错，似乎是'-'在MySQL代码中有特殊意义所以让程序误以为这是一个命令。同样的，如果表格的名称与代码语句重复，例如表格名称就叫做'table',也可能使程序报错，因此可以将表格的名称用两个`括起来（注意，这个点不是单引号，而是tab建上面的那个小撇）。

　　修改代码如下：

1
cursor.execute("create table `%s`(id char(100))" % tb_name)
　　2.信息的插入，代码如下：

1
cursor.execute(”insert into `%s`(id) values('%s')” % (tb_name,data))# 这种可能出错