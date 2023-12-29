package org.example.other.concur.charcounters;

import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCharCounterTest {

    private static long expected;
    private static char c = 'a';
    private static String text;

    @BeforeAll
    public static void init(){
        text = LoremIpsum.getInstance().getWords(100000000);
        expected = text.chars().filter(ch -> ch == c).count();

        System.out.printf("Length: %d%n", text.length());
    }

    public static Stream<Arguments> sources(){
        return Stream.of(
                Arguments.of("BasicThreadCounter", 16),
                Arguments.of("ConsecutiveCounter", 16)
        );
    }

    public static Stream<Integer> sources2(){
        return Stream.of(
                1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 100000
        );
    }

    public static Stream<Integer> sourcesFork(){
        return Stream.of(
                1, 2, 4, 8, 16, 32, 64, 128, 256
        );
    }

    @ParameterizedTest(name = "GenericThreadCounter - {0} impl, {1} threads")
    @MethodSource("sources")
    public void test_counter(String className, int param1) throws Exception{
        Class clazz = Class.forName("org.example.other.concur.charcounters."+className);
        AbstractCharCounter counter = (AbstractCharCounter) clazz.getConstructor().newInstance();
        assertEquals(expected, counter.countChars(text, c, param1, 0));
    }

    @ParameterizedTest(name = "VirtualThreadCounter - {0} threads")
    @MethodSource("sources2")
    public void test_virtual_thread_counter(int threads) throws Exception{
        AbstractCharCounter counter = new VirtualThreadCounter();
        assertEquals(expected, counter.countChars(text, c, threads, 0));
    }

    @ParameterizedTest(name = "ForkJoinCounter - {0} threads")
    @MethodSource("sourcesFork")
    public void test_fork_counter(int threads) throws Exception{
        AbstractCharCounter counter = new ForkJoinCounter();
        assertEquals(expected, counter.countChars(text, c, threads, 1000000));
    }

}