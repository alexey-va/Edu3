package org.example.other.concur;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityDiningPhilosophers extends AbstractDiningPhilosophers{

    int size;
    OrderedLock[] locks;
    List<Philosopher> philosophers = new ArrayList<>();
    @Getter
    List<Thread> threads = new ArrayList<>();

    @Getter
    Queue<String> log = new ConcurrentLinkedQueue<>();

    public PriorityDiningPhilosophers(int count, int bites) {
        this.size = count;

        locks = new OrderedLock[size];
        for(int i=0;i<size;i++) {
            locks[i] = new OrderedLock(new ReentrantLock(), i);
        }

        for (int i = 0; i < size; i++) {
            philosophers.add(new Philosopher(
                    locks[i],
                    locks[(i+1)%size],
                    i,
                    log,
                    bites));
        }
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    public void start() {
        for (Philosopher p : philosophers) {
            Thread thread = new Thread(p);
            thread.start();
            threads.add(thread);
        }
    }

    @AllArgsConstructor
    static class Philosopher implements Runnable {
        private final OrderedLock leftChopstick;
        private final OrderedLock rightChopstick;
        private final int id;
        private final Collection<String> log;
        private int bites;


        public void run() {
            OrderedLock firstLock = leftChopstick.id > rightChopstick.id ? rightChopstick : leftChopstick;
            OrderedLock secondLock = leftChopstick.id > rightChopstick.id ? leftChopstick : rightChopstick;
            try {
                while (bites > 0) {
                    log.add("Philosopher " +id+ " is thinking");
                    try {
                        firstLock.lock.lock();
                        try {
                            secondLock.lock.lock();
                            log.add("Philosopher " +id+ " is eating");
                            bites--;
                        } finally {
                            secondLock.lock.unlock();
                        }
                    } finally {
                        firstLock.lock.unlock();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AllArgsConstructor
    static class OrderedLock{
        Lock lock;
        final int id;
    }

}
