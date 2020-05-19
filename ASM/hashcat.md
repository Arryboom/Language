Your hash is base64 coded. You must decode this before use john. You can use the command :

base64 -d <hash>
If you want to be sure of the hash format, you can use :
```
hash-identifier
```

# Hashcat NTLM Hash Brute Force Notes

>https://cyberloginit.com/2017/12/26/hashcat-ntlm-brute-force.html

Dec 26, 2017

## Abstact

Notes on brute-force Windows NTLM Hash with hashcat on a Windows/Linux machine with decent graphics card/cards.

## Environment

-   Latest graphics card driver installed;
-   A text file `hash.txt` of all the NTLM hash like this \`aad3b435b51404eeaad3b435b51404ee’, one for each line.

## Benchmark

Just grab the latest copy of hashcat from [here](https://hashcat.net/hashcat/), extract it and you are good to go.

Then, copy the text file of the NTLM hash to the root of the hashcat directory.

Run

```
hashcat64.bin(on Linux) -b
hashcat64.exe(on Windows) -b
```

to simply run a benchmark, and to also make sure that the graphics card driver are properly recognized.

## Brute-Force

### Options

Some of the most commonly used options are as follows:

Options Short, Long

Type

Description

Example

\-m, –hash-type

Num

Hash-type, see references below

\-m 1000

\-a, –attack-mode

Num

Attack-mode, see references below

\-a 3

\-b, –benchmark

 

Run benchmark

 

\-O, –optimized-kernel-enable

 

Enable optimized kernels (limits password length)

 

\-w, –workload-profile

Num

Enable a specific workload profile, see pool below

\-w 3

\-1, –custom-charset1

CS

User-defined charset ?1

\-1 ?l?d?u

\-2, –custom-charset2

CS

User-defined charset ?2

\-2 ?l?d?s

\-i, –increment

 

Enable mask increment mode

 

–increment-min

Num

Start mask incrementing at X

–increment-min=4

–increment-max

Num

Stop mask incrementing at X

–increment-max=8

`-m` specifies the hash mode, i.e., LM(3000), NTLM(1000), MD5(0), SHA1(100);

`-a` is for the attack mode, they are

Num

Mode

0

Straight

1

Combination

3

Brute-force

6

Hybrid Wordlist + Mask

7

Hybrid Mask + Wordlist

we will use `3` Brute-force in our case;

`-w` sets the workload profile

Num

Performance

Runtime

Power Consumption

Desktop Impact

1

Low

2 ms

Low

Minimal

2

Default

12 ms

Economic

Noticeable

3

High

96 ms

High

Unresponsive

4

Nightmare

480 ms

Insane

Headless

You may choose the Nightmare mode if on a dedicated machine;

`-1/2/3` creates customized charsets which can be used later in the password mask

Hashcat has the following built-in charsets:

?

Charset

 

l

abcdefghijklmnopqrstuvwxyz

 

u

ABCDEFGHIJKLMNOPQRSTUVWXYZ

 

d

0123456789

 

h

0123456789abcdef

 

H

0123456789ABCDEF

 

s

!”#$%&’()\*+,-./:;<=>?@\[\]^\_\`{

}~

a

?l?u?d?s

 

b

0x00 - 0xff

 

you may use these charsets in your password mask directly.

I.E. for numeric, 6 character long password NTLM hash:

```
.\hashcat64.exe -m 1000 -a 3 hash.txt ?d?d?d?d?d?d
```

As you can see, in brute-fore mode, the string `?d?d?d?d?d?d` is the mask,

the charset of each position can be designated with a `?` followed by its charset.

`?d?d?d?d?d?d` means that hashcat should try every possible numeric strings of length 6.

You can change the charset of any position to reduce the overall complexity.

For a complex password consisting of both lowercase, numbers and special characters,

you can use

```
-1 ?l?d?s
```

for convenience.

That is

```
.\hashcat64.exe -m 1000 -a 3 hash.txt -1 ?l?d?s ?1?1?1?1?1?1
```

if the password is still 6 characters long.

`-i` enables mask increment mode, for example

```
.\hashcat64.exe -m 1000 -a 3 hash.txt ?d?d?d?d?d?d
```

only tries combinations that are 6 characters long.

With `-i` set, hashcat will start from 1 character to 6 characters.

You can also set the minimum length with `--increment-min=[length]` and the maxmum length with `--increment-max=[length]`.

### Attack

In our case, the NTLM hash is obtained with the metasploit framework module `post/windows/gather/hashdump` executed in a meterpreter reverse shell.

```
[*] Obtaining the boot key...
[*] Calculating the hboot key using SYSKEY []...
[*] Obtaining the user list and keys...
[*] Decrypting user keys...
[*] Dumping password hints...

No users with password hints on this system

[*] Dumping password hashes...


Administrator:500:aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0:::
Guest:501:aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0:::
[username]:1000:aad3b435b51404eeaad3b435b51404ee:0b82e1dace77e29dd1de00896ba1c5bc:::
```

Columns are separated by colon `:`, the 3rd one is LM(LAN Manager) hash, which has been deprecated since Windows Vista.

The 4th one `0b82e1dace77e29dd1de00896ba1c5bc` is the NTLM(NT LAN Manager) hash used by modern Windows operating systems,

and that is exactly what we are trying to brute-force here.

First create the `hash.txt` file

```
echo "0b82e1dace77e29dd1de00896ba1c5bc" > hash.txt
```

As we have no idea how long the password is, nor what characters may have been used,

we make an assumption that the length is between 5 and 9 , and only lowercase and numbers are used.

Use the custom charset

```
-1 ?l?d
```

This should word for most non tech-savvy people.

Then run

```
./hashcat64.bin -m 1000 -a 3 -w 3 -O hash.txt -1 ?l?d ?1?1?1?1?1?1?1?1?1 -i --increment-min=5
```

Finally we will get the result `qqqqqq`.

This should be pretty quick if your graphics cards have enough horse power.

## Reference

-   [https://www.4armed.com/blog/perform-mask-attack-hashcat/](https://www.4armed.com/blog/perform-mask-attack-hashcat/)















#about CUDA


### Updating GPU Drivers from the Command-Line (Recommended)

1.  Access the Terminal.
2.  Run `sudo lshw -c display` OR `sudo lshw -c video` to display the Ubuntu 18.04 stock-drivers loaded for the Nvidia GeForce GTX 1080 GPU cards.
3.  Since we haven’t installed any drivers from the command-line yet, the driver-detail within the configuration field should display “driver=nouveau”.
4.  Run `sudo ubuntu-drivers devices`
5.  After running the above command, only two drivers will be displayed (e.g. ‘nvidia-driver-390 – distro non-free recommended’ and ‘xserver-xorg-video-nouveau – distro free builtin’); we want the latter ‘non-free recommended’- so run `sudo apt install nvidia-driver-version-number` (e.g. `sudo apt install nvidia-driver-390`).
6.  After installing the drivers from the command-line, reboot the computer by running `sudo shutdown -r now`.
7.  If after rebooting your welcome-screen hangs, click on the settings-wheel icon, select the “Ubuntu on wayland” and then re-enter password.
8.  Access the terminal and run `sudo lshw -c display`; the driver field should now display “nvidia” instead of “nouveau”.




###driver fail

> https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#i_may_have_the_wrong_driver_installed_what_should_i_do


## I may have the wrong driver installed, what should I do?

(short URL: [https://hashcat.net/faq/wrongdriver](https://hashcat.net/faq/wrongdriver "https://hashcat.net/faq/wrongdriver"))

1. Completely uninstall the current driver
    
    - Windows: use software center
        
    - Linux:
        
        - NVIDIA: nvidia-uninstall
            
        - AMD: amdconfig --uninstall=force
            
        - If you installed the driver via a package manager (Linux), then you need to remove these packages too
            
        - Make sure to purge those package, not to just uninstall them
            
2. Reboot
    
3. For Windows only: download and start Driver Fusion (free version is enough; select “Display”, AMD/NVidia/Intel, ignore the warning about Premium version), then Reboot
    
4. Make sure that no Intel OpenCL SDK, AMD-APP-SDK or CUDA-SDK framework is installed – if it is installed, uninstall it!
    
5. For Windows only: manually delete remaining OpenCL.dll, OpenCL32.dll, OpenCL64.dll files on all folders. You should find at least 2. They usually reside in “c:\\windows\\syswow64” and “c:\\windows\\system32”. This step is very important!
    
6. For Linux only:
    
    - dpkg -S libOpenCL to find all packages installed that provide a libOpenCL, then purge them
        
    - find / -name libOpenCL\\\* -print0 | xargs -0 rm -rf
        
7. Reboot
    
8. For Linux only: apt-get install ocl-icd-libopencl1 opencl-headers clinfo
    
9. Install the driver recommended on [https://hashcat.net/hashcat/](https://hashcat.net/hashcat/ "https://hashcat.net/hashcat/"). If it says \*exact\* it means exact.
    
    - For AMD GPUs, see ROCm instructions [here](https://rocm.github.io/ROCmInstall.html "https://rocm.github.io/ROCmInstall.html").
        
10. Reboot
    
11. For Linux only: rm -rf ~/.hashcat/kernels
    
12. Reinstall hashcat, choose:
    
    - Stable version: Download and extract (under Linux, make sure to use: “7z x” to extract) the newest hashcat from [https://hashcat.net/](https://hashcat.net/ "https://hashcat.net/")
        
    - Beta version: [https://hashcat.net/beta/](https://hashcat.net/beta/ "https://hashcat.net/beta/")
        
    - Development version: git clone [https://github.com/hashcat/hashcat](https://github.com/hashcat/hashcat "https://github.com/hashcat/hashcat")
        
13. For Linux only: try to run “clinfo” first in your terminal
    
14. Try to run hashcat --benchmark
    

## What does the cuModuleLoad() 209 error mean?

> Official SDK ErrorCode: CUDA\_ERROR\_NO\_BINARY\_FOR\_GPU = 209

This error can simply mean that the version of the driver that you have installed is too old:

- Solution: [How does one install the correct driver for the GPU(s)?](https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#how_does_one_install_the_correct_driver_for_the_gpu_s "frequently_asked_questions")
    

However, it can also be worse than just that. The 209 error means that there was no precompiled kernel found in an existing precompiled kernel container file (the ones that end with .ptx). This sometimes happens with new GPUs that are not yet supported by hashcat.

- Solution: Please update your ForceWare driver to the latest one (not the recommended one)
    

If the problem still exist:

- Solution: Open an issue on [github](https://github.com/hashcat/hashcat/issues/ "https://github.com/hashcat/hashcat/issues/"). We will add support for your GPU with the next release.
    

## What does the cuModuleLoad() 301 error mean?

Official SDK ErrorCode: CUDA\_ERROR\_FILE\_NOT\_FOUND = 301

You've unpacked the hashcat.7z archive using the command “7z e”. This destroys the directory structure.

- Solution: Please use the **x** flag to decompress 7z archives. For example: “7z x”
    

NVidia added a completely new ShaderModel which is not yet support by hashcat.

- Solution: Please update your ForceWare driver to the latest one (not the recommended one)
    

If the problem still exist:

- Solution: Open an issue on [github](https://github.com/hashcat/hashcat/issues/ "https://github.com/hashcat/hashcat/issues/"). We will add your GPU with the next release.
    

## What does the clGetPlatformIDs() -64 error mean?

This is the typical error message you get when you're AMD driver installation is faulty. You need to reinstall the recommended driver (while making sure that there is no older driver libs conflicting with it).

- Solution: [I may have the wrong driver installed, what should I do?](https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#i_may_have_the_wrong_driver_installed_what_should_i_do "frequently_asked_questions")
    

## What does the clGetPlatformIDs() -1001 error mean?

This is the typical error message you get when your AMD driver installation is faulty. You need to reinstall the recommended driver.

- Solution: [I may have the wrong driver installed, what should I do?](https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#i_may_have_the_wrong_driver_installed_what_should_i_do "frequently_asked_questions")
    

## What does the clGetDeviceIDs() -1 Error error message mean?

Receiving this error means that hashcat could not detect any capable GPUs in your system. If you are positive that you have capable GPUs you can try the following suggestions to fix your system:

- #define CL\_DEVICE\_NOT\_FOUND -1
    
- Are you logged in as the user who has opened the X session?
    
- Did you use “export DISPLAY=:0”?
    
- Do you have X Authority and/or disable X11 ACLs with “xhost +”?
    
- Is your display manager configured correctly? (gdm example config: [http://www.sch0.org/gdm.conf](http://www.sch0.org/gdm.conf "http://www.sch0.org/gdm.conf") )
    
- Did you use the recommended driver (for the hashcat version you use) mentioned on the download page? [I may have the wrong driver installed, what should I do?](https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#i_may_have_the_wrong_driver_installed_what_should_i_do "frequently_asked_questions")
    

## What does the clBuildProgram() -11 error mean?

Official SDK ErrorCode: CL\_BUILD\_PROGRAM\_FAILURE -11

This means you are using an incompatible driver version. Sometimes the structure inside the .llvmir archive changes. This is something the developers of hashcat have no influence on. This structure is dictated by the driver.

- Solution: Make sure you have the latest hashcat version
    
- Solution: [I may have the wrong driver installed, what should I do?](https://hashcat.net/wiki/doku.php?id=frequently_asked_questions#i_may_have_the_wrong_driver_installed_what_should_i_do "frequently_asked_questions")
    

## What does the clCreateBuffer() -61 error mean?

Official SDK ErrorCode: CL\_INVALID\_BUFFER\_SIZE -61

This is the typical “Out-Of-Memory” Error, and is not driver related. Note that this refers to your GPU memory, not host memory. Also note that there is a maximum allocation size which is typically only 256MB. That means even if your GPU has 4GB ram, hashcat is allowed only to allocate 256MB per buffer.

Solution:

- Reduce -n
    
- Reduce -w
    
- Reduce number of hashes
    

## What does the clEnqueueCopyBuffer() -30 error mean?

Official SDK ErrorCode: CL\_INVALID\_VALUE -30

This is not a driver related error. This error occurs mostly in cases where you are using too many rules. You can adjust the following parameters to reduce your total rule count:

- decrease -g value
    
- decrease number of -r stacks
    

## What does the cuStreamSynchronize() 702 error mean?

This is the same error type as OpenCL -30 Error. It is not a driver related error. This error occurs mostly in cases where you are using too many rules. You can adjust the following parameters to reduce your total rule count:

- decrease -g value
    
- decrease number of -r stacks