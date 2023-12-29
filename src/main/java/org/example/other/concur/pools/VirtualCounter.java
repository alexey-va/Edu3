package org.example.other.concur.pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualCounter {

    ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
    static volatile int counter = 0;




    public int run(){
        return 0;
    }

}
