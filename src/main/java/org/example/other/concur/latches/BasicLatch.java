package org.example.other.concur.latches;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Log4j2
public class BasicLatch {

    CountDownLatch latch;
    List<Thread> threads;
    Queue<String> output = new ConcurrentLinkedQueue<>();

    public BasicLatch(int n) throws InterruptedException {
        latch = new CountDownLatch(n);
        threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(() -> {
                log.info("Loading resources on thread "+ Thread.currentThread().getName());
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
                log.info("Resources loaded on thread "+ Thread.currentThread().getName());
                output.add("Resources loaded on thread "+ Thread.currentThread().getName());
            }, "Thread-"+i));
        }
    }

    public boolean run() throws InterruptedException {
        for (Thread thread : threads) thread.start();

        boolean res = latch.await(10, TimeUnit.SECONDS);
        if(res) log.info("All resources loaded");
        else log.info("Not all resources loaded");

        return res;
    }

    public List<String> getOutput() {
        return new ArrayList<>(output);
    }

}
