package org.example.entrypoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.example.living.Bird;
import org.example.living.Meowable;
import org.example.geometry.*;

import static java.lang.Math.*;

import java.lang.Double;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
public class Main {
    public static void main(String[] args) throws Exception {

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

    public static double myPow(String a, String b) {
        return pow(Double.parseDouble(a), Double.parseDouble(b));
    }


}