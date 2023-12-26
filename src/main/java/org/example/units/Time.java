package org.example.units;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
public class Time {

    LocalTime time;

    public Time(int sec){
        time = LocalTime.ofSecondOfDay(sec % (LocalTime.MAX.toSecondOfDay()+1));
    }
    public Time(int h, int m, int s){
        this(h*3600+m*60+s);
    }

    public int hours(){
        return time.getHour();
    }

    public int minutes(){
        return time.getMinute();
    }

    public int seconds(){
        return time.getSecond();
    }

    @Override
    public String toString(){
        return time.format(
                DateTimeFormatter.ISO_LOCAL_TIME
        );
    }

}
