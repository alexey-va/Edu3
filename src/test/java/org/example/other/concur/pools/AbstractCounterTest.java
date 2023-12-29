package org.example.other.concur.pools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCounterTest {


    public static Stream<Arguments> sources(){
        return Stream.of(
                Arguments.of("org.example.other.concur.pools.ThreadCounter", 10, 10000000),
                Arguments.of("org.example.other.concur.pools.ExecutorCounter", 10, 10000000)
        );
    }

    @ParameterizedTest
    @MethodSource("sources")
    public void test_counter(String className, int threads, int countTo) throws Exception{
        Class clazz = Class.forName(className);
        AbstractCounter counter = (AbstractCounter) clazz.getConstructor(int.class, int.class).newInstance(threads, countTo);
        Assertions.assertEquals(countTo, counter.run());
    }

}