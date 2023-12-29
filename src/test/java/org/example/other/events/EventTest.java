package org.example.other.events;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Log4j2
class EventTest {


    @BeforeAll
    public static void init(){
        EventManager.getInstance().registerListener(new MyEventListener());
    }

    @Test
    public void test_run() throws Exception{
        MyCustomEvent event = MyCustomEvent.builder()
                .message("Hello world!")
                .build();
        event = EventManager.getInstance().run(event);
        System.out.println(event);

        MySecondCustomEvent event1 = MySecondCustomEvent.builder()
                .topic("Original topic")
                .build();
        EventManager.getInstance().run(event1);
        System.out.println(event1);
    }

}