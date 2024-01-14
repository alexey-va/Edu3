package org.example.generics;

import org.example.entities.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparableTest {

    @Test
    void testStudentComp(){
        Student student = new Student("Ivan", 2,3,4,5);
        Student student2 = new Student("Ivan2", 4,3,4,5);
        assertTrue(student.compareTo(student2) < 0);
    }

}