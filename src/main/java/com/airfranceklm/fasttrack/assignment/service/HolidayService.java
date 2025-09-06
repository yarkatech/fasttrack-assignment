package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.models.Holiday;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public List<Holiday> getHolidaysByEmployeeId(String employeeId) {
        return holidayRepository.findByEmployee_EmployeeId(employeeId);
    }

    public void cancelHoliday(int holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Holiday with ID " + holidayId + " does not exist."));

        validateCancellationWorkingDays(holiday.getStartOfHoliday().toLocalDate());

        holidayRepository.delete(holiday);
    }

    public Holiday scheduleHoliday(Holiday holiday) {
        validateHolidayDates(holiday);
//        checkForHolidayOverlap(holiday);
        if (holiday.getStatus() == null) {
            holiday.setStatus(HolidayStatus.DRAFT);
        }

        return holidayRepository.save(holiday);
    }

//    private void checkForHolidayOverlap(Holiday holiday) {
//        LocalDate startDate = holiday.getStartOfHoliday().toLocalDate();
//        LocalDate endDate = holiday.getEndOfHoliday().toLocalDate();
//
//        //Normaal gesproken een betere query van maken
//        List<Holiday> existingHolidays = holidayRepository.findAll();
//
//        boolean conflict = existingHolidays.stream().anyMatch(existing -> {
//            LocalDate existingStart = existing.getStartOfHoliday().toLocalDate();
//            LocalDate existingEnd = existing.getEndOfHoliday().toLocalDate();
//            return !endDate.isBefore(existingStart) && !startDate.isAfter(existingEnd);
//        });
//
//        if (conflict) {
//            throw new IllegalArgumentException(
//                    "The new holiday overlaps with an existing holiday."
//            );
//        }
//    }

    private void validateHolidayDates(Holiday newHoliday) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = newHoliday.getStartOfHoliday().toLocalDate();
        LocalDate endDate = newHoliday.getEndOfHoliday().toLocalDate();

        if (countWorkingDays(today, startDate) < 5) {
            throw new IllegalArgumentException(
                    "A holiday must be scheduled at least 5 working days before the start date."
            );
        }

        String employeeId = newHoliday.getEmployee().getEmployeeId();
        List<Holiday> existingHolidays = getHolidaysByEmployeeId(employeeId);

        boolean conflictingHoliday = existingHolidays.stream().anyMatch(existing -> {
            LocalDate existingStart = existing.getStartOfHoliday().toLocalDate();
            LocalDate existingEnd = existing.getEndOfHoliday().toLocalDate();

            int gapBefore = countWorkingDays(existingEnd.plusDays(1), startDate);
            int gapAfter = countWorkingDays(endDate.plusDays(1), existingStart);

            return (gapBefore < 3 && startDate.isAfter(existingEnd))
                    || (gapAfter < 3 && endDate.isBefore(existingStart));
        });

        if (conflictingHoliday) {
            throw new IllegalArgumentException(
                    "There must be at least 3 working days between holidays for the same employee."
            );
        }
    }

    private void validateCancellationWorkingDays(LocalDate startDate) {
        if (countWorkingDays(LocalDate.now(), startDate) < 5) {
            throw new IllegalArgumentException(
                    "A holiday can only be cancelled at least 5 working days before the start date."
            );
        }
    }

    private int countWorkingDays(LocalDate from, LocalDate to) {
        int workingDays = 0;
        LocalDate date = from;

        while (date.isBefore(to)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        return workingDays;
    }
}