#Using Phantomjs capture screen doesn't show chinese charactor



我在centos上安装的phantomjs，然后截图的时候，中文不显示，于是安装中文字体，最后貌似没有任何反映；于是又搜到网上说要安装所有的字体`yum -y install *-fonts-*`，安装完之后貌似是好了，但是中文的大小不一很难看，于是我又删掉了这些字体；最后发现只要安装了Arial字体就正常了。
```
# 拷贝字体到 /usr/share/fonts 或者 在下面建立一个目录（便于管理）
mkfontscale
mkfontdir
fc-cache -fv
```
至此，中文的问题就没了。但貌似效果还是没有mac或者pc上面的好。