package pl.sg.ip.controller;

import pl.sg.ip.api.IntellectualProperty;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.api.TaskData;

import java.util.List;


public interface IntellectualPropertyController {

    List<IntellectualProperty> getAll(Integer domainId);

    IntellectualProperty create(Integer domainId, IntellectualPropertyData createData);

    void update(int domainId, int intellectualPropertyId, IntellectualPropertyData createData);

    void delete(int domainId, int intellectualPropertyId);

    void createTask(int domainId, int intellectualPropertyId, TaskData taskData);
}
