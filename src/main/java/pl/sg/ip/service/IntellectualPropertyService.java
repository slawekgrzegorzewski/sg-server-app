package pl.sg.ip.service;

import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;

import java.util.Collection;

public interface IntellectualPropertyService {
    Collection<IntellectualProperty> getAll(int domainId);

    IntellectualProperty create(int domainId, IntellectualPropertyData createData);

    void update(int domainId, int intellectualPropertyId, IntellectualPropertyData createData);

    void delete(int domainId, int intellectualPropertyId);

    void createTask(int domainId, int intellectualPropertyId, TaskData taskData);

    Collection<Task> getTasksOfIntellectualProperty(int domainId, int intellectualPropertyId);
}
