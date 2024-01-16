package org.example.other.jdbc;

import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PostgresDAOTest {


    @BeforeAll
    static void populateTablesWithRandom() {
        PostgresDAO dao = new PostgresDAO("jdbc:postgresql://rus-crafting.ru:5432/spring", "spring", "123456q");
        dao.dropTables();
        dao.createTables();
        List<DepartmentEntity> departments = new ArrayList<>();
        List<EmployeeEntity> employees = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            DepartmentEntity department = new DepartmentEntity(LoremIpsum.getInstance().getCity());
            department.id = dao.putDepartment(department);
            departments.add(department);
            EmployeeEntity employee = EmployeeEntity.builder()
                    .firstName(LoremIpsum.getInstance().getFirstName())
                    .lastName(LoremIpsum.getInstance().getLastName())
                    .birthDate(LoremIpsum.getInstance().getPriorDate(Duration.of(100, ChronoUnit.DAYS)).toLocalDate())
                    .hireDate(LoremIpsum.getInstance().getPriorDate(Duration.of(100, ChronoUnit.DAYS)).toLocalDate())
                    .salary(new Random().nextDouble() * 1000)
                    .department(departments.get(new Random().nextInt(departments.size())))
                    .build();
            dao.putEmployee(employee);
            employees.add(employee);
        }
    }

    @Test
    void testGet() throws SQLException {

        PostgresDAO dao = new PostgresDAO("jdbc:postgresql://rus-crafting.ru:5432/spring", "spring", "123456q");

        try(var connection = dao.dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("""
                SELECT * FROM employees INNER JOIN departments on department_id=departments.id
                """);

            ResultSet set = statement.executeQuery();
            for(int i=1;i<=set.getMetaData().getColumnCount(); i++){
                System.out.print(set.getMetaData().getColumnName(i)+"("+set.getMetaData().getColumnTypeName(i)+") ");
            }
            while (set.next()){
                for(int i=1;i<=set.getMetaData().getColumnCount(); i++){
                    System.out.print(set.getObject(i)+" ");
                }
                System.out.println();
            }
        }
    }

}