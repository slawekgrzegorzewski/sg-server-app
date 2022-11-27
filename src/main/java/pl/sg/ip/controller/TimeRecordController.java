package pl.sg.ip.controller;

import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;


public interface TimeRecordController {

    TimeRecord create(int domainId, TimeRecordData createData);

    void update(int domainId, int timeRecordId, TimeRecordData updateData);

    void delete(int domainId, int timeRecordId);
}
