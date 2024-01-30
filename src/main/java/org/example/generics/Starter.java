package org.example.generics;

import java.util.*;
import java.util.function.*;

public class Starter {

    public static void main(String[] args) throws Exception {
        System.out.println(DataStream.of("Asd", "asd", "dsd", "DDD")
                .filter(s -> !s.isEmpty())
                .filter(s -> Character.isUpperCase(s.charAt(0)))
                .map(String::length)
                .reduce(Integer::sum, 0));

        System.out.println(DataStream.of(0, 0, 1, 2, -1, -2)
                .filter(i -> i != 0)
                .collect(() -> List.of(new ArrayList<>(), new ArrayList<>()),
                        (list, element) -> list.get(element > 0 ? 1 : 0).add(element)));

        System.out.println(DataStream.of("asd", "asd1", "1234", "423455")
                .filter(s -> s.matches("^-?\\d+(\\.\\d+)?$"))
                .map(Double::parseDouble)
                .reduce(Double::sum, 0.0));
    }
}

/*
1) статический метод инициализации
2) доделать метод collect
3) опционально: переделать текущую реализацию в ОО стиле
4) выполнить задачки c применением нашего DataStream
4.1 есть список строк, отобрать те строки, которые начинаются с большой буквы, и посчитать общую длину оставшихся строк
4.2 есть список чисел, удалить нули, результат разложить по двум спискам: в одном отрицательные значения, в другом положительные
4.3 дан список строк, некоторые из них числа. Удалить все строки НЕ являющиеся числами, преобразовать строки в числа, и получить их сумму
 */
class DataStream<T> {
    private final List<T> list;
    private final List<Action> actions = new ArrayList<>();

    private DataStream(List<T> list) {
        this.list = list;
    }

    @SafeVarargs
    public static <T> DataStream<T> of(T... args) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, args);
        return new DataStream<>(list);
    }
    public static <T> DataStream<T> of(Collection<T> collection) {
        List<T> list = new ArrayList<>();
        list.addAll(collection);
        return new DataStream<>(list);
    }


    public <R> DataStream<R> map(Function<T, R> function) {
        actions.add(MapAction.of(function));
        return (DataStream<R>) this;
    }

    public DataStream<T> filter(Predicate<T> rule) {
        actions.add(FilterAction.of(rule));
        return this;
    }

    public <R> R collect(Supplier<R> init, BiConsumer<R, T> op) {
        R accumulator = init.get();
        for (Object t : list) {
            boolean pass = true;
            for (Action action : actions) {
                Optional o = action.apply(t);
                if (o.isEmpty()){
                    pass = false;
                    break;
                }
                t = o.get();
            }
            if(pass) op.accept(accumulator, (T)t);
        }
        return accumulator;
    }

    public T reduce(BinaryOperator<T> operator, T start) {
        for (Object t : list) {
            Object temp = t;
            boolean pass = true;
            for (Action action : actions) {
                Optional o = action.apply(temp);
                if (o.isEmpty()){
                    pass = false;
                    break;
                }
                temp= o.get();
            }
            if(pass) start = operator.apply(start, (T)temp);
        }
        return start;
    }
}

interface Action {
    Optional apply(Object t);
}


class FilterAction implements Action {
    Predicate check;

    private FilterAction(Predicate predicate) {
        this.check = predicate;
    }

    public static FilterAction of(Predicate predicate) {
        return new FilterAction(predicate);
    }

    public Optional apply(Object t) {
        if (check.test(t)) return Optional.ofNullable(t);
        return Optional.empty();
    }
}

class MapAction implements Action {
    Function function;

    private MapAction(Function function) {
        this.function = function;
    }

    public static MapAction of(Function function) {
        return new MapAction(function);
    }

    public Optional apply(Object t) {
        return Optional.ofNullable(function.apply(t));
    }
}