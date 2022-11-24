package pl.sg.ip.service;

import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.TimeRecord;

public interface TimeRecordService {
    TimeRecord createWithoutTask(int domainId, TimeRecordData createData);

    TimeRecord createInTask(int domainId, int taskId, TimeRecordData createData);
}
