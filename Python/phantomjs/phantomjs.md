1.get http://phantomjs.org/download.html phantomjs.exe,and configuration your system environment variable.  
2.install selenium
```
from selenium import webdriver
driver = webdriver.PhantomJS()
driver.get("https://www.yahoo.com/")
data = driver.title
print data
driver.quit()
```


3.Fucking "UserWarning: Selenium support for PhantomJS has been deprecated, please use headless versions of Chrome or Firefox instead warnings.warn('Selenium support for PhantomJS has been deprecated, please use headless '"

Actually here I checked all comments on selenium,from comments of each version I got that 3.5.3 release should be the last version before deprecate support for phantomjs,but 3.5.0 is to slow to init,I don't know it's due to a known bug or something I wrong,so tested 3.6.0 - 3.8.0 too ,found that all these version no deprecated tips,and 3.7.0 was the most fast one.
https://github.com/SeleniumHQ/selenium/releases/tag/selenium-3.8.0
https://github.com/SeleniumHQ/selenium/releases/tag/selenium-3.5.3
https://github.com/SeleniumHQ/selenium/archive/selenium-3.7.0.zip

Some people say that simple change selenium to 2.48.0,well that depend on you.

pip show selenium默认安装的是3.8.1，将此版本卸载之后重新pip install selenium==2.48.0



# Get Page Source
1.
```
html = driver.execute_script("return document.documentElement.outerHTML")
```
2.
```
from selenium import webdriver
browser = webdriver.Firefox()
browser.get("http://www.baidu.com")
html_source = browser.page_source
```

# Change UA
```
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
 
dcap = dict(DesiredCapabilities.PHANTOMJS)  #设置userAgent
dcap["phantomjs.page.settings.userAgent"] = ("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0 ")
 
obj = webdriver.PhantomJS(executable_path='C:\Python27\Scripts\phantomjs.exe',desired_capabilities=dcap) #加载网址
obj.get('http://wap.95533pc.com')#打开网址
obj.save_screenshot("1.png")   #截图保存
obj.quit()  # 关闭浏览器。当出现异常时记得在任务浏览器中关闭PhantomJS，因为会有多个PhantomJS在运行状态，影响电脑性能
```


# SetTimeout
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
obj.set_page_load_timeout(5)
try:
    obj.get('http://www.xiaohuar.com')
    print obj.title
except Exception as e:
    print e
```

# meta locate
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
obj.set_page_load_timeout(5)
try:
    obj.get('http://www.baidu.com')
    obj.find_element_by_id('kw')                    #通过ID定位
    obj.find_element_by_class_name('s_ipt')         #通过class属性定位
    obj.find_element_by_name('wd')                  #通过标签name属性定位
    obj.find_element_by_tag_name('input')           #通过标签属性定位
    obj.find_element_by_css_selector('#kw')         #通过css方式定位
    obj.find_element_by_xpath("//input[@id='kw']")  #通过xpath方式定位
    obj.find_element_by_link_text("贴吧")           #通过xpath方式定位
 
    print obj.find_element_by_id('kw').tag_name   #获取标签的类型
except Exception as e:
    print e　　
```

# maximize_window
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
obj.set_page_load_timeout(5)
obj.maximize_window()  #设置全屏
try:
    obj.get('http://www.baidu.com')
    obj.save_screenshot('11.png')  # 截取全屏，并保存
except Exception as e:
    print e
```

# Set width,length of broswer
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
obj.set_page_load_timeout(5)
obj.set_window_size('480','800') #设置浏览器宽480，高800
try:
    obj.get('http://www.baidu.com')
    obj.save_screenshot('12.png')  # 截取全屏，并保存
except Exception as e:
    print e
```


# Back history
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
try:
    obj.get('http://www.baidu.com')   #访问百度首页
    obj.save_screenshot('1.png')
    obj.get('http://www.sina.com.cn') #访问新浪首页
    obj.save_screenshot('2.png')
    obj.back()                           #回退到百度首页
    obj.save_screenshot('3.png')
    obj.forward()                        #前进到新浪首页
    obj.save_screenshot('4.png')
except Exception as e:
    print e
```

# Submit form
```
from selenium import webdriver
obj = webdriver.PhantomJS(executable_path="D:\Python27\Scripts\phantomjs.exe")
obj.set_page_load_timeout(5)
try:
    obj.get('http://www.baidu.com')
    print obj.find_element_by_id("cp").text  # 获取元素的文本信息
    obj.find_element_by_id('kw').clear()              #用于清除输入框的内容
    obj.find_element_by_id('kw').send_keys('Hello')  #在输入框内输入Hello
    obj.find_element_by_id('su').click()              #用于点击按钮
    obj.find_element_by_id('su').submit()             #用于提交表单内容
 
except Exception as e:
    print e
```

# scroll to bottom
```
driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
```
You may need sleep for a little time to wait page load.
```
from selenium import webdriver
import time
import re
from selenium.webdriver import DesiredCapabilities
class Get_Messge_From_Phantomjs(object):
    def __init__(self,url):
        self.url = url
        self.headers =  {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
        'Accept-Language': 'zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3',
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.4882.400 QQBrowser/9.7.13059.400',
    }
        self.driver = webdriver.PhantomJS()
        # 为phantomjs添加请求头
        cap = DesiredCapabilities.PHANTOMJS.copy()
        for key, value in self.headers.items():
            cap['phantomjs.page.customHeaders.{}'.format(key)] = value
        # 不载入图片，爬页面速度会快很多
        cap["phantomjs.page.settings.loadImages"] = False


    def request(self):
        print('开始网页get请求')
        self.driver.get(self.url)
        self.scroll_down(driver=self.driver, times=2)  # 执行网页下拉到底部操作，执行5次
        # 提取所有文章标签
        self.parse(self.driver.page_source)
    
    def parse(self,content):
        datas = re.findall(r'http://news.mydrivers.com/\d+/\d+/\d+.htm',content)
        print(datas)
    
    def scroll_down(self,driver, times):
        for i in range(times):
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")  # 执行JavaScript实现网页下拉倒底部
            # 保存截图
             driver.save_screenshot(str(i + 1) + '.png')
            time.sleep(2)
if __name__ == '__main__':
    test = Get_Messge_From_Phantomjs("http://news.mydrivers.com/")
    test.request()
```


>http://www.cnblogs.com/luxiaojun/p/6144748.html
>http://www.cnblogs.com/fnng/p/3160606.html
>https://www.jianshu.com/p/9d408e21dc3a
>https://blog.csdn.net/xundh/article/details/74114974