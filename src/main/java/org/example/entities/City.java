package org.example.entities;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class City {

    private String name;
    private Set<Route> routes = new HashSet<>();

    public record Route(City target, int price) { }

    public City(String name) {
        this.name = name;
    }

    public City(String name, Route... routes) {
        this(name);
        this.routes = Arrays.stream(routes).collect(Collectors.toSet());
    }

    private boolean hasRouteTo(City target) {
        return routes.stream()
                .anyMatch(route -> route.target == target);
    }

    public boolean deleteRoute(City target) {
        return routes.removeIf(r -> r.target == target);
    }

    public void addRoute(City target, int price) {
        if (this.hasRouteTo(target))
            throw new IllegalArgumentException("Already has road to " + target + "!");
        routes.add(new Route(target, price));
    }

    // replaced public set<> with getter
    public Optional<Route> getRouteTo(City target) {
        return routes.stream().filter(r -> r.target == target).findAny();
    }

    @Override
    public String toString() {
        return name + ". " +
                "Routes:\n" +
                routes.stream()
                        .map(route -> " > To " + route.target.name + " for " + route.price + "$")
                        .collect(Collectors.joining("\n"));
    }

    @Override
    final public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof City city)) return false;
        if (!Objects.equals(this.name, city.name)) return false;
        if (this.routes.size() != city.routes.size()) return false;
        return this.routes.containsAll(city.routes);
    }

    @Override
    final public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (routes != null ? routes.hashCode() : 0);
        return result;
    }
}
