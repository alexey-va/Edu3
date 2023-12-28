package org.example.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    Employee employee ;
    Employee employee2;
    Department department ;
    Department department2;

    @BeforeEach
    void setUp() {
        employee = new Employee("Иван");
        employee2 = new Employee("Петр");
        department = new Department("Отдел");
        department2 = new Department("Отдел2");
    }

    @Test
    void moveToDepartment() {
        employee.moveToDepartment(department);
        assertEquals(employee.getDepartment(), department);
        assertTrue(department.getEmployeeList().contains(employee));

        employee.moveToDepartment(department2);
        assertEquals(employee.getDepartment(), department2);
        assertTrue(department2.getEmployeeList().contains(employee));
        assertFalse(department.getEmployeeList().contains(employee));
    }

    @Test
    void setDepartment() {
        employee.setDepartment(department);
        assertEquals(employee.getDepartment(), department);

        // Проверка на то, что сотрудник не добавился в список сотрудников отдела
        assertFalse(department.getEmployeeList().contains(employee));
    }

    @Test
    void testHead(){
        department.promoteToHead(employee);
        assertEquals(employee, department.getHead());
        assertEquals(department, employee.getDepartment());


        department.promoteToHead(employee2);
        assertEquals(employee2, department.getHead());
        assertEquals(department, employee2.getDepartment());
        assertEquals(department, employee.getDepartment());

        department.promoteToHead(employee);
        employee.moveToDepartment(department2);
        assertFalse(department.getEmployeeList().contains(employee));
        assertTrue(department2.getEmployeeList().contains(employee));
        assertNotSame(department.getHead(), employee);
    }


}