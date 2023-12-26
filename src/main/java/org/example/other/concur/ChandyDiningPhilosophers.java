package org.example.other.concur;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChandyDiningPhilosophers extends AbstractDiningPhilosophers {


    List<Thread> threads = new ArrayList<>();
    Queue<String> log = new ConcurrentLinkedQueue<>();

    public ChandyDiningPhilosophers(int count, int bites) {
        Token token = new Token(count, 0);
        Lock[] locks = new Lock[count];
        for (int i = 0; i < count; i++) {
            locks[i] = new ReentrantLock();
        }

        for (int i = 0; i < count; i++) {
            threads.add(new Thread(
                            new Philosopher(
                                    locks[i],
                                    locks[(i + 1) % count],
                                    i,
                                    log,
                                    bites,
                                    token
                            )
                    )
            );
        }
    }


    @Override
    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    @Override
    public List<Thread> getThreads() {
        return threads;
    }


    @AllArgsConstructor
    static class Philosopher implements Runnable {
        private final Lock leftChopstick;
        private final Lock rightChopstick;
        private final int id;
        private final Collection<String> log;
        private int bites;
        private final Token token;

        @Override
        public void run() {
            try {
                while (bites > 0) {
                    log.add("Philosopher " + id + " is thinking");
                    //System.out.println("Philosopher " + id + " is thinking "+bites);
                    token.request(id);

                    try {
                        leftChopstick.lock();
                        rightChopstick.lock();
                        bites--;
                        //System.out.println("Philosopher " + id + " is eating "+bites);
                        log.add("Philosopher " + id + " is eating");
                    } finally {
                        leftChopstick.unlock();
                        rightChopstick.unlock();
                    }
                    token.release(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AllArgsConstructor
    static class Token {
        private final int size;
        private int holder;

        public synchronized void request(int philId) throws InterruptedException {
            //System.out.println("Philosopher " + philId + " is requesting token");
            while (holder != philId) {
                wait();
            }
        }

        public synchronized void release(int philId) {
            holder = (philId + 1) % size;
            notifyAll();
        }
    }
}
