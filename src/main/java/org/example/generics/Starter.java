package org.example.generics;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class Starter {

    public static void main(String[] args) throws Exception {
        var list = List.of(1,2,3,4,5);
        System.out.println(DataStream.of(1, 2, 3, 4, 5)
                .filter(x -> x > 2)
                .map(x -> x * 2)
                .reduce((a, b) -> a + b, 0));

        var list2 = DataStream.of(1,2,3,4,5)
                .filter(x -> x>3)
                .map(x -> x*3)
                .map(x -> x+"a")
                .collect(ArrayList::new,ArrayList::add);
        System.out.println(list2);
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

    public <R> DataStream<R> map(Function<T, R> function) {
        actions.add(MapAction.of(function));
        return (DataStream<R>) this;
    }

    public DataStream<T> filter(Predicate<T> rule) {
        actions.add(PredicateAction.of(rule));
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
            if(pass) start = operator.apply(start, (T)t);
        }
        return start;
    }
}

interface Action<T, R> {
    Optional<R> apply(T t);
}


class PredicateAction<T> implements Action<T, T> {
    Predicate<T> check;

    private PredicateAction(Predicate<T> predicate) {
        this.check = predicate;
    }

    public static <T> PredicateAction<T> of(Predicate<T> predicate) {
        return new PredicateAction<>(predicate);
    }

    public Optional<T> apply(T t) {
        if (check.test(t)) return Optional.ofNullable(t);
        return Optional.empty();
    }
}

class MapAction<T, R> implements Action<T, R> {
    Function<T, R> function;

    private MapAction(Function<T, R> function) {
        this.function = function;
    }

    public static <T, R> MapAction<T, R> of(Function<T, R> function) {
        return new MapAction<>(function);
    }

    public Optional<R> apply(T t) {
        return Optional.ofNullable(function.apply(t));
    }
}