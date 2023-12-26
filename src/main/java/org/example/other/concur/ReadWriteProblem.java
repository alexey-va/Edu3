package org.example.other.concur;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.protocol.types.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Log4j2
public class ReadWriteProblem {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    List<String> strings = new ArrayList<>();

    public ReadWriteProblem() {
    }

    public ReadWriteProblem(List<String> strings) {
        this.strings = new ArrayList<>(strings);
    }

    public String read() {
        lock.readLock().lock();
        try {
            return String.join(" ", strings);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(String s){
        lock.writeLock().lock();
        try {
            strings.add(s);
        }finally {
            lock.writeLock().unlock();
        }
    }

}
