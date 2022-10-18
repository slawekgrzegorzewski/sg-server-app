package pl.sg.ipr.transport;

import java.time.LocalDate;

public record IntellectualPropertyCreateData(LocalDate startDate, LocalDate endDate, String description) {
}
