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

