package org.example.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.example.living.Bird;
import org.example.living.Meowable;
import org.example.geometry.*;
import org.example.living.Person;
import org.example.other.jni.TestLib;

import static java.lang.Math.*;
import java.awt.Point.*;

import java.io.Serializable;
import java.lang.Double;
import java.util.*;

@Log4j2
public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        TestLib testLib = new TestLib();
        testLib.sayHello();
        System.out.println(testLib.getPid());
    }




    public static BrokenLine getUnion(Collection<Chainable> chainables) {
        return new BrokenLine(chainables.stream()
                .flatMap(c -> c.toLine().getPoints().stream())
                .toArray(Point[]::new)
        );
    }

    public static void printLengths(Collection<Measurable> measurables) {
        measurables.forEach(m -> System.out.println(m.length()));
    }

    public static void batchMeow(Collection<Meowable> meowables) {
        meowables.forEach(Meowable::meow);
    }

    public static double commonArea(Collection<Figure> figures) {
        return figures.stream()
                .mapToDouble(Figure::area)
                .sum();
    }

    public static void tweet(Collection<Bird> birds) {
        birds.forEach(b -> System.out.println(b.getMessage()));
    }

    public static double addNumbers(Collection<Number> numbers) {
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    public static double myPow(String a, String b){
        return pow(Double.parseDouble(a), Double.parseDouble(b));
    }


}