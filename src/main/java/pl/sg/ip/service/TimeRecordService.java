package pl.sg.ip.service;

import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.TimeRecord;

public interface TimeRecordService {
    TimeRecord create(int domainId, TimeRecordData createData);

    void update(int domainId, int timeRecordId, TimeRecordData taskData);

    void delete(int domainId, int timeRecordId);
}
