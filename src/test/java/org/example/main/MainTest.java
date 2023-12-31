package org.example.main;

import org.example.geometry.*;
import org.example.living.*;
import org.example.units.Fraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void abc(){
        List<String> list = new ArrayList<>();
        list.add("1");
        List<?> newlist = list;
        newlist.add(null);
        List<Object> newlist2 = new ArrayList<>();
        newlist2.add("1");

        List<? super Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(null);

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte)1);
        byteBuffer.put((byte)2);
        byteBuffer.put((byte)3);
        byteBuffer.putInt(2_000_000_000);

        for(int i = 0; i < 10; i++){
            System.out.println(byteBuffer.get(i));
        }

        //print(list);
    }

    private void print(List<?> list){
        for (Object o : list){
            System.out.println(o);
        }
    }

    @Test
    void testGetUnion() {
        Chainable chainable1 = new Square(1,1,3);
        Chainable chainable2 = new Triangle(new Point(0, 0), new Point(3, 0), new Point(0, 4));

        BrokenLine brokenLine = Main.getUnion(Arrays.asList(chainable1, chainable2));
        Assertions.assertEquals(7, brokenLine.getPoints().size());
    }

    @Test
    void printLengths() {
        Measurable measurable = new Square(1,1,3).toLine();
        Measurable measurable1 = new Line(new Point(0, 0), new Point(3, 0));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        PrintStream initialOut = System.out;
        System.setOut(printStream);

        Main.printLengths(Arrays.asList(measurable, measurable1));

        System.setOut(initialOut);

        Assertions.assertEquals(outContent.toString().split("\n")[0].trim(), "12.0");
        Assertions.assertEquals(outContent.toString().split("\n")[1].trim(), "3.0");

    }

    @Test
    void batchMeow() {
        Meowable meowable = new Cat("cat1");
        Meowable meowable2 = new Cat("cat2");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        PrintStream initialOut = System.out;
        System.setOut(printStream);

        Main.batchMeow(Arrays.asList(meowable2, meowable));

        System.setOut(initialOut);

        //System.out.println(outContent.toString().trim());

        Assertions.assertEquals(outContent.toString().split("\n")[0].trim(), "cat2: meow!");
        Assertions.assertEquals(outContent.toString().split("\n")[1].trim(), "cat1: meow!");
    }

    @Test
    void commonArea() {

        Figure figure = new Squar(new Point(0, 0), 3);
        Figure figure1 = new Triangle(new Point(0, 0), new Point(3, 0), new Point(0, 4));

        Assertions.assertEquals(15, Main.commonArea(Arrays.asList(figure, figure1)));

    }

    @Test
    void tweet() {

        Bird bird1 = new Sparrow();
        Bird bird2 = new Cuckoo();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outContent);
        PrintStream initialOut = System.out;
        System.setOut(printStream);

        Main.tweet(Arrays.asList(bird1, bird2));

        System.setOut(initialOut);

        //System.out.println(outContent.toString().trim());

        Assertions.assertEquals(outContent.toString().split("\n")[0].trim(), "чырык");
        Assertions.assertTrue(outContent.toString().split("\n")[1].trim().contains("ку-ку"));

    }

    @Test
    void addNumbers() {

        double res = Main.addNumbers(Arrays.asList(7,
                new Fraction(11,3),
                3.21,
                new BigInteger("12345678912345678912")));

        Assertions.assertEquals(1.234567891234568E19, res);

    }
}