package pl.sg.ip.api;

import java.time.LocalDate;

public record IntellectualPropertyData(LocalDate startDate, LocalDate endDate, String description) {
}
