package pl.sg.ip.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeRecordData {
    private LocalDate date;
    private int numberOfHours;
    private String description;
    private AssignmentAction assignmentAction;
    private Integer taskId;

    enum AssignmentAction {
        NOP, ASSIGN, UNASSING
    }
}
