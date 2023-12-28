package org.example.other.concur.latches;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CyclicMatrixMultiplicationTest {

    private static double[][] matrixA;
    private static double[][] matrixB;
    static SimpleMatrix matrixExpected;

    @BeforeAll
    public static void setup(){
        int size = 1000;
        matrixA = new double[size][size];
        matrixB = new double[size][size];
        Random random = new Random();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                matrixA[i][j] = random.nextInt(10);
                matrixB[i][j] = random.nextInt(10);
            }
        }

        SimpleMatrix matrix1 = new SimpleMatrix(matrixA);
        SimpleMatrix matrix2 = new SimpleMatrix(matrixB);
        matrixExpected = matrix1.mult(matrix2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 10, 20, 30, 40, 80, 200})
    public void testMatrixMultiplication(int threads) throws InterruptedException, BrokenBarrierException {
        CyclicMatrixMultiplication cyclicMatrixMultiplication = new CyclicMatrixMultiplication(threads, matrixA, matrixB);
        double[][] res = cyclicMatrixMultiplication.run();
        SimpleMatrix matrix4 = new SimpleMatrix(res);
        assertTrue(matrix4.isIdentical(matrix4, 0.0001));
    }

}