package org.example.other.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.core.net.Priority;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.List;

@ToString
@Getter
@Setter
public abstract class Event {

    boolean isCancelled;

    public Event(){
        isCancelled = false;
    }

    public void cancel(){
        isCancelled = true;
    }

    public abstract EnumMap<Priority, List<Handler>> getHandlersMap();

    public Event run(){
        for(var handlersList : getHandlersMap().values()){
            for(var handler : handlersList){
                if(isCancelled && !handler.isIgnoreCancelled()){
                    continue;
                }
                try {
                    handler.handle(this);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public enum Priority{
        LOW,
        MEDIUM,
        HIGH
    }
}
