package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class Student {

    private String name;
    private List<Integer> grades;

    public Student(String name, int... grades){
        this.name = name;
        this.grades= IntStream.of(grades)
                .boxed()
                .peek(i -> {
                    if(i < 2 || i > 5) throw new IllegalArgumentException(i+" is invalid mark!");
                })
                .collect(Collectors.toList());
    }

    public void add(int... grades){
        this.grades.addAll(
                IntStream.of(grades)
                        .boxed()
                        .peek(i -> {
                            if(i < 2 || i > 5) throw new IllegalArgumentException(i+" is invalid mark!");
                        })
                        .toList()
        );
    }

    public double average(){
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public boolean isCool(){
        if(grades.isEmpty()) return false;
        return grades.stream().allMatch(i -> i==5);
    }

}
