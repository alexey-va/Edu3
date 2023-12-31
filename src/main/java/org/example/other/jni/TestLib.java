package org.example.other.jni;

public class TestLib {

    static {
        System.loadLibrary("native");
    }

    public native void sayHello();
    public native long getPid();
}
