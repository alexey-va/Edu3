package org.example.other.events.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;

@ToString
@Getter
@Setter
public abstract class Event {

    boolean isCancelled;

    public Event() {
        isCancelled = false;
    }

    public void cancel(){
        isCancelled = true;
    }

    public Event run(){
        EventManager
                .getInstance()
                .getHandlers(this.getClass())
                .forEach(handler -> {
                    try {
                        if(!isCancelled || handler.isIgnoreCancelled())
                            handler.handle(this);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        return this;
    }

    public enum Priority{
        LOW,
        MEDIUM,
        HIGH
    }
}
