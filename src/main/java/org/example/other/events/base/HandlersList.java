package org.example.other.events.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

public class HandlersList {

    private EnumMap<Event.Priority, List<Handler>> handlers = new EnumMap<>(Event.Priority.class);

    public void addHandler(Handler handler) {
        handlers.putIfAbsent(handler.getPriority(), new ArrayList<>());
        handlers.get(handler.getPriority()).add(handler);
    }

    public void removeHandler(Handler handler) {
        handlers.get(handler.getPriority()).remove(handler);
    }

    public Stream<Handler> stream(){
        return handlers.values()
                .stream()
                .flatMap(Collection::stream);
    }

}
