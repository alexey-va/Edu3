package org.example.other.concur.latches;

import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileLatchTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 20, 30, 40, 80, 200})
    public void testLatch(int threads) throws InterruptedException, IOException {
        int numThreads = threads;
        String text= LoremIpsum.getInstance().getWords(1000000);
        System.out.println("generated text of "+text.length()+" chars");
        ReadFileLatch latch = new ReadFileLatch(numThreads, text);
        String res = latch.run();
        assertEquals(text, res);
    }

}