#sqlite memory backend save as file

```
#encoding=utf-8
# 打开sqlite3内存数据库，执行操作，将内存数据库保存到文件

import sqlite3
from io import StringIO
import os

#使用:memory:标识打开的是内存数据库
con = sqlite3.connect(":memory:")
cur = con.cursor()
#使用executescript可以执行多个脚本
cur.executescript("""
    create table quotes(
        rid INTEGER PRIMARY KEY AUTOINCREMENT,
        code char(10) NOT NULL,
        high real,
        open real,
        low real,
        close real,
        amount real,
        volume real)""")


#execute执行脚本，参数要放到元组中
cur.execute('insert into quotes(code,high,open,low,close,amount,volume) values(?,?,?,?,?,?,?)',
            ('600036',12.0,11.8,11.7,11.9,999999,8999))

#打印数据表数据
cur.execute("select * from quotes")
print (cur.fetchall())

#生成内存数据库脚本
str_buffer = StringIO()
#con.itrdump() dump all sqls
for line in con.iterdump():
    str_buffer.write('%s\n' % line)
    print("line: "+line)

#关闭内存数据库

cur.close()

try:
    os.remove("quotes.db3")
except Exception as e:
    print(e)
#打开文件数据库
con_file = sqlite3.connect('quotes.db3')
cur_file = con_file.cursor()
#执行内存数据库脚本
cur_file.executescript(str_buffer.getvalue())
#关闭文件数据库
cur_file.execute("select * from quotes")
print(cur_file.fetchall())
cur_file.close()

```



---

- another approaching:

```
import sqlite3

def getTableDump(db_file, table_to_dump):
    conn = sqlite3.connect(':memory:')    
    cu = conn.cursor()
    cu.execute("attach database '" + db_file + "' as attached_db")
    cu.execute("select sql from attached_db.sqlite_master "
               "where type='table' and name='" + table_to_dump + "'")
    sql_create_table = cu.fetchone()[0]
    cu.execute(sql_create_table);
    cu.execute("insert into " + table_to_dump +
               " select * from attached_db." + table_to_dump)
    conn.commit()
    cu.execute("detach database attached_db")
    return "\n".join(conn.iterdump())

TABLE_TO_DUMP = 'table_to_dump'
DB_FILE = 'db_file'

print getTableDump(DB_FILE, TABLE_TO_DUMP)
```

>https://www.icode9.com/content-2-334154.html