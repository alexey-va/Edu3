package org.example.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testStudentUndo(){
        Student student = new Student("Vasia");

        student.setName("Petya");
        Student.Save save = student.save();

        student.addGrades(2);
        student.addGrades(3,3,3);
        System.out.println(student);

        save.undo();
        System.out.println(student);
    }

}