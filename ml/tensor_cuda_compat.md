tensorflow works with cuda10.1 by official comments now,but it could support cuda10.2 and cuda11 on both linux/windows too.
just create a Symlinking


CUDA 10.2 should be compatible with CUDA 10.1. We are building the official pips with CUDA 10.1 as we already changed infrastructure a lot to enable Python3.8 pips. Next release will have infrastructure changed for newer CUDA versions.

Until then, you can try compiling from source, or symlinking the libraries.



UPDATE: WARNING in [#34759 (comment)](https://github.com/tensorflow/tensorflow/issues/34759#issuecomment-633819017)

The symlink works for me too, details below (installed on Ubuntu 20.04):

- actual 10.2 libcudart code is in `/usr/local/cuda-10.2/`
- the tensorflow 2.2 code looks in a number of places (and fails to find it in all of them)

```
strace -o test1.log /usr/bin/python .../quick_tour.py
...
openat(AT_FDCWD, "/home/peter_v/.local/lib/python3.8/site-packages/tensorflow/python/../libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/home/peter_v/.local/lib/python3.8/site-packages/tensorflow/python/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/home/peter_v/.local/lib/python3.8/site-packages/tensorflow/python/../libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/etc/ld.so.cache", O_RDONLY|O_CLOEXEC) = 20
fstat(20, {st_mode=S_IFREG|0644, st_size=83403, ...}) = 0
mmap(NULL, 83403, PROT_READ, MAP_PRIVATE, 20, 0) = 0x7fb8ad602000
close(20)                               = 0
openat(AT_FDCWD, "/lib/x86_64-linux-gnu/tls/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/lib/x86_64-linux-gnu/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/usr/lib/x86_64-linux-gnu/tls/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/usr/lib/x86_64-linux-gnu/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/lib/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
openat(AT_FDCWD, "/usr/lib/libcudart.so.10.1", O_RDONLY|O_CLOEXEC) = -1 ENOENT (No such file or directory)
```

Somewhat at random, I decided to symlink from `/usr/lib/x86_64-linux-gnu/` to the libcudart.so.10.2 file.

```
sudo ln -s /usr/local/cuda-10.2/targets/x86_64-linux/lib/libcudart.so.10.2 /usr/lib/x86_64-linux-gnu/libcudart.so.10.1
```

I am actually using mostly the CPU (my 8 core CPU seems faster than a smallish laptop GPU and also the GPU runs easily into OOM for real work-loads).



Just to confirm, symlink idea works on Windows too. I symlinked C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v10.2\bin\cudart64_102.dll as cudart64_101.dll in the same folder.



>https://github.com/tensorflow/tensorflow/issues/38194








#to enable cuda on windows

you need both install

- video driver
- cuda 
- cudnn

then use tensorflow,sometimes default setting doesn't work you may need ``pip install tensorflow-gpu``.