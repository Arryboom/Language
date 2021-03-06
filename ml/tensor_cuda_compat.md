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





#after cuda installed,use following command to check 
```
C:\Users\root>nvcc -V
nvcc: NVIDIA (R) Cuda compiler driver
Copyright (c) 2005-2019 NVIDIA Corporation
Built on Sun_Jul_28_19:12:52_Pacific_Daylight_Time_2019
Cuda compilation tools, release 10.1, V10.1.243

C:\Users\root>nvidia-smi
Wed Jun 10 15:10:41 2020
+-----------------------------------------------------------------------------+
| NVIDIA-SMI 446.14       Driver Version: 446.14       CUDA Version: 11.0     |
|-------------------------------+----------------------+----------------------+
| GPU  Name            TCC/WDDM | Bus-Id        Disp.A | Volatile Uncorr. ECC |
| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |
|===============================+======================+======================|
|   0  GeForce GTX 108... WDDM  | 00000000:21:00.0  On |                  N/A |
|  0%   39C    P8    20W / 250W |    636MiB / 11264MiB |      0%      Default |
+-------------------------------+----------------------+----------------------+

+-----------------------------------------------------------------------------+
| Processes:                                                                  |
|  GPU                  PID   Type   Process name                  GPU Memory |
|                                                                  Usage      |
|=============================================================================|
|    0                 2996    C+G   Insufficient Permissions        N/A      |
|    0                 3052    C+G   ...y\ShellExperienceHost.exe    N/A      |
|    0                10236    C+G   C:\Windows\explorer.exe         N/A      |
|    0                11632    C+G   ...w5n1h2txyewy\SearchUI.exe    N/A      |
|    0                12496    C+G   ...cw5n1h2txyewy\LockApp.exe    N/A      |
|    0                14744    C+G   ...me\Application\chrome.exe    N/A      |
|    0                15428    C+G   ...es.TextInput.InputApp.exe    N/A      |
|    0                19560    C+G   Insufficient Permissions        N/A      |
|    0                20504    C+G   ...p-2.5.2\GitHubDesktop.exe    N/A      |
|    0                22996    C+G   Insufficient Permissions        N/A      |
|    0                23136    C+G   Insufficient Permissions        N/A      |
+-----------------------------------------------------------------------------+

C:\Users\root>
```