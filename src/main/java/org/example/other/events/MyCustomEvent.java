package org.example.other.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.List;

@Setter
@Getter
@Builder
public class MyCustomEvent extends Event{
    private static EnumMap<Priority, List<Handler>> HANDLERS_MAP = new EnumMap<>(Priority.class);

    private String message;

    @Override
    public EnumMap<Priority, List<Handler>> getHandlersMap() {
        return HANDLERS_MAP;
    }
}
