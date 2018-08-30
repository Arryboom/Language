#UnicodeEncodeError: 'gbk' codec can't encode character '\xXX' in position XX

从网上抓了一些字节流，想打印出来结果发生了一下错误：

UnicodeEncodeError: 'gbk' codec can't encode character '\xbb' in position 8530: illegal multibyte sequence

```
import urllib.request
res=urllib.request.urlopen('http://www.baidu.com')
htmlBytes=res.read()
print(htmlBytes.decode('utf-8'))
```

错误信息让人很困惑，为什么用的是'utf-8'解码，错误信息却提示'gbk'错误呢？

 
不仅如此，从百度首页的html中发现以下代码：

```
<meta http-equiv="content-type" content="text/html;charset=utf-8">
```

这说明网页的确用的是utf-8，为什么会出现Error呢？

 

在python3里，有几点关于编码的常识

1.字符就是unicode字符，字符串就是unicode字符数组

如果用以下代码测试，
```
print('a'=='\u0061')
```
会发现结果为True，足以说明两者的等价关系。

 

2.str转bytes叫encode，bytes转str叫decode，如上面的代码就是将抓到的字节流给decode成unicode数组

我根据上面的错误信息分析了字节流中出现\xbb的地方，发现有个\xc2\xbb的特殊字符»，我怀疑是它无法被解码。

用以下代码测试后
```
print(b'\xc2\xbb'.decode('utf-8'))
```
它果然报错了:UnicodeEncodeError\: 'gbk' codec can't encode character '\xbb' in position 0: illegal multibyte sequence

上网找了下utf-8编码表，发现的确特殊字符»的utf-8形式就是c2bb,unicode是'\u00bb'，为什么无法解码呢。。。

仔细看看错误信息，它提示'gbk'无法encode，但是我的代码是utf-8无法decode，压根牛头不对马嘴，终于让我怀疑是print函数出错了。。于是立即有了以下的测试
```
print('\u00bb')
```
结果报错了：UnicodeEncodeError: 'gbk' codec can't encode character '\xbb' in position 0: illegal multibyte sequence

 

原来是print()函数自身有限制，不能完全打印所有的unicode字符。

知道原因后，google了一下解决方法，其实print()函数的局限就是Python默认编码的局限，因为系统是win7的，python的默认编码不是'utf-8',改一下python的默认编码成'utf-8'就行了

```
import io
import sys
import urllib.request
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf8') #改变标准输出的默认编码
res=urllib.request.urlopen('http://www.baidu.com')
htmlBytes=res.read()
print(htmlBytes.decode('utf-8'))
```
>works on python3
>https://stackoverflow.com/questions/34447623/wrap-an-open-stream-with-io-textiowrapper/34511829

运行后不报错了，但是居然有好多乱码（英文显示正常，中文则显示乱码）！！又一阵折腾后发现是控制台的问题，具体来说就是我在cmd下运行该脚本会有乱码，而在IDLE下运行却很正常。

由此我推测是cmd不能很好地兼容utf8，而IDLE就可以，甚至在IDLE下运行，连“改变标准输出的默认编码”都不用，因为它默认就是utf8。如果一定要在cmd下运行，那就改一下编码，比如我换成“gb18030”，就能正常显示了：
```
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='gb18030')         
#改变标准输出的默认编码
```
最后，附上一些常用的和中文有关的编码的名称，分别赋值给encoding，就可以看到不同的效果了：

| 编码名称 | 用途 |
| --- | ---|
| utf8 | 所有语言 |
| gbk | 简体中文 |
| gb2312 | 简体中文 |
| gb18030 | 简体中文 |
| big5 | 繁体中文 |
| big5hkscs	| 繁体中文 |


| 左对齐标题 | 右对齐标题 | 居中对齐标题 |
| :------| ------: | :------: |
| 短文本 | 中等文本 | 稍微长一点的文本 |
| 稍微长一点的文本 | 短文本 | 中等文本 |



#Utf8

```
coding:utf-8
import sys 
reload(sys) 
sys.setdefaultencoding(‘utf-8’) 
```



#Detect and Convert Charset
```
>>> s = '水壶'
>>> s
18: '\xe6\xb0\xb4\xe5\xa3\xb6'
>>> print unicode(s, 'big5')
瘗游ㄥ
>>> print unicode(s, 'gbk')
姘村6
>>> print unicode(s, 'gb2312')
姘村6
>>> print unicode(s, 'utf-8')
水壶
```
```
a = '你好'
b = 'python'
print a.decode('utf-8').encode('gbk')##decode方法把字符串转换为unicode对象，然后通过encode方法转换为指定的编码字符串对象
print b.decode('utf-8')##decode方法把字符串转换为unicode对象
```

判断字符串编码

使用 chardet 可以很方便的实现字符串/文件的编码检测。尤其是中文网页，有的页面使用GBK/GB2312，有的使用UTF8，如果你需要去爬一些页面，知道网页编码很重要

函数返回值为字典，有2个元素，一个是检测的可信度，另外一个就是检测到的编码。


编码转换

先把其他编码转换为unicode再转换其他编码, 如utf-8转换为gb2312
```
>>> import chardet
>>> str = "我们"
>>> print(chardet.detect(str))
{'confidence': 0.7525, 'encoding': 'utf-8'}

>>> str1 = str.decode('utf-8')

>>> str2 = str1.encode('gb2312')
>>> print(chardet.detect(str2))
{'confidence': 0.8095977270813678, 'encoding': 'TIS-620'}
```