package org.example.generics.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

public class Starter {

    public static void main(String[] args) throws Exception {
        var i = DataStream.of(1, 2, 3, 4, 5)
                .filter(x -> x > 2)
                .map(x -> x * 2)
                .reduce((a, b) -> a * b, 1);
        System.out.println(i);

        var i2 = DataStream.of(1, 2, 3, 4, 5)
                .filter(x -> x > 3)
                .map(x -> x * -1)
                .collect(() -> new ArrayList<>(), (list, element) -> list.add(element));
        System.out.println(i2);

        //4.1

        var i3 = DataStream.of("Ah", "jdjd", "Gj", "dd", "GH")
                .filter(x -> Character.isUpperCase(x.charAt(0)))
                .reduce((str1, str2) -> str1 + str2, "").length();
        System.out.println(i3);

        //4.2
        var i4 = DataStream.of(-1, -2, 0, 3, -4, 5)
                .filter(x -> x != 0)
                .collect(() -> List.of(new ArrayList<Integer>(), new ArrayList<Integer>()),
                        (lists, element) -> lists.get(element > 0 ? 0 : 1).add(element));
        System.out.println(i4);

        //4.3
        var i5 = DataStream.of("Ah", "3", "5", "dd", "GH")
                .filter(s -> {
                    try {
                        Integer.parseInt(s);
                        return true;
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .map((s) -> Integer.parseInt(s))
                .reduce((a,b) -> a+b, 0);

        System.out.println(i5);



//        var res2 = Stream.of("Ah", "3", "5", "dd", "GH")
//                .filter(s -> {
//                    try {
//                        Integer.parseInt(s);
//                        return true;
//                    } catch (Exception ex) {
//                        return false;
//                    }
//                })
//                .mapToInt(s -> Integer.parseInt(s))
//                .sum();
//
//        System.out.println(res2);


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
    private List<T> list;
    private List<Action> actions = new ArrayList<>();

    private DataStream(List<T> list) {
        this.list = list;
    }

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
                Optional optional = action.apply(t);
                if (optional.isEmpty()) {
                    pass = false;
                    break;
                }
                t = optional.get();
            }
            if (pass) op.accept(accumulator, (T)t);
        }
        return accumulator;
    }

    public T reduce(BinaryOperator<T> operator, T start) {
        for (Object t : list) {
            boolean pass = true;
            for (Action action : actions) {
                Optional<T> optional = action.apply(t);
                if (optional.isEmpty()) {
                    pass = false;
                    break;
                }
                t = optional.get();
            }
            if (pass) start = operator.apply(start, (T)t);
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