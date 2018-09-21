1.	git clone git://git.sv.gnu.org/coreutils

2.	[root@localhost coreutils]# ./bootstrap
./bootstrap: line 442: autoconf: command not found
./bootstrap: Error: 'autoconf' not found
./bootstrap: line 442: automake: command not found
./bootstrap: Error: 'automake' not found
./bootstrap: line 442: autopoint: command not found
./bootstrap: Error: 'autopoint' not found
./bootstrap: line 221: bison: command not found
./bootstrap: Error: 'bison' not found
./bootstrap: line 221: gperf: command not found
./bootstrap: Error: 'gperf' not found
./bootstrap: line 442: makeinfo: command not found
./bootstrap: Error: 'makeinfo' not found
./bootstrap: line 221: patch: command not found
./bootstrap: Error: 'patch' not found

3.	[root@localhost coreutils]# yum install autoconf automake make bison patch gperf

4.	[root@localhost coreutils]# ./bootstrap
./bootstrap: line 442: autopoint: command not found
./bootstrap: Error: 'autopoint' not found
./bootstrap: line 442: makeinfo: command not found
./bootstrap: Error: 'makeinfo' not found

5.	yum install gettext-devel texinfo

6.	[root@localhost coreutils]# ./bootstrap
./bootstrap: Error: 'makeinfo' version == 5.1 is too old
./bootstrap:        'makeinfo' version >= 6.1 is required

7.	wget https://ftp.gnu.org/gnu/texinfo/texinfo-6.5.tar.gz
tar -zxvf texinfo-6.5.tar.gz
 cd texinfo-6.5
./configure
yum remove texinfo
make&&make install

8.	cd coreutils/
[root@localhost coreutils]# ./bootstrap
./bootstrap: Bootstrapping from checked-out coreutils sources...
./bootstrap: consider installing git-merge-changelog from gnulib
./bootstrap: getting gnulib files...
Submodule 'gnulib' (git://git.sv.gnu.org/gnulib.git) registered for path 'gnulib
'
Cloning into 'gnulib'...
remote: Counting objects: 194560, done.
remote: Compressing objects: 100% (26309/26309), done.
Receiving objects:   5% (9728/194560), 2.36 MiB | 284.00 KiB/s
....
cmp: .git/hooks/applypatch-msg: No such file or directory
cmp: .git/hooks/commit-msg: No such file or directory
cmp: .git/hooks/pre-applypatch: No such file or directory
cmp: .git/hooks/pre-commit: No such file or directory
./bootstrap: done.  Now you can run './configure'.

9.	[root@localhost coreutils]# ./configure LDFLAGS="-static"
checking for a BSD-compatible install... /usr/bin/install -c
checking whether build environment is sane... yes
checking for a thread-safe mkdir -p... /usr/bin/mkdir -p
checking for gawk... gawk
checking whether make sets $(MAKE)... yes
checking whether make supports nested variables... yes
checking whether make supports nested variables... (cached) yes
checking for style of include used by make... GNU
checking for gcc... gcc
checking whether the C compiler works... no
configure: error: in `/coreutils':
configure: error: C compiler cannot create executables
See `config.log' for more details

[root@localhost coreutils]# vi config.log

```
gcc version 4.8.5 20150623 (Red Hat 4.8.5-28) (GCC)
configure:5194: $? = 0
configure:5183: gcc -V >&5
gcc: error: unrecognized command line option '-V'
gcc: fatal error: no input files
compilation terminated.
configure:5194: $? = 4
configure:5183: gcc -qversion >&5
gcc: error: unrecognized command line option '-qversion'
gcc: fatal error: no input files
compilation terminated.
configure:5194: $? = 4
configure:5183: gcc -version >&5
gcc: error: unrecognized command line option '-version'
gcc: fatal error: no input files
compilation terminated.
configure:5194: $? = 4
configure:5214: checking whether the C compiler works
configure:5236: gcc   -static conftest.c  >&5
/usr/bin/ld: cannot find -lc
collect2: error: ld returned 1 exit status
configure:5240: $? = 1
configure:5278: result: no
configure: failed program was:
| /* confdefs.h */
| #define PACKAGE_NAME "GNU coreutils"
| #define PACKAGE_TARNAME "coreutils"
| #define PACKAGE_VERSION "8.30.10-e5dae2"
| #define PACKAGE_STRING "GNU coreutils 8.30.10-e5dae2"
| #define PACKAGE_BUGREPORT "bug-coreutils@gnu.org"
| #define PACKAGE_URL "https://www.gnu.org/software/coreutils/"
| #define PACKAGE "coreutils"
| #define VERSION "8.30.10-e5dae2"
| /* end confdefs.h.  */
|
| int
| main ()
| {
|
|   ;
|   return 0;

```
**check the line /usr/bin/ld: cannot find -lc,this means static glibc or libc missing**

so
[root@localhost coreutils]# yum install glibc-static
10. export FORCE_UNSAFE_CONFIGURE=1

11. [root@localhost coreutils]# ./configure LDFLAGS="-static"
checking for a BSD-compatible install... /usr/bin/install -c
checking whether build environment is sane... yes
checking for a thread-safe mkdir -p... /usr/bin/mkdir -p
checking for gawk... gawk
checking whether make sets $(MAKE)... yes
checking whether make supports nested variables... yes
checking whether make supports nested variables... (cached) yes
checking for style of include used by make... GNU
checking for gcc... gcc
checking whether the C compiler works... yes
checking for C compiler default output file name... a.out
checking for suffix of executables...
checking whether we are cross compiling... no
checking for suffix of object files... o
checking whether we are using the GNU C compiler... yes
checking whether gcc accepts -g... yes
checking for gcc option to enable C11 features... -std=gnu11
checking dependency style of gcc -std=gnu11... gcc3
checking whether gcc -std=gnu11 and cc understand -c and -o together... yes
......
configure: creating ./config.status
config.status: creating Makefile
config.status: creating po/Makefile.in
config.status: creating gnulib-tests/Makefile
config.status: creating lib/config.h
config.status: lib/config.h is unchanged
config.status: executing depfiles commands
config.status: executing po-directories commands
config.status: creating po/POTFILES
config.status: creating po/Makefile

12.	[root@localhost coreutils]# make
make[3]: Entering directory `/coreutils'
make[3]: Leaving directory `/coreutils'
Updating ./doc/version.texi
  GEN      doc/constants.texi
  MAKEINFO doc/coreutils.info
  CC       lib/copy-acl.o
  CC       lib/set-acl.o
  CC       lib/acl-errno-valid.o
  CC       lib/acl-internal.o
lib/acl-internal.c: In function 'free_permission_context':
lib/acl-internal.c:479:1: error: function might be candidate for attribute 'cons
t' [-Werror=suggest-attribute=const]
 free_permission_context (struct permission_context *ctx)
 ^
cc1: all warnings being treated as errors
make[2]: *** [lib/acl-internal.o] Error 1
make[2]: Leaving directory `/coreutils'
make[1]: *** [all-recursive] Error 1
make[1]: Leaving directory `/coreutils'
make: *** [all] Error 2

13.	Find -Werror in Makefile,clear it or comment it.
14. make



#some not work with ./configure LDFLAGS="-static"
try  make SHARED=0 CC='gcc -static'  
make -e LDFLAGS=-all-static
export LDFLAGS="-static -Wl,--no-export-dynamic"
make -e LDFLAGS=-all-static
make sense CC="gcc -static"

https://gcc.gnu.org/onlinedocs/gcc/Warning-Options.html
https://www.rapidtables.com/code/linux/gcc/gcc-wall.html
./configure --enable-static_link


```
Warnings are diagnostic messages that report constructions that are not inherently erroneous but that are risky or suggest there may have been an error.

The following language-independent options do not enable specific warnings but control the kinds of diagnostics produced by GCC.

-fsyntax-only
Check the code for syntax errors, but don’t do anything beyond that.

-fmax-errors=n
Limits the maximum number of error messages to n, at which point GCC bails out rather than attempting to continue processing the source code. If n is 0 (the default), there is no limit on the number of error messages produced. If -Wfatal-errors is also specified, then -Wfatal-errors takes precedence over this option.

-w
Inhibit all warning messages.

-Werror
Make all warnings into errors.

-Werror=
Make the specified warning into an error. The specifier for a warning is appended; for example -Werror=switch turns the warnings controlled by -Wswitch into errors. This switch takes a negative form, to be used to negate -Werror for specific warnings; for example -Wno-error=switch makes -Wswitch warnings not be errors, even when -Werror is in effect.

The warning message for each controllable warning includes the option that controls the warning. That option can then be used with -Werror= and -Wno-error= as described above. (Printing of the option in the warning message can be disabled using the -fno-diagnostics-show-option flag.)

Note that specifying -Werror=foo automatically implies -Wfoo. However, -Wno-error=foo does not imply anything.

-Wfatal-errors
This option causes the compiler to abort compilation on the first error occurred rather than trying to keep going and printing further error messages.

You can request many specific warnings with options beginning with ‘-W’, for example -Wimplicit to request warnings on implicit declarations. Each of these specific warning options also has a negative form beginning ‘-Wno-’ to turn off warnings; for example, -Wno-implicit. This manual lists only one of the two forms, whichever is not the default. For further language-specific options also refer to C++ Dialect Options and Objective-C and Objective-C++ Dialect Options.

Some options, such as -Wall and -Wextra, turn on other options, such as -Wunused, which may turn on further options, such as -Wunused-value. The combined effect of positive and negative forms is that more specific options have priority over less specific ones, independently of their position in the command-line. For options of the same specificity, the last one takes effect. Options enabled or disabled via pragmas (see Diagnostic Pragmas) take effect as if they appeared at the end of the command-line.

When an unrecognized warning option is requested (e.g., -Wunknown-warning), GCC emits a diagnostic stating that the option is not recognized. However, if the -Wno- form is used, the behavior is slightly different: no diagnostic is produced for -Wno-unknown-warning unless other diagnostics are being produced. This allows the use of new -Wno- options with old compilers, but if something goes wrong, the compiler warns that an unrecognized option is present.

-Wpedantic
-pedantic
Issue all the warnings demanded by strict ISO C and ISO C++; reject all programs that use forbidden extensions, and some other programs that do not follow ISO C and ISO C++. For ISO C, follows the version of the ISO C standard specified by any -std option used.

Valid ISO C and ISO C++ programs should compile properly with or without this option (though a rare few require -ansi or a -std option specifying the required version of ISO C). However, without this option, certain GNU extensions and traditional C and C++ features are supported as well. With this option, they are rejected.

-Wpedantic does not cause warning messages for use of the alternate keywords whose names begin and end with ‘__’. Pedantic warnings are also disabled in the expression that follows __extension__. However, only system header files should use these escape routes; application programs should avoid them. See Alternate Keywords.

Some users try to use -Wpedantic to check programs for strict ISO C conformance. They soon find that it does not do quite what they want: it finds some non-ISO practices, but not all—only those for which ISO C requires a diagnostic, and some others for which diagnostics have been added.

A feature to report any failure to conform to ISO C might be useful in some instances, but would require considerable additional work and would be quite different from -Wpedantic. We don’t have plans to support such a feature in the near future.

Where the standard specified with -std represents a GNU extended dialect of C, such as ‘gnu90’ or ‘gnu99’, there is a corresponding base standard, the version of ISO C on which the GNU extended dialect is based. Warnings from -Wpedantic are given where they are required by the base standard. (It does not make sense for such warnings to be given only for features not in the specified GNU C dialect, since by definition the GNU dialects of C include all features the compiler supports with the given option, and there would be nothing to warn about.)

-pedantic-errors
Give an error whenever the base standard (see -Wpedantic) requires a diagnostic, in some cases where there is undefined behavior at compile-time and in some other cases that do not prevent compilation of programs that are valid according to the standard. This is not equivalent to -Werror=pedantic, since there are errors enabled by this option and not enabled by the latter and vice versa.

-Wall
This enables all the warnings about constructions that some users consider questionable, and that are easy to avoid (or modify to prevent the warning), even in conjunction with macros. This also enables some language-specific warnings described in C++ Dialect Options and Objective-C and Objective-C++ Dialect Options.

-Wall turns on the following warning flags:

-Waddress   
-Warray-bounds=1 (only with -O2)  
-Wbool-compare  
-Wbool-operation  
-Wc++11-compat  -Wc++14-compat  
-Wcatch-value (C++ and Objective-C++ only)  
-Wchar-subscripts  
-Wcomment  
-Wduplicate-decl-specifier (C and Objective-C only) 
-Wenum-compare (in C/ObjC; this is on by default in C++) 
-Wformat   
-Wint-in-bool-context  
-Wimplicit (C and Objective-C only) 
-Wimplicit-int (C and Objective-C only) 
-Wimplicit-function-declaration (C and Objective-C only) 
-Winit-self (only for C++) 
-Wlogical-not-parentheses 
-Wmain (only for C/ObjC and unless -ffreestanding)  
-Wmaybe-uninitialized 
-Wmemset-elt-size 
-Wmemset-transposed-args 
-Wmisleading-indentation (only for C/C++) 
-Wmissing-attributes 
-Wmissing-braces (only for C/ObjC) 
-Wmultistatement-macros  
-Wnarrowing (only for C++)  
-Wnonnull  
-Wnonnull-compare  
-Wopenmp-simd 
-Wparentheses  
-Wpessimizing-move (only for C++)  
-Wpointer-sign  
-Wreorder   
-Wrestrict   
-Wreturn-type  
-Wsequence-point  
-Wsign-compare (only in C++)  
-Wsizeof-pointer-div 
-Wsizeof-pointer-memaccess 
-Wstrict-aliasing  
-Wstrict-overflow=1  
-Wswitch  
-Wtautological-compare  
-Wtrigraphs  
-Wuninitialized  
-Wunknown-pragmas  
-Wunused-function  
-Wunused-label     
-Wunused-value     
-Wunused-variable  
-Wvolatile-register-var 

Note that some warning flags are not implied by -Wall. Some of them warn about constructions that users generally do not consider questionable, but which occasionally you might wish to check for; others warn about constructions that are necessary or hard to avoid in some cases, and there is no simple way to modify the code to suppress the warning. Some of them are enabled by -Wextra but many of them must be enabled individually.

-Wextra
This enables some extra warning flags that are not enabled by -Wall. (This option used to be called -W. The older name is still supported, but the newer name is more descriptive.)

-Wclobbered  
-Wcast-function-type  
-Wempty-body  
-Wignored-qualifiers 
-Wimplicit-fallthrough=3 
-Wmissing-field-initializers  
-Wmissing-parameter-type (C only)  
-Wold-style-declaration (C only)  
-Woverride-init  
-Wsign-compare (C only) 
-Wredundant-move (only for C++)  
-Wtype-limits  
-Wuninitialized  
-Wshift-negative-value (in C++03 and in C99 and newer)  
-Wunused-parameter (only with -Wunused or -Wall) 
-Wunused-but-set-parameter (only with -Wunused or -Wall)  

The option -Wextra also prints warning messages for the following cases:

A pointer is compared against integer zero with <, <=, >, or >=.
(C++ only) An enumerator and a non-enumerator both appear in a conditional expression.
(C++ only) Ambiguous virtual bases.
(C++ only) Subscripting an array that has been declared register.
(C++ only) Taking the address of a variable that has been declared register.
(C++ only) A base class is not initialized in the copy constructor of a derived class.
-Wchar-subscripts
Warn if an array subscript has type char. This is a common cause of error, as programmers often forget that this type is signed on some machines. This warning is enabled by -Wall.

-Wchkp
Warn about an invalid memory access that is found by Pointer Bounds Checker (-fcheck-pointer-bounds).

-Wno-coverage-mismatch
Warn if feedback profiles do not match when using the -fprofile-use option. If a source file is changed between compiling with -fprofile-gen and with -fprofile-use, the files with the profile feedback can fail to match the source file and GCC cannot use the profile feedback information. By default, this warning is enabled and is treated as an error. -Wno-coverage-mismatch can be used to disable the warning or -Wno-error=coverage-mismatch can be used to disable the error. Disabling the error for this warning can result in poorly optimized code and is useful only in the case of very minor changes such as bug fixes to an existing code-base. Completely disabling the warning is not recommended.

-Wno-cpp
(C, Objective-C, C++, Objective-C++ and Fortran only)

Suppress warning messages emitted by #warning directives.

-Wdouble-promotion (C, C++, Objective-C and Objective-C++ only)
Give a warning when a value of type float is implicitly promoted to double. CPUs with a 32-bit “single-precision” floating-point unit implement float in hardware, but emulate double in software. On such a machine, doing computations using double values is much more expensive because of the overhead required for software emulation.

It is easy to accidentally do computations with double because floating-point literals are implicitly of type double. For example, in:

float area(float radius)
{
   return 3.14159 * radius * radius;
}
the compiler performs the entire computation with double because the floating-point literal is a double.

-Wduplicate-decl-specifier (C and Objective-C only)
Warn if a declaration has duplicate const, volatile, restrict or _Atomic specifier. This warning is enabled by -Wall.

-Wformat
-Wformat=n
Check calls to printf and scanf, etc., to make sure that the arguments supplied have types appropriate to the format string specified, and that the conversions specified in the format string make sense. This includes standard functions, and others specified by format attributes (see Function Attributes), in the printf, scanf, strftime and strfmon (an X/Open extension, not in the C standard) families (or other target-specific families). Which functions are checked without format attributes having been specified depends on the standard version selected, and such checks of functions without the attribute specified are disabled by -ffreestanding or -fno-builtin.

The formats are checked against the format features supported by GNU libc version 2.2. These include all ISO C90 and C99 features, as well as features from the Single Unix Specification and some BSD and GNU extensions. Other library implementations may not support all these features; GCC does not support warning about features that go beyond a particular library’s limitations. However, if -Wpedantic is used with -Wformat, warnings are given about format features not in the selected standard version (but not for strfmon formats, since those are not in any version of the C standard). See Options Controlling C Dialect.

-Wformat=1
-Wformat
Option -Wformat is equivalent to -Wformat=1, and -Wno-format is equivalent to -Wformat=0. Since -Wformat also checks for null format arguments for several functions, -Wformat also implies -Wnonnull. Some aspects of this level of format checking can be disabled by the options: -Wno-format-contains-nul, -Wno-format-extra-args, and -Wno-format-zero-length. -Wformat is enabled by -Wall.

-Wno-format-contains-nul
If -Wformat is specified, do not warn about format strings that contain NUL bytes.

-Wno-format-extra-args
If -Wformat is specified, do not warn about excess arguments to a printf or scanf format function. The C standard specifies that such arguments are ignored.

Where the unused arguments lie between used arguments that are specified with ‘$’ operand number specifications, normally warnings are still given, since the implementation could not know what type to pass to va_arg to skip the unused arguments. However, in the case of scanf formats, this option suppresses the warning if the unused arguments are all pointers, since the Single Unix Specification says that such unused arguments are allowed.

-Wformat-overflow
-Wformat-overflow=level
Warn about calls to formatted input/output functions such as sprintf and vsprintf that might overflow the destination buffer. When the exact number of bytes written by a format directive cannot be determined at compile-time it is estimated based on heuristics that depend on the level argument and on optimization. While enabling optimization will in most cases improve the accuracy of the warning, it may also result in false positives.

-Wformat-overflow
-Wformat-overflow=1
Level 1 of -Wformat-overflow enabled by -Wformat employs a conservative approach that warns only about calls that most likely overflow the buffer. At this level, numeric arguments to format directives with unknown values are assumed to have the value of one, and strings of unknown length to be empty. Numeric arguments that are known to be bounded to a subrange of their type, or string arguments whose output is bounded either by their directive’s precision or by a finite set of string literals, are assumed to take on the value within the range that results in the most bytes on output. For example, the call to sprintf below is diagnosed because even with both a and b equal to zero, the terminating NUL character ('\0') appended by the function to the destination buffer will be written past its end. Increasing the size of the buffer by a single byte is sufficient to avoid the warning, though it may not be sufficient to avoid the overflow.

void f (int a, int b)
{
  char buf [13];
  sprintf (buf, "a = %i, b = %i\n", a, b);
}
-Wformat-overflow=2
Level 2 warns also about calls that might overflow the destination buffer given an argument of sufficient length or magnitude. At level 2, unknown numeric arguments are assumed to have the minimum representable value for signed types with a precision greater than 1, and the maximum representable value otherwise. Unknown string arguments whose length cannot be assumed to be bounded either by the directive’s precision, or by a finite set of string literals they may evaluate to, or the character array they may point to, are assumed to be 1 character long.

At level 2, the call in the example above is again diagnosed, but this time because with a equal to a 32-bit INT_MIN the first %i directive will write some of its digits beyond the end of the destination buffer. To make the call safe regardless of the values of the two variables, the size of the destination buffer must be increased to at least 34 bytes. GCC includes the minimum size of the buffer in an informational note following the warning.

An alternative to increasing the size of the destination buffer is to constrain the range of formatted values. The maximum length of string arguments can be bounded by specifying the precision in the format directive. When numeric arguments of format directives can be assumed to be bounded by less than the precision of their type, choosing an appropriate length modifier to the format specifier will reduce the required buffer size. For example, if a and b in the example above can be assumed to be within the precision of the short int type then using either the %hi format directive or casting the argument to short reduces the maximum required size of the buffer to 24 bytes.

void f (int a, int b)
{
  char buf [23];
  sprintf (buf, "a = %hi, b = %i\n", a, (short)b);
}
-Wno-format-zero-length
If -Wformat is specified, do not warn about zero-length formats. The C standard specifies that zero-length formats are allowed.

-Wformat=2
Enable -Wformat plus additional format checks. Currently equivalent to -Wformat -Wformat-nonliteral -Wformat-security -Wformat-y2k.

-Wformat-nonliteral
If -Wformat is specified, also warn if the format string is not a string literal and so cannot be checked, unless the format function takes its format arguments as a va_list.

-Wformat-security
If -Wformat is specified, also warn about uses of format functions that represent possible security problems. At present, this warns about calls to printf and scanf functions where the format string is not a string literal and there are no format arguments, as in printf (foo);. This may be a security hole if the format string came from untrusted input and contains ‘%n’. (This is currently a subset of what -Wformat-nonliteral warns about, but in future warnings may be added to -Wformat-security that are not included in -Wformat-nonliteral.)

-Wformat-signedness
If -Wformat is specified, also warn if the format string requires an unsigned argument and the argument is signed and vice versa.

-Wformat-truncation
-Wformat-truncation=level
Warn about calls to formatted input/output functions such as snprintf and vsnprintf that might result in output truncation. When the exact number of bytes written by a format directive cannot be determined at compile-time it is estimated based on heuristics that depend on the level argument and on optimization. While enabling optimization will in most cases improve the accuracy of the warning, it may also result in false positives. Except as noted otherwise, the option uses the same logic -Wformat-overflow.

-Wformat-truncation
-Wformat-truncation=1
Level 1 of -Wformat-truncation enabled by -Wformat employs a conservative approach that warns only about calls to bounded functions whose return value is unused and that will most likely result in output truncation.

-Wformat-truncation=2
Level 2 warns also about calls to bounded functions whose return value is used and that might result in truncation given an argument of sufficient length or magnitude.

-Wformat-y2k
If -Wformat is specified, also warn about strftime formats that may yield only a two-digit year.

-Wnonnull
Warn about passing a null pointer for arguments marked as requiring a non-null value by the nonnull function attribute.

-Wnonnull is included in -Wall and -Wformat. It can be disabled with the -Wno-nonnull option.

-Wnonnull-compare
Warn when comparing an argument marked with the nonnull function attribute against null inside the function.

-Wnonnull-compare is included in -Wall. It can be disabled with the -Wno-nonnull-compare option.

-Wnull-dereference
Warn if the compiler detects paths that trigger erroneous or undefined behavior due to dereferencing a null pointer. This option is only active when -fdelete-null-pointer-checks is active, which is enabled by optimizations in most targets. The precision of the warnings depends on the optimization options used.

-Winit-self (C, C++, Objective-C and Objective-C++ only)
Warn about uninitialized variables that are initialized with themselves. Note this option can only be used with the -Wuninitialized option.

For example, GCC warns about i being uninitialized in the following snippet only when -Winit-self has been specified:

int f()
{
  int i = i;
  return i;
}
This warning is enabled by -Wall in C++.

-Wimplicit-int (C and Objective-C only)
Warn when a declaration does not specify a type. This warning is enabled by -Wall.

-Wimplicit-function-declaration (C and Objective-C only)
Give a warning whenever a function is used before being declared. In C99 mode (-std=c99 or -std=gnu99), this warning is enabled by default and it is made into an error by -pedantic-errors. This warning is also enabled by -Wall.

-Wimplicit (C and Objective-C only)
Same as -Wimplicit-int and -Wimplicit-function-declaration. This warning is enabled by -Wall.

-Wimplicit-fallthrough
-Wimplicit-fallthrough is the same as -Wimplicit-fallthrough=3 and -Wno-implicit-fallthrough is the same as -Wimplicit-fallthrough=0.

-Wimplicit-fallthrough=n
Warn when a switch case falls through. For example:

switch (cond)
  {
  case 1:
    a = 1;
    break;
  case 2:
    a = 2;
  case 3:
    a = 3;
    break;
  }
This warning does not warn when the last statement of a case cannot fall through, e.g. when there is a return statement or a call to function declared with the noreturn attribute. -Wimplicit-fallthrough= also takes into account control flow statements, such as ifs, and only warns when appropriate. E.g.

switch (cond)
  {
  case 1:
    if (i > 3) {
      bar (5);
      break;
    } else if (i < 1) {
      bar (0);
    } else
      return;
  default:
    …
  }
Since there are occasions where a switch case fall through is desirable, GCC provides an attribute, __attribute__ ((fallthrough)), that is to be used along with a null statement to suppress this warning that would normally occur:

switch (cond)
  {
  case 1:
    bar (0);
    __attribute__ ((fallthrough));
  default:
    …
  }
C++17 provides a standard way to suppress the -Wimplicit-fallthrough warning using [[fallthrough]]; instead of the GNU attribute. In C++11 or C++14 users can use [[gnu::fallthrough]];, which is a GNU extension. Instead of these attributes, it is also possible to add a fallthrough comment to silence the warning. The whole body of the C or C++ style comment should match the given regular expressions listed below. The option argument n specifies what kind of comments are accepted:

-Wimplicit-fallthrough=0 disables the warning altogether.
-Wimplicit-fallthrough=1 matches .* regular expression, any comment is used as fallthrough comment.
-Wimplicit-fallthrough=2 case insensitively matches .*falls?[ \t-]*thr(ough|u).* regular expression.
-Wimplicit-fallthrough=3 case sensitively matches one of the following regular expressions:
-fallthrough
@fallthrough@
lint -fallthrough[ \t]*
[ \t.!]*(ELSE,? |INTENTIONAL(LY)? )?
FALL(S | |-)?THR(OUGH|U)[ \t.!]*(-[^\n\r]*)?
[ \t.!]*(Else,? |Intentional(ly)? )?
Fall((s | |-)[Tt]|t)hr(ough|u)[ \t.!]*(-[^\n\r]*)?
[ \t.!]*([Ee]lse,? |[Ii]ntentional(ly)? )?
fall(s | |-)?thr(ough|u)[ \t.!]*(-[^\n\r]*)?
-Wimplicit-fallthrough=4 case sensitively matches one of the following regular expressions:
-fallthrough
@fallthrough@
lint -fallthrough[ \t]*
[ \t]*FALLTHR(OUGH|U)[ \t]*
-Wimplicit-fallthrough=5 doesn’t recognize any comments as fallthrough comments, only attributes disable the warning.
The comment needs to be followed after optional whitespace and other comments by case or default keywords or by a user label that precedes some case or default label.

switch (cond)
  {
  case 1:
    bar (0);
    /* FALLTHRU */
  default:
    …
  }
The -Wimplicit-fallthrough=3 warning is enabled by -Wextra.

-Wif-not-aligned (C, C++, Objective-C and Objective-C++ only)
Control if warning triggered by the warn_if_not_aligned attribute should be issued. This is enabled by default. Use -Wno-if-not-aligned to disable it.

-Wignored-qualifiers (C and C++ only)
Warn if the return type of a function has a type qualifier such as const. For ISO C such a type qualifier has no effect, since the value returned by a function is not an lvalue. For C++, the warning is only emitted for scalar types or void. ISO C prohibits qualified void return types on function definitions, so such return types always receive a warning even without this option.

This warning is also enabled by -Wextra.

-Wignored-attributes (C and C++ only)
Warn when an attribute is ignored. This is different from the -Wattributes option in that it warns whenever the compiler decides to drop an attribute, not that the attribute is either unknown, used in a wrong place, etc. This warning is enabled by default.

-Wmain
Warn if the type of main is suspicious. main should be a function with external linkage, returning int, taking either zero arguments, two, or three arguments of appropriate types. This warning is enabled by default in C++ and is enabled by either -Wall or -Wpedantic.

-Wmisleading-indentation (C and C++ only)
Warn when the indentation of the code does not reflect the block structure. Specifically, a warning is issued for if, else, while, and for clauses with a guarded statement that does not use braces, followed by an unguarded statement with the same indentation.

In the following example, the call to “bar” is misleadingly indented as if it were guarded by the “if” conditional.

  if (some_condition ())
    foo ();
    bar ();  /* Gotcha: this is not guarded by the "if".  */
In the case of mixed tabs and spaces, the warning uses the -ftabstop= option to determine if the statements line up (defaulting to 8).

The warning is not issued for code involving multiline preprocessor logic such as the following example.

  if (flagA)
    foo (0);
#if SOME_CONDITION_THAT_DOES_NOT_HOLD
  if (flagB)
#endif
    foo (1);
The warning is not issued after a #line directive, since this typically indicates autogenerated code, and no assumptions can be made about the layout of the file that the directive references.

This warning is enabled by -Wall in C and C++.

-Wmissing-attributes
Warn when a declaration of a function is missing one or more attributes that a related function is declared with and whose absence may adversely affect the correctness or efficiency of generated code. For example, in C++, the warning is issued when an explicit specialization of a primary template declared with attribute alloc_align, alloc_size, assume_aligned, format, format_arg, malloc, or nonnull is declared without it. Attributes deprecated, error, and warning suppress the warning. (see Function Attributes).

-Wmissing-attributes is enabled by -Wall.

For example, since the declaration of the primary function template below makes use of both attribute malloc and alloc_size the declaration of the explicit specialization of the template is diagnosed because it is missing one of the attributes.

template <class T>
T* __attribute__ ((malloc, alloc_size (1)))
allocate (size_t);

template <>
void* __attribute__ ((malloc))   // missing alloc_size
allocate<void> (size_t);
-Wmissing-braces
Warn if an aggregate or union initializer is not fully bracketed. In the following example, the initializer for a is not fully bracketed, but that for b is fully bracketed. This warning is enabled by -Wall in C.

int a[2][2] = { 0, 1, 2, 3 };
int b[2][2] = { { 0, 1 }, { 2, 3 } };
This warning is enabled by -Wall.

-Wmissing-include-dirs (C, C++, Objective-C and Objective-C++ only)
Warn if a user-supplied include directory does not exist.

-Wmultistatement-macros
Warn about unsafe multiple statement macros that appear to be guarded by a clause such as if, else, for, switch, or while, in which only the first statement is actually guarded after the macro is expanded.

For example:

#define DOIT x++; y++
if (c)
  DOIT;
will increment y unconditionally, not just when c holds. The can usually be fixed by wrapping the macro in a do-while loop:

#define DOIT do { x++; y++; } while (0)
if (c)
  DOIT;
This warning is enabled by -Wall in C and C++.

-Wparentheses
Warn if parentheses are omitted in certain contexts, such as when there is an assignment in a context where a truth value is expected, or when operators are nested whose precedence people often get confused about.

Also warn if a comparison like x<=y<=z appears; this is equivalent to (x<=y ? 1 : 0) <= z, which is a different interpretation from that of ordinary mathematical notation.

Also warn for dangerous uses of the GNU extension to ?: with omitted middle operand. When the condition in the ?: operator is a boolean expression, the omitted value is always 1. Often programmers expect it to be a value computed inside the conditional expression instead.

For C++ this also warns for some cases of unnecessary parentheses in declarations, which can indicate an attempt at a function call instead of a declaration:

{
  // Declares a local variable called mymutex.
  std::unique_lock<std::mutex> (mymutex);
  // User meant std::unique_lock<std::mutex> lock (mymutex);
}
This warning is enabled by -Wall.

-Wsequence-point
Warn about code that may have undefined semantics because of violations of sequence point rules in the C and C++ standards.

The C and C++ standards define the order in which expressions in a C/C++ program are evaluated in terms of sequence points, which represent a partial ordering between the execution of parts of the program: those executed before the sequence point, and those executed after it. These occur after the evaluation of a full expression (one which is not part of a larger expression), after the evaluation of the first operand of a &&, ||, ? : or , (comma) operator, before a function is called (but after the evaluation of its arguments and the expression denoting the called function), and in certain other places. Other than as expressed by the sequence point rules, the order of evaluation of subexpressions of an expression is not specified. All these rules describe only a partial order rather than a total order, since, for example, if two functions are called within one expression with no sequence point between them, the order in which the functions are called is not specified. However, the standards committee have ruled that function calls do not overlap.

It is not specified when between sequence points modifications to the values of objects take effect. Programs whose behavior depends on this have undefined behavior; the C and C++ standards specify that “Between the previous and next sequence point an object shall have its stored value modified at most once by the evaluation of an expression. Furthermore, the prior value shall be read only to determine the value to be stored.”. If a program breaks these rules, the results on any particular implementation are entirely unpredictable.

Examples of code with undefined behavior are a = a++;, a[n] = b[n++] and a[i++] = i;. Some more complicated cases are not diagnosed by this option, and it may give an occasional false positive result, but in general it has been found fairly effective at detecting this sort of problem in programs.

The C++17 standard will define the order of evaluation of operands in more cases: in particular it requires that the right-hand side of an assignment be evaluated before the left-hand side, so the above examples are no longer undefined. But this warning will still warn about them, to help people avoid writing code that is undefined in C and earlier revisions of C++.

The standard is worded confusingly, therefore there is some debate over the precise meaning of the sequence point rules in subtle cases. Links to discussions of the problem, including proposed formal definitions, may be found on the GCC readings page, at http://gcc.gnu.org/readings.html.

This warning is enabled by -Wall for C and C++.

-Wno-return-local-addr
Do not warn about returning a pointer (or in C++, a reference) to a variable that goes out of scope after the function returns.

-Wreturn-type
Warn whenever a function is defined with a return type that defaults to int. Also warn about any return statement with no return value in a function whose return type is not void (falling off the end of the function body is considered returning without a value).

For C only, warn about a return statement with an expression in a function whose return type is void, unless the expression type is also void. As a GNU extension, the latter case is accepted without a warning unless -Wpedantic is used.

For C++, a function without return type always produces a diagnostic message, even when -Wno-return-type is specified. The only exceptions are main and functions defined in system headers.

This warning is enabled by default for C++ and is enabled by -Wall.

-Wshift-count-negative
Warn if shift count is negative. This warning is enabled by default.

-Wshift-count-overflow
Warn if shift count >= width of type. This warning is enabled by default.

-Wshift-negative-value
Warn if left shifting a negative value. This warning is enabled by -Wextra in C99 and C++11 modes (and newer).

-Wshift-overflow
-Wshift-overflow=n
Warn about left shift overflows. This warning is enabled by default in C99 and C++11 modes (and newer).

-Wshift-overflow=1
This is the warning level of -Wshift-overflow and is enabled by default in C99 and C++11 modes (and newer). This warning level does not warn about left-shifting 1 into the sign bit. (However, in C, such an overflow is still rejected in contexts where an integer constant expression is required.)

-Wshift-overflow=2
This warning level also warns about left-shifting 1 into the sign bit, unless C++14 mode is active.

-Wswitch
Warn whenever a switch statement has an index of enumerated type and lacks a case for one or more of the named codes of that enumeration. (The presence of a default label prevents this warning.) case labels outside the enumeration range also provoke warnings when this option is used (even if there is a default label). This warning is enabled by -Wall.

-Wswitch-default
Warn whenever a switch statement does not have a default case.

-Wswitch-enum
Warn whenever a switch statement has an index of enumerated type and lacks a case for one or more of the named codes of that enumeration. case labels outside the enumeration range also provoke warnings when this option is used. The only difference between -Wswitch and this option is that this option gives a warning about an omitted enumeration code even if there is a default label.

-Wswitch-bool
Warn whenever a switch statement has an index of boolean type and the case values are outside the range of a boolean type. It is possible to suppress this warning by casting the controlling expression to a type other than bool. For example:

switch ((int) (a == 4))
  {
  …
  }
This warning is enabled by default for C and C++ programs.

-Wswitch-unreachable
Warn whenever a switch statement contains statements between the controlling expression and the first case label, which will never be executed. For example:

switch (cond)
  {
   i = 15;
  …
   case 5:
  …
  }
-Wswitch-unreachable does not warn if the statement between the controlling expression and the first case label is just a declaration:

switch (cond)
  {
   int i;
  …
   case 5:
   i = 5;
  …
  }
This warning is enabled by default for C and C++ programs.

-Wsync-nand (C and C++ only)
Warn when __sync_fetch_and_nand and __sync_nand_and_fetch built-in functions are used. These functions changed semantics in GCC 4.4.

-Wunused-but-set-parameter
Warn whenever a function parameter is assigned to, but otherwise unused (aside from its declaration).

To suppress this warning use the unused attribute (see Variable Attributes).

This warning is also enabled by -Wunused together with -Wextra.

-Wunused-but-set-variable
Warn whenever a local variable is assigned to, but otherwise unused (aside from its declaration). This warning is enabled by -Wall.

To suppress this warning use the unused attribute (see Variable Attributes).

This warning is also enabled by -Wunused, which is enabled by -Wall.

-Wunused-function
Warn whenever a static function is declared but not defined or a non-inline static function is unused. This warning is enabled by -Wall.

-Wunused-label
Warn whenever a label is declared but not used. This warning is enabled by -Wall.

To suppress this warning use the unused attribute (see Variable Attributes).

-Wunused-local-typedefs (C, Objective-C, C++ and Objective-C++ only)
Warn when a typedef locally defined in a function is not used. This warning is enabled by -Wall.

-Wunused-parameter
Warn whenever a function parameter is unused aside from its declaration.

To suppress this warning use the unused attribute (see Variable Attributes).

-Wno-unused-result
Do not warn if a caller of a function marked with attribute warn_unused_result (see Function Attributes) does not use its return value. The default is -Wunused-result.

-Wunused-variable
Warn whenever a local or static variable is unused aside from its declaration. This option implies -Wunused-const-variable=1 for C, but not for C++. This warning is enabled by -Wall.

To suppress this warning use the unused attribute (see Variable Attributes).

-Wunused-const-variable
-Wunused-const-variable=n
Warn whenever a constant static variable is unused aside from its declaration. -Wunused-const-variable=1 is enabled by -Wunused-variable for C, but not for C++. In C this declares variable storage, but in C++ this is not an error since const variables take the place of #defines.

To suppress this warning use the unused attribute (see Variable Attributes).

-Wunused-const-variable=1
This is the warning level that is enabled by -Wunused-variable for C. It warns only about unused static const variables defined in the main compilation unit, but not about static const variables declared in any header included.

-Wunused-const-variable=2
This warning level also warns for unused constant static variables in headers (excluding system headers). This is the warning level of -Wunused-const-variable and must be explicitly requested since in C++ this isn’t an error and in C it might be harder to clean up all headers included.

-Wunused-value
Warn whenever a statement computes a result that is explicitly not used. To suppress this warning cast the unused expression to void. This includes an expression-statement or the left-hand side of a comma expression that contains no side effects. For example, an expression such as x[i,j] causes a warning, while x[(void)i,j] does not.

This warning is enabled by -Wall.

-Wunused
All the above -Wunused options combined.

In order to get a warning about an unused function parameter, you must either specify -Wextra -Wunused (note that -Wall implies -Wunused), or separately specify -Wunused-parameter.

-Wuninitialized
Warn if an automatic variable is used without first being initialized or if a variable may be clobbered by a setjmp call. In C++, warn if a non-static reference or non-static const member appears in a class without constructors.

If you want to warn about code that uses the uninitialized value of the variable in its own initializer, use the -Winit-self option.

These warnings occur for individual uninitialized or clobbered elements of structure, union or array variables as well as for variables that are uninitialized or clobbered as a whole. They do not occur for variables or elements declared volatile. Because these warnings depend on optimization, the exact variables or elements for which there are warnings depends on the precise optimization options and version of GCC used.

Note that there may be no warning about a variable that is used only to compute a value that itself is never used, because such computations may be deleted by data flow analysis before the warnings are printed.

-Winvalid-memory-model
Warn for invocations of __atomic Builtins, __sync Builtins, and the C11 atomic generic functions with a memory consistency argument that is either invalid for the operation or outside the range of values of the memory_order enumeration. For example, since the __atomic_store and __atomic_store_n built-ins are only defined for the relaxed, release, and sequentially consistent memory orders the following code is diagnosed:

void store (int *i)
{
  __atomic_store_n (i, 0, memory_order_consume);
}
-Winvalid-memory-model is enabled by default.

-Wmaybe-uninitialized
For an automatic (i.e. local) variable, if there exists a path from the function entry to a use of the variable that is initialized, but there exist some other paths for which the variable is not initialized, the compiler emits a warning if it cannot prove the uninitialized paths are not executed at run time.

These warnings are only possible in optimizing compilation, because otherwise GCC does not keep track of the state of variables.

These warnings are made optional because GCC may not be able to determine when the code is correct in spite of appearing to have an error. Here is one example of how this can happen:

{
  int x;
  switch (y)
    {
    case 1: x = 1;
      break;
    case 2: x = 4;
      break;
    case 3: x = 5;
    }
  foo (x);
}
If the value of y is always 1, 2 or 3, then x is always initialized, but GCC doesn’t know this. To suppress the warning, you need to provide a default case with assert(0) or similar code.

This option also warns when a non-volatile automatic variable might be changed by a call to longjmp. The compiler sees only the calls to setjmp. It cannot know where longjmp will be called; in fact, a signal handler could call it at any point in the code. As a result, you may get a warning even when there is in fact no problem because longjmp cannot in fact be called at the place that would cause a problem.

Some spurious warnings can be avoided if you declare all the functions you use that never return as noreturn. See Function Attributes.

This warning is enabled by -Wall or -Wextra.

-Wunknown-pragmas
Warn when a #pragma directive is encountered that is not understood by GCC. If this command-line option is used, warnings are even issued for unknown pragmas in system header files. This is not the case if the warnings are only enabled by the -Wall command-line option.

-Wno-pragmas
Do not warn about misuses of pragmas, such as incorrect parameters, invalid syntax, or conflicts between pragmas. See also -Wunknown-pragmas.

-Wstrict-aliasing
This option is only active when -fstrict-aliasing is active. It warns about code that might break the strict aliasing rules that the compiler is using for optimization. The warning does not catch all cases, but does attempt to catch the more common pitfalls. It is included in -Wall. It is equivalent to -Wstrict-aliasing=3

-Wstrict-aliasing=n
This option is only active when -fstrict-aliasing is active. It warns about code that might break the strict aliasing rules that the compiler is using for optimization. Higher levels correspond to higher accuracy (fewer false positives). Higher levels also correspond to more effort, similar to the way -O works. -Wstrict-aliasing is equivalent to -Wstrict-aliasing=3.

Level 1: Most aggressive, quick, least accurate. Possibly useful when higher levels do not warn but -fstrict-aliasing still breaks the code, as it has very few false negatives. However, it has many false positives. Warns for all pointer conversions between possibly incompatible types, even if never dereferenced. Runs in the front end only.

Level 2: Aggressive, quick, not too precise. May still have many false positives (not as many as level 1 though), and few false negatives (but possibly more than level 1). Unlike level 1, it only warns when an address is taken. Warns about incomplete types. Runs in the front end only.

Level 3 (default for -Wstrict-aliasing): Should have very few false positives and few false negatives. Slightly slower than levels 1 or 2 when optimization is enabled. Takes care of the common pun+dereference pattern in the front end: *(int*)&some_float. If optimization is enabled, it also runs in the back end, where it deals with multiple statement cases using flow-sensitive points-to information. Only warns when the converted pointer is dereferenced. Does not warn about incomplete types.

-Wstrict-overflow
-Wstrict-overflow=n
This option is only active when signed overflow is undefined. It warns about cases where the compiler optimizes based on the assumption that signed overflow does not occur. Note that it does not warn about all cases where the code might overflow: it only warns about cases where the compiler implements some optimization. Thus this warning depends on the optimization level.

An optimization that assumes that signed overflow does not occur is perfectly safe if the values of the variables involved are such that overflow never does, in fact, occur. Therefore this warning can easily give a false positive: a warning about code that is not actually a problem. To help focus on important issues, several warning levels are defined. No warnings are issued for the use of undefined signed overflow when estimating how many iterations a loop requires, in particular when determining whether a loop will be executed at all.

-Wstrict-overflow=1
Warn about cases that are both questionable and easy to avoid. For example the compiler simplifies x + 1 > x to 1. This level of -Wstrict-overflow is enabled by -Wall; higher levels are not, and must be explicitly requested.

-Wstrict-overflow=2
Also warn about other cases where a comparison is simplified to a constant. For example: abs (x) >= 0. This can only be simplified when signed integer overflow is undefined, because abs (INT_MIN) overflows to INT_MIN, which is less than zero. -Wstrict-overflow (with no level) is the same as -Wstrict-overflow=2.

-Wstrict-overflow=3
Also warn about other cases where a comparison is simplified. For example: x + 1 > 1 is simplified to x > 0.

-Wstrict-overflow=4
Also warn about other simplifications not covered by the above cases. For example: (x * 10) / 5 is simplified to x * 2.

-Wstrict-overflow=5
Also warn about cases where the compiler reduces the magnitude of a constant involved in a comparison. For example: x + 2 > y is simplified to x + 1 >= y. This is reported only at the highest warning level because this simplification applies to many comparisons, so this warning level gives a very large number of false positives.

-Wstringop-overflow
-Wstringop-overflow=type
Warn for calls to string manipulation functions such as memcpy and strcpy that are determined to overflow the destination buffer. The optional argument is one greater than the type of Object Size Checking to perform to determine the size of the destination. See Object Size Checking. The argument is meaningful only for functions that operate on character arrays but not for raw memory functions like memcpy which always make use of Object Size type-0. The option also warns for calls that specify a size in excess of the largest possible object or at most SIZE_MAX / 2 bytes. The option produces the best results with optimization enabled but can detect a small subset of simple buffer overflows even without optimization in calls to the GCC built-in functions like __builtin_memcpy that correspond to the standard functions. In any case, the option warns about just a subset of buffer overflows detected by the corresponding overflow checking built-ins. For example, the option will issue a warning for the strcpy call below because it copies at least 5 characters (the string "blue" including the terminating NUL) into the buffer of size 4.

enum Color { blue, purple, yellow };
const char* f (enum Color clr)
{
  static char buf [4];
  const char *str;
  switch (clr)
    {
      case blue: str = "blue"; break;
      case purple: str = "purple"; break;
      case yellow: str = "yellow"; break;
    }

  return strcpy (buf, str);   // warning here
}
Option -Wstringop-overflow=2 is enabled by default.

-Wstringop-overflow
-Wstringop-overflow=1
The -Wstringop-overflow=1 option uses type-zero Object Size Checking to determine the sizes of destination objects. This is the default setting of the option. At this setting the option will not warn for writes past the end of subobjects of larger objects accessed by pointers unless the size of the largest surrounding object is known. When the destination may be one of several objects it is assumed to be the largest one of them. On Linux systems, when optimization is enabled at this setting the option warns for the same code as when the _FORTIFY_SOURCE macro is defined to a non-zero value.

-Wstringop-overflow=2
The -Wstringop-overflow=2 option uses type-one Object Size Checking to determine the sizes of destination objects. At this setting the option will warn about overflows when writing to members of the largest complete objects whose exact size is known. It will, however, not warn for excessive writes to the same members of unknown objects referenced by pointers since they may point to arrays containing unknown numbers of elements.

-Wstringop-overflow=3
The -Wstringop-overflow=3 option uses type-two Object Size Checking to determine the sizes of destination objects. At this setting the option warns about overflowing the smallest object or data member. This is the most restrictive setting of the option that may result in warnings for safe code.

-Wstringop-overflow=4
The -Wstringop-overflow=4 option uses type-three Object Size Checking to determine the sizes of destination objects. At this setting the option will warn about overflowing any data members, and when the destination is one of several objects it uses the size of the largest of them to decide whether to issue a warning. Similarly to -Wstringop-overflow=3 this setting of the option may result in warnings for benign code.

-Wstringop-truncation
Warn for calls to bounded string manipulation functions such as strncat, strncpy, and stpncpy that may either truncate the copied string or leave the destination unchanged.

In the following example, the call to strncat specifies a bound that is less than the length of the source string. As a result, the copy of the source will be truncated and so the call is diagnosed. To avoid the warning use bufsize - strlen (buf) - 1) as the bound.

void append (char *buf, size_t bufsize)
{
  strncat (buf, ".txt", 3);
}
As another example, the following call to strncpy results in copying to d just the characters preceding the terminating NUL, without appending the NUL to the end. Assuming the result of strncpy is necessarily a NUL-terminated string is a common mistake, and so the call is diagnosed. To avoid the warning when the result is not expected to be NUL-terminated, call memcpy instead.

void copy (char *d, const char *s)
{
  strncpy (d, s, strlen (s));
}
In the following example, the call to strncpy specifies the size of the destination buffer as the bound. If the length of the source string is equal to or greater than this size the result of the copy will not be NUL-terminated. Therefore, the call is also diagnosed. To avoid the warning, specify sizeof buf - 1 as the bound and set the last element of the buffer to NUL.

void copy (const char *s)
{
  char buf[80];
  strncpy (buf, s, sizeof buf);
  …
}
In situations where a character array is intended to store a sequence of bytes with no terminating NUL such an array may be annotated with attribute nonstring to avoid this warning. Such arrays, however, are not suitable arguments to functions that expect NUL-terminated strings. To help detect accidental misuses of such arrays GCC issues warnings unless it can prove that the use is safe. See Common Variable Attributes.

-Wsuggest-attribute=[pure|const|noreturn|format|cold|malloc]
Warn for cases where adding an attribute may be beneficial. The attributes currently supported are listed below.

-Wsuggest-attribute=pure
-Wsuggest-attribute=const
-Wsuggest-attribute=noreturn
-Wsuggest-attribute=malloc
Warn about functions that might be candidates for attributes pure, const or noreturn or malloc. The compiler only warns for functions visible in other compilation units or (in the case of pure and const) if it cannot prove that the function returns normally. A function returns normally if it doesn’t contain an infinite loop or return abnormally by throwing, calling abort or trapping. This analysis requires option -fipa-pure-const, which is enabled by default at -O and higher. Higher optimization levels improve the accuracy of the analysis.

-Wsuggest-attribute=format
-Wmissing-format-attribute
Warn about function pointers that might be candidates for format attributes. Note these are only possible candidates, not absolute ones. GCC guesses that function pointers with format attributes that are used in assignment, initialization, parameter passing or return statements should have a corresponding format attribute in the resulting type. I.e. the left-hand side of the assignment or initialization, the type of the parameter variable, or the return type of the containing function respectively should also have a format attribute to avoid the warning.

GCC also warns about function definitions that might be candidates for format attributes. Again, these are only possible candidates. GCC guesses that format attributes might be appropriate for any function that calls a function like vprintf or vscanf, but this might not always be the case, and some functions for which format attributes are appropriate may not be detected.

-Wsuggest-attribute=cold
Warn about functions that might be candidates for cold attribute. This is based on static detection and generally will only warn about functions which always leads to a call to another cold function such as wrappers of C++ throw or fatal error reporting functions leading to abort.

-Wsuggest-final-types
Warn about types with virtual methods where code quality would be improved if the type were declared with the C++11 final specifier, or, if possible, declared in an anonymous namespace. This allows GCC to more aggressively devirtualize the polymorphic calls. This warning is more effective with link time optimization, where the information about the class hierarchy graph is more complete.

-Wsuggest-final-methods
Warn about virtual methods where code quality would be improved if the method were declared with the C++11 final specifier, or, if possible, its type were declared in an anonymous namespace or with the final specifier. This warning is more effective with link-time optimization, where the information about the class hierarchy graph is more complete. It is recommended to first consider suggestions of -Wsuggest-final-types and then rebuild with new annotations.

-Wsuggest-override
Warn about overriding virtual functions that are not marked with the override keyword.

-Walloc-zero
Warn about calls to allocation functions decorated with attribute alloc_size that specify zero bytes, including those to the built-in forms of the functions aligned_alloc, alloca, calloc, malloc, and realloc. Because the behavior of these functions when called with a zero size differs among implementations (and in the case of realloc has been deprecated) relying on it may result in subtle portability bugs and should be avoided.

-Walloc-size-larger-than=byte-size
Warn about calls to functions decorated with attribute alloc_size that attempt to allocate objects larger than the specified number of bytes, or where the result of the size computation in an integer type with infinite precision would exceed the value of ‘PTRDIFF_MAX’ on the target. -Walloc-size-larger-than=‘PTRDIFF_MAX’ is enabled by default. Warnings controlled by the option can be disabled either by specifying byte-size of ‘SIZE_MAX’ or more or by -Wno-alloc-size-larger-than. See Function Attributes.

-Wno-alloc-size-larger-than
Disable -Walloc-size-larger-than= warnings. The option is equivalent to -Walloc-size-larger-than=‘SIZE_MAX’ or larger.

-Walloca
This option warns on all uses of alloca in the source.

-Walloca-larger-than=byte-size
This option warns on calls to alloca with an integer argument whose value is either zero, or that is not bounded by a controlling predicate that limits its value to at most byte-size. It also warns for calls to alloca where the bound value is unknown. Arguments of non-integer types are considered unbounded even if they appear to be constrained to the expected range.

For example, a bounded case of alloca could be:

void func (size_t n)
{
  void *p;
  if (n <= 1000)
    p = alloca (n);
  else
    p = malloc (n);
  f (p);
}
In the above example, passing -Walloca-larger-than=1000 would not issue a warning because the call to alloca is known to be at most 1000 bytes. However, if -Walloca-larger-than=500 were passed, the compiler would emit a warning.

Unbounded uses, on the other hand, are uses of alloca with no controlling predicate constraining its integer argument. For example:

void func ()
{
  void *p = alloca (n);
  f (p);
}
If -Walloca-larger-than=500 were passed, the above would trigger a warning, but this time because of the lack of bounds checking.

Note, that even seemingly correct code involving signed integers could cause a warning:

void func (signed int n)
{
  if (n < 500)
    {
      p = alloca (n);
      f (p);
    }
}
In the above example, n could be negative, causing a larger than expected argument to be implicitly cast into the alloca call.

This option also warns when alloca is used in a loop.

-Walloca-larger-than=‘PTRDIFF_MAX’ is enabled by default but is usually only effective when -ftree-vrp is active (default for -O2 and above).

See also -Wvla-larger-than=‘byte-size’.

-Wno-alloca-larger-than
Disable -Walloca-larger-than= warnings. The option is equivalent to -Walloca-larger-than=‘SIZE_MAX’ or larger.

-Warray-bounds
-Warray-bounds=n
This option is only active when -ftree-vrp is active (default for -O2 and above). It warns about subscripts to arrays that are always out of bounds. This warning is enabled by -Wall.

-Warray-bounds=1
This is the warning level of -Warray-bounds and is enabled by -Wall; higher levels are not, and must be explicitly requested.

-Warray-bounds=2
This warning level also warns about out of bounds access for arrays at the end of a struct and for arrays accessed through pointers. This warning level may give a larger number of false positives and is deactivated by default.

-Wattribute-alias
Warn about declarations using the alias and similar attributes whose target is incompatible with the type of the alias. See Declaring Attributes of Functions.

-Wbool-compare
Warn about boolean expression compared with an integer value different from true/false. For instance, the following comparison is always false:

int n = 5;
…
if ((n > 1) == 2) { … }
This warning is enabled by -Wall.

-Wbool-operation
Warn about suspicious operations on expressions of a boolean type. For instance, bitwise negation of a boolean is very likely a bug in the program. For C, this warning also warns about incrementing or decrementing a boolean, which rarely makes sense. (In C++, decrementing a boolean is always invalid. Incrementing a boolean is invalid in C++17, and deprecated otherwise.)

This warning is enabled by -Wall.

-Wduplicated-branches
Warn when an if-else has identical branches. This warning detects cases like

if (p != NULL)
  return 0;
else
  return 0;
It doesn’t warn when both branches contain just a null statement. This warning also warn for conditional operators:

  int i = x ? *p : *p;
-Wduplicated-cond
Warn about duplicated conditions in an if-else-if chain. For instance, warn for the following code:

if (p->q != NULL) { … }
else if (p->q != NULL) { … }
-Wframe-address
Warn when the ‘__builtin_frame_address’ or ‘__builtin_return_address’ is called with an argument greater than 0. Such calls may return indeterminate values or crash the program. The warning is included in -Wall.

-Wno-discarded-qualifiers (C and Objective-C only)
Do not warn if type qualifiers on pointers are being discarded. Typically, the compiler warns if a const char * variable is passed to a function that takes a char * parameter. This option can be used to suppress such a warning.

-Wno-discarded-array-qualifiers (C and Objective-C only)
Do not warn if type qualifiers on arrays which are pointer targets are being discarded. Typically, the compiler warns if a const int (*)[] variable is passed to a function that takes a int (*)[] parameter. This option can be used to suppress such a warning.

-Wno-incompatible-pointer-types (C and Objective-C only)
Do not warn when there is a conversion between pointers that have incompatible types. This warning is for cases not covered by -Wno-pointer-sign, which warns for pointer argument passing or assignment with different signedness.

-Wno-int-conversion (C and Objective-C only)
Do not warn about incompatible integer to pointer and pointer to integer conversions. This warning is about implicit conversions; for explicit conversions the warnings -Wno-int-to-pointer-cast and -Wno-pointer-to-int-cast may be used.

-Wno-div-by-zero
Do not warn about compile-time integer division by zero. Floating-point division by zero is not warned about, as it can be a legitimate way of obtaining infinities and NaNs.

-Wsystem-headers
Print warning messages for constructs found in system header files. Warnings from system headers are normally suppressed, on the assumption that they usually do not indicate real problems and would only make the compiler output harder to read. Using this command-line option tells GCC to emit warnings from system headers as if they occurred in user code. However, note that using -Wall in conjunction with this option does not warn about unknown pragmas in system headers—for that, -Wunknown-pragmas must also be used.

-Wtautological-compare
Warn if a self-comparison always evaluates to true or false. This warning detects various mistakes such as:

int i = 1;
…
if (i > i) { … }
This warning also warns about bitwise comparisons that always evaluate to true or false, for instance:

if ((a & 16) == 10) { … }
will always be false.

This warning is enabled by -Wall.

-Wtrampolines
Warn about trampolines generated for pointers to nested functions. A trampoline is a small piece of data or code that is created at run time on the stack when the address of a nested function is taken, and is used to call the nested function indirectly. For some targets, it is made up of data only and thus requires no special treatment. But, for most targets, it is made up of code and thus requires the stack to be made executable in order for the program to work properly.

-Wfloat-equal
Warn if floating-point values are used in equality comparisons.

The idea behind this is that sometimes it is convenient (for the programmer) to consider floating-point values as approximations to infinitely precise real numbers. If you are doing this, then you need to compute (by analyzing the code, or in some other way) the maximum or likely maximum error that the computation introduces, and allow for it when performing comparisons (and when producing output, but that’s a different problem). In particular, instead of testing for equality, you should check to see whether the two values have ranges that overlap; and this is done with the relational operators, so equality comparisons are probably mistaken.

-Wtraditional (C and Objective-C only)
Warn about certain constructs that behave differently in traditional and ISO C. Also warn about ISO C constructs that have no traditional C equivalent, and/or problematic constructs that should be avoided.

Macro parameters that appear within string literals in the macro body. In traditional C macro replacement takes place within string literals, but in ISO C it does not.
In traditional C, some preprocessor directives did not exist. Traditional preprocessors only considered a line to be a directive if the ‘#’ appeared in column 1 on the line. Therefore -Wtraditional warns about directives that traditional C understands but ignores because the ‘#’ does not appear as the first character on the line. It also suggests you hide directives like #pragma not understood by traditional C by indenting them. Some traditional implementations do not recognize #elif, so this option suggests avoiding it altogether.
A function-like macro that appears without arguments.
The unary plus operator.
The ‘U’ integer constant suffix, or the ‘F’ or ‘L’ floating-point constant suffixes. (Traditional C does support the ‘L’ suffix on integer constants.) Note, these suffixes appear in macros defined in the system headers of most modern systems, e.g. the ‘_MIN’/‘_MAX’ macros in <limits.h>. Use of these macros in user code might normally lead to spurious warnings, however GCC’s integrated preprocessor has enough context to avoid warning in these cases.
A function declared external in one block and then used after the end of the block.
A switch statement has an operand of type long.
A non-static function declaration follows a static one. This construct is not accepted by some traditional C compilers.
The ISO type of an integer constant has a different width or signedness from its traditional type. This warning is only issued if the base of the constant is ten. I.e. hexadecimal or octal values, which typically represent bit patterns, are not warned about.
Usage of ISO string concatenation is detected.
Initialization of automatic aggregates.
Identifier conflicts with labels. Traditional C lacks a separate namespace for labels.
Initialization of unions. If the initializer is zero, the warning is omitted. This is done under the assumption that the zero initializer in user code appears conditioned on e.g. __STDC__ to avoid missing initializer warnings and relies on default initialization to zero in the traditional C case.
Conversions by prototypes between fixed/floating-point values and vice versa. The absence of these prototypes when compiling with traditional C causes serious problems. This is a subset of the possible conversion warnings; for the full set use -Wtraditional-conversion.
Use of ISO C style function definitions. This warning intentionally is not issued for prototype declarations or variadic functions because these ISO C features appear in your code when using libiberty’s traditional C compatibility macros, PARAMS and VPARAMS. This warning is also bypassed for nested functions because that feature is already a GCC extension and thus not relevant to traditional C compatibility.
-Wtraditional-conversion (C and Objective-C only)
Warn if a prototype causes a type conversion that is different from what would happen to the same argument in the absence of a prototype. This includes conversions of fixed point to floating and vice versa, and conversions changing the width or signedness of a fixed-point argument except when the same as the default promotion.

-Wdeclaration-after-statement (C and Objective-C only)
Warn when a declaration is found after a statement in a block. This construct, known from C++, was introduced with ISO C99 and is by default allowed in GCC. It is not supported by ISO C90. See Mixed Declarations.

-Wshadow
Warn whenever a local variable or type declaration shadows another variable, parameter, type, class member (in C++), or instance variable (in Objective-C) or whenever a built-in function is shadowed. Note that in C++, the compiler warns if a local variable shadows an explicit typedef, but not if it shadows a struct/class/enum. Same as -Wshadow=global.

-Wno-shadow-ivar (Objective-C only)
Do not warn whenever a local variable shadows an instance variable in an Objective-C method.

-Wshadow=global
The default for -Wshadow. Warns for any (global) shadowing.

-Wshadow=local
Warn when a local variable shadows another local variable or parameter. This warning is enabled by -Wshadow=global.

-Wshadow=compatible-local
Warn when a local variable shadows another local variable or parameter whose type is compatible with that of the shadowing variable. In C++, type compatibility here means the type of the shadowing variable can be converted to that of the shadowed variable. The creation of this flag (in addition to -Wshadow=local) is based on the idea that when a local variable shadows another one of incompatible type, it is most likely intentional, not a bug or typo, as shown in the following example:

for (SomeIterator i = SomeObj.begin(); i != SomeObj.end(); ++i)
{
  for (int i = 0; i < N; ++i)
  {
    ...
  }
  ...
}
Since the two variable i in the example above have incompatible types, enabling only -Wshadow=compatible-local will not emit a warning. Because their types are incompatible, if a programmer accidentally uses one in place of the other, type checking will catch that and emit an error or warning. So not warning (about shadowing) in this case will not lead to undetected bugs. Use of this flag instead of -Wshadow=local can possibly reduce the number of warnings triggered by intentional shadowing.

This warning is enabled by -Wshadow=local.

-Wlarger-than=byte-size
Warn whenever an object is defined whose size exceeds byte-size. -Wlarger-than=‘PTRDIFF_MAX’ is enabled by default. Warnings controlled by the option can be disabled either by specifying byte-size of ‘SIZE_MAX’ or more or by -Wno-larger-than.

-Wno-larger-than
Disable -Wlarger-than= warnings. The option is equivalent to -Wlarger-than=‘SIZE_MAX’ or larger.

-Wframe-larger-than=byte-size
Warn if the size of a function frame exceeds byte-size. The computation done to determine the stack frame size is approximate and not conservative. The actual requirements may be somewhat greater than byte-size even if you do not get a warning. In addition, any space allocated via alloca, variable-length arrays, or related constructs is not included by the compiler when determining whether or not to issue a warning. -Wframe-larger-than=‘PTRDIFF_MAX’ is enabled by default. Warnings controlled by the option can be disabled either by specifying byte-size of ‘SIZE_MAX’ or more or by -Wno-frame-larger-than.

-Wno-frame-larger-than
Disable -Wframe-larger-than= warnings. The option is equivalent to -Wframe-larger-than=‘SIZE_MAX’ or larger.

-Wno-free-nonheap-object
Do not warn when attempting to free an object that was not allocated on the heap.

-Wstack-usage=byte-size
Warn if the stack usage of a function might exceed byte-size. The computation done to determine the stack usage is conservative. Any space allocated via alloca, variable-length arrays, or related constructs is included by the compiler when determining whether or not to issue a warning.

The message is in keeping with the output of -fstack-usage.

If the stack usage is fully static but exceeds the specified amount, it’s:
  warning: stack usage is 1120 bytes
If the stack usage is (partly) dynamic but bounded, it’s:
  warning: stack usage might be 1648 bytes
If the stack usage is (partly) dynamic and not bounded, it’s:
  warning: stack usage might be unbounded
-Wstack-usage=‘PTRDIFF_MAX’ is enabled by default. Warnings controlled by the option can be disabled either by specifying byte-size of ‘SIZE_MAX’ or more or by -Wno-stack-usage.

-Wno-stack-usage
Disable -Wstack-usage= warnings. The option is equivalent to -Wstack-usage=‘SIZE_MAX’ or larger.

-Wunsafe-loop-optimizations
Warn if the loop cannot be optimized because the compiler cannot assume anything on the bounds of the loop indices. With -funsafe-loop-optimizations warn if the compiler makes such assumptions.

-Wno-pedantic-ms-format (MinGW targets only)
When used in combination with -Wformat and -pedantic without GNU extensions, this option disables the warnings about non-ISO printf / scanf format width specifiers I32, I64, and I used on Windows targets, which depend on the MS runtime.

-Waligned-new
Warn about a new-expression of a type that requires greater alignment than the alignof(std::max_align_t) but uses an allocation function without an explicit alignment parameter. This option is enabled by -Wall.

Normally this only warns about global allocation functions, but -Waligned-new=all also warns about class member allocation functions.

-Wplacement-new
-Wplacement-new=n
Warn about placement new expressions with undefined behavior, such as constructing an object in a buffer that is smaller than the type of the object. For example, the placement new expression below is diagnosed because it attempts to construct an array of 64 integers in a buffer only 64 bytes large.

char buf [64];
new (buf) int[64];
This warning is enabled by default.

-Wplacement-new=1
This is the default warning level of -Wplacement-new. At this level the warning is not issued for some strictly undefined constructs that GCC allows as extensions for compatibility with legacy code. For example, the following new expression is not diagnosed at this level even though it has undefined behavior according to the C++ standard because it writes past the end of the one-element array.

struct S { int n, a[1]; };
S *s = (S *)malloc (sizeof *s + 31 * sizeof s->a[0]);
new (s->a)int [32]();
-Wplacement-new=2
At this level, in addition to diagnosing all the same constructs as at level 1, a diagnostic is also issued for placement new expressions that construct an object in the last member of structure whose type is an array of a single element and whose size is less than the size of the object being constructed. While the previous example would be diagnosed, the following construct makes use of the flexible member array extension to avoid the warning at level 2.

struct S { int n, a[]; };
S *s = (S *)malloc (sizeof *s + 32 * sizeof s->a[0]);
new (s->a)int [32]();
-Wpointer-arith
Warn about anything that depends on the “size of” a function type or of void. GNU C assigns these types a size of 1, for convenience in calculations with void * pointers and pointers to functions. In C++, warn also when an arithmetic operation involves NULL. This warning is also enabled by -Wpedantic.

-Wpointer-compare
Warn if a pointer is compared with a zero character constant. This usually means that the pointer was meant to be dereferenced. For example:

const char *p = foo ();
if (p == '\0')
  return 42;
Note that the code above is invalid in C++11.

This warning is enabled by default.

-Wtype-limits
Warn if a comparison is always true or always false due to the limited range of the data type, but do not warn for constant expressions. For example, warn if an unsigned variable is compared against zero with < or >=. This warning is also enabled by -Wextra.

-Wabsolute-value (C and Objective-C only)
Warn when a wrong absolute value function seems to be used or when it does not have any effect because its argument is an unsigned type. This warning be suppressed with an explicit type cast and it is also enabled by -Wextra.

-Wcomment
-Wcomments
Warn whenever a comment-start sequence ‘/*’ appears in a ‘/*’ comment, or whenever a backslash-newline appears in a ‘//’ comment. This warning is enabled by -Wall.

-Wtrigraphs
Warn if any trigraphs are encountered that might change the meaning of the program. Trigraphs within comments are not warned about, except those that would form escaped newlines.

This option is implied by -Wall. If -Wall is not given, this option is still enabled unless trigraphs are enabled. To get trigraph conversion without warnings, but get the other -Wall warnings, use ‘-trigraphs -Wall -Wno-trigraphs’.

-Wundef
Warn if an undefined identifier is evaluated in an #if directive. Such identifiers are replaced with zero.

-Wexpansion-to-defined
Warn whenever ‘defined’ is encountered in the expansion of a macro (including the case where the macro is expanded by an ‘#if’ directive). Such usage is not portable. This warning is also enabled by -Wpedantic and -Wextra.

-Wunused-macros
Warn about macros defined in the main file that are unused. A macro is used if it is expanded or tested for existence at least once. The preprocessor also warns if the macro has not been used at the time it is redefined or undefined.

Built-in macros, macros defined on the command line, and macros defined in include files are not warned about.

Note: If a macro is actually used, but only used in skipped conditional blocks, then the preprocessor reports it as unused. To avoid the warning in such a case, you might improve the scope of the macro’s definition by, for example, moving it into the first skipped block. Alternatively, you could provide a dummy use with something like:

#if defined the_macro_causing_the_warning
#endif
-Wno-endif-labels
Do not warn whenever an #else or an #endif are followed by text. This sometimes happens in older programs with code of the form

#if FOO
…
#else FOO
…
#endif FOO
The second and third FOO should be in comments. This warning is on by default.

-Wbad-function-cast (C and Objective-C only)
Warn when a function call is cast to a non-matching type. For example, warn if a call to a function returning an integer type is cast to a pointer type.

-Wc90-c99-compat (C and Objective-C only)
Warn about features not present in ISO C90, but present in ISO C99. For instance, warn about use of variable length arrays, long long type, bool type, compound literals, designated initializers, and so on. This option is independent of the standards mode. Warnings are disabled in the expression that follows __extension__.

-Wc99-c11-compat (C and Objective-C only)
Warn about features not present in ISO C99, but present in ISO C11. For instance, warn about use of anonymous structures and unions, _Atomic type qualifier, _Thread_local storage-class specifier, _Alignas specifier, Alignof operator, _Generic keyword, and so on. This option is independent of the standards mode. Warnings are disabled in the expression that follows __extension__.

-Wc++-compat (C and Objective-C only)
Warn about ISO C constructs that are outside of the common subset of ISO C and ISO C++, e.g. request for implicit conversion from void * to a pointer to non-void type.

-Wc++11-compat (C++ and Objective-C++ only)
Warn about C++ constructs whose meaning differs between ISO C++ 1998 and ISO C++ 2011, e.g., identifiers in ISO C++ 1998 that are keywords in ISO C++ 2011. This warning turns on -Wnarrowing and is enabled by -Wall.

-Wc++14-compat (C++ and Objective-C++ only)
Warn about C++ constructs whose meaning differs between ISO C++ 2011 and ISO C++ 2014. This warning is enabled by -Wall.

-Wc++17-compat (C++ and Objective-C++ only)
Warn about C++ constructs whose meaning differs between ISO C++ 2014 and ISO C++ 2017. This warning is enabled by -Wall.

-Wcast-qual
Warn whenever a pointer is cast so as to remove a type qualifier from the target type. For example, warn if a const char * is cast to an ordinary char *.

Also warn when making a cast that introduces a type qualifier in an unsafe way. For example, casting char ** to const char ** is unsafe, as in this example:

  /* p is char ** value.  */
  const char **q = (const char **) p;
  /* Assignment of readonly string to const char * is OK.  */
  *q = "string";
  /* Now char** pointer points to read-only memory.  */
  **p = 'b';
-Wcast-align
Warn whenever a pointer is cast such that the required alignment of the target is increased. For example, warn if a char * is cast to an int * on machines where integers can only be accessed at two- or four-byte boundaries.

-Wcast-align=strict
Warn whenever a pointer is cast such that the required alignment of the target is increased. For example, warn if a char * is cast to an int * regardless of the target machine.

-Wcast-function-type
Warn when a function pointer is cast to an incompatible function pointer. In a cast involving function types with a variable argument list only the types of initial arguments that are provided are considered. Any parameter of pointer-type matches any other pointer-type. Any benign differences in integral types are ignored, like int vs. long on ILP32 targets. Likewise type qualifiers are ignored. The function type void (*) (void) is special and matches everything, which can be used to suppress this warning. In a cast involving pointer to member types this warning warns whenever the type cast is changing the pointer to member type. This warning is enabled by -Wextra.

-Wwrite-strings
When compiling C, give string constants the type const char[length] so that copying the address of one into a non-const char * pointer produces a warning. These warnings help you find at compile time code that can try to write into a string constant, but only if you have been very careful about using const in declarations and prototypes. Otherwise, it is just a nuisance. This is why we did not make -Wall request these warnings.

When compiling C++, warn about the deprecated conversion from string literals to char *. This warning is enabled by default for C++ programs.

-Wcatch-value
-Wcatch-value=n (C++ and Objective-C++ only)
Warn about catch handlers that do not catch via reference. With -Wcatch-value=1 (or -Wcatch-value for short) warn about polymorphic class types that are caught by value. With -Wcatch-value=2 warn about all class types that are caught by value. With -Wcatch-value=3 warn about all types that are not caught by reference. -Wcatch-value is enabled by -Wall.

-Wclobbered
Warn for variables that might be changed by longjmp or vfork. This warning is also enabled by -Wextra.

-Wconditionally-supported (C++ and Objective-C++ only)
Warn for conditionally-supported (C++11 [intro.defs]) constructs.

-Wconversion
Warn for implicit conversions that may alter a value. This includes conversions between real and integer, like abs (x) when x is double; conversions between signed and unsigned, like unsigned ui = -1; and conversions to smaller types, like sqrtf (M_PI). Do not warn for explicit casts like abs ((int) x) and ui = (unsigned) -1, or if the value is not changed by the conversion like in abs (2.0). Warnings about conversions between signed and unsigned integers can be disabled by using -Wno-sign-conversion.

For C++, also warn for confusing overload resolution for user-defined conversions; and conversions that never use a type conversion operator: conversions to void, the same type, a base class or a reference to them. Warnings about conversions between signed and unsigned integers are disabled by default in C++ unless -Wsign-conversion is explicitly enabled.

-Wno-conversion-null (C++ and Objective-C++ only)
Do not warn for conversions between NULL and non-pointer types. -Wconversion-null is enabled by default.

-Wzero-as-null-pointer-constant (C++ and Objective-C++ only)
Warn when a literal ‘0’ is used as null pointer constant. This can be useful to facilitate the conversion to nullptr in C++11.

-Wsubobject-linkage (C++ and Objective-C++ only)
Warn if a class type has a base or a field whose type uses the anonymous namespace or depends on a type with no linkage. If a type A depends on a type B with no or internal linkage, defining it in multiple translation units would be an ODR violation because the meaning of B is different in each translation unit. If A only appears in a single translation unit, the best way to silence the warning is to give it internal linkage by putting it in an anonymous namespace as well. The compiler doesn’t give this warning for types defined in the main .C file, as those are unlikely to have multiple definitions. -Wsubobject-linkage is enabled by default.

-Wdangling-else
Warn about constructions where there may be confusion to which if statement an else branch belongs. Here is an example of such a case:

{
  if (a)
    if (b)
      foo ();
  else
    bar ();
}
In C/C++, every else branch belongs to the innermost possible if statement, which in this example is if (b). This is often not what the programmer expected, as illustrated in the above example by indentation the programmer chose. When there is the potential for this confusion, GCC issues a warning when this flag is specified. To eliminate the warning, add explicit braces around the innermost if statement so there is no way the else can belong to the enclosing if. The resulting code looks like this:

{
  if (a)
    {
      if (b)
        foo ();
      else
        bar ();
    }
}
This warning is enabled by -Wparentheses.

-Wdate-time
Warn when macros __TIME__, __DATE__ or __TIMESTAMP__ are encountered as they might prevent bit-wise-identical reproducible compilations.

-Wdelete-incomplete (C++ and Objective-C++ only)
Warn when deleting a pointer to incomplete type, which may cause undefined behavior at runtime. This warning is enabled by default.

-Wuseless-cast (C++ and Objective-C++ only)
Warn when an expression is casted to its own type.

-Wempty-body
Warn if an empty body occurs in an if, else or do while statement. This warning is also enabled by -Wextra.

-Wenum-compare
Warn about a comparison between values of different enumerated types. In C++ enumerated type mismatches in conditional expressions are also diagnosed and the warning is enabled by default. In C this warning is enabled by -Wall.

-Wextra-semi (C++, Objective-C++ only)
Warn about redundant semicolon after in-class function definition.

-Wjump-misses-init (C, Objective-C only)
Warn if a goto statement or a switch statement jumps forward across the initialization of a variable, or jumps backward to a label after the variable has been initialized. This only warns about variables that are initialized when they are declared. This warning is only supported for C and Objective-C; in C++ this sort of branch is an error in any case.

-Wjump-misses-init is included in -Wc++-compat. It can be disabled with the -Wno-jump-misses-init option.

-Wsign-compare
Warn when a comparison between signed and unsigned values could produce an incorrect result when the signed value is converted to unsigned. In C++, this warning is also enabled by -Wall. In C, it is also enabled by -Wextra.

-Wsign-conversion
Warn for implicit conversions that may change the sign of an integer value, like assigning a signed integer expression to an unsigned integer variable. An explicit cast silences the warning. In C, this option is enabled also by -Wconversion.

-Wfloat-conversion
Warn for implicit conversions that reduce the precision of a real value. This includes conversions from real to integer, and from higher precision real to lower precision real values. This option is also enabled by -Wconversion.

-Wno-scalar-storage-order
Do not warn on suspicious constructs involving reverse scalar storage order.

-Wsized-deallocation (C++ and Objective-C++ only)
Warn about a definition of an unsized deallocation function

void operator delete (void *) noexcept;
void operator delete[] (void *) noexcept;
without a definition of the corresponding sized deallocation function

void operator delete (void *, std::size_t) noexcept;
void operator delete[] (void *, std::size_t) noexcept;
or vice versa. Enabled by -Wextra along with -fsized-deallocation.

-Wsizeof-pointer-div
Warn for suspicious divisions of two sizeof expressions that divide the pointer size by the element size, which is the usual way to compute the array size but won’t work out correctly with pointers. This warning warns e.g. about sizeof (ptr) / sizeof (ptr[0]) if ptr is not an array, but a pointer. This warning is enabled by -Wall.

-Wsizeof-pointer-memaccess
Warn for suspicious length parameters to certain string and memory built-in functions if the argument uses sizeof. This warning triggers for example for memset (ptr, 0, sizeof (ptr)); if ptr is not an array, but a pointer, and suggests a possible fix, or about memcpy (&foo, ptr, sizeof (&foo));. -Wsizeof-pointer-memaccess also warns about calls to bounded string copy functions like strncat or strncpy that specify as the bound a sizeof expression of the source array. For example, in the following function the call to strncat specifies the size of the source string as the bound. That is almost certainly a mistake and so the call is diagnosed.

void make_file (const char *name)
{
  char path[PATH_MAX];
  strncpy (path, name, sizeof path - 1);
  strncat (path, ".text", sizeof ".text");
  …
}
The -Wsizeof-pointer-memaccess option is enabled by -Wall.

-Wsizeof-array-argument
Warn when the sizeof operator is applied to a parameter that is declared as an array in a function definition. This warning is enabled by default for C and C++ programs.

-Wmemset-elt-size
Warn for suspicious calls to the memset built-in function, if the first argument references an array, and the third argument is a number equal to the number of elements, but not equal to the size of the array in memory. This indicates that the user has omitted a multiplication by the element size. This warning is enabled by -Wall.

-Wmemset-transposed-args
Warn for suspicious calls to the memset built-in function, if the second argument is not zero and the third argument is zero. This warns e.g. about memset (buf, sizeof buf, 0) where most probably memset (buf, 0, sizeof buf) was meant instead. The diagnostics is only emitted if the third argument is literal zero. If it is some expression that is folded to zero, a cast of zero to some type, etc., it is far less likely that the user has mistakenly exchanged the arguments and no warning is emitted. This warning is enabled by -Wall.

-Waddress
Warn about suspicious uses of memory addresses. These include using the address of a function in a conditional expression, such as void func(void); if (func), and comparisons against the memory address of a string literal, such as if (x == "abc"). Such uses typically indicate a programmer error: the address of a function always evaluates to true, so their use in a conditional usually indicate that the programmer forgot the parentheses in a function call; and comparisons against string literals result in unspecified behavior and are not portable in C, so they usually indicate that the programmer intended to use strcmp. This warning is enabled by -Wall.

-Wlogical-op
Warn about suspicious uses of logical operators in expressions. This includes using logical operators in contexts where a bit-wise operator is likely to be expected. Also warns when the operands of a logical operator are the same:

extern int a;
if (a < 0 && a < 0) { … }
-Wlogical-not-parentheses
Warn about logical not used on the left hand side operand of a comparison. This option does not warn if the right operand is considered to be a boolean expression. Its purpose is to detect suspicious code like the following:

int a;
…
if (!a > 1) { … }
It is possible to suppress the warning by wrapping the LHS into parentheses:

if ((!a) > 1) { … }
This warning is enabled by -Wall.

-Waggregate-return
Warn if any functions that return structures or unions are defined or called. (In languages where you can return an array, this also elicits a warning.)

-Wno-aggressive-loop-optimizations
Warn if in a loop with constant number of iterations the compiler detects undefined behavior in some statement during one or more of the iterations.

-Wno-attributes
Do not warn if an unexpected __attribute__ is used, such as unrecognized attributes, function attributes applied to variables, etc. This does not stop errors for incorrect use of supported attributes.

-Wno-builtin-declaration-mismatch
Warn if a built-in function is declared with the wrong signature or as non-function. This warning is enabled by default.

-Wno-builtin-macro-redefined
Do not warn if certain built-in macros are redefined. This suppresses warnings for redefinition of __TIMESTAMP__, __TIME__, __DATE__, __FILE__, and __BASE_FILE__.

-Wstrict-prototypes (C and Objective-C only)
Warn if a function is declared or defined without specifying the argument types. (An old-style function definition is permitted without a warning if preceded by a declaration that specifies the argument types.)

-Wold-style-declaration (C and Objective-C only)
Warn for obsolescent usages, according to the C Standard, in a declaration. For example, warn if storage-class specifiers like static are not the first things in a declaration. This warning is also enabled by -Wextra.

-Wold-style-definition (C and Objective-C only)
Warn if an old-style function definition is used. A warning is given even if there is a previous prototype.

-Wmissing-parameter-type (C and Objective-C only)
A function parameter is declared without a type specifier in K&R-style functions:

void foo(bar) { }
This warning is also enabled by -Wextra.

-Wmissing-prototypes (C and Objective-C only)
Warn if a global function is defined without a previous prototype declaration. This warning is issued even if the definition itself provides a prototype. Use this option to detect global functions that do not have a matching prototype declaration in a header file. This option is not valid for C++ because all function declarations provide prototypes and a non-matching declaration declares an overload rather than conflict with an earlier declaration. Use -Wmissing-declarations to detect missing declarations in C++.

-Wmissing-declarations
Warn if a global function is defined without a previous declaration. Do so even if the definition itself provides a prototype. Use this option to detect global functions that are not declared in header files. In C, no warnings are issued for functions with previous non-prototype declarations; use -Wmissing-prototypes to detect missing prototypes. In C++, no warnings are issued for function templates, or for inline functions, or for functions in anonymous namespaces.

-Wmissing-field-initializers
Warn if a structure’s initializer has some fields missing. For example, the following code causes such a warning, because x.h is implicitly zero:

struct s { int f, g, h; };
struct s x = { 3, 4 };
This option does not warn about designated initializers, so the following modification does not trigger a warning:

struct s { int f, g, h; };
struct s x = { .f = 3, .g = 4 };
In C this option does not warn about the universal zero initializer ‘{ 0 }’:

struct s { int f, g, h; };
struct s x = { 0 };
Likewise, in C++ this option does not warn about the empty { } initializer, for example:

struct s { int f, g, h; };
s x = { };
This warning is included in -Wextra. To get other -Wextra warnings without this one, use -Wextra -Wno-missing-field-initializers.

-Wno-multichar
Do not warn if a multicharacter constant (‘'FOOF'’) is used. Usually they indicate a typo in the user’s code, as they have implementation-defined values, and should not be used in portable code.

-Wnormalized=[none|id|nfc|nfkc]
In ISO C and ISO C++, two identifiers are different if they are different sequences of characters. However, sometimes when characters outside the basic ASCII character set are used, you can have two different character sequences that look the same. To avoid confusion, the ISO 10646 standard sets out some normalization rules which when applied ensure that two sequences that look the same are turned into the same sequence. GCC can warn you if you are using identifiers that have not been normalized; this option controls that warning.

There are four levels of warning supported by GCC. The default is -Wnormalized=nfc, which warns about any identifier that is not in the ISO 10646 “C” normalized form, NFC. NFC is the recommended form for most uses. It is equivalent to -Wnormalized.

Unfortunately, there are some characters allowed in identifiers by ISO C and ISO C++ that, when turned into NFC, are not allowed in identifiers. That is, there’s no way to use these symbols in portable ISO C or C++ and have all your identifiers in NFC. -Wnormalized=id suppresses the warning for these characters. It is hoped that future versions of the standards involved will correct this, which is why this option is not the default.

You can switch the warning off for all characters by writing -Wnormalized=none or -Wno-normalized. You should only do this if you are using some other normalization scheme (like “D”), because otherwise you can easily create bugs that are literally impossible to see.

Some characters in ISO 10646 have distinct meanings but look identical in some fonts or display methodologies, especially once formatting has been applied. For instance \u207F, “SUPERSCRIPT LATIN SMALL LETTER N”, displays just like a regular n that has been placed in a superscript. ISO 10646 defines the NFKC normalization scheme to convert all these into a standard form as well, and GCC warns if your code is not in NFKC if you use -Wnormalized=nfkc. This warning is comparable to warning about every identifier that contains the letter O because it might be confused with the digit 0, and so is not the default, but may be useful as a local coding convention if the programming environment cannot be fixed to display these characters distinctly.

-Wno-deprecated
Do not warn about usage of deprecated features. See Deprecated Features.

-Wno-deprecated-declarations
Do not warn about uses of functions (see Function Attributes), variables (see Variable Attributes), and types (see Type Attributes) marked as deprecated by using the deprecated attribute.

-Wno-overflow
Do not warn about compile-time overflow in constant expressions.

-Wno-odr
Warn about One Definition Rule violations during link-time optimization. Requires -flto-odr-type-merging to be enabled. Enabled by default.

-Wopenmp-simd
Warn if the vectorizer cost model overrides the OpenMP simd directive set by user. The -fsimd-cost-model=unlimited option can be used to relax the cost model.

-Woverride-init (C and Objective-C only)
Warn if an initialized field without side effects is overridden when using designated initializers (see Designated Initializers).

This warning is included in -Wextra. To get other -Wextra warnings without this one, use -Wextra -Wno-override-init.

-Woverride-init-side-effects (C and Objective-C only)
Warn if an initialized field with side effects is overridden when using designated initializers (see Designated Initializers). This warning is enabled by default.

-Wpacked
Warn if a structure is given the packed attribute, but the packed attribute has no effect on the layout or size of the structure. Such structures may be mis-aligned for little benefit. For instance, in this code, the variable f.x in struct bar is misaligned even though struct bar does not itself have the packed attribute:

struct foo {
  int x;
  char a, b, c, d;
} __attribute__((packed));
struct bar {
  char z;
  struct foo f;
};
-Wpacked-bitfield-compat
The 4.1, 4.2 and 4.3 series of GCC ignore the packed attribute on bit-fields of type char. This has been fixed in GCC 4.4 but the change can lead to differences in the structure layout. GCC informs you when the offset of such a field has changed in GCC 4.4. For example there is no longer a 4-bit padding between field a and b in this structure:

struct foo
{
  char a:4;
  char b:8;
} __attribute__ ((packed));
This warning is enabled by default. Use -Wno-packed-bitfield-compat to disable this warning.

-Wpacked-not-aligned (C, C++, Objective-C and Objective-C++ only)
Warn if a structure field with explicitly specified alignment in a packed struct or union is misaligned. For example, a warning will be issued on struct S, like, warning: alignment 1 of 'struct S' is less than 8, in this code:

struct __attribute__ ((aligned (8))) S8 { char a[8]; };
struct __attribute__ ((packed)) S {
  struct S8 s8;
};
This warning is enabled by -Wall.

-Wpadded
Warn if padding is included in a structure, either to align an element of the structure or to align the whole structure. Sometimes when this happens it is possible to rearrange the fields of the structure to reduce the padding and so make the structure smaller.

-Wredundant-decls
Warn if anything is declared more than once in the same scope, even in cases where multiple declaration is valid and changes nothing.

-Wno-restrict
Warn when an object referenced by a restrict-qualified parameter (or, in C++, a __restrict-qualified parameter) is aliased by another argument, or when copies between such objects overlap. For example, the call to the strcpy function below attempts to truncate the string by replacing its initial characters with the last four. However, because the call writes the terminating NUL into a[4], the copies overlap and the call is diagnosed.

void foo (void)
{
  char a[] = "abcd1234";
  strcpy (a, a + 4);
  …
}
The -Wrestrict option detects some instances of simple overlap even without optimization but works best at -O2 and above. It is included in -Wall.

-Wnested-externs (C and Objective-C only)
Warn if an extern declaration is encountered within a function.

-Wno-inherited-variadic-ctor
Suppress warnings about use of C++11 inheriting constructors when the base class inherited from has a C variadic constructor; the warning is on by default because the ellipsis is not inherited.

-Winline
Warn if a function that is declared as inline cannot be inlined. Even with this option, the compiler does not warn about failures to inline functions declared in system headers.

The compiler uses a variety of heuristics to determine whether or not to inline a function. For example, the compiler takes into account the size of the function being inlined and the amount of inlining that has already been done in the current function. Therefore, seemingly insignificant changes in the source program can cause the warnings produced by -Winline to appear or disappear.

-Wno-invalid-offsetof (C++ and Objective-C++ only)
Suppress warnings from applying the offsetof macro to a non-POD type. According to the 2014 ISO C++ standard, applying offsetof to a non-standard-layout type is undefined. In existing C++ implementations, however, offsetof typically gives meaningful results. This flag is for users who are aware that they are writing nonportable code and who have deliberately chosen to ignore the warning about it.

The restrictions on offsetof may be relaxed in a future version of the C++ standard.

-Wint-in-bool-context
Warn for suspicious use of integer values where boolean values are expected, such as conditional expressions (?:) using non-boolean integer constants in boolean context, like if (a <= b ? 2 : 3). Or left shifting of signed integers in boolean context, like for (a = 0; 1 << a; a++);. Likewise for all kinds of multiplications regardless of the data type. This warning is enabled by -Wall.

-Wno-int-to-pointer-cast
Suppress warnings from casts to pointer type of an integer of a different size. In C++, casting to a pointer type of smaller size is an error. Wint-to-pointer-cast is enabled by default.

-Wno-pointer-to-int-cast (C and Objective-C only)
Suppress warnings from casts from a pointer to an integer type of a different size.

-Winvalid-pch
Warn if a precompiled header (see Precompiled Headers) is found in the search path but cannot be used.

-Wlong-long
Warn if long long type is used. This is enabled by either -Wpedantic or -Wtraditional in ISO C90 and C++98 modes. To inhibit the warning messages, use -Wno-long-long.

-Wvariadic-macros
Warn if variadic macros are used in ISO C90 mode, or if the GNU alternate syntax is used in ISO C99 mode. This is enabled by either -Wpedantic or -Wtraditional. To inhibit the warning messages, use -Wno-variadic-macros.

-Wvarargs
Warn upon questionable usage of the macros used to handle variable arguments like va_start. This is default. To inhibit the warning messages, use -Wno-varargs.

-Wvector-operation-performance
Warn if vector operation is not implemented via SIMD capabilities of the architecture. Mainly useful for the performance tuning. Vector operation can be implemented piecewise, which means that the scalar operation is performed on every vector element; in parallel, which means that the vector operation is implemented using scalars of wider type, which normally is more performance efficient; and as a single scalar, which means that vector fits into a scalar type.

-Wno-virtual-move-assign
Suppress warnings about inheriting from a virtual base with a non-trivial C++11 move assignment operator. This is dangerous because if the virtual base is reachable along more than one path, it is moved multiple times, which can mean both objects end up in the moved-from state. If the move assignment operator is written to avoid moving from a moved-from object, this warning can be disabled.

-Wvla
Warn if a variable-length array is used in the code. -Wno-vla prevents the -Wpedantic warning of the variable-length array.

-Wvla-larger-than=byte-size
If this option is used, the compiler will warn for declarations of variable-length arrays whose size is either unbounded, or bounded by an argument that allows the array size to exceed byte-size bytes. This is similar to how -Walloca-larger-than=byte-size works, but with variable-length arrays.

Note that GCC may optimize small variable-length arrays of a known value into plain arrays, so this warning may not get triggered for such arrays.

-Wvla-larger-than=‘PTRDIFF_MAX’ is enabled by default but is typically only effective when -ftree-vrp is active (default for -O2 and above).

See also -Walloca-larger-than=byte-size.

-Wno-vla-larger-than
Disable -Wvla-larger-than= warnings. The option is equivalent to -Wvla-larger-than=‘SIZE_MAX’ or larger.

-Wvolatile-register-var
Warn if a register variable is declared volatile. The volatile modifier does not inhibit all optimizations that may eliminate reads and/or writes to register variables. This warning is enabled by -Wall.

-Wdisabled-optimization
Warn if a requested optimization pass is disabled. This warning does not generally indicate that there is anything wrong with your code; it merely indicates that GCC’s optimizers are unable to handle the code effectively. Often, the problem is that your code is too big or too complex; GCC refuses to optimize programs when the optimization itself is likely to take inordinate amounts of time.

-Wpointer-sign (C and Objective-C only)
Warn for pointer argument passing or assignment with different signedness. This option is only supported for C and Objective-C. It is implied by -Wall and by -Wpedantic, which can be disabled with -Wno-pointer-sign.

-Wstack-protector
This option is only active when -fstack-protector is active. It warns about functions that are not protected against stack smashing.

-Woverlength-strings
Warn about string constants that are longer than the “minimum maximum” length specified in the C standard. Modern compilers generally allow string constants that are much longer than the standard’s minimum limit, but very portable programs should avoid using longer strings.

The limit applies after string constant concatenation, and does not count the trailing NUL. In C90, the limit was 509 characters; in C99, it was raised to 4095. C++98 does not specify a normative minimum maximum, so we do not diagnose overlength strings in C++.

This option is implied by -Wpedantic, and can be disabled with -Wno-overlength-strings.

-Wunsuffixed-float-constants (C and Objective-C only)
Issue a warning for any floating constant that does not have a suffix. When used together with -Wsystem-headers it warns about such constants in system header files. This can be useful when preparing code to use with the FLOAT_CONST_DECIMAL64 pragma from the decimal floating-point extension to C99.

-Wno-designated-init (C and Objective-C only)
Suppress warnings when a positional initializer is used to initialize a structure that has been marked with the designated_init attribute.

-Whsa
Issue a warning when HSAIL cannot be emitted for the compiled function or OpenMP construct.


```