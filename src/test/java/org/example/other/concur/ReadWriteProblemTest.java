package org.example.other.concur;

import org.example.other.concur.ReadWriteProblem;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ReadWriteProblemTest {


    @Test
    public void testReadWriteOperations() throws InterruptedException {
        ReadWriteProblem yourClass = new ReadWriteProblem();
        int numReaders = 5;
        int numWriters = 5;
        int iterations = 100;
        CountDownLatch latch = new CountDownLatch(numReaders + numWriters);

        // Start reader threads
        for (int i = 0; i < numReaders; i++) {
            new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    String result = yourClass.read();
                    // Verify that the result is not null
                    assertNotNull(result);
                }
                latch.countDown();
            }).start();
        }

        // Start writer threads
        for (int i = 0; i < numWriters; i++) {
            final int writerIndex = i;
            new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    String content = "Writer" + writerIndex + "_String" + j;
                    yourClass.write(content);
                }
                latch.countDown();
            }).start();
        }

        // Wait for all threads to finish
        latch.await();

        // Perform assertions after all threads complete
        String finalResult = yourClass.read();
        String[] finalResultArray = finalResult.split(" ");

        // Verify that the number of written strings equals the expected total
        assertEquals(numWriters * iterations, finalResultArray.length);

        // Verify that the written strings are present in the final result
        for (int i = 0; i < numWriters; i++) {
            for (int j = 0; j < iterations; j++) {
                String expectedContent = "Writer" + i + "_String" + j;
                assertTrue(finalResult.contains(expectedContent));
            }
        }
    }

    @Test
    public void fatStressTest_runAndGrabACoffee() throws InterruptedException {
        ReadWriteProblem yourClass = new ReadWriteProblem();
        int numReaders = 100;
        int numWriters = 100;
        int iterations = 100;
        CountDownLatch latch = new CountDownLatch(numReaders + numWriters);

        ExecutorService executorService = Executors.newFixedThreadPool(numReaders + numWriters);

        // Start writer threads first to ensure at least one write operation
        for (int i = 0; i < numWriters; i++) {
            final int writerIndex = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < iterations; j++) {
                        String content = "Writer" + writerIndex + "_String" + j;
                        yourClass.write(content);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Start reader threads
        for (int i = 0; i < numReaders; i++) {
            executorService.submit(() -> {
                try {
                    // Wait until at least one writer has executed
                    latch.await();
                    for (int j = 0; j < iterations; j++) {
                        String result = yourClass.read();
                        // Verify that the result is not null
                        assertTrue(result != null && !result.isEmpty());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to finish
        latch.await(10, TimeUnit.SECONDS); // Adjust the timeout as needed

        executorService.shutdownNow();

        // Perform assertions after all threads complete
        String finalResult = yourClass.read();
        //System.out.println(finalResult);
        String[] finalResultArray = finalResult.split(" ");

        // Verify that the number of written strings equals the expected total
        assertEquals(numWriters * iterations, finalResultArray.length);

        // Verify that the written strings are present in the final result
        for (int i = 0; i < numWriters; i++) {
            for (int j = 0; j < iterations; j++) {
                String expectedContent = "Writer" + i + "_String" + j;
                assertTrue(finalResult.contains(expectedContent));
            }
        }
    }
}