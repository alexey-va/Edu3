package org.example;

import lombok.Data;


@Data
public class Building {

    final int floors;

    public Building(int floors){
        if(floors <= 0) throw new IllegalArgumentException();
        this.floors=floors;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("дом с "+floors);
        // вроде так но я не оч помню
        if(floors % 10 == 1 && floors != 11) return builder.append(" этажем").toString();
        return builder.append(" этажами").toString();
    }
}
