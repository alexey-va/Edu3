package org.example.other.concur.executors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyExecutorTest {


    @Test
    public void test_execute() throws Exception{
        MyExecutor executor = new MyExecutor();
        executor.execute();
    }

}