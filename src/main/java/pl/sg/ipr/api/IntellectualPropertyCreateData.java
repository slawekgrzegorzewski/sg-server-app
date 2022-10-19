package pl.sg.ipr.api;

import java.time.LocalDate;

public record IntellectualPropertyCreateData(LocalDate startDate, LocalDate endDate, String description) {
}
