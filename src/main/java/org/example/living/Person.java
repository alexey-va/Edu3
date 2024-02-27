package org.example.living;

import lombok.*;
import org.example.entities.Name;

@NoArgsConstructor
public class Person {

    @Getter
    private Name name;
    @Getter
    @Setter
    private int height;
    @Getter
    private Person father;



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
        if (this.getName().getN1() == null && this.father.name.getN1() != null) this.name.setN1(this.father.name.getN1());
        if (this.getName().getN3() == null && this.father.name.getN2() != null) this.name.setN3(this.father.name.getN2() + "ович");
    }

    @Override
    public String toString() {
        return name + ", " + height;
    }
}
