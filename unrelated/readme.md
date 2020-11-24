#贴吧公众号等平台超长刷屏特殊符号是怎么制作的?

![](/pics/screencapture-oicqzone-pc-2020061024958-html-2020-11-24-13_41_35.png)


>http://www.zzyju.com/h/%E9%A3%9E%E9%A3%9E%E9%A3%9E%E0%B8%B6/

```
 ส็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็็侵略的个说ส้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้
```

```
2、我们输入字母 A（ \u0041 ） ，然后输入组合字符圆圈  ̊ （ \u030A ），接着将圆圈重复 100 个（ repeat( 100 ) ）。你也重复更多次。
 
3、打开浏览器，F12进入调试模式，点开console标签。
输入：'\u0041'+'\u030A'.repeat(100)

```



#法线贴图及其原理

>https://blog.csdn.net/puppet_master/article/details/53591167

![](/pics/screencapture-blog-csdn-net-puppet-master-article-details-53591167-2020-11-24-14_06_09.png)



#中文语音克隆

>https://github.com/KuangDD/zhrtvc



#反恶意爬虫趣闻

>看你这是被扫 RDP 了吧，把网站端口当成了远程桌面端口，这种没法跳转的
如果是正常的 http 流量的话可以 Nginx 写规则 301 跳转到你的推广链接去
if (!-e $request_filename) {
return 301 http://你的推广链接 /;
}

>返回GZIP炸弹（压缩大量空白字节）解压占用对方大量内存
blackhat 大会 2016 年就有分享了怎么反制 gzip bomb 了。
举个 Py 例子：
```
import zlib
def decompress(data, maxsize=[ Response Body Size ]):
dec = zlib.decompressobj()
data = dec.decompress(data, maxsize)
if dec.unconsumed_tail:
raise ValueError("Possible bomb") del dec
return data
```



#文本编码为emoji

https://github.com/CaffreySun/OHouEmoji

https://emoji.ohou.ga/