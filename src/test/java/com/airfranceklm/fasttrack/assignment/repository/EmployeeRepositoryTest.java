package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.models.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSaveAndGetEmployee() {
        Employee employee = new Employee("klm012345", "Yannick Test", null);

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee.getEmployeeId()).isEqualTo("klm012345");

        Employee dbEmployee = employeeRepository.findById("klm012345").orElse(null);
        assertThat(dbEmployee).isNotNull();
        assertThat(dbEmployee.getName()).isEqualTo("Yannick Test");
    }

    @Test
    void testFindAllEmployees() {
        Employee employee1 = new Employee("klm012345", "Yannick Test1", null);
        Employee employee2 = new Employee("klm012346", "Yannick Test2", null);
        Employee employee3 = new Employee("klm012347", "Yannick Test3", null);

        employeeRepository.saveAll(List.of(employee1, employee2, employee3));

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(3);
    }
}
