package org.example.entrypoints;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.living.Bird;
import org.example.living.Meowable;
import org.example.geometry.*;

import static java.lang.Math.*;

import java.lang.Double;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        var res = SumResult.ofNullable(null);

        //System.out.println(res.getOrDefault(Main::getInteger));


    }




    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class SumResult<T>{
        private T result;

        public static <T> SumResult<T> ofNullable(T value){
            return new SumResult<>(value);
        }

        public static <T> SumResult<T> of(T value){
            if(value == null) throw new IllegalArgumentException();
            return new SumResult<>(value);
        }

        public T getOrDefault(T defaultValue){
            return result == null ? defaultValue : result;
        }

        @SneakyThrows
        public T getOrDefault(Supplier<T> supplier){
            if(result == null) return supplier.get();
            return result;
        }
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