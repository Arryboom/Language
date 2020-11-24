### 发起请求

```python
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get('https://www.baidu.com') as resposne:
            print(await resposne.text())

loop = asyncio.get_event_loop()
tasks = [fetch(),]
loop.run_until_complete(asyncio.wait(tasks))
```

### 添加请求参数

```python
params = {'key': 'value', 'page': 10}
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get('https://www.baidu.com/s',params=params) as resposne:
            print(await resposne.url)

loop = asyncio.get_event_loop()
tasks = [fetch(),]
loop.run_until_complete(asyncio.wait(tasks))
```

### 自定义User-Agent

```python
url = 'http://httpbin.org/user-agent'
headers = {'User-Agent': 'test_user_agent'}

async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get(url,headers=headers) as resposne:
            print(await resposne.text())

loop = asyncio.get_event_loop()
tasks = [fetch(),]
loop.run_until_complete(asyncio.wait(tasks))
```

### 自定义cookies

```python
url = 'http://httpbin.org/cookies'
cookies = {'cookies_name': 'test_cookies'}

async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get(url,cookies=cookies) as resposne:
            print(await resposne.text())
          

loop = asyncio.get_event_loop()
tasks = [fetch(),]
loop.run_until_complete(asyncio.wait(tasks))
```

### post字符串

```python
url = 'http://httpbin.org'
payload = {'username': 'zhang', 'password': '123456'}
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.post(url, data=payload) as resposne:
            print(await resposne.text())

loop = asyncio.get_event_loop()
tasks = [fetch(), ]
loop.run_until_complete(asyncio.wait(tasks))
```

### post文件

```python
url = 'http://httpbin.org'
files = {'file': open('test.txt', 'rb')}
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.post(url, data=files) as resposne:
            print(await resposne.text())

loop = asyncio.get_event_loop()
tasks = [fetch(), ]
loop.run_until_complete(asyncio.wait(tasks))
```

### 设置代理

```python
url = "http://python.org"
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get(url, proxy="http://some.proxy.com") as resposne:
        print(resposne.status)

loop = asyncio.get_event_loop()
tasks = [fetch(), ]
loop.run_until_complete(asyncio.wait(tasks))
```

### 设置认证代理

```python
url = "http://python.org"
async def fetch():
    async with aiohttp.ClientSession() as session:
        proxy_auth = aiohttp.BasicAuth('user', 'pass')
        async with session.get(url, proxy="http://some.proxy.com", proxy_auth=proxy_auth) as resposne:
            print(response.status)

loop = asyncio.get_event_loop()
tasks = [fetch(), ]
loop.run_until_complete(asyncio.wait(tasks))

# 下面的方法也可以
url = "http://python.org"
async def fetch():
    async with aiohttp.ClientSession() as session:
        async with session.get(url, proxy="http://user:pass@some.proxy.com") as response:
            print(response.status)

loop = asyncio.get_event_loop()
tasks = [fetch(), ]
loop.run_until_complete(asyncio.wait(tasks))
```













#代理示例

>https://www.jianshu.com/p/1cd6d34407e2

```
async def douban(proxy, session):
# 使用代理访问目标网站，并按情况奖惩代理
    try:
        start = time.time()
        async with session.post(mobike_url,
                                data=data,
                                proxy=proxy.url,
                                headers=headers,  # 可以引用到外部的headers
                                timeout=10) as resp:
            end = time.time()
            # print(resp.status)
            if resp.status == 200:
                proxy.success(end - start)
                print('%6.3d' % proxy._score, 'Used time-->', end - start, 's')
            else:
                proxy.otherError()
                print('*****', resp.status, '*****')
    except TimeoutError as te:
        print('%6.3d' % proxy._score, 'timeoutError')
        proxy.timeoutError()
    except ClientConnectionError as ce:
        print('%6.3d' % proxy._score, 'connectError')
        proxy.connectError()
    except Exception as e:
        print('%6.3d' % proxy._score, 'otherError->', e)
        proxy.otherError()
# ClientHttpProxyError

# TCPConnector维持链接池，限制并行连接的总量，当池满了，有请求退出再加入新请求，500和100相差不大
# ClientSession调用TCPConnector构造连接，Session可以共用
# Semaphore限制同时请求构造连接的数量，Semphore充足时，总时间与timeout差不多


async def initDouban():

    conn = aiohttp.TCPConnector(verify_ssl=False,
                                limit=100,  # 连接池在windows下不能太大
                                use_dns_cache=True)
    tasks = []
    async with aiohttp.ClientSession(loop=loop, connector=conn) as session:
        for p in proxies:
            task = asyncio.ensure_future(douban(p, session))
            tasks.append(task)

        responses = asyncio.gather(*tasks)
        await responses
    conn.close()


def firstFilter():
    for i in range(2):
        s = time.time()
        future = asyncio.ensure_future(initDouban())
        loop.run_until_complete(future)
        e = time.time()
        print('----- init time %s-----\n' % i, e - s, 's')

    num = 0
    pq = PriorityQueue()
    for proxy in proxies:
        if proxy._score > 50:
            pq.put_nowait(proxy)
            num += 1
    print('原始ip数:%s' % len(all_ip), '; 筛选后:%s' % num)
    return pq
```






```
pq = firstFilter()


async def genDouban(sem, session):
    # Getter function with semaphore.
    while True:
        async with sem:
            proxy = await pq.get()
            await douban(proxy, session)
            await pq.put(proxy)


async def dynamicRunDouban(concurrency):
    '''
    TCPConnector维持链接池，限制并行连接的总量，当池满了，有请求退出再加入新请求
    ClientSession调用TCPConnector构造连接，Session可以共用
    Semaphore限制同时请求构造连接的数量，Semphore充足时，总时间与timeout差不多
    '''
    conn = aiohttp.TCPConnector(verify_ssl=False,
                                limit=concurrency,
                                use_dns_cache=True)
    tasks = []
    sem = asyncio.Semaphore(concurrency)

    async with aiohttp.ClientSession(loop=loop, connector=conn) as session:
        try:
            for i in range(concurrency):
                task = asyncio.ensure_future(genDouban(sem, session))
                tasks.append(task)

            responses = asyncio.gather(*tasks)
            await responses
        except KeyboardInterrupt:
            print('-----finishing-----\n')
            for task in tasks:
                task.cancel()
            if not conn.closed:
                conn.close()


future = asyncio.ensure_future(dynamicRunDouban(200))
loop.run_until_complete(future)
```




你可以在`session.get()`中设置你的`proxy`：

```
async with session.get(url, proxy=your_proxy_url) as response:
    return BeautifulSoup(await response.content, 'html.parser')
```

如果你的IP需要认证，可以这样设置：

```
proxy = 'http://your_user:your_password@your_proxy_url:your_proxy_port'
async with session.get(url, proxy=proxy) as response:
    return BeautifulSoup(await response.content, 'html.parser')
```

或者是这样设置：

```
proxy = 'http://your_proxy_url:your_proxy_port'
proxy_auth = aiohttp.BasicAuth('your_user', 'your_password')
async with session.get(url, proxy=proxy, proxy_auth=proxy_auth) as response:
    return BeautifulSoup(await response.content, 'html.parser')
```



#爬图片示例

```
import asyncio
import aiohttp
import time
import random
import os
from lxml import etree

path='F:\\wuso\\'      #文件保存路径

targe_url=[]            
for i in range(0,200):#total 178        建立任务链接
    targe_url.append('https://wuso.me/forum-photos-{}.html'.format(i))

async def run(url):
    path='F:\\wuso\\'
    headers={'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
 'Accept-Encoding':'gb2312,utf-8',
 'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0',
 'Accept-Language':'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
 'Connection':'Keep-alive'
}
    try:
        async with aiohttp.ClientSession() as session:
            aiohttp.Timeout(5)
            async with session.get(url,headers=headers) as response:
                res=await response.text()
                res=etree.HTML(res)
                fName=res.xpath("//div[@class='c cl']/a/@title")
                #print(fName)
                fLink=res.xpath("//div[@class='c cl']/a/@href")
                for i in range(len(fName)):
                    async with session.get(fLink[i],headers=headers) as imgres:
                        t=await imgres.text()
                        t=etree.HTML(t)
                        imgLinks=t.xpath('//img/@zoomfile')
                        try:
                            if not os.path.exists(path+fName[i]):
                                os.makedirs(path+fName[i])
                            for link in imgLinks:
                                async with session.get('https://wuso.me/'+link,headers=headers) as img:
                                    try:
                                        imgcode=await img.read()
                                        try:
                                            with open(path+fName[i]+'\\'+link.split('/')[-1],'wb') as f:
                                                f.write(imgcode)
                                                f.close()
                                                print(link.split('/')[-1],'Saved')
                                        except:
                                            print('文件创建失败')
                                            pass
                                    except:
                                        print('二进制文件读取失败')
                                        pass
                        except:
                            print('Img保存失败')
                            pass
    except:
        print('response失败*******************************************')
        pass
start=time.time()
loop=asyncio.get_event_loop()
tasks=[]
for u in targe_url:
    tasks.append(asyncio.ensure_future(run(u)))
loop.run_until_complete(asyncio.wait(tasks))
loop.close()
print('total {}pages,time cost:{}'.format(len(tasks),time.time()-start))
```




#连接复用reuse

>https://stackoverflow.com/questions/55879847/asyncio-how-to-reuse-a-socket
>https://stackoverflow.com/questions/52532075/session-reusing-in-aiohhttp
>https://stackoverflow.com/questions/46991562/how-to-reuse-aiohttp-clientsession-pool


You'd simply call the `asyncio.open_connection(server, port)` coroutine just once, and keep using the reader and writer (provided the server doesn't just close the connection on their end, of course).

I'd do so in a separate async context manager object for your connections, and use a [_connection pool_](https://en.wikipedia.org/wiki/Connection_pool) to manage the connections, so you can create and re-use socket connections for many concurrent tasks. By using an (async) context manager, Python makes sure to notify the connection when your code is done with it, so the connection can be released back to the pool:
```
ction"]] = OrderedDict()
        self._semaphore = asyncio.Semaphore(max_connections)

    async def connect(self, server: Server, port: Port) -> "Connection":
        host = (server, port)

        # enforce the connection limit, releasing connections notifies
        # the semaphore to release here
        await self._semaphore.acquire()

        connections = self._connections.setdefault(host, [])
        # find an un-used connection for this host
        connection = next((conn for conn in connections if not conn.in_use), None)
        if connection is None:
            # disconnect the least-recently-used un-used connection to make space
            # for a new connection. There will be at least one.
            for conns_per_host in reversed(self._connections.values()):
                for conn in conns_per_host:
                    if not conn.in_use:
                        await conn.close()
                        break

            reader, writer = await asyncio.open_connection(server, port)
            connection = Connection(self, host, reader, writer)
            connections.append(connection)

        connection.in_use = True
        # move current host to the front as most-recently used
        self._connections.move_to_end(host, False)

        return connection

    async def close(self):
        """Close all connections"""
        connections = [c for cs in self._connections.values() for c in cs]
        self._connections = OrderedDict()
        for connection in connections:
            await connection.close()

    def _remove(self, connection):
        conns_for_host = self._connections.get(connection._host)
        if not conns_for_host:
            return
        conns_for_host[:] = [c for c in conns_for_host if c != connection]

    def _notify_release(self):
        self._semaphore.release()

    async def __aenter__(self) -> "ConnectionPool":
        return self

    async def __aexit__(
        self,
        exc_type: Optional[Type[BaseException]],
        exc: Optional[BaseException],
        tb: Optional[TracebackType],
    ) -> None:
        await self.close()

    def __del__(self) -> None:
        connections = [repr(c) for cs in self._connections.values() for c in cs]
        if not connections:
            return

        context = {
            "pool": self,
            "connections": connections,
            "message": "Unclosed connection pool",
        }
        self._loop.call_exception_handler(context)


class Connection(base):
    def __init__(
        self,
        pool: ConnectionPool,
        host: Host,
        reader: asyncio.StreamReader,
        writer: asyncio.StreamWriter,
    ):
        self._host = host
        self._pool = pool
        self._reader = reader
        self._writer = writer
        self._closed = False
        self.in_use = False

    def __repr__(self):
        host = f"{self._host[0]}:{self._host[1]}"
        return f"Connection<{host}>"

    @property
    def closed(self):
        return self._closed

    def release(self) -> None:
        self.in_use = False
        self._pool._notify_release()

    async def close(self) -> None:
        if self._closed:
            return
        self._closed = True
        self._writer.close()
        self._pool._remove(self)
        try:
            await self._writer.wait_closed()
        except AttributeError:  # wait_closed is new in 3.7
            pass

    def __getattr__(self, name: str) -> Any:
        """All unknown attributes are delegated to the reader and writer"""
        if self._closed or not self.in_use:
            raise ValueError("Can't use a closed or unacquired connection")
        if hasattr(self._reader, name):
            return getattr(self._reader, name)
        return getattr(self._writer, name)

    async def __aenter__(self) -> "Connection":
        if self._closed or not self.in_use:
            raise ValueError("Can't use a closed or unacquired connection")
        return self

    async def __aexit__(
        self,
        exc_type: Optional[Type[BaseException]],
        exc: Optional[BaseException],
        tb: Optional[TracebackType],
    ) -> None:
        self.release()

    def __del__(self) -> None:
        if self._closed:
            return
        context = {"connection": self, "message": "Unclosed connection"}
        self._pool._loop.call_exception_handler(context)
```


then pass in a pool object to your lookup coroutine; the connection object produced proxies for both the reader and writer parts:


```
async def lookup(pool, server, port, query):
    try:
        conn = await pool.connect(server, port)
    except (ValueError, OSError):
        return {}

    async with conn:
        conn.write(query.encode("ISO-8859-1"))
        await conn.drain()
        data = b""
        while True:
            d = await conn.read(4096)
            if not d:
                break
            data += d
        data = data.decode("ISO-8859-1")
        return data
```

Note that the [standard WHOIS protocol (RFC 3912 or predecessors](https://tools.ietf.org/html/rfc3912) states that the connection is closed after every query. If you are connecting to a standard WHOIS service on port 43, there is no point in re-using sockets.

What happens in this case is that the reader will have reached EOF (`reader.at_eof()` is true), and any further attempts at reading will simply return nothing (`reader.read(...)` will always return an empty `b''` value). Writing to the writer is not going to be an error until the socket connection is terminated by the remote side after a time-out. You can write all you want to the connection, but the WHOIS server will just ignore the queries.


---


You can create a connection cache by storing the reader/writer pairs to a global dictionary.

```
# at top-level
connections = {}
```

Then in `lookup`, replace the call to `open_connection` with code that checks the dict first:

```
if (server, port) in connections:
    reader, writer = connections[server, port]
else:
    reader, writer = await asyncio.open_connection(server, port)
    connections[server, port] = reader, writer
```

---




