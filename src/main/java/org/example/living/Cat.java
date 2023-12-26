package org.example.living;

import lombok.AllArgsConstructor;

import java.util.Collections;

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
