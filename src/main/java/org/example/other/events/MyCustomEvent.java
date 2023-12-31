package org.example.other.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.other.events.base.Event;

@Setter
@Getter
@Builder
public class MyCustomEvent extends Event {
    private String message;


}
