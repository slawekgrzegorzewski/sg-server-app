package pl.sg.ipr.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sg.application.api.DomainSimple;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IntellectualProperty {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private DomainSimple domain;
}