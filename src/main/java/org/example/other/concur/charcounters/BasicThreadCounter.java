package org.example.other.concur.charcounters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BasicThreadCounter implements AbstractCharCounter{
    @Override
    public long countChars(String str, char c, int param1, int param2) {
        List<Thread> threadList = new ArrayList<>(param1);
        int step = str.length()/param1;
        AtomicLong atomicLong = new AtomicLong(0);

        for(int i = 0; i < param1; i++){
            int startInclusive = Math.min(str.length() -1, i*step);
            int endInclusive = i==param1-1 ? str.length()-1 : Math.min(str.length() -1, startInclusive+step-1);
            //System.out.printf("startInclusive: %d, endInclusive: %d%n", startInclusive, endInclusive);

            Thread thread = new Thread(() -> {
                int count=0;
                for(int j =startInclusive; j<= endInclusive;j++){
                    if(str.charAt(j) == c){
                        count++;
                    }
                }
                atomicLong.addAndGet(count);
            });
            threadList.add(thread);
            thread.start();
        }

        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return atomicLong.longValue();
    }
}
