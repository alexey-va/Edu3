package org.example.other.concur.charcounters;

import lombok.AllArgsConstructor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinCounter implements AbstractCharCounter{
    @Override
    public long countChars(String str, char c, int param1, int param2) {
        try(ForkJoinPool pool = new ForkJoinPool(param1)){
            RecursiveTask<Long> myRecursiveTask = new MyRecursiveTask(0, str.length()-1, str, c, param2);
            return pool.invoke(myRecursiveTask);
        }
    }


    @AllArgsConstructor
    private static class MyRecursiveTask extends RecursiveTask<Long>{
        int startInclusive;
        int endInclusive;
        String str;
        char c;
        int threshold;
        @Override
        protected Long compute() {
            if(endInclusive-startInclusive < threshold){
                long count = 0;
                for (int j = startInclusive; j <= endInclusive; j++) {
                    if (str.charAt(j) == c) {
                        count++;
                    }
                }
                return count;
            } else{
                int mid = (startInclusive+endInclusive)/2;
                MyRecursiveTask task1 = new MyRecursiveTask(startInclusive, mid, str, c, threshold);
                MyRecursiveTask task2 = new MyRecursiveTask(mid+1, endInclusive, str, c, threshold);
                task1.fork();
                task2.fork();
                return task1.join() + task2.join();
            }
        }
    }
}
