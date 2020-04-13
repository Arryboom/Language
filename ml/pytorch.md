# deploy on production
>https://discuss.pytorch.org/t/pytorch-models-for-production/18115


### 1

It depends on the platform to which you’re aiming to deploy and some other constraints, for example if your use case can be fulfilled via REST or similar service and you don’t mind the python overhead you could potentially use PyTorch as it is on a server to handle web requests. However if you’re aiming for edge deployment or you want to squeeze as much raw performance as possible, your best bet right now is to use ONNX 30. You can export 5 your PyTorch model in ONNX format and then use another framework (like caffe2, MXNet, CNTK, etc.) to actually run the model, these other frameworks do support edge and/or have specialized deployment extensions (e.g. the MXNet model server 20 which can also serve ONNX models directly).

BTW PyTorch 1.0 58 is coming and will be production-ready, and I’m very excited about that! :smiley:

### 2

>https://github.com/NirantK/pytorch-web-deploy.git


### 3

>https://www.cnblogs.com/Arborday/p/9890999.html

python的易上手和pytorch的动态图特性，使得pytorch在学术研究中越来越受欢迎，但在生产环境，碍于python的GIL等特性，可能达不到高并发、低延迟的要求，存在需要用c++接口的情况。除了将模型导出为ONNX外，pytorch1.0给出了新的解决方案：pytorch 训练模型 - 通过torch script中间脚本保存模型 -- C++加载模型。最近工作需要尝试做了转换，总结一下步骤和遇到的坑。

用torch script把torch模型转成c++接口可读的模型有两种方式：trace && script. trace比script简单，但只适合结构固定的网络模型，即forward中没有控制流的情况，因为trace只会保存运行时实际走的路径。如果forward函数中有控制流，需要用script方式实现。

trace顾名思义，就是沿着数据运算的路径走一遍，官方例子：

import torch
def foo(x, y):
    return 2*x + y
traced_foo = torch.jit.trace(foo, (torch.rand(3), torch.rand(3)))
 

 

 

 

script稍复杂，主要改三处：

1. Model由之前继承 nn.Model 改为继承 torch.jit.ScriptModule

2. forward函数前加 @torch.jit.script_method

3. 其他需要调用的函数前加 @torch.jit.script

 

踩过的坑&&解决方法：

A. torch script默认函数或方法的参数都是Tensor类型的，如果不是需要说明，不然调用非Tensor参数时会报类型不符的编译错误。

python3可以直接:

def example_func(param_1: Tensor, param_2: int, param_3: List[int]):
 

 

python2需要用type注释：

def example_func(param_1, param_2, param_3):

#type: (Tensor, int, List[int]) -> Tensor

 

 

 

B. model的方法中forward加@torch.jit.script_method， __init__函数不用

C. 前面说过，torch scrip支持的函数是pytorch的子集，意味着有一部分函数不支持，例如： not boolean，pass, List的切片赋值，CPU和GPU切换的value.to( ), 需要想办法绕过去。看github上讨论区说新版好像已经支持not操作了，没有验证。

 

结论：pytorch 1.0目前的预览版还有比较多优化的空间，至少是在torch script支持的函数集合上，不建议使用，等稳定版发布再看看吧。
