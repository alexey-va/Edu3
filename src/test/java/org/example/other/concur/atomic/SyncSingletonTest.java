package org.example.other.concur.atomic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SyncSingletonTest {


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 20, 30, 40, 80, 200, 1000, 10000})
    public void testSingletonFactory(int threads) throws InterruptedException {
        Supplier<SyncSingleton> supplier = SyncSingleton::getInstance;

        List<CompletableFuture<SyncSingleton>> futures = new ArrayList<>();

        for(int i=0;i<threads;i++) {
            futures.add(CompletableFuture.supplyAsync(supplier));
        }

        for(var future : futures){
            try {
                assertEquals(future.get(), SyncSingleton.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}