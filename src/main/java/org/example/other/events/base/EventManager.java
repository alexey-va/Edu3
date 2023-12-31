package org.example.other.events.base;



import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Log4j2

public class EventManager {

    private static volatile EventManager instance;
    private static Map<Class<? extends Event>, HandlersList> handlers = new HashMap<>();

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) instance = new EventManager();
            }
        }
        return instance;
    }

    public Stream<Handler> getHandlers(Class<? extends Event> eventClass) {
        return handlers.getOrDefault(eventClass, new HandlersList()).stream();
    }
    

    public void registerListener(EventListener eventListener) {
        Arrays.stream(eventListener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventHandler.class))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                .forEach(method -> {
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];

                    Handler handler = new Handler() {
                        @Override
                        public void handle(Event event) throws InvocationTargetException, IllegalAccessException {
                            method.invoke(eventListener, event);
                        }
                    };
                    handler.setPriority(annotation.priority());
                    handler.setIgnoreCancelled(annotation.ignoreCancelled());
                    handler.setParentInstance(eventListener);

                    handlers.putIfAbsent(eventClass, new HandlersList());
                    handlers.get(eventClass).addHandler(handler);
                });
    }


    public <T extends Event> T run(T event) {
        return (T) event.run();
    }

}
