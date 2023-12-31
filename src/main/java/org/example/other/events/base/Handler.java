package org.example.other.events.base;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
public abstract class Handler {

    Event.Priority priority;
    boolean ignoreCancelled;
    EventListener parentInstance;

    public abstract void handle(Event event) throws InvocationTargetException, IllegalAccessException;

}
