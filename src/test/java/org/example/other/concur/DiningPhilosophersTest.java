package org.example.other.concur;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiningPhilosophersTest {


    static Stream<Class<? extends AbstractDiningPhilosophers>> implementations() {
        return Stream.of(
                ChandyDiningPhilosophers.class,
                PriorityDiningPhilosophers.class,
                WithStateDiningPhilosophers.class,
                AssymetricDiningPhilosophers.class
        );
    }

    @ParameterizedTest
    @MethodSource("implementations")
    public void testDiningPhilosophers_2person(Class<? extends AbstractDiningPhilosophers> clazz)
            throws Exception {
        int numberOfPhilosophers = 2;
        int bites = 10000;

        AbstractDiningPhilosophers diningPhilosophers = clazz.getConstructor(int.class, int.class).newInstance(numberOfPhilosophers, bites);
        diningPhilosophers.start();

        // Wait for philosophers to finish
        for (Thread thread : diningPhilosophers.getThreads()) {
            thread.join();
        }

        // Check the log for expected messages
        List<String> log = diningPhilosophers.getLog();

        // Ensure each philosopher has logged "thinking" and "eating" messages
        for (int i = 0; i < numberOfPhilosophers; i++) {
            assertTrue(log.contains("Philosopher " + i + " is thinking"));
            assertTrue(log.contains("Philosopher " + i + " is eating"));
        }

        // Ensure the correct number of "eating" messages for each philosopher
        for (int i = 0; i < numberOfPhilosophers; i++) {
            int eatingCount = countOccurrences(log, "Philosopher " + i + " is eating");
            int thinkingCount = countOccurrences(log, "Philosopher " + i + " is thinking");
            assertEquals(eatingCount, bites, "Philosopher " + i + " should eat");
            assertEquals(thinkingCount, bites, "Philosopher " + i + " should think");
        }
    }

    @ParameterizedTest
    @MethodSource("implementations")
    public void testDiningPhilosophers_many(Class<? extends AbstractDiningPhilosophers> clazz)
            throws Exception {
        int numberOfPhilosophers = 100;
        int bites = 100;

        AbstractDiningPhilosophers diningPhilosophers = clazz.getConstructor(int.class, int.class).newInstance(numberOfPhilosophers, bites);
        diningPhilosophers.start();

        // Wait for philosophers to finish
        for (Thread thread : diningPhilosophers.getThreads()) {
            thread.join();
        }

        // Check the log for expected messages
        List<String> log = diningPhilosophers.getLog();

        // Ensure each philosopher has logged "thinking" and "eating" messages
        for (int i = 0; i < numberOfPhilosophers; i++) {
            assertTrue(log.contains("Philosopher " + i + " is thinking"));
            assertTrue(log.contains("Philosopher " + i + " is eating"));
        }

        // Ensure the correct number of "eating" messages for each philosopher
        for (int i = 0; i < numberOfPhilosophers; i++) {
            int eatingCount = countOccurrences(log, "Philosopher " + i + " is eating");
            int thinkingCount = countOccurrences(log, "Philosopher " + i + " is thinking");
            assertEquals(eatingCount, bites, "Philosopher " + i + " should eat");
            assertEquals(thinkingCount, bites, "Philosopher " + i + " should think");
        }
    }

    private int countOccurrences(List<String> list, String target) {
        return (int) list.stream().filter(target::equals).count();
    }


}