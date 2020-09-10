#async

>https://www.cnblogs.com/Summer-skr--blog/p/11486634.html


## **一、****背景**

 之前爬虫使用的是requests+多线程/多进程，后来随着前几天的深入了解，才发现，对于爬虫来说，真正的瓶颈并不是CPU的处理速度，**而是对于网页抓取时候的往返时间**，因为如果采用requests+多线程/多进程，他本身是阻塞式的编程，所以时间都花费在了等待网页结果的返回和对爬取到的数据的写入上面。而如果采用非阻塞编程，那么就没有这个困扰。这边首先要理解一下阻塞和非阻塞的区别。

（1）阻塞调用是指调用结果返回之前，当前线程会被挂起（线程进入非可执行状态，在这个状态下，CPU不会给线程分配时间片，即线程暂停运行）。函数只有在得到结果之后才会返回。

（2）对于非阻塞则不会挂起，直接执行接下去的程序，返回结果后再回来处理返回值。

 其实爬虫的本质就是client发请求，批量获取server的响应数据，如果我们有多个url待爬取，只用一个线程且采用串行的方式执行，那只能等待爬取一个结束后才能继续下一个，效率会非常低。需要强调的是：对于单线程下串行N个任务，并不完全等同于低效，如果这N个任务都是纯计算的任务，那么该线程对cpu的利用率仍然会很高，之所以单线程下串行多个爬虫任务低效，是因为爬虫任务是明显的**IO密集型（阻塞）**程序。那么该如何提高爬取性能呢？

## **二、****基本概念**

**2.1 阻塞**

阻塞状态指程序未得到所需计算资源时被挂起的状态。**程序在等待某个操作完成期间，自身无法继续干别的事情**，则称该程序在该操作上是阻塞的。

常见的阻塞形式有：网络 I/O 阻塞、磁盘 I/O 阻塞、用户输入阻塞等。阻塞是无处不在的，包括 CPU 切换上下文时，所有的进程都无法真正干事情，它们也会被阻塞。如果是多核 CPU 则正在执行上下文切换操作的核不可被利用。

**2.2 非阻塞**

**程序在等待某操作过程中，自身不被阻塞，可以继续运行干别的事情，则称该程序在该操作上是非阻塞的。**

非阻塞并不是在任何程序级别、任何情况下都可以存在的。仅当程序封装的级别可以囊括独立的子程序单元时，它才可能存在非阻塞状态。

非阻塞的存在是因为阻塞存在，正因为某个操作阻塞导致的耗时与效率低下，我们才要把它变成非阻塞的。

**2.3 同步**

不同程序单元为了完成某个任务，在执行过程中需靠某种通信方式以协调一致，称这些程序单元是同步执行的。例如购物系统中更新商品库存，需要用“行锁”作为通信信号，让不同的更新请求强制排队顺序执行，那更新库存的操作是同步的。简言之，**同步意味着有序。**

**2.4 异步**

为完成某个任务，不同程序单元之间过程中无需通信协调，也能完成任务的方式，不相关的程序单元之间可以是异步的。例如，爬虫下载网页。调度程序调用下载程序后，即可调度其他任务，而无需与该下载任务保持通信以协调行为。不同网页的下载、保存等操作都是无关的，也无需相互通知协调。这些异步操作的完成时刻并不确定。简言之，异步意味着无序。

**2.5 多进程**

多进程就是利用 CPU 的多核优势，在同一时间并行地执行多个任务，可以大大提高执行效率。

**2.6 协程**

协程，英文叫做 Coroutine，又称微线程，纤程，协程是一种用户态的轻量级线程。

协程拥有自己的寄存器上下文和栈。协程调度切换时，将寄存器上下文和栈保存到其他地方，在切回来的时候，恢复先前保存的寄存器上下文和栈。因此协程能保留上一次调用时的状态，即所有局部状态的一个特定组合，每次过程重入时，就相当于进入上一次调用的状态。

协程本质上是个单进程，协程相对于多进程来说，无需线程上下文切换的开销，无需原子操作锁定及同步的开销，编程模型也非常简单。

我们可以使用协程来实现异步操作，比如在网络爬虫场景下，**我们发出一个请求之后，需要等待一定的时间才能得到响应，但其实在这个等待过程中，程序可以干许多其他的事情，等到响应得到之后才切换回来继续处理，这样可以充分利用 CPU 和其他资源，这就是异步协程的优势。**

## **三、****分析处理** 

 同步调用：即提交一个任务后就在原地等待任务结束，等到拿到任务的结果后再继续下一行代码，效率低

```
import requests

def get_page(url):
    print('下载 %s' %url)
    response=requests.get(url)
    if response.status_code == 200:
        return response.text

def parse_page(res):
    print('解析 %s' %(len(res)))


def main():
    urls=['https://www.baidu.com/','http://www.sina.com.cn/','https://www.python.org']
    for url in urls:
        res=get_page(url)                         #调用一个任务，就在原地等待任务结束拿到结果后才继续往后执行
        parse_page(res)

if __name__ == "__main__":
    main()
```

**![](https://img2018.cnblogs.com/blog/1518468/201909/1518468-20190908155525884-1971985041.png)**

**a. 解决同步调用方案之多线程/多进程**

好处：在服务器端使用多线程（或多进程）。多线程（或多进程）的目的是让每个连接都拥有独立的线程（或进程），这样任何一个连接的阻塞都不会影响其他的连接。

弊端：开启多进程或都线程的方式，我们是无法无限制地开启多进程或多线程的：在遇到要同时响应成百上千路的连接请求，则无论多线程还是多进程都会严重占据系统资源，降低系统对外界响应效率，而且线程与进程本身也更容易进入假死状态。

**b. 解决同步调用方案之线程/进程池**

好处：很多程序员可能会考虑使用“线程池”或“连接池”。“线程池”旨在减少创建和销毁线程的频率，其维持一定合理数量的线程，并让空闲的线程重新承担新的执行任务。可以很好的降低系统开销。

弊端：“线程池”和“连接池”技术也只是在一定程度上缓解了频繁调用IO接口带来的资源占用。而且，所谓“池”始终有其上限，当请求大大超过上限时，“池”构成的系统对外界的响应并不比没有池的时候效果好多少。所以使用“池”必须考虑其面临的响应规模，并根据响应规模调整“池”的大小。

案例：基于multiprocessing.dummy线程池爬取梨视频的视频信息

```
import requests
import re
from lxml import etree
from multiprocessing.dummy import Pool
import random

header = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36'
}

def get_page(url):
    response = requests.get(url=url,headers=header)
    if response.status_code == 200:
        return response.text
    return None

def parse_page(res):
    tree = etree.HTML(res)
    li_list = tree.xpath('//div[@id="listvideoList"]/ul/li')

    video_url_list = []
    for li in li_list:
        detail_url = 'https://www.pearvideo.com/' + li.xpath('./div/a/@href')[0]
        detail_page = requests.get(url=detail_url, headers=header).text
        video_url = re.findall('srcUrl="(.*?)",vdoUrl', detail_page, re.S)[0]
        video_url_list.append(video_url)

    return video_url_list

# 获取视频
def getVideoData(url):
    return requests.get(url=url, headers=header).content

# 持久化存储
def saveVideo(data):
    fileName = str(random.randint(0, 5000)) + '.mp4'  # 因回调函数只能传一个参数，所以没办法再传名字了，只能自己取名
    with open(fileName, 'wb') as fp:
        fp.write(data)

def main():
    url = 'https://www.pearvideo.com/category_1'
    res = get_page(url)
    links = parse_page(res)


    pool = Pool(5)  # 实例化一个线程池对象

    #  pool.map(回调函数，可迭代对象）函数依次执行对象
    video_data_list = pool.map(getVideoData, links)
    pool.map(saveVideo, video_data_list)
    
    pool.close()
    pool.join()

if __name__== "__main__":
    main()
```

总结：对应上例中的所面临的可能同时出现的上千甚至上万次的客户端请求，“线程池”或“连接池”或许可以缓解部分压力，但是不能解决所有问题。总之，多线程模型可以方便高效的解决小规模的服务请求，但面对大规模的服务请求，多线程模型也会遇到瓶颈，可以用非阻塞接口来尝试解决这个问题。

**终极处理方案**

 上述无论哪种方案都没有解决一个性能相关的问题：**IO阻塞**，无论是多进程还是多线程，在遇到IO阻塞时都会被操作系统强行剥夺走CPU的执行权限，程序的执行效率因此就降低了下来。

 **解决这一问题的关键在于，我们自己从应用程序级别检测IO阻塞****，****然后切换到我们自己程序的其他任务执行，这样把我们程序的IO降到最低，我们的程序处于就绪态就会增多，以此来迷惑操作系统，操作系统便以为我们的程序是IO比较少的程序，从而会尽可能多的分配CPU给我们，这样也就达到了提升程序执行效率的目的。**

 **实现方式：单线程+协程实现异步IO操作。**

 **异步IO：就是你发起一个 网络IO 操作，却不用等它结束，你可以继续做其他事情，当它结束时，你会得到通知。**

## **四、** **异步协程**

在python3.4之后新增了asyncio模块，可以帮我们检测IO（只能是网络IO【HTTP连接就是网络IO操作】），实现应用程序级别的切换（**异步IO**）。**注意：asyncio只能发tcp级别的请求，不能发http协议。**

**asyncio 是干什么的？**

- 异步网络操作
- 并发
- 协程 

几个概念：

**event\_loop**：事件循环，相当于一个无限循环，我们可以把一些函数注册到这个事件循环上，当满足条件发生的时候，就会调用对应的处理方法。

**coroutine**：中文翻译叫协程，在 Python 中常指代为协程对象类型，我们可以将协程对象注册到时间循环中，它会被事件循环调用。我们可以使用 **async 关键字来定义一个方法，这个方法在调用时不会立即被执行，而是返回一个协程对象。**

**task**：任务，它是对协程对象的进一步封装，包含了任务的各个状态。

**future**：代表将来执行或没有执行的任务的结果，实际上和 task 没有本质区别。

**async关键字**：async 定义一个协程；

**await 关键字**：用来挂起阻塞方法的执行。

**注意事项：在特殊函数内部不可以出现不支持异步模块相关的代码。(例：time，request)**

**1.****定义一个协程**

```
import asyncio

async def execute(x):
    print('Number:', x)


coroutine = execute(1)
print('Coroutine:', coroutine)
print('After calling execute')

loop = asyncio.get_event_loop()
loop.run_until_complete(coroutine)
print('After calling loop')
```

**运行结果：**
```

Coroutine: <coroutine object execute at 0x1034cf830>

After calling execute

Number: 1

After calling loop```

**可见，async 定义的方法就会变成一个无法直接执行的 coroutine 对象，必须将其注册到事件循环中才可以执行。**

上文我们还提到了 task，它是对 coroutine 对象的进一步封装，它里面相比 coroutine 对象多了运行状态，比如 running、finished 等，我们可以用这些状态来获取协程对象的执行情况。

在上面的例子中，当我们**将** **coroutine 对象传递给 run\_until\_complete() 方法的时候，实际上它进行了一个操作就是将 coroutine 封装成了 task** **对象**，我们也可以显式地进行声明，如下所示：

```
import asyncio

async def execute(x):
    print('Number:',x)
    return x

coroutine = execute(1)
print('Coroutine:',coroutine)print('After calling execute')

loop = asyncio.get_event_loop()
task = loop.create_task(coroutine)
print('Task:',task)

loop.run_until_complete(task)
print('Task:',task)
print('After calling loop')
```

**运行结果：**
```
Coroutine: <coroutine object execute at 0x10e0f7830>

After calling execute

Task: <Task pending coro=<execute() running at demo.py:4>>

Number: 1

Task: <Task finished coro=<execute() done, defined at demo.py:4> result=1>

After calling loop
```
这里我们定义了 loop 对象之后，接着调用了它的 **create\_task() 方法将 coroutine 对象转化为了 task** **对象**，随后我们打印输出一下，发现它是 pending 状态。接着我们将 task 对象添加到事件循环中得到执行，随后我们再打印输出一下 task 对象，发现它的状态就变成了 finished，同时还可以看到其 result 变成了 1，也就是我们定义的 execute() 方法的返回结果。

另外，定义 task 对象还有一种方式，就是直接通过 asyncio 的 **ensure\_future() 方法，返回结果也是 task** **对象**，这样的话我们就可以不借助于 loop 来定义，即使我们还没有声明 loop 也可以提前定义好 task 对象，写法如下：

```
import asyncio

async def execute(x):
    print('Number:',x)
    return x

coroutine = execute(1)
print('Coroutine:',coroutine)
print('After calling execute')

task=asyncio.ensure_future(coroutine)
print('Task:',task)

loop=asyncio.get_event_loop()
loop.run_until_complete(task)
print('Task:',task)
print('After calling loop')
```

**2.****绑定回调：也可以为某个 task 绑定一个回调方法.**

```
import asyncio
import requests

async def request():
    url='https://www.baidu.com'
    status = requests.get(url).status_code
    return status

def  callback(task):
    print('Status:',task.result())

coroutine = request()
task = asyncio.ensure_future(coroutine)
task.add_done_callback(callback)
print('Task:',task)

loop = asyncio.get_event_loop()
loop.run_until_complete(task)
print('Task:',task)
```

 运行结果：

Task: <Task pending coro=<request() running at demo.py:5> cb=\[callback() at demo.py:11\]>

Status: <Response \[200\]>

Task: <Task finished coro=<request() done, defined at demo.py:5> result=<Response \[200\]>>

在这里我们定义了一个 request() 方法，请求了百度，返回状态码，但是这个方法里面我们没有任何 print() 语句。随后我们定义了一个 callback() 方法，这个方法接收一个参数，是 task 对象，然后调用 print() 方法打印了 task 对象的结果。这样我们就定义好了一个 coroutine 对象和一个回调方法，我们现在希望的效果是，当 coroutine 对象执行完毕之后，就去执行声明的 callback() 方法。

那么它们二者怎样关联起来呢？很简单，只需要调用 add\_done\_callback() 方法即可，我们将 callback() 方法传递给了封装好的 task 对象，这样当 task 执行完毕之后就可以调用 callback() 方法了，同时 task 对象还会作为参数传递给 callback() 方法，调用 task 对象的 result() 方法就可以获取返回结果了。

实际上不用回调方法，直接在 task 运行完毕之后也可以直接调用 result() 方法获取结果，运行结果是一样的。如下所示：

```
import asyncio
import requests

async def request():
    url='https://www.baidu.com'
    status=requests.get(url).status_code
    return status

coroutine=request()
task=asyncio.ensure_future(coroutine)
print('Task:',task)

loop=asyncio.get_event_loop()
loop.run_until_complete(task)
print('Task:',task)
print('Task Result:',task.result())
```

**3.****多任务协程**

 **上面的例子我们只执行了一次请求，如果我们想执行多次请求应该怎么办呢？我们可以定义一个 task 列表，然后使用 asyncio 的 wait() 方法即可执行。**

```
import asyncio
import requests

async def request():
    url = 'https://www.baidu.com'
    status = requests.get(url).status_code
    return status

tasks = [asyncio.ensure_future(request()) for _ in range(5)]
print('Tasks:',tasks)

loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))
for task in tasks:
    print('Task Result:',task.result())
```

运行结果:
```
Tasks: \[<Task pending coro=<request() running at demo.py:5>>, <Task pending coro=<request() running at demo.py:5>>, <Task pending coro=<request() running at demo.py:5>>, <Task pending coro=<request() running at demo.py:5>>, <Task pending coro=<request() running at demo.py:5>>\]

Task Result: <Response \[200\]>

Task Result: <Response \[200\]>

Task Result: <Response \[200\]>

Task Result: <Response \[200\]>

Task Result: <Response \[200\]>
```
这里我们使用一个 for 循环创建了五个 task，组成了一个列表，然后把这个列表首先传递给了 asyncio 的 wait() 方法，然后再将其注册到时间循环中，就可以发起五个任务了。

**4.****协程实现**

 上面的案例只是为后面的使用作铺垫，接下来我们正式来看下协程在解决 IO 密集型任务上有怎样的优势吧！

 为了表现出协程的优势，我们需要先创建一个合适的实验环境，最好的方法就是模拟一个需要等待一定时间才可以获取返回结果的网页，上面的代码中使用了百度，但百度的响应太快了，而且响应速度也会受本机网速影响，所以最好的方式是自己在本地模拟一个慢速服务器，这里我们选用 Flask。

```
服务器代码：
from flask import Flask
import time
 
app = Flask(__name__)
 
@app.route('/')
def index():
    time.sleep(3)
return 'Hello!'

if __name__ == '__main__':
    app.run(threaded=True)                #这表明 Flask 启动了多线程模式，不然默认是只有一个线程的。
```

接下来我们再重新使用上面的方法请求一遍：

```
import asyncio
import requests
import time
 
start = time.time()
 
async def request():
    url = 'http://127.0.0.1:5000'
    print('Waiting for', url)
    response = requests.get(url)
    print('Get response from', url, 'Result:', response.text)
 
tasks = [asyncio.ensure_future(request()) for _ in range(5)]
loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))
 
end = time.time()
print('Cost time:', end - start)
```

运行结果如下：
```
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
**Cost time: 15.049368143081665**
```
在这里我们还是创建了五个 task，然后将 task 列表传给 wait() 方法并注册到时间循环中执行。

**其实，要实现异步处理，我们得先要有挂起的操作，当一个任务需要等待 IO 结果的时候，可以挂起当前任务，转而去执行其他任务，这样我们才能充分利用好资源，上面方法都是一本正经的串行走下来，连个挂起都没有，怎么可能实现异步？**

**要实现异步，接下来我们再了解一下 await 的用法，使用 await 可以将耗时等待的操作挂起，让出控制权。当协程执行的时候遇到 await，时间循环就会将本协程挂起，转而去执行别的协程，直到其他的协程挂起或执行完毕。**

所以，我们可能会将代码中的 request() 方法改成如下的样子：

```
async def request():
    url = 'http://127.0.0.1:5000'
    print('Waiting for', url)
    response = await requests.get(url)
print('Get response from', url, 'Result:', response.text)
```

仅仅是在 requests 前面加了一个 await，然而执行以下代码，会得到如下报错：
```
Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Cost time: 15.048935890197754 Task exception was never retrieved
future: <Task finished coro=<request() done, defined at demo.py:7> exception=TypeError("object Response can't be used in 'await' expression",)> Traceback (most recent call last):
  File "demo.py", line 10, in request
    status = await requests.get(url)
TypeError: object Response can't be used in 'await' expression
```
这次它遇到 await 方法确实挂起了，也等待了，但是最后却报了这么个错，这个错误的意思是 requests 返回的 Response 对象不能和 await 一起使用，为什么呢？因为根据官方文档说明，**await 后面的对象必须是如下格式之一：**

- A native coroutine object returned from a native coroutine function，一个原生 coroutine 对象。
- A generator-based coroutine object returned from a function decorated with types.coroutine()，一个由 types.coroutine() 修饰的生成器，这个生成器可以返回 coroutine 对象。
- An object with an await\_\_ method returning an iterator，一个包含 \_\_await 方法的对象返回的一个迭代器。

reqeusts 返回的 Response 不符合上面任一条件，因此就会报上面的错误了。**既然** **await 后面可以跟一个 coroutine 对象，那么我将请求页面的方法独立出来，并用 async 修饰，这样就得到了一个 coroutine 对象**

```
import asyncio
import requests
import time
 
start = time.time()
 
async def get(url):
    return requests.get(url)
 
async def request():
    url = 'http://127.0.0.1:5000'
    print('Waiting for', url)
    response = await get(url)
    print('Get response from', url, 'Result:', response.text)
 
tasks = [asyncio.ensure_future(request()) for _ in range(5)]
loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))
 
end = time.time()
print('Cost time:', end - start)
```

这里我们，我们运行一下看看：
```
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Waiting for http://127.0.0.1:5000
Get response from http://127.0.0.1:5000 Result: Hello!
Cost time: 15.134317874908447
```
还是不行，它还不是异步执行，也就是说我们仅仅将涉及 IO 操作的代码封装到 async 修饰的方法里面是不可行的！我们必须要使用支持异步操作的请求方式才可以实现真正的异步，所以这里就需要 **aiohttp** 派上用场了。**(由于requests 模块不支持异步，所以用aiohttp 模块)**

**5.使用** **aiohttp** 

\-环境安装：pip install aiohttp

我们将 aiohttp 用上来，**将请求库由** **requests 改成了 aiohttp，通过 aiohttp 的 ClientSession 类的 get()** **方法进行请求**

```
import asyncio
import aiohttp
import time

start= time.time()

async def get(url):
    session = aiohttp.ClientSession()
    response = await session.get(url)
    result = await response.text()
    session.close()
    return result

async def request():
    url = 'http://127.0.0.1:5000'
    print('Waiting for',url)
    result = await get(url)
    print('Get response from',url,'Result:',result)

tasks = [asyncio.ensure_future(request()) for _ in range(5)]
loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))

end = time.time()
print('Cost time:', end - start)
```

结果如下：我们发现这次请求的耗时由 15 秒变成了 3 秒，耗时直接变成了原来的 1/5
```
Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Waiting for http://127.0.0.1:5000 Get response from http://127.0.0.1:5000 Result: Hello!
Get response from http://127.0.0.1:5000 Result: Hello!
Get response from http://127.0.0.1:5000 Result: Hello!
Get response from http://127.0.0.1:5000 Result: Hello!
Get response from http://127.0.0.1:5000 Result: Hello!
Cost time: **3.0199508666992188  **
```


代码里面我们使用了 await，后面跟了 get() 方法，**在执行这五个协程的时候，如果遇到了 await，那么就会将当前协程挂起，转而去执行其他的协程，直到其他的协程也挂起或执行完毕，再进行下一个协程的执行。充分利用 CPU 时间，而不必把时间浪费在等待 IO 上**

开始运行时，时间循环会运行第一个 task，针对第一个 task 来说，当执行到第一个 await 跟着的 get() 方法时，它被挂起，但这个 get() 方法第一步的执行是非阻塞的，挂起之后立马被唤醒，所以立即又进入执行，创建了 ClientSession 对象，接着遇到了第二个 await，调用了 session.get() 请求方法，然后就被挂起了，由于请求需要耗时很久，所以一直没有被唤醒，好第一个 task 被挂起了，那接下来该怎么办呢？事件循环会寻找当前未被挂起的协程继续执行，于是就转而执行第二个 task 了，也是一样的流程操作，直到执行了第五个 task 的 session.get() 方法之后，全部的 task 都被挂起了。所有 task 都已经处于挂起状态，那咋办？只好等待了。3 秒之后，几个请求几乎同时都有了响应，然后几个 task 也被唤醒接着执行，输出请求结果，最后耗时，3 秒！

在上面的例子中，在发出网络请求后，既然接下来的 3 秒都是在等待的，在 3 秒之内，CPU 可以处理的 task 数量远不止这些，那么岂不是我们放 很多 个 task 一起执行，最后得到所有结果的耗时不都是 3 秒左右吗？因为这几个任务被挂起后都是一起等待的。理论来说确实是这样的，不过有个前提，那就是服务器在同一时刻接受无限次请求都能保证正常返回结果，也就是服务器无限抗压，另外还要忽略 IO 传输时延，确实可以做到无限 task 一起执行且在预想时间内得到结果。

我们这里将 task 数量设置成 100，再试一下：

```tasks = [asyncio.ensure_future(request()) for _ in range(100)]```

耗时结果如下：
Cost time: **3.106252670288086**  

最后运行时间也是在 3 秒左右，当然多出来的时间就是 IO 时延了。可见，使用了异步协程之后，我们几乎可以在相同的时间内实现成百上千倍次的网络请求，把这个运用在爬虫中，速度提升可谓是非常可观了。

**6. 与单进程、多进程对比**

单进程

```
import requests
import time

start = time.time()


def request():
    url = 'http://127.0.0.1:5000'
    print('Waiting for', url)
    result = requests.get(url).text
    print('Get response from', url, 'Result:', result)


for _ in range(100):
    request()

end = time.time()
print('Cost time:', end - start)
```

最后耗时：
Cost time: **305**.16639709472656  
  
多进程  

```
import requests
import time
import multiprocessing

start = time.time()


def request(_):
    url = 'http://127.0.0.1:5000'
    print('Waiting for', url)
    result = requests.get(url).text
    print('Get response from', url, 'Result:', result)


cpu_count = multiprocessing.cpu_count()
print('Cpu count:', cpu_count)
pool = multiprocessing.Pool(cpu_count)
pool.map(request, range(100))

end = time.time()
print('Cost time:', end - start)
```

这里我使用了multiprocessing 里面的 Pool 类，即进程池。我的电脑的 CPU 个数是 8 个，这里的进程池的大小就是 8。

耗时：
Cost time: **48**.17306900024414  
  

**7.****与多进程结合**

在最新的 PyCon 2018 上，来自 Facebook 的 John Reese 介绍了 asyncio 和 multiprocessing 各自的特点，并开发了一个新的库，叫做 aiomultiprocess。需要 Python 3.6 及更高版本才可使用。

安装：pip install aiomultiprocess

使用这个库，我们可以将上面的例子改写如下：

```
import asyncio
import aiohttp
import time
from aiomultiprocess import Pool
 
start = time.time()
 
async def get(url):
    session = aiohttp.ClientSession()
    response = await session.get(url)
    result = await response.text()
    session.close()
    return result
 
async def request():
    url = 'http://127.0.0.1:5000'
    urls = [url for _ in range(100)]
    async with Pool() as pool:
        result = await pool.map(get, urls)
        return result
 
coroutine = request()
task = asyncio.ensure_future(coroutine)
loop = asyncio.get_event_loop()
loop.run_until_complete(task)
 
end = time.time()
print('Cost time:', end - start)
```

这样就会同时使用多进程和异步协程进行请求，当然最后的结果其实和异步是差不多的：

Cost time: **3**.1156570434570312

因为我的测试接口的原因，最快的响应也是 3 秒，所以这部分多余的时间基本都是 IO 传输时延。但在真实情况下，我们在做爬取的时候遇到的情况千变万化，一方面我们使用异步协程来防止阻塞，另一方面我们使用 multiprocessing 来利用多核成倍加速，节省时间其实还是非常可观的。

更多案例

```
import aiohttp
import asyncio
from lxml import etree

all_titles = []

headers = {
    'User-Agent':'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36'

}
async def request(url):
    async with aiohttp.ClientSession() as s:
        async with await s.get(url,headers=headers) as response:
            page_text = await response.text()
            return page_text


def parse(task):
    page_text = task.result()
    page_text = page_text.encode('gb2312').decode('gbk')
    tree = etree.HTML(page_text)
    tr_list = tree.xpath('//*[@id="morelist"]/div/table[2]//tr/td/table//tr')
    for tr in tr_list:
        title = tr.xpath('./td[2]/a[2]/text()')[0]
        print(title)
        all_titles.append(title)

urls = []
url = 'http://wz.sun0769.com/index.php/question/questionType?type=4&page=%d'
for page in range(100):
    u_page = page * 30
    new_url = format(url%u_page)
    urls.append(new_url)

tasks = []
for url in urls:
    c = request(url)
    task = asyncio.ensure_future(c)
    task.add_done_callback(parse)
    tasks.append(task)

loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))
```

参考链接： https://blog.csdn.net/zhusongziye/article/details/81637088












---
## asyncio 和 uvloop

asyncio 模块, 是在 PEP 3156引入的, 是一个集合，包含网络传输, 协议, 和抽象的流, 带有可插拔的事件循环. 事件循环是asyncio的核心.它提供如下API:

- 调用方法的调度
- 通过网络传输数据
- 执行 DNS 查询,
- 处理 OS 操作系统信号
- 对创建服务器和连接进行封装
- 子进程异步处理

目前 uvloop 只支持 \*nix 平台和 Python 3.5。

uvloop 是 Python 内建的 asyncio 事件循环的替代品，你可以通过 pip 来安装：

$ pip install uvloop

在你的 asyncio 代码中使用 uvloop 非常简单：

**import** asyncio
**import** uvloop
asyncio.set\_event\_loop\_policy(uvloop.EventLoopPolicy())

上面的代码片段让 `asyncio.get_event_loop()` 返回一个 uvloop 的实例。