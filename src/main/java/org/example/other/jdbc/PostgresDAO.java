package org.example.other.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class PostgresDAO {

    DataSource dataSource;

    public PostgresDAO(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("springHikariCP");

        config.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        config.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        config.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        config.addDataSourceProperty("dataSource.useLocalSessionState", "true");
        config.addDataSourceProperty("dataSource.rewriteBatchedStatements", "true");
        config.addDataSourceProperty("dataSource.cacheResultSetMetadata", "true");
        config.addDataSourceProperty("dataSource.cacheServerConfiguration", "true");
        config.addDataSourceProperty("dataSource.elideSetAutoCommits", "true");
        this.dataSource = new HikariDataSource(config);
    }

    public void close() {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void dropTables() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmEmployees = connection.prepareStatement("""
                    DROP TABLE IF EXISTS employees CASCADE;
                    DROP TABLE IF EXISTS departments CASCADE;
                    """);
            stmEmployees.execute();
        }

    }

    @SneakyThrows
    public void createTables() {
        try (var connection = dataSource.getConnection()) {
            PreparedStatement stmEmployees = connection.prepareStatement("""
                      
                          CREATE TABLE IF NOT EXISTS employees (
                          id SERIAL PRIMARY KEY,
                          first_name varchar(50) NOT NULL,
                          last_name varchar(50) NOT NULL,
                          birth_date DATE,
                          hire_date DATE,
                          salary DECIMAL(10,2),
                          department_id INT
                      );
                                      
                      CREATE TABLE IF NOT EXISTS departments (
                          id SERIAL PRIMARY KEY,
                          name varchar(50),
                          boss_id INT,
                          foreign key (boss_id) references employees(id) on delete set null
                      );
                                      
                      DO $$
                      BEGIN
                          IF NOT EXISTS (
                                  SELECT 1 FROM information_schema.table_constraints
                                  WHERE constraint_name = 'fk_department'
                              ) THEN
                                  alter table employees
                                  add constraint fk_department
                                  foreign key (department_id)
                                  references departments(id) on delete cascade;
                          END IF;
                      END $$;
                        

                    """);

            stmEmployees.execute();
        }
    }

    @SneakyThrows
    private void putEmployee(String firstName, String lastName, LocalDate birthDate, LocalDate hireDate, double salary, int departmentId) {
        try (var connection = dataSource.getConnection()) {


            PreparedStatement stmEmployees = connection.prepareStatement("""
                    INSERT INTO employees (first_name, last_name, birth_date, hire_date, salary, department_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """);
            stmEmployees.setString(1, firstName);
            stmEmployees.setString(2, lastName);
            stmEmployees.setDate(3, Date.valueOf(birthDate));
            stmEmployees.setDate(4, Date.valueOf(hireDate));
            stmEmployees.setDouble(5, salary);
            stmEmployees.setInt(6, departmentId);
            stmEmployees.execute();
        }
    }

    @SneakyThrows
    private int putDepartment(String name, Integer bossId) {
        try (var connection = dataSource.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("""
                     
                         INSERT INTO departments (name, boss_id)
                     VALUES (?, ?);
                        
                    """);
            PreparedStatement get = connection.prepareStatement(
                    """
                            SELECT id from
                                    departments WHERE name=?
                            """);
            stm.setString(1, name);
            stm.setObject(2, bossId);

            //System.out.println(stm);
            stm.executeUpdate();

            get.
                    setString(1, name);
            ResultSet set = get.executeQuery();
            set.next();
            return set.getInt("id");
        }
    }

    public void putEmployee(EmployeeEntity employee) {
        putEmployee(employee.firstName, employee.lastName, employee.birthDate, employee.hireDate, employee.salary, employee.department.id);
    }

    public int putDepartment(DepartmentEntity department) {
        return putDepartment(department.name, department.bossId);
    }

}
