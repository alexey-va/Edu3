package org.example.other.reactive;

import com.thedeanda.lorem.LoremIpsum;
import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static void test1() {
        Observable.create((emitter) -> {
                    System.out.println("Emitter thread: " + Thread.currentThread().getName());
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onNext(4);
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe((r) -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    System.out.println("Data: " + r);
                });

    }

}
