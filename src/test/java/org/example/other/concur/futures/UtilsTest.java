package org.example.other.concur.futures;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testDoStuff() throws InterruptedException, ExecutionException {
        Utils.doStuff();
        //Thread.sleep(2000);
    }

}