package org.example.other.concur.atomic;

public class SyncSingleton {

    private volatile static SyncSingleton instance;

    private SyncSingleton() {
        System.out.println("SyncSingleton created");
    }

    public static SyncSingleton getInstance() {
        if (instance == null) {
            synchronized (SyncSingleton.class) {
                if (instance == null)
                    instance = new SyncSingleton();
            }
        }
        return instance;
    }

}
