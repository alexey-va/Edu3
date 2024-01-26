package org.example.other.test;

import lombok.AllArgsConstructor;
import org.example.other.events.base.Event;

@AllArgsConstructor
public class MessageEvent extends Event {

    String topic;
    String key;
    String message;

}
