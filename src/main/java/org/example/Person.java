package org.example;

import lombok.*;

public class Person {

    @Getter
    private Name name;
    @Getter
    @Setter
    private int height;
    @Getter
    private final Person father;



    public Person(@NonNull Name name, int height, Person father) {
        this.name = name;
        this.height = height;
        this.father = father;

        parseFather();
    }

    public Person(@NonNull Name name, int height) {
        this(name, height, null);
    }

    public Person(String firstName, int height){
        this(new Name(firstName), height, null);
    }

    public Person(String firstName, int height, Person father){
        this(new Name(firstName), height, father);
    }

    public void parseFather() {
        if (father == null || father.name == null) return;
        if (this.name.n1 == null && this.father.name.n1 != null) this.name.n1 = this.father.name.n1;
        if (this.name.n3 == null && this.father.name.n2 != null) this.name.n3 = this.father.name.n2 + "ович";
    }

    @Override
    public String toString() {
        return name + ", " + height;
    }
}
