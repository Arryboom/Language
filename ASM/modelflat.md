# http://masm32.com/board/index.php?topic=4755.0
```

If in .code you need to do this:
Code: [Select]
	invoke	VirtualProtect, addr codeBuffer, 100, PAGE_EXECUTE_READWRITE, addr ...

```

```
include \masm32\include\masm32rt.inc

.code
destination	db "Surprise.."		; the MessageBox code will go here
start:
	mov edi, offset destination
	push edi
	mov esi, offset MyMB
	.Repeat
		movsb
	.Until esi>=MyMB_end
	pop edi
	int 3   ; for the debugger
	call edi
  exit

MyMB:
  mov esi, MessageBox
  push MB_OK
  push chr$("Copied:")
  push chr$("Hello World")
  push 0
  call esi			; MsgBox 0, "Hello World", "Copied:", MB_OK
  retn
MyMB_end:
end start

```

```
The linker commandline must contain /SECTION:.text,rwe

In case you are using RichMasm, insert this line under "end start", then hit F6:
OPT_DebugL   /SECTION:.text,RWE

Otherwise, use a batch file.

P.S.: Before & after:
```
```
destination                   53                         db 53                                ;  CHAR 'S'
00401001                      75                         db 75                                ;  CHAR 'u'
00401002                      72                         db 72                                ;  CHAR 'r'
00401003                      70                         db 70                                ;  CHAR 'p'
00401004                      72                         db 72                                ;  CHAR 'r'
00401005                      69                         db 69                                ;  CHAR 'i'
00401006                      73                         db 73                                ;  CHAR 's'
00401007                      65                         db 65                                ;  CHAR 'e'
00401008                      2E                         db 2E                                ;  CHAR '.'
00401009                      2E                         db 2E                                ;  CHAR '.'
<ModuleEntryPoint>        /$  BF 00104000                mov edi, destination                 ;  ASCII "Surprise..¿"
0040100F                  |.  57                         push edi
00401010                  |.  BE 28104000                mov esi, 00401028
00401015                  |>  A4                         movsb
00401016                  |.  81FE 3E104000              cmp esi, MessageBoxA                 ;  jmp to user32.MessageBoxA
0040101C                  |.^ 72 F7                      jb short 00401015
0040101E                  |.  5F                         pop edi
0040101F                  |.  FFD7                       call near edi
```
```
destination                 BE 40104000                  mov esi, MessageBoxA                 ; jmp to user32.MessageBoxA
00401005                    6A 00                        push 0
00401007                    68 00304000                  push offset ??0019                   ; ASCII "Copied:"
0040100C                    68 08304000                  push offset ??001A                   ; ASCII "Hello World"
00401011                    6A 00                        push 0
00401013                    FFD6                         call near esi
00401015                    C3                           retn
00401016                    81FE 3F104000                cmp esi, 0040103F
0040101C                  ^ 72 F7                        jb short 00401015
0040101E                    5F                           pop edi
0040101F                    CC                           int3
00401020                    FFD7                         call near edi                        ; NewMasm3.destination
00401022                    6A 00                        push 0
00401024                    E8 1D000000                  call ExitProcess                     ; jmp to kernel32.ExitProcess
00401029                    BE 40104000                  mov esi, MessageBoxA                 ; jmp to user32.MessageBoxA
0040102E                    6A 00                        push 0
00401030                    68 00304000                  push offset ??0019                   ; ASCII "Copied:"
00401035                    68 08304000                  push offset ??001A                   ; ASCII "Hello World"
0040103A                    6A 00                        push 0
0040103C                    FFD6                         call near esi
MessageBoxA                 C3                           retn
```
```
You might have a closer look at address 00401015 :biggrin:

An even weirder example is attached. Open it in Olly, hit F8 until VirtualProtect is passed, then continue with F7 (single step). From time to time, select a few lines and hit Ctrl A to force Olly to update the disassembly.
```