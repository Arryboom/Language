#include "com_jnitest_JNIDemo.h"  

JNIEXPORT void JNICALL Java_com_jnitest_JNIDemo_sayHello (JNIEnv * env, jobject obj)
{
    printf("Hello Jni\n");  
    return;  
}