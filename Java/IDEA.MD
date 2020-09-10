#IDEA中git远程版本回退
>https://www.cnblogs.com/renmengkai/p/9881721.html

![](/pic/screencapture-cnblogs-renmengkai-p-9881721-html-2020-09-10-11_36_15.png)




#Lombok使用与原理

>https://www.cnblogs.com/renmengkai/p/9881932.html


![](/pic/screencapture-cnblogs-renmengkai-p-9881932-html-2020-09-10-11_37_30.png)



#Mybatislog


>https://github.com/Link-Kou/intellij-mybaitslog

Mybatislog是基于IntelliJ 开发的项目，用来格式化输出Mybatis的Sql。

```
--  1  2020.04.10 23:30:19 CST DEBUG com.cms.dao.ProductTypeConfigTitleDao.queryAll - ==>
select f_id, f_name, f_preId, f_type, createtime, updatedtime
 FROM cms.t_product_type_config_title
 WHERE f_type = 2;
------------------------------------------------------------------------------------------------------------------------
--  2  2020.04.10 23:30:20 CST DEBUG com.cms.dao.ProductTypeConfigGroupDao.queryAll - ==>
select f_id, f_titleId, f_name, f_preId, f_type, createtime, updatedtime
 FROM cms.t_product_type_config_group
 WHERE f_type = 2;
------------------------------------------------------------------------------------------------------------------------
--  3  2020.04.10 23:30:20 CST DEBUG com.cms.dao.ProductTypeConfigItemDao.queryAll - ==>
select f_id, f_groupId, f_preId, f_name, f_type, createtime, updatedtime
 FROM cms.t_product_type_config_item
 WHERE f_type = 2;
------------------------------------------------------------------------------------------------------------------------
```