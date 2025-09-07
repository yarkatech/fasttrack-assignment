package com.airfranceklm.fasttrack.assignment.dto;

import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDto {

    private Integer holidayId;
    private String holidayLabel;
    private OffsetDateTime startOfHoliday;
    private OffsetDateTime endOfHoliday;
    private HolidayStatus status;
}
