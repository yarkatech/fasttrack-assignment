package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.models.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
    List<Holiday> findByEmployee_EmployeeId(String employeeId);
}
