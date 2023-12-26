package org.example.living;

public interface Meowable {

    default public void meow() {
        System.out.println("meow");
    }

}
