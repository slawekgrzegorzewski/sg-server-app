package pl.sg.ip.service;

import pl.sg.ip.api.TaskData;

public interface TaskService {

    void update(int domainId, int intellectualPropertyId, TaskData updateData);
}
