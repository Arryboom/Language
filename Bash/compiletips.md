# Install mingw on ubuntu
``sudo apt-get install mingw-w64``

#MakeFile
https://en.wikipedia.org/wiki/Makefile


#ccmake not found
``sudo apt-get install cmake-curses-gui``

#gcc undefined error on ubuntu


Guys thank you for your comments. The problem as i found that is because the default behavior of gcc is changed in Ubuntu (at-least the version i am using) The hint is on this wiki page of debian https://wiki.debian.org/ToolChain/DSOLinking

According to this the gcc is changed to add --as-needed to the linker. The down side of this behavior is that and i quote the wiki " Binaries, which are using symbols from an indirectly linked shared library will fail to link "

This was exactly the problem with me, as libconvert was using libnuma, but not linked to it, and the binary i was building tried to link everything including both libconvert and libnuma to it. The default gcc would work because it uses no-as-needed behavior which is changed in Debian and made it to Ubuntu as well.

So in short the linking works if i add -Wl,--no-as-needed.

```gcc -Wl,--no-as-needed -L/home/sam/gmdb/lib -L/home/sam/db/add-ons/lib -L/home/sam/convert/lib -L/home/sam/rtana/lib -L/home/sam/rtana/add-ons/lib -o /home/sam/gmdb/bin/server /home/sam/db/obj/tools/server/server.o /home/sam/db/obj/tools/common/tool_data_parse.o /home/sam/db/obj/tools/common/tool_param.o /home/sam/gmdb/obj/tools/common/tool_public.o -lgmcommon -L/home/sam/db/add-ons/vpp/lib/suse -lipsi_crypto -lipsi_osal -lipsi_pse -lipsi_ssl -lgmmd5 -lgmkernel -lgmpl -lgmrep -lgmsqlserver -lgmsqlclient -lconvert -lrtana -lglog -lgflags -lprotobuf -lre2 -lboost_timer -lnuma -lpthread -lm -lrt```

>https://stackoverflow.com/questions/21140269/gcc-undefined-reference-error-on-ubuntu


#check all compile variable 
jump to your project dir,``cmake -LA``

will show something like:

```
x@x:~/Desktop/libbpg-0.9.8/x265/build/linux$ cmake -LA
CMake Warning:
  No source or binary directory provided.  Both will be assumed to be the
  same as the current working directory, but note that this warning will
  become a fatal error in future CMake releases.


CMake Error: The source directory "/home/jjj/Desktop/libbpg-0.9.8/x265/build/linux" does not appear to contain CMakeLists.txt.
Specify --help for usage, or press the help button on the CMake GUI.
-- Cache values
BIN_INSTALL_DIR:STRING=bin
CHECKED_BUILD:BOOL=OFF
CMAKE_AR:FILEPATH=/usr/bin/ar
CMAKE_ASM_YASM_FLAGS:STRING=
CMAKE_ASM_YASM_FLAGS_DEBUG:STRING=
CMAKE_ASM_YASM_FLAGS_MINSIZEREL:STRING=
CMAKE_ASM_YASM_FLAGS_RELEASE:STRING=
CMAKE_ASM_YASM_FLAGS_RELWITHDEBINFO:STRING=
CMAKE_BUILD_TYPE:STRING=Release
CMAKE_COLOR_MAKEFILE:BOOL=ON
CMAKE_CXX_COMPILER:FILEPATH=/usr/bin/c++
CMAKE_CXX_COMPILER_AR:FILEPATH=/usr/bin/gcc-ar-8
CMAKE_CXX_COMPILER_RANLIB:FILEPATH=/usr/bin/gcc-ranlib-8
CMAKE_CXX_FLAGS:STRING=
CMAKE_CXX_FLAGS_DEBUG:STRING=-g
CMAKE_CXX_FLAGS_MINSIZEREL:STRING=-Os -DNDEBUG
CMAKE_CXX_FLAGS_RELEASE:STRING=-O3 -DNDEBUG
CMAKE_CXX_FLAGS_RELWITHDEBINFO:STRING=-O2 -g -DNDEBUG
CMAKE_C_COMPILER:FILEPATH=/usr/bin/cc
CMAKE_C_COMPILER_AR:FILEPATH=/usr/bin/gcc-ar-8
CMAKE_C_COMPILER_RANLIB:FILEPATH=/usr/bin/gcc-ranlib-8
CMAKE_C_FLAGS:STRING=
CMAKE_C_FLAGS_DEBUG:STRING=-g
CMAKE_C_FLAGS_MINSIZEREL:STRING=-Os -DNDEBUG
CMAKE_C_FLAGS_RELEASE:STRING=-O3 -DNDEBUG
CMAKE_C_FLAGS_RELWITHDEBINFO:STRING=-O2 -g -DNDEBUG
CMAKE_EXE_LINKER_FLAGS:STRING=
CMAKE_EXE_LINKER_FLAGS_DEBUG:STRING=
CMAKE_EXE_LINKER_FLAGS_MINSIZEREL:STRING=
CMAKE_EXE_LINKER_FLAGS_RELEASE:STRING=
CMAKE_EXE_LINKER_FLAGS_RELWITHDEBINFO:STRING=
CMAKE_EXPORT_COMPILE_COMMANDS:BOOL=OFF
CMAKE_INSTALL_PREFIX:PATH=/usr/local
CMAKE_LINKER:FILEPATH=/usr/bin/ld
CMAKE_MAKE_PROGRAM:FILEPATH=/usr/bin/make
CMAKE_MODULE_LINKER_FLAGS:STRING=
CMAKE_MODULE_LINKER_FLAGS_DEBUG:STRING=
CMAKE_MODULE_LINKER_FLAGS_MINSIZEREL:STRING=
CMAKE_MODULE_LINKER_FLAGS_RELEASE:STRING=
CMAKE_MODULE_LINKER_FLAGS_RELWITHDEBINFO:STRING=
CMAKE_NM:FILEPATH=/usr/bin/nm
CMAKE_OBJCOPY:FILEPATH=/usr/bin/objcopy
CMAKE_OBJDUMP:FILEPATH=/usr/bin/objdump
CMAKE_RANLIB:FILEPATH=/usr/bin/ranlib
CMAKE_SHARED_LINKER_FLAGS:STRING=
CMAKE_SHARED_LINKER_FLAGS_DEBUG:STRING=
CMAKE_SHARED_LINKER_FLAGS_MINSIZEREL:STRING=
CMAKE_SHARED_LINKER_FLAGS_RELEASE:STRING=
CMAKE_SHARED_LINKER_FLAGS_RELWITHDEBINFO:STRING=
CMAKE_SKIP_INSTALL_RPATH:BOOL=NO
CMAKE_SKIP_RPATH:BOOL=NO
CMAKE_STATIC_LINKER_FLAGS:STRING=
CMAKE_STATIC_LINKER_FLAGS_DEBUG:STRING=
CMAKE_STATIC_LINKER_FLAGS_MINSIZEREL:STRING=
CMAKE_STATIC_LINKER_FLAGS_RELEASE:STRING=
CMAKE_STATIC_LINKER_FLAGS_RELWITHDEBINFO:STRING=
CMAKE_STRIP:FILEPATH=/usr/bin/strip
CMAKE_VERBOSE_MAKEFILE:BOOL=FALSE
DETAILED_CU_STATS:BOOL=OFF
ENABLE_AGGRESSIVE_CHECKS:BOOL=OFF
ENABLE_ASSEMBLY:BOOL=ON
ENABLE_CLI:BOOL=ON
ENABLE_LIBNUMA:BOOL=ON
ENABLE_PIC:BOOL=ON
ENABLE_PPA:BOOL=OFF
ENABLE_SHARED:BOOL=ON
ENABLE_TESTS:BOOL=OFF
ENABLE_VTUNE:BOOL=OFF
EXPORT_C_API:BOOL=ON
EXTRA_LIB:STRING=
EXTRA_LINK_FLAGS:STRING=
FPROFILE_GENERATE:BOOL=OFF
FPROFILE_USE:BOOL=OFF
FSANITIZE:STRING=
GIT_EXECUTABLE:FILEPATH=/usr/bin/git
HG_EXECUTABLE:FILEPATH=HG_EXECUTABLE-NOTFOUND
HIGH_BIT_DEPTH:BOOL=OFF
LIBDL:FILEPATH=/usr/lib/x86_64-linux-gnu/libdl.so
LIBRT:FILEPATH=/usr/lib/x86_64-linux-gnu/librt.so
LIB_INSTALL_DIR:STRING=lib
LINKED_10BIT:BOOL=OFF
LINKED_12BIT:BOOL=OFF
LINKED_8BIT:BOOL=OFF
NATIVE_BUILD:BOOL=OFF
NO_ATOMICS:BOOL=OFF
NUMA_INCLUDE_DIR:PATH=/usr/include
NUMA_LIBRARY:FILEPATH=/usr/lib/x86_64-linux-gnu/libnuma.so
NUMA_ROOT_DIR:PATH=/usr
STATIC_LINK_CRT:BOOL=OFF
WARNINGS_AS_ERRORS:BOOL=OFF
YASM_EXECUTABLE:FILEPATH=/usr/bin/yasm

```


#change something then make again the same err
maybe your cache didn't removed.
``rm -f CMakeCache.txt``