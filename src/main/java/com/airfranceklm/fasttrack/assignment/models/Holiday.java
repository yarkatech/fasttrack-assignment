package com.airfranceklm.fasttrack.assignment.models;

import com.airfranceklm.fasttrack.assignment.enums.HolidayStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holidayId;

    @Column(nullable = false)
    private String holidayLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    private OffsetDateTime startOfHoliday;
    private OffsetDateTime endOfHoliday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayStatus status;
}
