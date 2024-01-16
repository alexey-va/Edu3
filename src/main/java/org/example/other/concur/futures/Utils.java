package org.example.other.concur.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String doStuff() throws ExecutionException, InterruptedException {
        System.out.println(CompletableFuture.completedFuture(1)
                .thenCombine(CompletableFuture.completedFuture(2), (a, b) -> a + b)
                .thenApplyAsync(s -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    throw new RuntimeException("Exception in thenApplyAsync");
                    //return 5;
                })
                .whenComplete((s, e) -> {
                    if (e != null) {
                        System.out.println("Exception: " + e.getMessage());
                    } else {
                        System.out.println("Result: " + s);
                    }
                })
                //.orTimeout(1, TimeUnit.MILLISECONDS)
                .join());
        return "done";
    }

}
