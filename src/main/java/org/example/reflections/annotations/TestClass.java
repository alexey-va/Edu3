package org.example.reflections.annotations;

import org.example.reflections.PersonTests;

@Default(String.class)
@Validate({PersonTests.class})
@Two(first = "asd", second = 123)
@Cache("cache_1")
public class TestClass {


    @ToString(Type.NO)
    String field;

    @Invoke
    public void test(String s){

    }
}
