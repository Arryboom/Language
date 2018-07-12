package com.jnitest;
public class JNIDemo {
    public native void sayHello();
    public static void main(String[] args){
        System.loadLibrary("hellojni");
        JNIDemo jniDemo = new JNIDemo();
        jniDemo.sayHello();
        jniDemo.sayHello();
        jniDemo.sayHello();
    }
}