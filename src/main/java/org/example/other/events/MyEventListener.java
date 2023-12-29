package org.example.other.events;

public class MyEventListener implements EventListener{

    @EventHandler
    public void onMyCustomEvent(MyCustomEvent event){
        System.out.println("MyCustomEvent MEDIUM: " + event.getMessage());
        event.setCancelled(true);
    }

    @EventHandler(priority = Event.Priority.LOW)
    public void onMyEvent2(MyCustomEvent event){
        System.out.println("MyCustomEvent LOW: " + event.getMessage());
    }

    @EventHandler(priority = Event.Priority.HIGH, ignoreCancelled = true)
    public void onMyEvent3(MyCustomEvent event){
        event.setCancelled(true);
        System.out.println("MyCustomEvent HIGH: " + event.getMessage());
    }

    @EventHandler(priority = Event.Priority.LOW)
    public void onMyEvent4(MyCustomEvent event){
        System.out.println("MyCustomEvent LOW 2: " + event.getMessage());
    }

    @EventHandler
    public void onDoSmth(MySecondCustomEvent event){
        event.setTopic("I changed it!");
    }

}
