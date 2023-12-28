package org.example.other.concur.atomic;

import java.util.ArrayList;
import java.util.List;

public class BasicIncrementer extends AbstractIncrementer{

    List<Thread> threads = new ArrayList<>();
    public static long counter = 0;

    public BasicIncrementer(int threads, int iterations){
        for(int i=0;i<threads;i++){
            this.threads.add(new MyThread(iterations));
        }
    }

    @Override
    public long start() {
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
        return counter;
    }

    static class MyThread extends Thread{
        int iterations;
        public MyThread(int iterations){
            this.iterations = iterations;
        }
        public void run(){
            for(int i=0;i<iterations;i++){
                counter++;
            }
        }
    }
}
