package org.example.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testStudentUndo(){
        Student student = new Student("Vasia");
        Student.Save save = student.save();
        student.setName("Petya");



        student.addGrades(2);
        student.addGrades(3,3,3);
        student.removeGrade(3);
        System.out.println(student);

        System.out.println(save.fromSave());
        System.out.println(student);


    }

}