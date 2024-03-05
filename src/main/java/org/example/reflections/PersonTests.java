package org.example.reflections;

import org.example.entities.Name;
import org.example.living.Person;

public class PersonTests {

    public static void validateName(Person person){
        Name name = person.getName();
        if(name == null)
            throw new ValidationException( "Name cant be null!");
        if(name.getN1() == null || name.getN1().isEmpty())
            throw new ValidationException("First name cant be empty!");
    }

    public static void validateHeight(Person person){
        if(person.getHeight() < 100)
            throw new ValidationException("Height cant be lower than 100cm!");
        if(person.getHeight() > 250)
            throw new ValidationException( "Height cant be higher than 250cm!");

    }

}
