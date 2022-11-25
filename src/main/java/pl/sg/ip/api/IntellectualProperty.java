package pl.sg.ip.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sg.application.api.DomainSimple;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IntellectualProperty {
    private int id;
    private String description;
    private List<Task> tasks;
    private DomainSimple domain;
}