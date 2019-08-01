#Defense

>https://blog.csdn.net/duke56/article/details/86498558

1、首先生成一个密钥(可在Linux控制台,Windows下有Cygwin64等)安装好gnupg套件后

    $ gpg --gen-keygpg (GnuPG) 1.4.23; Copyright (C) 2015 Free Software Foundation, Inc.This is free software: you are free to change and redistribute it.There is NO WARRANTY, to the extent permitted by law. 请选择您要使用的密钥种类：   (1) RSA and RSA (default)   (2) DSA and Elgamal   (3) DSA (仅用于签名)   (4) RSA (仅用于签名)您的选择？RSA 密钥长度应在 1024 位与 4096 位之间。您想要用多大的密钥尺寸？(2048)4096您所要求的密钥尺寸是 4096 位请设定这把密钥的有效期限。         0 = 密钥永不过期      <n>  = 密钥在 n 天后过期      <n>w = 密钥在 n 周后过期      <n>m = 密钥在 n 月后过期      <n>y = 密钥在 n 年后过期密钥的有效期限是？(0)

**注意:生成GPG Key时所填写的邮箱地址必须跟注册GitHub账号所使用的邮箱相同**

> DSA和RSA的区别（密钥加密算法）
> 
>   
> RSA加密算法是最常用的非对称加密算法，CFCA在证书服务中离不了它。它既能用于加密，也能用于数字签名。
> 
> RSA是目前最有影响力的公钥加密算法，该算法基于一个十分简单的数论事实：将两个大素数相乘十分容易，  
>  但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥，即公钥，而两个大素数组合成私钥。  
>  公钥是可发布的供任何人使用，私钥则为自己所有，供解密之用。  
>     
>  解密者拥有私钥，并且将由私钥计算生成的公钥发布给加密者。  
>  加密都使用公钥进行加密，并将密文发送到解密者，解密者用私钥解密将密文解码为明文。
> 
> -------------------------------------------------------------------------------------
> 
> Digital Signature Algorithm (DSA)是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)
> 
> DSA的一个重要特点是两个素数公开，这样，当使用别人的p和q时，  
>  即使不知道私钥，你也能确认它们是否是随机产生的，还是作了手脚。RSA算法却做不到
> 
> DSA是基于整数有限域离散对数难题的，其安全性与RSA相比差不多
> 
> DSA算法是美国的国家标准数字签名算法，它只能用户数字签名，而不能用户数据加密和密钥交换。  
>  DSA 一般用于数字签名和认证，只用于签名，它比RSA要快很多

2、查看生成的密钥ID,这里的KeyID为886C8FB9

    $ gpg -k/home/kend/.gnupg/pubring.gpg-----------------------------pub   3072D/886C8FB9 2019-01-15        # 3072D为密钥长度和加密类型,886C8FB9才是KeyIDuid                  kend56 (Git签名) <kend56@qq.com>

3、导出公钥

    $ gpg -a -o key.txt --export 886C8FB9    # 导出公钥,公钥可以随便公开,千万别公开私钥

4、用文本编辑器打开导出的key.txt文件,并将里边的内容放GitHub的Settings --- > SSH and GPG keys

![](https://img-blog.csdnimg.cn/20190115211507818.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1a2U1Ng==,size_16,color_FFFFFF,t_70)

5、设置 Git 全局要签名的Key

    # 设置 Git 全局要签名的Keygit config --global user.signingkey 886C8FB9    # 886C8FB9为生成的GPG KeyID # 如果要让当前git项目启用签名验证，使用下面的命令：git config commit.gpgsign true			# 设置：false 为关闭 # 如果要让所有项目都启用签名验证：git config --global commit.gpgsign true		# 设置 Git 全局提交都要签名 # 如果没有设置局部仓库或全局仓库默认开启 commits GPG 验证，提交时可以添加 -S 参数，要求进行 GPG 验证 # Git里填写的提交者信息,必须跟注册的GitHub帐号相同的邮箱,否则会显示 "Unverified"标志git config --global user.email "kend56@qq.com"

6、commit 带签名提交

    $ git commit -am "add"		# 提交[master（根提交） 72064f0] add 1 file changed, 0 insertions(+), 0 deletions(-) create mode 100644 t.txt # 未设置git config --global commit.gpgsign true# 可手动选择带签名提交:git commit -S -am "add" $ git log --show-signature	# 查看带签名的提交commit 72064f01e84f10497664f4a205832abff72b4cc2 (HEAD -> master)gpg: 签名建立于 2019年01月15日,周二 20:43:55 CSTgpg:               使用 DSA 密钥 2CAF2804886C8FB9gpg: 完好的签名，来自于“kend56 (Git签名) <kend56@qq.com>”Author: kend56 <kend56@qq.com>Date:   Tue Jan 15 20:43:54 2019 +0800     add

7、带签名的tag提交

    # -u 后面跟着的就是GPG KeyID$ git tag -u "886C8FB9" -s v0.0.1 -m "说明" $ git show v0.0.1    # 查看带签名tag信息tag v0.0.1Tagger: kend56 <kend56@qq.com>Date:   Tue Jan 15 21:24:50 2019 +0800 说明-----BEGIN PGP SIGNATURE----- iF4EABEIAAYFAlw93yMACgkQLK8oBIhsj7kqRQEAldiUJ0m3i3A30WtrXZsp2rwswrbyIAJd1WXZLxQHp3gA/3FHG7XJ3nFOuvu6lo4K5EVDWyrgNmedffaf9TAay2W0=c5fI-----END PGP SIGNATURE----- commit 72064f01e84f10497664f4a205832abff72b4cc2 (HEAD -> master, tag: v0.0.1)Author: kend56 <kend56@qq.com>Date:   Tue Jan 15 20:43:54 2019 +0800     add diff --git a/t.txt b/t.txtnew file mode 100644index 0000000..e69de29

8、提交后查看,如图(一个使用和注册GitHub相同邮箱,另一个为不同邮箱,所显示的认证标志)

点击认证标志可以看到用户相关内容

![](https://img-blog.csdnimg.cn/20190115213848764.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1a2U1Ng==,size_16,color_FFFFFF,t_70)




#Attack

>https://www.v2ex.com/amp/t/290802

举个栗子， jQuery 之父和 PHP 之父在我的项目里提交了一些好玩的玩意。当然，我对两位充满敬意且无意冒犯。

这些都是真实的提交，并且会被 Github 和基本上所有 git 代码托管网站识别成其用户，只要这个用户存在。

## 安装

    git clone https://github.com/hanbang-wang/FakeGit.git
    cd FakeGit
    python setup.py install

或者使用 pip ：

    pip install fakegit

## 用法

    fakegit <command> [--user] [--help|-h]

FakeGit 会把所有的参数传递给原 git 命令行程序，除了以下的：

    change       永远更改你的本地身份
    recover      快速删除本地 git 配置文件内的 user 关键字
    --help, -h   一个简单的帮助

FakeGit 会拦截 `--user` 和紧接着的一个参数，就是提交者的信息。

### 身份格式

如果你要钦定某个人, 使用 `姓名 <邮箱>` 这样的格式, 比如：

    --user 'John Doe <johndoe@example.com>'

或者你不想填写邮箱地址，留空即可：

    --user 'No Email <>'

我同时也给 Github 用户做了个身份查询，填写用户名即可：

    --user 'example'

### 例子

    fakegit commit -a -m "A example." --user SuperFashi"

程序将会使用 Github API 查询使用相应 id `SuperFashi` 的用户，也就是我，查询我的提交历史和提交记录中的邮箱，生成一个类似于 `SuperFashi <admin@superfashi.com>` 的格式，用这个信息来进行提交。

但是我还是推荐指定用户信息：

    fakegit commit -a -m "A example." --user "SuperFashi <admin@superfashi.com>"

当然你也可以用 FakeGit 执行任何 git 有的命令：

    fakegit push --user "whateveryoulike <>"
    # 和 `git push` 一样

因为只有 `commit` 命令会使用 `user` 关键字，所以都一样。

### 附加

`fakegit change` 会永久更改你的本地身份，所以必须跟着 `--user`，否则会抛出个错误。

`fakegit recover` 是一个快速重置你本地 git 配置文件的小工具，预防你后悔或者想在突然退出程序后恢复。



---
>https://blog.csdn.net/duke56/article/details/86531158

防伪造GitHub提交:[https://blog.csdn.net/duke56/article/details/86498558](https://blog.csdn.net/duke56/article/details/86498558) [供参考]

> **伪造提交过程**

    [dem-PC@dem]$git init已初始化空的 Git 仓库于 /home/dem/dem/.git/[dem-PC@dem]$git remote add origin  git@live86.github.com:live86/dem.git[dem-PC@dem]$[dem-PC@dem]$echo 1 >> tex.txt[dem-PC@dem]$git config user.name dem        # 第一次提交的name和E-mail信息[dem-PC@dem]$git config user.email leichixian@outlook.com[dem-PC@dem]$git add tex.txt[dem-PC@dem]$git commit -am "add 1"[master（根提交） 8ebf4a5] add 1 1 file changed, 1 insertion(+) create mode 100755 tex.txt[dem-PC@dem]$echo 2 >> tex.txt[dem-PC@dem]$git config user.name CSDN.com    # 修改一下第二次提交的name[dem-PC@dem]$git commit -am "add 2"[master d6e5f45] add 2 1 file changed, 1 insertion(+)[dem-PC@dem]$echo 3 >>tex.txt[dem-PC@dem]$git config user.emailleichixian@outlook.com[dem-PC@dem]$git config user.email dqfext@gmail.com    #修改一下第三次提交的E-mail[dem-PC@dem]$git commit -am "add 3"[master a197321] add 3 1 file changed, 1 insertion(+)[dem-PC@dem]$echo 4 >> tex.txt[dem-PC@dem]$git config user.email abcd@abcd.CSDN    # 第四次随便改个没人用的E-mail[dem-PC@dem]$git commit -am "add 4"[master 9aa9113] add 4 1 file changed, 1 insertion(+)[dem-PC@dem]$git push -u origin master -f对象计数中: 12, 完成.Delta compression using up to 8 threads.压缩对象中: 100% (4/4), 完成.写入对象中: 100% (12/12), 830 bytes | 166.00 KiB/s, 完成.Total 12 (delta 0), reused 0 (delta 0)To live86.github.com:live86/dem.git + 37a82aa...9aa9113 master -> master (forced update)分支 'master' 设置为跟踪来自 'origin' 的远程分支 'master'。[dem-PC@dem]$[dem-PC@dem]$git logcommit 9aa911359e47e1f14e19d595c4e85da5847b3f2d (HEAD -> master, origin/master)Author: CSDN.com <abcd@abcd.CSDN>Date:   Thu Jan 17 21:04:08 2019 +0800     add 4 commit a197321f82289b767a25b63a67013150c1858cc6Author: CSDN.com <dqfext@gmail.com>Date:   Thu Jan 17 21:02:13 2019 +0800     add 3 commit d6e5f459fcc0dd84116bf69fc68e0d3252f0ed6eAuthor: CSDN.com <leichixian@outlook.com>Date:   Thu Jan 17 21:00:03 2019 +0800     add 2 commit 8ebf4a58e8239b6eb2810b0c0cf4d2ce5501172bAuthor: dem <leichixian@outlook.com>Date:   Thu Jan 17 20:58:08 2019 +0800     add 1[dem-PC@dem]$ 

> **GitHub上显效的提交**

![](https://img-blog.csdnimg.cn/20190117211926771.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1a2U1Ng==,size_16,color_FFFFFF,t_70)

> 说明
> 
> 第一次所提交的E-mail是网上找到有在GitHub上注册过的邮箱,user.name随便填写的
> 
> 第二次更改了提交者的用户名(user.name)
> 
> 第三次使用的user.email还是找了个在GitHub上注册过的邮箱
> 
> 第四次为随便填写个没有在GitHub上注册过的邮箱

> **总结**
> 
> **GitHub上显示的提交信息是根据提交者在git config user.email 后面所填写的邮箱进行关联的,第二次修改了提交者的作者,仍然显示和第一次提交一样**
> 
> **再没有找到提交者在GitHub上的注册帐号时才会显示提交者所填写的user.name**

> **为了安全起见,最好是使用GitHub官方所使用的GPG keys进行验证签名提交**
> 
>  [GitHub GPG keys 添加签名,进行认证 Verified认证](https://blog.csdn.net/duke56/article/details/86498558)  (如下图所示)

![](https://img-blog.csdnimg.cn/20190117213529240.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1a2U1Ng==,size_16,color_FFFFFF,t_70)