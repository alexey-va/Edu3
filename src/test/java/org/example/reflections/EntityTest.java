package org.example.reflections;

import org.example.reflections.annotations.ToString;
import org.example.reflections.annotations.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EntityTest {


    static class Test1 extends Entity{
        String s = "asd1";
        private String s2 = "asd2";
        static String s3 = "asd3";
    }

    static class Test2 extends Test1{
        String s4 = "asd4";
        private String s5 = "asd5";
        static String s6 = "asd6";
    }

    static class Test3 extends Test2{
        @ToString(Type.NO)
        String s7 = "asd7";
    }

    @ToString(Type.NO)
    static class Test4 extends Test3{
        String s8 = "asd8";
    }

    @DisplayName("Entity toString has its non-static fields")
    @Test
    void testEntityToStringHasItsNonStaticFields_includingPrivateAndSuperclass(){
        Test2 test2 = new Test2();

        // Expected output:
        // Test2{s=asd1, s2=asd2, s4=asd4, s5=asd5}

        Assertions.assertTrue(test2.toString().contains("s=asd1"));
        Assertions.assertTrue(test2.toString().contains("s2=asd2"));
        Assertions.assertTrue(test2.toString().contains("s4=asd4"));
        Assertions.assertTrue(test2.toString().contains("s5=asd5"));
    }


    @DisplayName("Entity toString does not have its static fields")
    @Test
    void testEntityToStringDoesNotHaveItsStaticFields(){
        Test2 test2 = new Test2();

        // Expected output:
        // Test2{s=asd1, s2=asd2, s4=asd4, s5=asd5}

        Assertions.assertFalse(test2.toString().contains("s3=asd3"));
        Assertions.assertFalse(test2.toString().contains("s6=asd6"));
    }

    @DisplayName("Entity toString has its non-static fields in superclass")
    @Test
    void testEntityToStringHasItsNonStaticFieldsInSuperClass(){
        Test2 test2 = new Test2();

        // Expected output:
        // Test2{s=asd1, s2=asd2, s4=asd4, s5=asd5}

        Assertions.assertTrue(test2.toString().contains("s=asd1"));
        Assertions.assertTrue(test2.toString().contains("s2=asd2"));
    }

    @DisplayName("Entity toString does not have its static fields in superclass")
    @Test
    void testEntityToStringDoesNotHaveItsStaticFieldsInSuperClass(){
        Test2 test2 = new Test2();

        // Expected output:
        // Test2{s=asd1, s2=asd2, s4=asd4, s5=asd5}

        Assertions.assertFalse(test2.toString().contains("s3=asd3"));
    }

    @DisplayName("Entity toString has its private fields")
    @Test
    void testEntityToStringHasItsPrivateFields(){
        Test2 test2 = new Test2();

        // Expected output:
        // Test2{s=asd1, s2=asd2, s4=asd4, s5=asd5}

        Assertions.assertTrue(test2.toString().contains("s2=asd2"));
        Assertions.assertTrue(test2.toString().contains("s5=asd5"));
    }

    @Test
    void testToStringAnnotation(){
        Test3 test3 = new Test3();

        Assertions.assertFalse(test3.toString().contains("s7=asd7"));
    }

    @Test
    void testToStringAnnotationOnEntireClass(){
        Test4 test4 = new Test4();

        Assertions.assertFalse(test4.toString().contains("s8=asd8"));
    }

}