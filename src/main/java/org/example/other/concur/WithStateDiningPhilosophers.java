package org.example.other.concur;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WithStateDiningPhilosophers extends AbstractDiningPhilosophers {

    Queue<String> log = new ConcurrentLinkedQueue<>();
    List<Thread> threads = new ArrayList<>();
    Lock lock = new ReentrantLock();
    Condition[] conditions;
    private enum State {THINKING, HUNGRY, EATING};
    State[] states;
    int size;



    public WithStateDiningPhilosophers(int size, int bites) {
        this.size = size;

        states = new State[size];
        for(int i=0;i<size;i++) states[i]= State.THINKING;

        conditions = new Condition[size];
        for(int i=0;i<size;i++) conditions[i] = lock.newCondition();

        for (int i = 0; i < size; i++) threads.add(new Philosopher(log, i, bites, this));
    }

    @Override
    public void start() {
        for (Thread thread : threads) thread.start();
    }

    @Override
    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    @Override
    public List<Thread> getThreads() {
        return threads;
    }

    public void pickup(int pid) throws InterruptedException {
        lock.lock();
        try{
            states[pid] = State.HUNGRY;
            test(pid);
            if(states[pid] != State.EATING){
                conditions[pid].await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void putdown(int pid){
        lock.lock();
        try {
            states[pid] = State.THINKING;
            test((pid+1)%size);
            test((pid-1 < 0 ? size-1 : pid-1)%size);
        } finally {
            lock.unlock();
        }
    }

    public void test(int pid){
        int next = (pid+1)%size;
        int prev = (pid-1 < 0 ? size-1 : pid-1)%size;
        if(states[pid] == State.HUNGRY &&
                states[next] != State.EATING &&
                states[prev] != State.EATING){
            states[pid] = State.EATING;
            conditions[pid].signal();
        }

    }


    @AllArgsConstructor
    static class Philosopher extends Thread {
        Collection<String> log;
        int id;
        int bites;
        WithStateDiningPhilosophers philosophers;

        public void run() {
            try {
                while (bites > 0) {
                    //System.out.println("Philosopher " + id + " is thinking");
                    log.add("Philosopher " + id + " is thinking");
                    philosophers.pickup(id);
                    //System.out.println("Philosopher " + id + " is eating");
                    log.add("Philosopher " + id + " is eating");
                    bites--;
                    philosophers.putdown(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
