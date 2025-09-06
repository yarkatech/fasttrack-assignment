package com.airfranceklm.fasttrack.assignment.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Pattern(regexp = "^klm[0-9]{6}$")
    @Column(nullable = false)
    private String employeeId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Holiday> holidays;
}
