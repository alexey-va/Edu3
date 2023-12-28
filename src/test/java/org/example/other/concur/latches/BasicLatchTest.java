package org.example.other.concur.latches;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicLatchTest {

    @Test
    public void testLatch() throws InterruptedException {
        int numThreads = 10;
        BasicLatch latch = new BasicLatch(numThreads);
        boolean res = latch.run();
        assertTrue(res);
        assertEquals(numThreads, latch.getOutput().size());
    }

}