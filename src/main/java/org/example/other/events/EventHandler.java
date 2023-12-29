package org.example.other.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    Event.Priority priority() default Event.Priority.MEDIUM;
    boolean ignoreCancelled() default false;
}
