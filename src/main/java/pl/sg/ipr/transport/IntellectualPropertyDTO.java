package pl.sg.ipr.transport;

import pl.sg.application.transport.DomainTO;

import java.time.LocalDate;

public record IntellectualPropertyDTO(LocalDate startDate, LocalDate endDate, String description, DomainTO domain) {
}
