package org.example.entrypoints;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.living.Bird;
import org.example.living.Cat;
import org.example.living.Meowable;
import org.example.geometry.*;
import org.example.other.events.base.EventManager;
import org.example.other.network.webserver.WebServer;
import org.example.other.test.*;

import static java.lang.Math.*;

import java.lang.Double;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException, ExecutionException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        WebServer webServer = new WebServer(9090);
        RequestManager.reqProducer = new MessageProducer("request");
        RequestManager.reqProducer.init();
        RequestManager.initCleanTask();

        MessageProducer responseProducer = new MessageProducer("response");
        responseProducer.init();
        ReqListener reqListener = new ReqListener(responseProducer);
        EventManager.getInstance().registerListener(reqListener);

        RespListener respListener = new RespListener();
        EventManager.getInstance().registerListener(respListener);

        MessageConsumer consumer = new MessageConsumer("request");
        consumer.init();
        MessageConsumer consumer1 = new MessageConsumer("response");
        consumer1.init();
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