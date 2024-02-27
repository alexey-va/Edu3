package org.example.reflections;

import lombok.*;
import org.example.geometry.Line;
import org.example.geometry.Point;
import org.example.living.Person;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.example.reflections.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testFieldOf(){
        fieldsOf(Point.class).stream().map(Field::getName).forEach(System.out::println);
    }

    @Test
    void testLineConnector(){
        var l1 = new Line<>(new Point(1,1), new Point(2,2));
        var l2 = new Line<>(new Point(6,6), new Point(7,7));

        lineConnector(l1, l2);
        System.out.println(l1);
        System.out.println(l2);
    }

    @Test
    void testEntity(){
        B b = new B();
        System.out.println(b);
    }

    @Test
    void testValidation(){
        Person person = new Person("Bob", 300);
        System.out.println(validate(person, PersonTests.class));

        Person person2 = new Person("Bob", 150);
        System.out.println(validate(person2, PersonTests.class));

        Person person3 = new Person();
        person3.setHeight(150);
        System.out.println(validate(person3, PersonTests.class));
    }


    @Test
    void testWriter(){
        Test2 test2 = new Test2("asd", null, 123, 123.2);
        FileWriter fileWriter = new FileWriter(new File("test_writer.txt"));
        fileWriter.write(test2);
    }

    @Test
    void testReader(){
        FileReader fileReader = new FileReader(new File("test_writer.txt"));
        System.out.println(fileReader.read(Test2.class));
    }

    @Test
    void testCache(){
        Test1 test1 = new Test1();
        Test1 test11 = cache(test1);
        System.out.println("Make sure void methods are not cached");
        test11.print();
        test11.print();

        System.out.println();
        System.out.println("Call getString twice:");
        System.out.println(test11.getString());
        System.out.println(test11.getString());

        System.out.println();
        System.out.println("Mutated state");
        test11.setA("asd");
        System.out.println();
        System.out.println("Call getString again");
        System.out.println(test11.getString());

        System.out.println();
        System.out.println(test11.getString2(0));
        System.out.println(test11.getString2(0));
        System.out.println(test11.getString2(1));
        System.out.println(test11.getString2(1));
    }


    @NoArgsConstructor
    @Setter @Getter
    @ToString
    static class Test1{
        String a =null;
        int b = 123;

        void print(){
            System.out.println("Called print");
            System.out.println("> Executing[print]");
        }

        String getString(){
            System.out.println("> Executing[getString]");
            return "this is string";
        }

        String getString2(int a){
            System.out.println("> Executing[getString2("+a+")]");
            return "Strign2 "+a;
        }
    }

    @NoArgsConstructor
    @ToString
    @AllArgsConstructor
    static class Test2{
        String a =null;
        String b = "asdasd";
        int c = 123;
        double d = 123.31;
    }


}