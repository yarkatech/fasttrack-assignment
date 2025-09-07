package com.airfranceklm.fasttrack.assignment.mappers;

import com.airfranceklm.fasttrack.assignment.dto.HolidayDto;
import com.airfranceklm.fasttrack.assignment.models.Holiday;

public class HolidayMapper {


    public static HolidayDto toDto(Holiday holiday) {
        if (holiday == null) return null;
        return new HolidayDto(
                holiday.getHolidayId(),
                holiday.getHolidayLabel(),
                holiday.getStartOfHoliday(),
                holiday.getEndOfHoliday(),
                holiday.getStatus()
        );
    }
}
