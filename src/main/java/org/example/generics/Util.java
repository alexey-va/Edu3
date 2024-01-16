package org.example.generics;


import org.example.geometry.Line;
import org.example.geometry.Point;
import org.example.geometry.Point3D;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    public static void shiftX(Line<Point> line, double shift){
        Point start = line.getStart();
        start.setX(start.getX()+shift);
        line.setStart(start);
    }

    public static OptionalDouble max(Collection<MyOptional<? extends Number>> optionals){
        return optionals.stream()
                .filter(MyOptional::isPresent)
                .map(MyOptional::getOrThrow)
                .mapToDouble(Number::doubleValue)
                .max();
    }

    public static void putPoint(Holder<? super Point3D> holder){
        Random random = new Random();
        holder.setValue(new Point3D(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    public static void putNumbers(List<? super Integer> list, int fromInclusive, int toIncusive){
        IntStream.range(fromInclusive, toIncusive+1).forEach(list::add);
    }

    public static <P extends Collection<T>, T> P collect(Collection<T> values,
                                                         Supplier<P> collectionSupplier,
                                                         BiConsumer<P, T> inserter){
        P collection = collectionSupplier.get();
        values.forEach(v -> inserter.accept(collection, v));
        return collection;
    }

    public static <T> T reduce(Collection<T> collection, T identity, BiFunction<T, T, T> reducer){
        for(T t : collection){
            identity = reducer.apply(identity, t);
        }
        return identity;
    }

    public static <T> List<T> trim(Collection<T> collection, Predicate<T> predicate){
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, P> List<P> applyToAll(Collection<T> collection, Function<T, P> function){
        return collection.stream().map(function).collect(Collectors.toList());
    }

}
