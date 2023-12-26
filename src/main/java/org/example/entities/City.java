package org.example.entities;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class City {

    private String name;
    private Set<Route> routeList = new HashSet<>();
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
        if(this.hasRouteTo(target))
            throw new IllegalArgumentException("Already has road to "+target+"!");
        routeList.add(new Route(target, price));
    }

    // replaced public set<> with getter
    public Optional<Route> getRouteTo(City target){
        return routeList.stream().filter(r -> r.target == target).findAny();
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
