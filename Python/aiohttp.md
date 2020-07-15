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