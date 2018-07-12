#https://jdhao.github.io/2017/09/04/install-gcc-newer-version-on-centos/
```
[root@localhost gcc-7.3.0]# curl https://ftp.gnu.org/gnu/gcc/gcc-7.3.0/gcc-7.3.0.tar.gz -O
tar -zxvf gcc-7.3.0.tar.gz
yum install gmp-devel mpfr-devel libmpc-devel
./configure --enable-languages=c,c++ --disable-multilib
make -j$(nproc) && make install
```
You should add the install dir of GCC to your PATH and LD_LIBRARY_PATH in order to use the newer GCC. Add the following settings to /etc/profile:
```
export PATH=/usr/local/bin:$PATH
export LD_LIBRARY_PATH=/usr/local/lib64:$LD_LIBRARY_PATH
```
Maybe a restart of your current session is also needed.


**Inorder to compile Orignal C you need 
```
cd /usr/local/bin
ln /usr/local/bin/gcc cc
ln /usr/local/libexec/gcc/x86_64-pc-linux-gnu/7.3.0/cc1 cc1
```
