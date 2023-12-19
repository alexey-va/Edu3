package org.example;

import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.Collections;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Cat implements Meowable {

    String name;


    public void meow(){
        meow(1);
    }

    public void meow(int n){
        System.out.println(name+": "+ String.join("-", Collections.nCopies(n, "meow"))+"!");
    }

    @Override
    public String toString() {
        return "cat: "+name;
    }
}
