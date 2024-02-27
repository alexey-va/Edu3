package org.example.reflections;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.example.geometry.Line;
import org.example.geometry.Point;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.any;

public class Utils {

    @SneakyThrows
    public static <T extends Point> void lineConnector(Line<T> l1, Line<T> l2) {
        //Field start = Line.class.getDeclaredField("start");
        Field end = Line.class.getDeclaredField("end");
        //start.setAccessible(true);
        end.setAccessible(true);

        end.set(l1, l2.getStart());
    }

    public static List<Field> fieldsOf(Class clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
        if (clazz.getSuperclass() == null) return fields;
        fields.addAll(fieldsOf(clazz.getSuperclass()));
        return fields;
    }

    public static ValidationResponse validate(Object o, Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> m.getParameterTypes()[0].isAssignableFrom(o.getClass()))
                .filter(m -> m.getReturnType() == ValidationResponse.class)
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .map(m -> {
                    try {
                        return (ValidationResponse) m.invoke(null, o);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .filter(vr -> !vr.isValid())
                .findFirst().orElse(new ValidationResponse(true, null));
    }

    @SneakyThrows
    public static <T> T cache(T object) {
        return (T) new ByteBuddy()
                .subclass(object.getClass())
                .method(any())
                .intercept(InvocationHandlerAdapter.of(new CacheInterceptor(object)))
                .make()
                .load(object.getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .newInstance();
    }


}
