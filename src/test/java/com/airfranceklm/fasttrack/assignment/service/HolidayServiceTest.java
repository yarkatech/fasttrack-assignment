package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.models.Employee;
import com.airfranceklm.fasttrack.assignment.models.Holiday;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HolidayServiceTest {

    private HolidayRepository holidayRepository;
    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        holidayRepository = mock(HolidayRepository.class);
        holidayService = new HolidayService(holidayRepository);
    }

    private Holiday createHoliday(String employeeId, LocalDate start, LocalDate end) {
        Holiday holiday = new Holiday();
        holiday.setHolidayLabel("Test Holiday");
        holiday.setEmployee(new Employee(employeeId, "Test", null));
        holiday.setStartOfHoliday(start.atStartOfDay(ZoneId.of("Europe/Amsterdam")).toOffsetDateTime());
        holiday.setEndOfHoliday(end.atTime(18, 0).atZone(ZoneId.of("Europe/Amsterdam")).toOffsetDateTime());
        return holiday;
    }

    @Test
    void saveHolidayTest() {
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(5);

        Holiday holiday = createHoliday("klm012345", startDate, endDate);

        when(holidayRepository.findByEmployee_EmployeeId("klm012345"))
                .thenReturn(Collections.emptyList());
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);

        Holiday result = holidayService.scheduleHoliday(holiday);

        assertNotNull(result);
        assertEquals(HolidayStatus.DRAFT, result.getStatus());

        verify(holidayRepository, times(1)).save(holiday);
    }

    @Test
    void saveHolidayNotEnoughWorkingDaysInAdvanceTest() {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = startDate.plusDays(5);

        Holiday holiday = createHoliday("klm012345", startDate, endDate);

        when(holidayRepository.findByEmployee_EmployeeId("klm012345"))
                .thenReturn(Collections.emptyList());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> holidayService.scheduleHoliday(holiday)
        );

        assertTrue(ex.getMessage().contains("5 working days"));
    }

    @Test
    void cancelHolidayTest() {
        LocalDate startDate = LocalDate.now().plusDays(10);
        Holiday holiday = createHoliday("klm012345", startDate, startDate.plusDays(2));

        when(holidayRepository.findById(1)).thenReturn(Optional.of(holiday));

        holidayService.cancelHoliday(1);

        verify(holidayRepository, times(1)).delete(holiday);
    }

    @Test
    void cancelHolidayWithingFiveWorkingDaysTest() {
        LocalDate startDate = LocalDate.now().plusDays(2);
        Holiday holiday = createHoliday("klm012345", startDate, startDate.plusDays(2));

        when(holidayRepository.findById(1)).thenReturn(Optional.of(holiday));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> holidayService.cancelHoliday(1)
        );

        assertTrue(ex.getMessage().contains("cancelled at least 5 working days"));
        verify(holidayRepository, never()).delete(any());
    }

    @Test
    void holidayNotFoundTest() {
        when(holidayRepository.findById(9)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> holidayService.cancelHoliday(9)
        );

        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void updateHolidayTest() {
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = startDate.plusDays(5);

        Holiday existing = createHoliday("klm012345", startDate, endDate);
        existing.setHolidayId(1);
        existing.setStatus(HolidayStatus.DRAFT);

        Holiday updated = createHoliday("klm012345", startDate.plusDays(1), endDate.plusDays(1));
        updated.setHolidayLabel("Updated Holiday");

        when(holidayRepository.findById(1)).thenReturn(Optional.of(existing));
        when(holidayRepository.findByEmployee_EmployeeId("klm012345")).thenReturn(Collections.emptyList());
        when(holidayRepository.save(any(Holiday.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Holiday result = holidayService.updateHoliday(1, updated);

        assertNotNull(result);
        assertEquals("Updated Holiday", result.getHolidayLabel());
        assertEquals(startDate.plusDays(1), result.getStartOfHoliday().toLocalDate());
        assertEquals(endDate.plusDays(1), result.getEndOfHoliday().toLocalDate());

        verify(holidayRepository, times(1)).save(existing);
    }
}
