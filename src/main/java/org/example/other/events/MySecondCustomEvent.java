package org.example.other.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.EnumMap;
import java.util.List;

@Builder
@ToString
@Setter @Getter
public class MySecondCustomEvent extends Event{

    private static EnumMap<Event.Priority, List<Handler>> HANDLERS_MAP = new EnumMap<>(Event.Priority.class);

    String topic;

    @Override
    public EnumMap<Event.Priority, List<Handler>> getHandlersMap() {
        return HANDLERS_MAP;
    }

}
