package pl.sg.ip.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeRecordData {
    private LocalDate date;
    private int numberOfHours;
    private String description;
    private AssignmentAction assignmentAction;
    private Integer taskId;

    public enum AssignmentAction {
        NOP, ASSIGN, UNASSIGN;
        public static final Collection<AssignmentAction> ACTIONS_ALLOWED_DURING_CREATION = List.of(NOP, ASSIGN);
    }

    public TimeRecordData(LocalDate date, int numberOfHours, String description) {
        this(date, numberOfHours, description, AssignmentAction.NOP);
    }

    public TimeRecordData(LocalDate date, int numberOfHours, String description, AssignmentAction action) {
        this(date, numberOfHours, description, action, null);
    }
}