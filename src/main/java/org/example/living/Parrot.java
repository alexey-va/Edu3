package org.example.living;

import java.util.Random;

public class Parrot extends Bird {
    private String vocabulary;

    private Parrot() {
    }

    public Parrot(String s) {
        if (s.isEmpty()) throw new IllegalArgumentException("String is empty");
        this.vocabulary = s;
    }

    @Override
    public String getMessage() {
        return vocabulary.substring(0, new Random().nextInt(1, vocabulary.length() + 1));
    }
}
