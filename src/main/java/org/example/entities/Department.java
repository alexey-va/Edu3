package org.example.entities;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Department {


    String name;
    Employee head;
    Set<Employee> employeeList = new HashSet<>();

    public Department(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEmployees(@NonNull Employee... employee){
        employeeList.addAll(Arrays.asList(employee));
        for(Employee e : employee){
            if(e.getDepartment() != this) e.setDepartment(this);
        }
    }

    public void removeEmployees(@NonNull Employee... employee){
        Arrays.asList(employee).forEach(employeeList::remove);
        for(Employee e : employee){
            if(head == e) head = null;
            if(e.getDepartment() == this) e.setDepartment(null);
        }
    }

    public void promoteToHead(@NonNull Employee head) {
        addEmployees(head);
        this.head = head;
    }

    public void demoteHead(){
        if(head == null) return;
        head = null;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", head=" + head.getName() +
                ", members="+employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(",","[","]")) +
                '}';
    }
}
