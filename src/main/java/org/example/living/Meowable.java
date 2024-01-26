package org.example.living;

public interface Meowable {

    default void meow() {
        System.out.println("meow");
    }

}
