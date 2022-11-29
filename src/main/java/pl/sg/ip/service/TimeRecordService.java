package pl.sg.ip.service;

import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.TimeRecord;

import java.util.Collection;
import java.util.List;

public interface TimeRecordService {

    List<TimeRecord> getUnassociatedTimeRecords(int domainId);

    TimeRecord create(int domainId, TimeRecordData createData);

    void update(int domainId, int timeRecordId, TimeRecordData taskData);

    void delete(int domainId, int timeRecordId);
}
