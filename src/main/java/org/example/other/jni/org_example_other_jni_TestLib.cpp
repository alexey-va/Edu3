#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "org_example_other_jni_TestLib.h"   // Generated
#include <unistd.h>

// Implementation of the native method sayHello()
JNIEXPORT void JNICALL Java_org_example_other_jni_TestLib_sayHello(JNIEnv *env, jobject thisObj) {
   printf("Hello World from C++!\n");
}

JNIEXPORT jlong JNICALL Java_org_example_other_jni_TestLib_getPid(JNIEnv *env, jobject thisObj){
    return (jlong)getpid();
}