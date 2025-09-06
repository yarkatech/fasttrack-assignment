package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
