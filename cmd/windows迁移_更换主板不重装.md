进Pe用SkyIAR 重新安装下磁盘驱动
或是进BIOS把硬盘的AHCI改成IDE试试

SKYIAR 2.0选中右侧的清理目标系统IAR驱动和PNP驱动试一试。

skyiar清理磁盘驱动，可以用天意PE。
如果不行，在加上syspreg重新配置

再不行，UTBOOTAutoFix v2.6.0修复下启动

离线驱动处理工具。

---
1.skyiar老版叫skysrs

2.win10本身就准备了PnPUtil命令和PnPUtil.exe程序给我们使用。

经过了这么多期的培训，大家都会在管理员模式下启动命里提示符了吧？

想详细了解PnPUtil的详细用法，输入pnputil /?查看即可，会详细列出可执行命令行

建议想删除旧驱动的童鞋，使用DriverStore Explorer这款基于PnPUtil命令图形化的小工具，只要运行它就能直接显示在此系统中所有已安装的驱动程序和保存的旧版驱动，按着后面的版本号勾选（需要以管理员模式运行），执行删除操作就行了，DriverStore文件夹能一下子就瘦下来。

DriverStore Explorer v1.0正式版

3.万能驱动工具