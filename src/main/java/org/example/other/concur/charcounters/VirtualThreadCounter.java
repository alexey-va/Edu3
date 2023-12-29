package org.example.other.concur.charcounters;

import org.example.other.concur.pools.ExecutorCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualThreadCounter implements AbstractCharCounter{
    @Override
    public long countChars(String str, char c, int param1, int param2) {
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

        List<Future<?>> threadList = new ArrayList<>(param1);
        int step = str.length()/param1;
        AtomicLong atomicLong = new AtomicLong(0);

        for(int i = 0; i < param1; i++){
            int startInclusive = Math.min(str.length() -1, i*step);
            int endInclusive = i==param1-1 ? str.length()-1 : Math.min(str.length() -1, startInclusive+step-1);
            //System.out.printf("startInclusive: %d, endInclusive: %d%n", startInclusive, endInclusive);

            Runnable runnable = () -> {
                int count = 0;
                for (int j = startInclusive; j <= endInclusive; j++) {
                    if (str.charAt(j) == c) {
                        count++;
                    }
                }
                atomicLong.addAndGet(count);
            };
            threadList.add(service.submit(runnable));
        }

        threadList.forEach(thread -> {
            try {
                thread.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return atomicLong.longValue();
    }
}
