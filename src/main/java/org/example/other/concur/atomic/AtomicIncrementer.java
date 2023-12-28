package org.example.other.concur.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.*;

public class AtomicIncrementer extends  AbstractIncrementer{

    AtomicLong atomicInteger = new AtomicLong(0);


    List<Thread> threads = new ArrayList<>();
    public AtomicIncrementer(int threads, int iterations){
        for(int i=0;i<threads;i++){
            this.threads.add(new MyThread(iterations, atomicInteger));
        }
    }

    public long start(){
        for(Thread thread:threads){
            thread.start();
        }
        for(Thread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return atomicInteger.get();
    }


    static class MyThread extends Thread{
        int iterations;
        AtomicLong atomics;
        public MyThread(int iterations, AtomicLong atomics){
            this.iterations = iterations;
            this.atomics = atomics;
        }
        public void run(){
            for(int i=0;i<iterations;i++){
                atomics.incrementAndGet();
            }
        }
    }

}
