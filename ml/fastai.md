#setup

安装Anaconda,启动Anaconda Prompt (Anaconda)

> conda国内很慢，可以使用清华镜像加速
```
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main/
conda config --set show_channel_urls yes
```

1. 确保你的 conda 版本已经更新到最新：

```
conda update conda
```

2. 安装每日编译 nightly 的 PyTorch，注意 cuda 的版本要和你自己的系统保持一致，比如在 CUDA 9.2 上安装：

```
conda install -c pytorch pytorch-nightly cuda92
```
如果你的系统没有安装 cuda，那么可以通过下面的命令安装 cpu 版本的 PyTorch：

```
conda install -c pytorch pytorch-nightly-cpu
```
3. 安装 fastai

```
conda install -c fastai fastai
```




---
or 
>https://pytorch.org/get-started/locally/

*error: Microsoft Visual C++ 14.0 is required. Get it with "Build Tools for V
isual Studio": https://visualstudio.microsoft.com/downloads/*

download and setup https://go.microsoft.com/fwlink/?LinkId=691126.


#name 'ConvLearner' is not defined

在最新的fast ai包中，ConvLearner已经被create_cnn取代，所以替换为下列语句就好了：
```
learn = create_cnn(data, models.resnet34, metrics=error_rate)
```
更新的fast ai包中，create_cnn已经被cnn_learner取代。