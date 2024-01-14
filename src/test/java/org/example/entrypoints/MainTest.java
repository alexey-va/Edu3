package org.example.entrypoints;

import lombok.extern.log4j.Log4j2;
import org.example.Logged;
import org.example.geometry.*;
import org.example.living.*;
import org.example.units.Fraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.PhantomReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Logged
@Log4j2
class MainTest {

    @Test
    void myTest(){
        IntStream.range(-100, 1000)
                .filter(i -> i%3 == 0)
                .filter(i -> i%5 != 0)
                .filter(i -> digitSum(i) < 10)
                .forEach(System.out::println);
    }

    static int digitSum(int x){
        x = Math.abs(x);
        int res = 0;
        while (x > 0){
            res+=x%10;
            x/=10;
        }
        return res;
    }



    @Test
    void fizzBuzz(){
        var map = new LinkedHashMap<>(Map.of(
                3, "Fizz",
                5, "Buzz",
                6, "Bazz"));

        System.out.println(IntStream.range(0, 100)
                .mapToObj((i) -> map.entrySet().stream()
                                .filter((entry) -> i % entry.getKey() == 0)
                                .map(Map.Entry::getValue)
                                .collect(Collectors.joining())
                                .transform((s) -> s.isEmpty() ? String.valueOf(i) : s)
                ).collect(Collectors.joining(" ", "[", "]")));
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