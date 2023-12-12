package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class City {

    String name;
    Set<Route> routeList = new HashSet<>();
    public record Route(City target, int price){}

    public City(String name){
        this.name=name;
    }
    public City(String name, Route... routes){
        this(name);
        this.routeList= Arrays.stream(routes).collect(Collectors.toSet());
    }

    private boolean hasRouteTo(City target){
        return routeList.stream()
                .anyMatch(route -> route.target == target);
    }

    public boolean deleteRoute(City target){
        return routeList.removeIf(r -> r.target==target);
    }

    public void addRoute(City target, int price){
        // двухсторонняя дорога - это 2 дороги или 1? считаем что 2
        if(this.hasRouteTo(target) || target.hasRouteTo(this))
            throw new IllegalArgumentException("Already has road!");
        routeList.add(new Route(target, price));
    }

    @Override
    public String toString(){
        return  name+ ". "+
                "Routes:\n"+
                routeList.stream()
                        .map(route -> " > To "+ route.target.name+" for "+route.price+"$")
                        .collect(Collectors.joining("\n"));
    }



}
