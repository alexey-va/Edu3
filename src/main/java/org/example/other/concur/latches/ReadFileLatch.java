package org.example.other.concur.latches;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReadFileLatch {

    List<MyReader> threads;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CountDownLatch latch;


    public ReadFileLatch(int numThreads, String text) {
        threads = new ArrayList<>();
        latch = new CountDownLatch(numThreads);
        for(int i=0;i<numThreads;i++){
            threads.add(new MyReader(
                    i*text.length()/numThreads,
                    (i+1)*text.length()/numThreads,
                    text,
                    latch
            ));
        }
    }

    public String run() throws InterruptedException, IOException {
        for(Thread thread : threads) thread.start();
        boolean res = latch.await(20, TimeUnit.SECONDS);
        if(res) System.out.println("All resources loaded");
        else{
            System.out.println("Not all resources loaded");
            return null;
        }
        for(MyReader reader : threads) reader.baos.writeTo(baos);
        return baos.toString();
    }


    @RequiredArgsConstructor
    static class MyReader extends Thread{
        final int start, end;
        final String text;
        final CountDownLatch latch;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        public void run(){
            for(int i=start;i<end;i++){
                baos.write(text.charAt(i));
            }
            latch.countDown();
        }
    }


}
