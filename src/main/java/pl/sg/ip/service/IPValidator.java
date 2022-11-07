package pl.sg.ip.service;

import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

public class IPValidator {
    private final IntellectualProperty intellectualProperty;

    public IPValidator(IntellectualProperty intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
    }


    public boolean validateDomain(int domainId) {
        return domainId == intellectualProperty.getDomain().getId();
    }

    public boolean validateStartDate(LocalDate startDate) {
        return ofNullable(intellectualProperty.tasks()).orElse(List.of()).stream()
                .map(Task::getTimeRecords)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(TimeRecord::getDate)
                .min(LocalDate::compareTo)
                .map(min -> !min.isBefore(startDate))
                .orElse(true);
    }

    public boolean validateEndDate(LocalDate endDate) {
        return ofNullable(intellectualProperty.tasks()).orElse(List.of()).stream()
                .map(Task::getTimeRecords)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(TimeRecord::getDate)
                .max(LocalDate::compareTo)
                .map(max -> !max.isAfter(endDate))
                .orElse(true);
    }
}
