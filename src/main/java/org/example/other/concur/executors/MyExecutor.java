package org.example.other.concur.executors;

import java.util.concurrent.*;

public class MyExecutor {

    ScheduledExecutorService service;

    public void execute() throws ExecutionException, InterruptedException {
        Runnable task = () -> {
            System.out.println("Thread name: " + Thread.currentThread().getName());
            //throw new RuntimeException("Exception from thread: " + Thread.currentThread().getName());
        };

        Callable<Integer> callable = () -> {
          return ThreadLocalRandom.current().nextInt(1, 100);
        };

        service=Executors.newScheduledThreadPool(4);
        Future<Integer> future = service.schedule(callable, 1, TimeUnit.SECONDS);
        System.out.println("Result from callable: " + future.get());
    }

}
