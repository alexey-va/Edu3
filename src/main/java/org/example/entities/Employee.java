package org.example.entities;

import lombok.*;

@Getter
@NoArgsConstructor
public class Employee {

    public Employee(String name) {
        this.name = name;
    }

    @Setter
    private String name;
    private Department department;

    public void moveToDepartment(@NonNull Department department){
        Department oldDepartment = this.department;
        if(oldDepartment != null) oldDepartment.removeEmployees(this);
        this.department = department;
        department.addEmployees(this);
    }

    void setDepartment(Department department){
        this.department = department;
    }

    @Override
    public String toString() {
        if (department.head == this)
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", boss of department=" + department.name +
                    '}';
        else return "Employee{" +
                "name='" + name + '\'' +
                ", department=" + department.name +
                ", boss=" + department.head.name +
                '}';
    }
}
