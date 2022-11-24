package pl.sg.ip.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sg.application.api.DomainSimple;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeRecord {
    private int id;
    private LocalDate date;
    private int numberOfHours;
    private String description;
    private DomainSimple domain;
}
