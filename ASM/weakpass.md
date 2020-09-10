medusa -h ip -u user -P password -M ssh/ftp/telnet/http 支持很多服务破解

-H target_ip  list  批量扫弱口令

补充：

hydra 针对单个ip 实施爆破，速度比medusa要快好多

hydra 用法

hydra -l 用户名 -P password ip ssh 

-L  用户名列表

-e ns  尝试空口令和密码相同的密码 

-vV 显示破解详细过程  

2014/10/23 新加

hydra 用法

-l 用户名

-L 用户名列表

-P 密码列表

-M ip列表

hydra比medusa 更快，支持的服务更多