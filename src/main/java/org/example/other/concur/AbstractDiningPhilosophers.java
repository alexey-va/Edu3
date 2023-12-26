package org.example.other.concur;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractDiningPhilosophers {


    public abstract void start();
    public abstract List<String> getLog();
    public abstract List<Thread> getThreads();

}
