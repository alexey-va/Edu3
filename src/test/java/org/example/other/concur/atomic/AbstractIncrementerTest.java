package org.example.other.concur.atomic;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

@Log4j2
class AbstractIncrementerTest {

    public static Stream<Class<? extends AbstractIncrementer>> provideIncrementers(){
        return Stream.of(AtomicIncrementer.class, BasicIncrementer.class);
    }

    @ParameterizedTest
    @CsvSource({
            "AtomicIncrementer, 1",
            "AtomicIncrementer, 2",
            "AtomicIncrementer, 4",
            "AtomicIncrementer, 8",
            "AtomicIncrementer, 16",
            "AtomicIncrementer, 32",
            "AtomicIncrementer, 64",
            "AtomicIncrementer, 128",
            "AtomicIncrementer, 256",
            "BasicIncrementer, 1",
            "BasicIncrementer, 2",
            "BasicIncrementer, 4",
            "BasicIncrementer, 8",
            "BasicIncrementer, 16",
            "BasicIncrementer, 32",
            "BasicIncrementer, 64",
            "BasicIncrementer, 128",
            "BasicIncrementer, 256"
    })
    public void test(String className, int threads) throws Exception{

        Class clazz = Class.forName("org.example.other.concur.atomic."+className);
        AbstractIncrementer abstractIncrementer =
                (AbstractIncrementer) clazz.getConstructor(int.class, int.class).newInstance(threads, 10000000);
        long result = abstractIncrementer.start();
        if(result!=threads*10000000L)
            log.info( "Class: "+ className+" threads: "+threads+" result: " +result+" % loss: "+
                    Math.round(100-result/(threads*10000000.0)*100.0)+"%");
        else log.info( "Class: "+ className+" threads: "+threads+" result: " +result);
    }

}