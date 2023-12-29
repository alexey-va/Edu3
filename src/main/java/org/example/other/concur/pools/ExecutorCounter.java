package org.example.other.concur.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCounter extends AbstractCounter {

    ExecutorService service;
    private Runnable runnable;
    int n;
    private static volatile int counter = 0;

    public ExecutorCounter(int n, int max) {
        service = Executors.newFixedThreadPool(n);
        this.n=n;
        runnable = () -> {
            while (true) {
                synchronized (ThreadCounter.class) {
                    if (counter >= max)
                        break;
                    counter++;
                }
            }
        };


    }

    public int run(){
        List<Future<?>> futures = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            futures.add(service.submit(runnable));
        }
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return counter;
    }

}
