package org.example.other.concur.collections;

import java.util.*;
import java.util.concurrent.*;

public class AllColls {

    Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
    Queue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
    BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
    BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
    NavigableMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
    Deque<String> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();

}
