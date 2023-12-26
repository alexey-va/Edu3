package org.example.main;

import lombok.extern.log4j.Log4j2;
import org.example.living.Bird;
import org.example.living.Meowable;
import org.example.geometry.*;
import static java.lang.Math.*;
import java.awt.Point.*;

import java.lang.Double;
import java.util.*;

@Log4j2
public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        if(args.length == 2){
            log.info(myPow(args[0], args[1]));
        }

        java.awt.Point p1 = new java.awt.Point(1, 1);
        Point p2 = new Point(2, 2);

        log.info(p1);
        log.info(p2);


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