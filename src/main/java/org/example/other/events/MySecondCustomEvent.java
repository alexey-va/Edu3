package org.example.other.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.other.events.base.Event;

@Builder
@ToString
@Setter @Getter
public class MySecondCustomEvent extends Event {

    String topic;

}
