package org.example.other.concur;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AssymetricDiningPhilosophers extends AbstractDiningPhilosophers{

    int size;
    List<Thread> threads = new ArrayList<>();
    Queue<String> queue = new ConcurrentLinkedQueue<>();


    public AssymetricDiningPhilosophers(int size, int bites) {
        this.size = size;

        Lock[] chopsticks = new Lock[size];
        for(int i=0;i<size;i++) chopsticks[i] = new ReentrantLock();

        for (int i = 0; i < size; i++) threads.add(new Philosopher(queue, bites, i, chopsticks[i], chopsticks[(i+1)%size]));
    }

    @Override
    public void start() {
        for (Thread thread : threads) thread.start();
    }

    @Override
    public List<String> getLog() {
        return new ArrayList<>(queue);
    }

    @Override
    public List<Thread> getThreads() {
        return threads;
    }

    @AllArgsConstructor
    public static class Philosopher extends Thread{
        Collection<String> log;
        int bites;
        int id;
        Lock leftChopstick;
        Lock rightChopstick;

        public void run(){
            Lock main, sub;
            if(id%2==0){
                main = leftChopstick;
                sub = rightChopstick;
            } else{
                main = rightChopstick;
                sub = leftChopstick;
            }
            while(bites > 0){
                log.add("Philosopher " + id + " is thinking");
                synchronized (main){
                    synchronized (sub){
                        log.add("Philosopher " + id + " is eating");
                        bites--;
                    }
                }
            }
        }

    }
}
