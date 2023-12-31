package org.example.other.events;

import lombok.extern.log4j.Log4j2;
import org.example.other.events.base.EventManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class EventTest {


    @BeforeAll
    public static void init(){
        EventManager.getInstance().registerListener(new MyEventListener());
    }

    @Test
    public void test_cancelling_event() {
        MyCustomEvent myEvent = new MyCustomEvent("Initial message");
        myEvent.run();
        assertEquals("Second filter", myEvent.getMessage());
        assertTrue(myEvent.isCancelled());
    }

    @Test
    public void test_ignoring_cancelled_event() {
        MySecondCustomEvent myEvent = new MySecondCustomEvent("Initial topic");
        myEvent.run();
        assertEquals("Third filter", myEvent.getTopic());
        assertTrue(myEvent.isCancelled());
    }

}