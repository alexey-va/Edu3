package org.example.other.events;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
public abstract class Handler {

    private Event.Priority priority;
    private boolean ignoreCancelled;

    public abstract void handle(Event event) throws InvocationTargetException, IllegalAccessException;

}
