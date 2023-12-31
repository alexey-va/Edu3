package org.example.other.events;

import org.example.other.events.base.Event;
import org.example.other.events.base.EventHandler;
import org.example.other.events.base.EventListener;

public class MyEventListener implements EventListener {

    @EventHandler(priority = Event.Priority.HIGH)
    public void onMyCustomEvent(MyCustomEvent event) {
            event.setMessage("Third filter");
    }

    @EventHandler(priority = Event.Priority.LOW)
    public void onMyCustomEvent2(MyCustomEvent event) {
            event.setMessage("First filter");
    }

    @EventHandler(priority = Event.Priority.MEDIUM)
    public void onMyCustomEvent3(MyCustomEvent event) {
            event.setMessage("Second filter");
            event.setCancelled(true);
    }

    @EventHandler(priority = Event.Priority.LOW)
    public void onMyCustomEvent4(MySecondCustomEvent event) {
            event.setTopic("First filter");
    }

    @EventHandler(priority = Event.Priority.MEDIUM)
    public void onMyCustomEvent5(MySecondCustomEvent event) {
            event.setTopic("Second filter");
            event.setCancelled(true);
    }

    @EventHandler(priority = Event.Priority.HIGH, ignoreCancelled = true)
    public void onMyCustomEvent6(MySecondCustomEvent event) {
            event.setTopic("Third filter");
    }


}
