package org.example.other.concur.latches;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicMatrixMultiplication {

    CyclicBarrier cyclicBarrier;
    List<MyThread> threads = new ArrayList<>();
    int size;

    double[][] a;
    double[][] b;

    public CyclicMatrixMultiplication(int size, double[][] a, double[][] b) {
        if(a.length == 0 || b.length == 0) throw new IllegalArgumentException("Matrix dimensions do not match");
        if(a.length != b[0].length) throw new IllegalArgumentException("Matrix dimensions do not match");
        this.a = a;
        this.b = b;
        this.cyclicBarrier = new CyclicBarrier(size);
        this.size = size;

        for (int i = 0; i < size; i++) {
            int start = i*a.length/size;
            int end = (i+1)*a.length/size-1;
            //System.out.println(start + " " + end);
            threads.add(new MyThread(start, end, a, b, cyclicBarrier));
        }
    }

    public double[][] run() throws BrokenBarrierException, InterruptedException {
        for(MyThread thread : threads) thread.start();
        for (MyThread thread : threads) thread.join();

        double[][] res = new double[a.length][b[0].length];
        for(MyThread thread : threads){
            for(int i=0;i<thread.res.length;i++){
                System.arraycopy(thread.res[i], 0, res[i + thread.start], 0, thread.res[0].length);
            }
        }
        return res;
    }




    static class MyThread extends Thread{
        int start, end;
        int length;
        double[][] res;
        double[][] a, b;
        CyclicBarrier cyclicBarrier;

        public MyThread(int start, int end, double[][] a, double[][] b, CyclicBarrier cyclicBarrier) {
            this.start = start;
            this.end = end;
            this.cyclicBarrier = cyclicBarrier;

            length = b[0].length;
            this.a = a;
            this.b = b;

            res = new double[end-start+1][length];
        }

        public void run(){
            for(int i=start;i<=end;i++){
                for(int j=0;j<length;j++){
                    for(int k=0;k<a[0].length;k++){
                        res[i-start][j] += a[i][k]*b[k][j];
                    }
                }

            }

        }
    }

}
