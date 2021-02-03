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





#视频流

树莓派使用 ffmpeg+rtmp 推流直播，延迟严重



#爬虫练习网站

https://www.yuanrenxue.com/crawler/three-state-of-crawler.html

| 16 | webpack - 初体验新     | 简单   | js加密 |  |
| -- | -------------------------- | -------- | ------- |  |
| 17 | 散而后擒-兵不血刃新 | 非常困难 | js加密 |  |
| 1  | js 混淆 - 源码乱码赛 | 简单   | js加密 |  |
| 2  | js 混淆 - 动态cookie 1赛 | 中等   | js加密 |  |
| 3  | 访问逻辑 - 推心置腹赛 | 简单   | 骚操作 |  |
| 4  | 雪碧图、样式干扰赛 | 简单   | css加密 |  |
| 5  | js 混淆 - 乱码增强赛 | 中等   | js加密 |  |
| 6  | js 混淆 - 回溯赛      | 中等   | js加密 |  |
| 7  | 动态字体，随风漂移赛 | 中等   | css加密 |  |
| 8  | 验证码 - 图文点选赛 | 困难   | 验证码 |  |
| 9  | js 混淆 - 动态cookie 2赛 | 困难   | js加密 |  |
| 9  | js 混淆 - js重放攻击对抗 2赛 | 非常困难   | js加密 |  |
| 11 | app抓取 - so文件协议破解赛 | 中等   | 安卓  |  |
| 12 | 入门级js                | 非常简单 | js加密 |  |
| 13 | 入门级cookie            | 非常简单 | js加密 |  |
| 14 | 备而后动-勿使有变  | 困难   | js加密 |  |
| 15 | 备周则意怠-常见则不疑 | 简单   | 骚操作 |  |




#国产多人协同电子表格

Github: https://mengshukeji.github.io/LuckysheetDocs/
Demo: https://mengshukeji.github.io/LuckysheetDemo/

国产电子表格 Luckysheet 后台也开源了！支持在线协作，一键 docker 私有部署

Luckysheet - https://github.com/mengshukeji/Luckysheet （求个 Star[手动狗头]）
Luckysheet 后台 - https://github.com/mengshukeji/LuckysheetServer
Luckysheet 一键 docker 部署 - https://github.com/mengshukeji/LuckysheetServerStarter


#像 VMOS 这类能够在 Android 手机上面运行其他版本的 Android 系统,技术原理是什么?

>https://www.v2ex.com/t/745210#reply7

>这玩意就是自己实现了一个中间层，相当于被模拟系统的 framework，然后隔离开系统自身的 framework 。讲原理没啥难度，写起来巨麻烦，到处都是坑。
>这个大概就是自己实现了 HAL，在一个容器里运行单独的 system image，和宿主机共享内核。文档可以看 aosp 的文档，实现可以参考 anbox
https://source.android.com/devices/architecture

>@Jirajine 我刚开始也以为是 Anbox,但是后来我发现它内核和宿主机都不是同一个...
>@rust 不可能吧，内核不同的话就是虚拟化了，用户态的虚拟化无论实现难度还是性能都不太可能达到这样的效果。
>proot 容器
>所以我才觉得神奇,虽然 arm64-v8a 支持硬件虚拟化,但是这得 root 权限啊.它这个都没有 root 权限就可以安装使用了.然后虚拟出来的系统还挺流畅...
>内核应该是共享的，你看到的不一样可能是接口修改了数值。
>还有一个叫光速虚拟机，声称比 VMOS 快，它在自己的博客上谢了一篇文章《安卓实现安卓-光速虚拟机技术内幕》
https://gsxnj.cn/vphonegaga_blog/archives/2020/07/15/dingding/454
反正我是看不懂，你可以看看
虽然 uname 之类途径看到的好像是不一样的版本，但是通过在 anbox 内拿到 root 之后试图干一些对内核的特权操作（比如操纵 SELinux ）会 denied，所以应该还是同一个内核，只是 hook 掉了一些调用。