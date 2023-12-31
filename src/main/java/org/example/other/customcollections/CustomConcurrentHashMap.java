package org.example.other.customcollections;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomConcurrentHashMap<K, V> implements ConcurrentMap<K, V> {

    private double loadFactor;
    private int parallelism;
    private Region<K, V>[] regions;

    public CustomConcurrentHashMap(int parallelism, double loadFactor) {
        this.parallelism = parallelism;
        this.loadFactor = loadFactor;
        regions = new Region[parallelism];

        for (int i = 0; i < parallelism; i++) {
            regions[i] = new Region<>(loadFactor);
        }
    }

    public CustomConcurrentHashMap(int parallelism) {
        this(parallelism, 0.75);
    }

    public CustomConcurrentHashMap(double loadFactor) {
        this(16, loadFactor);
    }

    public CustomConcurrentHashMap(Map<? extends K, ? extends V> m) {
        this(16, 0.75);
        putAll(m);
    }

    public CustomConcurrentHashMap() {
        this(16, 0.75);
    }


    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    @Override
    public int size() {
        int size = 0;
        for (Region<K, V> region : regions) {
            //System.out.println(region.size.get()+ " "+region);
            size += region.size.get();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = hash(key);
        Entry<K, V> entry = regions[hash % parallelism].getEntry(key, hash);
        //System.out.println(entry+" "+key+" "+hash);
        return entry != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Region<K, V> region : regions) {
            for (Map.Entry<K, V> Entry : region.getEntrySet()) {
                if (Objects.equals(value, Entry.getValue())) return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = hash(key);
        Entry<K, V> entry = regions[hash % parallelism].getEntry(key, hash);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        Entry<K, V> entry = new Entry<>(key, value, null, hash);
        return regions[hash % parallelism].addEntry(entry);
    }

    @Override
    public V remove(Object key) {
        int hash = hash(key);
        return regions[hash % parallelism].remove(key, hash);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (Region<K, V> region : regions) {
            region.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> values = new HashSet<>();
        for (Region<K, V> region : regions) {
            values.addAll(region.getEntrySet().stream().map(Map.Entry::getKey).toList());
        }
        return values;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (Region<K, V> region : regions) {
            values.addAll(region.getEntrySet().stream().map(Map.Entry::getValue).toList());
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrieset = new HashSet<>();
        for (Region<K, V> region : regions) {
            entrieset.addAll(region.getEntrySet());
        }
        return entrieset;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value, null, hash(key));
        return regions[entry.hash % parallelism].putIfAbsent(entry);
    }

    @Override
    public boolean remove(Object key, Object value) {
        int hash = hash(key);
        return regions[hash % parallelism].remove(key, hash) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int hash = hash(key);
        return regions[hash % parallelism].replace(key, oldValue, newValue, hash);
    }

    @Override
    public V replace(K key, V value) {
        int hash = hash(key);
        return regions[hash % parallelism].replace(key, value, hash);
    }


    @ToString
    static class Region<K, V> {
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        private Entry<K, V>[] entries;
        private double loadFactor;
        private AtomicInteger size = new AtomicInteger(0);

        public Region(double loadFactor) {
            this.loadFactor = loadFactor;
        }


        public void clear() {
            try {
                lock.writeLock().lock();
                entries = null;
                size.set(0);
            } finally {
                lock.writeLock().unlock();
            }
        }


        public V replace(Object key, V newValue, int hash) {
            try {
                lock.writeLock().lock();

                if(entries == null) return null;

                int bucket = hash % entries.length;
                if (entries[bucket] == null) return null;
                Entry<K, V> head = entries[bucket];
                if (check(key, hash, head)) {
                    Entry<K, V> newValueEntry = new Entry<>((K) key, newValue, null, hash);
                    entries[bucket] = newValueEntry;
                    return head.value;
                }
                while (head.next != null) {
                    if (check(key, hash, head.next)) {
                        Entry<K, V> newValueEntry = new Entry<>((K) key, newValue, null, hash);
                        V oldValue = head.next.value;
                        head.next = newValueEntry;
                        return oldValue;
                    }
                    head = head.next;
                }
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean replace(Object key, V oldValue, V newValue, int hash) {
            try {
                lock.writeLock().lock();

                if(entries == null) return false;

                int bucket = hash % entries.length;
                if (entries[bucket] == null) return false;
                Entry<K, V> head = entries[bucket];
                if (check(key, hash, head)) {
                    if (Objects.equals(oldValue, head.value)) {
                        head.value = newValue;
                        return true;
                    }
                    return false;
                }
                while (head.next != null) {
                    if (check(key, hash, head.next)) {
                        if (Objects.equals(oldValue, head.next.value)) {
                            head.next.value = newValue;
                            return true;
                        }
                        return false;
                    }
                    head = head.next;
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public V remove(Object key, int hash) {
            try {
                lock.writeLock().lock();

                if(entries == null) return null;

                int bucket = hash % entries.length;
                if (entries[bucket] == null) return null;
                Entry<K, V> head = entries[bucket];
                if (check(key, hash, head)) {
                    V old = head.value;
                    entries[bucket] = head.next;
                    size.decrementAndGet();
                    return old;
                }
                while (head.next != null) {
                    if (check(key, hash, head.next)) {
                        V old = head.value;
                        head.next = head.next.next;
                        size.decrementAndGet();
                        return old;
                    }
                    head = head.next;
                }
                return null;
            } finally {
                lock.writeLock().unlock();
            }

        }

        private V addEntry(Entry<K, V> entry) {
            try {
                lock.writeLock().lock();

                if(entries == null || size.get() > entries.length * loadFactor) resize();

                int bucket = entry.hash % entries.length;
                Entry<K, V> newEntry = new Entry<>(entry.key, entry.value, null, entry.hash);

                if (entries[bucket] == null) entries[bucket] = newEntry;
                else {
                    Entry<K, V> head = entries[bucket];
                    while (head.next != null) {
                        if (check(entry.key, entry.hash, head.next)) {
                            newEntry.next = head.next.next;
                            V old = head.next.value;
                            head.next = newEntry;
                            return old;
                        }
                        head = head.next;
                    }
                    head.next = newEntry;
                }
                size.incrementAndGet();
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public Entry<K, V> getEntry(Object key, int hash) {
            try {
                lock.readLock().lock();

                if(entries == null) return null;
                int bucket = hash % entries.length;
                if (entries[bucket] == null) return null;

                Entry<K, V> head = entries[bucket];
                if (check(key, hash, head)) return head;
                while (head.next != null) {
                    head = head.next;
                    if (check(key, hash, head)) return head;
                }
                return null;
            } finally {
                lock.readLock().unlock();
            }
        }

        public Set<Map.Entry<K, V>> getEntrySet() {
            try {
                lock.readLock().lock();
                Set<Map.Entry<K, V>> entrieset = new HashSet<>();
                if (entries == null) return entrieset;
                for (Entry<K, V> Entry : entries) {
                    if (Entry == null) continue;
                    Entry<K, V> head = Entry;
                    entrieset.add(head);
                    while (head.next != null) {
                        head = head.next;
                        entrieset.add(head);
                    }
                }
                return entrieset;
            } finally {
                lock.readLock().unlock();
            }
        }

        private boolean check(Object key, int hash, Entry<K, V> entry) {
            if (hash != entry.hash) return false;
            return Objects.equals(key, entry.key);
        }

        private void resize() {
            if (entries == null) {
                entries = new Entry[16];
                return;
            }

            //System.out.println("from resize " + Arrays.toString(entries)+" "+size.get());

            Entry<K, V>[] oldEntries = entries;
            entries = new Entry[oldEntries.length * 2];

            size.set(0);

            for (Entry<K, V> entry : oldEntries) {
                while (entry != null) {
                    addEntry(entry);
                    entry = entry.next;
                }
            }

            //System.out.println("to resize " + Arrays.toString(entries)+" "+size.get());
        }

        public V putIfAbsent(Entry<K, V> entry) {
            try {
                lock.writeLock().lock();

                if(entries == null || size.get() > entries.length * loadFactor) resize();

                int bucket = entry.hash % entries.length;
                if (entries[bucket] == null) {
                    entries[bucket] = entry;
                    size.incrementAndGet();
                    return null;
                }
                Entry<K, V> head = entries[bucket];
                if (check(entry.key, entry.hash, head)) return head.value;
                while (head.next != null) {
                    if (check(entry.key, entry.hash, head.next)) return head.next.value;
                    head = head.next;
                }
                head.next = entry;
                size.incrementAndGet();
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }


    }

    @AllArgsConstructor
    @ToString
    static class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        @ToString.Exclude
        Entry<K, V> next;
        int hash;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            return Objects.equals(o, key);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

}
