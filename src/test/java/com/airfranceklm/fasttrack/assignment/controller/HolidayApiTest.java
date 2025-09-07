package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.dto.HolidayDto;
import com.airfranceklm.fasttrack.assignment.models.Employee;
import com.airfranceklm.fasttrack.assignment.models.Holiday;
import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidaysApi.class)
class HolidayApiJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidayService holidayService;

    @Test
    void createHolidayTest() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setHolidayLabel("Test");
        holiday.setEmployee(new Employee("klm012345", "Test", null));
        holiday.setStartOfHoliday(OffsetDateTime.parse("2025-09-15T08:00:00+02:00"));
        holiday.setEndOfHoliday(OffsetDateTime.parse("2025-09-20T18:00:00+02:00"));
        holiday.setStatus(HolidayStatus.DRAFT);

        when(holidayService.scheduleHoliday(any(Holiday.class))).thenReturn(holiday);

        String holidayJson = """
            {
              "holidayLabel": "Test Holiday",
              "startOfHoliday": "2025-09-15T08:00:00+02:00",
              "endOfHoliday": "2025-09-20T18:00:00+02:00",
              "employee": {
                "employeeId": "klm012345"
              }
            }
            """;

        mockMvc.perform(post("/api/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(holidayJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.holidayLabel").value("Test"));
    }

    @Test
    void getHolidayByEmployeeIdTest() throws Exception {
        HolidayDto holiday = new HolidayDto();
        holiday.setHolidayLabel("Test");
        holiday.setStartOfHoliday(OffsetDateTime.parse("2025-09-15T08:00:00+02:00"));
        holiday.setEndOfHoliday(OffsetDateTime.parse("2025-09-20T18:00:00+02:00"));
        holiday.setStatus(HolidayStatus.DRAFT);

        when(holidayService.getHolidaysByEmployeeId("klm012345"))
                .thenReturn(List.of(holiday));

        mockMvc.perform(get("/api/holidays")
                        .param("employeeId", "klm012345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].holidayLabel").value("Test"));
    }
}
