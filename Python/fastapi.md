#demo

```
#coding:utf-8
from fastapi import FastAPI
#import 
import json
from enum import Enum
from pydantic import BaseModel
import time,datetime
from typing import Optional
from sqlalchemy import Column, String,Integer, create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base


# ##init
# class BadKeyWord(BaseModel):
    # keyword: str
    # description: Optional[str] = "Null"
    # date: Optional[str]="1970-01-01 15:30"
    # main_type: Optional[str]="normall"
    
# class UserIn(BaseModel):
    # username: str
    # password: str=None
    # email: str=None
    # full_name: str = None

# ###structure

# app = FastAPI()

# @app.get("/")
# async def root():
    # return {"message": "Hello World"}

# @app.post("/putkeyword")
# async def putkeyword(kwdobj: BadKeyWord):
    # if (kwdobj.keyword!="Null"):
        # nowtime=time.strftime('%Y-%m-%d %H:%M:%S')
        # kwdobj.date=nowtime
        # return {kwdobj}
    # else:
        # return {"success":"no"}
    
# @app.post("/xputkeyword",response_model=BadKeyWord)
# async def putkeyword(kwdobj: BadKeyWord):
    # if (kwdobj.keyword!="Null"):
        # nowtime=time.strftime('%Y-%m-%d %H:%M:%S')
        # kwdobj.date=str(nowtime)
        # return {kwdobj}
    # else:
        # return {"success":"no"}


# # Don't do this in production!
# @app.post("/user/", response_model=UserIn) 
# async def create_user(*, user: UserIn):
    # return user
    
   
   
###----------------------------------------------------------------------------------------------------------.
###db init###
Base = declarative_base()
engine = create_engine('sqlite:///words.db?check_same_thread=False', echo=True)
DBSession = sessionmaker(bind=engine)
#from sqlalchemy import Column, Integer, String

# 定义映射类User，其继承上一步创建的Base
class Words(Base):
    # 指定本类映射到users表
    __tablename__ = 'words'
    # 如果有多个类指向同一张表，那么在后边的类需要把extend_existing设为True，表示在已有列基础上进行扩展
    # 或者换句话说，sqlalchemy允许类是表的字集
    # __table_args__ = {'extend_existing': True}
    # 如果表在同一个数据库服务（datebase）的不同数据库中（schema），可使用schema参数进一步指定数据库
    # __table_args__ = {'schema': 'test_database'}
    
    # 各变量名一定要与表的各字段名一样，因为相同的名字是他们之间的唯一关联关系
    # 从语法上说，各变量类型和表的类型可以不完全一致，如表字段是String(64)，但我就定义成String(32)
    # 但为了避免造成不必要的错误，变量的类型和其对应的表的字段的类型还是要相一致
    # sqlalchemy强制要求必须要有主键字段不然会报错，如果要映射一张已存在且没有主键的表，那么可行的做法是将所有字段都设为primary_key=True
    # 不要看随便将一个非主键字段设为primary_key，然后似乎就没报错就能使用了，sqlalchemy在接收到查询结果后还会自己根据主键进行一次去重
    # 指定id映射到id字段; id字段为整型，为主键，自动增长（其实整型主键默认就自动增长）
    id = Column(Integer, primary_key=True, autoincrement=True)
    # 指定name映射到name字段; name字段为字符串类形，
    word = Column(String(40))
    description = Column(String(256))
    time = Column(String(64))
    maintype = Column(String(64))

    # __repr__方法用于输出该类的对象被print()时输出的字符串，如果不想写可以不写
    # def __repr__(self):
        # return "<User(name='%s', fullname='%s', password='%s')>" % (
                   # self.name, self.fullname, self.password)
###
Base.metadata.create_all(engine, checkfirst=True)
# 创建session对象:
session = DBSession()
# 创建新User对象:
# new_user = User(id='5', name='Bob')
# # 添加到session:
# session.add(new_user)
# # 提交即保存到数据库:
# session.commit()
# # 关闭session:
# session.close()

##################################################################DB#####
class Keyword(BaseModel):
    keyword: str
    description: Optional[str] = None
    time: Optional[str] = "1970-01-01 15:00"
    maintype: Optional[str] = "common"
class XText(BaseModel):
    text: str
def checkrepeat(item: Keyword):
    result=session.query(Words).filter_by(word=item.keyword).first()
    if result:
        return False
    else:
        return True
def insertKWD(item: Keyword):
    db_item=Words(word=item.keyword,description=item.description,time=item.time,maintype=item.maintype)
    session.add(db_item)
    session.commit()
def deleteKWD(item: Keyword):
    result=session.query(Words).filter_by(word=item.keyword).first()
    if result:
        id=result.id
        session.delete(del_user)
        session.commit()
def getall():
    all_res={}
    counter=0
    holder=[]
    for word in session.query(Words):
        tempx=[]
        tempx.append(word.word)
        tempx.append(word.description)
        tempx.append(word.time)
        tempx.append(word.maintype)
        holder.append(tempx)
        #print(user)
        counter+=1
    all_res["count"]=counter
    all_res["result"]=holder
    return all_res

def clearall():
    # all_res={}
    # counter=0
    # holder=[]
    # for word in session.query(Words):
        # tempx=[]
        # tempx.append(word.word)
        # tempx.append(word.description)
        # tempx.append(word.time)
        # tempx.append(word.maintype)
        # holder.append(tempx)
        # #print(user)
        # counter+=1
    # all_res["count"]=counter
    # all_res["result"]=holder
    # return all_res
    # for word in session.query(Words):
        # session.delete(word)
        
        # #print(user)
        # #counter+=1
    #session.commit()
    session.execute('DELETE FROM words;')
    #session.execute("DELETE FROM sqlite_sequence WHERE name = 'words'")
    session.commit()
    # print("hehre")
    res={}
    res["success"]="true"
    return res
    #pass
#    db_item=Words(words=item.keyword,description=item.description,time=item.time,maintype=item.maintype)
#our_user = session.query(User).filter_by(name='ed').first()
def checkit(text: XText):
    for word in session.query(Words):
        if (word.word in text.text):
            return True
    return False
###--------------------------------------------------------------------------------------------------------------





app = FastAPI()

###CORS
from fastapi.middleware.cors import CORSMiddleware
# origins = [
    # "http://localhost",
    # "http://localhost:8080",
    # "http://localhost:8080",
# ]
origins = [
    "http://localhost",
    "http://127.0.0.1:32768",
    "http://127.0.0.1:61112"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

####



@app.post("/putkwd/")
async def create_item(item: Keyword):
    nowtime=time.strftime('%Y-%m-%d %H:%M:%S')
    item.time=str(nowtime)
    item_dict = item.dict()
    item_dict["repeat"]=checkrepeat(item)
    if (checkrepeat(item) and item.keyword):
        insertKWD(item)
    return item_dict
@app.post("/getkwds/")
async def getkwds():
    return getall()
@app.post("/clearall/")
async def clearxall():
    return clearall()
@app.post("/checkjunk/")
async def checkxit(text: XText):
    if checkit(text):
       return "True"
    return "False"
```






#base

您还可以通过 ``//localhost:8000/docs`` 免费获得 Swagger UI。


you have main.py,and app as init function in it.start by
```uvicorn main:app --reload```

```
pip install fastapi uvicorn
```


```
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def home():
    return {"Hello": "World"}

```

Dockerizing and Deploying
Fastapi 的作者使出乎意料的轻松之一就是 Dockerizing！默认的 Dockerfile 是 2 行！

```
FROM tiangolo/uvicorn-gunicorn-fastapi:python3.7

COPY ./app /app
```
是否想通过自动重新加载进行 Dockerize 开发？这是我在撰写文件中使用的秘方：
```
version: "3"
services:
  test-api:
    build: ..
    entrypoint: '/start-reload.sh'
    ports:
        - 8080:80
    volumes:
        - ./:/app

```


>https://learnku.com/python/t/38942


#使用CORSMiddleware
我们通过以下流程在FastAPI应用中使用CORSMiddleware。

1、导入CORSMiddleware。

2、创建允许的origins列表。

3、在应用中引入CORSMiddleware中间件。

如果后端支持我们也可以加入以下信息：

4、鉴权信息(Authorization headers, Cookies等)。

5、支持的HTTP方法(POST，GET，或者所有"*")。

6、支持的HTTP头信息或者所有"*"。


```
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

origins = [
    "http://localhost.tiangolo.com",
    "https://localhost.tiangolo.com",
    "http://localhost",
    "http://localhost:8080",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
async def main():
    return {"message": "Hello World"}
```

CORSMiddleware的参数默认值是受限制的，为了在跨域访问中支持相应的功能，我们应当显示指定具体参数的的信息。

CORSMiddleware支持参数信息如下：

1、allow_origins：允许跨域请求的域名列表，例如 ['https://example.org', 'https://www.example.org'] 或者 ['*']。

2、allow_origin_regex：允许跨域请求的域名正则表达式，例如 'https://.*\.example\.org'。

3、allow_methods：允许跨域请求的HTTP方法列表，默认为['GET']，['*'] 表示允许所有HTTP方法。

4、allow_headers：跨域请求支持的HTTP头信息列表。['*'] 表示允许所有头信息。Accept, Accept-Language, Content-Language 和 Content-Type头信息默认全都支持。

5、allow_credentials：表示在跨域请求时是否支持cookie，默认为False。

6、expose_headers：表示对浏览器可见的返回结果头信息，默认为[]。

7、max_age：浏览器缓存CORS返回结果的最大时长，默认为600(单位秒)。







#Response Model(响应模型)
>https://www.jianshu.com/p/37854e7db4b7

您可以在任何路径操作中使用参数 `response_model` 声明用于响应的模型：

- `@app.get()`
- `@app.post()`
- `@app.put()`
- `@app.delete()`
- etc.

## 一、`response_model`

### 1\. 举例

```python
from typing import List

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Item(BaseModel):
    name: str
    description: str = None
    price: float
    tax: float = None
    tags: List[str] = []

@app.post("/items/", response_model=Item) async def create_item(item: Item):
    return item
```

**注意**

- `response_model`是“ decorator”方法（`get`，`post`等）的参数。 不像所有参数和主体一样，具有路径操作功能。
- 它接收的类型与您为`Pydantic`模型属性声明的类型相同，因此它可以是`Pydantic`模型，但也可以是例如 一个`Pydantic`模型的清单，例如`List [Item]`。

### 2\. `response_model`功能

FastAPI 将使用 `response_model` 实现以下功能:

- 将输出数据转换为其类型声明。
- 验证数据。
- 在OpenAPI路径操作中为响应添加一个JSON模式。
- 将由自动文档系统使用。

**最重要的功能**：

> **将输出数据限制为模型的数据.**

**技术细节**  
**响应模型在此参数中声明，而不是作为函数返回类型注释声明**

> 因为路径函数实际上可能不会返回该响应模型，而是返回`dict`，数据库对象或其他模型，然后使用`response_model`，执行字段限制和序列化。

## 二、返回相同的输入数据

这里我们定义了一个`UserIn`模型，它会包含一个纯文本的密码：

```python
from fastapi import FastAPI
from pydantic import BaseModel
from pydantic.types import EmailStr

app = FastAPI()

class UserIn(BaseModel):
    username: str
 password: str    email: EmailStr
    full_name: str = None

# Don't do this in production!
# 不要在生产环境中使用这个！
@app.post("/user/", response_model=UserIn)
async def create_user(*, user: UserIn):
    return user
```

我们正在使用此模型声明输入，并使用同一模型声明输出：

```python
from fastapi import FastAPI
from pydantic import BaseModel
from pydantic.types import EmailStr

app = FastAPI()

class UserIn(BaseModel):
    username: str
    password: str
    email: EmailStr
    full_name: str = None

# Don't do this in production!
@app.post("/user/", response_model=UserIn) async def create_user(*, user: UserIn):
    return user
```

现在，每当浏览器使用密码创建用户时，API都会在响应中返回相同的密码。

在这种情况下，这可能不是问题，因为用户自己正在发送密码。

但是，如果我们对另一个路径操作使用相同的模型，则可能会将用户的密码发送给每个客户端。

### 危险

切勿在响应中发送用户的普通密码。

## 三、添加一个输出模型

我们可以改用纯文本密码创建输入模型，而没有明文密码则创建输出模型：

```python
from pydantic import BaseModel
from pydantic.types import EmailStr


class UserIn(BaseModel):
    username: str
    password: str
    email: EmailStr
    full_name: str = None
```

在这里，即使我们的路径操作函数正在返回包含密码的相同输入用户：

```python
from fastapi import FastAPI
from pydantic import BaseModel
from pydantic.types import EmailStr

app = FastAPI()

class UserIn(BaseModel):
    username: str
    password: str
    email: EmailStr
    full_name: str = None

class UserOut(BaseModel):
    username: str
    email: EmailStr
    full_name: str = None

@app.post("/user/", response_model=UserOut)
async def create_user(*, user: UserIn):
 return user 
```

...我们将`response_model`声明为我们的模型'UserOut'，其中不包含密码：

```python
from fastapi import FastAPI
from pydantic import BaseModel
from pydantic.types import EmailStr

app = FastAPI()

class UserIn(BaseModel):
    username: str
    password: str
    email: EmailStr
    full_name: str = None

class UserOut(BaseModel):
    username: str
    email: EmailStr
    full_name: str = None

@app.post("/user/", response_model=UserOut) async def create_user(*, user: UserIn):
    return user
```

因此，**FastAPI** 将负责过滤掉未在输出模型中声明的所有数据（使用Pydantic）。

## 四、查看文档

当您看到自动文档时，可以检查输入模型和输出模型是否都具有自己的JSON模式：

![](//upload-images.jianshu.io/upload_images/12745724-5475520f134055aa.png?imageMogr2/auto-orient/strip|imageView2/2/w/1196/format/webp)

两种模型都将用于交互式API文档：

![](//upload-images.jianshu.io/upload_images/12745724-eeb04befb6121818.png?imageMogr2/auto-orient/strip|imageView2/2/w/1196/format/webp)

## 五、设置响应模型中的参数

### 1\. 响应模型中设置默认值

比如：

```python
from typing import List

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Item(BaseModel):
    name: str
    description: str = None
    price: float
    tax: float = 10.5
    tags: List[str] = [] 

items = {
    "foo": {"name": "Foo", "price": 50.2},
    "bar": {"name": "Bar", "description": "The bartenders", "price": 62, "tax": 20.2},
    "baz": {"name": "Baz", "description": None, "price": 50.2, "tax": 10.5, "tags": []},
}

@app.get("/items/{item_id}", response_model=Item, response_model_skip_defaults=True)
async def read_item(item_id: str):
    return items[item_id]
```

- `description: str = None` 有默认值 `None`.
- `tax: float = 10.5` 有默认值 `10.5`.
- `tags: List[str] = []` 有默认值: `[]`.

但是，如果实际上没有存储它们，则可能要从结果中忽略它们。

例如，如果您的模型在NoSQL数据库中具有很多可选属性，但是您不想发送很长的JSON响应（包含默认值）。

### 2\. 使用 `response_model_skip_defaults` 参数

您可以设置 **path操作装饰器** 参数 `response_model_skip_defaults = True`：

```python
from typing import List

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Item(BaseModel):
    name: str
    description: str = None
    price: float
    tax: float = 10.5
    tags: List[str] = []

items = {
    "foo": {"name": "Foo", "price": 50.2},
    "bar": {"name": "Bar", "description": "The bartenders", "price": 62, "tax": 20.2},
    "baz": {"name": "Baz", "description": None, "price": 50.2, "tax": 10.5, "tags": []},
}

@app.get("/items/{item_id}", response_model=Item, response_model_skip_defaults=True) 
async def read_item(item_id: str):
    return items[item_id]
```

这些默认值将不会包含在响应中。

因此，如果您向ID为`foo`的项目发送请求到该路径操作，则响应（不包括默认值）将为：

```json
{
    "name": "Foo",
    "price": 50.2
}
```

**说明**  
FastAPI使用具有[其skip\_defaults参数](https://links.jianshu.com/go?to=https%3A%2F%2Fpydantic-docs.helpmanual.io%2F%23copying)的Pydantic模型的`.dict（）`来实现此目的。

#### a. 具有默认值的字段的值的数据

但是如果您的数据具有默认值的模型字段值，例如ID为bar的项目：

```json
{
    "name": "Bar",
    "description": "The bartenders",
    "price": 62,
    "tax": 20.2 }
```

它们将包含在响应中。

#### b. 数据具有与默认值相同的值

如果数据具有与默认值相同的值，例如ID为`baz`的项：

```json
{
    "name": "Baz",
    "description": None,
    "price": 50.2,
    "tax": 10.5,
    "tags": [] 
}
```

> FastAPI足够聪明（实际上，Pydantic足够聪明）可以认识到，即使`description`，`tax`和`tags`与默认值具有相同的值，它们也是显式设置的（而不是取自默认值） 。

因此，它们将包含在JSON响应中。

**注意**

> **默认值可以是任何值，不仅可以是None。**  
> 例如可以是一个 list (`[]`), 一个 `float` 值为`10.5`, 等等.

### 3\. `response_model_include` 和 `response_model_exclude`

您还可以使用**path操作装饰器** 参数`response_model_include`和`response_model_exclude`。

他们使用带有属性名称的`str`集`str`来**包括**（省略其余部分）或**排除**（包括其余部分）。

如果您只有一个`Pydantic`模型，并且想要从输出中删除一些数据，则可以将其用作快速捷径。

**注意**

但是仍然建议使用上述想法，使用多个类而不是这些参数。

这是因为即使您使用`response_model_include`或`response_model_exclude`来省略某些属性，**在应用程序的OpenAPI（和文档）中生成的JSON模式仍将是完整模型的JSON模式**。

```python
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Item(BaseModel):
    name: str
    description: str = None
    price: float
    tax: float = 10.5

items = {
    "foo": {"name": "Foo", "price": 50.2},
    "bar": {"name": "Bar", "description": "The Bar fighters", "price": 62, "tax": 20.2},
    "baz": {
        "name": "Baz",
        "description": "There goes my baz",
        "price": 50.2,
        "tax": 10.5,
    },
}

@app.get("/items/{item_id}/name", response_model=Item, response_model_include={"name", "description"}, )
async def read_item_name(item_id: str):
    return items[item_id]

@app.get("/items/{item_id}/public", response_model=Item, response_model_exclude={"tax"})
 async def read_item_public_data(item_id: str):
    return items[item_id]
```

**注意：**

> 语法{“ name”，“ description”}\`用这两个值创建一个set。
> 
> 相当于 `set(["name", "description"])`.

#### 4\. 使用 `list`代替`set`

如果您忘记使用`set`而是使用`list`或`tuple`，则FastAPI仍会将其转换为`set`，并且可以正常工作：

```python
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Item(BaseModel):
    name: str
    description: str = None
    price: float
    tax: float = 10.5

items = {
    "foo": {"name": "Foo", "price": 50.2},
    "bar": {"name": "Bar", "description": "The Bar fighters", "price": 62, "tax": 20.2},
    "baz": {
        "name": "Baz",
        "description": "There goes my baz",
        "price": 50.2,
        "tax": 10.5,
    },
}

@app.get(
    "/items/{item_id}/name",
    response_model=Item,
 response_model_include=["name", "description"], )
async def read_item_name(item_id: str):
    return items[item_id]

@app.get("/items/{item_id}/public", response_model=Item, response_model_exclude=["tax"]) async def read_item_public_data(item_id: str):
    return items[item_id]
```

## 六、概括

使用路径操作修饰符的参数`response_model`定义响应模型，尤其是确保私有数据被过滤掉。  
使用`response_model_skip_defaults`仅返回显式设置的值。

1人点赞

[fastapi官方文档翻译](https://www.jianshu.com/nb/39726436)



#response-change-status-code

## Use a `Response` parameter[¶](https://fastapi.tiangolo.com/advanced/response-change-status-code/#use-a-response-parameter "Permanent link")

You can declare a parameter of type `Response` in your _path operation function_ (as you can do for cookies and headers).

And then you can set the `status_code` in that _temporal_ response object.

```
from fastapi import FastAPI, Response, status

app = FastAPI()

tasks = {"foo": "Listen to the Bar Fighters"}


@app.put("/get-or-create-task/{task_id}", status_code=200)
def get_or_create_task(task_id: str, response: Response):
    if task_id not in tasks:
        tasks[task_id] = "This didn't exist before"
        response.status_code = status.HTTP_201_CREATED
    return tasks[task_id]
```
And then you can return any object you need, as you normally would (a `dict`, a database model, etc).

And if you declared a `response_model`, it will still be used to filter and convert the object you returned.

**FastAPI** will use that _temporal_ response to extract the status code (also cookies and headers), and will put them in the final response that contains the value you returned, filtered by any `response_model`.

You can also declare the `Response` parameter in dependencies, and set the status code in them. But have in mind that the last one to be set will win.