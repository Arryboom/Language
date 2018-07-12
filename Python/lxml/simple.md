# Xpath
```
import sys
from lxml import etree

reload(sys)
sys.setdefaultencoding('utf8')
xf=open("test.txt","r")
#test.txt is html source of baidu.com
#And Fucking baidu garbage generator
htm=xf.read()
xf.close()
fke=etree.HTML(htm)
ec1=fke.xpath("/html/body/div[@id='page']/div[@id='index-card']/div[@class='blank-frame']/div[@id='center-content-1']/div[@class='tab-wrap']/div[@id='tab_news_1']/div[@class='tab-news-content water-container']/div[@class='news-list-wrapper']/div/div[@class='rn-container']/a[@class='rn-tpl2']/div[@class='rn-text-content']/div[@class='rn-h1']")
for e in ec1:
    print e.text
```
Now we get some shitty output like:
>嘉兴接受胡海峰辞去市长请求 毛宏芳为代理市长
男子在北京西站停车56小时被收1115元 管理方:不多
这群绝了！总共50多个人，除了1个受害人，其他全...
悲剧！凌晨河边现男尸，疑电鱼时触电身亡


#To generate xpath rule ,simple install xpath helper on chrome.