#dis

dis redis库是python(默认的CPython)自带的一个库,可以用来分析字节码


首先导入dis库
```
>>> import dis
```
然后在repl中,创建一个函数
```
>>> def add(a, b = 0):
...     return a + b
... 
>>>
``` 
最后将add函数传给dis库的dis函数
```
>>> dis.dis(add)
  2           0 LOAD_FAST                0 (a)
              2 LOAD_FAST                1 (b)
              4 BINARY_ADD
              6 RETURN_VALUE
>>> 
```