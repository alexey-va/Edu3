package org.example.reflections;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.example.geometry.Line;
import org.example.geometry.Point;
import org.example.reflections.annotations.Cache;
import org.example.reflections.annotations.Default;
import org.example.reflections.annotations.Invoke;
import org.example.reflections.annotations.Validate;

import java.lang.annotation.Annotation;
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

    public static void validate(Collection<Object> objects) {
        for (Object o : objects) {
            Class oClass = o.getClass();
            List<Class> classes = new ArrayList<>();
            Validate validateAnnotation = (Validate) oClass.getAnnotation(Validate.class);
            if (validateAnnotation != null) Collections.addAll(classes, validateAnnotation.value());
            for (Annotation annotation : oClass.getAnnotations()) {
                if (!annotation.annotationType().isAnnotationPresent(Validate.class)) continue;
                Validate validate = annotation.annotationType().getAnnotation(Validate.class);
                Collections.addAll(classes, validate.value());
            }
            classes.stream()
                    .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                    .filter(m -> m.getParameterCount() == 1)
                    .filter(m -> m.getParameterTypes()[0].isAssignableFrom(o.getClass()))
                    .filter(m -> Modifier.isStatic(m.getModifiers()))
                    .forEach(m -> {
                        try {
                            m.invoke(null, o);
                        } catch (Exception e) {
                            Throwable cause = e.getCause();
                            if (cause instanceof ValidationException validationException) throw validationException;
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @SneakyThrows
    public static List<?> cache(List<?> objects) {
        return objects.stream().map(object -> {
            try {
                if(!object.getClass().isAnnotationPresent(Cache.class)) return object;
                return new ByteBuddy()
                        .subclass(object.getClass())
                        .method(any())
                        .intercept(InvocationHandlerAdapter.of(new CacheInterceptor(object)))
                        .make()
                        .load(object.getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                        .getLoaded()
                        .newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @SneakyThrows
    public static Map<String, Object> collect(Collection<Class> classes) {
        Map<String, Object> result = new HashMap<>();
        for (Class clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getParameterCount() != 0) continue;
                if (method.getReturnType() == void.class) continue;
                if (!method.isAnnotationPresent(Invoke.class)) continue;
                Object o = null;
                if (!Modifier.isStatic(method.getModifiers())) {
                    Constructor constructor = clazz.getConstructor();
                    o = constructor.newInstance();
                }
                result.put(clazz.getSimpleName() + "." + method.getName(), method.invoke(o));
            }
        }
        return result;
    }

    public static class Defaults {
        public static final String DEFAULT_STRING = "default";
        public static final int DEFAULT_INT = 0;
        public static final double DEFAULT_DOUBLE = 0.0;
        public static final boolean DEFAULT_BOOLEAN = false;
    }

    @SneakyThrows
    public static void reset(Collection<Object> objects) {
        for (Object o : objects) {
            List<Field> fields = fieldsOf(o.getClass());
            // Annotation not on class -> resetting only fields with Default annotation
            if (!o.getClass().isAnnotationPresent(Default.class)) {
                fields.removeIf(f -> !f.isAnnotationPresent(Default.class));
            }

            for (Field f : fields) {
                f.setAccessible(true);
                Default annotation = f.getAnnotation(Default.class);
                Class defaultClass = void.class;
                if (annotation != null) defaultClass = annotation.value();
                if (defaultClass == void.class) defaultClass = f.getType();

                final Class finalDefaultClass = defaultClass;
                Optional<Field> defaultField = Arrays.stream(Defaults.class.getDeclaredFields())
                        .filter(f1 -> f1.getType() == finalDefaultClass)
                        .findFirst();
                if (defaultField.isPresent() && Modifier.isStatic(defaultField.get().getModifiers())) {
                    f.set(o, defaultField.get().get(null));
                } else {
                    if (defaultClass.isEnum()) {
                        f.set(o, defaultClass.getEnumConstants()[0]);
                    } else if (Object.class.isAssignableFrom(defaultClass)) {
                        f.set(o, null);
                    } else if (defaultClass.isArray()) {
                        f.set(o, Array.newInstance(defaultClass.getComponentType(), 0));
                    } else if (defaultClass.isPrimitive()) {
                        f.set(o, 0);
                    } else {
                        System.out.println("Could not find default value for " + f.getName() +
                                " of type " + defaultClass.getName() + ", using default constructor");
                    }
                }
            }
        }
    }
}
