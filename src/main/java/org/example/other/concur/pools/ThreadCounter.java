package org.example.other.concur.pools;

import java.util.ArrayList;
import java.util.List;

public class ThreadCounter extends AbstractCounter {

    List<Thread> threads;
    static volatile int counter = 0;

    public ThreadCounter(int n, int max) {
        threads = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            threads.add(new MyThread(max));
        }
    }

    public int run(){
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
        return counter;
    }

    static class MyThread extends Thread {
        int to;

        public MyThread(int to) {
            this.to = to;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (ThreadCounter.class) {
                    if(counter >= to)
                        break;
                    counter++;
                }
            }
        }
    }

}
