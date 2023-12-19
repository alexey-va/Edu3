package org.example;

public interface Meowable {

    default public void meow() {
        System.out.println("meow");
    }

}
