package org.example.other.events;



import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

@Getter
@Log4j2

public class EventManager {

    private static volatile EventManager instance;

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) instance = new EventManager();
            }
        }
        return instance;
    }

    private List<EventListener> listeners = new ArrayList<>();

    public void registerListener(EventListener eventListener) {
        Class<?> clazz = eventListener.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class))
                continue;
            if (method.getParameterCount() != 1) {
                log.warn("Method {} in class {} has invalid parameter count", method.getName(), clazz.getName());
                continue;
            }

            if (!Event.class.isAssignableFrom(method.getParameterTypes()[0].getSuperclass())) {
                log.warn("Method {} in class {} has invalid parameter type", method.getName(), clazz.getName());
                continue;
            }
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

            try {
                //System.out.println(Arrays.toString(eventClass.getDeclaredFields()));
                Field field = eventClass.getDeclaredField("HANDLERS_MAP");
                field.setAccessible(true);
                EnumMap<Event.Priority, List<Handler>> map = (EnumMap<Event.Priority, List<Handler>>) field.get(null);
                map.computeIfAbsent(handler.getPriority(), k -> new ArrayList<>());
                map.get(handler.getPriority()).add(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //System.out.println(Arrays.toString(method.getParameterTypes()) +" "+annotation.priority()+" "+annotation.ignoreCancelled());

        }
    }


    public <T extends Event> T run(T event) {
        return (T) event.run();
    }

}
