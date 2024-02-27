package org.example.entrypoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.log4j.Log4j2;
import org.example.Logged;
import org.example.geometry.*;
import org.example.living.*;
import org.example.units.Fraction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Logged
@Log4j2
class MainTest {

    private static List<Point> points;

    @BeforeAll
    public static void setup() throws Exception{
        points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(
                    ThreadLocalRandom.current().nextInt(-100, 100),
                    ThreadLocalRandom.current().nextInt(-100, 1)
            ));
        }

        // create test sample and write it to file
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append(LoremIpsum.getInstance().getFirstName().toLowerCase());
            if (ThreadLocalRandom.current().nextBoolean()) {
                builder.append(":")
                        .append(ThreadLocalRandom.current().nextInt(0, 20));
            }
            builder.append("\n");
        }

        Path path = Paths.get("test_file.txt");
        Files.writeString(path, builder.toString(),
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    @Test
    void test1() throws Exception {
        BrokenLine brokenLine = points.stream()
                .distinct()
                .sorted(Comparator.comparingDouble(Point::getX))
                .peek(p -> p.setY(Math.abs(p.getY())))
                .collect(BrokenLine::new, BrokenLine::add, BrokenLine::addAll);
        System.out.println(brokenLine);
    }

    @Test
    void test2() throws Exception {
        Path path = Paths.get("test_file.txt");
        // read file to a map
        try (var stream = Files.lines(path)) {
            record NameData(String name, int id) {}
            var map = stream
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                    .map(s -> s.split(":"))
                    .filter(array -> array.length >= 2)
                    .filter(array -> array[1].matches("^-?\\d+$"))
                    .map(array -> new NameData(array[0], Integer.parseInt(array[1])))
                    .collect(Collectors.groupingBy(
                            NameData::id,
                            Collectors.mapping(NameData::name, Collectors.toList())
                    ));

            // Print json with indentation
            System.out.println(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    void fizzBuzz() {
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
        Chainable chainable1 = new Square(1, 1, 3);
        Chainable chainable2 = new Triangle(new Point(0, 0), new Point(3, 0), new Point(0, 4));

        BrokenLine brokenLine = Main.getUnion(Arrays.asList(chainable1, chainable2));
        Assertions.assertEquals(7, brokenLine.getPoints().size());
    }

    @Test
    void printLengths() {
        Measurable measurable = new Square(1, 1, 3).toLine();
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
                new Fraction(11, 3),
                3.21,
                new BigInteger("12345678912345678912")));

        Assertions.assertEquals(1.234567891234568E19, res);

    }
}