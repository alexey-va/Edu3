package org.example.entities;

import org.example.entities.City;

class SmartCity extends City {
    public SmartCity(String name) {
        super(name);
    }

    public SmartCity(String name, City.Route... routes) {
        super(name, routes);
    }

    @Override
    public void addRoute(City target, int price) {
        super.addRoute(target, price);
        target.getRouteTo(this).ifPresentOrElse(
                (s) -> {
                },
                () -> target.addRoute(this, price)
        );
    }
}
