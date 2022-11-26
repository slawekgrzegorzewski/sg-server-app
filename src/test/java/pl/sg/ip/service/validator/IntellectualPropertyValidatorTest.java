package pl.sg.ip.service.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.sg.application.model.Domain;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IntellectualPropertyValidatorTest {

    @ParameterizedTest
    @MethodSource("domainValidators")
    void validateDomain(Validator validator) {
        //when, then
        assertTrue(validator.validateDomain(1));
        assertFalse(validator.validateDomain(2));
        assertFalse(validator.validateDomain(-1));
        assertFalse(validator.validateDomain(0));
    }

    public static Stream<Arguments> domainValidators() {
        return Stream.of(
                Arguments.of(new IntellectualPropertyValidator(mockIntellectualProperty())),
                Arguments.of(new TaskValidator(mockTask())),
                Arguments.of(new TimeRecordValidator(mockTimeRecord())));
    }

    @ParameterizedTest
    @MethodSource("deleteValidatorsAndResults")
    void validateDeletion(Validator validator, boolean expectedResult) {
        assertEquals(expectedResult, validator.validateDeletion());
    }

    public static Stream<Arguments> deleteValidatorsAndResults() {
        return Stream.of(
                Arguments.of(new IntellectualPropertyValidator(mockIntellectualProperty()), true),
                Arguments.of(new IntellectualPropertyValidator(mockIntellectualPropertyWithEmptyTaskList()), true),
                Arguments.of(new IntellectualPropertyValidator(mockTask().getIntellectualProperty()), false),
                Arguments.of(new TaskValidator(mockTask()), true),
                Arguments.of(new TaskValidator(mockTaskWithEmptyTimeRecordList()), true),
                Arguments.of(new TaskValidator(mockTimeRecord().getTask()), false),
                Arguments.of(new TimeRecordValidator(mockTimeRecord()), true)
        );
    }

    private static Domain mockDomain() {
        Domain d = mock(Domain.class);
        when(d.getId()).thenReturn(1);
        return d;
    }

    private static IntellectualProperty mockIntellectualProperty() {
        IntellectualProperty ip = mock(IntellectualProperty.class);
        Domain domain = mockDomain();
        when(ip.getDomain()).thenReturn(domain);
        return ip;
    }

    private static IntellectualProperty mockIntellectualPropertyWithEmptyTaskList() {
        IntellectualProperty ip = mockIntellectualProperty();
        when(ip.tasks()).thenReturn(List.of());
        return ip;
    }

    private static Task mockTask() {
        Task task = mock(Task.class);
        IntellectualProperty intellectualProperty = mockIntellectualProperty();
        when(intellectualProperty.tasks()).thenReturn(List.of(task));
        when(task.getIntellectualProperty()).thenReturn(intellectualProperty);
        return task;
    }

    private static Task mockTaskWithEmptyTimeRecordList() {
        Task task = mockTask();
        when(task.getTimeRecords()).thenReturn(List.of());
        return task;
    }

    private static TimeRecord mockTimeRecord() {
        Task task = mockTask();
        TimeRecord timeRecord = mock(TimeRecord.class);
        when(task.getTimeRecords()).thenReturn(List.of(timeRecord));
        when(timeRecord.getTask()).thenReturn(task);
        Domain domain = mockDomain();
        when(timeRecord.getDomain()).thenReturn(domain);
        return timeRecord;
    }
}