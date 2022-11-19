package pl.sg.ip.controller;

import pl.sg.ip.api.TaskData;


public interface TaskController {

    void update(int domainId, int taskId, TaskData taskData);
}
