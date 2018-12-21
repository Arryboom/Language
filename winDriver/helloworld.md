```
#include<ntddk.h>

NTSTATUS DriverUnload(PDRIVER_OBJECT undriver){
	DbgPrint("UnloadingDriver!");
	return STATUS_SUCCESS;
}


NTSTATUS DriverEntry(PDRIVER_OBJECT driver, PUNICODE_STRING regpath)
{
	DbgPrint("driverloading");
	DbgPrint("%ws",regpath->Buffer);
	driver->DriverUnload = DriverUnload;
	return STATUS_SUCCESS;
}
```

tools
>http://www.osronline.com/section.cfm?section=27

内联汇编ASMwith2013
https://blog.csdn.net/asaqlp/article/details/51208547


```
#include<ntddk.h>
#define DEVICE_NAME L"\\DEVICE\\GlobalFinderDEV"
#define SYM_LINK_NAME L"\\??\\GlobalFinder"

PDEVICE_OBJECT pdd;

NTSTATUS DriverUnload(PDRIVER_OBJECT undriver){
	DbgPrint("UnloadingDriver!");
	UNICODE_STRING symlinkname = RTL_CONSTANT_STRING(SYM_LINK_NAME);
	IoDeleteSymbolicLink(&symlinkname);
/*	UNICODE_STRING Devicename;
	RtlInitUnicodeString(&Devicename, DEVICE_NAME);*/
	if (pdd == undriver->DeviceObject){
		DbgPrint("same with object!!!");
	}
	else{
		DbgPrint("not the same!!!");
	}
	IoDeleteDevice(undriver->DeviceObject);
	return STATUS_SUCCESS;
}
NTSTATUS Findread(PDEVICE_OBJECT device, PIRP pirp){

	NTSTATUS status=STATUS_SUCCESS;
	PIO_STACK_LOCATION stack = IoGetCurrentIrpStackLocation(pirp);
	ULONG readlength = stack->Parameters.Read.Length;
	pirp->IoStatus.Status = STATUS_SUCCESS;
	pirp->IoStatus.Information = readlength;
	memset(pirp->AssociatedIrp.SystemBuffer, 0xee, readlength);
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	DbgPrint("Read Complete.");
	return status;
}
NTSTATUS Findcreate(PDEVICE_OBJECT device, PIRP pirp){
	pirp->IoStatus.Status = STATUS_SUCCESS;
	pirp->IoStatus.Information = 0;
	DbgPrint("Findcreate over!");
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	return STATUS_SUCCESS;
}
NTSTATUS Findclose(PDEVICE_OBJECT device, PIRP pirp){
pirp->IoStatus.Status = STATUS_SUCCESS;
pirp->IoStatus.Information = 0;
DbgPrint("Findclose over!");
IoCompleteRequest(pirp, IO_NO_INCREMENT);
return STATUS_SUCCESS;
}
NTSTATUS DriverEntry(PDRIVER_OBJECT driver, PUNICODE_STRING regpath)
{
	driver->DriverUnload = DriverUnload;
	UNICODE_STRING devicename;
	RtlInitUnicodeString(&devicename, DEVICE_NAME);
	NTSTATUS createstatus=IoCreateDevice(driver, 0, &devicename, FILE_DEVICE_UNKNOWN,0,TRUE, &pdd);
	if (!NT_SUCCESS(createstatus)){
		DbgPrint("Create Device FAILED!!!");
		return STATUS_FILE_INVALID;
	}
	 UNICODE_STRING symlinkname = RTL_CONSTANT_STRING(SYM_LINK_NAME);
	 NTSTATUS symcratestatus = IoCreateSymbolicLink(&symlinkname, &devicename);
	 if (!NT_SUCCESS(symcratestatus)){
		 DbgPrint("Sym Link create failed!");
		 IoDeleteDevice(pdd);
	 }
	DbgPrint("driver loading,try to Find XMASH in ring0");
	DbgPrint("%ws", regpath->Buffer);
	pdd->Flags |=DO_BUFFERED_IO;
	driver->MajorFunction[IRP_MJ_CREATE] = Findcreate;
	driver->MajorFunction[IRP_MJ_READ] = Findread;
	driver->MajorFunction[IRP_MJ_CLOSE] = Findclose;
	return STATUS_SUCCESS;
}
```

DriverReader(Ring3)
```
// driverreader.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <windows.h>

int _tmain(int argc, _TCHAR* argv[])
{
	HANDLE hDevice = CreateFile(L"\\\\.\\GlobalFinder", GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (hDevice == INVALID_HANDLE_VALUE){
		printf("#ERROR cannot find the available device,%d", GetLastError());
		return 1;
	}
	else{
		UCHAR buffer[10];
		ULONG readlong;
		if (ReadFile(hDevice, &buffer, 10, &readlong, 0)){
			printf("read long is :%d\n", readlong);
			for (int i = 0; i < int(readlong); i++){
				printf("%02X", buffer[i]);
			}
			CloseHandle(hDevice);
		}
		else{
			printf("failed to read");
		}
	}
	


	return 0;
}

```
#IO CTL
>https://blog.csdn.net/liujiayu2/article/details/73250363

**R3:**

---
```
#ifndef  IOCTLS_H
#define	 IOCTLS_H
 
 
#ifndef CTL_CODE
#pragma  message("CTL_CODE undefined. Include winioctl.h or ntddk.h")
#endif
 
 
//缓冲内存模式IOCTL
#define IOCTL_TEST1  CTL_CODE(FILE_DEVICE_UNKNOWN,0X800,METHOD_BUFFERED,FILE_ANY_ACCESS)
 
//直接内存模式IOCTL
#define IOCTL_TEST2 CTL_CODE(FILE_DEVICE_UNKNOWN,0X801,METHOD_IN_DIRECT,FILE_ANY_ACCESS)
#define IOCTL_TEST3 CTL_CODE(FILE_DEVICE_UNKNOWN,0X802,METHOD_OUT_DIRECT,FILE_ANY_ACCESS)
 
//其他内存模式IOCTL
#define IOCTL_TEST4 CTL_CODE(FILE_DEVICE_UNKNOWN,0X803,METHOD_NEITHER,FILE_ANY_ACCESS)
 
 
#endif
```
```
#include <stdio.h>
#include <Windows.h>
#include <WinIoCtl.h>
#include "Ioctl.h"
 
 
 
int main (void)
{
	char linkname[]="\\\\.\\HelloDDK";
	HANDLE hDevice = CreateFileA(linkname,GENERIC_READ | GENERIC_WRITE,0,NULL,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,NULL);
	if (hDevice == INVALID_HANDLE_VALUE)
	{
		printf("Win32 error code: %d\n",GetLastError());
		return 1;
	}
 
 
	UCHAR InputBuffer[10]={0};
	UCHAR OutputBuffer[10]={0};
	DWORD dwOutput=0;
	memset(InputBuffer,0xBB,10);
 
	if (DeviceIoControl(hDevice,IOCTL_TEST1,InputBuffer,sizeof(InputBuffer),OutputBuffer,sizeof(OutputBuffer),&dwOutput,NULL))
	{
		printf("读入字节数%d\n",dwOutput);
		for (int i=0;i<(int)dwOutput;i++)
		{
			printf("%02X",OutputBuffer[i]);
		}
		printf("\n");
	}
	getchar();
	getchar();
	
	if (DeviceIoControl(hDevice,IOCTL_TEST2,InputBuffer,sizeof(InputBuffer),OutputBuffer,sizeof(OutputBuffer),&dwOutput,NULL))
	{
		printf("读入字节数%d\n",dwOutput);
		for (int i=0;i<(int)dwOutput;i++)
		{
			printf("%02X",OutputBuffer[i]);
		}
		printf("\n");
	}
	getchar();
	getchar();
 
	if (DeviceIoControl(hDevice,IOCTL_TEST4,InputBuffer,sizeof(InputBuffer),OutputBuffer,sizeof(OutputBuffer),&dwOutput,NULL))
	{
		printf("读入字节数%d\n",dwOutput);
		for (int i=0;i<(int)dwOutput;i++)
		{
			printf("%02X",OutputBuffer[i]);
		}
		printf("\n");
	}
	getchar();
	getchar();
 
 
	CloseHandle(hDevice);
 
	getchar();
	getchar();
	return 0;
}
```

**R0 SYS:**

---
```
#pragma  message("哈哈哈哈哈哈哈哈哈哈哈哈哈哈11111111112222")
#ifndef  IOCTLS_H
#define IOCTLS_H
 
 
#ifndef CTL_CODE
#pragma  message("CTL_CODE undefined. Include winioctl.h or ntddk.h")
#endif
 
 
//缓冲内存模式IOCTL
#define IOCTL_TEST1  CTL_CODE(FILE_DEVICE_UNKNOWN,0X800,METHOD_BUFFERED,FILE_ANY_ACCESS)
 
//直接内存模式IOCTL
#define IOCTL_TEST2 CTL_CODE(FILE_DEVICE_UNKNOWN,0X801,METHOD_IN_DIRECT,FILE_ANY_ACCESS)
#define IOCTL_TEST3 CTL_CODE(FILE_DEVICE_UNKNOWN,0X802,METHOD_OUT_DIRECT,FILE_ANY_ACCESS)
 
//其他内存模式IOCTL
#define IOCTL_TEST4 CTL_CODE(FILE_DEVICE_UNKNOWN,0X803,METHOD_NEITHER,FILE_ANY_ACCESS)
 
 
#endif
```

```
#pragma once
#include <ntddk.h>
#define CountArray(Array)  (	sizeof(Array)	/	sizeof(Array[0])	)
 
#define MAX_FILE_LENGTH 1024
 
typedef struct _DEVICE_EXTENSION
{
	PDEVICE_OBJECT pDevice;										//设备对象
	UNICODE_STRING ustrDeviceName;					//设备名称
	UNICODE_STRING ustrSymLinkName;					//符号名称
}DEVICE_EXTENSION,*PDEVICE_EXTENSION;
 
 
 
#ifdef __cplusplus
extern "C" NTSTATUS DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING  RegistryPath);
#endif
 
void HelloUnload(IN PDRIVER_OBJECT DriverObject);														//卸载函数
NTSTATUS CreateDevice(PDRIVER_OBJECT PDevObj);													//创建设备
NTSTATUS HelloDDKDispatchRoutine(IN PDEVICE_OBJECT pDevObj,IN PIRP pIrp);	//派遣函数
NTSTATUS HelloDDKControl(IN PDEVICE_OBJECT pDevObj,IN PIRP pIrp);					//IRP_MJ_DIRECTORY_CONTROL
```
```
#include "hello.h"
#include "Ioctl.h"
 
NTSTATUS DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING  RegistryPath)
{
		DbgPrint("Hello from!\n");
		DriverObject->DriverUnload = HelloUnload;
		for (int i=0;i<IRP_MJ_MAXIMUM_FUNCTION;i++)
		{
			DriverObject->MajorFunction[i]=HelloDDKDispatchRoutine;
		}
		DriverObject->MajorFunction[IRP_MJ_DEVICE_CONTROL]=HelloDDKControl;
 
 
 
#if DBG
		_asm int 3
#endif
		//创建设备
		CreateDevice(DriverObject);
 
		return STATUS_SUCCESS;
}
 
 
 
 
//卸载函数
void HelloUnload(IN PDRIVER_OBJECT DriverObject)
{
		DbgPrint("Goodbye from!\n");
		PDEVICE_OBJECT pNextObj=NULL;
		pNextObj=DriverObject->DeviceObject;
 
		while (pNextObj)
		{
			PDEVICE_EXTENSION pDevExt=(PDEVICE_EXTENSION)pNextObj->DeviceExtension;
			//删除符号连接
			IoDeleteSymbolicLink(&pDevExt->ustrSymLinkName);
			//删除设备
			IoDeleteDevice(pDevExt->pDevice);
			pNextObj=pNextObj->NextDevice;
		}
}
 
NTSTATUS HelloDDKControl(IN PDEVICE_OBJECT pDevObj,IN PIRP pIrp)
{
#if DBG
	_asm int 3
#endif
 
	NTSTATUS status=STATUS_SUCCESS;
	//获取当前堆栈
	PIO_STACK_LOCATION stack=IoGetCurrentIrpStackLocation(pIrp);
	//获取输入缓冲区大小
	ULONG cbin=stack->Parameters.DeviceIoControl.InputBufferLength;
	//得到输出缓冲区大小
	ULONG cbout=stack->Parameters.DeviceIoControl.OutputBufferLength;
	//得到IOCTL控制码
	ULONG code=stack->Parameters.DeviceIoControl.IoControlCode;
	ULONG info=0;
 
	switch (code)
	{
	case IOCTL_TEST1:	//缓冲区方式IOCTL
		{
			UCHAR* InputBuffer=(UCHAR*)pIrp->AssociatedIrp.SystemBuffer;
			for (ULONG i=0;i<cbin;i++)
			{
				DbgPrint("%X\n",InputBuffer[i]);
			}
 
			//操作输出缓冲区
			UCHAR* OutputBuffer=(UCHAR*)pIrp->AssociatedIrp.SystemBuffer;
			memset(OutputBuffer,0XAA,cbout);
			info=cbout;
		}
		break;
	case IOCTL_TEST2://直接内存模式IOCTL 读
	case IOCTL_TEST3://直接内存模式IOCTL 写
		{
			//显示输入缓冲区内容
			UCHAR* InputBuffer=(UCHAR*)pIrp->AssociatedIrp.SystemBuffer;
			for (ULONG i=0;i<cbin;i++)
			{
				DbgPrint("%X\n",InputBuffer[i]);
			}
 
			//pIrp->MdlAddress为DeviceIoControl输出缓冲区地址相同
			DbgPrint("user address: 0X%08X\n",MmGetMdlVirtualAddress(pIrp->MdlAddress));
			UCHAR* OutputBuffer=(UCHAR*)MmGetSystemAddressForMdlSafe(pIrp->MdlAddress,NormalPagePriority);
			//InputBuffer被影射到内核模式下的的内存地址，必定在0X80000000-0XFFFFFFFF之间
			memset(OutputBuffer,0XAA,cbout);
			info=cbout;
		}
		break;
	case IOCTL_TEST4:	//其他内存模式IOCTL
		{
			//显示输入缓冲区数据
			UCHAR* UserInputBuffer=(UCHAR*)stack->Parameters.DeviceIoControl.Type3InputBuffer;
			DbgPrint("userInputBuffer:0X%0X\n",UserInputBuffer);
			//得到用户模式地址
			PVOID UserOutputBuffer=pIrp->UserBuffer;
			DbgPrint("UserOutputBuffer:0X%0X\n",UserOutputBuffer);
 
			__try
			{
				//判断指针是否可读
				ProbeForRead(UserInputBuffer,cbin,4);
 
				//显示输入缓冲区内容
				for (ULONG i=0;i<cbin;i++)
				{
					DbgPrint("%X\n",UserInputBuffer[i]);
				}
 
				//判断指针是否可写
				ProbeForWrite(UserOutputBuffer,cbout,4);
 
				//操作输出缓冲区
				memset(UserOutputBuffer,0XAA,cbout);
				info=cbout;
				DbgPrint("OK\n");
			}
			__except(EXCEPTION_EXECUTE_HANDLER)
			{
				DbgPrint("打我PG我不乖\n");
				status=STATUS_UNSUCCESSFUL;
			}
			info=cbout;
		}
		break;
	default:
		status=STATUS_INVALID_VARIANT;
	}
 
	//设置IRP的完成状态
	pIrp->IoStatus.Status=status;
	pIrp->IoStatus.Information=info;
	IoCompleteRequest(pIrp,IO_NO_INCREMENT);
	return status;
}
 
//创建设备
NTSTATUS CreateDevice(PDRIVER_OBJECT pDriver_Object)
{
	//定义变量
	NTSTATUS status=STATUS_SUCCESS;
	PDEVICE_OBJECT pDevObje=NULL;
	PDEVICE_EXTENSION pDevExt=NULL;
 
	//初始化字符串
	UNICODE_STRING devname;
	UNICODE_STRING symLinkName;
	RtlInitUnicodeString(&devname,L"\\device\\hello");
	RtlInitUnicodeString(&symLinkName,L"\\??\\HelloDDK");
 
	//创建设备
	if (IoCreateDevice(pDriver_Object,sizeof(PDEVICE_EXTENSION),&devname,FILE_DEVICE_UNKNOWN,NULL,TRUE,&pDevObje)!=STATUS_SUCCESS )
	{
		DbgPrint("创建设备失败\n");
		return status;
	}
	pDevObje->Flags |= DO_DIRECT_IO;
	pDevExt=(PDEVICE_EXTENSION)pDevObje->DeviceExtension;
	pDevExt->pDevice=pDevObje;
	pDevExt->ustrDeviceName=devname;
	pDevExt->ustrSymLinkName=symLinkName;
 
	//创建符号连接
	if (IoCreateSymbolicLink(&symLinkName,&devname)!=STATUS_SUCCESS )
	{
		DbgPrint("创建符号连接失败\n");
		IoDeleteDevice(pDevObje);
		return status;
	}
	return STATUS_SUCCESS;
}
 
//派遣函数
NTSTATUS HelloDDKDispatchRoutine(IN PDEVICE_OBJECT pDevObj,IN PIRP pIrP)
{
//#if DBG
//	_asm int 3
//#endif
 
	PIO_STACK_LOCATION stack = IoGetCurrentIrpStackLocation(pIrP);
	//建立一个字符串数组与IRP类型对应起来
	static char* irpname[] = 
	{
		"IRP_MJ_CREATE",
		"IRP_MJ_CREATE_NAMED_PIPE",
		"IRP_MJ_CLOSE",
		"IRP_MJ_READ",
		"IRP_MJ_WRITE",
		"IRP_MJ_QUERY_INFORMATION",
		"IRP_MJ_SET_INFORMATION",
		"IRP_MJ_QUERY_EA",
		"IRP_MJ_SET_EA",
		"IRP_MJ_FLUSH_BUFFERS",
		"IRP_MJ_QUERY_VOLUME_INFORMATION",
		"IRP_MJ_SET_VOLUME_INFORMATION",
		"IRP_MJ_DIRECTORY_CONTROL",
		"IRP_MJ_FILE_SYSTEM_CONTROL",
		"IRP_MJ_DEVICE_CONTROL",
		"IRP_MJ_INTERNAL_DEVICE_CONTROL",
		"IRP_MJ_SHUTDOWN",
		"IRP_MJ_LOCK_CONTROL",
		"IRP_MJ_CLEANUP",
		"IRP_MJ_CREATE_MAILSLOT",
		"IRP_MJ_QUERY_SECURITY",
		"IRP_MJ_SET_SECURITY",
		"IRP_MJ_POWER",
		"IRP_MJ_SYSTEM_CONTROL",
		"IRP_MJ_DEVICE_CHANGE",
		"IRP_MJ_QUERY_QUOTA",
		"IRP_MJ_SET_QUOTA",
		"IRP_MJ_PNP",
	};
 
	UCHAR type = stack->MajorFunction;
 
	if (type >= CountArray(irpname))
		KdPrint(("无效的IRP类型 %X\n", type));
	else
		KdPrint(("%s\n", irpname[type]));
 
 
 
 
	pIrP->IoStatus.Status=STATUS_SUCCESS;					//设置完成状态
	pIrP->IoStatus.Information=0;										//设置操作字节为0
	IoCompleteRequest(pIrP,IO_NO_INCREMENT);			//结束IRP派遣函数，第二个参数表示不增加优先级
	return STATUS_SUCCESS;
}
```







#my IOCTL DEMO
###driver
```
#include<ntddk.h>
#define DEVICE_NAME L"\\DEVICE\\GlobalFinderDEV"
#define SYM_LINK_NAME L"\\??\\GlobalFinder"
#define IOCTL_TEST CTL_CODE(FILE_DEVICE_UNKNOWN,0x815,METHOD_BUFFERED,FILE_ANY_ACCESS)

PDEVICE_OBJECT pdd;

NTSTATUS DriverUnload(PDRIVER_OBJECT undriver){
	DbgPrint("UnloadingDriver!");
	UNICODE_STRING symlinkname = RTL_CONSTANT_STRING(SYM_LINK_NAME);
	IoDeleteSymbolicLink(&symlinkname);
/*	UNICODE_STRING Devicename;
	RtlInitUnicodeString(&Devicename, DEVICE_NAME);*/
	if (pdd == undriver->DeviceObject){
		DbgPrint("same with object!!!");
	}
	else{
		DbgPrint("not the same!!!");
	}
	IoDeleteDevice(undriver->DeviceObject);
	return STATUS_SUCCESS;
}
NTSTATUS Findread(PDEVICE_OBJECT device, PIRP pirp){

	NTSTATUS status=STATUS_SUCCESS;
	PIO_STACK_LOCATION stack = IoGetCurrentIrpStackLocation(pirp);
	ULONG readlength = stack->Parameters.Read.Length;
	pirp->IoStatus.Status = STATUS_SUCCESS;
	pirp->IoStatus.Information = readlength;
	memset(pirp->AssociatedIrp.SystemBuffer, 0xee, readlength);
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	DbgPrint("Read Complete.");
	return status;
}
NTSTATUS Findcreate(PDEVICE_OBJECT device, PIRP pirp){
	pirp->IoStatus.Status = STATUS_SUCCESS;
	pirp->IoStatus.Information = 0;
	DbgPrint("Findcreate over!");
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	return STATUS_SUCCESS;
}
NTSTATUS Findclose(PDEVICE_OBJECT device, PIRP pirp){
pirp->IoStatus.Status = STATUS_SUCCESS;
pirp->IoStatus.Information = 0;
DbgPrint("Findclose over!");
IoCompleteRequest(pirp, IO_NO_INCREMENT);
return STATUS_SUCCESS;
}
NTSTATUS Findcommen(PDEVICE_OBJECT device, PIRP pirp){
	pirp->IoStatus.Status = STATUS_SUCCESS;
	pirp->IoStatus.Information = 0;
	DbgPrint("Findcommen req!");
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	return STATUS_SUCCESS;
}
NTSTATUS Findioctl(PDEVICE_OBJECT device, PIRP pirp){
	int info;
	NTSTATUS status = STATUS_SUCCESS;
	PIO_STACK_LOCATION stack = IoGetCurrentIrpStackLocation(pirp);
	ULONG inlength = stack->Parameters.DeviceIoControl.InputBufferLength;
	ULONG outlength = stack->Parameters.DeviceIoControl.OutputBufferLength;
	ULONG iocode = stack->Parameters.DeviceIoControl.IoControlCode;
	switch(iocode){
	case IOCTL_TEST:{
		DbgPrint("Into 0x815");
		PUCHAR inbuffer = pirp->AssociatedIrp.SystemBuffer;
		for (ULONG i=0;i<inlength; i++){
			DbgPrint("Received: %x", inbuffer[i]);
		}
			PUCHAR outbuffer = pirp->AssociatedIrp.SystemBuffer;
			memset(outbuffer, 0xcc, 5);
			info = 5;
			break;
		}
		default:
			DbgPrint("I don't know what into");
			status=STATUS_UNSUCCESSFUL;
			break;
	}

	pirp->IoStatus.Status = status;
	pirp->IoStatus.Information = info;
	DbgPrint("Findcommen req!");
	IoCompleteRequest(pirp, IO_NO_INCREMENT);
	return status;
}

NTSTATUS DriverEntry(PDRIVER_OBJECT driver, PUNICODE_STRING regpath)
{
	driver->DriverUnload = DriverUnload;
	UNICODE_STRING devicename;
	RtlInitUnicodeString(&devicename, DEVICE_NAME);
	NTSTATUS createstatus=IoCreateDevice(driver, 0, &devicename, FILE_DEVICE_UNKNOWN,0,TRUE, &pdd);
	if (!NT_SUCCESS(createstatus)){
		DbgPrint("Create Device FAILED!!!");
		return STATUS_FILE_INVALID;
	}
	 UNICODE_STRING symlinkname = RTL_CONSTANT_STRING(SYM_LINK_NAME);
	 NTSTATUS symcratestatus = IoCreateSymbolicLink(&symlinkname, &devicename);
	 if (!NT_SUCCESS(symcratestatus)){
		 DbgPrint("Sym Link create failed!");
		 IoDeleteDevice(pdd);
	 }
	DbgPrint("driver loading,try to Find XMASH in ring0");
	DbgPrint("%ws", regpath->Buffer);
	pdd->Flags |=DO_BUFFERED_IO;
	for (int i = 0; i < IRP_MJ_MAXIMUM_FUNCTION; i++){
		driver->MajorFunction[i] = Findcommen;
	}
	driver->MajorFunction[IRP_MJ_CREATE] = Findcreate;
	driver->MajorFunction[IRP_MJ_READ] = Findread;
	driver->MajorFunction[IRP_MJ_CLOSE] = Findclose;
	driver->MajorFunction[IRP_MJ_DEVICE_CONTROL] = Findioctl;

	return STATUS_SUCCESS;
}
```
###reader
```
// driverreader.cpp : Defines the entry point for the console application.
//


#include "stdafx.h"
#include <windows.h>
#define IOCT_TEST CTL_CODE(FILE_DEVICE_UNKNOWN,0x815,METHOD_BUFFERED,FILE_ANY_ACCESS)

int _tmain(int argc, _TCHAR* argv[])
{
	HANDLE hDevice = CreateFile(L"\\\\.\\GlobalFinder", GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (hDevice == INVALID_HANDLE_VALUE){
		printf("#ERROR cannot find the available device,%d", GetLastError());
		return 1;
	}
	else{/*
		UCHAR buffer[10];
		ULONG readlong;
		if (ReadFile(hDevice, &buffer, 10, &readlong, 0)){
			printf("read long is :%d\n", readlong);
			for (int i = 0; i < int(readlong); i++){
				printf("%02X", buffer[i]);
			}
			CloseHandle(hDevice);
		}
		else{
			printf("failed to read");
		}
		*/
		DWORD dwOutput;
		UCHAR inputbuffer[10];
		UCHAR outputbuffer[10];
		memset(outputbuffer, 0x00, 10);
		memset(inputbuffer, 0xaa, 10);
		BOOL bret;
		bret = DeviceIoControl(hDevice, IOCT_TEST, inputbuffer, 10, outputbuffer, 10, &dwOutput, NULL);
		if (bret){
			printf("output buffer length:%d bytes\n", dwOutput);
			for (int i = 0; i < (int)dwOutput; i++){
				printf("%02X", outputbuffer[i]);
			}
			printf("\n");
		}
	}
	


	return 0;
}

```

#配置环境参考
>https://blog.csdn.net/charlessimonyi/article/details/50904956
>https://blog.csdn.net/blog_index/article/details/18084463
>https://blog.csdn.net/laowu_csdn/article/details/50672869