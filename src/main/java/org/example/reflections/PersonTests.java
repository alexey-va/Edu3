package org.example.reflections;

import org.example.entities.Name;
import org.example.living.Person;

public class PersonTests {

    public static ValidationResponse validateName(Person person){
        Name name = person.getName();
        if(name == null)
            return new ValidationResponse(false, "Name cant be null!");
        if(name.getN1() == null || name.getN1().isEmpty())
            return new ValidationResponse(false, "First name cant be empty!");

        return new ValidationResponse(true, null);
    }

    public static ValidationResponse validateHeight(Person person){
        if(person.getHeight() < 100)
            return new ValidationResponse(false, "Height cant be lower than 100cm!");
        if(person.getHeight() > 250)
            return new ValidationResponse(false, "Height cant be higher than 250cm!");
        return new ValidationResponse(true, null);
    }

}
