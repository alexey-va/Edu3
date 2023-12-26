package org.example.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    Employee employee ;
    Employee employee2;
    Department department ;

    @BeforeEach
    void setUp() {
        employee = new Employee("Иван");
        employee2 = new Employee("Петр");
        department = new Department("Отдел");
    }

    @Test
    void moveToDepartment() {
        employee.moveToDepartment(department);
        assertEquals(employee.getDepartment(), department);
        assertTrue(department.getEmployeeList().contains(employee));
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
    }

}