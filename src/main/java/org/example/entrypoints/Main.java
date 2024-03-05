package org.example.entrypoints;

import lombok.extern.log4j.Log4j2;
import org.example.geometry.*;
import org.example.living.Bird;
import org.example.living.Meowable;
import org.example.other.ChatJippity;

import java.util.Collection;
import java.util.Scanner;

import static java.lang.Math.pow;
import static org.example.reflections.Utils.*;

@Log4j2
public class Main {
    private static final int TIMEOUT_SECONDS = 5;

    public static void main(String[] args) throws Exception {
        ChatJippity chatJippity = new ChatJippity();
        Scanner scanner = new Scanner(System.in);
        while (true){
            chatJippity.sendOpenaiApiRequest(scanner.nextLine());
        }
    }


    public static BrokenLine getUnion(Collection<Chainable> chainables) {
        return new BrokenLine(chainables.stream()
                .flatMap(c -> c.toLine().getPoints().stream())
                .toArray(Point[]::new)
        );
    }

    public static void printLengths(Collection<Measurable> measurables) {
        measurables.forEach(m -> System.out.println(m.length()));
    }

    public static void batchMeow(Collection<Meowable> meowables) {
        meowables.forEach(Meowable::meow);
    }

    public static double commonArea(Collection<Figure> figures) {
        return figures.stream()
                .mapToDouble(Figure::area)
                .sum();
    }

    public static void tweet(Collection<Bird> birds) {
        birds.forEach(b -> System.out.println(b.getMessage()));
    }

    public static double addNumbers(Collection<Number> numbers) {
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    public static double myPow(String a, String b) {
        return pow(Double.parseDouble(a), Double.parseDouble(b));
    }


}