package com.airfranceklm.fasttrack.assignment.repository;

import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.models.Employee;
import com.airfranceklm.fasttrack.assignment.models.Holiday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        Employee employee1 = new Employee("klm012345", "Test", null);
        Employee employee2 = new Employee("klm012346", "Test2", null);

        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);
        Holiday holiday1 = createHoliday(employee1, LocalDate.of(2025, 9, 10), LocalDate.of(2025, 9, 15));
        Holiday holiday2 = createHoliday(employee1, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 5));
        Holiday holiday3 = createHoliday(employee2, LocalDate.of(2025, 9, 20), LocalDate.of(2025, 9, 25));

        holidayRepository.saveAll(List.of(holiday1, holiday2, holiday3));
    }

    @Test
    void findByEmployeeIdTest() {
        List<Holiday> holidaysFirstEmployee = holidayRepository.findByEmployee_EmployeeId("klm012345");
        List<Holiday> holidaysSecondEmployee = holidayRepository.findByEmployee_EmployeeId("klm012346");
        //normaal gesproken uitgebreider testen of alle velden goed zijn gevuld
        assertThat(holidaysFirstEmployee).hasSize(2);
        assertThat(holidaysSecondEmployee).hasSize(1);
    }

    private Holiday createHoliday(Employee employee, LocalDate start, LocalDate end) {
        Holiday holiday = new Holiday();
        holiday.setHolidayLabel("Test Holiday");
        holiday.setEmployee(employee);
        holiday.setStatus(HolidayStatus.REQUESTED);
        holiday.setStartOfHoliday(start.atStartOfDay(ZoneId.of("Europe/Amsterdam")).toOffsetDateTime());
        holiday.setEndOfHoliday(end.atTime(18, 0).atZone(ZoneId.of("Europe/Amsterdam")).toOffsetDateTime());
        return holiday;
    }
}
