package org.example.living;

import org.example.reflections.PersonTests;
import org.example.reflections.annotations.Validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Validate({PersonTests.class})
public @interface AValidate {
}
