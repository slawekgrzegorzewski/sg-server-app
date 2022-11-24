package pl.sg.ip.controller;

import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;


public interface TimeRecordController {

    TimeRecord create(int domainId, TimeRecordData taskData);

    TimeRecord create(int domainId, int taskId, TimeRecordData taskData);
}
