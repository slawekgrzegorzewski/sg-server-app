package pl.sg.ip.service;

import org.junit.jupiter.api.Test;
import pl.sg.application.model.Domain;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IPValidatorTest {

    @Test
    void validateDomain() {
        //given
        Domain d = new Domain(1, "name");
        IntellectualProperty ip = new IntellectualProperty();
        ip.setDomain(d);
        IPValidator validator = new IPValidator(ip);
        //when, then
        assertTrue(validator.validateDomain(1));
        assertFalse(validator.validateDomain(2));
        assertFalse(validator.validateDomain(-1));
        assertFalse(validator.validateDomain(0));
    }

    @Test
    void validateStartDate() {
        //given
        LocalDate now = LocalDate.now();

        TimeRecord timeRecord1 = new TimeRecord();
        timeRecord1.setDate(now);
        TimeRecord timeRecord2 = new TimeRecord();
        timeRecord2.setDate(now.minusDays(2));
        Task task = new Task();
        task.setTimeRecords(List.of(timeRecord1, timeRecord2));
        IntellectualProperty ip = new IntellectualProperty();
        ip.setTasks(List.of(task));
        IPValidator validator = new IPValidator(ip);
        //when, then
        assertTrue(validator.validateStartDate(now.minusDays(3)));
        assertTrue(validator.validateStartDate(now.minusDays(2)));
        assertFalse(validator.validateStartDate(now.minusDays(1)));
        assertFalse(validator.validateStartDate(now));
        assertFalse(validator.validateStartDate(now.plusDays(1)));
    }

    @Test
    void validateStartDateWhenNoTimeRecords() {
        //given
        LocalDate now = LocalDate.now();

        IntellectualProperty ip = new IntellectualProperty();
        ip.setTasks(List.of(new Task()));
        IPValidator validator = new IPValidator(ip);
        //when, then
        assertTrue(validator.validateStartDate(now.minusDays(3)));
        assertTrue(validator.validateStartDate(now.minusDays(2)));
        assertTrue(validator.validateStartDate(now.minusDays(1)));
        assertTrue(validator.validateStartDate(now));
        assertTrue(validator.validateStartDate(now.plusDays(1)));
    }

    @Test
    void validateStartDateWhenNoTask() {
        //given
        LocalDate now = LocalDate.now();


        IPValidator validator = new IPValidator(new IntellectualProperty());
        //when, then
        assertTrue(validator.validateStartDate(now.minusDays(3)));
        assertTrue(validator.validateStartDate(now.minusDays(2)));
        assertTrue(validator.validateStartDate(now.minusDays(1)));
        assertTrue(validator.validateStartDate(now));
        assertTrue(validator.validateStartDate(now.plusDays(1)));
    }

    @Test
    void validateEndDate() {
        //given
        LocalDate now = LocalDate.now();

        TimeRecord timeRecord1 = new TimeRecord();
        timeRecord1.setDate(now);
        TimeRecord timeRecord2 = new TimeRecord();
        timeRecord2.setDate(now.minusDays(2));
        Task task = new Task();
        task.setTimeRecords(List.of(timeRecord1, timeRecord2));
        IntellectualProperty ip = new IntellectualProperty();
        ip.setTasks(List.of(task));
        IPValidator validator = new IPValidator(ip);
        //when, then
        assertFalse(validator.validateEndDate(now.minusDays(3)));
        assertFalse(validator.validateEndDate(now.minusDays(2)));
        assertFalse(validator.validateEndDate(now.minusDays(1)));
        assertTrue(validator.validateEndDate(now));
        assertTrue(validator.validateEndDate(now.plusDays(1)));
    }

    @Test
    void validateEndDateWhenNoTimeRecords() {
        //given
        LocalDate now = LocalDate.now();

        IntellectualProperty ip = new IntellectualProperty();
        ip.setTasks(List.of(new Task()));
        IPValidator validator = new IPValidator(ip);
        //when, then
        assertTrue(validator.validateEndDate(now.minusDays(3)));
        assertTrue(validator.validateEndDate(now.minusDays(2)));
        assertTrue(validator.validateEndDate(now.minusDays(1)));
        assertTrue(validator.validateEndDate(now));
        assertTrue(validator.validateEndDate(now.plusDays(1)));
    }

    @Test
    void validateEndDateWhenNoTask() {
        //given
        LocalDate now = LocalDate.now();


        IPValidator validator = new IPValidator(new IntellectualProperty());
        //when, then
        assertTrue(validator.validateEndDate(now.minusDays(3)));
        assertTrue(validator.validateEndDate(now.minusDays(2)));
        assertTrue(validator.validateEndDate(now.minusDays(1)));
        assertTrue(validator.validateEndDate(now));
        assertTrue(validator.validateEndDate(now.plusDays(1)));
    }
}