package org.example;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class Bird {
    public String getMessage(){
        return "Tweet tweet";
    };
}

class Sparrow extends Bird{
    @Override
    public String getMessage(){
        return "чырык";
    }
}

class Cuckoo extends Bird{
    @Override
    public String getMessage(){
        return String.join(" ",
                Collections.nCopies(new Random().nextInt(1, 11), "ку-ку")
        );
    }
}


class Parrot extends Bird{
    private String vocabulary;

    private Parrot(){}

    public Parrot(String s){
        if(s.isEmpty()) throw new IllegalArgumentException("String is empty");
        this.vocabulary=s;
    }
    @Override
    public String getMessage(){
        return vocabulary.substring(0, new Random().nextInt(1,vocabulary.length()+1));
    }
}
