package org.example.other.jni;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestLibTest {

    @Test
    void sayHello() {
        TestLib testLib = new TestLib();
        testLib.sayHello();
    }

}