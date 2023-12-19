package org.example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        LoopedBrokenLine line = new LoopedBrokenLine(new Point(0, 0), new Point(1, 0), new Point(2, 0));
        System.out.println("3.1.2" + line + " " + line.length() + "\n");

        SmartCity smartCity = new SmartCity("A");
        SmartCity smartCity1 = new SmartCity("B");

        smartCity.addRoute(smartCity1, 100);
        System.out.println(smartCity+"\n"+smartCity1+"\n");

        Point3D point3D = new Point3D(1,2,3);
        System.out.println(point3D+"\n");

        System.out.println(new Fraction(3,2).intValue()+" "+new Fraction(3,2).floatValue());


        Bird b1 = new Parrot("ab");
        Bird b2= new Cuckoo();
        Bird b3 = new Sparrow();
        Stream.of(b3,b2,b1).forEach(b -> System.out.println(b.getMessage()));


        System.out.println();

        Figure c = new Circle(new Point(0,0),1);
        Figure sq = new Squar(new Point(1,1), 10);
        Figure rect = new Rectangle(new Point(1,1), 3,4);
        Figure tri = new Triangle(new Point(0,0), new Point(0,3), new Point(4,0));

        Stream.of(c,sq,rect,tri).forEach(f -> System.out.println(f.area()));
        System.out.println();

        AttributedPoint atp = new AttributedPoint(new AttributedPoint.Attribute("x", 1));
        System.out.println(atp+" "+atp.getAttributeValue("x")+" "+atp.getAttributeValue("y"));
        System.out.println();

        System.out.println(
                addNumbers(List.of(new Fraction(1,2), 5.0, 2))
        );

        System.out.println();

        tweet(List.of(b3,b2,b1));

        System.out.println();
        System.out.println(commonArea(List.of(c,sq,rect,tri)));

        System.out.println();
        batchMeow(List.of(new Cat("b"), new Cat("a"), new Dog()));

        System.out.println();
        printLengths(List.of(line, new BrokenLine(new Point(0,0), new Point(0,1))));

        System.out.println();
        System.out.println(new Square(0,0,3).toLine()+" "+new Square(0,0,3).toLine().length());

        System.out.println();
        Chainable sq1 = new Squar(new Point(1,1), 10);
        Chainable rect1 = new Rectangle(new Point(1,1), 3,4);
        System.out.println(getUnion(List.of(sq1, rect1)));
        System.out.println();

        City sd = new SmartCity("D");
        City sa = new SmartCity("A");
        City sb = new City("B");
        City sc = new City("C");
        City se = new City("E");

        sd.addRoute(sb, 1);
        sd.addRoute(se, 1);

        sa.addRoute(sc, 1);
        sa.addRoute(sb, 1);

        se.addRoute(sc, 1);
        sb.addRoute(sc, 1);

        Stream.of(sa,sb,sc,sd,se).forEach(s-> System.out.println(s));
    }

    public static BrokenLine getUnion(Collection<Chainable> chainables){
        return new BrokenLine(chainables.stream()
                .flatMap(c->c.toLine().getPoints().stream())
                .toArray(Point[]::new)
        );
    }

    public static void printLengths(Collection<Measurable> measurables){
        measurables.forEach(m -> System.out.println(m.length()));
    }

    public static void batchMeow(Collection<Meowable> meowables){
        meowables.forEach(Meowable::meow);
    }

    public static double commonArea(Collection<Figure> figures){
        return figures.stream()
                .mapToDouble(Figure::area)
                .sum();
    }

    public static void tweet(Collection<Bird> birds){
        birds.forEach(b -> System.out.println(b.getMessage()));
    }

    public static double addNumbers(Collection<Number> numbers){
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    static class Dog implements Meowable{
        @Override
        public void meow(){
            System.out.println("Bark");
        }
    }


    static class LoopedBrokenLine extends BrokenLine implements Measurable {
        public LoopedBrokenLine(Point... points) {
            super(points);
        }

        @Override
        public double length() {
            return super.length() +
                    this.getPoints().get(0)
                            .distanceTo(this.getPoints().get(getPoints().size() - 1));
        }


    }

    static class SmartCity extends City{
        public SmartCity(String name){
            super(name);
        }

        @Override
        public void addRoute(City target, int price) {
            super.addRoute(target, price);
            target.getRouteTo(this).ifPresentOrElse(
                    (s) -> {},
                    () -> target.addRoute(this, price)
            );
        }
    }

    static class Point3D extends Point{

        double z;

        public Point3D(double x, double y, double z) {
            super(x, y);
            this.z=z;
        }
        public Point3D(double x, double y){
            this(x,y,0);
        }


        @Override
        public String toString() {
            return "Point3D{" +
                    "z=" + z +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }


}