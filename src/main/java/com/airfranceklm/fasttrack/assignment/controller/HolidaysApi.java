package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.dto.HolidayDto;
import com.airfranceklm.fasttrack.assignment.models.Holiday;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/holidays")
public class HolidaysApi {

    private final HolidayService holidayService;

    public HolidaysApi(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public ResponseEntity<List<HolidayDto>> getHolidaysByEmployeeId(@RequestParam String employeeId) {
        return new ResponseEntity<>(holidayService.getHolidaysByEmployeeId(employeeId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Holiday> createHoliday(@Valid @RequestBody Holiday holiday) {
        return new ResponseEntity<>(holidayService.scheduleHoliday(holiday), HttpStatus.CREATED);
    }

    @DeleteMapping("/{holidayId}")
    public ResponseEntity<Void> cancelHoliday(@PathVariable int holidayId) {
        holidayService.cancelHoliday(holidayId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{holidayId}")
    public ResponseEntity<Holiday> updateHoliday(
            @PathVariable int holidayId,
            @Valid @RequestBody Holiday updatedHoliday
    ) {
        return new ResponseEntity<>(
                holidayService.updateHoliday(holidayId, updatedHoliday),
                HttpStatus.OK
        );
    }
}
