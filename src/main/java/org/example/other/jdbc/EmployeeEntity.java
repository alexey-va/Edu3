package org.example.other.jdbc;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
public class EmployeeEntity {

    String firstName;
    String lastName;
    LocalDate birthDate;
    LocalDate hireDate;
    Double salary;
    DepartmentEntity department;

}
